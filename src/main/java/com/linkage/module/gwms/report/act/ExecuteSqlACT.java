package com.linkage.module.gwms.report.act;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.linkage.module.gwms.report.bio.ExecuteSqlBIO;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExecuteSqlACT {

	private String querySql;
	
	private String updateSql;
	
	private int[] updateResArr;
	
	private List queryRetList;
	
	private String ajax;
	
	ExecuteSqlBIO sqlBio;
	/**
	 * 
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return String
	 */
	public String execute(){
		return "success";
	}
	
	public String query(){
		ajax = sqlBio.getHtmlQueryData(querySql);
		return "ajax";
	}
	
	public String batchUpdate(){
		ajax = sqlBio.getHtmlUpdateData(updateSql);
		return "ajax";
	}

	
	/*** getter, setter method**/
	public int[] getUpdateResArr() {
		return updateResArr;
	}

	public List getQueryRetList() {
		return queryRetList;
	}

	public void setQuerySql(String querySql) {
		try {
			this.querySql = new String(querySql.getBytes("gbk"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setUpdateSql(String updateSql) {
		try {
			this.updateSql = new String(updateSql.getBytes("gbk"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setSqlBio(ExecuteSqlBIO sqlBio) {
		this.sqlBio = sqlBio;
	}

}
