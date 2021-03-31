/*
 * Created on 2004-4-1
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.common.util;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author yuht
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbUtil {
	private static Logger logger = LoggerFactory.getLogger(DbUtil.class);

	private static String dbtype = null;

	public static String dateFormat(String strdate){
		String result = null;
		if(dbtype == null)
			getDbType();
		if(dbtype.indexOf("oracle")!=-1){
			result = "to_date('"+ strdate +"','YYYY-MM-DD')";
		}
		else if(dbtype.indexOf("postgres")!=-1){
			result = "timestamp'"+ strdate +"'";
		}
		else if(dbtype.indexOf("sybase")!=-1){
			result = "'"+ strdate +"'";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "str_to_date('" + strdate + "', '%Y-%m-%d')";
		}
		return result;
	}

	public static String dateFormat(String strdate,int day){
		String result = null;
		if(dbtype == null)
			getDbType();
		if(dbtype.indexOf("oracle")!=-1){
			result = "to_date('"+ strdate +"','YYYY-MM-DD') + "+ day;
		}
		else if(dbtype.indexOf("postgres")!=-1){
			result = "timestamp'"+ strdate +"' + timespan('"+ day +" day')";
		}
		else if(dbtype.indexOf("sybase")!=-1){
			result = "dateadd(dd,"+ day +",'"+ strdate +"')";
		}
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "str_to_date('" + strdate + "', '%Y-%m-%d') + " + day;
		}
		return result;
	}
	

	public static String getDbType(){
		dbtype = LipossGlobals.getLipossProperty("database.serverURL").toLowerCase();
		return dbtype;
	}

	public static String dbLimitFormat(int row, String sortfield){
		String result = null;
		if(dbtype == null)
			getDbType();
		if(dbtype.indexOf("oracle")!=-1){
			result = " and rownum<="+ row +" order by "+ sortfield;
		}
		else if(dbtype.indexOf("postgres")!=-1){
			result = "order by "+ sortfield +" limit "+ row;
		}
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			result = "order by "+ sortfield +" limit "+ row;
		}
		return result;
	}
	
	/**
	 * 获得连接标识符
	 * <br>
	 * Oracle为 ||<br>
	 * Sysbase为 +
	 * 
	 * @return String
	 * @author ZhangCong
	 */
	public static String getContactIdentifier()
	{
		if(LipossGlobals.isOracle())
		{
			return "||";
		}else
		{
			return "+";
		}
	}
	
	/**
	 * 
	 * 获得空值替换函数
	 * <br>
	 * Oracle为 nvl(expression,value)<br>
	 * Sysbase为  isnull(expression,value)
	 * 
	 * @param expression 表达式或者字段
	 * @param value 替换的内容
	 * @return String
	 * @author ZhangCong
	 */
	public static String getNullFunction(String expression,String value)
	{
		// oracle
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			return "nvl(" + expression + "," + value + ")";
		}
		// sysbase
		else if (Global.DB_SYBASE == DBUtil.GetDB()) {
			return "isnull(" + expression + "," + value + ")";
		}
		// teledb
		else {
			return "ifnull(" + expression + "," + value + ")";
		}
	}

	/**
	 * <pre>
	 * 针对Sybase数据的分页查询进行优化
	 * 在SuperDAO.querySP()方法中分页的基本思想是：在数据库将所有满足条件的结果集都查询出来，
	 * 然后再SuperDAO中将满足条件的行数提取出来，其他的丢弃。
	 * 这种方法的一个缺点就是数据库需要处理所有满足条件的结果集。而Sybase数据库中使用top的话，
	 * 当满足top的数量条件后，数据库停止查询，立即返回，效率会很快。该方法就是针对这种情况进行优化
	 * </pre>
	 * 
	 * @see SuperDAO#querySP(String, int, int)
	 * @param sql
	 * @param topNum
	 * @return
	 */
	public static String sybaseSqlTop(String sql, int topNum)
	{
		if (LipossGlobals.isOracle() || DBUtil.GetDB() == Global.DB_MYSQL)
		{
			// oracle database not supported top
			return sql;
		}
		StringBuffer result = new StringBuffer();
		String lowderSql = sql.toLowerCase();
		if (lowderSql.contains("distinct") || lowderSql.contains(" top "))
		{
			return sql;
		}
		// SQL exists "select " or "SELECT " or some other mix char
		int index = lowderSql.indexOf("select");
		if (index > 0)
		{
			result.append(sql.substring(0, index));
		}
		result.append("select top ").append(topNum)
				.append(sql.substring(index + "select".length()));
		String topSql = result.toString();
		logger.info("sybase top sql [{}]", topSql);
		return topSql;
	}
}
