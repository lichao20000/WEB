
package com.linkage.module.gwms.dao.gw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.gw.DeviceOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.DateTimeUtil;
/**
 * @author Jason(3412)
 * @date 2009-10-21
 */
@SuppressWarnings({"rawtypes","deprecation"})
public class DeviceDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceDAO.class);
	private PrepareSQL psql = new PrepareSQL();
	private DeviceOBJ devObj;
	private Map devInfoMap;
	private int total ;
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * HashMap<vendor_id,vendor_add>
	 */
	private HashMap<String, String> vendorMap = null;
	/**
	 * HashMap<device_model_id,device_model>
	 */
	private HashMap<String, String> deviceModelMap = null;
	
	private HashMap<String, String> softwareversionMap = null;
	private HashMap<String, String> hardwareversionMap = null;

	/**
	 * 获取设备信息Map
	 * 
	 * @param device_id
	 * @return Map,不存在或记录大于1条返回null
	 */
	public Map getDevInfoMap(String deviceId)
	{
		logger.debug("getDevInfoMap({})", deviceId);
		String sqlDevInfo = "select device_name, gather_id, loopback_ip, cr_port"
				+ ",cr_path, acs_username, acs_passwd, device_id, device_serialnumber"
				+ ",oui from tab_gw_device where device_id=?";
		psql.setSQL(sqlDevInfo);
		psql.setString(1, deviceId);
		devInfoMap = queryForMap(psql.getSQL());
		return devInfoMap;
	}

	/**
	 * 获取设备信息对象
	 * 
	 * @param device_id
	 * @return DeviceOBJ,不存在返回null
	 */
	public DeviceOBJ getDevInfoObj(String deviceId)
	{
		logger.debug("getDevInfoObj({})", deviceId);
		devObj = null;
		devInfoMap = getDevInfoMap(deviceId);
		// 设备信息map转对象
		map2Obj(devInfoMap, devObj);
		return devObj;
	}

	/**
	 * 设备信息Map转设备信息对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-22
	 * @return void
	 */
	private void map2Obj(Map devMap, DeviceOBJ devObj)
	{
		logger.debug("map2Obj({},{})", devMap, devObj);
		if (null == devMap || devMap.isEmpty())
		{
			logger.debug("deviceMap相关信息不存在");
			return;
		}
		if (null == devObj)
		{
			logger.debug("new DeviceOBJ()");
			devObj = new DeviceOBJ();
		}
		devObj.setDeviceId(StringUtil.getStringValue(devInfoMap.get("device_id")));
		devObj.setDeviceName(StringUtil.getStringValue(devInfoMap.get("device_name")));
		devObj.setOui(StringUtil.getStringValue(devInfoMap.get("oui")));
		devObj.setDevSn(StringUtil.getStringValue(devInfoMap.get("device_serialnumber")));
		devObj.setGatherId(StringUtil.getStringValue(devInfoMap.get("gather_id")));
		devObj.setLoopbackIp(StringUtil.getStringValue(devInfoMap.get("loopback_ip")));
		devObj.setCrPath(StringUtil.getStringValue(devInfoMap.get("cr_path")));
		devObj.setCrPort(StringUtil.getStringValue(devInfoMap.get("cr_port")));
	}

	/**
	 * 获取设备类型Cursor (select type_name from gw_dev_type)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-21
	 * @return Cursor DataSetBean.getCursor(strSQL)
	 */
	public static Cursor getDevType()
	{
		logger.debug("getDevType()");
		String strSQL = "select type_name from gw_dev_type";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}

	/**
	 * * 分页查询设备记录
	 * 
	 * @param infoType
	 *            用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @param area_id
	 *            域
	 * @param cityIdList
	 *            属地列表
	 * @param bindState
	 *            绑定状态 未绑定：1 绑定：2
	 * @param timeType
	 *            时间类型 上报时间：1 绑定时间：2
	 * @param starttime1
	 *            开始时间
	 * @param endtime1
	 *            结束时间
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return List<Map>
	 */
	public List<Map> getDeviceList(final String infoType, int curPage_splitPage,
			int num_splitPage, UserRes curUser, final String bindState, String timeType,
			String starttime1, String endtime1, String sn,
			String loopback_ip, String device_logicsn, String device_status)
	{
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
		String cityId = curUser.getCityId();
		long areaId = curUser.getAreaId();
		ArrayList<String> cityIdList = null;
		if ("00".equals(cityId))
		{
			cityIdList = null;
		}
		else
		{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if("1".equals(bindState)){				
				List<String> list = CityDAO.getAllPcityIdByCityId(cityId);
				cityIdList.addAll(list);
			}
			
		}
		StringBuffer sql = new StringBuffer();
		// 查询家庭网关设备
		if ("1".equals(infoType))
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.oui,");
					sql.append("a.device_model_id,a.device_serialnumber,a.loopback_ip,a.complete_time,");
					sql.append("b.binddate,b.user_id,b.username,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_hgwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1 ");
				}
				else
				{
				/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
					} */
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,");
					sql.append("b.user_id,b.username,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_hgwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
				sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,");
				sql.append("a.cpe_allocatedstatus,a.device_id_ex ");
				sql.append("from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=1 ");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				if ("2".equals(timeType))
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,");
					sql.append("a.device_serialnumber,a.loopback_ip,a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=1 ");
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,");
					sql.append("a.device_serialnumber,a.loopback_ip,a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a ");
					sql.append("where a.gw_type=1 ");
				}
				
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (!StringUtil.IsEmpty(sn))
				{
					if(sn.length()>5){
						sql.append(" and a.dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"'");
					}
					sql.append(" and a.device_serialnumber like'%"+sn+"'");
				}
				if (!StringUtil.IsEmpty(loopback_ip))
				{
					sql.append(" and a.loopback_ip like '%"+loopback_ip+"%'");
				}
				if (!StringUtil.IsEmpty(device_logicsn))
				{
					sql.append(" and a.device_id_ex like '%"+device_logicsn+"%'");
				}
			}
			sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");
		}
		// 查询企业网关设备
		else
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,");
					sql.append("b.binddate,b.user_id,b.username,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_egwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2");
				}
				else
				{
				/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
					} */
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,");
					sql.append("b.user_id,b.username,c.area_id,a.cpe_allocatedstatus,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_egwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,");
				sql.append("a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ");
				sql.append("from tab_gw_device a ");
				sql.append("where a.cpe_allocatedstatus=0 and a.gw_type=2 ");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,");
				sql.append("a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ");
				if ("2".equals(timeType))
				{
					sql.append("from tab_gw_device a left join tab_egwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=2 ");
					if (!StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (!StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					sql.append("from tab_gw_device a where a.gw_type=2 ");
				}
				
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (!StringUtil.IsEmpty(sn))
				{
					if(sn.length()>5){
						sql.append(" and a.dev_sub_sn ='"+sn.substring(sn.length()-6, sn.length())+"'");
					}
					sql.append(" and a.device_serialnumber like'%"+sn+"'");
				}
				if (!StringUtil.IsEmpty(loopback_ip))
				{
					sql.append(" and a.loopback_ip like '%"+loopback_ip+"%'");
				}
			}
			sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		hardwareversionMap = VendorModelVersionDAO.getDeviceTypeInfoMap();
		cityIdList = null;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						long time = 0;
						DateTimeUtil dateTimeUtil = null;
						map.put("softwareversion", softwareversionMap.get(rs.getString("devicetype_id")));
						map.put("hardwareversion", hardwareversionMap.get(rs.getString("devicetype_id")));
						map.put("device_id", rs.getString("device_id"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil
								.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						String vendor_id = rs.getString("vendor_id");
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(vendor_id));
						if (false == StringUtil.IsEmpty(vendor_add))
						{
							map.put("vendor_add", vendor_add);
						}
						else
						{
							map.put("vendor_add", "");
						}
						String device_model_id = rs.getString("device_model_id");
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(device_model_id));
						if (false == StringUtil.IsEmpty(device_model))
						{
							map.put("device_model", device_model);
						}
						else
						{
							map.put("device_model", "");
						}
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs
								.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-"
								+ rs.getString("device_serialnumber"));
						map.put("loopback_ip", rs.getString("loopback_ip"));
