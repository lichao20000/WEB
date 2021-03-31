package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class VersionDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(VersionDAO.class);

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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("rawtypes")
	public List<Map> queryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO==>queryVersionList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.devicetype_id,b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,b.add_time,b.versionttime "
				+ "from tab_devicetype_info b,tab_bss_dev_port e "
				+ "where b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(versionSpecification)) {
			if ("1".equals(versionSpecification)) {
				sql.append(" and b.is_check=" + versionSpecification + "");
			} else {
				sql.append(" and b.is_check in (0,-1)");
			}
			// 不规范查询
			if ("0".equals(versionSpecification)) {
				sql.append(" and b.is_check in(0,-1)");
			}
			// 规范查询
			if ("1".equals(versionSpecification)) {
				sql.append(" and b.is_check=1");
			}

		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
		}
		sql.append(" order by b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion");
		// cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// String cityId = StringUtil.getStringValue(rs
				// .getString("city_id"));
				// map.put("city_id", cityId);
				// map.put("city_name", cityMap.get(cityId));
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
				map.put("devicetype_id", StringUtil.getStringValue(rs
						.getString("devicetype_id")));
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("add_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				try {
					long versionttime = StringUtil.getLongValue(rs
							.getString("versionttime")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(versionttime);
					map.put("versionttime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("versionttime", "");
				} catch (Exception e) {
					map.put("versionttime", "");
				}
				return map;
			}
		});
		return list;
	}

	public int countQueryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO==>countQueryVersionList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) "
				+ "from tab_devicetype_info b,tab_bss_dev_port e "
				+ "where b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(versionSpecification)) {
			if ("1".equals(versionSpecification)) {
				sql.append(" and b.is_check=" + versionSpecification + "");
			} else {
				sql.append(" and b.is_check in (0,-1)");
			}
			// 不规范查询
			if ("0".equals(versionSpecification)) {
				sql.append(" and b.is_check in(0,-1)");
			}
			// 规范查询
			if ("1".equals(versionSpecification)) {
				sql.append(" and b.is_check=1");
			}
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
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
	public List<Map> excelQueryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval) {
		logger.debug("VersionDAO==>excelQueryVersionList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,b.add_time,b.versionttime "
				+ "from tab_devicetype_info b,tab_bss_dev_port e "
				+ "where b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(versionSpecification)) {
			if ("1".equals(versionSpecification)) {
				sql.append(" and b.is_check=" + versionSpecification + "");
			} else {
				sql.append(" and b.is_check in (0,-1)");
			}
		}
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
		}
		sql.append(" order by b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion");
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		// cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL pSql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				// String cityId = StringUtil.getStringValue(list.get(i).get(
				// "city_id"));
				// list.get(i).put("city_id", cityId);
				// list.get(i).put("city_name", cityMap.get(cityId));
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
				try {
					long complete_time = StringUtil.getLongValue(list.get(i)
							.get("add_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(complete_time);
					list.get(i).put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("complete_time", "");
				} catch (Exception e) {
					list.get(i).put("complete_time", "");
				}
				try {
					long versionttime = StringUtil.getLongValue(list.get(i)
							.get("versionttime")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(versionttime);
					list.get(i).put("versionttime", dt.getLongDate());
				} catch (NumberFormatException e) {
					list.get(i).put("versionttime", "");
				} catch (Exception e) {
					list.get(i).put("versionttime", "");
				}
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> excelqueryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion) {
		logger.debug("VersionDAO==>queryVersionList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber,d.username,a.complete_time,a.city_id "
				+ "from tab_gw_device a,tab_devicetype_info b,tab_hgwcustomer d,tab_bss_dev_port e "
				+ "where a.devicetype_id = b.devicetype_id and a.device_id = d.device_id  and b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		// if (!StringUtil.IsEmpty(versionSpecification)) {
		// sql.append(" and b.is_normal=" + versionSpecification + "");
		// }
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
		}
		if (!StringUtil.IsEmpty(softwareversion)) {
			sql.append(" and b.devicetype_id=" + softwareversion + "");
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put(
						"device_serialnumber",
						StringUtil.getStringValue(list.get(i).get(
								"device_serialnumber")));
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
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
				String cityId = StringUtil.getStringValue(list.get(i).get(
						"city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion, int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO==>queryVersionList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber,d.username,a.complete_time,a.city_id "
				+ "from tab_gw_device a,tab_devicetype_info b,tab_hgwcustomer d,tab_bss_dev_port e "
				+ "where a.devicetype_id = b.devicetype_id and a.device_id = d.device_id  and b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		// if (!StringUtil.IsEmpty(versionSpecification)) {
		// sql.append(" and b.is_normal=" + versionSpecification + "");
		// }
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
		}
		if (!StringUtil.IsEmpty(softwareversion)) {
			sql.append(" and b.devicetype_id=" + softwareversion + "");
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
				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));
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
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings({ "unchecked", "finally" })
	public List<Map<String, Object>> getDroopList() {
		PrepareSQL psql = new PrepareSQL(
				"select t.vendor_id,t.vendor_name,t.tx_power,t.rx_power from tab_power_threshold t");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (null != queryList && queryList.size() > 0) {
				Map<String, Object> map = null;
				for (int i = 0; i < queryList.size(); i++) {
					Map<String, String> queryMap = queryList.get(i);
					map = new HashMap<String, Object>();
					map.put("vendor_id",
							StringUtil.getStringValue(queryMap, "vendor_id"));
					map.put("vendor_name",
							StringUtil.getStringValue(queryMap, "vendor_name"));
					map.put("tx_power",
							StringUtil.getStringValue(queryMap, "tx_power"));
					map.put("rx_power",
							StringUtil.getStringValue(queryMap, "rx_power"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVendors() {
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor a "
						+ "where a.vendor_name is not null and not exists "
						+ "(select 1 from tab_power_threshold b where a.vendor_id = b.vendor_id) "
						+ "order by vendor_name desc");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("vendor_id", queryMap.get("vendor_id"));
				map.put("vendorName", queryMap.get("vendor_add"));
				list.add(map);
			}
		}
		return list;
	}

	public String updateDroop(String vendor_id, String tx_power, String rx_power) {
		PrepareSQL psql = new PrepareSQL(
				"update tab_power_threshold set tx_power = ?,rx_power= ? "
						+ "where vendor_id = ?");
		psql.setDouble(1, StringUtil.getDoubleValue(tx_power));
		psql.setDouble(2, StringUtil.getDoubleValue(rx_power));
		psql.setString(3, vendor_id);
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}

	@SuppressWarnings("finally")
	public String saveDroop(String vendor_id, String vendor_name,
			String tx_power, String rx_power) {
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_power_threshold(vendor_id,vendor_name,tx_power,rx_power) values(?,?,?,?)");
		try {
			psql.setString(1, vendor_id);
			psql.setString(2, vendor_name);
			psql.setDouble(3, StringUtil.getDoubleValue(tx_power));
			psql.setDouble(4, StringUtil.getDoubleValue(rx_power));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}

	public String delDroop(String vendor_id) {
		PrepareSQL psql = new PrepareSQL(
				"delete from tab_power_threshold where vendor_id = ?");
		psql.setString(1, vendor_id);
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}

	@SuppressWarnings("null")
	public String orderPower(String cityId, String colType) {
		int num = 0;
		if ("0".equals(colType)) {
			PrepareSQL psql = new PrepareSQL(
					"select count(*) as num from tab_gather_dev "
							+ "where status = 0 ");
			if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
				List<String> cityList = CityDAO
						.getAllNextCityIdsByCityPid(cityId);
				psql.append("and city_id in (" + StringUtils.weave(cityList)
						+ ")");
			}
			Map<String, String> checkMap = DBOperation.getRecord(psql.getSQL());
			if (null != checkMap || checkMap.size() > 0) {
				int count = StringUtil.getIntegerValue(StringUtil
						.getStringValue(checkMap, "num"));
				if (count > 0) {
					return "-1";
				}
			}
			psql = null;
			psql = new PrepareSQL("delete from tab_gather_dev ");
			if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
				List<String> cityList = CityDAO
						.getAllNextCityIdsByCityPid(cityId);
				psql.append("where city_id in (" + StringUtils.weave(cityList)
						+ ")");
			}
			jt.update(psql.getSQL());
			psql = null;
			psql = new PrepareSQL("delete from tab_power_result ");
			if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
				List<String> cityList = CityDAO
						.getAllNextCityIdsByCityPid(cityId);
				psql.append("where city_id in (" + StringUtils.weave(cityList)
						+ ")");
			}
			jt.update(psql.getSQL());
			psql = null;
			psql = new PrepareSQL(
					"select distinct a.device_id,a.device_serialnumber,a.city_id,a.devicetype_id from "
							+ "tab_gw_device a "
							+ "where a.customer_id is not null ");
			if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
				List<String> cityList = CityDAO
						.getAllNextCityIdsByCityPid(cityId);
				psql.append("and a.city_id in (" + StringUtils.weave(cityList)
						+ ")");
			}
			@SuppressWarnings("unchecked")
			List<Map<String, String>> queryList = jt
					.queryForList(psql.getSQL());
			if (queryList != null && queryList.size() > 0) {
				ArrayList<String> sqlList = new ArrayList<String>();
				for (int i = 0; i < queryList.size(); i++) {
					Map<String, String> queryMap = queryList.get(i);
					psql = new PrepareSQL(
							"insert into tab_gather_dev(device_id,device_serialnumber,"
									+ "city_id,loid,devicetype_id,status,order_time) values(?,?,?,?,?,?,?)");
					psql.setString(1, queryMap.get("device_id"));
					psql.setString(2, queryMap.get("device_serialnumber"));
					psql.setString(3, queryMap.get("city_id"));
					psql.setString(4, "");
					psql.setInt(5, StringUtil.getIntegerValue(queryMap
							.get("devicetype_id")));
					psql.setInt(6, 0);
					psql.setInt(7, StringUtil.getIntegerValue(TimeUtil
							.getCurrentTime()));
					sqlList.add(psql.getSQL());
					if (sqlList.size() > 200) {
						num = DBOperation.executeUpdate(sqlList);
						sqlList.clear();
					}
				}
				if (num > 0)
					return "1";
				else
					return "0";
			}
		} else if ("1".equals(colType)) {
			num = sql_update(cityId, "d.tx_power > c.tx_power ");
			if (num > 0) {
				int row = sql_delete(cityId, "b.tx_power > c.tx_power");
				if (row > 0)
					return "1";
				else
					return "0";
			} else {
				return "2";
			}
		} else if ("2".equals(colType)) {
			num = sql_update(cityId, "d.rx_power > c.rx_power ");
			if (num > 0) {
				int row = sql_delete(cityId, "b.rx_power > c.rx_power");
				if (row > 0)
					return "1";
				else
					return "0";
			} else {
				return "2";
			}
		} else {
			num = sql_update(cityId,
					"d.tx_power > c.tx_power and d.rx_power > c.rx_power ");
			if (num > 0) {
				int row = sql_delete(cityId,
						"b.tx_power > c.tx_power and b.rx_power > c.rx_power");
				if (row > 0)
					return "1";
				else
					return "0";
			} else {
				return "2";
			}
		}
		return "0";
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getpowerList(int curPage_splitPage, int num_splitPage,
			String starttime, String endtime, String username,
			String device_serialnumber, String cityId, String vendor_id,
			String soft_version, String powerVal, String powerScope) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.loid,a.vendor_id,a.device_serialnumber,"
				+ "a.tx_power,a.rx_power,a.start_time,a.user_addr,a.is_sendalarm,c.device_model_id "
				+ "from tab_power_result a,tab_power_threshold b,tab_gw_device c where a.vendor_id = b.vendor_id " 
				+ "and a.device_id=c.device_id");
		if (null != starttime) {
			sql.append(" and a.start_time >= ");
			sql.append(new DateTimeUtil(starttime).getLongTime());
		}
		if (null != endtime) {
			sql.append(" and a.end_time <= ");
			sql.append(new DateTimeUtil(endtime).getLongTime());
		}
		if (!(null == username || "".equals(username))) {
			sql.append(" and a.loid = '");
			sql.append(username);
			sql.append("' ");
		}
		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			sql.append(" and a.device_serialnumber = '");
			sql.append(device_serialnumber);
			sql.append("' ");
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendor_id && !"".equals(vendor_id)
				&& !"0".equals(vendor_id)) {
			sql.append(" and a.vendor_id = '");
			sql.append(vendor_id);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (null != soft_version && !"".equals(soft_version)
				&& !"0".equals(soft_version)) {
			sql.append(" and a.softwareversion = '");
			sql.append(soft_version);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (!(null == powerVal || "".equals(powerVal))
				&& (!(null == powerVal || "".equals(powerVal)))) {
			if ("1".equals(powerScope)) {
				if ("0".equals(powerVal)) {
					sql.append(" and a.tx_power > b.tx_power or a.rx_power < b.rx_power ");
				} else if ("1".equals(powerVal)) {
					sql.append(" and a.tx_power > b.tx_power ");
				} else if ("2".equals(powerVal)) {
					sql.append(" and a.rx_power < b.rx_power ");
				}
			}
			if ("-1".equals(powerVal)) {
				sql.append(" and a.gather_status = -1 ");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		final Map<String, String> vendorMap = getVendor();
		final Map<String, String> deviceModelMap = getDeviceModel();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String parentName = StringUtil.getStringValue(rs.getString("city_id"));
				String cityName = "";
				if(parentName != null && !"".equals(parentName)){
					if ("00".equals(rs.getString("city_id"))
							|| "00".equals(CityDAO.getCityIdPidMap().get(
									rs.getString("city_id")))) {
						parentName = CityDAO.getCityIdCityNameMap().get(
								rs.getString("city_id"));
						
					} else {
						parentName = CityDAO.getCityIdCityNameMap().get(
								CityDAO.getCityIdPidMap().get(
										rs.getString("city_id")));
					}
					cityName = CityDAO.getCityIdCityNameMap().get(StringUtil.getStringValue(rs.getString("city_id")));
				}else{
					parentName = CityDAO.getCityIdCityNameMap().get("00");
					cityName = CityDAO.getCityIdCityNameMap().get("00");
				}
				
				map.put("parentName", parentName);
				map.put("cityName",cityName);
				map.put("username", StringUtil.getStringValue(rs.getString("loid")));
				map.put("device_model", StringUtil.getStringValue(deviceModelMap.get(rs.getString("device_model_id"))));
				map.put("vendorName",  StringUtil.getStringValue(vendorMap.get(rs.getString("vendor_id"))));
				map.put("deviceSn",  StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("txPower",  StringUtil.getStringValue(rs.getString("tx_power")));
				map.put("rxPower",  StringUtil.getStringValue(rs.getString("rx_power")));
				map.put("colDate",
						new DateTimeUtil(Long.valueOf(rs.getString("start_time") + "000")).getLongDate());
				String user_addr =  StringUtil.getStringValue(rs.getString("user_addr"));
				if ("".equals(user_addr) || null == user_addr) {
					map.put("userAddr", "无");
				} else {
					map.put("userAddr",  StringUtil.getStringValue(rs.getString("user_addr")));
				}
				String is_sendalarm =  StringUtil.getStringValue(rs.getString("is_sendalarm"));
				if ("1".equals(is_sendalarm)) {
					map.put("isSendalarm", "告警");
				} else {
					map.put("isSendalarm", "无");
				}
				return map;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Map> getPowerExcel(String starttime, String endtime,
			String username, String device_serialnumber, String cityId,
			String vendor_id, String soft_version, String powerVal,
			String powerScope) {
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.city_id,a.loid,a.vendor_id,a.device_serialnumber,");
		sql.append("a.tx_power,a.rx_power,a.start_time,a.user_addr,a.is_sendalarm,c.device_model_id ");
		sql.append("from tab_power_result a,tab_power_threshold b,tab_gw_device c ");
		sql.append("where a.vendor_id = b.vendor_id and a.device_id=c.device_id ");
		if (null != starttime) {
			sql.append(" and a.start_time >= ");
			sql.append(new DateTimeUtil(starttime).getLongTime()+"");
		}
		if (null != endtime) {
			sql.append(" and a.end_time <= ");
			sql.append(new DateTimeUtil(endtime).getLongTime()+"");
		}
		if (!(null == username || "".equals(username))) {
			sql.append(" and a.loid = '");
			sql.append(username);
			sql.append("' ");
		}
		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			if (device_serialnumber.length() > 5) {
				sql.append(" and a.dev_sub_sn ='");
				sql.append(device_serialnumber.substring(device_serialnumber.length()-6,device_serialnumber.length()));
				sql.append("'");
			}
			sql.append(" and a.device_serialnumber like '%");
			sql.append(device_serialnumber);
			sql.append("' ");
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendor_id && !"".equals(vendor_id)
				&& !"0".equals(vendor_id)) {
			sql.append(" and a.vendor_id = '");
			sql.append(vendor_id);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (null != soft_version && !"".equals(soft_version)
				&& !"0".equals(soft_version)) {
			sql.append(" and a.softwareversion = '");
			sql.append(soft_version);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (!(null == powerVal || "".equals(powerVal))
				&& (!(null == powerVal || "".equals(powerVal)))) {
			if ("1".equals(powerScope)) {
				if ("0".equals(powerVal)) {
					sql.append(" and a.tx_power > b.tx_power and a.rx_power < b.rx_power ");
				} else if ("1".equals(powerVal)) {
					sql.append(" and a.tx_power < b.tx_power ");
				} else if ("2".equals(powerVal)) {
					sql.append(" and a.rx_power < b.rx_power ");
				}
			}
			if ("-1".equals(powerVal)) {
				sql.append(" and a.gather_status = -1 ");
			}
		}
		
		List<Map<String, String>> queryList = jt.queryForList(sql.getSQL());
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> map = null;
		Map<String, String> vendorMap = getVendor();
		final Map<String, String> deviceModelMap = getDeviceModel();
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				String parentName = StringUtil.getStringValue(queryMap, "city");
				String cityName = "";
				if(parentName != null && !"".equals(parentName)){
					if ("00".equals(StringUtil.getStringValue(queryMap, "city"))
							|| "00".equals(CityDAO.getCityIdPidMap().get(StringUtil.getStringValue(queryMap, "city")))) {
						parentName = CityDAO.getCityIdCityNameMap().get(
								StringUtil.getStringValue(queryMap, "city"));
						
					} else {
						parentName = CityDAO.getCityIdCityNameMap().get(
								CityDAO.getCityIdPidMap().get(
										StringUtil.getStringValue(queryMap, "city")));
					}
					cityName = CityDAO.getCityIdCityNameMap().get(StringUtil.getStringValue(StringUtil.getStringValue(queryMap, "city")));
				}else{
					parentName = CityDAO.getCityIdCityNameMap().get("00");
					cityName = CityDAO.getCityIdCityNameMap().get("00");
				}
				
				map.put("index",i+1);
				map.put("parentName", parentName);
				map.put("cityName", cityName);
				map.put("username", StringUtil.getStringValue(queryMap,"loid"));
				map.put("vendorName", StringUtil.getStringValue(vendorMap.get(queryMap.get("vendor_id"))));
				map.put("device_model",StringUtil.getStringValue(deviceModelMap.get(queryMap.get("device_model_id"))));
				map.put("deviceSn", StringUtil.getStringValue(queryMap,"device_serialnumber"));
				map.put("txPower", StringUtil.getStringValue(queryMap,"tx_power"));
				map.put("rxPower", StringUtil.getStringValue(queryMap,"rx_power"));
				try {
					long opendate = StringUtil.getLongValue(queryMap.get("start_time"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("colDate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				String user_addr = StringUtil.getStringValue(queryMap,"user_addr");
				if ("".equals(user_addr) || null == user_addr) {
					map.put("userAddr", "无");
				} else {
					map.put("userAddr",
							StringUtil.getStringValue(queryMap, "user_addr"));
				}
				String is_sendalarm = StringUtil.getStringValue(queryMap,
						"is_sendalarm");
				if ("1".equals(is_sendalarm)) {
					map.put("isSendalarm", "告警");
				} else {
					map.put("isSendalarm", "无");
				}
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getSoftVersion(String vendor_id) {
		PrepareSQL psql = new PrepareSQL(
				"select distinct b.softwareversion from gw_device_model a,tab_devicetype_info b "
						+ "where a.device_model_id = b.device_model_id and a.vendor_id = ? "
						+ "and b.softwareversion is not null order by b.softwareversion");
		psql.setString(1, vendor_id);
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		StringBuilder softVersion = new StringBuilder();
		boolean flag = false;
		for (int i = 0; i < queryList.size(); i++) {
			Map<String, String> queryMap = queryList.get(i);
			String value = StringUtil.getStringValue(queryMap,
					"softwareversion");
			if (flag) {
				softVersion.append(",");
			} else {
				flag = true;
			}
			softVersion.append(value);
		}
		return softVersion.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getVendorList() {
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor where vendor_name is not null");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("vendor_id", queryMap.get("vendor_id"));
				map.put("vendorName", queryMap.get("vendor_add"));
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, String> getVendor() {
		Cursor cursor = DataSetBean
				.getCursor("select vendor_id,vendor_name, vendor_add from tab_vendor where vendor_name is not null");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null) {
			String vendor_add = (String) fields.get("vendor_add");
			if (false == StringUtil.IsEmpty(vendor_add)) {
				vendorMap.put((String) fields.get("vendor_id"), vendor_add);
			} else {
				vendorMap.put((String) fields.get("vendor_id"),
						(String) fields.get("vendor_name"));
			}
			fields = cursor.getNext();
		}
		return vendorMap;
	}

	public int getpowerCount(int num_splitPage, String starttime,
			String endtime, String username, String device_serialnumber,
			String cityId, String vendor_id, String soft_version,
			String powerVal, String powerScope) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_power_result a,tab_power_threshold b,tab_gw_device c "
					+ "where a.vendor_id = b.vendor_id and a.device_id=c.device_id");
		if (null != starttime) {
			sql.append(" and a.start_time >= ");
			sql.append(new DateTimeUtil(starttime).getLongTime());
		}
		if (null != endtime) {
			sql.append(" and a.end_time <= ");
			sql.append(new DateTimeUtil(endtime).getLongTime());
		}
		if (!(null == username || "".equals(username))) {
			sql.append(" and a.loid = '");
			sql.append(username);
			sql.append("' ");
		}
		if (!(null == device_serialnumber || "".equals(device_serialnumber))) {
			sql.append(" and a.device_serialnumber = '");
			sql.append(device_serialnumber);
			sql.append("' ");
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendor_id && !"".equals(vendor_id)
				&& !"0".equals(vendor_id)) {
			sql.append(" and a.vendor_id = '");
			sql.append(vendor_id);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (null != soft_version && !"".equals(soft_version)
				&& !"0".equals(soft_version)) {
			sql.append(" and a.softwareversion = '");
			sql.append(soft_version);
			sql.append("' ");
		} else {
			sql.append(" ");
		}
		if (!(null == powerVal || "".equals(powerVal))
				&& (!(null == powerVal || "".equals(powerVal)))) {
			if ("1".equals(powerScope)) {
				if ("0".equals(powerVal)) {
					sql.append(" and a.tx_power > b.tx_power and a.rx_power < b.rx_power ");
				} else if ("1".equals(powerVal)) {
					sql.append(" and a.tx_power > b.tx_power ");
				} else if ("2".equals(powerVal)) {
					sql.append(" and a.rx_power < b.rx_power ");
				}
			}
			if ("-1".equals(powerVal)) {
				sql.append(" and a.gather_status = -1 ");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}

	public int countQueryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion, int curPage_splitPage, int num_splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) "
				+ "from tab_gw_device a,tab_devicetype_info b,tab_hgwcustomer d,tab_bss_dev_port e "
				+ "where a.devicetype_id = b.devicetype_id and a.device_id = d.device_id  and b.spec_id=e.id ");
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		// if (!StringUtil.IsEmpty(versionSpecification)) {
		// sql.append(" and b.is_normal=" + versionSpecification + "");
		// }
		if (!StringUtil.IsEmpty(specName)) {
			sql.append(" and e.gw_type='" + specName + "'");
		}
		if (!StringUtil.IsEmpty(deviceType)) {
			sql.append(" and b.rela_dev_type_id=" + deviceType + "");
		}
		if (!StringUtil.IsEmpty(accessWay)) {
			sql.append(" and b.access_style_relay_id=" + accessWay + "");
		}
		if (!StringUtil.IsEmpty(voiceAgreement)) {
			sql.append(" and b.prot_id=" + voiceAgreement + "");
		}
		if (!StringUtil.IsEmpty(zeroconf)) {
			sql.append(" and b.zeroconf=" + zeroconf + "");
		}
		if (!StringUtil.IsEmpty(mbbndwidth)) {
			sql.append(" and b.mbbroadband=" + mbbndwidth + "");
		}
		if (!StringUtil.IsEmpty(ipvsix)) {
			if ("1".equals(ipvsix)) {
				sql.append(" and b.ip_type=0 ");
			} else {
				sql.append(" and b.ip_type<>0 ");
			}
		}
		if (!StringUtil.IsEmpty(softwareversion)) {
			sql.append(" and b.devicetype_id=" + softwareversion + "");
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

	/**
	 * 更新表数据sql
	 * 
	 * @param cityId
	 * @param msg
	 * @return int
	 */
	public int sql_update(String cityId, String msg) {
		PrepareSQL psql = new PrepareSQL(
				"update tab_gather_dev set status = 0 "
						+ "from tab_gather_dev a,tab_power_threshold c, tab_power_result d "
						+ "where a.device_id = d.device_id and d.vendor_id = c.vendor_id and "
						+ msg);
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("and d.city_id in (" + StringUtils.weave(cityList)
					+ ")");
		}
		return jt.update(psql.getSQL());
	}

	/**
	 * 删除表数据sql
	 * 
	 * @param cityId
	 * @param msg
	 * @return
	 */
	public int sql_delete(String cityId, String msg) {
		PrepareSQL psql = new PrepareSQL("delete tab_power_result "
				+ "from tab_power_result b,tab_power_threshold c "
				+ "where b.vendor_id = c.vendor_id and " + msg);
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("and b.city_id in (" + StringUtils.weave(cityList)
					+ ")");
		}
		return jt.update(psql.getSQL());
	}
	

	public static void main(String[] args) {
		System.out.println(new DateTimeUtil("2015-09-01 00:00:00")
				.getLongTime());
		System.out.println(new DateTimeUtil("2015-09-16 23:59:59")
				.getLongTime());
		System.out.println(new DateTimeUtil(Long.valueOf("1443578325" + "000"))
				.getLongDate());
	}
}
