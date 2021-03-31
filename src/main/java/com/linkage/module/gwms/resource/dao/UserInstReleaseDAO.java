/**
 * 
 */
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-5
 * @category com.linkage.module.gwms.resource.dao
 * 
 */
public class UserInstReleaseDAO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UserInstReleaseDAO.class);

	/** JDBC */
	private JdbcTemplateExtend jt;

	/**
	 * set jdbc
	 */
	public void setDao(DataSource dao) {
		logger.debug("setDao(DataSource)");

		this.jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询未绑定的设备
	 * 
	 * @param cityId
	 *            属地：如果不需要过滤，则传入null
	 * @param deviceNo
	 *            设备序列号：如果不需要过滤，则传入null
	 * @param gwType
	 *            该字段不可为空
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDeviceInfoByNoBind(String cityId,
			String deviceNo, String loopbackIp, int gwType) {
		logger.debug(
				"UserInstReleaseDAO=>getDeviceInfoByNoBind(cityId:{},deviceNo:{},loopbackIp:{},gwType:{})",
				new Object[] { cityId, deviceNo, loopbackIp, gwType });
		PrepareSQL ppSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		ppSQL.setSQL("select a.device_id,a.city_id,a.oui,a.device_serialnumber," +
				"a.vendor_id,a.device_model_id,a.devicetype_id,a.loopback_ip,b.vendor_add," +
				"c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				"and a.devicetype_id=d.devicetype_id and a.cpe_allocatedstatus=0 ");
		ppSQL.appendAndNumber("a.gw_type", PrepareSQL.EQUEAL,String.valueOf(gwType));
		if (null != deviceNo && !"".equals(deviceNo)) {
			if (deviceNo.length() > 5) {
				ppSQL.appendAndString("a.dev_sub_sn",PrepareSQL.EQUEAL,
						deviceNo.substring(deviceNo.length() - 6,
								deviceNo.length()));
			}
			ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
					deviceNo);
		}
		if (null != loopbackIp && !"".equals(loopbackIp)) {
			ppSQL.appendAndString("a.loopback_ip", PrepareSQL.EQUEAL,
					loopbackIp);
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {

			List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			list.addAll(list1);
			ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			list = null;
			list1 = null;
		}

		List rs = jt.queryForList(ppSQL.toString());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++) {

			Map one = (Map) rs.get(i);

			Map<String, String> map = new HashMap<String, String>();

			map.put("device_id", String.valueOf(one.get("device_id")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name",
					cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", String.valueOf(one.get("oui")));
			map.put("device_serialnumber",
					String.valueOf(one.get("device_serialnumber")));
			map.put("vendor_id", String.valueOf(one.get("vendor_id")));
			map.put("vendor_add", String.valueOf(one.get("vendor_add")));
			map.put("device_model_id",
					String.valueOf(one.get("device_model_id")));
			map.put("device_model", String.valueOf(one.get("device_model")));
			map.put("devicetype_id", String.valueOf(one.get("devicetype_id")));
			map.put("softwareversion",
					String.valueOf(one.get("softwareversion")));
			map.put("loopback_ip", String.valueOf(one.get("loopback_ip")));
			result.add(map);
		}
		cityMap = null;
		return result;
	}

	/**
	 * 查询设备信息，绑定不绑定不限制
	 * 
	 * @param deviceId
	 *            设备Id
	 * @param cityId
	 *            属地：如果不需要过滤，则传入null
	 * @param deviceNo
	 *            设备序列号：如果不需要过滤，则传入null
	 * @param gwType
	 *            该字段不可为空
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDeviceInfo(String deviceId,
			String cityId, String deviceNo, String loopbackIp, int gwType) {
		logger.debug(
				"UserInstReleaseDAO=>getDeviceInfo(deviceId:{},cityId:{},deviceNo:{},loopbackIp:{},gwType:{})",
				new Object[] { deviceId, cityId, deviceNo, loopbackIp, gwType });

		PrepareSQL ppSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		ppSQL.setSQL(" select a.device_id,a.city_id,a.oui,a.device_serialnumber," +
				"a.vendor_id,a.device_model_id,a.devicetype_id,b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
				"and a.devicetype_id=d.devicetype_id ");
		ppSQL.appendAndNumber("a.gw_type", PrepareSQL.EQUEAL,
				String.valueOf(gwType));
		if (null != deviceId && !"".equals(deviceId)) {
			ppSQL.appendAndString("a.device_id", PrepareSQL.EQUEAL, deviceId);
		}
		if (null != deviceNo && !"".equals(deviceNo)) {
			if (deviceNo.length() > 5) {
				ppSQL.appendAndString(
						"a.dev_sub_sn",
						PrepareSQL.EQUEAL,
						deviceNo.substring(deviceNo.length() - 6,
								deviceNo.length()));
			}
			ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
					deviceNo);
		}
		if (null != loopbackIp && !"".equals(loopbackIp)) {
			ppSQL.appendAndString("a.loopback_ip", PrepareSQL.EQUEAL,
					loopbackIp);
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {

			List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			list.addAll(list1);
			ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			list = null;
			list1 = null;
		}

		List rs = jt.queryForList(ppSQL.toString());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++) {

			Map one = (Map) rs.get(i);

			Map<String, String> map = new HashMap<String, String>();

			map.put("device_id", String.valueOf(one.get("device_id")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name",
					cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", String.valueOf(one.get("oui")));
			map.put("device_serialnumber",
					String.valueOf(one.get("device_serialnumber")));
			map.put("vendor_id", String.valueOf(one.get("vendor_id")));
			map.put("vendor_add", String.valueOf(one.get("vendor_add")));
			map.put("device_model_id",
					String.valueOf(one.get("device_model_id")));
			map.put("device_model", String.valueOf(one.get("device_model")));
			map.put("devicetype_id", String.valueOf(one.get("devicetype_id")));
			map.put("softwareversion",
					String.valueOf(one.get("softwareversion")));

			result.add(map);
		}
		cityMap = null;
		return result;
	}

	/**
	 * 商务领航查询用户信息
	 * 
	 * @param cityId
	 *            属地为必须，否则以省中心来处理
	 * @param username
	 *            |
	 * @param deviceSN
	 *            |username、deviceSN必须传一个，否则返回size为0的List实例
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfoByBBMS(String cityId,
			String username, String deviceSN) {
		logger.debug(
				"UserInstReleaseDAO=>getUserInfoByBBMS(cityId:{},username:{},deviceSN:{})",
				new Object[] { cityId, username, deviceSN });

		if (null == username || "".equals(username)) {
			if (null == deviceSN || "".equals(deviceSN)) {
				return new ArrayList<Map<String, String>>();
			}
		}

		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber,b.customer_id,b.city_id from tab_egwcustomer a,tab_customerinfo b where a.user_state='1' and a.customer_id=b.customer_id ");
		if (null != username && !"".equals(username)) {
			ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
		}
		if (null != deviceSN && !"".equals(deviceSN)) {
			ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
					deviceSN);
		}

		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {

			List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			list.addAll(list1);
			ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			list = null;
			list1 = null;
		}

		List rs = jt.queryForList(ppSQL.toString());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++) {

			Map one = (Map) rs.get(i);

			Map<String, String> map = new HashMap<String, String>();

			String device_id = String.valueOf(one.get("device_id"));
			String oui = String.valueOf(one.get("oui"));
			String device_serialnumber = String.valueOf(one
					.get("device_serialnumber"));
			if ("null".equals(device_id) || null == device_id) {
				device_id = "";
			}
			if ("null".equals(oui) || null == oui) {
				oui = "";
			}
			if ("null".equals(device_serialnumber)
					|| null == device_serialnumber) {
				device_serialnumber = "";
			}

			map.put("user_id", String.valueOf(one.get("user_id")));
			map.put("username", String.valueOf(one.get("username")));
			map.put("device_id", device_id);
			map.put("customer_id", String.valueOf(one.get("customer_id")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name",
					cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", oui);
			map.put("device_serialnumber", device_serialnumber);

			result.add(map);
		}
		cityMap = null;
		return result;
	}

	/**
	 * 家庭网关查询用户信息
	 * 
	 * @param cityId
	 *            属地为必须，否则以省中心来处理
	 * @param username
	 *            |
	 * @param deviceSN
	 *            |username、deviceSN必须传一个，否则返回size为0的List实例 nameType "1" 用户账号
	 *            "2"业务账号 "3"Voip账号 "4"Voip电话号码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfoByITMS(String cityId,
			String username, String nameType, String deviceSN) {
		logger.debug(
				"UserInstReleaseDAO=>getUserInfoByITMS(cityId:{},username:{},deviceSN:{})",
				new Object[] { cityId, username, deviceSN });
		if (null == username || "".equals(username)) {
			if (null == deviceSN || "".equals(deviceSN)) {
				return new ArrayList<Map<String, String>>();
			}
		}

		PrepareSQL ppSQL = new PrepareSQL();
		if ("1".equals(nameType) || null == nameType) {
			ppSQL.setSQL(" select a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a where a.user_state='1' ");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			}
			if (null != deviceSN && !"".equals(deviceSN)) {
				String subdeviceSN = deviceSN.substring(deviceSN.length() - 6);
				ppSQL.setSQL(" select a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a,tab_gw_device b where a.user_state='1' and a.device_id = b.device_id");
				ppSQL.appendAndString("b.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
				ppSQL.appendAndString("b.dev_sub_sn", PrepareSQL.EQUEAL,
						subdeviceSN);
			}
		} else if ("2".equals(nameType)) {
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state='1' and b.serv_type_id=10");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			// if (null != username && !"".equals(username)) {
			// ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			// }
			if (null != deviceSN && !"".equals(deviceSN)) {
				ppSQL.appendAndString("a.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
			}
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("b.username", PrepareSQL.EQUEAL, username);
			}
		} else if ("3".equals(nameType)) {
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state='1' and b.serv_type_id=11");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			// if (null != username && !"".equals(username)) {
			// ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			// }
			if (null != deviceSN && !"".equals(deviceSN)) {
				ppSQL.appendAndString("a.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
			}
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("b.username", PrepareSQL.EQUEAL, username);
			}
		} else if ("4".equals(nameType)) {
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state='1' ");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }

			if (null != deviceSN && !"".equals(deviceSN)) {
				ppSQL.appendAndString("a.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
			}
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("b.voip_username", PrepareSQL.EQUEAL,
						username);
			}
		} else if ("5".equals(nameType)) {
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state='1' ");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }

			if (null != deviceSN && !"".equals(deviceSN)) {
				ppSQL.appendAndString("a.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
			}
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("b.voip_phone", PrepareSQL.EQUEAL,
						username);
			}
		} else {
			ppSQL.setSQL(" select a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a where a.user_state='1' ");

			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
			// {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != username && !"".equals(username)) {
				ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			}
			if (null != deviceSN && !"".equals(deviceSN)) {
				ppSQL.appendAndString("a.device_serialnumber",
						PrepareSQL.L_LIKE, deviceSN);
			}
		}

		List rs = jt.queryForList(ppSQL.toString());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++) {

			Map one = (Map) rs.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("user_id", String.valueOf(one.get("user_id")));
			map.put("username", String.valueOf(one.get("username")));
			map.put("type_name",
					this.getUserDevType(String.valueOf(one.get("user_id"))));
			String device_id = String.valueOf(one.get("device_id"));
			String oui = String.valueOf(one.get("oui"));
			String device_serialnumber = String.valueOf(one
					.get("device_serialnumber"));
			if ("null".equals(device_id) || null == device_id) {
				device_id = "";
			}
			if ("null".equals(oui) || null == oui) {
				oui = "";
			}
			if ("null".equals(device_serialnumber)
					|| null == device_serialnumber) {
				device_serialnumber = "";
			}
			map.put("device_id", device_id);
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name",
					cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", oui);
			map.put("device_serialnumber", device_serialnumber);
			String is_chk_bind = String.valueOf(one.get("is_chk_bind"));
			if ("2".equals(is_chk_bind)) {
				map.put("is_chk_bind", "已精确验证");
			} else {
				map.put("is_chk_bind", "");
			}
			result.add(map);
		}
		cityMap = null;
		return result;
	}

	/**
	 * 获取用户终端类型
	 * 
	 * @param userId
	 * @return
	 */
	private String getUserDevType(String userId) {
		logger.debug("UserInstReleaseDAO=>getUserDevType(userId:{})",
				new Object[] { userId });

		PrepareSQL ppSQL = new PrepareSQL(
				" select b.type_name from gw_cust_user_dev_type a,gw_dev_type b where a.type_id=b.type_id and a.user_id=? ");
		ppSQL.setLong(1, Long.parseLong(userId));
		List list = jt.queryForList(ppSQL.toString());
		if (null == list || list.size() == 0 || null == list.get(0)) {
			return "e8-b";
		}
		Map map = (Map) list.get(0);
		if (null == map.get("type_name")) {
			return "e8-b";
		}
		return map.get("type_name").toString();
	}

	/**
	 * 插入现场安装记录表
	 * 
	 * @param gatherId
	 *            不能为null，否则入库造成数据不整
	 * @param deviceId
	 *            不能为null，否则入库造成数据不整
	 * @param username
	 *            不能为null，否则入库造成数据不整
	 * @param dealstaff
	 *            不能为null，否则入库造成数据不整
	 * @return
	 */
	public String getSQLByAddUserinst(String gatherId, String deviceId,
			String username, String dealstaff, int userFlag) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByAddUserinst(gatherId:{},deviceId:{},username:{},dealstaff:{})",
				new Object[] { gatherId, deviceId, username, dealstaff });

		PrepareSQL ppSQL = new PrepareSQL(
				"insert into tab_userinst (gather_id,device_id,username,dealstaff,dealtime,user_flag) values (?,?,?,?,?,?)");
		ppSQL.setString(1, gatherId);
		ppSQL.setString(2, deviceId);
		ppSQL.setString(3, username);
		ppSQL.setString(4, dealstaff);
		ppSQL.setLong(5, new DateTimeUtil().getLongTime());
		ppSQL.setInt(6, userFlag);

		return ppSQL.toString();
	}

	/**
	 * modify date 2010-5-20
	 * 
	 * 插入绑定日志表，bind_log，自修改日起，所有绑定日志均操作bind_log
	 * 
	 */
	public String getSQLByAddBindlog(String username, String deviceId,
			int bindStatus, int bindResult, String bindDesc, int userline,
			String remark, int operType, int bindType, String dealstaff) {
		logger.debug("UserInstReleaseDAO=>getSQLByAddBindlog()");

		PrepareSQL ppSQL = new PrepareSQL(
				" insert into bind_log (bind_id,username,"
						+ "device_id,binddate,bind_status,bind_result,"
						+ "bind_desc,userline,remark,oper_type,bind_type,"
						+ "dealstaff) values (?,?,?,?,?,?,?,?,?,?,?,?)");

		ppSQL.setLong(1, StaticTypeCommon.generateLongId());
		ppSQL.setString(2, username);
		ppSQL.setString(3, deviceId);
		ppSQL.setLong(4, new DateTimeUtil().getLongTime());
		ppSQL.setInt(5, bindStatus);
		ppSQL.setInt(6, bindResult);
		ppSQL.setString(7, bindDesc);
		ppSQL.setInt(8, userline);
		ppSQL.setString(9, remark);
		ppSQL.setInt(10, operType);
		ppSQL.setInt(11, bindType);
		ppSQL.setString(12, dealstaff);

		return ppSQL.toString();
	}

	/**
	 * 更新设备表，现场安装时更新 （为了性能，传入字段不能为空，但不作控制）
	 * 
	 * @param cityId
	 *            不可为空
	 * @param customerId
	 *            不可为空
	 * @param deviceId
	 *            不可为空,为空则返回null的sql
	 * @return
	 */
	public String getSQLByInstUpdDevice(String cityId, String customerId,
			String deviceId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByAddUserinst(cityId:{},customerId:{},deviceId:{})",
				new Object[] { cityId, customerId, deviceId });

		if (null == deviceId || "".equals(deviceId)) {
			return null;
		}

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_gw_device set device_status=1,cpe_allocatedstatus=1,city_id=?,customer_id=? where device_id = ?");
		ppSQL.setString(1, cityId);
		ppSQL.setString(2, customerId);
		ppSQL.setString(3, deviceId);

		return ppSQL.toString();
	}

	/**
	 * 更新设备表，解绑时更新
	 * 
	 * @param deviceId
	 *            不可为空 为空则返回sql为null；
	 * @param cityId
	 *            不可为空 一般来说，ITMS到本地网级，BBMS到省中心；
	 * @param cityUpdFlag
	 *            不可为空，true表示需要更新属地，false表示不需要更新属地；
	 * @return
	 */
	public String getSQLByReleaseUpdDevice(String deviceId, String cityId,
			boolean cityUpdFlag) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByReleaseUpdDevice(deviceId:{},cityId:{},cityUpdFlag:{})",
				new Object[] { deviceId, cityId, cityUpdFlag });

		if (null == deviceId || "".equals(deviceId)) {
			return null;
		}

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_gw_device set cpe_allocatedstatus=0,customer_id=null ");
		if (cityUpdFlag) {
			ppSQL.append(" ,city_id='");
			ppSQL.append(cityId);
			ppSQL.append("' ");
		}
		ppSQL.append(PrepareSQL.WHERE, "device_id", PrepareSQL.EQUEAL,
				deviceId, false);

		return ppSQL.toString();
	}

	/**
	 * 删除权限域表
	 * 
	 * @param resId
	 * @return
	 */
	public String getSQLByDelResArea(String resId) {
		logger.debug("UserInstReleaseDAO=>getSQLByDelResArea(resId:{})", resId);

		PrepareSQL ppSQL = new PrepareSQL(
				" delete from tab_gw_res_area where res_id =? and res_type = 1 ");
		ppSQL.setString(1, resId);

		return ppSQL.toString();
	}

	/**
	 * 插入权限域表
	 * 
	 * @param resType
	 * @param resId
	 * @param AreaId
	 * @return
	 */
	public String getSQLByAddResArea(int resType, String resId, int AreaId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByAddResArea(resType:{},resId:{},AreaId:{})",
				new Object[] { resType, resId, AreaId });

		PrepareSQL ppSQL = new PrepareSQL(
				" insert into tab_gw_res_area(res_type,res_id,area_id) values(?,?,?) ");
		ppSQL.setInt(1, resType);
		ppSQL.setString(2, resId);
		ppSQL.setInt(3, AreaId);

		return ppSQL.toString();
	}

	/**
	 * 针对修障，添加修障原因
	 * 
	 * @param username
	 *            修障的账号
	 * @param device_id
	 *            修障的设备
	 * @param fault_id
	 *            修障原因ID
	 * @param dealStaff
	 * @param dealStaffid
	 * @return
	 */
	public String getSaveFault(String username, String deviceId,
			String faultId, String dealStaff, String dealStaffid) {
		logger.debug(
				"UserInstReleaseDAO=>getSaveFault(username:{},deviceId:{},faultId:{},dealStaff:{},dealStaffid:{})",
				new Object[] { username, deviceId, faultId, dealStaff,
						dealStaffid });
		PrepareSQL ppSQL = new PrepareSQL(
				"insert into tab_devicefault(username,device_id,fault_id,faulttime,dealstaff,dealstaffid) values(?,?,?,?,?,?)");
		ppSQL.setString(1, username);
		ppSQL.setString(2, deviceId);
		ppSQL.setString(3, faultId);
		ppSQL.setLong(4, (new Date()).getTime() / 1000);
		ppSQL.setString(5, dealStaff);
		ppSQL.setString(6, dealStaffid);
		return ppSQL.toString();
	}

	public long getMaxUserId() {
		if(DBUtil.GetDB() == 1 || DBUtil.GetDB() == 2) {
			return getMaxUserIdOld();
		}
		return DbUtils.getUnusedID("sql_tab_hgwcustomer", 1);
	}
	
	/**
	 * 获取用户表ID
	 * 
	 * @return
	 */
	public long getMaxUserIdOld() {
		logger.debug("UserInstReleaseDAO=>getMaxUserId");

		return jt.queryForLong("maxHgwUserIdProc 1");
	}

	// /////////////////////////////////////////////////////////////////////////
	// ////////////////////////////商务领航现场安装相关SQL//////////////////////////
	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 更新客户表属地（目前暂定为新疆的安装时操作，针对安装时更新）
	 * 
	 * @param cityId
	 * @param customerId
	 *            不可为空，为空则返回nul的sql。
	 */
	public String getSQLByInstUpdCustomer(String cityId, String customerId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByUpCustomer(cityId:{},customerId:{})",
				new Object[] { cityId, customerId });

		if (null == customerId || "".equals(customerId)) {
			return null;
		}

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_customerinfo set city_id=? where customer_id=? ");
		ppSQL.setString(1, cityId);
		ppSQL.setString(2, customerId);

		return ppSQL.toString();
	}

	/**
	 * 更新用户表（针对安装时更新）
	 * 
	 * @param userId
	 *            不可为空
	 * @param deviceId
	 *            不可为空
	 * @param oui
	 *            不可为空
	 * @param deviceNo
	 *            不可为空
	 * @return
	 */
	public String getSQLByInstUpdEgwUser(long userId, String deviceId,
			String oui, String deviceNo) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByUpEgwUser(userId:{},deviceId:{},oui:{},deviceNo:{})",
				new Object[] { userId, deviceId, oui, deviceNo });

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_egwcustomer set oui=?,device_serialnumber=?,device_id=?,updatetime=?,binddate=?,userline=? where user_id=?");
		long time = new DateTimeUtil().getLongTime();
		ppSQL.setString(1, oui);
		ppSQL.setString(2, deviceNo);
		ppSQL.setString(3, deviceId);
		ppSQL.setLong(4, time);
		ppSQL.setLong(5, time);
		ppSQL.setInt(6, 1);
		ppSQL.setLong(7, userId);

		return ppSQL.toString();
	}

	/**
	 * 更新用户表（商务领航解绑时操作）
	 * 
	 * @param userId
	 *            不可为空
	 * @return
	 */
	public String getSQLByReleaseUpdEgwUser(long userId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByReleaseUpdEgwUser(userId:{})",
				userId);

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_egwcustomer set is_chk_bind=0,oui=null,device_serialnumber=null,device_id=null,updatetime=?,binddate=null,userline=-1 where user_id=?");
		ppSQL.setLong(1, new DateTimeUtil().getLongTime());
		ppSQL.setLong(2, userId);

		return ppSQL.toString();
	}

	// /////////////////////////////////////////////////////////////////////////
	// ////////////////////////////家庭网关现场安装相关SQL//////////////////////////
	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 家庭网关更新数据库（绑定时操作）
	 * 
	 * @param userId
	 *            不可为空
	 * @param deviceId
	 *            不可为空
	 * @param oui
	 *            不可为空
	 * @param deviceNo
	 *            不可为空
	 * @param cityId
	 *            不可为空
	 * @param cityUpdFlag
	 *            不可为空
	 * @return
	 */
	public String getSQLByInstUpdHgwUser(long userId, String deviceId,
			String oui, String deviceNo, String cityId, boolean cityUpdFlag,
			int userline) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByInstUpdHgwUser(userId:{},deviceId:{},oui:{},deviceNo:{},cityId:{},cityUpdFlag:{})",
				new Object[] { userId, deviceId, oui, deviceNo, cityId,
						cityUpdFlag });

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_hgwcustomer set oui=?,device_serialnumber=?, device_id=? ,updatetime=? , binddate=? , userline=?");
		if (cityUpdFlag) {
			ppSQL.append(" ,city_id='");
			ppSQL.append(cityId);
			ppSQL.append("' ");
		}
		long time = new DateTimeUtil().getLongTime();
		ppSQL.append(" where user_id= ");
		ppSQL.append(String.valueOf(userId));
		ppSQL.setString(1, oui);
		ppSQL.setString(2, deviceNo);
		ppSQL.setString(3, deviceId);
		ppSQL.setLong(4, time);
		ppSQL.setLong(5, time);
		ppSQL.setInt(6, userline);

		return ppSQL.toString();
	}

	/**
	 * 家庭网关现场安装插入（针对绑定时用户不存在操作）
	 * 
	 * @param userId
	 * @param gatherId
	 * @param username
	 * @param deviceId
	 * @param oui
	 * @param deviceNo
	 * @param cityId
	 * @param userTypeId
	 * 
	 * @return
	 */
	public String getSQLByInstAddHgwUser(long userId, String gatherId,
			String username, String deviceId, String oui, String deviceNo,
			String cityId, String userTypeId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByInstAddHgwUser(userId({})...)",
				userId);

		PrepareSQL ppSQL = new PrepareSQL(
				" insert into tab_hgwcustomer (user_id,gather_id,username,oui,device_serialnumber,city_id,cust_type_id,user_type_id,user_state,opendate,binddate,updatetime,device_id,userline) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		long time = new DateTimeUtil().getLongTime();
		ppSQL.setLong(1, userId);
		ppSQL.setString(2, gatherId);
		ppSQL.setString(3, username);
		ppSQL.setString(4, oui);
		ppSQL.setString(5, deviceNo);
		ppSQL.setString(6, cityId);
		ppSQL.setInt(7, 2);
		ppSQL.setString(8, userTypeId);
		ppSQL.setString(9, "1");
		ppSQL.setLong(10, time);
		ppSQL.setLong(11, time);
		ppSQL.setLong(12, time);
		ppSQL.setString(13, deviceId);
		ppSQL.setInt(14, 0);

		return ppSQL.toString();
	}

	/**
	 * 家庭网关现场安装（业务用户表）(IPOSS安装时操作)
	 * 
	 * @param userId
	 *            不可为空
	 * @param servTypeId
	 *            不可为空
	 * @param username
	 *            不可为空
	 * @return
	 */
	public String getSQLByInstAddHgwcust(long userId, int servTypeId,
			String username) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByInstAddHgwcust(userId({})...)",
				userId);

		PrepareSQL ppSQL = new PrepareSQL(
				" insert into hgwcust_serv_info (user_id,serv_type_id,username,serv_status,dealdate,updatetime) values(?,?,?,?,?,?) ");
		long time = new DateTimeUtil().getLongTime();
		ppSQL.setLong(1, userId);
		ppSQL.setInt(2, servTypeId);
		ppSQL.setString(3, username);
		ppSQL.setInt(4, 1);
		ppSQL.setLong(5, time);
		ppSQL.setLong(6, time);

		return ppSQL.toString();
	}

	/**
	 * 更新家庭网关用户数据（家庭网关解绑时操作）
	 * 
	 * @param userId
	 * @return
	 */
	public String getSQLByInstUpdServUser(long userId) {
		logger.debug("UserInstReleaseDAO=>getSQLByInstUpdServUser(userId:{})",
				userId);

		PrepareSQL ppSQL = new PrepareSQL(" update hgwcust_serv_info set "
				+ " open_status=0,updatetime=? where user_id=? and "
				+ " serv_status in (1,2) and open_status!=0 ");
		ppSQL.setLong(1, new DateTimeUtil().getLongTime());
		ppSQL.setLong(2, userId);

		return ppSQL.toString();
	}

	/**
	 * 批量更新数据库
	 */
	public int[] batchUpdate(String[] sql) {
		logger.debug("UserInstReleaseDAO=>batchUpdate(sql:{})", sql);

		return jt.batchUpdate(sql);
	}

	/**
	 * 单条更新数据库
	 */
	public int update(String sql) {
		logger.debug("UserInstReleaseDAO=>update(sql:{})", sql);

		return jt.update(sql);
	}

	/**
	 * 更新家庭网关用户数据（家庭网关解绑时操作）
	 * 
	 * @param userId
	 * @return
	 */
	public String getSQLByReleaseUpdHgwUser(long userId) {
		logger.debug(
				"UserInstReleaseDAO=>getSQLByReleaseUpdHgwUser(userId:{})",
				userId);

		PrepareSQL ppSQL = new PrepareSQL(
				" update tab_hgwcustomer set oui=null,device_serialnumber=null,device_id=null, binddate=null,userline=-1,is_chk_bind=0, updatetime=? where user_id=?");
		ppSQL.setLong(1, new DateTimeUtil().getLongTime());
		ppSQL.setLong(2, userId);

		return ppSQL.toString();
	}

	/**
	 * 获取用户业务用户表信息
	 */
	public List getUserServInfo(long userId) {
		logger.debug("UserInstReleaseDAO=>getUserServInfo(userId:{})", userId);
		PrepareSQL ppSQL = new PrepareSQL(
				" select open_status from hgwcust_serv_info where user_id=? and serv_status in (1,2) ");
		ppSQL.setLong(1, userId);
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 判断当前的绑定关系
	 */
	public List getUserBindInfo(long userId) {
		logger.debug("UserInstReleaseDAO=>getUserBindInfo({})", userId);
		PrepareSQL ppSQL = new PrepareSQL(
				" select user_id,device_id from tab_hgwcustomer where user_state in ('1','2') and user_id=? ");
		ppSQL.setLong(1, userId);
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 判断当前的绑定关系
	 */
	public List getDeviceBindInfo(String deviceId) {
		logger.debug("UserInstReleaseDAO=>getUserBindInfo({})", deviceId);
		PrepareSQL ppSQL = new PrepareSQL(
				" select user_id from tab_hgwcustomer where user_state in ('1','2') and device_id=? ");
		ppSQL.setString(1, deviceId);
		return jt.queryForList(ppSQL.toString());
	}
}
