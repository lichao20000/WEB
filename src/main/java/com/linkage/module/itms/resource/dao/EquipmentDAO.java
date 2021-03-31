package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class EquipmentDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(EquipmentDAO.class);

	private Map<String, String> vendorMap = new HashMap<String, String>();
	private Map<String, String> modelMap;
	private Map<String, String> cityMap = new HashMap<String, String>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getVendorMap() {
		Map<String, String> vendorMap = null;
		if (vendorMap == null) {
			vendorMap = new HashMap<String, String>();

			String sql = "select * from tab_vendor order by vendor_add";
			// teledb
			if (DBUtil.GetDB() == 3) {
				sql = "select vendor_add, vendor_id, vendor_name from tab_vendor order by vendor_add";
			}
			PrepareSQL psql = new PrepareSQL(sql);
			List<Map> list = new ArrayList<Map>();
			list = jt.queryForList(psql.getSQL());
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String vendor_add = StringUtil.getStringValue(list.get(i)
							.get("vendor_add"));
					if (vendor_add != null && !"".equals(vendor_add)) {
						vendorMap.put(
								StringUtil.getStringValue(list.get(i).get(
										"vendor_id")), vendor_add);
					} else {
						vendorMap.put(
								StringUtil.getStringValue(list.get(i).get(
										"vendor_id")),
								list.get(i).get("vendor_name").toString());
					}
				}
			}
		}
		return vendorMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDeviceModel() {
		logger.debug("DeviceServiceDAO=>getDeviceModel()");
		Map<String, String> modelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				modelMap.put(
						StringUtil.getStringValue(list.get(i).get(
								"device_model_id")),
						StringUtil.getStringValue(list.get(i).get(
								"device_model")));
			}
		}
		return modelMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> queryEquipment(String city_id,
			String start_time, String end_time, String deviceType,
			String isActive, String specName, String vendor, String model,
			String hardVersion, String softVersion, String querytype) {
		StringBuffer sql = new StringBuffer();
		Date date = new Date(Long.valueOf(start_time) * 1000);
		if ("1".equals(querytype)) {
			long StartTime = yesterdayStartTime(date);
			long endTime = yesterdayEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=1 and a.module='3' ");
			sql.append(" and a.id=-1  ");
			sql.append(" and a.time>=").append(StartTime);
			sql.append(" and a.time<=").append(endTime);
		}
		if ("2".equals(querytype)) {
			long StartTime = lastWeekStartTime(date);
			long endTime = lastWeekEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=2 and a.module='3' ");
			sql.append(" and a.id=-1  ");
			sql.append(" and a.time>=").append(StartTime);
			sql.append(" and a.time<=").append(endTime);
		}
		if ("3".equals(querytype)) {
			long StartTime = lastMonthStartTime(date);
			long endTime = lastMonthEndTime(date);
			sql.append("select a.city_id, a.num from tab_terminaluserbinding a where a.type=3 and a.module='3' ");
			sql.append(" and a.id=-1  ");
			sql.append(" and a.time>=").append(StartTime);
			sql.append(" and a.time<=").append(endTime);
		}
		if ("4".equals(querytype)) {
			sql.append("select a.city_id,count(1) num from tab_gw_device a,tab_devicetype_info b");
			if (!StringUtil.IsEmpty(isActive)) {
				sql.append(",tab_hgwcustomer c");
			}
			sql.append(",tab_bss_dev_port d where a.devicetype_id = b.devicetype_id and b.spec_id = d.id ");
			if (!StringUtil.IsEmpty(deviceType)) {
				sql.append(" and a.device_type='" + deviceType + "'");
			}
			if (!StringUtil.IsEmpty(start_time)) {
				sql.append(" and a.complete_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time)) {
				sql.append(" and a.complete_time<=").append(end_time);
			}
			if (!StringUtil.IsEmpty(isActive)) {
				sql.append(" and a.device_id = c.device_id and c.is_active=")
						.append(isActive);
			}
			if (!StringUtil.IsEmpty(specName)) {
				sql.append(" and d.gw_type='" + specName + "'");
			}
			if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
				sql.append(" and b.vendor_id='" + vendor + "'");
			}
			if (!StringUtil.IsEmpty(model) && !"-1".equals(model)) {
				sql.append(" and b.device_model_id='" + model + "'");
			}
			if (!StringUtil.IsEmpty(hardVersion)) {
				sql.append(" and b.hardwareversion='" + hardVersion + "'");
			}
			if (!StringUtil.IsEmpty(softVersion)) {
				sql.append(" and b.softwareversion='" + softVersion + "'");
			}
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("4".equals(querytype)) {
			sql.append(" group by a.city_id ");
		}
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
	public List<Map> queryEquipmentList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String vendor, String model, String hardVersion,
			String softVersion, int curPage_splitPage, int num_splitPage) {
		logger.debug("EquipmentDAO==>queryEquipmentList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.device_serialnumber,b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,a.complete_time,d.spec_name "
				+ "from tab_gw_device a,tab_devicetype_info b");
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(",tab_hgwcustomer c");
		}
		sql.append(",tab_bss_dev_port d where a.devicetype_id = b.devicetype_id and b.spec_id = d.id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and a.device_type='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and a.device_id = c.device_id and c.is_active=")
					.append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(model) && !"-1".equals(model)) {
			sql.append(" and b.device_model_id='" + model + "'");
		}
		if (!StringUtil.IsEmpty(hardVersion)) {
			sql.append(" and b.hardwareversion='" + hardVersion + "'");
		}
		if (!StringUtil.IsEmpty(softVersion)) {
			sql.append(" and b.softwareversion='" + softVersion + "'");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String cityId = StringUtil.getStringValue(rs
						.getString("city_id"));
				map.put("city_id", cityId);
				map.put("city_name", cityMap.get(cityId));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				String vendor_id = StringUtil.getStringValue(rs
						.getString("vendor_id"));
				map.put("vendor_id", vendorMap.get(vendor_id));
				String device_model_id = StringUtil.getStringValue(rs
						.getString("device_model_id"));
				map.put("device_model_id", modelMap.get(device_model_id));
				map.put("hardwareversion", StringUtil.getStringValue(rs
						.getString("hardwareversion")));
				map.put("softwareversion", StringUtil.getStringValue(rs
						.getString("softwareversion")));
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				map.put("spec_id",
						StringUtil.getStringValue(rs.getString("spec_name")));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int countQueryEquipmentList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String vendor, String model, String hardVersion,
			String softVersion, int curPage_splitPage, int num_splitPage) {
		logger.debug("EquipmentDAO==>countQueryEquipmentList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) "
				+ "from tab_gw_device a,tab_devicetype_info b");
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(",tab_hgwcustomer c");
		}
		sql.append(",tab_bss_dev_port d where a.devicetype_id = b.devicetype_id and b.spec_id = d.id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and a.device_type='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and a.device_id = c.device_id and c.is_active=")
					.append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(model) && !"-1".equals(model)) {
			sql.append(" and b.device_model_id='" + model + "'");
		}
		if (!StringUtil.IsEmpty(hardVersion)) {
			sql.append(" and b.hardwareversion='" + hardVersion + "'");
		}
		if (!StringUtil.IsEmpty(softVersion)) {
			sql.append(" and b.softwareversion='" + softVersion + "'");
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> excelQueryEquipmentList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String vendor, String model, String hardVersion,
			String softVersion) {
		logger.debug("EquipmentDAO==>excelQueryEquipmentList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.device_serialnumber,b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,a.complete_time,d.spec_name"
				+ " from tab_gw_device a,tab_devicetype_info b");
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(",tab_hgwcustomer c");
		}
		sql.append(",tab_bss_dev_port d where a.devicetype_id = b.devicetype_id and b.spec_id = d.id ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
				&& !"00".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and a.device_type='" + deviceType + "'");
		}
		if (!StringUtil.IsEmpty(start_time)) {
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(isActive)) {
			sql.append(" and a.device_id = c.device_id and c.is_active=")
					.append(isActive);
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and d.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(model) && !"-1".equals(model)) {
			sql.append(" and b.device_model_id='" + model + "'");
		}
		if (!StringUtil.IsEmpty(hardVersion)) {
			sql.append(" and b.hardwareversion='" + hardVersion + "'");
		}
		if (!StringUtil.IsEmpty(softVersion)) {
			sql.append(" and b.softwareversion='" + softVersion + "'");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String cityId = StringUtil.getStringValue(list.get(i).get(
						"city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				list.get(i).put(
						"device_serialnumber",
						StringUtil.getStringValue(list.get(i).get(
								"device_serialnumber")));
				String vendor_id = StringUtil.getStringValue(list.get(i).get(
						"vendor_id"));
				list.get(i).put("vendor_id", vendorMap.get(vendor_id));
				String device_model_id = StringUtil.getStringValue(list.get(i)
						.get("device_model_id"));
				list.get(i).put("device_model_id",
						modelMap.get(device_model_id));
				list.get(i).put(
						"hardwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"hardwareversion")));
				list.get(i).put(
						"softwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"softwareversion")));
				list.get(i)
						.put("spec_id",
								StringUtil.getStringValue(list.get(i).get(
										"spec_name")));
				try {
					long complete_time = StringUtil.getLongValue(list.get(i)
							.get("complete_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					list.get(i).put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("complete_time", "");
				} catch (Exception e) {
					list.get(i).put("complete_time", "");
				}
			}
		}
		return list;
	}

	private long yesterdayStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	private long yesterdayEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastWeekStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastWeekEndTime(Date date) {
		final long weekInSecond = 7 * 24 * 3600;
		return lastWeekStartTime(date) + weekInSecond - 1;
	}

	public long lastMonthStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	private long lastMonthEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 0);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTimeInMillis() / 1000;
	}
}
