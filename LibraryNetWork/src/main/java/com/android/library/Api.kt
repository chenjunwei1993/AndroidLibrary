package com.android.library

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class Api {
    /**
     * 请求头
     */
    var headerMap: HashMap<String, String>? = null

    var sGsonConverterFactory = GsonConverterFactory.create()
    var sRxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create()

    var connectTimeOut = DEFAULT_TIME_OUT

    var readTimeOut = DEFAULT_TIME_OUT

    var writeTimeOut = DEFAULT_TIME_OUT

    var isDebug = false

    var baseUrl = ""

    private var retrofit: Retrofit? = null


    companion object {
        /**
         * https://blog.csdn.net/calm1516/article/details/83025640
         */
        val api: Api by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Api() }

        private const val DEFAULT_TIME_OUT: Long = 12L
    }

    @Synchronized
    fun <T> getApi(tClass: Class<T>): T {
        if(null == retrofit){
            retrofit = Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(sGsonConverterFactory)
                .addCallAdapterFactory(sRxJavaCallAdapterFactory)
                .build()
        }
        return retrofit!!.create(tClass)
    }

    private fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(connectTimeOut, TimeUnit.SECONDS)
        builder.readTimeout(readTimeOut, TimeUnit.SECONDS)
        builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS)
        try {
            val sslSocketFactory = trustAllHosts()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier(hostnameVerifier)

            //请求头
            val requestInterceptor = RequestInterceptor()
            requestInterceptor.setHeaderMap(headerMap)
            builder.addInterceptor(requestInterceptor)

            if (isDebug) {
                val logging = HttpLogInterceptor()
                builder.interceptors().add(logging)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.build()
    }


    /**
     * 允许所有的SSL
     */
    private fun allowAllSSL(client: OkHttpClient.Builder) {
        val trustManager = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManager, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            client.sslSocketFactory(sslSocketFactory)
            client.hostnameVerifier { _, _ -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }

    private fun trustAllHosts(): SSLSocketFactory? {
        var sslSocketFactory: SSLSocketFactory? = null
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>, uthType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>, authType: String
            ) {
            }
        })

        // Install the all-trusting trust manager
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            sslSocketFactory = sc.socketFactory
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return sslSocketFactory
    }

    // always verify the host - dont check for certificate
    private val hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ -> true }

    /**
     * This interceptor compresses the HTTP request body. Many webservers can't handle this!
     */
    class RequestInterceptor : Interceptor {

        private var headerMap: HashMap<String, String>? = null

        fun setHeaderMap(headerMap: HashMap<String, String>?) {
            this.headerMap = headerMap
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val build = originalRequest.newBuilder()
            headerMap?.let {
                for (s in it.keys) {
                    build.header(s, it[s])
                }
            }

            val compressedRequest = build.build()
            return chain.proceed(compressedRequest)
        }
    }

}