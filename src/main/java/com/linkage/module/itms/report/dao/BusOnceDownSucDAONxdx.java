
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class BusOnceDownSucDAONxdx extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BusOnceDownSucDAONxdx.class);
	private Map<String, String> servTypeMap = null;

	/**
	 * 返回业务一次下发所有的数据
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map<String, Object>> getDataList(String cityId, String starttime1,
			String endtime1, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		logger.debug("getDataList({},{},{})",
				new Object[] { cityId, starttime1, endtime1 });
		String sql = getSql(cityId, starttime1, endtime1, gwType, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId);
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 返回业务一次下发所有的数据（宁夏机顶盒查询机顶盒的数据库，"xml-stb"）
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getStbDataList(String cityId, String starttime1,
			String endtime1, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		logger.debug("getStbDataList({},{},{})",
				new Object[] { cityId, starttime1, endtime1 });
		String sql = getSql(cityId, starttime1, endtime1, gwType, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId);
		PrepareSQL psql = new PrepareSQL(sql);
		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			return DBOperation.getRecords(psql.getSQL(),"xml-stb");
		}
		else {
			return DBOperation.getRecords(psql.getSQL());
		}
		
	}

	private String getSql(String cityId, String starttime1, String endtime1,
			String gwType, String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		StringBuffer sql = new StringBuffer();
		if ("2".equals(gwType))
		{
			if(DBUtil.GetDB()==3){
				sql.append("select a.city_id ,b.serv_type_id,b.open_status ,count(*) num   ");
			}else{
				sql.append("select a.city_id ,b.serv_type_id,b.open_status ,count(1) num   ");
			}
			
			sql.append(" from   tab_egwcustomer a , egwcust_serv_info b");
			sql.append(" where  a.user_id = b.user_id");
			// 安徽电信目前只统计VoIP下发成功率 add by zhangchy 20130220
			if (Global.AHDX.equals(Global.instAreaShortName))
			{
				sql.append("  and b.serv_type_id = 14 ");
			}
			// ITMS界面宽带、VOIP下发率换算公式修改处
			sql.append(" and b.open_status!=0");
			if (LipossGlobals.isOracle())
			{
				sql.append("  and a.device_id is not null");
			}
			else
			{
				sql.append("  and a.device_id <> null"); // Oracle 不支持此查询条件 add by
															// zhangchy 2013-02-20
			}
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.dealdate > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.dealdate < " + endtime1);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
			sql.append(" group by  a.city_id,b.serv_type_id,b.open_status");
		}
		else if ("4".equals(gwType))
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("  select a.city_id,a.user_status ,count(*) num ");
			}else{
				sql.append("  select a.city_id,a.user_status ,count(1) num ");
			}
			
			sql.append("  from stb_tab_customer a ,stb_tab_gw_device b ,stb_gw_device_model c,");
			sql.append("  stb_tab_devicetype_info t  ");
			sql.append("where a.customer_id = b.customer_id and b.devicetype_id = t.devicetype_id ");
			sql.append("and b.device_model_id = c.device_model_id");
			// ITMS界面宽带、VOIP下发率换算公式修改处
			sql.append(" and a.user_status!=0");
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.updatetime > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.updatetime < " + endtime1);
			}
			if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId))
			{
				sql.append(" and b.vendor_id = '" + gwShare_vendorId + "'");
			}
			if (!StringUtil.IsEmpty(gwShare_deviceModelId)&& !"-1".equals(gwShare_deviceModelId))
			{
				sql.append(" and c.device_model_id = '" + gwShare_deviceModelId+ "'");
			}
			if (!StringUtil.IsEmpty(gwShare_devicetypeId)&& !"-1".equals(gwShare_devicetypeId))
			{
				sql.append(" and t.devicetype_id = " + gwShare_devicetypeId);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
			sql.append(" group by  a.city_id,a.user_status");
		}
		else
		{
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select a.city_id ,b.serv_type_id ,b.open_status ,count(*) num ");
			}else{
				sql.append("select a.city_id ,b.serv_type_id ,b.open_status ,count(1) num ");
			}
			
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b,gw_cust_user_dev_type c,");
			sql.append("tab_gw_device d ,gw_device_model e,tab_devicetype_info t ");
			sql.append("where  a.user_id = b.user_id and a.user_id = c.user_id ");
			sql.append("and a.device_id = d.device_id and d.devicetype_id = t.devicetype_id ");
			sql.append("and d.device_model_id = e.device_model_id ");
			// ITMS界面宽带、VOIP下发率换算公式修改处
			sql.append(" and b.open_status!=0");
			if (LipossGlobals.isOracle())
			{
				sql.append("  and a.device_id is not null");
			}
			else
			{
				sql.append("  and a.device_id <> null"); // Oracle 不支持此查询条件 add by
															// zhangchy 2013-02-20
			}
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.dealdate > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.dealdate < " + endtime1);
			}
			if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId))
			{
				sql.append(" and d.vendor_id = '" + gwShare_vendorId + "'");
			}
			if (!StringUtil.IsEmpty(gwShare_deviceModelId)&& !"-1".equals(gwShare_deviceModelId))
			{
				sql.append(" and e.device_model_id = '" + gwShare_deviceModelId+ "'");
			}
			if (!StringUtil.IsEmpty(gwShare_devicetypeId)&& !"-1".equals(gwShare_devicetypeId))
			{
				sql.append(" and d.devicetype_id = " + gwShare_devicetypeId);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
			sql.append(" group by  a.city_id,b.serv_type_id,b.open_status");
		}
		return sql.toString();
	}

	/**
	 * 业务详细信息
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getServInfoDetail(String cityId, String starttime1, String endtime1,
			String servTypeId, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select a.device_serialnumber,a.username logicSN, a.dealdate ,b.serv_type_id,b.username,b.open_status," +
				"f.vendor_name,t.softwareversion,t.hardwareversion,e.device_model," +
				"d.vendor_id,e.device_model_id,t.devicetype_id");
		sql.append(getBothSQL(cityId, starttime1, endtime1, servTypeId, gwType,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId));
		servTypeMap = this.getServType();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备逻辑SN
				map.put("logicSN", rs.getString("logicSN"));
				// 业务名称
				map.put("servType", servTypeMap.get(rs.getString("serv_type_id")));
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务帐号
				map.put("username", rs.getString("username"));
				// 厂商
				map.put("vendorName", rs.getString("vendor_name"));
				// 软件版本
				map.put("softwareversion", rs.getString("softwareversion"));
				// 硬件版本
				map.put("hardwareversion", rs.getString("hardwareversion"));
				// 型号
				map.put("deviceModel", rs.getString("device_model"));
				// 厂商id
				map.put("gwShare_vendorId", rs.getString("vendor_id"));
				// 型号id
				map.put("gwShare_deviceModelId", rs.getString("device_model_id"));
				// 版本id
				map.put("gwShare_devicetypeId", rs.getString("devicetype_id"));
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}
	
	public List<Map> getStbServInfoDetail(String cityId, String starttime1, String endtime1,
			String servTypeId, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select b.device_serialnumber,b.serv_account username, a.updatetime dealdate,a.user_status open_status," +
					"d.vendor_name,t.softwareversion,t.hardwareversion,c.device_model," +
					"d.vendor_id,c.device_model_id,t.devicetype_id");
		sql.append(getBothSQL(cityId, starttime1, endtime1, servTypeId, gwType,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId));
		servTypeMap = this.getServType();
		List<Map> list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务帐号
				map.put("username", rs.getString("username"));
				// 厂商
				map.put("vendorName", rs.getString("vendor_name"));
				// 软件版本
				map.put("softwareversion", rs.getString("softwareversion"));
				// 硬件版本
				map.put("hardwareversion", rs.getString("hardwareversion"));
				// 型号
				map.put("deviceModel", rs.getString("device_model"));
				// 厂商id
				map.put("gwShare_vendorId", rs.getString("vendor_id"));
				// 型号id
				map.put("gwShare_deviceModelId", rs.getString("device_model_id"));
				// 版本id
				map.put("gwShare_devicetypeId", rs.getString("devicetype_id"));
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 返回的页数
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getServInfoCount(String cityId, String starttime1, String endtime1,
			String servType, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append(" select count(*) ");
		}else{
			sql.append(" select count(1) ");
		}
		sql.append(getBothSQL(cityId, starttime1, endtime1, servType, gwType,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int totalPageNum = 1;
		if (total % num_splitPage == 0)
		{
			totalPageNum = total / num_splitPage;
		}
		else
		{
			totalPageNum = total / num_splitPage + 1;
		}
		logger.warn("返回值totalPageNum" + totalPageNum);
		return totalPageNum;
	}

	/**
	 * 业务详细信息的导出数据
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getServInfoExcel(String cityId, String starttime1,
			String endtime1, String servType, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select a.device_serialnumber,a.username logicSN, a.dealdate ,b.serv_type_id," +
					"b.username,b.open_status,f.vendor_name,t.softwareversion," +
					"t.hardwareversion,e.device_model,d.vendor_id,e.device_model_id,t.devicetype_id");
		sql.append(getBothSQL(cityId, starttime1, endtime1, servType, gwType,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		servTypeMap = this.getServType();
		List<Map<String, Object>> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备逻辑SN
				map.put("logicSN", rs.getString("logicSN"));
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务名称
				map.put("servType", servTypeMap.get(rs.getString("serv_type_id")));
				// 业务帐号
				map.put("username", rs.getString("username"));
				// 厂商
				map.put("vendorName", rs.getString("vendor_name"));
				// 软件版本
				map.put("softwareversion", rs.getString("softwareversion"));
				// 硬件版本
				map.put("hardwareversion", rs.getString("hardwareversion"));
				// 型号
				map.put("deviceModel", rs.getString("device_model"));
				// 厂商id
				map.put("gwShare_vendorId", rs.getString("vendor_id"));
				// 型号id
				map.put("gwShare_deviceModelId", rs.getString("device_model_id"));
				// 版本id
				map.put("gwShare_devicetypeId", rs.getString("devicetype_id"));
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStbServInfoExcel(String cityId, String starttime1,
			String endtime1, String servType, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append(" select b.device_serialnumber,b.serv_account username, a.updatetime dealdate,a.user_status open_status," +
				"d.vendor_name,t.softwareversion,t.hardwareversion,c.device_model," +
				"d.vendor_id,c.device_model_id,t.devicetype_id");
		sql.append(getBothSQL(cityId, starttime1, endtime1, servType, gwType,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		servTypeMap = this.getServType();
		List<Map<String, Object>> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
				// 业务帐号
				map.put("username", rs.getString("username"));
				// 厂商
				map.put("vendorName", rs.getString("vendor_name"));
				// 软件版本
				map.put("softwareversion", rs.getString("softwareversion"));
				// 硬件版本
				map.put("hardwareversion", rs.getString("hardwareversion"));
				// 型号
				map.put("deviceModel", rs.getString("device_model"));
				// 厂商id
				map.put("gwShare_vendorId", rs.getString("vendor_id"));
				// 型号id
				map.put("gwShare_deviceModelId", rs.getString("device_model_id"));
				// 版本id
				map.put("gwShare_devicetypeId", rs.getString("devicetype_id"));
				// 开通状态
				String open_status = StringUtil.getStringValue(rs
						.getString("open_status"));
				if ("1".equals(open_status))
				{
					map.put("openStatus", "已开通");
				}
				else if ("0".equals(open_status))
				{
					map.put("openStatus", "未开通");
				}
				else
				{
					map.put("openStatus", "开通失败");
				}
				// 受理时间
				try
				{
					long completeTime = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 详细sql 有许多查询用到相同的语句。
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param servType
	 * @return
	 */
	public String getBothSQL(String cityId, String starttime1, String endtime1,
			String servTypeId, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		StringBuffer sql = new StringBuffer();
		if ("2".equals(gwType))
		{
			sql.append(" from   tab_egwcustomer a , egwcust_serv_info b");
			sql.append(" where a.user_id = b.user_id ");
			if (LipossGlobals.isOracle())
			{
				sql.append("  and a.device_id is not null");
			}
			else
			{
				sql.append("  and a.device_id <> null"); // Oracle 不支持此查询条件 add by
															// zhangchy 2013-02-20
			}
			if (!StringUtil.IsEmpty(servTypeId) && !"-1".endsWith(servTypeId))
			{
				sql.append(" and  b.serv_type_id =  ").append(servTypeId);
			}
			else
			{
				if (Global.AHDX.equals(Global.instAreaShortName))
				{
					// 安徽电信目前只统计VoIP下发成功率 add by zhangchy 20130220
					sql.append(" and  b.serv_type_id in (14) ");
				}
				else
				{
					sql.append(" and  b.serv_type_id in (10,11,14) ");
				}
			}
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.dealdate > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.dealdate < " + endtime1);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
		}
		else if ("4".equals(gwType))
		{
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append(" from stb_tab_customer a ,stb_tab_gw_device b ,stb_gw_device_model c,");
			sql.append("stb_tab_vendor d,stb_tab_devicetype_info t ");
			sql.append("where a.customer_id = b.customer_id and b.devicetype_id = t.devicetype_id ");
			sql.append("and b.vendor_id = d.vendor_id and b.device_model_id = c.device_model_id ");
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.updatetime > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.updatetime < " + endtime1);
			}
			if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId))
			{
				sql.append(" and b.vendor_id = '" + gwShare_vendorId + "'");
			}
			if (!StringUtil.IsEmpty(gwShare_deviceModelId)&& !"-1".equals(gwShare_deviceModelId))
			{
				sql.append(" and c.device_model_id = '" + gwShare_deviceModelId+ "'");
			}
			if (!StringUtil.IsEmpty(gwShare_devicetypeId)&& !"-1".equals(gwShare_devicetypeId))
			{
				sql.append(" and t.devicetype_id = " + gwShare_devicetypeId);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
		}
		else
		{
			sql.append(" from  tab_hgwcustomer a,hgwcust_serv_info b,gw_cust_user_dev_type c,");
			sql.append("tab_gw_device d,gw_device_model e,tab_vendor f,tab_devicetype_info t");
			sql.append("where a.user_id = b.user_id and a.user_id = c.user_id ");
			sql.append("and a.device_id = d.device_id and d.vendor_id = f.vendor_id ");
			sql.append("and d.devicetype_id = t.devicetype_id and d.device_model_id = e.device_model_id ");
			if (LipossGlobals.isOracle())
			{
				sql.append("  and a.device_id is not null");
			}
			else
			{
				sql.append("  and a.device_id <> null"); // Oracle 不支持此查询条件 add by
															// zhangchy 2013-02-20
			}
			if (!StringUtil.IsEmpty(servTypeId) && !"-1".endsWith(servTypeId))
			{
				sql.append(" and  b.serv_type_id =  ").append(servTypeId);
			}
			else
			{
				sql.append(" and  b.serv_type_id in (10,11,14) ");
			}
			if (!StringUtil.IsEmpty(starttime1))
			{
				sql.append(" and a.dealdate > " + starttime1);
			}
			if (!StringUtil.IsEmpty(endtime1))
			{
				sql.append(" and a.dealdate < " + endtime1);
			}
			if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId))
			{
				sql.append(" and d.vendor_id = '" + gwShare_vendorId + "'");
			}
			if (!StringUtil.IsEmpty(gwShare_deviceModelId)&& !"-1".equals(gwShare_deviceModelId))
			{
				sql.append(" and e.device_model_id = '" + gwShare_deviceModelId+ "'");
			}
			if (!StringUtil.IsEmpty(gwShare_devicetypeId)&& !"-1".equals(gwShare_devicetypeId))
			{
				sql.append(" and d.devicetype_id = " + gwShare_devicetypeId);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{ // 设备属地
				List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
		}
		return sql.toString();
	}

	/**
	 * 业务类型
	 * 
	 * @return
	 */
	public HashMap<String, String> getServType()
	{
		PrepareSQL sql = new PrepareSQL(
				" select serv_type_id,serv_type_name from tab_gw_serv_type ");
		List<Map> list = jt.queryForList(sql.getSQL());
		HashMap<String, String> servTypeMap = new HashMap<String, String>();
		for (Map<String, String> map : list)
		{
			servTypeMap.put(StringUtil.getStringValue(map.get("serv_type_id")), 
							StringUtil.getStringValue(map.get("serv_type_name")));
		}
		return servTypeMap;
	}
}
