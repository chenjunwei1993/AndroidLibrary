package com.android.library.util;

import android.content.Context;
import android.text.TextUtils;

import com.android.library.coreutil.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DataConvertUtil {


    /**
     * 时间相关字符串格式化处理
     */

    public static final String FORMAT_DATA = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATA_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATA_TIME_1 = "MM-dd HH:mm";
    public static final String FORMAT_DATA_TIME_2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATA_TIME_3 = "yyyyMMdd";
    public static final String FORMAT_DATA_TIME_4 = "yyyy-MM";
    public static final String FORMAT_DATA_TIME_5 = "yyyy年MM月";
    public static final String FORMAT_DATA_TIME_6 = "HH:mm";
    public static final String FORMAT_DATA_TIME_7 = "MM/dd";
    public static final String FORMAT_DATA_TIME_8 = "yyyy年MM月dd日";
    public static final String FORMAT_DATA_TIME_9 = "MM月dd日";
    public static final String FORMAT_DATA_TIME_10 = "yyyy/MM/dd";
    public static final String FORMAT_DATA_TIME_11 = "MM-dd";
    public static final String FORMAT_DATA_TIME_12 = "MM/dd HH:mm";
    public static final String FORMAT_DATA_TIME_13 = "MM-dd HH:mm";
    public static final String FORMAT_DATA_TIME_14 = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_TIME_TIME_15 = "mm:ss";
    public static final String FORMAT_DATA_TIME_15 = "yyyy.MM.dd";
    public static final String FORMAT_DATA_TIME_17 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_DATA_TIME_16 = "MM月dd日 HH:mm";
    public static final String FORMAT_DATA_TIME_18 = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_DATA_TIME_19 = "MM月dd日 a HH:mm"; // a 对应上下午

    /**
     * 注意 ：如果 Variable.APP_LANGUAGE = "English";
     * 那么 replace 则不能使用 ResourceUtils.getString(R.string.year)类似的，因为这样取到的是对应的英文单词 并不能替换掉 年 月日
     *
     * @param time
     * @return
     */
    private static String changeLanguage(String time) {
        if (TextUtils.equals("English", AppUtils.getAppLanguage())) {
            if (time.endsWith("日")) {
                time = time.replace("日", "");
            }
            if (time.endsWith("月")) {
                time = time.replace("月", "");
            }
            time = time.replace("年", "-");
            time = time.replace("月", "-");
            time = time.replace("日", "-");
        }
        return time;
    }

    // =======================================时间转化==============================================
    // 2016年12月2日10:44:30 扩展方法 目的为产品化制定统一标准：

    /**
     * @param timeStr    需要转化的时间 eg:2015-05-27 14:22:22
     * @param oldPattern 转化前的时间格式 eg:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String standard_MixList(String timeStr, String oldPattern) {
        if (TextUtils.isEmpty(timeStr)) {
            return ResourceUtils.getString(R.string.time_naver);
        }
        long time = 0;
        try {
            time = new SimpleDateFormat(oldPattern).parse(timeStr).getTime();
        } catch (ParseException e) {
            try {
                time = Long.parseLong(timeStr) * 1000;
            } catch (NumberFormatException e1) {
            }
        }
        return standard_MixList(time);
    }

    /**
     * 资讯列表页时间显示：若为当天发布，则显示多场时间之前，如“32分钟前”、“3小时前”；
     * 非当天发布的则直接显示日期，如“07-28”、“11-15”；非当年发布的要加上年份，如“2015-10-25”。
     *
     * @param time
     * @return
     */
    public static String standard_MixList(long time) {//资讯列表时间的展示
        if (time == 0) {
            return ResourceUtils.getString(R.string.time_justnow);
        }
        long cur_time = System.currentTimeMillis();
        long del_time = cur_time - time;
        long sec = del_time / 1000;
        long min = sec / 60;
        if (min < 60) {
            if (min < 1) {
                return ResourceUtils.getString(R.string.time_justnow);
            }
            return min + ResourceUtils.getString(R.string.time_mins_ago);
        } else {
            long h = min / 60;
            if (h < 24) {
                return h + ResourceUtils.getString(R.string.time_hour_ago);
            }
            int nowYear = getNowYear();
            int time_year = ConvertUtils.toInt(timestampToString(time, "yyyy"));
            if (nowYear == time_year) {
                return changeLanguage(new SimpleDateFormat(FORMAT_DATA_TIME_11,
                        Locale.CHINA).format(new Date(time)));
            } else {
                return changeLanguage(new SimpleDateFormat(FORMAT_DATA,
                        Locale.CHINA).format(new Date(time)));
            }
        }
    }

    /**
     * 资讯详情页时间显示：全部显示日期，如“12-02"，文章来源放在时间前面。非当年发布的增加年份显示，如“2015-07-01”。
     *
     * @return
     */
    public static String standard_MixListDetail(String timeStr, String oldPattern) {
        if (TextUtils.isEmpty(timeStr)) {
            return ResourceUtils.getString(R.string.time_naver);
        }
        long time = 0;
        try {
            time = new SimpleDateFormat(oldPattern).parse(timeStr).getTime();
        } catch (ParseException e) {
            try {
                time = Long.parseLong(timeStr) * 1000;
            } catch (NumberFormatException e1) {
            }
        }
        return changeLanguage(new SimpleDateFormat(FORMAT_DATA_TIME_13,
                Locale.CHINA).format(new Date(time)));
    }

    /**
     * 资讯详情页时间显示：全部显示日期与时分，如“12-02 10:06”，文章来源放在时间前面。非当年发布的增加年份显示，如“2015-07-01 08:45”。
     *
     * @param time
     * @return
     */
    public static String standard_MixDetail(long time) {//资讯列表时间的展示
        int nowYear = getNowYear();
        int time_year = ConvertUtils.toInt(timestampToString(time, "yyyy"));
        if (nowYear == time_year) {//当年发布的
            return changeLanguage(new SimpleDateFormat(FORMAT_DATA_TIME_1,
                    Locale.CHINA).format(new Date(time)));
        } else {
            return changeLanguage(new SimpleDateFormat(FORMAT_DATA_TIME_2,
                    Locale.CHINA).format(new Date(time)));
        }
    }


    /**
     * @param time    需要转化的时间 eg:2015-05-27 14:22:22
     * @param pattern 需要转化的时间格式 pattern的元素需要大于等于 time的，eg:time=2015-05-27 14:22:22
     *                pattern=yyyyMMdd 得到=20150527， 反过来
     *                time=20150527,pattern=yyyy-MM-dd HH:mm:ss,就会报异常
     * @return Date
     * @Description: 格式化的string时间 转化成Date
     */
    public static Date formatTimeToDate(String time, String pattern) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param pattern {@link #FORMAT_DATA} {@link #FORMAT_TIME}
     *                {@link #FORMAT_DATA_TIME}
     * @return String 格式化时间String
     * @Description: 获取当前时间
     */
    public static String getNowTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return changeLanguage(format.format(new Date()));
    }

    /**
     * 格式化的string时间 转化成指定需要的格式
     *
     * @param time
     * @param oldPattern time的格式
     * @param pattern    新样式
     * @return
     */
    public static String formatTime(String time, String oldPattern,
                                    String pattern) {
        if (TextUtils.isEmpty(time)) {
            return time;
        }
        SimpleDateFormat format = new SimpleDateFormat(oldPattern);
        try {
            Date date = format.parse(time);
            return changeLanguage(new SimpleDateFormat(pattern).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return changeLanguage(time);
        }
    }

    public static String formatTime(String time, String pattern) {
        return formatTime(time, FORMAT_DATA_TIME, pattern);
    }

    public static String setFormatData(String time, String format) {

        try {
            long t = Long.parseLong(time) * 1000;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = new Date(t);
            return simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            return "";
        }
    }


    /**
     * 时间戳 转化为 指定格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String timestampToString(long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return changeLanguage(format.format(new Date(time)));
    }

    /**
     * Date转化为指定格式
     * @param date
     * @param pattern
     * @return
     */
    public static String date2String(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return changeLanguage(format.format(date));
    }

    /**
     * 指定格式 转化为 时间戳
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long stringToTimestamp(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将年月日转化为指定时间格式
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @param pattern 需要的时间格式
     * @return
     */
    public static String formatTime(int year, int month, int day, int hour,
                                    int min, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min, 0);
        Date date = calendar.getTime();
        return changeLanguage(dateFormat.format(date));
    }

    /**
     * 默认 FORMAT_DATA_TIME
     */
    public static String formatTime(int year, int month, int day, int hour,
                                    int min) {
        return formatTime(year, month, day, hour, min, FORMAT_DATA_TIME);
    }

    // TODO
    // ==========================================获得相隔时间============================================

    /**
     * 计算两个日期之间相差的天数
     * @throws ParseException
     */
    public static String getDaysBetween(Date date) {
        int days = 0;
        try {
            days = daysBetween(new Date(), date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        if (days == 0) {
            return ResourceUtils.getString(R.string.today_str);
        } else if (days == 1) {
            return ResourceUtils.getString(R.string.tomorrow_str);
        } else if (days == 2) {
            return ResourceUtils.getString(R.string.the_day_after_tomorrow_str);
        } else {
            return days + ResourceUtils.getString(R.string.days_later_str);
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getRefrshTime(long time) {
        return getRefrshTime(time, FORMAT_DATA_TIME);
    }

    public static String getRefrshTime(String timeStr, String pattern) {
        return getRefrshTime(timeStr, FORMAT_DATA_TIME, pattern);
    }

    /**
     * 时间格式转换
     *
     * @param timeStr          需要转化的时间 eg:2015-05-27 14:22:22
     * @param oldPattern       转化前的时间格式 eg:yyyy-MM-dd HH:mm:ss
     * @param newPatternString 转化后的时间格式 eg:M月d日
     *                         1：3天前显示XX之前，3天后显示成指定格式
     * @return 返回新的时间格式 eg:5月27日
     */
    public static String getRefrshTime(String timeStr, String oldPattern,
                                       String newPatternString) {
        if (TextUtils.isEmpty(timeStr)) {
            return ResourceUtils.getString(R.string.time_naver);
        }
        long time = 0;
        try {
            time = new SimpleDateFormat(oldPattern).parse(timeStr).getTime();
        } catch (ParseException e) {
            try {
                time = Long.parseLong(timeStr) * 1000;
            } catch (NumberFormatException e1) {
            }
        }
        return getRefrshTime(time, newPatternString);
    }

    /**
     * 刚刚，几分钟前，几小时前，几天前
     *
     * @param time 时间戳
     * @return
     */
    public static String getRefrshTime(long time, String pattern) {
        if (time == 0) {
            return ResourceUtils.getString(R.string.time_justnow);
        }
        long cur_time = System.currentTimeMillis();
        long del_time = cur_time - time;
        long sec = del_time / 1000;
        long min = sec / 60;
        if (min < 60) {
            if (min < 1) {
                return ResourceUtils.getString(R.string.time_justnow);
            }
            return min
                    + ResourceUtils.getString(R.string.time_mins_ago);
        } else {
            long h = min / 60;
            if (h < 24) {
                return h + ResourceUtils.getString(R.string.time_hour_ago);
            }
            long day = h / 24;
            if (day <= 3) {
                return day + ResourceUtils.getString(R.string.time_day_ago);
            } else {
                return changeLanguage(new SimpleDateFormat(pattern,
                        Locale.CHINA).format(new Date(time)));
            }
        }
    }

    /**
     *
     * @param time
     * @param pattern
     * @param days 匹配数组,可匹配今天或者明天多级匹配
     * @return
     */
    public static String getRefrshRoadTime(long time, String pattern, String[] days) {
        if (time == 0 || days==null) {
            return CoreUtils.getContext().getString(R.string.time_naver);
        }
        for(int i=0;i<days.length;i++){
            if(TextUtils.equals(days[i],getDaysBetween(new Date(time)))){
                return changeLanguage(new SimpleDateFormat("HH:mm", Locale.CHINA)
                        .format(new Date(time)));
            }
        }
        return changeLanguage(new SimpleDateFormat(pattern, Locale.CHINA)
                    .format(new Date(time)));
    }

    /**
     * 接口返回的时间戳 转化成long类型*1000 可用时间戳
     *
     * @param timestamp
     * @return
     */
    public static long timestampToLong(String timestamp) {
        return ConvertUtils.toLong(timestamp) * 1000;
    }

    // TODO
    // ==========================================比较时间先后============================================

    /**
     * @param time1
     * @param time2
     * @param pattern 格式按元素少的来
     * @return
     * @Description: 比较时间前后
     */
    public static boolean compareTime(String time1, String time2, String pattern) {
        int compare = compare2Time(time1, time2, pattern);
        switch (compare) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    /**
     * 比较时间前后
     *
     * @param time1
     * @param time2
     * @param pattern
     * @return 1:"dt1 在dt2前";-1:"dt1在dt2后";0相等
     */
    public static int compare2Time(String time1, String time2, String pattern) {
        if (time1 == null || time2 == null) {
            return 1;
        }
        time1 = time1.trim();
        time2 = time2.trim();
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            Date dt1 = df.parse(time1);
            Date dt2 = df.parse(time2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;

    }

    /**
     * 比较时间戳
     *
     * @param time1
     * @param time2 计算两小时之后时间戳:System.currentTimeMillis()+2*60*60*1000
     * @return
     */
    public static boolean compareTimestamp(long time1, long time2) {
        long deltaTime = time1 - time2;
        if (deltaTime > 0) {
            return true;
        } else {
            return false;
        }
    }

    // TODO
    // =====================================================================================

    /**
     * 格式化月份
     *
     * @param year
     * @param month 月数-1，0月既是1月
     * @param day
     * @return
     */
    public static String formatMonth(int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return changeLanguage(dateFormat.format(date));
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getNowYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String formatDate(int year, int month, int day, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    // 获取几天后日期
    public static String afterFormatDate(int num) {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATA);
        return df.format(new Date(d.getTime() + num * 24 * 60 * 60 * 1000));
    }

    // 获取制定日期几天后的日期
    public static String afterFormatDate(Date date, int num) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATA);
        return df.format(new Date(date.getTime() + num * 24 * 60 * 60 * 1000));
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime, String parent)
            throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(parent);
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        String dayForWeek = "";
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[0];
        } else {
            switch (c.get(Calendar.DAY_OF_WEEK) - 1) {
                case 1:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[1];
                    break;
                case 2:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[2];
                    break;
                case 3:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[3];
                    break;
                case 4:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[4];
                    break;
                case 5:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[5];
                    break;
                case 6:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[6];
                    break;
            }
        }
        return dayForWeek;
    }

    /**
     * 获取当前星期几
     *
     * @return
     * @throws Exception
     */
    public static String getNowWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String dayForWeek = "";
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek =ResourceUtils.getStringArray(R.array.week_array2)[0];
        } else {
            switch (c.get(Calendar.DAY_OF_WEEK) - 1) {
                case 1:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[1];
                    break;
                case 2:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[2];
                    break;
                case 3:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[3];
                    break;
                case 4:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[4];
                    break;
                case 5:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[5];
                    break;
                case 6:
                    dayForWeek = ResourceUtils.getStringArray(R.array.week_array2)[6];
                    break;
            }
        }
        return dayForWeek;
    }

    /**
     * 录音格式化时间
     *
     * @param time 时间 秒数
     * @return
     */
    public static String getAudioTime(long time) {
        if (time == 0) {
            return "0\"";
        }
        String formatTime = "";
        if (time < 60) {
            return time + "\"";
        }
        long seconds = time % 60;
        long minutes = time / 60;
        if (minutes > 0) {
            formatTime = minutes + "\'";
        }
        if (seconds >= 0) {
            formatTime = formatTime + seconds + "\"";
        }
        return formatTime;
    }

    public static String getTimeByLong(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//秒
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }

    /**
     * 对今天特殊显示
     * @param time
     * @param format
     * @return
     */
    public static String getTimeSpecialToday(long time, String format) {
        StringBuilder timeStr = new StringBuilder();
        if (time-getTodayZeroTime() >=0 && time-getTodayZeroTime() < 24*60*60*1000) {
            timeStr.append(ResourceUtils.getString(R.string.today_str))
                    .append(timestampToString(time,FORMAT_DATA_TIME_6));
        }else{
            timeStr.append(timestampToString(time,format));
        }
        return timeStr.toString();
    }

    public static long getTodayZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        return calendar.getTimeInMillis();
    }

    /**==========================特殊用处===============================**/
    /**
     * NewRoadStyle1用到
     * @param time
     * @return
     */
    public static String getRefrshRoadTime(long time) {
        return getRefrshRoadTime(time, FORMAT_DATA_TIME_2);
    }
    /**
     * 今天 显示： hh:mm 超过今天显示 ：yyyy-MM-dd HH:mm
     *
     * @author mark
     */
    public static String getRefrshRoadTime(long time, String pattern) {
        Context mContext = CoreUtils.getContext();
        if (time == 0) {
            return ResourceUtils.getString(R.string.time_naver);
        }
        if (ResourceUtils.getString(R.string.today_str).equals(getDaysBetween(new Date(time)))) {
            return changeLanguage(new SimpleDateFormat("HH:mm", Locale.CHINA)
                    .format(new Date(time)));
        } else {
            return changeLanguage(new SimpleDateFormat(pattern, Locale.CHINA)
                    .format(new Date(time)));
        }
    }
    /**
     * 判断当前日期是星期几
     *
     * 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static List<String> getNowWeekList() {
        List<String> list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String dayForWeek = "";
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        for (int i = dayOfWeek - 6 ; i < dayOfWeek + 1; i++ ){
            int day = i;
            if(day < 0){
                day = day + 7;
            }
            switch (day) {
                case 0:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[0];
                    break;
                case 1:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[1];
                    break;
                case 2:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[2];
                    break;
                case 3:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[3];
                    break;
                case 4:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[4];
                    break;
                case 5:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[5];
                    break;
                case 6:
                    dayForWeek = CoreUtils.getContext().getResources()
                            .getStringArray(R.array.week_array)[6];
                    break;
            }
            list.add(dayForWeek);
        }

        return list;
    }
    public static int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String dayForWeek = "";
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 将ISO 8601转普通时间
     *
     * @param date
     * @return
     */
    public static String fromISOTime(String date) {
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return sdf1.format(sdf2.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将ISO 8601转普通时间
     * @param date
     * @return
     */
    public static String fromISOTimeDay(String date) {
        DateFormat sdf1 = new SimpleDateFormat("MM.dd");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return sdf1.format(sdf2.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将ISO 8601转普通时间
     * @param date
     * @return
     */
    public static String fromISOTimeTimes(String date) {
        DateFormat sdf1 = new SimpleDateFormat("HH:mm");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return sdf1.format(sdf2.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将ISO 8601转普通时间
     * @param date
     * @return
     */
    public static String fromISOTimeOne(String date) {
        DateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return sdf1.format(sdf2.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将普通时间转ISO 8601
     *
     * @param date
     * @return
     */
    public static String forISOTime(String date) {
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return sdf2.format(sdf1.parse(date)) + "Z";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  判断是否是今天
     */
    public static boolean isRefrshToday(long time){
        if (ResourceUtils.getString(R.string.today_str)
                .equals(getDaysBetween(new Date(time)))){
            return true;
        }else {
            return false;
        }
    }

    
    //////////////// 协同 新增/////////////////
    /**
     * 打回理由时间的展示：若为当天发布，则显示多场时间之前，如“32分钟前”、“3小时前”；
     * 非当天发布的则直接显示，如“2015-10-25”。
     *
     * @param time
     * @return
     */
    public static String repulseReasonTime(long time) {//打回理由时间的展示
        time = time * 1000;
        if (time == 0) {
            return CoreUtils.getContext().getResources().getString(R.string.time_justnow);
        }
        long cur_time = System.currentTimeMillis();
        long del_time = cur_time - time;
        long sec = del_time / 1000;
        long min = sec / 60;
        if (min < 60) {
            if (min < 1) {
                return CoreUtils.getContext().getResources().getString(R.string.time_justnow);
            }
            return min + CoreUtils.getContext().getResources().getString(R.string.time_mins_ago);
        } else {
            long h = min / 60;
            if (h < 24) {
                return h + CoreUtils.getContext().getResources().getString(R.string.time_hour_ago);
            }
            return changeLanguage(new SimpleDateFormat(FORMAT_DATA_TIME_15,
                    Locale.CHINA).format(new Date(time)));
        }
    }

    /**
     * 客户端日期时间显示规范
     * 1、今天的时间以“xxx前”格式显示，如“15秒钟前”、“23分钟前”、“5小时前”；对于刚进行的操作，显示时间为“刚刚”(5秒内)；
     * 2、昨天的时间以“昨天 xx:xx”格式显示；
     * 3、昨天以前的日期显示具体的日期时间“x月x日 xx:xx”；
     * 4、根据情况可以只有日期，没有时间（具体看设计稿），今天的还是按照第1条说明来显示。
     *
     * @param time 秒，非毫秒
     * @return
     */
    public static String getStandardTime(long time) {
        time = time * 1000;
        if (time == 0) {
            return CoreUtils.getContext().getResources().getString(R.string.time_justnow);
        }
        long cur_time = System.currentTimeMillis();
        long del_time = cur_time - time;
        long sec = del_time / 1000;
        long min = sec / 60;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
        String yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(cal.getTime());
        String day = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(time));

        if (TextUtils.equals(day, today)) {
            if (min < 60) {
                if (min < 1) {
                    if (sec < 5) {
                        return CoreUtils.getContext().getResources().getString(R.string.time_justnow);
                    } else {
                        return sec + CoreUtils.getContext().getResources().getString(R.string.time_seconds_ago);
                    }
                }
                return min + CoreUtils.getContext().getResources().getString(R.string.time_mins_ago);
            } else {
                long h = min / 60;
                return h + CoreUtils.getContext().getResources().getString(R.string.time_hour_ago);
            }
        } else if (TextUtils.equals(day, yesterday)) {
            return CoreUtils.getContext().getResources().getString(R.string.yesterday_str) + " " + new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(time));
        } else if (TextUtils.equals(today.substring(0, 4), day.substring(0, 4))) {
            return new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA).format(new Date(time));
        } else {
            return new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(new Date(time));
        }
    }

}
