
package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class StackRefreshCountDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(StackRefreshCountDAO.class);
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	private static final String CENTER_CITY = "00";

	public List<Map> queryStackRefreshInfo(String serviceId) {
		logger.debug("StackRefreshCountDAO--queryStackRefreshInfo({})", serviceId);
		StringBuffer sql =new StringBuffer();// TODO wait (more table related)
		if(Global.NXDX.equals(Global.instAreaShortName)){

			sql.append("SELECT f.city_id, d.status, d.result_id, COUNT(1) AS total  ");
			sql.append("FROM (  ");
			sql.append("	SELECT b.status, b.result_id, b.device_id, b.task_id ");
			sql.append("	FROM (  ");
			sql.append("		SELECT MAX(add_time) AS max_addtime, device_id  ");
			sql.append("		FROM tab_stack_task_dev  ");
			sql.append("		GROUP BY device_id  ");
			sql.append("	) a, tab_stack_task_dev b  ");
			sql.append("	WHERE a.device_id = b.device_id  ");
			sql.append("		AND a.max_addtime = b.add_time  ");
			sql.append(") d, tab_stack_task t, tab_gw_device f  ");
			sql.append("WHERE (d.device_id = f.device_id  ");
			sql.append("	AND t.task_id = d.task_id  ");
			sql.append("	AND t.service_id = "+serviceId+")  ");
			sql.append("GROUP BY f.city_id, d.status, d.result_id  ");

		}else {// TODO wait (more table related)
			sql.append("select b.city_id, d.status, d.result_id,count(*) as total  from tab_stack_task_dev d , tab_stack_task t ,tab_gw_device  b   where d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = "
				+ serviceId + " group by b.city_id,d.status,d.result_id");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return this.jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String serviceId, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		String sql = appendSqlMethod(serviceId, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql);
		this.cityMap = CityDAO.getCityIdCityNameMap();
		this.vendorMap = VendorModelVersionDAO.getVendorMap();
		this.deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		this.devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map map = new HashMap();
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
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		this.cityMap = null;
		this.vendorMap = null;
		this.deviceModelMap = null;
		return list;
	}

	private String appendSqlMethod(String serviceId, String status, String cityId) {
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,a.res fault_desc  ");
			sql.append("FROM (  ");
			sql.append("	SELECT q.res, q.device_id, q.task_id ");
			sql.append("	FROM (  ");
			sql.append("		SELECT MAX(add_time) AS max_addtime, device_id  ");
			sql.append("		FROM tab_stack_task_dev  ");
			sql.append("		GROUP BY device_id  ");
			sql.append("	) d, tab_stack_task_dev q  ");
			sql.append("	WHERE d.device_id = q.device_id  ");
			sql.append("		AND d.max_addtime = q.add_time  ");
			sql.append(") a, tab_stack_task t, tab_gw_device b  ");
			sql.append("WHERE a.device_id = b.device_id  ");
			sql.append("	AND t.task_id = a.task_id  ");
			sql.append("	AND t.service_id = "+serviceId+" ");

		}
		else if (Global.ZJLT.equals(Global.instAreaShortName)){
			sql.append("select aa.username loid,serv.username,b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,a.res fault_desc from tab_stack_task_dev a,tab_stack_task t,tab_gw_device b left join tab_hgwcustomer aa on(aa.device_id=b.device_id) left join hgwcust_serv_info serv on(serv.user_id=aa.user_id and serv.serv_type_id=10) where a.device_id=b.device_id and a.task_id = t.task_id and t.service_id = "
					+ serviceId + " ");
		}
		else {
			// TODO wait (more table related)
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,a.res fault_desc from tab_stack_task_dev a,tab_gw_device b,tab_stack_task t where a.device_id=b.device_id and a.task_id = t.task_id and t.service_id = "
					+ serviceId + " ");
		}
		if ((!StringUtil.IsEmpty(cityId)) && (!CENTER_CITY.equals(cityId))) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(status)) {
			sql.append(" and a.result_id=1 ");
			sql.append(" and a.status=100 ");
		}
		else if ("2".equals(status)) {
			sql.append(" and a.result_id is null ");
			sql.append(" and a.status is null");
		}
		else if ("3".equals(status)) {
			sql.append(" and (a.result_id <> 1 ");
			sql.append(" or a.status <> 100) ");
		}
		return sql.toString();
	}

	public int getDevCount(String serviceId, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		logger.warn("getDevCount(serviceId:{},status:{},cityId:{})", new Object[] { serviceId, status, cityId });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		if(Global.NXDX.equals(Global.instAreaShortName)){
			sql.append("SELECT  COUNT(1) ");
			sql.append("FROM (  ");
			sql.append("	SELECT d.device_id, d.task_id ");
			sql.append("	FROM (  ");
			sql.append("		SELECT MAX(add_time) AS max_addtime, device_id  ");
			sql.append("		FROM tab_stack_task_dev  ");
			sql.append("		GROUP BY device_id  ");
			sql.append("	) q, tab_stack_task_dev d  ");
			sql.append("	WHERE q.device_id = d.device_id  ");
			sql.append("		AND q.max_addtime = d.add_time  ");
			sql.append(") a, tab_stack_task t, tab_gw_device b  ");
			sql.append("WHERE a.device_id = b.device_id  ");
			sql.append("	AND t.task_id = a.task_id  ");
			sql.append("	AND t.service_id = "+serviceId+" ");

		}
		else {
		// TODO wait (more table related)
		sql.append("select count(*) from tab_stack_task_dev a,tab_gw_device b,tab_stack_task t where a.device_id=b.device_id and a.task_id = t.task_id and t.service_id = "
				+ serviceId + " ");
		}
		if ((!StringUtil.IsEmpty(cityId)) && (!CENTER_CITY.equals(cityId))) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(status)) {
			sql.append(" and a.result_id=1 ");
			sql.append(" and a.status=100 ");
		}
		else if ("2".equals(status)) {
			sql.append(" and a.result_id is null ");
			sql.append(" and a.status is null");
		}
		else if ("3".equals(status)) {
			sql.append(" and (a.result_id <> 1 ");
			sql.append(" or a.status <> 100) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = this.jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}
		else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getDevExcel(String serviceId1, String status, String cityId) {
		String sql = appendSqlMethod(serviceId1, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		this.cityMap = CityDAO.getCityIdCityNameMap();
		this.vendorMap = VendorModelVersionDAO.getVendorMap();
		this.deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		this.devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List list = this.jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map map = new HashMap();
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
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loid", rs.getString("loid"));
				map.put("username", rs.getString("username"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		this.cityMap = null;
		this.vendorMap = null;
		this.deviceModelMap = null;
		logger.debug("devList:" + list.toString());
		return list;
	}

	/**
	 * 统计单双栈信息
	 *
	 * @param serviceId
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> queryStackRefreshInfo(String serviceId, String starttime1, String endtime1, String cityId) {
		logger.warn("StackRefreshCountDAO--queryStackRefreshInfo({})", serviceId);
		/**
		 * select b.city_id, d.status, d.result_id,d.res,count(*) as total from
		 * tab_stack_task_dev d , tab_stack_task t ,tab_gw_device b where
		 * d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = 2301 group
		 * by b.city_id,d.status,d.result_id,d.res
		 */
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.city_id, d.status, d.result_id,count(*) as total  from tab_stack_task_dev d , tab_stack_task t ,tab_gw_device  b  "
				+ " where d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = ");
		sql.append(" " + serviceId + " ");
		// 判断属地
		if (false == StringUtil.IsEmpty(cityId) && !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		// 判断时间
		if (null != starttime1 && !"".equals(starttime1)) {
			sql.append(" and d.add_time > " + starttime1 + " ");
		}
		// 判断时间
		if (null != endtime1 && !"".equals(endtime1)) {
			sql.append(" and d.add_time < " + endtime1 + " ");
		}
		sql.append(" group by b.city_id,d.status,d.result_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询详情列表
	 *
	 * @param serviceId
	 * @param status
	 *            (结果状态 1成功 2未做 3失败 空串全部)
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDevList(String serviceId, String status, String cityId, int curPage_splitPage,
			int num_splitPage, String starttime1, String endtime1) {
		String sql = appendSqlMethod(serviceId, status, cityId, starttime1, endtime1);
		PrepareSQL psql = new PrepareSQL(sql);
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("device_id", rs.getString("device_id"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name)) {
							map.put("city_name", city_name);
						}
						else {
							map.put("city_name", "");
						}
						String vendor_id = rs.getString("vendor_id");
						String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendor_add", vendor_add);
						}
						else {
							map.put("vendor_add", "");
						}
						String device_model_id = rs.getString("device_model_id");
						String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						}
						else {
							map.put("device_model", "");
						}
						String devicetype_id = rs.getString("devicetype_id");
						String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
						if (false == StringUtil.IsEmpty(softwareversion)) {
							map.put("softwareversion", softwareversion);
						}
						else {
							map.put("softwareversion", "");
						}
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
						map.put("loopback_ip", rs.getString("loopback_ip"));
						map.put("fault_desc", rs.getString("fault_desc"));
						map.put("loid", rs.getString("loid"));
						map.put("serv_account", rs.getString("serv_account"));
						return map;
					}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}

	private String appendSqlMethod(String serviceId, String status, String cityId, String starttime1, String endtime1) {
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,h.username loid,c.username serv_account,a.res fault_desc "
				+ "from tab_stack_task_dev a,tab_gw_device b,tab_stack_task t, tab_hgwcustomer h,hgwcust_serv_info c  where a.device_id=b.device_id and a.task_id = t.task_id and a.loid = h.username and h.user_id = c.user_id and c.serv_type_id = 10 and t.service_id = "
				+ serviceId + " ");
		if (false == StringUtil.IsEmpty(cityId) && !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		// 添加时间判断
		if (null != starttime1 && !"".equals(starttime1)) {
			sql.append(" and a.add_time > " + starttime1 + " ");
		}
		// 判断时间
		if (null != endtime1 && !"".equals(endtime1)) {
			sql.append(" and a.add_time < " + endtime1 + " ");
		}
		// 查询成功的
		if ("1".equals(status)) {
			sql.append(" and a.result_id=1 ");
			sql.append(" and a.status=100 ");
		}
		else if ("2".equals(status)) {
			// 查询未作的
			sql.append(" and a.result_id is null ");
			sql.append(" and a.status is null ");
		}
		else if ("3".equals(status)) {
			// 查询失败的
			sql.append(" and (a.result_id <> 1 ");
			sql.append(" or a.status <> 100) ");
		}
		return sql.toString();
	}

	/**
	 * 查询总量
	 *
	 * @param serviceId1
	 * @param status
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public int getDevCount(String serviceId, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		logger.warn("getDevCount(serviceId:{},status:{},cityId:{})", serviceId, status, cityId);
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) "
				+ "from tab_stack_task_dev a,tab_gw_device b,tab_stack_task t, tab_hgwcustomer h,hgwcust_serv_info c  where a.device_id=b.device_id and a.task_id = t.task_id and a.loid = h.username and h.user_id = c.user_id and c.serv_type_id = 10 and t.service_id = "
				+ serviceId + " ");
		if (false == StringUtil.IsEmpty(cityId) && !CENTER_CITY.equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		// 添加时间判断
		if (null != starttime1 && !"".equals(starttime1)) {
			sql.append(" and a.add_time > " + starttime1 + " ");
		}
		// 判断时间
		if (null != endtime1 && !"".equals(endtime1)) {
			sql.append(" and a.add_time < " + endtime1 + " ");
		}
		// 查询成功的
		if ("1".equals(status)) {
			sql.append(" and a.result_id=1 ");
			sql.append(" and a.status=100 ");
		}
		else if ("2".equals(status)) {
			// 查询未作的
			sql.append(" and a.result_id is null ");
			sql.append(" and a.status is null ");
		}
		else if ("3".equals(status)) {
			// 查询失败的
			sql.append(" and (a.result_id <> 1 ");
			sql.append(" or a.status <> 100) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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

	/**
	 * 详情页面导出
	 *
	 * @param serviceId1
	 * @param status
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDevExcel(String serviceId1, String status, String cityId, String starttime1, String endtime1) {
		String sql = appendSqlMethod(serviceId1, status, cityId, starttime1, endtime1);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				}
				else {
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)) {
					map.put("vendor_add", vendor_add);
				}
				else {
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)) {
					map.put("device_model", device_model);
				}
				else {
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion)) {
					map.put("softwareversion", softwareversion);
				}
				else {
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				map.put("loid", rs.getString("loid"));
				map.put("serv_account", rs.getString("serv_account"));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		logger.debug("devList:" + list.toString());
		return list;
	}
}