//						if ("1".equals(infoType))   // 注释 by zhangchy 2012-08-31 (ITMS与BBMS融合后使用同一个tab_gw_device)
//						{
							// 家庭网关才有这个字段 // by zhangchy 企业网关现在也有这个字段
							map.put("device_id_ex", rs.getString("device_id_ex"));
//						}
						// 将complete_time转换成时间
						try
						{
							time = Long.parseLong(rs.getString("complete_time"));
							dateTimeUtil = new DateTimeUtil(time * 1000);
							map.put("complete_time", dateTimeUtil.getDate());
						}
						catch (NumberFormatException e)
						{
							map.put("complete_time", "");
						}
						catch (Exception e)
						{
							map.put("complete_time", "");
						}
						if ("2".equals(bindState))
						{
							// 将binddate转换成时间
							try
							{
								time = Long.parseLong(rs.getString("binddate"));
								dateTimeUtil = new DateTimeUtil(time * 1000);
								map.put("binddate", dateTimeUtil.getDate());
							}
							catch (NumberFormatException e)
							{
								map.put("binddate", "");
							}
							catch (Exception e)
							{
								map.put("binddate", "");
							}
							map.put("user_id", rs.getString("user_id"));
							map.put("username", rs.getString("username"));
							map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
						}
						return map;
					}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		softwareversionMap = null;
		hardwareversionMap = null;
		return list;
	}

	/**
	 * 设备记录总记录数
	 * 
	 * @param infoType
	 *            用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @param area_id
	 *            域
	 * @param cityIdList
	 *            属地列表
	 * @param bindState
	 *            绑定状态 未绑定：1 绑定：2
	 * @param timeType
	 *            时间类型 上报时间：1 绑定时间：2
	 * @param starttime1
	 *            开始时间
	 * @param endtime1
	 *            结束时间
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return int
	 */
	public int getDeviceCount(String infoType, int curPage_splitPage, int num_splitPage,
			UserRes curUser, String bindState, String timeType, String starttime1,
			String endtime1, String device_serialnumber, String loopback_ip, String device_logicsn, 
			String device_status)
	{
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
		String cityId = curUser.getCityId();
		long areaId = curUser.getAreaId();
		ArrayList<String> cityIdList = null;
		if ("00".equals(cityId))
		{
			cityIdList = null;
		}
		else
		{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if("2".equals(infoType)&&"1".equals(bindState)){				
				List<String> list = CityDAO.getAllPcityIdByCityId(cityId);
				cityIdList.addAll(list);
			}
		}
		StringBuffer sql = new StringBuffer();
		// 查询家庭网关设备
		if ("1".equals(infoType))
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a,tab_hgwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1 ");
				}
				else
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a,tab_hgwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("select count(*) ");
				}else{
					sql.append("select count(1) ");
				}
				
				sql.append("from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=1 ");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				if ("2".equals(timeType))
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=1 ");
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a where a.gw_type=1");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (device_serialnumber != null && !"".equals(device_serialnumber))
				{
					if(LipossGlobals.inArea(Global.NXLT)){
						sql.append(" and a.device_serialnumber like'%").append(device_serialnumber).append("%'");
					}else{
						if(device_serialnumber.length()>5){
							sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
						}
						sql.append(" and a.device_serialnumber like'%").append(
								device_serialnumber).append("'");
					}
				}
				if (loopback_ip != null && !"".equals(loopback_ip))
				{
					sql.append(" and a.loopback_ip like '%").append(loopback_ip).append(
							"%'");
				}
				if(!StringUtil.IsEmpty(device_logicsn))
				{
					sql.append(" and a.device_id_ex like'%").append(device_logicsn).append("%'");
				}
			}
		}
		else
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a,tab_egwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2 ");
				}
				else
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a,tab_egwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("select count(*) ");
				}else{
					sql.append("select count(1) ");
				}
				sql.append(" from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=2 ");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				if ("2".equals(timeType))
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a left join tab_egwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=2");
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_gw_device a where a.gw_type=2");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (device_serialnumber != null && !"".equals(device_serialnumber))
				{
					if(device_serialnumber.length()>5){
						sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
					}
					sql.append(" and a.device_serialnumber like'%").append(
							device_serialnumber).append("'");
				}
				if (loopback_ip != null && !"".equals(loopback_ip))
				{
					sql.append(" and a.loopback_ip like '%").append(loopback_ip).append(
							"%'");
				}
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		this.total = total;
		cityIdList = null;
		return maxPage;
	}
	
	
	
	
	/**
	 * 分页查询设备记录
	 * 
	 * add by zhangchy 2012-12-27 安徽需求
	 * 
	 * @param infoType
	 *            用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 *            
	 * @return List<Map>
	 */
	public List<Map> getDeviceListByDeviceId(String infoType, String deviceId, int curPage_splitPage,
			int num_splitPage) {
		
		logger.debug("getDeviceList({},{},{}})", new Object[] { deviceId,
				curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,")
		   .append("a.device_serialnumber,a.loopback_ip,a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ")
		   .append("from tab_gw_device a ")
		   .append("where a.gw_type=").append(infoType)
		   .append(" and a.device_id='").append(deviceId).append("'");
		
		if(LipossGlobals.inArea(Global.NXLT)){
			sql.append(" order by a.complete_time desc");
		}else{
			sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		if(LipossGlobals.inArea(Global.NXLT))
		{
			softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		}

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						long time = 0;
						DateTimeUtil dateTimeUtil = null;
						map.put("device_id", rs.getString("device_id"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil
								.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						String vendor_id = rs.getString("vendor_id");
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(vendor_id));
						if (false == StringUtil.IsEmpty(vendor_add))
						{
							map.put("vendor_add", vendor_add);
						}
						else
						{
							map.put("vendor_add", "");
						}
						String device_model_id = rs.getString("device_model_id");
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(device_model_id));
						if (false == StringUtil.IsEmpty(device_model))
						{
							map.put("device_model", device_model);
						}
						else
						{
							map.put("device_model", "");
						}
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs
								.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-"
								+ rs.getString("device_serialnumber"));
						map.put("loopback_ip", rs.getString("loopback_ip"));
//						if ("1".equals(infoType))   // 注释 by zhangchy 2012-08-31 (ITMS与BBMS融合后使用同一个tab_gw_device)
//						{
							// 家庭网关才有这个字段 // by zhangchy 企业网关现在也有这个字段
							map.put("device_id_ex", rs.getString("device_id_ex"));
//						}
						// 将complete_time转换成时间
						try
						{
							time = Long.parseLong(rs.getString("complete_time"));
							dateTimeUtil = new DateTimeUtil(time * 1000);
							map.put("complete_time", dateTimeUtil.getDate());
						}
						catch (NumberFormatException e)
						{
							map.put("complete_time", "");
						}
						catch (Exception e)
						{
							map.put("complete_time", "");
						}
						if(LipossGlobals.inArea(Global.NXLT))
						{
							String deviceTypeId = rs.getString("devicetype_id");
							String softwareversion = StringUtil.getStringValue(softwareversionMap.get(deviceTypeId));
							if(!StringUtil.IsEmpty(softwareversion))
							{
								map.put("softwareversion",softwareversion);
							}
							else
							{
								map.put("softwareversion","");
							}
						}
						return map;
					}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		softwareversionMap = null;
		return list;
	}

	public List<Map> getDeviceListByDeviceIdForSxlt(String infoType, String deviceId, 
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceList({},{},{}})", new Object[] { deviceId,
				curPage_splitPage, num_splitPage });

		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,")
				.append("a.device_serialnumber,a.loopback_ip,")
				.append("a.complete_time,a.cpe_allocatedstatus,a.device_id_ex ")
				.append("  from tab_gw_device a ")
				.append(" where a.gw_type = ").append(infoType)
				.append("   and a.device_id = '").append(deviceId).append("'");

		sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						long time = 0;
						DateTimeUtil dateTimeUtil = null;
						map.put("device_id", rs.getString("device_id"));
						map.put("softwareversion", softwareversionMap.get(rs.getString("devicetype_id")));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil
								.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name))
						{
							map.put("city_name", city_name);
						}
						else
						{
							map.put("city_name", "");
						}
						String vendor_id = rs.getString("vendor_id");
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(vendor_id));
						if (false == StringUtil.IsEmpty(vendor_add))
						{
							map.put("vendor_add", vendor_add);
						}
						else
						{
							map.put("vendor_add", "");
						}
						String device_model_id = rs.getString("device_model_id");
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(device_model_id));
						if (false == StringUtil.IsEmpty(device_model))
						{
							map.put("device_model", device_model);
						}
						else
						{
							map.put("device_model", "");
						}
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs
								.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-"
								+ rs.getString("device_serialnumber"));
						map.put("loopback_ip", rs.getString("loopback_ip"));
