/*
 * @(#)PrepareSQL.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.database;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 预处理SQL语句类似于PreparedStatement，它的实例一般由DataSetBean创建。
 * 
 * @author yuht
 * @version 1.00, 1/5/2006
 * @see DataSetBean
 * @since Liposs 2.1
 */
public class PrepareSQL {
	private StringBuffer sql;

	private String[] arr;

	private HashMap map;

	/**
	 * 构造函数
	 * 
	 */
	public PrepareSQL() {
		map = new HashMap();
		sql = new StringBuffer();
	}

	/**
	 * 构造函数，初试化SQL语句
	 * 
	 * @param sql
	 *            带?参数SQL语句
	 */
	public PrepareSQL(String sql) {
		this.sql = new StringBuffer(sql + " ");
		analyseSQL();
		map = new HashMap();
	}

	/**
	 * 设置SQL语句
	 * 
	 * @param sql
	 *            带?参数SQL语句
	 */
	public void setSQL(String sql) {
		if (map != null)
			map.clear();
		this.sql = new StringBuffer(sql + " ");
		analyseSQL();
	}

	/**
	 * 分析带?参数SQL语句。
	 * 
	 */
	private void analyseSQL() {
		String re = "[?]";
		arr = Pattern.compile(re).split(sql);
	}

	/**
	 * 设置指定参数为java String 值，一般对应数据库类型是VARCHAR，LONGVARCHAR
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 String 类型
	 */
	public void setString(int parameterIndex, String x) {
		setStringExt(parameterIndex, x, true);
	}

	/**
	 * 设置指定参数为java String 值，用于SQL语句中的in(?)
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 带","的字符串
	 * @param flag
	 *            对应数据库字段是否是字符类型，true为字符类型，false反之
	 */
	public void setStringExt(int parameterIndex, String x, boolean flag) {
		if(true == flag && null != x){
			//对单引号进行处理
			x = x.replaceAll("'", "''");
			map.put((new Integer(parameterIndex)).toString(), "'" + x + "'");
		}else{
			map.put((new Integer(parameterIndex)).toString(), x);
		}
	}

	/**
	 * 设置指定参数为java int 值，一般对应数据库类型是INTEGER
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 int 类型
	 */
	public void setInt(int parameterIndex, int x) {
		map.put((new Integer(parameterIndex)).toString(), (new Integer(x))
				.toString());
	}

	/**
	 * 设置指定参数为java double 值，一般对应数据库类型是DOUBLE
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 double 类型
	 */
	public void setDouble(int parameterIndex, double x) {
		map.put((new Integer(parameterIndex)).toString(), (new Double(x))
				.toString());
	}

	/**
	 * 设置指定参数为java float 值，一般对应数据库类型是FLOAT
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 float 类型
	 */
	public void setFloat(int parameterIndex, float x) {
		map.put((new Integer(parameterIndex)).toString(), (new Float(x))
				.toString());
	}

	/**
	 * 设置指定参数为java long 值，一般对应数据库类型是BIGINT
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 long 类型
	 */
	public void setLong(int parameterIndex, long x) {
		map.put((new Integer(parameterIndex)).toString(), (new Long(x))
				.toString());
	}

	/**
	 * 设置指定参数为java Date 对象，一般对应数据库类型是DATE
	 * 
	 * @param parameterIndex
	 *            参数索引 第一个参数是1，第二个是2
	 * @param x
	 *            参数值 Date 类型
	 */
	public void setDate(int parameterIndex, Date x) {
		DateTimeUtil dtUtil = new DateTimeUtil(x);
		String s = dtUtil.getLongDate();
		setString(parameterIndex, s);
	}

	/**
	 * 通过分析SQL语句，并把相应的参数值注入语句中
	 * 
	 * @return 返回不带?的SQL语句
	 */
	public String getSQL() {
		//analyseSQL();
		sql.delete(0, sql.length());
		int ln;
		for (int i = 0; i < arr.length; i++) {
			sql.append(arr[i]);
			ln = i + 1;
			if (ln < arr.length)
				sql.append(map.get((new Integer(ln)).toString()));
		}
		return sql.toString();
	}
	
	/**
	 * 返回sql语句
	 * 
	 *@author Jason(3412)
	 *@date 2009-05-07
	 *@return String
	 */
	@Override
	public String toString(){
		return getSQL();
	}
	
	/**
	 * 在sql语句后面加and 查询字段为数字型
	 * 
	 * @param 
	 * 		queryFiled:查询列名
	 * 		queryCondition:[=|like|>|>=|<|<=],调用该类的静态方法
	 * 		queryExp:匹配字符串
	 * @author Jason(3412)
	 * @date 2009-5-7
	 * @return void
	 */
	public void appendAndNumber(String queryFiled, String queryCondition, String queryExp){
		this.append(AND, queryFiled, queryCondition, queryExp, true);
	}
	
	/**
	 * 在sql语句后面加and 查询字段为字符型
	 * 
	 * @param 
	 * 		queryFiled:查询列名
	 * 		queryCondition:[=|like|>|>=|<|<=],调用该类的静态方法
	 * 		queryExp:匹配字符串
	 * @author Jason(3412)
	 * @date 2009-5-7
	 * @return void
	 */
	public void appendAndString(String queryFiled, String queryCondition, String queryExp){
		this.append(AND, queryFiled, queryCondition, queryExp, false);
	}
	
	/**
	 * 在sql语句后面加条件
	 * 
	 * @param 如：
	 * 		linkCondition:连接条件[and|or],调用该类的静态方法
	 * 		queryFiled:查询列名
	 * 		queryCondition:[=|like|>|>=|<|<=],调用该类的静态方法
	 * 		queryExp:匹配字符串
	 * 		needQuotMark:是否要加单引号
	 * @author Jason(3412)
	 * @date 2009-5-7
	 * @return void
	 */
	public void append(String linkCondition, String queryFiled, String queryCondition, String queryExp, boolean needQuotMark){
		sql.append(linkCondition);
		sql.append("(");
		sql.append(queryFiled);
		sql.append(queryCondition);
		if(LIKE.equals(queryCondition)){
			queryExp = "%" + queryExp + "%";
			queryExp = queryExp.replaceAll("'", "''");
			sql.append(this.getStrQuery(queryExp));
		}else{
			if(false == needQuotMark){
				queryExp = queryExp.replaceAll("'", "''");
				sql.append(this.getStrQuery(queryExp));
			}else{
				sql.append(queryExp);
			}
		}
		sql.append(")");
	}
	
	/**
	 * 追加一个查询条件或其他字符串，字符串中不带有表示查询条件的'?'
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-7-28
	 * @return void
	 */
	public void append(String sqlStat){
		sql.append(sqlStat);
		analyseSQL();
	}
	
	/**
	 * 字符串类型的查询，增加单引号
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-5-7
	 * @return String
	 */
	public String getStrQuery(String queryExp){
		return "'" + queryExp + "'";
	}
	
	/**
	 * 查询条件
	 */
	public static final String EQUEAL = "=";
	public static final String BIGGER = ">";
	public static final String BIGGEREQUEAL = ">=";
	public static final String SMALLER = "<";
	public static final String SMALLERQUEAL = "=";
	public static final String LIKE = " like ";
	
	/**
	 * 连接条件
	 */
	public static final String AND = " and ";
	public static final String OR = " or ";
	
	/*
	 * public static void main(String[] args){ String s = "select * from
	 * tab_accounts where acc_loginname=?"; //String[] arr; //String re = "[?]";

	 * PrepareSQL test = new PrepareSQL(s); test.setString(1,"admin");


	 */
}
