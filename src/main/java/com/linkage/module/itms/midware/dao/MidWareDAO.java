
package com.linkage.module.itms.midware.dao;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings("unchecked")
public class MidWareDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(MidWareDAO.class);
	
	/**
	 * 查询中间件设备表
	 * @param deviceId
	 * @return
	 */
	public List getMidWareDevice(String deviceId){
		logger.debug("getMidWareDevice({})",deviceId);
		String sql = " select device_id,city_id,oui,device_serialnumber," +
				"device_model,device_group,device_phone,device_status," +
				"device_ad,update_time,remark from gw_device_midware where device_id='"+deviceId+"'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return jt.queryForList(sql);
	}
	
	/**
	 * 插入中间件设备表
	 * 
	 * @param deviceId
	 * @param cityId
	 * @param oui
	 * @param deviceSn
	 * @param deviceModel
	 * @param deviceGroup
	 * @param devicePhone
	 * @param deviceStatus
	 * @param deviceAd
	 * @param remark
	 * @return
	 */
	public int insertMidWareDevice(String deviceId,String cityId,String oui,
			String deviceSn,String deviceModel,String deviceGroup,
			String devicePhone,String deviceStatus,String deviceAd,String remark){
		logger.debug("insertMidWareDevice");
		StringBuffer bf = new StringBuffer();
		bf.append(" insert into gw_device_midware (device_id,city_id,oui," +
				"device_serialnumber,device_model,device_group,device_phone," +
				"device_status,device_ad,update_time,remark) values ('");
		bf.append(deviceId);
		bf.append("','");
		bf.append(cityId);
		bf.append("','");
		bf.append(oui);
		bf.append("','");
		bf.append(deviceSn);
		bf.append("','");
		bf.append(deviceModel);
		bf.append("','");
		bf.append(deviceGroup);
		bf.append("','");
		bf.append(devicePhone);
		bf.append("',");
		bf.append(deviceStatus);
		bf.append(",'");
		bf.append(deviceAd);
		bf.append("',");
		bf.append((new Date()).getTime()/1000);
		bf.append(",'");
		bf.append(remark);
		bf.append("')");

		String sql = bf.toString();
		sql = sql.replaceAll(",''", ",null");
		sql = sql.replaceAll(",'null'", ",null");
		sql = sql.replaceAll(",,", ",null,");
		
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return jt.update(sql);
	}
	
	/**
	 * 更新相关参数
	 * 
	 * @param deviceId
	 * @param cityId
	 * @param oui
	 * @param deviceSn
	 * @param deviceModel
	 * @param deviceGroup
	 * @param devicePhone
	 * @param deviceStatus
	 * @param deviceAd
	 * @param remark
	 * @return
	 */
	public int updateMidWareDevice(String deviceId,String cityId,String oui,
			String deviceSn,String deviceModel,String deviceGroup,
			String devicePhone,String deviceStatus,String deviceAd,String remark){
		logger.debug("updateMidWareDevice");
		StringBuffer bf = new StringBuffer();
		bf.append(" update gw_device_midware set city_id='");
		bf.append(cityId);
		bf.append("',oui='");
		bf.append(oui);
		bf.append("',device_serialnumber='");
		bf.append(deviceSn);
		bf.append("',device_model='");
		bf.append(deviceModel);
		bf.append("',device_group='");
		bf.append(deviceGroup);
		bf.append("',device_phone='");
		bf.append(devicePhone);
		bf.append("',device_status=");
		bf.append(deviceStatus);
		bf.append(",device_ad='");
		bf.append(deviceAd);
		bf.append("',update_time=");
		bf.append((new Date()).getTime()/1000);
		bf.append(",remark='");
		bf.append(remark);
		bf.append("' where device_id='");
		bf.append(deviceId);
		bf.append("'");
		
		String sql = bf.toString();
		sql = sql.replaceAll(",''", ",null");
		sql = sql.replaceAll(",'null'", ",null");
		sql = sql.replaceAll(",,", ",null,");
		
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return jt.update(sql);
	}
	
	/**
	 * 删除中间设备表记录
	 * @param deviceId
	 * @return
	 */
	public int deleteMidWareDevice(String deviceId){
		logger.debug("deleteMidWareDevice({})",deviceId);
		PrepareSQL psql = new PrepareSQL(" delete gw_device_midware where device_id='"+deviceId+"'");
    	psql.getSQL();
		return jt.update(" delete gw_device_midware where device_id='"+deviceId+"'");
	}
}
