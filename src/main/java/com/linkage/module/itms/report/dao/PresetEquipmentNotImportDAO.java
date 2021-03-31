package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class PresetEquipmentNotImportDAO extends SuperDAO {

	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> countPresetEquipmentNotImport(String starttime,
			String endtime, String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) num ");
		}else{
			sql.append("select a.city_id, count(1) num ");
		}
		sql.append("from tab_gw_device_refuse a where 1=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.complete_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.complete_time<=").append(endtime);
		}

		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime, int curPage_splitPage,int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.oui,a.device_serialnumber,a.vendor_name,");
		sql.append("a.device_model_name,a.complete_time ");
		sql.append("from tab_gw_device_refuse a where 1=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.complete_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.complete_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("oui", StringUtil.getStringValue(rs.getString("oui")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				map.put("vendor_name",
						StringUtil.getStringValue(rs.getString("vendor_name")));
				map.put("device_model_name", StringUtil.getStringValue(rs
						.getString("device_model_name")));
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(complete_time * 1000);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime, int curPage_splitPage,
			int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_gw_device_refuse a where 1=1  ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.complete_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.complete_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getDevExcel(String city_id, String starttime,String endtime) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.oui,a.device_serialnumber,");
		sql.append("a.vendor_name,a.device_model_name,a.complete_time ");
		sql.append("from tab_gw_device_refuse a where 1=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.complete_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.complete_time<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("oui", StringUtil.getStringValue(rs.getString("oui")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				map.put("vendor_name",
						StringUtil.getStringValue(rs.getString("vendor_name")));
				map.put("device_model_name", StringUtil.getStringValue(rs
						.getString("device_model_name")));
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(complete_time * 1000);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
