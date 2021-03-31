package com.linkage.litms.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

public class SysManage {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SysManage.class);
	String acc_oid = "";
	List<String> list = new ArrayList<String>();
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	
	public int tabOperation(HttpServletRequest request){
		
		String _sql = "";
		String acc_loginname = request.getParameter("acc_loginname");
		
		String[] tab_id = request.getParameterValues("tab_id");
		logger.debug("tab_id :" + tab_id.length);
		Map _tabMap = tabMap();
		
		ArrayList sqlList = new ArrayList();
		// 取得当前时间
		Date date = new Date();
		long nowtime = date.getTime();
		String _name = StringUtils.formatDate("yyyyMMddHHmmss",nowtime/1000);
//		logger.debug("_name :" + _name);
		long backup_id = DataSetBean.getMaxId("tab_db_backup","backup_id");	
		
		for(int i=0;i<tab_id.length;i++){
			_sql = "insert into tab_db_backup(backup_id,tab_name,tab_name_bk,execute_time,complet_time,exec_status,acc_loginname)"
					+ " values(" + backup_id
					+ ",'" + _tabMap.get(tab_id[i])+ "'"
					+ ",'" + _tabMap.get(tab_id[i])+ "_" +_name + "'"
					+ ",null,null,0"
					+ ",'" + acc_loginname + "')";
			PrepareSQL psql = new PrepareSQL(_sql);
	        psql.getSQL();
			sqlList.add(_sql);
		}
		
		if(sqlList != null && sqlList.size() > 0){			
			int code[] = DataSetBean.doBatch(sqlList);
			if(code != null && code.length > 0){
				return Integer.parseInt(String.valueOf(backup_id));
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	
	
	public Map tabMap(){
		
		Map tab_Map = new HashMap();
		String _sql = "select * from tab_table_sys where 1=1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			_sql = "select tab_id, tab_name from tab_table_sys where 1=1 ";
		}
		PrepareSQL psql = new PrepareSQL(_sql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(_sql);
		Map fields = cursor.getNext();
		if(fields != null){
			while (fields != null){
				tab_Map.put((String)fields.get("tab_id"),(String)fields.get("tab_name"));
				logger.debug("tab_Map :" + tab_Map);
				fields = cursor.getNext();
			}			
		}
		return tab_Map;		
	}
	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public String gettableHtml(HttpServletRequest request) {

		String tab_name = request.getParameter("table_name");

		String tmpSql = "select * from tab_db_backup where tab_name='" + tab_name + "' order by execute_time desc";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select tab_name_bk from tab_db_backup where tab_name='" + tab_name + "' order by execute_time desc";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null) {
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>没有历史备份记录</span></td></tr>";
		} else {
			int iflag = 1;
			while (fields != null) {
				if (iflag % 2 == 0) {
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"radio\"  id=\"tab_name_bk\" name=\"tab_name_bk\" value=\""
							+ (String) fields.get("tab_name_bk") + "\">";
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("tab_name_bk");
					serviceHtml += "</td></tr>";
				} else {
					serviceHtml += "<tr>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%'>";
					serviceHtml += "<input type=\"radio\"  id=\"tab_name_bk\" name=\"tab_name_bk\" value=\""
							+ (String) fields.get("tab_name_bk") + "\">";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("tab_name_bk");
					serviceHtml += "</td></tr>";
				}
				iflag++;
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;

	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	public int tabRecover(HttpServletRequest request){
//		String _sql = "";
		String acc_loginname = request.getParameter("acc_loginname");
		
		String tab_name = request.getParameter("tab_name");
		
		String tab_name_bk = request.getParameter("tab_name_bk");
		
		
		long recover_id = DataSetBean.getMaxId("tab_db_recover","recover_id");	
		
		String _sql = "insert into tab_db_recover(recover_id,tab_name,tab_name_bk,execute_time,complet_time,exec_status,acc_loginname)"
				+ " values(" + recover_id
				+ ",'" + tab_name + "'"
				+ ",'" + tab_name_bk + "'"
				+ ",null,null,0"
				+ ",'" + acc_loginname + "')";		
		PrepareSQL psql = new PrepareSQL(_sql);
        psql.getSQL();
		int code = DataSetBean.executeUpdate(_sql);
		if(code > 0){
			return Integer.parseInt(String.valueOf(recover_id));
		} else {
			return -1;
		}

	}
	
	/**
	 * 递归获取创建者 add by gongsj
	 * @param cur_creator
	 * @return Creator's List
	 */
	public List getAllCreators(String cur_creator) {
		//list.add(cur_creator);
		String _sql = "select acc_oid from tab_accounts where creator = " + cur_creator;
		//logger.debug("GSJ---------------getAllCreators:" + _sql);
		Cursor cursor = DataSetBean.getCursor(new PrepareSQL(_sql).getSQL());
		Map tab_Map = cursor.getNext();
		
		if(tab_Map != null){
			while (tab_Map != null){
				acc_oid = tab_Map.get("acc_oid").toString();
				list.add(acc_oid);
				getAllCreators(acc_oid);
				tab_Map = cursor.getNext();
			}
			
		} else {
			return list;
		}
	
		return list;
		
	}
//	public static void main(String[] args) {
//		List list = new SysManage().getAllCreators("8");
//		logger.debug("GSJ---------------list:" + list);
//	}
}


















