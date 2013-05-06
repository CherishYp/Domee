package com.domee.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	public static String longToDateString(long longTime){
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		return sdf.format(longTime);
	}
	
	/**
	 * 将日期转换为字符串
	 * @param date		要转换为String的日期
	 * @param format	日期格式
	 * @return
	 */
	public static String Date2String(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		String time = df.format(date);
		return time;
	}
	public static String dateToWeek(Date date){
		final String[] weekArra = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		int iw = date.getDay();
		return weekArra[iw];
	}
	/**
	 * String 类型转化 Date类型   格式 yyyy-MM-dd
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date String2Date(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * String 类型转化 Datetime类型   格式 yyyy-MM-dd HH:mm:ss
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date String2DateTime(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * String 类型转化 time类型   格式 HH:mm
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date String2Time(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String string2Week(String dt){
		Date date = String2Date(dt);
		return dateToWeek(date);
	}
	
	public static String Timestamp2Week(Timestamp ts){
		final String[] weekArra = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		int iw = ts.getDay();
		return weekArra[iw];
	}
	
	
	/*
	 * 几分钟前。。。
	 */
	public static String timeAgo(String strDate) {
		
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = String2DateTime(strDate);
        Date time = String2Time(strDate);
        long creTime = date.getTime();
        
        long curTime = System.currentTimeMillis();
        
        long days = 1000* 60 * 60 * 24;
        long oneDays = 1000* 60 * 60 * 24 * 2;
        long hours = 1000 * 60 * 60;
        long minutes = 1000*60;
        
        if ((curTime - creTime) < hours && (curTime - creTime) > minutes) {
			
        	return (curTime - creTime) / (1000*60) + " 分钟前";
		} else if ((curTime - creTime) > days && (curTime - creTime) < oneDays) {
			
			return "昨天 " + time;
		} else if ((curTime - creTime) > oneDays) {
			
			return  date + "";
		}
        return "刚刚";
        
	}

	
	public static void main(String[] args) {
		String str = "Fri Apr 26 09:37:49 +0800 2013";
		
		Date date = String2DateTime(str);
		System.out.println(date);
	}
}
