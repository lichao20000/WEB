/*
 * Created on 2004-4-1
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.liposs.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;

/**
 * @author 俞海腾
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbUtil
{
	private static String dbtype = null;
	public static String dateFormat(String strdate)
	{
		String result = null;
		if (dbtype == null) getDbType();
		if (dbtype.indexOf("oracle") != -1) {
			result = "to_date('" + strdate + "','YYYY-MM-DD')";
		}
		else if (dbtype.indexOf("postgres") != -1) {
			result = "timestamp'" + strdate + "'";
		}
		else if (dbtype.indexOf("sybase") != -1) {
			result = "'" + strdate + "'";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "str_to_date('" + strdate + "', '%Y-%m-%d')";
		}
		return result;
	}
	public static String dateFormat(String strdate, int day)
	{
		String result = null;
		if (dbtype == null) getDbType();
		if (dbtype.indexOf("oracle") != -1) {
			result = "to_date('" + strdate + "','YYYY-MM-DD') + " + day;
		}
		else if (dbtype.indexOf("postgres") != -1) {
			result = "timestamp'" + strdate + "' + timespan('" + day + " day')";
		}
		else if (dbtype.indexOf("sybase") != -1) {
			result = "dateadd(dd," + day + ",'" + strdate + "')";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "str_to_date('" + strdate + "', '%Y-%m-%d') + " + day;
		}
		return result;
	}
	public static String getDbType()
	{
		dbtype = LipossGlobals.getLipossProperty("database.serverURL").toLowerCase();
		return dbtype;
	}
	public static String dbLimitFormat(int row, String sortfield)
	{
		String result = null;
		if (dbtype == null) getDbType();
		if (dbtype.indexOf("oracle") != -1)
		{
			result = " and rownum<=" + row + " order by " + sortfield;
		}
		else if (dbtype.indexOf("postgres") != -1)
		{
			result = "order by " + sortfield + " limit " + row;
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "order by " + sortfield + " limit " + row;
		}
		return result;
	}
	/**
	 * 通过传入的时间点，返回所有的表名的列表
	 * 
	 * @param startTime
	 *            起始时间点的毫秒数
	 * @param endTime
	 *            结束时间点的毫秒数
	 * @param unitType
	 *            单位类型，请使用Calendar类中的单位常量，否则会出错
	 * @param stepSize
	 *            递增的步长
	 * @param prefix
	 *            表明的前缀
	 * @param pattern
	 *            格式化时间的模板
	 * @return
	 */
	static public List<String> createTableNames(long startTime, long endTime,
			int unitType, int stepSize, String prefix, String pattern)
	{
		if (startTime > endTime)
			{
				throw new UnsupportedOperationException("起始时间大于结束时间");
			}
		List<String> tns = new ArrayList<String>();
		Calendar sc = Calendar.getInstance();
		sc.setTimeInMillis(startTime);
		Calendar ec = Calendar.getInstance();
		ec.setTimeInMillis(endTime);
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		if (unitType == Calendar.YEAR)
			{
				int sy = sc.get(Calendar.YEAR);
				int ey = ec.get(Calendar.YEAR);
				do
					{
						tns.add(prefix + f.format(sc.getTime()));
						sc.add(Calendar.YEAR, stepSize);
						sy = sc.get(Calendar.YEAR);
					} while (sy <= ey);
			}
		else if (unitType == Calendar.MONTH)
			{
				int sy = sc.get(Calendar.YEAR);
				int ey = ec.get(Calendar.YEAR);
				int sm = sc.get(Calendar.MONTH);
				int em = ec.get(Calendar.MONTH);
				do
					{
						tns.add(prefix + f.format(sc.getTime()));
						sc.add(Calendar.MONTH, stepSize);
						sy = sc.get(Calendar.YEAR);
						sm = sc.get(Calendar.MONTH);
					} while ((sy < ey) || (sy == ey && sm <= em));
			}
		else if (unitType == Calendar.WEEK_OF_YEAR)
			{
			}
		else if (unitType == Calendar.DAY_OF_MONTH)
			{
				int sy = sc.get(Calendar.YEAR);
				int ey = ec.get(Calendar.YEAR);
				int sm = sc.get(Calendar.MONTH);
				int em = ec.get(Calendar.MONTH);
				int sd = sc.get(Calendar.DAY_OF_MONTH);
				int ed = ec.get(Calendar.DAY_OF_MONTH);
				do
					{
						tns.add(prefix + f.format(sc.getTime()));
						sc.add(Calendar.DAY_OF_MONTH, stepSize);
						sy = sc.get(Calendar.YEAR);
						sm = sc.get(Calendar.MONTH);
						sd = sc.get(Calendar.DAY_OF_MONTH);
					} while ((sy < ey) || ((sy == ey) && (sm < em))
						|| ((sy == ey) && (sm < em) && (sd <= ed)));
			}
		else
			{
				throw new UnsupportedOperationException(
						"你传入的时间类型不受支持，只支持：Calendar.YEAR，Calendar.MONTH，Calendar.WEEK_OF_YEAR，Calendar.DAY_OF_MONTH");
			}
		return tns;
	}
	/**
	 * 通过传入的时间点，返回所有的表名的列表
	 * 
	 * @param startDate
	 *            起始时间点的日期
	 * @param endDate
	 *            结束时间点的毫日期
	 * @param unitType
	 *            单位类型，请使用Calendar类中的单位常量，否则会出错
	 * @param stopSize
	 *            递增的步长
	 * @param prefix
	 *            表名的前缀
	 * @param pattern
	 *            格式化时间的模板
	 * @return
	 */
	static public List<String> createTableNames(Date startDate, Date endDate,
			int unitType, int stopSize, String prefix, String pattern)
	{
		return createTableNames(startDate.getTime(), endDate.getTime(), unitType,
				stopSize, prefix, pattern);
	}
}