//						if ("1".equals(infoType))   // 注释 by zhangchy 2012-08-31 (ITMS与BBMS融合后使用同一个tab_gw_device)
//						{
						// 家庭网关才有这个字段 // by zhangchy 企业网关现在也有这个字段
						map.put("device_id_ex", rs.getString("device_id_ex"));
//						}
						// 将complete_time转换成时间
						try
						{
							time = Long.parseLong(rs.getString("complete_time"));
							dateTimeUtil = new DateTimeUtil(time * 1000);
							map.put("complete_time", dateTimeUtil.getDate());
						}
						catch (NumberFormatException e)
						{
							map.put("complete_time", "");
						}
						catch (Exception e)
						{
							map.put("complete_time", "");
						}

						return map;
					}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		softwareversionMap = null;
		return list;
	}
	
	/**
	 * 设备记录总记录数
	 * 
	 * add by zhangchy 2012-12-27 安徽需求
	 * 
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @param cityIdList
	 *            属地列表
	 * 
	 * @return int
	 */
	public int getDeviceCountByDeviceId(String infoType, String deviceId, 
			int curPage_splitPage,int num_splitPage)
	{
		logger.debug("getDeviceList({},{},{},{})", new Object[] { deviceId,
				curPage_splitPage, num_splitPage });

		StringBuffer sql = new StringBuffer();
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_gw_device ")
		   .append("where gw_type = ").append(infoType)
		   .append(" and device_id = '").append(deviceId).append("'");			
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		
		return maxPage;
	}
	

