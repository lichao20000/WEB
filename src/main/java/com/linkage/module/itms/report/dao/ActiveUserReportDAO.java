
package com.linkage.module.itms.report.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-8-13
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActiveUserReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ActiveUserReportDAO.class);
	
	public List<Map> getActiveUserInfo(int curPage_splitPage, int num_splitPage, String start_time,
			String end_time, String gwShare_cityId, String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId) 
	{
		StringBuffer sql = getsql(start_time, end_time, gwShare_cityId, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
		logger.warn("每月活跃机顶盒查询sql:{}",sql.toString());
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
		if(null == list || list.isEmpty()){
			return new ArrayList();
		}
		for (Map map : list)
		{
			map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
					.getStringValue(map.get("vendor_id"))));
			map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
					.getStringValue(map.get("device_model_id"))));
			map.put("cityName",
					CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			// 时间处理
			long add_time_l = StringUtil.getLongValue(map.get("last_time"));
			map.put("lasttime",
					new DateTimeUtil(add_time_l * 1000).getYYYY_MM_DD_HH_mm_ss());
		}
		return list;
	}

	private StringBuffer getsql(String start_time, String end_time, String gwShare_cityId, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId) 
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.serv_account,b.device_serialnumber,b.vendor_id,b.device_model_id,");
		sql.append("b.city_id,c.hardwareversion,c.softwareversion,d.last_time ");
		sql.append("from stb_tab_customer a,stb_tab_gw_device b,stb_tab_devicetype_info c,stb_gw_devicestatus d ");
		sql.append("where a.customer_id=b.customer_id and b.devicetype_id=c.devicetype_id and b.device_id=d.device_id ");
		if(!StringUtil.IsEmpty(gwShare_cityId) && !"-1".equals(gwShare_cityId)){
			// 带上子属地及其本身
			ArrayList<String>  cityIdList = CityDAO.getAllNextCityIdsByCityPid(gwShare_cityId);
			if(null == cityIdList || cityIdList.isEmpty()){
				sql.append(" and b.city_id = " + gwShare_cityId);
			}else{
				sql.append(" and b.city_id in (")
				.append(StringUtils.weave(cityIdList)).append(")");
				cityIdList = null;
			}
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and d.last_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and d.last_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId)) {
			sql.append(" and b.vendor_id = ").append(gwShare_vendorId);
		}
		if (!StringUtil.IsEmpty(gwShare_deviceModelId) && !"-1".equals(gwShare_deviceModelId)) {
			sql.append(" and b.device_model_id = ").append(gwShare_deviceModelId);
		}
		if (!StringUtil.IsEmpty(gwShare_devicetypeId) && !"-1".equals(gwShare_devicetypeId)) {
			sql.append(" and b.devicetype_id = ").append(gwShare_devicetypeId);
		}
		return sql;
	}

	public int getActiveUserCount(int num_splitPage, String start_time, String end_time, String gwShare_cityId,
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from stb_tab_customer a,stb_tab_gw_device b,stb_tab_devicetype_info c,stb_gw_devicestatus d ");
		sql.append("where a.customer_id=b.customer_id and b.devicetype_id=c.devicetype_id and b.device_id=d.device_id ");
		if(!StringUtil.IsEmpty(gwShare_cityId) && !"-1".equals(gwShare_cityId)){
			sql.append(" and b.city_id = " + gwShare_cityId);
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and d.last_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and d.last_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(gwShare_vendorId) && !"-1".equals(gwShare_vendorId)) {
			sql.append(" and b.vendor_id = ").append(gwShare_vendorId);
		}
		if (!StringUtil.IsEmpty(gwShare_deviceModelId) && !"-1".equals(gwShare_deviceModelId)) {
			sql.append(" and b.device_model_id = ").append(gwShare_deviceModelId);
		}
		if (!StringUtil.IsEmpty(gwShare_devicetypeId) && !"-1".equals(gwShare_devicetypeId)) {
			sql.append(" and b.devicetype_id = ").append(gwShare_devicetypeId);
		}
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
		return maxPage;
	}

	
	public List<Map> getAllActiveUserInfo(String start_time, String end_time, String gwShare_cityId,
			String gwShare_vendorId, String gwShare_deviceModelId, String gwShare_devicetypeId) {
		StringBuffer sql = getsql(start_time, end_time, gwShare_cityId, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
		
		List<Map> list = jt.queryForList(sql.toString());
		for (Map map : list)
		{
			map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
					.getStringValue(map.get("vendor_id"))));
			map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
					.getStringValue(map.get("device_model_id"))));
			map.put("cityName",
					CityDAO.getCityName(StringUtil.getStringValue(map.get("city_id"))));
			// 时间处理
			long add_time_l = StringUtil.getLongValue(map.get("last_time"));
			map.put("lasttime",
					new DateTimeUtil(add_time_l * 1000).getYYYY_MM_DD_HH_mm_ss());
		}
		return list;
	}

}
