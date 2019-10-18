package com.android.library.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.library.coreutil.R;
import com.android.library.util.security.EncodeUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/13
 *     desc  : 转换相关工具类
 * </pre>
 */
public final class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) {
            return null;
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <ul>
     *                  <li>precision = 0，返回null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * bytes转bits
     *
     * @param bytes 字节数组
     * @return bits
     */
    public static String bytes2Bits(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * bits转bytes
     *
     * @param bits 二进制
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap转drawable
     *
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(CoreUtils.getContext().getResources(), bitmap);
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr转drawable
     *
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) {
            return null;
        }
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static final float UNIT = 1000.0F;

    /**
     * 毫秒转秒
     *
     * @param time 毫秒
     * @return
     */
    public static float ms2s(long time) {
        return time / UNIT;
    }

    /**
     * 微秒转秒
     *
     * @param time 微秒
     * @return
     */
    public static float us2s(long time) {
        return time / UNIT / UNIT;
    }

    /**
     * 纳秒转秒
     *
     * @param time 纳秒
     * @return
     */
    public static float ns2s(long time) {
        return time / UNIT / UNIT / UNIT;
    }

    /**
     * @param mms 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(float mms) {
        int days = (int) (mms / (1000 * 60 * 60 * 24));
        int hours = (int) ((mms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        StringBuilder sb = new StringBuilder();
        sb.append(days > 0 ? days + ResourceUtils.getString(R.string.day) : "")
                .append(hours > 0 ? hours + ResourceUtils.getString(R.string.hour) : "")
                .append(minutes > 0 ? minutes + ResourceUtils.getString(R.string.minute) : "")
                .append(seconds > 0 ? seconds + ResourceUtils.getString(R.string.video_second) : "");
        return sb.toString();
    }

    /**
     * * 保留几位小数 四舍五入
     *
     * @param data     原始数据
     * @param saveByte 保留位数
     * @return
     */
    public static double round(float data, int saveByte) {
        BigDecimal b = new BigDecimal(data);
        double f1 = 0;
        try {
            f1 = b.setScale(saveByte, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
        }
        return f1;
    }

    /**
     * 转换字符串为boolean
     *
     * @param str
     * @return
     */
    public static boolean toBoolean(String str) {
        return toBoolean(str, false);
    }

    /**
     * 转换字符串为boolean
     *
     * @param str
     * @param def
     * @return
     */
    public static boolean toBoolean(String str, boolean def) {
        if (TextUtils.isEmpty(str)) {
            return def;
        }
        if ("false".equalsIgnoreCase(str) || "0".equals(str) || "DEL_ERR".equalsIgnoreCase(str)) {
            return false;
        } else if ("true".equalsIgnoreCase(str) || "1".equals(str) || "SUCCESS".equalsIgnoreCase(str)) {
            return true;
        } else {
            return def;
        }
    }

    /**
     * 转换字符串为float
     *
     * @param str
     * @return
     */
    public static float toFloat(String str) {
        return toFloat(str, 0F);
    }

    /**
     * 转换字符串为float
     *
     * @param str
     * @param def
     * @return
     */
    public static float toFloat(String str, float def) {
        if (TextUtils.isEmpty(str)) {
            return def;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            Log.d("app_factory", "e = " + e);
            return def;
        }
    }

    /**
     * 转换字符串为long
     *
     * @param str
     * @return
     */
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    /**
     * 转换字符串为long
     *
     * @param str
     * @param def
     * @return
     */
    public static long toLong(String str, long def) {
        if (TextUtils.isEmpty(str)) {
            return def;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 转换字符串为Double
     *
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        return toDouble(str, (short) 0);
    }

    /**
     * 转换字符串为Double
     *
     * @param str
     * @param def
     * @return
     */
    public static double toDouble(String str, double def) {
        if (TextUtils.isEmpty(str)) {
            return def;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 转换字符串为int
     *
     * @param str
     * @return
     */
    public static int toInt(String str) {
        return toInt(str, 0);
    }

    /**
     * 转换字符串为int
     *
     * @param str
     * @param def
     * @return
     */
    public static int toInt(String str, int def) {
        if (TextUtils.isEmpty(str)) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static String toString(Object o) {
        return toString(o, "");
    }

    public static String toString(Object o, String def) {
        if (o == null) {
            return def;
        }

        return o.toString();
    }

    /**
     * 参数依个判断是否为空,返回第一个不为空的 null也判断为空
     *
     * @param strings
     * @return first not empty or ""
     */
    public static String outFirstNotEmpty(String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        for (int i = 0; i < strings.length; i++) {
            String str_i = strings[i];
            if (!TextUtils.isEmpty(str_i)
                    && !"null".equals(str_i.toLowerCase())) {
                return str_i;
            }
        }
        return "";
    }

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 中文转码方法,并且去除空格
     *
     * @param strName
     * @return
     */
    public static String convertChinese(String strName) {
        if (TextUtils.isEmpty(strName)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String str = "";
        char[] ch = strName.replace(" ", "").toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            str = String.valueOf(c);
            if (isChinese(c)) {
                str = EncodeUtils.urlEncode(str);
            }
            sb.append(str);
        }
        return sb.toString();
    }

}
