package com.etar.purifier.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 日期转换工具类
 *
 * @author hzh
 * @date 2018/8/9
 */
public class DateUtil {

    public static String DEFAULT_SIMPLE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_DAY_FORMAT = "yyyy-MM-dd";

    public static Date DEFAULT_TIME = Objects.requireNonNull(DateUtil.parse("2019-01-01 00:00:00"));

    /**
     * 字符串转日期
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date strToDate(String dateStr) {
        DateFormat format = new SimpleDateFormat(DEFAULT_SIMPLE_FORMAT);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date strToDate(String dateStr, String formatStr) {
        DateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式化日期
     *
     * @param date         日期
     * @param formatString 格式化
     */
    public static String format(Date date, String formatString) {
        SimpleDateFormat df = new SimpleDateFormat(formatString);
        return df.format(date);
    }

    /**
     * 格式化日期(使用默认格式)
     *
     * @param date 日期
     */
    public static String format(Date date) {
        return format(date, DEFAULT_SIMPLE_FORMAT);
    }


    /**
     * 转换成日期指定格式
     *
     * @param dateString 要转换的时间
     * @return 使劲
     */
    public static Date parse(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_SIMPLE_FORMAT);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 与当前时间比较，是否超过5分钟
     *
     * @param dbDate  要比较的时间
     * @param nowDate 当前时间
     * @return 表结果 在5分钟内返回真
     */
    public static boolean isInFiveMin(Date dbDate, Date nowDate) {
        if (dbDate == null || nowDate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_SIMPLE_FORMAT);
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(format(dbDate));
            end = sdf.parse(format(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long cha = Objects.requireNonNull(end).getTime() - start.getTime();
        //说明小于5分钟
        return cha <= 300000 / 5;
    }


    /**
     * 获取某个日期的开始时间
     *
     * @param d 日期
     * @return 格式化的字符串
     */
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();

        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d 日期
     * @return 格式化的字符串
     */
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date dayStartTime = getDayEndTime(new Date());
        System.out.println(format(dayStartTime));
//        Date date = parse("2018-10-15 16:02:40");
//        System.out.println(date);
//        Date date1 = strToDate("2018-10-15 16:02:40");
//        System.out.println(date1);
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }


}
