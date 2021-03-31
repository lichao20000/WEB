
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class UpdateDevCityByIPDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(UpdateDevCityByIPDAO.class);

	public List<Map> queryDevice(String device_serialnumber, String loopbackIp, String gw_type)
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.device_id,a.city_id,a.oui,a.device_serialnumber,a.vendor_id," +
				"a.device_model_id,a.devicetype_id,a.loopback_ip,b.vendor_add,c.device_model," +
				"d.softwareversion,a.cpe_allocatedstatus,a.device_type " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				"and a.devicetype_id=d.devicetype_id ");
		if (false == StringUtil.IsEmpty(device_serialnumber))
		{
			if(device_serialnumber.length()>5){
				sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
			}
			sql.append(" and a.device_serialnumber like '%").append(device_serialnumber)
					.append("' ");
		}
		if (false == StringUtil.IsEmpty(loopbackIp))
		{
			sql.append(" and a.loopback_ip='").append(loopbackIp).append("' ");
		}
		// gw_type
		if(false == StringUtil.IsEmpty(gw_type))
			sql.append(" and a.gw_type=").append(gw_type);
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * 安徽修改属地
	 * @author 岩 
	 * @date 2016-9-22
	 * @param device_serialnumber
	 * @param loopbackIp
	 * @param gw_type
	 * @return
	 */
	public List<Map> queryAhDevice(String device_serialnumber, String loopbackIp )
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.city_id ,a.device_serialnumber,a.loopback_ip, a.cpe_allocatedstatus  " +
						"from tab_gw_device a where  1 = 1 ");
	
		if (false == StringUtil.IsEmpty(device_serialnumber))
		{
			if(device_serialnumber.length()>5){
				sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
			}
			sql.append(" and a.device_serialnumber like '%").append(device_serialnumber)
					.append("' ");
		}
		if (false == StringUtil.IsEmpty(loopbackIp))
		{
			sql.append(" and a.loopback_ip='").append(loopbackIp).append("' ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	public List<Map> getAhDeviceInfoByDev(String deviceId )
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.city_id ,a.device_serialnumber,a.loopback_ip, a.cpe_allocatedstatus  " +
						"from tab_gw_device a where a.device_id='" + deviceId+"'");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}

	public String getCityIdByIP(String fillIP)
	{
		String sql = "select a.city_id,country from gw_ipmain a,gw_subnets b"
				+ " where a.subnet=b.subnet and a.inetmask=b.inetmask"
				+ " and a.city_id=b.subnetgrp and flowaddress<='" + fillIP + "'"
				+ " and fhighaddress>='" + fillIP + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map<String, String> map = queryForMap(sql);
		sql = null;
		if (null == map)
		{
			return "0000";
		}
		else
		{
			if (null == map.get("country")
					|| map.get("country").toString().trim().length() == 0)
			{
				if (null == map.get("city_id")
						|| map.get("city_id").toString().trim().length() == 0)
				{
					return "0000";
				}
				else
				{
					return map.get("city_id").toString().trim();
				}
			}
			else
			{
				return map.get("country").toString().trim();
			}
		}
	}

	public String release(String deviceId)
	{
		PrepareSQL ppSQL = null;
		if (LipossGlobals.IsITMS())
		{
			ppSQL = new PrepareSQL(
					" update tab_hgwcustomer set oui=null,device_serialnumber=null,device_id=null, binddate=null, updatetime=? where device_id=? ");
			ppSQL.setLong(1, new DateTimeUtil().getLongTime());
			ppSQL.setString(2, deviceId);
		}
		else
		{
			ppSQL = new PrepareSQL(
					" update tab_egwcustomer set oui=null,device_serialnumber=null,device_id=null,updatetime=?,binddate=null where device_id=? ");
			ppSQL.setLong(1, new DateTimeUtil().getLongTime());
			ppSQL.setString(2, deviceId);
		}
		return ppSQL.getSQL();
	}

	public String updateCityByIP(String deviceId, String ipCityId)
	{
		PrepareSQL ppSQL = new PrepareSQL(
				"update tab_gw_device set cpe_allocatedstatus=0,city_id=? where device_id=?");
		ppSQL.setString(1, ipCityId);
		ppSQL.setString(2, deviceId);
		return ppSQL.getSQL();
	}
	
	/**
	 * 批量更新数据库
	 * 
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param
	 * @return int[]
	 */
	public int[] batchUpdate(String[] sql)
	{
		logger.debug("batchUpdate(sql:{})", sql);
		return jt.batchUpdate(sql);
	}
}
