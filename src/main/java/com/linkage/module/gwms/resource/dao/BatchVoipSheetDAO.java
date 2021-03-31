
package com.linkage.module.gwms.resource.dao;

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
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class BatchVoipSheetDAO extends SuperDAO {
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchVoipSheetDAO.class);
	
	public int getInfoCount(String loid, Long time)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_batch_voip_sheet where loid='" 
				+ loid +"' and time=" + time);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 批量语音工单下发记录表
	 * @param loid
	 * @param city
	 */
	public void saveInfo(String loid, String cityId, Long time, int result){
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_batch_voip_sheet(loid, time, city_id, result) values(?, ?, ?, ?)");
		psql.setString(1, loid);
		psql.setLong(2, time);
		psql.setString(3, cityId);
		psql.setInt(4, result);
		jt.update(psql.getSQL());
	}
	
	/**
	 * 统计信息
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getListInfo(String starttime, String endtime, String cityId) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, d.open_status, count(*) as total ");
		}else{
			sql.append("select a.city_id, d.open_status, count(1) as total ");
		}
		
		sql.append(" from tab_batch_voip_sheet a left join ( ");
		sql.append(" select b.username, c.open_status from tab_hgwcustomer b, ");
		sql.append(" hgwcust_serv_info c where b.user_id = c.user_id and c.serv_type_id = 14) d ");
		sql.append(" on a.loid = d.username ");
		sql.append(" where a.result = 0 ");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			ArrayList<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.time>=").append(new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.time<=").append(new DateTimeUtil(endtime).getLongTime());
		}
		sql.append(" group by a.city_id,d.open_status");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	/**
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param result
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDevList(String starttime, String endtime, String cityId, String result, 
			int curPage_splitPage, int num_splitPage) {
		logger.debug("getDevList({},{},{},{},{},{})", new Object[] { starttime,
				endtime, cityId, result, curPage_splitPage, num_splitPage });
		
		PrepareSQL psql = new PrepareSQL(getDevSql(starttime, endtime, cityId, result, "list"));
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				}
				else {
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)) {
					map.put("vendor_add", vendor_add);
				}
				else {
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)) {
					map.put("device_model", device_model);
				}
				else {
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (!StringUtil.IsEmpty(softwareversion)) {
					map.put("softwareversion", softwareversion);
				}
				else {
					map.put("softwareversion", "");
				}
				String oui = rs.getString("oui");
				String devSn = rs.getString("device_serialnumber");
				map.put("oui", oui);
				map.put("device_serialnumber", devSn);
				if (!StringUtil.IsEmpty(oui) && !StringUtil.IsEmpty(devSn)) {
					map.put("device", oui + "-" + devSn);
				}
				else {
					map.put("device", "");
				}
				map.put("loopback_ip", rs.getString("loopback_ip"));
				String status = rs.getString("open_status");
				if ("0".equals(status)) {
					map.put("status", "未做");
				}
				else if ("-1".equals(status)) {
					map.put("status", "失败");
				}
				else if ("1".equals(status)) {
					map.put("status", "成功");
				}
				map.put("loid", rs.getString("loid"));
				map.put("voip_phone", rs.getString("voip_phone"));
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 获取设备查询sql
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param result
	 * @param flat
	 * @return
	 */
	private String getDevSql (String starttime, String endtime, String cityId, String result, String flat) 
	{
		StringBuffer sql = new StringBuffer();
		String selectStr;
		if(DBUtil.GetDB()==3){
			selectStr = "select e.city_id,e.loid,e.device_id,e.open_status,e.voip_phone,"
						+ "d.device_id,d.city_id,d.vendor_id,d.device_model_id,d.devicetype_id,"
						+ "d.oui,d.device_serialnumber,d.loopback_ip,d.open_status,d.loid,d.voip_phone ";
		}else{
			selectStr = "select * ";
		}
		
		if ("count".equals(flat)) {
			if(DBUtil.GetDB()==3){
				selectStr = "select count(*) ";
			}else{
				selectStr = "select count(1) ";
			}
			
		}
		if ("list".equals(flat)) {
			if(DBUtil.GetDB()==3){
				selectStr = "select e.city_id,e.loid,e.device_id,e.open_status,e.voip_phone,";
			}else{
				selectStr = "select e.*,";
			}
			selectStr += "d.device_id, d.vendor_id, d.devicetype_id, " +
					"d.device_model_id, d.oui, d.device_serialnumber, d.loopback_ip";
		}
		
		sql.append(selectStr);
		sql.append(" from (select a.city_id, a.loid, b.device_id, c.open_status, f.voip_phone from ");
		sql.append(" tab_batch_voip_sheet a, tab_hgwcustomer b, hgwcust_serv_info c , tab_voip_serv_param f");
		sql.append(" where b.user_id = f.user_id and a.loid = b.username and b.user_id = c.user_id ");
		sql.append(" and c.serv_type_id = 14 and a.result = 0 ");
		
		// 全部数据不用状态
		if (!StringUtil.IsEmpty(result) && !"2".equals(result)) {
			sql.append(" and c.open_status = " + result);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			ArrayList<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.time>=").append(new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.time<=").append(new DateTimeUtil(endtime).getLongTime());
		}
		sql.append(" ) e left join tab_gw_device d on e.device_id = d.device_id");
		return sql.toString();
	}
	
	/**
	 * 
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @param result
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDevCount(String starttime, String endtime, String cityId, String result, 
			int curPage_splitPage, int num_splitPage) {
		logger.debug("getDevCount({},{},{},{},{},{})", new Object[] { starttime,
				endtime, cityId, result, curPage_splitPage, num_splitPage });
		
		PrepareSQL psql = new PrepareSQL(getDevSql(starttime, endtime, cityId, result, "count"));
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}
		else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}

