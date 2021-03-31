package com.linkage.module.gwms.report.bio;

import java.util.Iterator;
import java.util.List;

import com.linkage.module.gwms.report.dao.ExecuteSqlDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExecuteSqlBIO {

	ExecuteSqlDAO sqlDao;
	
	
	/**
	 * 返回查询结果HTML的一个<table>
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-7
	 * @return String
	 */
	public String getHtmlQueryData(String strSQL){
		StringBuffer htmlBuf = new StringBuffer();
		List tlist = queryData(strSQL);
		htmlBuf.append("<table border='1'>");
		if(null == tlist || 0 == tlist.size()){
			
			return "";
		}else{
			Iterator itor = tlist.iterator();
			while(itor.hasNext()){
				String[] recordArr = (String[])itor.next();
				int i = 0;
				int arrLen = recordArr.length;
				htmlBuf.append("<tr>");
				while(i < arrLen){
					htmlBuf.append("<td>" + recordArr[i++] + "</td>");
				}
				htmlBuf.append("</tr>");
			}
		}
		htmlBuf.append("</table>");
		return htmlBuf.toString();
	}
	
	
	/**
	 * 查询数据
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return List
	 */
	public List queryData(String strSQL){
		List rList = null;
		rList = sqlDao.queryData(strSQL);
		return rList;
	}
	
	
	/**
	 * 返回查询结果HTML的一个<table>
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-7
	 * @return String
	 */
	public String getHtmlUpdateData(String strSQL){
		StringBuffer htmlBuf = new StringBuffer();
		int[] iup = updateData(strSQL);
		htmlBuf.append("<table border='1'>");
		if(null == iup || 0 == iup.length){
			
			return "";
		}else{
			int len = iup.length;
			htmlBuf.append("<tr>");
			for(int i = 0; i < len; i++){
				htmlBuf.append("<td>" + iup[i] + "</td>");
			}
			htmlBuf.append("</tr>");
		}
		htmlBuf.append("</table>");
		return htmlBuf.toString();
	}
	
	
	/**
	 * 更新数据
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return int
	 */
	public int[] updateData(String strSQL){
		String[] sqlArr = strToArr(strSQL);
		int[] iret = sqlDao.updateData(sqlArr);
		return iret;
	}
	
	
	/**
	 * 
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-6
	 * @return List
	 */
	public String[] strToArr(String strSql){
		String[] strArr = null;
		if(false == StringUtil.IsEmpty(strSql)){
			strArr = strSql.split("\\;");
		}
		
		return strArr;
	}
	
	
	
	/** DAO */
	public void setSqlDao(ExecuteSqlDAO sqlDao) {
		this.sqlDao = sqlDao;
	}
	
}
