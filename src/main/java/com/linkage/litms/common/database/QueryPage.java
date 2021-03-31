/*
 * @(#)QueryPage.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.database;

import java.util.HashMap;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 封装数据库分页。
 * 
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */
public class QueryPage {
	
	
	private int offset;

	private int MaxLine;

	private int total;

	private int curPage;

	private int totalPage;

	private String query;

	/**
	 * 设置数据分页条件
	 * 
	 * @param query
	 *            获取数据SQL语句
	 * @param start
	 *            每页数据起始点
	 * @param len
	 *            每页数据条数
	 */
	public void initPage(String query, int start, int len) {
		offset = start;
		MaxLine = len;
		this.query = query;
		total = getTotal();
		totalPage = (int) Math.ceil((double) total / MaxLine);
		curPage = (int) Math.floor((double) offset / MaxLine + 1);
	}
	public void initPage1(String query, int start, int len,String dataSource) {
		offset = start;
		MaxLine = len;
		this.query = query;
		total = getTotal1(dataSource);
		totalPage = (int) Math.ceil((double) total / MaxLine);
		curPage = (int) Math.floor((double) offset / MaxLine + 1);
	}

	private int getTotal1(String dataSource) {
		String query_pos;
//		String countSTR = "*";
		int begin, end, begin1;
		if (query.toUpperCase().indexOf("UNION ALL") == -1 && query.toUpperCase().indexOf(" DISTINCT ") == -1) {
			begin = query.toUpperCase().indexOf(" FROM ");
			end = query.toUpperCase().indexOf(" GROUP ");
			if (end == -1)
				end = query.toUpperCase().indexOf("ORDER");
			if (end == -1)
				end = query.length();
			
			query_pos = query.substring(begin, end).trim();
			String strSQL = "select count(1) As total "
					+ query_pos;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select count(*) As total "
						+ query_pos;
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			HashMap fields = DataSetBean.getRecord(strSQL,dataSource);
			if (fields != null) {
				return (Integer.parseInt((String) fields.get("total")));
			} else
				return 0;
		} else {
			Cursor cursor = DataSetBean.getCursor(this.query);
			return (cursor.getRecordSize());
			
		}
	}
	private int getTotal() {
		String query_pos;
//		String countSTR = "*";
		int begin, end, begin1;
		if (query.toUpperCase().indexOf("UNION ALL") == -1 && query.toUpperCase().indexOf(" DISTINCT ") == -1) {
			begin = query.toUpperCase().indexOf(" FROM ");
			end = query.toUpperCase().indexOf(" GROUP ");
			if (end == -1)
				end = query.toUpperCase().indexOf("ORDER");
			if (end == -1)
				end = query.length();

			query_pos = query.substring(begin, end).trim();
//			begin1 = query.toUpperCase().indexOf(" DISTINCT ");
//			if (begin1 != -1)
//				countSTR = query.substring(begin1, begin);

//			String strSQL = "select count(" + countSTR + ") As total "
			String strSQL = "select count(1) As total "
					+ query_pos;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select count(*) As total "
						+ query_pos;
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			HashMap fields = DataSetBean.getRecord(strSQL);
			if (fields != null) {
				return (Integer.parseInt((String) fields.get("total")));
			} else
				return 0;
		} else {
			Cursor cursor = DataSetBean.getCursor(this.query);
			return (cursor.getRecordSize());

		}
	}

	/**
	 * 生成分页导航栏
	 * 
	 * @return 返回导航栏HTML代码
	 */
	public String getPageBar() {
		String strHTML = "";
		String strColor = "#535353";
		int first, next, prev, last;

		first = 1;
		next = offset + MaxLine;
		prev = offset - MaxLine;
		last = (totalPage - 1) * MaxLine + 1;

		if (offset > MaxLine)
			strHTML += "<A HREF=?offset=" + first + ">首页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";

		if (prev > 0)
			strHTML += "<A HREF=?offset=" + prev + ">前页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";

		if (next <= total)
			strHTML += "<A HREF=?offset=" + next + ">后页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";

		if (totalPage != 0 && curPage < totalPage)
			strHTML += "<A HREF=?offset=" + last + ">尾页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";

		strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
		strHTML += MaxLine + "</b>条/页 共<b>" + total + "</b>条";

		return strHTML;
	}

