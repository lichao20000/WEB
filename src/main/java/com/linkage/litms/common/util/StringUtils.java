/*
 * @(#)StringUtils.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;

/**
 * 格式化一些与字符串相关数据类
 *
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */
public class StringUtils {
	private static Logger m_logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 按照<code>pattern</code>格式化日期格式
	 *
	 * @param pattern
	 *            日期格式，如：yyyy-MM-dd HH:mm:ss
	 * @param lms
	 *            自 1970 年 1 月 1 日 00:00:00 GMT 以来的秒数
	 * @return 返回<code>pattern</code>格式的字符
	 */
	public static String formatDate(String pattern, long lms) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date nowc = new Date(lms * 1000L);

		return formatter.format(nowc);
	}

	/**
	 * 将字符串格式化成数字格式，小数点由<code>digits</code>决定
	 *
	 * @param str
	 *            需要格式化数字字符串
	 * @param digits
	 *            保留小数点位数
	 * @return 返回数字格式字符串
	 */
	public static String formatNumber(String str, int digits) {
		if (str == null || str.equals(""))
			return "0";
		Number n = null;
		NumberFormat nfLocal = NumberFormat.getNumberInstance();
		try {
			n = nfLocal.parse(str);
		} catch (ParseException parseexception) {
		}
		return formatNumber(n, digits);
	}

	/**
	 * 格式化Number数字，小数点由<code>digits</code>决定
	 *
	 * @param v
	 *            Number值，必须是BigDecimal、BigInteger、Byte、Double、Float、Integer、Long
	 *            和 Short 类
	 * @param digits
	 *            保留小数点位数
	 * @return 返回数字格式字符串
	 */
	public static String formatNumber(Number v, int digits) {
		NumberFormat nfLocal = NumberFormat.getNumberInstance();
		nfLocal.setMaximumFractionDigits(digits);
		return nfLocal.format(v);
	}

	/**
	 * 将字符串数组用“,”分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param arr
	 *            字符串数组
	 * @return 返回数组所有元素，逗号分隔
	 */
	public static String weave(String[] arr) {
		return weave(arr, ",");
	}

	//取得这一天在一周中的第几天
	public static int getDayOfWeek(long mills) {
		Calendar cale = Calendar.getInstance();
		long ms = (long) mills * 1000;
		cale.setTimeInMillis(ms);
		return cale.get(Calendar.DAY_OF_WEEK);
	}

	// 取得这一天在一年中的第几周

	public static int getWeekOfYear(int mills) {
		Calendar cale = Calendar.getInstance();
		long ms = (long) mills * 1000;
		cale.setTimeInMillis(ms);

		return cale.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 将list中内容用“,”分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param list
	 * @return String
	 * @author yanhj
	 * @date 2006-2-6
	 */
	public static String weave(List list) {
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append("'").append(list.get(0)).append("'");

			for (int i = 1; i < list.size(); i++) {
				sb.append(",'").append(list.get(i)).append("'");
			}
		}

		return sb.toString();
	}

	/**
	 * 将list中内容用“,”分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param list
	 * @return String
	 * @author yanhj
	 * @date 2006-2-6
	 */
	public static String weaveNumber(List list) {
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append(list.get(0));

			for (int i = 1; i < list.size(); i++) {
				sb.append(",").append(list.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * 将list中内容用“,”分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param list
	 * @return String
	 * @author yanhj
	 * @date 2006-2-6
	 */
	public static String weave(List list, String regx) {
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append(list.get(0));

			for (int i = 1; i < list.size(); i++) {
				sb.append(regx).append(list.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * 将字符串数组用<code>regx</code>分隔符组合一个字符串是java String类split反向操作
	 *
	 * @param arr
	 *            字符串数组
	 * @param regx
	 *            分隔字符串
	 * @return 返回数组所有元素，<code>regx</code>分隔
	 */
	public static String weave(String[] arr, String regx) {
		StringBuffer sb = new StringBuffer(100);
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i < arr.length - 1)
				sb.append(regx);
		}
		m_logger.debug("sb = " + sb.toString());
		return sb.toString();
	}

	/**
	 * 静态方法，用于分割字符串，返回链表 s 待分割字符串 separator 分隔符 返回：链表
	 */
	public static ArrayList split(String s, String separator) {
		if (s == null)
			throw new NullPointerException("source String cannot be null");
		if (separator == null)
			throw new NullPointerException("separator cannot be null");
		if (separator.length() == 0)
			throw new IllegalArgumentException("separator cannot be empty");

		ArrayList tmp = new ArrayList();
		tmp.clear();

		int start = 0;
		int separatorLen = separator.length();
		int end = s.indexOf(separator);

		while (end != -1) {
			tmp.add(s.substring(start, end));
			start = end + separatorLen;
			end = s.indexOf(separator, start);
		}

		tmp.add(s.substring(start, s.length()));
		// String[] result = new String[tmp.size()];
		// tmp.toArray(result);

		// 消除分割产生的空格
		for (int ss = 0; ss < tmp.size(); ss++) {
			if (tmp.get(ss).equals("")) {
				tmp.remove(ss);
				ss--;
			}
		}

		return tmp;
	}

	/**
	 * 静态方法，用于分割字符串，返回链表 s 待分割字符串 separator 分隔符 返回：数组
	 */
	public static Object[] partOff(String s, String separator) {
		return StringUtils.split(s, separator).toArray();
	}

	/**
	 * 获取下一个月第一天的时间
	 *
	 * @param mills
	 * @return
	 */
	public static long getNextMonthDay(long mills) {
		Calendar cale = Calendar.getInstance();
		long ms = (long) mills * 1000;
		cale.setTimeInMillis(ms);
		cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH) + 1, 1);
		return (cale.getTimeInMillis() / 1000);
	}

	/**
	 * 获取当月的第一天的时间
	 *
	 * @param mills
	 * @return
	 */
	public static long getNowMonthDay(long mills) {
		Calendar cale = Calendar.getInstance();
		long ms = (long) mills * 1000;
		cale.setTimeInMillis(ms);
		cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), 1);
		return (cale.getTimeInMillis() / 1000);
	}

	/**
	 * 用于统计端口
	 *
	 * @param ports
	 *            端口字符串
	 * @param len
	 *            长度
	 * @retuen 返回列表
	 */
	public static ArrayList getPorts(String ports, int len) {
		String[] tmpPorts = ports.split(",");
		ArrayList list = new ArrayList();
		if (len < 2) {
			return list;
		}
		int j = 0;
		String tmpPort = "";
		for (int i = 0; i < tmpPorts.length; i++) {
			if (j == 0) {
				tmpPort += tmpPorts[i];
			} else {
				tmpPort += "," + tmpPorts[i];
			}
			j++;
			if (i != 0 && ((i + 1) % len) == 0) {
				list.add(tmpPort);
				tmpPort = "";
				j = 0;
			} else if (i == (tmpPorts.length - 1)) {
				list.add(tmpPort);
				tmpPort = "";
				j = 0;
			}
		}

		return list;
	}

	/**
	 * 获取一时间（s)的年，月，日，时，分，秒.
	 *
	 * @param datetime
	 *            以秒为单位的时间(int).
	 * @return String[].
	 * @author Yanhj
	 * @date 2006-1-23
	 */
	public static String[] secondToDateStr(int datetime) {
		long temp = (long) datetime * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(temp);
		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH) + 1;
		int iDay = cal.get(Calendar.DATE);
		int iHour = cal.get(Calendar.HOUR);
		int iMinute = cal.get(Calendar.MINUTE);
		int iSecond = cal.get(Calendar.SECOND);

		ArrayList result = new ArrayList();
		result.add(new Integer(iYear).toString());
		result.add(new Integer(iMonth).toString());
		result.add(new Integer(iDay).toString());
		result.add(new Integer(iHour).toString());
		result.add(new Integer(iMinute).toString());
		result.add(new Integer(iSecond).toString());
		String[] r = new String[result.size()];

		return (String[]) result.toArray(r);
	}

	/**
	 * 获取一时间（s)的年
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return int.
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static int getYear(int datetime) {
		long temp = (long) datetime * 1000L;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(temp);
		int iYear = cal.get(Calendar.YEAR);

		return iYear;
	}

	/**
	 * 获取一时间（s)的月
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return int.
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static int getMonth(int datetime) {
		long temp = (long) datetime * 1000L;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(temp);
		int iMonth = cal.get(Calendar.MONTH) + 1;

		return iMonth;
	}

	/**
	 * 获取一时间（s)的天
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return int.
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static int getDate(int datetime) {
		long temp = (long) datetime * 1000L;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(temp);
		int iDate = cal.get(Calendar.DATE);

		return iDate;
	}

	/**
	 * 获取一时间（s)的周,以周日为一周第一天
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return int.
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static int getWeek(int datetime) {
		long temp = (long) datetime * 1000L;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(temp);
		int iWeek = cal.get(Calendar.WEEK_OF_YEAR);

		return iWeek;
	}

	/**
	 * 获取一时间（s)的周,以周日为一周第一天
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return String.eg."01"
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static String getDateMore(int datetime) {
		String tem = "";
		int month = StringUtils.getDate(datetime);
		if (month < 10)
			tem = "0" + month;
		else
			tem = "" + month;

		return tem;
	}

	/**
	 * 获取一时间（s)的周,以周日为一周第一天
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return String.eg."01"
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static String getMonthMore(int datetime) {
		String tem = "";
		int month = StringUtils.getMonth(datetime);
		if (month < 10)
			tem = "0" + month;
		else
			tem = "" + month;

		return tem;
	}

	/**
	 * 获取一时间（s)的周,以周一为一周第一天
	 *
	 * @param datetime以秒为单位的时间(int).
	 * @return int.
	 * @author yanhj.
	 * @date 2006-1-24
	 */
	public static int getWeekNew(int datetime) {
		long temp = (long) datetime * 1000L;
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTimeInMillis(temp);
		int iWeek = cal.get(Calendar.WEEK_OF_YEAR);

		return iWeek;
	}

	/**
	 * 字符串替换
	 *
	 * @return 替换后的字符串
	 * @author shenkj
	 * @date 2006-2-7
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		java.lang.String strDest = "";
		int intFromLen = strFrom.length();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, intPos);
			strDest = strDest + strTo;
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest + strSource;

		return strDest;
	}

	/**
	 * 数据格式化，取小数点后指定位
	 *
	 * @param str
	 *            格式化的客串
	 * @param digits
	 *            小数点后位数
	 * @return
	 * @author yanhj
	 * @date 2006-2-9
	 */
	public static String formatString(String str, int digits) {
		if (str == null || str.equals(""))
			return "0";

		Number n = null;
		NumberFormat nfLocal = NumberFormat.getNumberInstance();

		try {
			n = nfLocal.parse(str);
		} catch (ParseException parseexception) {
			m_logger.error(parseexception.getMessage());
		}

		nfLocal.setMaximumFractionDigits(digits);

		return nfLocal.format(Double.parseDouble(String.valueOf(n)));
	}

	/**
	 * 数据格式化，取小数点后指定位
	 *
	 * @param d
	 * @param digits
	 * @return
	 * @author yanhj
	 * @date 2006-2-9
	 */
	public static String formatString(double d, int digits) {
		NumberFormat nfLocal = NumberFormat.getNumberInstance();
		nfLocal.setMaximumFractionDigits(digits);

		return nfLocal.format(d);

	}

	/**
	 * 数据格式化，取小数点后指定位
	 *
	 * @param v
	 * @param digits
	 * @return
	 * @author yanhj
	 * @date 2006-2-9
	 */
	public static String formatString(int v, int digits) {
		NumberFormat nfLocal = NumberFormat.getNumberInstance();
		nfLocal.setMaximumFractionDigits(digits);

		return nfLocal.format(v);

	}

	/**
	 * 将指定时间格式化.
	 *
	 * @param pattern
	 * @param lms
	 * @return
	 */
	public static String getDateTimeStr(String pattern, long lms) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date nowc = new Date(lms);

		return formatter.format(nowc);
	}

	// 获取各个地市的属地编码和对应的属地名称
	/**
	 * needSZX:判断是否要将省中心的代码加入到查询表里面去
	 */
	public static String getCitySQL(boolean needSZX, HttpSession session) {

		DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
		String parent_id = (String) dbUserRes.getCityId();

		String mysql = "select city_id,city_name from tab_city";

		// 是否需要省中心
		if (needSZX) {
			mysql = "select city_id,city_name from tab_city where parent_id ='"
					+ parent_id + "' or parent_id ='-1'";
		} else {
			mysql = "select city_id,city_name from tab_city where parent_id ='"
					+ parent_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		return mysql;
	}

	public static String getCitySQL(boolean needSZX) {
		String mysql = "select city_id,city_name from tab_city";
		String IsSZX = LipossGlobals.getLipossProperty("InstArea.IsSZX");


		// 需要将省中心加入，对于本地网来说是不需要将省中心的属地编码加入的，只对于省局有效
		if (needSZX) {
			if (IsSZX != null && IsSZX.equals("true")) {
				if (Global.instAreaShortName != null) {
					
					//add by zhangcong@ 2011-06-21
					//TODO 待整理

					// oracle
					if(Global.DB_ORACLE == DBUtil.GetDB()){
						if (Global.instAreaShortName.substring(0, 2).equals("js")) {
							mysql = "select city_id,city_name from tab_city where to_number(city_id)>=0 and to_number(city_id)<14 order by city_id";
						} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {

							mysql = "select * from  tab_city where to_char(city_id) like '%01'";
						} else {
							mysql = "select * from tab_city where parent_id='00' or parent_id is null";
						}
					}
					// sysbase
					else if (Global.DB_SYBASE == DBUtil.GetDB()) {
						if (Global.instAreaShortName.substring(0, 2).equals("js")) {
							mysql = "select city_id,city_name from tab_city where convert(int,city_id)>=0 and convert(int,city_id)<14 order by city_id";
						} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {

							mysql = "select * from  tab_city where convert(varchar,city_id) like '%01'";
						} else {
							mysql = "select * from tab_city where parent_id='00' or parent_id=null";
						}
					}
					// teledb
					else if (DBUtil.GetDB() == Global.DB_MYSQL) {
						if (Global.instAreaShortName.substring(0, 2).equals("js")) {
							mysql = "select city_id,city_name from tab_city where cast(city_id as SIGNED)>=0 and cast(city_id as SIGNED)<14 order by city_id";
						} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {
							mysql = "select city_id,city_name, parent_id, staff_id from  tab_city where CAST(city_id AS CHAR) like '%01'";
						} else {
							mysql = "select city_id,city_name, parent_id, staff_id from tab_city where parent_id='00' or (parent_id is null or parent_id = '')";
						}
					}
				}

			}
			// 判断是否是其他地市
			else {
				if (Global.instAreaShortName != null) {
					
					//add by zhangcong@ 2011-06-21
					//TODO 待整理
					// oracle
					if(Global.DB_ORACLE == DBUtil.GetDB()) {
						if (Global.instAreaShortName.substring(0, 2).equals("js"))
						{
							mysql = "select city_id,city_name from tab_city where to_number(city_id)>0 and to_number(city_id)<14 order by city_id";

						} else if (Global.instAreaShortName.substring(0, 2).equals("sx"))
						{
							mysql = "select * from  tab_city where to_char(city_id) like '%01'";
						} else
						{
							mysql = "select * from tab_city where parent_id='00' or parent_id is null";
						}
					}
					// sysbase
					else if (Global.DB_SYBASE == DBUtil.GetDB()) {
						if (Global.instAreaShortName.substring(0, 2).equals("js")) {
							mysql = "select city_id,city_name from tab_city where convert(int,city_id)>0 and convert(int,city_id)<14 order by city_id";

						} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {
							mysql = "select * from  tab_city where convert(varchar,city_id) like '%01'";
						} else {
							mysql = "select * from tab_city where parent_id='00' or parent_id=null";
						}
					}
					// teledb
					else if (DBUtil.GetDB() == Global.DB_MYSQL) {
						if (Global.instAreaShortName.substring(0, 2).equals("js")) {
							mysql = "select city_id,city_name from tab_city where cast(city_id as SIGNED)>0 and cast(city_id as SIGNED)<14 order by city_id";
						} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {
							mysql = "select city_id,city_name, parent_id, staff_id from tab_city where CAST(city_id AS CHAR) like '%01'";
						} else {
							mysql = "select city_id,city_name, parent_id, staff_id from tab_city where parent_id='00' or (parent_id is null or parent_id = '')";
						}
					}

				}

			}

		}
		// 不需要将省中心加入
		else {
			if (Global.instAreaShortName != null) {

				// oracle
				if(Global.DB_ORACLE == DBUtil.GetDB()) {
					if (Global.instAreaShortName.substring(0, 2).equals("js")) {
						mysql = "select city_id,city_name from tab_city where  to_number(city_id)>0 and to_number(city_id)<14 order by to_number(city_id)";
					} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {

						mysql = "select * from  tab_city where to_char(city_id) like '%01'";
						// mysql = "select * from tab_city where
						// to_char(city_id) like '%01' order by sequ";
					} else {
						mysql = "select * from tab_city where parent_id='00'";
						// mysql = "select * from tab_city where parent_id='00'
						// order by sequ";
					}
				}
				// sysbase
				else if (Global.DB_SYBASE == DBUtil.GetDB()) {
					if (Global.instAreaShortName.substring(0, 2).equals("js")) {
						mysql = "select city_id,city_name from tab_city where  convert(int,city_id)>0 and convert(int,city_id)<14 order by convert(int,city_id)";
					} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {
						mysql = "select * from  tab_city where convert(varchar,city_id) like '%01'";
						// mysql = "select * from tab_city where
						// convert(varchar,city_id) like '%01' order by sequ";
					} else {
						mysql = "select * from tab_city where parent_id='00'";
						// mysql = "select * from tab_city where parent_id='00'
						// order by sequ";
					}
				}
				// teledb
				else if (DBUtil.GetDB() == Global.DB_MYSQL) {
					if (Global.instAreaShortName.substring(0, 2).equals("js")) {
						mysql = "select city_id,city_name from tab_city where cast(city_id as SIGNED)>0 and cast(city_id as SIGNED)<14 order by cast(city_id as SIGNED)";
					} else if (Global.instAreaShortName.substring(0, 2).equals("sx")) {
						mysql = "select city_id,city_name, parent_id, staff_id from tab_city where CAST(city_id AS CHAR) like '%01'";
					} else {
						mysql = "select city_id,city_name, parent_id, staff_id from tab_city where parent_id='00'";
					}
				}

			}
		}
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		return mysql;

	}

	/**
	 * 根据时间（String）转秒
	 *
	 * @param dtStr
	 *            String
	 * @return 秒
	 */
	public static long getTimeMills(String dtStr) {
		String pattern = "yyyy_MM_dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		long l = 0;
		if (dtStr.equals("") || dtStr.equals("/  /")) {
			Date dt = new Date();

			return (long) dt.getTime() / 1000;
		}

		try {
			Date dt = sdf.parse(dtStr);
			l = (long) dt.getTime() / 1000;
		} catch (ParseException e) {
			pattern = "yyyy-MM-dd HH:mm";
			sdf = new SimpleDateFormat(pattern);
			try {
				Date dt = sdf.parse(dtStr);
				l = (long) dt.getTime() / 1000;
			} catch (ParseException e1) {
				/*
				 * pattern = "yyyy-MM-dd HH:mm:ss"; sdf = new
				 * SimpleDateFormat(pattern); try { Date dt = sdf.parse(dtStr);
				 * l = (long) dt.getTime() / 1000; } catch (ParseException e2) {
				 */
			}
		}
		return l;

	}

	/**
	 * 通过秒数得到最大天数
	 *
	 * @param timeMills
	 * @return
	 */
	public static int getMaxDaysOfMonth(long timeMills) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMills);
		cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * get db
	 * @return
	 * 	   <LI>0:unknown(default)</LI>
	 *     <LI>1:SYBASE</LI>
	 *     <LI>2:ORACEL</LI>
	 * @author yanhj
	 * @date 2006-11-8
	 */
	public static int getDB() {
		int tem = 0;
		int i = LipossGlobals.getLipossProperty("database.driver").indexOf(
				"sybase");
		int j = -1;
		int k = -1;
		if (i >= 0)
			tem = 1;
		else {
			j = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"oracle");
			if (j >= 0)
				tem = 2;

			k = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"mysql");
			if (k >= 0)
				tem = 3;

		}
		return tem;
	}

	/**
	 * 根据数据库驱动取得数据库的 isnull函数
	 * <UL>
	 * <LI>sybase:isnull</LI>
	 * <LI>oracle:NVL</LI>
	 * </UL>
	 *
	 * @author yanhj
	 * @date 2006-6-27
	 * @return
	 */
	public static String getDBNullFun() {
		String fun = "isnull";
		int i = LipossGlobals.getLipossProperty("database.driver").indexOf(
				"sybase");
		int j = -1;
		int k = -1;
		if (i >= 0)
			fun = "isnull";
		else {
			j = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"oracle");
			if (j >= 0)
				fun = "NVL";

			k = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"mysql");
			if (k >= 0)
				fun = "ifnull";
		}

		m_logger.debug("======getDBNullFun=" + fun);

		return fun;

	}

	/**
	 * 根据数据库驱动取得数据库的 substring函数
	 * <UL>
	 * <LI>sybase:substring</LI>
	 * <LI>oracle:substr</LI>
	 * </UL>
	 *
	 * @author yanhj
	 * @date 2006-8-9
	 * @return
	 */
	public static String getSubstrFun() {
		String fun = "substring";
		int i = LipossGlobals.getLipossProperty("database.driver").indexOf(
				"sybase");
		int j = -1;
		int k = -1;
		if (i >= 0)
			fun = "substring";
		else {
			j = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"oracle");
			if (j >= 0)
				fun = "substr";
			k = LipossGlobals.getLipossProperty("database.driver").indexOf(
					"mysql");
			if (k >= 0)
				fun = "substr";
		}

		m_logger.debug("======getDBNullFun=" + fun);

		return fun;
	}

	/**
	 * Get all process.
	 *
	 * @author Yanhj
	 * @date 2006-9-1
	 *
	 * @return String[]
	 */
	public static String[] getAllProcess() {
		String[] arr;
		String sql = "select distinct gather_id from tab_process_desc";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		arr = new String[cursor.getRecordSize()];
		Map dataMap = cursor.getNext();

		for (int i = 0; i < arr.length; i++) {
			arr[i] = (String) dataMap.get("gather_id");
		}

		return arr;
	}

	public static String ParseXMLCode(String input) {
		String output = input;
		output = output.replaceAll("<", "&lt;");
		output = output.replaceAll(">", "&gt;");
		output = output.replaceAll("#xD", "&#xD;");
		output = output.replaceAll("&&", "&");
		output = output.replaceAll("'", "&apos;");
		return output;

	}

	/**
	 * compareArray(String a[], String b[])
	 *
	 * @author Yuann
	 * @date 2006-9-21
	 *
	 */
	public static boolean compareArray(String a[], String b[]) {

		boolean flag = true;
		for (int i = 0; i < a.length; i++) {
			if (!a[i].equals(b[i])) {
				flag = false;
				break;
			}
		}

		return flag;
	}
    /**
     * 将map格式化成json字符串，便于javascript和java之间的数据传递
     * 关于json格式可通过查资料了解
     * @param map 需要格式化的map对象
     * @return 返回json格式数据字符串
     */
    public static String formateJSON(Map map){
        StringBuffer result = new StringBuffer();
        Iterator iterator = map.entrySet().iterator();
        result.append("{");
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            result.append("'").append(entry.getKey()).append("'");
            result.append(":");
            result.append("'").append(entry.getValue()).append("'");
            if(iterator.hasNext())
                result.append(",");
        }
        result.append("}");
        return result.toString();
    }
    public static void main(String[] args){
        Map map = new HashMap();
        map.put("device_id", "111");
        map.put("dadf", "222");
        map.put("hemc", "223");
        map.put("wd", "asf");
    }
}

