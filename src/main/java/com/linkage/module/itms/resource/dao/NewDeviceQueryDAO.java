package com.linkage.module.itms.resource.dao;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class NewDeviceQueryDAO extends SuperDAO {

	private static Logger logger = LoggerFactory
			.getLogger(NewDeviceQueryDAO.class);
	private Map<String, String> cityMap;
	private static final String NEWDEVICEQUERY_SQL = "select b.username,a.device_name,a.device_serialnumber,d.device_model,c.vendor_name,a.cpe_currentupdatetime,b.binddate from tab_gw_device a,"
			+ "tab_hgwcustomer b, tab_vendor c, gw_device_model d,tab_devicetype_info e where "
			+ "a.customer_id is not null and b.username is not null "
			+ "and a.device_id = b.device_id and a.VENDOR_ID = c.VENDOR_ID "
			+ "and a.DEVICETYPE_ID = e.DEVICETYPE_ID  and d.device_model_id =e.device_model_id ";
	private static final String NEWDEVICEQUERY_STB_SQL = "select b.serv_account username,a.device_name,a.device_serialnumber,d.device_model,c.vendor_name,a.cpe_currentupdatetime,a.bind_time binddate from stb_tab_gw_device a, "
			+ "stb_tab_customer b, stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e  where "
			+ "a.customer_id is not null and b.serv_account is not null "
			+ "and a.customer_id = b.customer_id and a.VENDOR_ID = c.VENDOR_ID "
			+ "and a.DEVICETYPE_ID = e.DEVICETYPE_ID and d.device_model_id =e.device_model_id";

	public NewDeviceQueryDAO() {
	}

	@SuppressWarnings("unchecked")
	public List<Map> newDeviceQueryInfo(String city_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage,
			String gw_type) {
		logger.debug("newDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		String condition = "";
		if ("1".equals(gw_type)) {
			sql.append(NEWDEVICEQUERY_SQL);
			condition = " and b.binddate";
		} else {
			sql.append(NEWDEVICEQUERY_STB_SQL);
			condition = " and a.bind_time";
		}

		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(condition).append("<=").append(end_time);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list =  querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage);
		for (Map map : list)
		{
			map.put("username",map.get("username"));
			map.put("device_model",map.get("device_model"));
			map.put("vendor_name",map.get("vendor_name"));
			map.put("device_name",map.get("device_name"));
			map.put("device_serialnumber",map.get("device_serialnumber"));
			long cpe_currentupdatetime_1 = StringUtil.getLongValue(map.get("cpe_currentupdatetime"));
			logger.warn(String.valueOf(cpe_currentupdatetime_1));
			map.put("cpe_currentupdatetime",
					new DateTimeUtil(cpe_currentupdatetime_1 * 1000).getYYYY_MM_DD_HH_mm_ss());
			long binddate_1 = StringUtil.getLongValue(map.get("binddate"));
			map.put("binddate",
					new DateTimeUtil(binddate_1 * 1000).getYYYY_MM_DD_HH_mm_ss());
		}
		return list;
	}

	public int countNewDeviceQueryInfo(String city_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage,
			String gw_type) {
		StringBuffer sql = new StringBuffer();
		String condition = "";
		if ("1".equals(gw_type)) {
			sql.append(" select count(1) from tab_gw_device a,");
			sql.append("tab_hgwcustomer b, tab_vendor c, gw_device_model d,tab_devicetype_info e where ");
			sql.append("a.customer_id is not null and b.username is not null ");
			sql.append("and a.device_id = b.device_id and a.VENDOR_ID = c.VENDOR_ID ");
			sql.append("and a.DEVICETYPE_ID = e.DEVICETYPE_ID  and d.device_model_id =e.device_model_id ");
		
			condition = " and b.binddate";
		} else {
			sql.append(" select count(1) from stb_tab_gw_device a, ");
			sql.append("stb_tab_customer b, stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e  where ");
			sql.append("a.customer_id is not null and b.serv_account is not null ");
			sql.append("and a.customer_id = b.customer_id and a.VENDOR_ID = c.VENDOR_ID ");
			sql.append("and a.DEVICETYPE_ID = e.DEVICETYPE_ID and d.device_model_id =e.device_model_id");
		
			condition = " and a.bind_time";
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(condition).append("<=").append(end_time);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("unchecked")
	public List<Map> newDeviceQueryExcel(String city_id, String start_time,
			String end_time, String gw_type) {
		logger.debug("newDeviceQueryExcel()");
		String condition = "";
		StringBuffer sql = new StringBuffer();
		if ("1".equals(gw_type)) {
			sql.append(NEWDEVICEQUERY_SQL);
			condition = " and b.binddate";
		} else {
			sql.append(NEWDEVICEQUERY_STB_SQL);
			condition = " and a.bind_time";
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(condition).append("<=").append(end_time);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			for (Map map : list)
			{
				map.put("username",map.get("username"));
				map.put("device_model",map.get("device_model"));
				map.put("vendor_name",map.get("vendor_name"));
				map.put("device_name",map.get("device_name"));
				map.put("device_serialnumber",map.get("device_serialnumber"));
				long cpe_currentupdatetime_1 = StringUtil.getLongValue(map.get("cpe_currentupdatetime"));
				map.put("cpe_currentupdatetime",
						new DateTimeUtil(cpe_currentupdatetime_1 * 1000).getYYYY_MM_DD_HH_mm_ss());
				long binddate_1 = StringUtil.getLongValue(map.get("binddate"));
				map.put("binddate",
						new DateTimeUtil(binddate_1 * 1000).getYYYY_MM_DD_HH_mm_ss());
			}
		}
		return list;
	}

}
