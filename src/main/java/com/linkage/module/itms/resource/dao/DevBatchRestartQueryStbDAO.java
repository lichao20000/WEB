package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


@SuppressWarnings({"unchecked","rawtypes"})
public class DevBatchRestartQueryStbDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DevBatchRestartQueryStbDAO.class);
	private Map<String, String> modelMap=new HashMap<String,String>();
	private Map<String, String> versionMap=new HashMap<String,String>();
	private Map<String, String> vendorNameMap=new HashMap<String,String>();
	private Map<String, String> vendorNameAddMap=new HashMap<String,String>();
	private Map<String, String> reasonMap = new HashMap<String, String>();

	private String instArea=Global.instAreaShortName;

	/**光猫系统*/
	private static final String SYSTEM_ITMS = "1";
	/**宁夏电信*/
	private static final String NXDX = "nx_dx";
	/**内蒙古电信*/
	private static final String NMGDX = "nmg_dx";
	/**省中心*/
	private static final String CENTER_CITY = "00";
	// 无效的city_id
	private static final String INVALID_CITY = "-1";

	/**机顶盒表前缀*/
	private static final String STB_NAMEFIX = "stb_";

	/**设备序列号长度必须大于5*/
	private static final int SNLESS = 5;

	public DevBatchRestartQueryStbDAO()
	{
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}
	
	
	public List<Map> devBatchRestartQueryInfo(String cityId,
			String startTime, String endTime, int curPage_splitPage,
			int num_splitPage, final String gw_type, String gwShareCityId,
			String gwShareVendorId, String gwShareDeviceModelId,
			String gwShareDevicetypeId,String loid,String sn,String servAccount)
	{
		logger.debug("devBatchRestartQueryInfo()");
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gw_type)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.task_id,a.add_time,a.restart_status,a.restart_time,");
		sql.append("b.device_serialnumber,b.vendor_id,b.devicetype_id,");
		sql.append("b.device_model_id,b.city_id,c.city_name ");
		if(NXDX.equals(instArea)){
			if (!SYSTEM_ITMS.equals(gw_type)) {
				sql.append(",d.serv_account ");
			}else {
				sql.append(",d.username as loid ");
			}
		}
		sql.append("from "+ nameFix +"tab_dev_batch_restart a,"+ nameFix +"tab_gw_device b,tab_city c ");
		
		if(NXDX.equals(instArea)){
			if (!SYSTEM_ITMS.equals(gw_type)) {
				sql.append(",stb_tab_customer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.customer_id=d.customer_id ");
				
				if(!StringUtil.IsEmpty(servAccount)){
					sql.append("and d.serv_account='"+servAccount.trim()+"' ");
				}
			}else {
				sql.append(",tab_hgwcustomer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.device_id=d.device_id ");
				
				if(!StringUtil.IsEmpty(loid)){
					sql.append("and d.username='"+loid.trim()+"' ");
				}
			}
			
			if(!StringUtil.IsEmpty(sn)){
				if(sn.length()>SNLESS){
					sql.append("and b.dev_sub_sn='"+sn.substring(sn.length()-6, sn.length())+"' ");
				}
				sql.append("and b.device_serialnumber like '%"+sn+"' ");
			}
		}else{
			sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
		}
		
		sql.append(getSqlConditions(gwShareCityId,cityId,startTime,
				endTime,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId));
		sql.append(" order by a.task_id,b.city_id");
		
		vendorNameMap=getData(nameFix+"tab_vendor","vendor_id","vendor_name");
		vendorNameAddMap=getData(nameFix+"tab_vendor","vendor_id","vendor_add");
		modelMap=getData(nameFix+"gw_device_model","device_model_id","device_model");
		versionMap=getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
		
		List<Map> list = querySP(sql.getSQL(), 
				(curPage_splitPage - 1) * num_splitPage+1, num_splitPage, new RowMapper() 
		{
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_id",rs.getString("task_id"));
				map.put("device_serialnumber",rs.getString("device_serialnumber"));
				map.put("city_name", rs.getString("city_name"));
				String enabled =rs.getString("restart_status");
				String message = "";
				if (!StringUtil.IsEmpty(enabled)) {
					if ("-1".equals(enabled)) {
						message = "失败";
					} else if ("0".equals(enabled)) {
						message = "未做";
					} else if ("1".equals(enabled)) {
						message = "成功";
					} else if ("99".equals(enabled)) {
						message = "在设备表中不存在";
					}
				}
				map.put("restart_status", message);
				map.put("add_time", getTime(rs.getLong("add_time")));
				map.put("restart_time", getTime(rs.getLong("restart_time")));
				String vname=vendorNameAddMap.get(rs.getString("vendor_id"));
				if(StringUtil.IsEmpty(vname)){
					vname=vendorNameMap.get(rs.getString("vendor_id"));
				}
				map.put("vendor_name",vname);
				map.put("device_model", modelMap.get(rs.getString("device_model_id")));
				map.put("softwareversion", versionMap.get(rs.getString("devicetype_id")));
				
				if(NXDX.equals(instArea)){
					if (!SYSTEM_ITMS.equals(gw_type)) {
						map.put("serv_account", rs.getString("serv_account"));
					}else {
						map.put("loid", rs.getString("loid"));
					}
				}
				return map;
			}
		});
		return list;
	}

	public int countDevBatchRestartQueryInfo(String cityId, String startTime,
			String endTime, int curPage_splitPage, int num_splitPage,
			String gwType, String gwShareCityId, String gwShareVendorId,
			String gwShareDeviceModelId, String gwShareDevicetypeId,String loid,String sn,String servAccount )
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL sql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from ");
		}
		else {
			sql.append("select count(1) from ");
		}
		sql.append(nameFix +"tab_dev_batch_restart a,"+ nameFix +"tab_gw_device b,tab_city c ");
		
		if(NXDX.equals(instArea)){
			if (!SYSTEM_ITMS.equals(gwType)) {
				sql.append(",stb_tab_customer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.customer_id=d.customer_id ");
				
				if(!StringUtil.IsEmpty(servAccount)){
					sql.append("and d.serv_account='"+servAccount.trim()+"' ");
				}
			}else {
				sql.append(",tab_hgwcustomer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.device_id=d.device_id ");
				if(!StringUtil.IsEmpty(loid)){
					sql.append("and d.username='"+loid.trim()+"' ");
				}
			}
			if(!StringUtil.IsEmpty(sn)){
				if(sn.length()>SNLESS){
					sql.append("and b.dev_sub_sn='"+sn.substring(sn.length()-6, sn.length())+"' ");
				}
				sql.append("and b.device_serialnumber like '%"+sn+"' ");
			}
		}else{
			sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
		}
		
		sql.append(getSqlConditions(gwShareCityId,cityId,startTime,
				endTime,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId));
		
		int total = jt.queryForInt(sql.getSQL());
		if (total % num_splitPage == 0) {
			return total / num_splitPage;
		} 
		return total / num_splitPage + 1;
	}

	/**
	 * @param city_id二级属地
	 * @param gwShare_cityId三级属地
	 */
	public List<Map> devBatchRestartQueryExcel(String cityId, String start_time,
			String endTime, String gwType, String gwShareCityId,
			String gwShareVendorId, String gwShareDeviceModelId,
			String gwShareDevicetypeId,String loid,String sn,String servAccount)
	{
		logger.debug("voiceDeviceQueryInfo()");
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.task_id,a.add_time,a.restart_status,a.restart_time,");
		sql.append("b.device_serialnumber,b.vendor_id,b.devicetype_id,");
		sql.append("b.device_model_id,b.city_id,c.city_name ");
		if(NXDX.equals(instArea)){
			if (!SYSTEM_ITMS.equals(gwType)) {
				sql.append(",d.serv_account ");
			}else {
				sql.append(",d.username as loid ");
			}
		}
		sql.append("from "+ nameFix +"tab_dev_batch_restart a,"+ nameFix +"tab_gw_device b,tab_city c");
		
		if(NXDX.equals(instArea)){
			
			if (!SYSTEM_ITMS.equals(gwType)) {
				sql.append(",stb_tab_customer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.customer_id=d.customer_id and a.restart_status != -2 ");
				
				if(!StringUtil.IsEmpty(servAccount)){
					sql.append("and d.serv_account='"+servAccount.trim()+"' ");
				}
			}else {
				sql.append(",tab_hgwcustomer d ");
				sql.append("where a.device_id=b.device_id and b.city_id=c.city_id ");
				sql.append("and b.device_id=d.device_id and a.restart_status != -2 ");
				if(!StringUtil.IsEmpty(loid)){
					sql.append("and d.username='"+loid.trim()+"' ");
				}
			}
			
			if(!StringUtil.IsEmpty(sn)){
				if(sn.length()>SNLESS){
					sql.append("and b.dev_sub_sn='"+sn.substring(sn.length()-6, sn.length())+"' ");
				}
				sql.append("and b.device_serialnumber like '%"+sn+"' ");
			}
		}else{
			sql.append(" where a.device_id=b.device_id and b.city_id=c.city_id ");
		}
		sql.append(getSqlConditions(gwShareCityId,cityId,start_time,
				endTime,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId));
		sql.append(" order by a.task_id,b.city_id");
		
		vendorNameMap=getData(nameFix+"tab_vendor","vendor_id","vendor_name");
		vendorNameAddMap=getData(nameFix+"tab_vendor","vendor_id","vendor_add");
		modelMap=getData(nameFix+"gw_device_model","device_model_id","device_model");
		versionMap=getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
		
		List<Map> list=jt.queryForList(sql.getSQL());
		if (null != list && list.size() > 0) {
			for (Map<String,String> map:list) 
			{
				map.put("task_id",StringUtil.getStringValue(map,"task_id"));
				map.put("device_serialnumber",StringUtil.getStringValue(map,"device_serialnumber"));
				String enabled = StringUtil.getStringValue(map,"restart_status");
				String message = "";
				if (!StringUtil.IsEmpty(enabled)) {
					if ("-1".equals(enabled)) {
						message = "失败";
					} else if ("0".equals(enabled)) {
						message = "未做";
					} else if ("1".equals(enabled)) {
						message = "成功";
					} else if ("99".equals(enabled)) {
						message = "在设备表中不存在";
					}
				}
				map.put("restart_status", message);
				map.put("add_time",getTime(StringUtil.getLongValue(map,"add_time")));
				map.put("restart_time",getTime(StringUtil.getLongValue(map,"restart_time")));
				String vname=vendorNameAddMap.get(StringUtil.getStringValue(map,"vendor_id"));
				if(StringUtil.IsEmpty(vname)){
					vname=vendorNameMap.get(StringUtil.getStringValue(map,"vendor_id"));
				}
				map.put("vendor_name",vname);
				map.put("device_model", modelMap.get(StringUtil.getStringValue(map,"device_model_id")));
				map.put("softwareversion", versionMap.get(StringUtil.getStringValue(map,"devicetype_id")));
				map.put("city_name", StringUtil.getStringValue(map,"city_name"));
				
				if(NXDX.equals(instArea)){
					if (!SYSTEM_ITMS.equals(gwType)) {
						map.put("serv_account", StringUtil.getStringValue(map,"serv_account"));
					}else {
						map.put("loid", StringUtil.getStringValue(map,"loid"));
					}
				}
			}
		}
		return list;
	}
	
	
	
	
	public List devBatchRestartQueryInfoStat(String cityId,String startTime,String endTime,
			String gwType,String vendorId,String deviceModelId,String devicetypeId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,count(1) as numbers");
		psql.append(" from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status != -2 ");

		
		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id");
		return DBOperation.getRecords(psql.getSQL());
	}

	public List devBatchRestartQueryInfoCity(String cityId,String startTime,String endTime,
											 String gwType,String vendorId,String deviceModelId,String devicetypeId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		//宁夏电信增加属地维度的统计
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,b.city_id,count(1) as numbers");
		psql.append(" from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status != -2 ");


		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id,b.city_id");
		return DBOperation.getRecords(psql.getSQL());
	}

	public List devNotRestartQueryInfoCity(String cityId,String startTime,String endTime,
											 String gwType,String vendorId,String deviceModelId,String devicetypeId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		//宁夏电信增加属地维度的统计
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,b.city_id,count(1) as numbers");
		psql.append(" from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status = -2 ");


		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id,b.city_id");
		return DBOperation.getRecords(psql.getSQL());
	}

	public List devRestartNumByCityId(String cityId, String startTime, String endTime,
										   String gwType, String vendorId, String deviceModelId, String devicetypeId)
	{
		String nameFix = "";
		if (!SYSTEM_ITMS.equals(gwType)) {
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		//宁夏电信增加属地维度的统计
		psql.append("select b.city_id, count(*) as numbers");
		psql.append(" from " + nameFix + "tab_dev_batch_restart a," + nameFix + "tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status != -2 ");

		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in (" + StringUtils.weave(cityIdList) + ")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime, endTime, vendorId, deviceModelId, devicetypeId));
		psql.append(" group by b.city_id");
		return DBOperation.getRecords(psql.getSQL());
	}

	public List devNotRestartNumByCityId(String cityId, String startTime, String endTime,
										   String gwType, String vendorId, String deviceModelId, String devicetypeId)
	{
		String nameFix = "";
		if (!SYSTEM_ITMS.equals(gwType)) {
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		//宁夏电信增加属地维度的统计
		psql.append("select b.city_id, count(*) as numbers");
		psql.append(" from " + nameFix + "tab_dev_batch_restart a," + nameFix + "tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status = -2 ");

		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in (" + StringUtils.weave(cityIdList) + ")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		psql.append(" group by b.city_id");
		return DBOperation.getRecords(psql.getSQL());
	}


	public List<Map> getDetail(String gwType,String startTime,String endTime,String vendorId,
			String deviceModelId,String devicetypeId,int curPage_splitPage,int num_splitPage)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if (!SYSTEM_ITMS.equals(gwType)) {
			// teledb
			if (DBUtil.GetDB() == 3) {
				psql.append("select x.vendor_id, x.device_model_id, x.devicetype_id, x.city_id, x.device_serialnumber, x.restart_status, x.device_id, c.serv_account as loid from ");
			}
			else {
				psql.append("select x.*,c.serv_account as loid from ");
			}
			psql.append("(select a.restart_status,b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id,b.customer_id ");
			psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b ");
			psql.append("where a.device_id=b.device_id ");
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join stb_tab_customer c ");
			psql.append("on x.customer_id=c.customer_id ");
		}else {
			// teledb
			if (DBUtil.GetDB() == 3) {
				psql.append("select x.vendor_id, x.device_model_id, x.devicetype_id, x.city_id, x.device_serialnumber, x.restart_status, x.device_id, c.username as loid from ");
			}
			else {
				psql.append("select x.*,c.username as loid from ");
			}
			psql.append("(select a.restart_status,b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id ");
			psql.append("from  tab_dev_batch_restart a, tab_gw_device b ");
			psql.append("where a.device_id=b.device_id ");
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join tab_hgwcustomer c ");
			psql.append("on x.device_id=c.device_id ");
		}
		
		vendorNameMap=getData(nameFix+"tab_vendor","vendor_id","vendor_name");
		vendorNameAddMap=getData(nameFix+"tab_vendor","vendor_id","vendor_add");
		modelMap=getData(nameFix+"gw_device_model","device_model_id","device_model");
		versionMap=getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");

		if(num_splitPage==-1)
		{
			//导出excel，查询所有数据
			List<Map> list=jt.queryForList(psql.getSQL());
			if (null != list && list.size() > 0) {
				for (Map<String,String> map:list)
				{
					map=getMap(map,StringUtil.getStringValue(map,"vendor_id"),
							StringUtil.getStringValue(map,"device_model_id"),
							StringUtil.getStringValue(map,"devicetype_id"),
							StringUtil.getStringValue(map,"city_id"),
							StringUtil.getStringValue(map,"device_serialnumber"),
							StringUtil.getStringValue(map,"loid"),
							StringUtil.getStringValue(map,"restart_status"),
							StringUtil.getStringValue(map,"device_id"));
				}
			}
			return list;
		}

		List<Map> list = querySP(psql.getSQL(),
				(curPage_splitPage - 1) * num_splitPage+1, num_splitPage, new RowMapper()
		{
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return getMap(new HashMap<String, String>(),
								rs.getString("vendor_id"),rs.getString("device_model_id"),
								rs.getString("devicetype_id"),rs.getString("city_id"),
								rs.getString("device_serialnumber"),rs.getString("loid"),
								rs.getString("restart_status"),
								rs.getString("device_id"));
			}
		});
		return list;
	}

	public List<Map> getDetail4NX(String gwType,String startTime,String endTime,String vendorId,
							   String deviceModelId,String devicetypeId,int curPage_splitPage,int num_splitPage,String cityId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if (!SYSTEM_ITMS.equals(gwType)) {
			psql.append("select x.*,c.serv_account as loid from ");
			psql.append("(select a.restart_status,b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id,b.customer_id ");
			psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b,tab_city c ");
			psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status != -2 ");
			if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
				psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
			}
			if(CENTER_CITY.equals(cityId)){
				psql.append(" and b.city_id='" + cityId + "'");
			}
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join stb_tab_customer c ");
			psql.append("on x.customer_id=c.customer_id ");
		}else {
			psql.append("select x.*,c.username as loid from ");
			psql.append("(select a.restart_status,b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id ");
			psql.append("from  tab_dev_batch_restart a, tab_gw_device b,tab_city c ");
			psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status != -2");
			if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
				psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
			}
			if(CENTER_CITY.equals(cityId)){
				psql.append(" and b.city_id='" + cityId + "'");
			}
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join tab_hgwcustomer c ");
			psql.append("on x.device_id=c.device_id ");
		}

		vendorNameMap=getData(nameFix+"tab_vendor","vendor_id","vendor_name");
		vendorNameAddMap=getData(nameFix+"tab_vendor","vendor_id","vendor_add");
		modelMap=getData(nameFix+"gw_device_model","device_model_id","device_model");
		versionMap=getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");

		if(num_splitPage==-1)
		{
			//导出excel，查询所有数据
			List<Map> list=jt.queryForList(psql.getSQL());
			if (null != list && list.size() > 0) {
				for (Map<String,String> map:list)
				{
					map=getMap(map,StringUtil.getStringValue(map,"vendor_id"),
							StringUtil.getStringValue(map,"device_model_id"),
							StringUtil.getStringValue(map,"devicetype_id"),
							StringUtil.getStringValue(map,"city_id"),
							StringUtil.getStringValue(map,"device_serialnumber"),
							StringUtil.getStringValue(map,"loid"),
							StringUtil.getStringValue(map,"restart_status"),
							StringUtil.getStringValue(map,"device_id"));
				}
			}
			return list;
		}

		List<Map> list = querySP(psql.getSQL(),
				(curPage_splitPage - 1) * num_splitPage+1, num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						return getMap(new HashMap<String, String>(),
								rs.getString("vendor_id"),rs.getString("device_model_id"),
								rs.getString("devicetype_id"),rs.getString("city_id"),
								rs.getString("device_serialnumber"),rs.getString("loid"),
								rs.getString("restart_status"),
								rs.getString("device_id"));
					}
				});
		return list;
	}
	
	public int count(String gwType,String startTime,String endTime,String vendorId,
			String deviceModelId,String devicetypeId,int num_splitPage)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		
		psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b ");
		psql.append("where a.device_id=b.device_id  ");
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		
		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0) {
			return total / num_splitPage;
		} 
		return total / num_splitPage + 1;
	}

	public int count4NX(String gwType,String startTime,String endTime,String vendorId,
					 String deviceModelId,String devicetypeId,int num_splitPage,String cityId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b,tab_city c ");
		psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status != -2 ");
		if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
			psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
		}
		if(CENTER_CITY.equals(cityId)){
			psql.append(" and b.city_id='" + cityId + "'");
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));

		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0) {
			return total / num_splitPage;
		}
		return total / num_splitPage + 1;
	}

	private Map<String,String> getMap(Map map,String vendorId,String deviceModelId,String devicetypeId,
			String cityId,String deviceSerialnumber,String loid,String restartStatus,String deviceId)
	{
		String vname=vendorNameAddMap.get(vendorId);
		if(StringUtil.IsEmpty(vname)){
			vname=vendorNameMap.get(StringUtil.getStringValue(map,"vendor_id"));
		}
		map.put("vendor_name",vname);
		map.put("device_model", modelMap.get(deviceModelId));
		map.put("softwareversion", versionMap.get(devicetypeId));
		map.put("city_name", CityDAO.getCityName(cityId));
		map.put("device_serialnumber",deviceSerialnumber);
		map.put("loid", loid);
		String message = "";
		if (!StringUtil.IsEmpty(restartStatus)) {
			if ("-1".equals(restartStatus)) {
				message = "失败";
			} else if ("0".equals(restartStatus)) {
				message = "未做";
			} else if ("1".equals(restartStatus)) {
				message = "成功";
			} else if ("99".equals(restartStatus)) {
				message = "在设备表中不存在";
			}
		}
		map.put("restart_status", message);
		map.put("device_id", deviceId);
		return map;
	}
	
	/**
	 * 拼接sql条件
	 */
	private String getSqlConditions(String cityId,String city_id,String startTime,
			String endTime,String vendorId,String deviceModelId,String devicetypeId)
	{
		StringBuffer sql=new StringBuffer();
		if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)){
			if(NMGDX.equals(instArea.toLowerCase())
					|| NXDX.equals(instArea)){
				if(!CENTER_CITY.equals(cityId)){
					sql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
				}
			}else{
				sql.append(" and b.city_id='" + cityId + "'");
			}
		}else{
			if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id)
					&& !CENTER_CITY.equals(city_id)) {
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
				cityIdList = null;
			}
		}
		sql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		
		return sql.toString();
	}
	
	private String getSqlConditions(String startTime,String endTime,String vendorId,
			String deviceModelId,String devicetypeId)
	{
		StringBuffer psql=new StringBuffer();
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append(" and a.restart_time>="+startTime);
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append(" and a.restart_time<="+endTime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			psql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			psql.append(" and b.device_model_id='"+deviceModelId+"'");
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append(" and b.devicetype_id="+devicetypeId);
		}
		return psql.toString();
	}
	
	/**
	 * 加载表数据
	 */
	public Map<String,String> getData(String table,String key,String value)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select "+key+","+value+" from "+table+" order by "+key);
		
		List<HashMap<String,String>> list = DBOperation.getRecords(psql.getSQL());
		Map<String,String> result_map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty())
		{
			for(Map map:list){
				result_map.put(StringUtil.getStringValue(map,key),StringUtil.getStringValue(map,value));
			}
		}
		return result_map;
	}

	public List devNotRestartQueryInfoStat(String cityId,String startTime,String endTime,
											 String gwType,String vendorId,String deviceModelId,String devicetypeId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		//宁夏电信增加 -2表示大数据分析不需要做重启的
		psql.append("select b.vendor_id,b.device_model_id,b.devicetype_id,count(1) as numbers");
		psql.append(" from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b");
		psql.append(" where a.device_id=b.device_id and a.restart_status = -2");


		if (!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId)
				&& !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
		psql.append(" group by b.vendor_id,b.device_model_id,b.devicetype_id");
		return DBOperation.getRecords(psql.getSQL());
	}

	public List<Map> getNotRestartDetail(String gwType,String startTime,String endTime,String vendorId,
								  String deviceModelId,String devicetypeId,int curPage_splitPage,int num_splitPage,String cityId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if (!SYSTEM_ITMS.equals(gwType)) {
			psql.append("select x.*,c.serv_account as loid from ");
			psql.append("(select b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id,b.customer_id ");
			psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b,tab_city c ");
			psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status = -2 ");
			if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
				psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
			}
			if(CENTER_CITY.equals(cityId)){
				psql.append(" and b.city_id='" + cityId + "'");
			}
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join stb_tab_customer c ");
			psql.append("on x.customer_id=c.customer_id ");
		}else {
			psql.append("select x.*,c.username as loid from ");
			psql.append("(select a.restart_status,b.device_id,b.device_serialnumber,b.vendor_id,");
			psql.append("b.devicetype_id,b.device_model_id,b.city_id ");
			psql.append("from  tab_dev_batch_restart a, tab_gw_device b,tab_city c ");
			psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status = -2 ");
			if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
				psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
			}
			if(CENTER_CITY.equals(cityId)){
				psql.append(" and b.city_id='" + cityId + "'");
			}
			psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));
			psql.append(") x left join tab_hgwcustomer c ");
			psql.append("on x.device_id=c.device_id ");
		}

		vendorNameMap=getData(nameFix+"tab_vendor","vendor_id","vendor_name");
		vendorNameAddMap=getData(nameFix+"tab_vendor","vendor_id","vendor_add");
		modelMap=getData(nameFix+"gw_device_model","device_model_id","device_model");
		versionMap=getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");

		if(num_splitPage==-1)
		{
			//导出excel，查询所有数据
			List<Map> list=jt.queryForList(psql.getSQL());
			if (null != list && list.size() > 0) {
				for (Map<String,String> map : list)
				{
					map=getMap(map,StringUtil.getStringValue(map,"vendor_id"),
							StringUtil.getStringValue(map,"device_model_id"),
							StringUtil.getStringValue(map,"devicetype_id"),
							StringUtil.getStringValue(map,"city_id"),
							StringUtil.getStringValue(map,"device_serialnumber"),
							StringUtil.getStringValue(map,"loid"),
							StringUtil.getStringValue(map,"device_id"));
				}
			}
			return list;
		}

		List<Map> list = querySP(psql.getSQL(),
				(curPage_splitPage - 1) * num_splitPage+1, num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						return getMap(new HashMap<String, String>(),
								rs.getString("vendor_id"),rs.getString("device_model_id"),
								rs.getString("devicetype_id"),rs.getString("city_id"),
								rs.getString("device_serialnumber"),rs.getString("loid"),
								rs.getString("device_id"));
					}
				});
		return list;
	}

	private Map<String,String> getMap(Map map,String vendorId,String deviceModelId,String devicetypeId,
									  String cityId,String deviceSerialnumber,String loid,String deviceId)
	{
		String vname = vendorNameAddMap.get(vendorId);
		if(StringUtil.IsEmpty(vname)){
			vname=vendorNameMap.get(StringUtil.getStringValue(map,"vendor_id"));
		}
		map.put("vendor_name",vname);
		map.put("device_model", modelMap.get(deviceModelId));
		map.put("softwareversion", versionMap.get(devicetypeId));
		map.put("city_name", CityDAO.getCityName(cityId));
		map.put("device_serialnumber",deviceSerialnumber);
		map.put("loid", loid);
		map.put("device_id", deviceId);
		return map;
	}
	public static void main(String[] args){
		long time = System.currentTimeMillis();
		System.out.println((time - 86400000)/1000);
		System.out.println(time/1000 - 86400);
	}

	public int count4NoStart(String gwType,String startTime,String endTime,String vendorId,
						String deviceModelId,String devicetypeId,int num_splitPage,String cityId)
	{
		String nameFix = "";
		if(!SYSTEM_ITMS.equals(gwType)){
			nameFix = STB_NAMEFIX;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from "+nameFix+"tab_dev_batch_restart a,"+nameFix+"tab_gw_device b,tab_city c ");
		psql.append("where a.device_id=b.device_id and b.city_id = c.city_id and a.restart_status = -2 ");
		if(!StringUtil.IsEmpty(cityId) && !INVALID_CITY.equals(cityId) && !CENTER_CITY.equals(cityId)){
			psql.append(" and (b.city_id='"+cityId+"' or c.parent_id='"+cityId+"')");
		}
		if(CENTER_CITY.equals(cityId)){
			psql.append(" and b.city_id='" + cityId + "'");
		}
		psql.append(getSqlConditions(startTime,endTime,vendorId,deviceModelId,devicetypeId));

		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0) {
			return total / num_splitPage;
		}
		return total / num_splitPage + 1;
	}
	
	/**
	 * 时间转换
	 */
	private String getTime(long time)
	{
		return DateUtil.transTime(time,"yyyy-MM-dd HH:mm:ss");
	}




}