	/**
	 * 生成分页导航栏
	 * 
	 * @return 返回导航栏HTML代码
	 */
	public String getGoPageBar() {
		String strHTML = "";
		String strColor = "#535353";
		int first, next, prev, last;

		first = 1;
		next = offset + MaxLine;
		prev = offset - MaxLine;
		last = (totalPage - 1) * MaxLine + 1;

		if (offset > MaxLine)
			strHTML += "<A HREF='#' onclick=goPage('" + first + "')>首页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";

		if (prev > 0)
			strHTML += "<A HREF='#' onclick=goPage('" + prev + "')>前页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";

		if (next <= total)
			strHTML += "<A HREF='#' onclick=goPage('" + next + "')>后页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";

		if (totalPage != 0 && curPage < totalPage)
			strHTML += "<A HREF='#' onclick=goPage('" + last + "')>尾页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";

		strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
		strHTML += MaxLine + "</b>条/页 共<b>" + total + "</b>条";

		return strHTML;
	}
	/**
	 * 生成分页导航栏
	 * 
	 * @param search
	 *            获取数据的查询条件
	 * @return 返回导航栏HTML代码
	 */
	public String getPageBar(String search) {
		String strHTML = "";
		String strColor = "#535353";
		int first, next, prev, last;

		first = 1;
		next = offset + MaxLine;
		prev = offset - MaxLine;
		last = (totalPage - 1) * MaxLine + 1;
		
		int curPageTmp = (int) Math.floor((double) offset / MaxLine + 1);
		String searchTemp = StringUtil.getStringValue(search);
		
		if (offset > MaxLine){
			if(Global.JLDX.equals(Global.instAreaShortName))
			{
				if(searchTemp.contains("isBind"))
				{
					search = searchTemp + "&sqlid=1";
				}
				else{
					search = searchTemp + "&isBind=null&sqlid=1";
				}
			}
			strHTML += "<A HREF=\"?offset=" + first + search
					+ "\">首页</A>&nbsp;";
		}
		else{
			strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";
		}
		if (prev > 0){
			if(Global.JLDX.equals(Global.instAreaShortName))
			{
				if(searchTemp.contains("isBind"))
				{
					search = searchTemp + "&sqlid=" + (curPageTmp-1);
				}
				else{
					search = searchTemp + "&isBind=null&sqlid=" + (curPageTmp-1);
				}
			}
			strHTML += "<A HREF=\"?offset=" + prev + search + "\">前页</A>&nbsp;";
		}
		else{
			strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";
		}
		if (next <= total){
			if(Global.JLDX.equals(Global.instAreaShortName))
			{
				if(searchTemp.contains("isBind"))
				{
					search = searchTemp + "&sqlid=" + (curPageTmp+1);
				}
				else{
					search = searchTemp + "&isBind=null&sqlid=" + (curPageTmp+1);
				}
			}
			strHTML += "<A HREF=\"?offset=" + next + search + "\">后页</A>&nbsp;";
		}
		else{
			strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";
		}
		if (totalPage != 0 && curPage < totalPage){
			if(Global.JLDX.equals(Global.instAreaShortName))
			{
				if(searchTemp.contains("isBind"))
				{
					search = searchTemp + "&sqlid=" + ((int) Math.ceil((double) last / MaxLine));
				}
				else{
					search = searchTemp + "&isBind=null&sqlid=" + ((int) Math.ceil((double) last / MaxLine));
				}
			}
			strHTML += "<A HREF=\"?offset=" + last + search + "\">尾页</A>&nbsp;";
		}
		else{
			strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";
		}
		
		strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
		strHTML += MaxLine + "</b>条/页 共<b>" + total + "</b>条";

		return strHTML;
	}
}
