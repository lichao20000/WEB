package com.linkage.litms.uss;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 
 * @version 1.0
 * @since 
 * @category 
 * 
 */
public class DateUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final int[] dayArray = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	
	public static final long MILLIS_PER_SECOND = 1000;
	
    private static SimpleDateFormat sdf = new SimpleDateFormat();

	public static synchronized Calendar getCalendar() {
		return GregorianCalendar.getInstance();
	}

    /**
     * @return String
     */
	public static synchronized String getDateMilliFormat() {
		Calendar cal = Calendar.getInstance();
		return getDateMilliFormat(cal);
	}
	
	/**
	 * 根据传入的DateFormat参数，返回当前时间的format字符串时间
	 * 
	 * @param format
	 *            字符串参数,never null，eg. yyyy-MM-dd HH:mm:ss
	 * @return 返回当前时间的format字符串时间格式
	 */
	public static String getNowTime(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	/**
	 * 将秒计算成多少天多少小时多少分钟多少秒的可视化字符串
	 * 
	 * @param timeInSecond 秒
	 * @return 
	 */
	public static String formatTime(long timeInSecond)
	{
		DecimalFormat df = new DecimalFormat("00");
		StringBuilder result = new StringBuilder();
		// day
		if (timeInSecond >= 86400)
		{
			long day = timeInSecond / 86400;
			result.append(day).append("d ");
			timeInSecond = timeInSecond - day * 86400;
		}
		// hour
		long hh = timeInSecond >= 3600 ? timeInSecond / 3600 : 0;
		timeInSecond = timeInSecond - hh * 3600;
		// minute
		long mm = timeInSecond >= 60 ? timeInSecond / 60 : 0;
		timeInSecond = timeInSecond - mm * 60;
		// second
		long ss = timeInSecond;
		result.append(df.format(hh)).append(":").append(df.format(mm)).append(":")
				.append(df.format(ss));
		return result.toString();
	}

    /**
     * 格式化日期输出
     * 
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
	public static String format(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}
    
	
	/**
	 * <pre>
	 * 根据特定的时间格式返回当月第一天的字符串时间
	 * 如：时间格式为yyyy-MM-dd HH:mm:ss，当前时间为2013-08-28 12:12:12，则返回2013-08-01 00:00:00
	 * 如：时间格式为yyyy-MM-dd HH:mm，当前时间为2013-08-28 12:12:12，则返回2013-08-01 00:00
	 * </pre>
	 * @param datePattern 时间格式 ，never null
	 * @return 返回当前时间按时间格式转换后的当天第一天时间格式
	 */
	public static String firstDayOfCurrentMonth(String datePattern)
	{
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return new SimpleDateFormat(datePattern).format(now.getTime());
	}
	
	/**
	 * <pre>
	 * 根据特定的时间格式返回当年第一天的字符串时间
	 * 如：时间格式为yyyy-MM-dd HH:mm:ss，当前时间为2013-08-28 12:12:12，则返回2013-01-01 00:00:00
	 * 如：时间格式为yyyy-MM-dd HH:mm，当前时间为2013-08-28 12:12:12，则返回2013-01-01 00:00
	 * </pre>
	 * @param datePattern 时间格式 ，never null
	 * @return 返回当前时间按时间格式转换后的当年第一天时间格式
	 */
	public static String firstDayOfCurrentYear(String datePattern)
	{
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return new SimpleDateFormat(datePattern).format(now.getTime());
	}
	
	
	/**
	 * <pre>
	 * 根据特定时间格式返回当天时间的最后时间字符串，主要针对时分秒
	 * 如：时间格式为yyyy-MM-dd HH:mm:ss，当前时间为2013-08-28 12:12:12，则返回2013-08-28 23:59:59
	 * 如：时间格式为yyyy-MM-dd HH:mm，当前时间为2013-08-28 12:12:12，则返回2013-08-01 23:59
	 * </pre>
	 * @param datePattern 时间格式，never null
	 * @return 返回当天时间个最后时间字符串
	 */
	public static String lastTimeOfCurrentDay(String datePattern)
	{
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		return new SimpleDateFormat(datePattern).format(now.getTime());
	}
	
	/**
	 * <pre>
	 * 将秒单位时间转换为Date类型
	 * 调用该方法必须确保正确的秒参数<code>timeInSecond</code>，
	 * 如通过ResultSet.getLong()方法获取值，如果对应字段值为null，则该方法返回值为0，所以需要进行逻辑校验
	 * </pre>
	 * 
	 * @param timeInSecond
	 *            正确的秒数时间，必须大于0
	 * @return 返回秒数转换后的Date对象
	 */
	public static Date transTime(long timeInSecond)
	{
		return new Date(timeInSecond * MILLIS_PER_SECOND);
	}
	
	
	/**
	 * 将秒单位时间转换为特定时间格式的字符串
	 * 
	 * @param timeInSecond
	 *            正确的秒数时间，never null
	 * @param datePattern
	 *            需要转换时间格式，如yyyy-MM-dd，never null
	 * @return 返回秒数按时间格式转换后的时间字符串
	 * @see #transTime(long)
	 */
	public static String transTime(long timeInSecond, String datePattern)
	{
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		return df.format(transTime(timeInSecond));
	}
    
	
	/**
	 * 根据传入的Date时间，转化为以秒为单位的时间。
	 * 
	 * @param time
	 *            Date，never null
	 * @return 返回入参时间转化后的时间，以秒为单位
	 */
	public static long getTimeInSecond(Date time)
	{
		return time.getTime() / MILLIS_PER_SECOND;
	}
	
	
	/**
	 * <pre>
	 * 将字符串时间格式转化为秒单位时间。
	 * 如果date与datePattern格式不匹配等导致字符串时间格式转化失败，则抛出IllegalArgumentException异常。
	 * </pre>
	 * 
	 * @param date
	 *            字符串时间，如2013-08-27,never null
	 * @param datePattern
	 *            字符串时间格式，如yyyy-MM-dd，是date的正确格式.never null
	 * @return 返回将字符串时间正确转化后的秒单位时间格式
	 */
	public static long getTimeInSecond(String date, String datePattern)
	{
		try
		{
			SimpleDateFormat df = new SimpleDateFormat(datePattern);
			Date formatDate = df.parse(date);
			return getTimeInSecond(formatDate);
		}
		catch (ParseException e)
		{
			String message = "format date[" + date + "] in pattern[" + datePattern
					+ "] error";
			logger.error(message, e);
			throw new IllegalArgumentException(message, e);
		}
	}
	

    /**
     * 格式化日期输出
     * 
     * @param time
     * @param pattern
     * @return
     * @throws Exception
     */
	public static String format(long time, String pattern) {
		return format(new Date(time), pattern);
	}

    /**
     * 生成日报表文件名时间标记
     * 
     * @param date
     * @return
     */
	public static String getDayFileName(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		cale.add(Calendar.DATE, -1);
		Date date_ = new Date(cale.getTimeInMillis());
		return format(date_, "yyyyMMdd");
	}

    /**
     * day 流量
     * 
     * @param date
     * @return
     */
	public static String getDayOfFlux(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		cale.add(Calendar.DATE, -1);
		Date date_ = new Date(cale.getTimeInMillis());
		return format(date_, "yyyy-MM-dd");
	}

    /**
     * 生成周报表文件名时间标记
     * 
     * @param date
     * @return
     */
	public static String getWeekFileName(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		cale.add(Calendar.DATE, -7);
		return "" + cale.get(Calendar.YEAR) + "_" + cale.get(Calendar.WEEK_OF_YEAR);
	}

    /**
     * week 流量
     * 
     * @param date
     * @return
     */
	public static String getWeekOfFlux(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		int dayOfWeek = cale.get(Calendar.DAY_OF_WEEK);
		cale.add(Calendar.DATE, (-6 - dayOfWeek));
		Date date_ = new Date(cale.getTimeInMillis());
		return format(date_, "yyyy-MM-dd");
	}

    /**
     * 生成月报表文件名时间标记
     * 
     * @param date
     * @return
     */
	public static String getMonthFileName(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		int month = cale.get(Calendar.MONTH);
		// if (0 == month) {
		// cale.add(Calendar.DATE, -31);
		// }
		cale.add(Calendar.DATE, -31);
		return "" + cale.get(Calendar.YEAR) + (cale.get(Calendar.MONTH) + 1);
	}

    /**
     * month 流量
     * 
     * @param date
     * @return
     */
	public static String getMonthOfFlux(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		int month = cale.get(Calendar.MONTH);
		cale.add(Calendar.DATE, -31);
		Date date_ = new Date(cale.getTimeInMillis());
		return format(date_, "yyyy-MM");
	}

    /**
     * 生成年报表文件名时间标记
     * 
     * @param date
     * @return
     */
	public static String getYearFileName(Date date) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTime(date);
		return "" + (cale.get(Calendar.YEAR) - 1);
	}

    /**
     * year 流量
     * 
     * @param date
     * @return
     */
	public static String getYearOfFlux(Date date) {
		return getYearFileName(date);
	}

   

	public static String yesterday(long time) throws Exception {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(time);
		cale.add(Calendar.DATE, -1);
		Date dt = new Date(cale.getTimeInMillis());
		return "";
	}

    /**
     * @param cal
     * @return String
     */
	public static synchronized String getDateMilliFormat(java.util.Calendar cal) {
		String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
		return getDateFormat(cal, pattern);
	}

    /**
     * @param date
     * @return String
     */
	public static synchronized String getDateMilliFormat(java.util.Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
		return getDateFormat(date, pattern);
	}

    /**
     * @param strDate
     * @return java.util.Calendar
     */
	public static synchronized Calendar parseCalendarMilliFormat(String strDate) {
		String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
		return parseCalendarFormat(strDate, pattern);
	}

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateMilliFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
		return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateSecondFormat() {
    	Calendar cal = Calendar.getInstance();
		return getDateSecondFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateSecondFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
		return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateSecondFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
		return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarSecondFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
		return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateSecondFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateMinuteFormat() {
    	Calendar cal = Calendar.getInstance();
    	return getDateMinuteFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateMinuteFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd HH:mm";
		return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateMinuteFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd HH:mm";
    	return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarMinuteFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm";
    	return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateMinuteFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm";
    	return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateDayFormat() {
    	Calendar cal = Calendar.getInstance();
    	return getDateDayFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateDayFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd";
    	return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateDayFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd";
    	return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarDayFormat(String strDate) {
    	String pattern = "yyyy-MM-dd";
    	return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateDayFormat(String strDate) {
    	String pattern = "yyyy-MM-dd";
    	return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateFileFormat() {
    	Calendar cal = Calendar.getInstance();
    	return getDateFileFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateFileFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd_HH-mm-ss";
    	return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateFileFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd_HH-mm-ss";
		return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarFileFormat(String strDate) {
    	String pattern = "yyyy-MM-dd_HH-mm-ss";
    	return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateFileFormat(String strDate) {
    	String pattern = "yyyy-MM-dd_HH-mm-ss";
    	return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateW3CFormat() {
    	Calendar cal = Calendar.getInstance();
    	return getDateW3CFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateW3CFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateW3CFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarW3CFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateW3CFormat(String strDate) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return parseDateFormat(strDate, pattern);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateFormat(java.util.Calendar cal) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateFormat(java.util.Date date) {
    	String pattern = "yyyy-MM-dd HH:mm:ss";
    	return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
	public static synchronized Calendar parseCalendarFormat(String strDate) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		return parseCalendarFormat(strDate, pattern);
	}

    /**
     * @param strDate
     * @return java.util.Date
     */
	public static synchronized Date parseDateFormat(String strDate) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		return parseDateFormat(strDate, pattern);
	}

    /**
     * @param cal
     * @param pattern
     * @return String
     */
	public static synchronized String getDateFormat(java.util.Calendar cal, String pattern) {
		return getDateFormat(cal.getTime(), pattern);
	}

    /**
     * @param date
     * @param pattern
     * @return String
     */
	public static synchronized String getDateFormat(java.util.Date date, String pattern) {
		synchronized (sdf) {
			String str = null;
			sdf.applyPattern(pattern);
			str = sdf.format(date);
			return str;
		}
	}

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Calendar
     */
	public static synchronized Calendar parseCalendarFormat(String strDate, String pattern) {
		synchronized (sdf) {
			Calendar cal = null;
			sdf.applyPattern(pattern);
			try {
				sdf.parse(strDate);
				cal = sdf.getCalendar();
			} catch (Exception e) {
			}
			return cal;
		}
	}

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Date
     */
	public static synchronized Date parseDateFormat(String strDate, String pattern) {
		synchronized (sdf) {
			Date date = null;
			sdf.applyPattern(pattern);
			try {
				date = sdf.parse(strDate);
			} catch (Exception e) {
			}
			return date;
		}
	}

	public static synchronized int getLastDayOfMonth(int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear()) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

	public static synchronized int getLastDayOfMonth(int year, int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear(year)) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

    public static synchronized boolean isLeapYear() {
    	Calendar cal = Calendar.getInstance();
    	int year = cal.get(Calendar.YEAR);
    	return isLeapYear(year);
    }

	public static synchronized boolean isLeapYear(int year) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年 3.能被4整除同时能被100整除则不是闰年
		 */
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

    /**
     * 判断指定日期的年份是否是闰年
     * 
     * @param date
     *                指定日期。
     * @return 是否闰年
     */
	public static synchronized boolean isLeapYear(java.util.Date date) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年 3.能被4整除同时能被100整除则不是闰年
		 */
		// int year = date.getYear();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		int year = gc.get(Calendar.YEAR);
		return isLeapYear(year);
	}

    public static synchronized boolean isLeapYear(java.util.Calendar gc) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年 3.能被4整除同时能被100整除则不是闰年
		 */
    	int year = gc.get(Calendar.YEAR);
    	return isLeapYear(year);
    }

    /**
     * 得到指定日期的前一个工作日
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的前一个工作日
     */
	public static synchronized java.util.Date getPreviousWeekDay(java.util.Date date) {
		{
			/**
			 * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
			 */
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(date);
			return getPreviousWeekDay(gc);
			// switch ( gc.get( Calendar.DAY_OF_WEEK ) )
			// {
			// case ( Calendar.MONDAY ):
			// gc.add( Calendar.DATE, -3 );
			// break;
			// case ( Calendar.SUNDAY ):
			// gc.add( Calendar.DATE, -2 );
			// break;
			// default:
			// gc.add( Calendar.DATE, -1 );
			// break;
			// }
			// return gc.getTime();
		}
	}

	public static synchronized java.util.Date getPreviousWeekDay(java.util.Calendar gc) {
		{
			/**
			 * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
			 */
			switch (gc.get(Calendar.DAY_OF_WEEK)) {
				case (Calendar.MONDAY):
					gc.add(Calendar.DATE, -3);
					break;
				case (Calendar.SUNDAY):
					gc.add(Calendar.DATE, -2);
					break;
				default:
					gc.add(Calendar.DATE, -1);
					break;
			}
			return gc.getTime();
		}
	}

    /**
     * 得到指定日期的后一个工作日
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的后一个工作日
     */
	public static synchronized java.util.Date getNextWeekDay(java.util.Date date) {
		/**
		 * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 2);
				break;
			default:
				gc.add(Calendar.DATE, 1);
				break;
		}
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getNextWeekDay(java.util.Calendar gc) {
		/**
		 * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
		 */
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 2);
				break;
			default:
				gc.add(Calendar.DATE, 1);
				break;
		}
		return gc;
	}

    /**
     * 取得指定日期的下一个月的最后一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个月的最后一天
     */
	public static synchronized java.util.Date getLastDayOfNextMonth(java.util.Date date) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getLastDayOfMonth
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateUtil.getNextMonth(gc.getTime()));
		gc.setTime(DateUtil.getLastDayOfMonth(gc.getTime()));
		return gc.getTime();
	}

    /**
     * 取得指定日期的下一个星期的最后一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个星期的最后一天
     */
	public static synchronized java.util.Date getLastDayOfNextWeek(java.util.Date date) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getLastDayOfWeek
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateUtil.getNextWeek(gc.getTime()));
		gc.setTime(DateUtil.getLastDayOfWeek(gc.getTime()));
		return gc.getTime();
	}

    /**
     * 取得指定日期的下一个月的第一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个月的第一天
     */
	public static synchronized java.util.Date getFirstDayOfNextMonth(java.util.Date date) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateUtil.getNextMonth(gc.getTime()));
		gc.setTime(DateUtil.getFirstDayOfMonth(gc.getTime()));
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getFirstDayOfNextMonth(
			java.util.Calendar gc) {
		/**
		 * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
		 */
		gc.setTime(DateUtil.getNextMonth(gc.getTime()));
		gc.setTime(DateUtil.getFirstDayOfMonth(gc.getTime()));
		return gc;
	}

    /**
     * 取得指定日期的下一个星期的第一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个星期的第一天
     */
	public static synchronized java.util.Date getFirstDayOfNextWeek(java.util.Date date) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.setTime(DateUtil.getNextWeek(gc.getTime()));
		gc.setTime(DateUtil.getFirstDayOfWeek(gc.getTime()));
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getFirstDayOfNextWeek(
			java.util.Calendar gc) {
		/**
		 * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
		 */
		gc.setTime(DateUtil.getNextWeek(gc.getTime()));
		gc.setTime(DateUtil.getFirstDayOfWeek(gc.getTime()));
		return gc;
	}

    /**
     * 取得指定日期的下一个月
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个月
     */
    public static synchronized java.util.Date getNextMonth(java.util.Date date) {
		/**
		 * 详细设计： 1.指定日期的月份加1
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, 1);
		return gc.getTime();
	}

    public static synchronized java.util.Calendar getNextMonth(
	    java.util.Calendar gc) {
	/**
	 * 详细设计： 1.指定日期的月份加1
	 */
	gc.add(Calendar.MONTH, 1);
	return gc;
    }

    /**
     * 取得指定日期的下一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一天
     */
	public static synchronized java.util.Date getNextDay(java.util.Date date) {
		/**
		 * 详细设计： 1.指定日期加1天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return gc.getTime();
	}

    public static synchronized java.util.Calendar getNextDay(
	    java.util.Calendar gc) {
	/**
	 * 详细设计： 1.指定日期加1天
	 */
	gc.add(Calendar.DATE, 1);
	return gc;
    }

    /**
     * 取得指定日期的下一个星期
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的下一个星期
     */
	public static synchronized java.util.Date getNextWeek(java.util.Date date) {
		/**
		 * 详细设计： 1.指定日期加7天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 7);
		return gc.getTime();
	}

    public static synchronized java.util.Calendar getNextWeek(
	    java.util.Calendar gc) {
	/**
	 * 详细设计： 1.指定日期加7天
	 */
	gc.add(Calendar.DATE, 7);
	return gc;
    }

    /**
     * 取得指定日期的所处星期的最后一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的所处星期的最后一天
     */
	public static synchronized java.util.Date getLastDayOfWeek(java.util.Date date) {
		/**
		 * 详细设计： 1.如果date是星期日，则加6天 2.如果date是星期一，则加5天 3.如果date是星期二，则加4天 4.如果date是星期三，则加3天
		 * 5.如果date是星期四，则加2天 6.如果date是星期五，则加1天 7.如果date是星期六，则加0天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 6);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, 5);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, 4);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, 3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, 2);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, 1);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, 0);
				break;
		}
		return gc.getTime();
	}

    /**
     * 取得指定日期的所处星期的第一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的所处星期的第一天
     */
	public static synchronized java.util.Date getFirstDayOfWeek(java.util.Date date) {
		/**
		 * 详细设计： 1.如果date是星期日，则减0天 2.如果date是星期一，则减1天 3.如果date是星期二，则减2天 4.如果date是星期三，则减3天
		 * 5.如果date是星期四，则减4天 6.如果date是星期五，则减5天 7.如果date是星期六，则减6天
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 0);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -1);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, -2);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, -4);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, -5);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, -6);
				break;
		}
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getFirstDayOfWeek(java.util.Calendar gc) {
		/**
		 * 详细设计： 1.如果date是星期日，则减0天 2.如果date是星期一，则减1天 3.如果date是星期二，则减2天 4.如果date是星期三，则减3天
		 * 5.如果date是星期四，则减4天 6.如果date是星期五，则减5天 7.如果date是星期六，则减6天
		 */
		switch (gc.get(Calendar.DAY_OF_WEEK)) {
			case (Calendar.SUNDAY):
				gc.add(Calendar.DATE, 0);
				break;
			case (Calendar.MONDAY):
				gc.add(Calendar.DATE, -1);
				break;
			case (Calendar.TUESDAY):
				gc.add(Calendar.DATE, -2);
				break;
			case (Calendar.WEDNESDAY):
				gc.add(Calendar.DATE, -3);
				break;
			case (Calendar.THURSDAY):
				gc.add(Calendar.DATE, -4);
				break;
			case (Calendar.FRIDAY):
				gc.add(Calendar.DATE, -5);
				break;
			case (Calendar.SATURDAY):
				gc.add(Calendar.DATE, -6);
				break;
		}
		return gc;
	}

    /**
     * 取得指定日期的所处月份的最后一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的所处月份的最后一天
     */
	public static synchronized java.util.Date getLastDayOfMonth(java.util.Date date) {
		/**
		 * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日 4.如果date在4月，则为30日
		 * 5.如果date在5月，则为31日 6.如果date在6月，则为30日 7.如果date在7月，则为31日 8.如果date在8月，则为31日
		 * 9.如果date在9月，则为30日 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
		 * 1.如果date在闰年的2月，则为29日
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(Calendar.MONTH)) {
			case 0:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 1:
				gc.set(Calendar.DAY_OF_MONTH, 28);
				break;
			case 2:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 3:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 4:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 5:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 6:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 7:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 8:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 9:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 10:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 11:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
		}
		// 检查闰年
		if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY)
				&& (isLeapYear(gc.get(Calendar.YEAR)))) {
			gc.set(Calendar.DAY_OF_MONTH, 29);
		}
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getLastDayOfMonth(java.util.Calendar gc) {
		/**
		 * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日 4.如果date在4月，则为30日
		 * 5.如果date在5月，则为31日 6.如果date在6月，则为30日 7.如果date在7月，则为31日 8.如果date在8月，则为31日
		 * 9.如果date在9月，则为30日 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
		 * 1.如果date在闰年的2月，则为29日
		 */
		switch (gc.get(Calendar.MONTH)) {
			case 0:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 1:
				gc.set(Calendar.DAY_OF_MONTH, 28);
				break;
			case 2:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 3:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 4:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 5:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 6:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 7:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 8:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 9:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
			case 10:
				gc.set(Calendar.DAY_OF_MONTH, 30);
				break;
			case 11:
				gc.set(Calendar.DAY_OF_MONTH, 31);
				break;
		}
		// 检查闰年
		if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY)
				&& (isLeapYear(gc.get(Calendar.YEAR)))) {
			gc.set(Calendar.DAY_OF_MONTH, 29);
		}
		return gc;
	}

    /**
     * 取得指定日期的所处月份的第一天
     * 
     * @param date
     *                指定日期。
     * @return 指定日期的所处月份的第一天
     */
	public static synchronized java.util.Date getFirstDayOfMonth(java.util.Date date) {
		/**
		 * 详细设计： 1.设置为1号
		 */
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return gc.getTime();
	}

	public static synchronized java.util.Calendar getFirstDayOfMonth(java.util.Calendar gc) {
		/**
		 * 详细设计： 1.设置为1号
		 */
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return gc;
	}

    /**
     * 将日期对象转换成为指定ORA日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
     * 
     * @param theDate
     *                将要转换为字符串的日期对象。
     * @param hasTime
     *                如果返回的字符串带时间则为true
     * @return 转换的结果
     */
	public static synchronized String toOraString(Date theDate, boolean hasTime) {
		/**
		 * 详细设计： 1.如果有时间，则设置格式为getOraDateTimeFormat()的返回值 2.否则设置格式为getOraDateFormat()的返回值
		 * 3.调用toString(Date theDate, DateFormat theDateFormat)
		 */
		DateFormat theFormat;
		if (hasTime) {
			theFormat = getOraDateTimeFormat();
		} else {
			theFormat = getOraDateFormat();
		}
		return toString(theDate, theFormat);
	}

	/**
	 * 将日期对象转换成为指定日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
	 * 
	 * @param theDate
	 *            将要转换为字符串的日期对象。
	 * @param hasTime
	 *            如果返回的字符串带时间则为true
	 * @return 转换的结果
	 */
	public static synchronized String toString(Date theDate, boolean hasTime) {
		/**
		 * 详细设计： 1.如果有时间，则设置格式为getDateTimeFormat的返回值 2.否则设置格式为getDateFormat的返回值
		 * 3.调用toString(Date theDate, DateFormat theDateFormat)
		 */
		DateFormat theFormat;
		if (hasTime) {
			theFormat = getDateTimeFormat();
		} else {
			theFormat = getDateFormat();
		}
		return toString(theDate, theFormat);
	}

	/**
	 * 标准日期格式
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * 标准时间格式
	 */
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm");
	/**
	 * 带时分秒的标准时间格式
	 */
	private static final SimpleDateFormat DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");
	/**
	 * ORA标准日期格式
	 */
	private static final SimpleDateFormat ORA_DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	/**
	 * ORA标准时间格式
	 */
	private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm");
	/**
	 * 带时分秒的ORA标准时间格式
	 */
	private static final SimpleDateFormat ORA_DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * 创建一个标准日期格式的克隆
	 * 
	 * @return 标准日期格式的克隆
	 */
	public static synchronized DateFormat getDateFormat() {
		/**
		 * 详细设计： 1.返回DATE_FORMAT
		 */
		SimpleDateFormat theDateFormat = (SimpleDateFormat) DATE_FORMAT.clone();
		theDateFormat.setLenient(false);
		return theDateFormat;
	}

	/**
	 * 创建一个标准时间格式的克隆
	 * 
	 * @return 标准时间格式的克隆
	 */
	public static synchronized DateFormat getDateTimeFormat() {
		/**
		 * 详细设计： 1.返回DATE_TIME_FORMAT
		 */
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) DATE_TIME_FORMAT.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 创建一个标准ORA日期格式的克隆
	 * 
	 * @return 标准ORA日期格式的克隆
	 */
	public static synchronized DateFormat getOraDateFormat() {
		/**
		 * 详细设计： 1.返回ORA_DATE_FORMAT
		 */
		SimpleDateFormat theDateFormat = (SimpleDateFormat) ORA_DATE_FORMAT.clone();
		theDateFormat.setLenient(false);
		return theDateFormat;
	}

	/**
	 * 创建一个标准ORA时间格式的克隆
	 * 
	 * @return 标准ORA时间格式的克隆
	 */
	public static synchronized DateFormat getOraDateTimeFormat() {
		/**
		 * 详细设计： 1.返回ORA_DATE_TIME_FORMAT
		 */
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串，而不是一个空对象。
	 * 
	 * @param theDate
	 *            要转换的日期对象
	 * @param theDateFormat
	 *            返回的日期字符串的格式
	 * @return 转换结果
	 */
	public static synchronized String toString(Date theDate, DateFormat theDateFormat) {
		/**
		 * 详细设计： 1.theDate为空，则返回"" 2.否则使用theDateFormat格式化
		 */
		if (theDate == null)
			return "";
		return theDateFormat.format(theDate);
	}

}

