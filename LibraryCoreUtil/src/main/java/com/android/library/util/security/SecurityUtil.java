package com.android.library.util.security;

import android.text.TextUtils;

/**
 * 数据加密工具类
 *
 * @author WANGYA
 */
public final class SecurityUtil {
    public static String[] chars_array = {"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z",};

    /**
     * 获得由微秒组成的19位随机数
     *
     * @return
     */
    public static String getRandomData(int num) {
        long microsecond = System.currentTimeMillis();// 获得微秒
        StringBuilder charsbuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            charsbuilder
                    .append(chars_array[(int) (Math.random() * chars_array.length)]);
        }
        return microsecond + charsbuilder.toString();
    }

    /**
     * 进行数据加密
     *
     * @param data
     * @return
     */
    public static String encode(String data) {
        data = encodebybase64(data);

        StringBuilder newstr1 = new StringBuilder();
        StringBuilder newstr2 = new StringBuilder();
        for (int i = 0, j = data.length(); i < j; i++) {
            if (i % 2 == 1) {
                newstr1.append(data.charAt(i));
            } else {
                newstr2.append(data.charAt(i));
            }
        }
        data = newstr1.toString() + newstr2.toString();
        // LogUtil.d("奇偶数转换后>>>>>" + data);
        return data;
    }

    /**
     * 进行64位加密处理
     *
     * @param data
     * @return
     */
    private static String encodebybase64(String data) {
        // LogUtil.d("转换前>>>>>" + data);
        data = AliBase64.encode(data.getBytes());
        // LogUtil.d("Base64转换后>>>>>" + data);
        data = data.replace("+", "-").replace("/", "_");
        // LogUtil.d("转换后>>>>>" + data);
        return sideTrim(data, "=");
    }

    /**
     * 去掉指定字符串的开头和结尾的指定字符
     *
     * @param stream  要处理的字符串
     * @param trimstr 要去掉的字符串
     * @return 处理后的字符串
     */
    private static String sideTrim(String stream, String trimstr) {
        if (TextUtils.isEmpty(stream)) {
            return stream;
        }
        while (true) {
            if (stream.startsWith(trimstr)) {
                stream = stream.substring(1, stream.length() - 1);
            } else {
                break;
            }
        }
        while (true) {
            if (TextUtils.equals(trimstr,
                    String.valueOf(stream.charAt(stream.length() - 1)))) {
                stream = stream.substring(0, stream.length() - 2);
            } else {
                break;
            }
        }
        // LogUtil.d("替换后=>>>>>" + stream);
        return stream;
    }
}
