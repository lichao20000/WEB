package com.linkage.module.gwms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-8-7
 */
public class AutoBindReportDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
		.getLogger(AutoBindReportDAO.class);
	
	/**
	 * 查询用户绑定，以及回综调的情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return void
	 */
	public Map<String,String> queryBindUser(String username){
		logger.debug("queryBindUser({})", username);
		Map<String,String> resMap = null;
		if(StringUtil.IsEmpty(username)){
			logger.warn("username is empty");
		}else{
			String strSQL = "select a.username,a.device_serialnumber,a.userline"
				+ " from tab_hgwcustomer a where a.username=? and a.user_state='1'";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.setString(1, username);
			Map tMap = queryForMap(psql.getSQL());
			if(null != tMap){
				resMap = new HashMap<String,String>();
				resMap.put("username", StringUtil.getStringValue(tMap.get("username")));
				String sn = StringUtil.getStringValue(tMap.get("device_serialnumber"));
				String bindType = StringUtil.getStringValue(tMap.get("userline"));
				if(StringUtil.IsEmpty(sn)){
					resMap.put("device_serialnumber", "");
					resMap.put("bindState", "否");
					resMap.put("bindType", "");
				}else{
					resMap.put("device_serialnumber", sn);
					resMap.put("bindState", "是");
					if("2".equals(bindType)){
						resMap.put("bindType", "自助绑定");
						//自助绑定的数据，需要查询是否回综调
						strSQL = "select b.result_code,b.result_desc"
							+ " from tab_back_dispatch_sheet b"
							+ " where b.username=?";
						psql.setSQL(strSQL);
						psql.setString(1, username);
						Map dispMap = queryForMap(psql.getSQL());
						if(null != dispMap){
							if(null != dispMap.get("result_code")){
								resMap.put("dispatch", "成功");
								resMap.put("dispatchResult", StringUtil.getStringValue(dispMap.get("result_desc")));
							}else{
								resMap.put("dispatch", "失败");
								resMap.put("dispatchResult", "");
							}
						}else{
							resMap.put("dispatch", "未回");
							resMap.put("dispatchResult", "");
						}
					}else{
						resMap.put("bindType", "手工绑定");
						resMap.put("dispatch", "未回");
						resMap.put("dispatchResult", "");
					}
				}
				
				
			}
		}
		return resMap;
	}
	
	/**
	 * 查询用户绑定情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return void
	 */
	public List queryBindUser(List userList){
		logger.debug("queryBindUser()");
		if(null == userList){
			logger.warn("queryBindUser(userList) is null");
			return null;
		}
		List resList = null;
		Map<String,String> resMap = null;
		String waveUser = StringUtils.weave(userList);
		String strSQL = "select a.username,a.device_serialnumber,a.userline"
			+ " from tab_hgwcustomer a where a.username in (?) and a.user_state='1'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setStringExt(1, waveUser, false);
		List userTempList = jt.queryForList(psql.getSQL());
		if(null != userTempList){
			resList = new ArrayList();
			int size = userTempList.size();
			for(int i = 0; i < size; i++){
				Map tMap = (Map)userTempList.get(i);
				resMap = new HashMap<String,String>();
				resMap.put("username", StringUtil.getStringValue(tMap.get("username")));
				String sn = StringUtil.getStringValue(tMap.get("device_serialnumber"));
				String bindType = StringUtil.getStringValue(tMap.get("userline"));
				if(StringUtil.IsEmpty(sn)){
					resMap.put("device_serialnumber", "");
					resMap.put("bindState", "否");
					resMap.put("bindType", "");
				}else{
					resMap.put("device_serialnumber", sn);
					resMap.put("bindState", "是");
					if("2".equals(bindType)){
						resMap.put("back", "1");
					}else{
						resMap.put("bindType", "手工绑定");
						resMap.put("dispatch", "未回");
						resMap.put("dispatchResult", "");
					}
				}
				resList.add(resMap);
			}
		}
		return resList;
	}
	
	/**
	 * 查询自助绑定用户回综调的情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return void
	 */
	public List querydispatchUser(List userList){
		logger.debug("querydispatchUser()");
		if(null == userList){
			logger.warn("queryBindUser(userList) is null");
			return null;
		}
		List resList = null;
		Map<String,String> resMap = null;
		String waveUser = StringUtils.weave(userList);
		
		//回综调信息获取
		//自助绑定的数据，需要查询是否回综调
		String strSQL = "select b.result_code,b.result_desc,b.username"
			+ " from tab_back_dispatch_sheet b"
			+ " where b.username in (?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setStringExt(1, waveUser, false);
		List dispatchList = jt.queryForList(psql.getSQL());
		if(null != dispatchList){
			resList = new ArrayList();
			int size = dispatchList.size();
			for(int i = 0; i < size; i++){
				resMap = new HashMap();
				Map dispMap = (Map)dispatchList.get(i);
				resMap.put("bindType", "自助绑定");
				resMap.put("username", StringUtil.getStringValue(dispMap.get("username")));
				if(null != dispMap){
					if(null != dispMap.get("result_code")){
						resMap.put("dispatch", "成功");
						resMap.put("dispatchResult", StringUtil.getStringValue(dispMap.get("result_desc")));
					}else{
						resMap.put("dispatch", "失败");
						resMap.put("dispatchResult", "");
					}
				}else{
					resMap.put("dispatch", "未回");
					resMap.put("dispatchResult", "");
				}
				resList.add(resMap);
			}
		}
		return resList;
	}


}
