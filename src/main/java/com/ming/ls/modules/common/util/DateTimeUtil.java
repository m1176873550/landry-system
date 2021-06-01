package com.ming.ls.modules.common.util;

import cn.hutool.core.date.DateTime;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd";

    /**
     * 字符串转 Date
     *
     * @param dateTimeStr 时间字符串
     * @param formatStr   时间格式
     * @return Date
     */
    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        org.joda.time.DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * Date 转字符串
     *
     * @param date      时间
     * @param formatStr 转换格式
     * @return String
     */
    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);

        return dateTime.toString(formatStr);
    }

    /**
     * 字符串转 Date
     *
     * @param dateTimeStr 时间字符串
     * @return Date
     */
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        org.joda.time.DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    /**
     * Date 转字符串
     *
     * @param date 时间
     * @return String
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

//    /**
//     * Date -> LocalDateTime
//     *
//     * @param date 日期
//     * @return LocalDate
//     */
//    public static LocalDate date2LocalDate(Date date) {
//        if (date != null)
//            return LocalDateTime.ofInstant(date.toInstant(), Zonid.systemDefault()).toLocalDate();
//        return null;
//    }
//
//    /**
//     * LocalDateTime -> Date
//     *
//     * @param localDate 日期
//     * @return Date
//     */
//    public static Date localDate2Date(LocalDate localDate) {
//        if (localDate != null)
//            return Date.from(localDate.atStartOfDay().atZone(Zonid.systemDefault()).toInstant());
//        return null;
//    }

    /**
     * timeStamp(毫秒) -> Date
     *
     * @param timeStamp 时间戳
     * @return Date
     */
    public static String timeStamp2Date(String timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long longTimeStamp = Long.parseLong(timeStamp);
        Date date = new Date(longTimeStamp);
        return simpleDateFormat.format(date);
    }
}
