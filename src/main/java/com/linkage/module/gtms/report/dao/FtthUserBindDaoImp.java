package com.linkage.module.gtms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 *
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 25, 2012 3:03:08 PM
 * @category com.linkage.module.gtms.report.dao
 * @copyright 南京联创科技 网管科技部
 *
 */
public class FtthUserBindDaoImp extends SuperDAO implements FtthUserBindDao {

	private static Logger logger = LoggerFactory
			.getLogger(FtthUserBindDaoImp.class);

	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;
	private Map<String, String> bindTypeMap = null; // 绑定方式
	private HashMap<String, String> venderMap = null; // 设备厂商
	private HashMap<String, String> deviceModelMap = null; // 设备型号
	private HashMap<String, String> deviceSoftVersionMap = null; // 设备版本

	/**
	 * 所有工单用户数（全量FTTH用户）
	 *
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> getAllFtthUser(String cityId, String starttime1,
			String endtime1, String isFiber) {

		logger.debug("FtthUserBindDaoImp==>getAllFtthUser({},{},{})",
				new Object[] { cityId, starttime1, endtime1 });

		Map<String, String> map = new HashMap<String, String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.city_id, count(*) as total ");
		psql.append("  from tab_hgwcustomer a , gw_cust_user_dev_type b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and b.type_id = '2' ");

		if (false == StringUtil.IsEmpty(starttime1)) {
			psql.append(" and a.opendate >= " + starttime1);
		}

		if (false == StringUtil.IsEmpty(endtime1)) {
			psql.append(" and a.opendate <= " + endtime1);
		}
		if (!StringUtil.IsEmpty(isFiber) && "1".equals(isFiber)) {
			psql.append(" and a.adsl_hl in (3,4) ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}

		psql.append(" group by a.city_id");

		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}

		return map;
	}

	/**
	 * 绑定用户数
	 *
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> getBindFtthUser(String cityId,
			String starttime1, String endtime1, String isFiber) {

		logger.debug("FtthUserBindDaoImp==>getBindFtthUser({},{},{})",
				new Object[] { cityId, starttime1, endtime1 });

		Map<String, String> map = new HashMap<String, String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.city_id, count(*) as total ");
		psql.append("  from tab_hgwcustomer a , gw_cust_user_dev_type b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and b.type_id = '2' ");

		if (LipossGlobals.isOracle()) {
			psql.append("   and a.device_id is not null ");
		} else {
			psql.append("   and a.device_id != null ");
			psql.append("   and a.device_id != '' ");
		}

		if (false == StringUtil.IsEmpty(starttime1)) {
			psql.append(" and a.opendate >= " + starttime1);
		}

		if (false == StringUtil.IsEmpty(endtime1)) {
			psql.append(" and a.opendate <= " + endtime1);
		}
		if (!StringUtil.IsEmpty(isFiber) && "1".equals(isFiber)) {
			psql.append(" and a.adsl_hl in (3,4) ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}

		psql.append(" group by a.city_id");

		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}

		return map;
	}

	/**
	 * 获取用户列表
	 *
	 * @param bindFlag
	 *            0 表示获取所有用户列表 1 表示获取已绑定用户列表
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId,
			String starttime1, String endtime1, int curPage_splitPage,
			int num_splitPage, String isFiber) {

		logger.debug("FtthUserBindDaoImp==>getUserList({},{},{},{},{},{})",
				new Object[] { bindFlag, cityId, starttime1, endtime1,
						curPage_splitPage, num_splitPage });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select a.device_id, a.city_id, a.username, a.user_id, ");
		psql.append(" a.user_type_id, a.oui, a.device_serialnumber, a.opendate, a.userline,c.vendor_id,c.device_model_id,c.devicetype_id ");
		psql.append("  from tab_hgwcustomer a left join tab_gw_device c on a.device_id = c.device_id,gw_cust_user_dev_type b");
		psql.append(" where 1=1");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and b.type_id = '2' ");

		/** 绑定用户 */
		if ("1".equals(bindFlag)) {
			if (LipossGlobals.isOracle()) {
				psql.append("   and a.device_id is not null ");
			} else {
				psql.append("   and a.device_id != null and a.device_id != '' ");
			}
		}

