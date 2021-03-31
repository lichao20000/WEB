package com.linkage.litms.flux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 公用工具类
 * 
 * @author suixz(5253) tel(13512508857)
 * @since 2008-01-09
 * @version 1.0
 * @category util
 */
public class CommonUtil
{
	/**
	 * 根据出入的时间参数mills,获取它处于该参数所在一年中的第几周
	 * 
	 * @param mills
	 *            时间(毫秒)
	 * @return WEEK_OF_YEAR
	 */
	public static int getWeekOfYear(long mills)
	{
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		long asj = mills - 3600000 * 24 * 7;// 判断依据周
		cale.setTimeInMillis(mills);
		int sweek = cale.get(Calendar.WEEK_OF_YEAR);
		int mon = cale.get(Calendar.MONTH);
		if (mon == 11)
			{
				cale.setTimeInMillis(asj);
				int eweek = cale.get(Calendar.WEEK_OF_YEAR);
				if (eweek > sweek)
					{
						return eweek + 1;
					}
			}
		return sweek;
	}
	/**
	 * 根据传入的时间参数mills,获取该时间所在的年份
	 * 
	 * @param mills
	 *            时间(毫秒)
	 * @return Calendar.YEAR
	 */
	public static int getCurYearWithTime(long mills)
	{
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(mills);
		return cale.get(Calendar.YEAR);
	}
	/**
	 * 根据表明查找数据库中的所有以此表名开头的表
	 * 
	 * @param tab_name：表名的前缀
	 * @return：表名集合
	 */
	public static Map getExistTables(String tab_name)
	{
		String sql = "select name from sysobjects where name like '" + tab_name + "%'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql = "select table_name as name from user_tables where table_name like '" + StringUtil.getUpperCase(tab_name) + "%'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql = "select name from sysobjects where name like '" + tab_name + "%'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map field = cursor.getNext();
		HashMap map = new HashMap();
		while (field != null)
			{
				map.put((String) field.get("name"), (String) field.get("name"));
				field = cursor.getNext();
			}
		return map;
	}
	/**
	 * 查询tableList中数据库中存在表名，并将存在的表以列表返回
	 * @param tableList
	 * @return 存在表名List，如果都不存在，则返回长度为0的List对象
	 */
	public static List<String> getExistTables(List<String> tableList){
	    String sql = "select name from sysobjects where type='U' and name in(?)";
	    if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql = "select table_name as name from user_tables where table_name in (?)";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql = "select name from sysobjects where type='U' and name in(?)";
		}
	    PrepareSQL pSQL = new PrepareSQL();
	    pSQL.setSQL(sql);
	    if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			pSQL.setStringExt(1, StringUtil.getUpperCase(StringUtils.weave(tableList)), false);
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			pSQL.setStringExt(1, StringUtils.weave(tableList), false);
		}
		else
		{
			pSQL.setStringExt(1, StringUtils.weave(tableList), false);
		}
	    Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
	    List<String> listExistTable = new ArrayList<String>(cursor.getRecordSize()); 
	    Map field = cursor.getNext();
	    while(field != null){
		listExistTable.add((String)field.get("name"));
		field = cursor.getNext();
	    }
	    pSQL = null;
	    cursor = null;
	    return listExistTable;
	}
	/**
	 * 查询数据表是否存在,0表示不存在,1表示存在
	 * 
	 * @param tableName
	 *            表名
	 * @param jt
	 *            数据源
	 * @return 表的个数
	 */
	public static int tableIsExist(String tableName, JdbcTemplate jt)
	{
		String sql = "select count(1) from sysobjects where type='U' and name='"
				+ tableName + "'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql = "select count(1) from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql = "select count(1) from sysobjects where type='U' and name='" + tableName
					+ "'";
		}// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return jt.queryForInt(sql);
	}
	/**
	 * 单位转换(B、KB、M、G)
	 * 
	 * @param data:需要转换的数据（必须是以B为单位的数据，如果不是则必须转化为B）
	 * 
	 * @return
	 */
	public static String TransUnit(long data)
	{
		String result = "";
		double d = 0.00d;
		double t = (double) data;
		if (data < 1024)
			{
				result = data + "B";
			}
		else
			if (data < 1024 * 1024)
				{
					d = t / 1024;
					result = StringUtils.formatString(d, 2) + "KB";
				}
			else
				if (data < 1024 * 1024 * 1024)
					{
						d = t / (1024 * 1024);
						result = StringUtils.formatString(d, 2) + "M";
					}
				else
					{
						d = t / (1024 * 1024 * 1024);
						result = StringUtils.formatString(d, 2) + "G";
					}
		return result;
	}
	/**
	 * 根据开始时间、结束时间、表名称、条件拼接sql语句(以天分隔表e.g. flux_hour_stat_2008_3_8)
	 * 报表中经常使用到跨表查询，如果表不存在又没有做判断就容易出错，这个通用类就是为了解决之中的错误 注：start、end和st、et只能使用一种
	 * 
	 * @author benyp(5260) benyp@lianchuang.com
	 * @param start:开始时间:默认null
	 * @param end:结束时间：默认null
	 * @param st:开始时间：长整形，默认0
	 * @param et:结束时间：长整形，默认0
	 * @param sel:要查询的参数
	 * @param whe:查询的条件,如果条件没有就用where
	 *            1=1
	 * @param order:group
	 *            by 或者 order 的参数,如果没有则为"",如果有则先group by 后order by
	 * @param time:条件中时间参数
	 * @param tab_name:表名(以什么形式开头)
	 *            eg:sql=getSQLByDay("2008-02-01","2008-03-17","select
	 *            name,time,id from "," where 1=1 and
	 *            ip='1002'","group by name order by time","time","flux_hour_stat_");
	 * @return sql:返回的sql
	 */
	public static String getSQLByDay(String start, String end, long st, long et,
			String sel, String whe, String orderString, String time, String tab_name)
	{
		String order="",group="";
		if(orderString!=null && orderString.contains("order") && orderString.contains("group")){
			group=orderString.substring(0,orderString.indexOf("order"));
			order=orderString.substring(orderString.indexOf("order"),orderString.length());
		}else if(orderString!=null && orderString.contains("order")){
			order=orderString;
		}else if(orderString!=null && orderString.contains("group")){
			group=orderString;
		}
		Map tabMap = getExistTables(tab_name); // 获取所有存在的table名称
		DateTimeUtil sdt , edt; // 开始时间、结束时间
		boolean type = (st == 0 && et == 0) ? true : false;// 时间是否是字符串格式，true：是字符串格式，false：是长整形格式
		sdt = type ? (new DateTimeUtil(start)) : (new DateTimeUtil(st));
		edt = type ? (new DateTimeUtil(end)) : (new DateTimeUtil(et));
		String date = "";// 时间
		String sql = "";
		boolean flg = false;
		String table;// 表名
		String stab = "";// 开始时间的表
		int num = Math
				.abs(DateTimeUtil.nDaysBetweenTwoDate(sdt.getDate(), edt.getDate())) - 1;// 开始时间和结束时间之间一共有几天（去除头尾）
		// 开始时间
		stab = table = tab_name + sdt.getDate2().split(" ")[0].replaceAll("-", "_");
		if (tabMap.get(table) != null)
			{
				sql = sel + " " + table + " " + whe + " and " + time + ">="
						+ sdt.getLongTime() + " and " + time + "<=" + edt.getLongTime()
						+ group + " ";
				flg = true;
			}
		for (int i = 0; i < num; i++)
			{
				date = sdt.getNextDateTimeWithoutZero("day", 1);
				table = tab_name + date.split(" ")[0].replaceAll("-", "_");
				if (tabMap.get(table) != null)
					{
						if (flg)
							{
								sql += "union all " + sel + " " + table + " " + whe
										+ group + " ";
							}
						else
							{
								sql = sel + " " + table + " " + whe + group + " ";
								flg = true;
							}
					}
			}
		// 结束时间
		table = tab_name + edt.getDate2().split(" ")[0].replaceAll("-", "_");
		if (!table.equals(stab) && tabMap.get(table) != null)
			{
				if (flg)
					{
						sql += "union all " + sel + " " + table + " " + whe + " and "
								+ time + "<=" + edt.getLongTime() + group + " ";
					}
				else
					{
						sql = sel + " " + table + " " + whe + " and " + time + ">="
								+ sdt.getLongTime() + " and " + time + "<="
								+ edt.getLongTime() + group + " ";
					}
			}
		sql+=" "+order;
		// 清除变量
		tabMap = null;
		table = null;
		sdt = null;
		edt = null;
		stab = null;
		date = null;
		return sql;
	}
	/**
	 * 根据开始时间、结束时间、表名称、条件拼接sql语句(以月分隔表e.g. tab_servicedata_2007_1)
	 * 报表中经常使用到跨表查询，如果表不存在又没有做判断就容易出错，这个通用类就是为了解决之中的错误 注：start、end和st、et只能使用一种
	 * 
	 * @author benyp(5260) benyp@lianchuang.com
	 * @param start:开始时间:默认null
	 * @param end:结束时间：默认null
	 * @param st:开始时间：长整形，默认0
	 * @param et:结束时间：长整形，默认0
	 * @param sel:要查询的参数
	 * @param whe:查询的条件,如果条件没有就用where
	 *            1=1
	 * @param order:group
	 *            by 或者 order 的参数,如果没有则为""
	 * @param time:条件中时间参数
	 * @param tab_name:表名(以什么形式开头)
	 *            eg:sql=getSQLByDay("2008-02-01","2008-03-17","select
	 *            name,time,id from "," where 1=1 and
	 *            ip='1002'","time","flux_hour_stat_");
	 * @return sql:返回的sql
	 */
	public static String getSQLByMonth(String start, String end, long st, long et,
			String sel, String whe, String orderString, String time, String tab_name)
	{
		String order="",group="";
		if(orderString!=null && orderString.contains("order") && orderString.contains("group")){
			group=orderString.substring(0,orderString.indexOf("order"));
			order=orderString.substring(orderString.indexOf("order"),orderString.length());
		}else if(orderString!=null && orderString.contains("order")){
			order=orderString;
		}else if(orderString!=null && orderString.contains("group")){
			group=orderString;
		}
		Map tabMap = getExistTables(tab_name); // 获取所有存在的table名称
		DateTimeUtil sdt , edt; // 开始时间、结束时间
		boolean type = (st == 0 && et == 0) ? true : false;// 时间是否是字符串格式，true：是字符串格式，false：是长整形格式
		sdt = type ? (new DateTimeUtil(start)) : (new DateTimeUtil(st));
		edt = type ? (new DateTimeUtil(end)) : (new DateTimeUtil(et));
		String date = "";// 时间
		String sql = "";
		boolean flg = false;
		String table;// 表名
		int num = 0;// 开始时间和结束时间之间一共有几天（去除头尾）
		String[] tmp;// 临时
		String stab = "";// 开始时间的表
		// 获取开始时间和结束时间之间一共有几个月
		int sy = sdt.getYear();
		int ey = edt.getYear();
		int sm = sdt.getMonth();
		int em = edt.getMonth();
		if (sy == ey)
			{
				num = Math.abs(em - sm) - 1;
			}
		else
			if (sy < ey)
				{
					num = (ey - sy - 1) * 12 + (12 - sm) + em - 1;
				}
			else
				{
					num = (sy - ey - 1) * 12 + (12 - em) + sm - 1;
				}
		// 开始时间
		tmp = sdt.getDate2().split("-");
		stab = table = tab_name + (tmp[0] + "_" + tmp[1]);
		if (tabMap.get(table) != null)
			{
				sql = sel + " " + table + " " + whe + " and " + time + ">="
						+ sdt.getLongTime() + " and " + time + "<=" + edt.getLongTime()
						+ group + " ";
				flg = true;
			}
		for (int i = 0; i < num; i++)
			{
				date = sdt.getNextDateTimeWithoutZero("month", 1);
				tmp = date.split("-");
				table = tab_name + (tmp[0] + "_" + tmp[1]);
				if (tabMap.get(table) != null)
					{
						if (flg)
							{
								sql += "union all " + sel + " " + table + " " + whe
										+ group + " ";
							}
						else
							{
								sql = sel + " " + table + " " + whe + group + " ";
								flg = true;
							}
					}
			}
		// 结束时间
		tmp = edt.getDate2().split("-");
		table = tab_name + (tmp[0] + "_" + tmp[1]);
		if (!table.equals(stab) && tabMap.get(table) != null)
			{
				if (flg)
					{
						sql += "union all " + sel + " " + table + " " + whe + " and "
								+ time + "<=" + edt.getLongTime() + group + " ";
					}
				else
					{
						sql = sel + " " + table + " " + whe + " and " + time + ">="
								+ sdt.getLongTime() + " and " + time + "<="
								+ edt.getLongTime() + group + " ";
					}
			}
		sql+=" "+order;
		// 清除变量
		tabMap = null;
		table = null;
		tmp = null;
		stab = null;
		sdt = null;
		edt = null;
		date = null;
		return sql;
	}
}
