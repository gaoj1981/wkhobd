package com.wkhmedical.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateUtil {
	private static final String days[] = { "周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	/**
	 * 日期比较
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(int[] date1, int[] date2) {
		int countEqual = 0;
		for (int i = 0; i < date1.length; i++) {
			if (date1[i] < date2[i]) {
				return true;
			}
			else if (date1[i] == date2[i]) {
				countEqual++;
			}
			else {
				return false;
			}
		}
		if (countEqual == date1.length) return true;
		return false;
	}

	/**
	 * 返回相对日期
	 * 
	 * @param dateTime 原日期
	 * @param month 偏移月数
	 * @return Date
	 */
	public static Date getDateAddMonth(Date dateTime, int month) {
		Calendar cald = Calendar.getInstance();
		cald.setTime(dateTime);
		cald.add(Calendar.MONTH, month);
		return cald.getTime();
	}

	public static String getDateAddMonth4Str(String dateTime, String dateFormat, int month) {
		return formatDate(getDateAddMonth(parseToDate(dateTime, dateFormat), month), dateFormat);
	}

	/**
	 * 返回相对日期
	 * 
	 * @param dateTime 原日期
	 * @param day 偏移天数
	 * @return Date
	 */
	public static Date getDateAddDay(Date dateTime, int day) {
		Calendar cald = Calendar.getInstance();
		cald.setTime(dateTime);
		cald.add(Calendar.DATE, day);
		return cald.getTime();
	}

	public static String getDateAddDay4Str(String dateTime, String dateFormat, int day) {
		return formatDate(getDateAddDay(parseToDate(dateTime, dateFormat), day), dateFormat);
	}

	/**
	 * 返回相对日期
	 * 
	 * @param dateTime 原日期
	 * @param minute 偏移分钟数
	 * @return Date
	 */
	public static Date getDateAddMinute(Date dateTime, int minute) {
		Calendar cald = Calendar.getInstance();
		cald.setTime(dateTime);
		cald.add(Calendar.MINUTE, minute);
		return cald.getTime();
	}

	public static String getDateAddMinute4Str(String dateTime, String dateFormat, int minute) {
		return formatDate(getDateAddMinute(parseToDate(dateTime, dateFormat), minute), dateFormat);
	}

	/**
	 * 获取日期间的相差天数。确保格式：yyyy-MM-dd
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDays(String date1, String date2) {
		Date dt1 = parseToDate(date1, "yyyy-MM-dd");
		Date dt2 = parseToDate(date2, "yyyy-MM-dd");
		int days = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	public static int getDiffDays(Date date1, Date date2) {
		Date dt1 = parseToDate(formatDate(date1, "yyyy-MM-dd"), "yyyy-MM-dd");
		Date dt2 = parseToDate(formatDate(date2, "yyyy-MM-dd"), "yyyy-MM-dd");
		int days = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	public static int getDiffMinutes(Date date1, Date date2) {
		int days = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60));
		return days;
	}

	public static Date parseToDate(String s, String format) {
		if (s == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(s);
		}
		catch (ParseException e) {
			date = null;
		}
		return date;
	}

	public static String formatDate(Date date, String format) {
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(date);
		return s;
	}

	public static String formatDate4Eng(Date date, String format) {
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		String s = sdf.format(date);
		return s;
	}

	/**
	 * 根据日期算出周几
	 * 
	 * @param ymd
	 * @return
	 * @throws Exception
	 */
	public static String getweek(String str, String format) {
		int weekday = 0;
		try {
			if (str != null && !"".equals(str)) {
				SimpleDateFormat adf = new SimpleDateFormat(format, java.util.Locale.CHINA);
				Date date = adf.parse(str);
				Calendar calendar1 = Calendar.getInstance(java.util.Locale.CHINA);
				calendar1.setTime(date);
				weekday = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
			}
		}
		catch (Exception e) {
		}
		return days[weekday];
	}

	/**
	 * 根据日期算出周几
	 * 
	 * @param ymd
	 * @return
	 * @throws Exception
	 */
	public static String getweek(Date date) {
		int weekday = 0;
		if (date != null) {
			Calendar calendar1 = Calendar.getInstance(java.util.Locale.CHINA);
			calendar1.setTime(date);
			weekday = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return days[weekday];
	}

	/**
	 * 根据日期算出周几
	 * 
	 * @param ymd
	 * @return
	 * @throws Exception
	 */
	public static int getweekNum(Date date) {
		int weekday = 0;
		if (date != null) {
			Calendar calendar1 = Calendar.getInstance(java.util.Locale.CHINA);
			calendar1.setTime(date);
			weekday = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return weekday;
	}

	public static String getStrTimes() {
		Long timeLn = new Date().getTime();
		return timeLn.toString();
	}

	public static Long getTimestamp() {
		return new Date().getTime();
	}

	/**
	 * 获得时间差，单位秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static float getTimesInterval(Date date1, Date date2) {
		Long time1 = date1.getTime();
		Long time2 = date2.getTime();
		return (time2 - time1) / 1000;
	}

	public static long getDiffSeconds(Date date1, Date date2) {
		long seconds = (long) ((date2.getTime() - date1.getTime()) / 1000);
		return seconds;
	}

	public static String getNowDateByFormat(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = new Date();
		String dt = format.format(date);
		return dt;
	}

	public static Date getCurDateByFormat(String formatStr) {
		String nowStr = getNowDateByFormat(formatStr);
		return parseToDate(nowStr, formatStr);
	}

	public static String getCurDateBegin() {
		return getNowDateByFormat("yyyy-MM-dd") + " 00:00:00";
	}

	public static String getDateBegin(Date date) {
		return formatDate(date, "yyyy-MM-dd") + " 00:00:00";
	}

	public static String getNextDateBegin(Date date) {
		Date date1 = getDateAddDay(date, 1);
		return formatDate(date1, "yyyy-MM-dd") + " 00:00:00";
	}

	public static String getLastDateBegin(Date date) {
		Date date1 = getDateAddDay(date, -1);
		return formatDate(date1, "yyyy-MM-dd") + " 00:00:00";
	}

	public static String getStrDateByFormat(String dateStr, String format, String formatStr) {
		Date date = parseToDate(dateStr, format);
		return formatDate(date, formatStr);
	}

	public static String getCurWeekBegin() {
		Date date = new Date();
		Calendar calendar1 = Calendar.getInstance(java.util.Locale.CHINA);
		calendar1.setTime(date);
		int weekday = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekday == 0) {
			weekday = 7;
		}
		int days = weekday - 1;
		String dtNow = formatDate(date, "yyyy-MM-dd");
		if (days == 0) {
			return dtNow;
		}
		else {
			days = 0 - days;
			return getDateAddDay4Str(dtNow, "yyyy-MM-dd", days);
		}
	}

	public static Date getCurMonthBegin() {
		int year = getNowYear();
		int month = getNowMonth();
		String preMonth = "";
		if (month < 10) {
			preMonth = "0";
		}
		return parseToDate(year + "-" + preMonth + month + "-01", "yyyy-MM-dd");
	}

	public static Date getCurYearBegin() {
		int year = getNowYear();
		return parseToDate(year + "-01-01", "yyyy-MM-dd");
	}

	public static String getDateEnd(Date date) {
		return formatDate(date, "yyyy-MM-dd") + " 23:59:59";
	}

	public static int getNowMonth() {
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH);
		return month + 1;
	}

	public static int getNowYear() {
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static int getDaysOfYM(int year, int month) {
		String preMonth = "";
		if (month < 10) {
			preMonth = "0";
		}
		Calendar calendar = Calendar.getInstance(java.util.Locale.CHINA);
		calendar.setTime(parseToDate(year + preMonth + month + "01", "yyyyMMdd"));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getDaysOfMonth(int month) {
		String preMonth = "";
		if (month < 10) {
			preMonth = "0";
		}
		Calendar calendar = Calendar.getInstance(java.util.Locale.CHINA);
		calendar.setTime(parseToDate(getNowYear() + preMonth + month + "01", "yyyyMMdd"));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getDaysOfMonth(String month) {
		Calendar calendar = Calendar.getInstance(java.util.Locale.CHINA);
		calendar.setTime(parseToDate(getNowYear() + month + "01", "yyyyMMdd"));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Date[] getMonthSTArr(int month) {
		String preMonth = "";
		if (month < 10) {
			preMonth = "0";
		}
		int year = getNowYear();
		Date dt1 = parseToDate(year + preMonth + month + "01", "yyyyMMdd");
		int days = getDaysOfMonth(month);
		Date dt2 = parseToDate(year + preMonth + month + days, "yyyyMMdd");
		return new Date[] { dt1, dt2 };
	}

	public static Date[] getMonthSTArr(String ymStr) {
		String month = ymStr.substring(4);
		String year = ymStr.substring(0, 4);
		Date dt1 = parseToDate(year + month + "01", "yyyyMMdd");
		int days = getDaysOfMonth(month);
		Date dt2 = parseToDate(year + month + days, "yyyyMMdd");
		return new Date[] { dt1, dt2 };
	}

	public static String[] getMonthYear() {
		String[] latest12Months = new String[12];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); // 要先+1,才能把本月的算进去
		for (int i = 0; i < 12; i++) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); // 逐次往前推1个月
			latest12Months[11 - i] = cal.get(Calendar.YEAR) + fillZero(cal.get(Calendar.MONTH) + 1);
		}
		return latest12Months;
	}

	public static String fillZero(int i) {
		String month = "";
		if (i < 10) {
			month = "0" + i;
		}
		else {
			month = String.valueOf(i);
		}
		return month;
	}

	public static void main(String[] args) {
		System.out.println(getCurMonthBegin());
	}
}
