package com.linkage.module.gtms.config.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModifyVlanToolsDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(ModifyVlanToolsDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() {
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(" select count(*) allnum from tab_modify_vlan_task_dev where add_time>="
				+ startTime + " and add_time<" + (startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "allnum", 0);
	}


	public long getStartTime() {
		try {
			String day = sdfDay.format(new Date());
			return sdfSec.parse(day + " 00:00:00").getTime() / 1000L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds4NX(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}

	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTask4NX(long taskid, long accoid, String param, int type, String serviceId, String strategy_type){

		PrepareSQL psql = new PrepareSQL(
				"insert into tab_modify_vlan_task(task_id,acc_oid,add_time,service_id,param,type,strategy_type) values(?,?,?,?,?, ?,?)");
		psql.setLong(1, taskid);
		psql.setLong(2, accoid);
		psql.setLong(3, taskid);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, StringUtil.getIntegerValue(strategy_type));

		return jt.update(psql.getSQL());

	}


	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		ArrayList<String> sqlListTmp = new ArrayList<String>();
		if(sqlList.size()>0){
			for(String sql : sqlList){
				sqlListTmp.add(sql);
				if(sqlListTmp.size()>=200){
					int resTmp = DBOperation.executeUpdate(sqlListTmp);
					if(resTmp>0){
						res += sqlListTmp.size();
					}
					sqlListTmp.clear();
				}
			}
		}
		if(sqlListTmp.size()>0){
			int resTmp = DBOperation.executeUpdate(sqlListTmp);
			if(resTmp>0){
				res += sqlListTmp.size();
			}
			sqlListTmp.clear();
		}
		logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
	}


	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevList4NX(String filename) {

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append(" select a.device_serialnumber a_device_id, b.device_id, b.oui, b.device_serialnumber, ");
		psql.append(" c.username, b.city_id from tab_temporary_device a left join tab_gw_device b on ");
		psql.append(" a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
		psql.append(" where a.filename=? ");
		psql.setString(1, filename);

		return DBOperation.getRecords(psql.getSQL());
	}

	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,long taskid, String serviceId){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String a_device_id = String.valueOf(devList.get(i).get("a_device_id"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));
			String netusername = String.valueOf(devList.get(i).get("serusername"));
			String loid = String.valueOf(devList.get(i).get("username"));

			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into tab_modify_vlan_task_dev (task_id,device_id,oui,device_serialnumber ");
			pSql.append(" ,file_username,loid,netusername,add_time,update_time) values(?,?,?,?,?, ?,?,?,?)");

			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, oui);
			pSql.setString(4, device_serialnumber);
			pSql.setString(5, a_device_id);
			pSql.setString(6, loid);
			pSql.setString(7, netusername);
			pSql.setLong(8, taskid);
			pSql.setLong(9, taskid);

			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}


	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev4NX(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}


	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	public List<Map> queryModifyVlanInfo(String serviceId) {// TODO wait (more table related)
		logger.debug("ModifyVlanCountDAO--queryModifyVlanInfo({})", serviceId);
		String sql = "select b.city_id, d.status, d.result_id,count(*) as total  from tab_modify_vlan_task_dev d , tab_modify_vlan_task t ,tab_gw_device  b   where d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = "
				+ serviceId + " group by b.city_id,d.status,d.result_id";
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
				map.put("loid",rs.getString("loid"));
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
		sql.append("select a.loid,b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,a.res fault_desc from tab_modify_vlan_task_dev a,tab_gw_device b,tab_modify_vlan_task t where a.device_id=b.device_id and a.task_id = t.task_id and t.service_id = "
				+ serviceId + " ");
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId))) {
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
		sql.append("select count(*) from tab_modify_vlan_task_dev a,tab_gw_device b,tab_modify_vlan_task t where a.device_id=b.device_id and a.task_id = t.task_id and t.service_id = "
				+ serviceId + " ");
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId))) {
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
				map.put("loid",rs.getString("loid"));
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
	public List<Map> queryModifyVlanInfo(String serviceId, String starttime1, String endtime1, String cityId) {
		logger.warn("ModifyVlanCountDAO--queryModifyVlanInfo({})", serviceId);
		/**
		 * select b.city_id, d.status, d.result_id,d.res,count(*) as total from
		 * tab_modify_vlan_task_dev d , tab_modify_vlan_task t ,tab_gw_device b where
		 * d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = 2301 group
		 * by b.city_id,d.status,d.result_id,d.res
		 */
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.city_id, d.status, d.result_id,count(*) as total  from tab_modify_vlan_task_dev d , tab_modify_vlan_task t ,tab_gw_device  b  "
				+ " where d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = ");
		sql.append(" " + serviceId + " ");
		// 判断属地
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
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
				+ "from tab_modify_vlan_task_dev a,tab_gw_device b,tab_modify_vlan_task t, tab_hgwcustomer h,hgwcust_serv_info c  where a.device_id=b.device_id and a.task_id = t.task_id and a.loid = h.username and h.user_id = c.user_id and c.serv_type_id = 10 and t.service_id = "
				+ serviceId + " ");
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
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
				+ "from tab_modify_vlan_task_dev a,tab_gw_device b,tab_modify_vlan_task t, tab_hgwcustomer h,hgwcust_serv_info c  where a.device_id=b.device_id and a.task_id = t.task_id and a.loid = h.username and h.user_id = c.user_id and c.serv_type_id = 10 and t.service_id = "
				+ serviceId + " ");
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
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
