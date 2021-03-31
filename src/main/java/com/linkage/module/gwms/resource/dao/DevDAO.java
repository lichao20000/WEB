/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.config.obj.DevAndUserObj;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 25, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class DevDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DevDAO.class);

	/**
	 * get data by device_id from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getDevByDevId(String deviceId) 
	{
		logger.debug("getDevByDevId({})", deviceId);
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}else{
			
		} */
		String sql = "select * from tab_gw_device where a.device_id='"
				+ deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return queryForMap(psql.getSQL());
	}

	/**
	 * get oui,sn,username by device_id from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getDevUserByDevId(String deviceId) 
	{
		logger.debug("getDevUserByDevId({})", deviceId);

		String sql = "select a.oui,a.device_serialnumber,b.username"
				+ " from tab_gw_device a left join " + Global.G_UserTab
				+ " b on a.device_id=b.device_id where a.device_id='"
				+ deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return queryForMap(psql.getSQL());
	}

	/**
	 * 获得设备用户相关信息
	 * @author gongsj
	 * @date 2009-7-29
	 * @param deviceSn
	 * @param userAccount
	 * @return
	 */
	public DevAndUserObj getDevAndUserObj(String sn, String userAccount) 
	{
		logger.debug("getDevAndUserObj DAO：{},{}", sn, userAccount);
		
		DevAndUserObj devAndUserObj = null;
		StringBuilder sql = new StringBuilder();
		
		if (null != sn && !"".equals(sn.trim())) {
			sql.append("select device_id, device_serialnumber from tab_gw_device where 1=1 ");
			if(sn.length()>5){
				sql.append(" and dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"'");
			}
			sql.append(" and device_serialnumber like '%"+sn+"'");
		} else {
			sql.append("select a.device_id,a.device_serialnumber " +
					"from tab_gw_device a,tab_hgwcustomer b" +
					" where a.device_id = b.device_id and b.username='"+userAccount+"'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		
		if (null == list || list.isEmpty()) {
			return null;
		}
		
		devAndUserObj = new DevAndUserObj();
		devAndUserObj.setDevNum(list.size());
		devAndUserObj.setDeviceId((list.get(0)).get("device_id"));
		devAndUserObj.setDeviceSn((list.get(0)).get("device_serialnumber"));
		
		return devAndUserObj;
	}
	
	
	/**
	 * 获得设备用户相关信息
	 * @author gongsj
	 * @date 2009-7-29
	 * @param deviceSn
	 * @param userAccount
	 * @return
	 */
	public DevAndUserObj getDevAndUserObj(String deviceId) 
	{
		logger.debug("getDevAndUserObj DAO deviceId：{}", deviceId);
		
		DevAndUserObj devAndUserObj = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("select a.device_serialnumber, b.username from tab_gw_device a, tab_hgwcustomer b")
		   .append(" where a.device_id = b.device_id and a.device_id='")
		   .append(deviceId).append("'");
		
//		Map<String, String> map = dao.queryForMap(sql.toString());
//		devAndUserObj = new DevAndUserObj();
//		devAndUserObj.setDeviceId(StringUtil.getStringValue(map, "device_id"));
//		devAndUserObj.setDeviceSn(StringUtil.getStringValue(map, "device_serialnumber"));
//		devAndUserObj.setUserAccount(StringUtil.getStringValue(map, "username"));
//		return devAndUserObj;
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		
		if (null == list || list.isEmpty()) {
			return null;
		}
		
		devAndUserObj = new DevAndUserObj();
		devAndUserObj.setDevNum(list.size());
		devAndUserObj.setDeviceId((list.get(0)).get("device_id"));
		devAndUserObj.setDeviceSn((list.get(0)).get("device_serialnumber"));
		devAndUserObj.setUserAccount((list.get(0)).get("username"));
		
		return devAndUserObj;
	}

}
