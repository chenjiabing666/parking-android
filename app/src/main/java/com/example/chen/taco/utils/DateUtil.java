package com.example.chen.taco.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 时间工具
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

	private final static SimpleDateFormat dateFormatString = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormat24_2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private final static SimpleDateFormat dateFormat12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm:ss");
	private final static SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm:ss");

	/**
	 *  24小时制 日期转字符串 转换的格式yyyy-MM-dd HH:mm:ss
	 *
	 *  @param date 日期
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromDate24(Date date) {
		String strDate = dateFormat24.format(date);
		return strDate;
	}
	
	public static String stringFromDate(Date date) {
		String strDate = dateFormatString.format(date);
		return strDate;
	}
	public static String stringFromDate24_2(Date date) {
		String strDate = dateFormat24_2.format(date);
		return strDate;
	}

	/**
	 *  24小时制 字符串转日期 转换的格式yyyy-MM-dd HH:mm:ss
	 *
	 *  @param dateString 日期字符串
	 *
	 *  @return date
	 * @throws ParseException
	 */
	public static Date dateFromString24(String dateString) throws ParseException {
		Date date = dateFormat24.parse(dateString);
		return date;
	}
	/**
	 *  字符串转纯日期 转换的格式yyyy-MM-dd
	 *
	 *  @param dateString 日期字符串
	 *
	 *  @return date
	 * @throws ParseException
	 */
	public static Date dateFromString(String dateString) throws ParseException {
		Date date = dateFormatString.parse(dateString);
		return date;
	}
	
	/**
	 *  12小时制 日期转字符串 转换的格式yyyy-MM-dd hh:mm:ss
	 *
	 *  @param date 日期
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromDate12(Date date) {
		String strDate = dateFormat12.format(date);
		return strDate;
	}
	
	/**
	 *  12小时制 字符串转日期 转换的格式yyyy-MM-dd hh:mm:ss
	 *
	 *  @param dateString 日期字符串
	 *
	 *  @return date
	 */
	public static Date dateFromString12(String dateString) throws ParseException {
		Date date = dateFormat12.parse(dateString);
		return date;
	}

	/**
	 *  日期转字符串 转换的格式yyyy-MM-dd
	 *
	 *  @param date 日期
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromDay(Date date) {
		String strDate = dayFormat.format(date);
		return strDate;
	}

	/**
	 *  字符串转日期 转换的格式yyyy-MM-dd
	 *
	 *  @param dateString 日期字符串
	 *
	 *  @return date
	 * @throws ParseException
	 */
	public static Date dayFromString(String dateString) throws ParseException {
		Date date = dayFormat.parse(dateString);
		return date;
	}

	/**
	 *  24小时制 时间转字符串 转换的格式HH:mm:ss
	 *
	 *  @param time 时间
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromTime24(Date time) {
		String strDate = timeFormat24.format(time);
		return strDate;
	}

	/**
	 *  24小时制 字符串转时间 转换的格式HH:mm:ss
	 *
	 *  @param timeString 时间字符串
	 *
	 *  @return time
	 * @throws ParseException
	 */
	public static Date timeFromString24(String timeString) throws ParseException {
		Date date = timeFormat24.parse(timeString);
		return date;
	}

	/**
	 *  12小时制 时间转字符串 转换的格式hh:mm:ss
	 *
	 *  @param time 时间
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromTime12(Date time) {
		String strDate = timeFormat12.format(time);
		return strDate;
	}

	/**
	 *  12小时制 字符串转时间 转换的格式hh:mm:ss
	 *
	 *  @param timeString 时间字符串
	 *
	 *  @return time
	 * @throws ParseException
	 */
	public static Date timeFromString12(String timeString) throws ParseException {
		Date date = timeFormat12.parse(timeString);
		return date;
	}

	/**
	 *  时间转字符串 转换的格式format
	 *
	 *  @param time 时间
	 *  @param format 自定义日期时间格式
	 *
	 *  @return 格式化字符串
	 */
	public static String stringFromTime(Date time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String strDate = dateFormat.format(time);
		return strDate;
	}

	/**
	 *  字符串转时间 转换的格式format
	 *
	 *  @param timeString 时间字符串
	 *  @param format 自定义日期时间格式
	 *
	 *  @return time
	 * @throws ParseException
	 */
	public static Date timeFromString(String timeString, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(timeString);
		return date;
	}
	
	/**
	 * 同步服务器时间
	 * @param timeInteval 服务器上的GMT时间
	 * @return
	 */
	public static long timeSyncInGMT(long timeMillis) {
		long currentTimeMillis = System.currentTimeMillis();
		//比对客户端与服务端时间，若差值超过设定值，则以服务端时间为准
		if (Math.abs(currentTimeMillis-timeMillis) > ENVConfig.timeInteval) {
			return timeMillis;
		}
		return currentTimeMillis;
	}

	/**
	 * 将long时间类型转成自己想要的日期字符串格式
	 *
	 * @param long :long类型
	 * @param String :自己想要的时间格式，例如： "MM/dd" ==几月几号
	 * @Author 龙茂林
	 */
	public static String longToString(long longDate, String stringDate){
		String str;
		Date date = new Date(longDate);
		SimpleDateFormat sdf = new SimpleDateFormat(stringDate);
		str = sdf.format(date).toString();
		return  str;
	}
}