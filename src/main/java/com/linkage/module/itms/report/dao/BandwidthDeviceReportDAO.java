
package com.linkage.module.itms.report.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.act.BandwidthDeviceReportACT;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BandwidthDeviceReportDAO extends SuperDAO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BandwidthDeviceReportDAO.class);
	
	public List<Map> getDeviceInfo(int curPage_splitPage, int num_splitPage, 
			String cityId, String bandwidth, String isSpeedUp) {
		
		StringBuffer sql = getsql(cityId, bandwidth, isSpeedUp, "1");
		logger.warn("带宽设备查询sql:{}",sql.toString());
		
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
		
		if(null == list || list.isEmpty()){
			return new ArrayList<Map>();
		}
		for (Map map : list) {
			map.put("vendorName", BandwidthDeviceReportACT.getVendor(StringUtil.getStringValue(map.get("vendor_id"))));
			map.put("deviceModel", BandwidthDeviceReportACT.getModel(StringUtil.getStringValue(map.get("device_model_id"))));
			map.put("cityName", CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			if (StringUtil.IsEmpty(cityId) || "-1".equals(cityId)) {
				cityId = Global.G_City_Pcity_Map.get(map.get("city_id"));
			}
			map.put("parentCityName", CityDAO.getCityName(cityId));
			String _isSpeedUp = StringUtil.getStringValue(map, "gigabit_port", "0");
			map.put("isSpeedUp", "1".equals(_isSpeedUp) ? "是" : "否");
		}
		return list;
	}

	private StringBuffer getsql(String cityId, String bandwidth, String isSpeedUp, String type) {
		StringBuffer sql = new StringBuffer();
		if ("1".equals(type)) {
			sql.append("select a.city_id, b.username as loid , a.device_name, c.username, a.vendor_id, ");
			sql.append("a.device_model_id, d.down_bandwidth, e.gigabit_port from ");
		}
		else {
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("select count(*) from ");
			}else{
				sql.append("select count(1) from ");
			}
		}
		sql.append("tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_net_serv_param d,tab_device_version_attribute e ");
		sql.append("where a.device_id = b.device_id and b.user_id = c.user_id ");
		sql.append("and b.user_id = d.user_id and a.devicetype_id = e.devicetype_id ");
		sql.append("and c.serv_type_id = 10 ");
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)){
			// 带上子属地及其本身
			ArrayList<String>  cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			if(null == cityIdList || cityIdList.isEmpty()){
				sql.append(" and a.city_id = " + cityId);
			}else{
				sql.append(" and a.city_id in (")
				.append(StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}
		if ("1".equals(bandwidth)) {
			sql.append(" and replace(d.down_bandwidth,'M') > 0 and replace(d.down_bandwidth,'M') <= 100");
		}
		else if ("2".equals(bandwidth)) {
			sql.append(" and replace(d.down_bandwidth,'M') > 100 and replace(d.down_bandwidth,'M') <= 200");
		}
		else if ("3".equals(bandwidth)) {
			sql.append(" and replace(d.down_bandwidth,'M') > 200");
		}
		sql.append(" and d.down_bandwidth is not null");
		if ("1".equals(isSpeedUp)) {
			sql.append(" and e.gigabit_port = 1");
		}
		else if ("2".equals(isSpeedUp)) {
			sql.append(" and e.gigabit_port != 1");
		}
		return sql;
	}

	public int getDeviceInfoCount(int num_splitPage, String cityId, String bandwidth, String isSpeedUp) {
		StringBuffer sql = getsql(cityId, bandwidth, isSpeedUp, "");
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}
		else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getAllDeviceInfo(String cityId, String bandwidth, String isSpeedUp) {
		StringBuffer sql = getsql(cityId, bandwidth, isSpeedUp, "1");
		
		List<Map> list = jt.queryForList(sql.toString());
		if(null == list || list.isEmpty()){
			return new ArrayList<Map>();
		}
		for (Map map : list) {
			map.put("vendorName", BandwidthDeviceReportACT.getVendor(StringUtil.getStringValue(map.get("vendor_id"))));
			map.put("deviceModel", BandwidthDeviceReportACT.getModel(StringUtil.getStringValue(map.get("device_model_id"))));
			map.put("cityName", CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			if (StringUtil.IsEmpty(cityId) || "-1".equals(cityId)) {
				cityId = Global.G_City_Pcity_Map.get(map.get("city_id"));
			}
			map.put("parentCityName", CityDAO.getCityName(cityId));
			String _isSpeedUp = StringUtil.getStringValue(map, "gigabit_port", "0");
			map.put("isSpeedUp", "1".equals(_isSpeedUp) ? "是" : "否");
		}
		return list;
	}

}
