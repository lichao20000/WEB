package com.linkage.liposs.buss.util;

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
public class CommonUtil {
	/**
	 * 根据出入的时间参数mills,获取它处于该参数所在一年中的第几周
	 * 
	 * @param mills
	 *            时间(毫秒)
	 * @return WEEK_OF_YEAR
	 */
	public static int getWeekOfYear(long mills) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		long asj = mills - 3600000 * 24 * 7;// 判断依据周
		cale.setTimeInMillis(mills);
		int sweek = cale.get(Calendar.WEEK_OF_YEAR);
		int mon = cale.get(Calendar.MONTH);
		if (mon == 11) {
			cale.setTimeInMillis(asj);
			int eweek = cale.get(Calendar.WEEK_OF_YEAR);
			if (eweek > sweek) {
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
	public static int getCurYearWithTime(long mills) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(mills);
		return cale.get(Calendar.YEAR);
	}
	
	/**
	 * 根据表明查找数据库中的所有以此表名开头的表
	 * @param tab_name：表名的前缀
	 * @return：表名集合
	 */
	public static Map getExistTables(String tab_name)
	{
		String sql = "select name from sysobjects where name like '" + tab_name + "%'";;
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql = "select table_name as name from user_tables where table_name like '" + StringUtil.getUpperCase(tab_name) + "%'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql = "select name from sysobjects where name like '" + tab_name + "%'";
		}
		
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
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
	public static int tableIsExist(String tableName, JdbcTemplate jt) {
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
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}
}
