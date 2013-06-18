package com.domee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by duyuan on 13-5-30.
 */
public class DMDateUtils {

    public static String global2Date(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println(formatStr);
        return formatStr;
    }

    public static long str2TimeMillis(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String str2Time(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatStr = new SimpleDateFormat("HH:mm").format(date);
        return formatStr;
    }

    public static String str2Date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatStr = new SimpleDateFormat("yy-MM-dd").format(date);
        return formatStr;
    }

    public static String changeToDate(String created_at) {
        String tempStr = "";
        long curTime = System.currentTimeMillis();
        long createTime = str2TimeMillis(created_at);
        long offset = curTime - createTime;
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数

        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date date = currentDate.getTime();
        long curDateStart = currentDate.getTime().getTime();

        if(offset < 3000) {
            tempStr = "刚刚";
        } else if (offset > 3000 && offset < ns) {
            tempStr = offset/ns + "秒前";
        } else if (offset >= ns && offset < nh) {
            tempStr = offset/nm + "分钟前";
        } else if (offset >= nh && offset < (curTime - curDateStart)) {
            tempStr = "今天" + str2Time(created_at);
        } else {
            tempStr = str2Date(created_at);
        }
        return tempStr;
    }

}
