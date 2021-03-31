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

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class DPIDAO extends SuperDAO {
	private static Logger logger = LoggerFactory.getLogger(DPIDAO.class);

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

	@SuppressWarnings("rawtypes")
	public List queryDPIList(String startOpenDate1, String endOpenDate1,
			String city_id, String vendor, String devicemodel) {
		logger.debug("DPIDAO==>queryDPIList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.vendor_id, b.device_model_id, c.result_id, c.status,count(1) as total "
				+ "from tab_gw_device a, tab_devicetype_info b, gw_serv_strategy c where a.devicetype_id = b.devicetype_id "
				+ "and a.device_id = c.device_id and c.service_id = 117 ");
		if (!StringUtil.IsEmpty(startOpenDate1)) {
			sql.append(" and c.time>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1)) {
			sql.append(" and c.time<=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		sql.append(" group by a.city_id, b.vendor_id, b.device_model_id, c.result_id, c.status ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryDetail(String startOpenDate1, String endOpenDate1,
			String city_id, String vendor, String devicemodel, String status,
			String resultId, int curPage_splitPage, int num_splitPage) {
		logger.debug("DPIDAO==>queryDPIList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,b.vendor_id,b.device_model_id,b.softwareversion,a.city_id,a.device_serialnumber,d.fault_desc from tab_gw_device a, tab_devicetype_info b, gw_serv_strategy c,"
				+ "tab_cpe_faultcode d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and"
				+ " c.result_id = d.fault_code and c.service_id = 117  ");
		if (!StringUtil.IsEmpty(startOpenDate1)) {
			sql.append(" and c.time>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1)) {
			sql.append(" and c.time<=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and c.status=").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if ("100".equals(status)) {
					sql.append(" and c.result_id!=1");
				} else if ("0".equals(status)) {
					sql.append(" and c.result_id!=1 and a.result_id!=0");
				}
			} else {
				sql.append(" and c.result_id=").append(resultId);
			}
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
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				map.put("vendor_id", vendorMap.get(vendor_id));
				String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
				map.put("device_model_id", modelMap.get(device_model_id));
				map.put("softwareversion", StringUtil.getStringValue(rs.getString("softwareversion")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				String cityId = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("city_name", cityMap.get(cityId));
				map.put("fault_desc", StringUtil.getStringValue(rs.getString("fault_desc")));
				map.put("device_id", StringUtil.getStringValue(rs.getString("device_id")));
				return map;
			}
		});
		return list;
	}


	@SuppressWarnings("rawtypes")
	public int countQueryDetail(String startOpenDate1, String endOpenDate1,
			String city_id, String vendor, String devicemodel, String status,
			String resultId, int curPage_splitPage, int num_splitPage) {
		logger.debug("DPIDAO==>countQueryDPIList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_gw_device a, tab_devicetype_info b, gw_serv_strategy c,"
				+ "tab_cpe_faultcode d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and"
				+ " c.result_id = d.fault_code and c.service_id = 117  ");
		if (!StringUtil.IsEmpty(startOpenDate1)) {
			sql.append(" and c.time>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1)) {
			sql.append(" and c.time<=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and c.status=").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if ("100".equals(status)) {
					sql.append(" and c.result_id!=1");
				} else if ("0".equals(status)) {
					sql.append(" and c.result_id!=1 and a.result_id!=0");
				}
			} else {
				sql.append(" and c.result_id=").append(resultId);
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
	public List<Map> excelqueryDetail(String startOpenDate1,
			String endOpenDate1, String city_id, String vendor,
			String devicemodel, String status, String resultId) {
		logger.debug("DPIDAO==>queryDPIList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.vendor_id,b.device_model_id,b.softwareversion,a.city_id,a.device_serialnumber,d.fault_desc from tab_gw_device a, tab_devicetype_info b, gw_serv_strategy c,"
				+ "tab_cpe_faultcode d where a.devicetype_id = b.devicetype_id and a.device_id = c.device_id and"
				+ " c.result_id = d.fault_code and c.service_id = 117  ");
		if (!StringUtil.IsEmpty(startOpenDate1)) {
			sql.append(" and c.time>=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1)) {
			sql.append(" and c.time<=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			sql.append(" and b.vendor_id='" + vendor + "'");
		}
		if (!StringUtil.IsEmpty(devicemodel) && !"-1".equals(devicemodel)) {
			sql.append(" and b.device_model_id='" + devicemodel + "'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and c.status=").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if ("100".equals(status)) {
					sql.append(" and c.result_id!=1");
				} else if ("0".equals(status)) {
					sql.append(" and c.result_id!=1 and a.result_id!=0");
				}
			} else {
				sql.append(" and c.result_id=").append(resultId);
			}
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		modelMap = getDeviceModel();
		PrepareSQL pSql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String vendor_id = StringUtil.getStringValue(list.get(i).get("vendor_id"));
				list.get(i).put("vendor_id",vendorMap.get(vendor_id));
				String device_model_id = StringUtil.getStringValue(list.get(i).get("device_model_id"));
				list.get(i).put("device_model_id",modelMap.get(device_model_id));
				list.get(i).put("softwareversion",StringUtil.getStringValue(list.get(i).get("softwareversion")));
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				list.get(i).put("device_serialnumber",StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
				list.get(i).put("fault_desc",StringUtil.getStringValue(list.get(i).get("fault_desc")));
			}
		}
		return list;
	}
}