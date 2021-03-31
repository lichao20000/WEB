package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class SheetExeRuleAct {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SheetExeRuleAct.class);
	private String m_SheetRules_List_SQL = "select * FROM tab_sche_plan ";
	
//	private String m_Vendor_SQL = "select vendor_id,vendor_name from tab_vendor";
//	
//	private String m_DevModel_SQL = "select oui,device_model,os_version from tab_devicetype_info";
	
	/**
	 * 新增计划任务信息
	 */
	private String m_SheetRulesAdd_SQL = "insert into tab_sche_plan (task_id,acc_loginname,execute_time,active,"
			+ "plan_desc) values (?,?,?,?,?)";


	private Cursor cursor = null;

//	private String str_userid = null;

	private PrepareSQL pSQL;

	private String str_sheet_id = null;

	public SheetExeRuleAct() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 所有计划任务列表
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getSheetRules(HttpServletRequest request) {
		//用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_SheetRules_List_SQL = "select active, task_id, acc_loginname, execute_time, plan_desc " +
					"FROM tab_sche_plan ";
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(m_SheetRules_List_SQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(m_SheetRules_List_SQL);
		psql.getSQL();
		cursor = DataSetBean
				.getCursor(m_SheetRules_List_SQL, offset, pagelen);
		list.add(cursor);
		
		return list;
	}
	
	/**
	 * 取得当前用户的域下的所有工单
	 * 
	 * @author 
	 * @date 2006-8-22
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @param request
	 * @return
	 */
	public String getSheetIDs(boolean flag, String compare, String rename,
			HttpServletRequest request) {
		StringBuffer strSheetIDsList = new StringBuffer();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long area_id = curUser.getAreaId();
		String sql = "select sheet_id from tab_sheet where device_id in ("
					 +" select res_id from tab_gw_res_area where res_type=1 and area_id="+area_id+")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null) {
		} else {
			while(fields != null) {
				strSheetIDsList.append((String)fields.get("sheet_id"));
				strSheetIDsList.append(",");
				fields = cursor.getNext();
			}
		}
		logger.debug("A=GSJ---------"+sql);
		return strSheetIDsList.toString();
	}
	
	/**
	 * 通过设备IP查询设备内部ID
	 * 
	 * @param _deviceIP
	 * @return string 
	 */
	public String getDeviceIdByIP(String _deviceIP) {
		String deviceId = "";
		String strSQL = "";
		if (_deviceIP != null) {
			strSQL = "select device_id from tab_gw_device where loopback_ip = "
					+ "'" + _deviceIP + "'";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			Map resDS = DataSetBean.getRecord(strSQL);
			if (resDS.size() > 0) {
				deviceId = (String) resDS.get("device_id");
			}
		}
		return deviceId;
	}

	/**
	 * 增加、删除、更新
	 * 
	 * @param request
	 * @return String
	 */
	public String SheetRulesActExe(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";
		ArrayList sqlList = new ArrayList();
		
		String strAction = request.getParameter("action");
		String task_id = request.getParameter("task_id");
		
		if (strAction.equals("delete")) { // 删除操作
			

			strSQL = "delete from tab_sche_plan where task_id=" + task_id +"";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			sqlList.add(strSQL);
			strSQL = "delete from tab_sche_sheet where task_id=" + task_id +"";
			psql = new PrepareSQL(strSQL);
			psql.getSQL();
			sqlList.add(strSQL);
		} else {
			String acc_loginname = request.getParameter("acc_loginname");
			String[] sheet_id = request.getParameterValues("sheet_id");
			String delSheet = request.getParameter("delSheet");
			
			
			String execute_time = request.getParameter("execute_time");
			String active = request.getParameter("active");
			String plan_desc = request.getParameter("plan_desc");
			


			if (strAction.equals("add")) {
				// modify by lizhaojun 2007-07-31-------------------------------
				//for(int i=0;i<sheet_id.length;i++) {
					
				//long _task_id = DataSetBean.getMaxId("tab_sche_plan","task_id");
				long _task_id = (new Date()).getTime();
				pSQL.setSQL(m_SheetRulesAdd_SQL);

				pSQL.setLong(1, _task_id);
				pSQL.setString(2, acc_loginname);
				pSQL.setString(3, execute_time);
				pSQL.setInt(4, Integer.parseInt(active));
				pSQL.setString(5, plan_desc);

				strSQL = pSQL.getSQL();

				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				sqlList.add(strSQL);
				logger.debug("insert into sql :" + strSQL);
//				}
				for(int i=0;i<sheet_id.length;i++){					
					strSQL = "insert into tab_sche_sheet(task_id,sheet_id) values("
						+ _task_id + ",'" + sheet_id[i] + "')";
					PrepareSQL psql = new PrepareSQL(strSQL);
					psql.getSQL();
					sqlList.add(strSQL);
				}
				
			} else {
				//str_sheet_id = request.getParameter("sheet_id");
				strSQL = "update tab_sche_plan set execute_time='"
					+ execute_time +"' ,active= "+ active +" , plan_desc='"+plan_desc
					+ "' where task_id=" + task_id;
				
				strSQL = strSQL.replaceAll("=,", "=null,");
				strSQL = strSQL.replaceAll("= where", "=null where");
				PrepareSQL psql = new PrepareSQL(strSQL);
				psql.getSQL();
				sqlList.add(strSQL);				
				
				String[] delSheetList = null;
				if (delSheet != null){
					delSheetList = delSheet.split("#");
				}
				
				if (delSheetList != null && delSheetList.length > 0){
					for(int i=0;i<delSheetList.length;i++){					
						if (delSheetList[i] != null && !"".equals(delSheetList[i])){
							strSQL = "delete from tab_sche_sheet where task_id=" + task_id 
									+ " and sheet_id='" + delSheetList[i] + "'";
							psql = new PrepareSQL(strSQL);
							psql.getSQL();
							sqlList.add(strSQL);
						}
					}
				}
				
				/*
				strSQL = "delete from tab_sche_sheet where task_id =" + task_id;
				sqlList.add(strSQL);
				for(int i=0;i<sheet_id.length;i++){					
					strSQL = "insert into tab_sche_sheet(task_id,sheet_id) values("
						+ task_id + ",'" + sheet_id[i] + "')";
					logger.debug("insert into tab_sche_plan tab_sche_sheet sql :" + strSQL);
					sqlList.add(strSQL);
				}
				*/
			}
		}
		if (sqlList != null && sqlList.size() > 0) {
			int[] iCodes = DataSetBean.doBatch(sqlList);
			if (iCodes != null && iCodes.length > 0) {
				strMsg = "1";
			} else {
				strMsg = "0";
			}
		} 
		return strMsg;
	}
	
	//判断该规则在表中是否已存在
	public boolean IfExist(String sheet_id) {
		PrepareSQL psql = new PrepareSQL("select * from tab_sche_plan where sheet_id='"
                + sheet_id + "'");
		psql.getSQL();
        Map fields = DataSetBean
                .getRecord("select * from tab_sche_plan where sheet_id='"
                        + sheet_id + "'");
        if (fields == null) {
            return false;
        }
        return true;
    }
	
	//通知后台方法
//	public void nofityBGPro() {
//		
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}






