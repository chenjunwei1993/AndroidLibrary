package com.android.library.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验格式
 */
public class CheckUtil {

    public static boolean checkID(String str) {
        String regex = "\\d{15}$|^\\d{17}([0-9]|X|x)";
        return str.matches(regex);
    }

    public static boolean checkPHONE(String str) {
        // String regex = "[1][3|4|5|7|8][\\d]{9}";
        String regex = "\\d{11}";// 11位数字
        return str.matches(regex);
    }

    public static boolean checkEMAIL(String str) {
        // String regex =
        // "([0-9]*\\w*[.-]*){3,18}@([0-9]{3}|[a-z]{2,8})(\\.[\\w]{2,3})+";
        String regex = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return str.matches(regex);
    }

    public static boolean checkPOSTCODE(String str) {
        String regex = "\\d{6}";
        return str.matches(regex);
    }

    public static boolean checkDate(String str) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return str.matches(regex);
    }

    public static boolean checkTEL(String str) {
        String regex = "\\d{0,4}-{0,1}\\d{7,8}";
        return str.matches(regex);
    }

    public static boolean isEditNull(String str) {
        if (null == str || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (TextUtils.equals("English", AppUtils.getAppLanguage())) {//英文app 忽略校验
            return true;
        }
        Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(str);
        return matcher.find();
    }

    /**
     * 校验Tag Alias 只能是数字和英文字母
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }


    /**
     * 判断是不是电信号码
     */
    public static boolean isChinaTelecom(String str) {
        String regex = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
        return str.matches(regex);
    }

    /**
     * 判断是不是联通号码
     */
    public static boolean isChinaUnico(String str) {
        String regex = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
        return str.matches(regex);
    }

    /**
     * 判断是不是移动号码
     */
    public static boolean isChinaMobile(String str) {
        String regex = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
        return str.matches(regex);
    }

    public static boolean checkIDCard(String input) {
        boolean isValid = false;
        String expression = "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)";
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(input);
        if (m.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
