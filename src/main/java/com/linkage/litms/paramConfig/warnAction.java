package com.linkage.litms.paramConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;

/**
 * 封装一些设备告警的常用数据，以及对告警的确认清除操作
 * 
 * @author chenzm
 * @version 1.00, 2007-7-23
 */
public class warnAction {
	
	public Map warnTypeMap = new HashMap();
	public Map warnLevelMap = new HashMap();
	public Map warnStatusMap = new HashMap();
	public Map warnNameMap = new HashMap();
	
	public warnAction(){
		warnTypeMap.put("1","设备告警");
		warnTypeMap.put("2","服务质量告警");
		warnTypeMap.put("3","通信告警");
		warnTypeMap.put("4","处理失败告警");
		warnTypeMap.put("5","网管系统产生的告警");
/**
		warnLevelMap.put("1","紧急告警");
		warnLevelMap.put("2","主要告警");
		warnLevelMap.put("3","次要告警");
		warnLevelMap.put("4","警告告警");
		warnLevelMap.put("5","不确定告警");
		warnLevelMap.put("6","已清除告警");
**/
		EventLevelLefDAO eventLevelLefDao = new EventLevelLefDAO();
		warnLevelMap = eventLevelLefDao.getWarnLevel();
//		warnLevelMap.put("1","严重告警");
//		warnLevelMap.put("2","主要告警");
//		warnLevelMap.put("3","次要告警");
//		warnLevelMap.put("4","警告告警");
//		warnLevelMap.put("5","事件告警");
//		warnLevelMap.put("6","清除告警");
		
		warnStatusMap.put("1","未确认未清除");
		warnStatusMap.put("2","已确认未清除");
		warnStatusMap.put("3","未确认但已清除");
		warnStatusMap.put("4","已确认并已清除");

		String sql = "select * from tab_alarm_knowledge";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select id, name from tab_alarm_knowledge";
		}
		PrepareSQL psql = new PrepareSQL();
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		while(fields != null){
			warnNameMap.put(fields.get("id"), fields.get("name"));
			fields = cursor.getNext();
		}
		
	}
	
	/**
	 * 根据id获取告警类型
	 * @param id
	 * @return 告警类型
	 */
	public String getWarnType(Object id){
		
		Object tmp = warnTypeMap.get(id);
		
		if (tmp != null){
			return (String)tmp;
		}
		else{
			return "未知类型";
		}
	}
	
	/**
	 * 根据id获取告警等级
	 * @param id
	 * @return 告警等级
	 */
	public String getWarnLevel(Object id){
		
		Object tmp = warnLevelMap.get(id);
		
		if (tmp != null){
			return (String)tmp;
		}
		else{
			return "未知等级";
		}
	}
	
	/**
	 * 根据id获取告警状态
	 * @param id
	 * @return 告警状态
	 */
	public String getWarnStatus(Object id){
		
		Object tmp = warnStatusMap.get(id);
		
		if (tmp != null){
			return (String)tmp;
		}
		else{
			return "未知状态";
		}
	}
	
	/**
	 * 根据id获取告警状态
	 * @param id
	 * @return 告警名称
	 */
	public String getWarnName(Object id){
		
		Object tmp = warnNameMap.get(id);
		
		if (tmp != null){
			return (String)tmp;
		}
		else{
			return "未知告警";
		}
	}
	
	/**
	 * 对告警进行确认和清除
	 * 
	 * @param request
	 * @return 返回操作的记录条数
	 */
	public int changeStatus(HttpServletRequest request){
		
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		String device_id = request.getParameter("device_id");
		String time = request.getParameter("time");
		String status = request.getParameter("status");
		
		return doChange(action,id,device_id,time,status);
	}
	
	/**
	 * 对告警进行批量确认和批量清除
	 * 
	 * @param request
	 * @return 返回成功标志 0：失败 1：成功
	 */
	public int changeStatusBatch(HttpServletRequest request){
		
		int result = 1;
		String action = request.getParameter("action");
		String warmList = request.getParameter("warmList");
		
		String[] warmArray = null;
		String[] warmInfo = null;
		
		if (warmList != null){
			warmArray = warmList.split("#");
		}
		else{
			return 0;
		}
		
		for (int i=0;i<warmArray.length;i++){
			if (warmArray[i] != null){
				warmInfo = warmArray[i].split(",");
				
				if (warmInfo.length >= 4){
					doChange(action,warmInfo[0],warmInfo[1],warmInfo[2],warmInfo[3]);
				}
			}
		}
		
		return result;
	}
	
	private int doChange(String action,String id,String device_id,String time,String status){
		String sql = "";
		int result = 0;
		
		//确认告警
		if (action != null && "confirm".equals(action)){
			if (id != null && device_id != null & time != null){
				
				//未确认未清除的告警更新为已确认未清除
				if (status != null && "1".equals(status)){
					sql += "update tab_alarm set status = 2";
				}
				
				//未确认已清除的告警更新为已确认已清除
				else if (status != null && "3".equals(status)){
					sql += "update tab_alarm set status = 4";
				}
				
				else{
					return 0;
				}
				
				sql += " where id = " + id + " and device_id = '" + device_id + "' and occurtime = " + time;
			}
		}
		//清除告警
		else if (action != null && "clean".equals(action)){
			if (id != null && device_id != null & time != null){
				
				//未确认未清除的告警更新为未确认已清除
				if (status != null && "1".equals(status)){
					sql += "update tab_alarm set status = 3";
				}
				
				//已确认未清除的告警更新为已确认已清除
				else if (status != null && "2".equals(status)){
					sql += "update tab_alarm set status = 4";
				}
				
				else{
					return 0;
				}
				
				long curtime = (new Date()).getTime()/1000;
				
				sql += ", cleartime = " + curtime + " where id = " + id + " and device_id = '" + device_id + "' and occurtime = " + time;
			}
		}
		//取消告警
		else if (action != null && "cancelConfirm".equals(action)){
			if (id != null && device_id != null & time != null){
				
				//已确认未清除的告警更新为未确认未清除
				if (status != null && "2".equals(status)){
					sql += "update tab_alarm set status = 1";
				}
				
				//已确认已清除的告警更新为未确认已清除
				else if (status != null && "4".equals(status)){
					sql += "update tab_alarm set status = 3";
				}
				
				else{
					return 0;
				}
				
				long curtime = (new Date()).getTime()/1000;
				
				sql += ", cleartime = " + curtime + " where id = " + id + " and device_id = '" + device_id + "' and occurtime = " + time;
			}
		}
		
		if (!"".equals(sql)){
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			result=DataSetBean.executeUpdate(sql);
		}
		
		return result;
	}

}
