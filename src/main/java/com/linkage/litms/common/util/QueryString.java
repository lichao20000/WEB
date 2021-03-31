/*
 * 创建日期 2005-1-13
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.linkage.litms.common.util;

import java.net.URLEncoder;

/**
 * @author dolphin
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class QueryString {
	private String query;
	
	public QueryString(Object name, Object value){
		query = URLEncoder.encode(name.toString()) + "=" +
				URLEncoder.encode(value.toString());
	}
	
	public QueryString(){
		query = "";
	}
	
	public synchronized void add(Object name, Object value){
		if(!query.trim().equals("")) query += "&";
		query = URLEncoder.encode(name.toString()) + "=" +
				URLEncoder.encode(value.toString());	
	}
	
	public String getQuery(){
		return query;
	}
}
