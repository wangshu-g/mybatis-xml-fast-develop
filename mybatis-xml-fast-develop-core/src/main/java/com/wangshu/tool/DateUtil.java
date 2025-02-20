package com.wangshu.tool;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    @NotNull
    public static String getNowYMD() {
        return formatDate(new Date(), YMD);
    }

    @NotNull
    public static String getNowYMDHMS() {
        return formatDate(new Date(), YMDHMS);
    }

    @NotNull
    public static String getYMD(Date date) {
        return formatDate(date, YMD);
    }

    @NotNull
    public static String getYMDHMS(Date date) {
        return formatDate(date, YMDHMS);
    }

    @NotNull
    public static String formatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

}