		if (false == StringUtil.IsEmpty(starttime1)) {
			psql.append(" and a.opendate >= ");
			psql.append(starttime1);
		}

		if (false == StringUtil.IsEmpty(endtime1)) {
			psql.append(" and a.opendate <= ");
			psql.append(endtime1);
		}
		if (!StringUtil.IsEmpty(isFiber) && "1".equals(isFiber)) {
			psql.append(" and a.adsl_hl in (3,4) ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}

		psql.append(" order by a.city_id ");

		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		venderMap = VendorModelVersionDAO.getVendorMap(); // 设备厂商
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 设备型号
		deviceSoftVersionMap = VendorModelVersionDAO.getDevicetypeMap(); // 设备版本

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String, String> map = new HashMap<String, String>();

				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));

				String city_id = StringUtil.getStringValue(rs
						.getString("city_id"));
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("user_id",
						StringUtil.getStringValue(rs.getString("user_id")));

				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)) {
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)) {
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs
						.getString("oui");
				String device_serialnumber = rs
						.getString("device_serialnumber") == null ? "" : rs
						.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);
				// 将opendate转换成时间
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)) {
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)) {
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				String vendor_id = rs.getString("vendor_id"); // 设备ID
				if (false == StringUtil.IsEmpty(vendor_id)) {
					map.put("vendor_add", venderMap.get(vendor_id)); // 设备厂商
				} else {
					map.put("vendor_add", "-");
				}
				String device_model_id = rs.getString("device_model_id"); // 型号ID
				if (false == StringUtil.IsEmpty(device_model_id)) {
					map.put("device_model", deviceModelMap.get(device_model_id));// 设备型号
				} else {
					map.put("device_model", "-");
				}
				String devicetype_id = rs.getString("devicetype_id");// 版本ID
				if (false == StringUtil.IsEmpty(devicetype_id)) {
					map.put("softversion",
							deviceSoftVersionMap.get(devicetype_id)); // 设备型号
				} else {
					map.put("softversion", "-");
				}
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		venderMap = null;
		deviceModelMap = null;
		deviceSoftVersionMap = null;

		return list;
	}

	/**
	 * 用户列表分页
	 *
	 * @param bindFlag
	 *            0 表示获取所有用户列表 1 表示获取已绑定用户列表
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage,
			String isFiber) {

		logger.debug("FtthUserBindDaoImp==>getUserCount({},{},{},{},{},{})",
				new Object[] { bindFlag, cityId, starttime1, endtime1,
						curPage_splitPage, num_splitPage });

		PrepareSQL psql = new PrepareSQL();

		psql.append("select count(*) ");
		psql.append("  from tab_hgwcustomer a ,gw_cust_user_dev_type b");
		psql.append(" where 1=1");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and b.type_id = '2' ");

		/** 绑定用户 */
		if ("1".equals(bindFlag)) {
			if (LipossGlobals.isOracle()) {
				psql.append("   and a.device_id is not null ");
			} else {
				psql.append("   and a.device_id != null and a.device_id != '' ");
			}
		}

		if (false == StringUtil.IsEmpty(starttime1)) {
			psql.append(" and a.opendate >= ");
			psql.append(starttime1);
		}

		if (false == StringUtil.IsEmpty(endtime1)) {
			psql.append(" and a.opendate <= ");
			psql.append(endtime1);
		}
		if (!StringUtil.IsEmpty(isFiber) && "1".equals(isFiber)) {
			psql.append(" and a.adsl_hl in (3,4) ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}

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
	 * 用户列表导出Excel
	 *
	 * @param bindFlag
	 *            0 表示获取所有用户列表 1 表示获取已绑定用户列表
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */

	public List<Map> getUserExcel(String bindFlag, String cityId,
			String starttime1, String endtime1, String isFiber) {

		logger.debug("FtthUserBindDaoImp==>getUserExcel({},{},{},{})",
				new Object[] { bindFlag, cityId, starttime1, endtime1 });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select a.device_id, a.city_id, a.username, a.user_id, a.user_type_id,a.oui ,");
		psql.append(" a.device_serialnumber, a.opendate, a.userline,c.vendor_id,c.device_model_id,c.devicetype_id ");
		psql.append(" from tab_hgwcustomer a left join tab_gw_device c on a.device_id = c.device_id,gw_cust_user_dev_type b");
		psql.append(" where 1=1");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and b.type_id = '2' ");

		/** 已绑定用户 */
		if ("1".equals(bindFlag)) {
			if (LipossGlobals.isOracle()) {
				psql.append("   and a.device_id is not null ");
			} else {
				psql.append("   and a.device_id != null and a.device_id != '' ");
			}
		}

		if (false == StringUtil.IsEmpty(starttime1)) {
			psql.append(" and a.opendate >= " + starttime1);
		}

		if (false == StringUtil.IsEmpty(endtime1)) {
			psql.append(" and a.opendate <= " + endtime1);
		}
		if (!StringUtil.IsEmpty(isFiber) && "1".equals(isFiber)) {
			psql.append(" and a.adsl_hl in (3,4) ");
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}

		psql.append(" order by a.city_id ");

		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		venderMap = VendorModelVersionDAO.getVendorMap(); // 设备厂商
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap(); // 设备型号
		deviceSoftVersionMap = VendorModelVersionDAO.getDevicetypeMap(); // 设备版本

		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)) {
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)) {
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs
						.getString("oui");
				String device_serialnumber = rs
						.getString("device_serialnumber") == null ? "" : rs
						.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);

				// 将opendate转换成时间
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)) {
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)) {
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				String vendor_id = rs.getString("vendor_id"); // 设备ID
				if (false == StringUtil.IsEmpty(vendor_id)) {
					map.put("vendor_add", venderMap.get(vendor_id)); // 设备厂商
				} else {
					map.put("vendor_add", "-");
				}
				String device_model_id = rs.getString("device_model_id"); // 型号ID
				if (false == StringUtil.IsEmpty(device_model_id)) {
					map.put("device_model", deviceModelMap.get(device_model_id));// 设备型号
				} else {
					map.put("device_model", "-");
				}
				String devicetype_id = rs.getString("devicetype_id");// 版本ID
				if (false == StringUtil.IsEmpty(devicetype_id)) {
					map.put("softversion",
							deviceSoftVersionMap.get(devicetype_id)); // 设备型号
				} else {
					map.put("softversion", "-");
				}
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		venderMap = null;
		deviceModelMap = null;
		deviceSoftVersionMap = null;
		return list;
	}

	/**
	 * 获取 用户类型ID 与用户类型名称 对应的Map
	 *
	 * @return
	 */
	public Map<String, String> getUserType() {

		logger.debug("FtthUserBindDaoImp==>getUserType()");

		String sql = "select user_type_id, type_name from user_type";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		Map<String, String> userTypeMap = new HashMap<String, String>();
		for (Map map : list) {
			userTypeMap.put(StringUtil.getStringValue(map.get("user_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return userTypeMap;
	}

	/**
	 * 获取绑定方式ID 与 绑定方式名称对应的Map
	 *
	 * @return
	 */
	public Map<String, String> getBindType() {

		logger.debug("FtthUserBindDaoImp==>getBindType()");

		String sql = "select bind_type_id, type_name from bind_type";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		Map<String, String> bindTypeMap = new HashMap<String, String>();
		for (Map map : list) {
			bindTypeMap.put(StringUtil.getStringValue(map.get("bind_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return bindTypeMap;
	}

}