//	/**
//	 * 导出设备列表
//	 * 
//	 * @author wangsenbo
//	 * @date Nov 18, 2009
//	 * @return List<Map>
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Map> getDeviceExcel(String infoType, UserRes curUser,
//			final String bindState, String timeType, String starttime1, String endtime1,
//			String device_serialnumber, String loopback_ip, String device_status)
//	{
//		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
//		List<Map> list;
//		String cityId = curUser.getCityId();
//		long areaId = curUser.getAreaId();
//		ArrayList<String> cityIdList = null;
//		if ("00".equals(cityId))
//		{
//			cityIdList = null;
//		}
//		else
//		{
//			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			if("2".equals(infoType)&&"1".equals(bindState)){				
//				List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
//				cityIdList.addAll(list1);
//			}
//		}
//		StringBuffer sql = new StringBuffer();
//		// 查询家庭网关设备
//		if ("1".equals(infoType))
//		{
//			// 查询绑定设备
//			if ("2".equals(bindState))
//			{
//				if (curUser.getUser().isAdmin())
//				{
//					sql
//							.append(
//									"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,b.user_id,b.username")
//							.append(
//									" from tab_gw_device a,tab_hgwcustomer b where b.user_state in('1','2') and a.device_id=b.device_id");
//				}
//				else
//				{
//					sql
//							.append(
//									"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,b.user_id,b.username,c.area_id from tab_gw_device a,tab_hgwcustomer b,tab_gw_res_area c where a.device_id=c.res_id and c.res_type=1 and c.area_id=")
//							.append(areaId)
//							.append(
//									" and b.user_state in('1','2') and a.device_id=b.device_id");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//				if ("2".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and b.binddate>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and b.binddate<").append(endtime1);
//					}
//				}
//			}
//			else if ("1".equals(bindState))
//			{
//				sql
//						.append(
//								"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time")
//						.append(" from tab_gw_device a where a.cpe_allocatedstatus=0");
//				if (cityIdList != null)
//				{
//					sql.append(" and a.city_id in (").append(
//							StringUtils.weave(cityIdList)).append(")");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//			}
//			else
//			{
//				if ("2".equals(timeType))
//				{
//					sql
//							.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id where 1=1");
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and b.binddate>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and b.binddate<").append(endtime1);
//					}
//				}
//				else
//				{
//					sql
//							.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time from tab_gw_device a where 1=1");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//				if (true == StringUtil.IsEmpty(device_status)
//						|| "1".equals(device_status))
//				{
//					sql.append(" and a.device_status=1");
//				}
//				if ("0".equals(device_status))
//				{
//					sql.append(" and a.device_status=0");
//				}
//				if (cityIdList != null)
//				{
//					sql.append(" and a.city_id in (").append(
//							StringUtils.weave(cityIdList)).append(")");
//				}
//				if (device_serialnumber != null && !"".equals(device_serialnumber))
//				{
//					if(device_serialnumber.length()>5){
//						sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
//					}
//					sql.append(" and a.device_serialnumber like'%").append(
//							device_serialnumber).append("'");
//				}
//				if (loopback_ip != null && !"".equals(loopback_ip))
//				{
//					sql.append(" and a.loopback_ip like'%").append(loopback_ip).append(
//							"%'");
//				}
//			}
//			sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");
//		}
//		// 查询企业网关设备
//		else
//		{
//			// 查询绑定设备
//			if ("2".equals(bindState))
//			{
//				if (curUser.getUser().isAdmin())
//				{
//					sql
//							.append(
//									"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,b.user_id,b.username")
//							.append(
//									" from tab_gw_device a,tab_egwcustomer b where b.user_state in('1','2') and a.device_id=b.device_id");
//				}
//				else
//				{
//					sql
//							.append(
//									"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,b.user_id,b.username,c.area_id from tab_gw_device a,tab_egwcustomer b,tab_gw_res_area c where a.device_id=c.res_id and c.res_type=1 and c.area_id=")
//							.append(areaId)
//							.append(
//									" and b.user_state in('1','2') and a.device_id=b.device_id");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//				if ("2".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and b.binddate>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and b.binddate<").append(endtime1);
//					}
//				}
//			}
//			else if ("1".equals(bindState))
//			{
//				sql
//						.append(
//								"select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time")
//						.append(" from tab_gw_device a where a.cpe_allocatedstatus=0");
//				if (cityIdList != null)
//				{
//					sql.append(" and a.city_id in (").append(
//							StringUtils.weave(cityIdList)).append(")");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//			}
//			else
//			{
//				if ("2".equals(timeType))
//				{
//					sql
//							.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time from tab_gw_device a left join tab_egwcustomer b on a.device_id=b.device_id where 1=1");
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and b.binddate>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and b.binddate<").append(endtime1);
//					}
//				}
//				else
//				{
//					sql
//							.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time from tab_gw_device a where 1=1");
//				}
//				if ("1".equals(timeType))
//				{
//					if (false == StringUtil.IsEmpty(starttime1))
//					{
//						sql.append(" and a.complete_time>=").append(starttime1);
//					}
//					if (false == StringUtil.IsEmpty(endtime1))
//					{
//						sql.append(" and a.complete_time<").append(endtime1);
//					}
//				}
//				if (true == StringUtil.IsEmpty(device_status)
//						|| "1".equals(device_status))
//				{
//					sql.append(" and a.device_status=1");
//				}
//				if ("0".equals(device_status))
//				{
//					sql.append(" and a.device_status=0");
//				}
//				if (cityIdList != null)
//				{
//					sql.append(" and a.city_id in (").append(
//							StringUtils.weave(cityIdList)).append(")");
//				}
//				if (device_serialnumber != null && !"".equals(device_serialnumber))
//				{
//					if(device_serialnumber.length()>5){
//						sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
//					}
//					sql.append(" and a.device_serialnumber like'%").append(
//							device_serialnumber).append("'");
//				}
//				if (loopback_ip != null && !"".equals(loopback_ip))
//				{
//					sql.append(" and a.loopback_ip like'%").append(loopback_ip).append(
//							"%'");
//				}
//			}
//			sql.append(" order by a.city_id, a.vendor_id, a.device_model_id ");
//		}
//		PrepareSQL psql = new PrepareSQL(sql.toString());
//    	psql.getSQL();
//		cityMap = CityDAO.getCityIdCityNameMap();
//		vendorMap = VendorModelVersionDAO.getVendorMap();
//		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
//		cityIdList = null;
//		list = jt.query(sql.toString(), new RowMapper()
//		{
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException
//			{
//				Map<String, String> map = new HashMap<String, String>();
//				long time = 0;
//				DateTimeUtil dateTimeUtil = null;
//				map.put("device_id", rs.getString("device_id"));
//				String city_id = rs.getString("city_id");
//				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
//				if (false == StringUtil.IsEmpty(city_name))
//				{
//					map.put("city_name", city_name);
//				}
//				else
//				{
//					map.put("city_name", "");
//				}
//				String vendor_id = rs.getString("vendor_id");
//				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
//				if (false == StringUtil.IsEmpty(vendor_add))
//				{
//					map.put("vendor_add", vendor_add);
//				}
//				else
//				{
//					map.put("vendor_add", "");
//				}
//				String device_model_id = rs.getString("device_model_id");
//				String device_model = StringUtil.getStringValue(deviceModelMap
//						.get(device_model_id));
//				if (false == StringUtil.IsEmpty(device_model))
//				{
//					map.put("device_model", device_model);
//				}
//				else
//				{
//					map.put("device_model", "");
//				}
//				map.put("oui", rs.getString("oui"));
//				map.put("device_serialnumber", rs.getString("device_serialnumber"));
//				map.put("device", rs.getString("oui") + "-"
//						+ rs.getString("device_serialnumber"));
//				map.put("loopback_ip", rs.getString("loopback_ip"));
//				// 将complete_time转换成时间
//				try
//				{
//					time = Long.parseLong(rs.getString("complete_time"));
//					dateTimeUtil = new DateTimeUtil(time * 1000);
//					map.put("complete_time", dateTimeUtil.getDate());
//				}
//				catch (NumberFormatException e)
//				{
//					map.put("complete_time", "");
//				}
//				catch (Exception e)
//				{
//					map.put("complete_time", "");
//				}
//				if ("2".equals(bindState))
//				{
//					// 将binddate转换成时间
//					try
//					{
//						time = Long.parseLong(rs.getString("binddate"));
//						dateTimeUtil = new DateTimeUtil(time * 1000);
//						map.put("binddate", dateTimeUtil.getDate());
//					}
//					catch (NumberFormatException e)
//					{
//						map.put("binddate", "");
//					}
//					catch (Exception e)
//					{
//						map.put("binddate", "");
//					}
//					map.put("user_id", rs.getString("user_id"));
//					map.put("username", rs.getString("username"));
//				}
//				return map;
//			}
//		});
//		cityMap = null;
//		vendorMap = null;
//		deviceModelMap = null;
//		return list;
//	}
	
	/**
	 * 导出设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 18, 2009
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDeviceExcel(final String infoType, UserRes curUser,
			final String bindState, String timeType, String starttime1, String endtime1,
			String device_serialnumber, String loopback_ip, String device_logicsn, String device_status)
	{
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
		List<Map> list;
		String cityId = curUser.getCityId();
		long areaId = curUser.getAreaId();
		ArrayList<String> cityIdList = null;
		if ("00".equals(cityId))
		{
			cityIdList = null;
		}
		else
		{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if("2".equals(infoType)&&"1".equals(bindState)){				
				List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
				cityIdList.addAll(list1);
			}
		}
		StringBuffer sql = new StringBuffer();
		// 查询家庭网关设备
		if ("1".equals(infoType))
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,");
					sql.append("b.binddate,b.user_id,b.username,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_hgwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1");
				}
				else
				{
				/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
					} */

					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,");
					sql.append("b.user_id,b.username,c.area_id,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_hgwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=1");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
				sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,a.device_id_ex ");
				sql.append("from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=1");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				if ("2".equals(timeType))
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,a.device_id_ex ");
					sql.append("from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=1 ");
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,a.device_id_ex ");
					sql.append("from tab_gw_device a where a.gw_type=1");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				/*
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				*/
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (device_serialnumber != null && !"".equals(device_serialnumber))
				{
					if(device_serialnumber.length()>5){
						sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
					}
					sql.append(" and a.device_serialnumber like'%").append(
							device_serialnumber).append("'");
				}
				if (loopback_ip != null && !"".equals(loopback_ip))
				{
					sql.append(" and a.loopback_ip ='").append(loopback_ip).append(
							"'");
				}
				if (!StringUtil.IsEmpty(device_logicsn))
				{
					sql.append(" and a.device_id_ex like '%").append(device_logicsn).append("%'");
				}
			}
			sql.append(" and a.device_status=1 order by a.city_id, a.vendor_id, a.device_model_id ");
		}
		// 查询企业网关设备
		else
		{
			// 查询绑定设备
			if ("2".equals(bindState))
			{
				if (curUser.getUser().isAdmin())
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,");
					sql.append("b.binddate,b.user_id,b.username, a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_egwcustomer b ");
					sql.append("where b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2");
				}
				else
				{
				/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
						//TODO wait
					} */
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,b.binddate,");
					sql.append("b.user_id,b.username,c.area_id,a.device_id_ex ");
					sql.append("from tab_gw_device a,tab_egwcustomer b,tab_gw_res_area c ");
					sql.append("where a.device_id=c.res_id and c.res_type=1 ");
					sql.append("and c.area_id="+areaId);
					sql.append(" and b.user_state in('1','2') and a.device_id=b.device_id and a.gw_type=2 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if ("2".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
			}
			else if ("1".equals(bindState))
			{
				sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
				sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,a.device_id_ex ");
				sql.append("from tab_gw_device a where a.cpe_allocatedstatus=0 and a.gw_type=2 ");
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
			}
			else
			{
				if ("2".equals(timeType))
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time,a.device_id_ex ");
					sql.append("from tab_gw_device a left join tab_egwcustomer b on a.device_id=b.device_id ");
					sql.append("where a.gw_type=2 ");
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and b.binddate>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and b.binddate<").append(endtime1);
					}
				}
				else
				{
					sql.append("select a.device_id,a.city_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
					sql.append("a.oui,a.device_serialnumber,a.loopback_ip,a.complete_time, a.device_id_ex ");
					sql.append("from tab_gw_device a where a.gw_type=2 ");
				}
				if ("1".equals(timeType))
				{
					if (false == StringUtil.IsEmpty(starttime1))
					{
						sql.append(" and a.complete_time>=").append(starttime1);
					}
					if (false == StringUtil.IsEmpty(endtime1))
					{
						sql.append(" and a.complete_time<").append(endtime1);
					}
				}
				if (true == StringUtil.IsEmpty(device_status)
						|| "1".equals(device_status))
				{
					sql.append(" and a.device_status=1");
				}
				if ("0".equals(device_status))
				{
					sql.append(" and a.device_status=0");
				}
				if (cityIdList != null)
				{
					sql.append(" and a.city_id in (").append(
							StringUtils.weave(cityIdList)).append(")");
				}
				if (device_serialnumber != null && !"".equals(device_serialnumber))
				{
					if(device_serialnumber.length()>5){
						sql.append(" and a.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
					}
					sql.append(" and a.device_serialnumber like'%").append(
							device_serialnumber).append("'");
				}
				if (loopback_ip != null && !"".equals(loopback_ip))
				{
					sql.append(" and a.loopback_ip ='").append(loopback_ip).append(
							"'");
				}
			}
			sql.append(" and a.device_status=1 order by a.city_id, a.vendor_id, a.device_model_id ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		hardwareversionMap = VendorModelVersionDAO.getDeviceTypeInfoMap();
		cityIdList = null;
		list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				long time = 0;
				DateTimeUtil dateTimeUtil = null;
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					map.put("vendor_add", vendor_add);
				}
				else
				{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
//				if ("1".equals(infoType))  // 注释 by zhangchy 2012-08-31 (ITMS与BBMS融合后，使用同一个tab_gw_device)
//				{
					// 家庭网关才有这个字段 (将if语句注释了，因为ITMS与BBMS融合后，使用同一个tab_gw_device表 by zhangchy 2012-08-31)
					map.put("device_id_ex", rs.getString("device_id_ex")==null ? "" : rs.getString("device_id_ex"));
//				}
				map.put("device", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				// 将complete_time转换成时间
				try
				{
					time = Long.parseLong(rs.getString("complete_time"));
					dateTimeUtil = new DateTimeUtil(time * 1000);
					map.put("complete_time", dateTimeUtil.getDate());
					//软件版本
					map.put("software", softwareversionMap.get(rs.getString("devicetype_id")));
				}
				catch (NumberFormatException e)
				{
					map.put("complete_time", "");
				}
				catch (Exception e)
				{
					map.put("complete_time", "");
				}
				if ("2".equals(bindState))
				{
					// 将binddate转换成时间
					try
					{
						time = Long.parseLong(rs.getString("binddate"));
						dateTimeUtil = new DateTimeUtil(time * 1000);
						map.put("binddate", dateTimeUtil.getDate());
					}
					catch (NumberFormatException e)
					{
						map.put("binddate", "");
					}
					catch (Exception e)
					{
						map.put("binddate", "");
					}
					map.put("user_id", rs.getString("user_id"));
					map.put("username", rs.getString("username"));
				}
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		softwareversionMap = null;
		hardwareversionMap = null;
		return list;
	}

	public List<Map> getImpInitDeviceList(int curPage_splitPage, int num_splitPage, String device_serialnumber)
	{
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select device_id,oui,device_serialnumber,status ");
		}else{
			sql.append("select * ");
		}
		sql.append("from tab_gw_device_init where 1=1 ");		
		if(!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append("and dev_sub_sn = '"+device_serialnumber.substring(device_serialnumber.length()-6)
						+"' and device_serialnumber like '%"+device_serialnumber+"'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
    	
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						
						map.put("device_id", rs.getString("device_id"));
					
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
						if ("2".equals(rs.getString("status"))) 
						{
							map.put("status", "已上线未绑定");
						}
						else if ("3".equals(rs.getString("status")))
						{
							map.put("status", "已使用已绑定");
						}
						else if ("4".equals(rs.getString("status")))
						{
							map.put("status", "曾经使用");
						}
						else 
						{
							map.put("status", "未使用");
						}
						return map;
					}
				});
		
		return list;
	}

	public int getImpInitDeviceCount(int curPage_splitPage,int num_splitPage, String device_serialnumber)
	{
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append(" select count(*) from tab_gw_device_init where 1=1 ");
		}else{
			sql.append(" select count(1) from tab_gw_device_init where 1=1 ");
		}
		
		if(!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append(" and device_serialnumber ='"+device_serialnumber+"'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
	public int getTotal(){
		return total;
	}
	
	public void setTotal(int total){
		this.total = total;
	}
}