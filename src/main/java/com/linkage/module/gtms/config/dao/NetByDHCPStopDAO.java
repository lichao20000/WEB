package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class NetByDHCPStopDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(NetByDHCPStopDAO.class);
	private List<Map<String, String>> countList;

	public static Logger getLogger() {
		return logger;
	}

	@SuppressWarnings("unchecked")
	public String isBind(String gwShare_queryField, String gwShare_queryParam) {
		logger.warn("NetByDHCPStopDAO.isBind()");
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		String flag = "1";
		if ("deviceSn".equals(gwShare_queryField)) {
			String sub = gwShare_queryParam.substring(gwShare_queryParam
					.length() - 6);
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id,a.customer_id as queryid from tab_gw_device a"
							+ " where  a.device_serialnumber like '%"
							+ gwShare_queryParam + "' and a.dev_sub_sn = '"
							+ sub + "'");
			list = jt.queryForList(psql.getSQL());
		} else if ("username".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.username,a.device_id as queryid from tab_hgwcustomer a "
							+ "where a.username = '" + gwShare_queryParam + "'");
			list = jt.queryForList(psql.getSQL());
		} else if ("kdname".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select b.username,a.device_id as queryid from tab_hgwcustomer a,hgwcust_serv_info b "
							+ "where b.serv_type_id = 10 and b.username = '"
							+ gwShare_queryParam
							+ "' and a.user_id = b.user_id");
			list = jt.queryForList(psql.getSQL());
		} else if ("voipPhoneNum".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select b.voip_phone,a.device_id as queryid from tab_hgwcustomer a,tab_voip_serv_param b"
							+ " where b.voip_phone = '"
							+ gwShare_queryParam
							+ "' and a.user_id = b.user_id");
			list = jt.queryForList(psql.getSQL());
		}
		if (null != list && list.size() > 0) {
			map = list.get(0);
			if (null != map) {
				String queryId = StringUtil.getStringValue(map.get("queryid"));
				if (queryId.length() > 0) {
					String deviceId = StringUtil.getStringValue(map
							.get("device_id"));
					flag = deviceId;
				} else {
					if ("deviceSn".equals(gwShare_queryField)) {
						flag = "2";
					} else {
						flag = "3";
					}
				}
			}
		} else {
			flag = "1";
		}
		return flag;
	}

	/*
	 * 查询宽带帐号
	 *
	 * @SuppressWarnings("unchecked") public String getUsername(String
	 * deviceIds) { String username = ""; PrepareSQL psql = new PrepareSQL(
	 * "select b.username from tab_hgwcustomer a,hgwcust_serv_info b where b.serv_type_id = 10  and a.user_id = b.user_id and a.device_id  = '"
	 * + deviceIds + "'"); List<Map<String, String>> list =
	 * jt.queryForList(psql.getSQL()); if (null != list) { for (int i = 0; i <
	 * list.size(); i++) { Map<String, String> map = list.get(i); if (i !=
	 * list.size() - 1) { username +=
	 * StringUtil.getStringValue(map.get("username")) + ","; } else { username
	 * += StringUtil.getStringValue(map.get("username")); } } } return username;
	 * }
	 */

	@SuppressWarnings("unchecked")
	public void doConfig(long userId, List<String> list, String gwType,
			String serviceId, String strategy_type, String username, long time) {
		logger.warn("NetByDHCPStopDAO.doConfig()");
		ArrayList<String> sqllist = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_dhcp_task(task_id,acc_oid,add_time,service_id,net_account,strategy_type) values(?,?,?,?,?,?)");
		psql.setLong(1, time);
		psql.setLong(2, userId);
		psql.setLong(3, time);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		if (!username.equals("undefined")) {
			psql.setString(5, StringUtil.getStringValue(username));
		} else {
			psql.setString(5, "");
		}
		psql.setString(6, StringUtil.getStringValue(strategy_type));
		sqllist.add(psql.getSQL());
		String deviceIds = "";
		if (null != list && list.size() > 0) {
			for (String dev : list) {
				deviceIds += "'" + dev + "'" + ",";
			}
		}
		if (deviceIds.length() > 0) {
			deviceIds = deviceIds.substring(0, deviceIds.length() - 1);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select a.device_id ,a.oui,a.device_serialnumber,b.username as loid from tab_gw_device a ,tab_hgwcustomer b ");
		sb.append(" where a.device_id = b.device_id ");
		sb.append(" and a.device_id in(");
		sb.append(deviceIds).append(")");
		PrepareSQL sql = new PrepareSQL(sb.toString());
		List<Map<String, String>> lt = jt.queryForList(sql.getSQL());
		if (null != lt && lt.size() > 0) {
			for (Map<String, String> map : lt) {
				psql = new PrepareSQL(" insert into tab_dhcp_task_dev(task_id,device_id,oui,device_serialnumber,loid,result_id,status) values(?,?,?,?,?,?,?)");
				psql.setLong(1, time);
				psql.setString(2, StringUtil.getStringValue(map.get("device_id")));
				psql.setString(3, StringUtil.getStringValue(map.get("oui")));
				psql.setString(4, StringUtil.getStringValue(map.get("device_serialnumber")));
				psql.setString(5, StringUtil.getStringValue(map.get("loid")));
				psql.setInt(6, 0);
				psql.setInt(7, 0);
				sqllist.add(psql.getSQL());
			}
		}
		jt.batchUpdate((String[]) sqllist.toArray(new String[0]));
	}

	public List<Map> getDevice_id(String con, String condition) {
		logger.debug("NetByDHCPStopDAO.getDevice_id()");
		List<Map> list = new ArrayList<Map>();
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id ");
		if ("1".equals(con)) {
			String sub = condition.substring(condition.length() - 6);
			sql.append("from tab_gw_device where device_serialnumber like '%"
					+ condition + "' and dev_sub_sn = '" + sub + "'");
		} else if ("0".equals(con)) {
			sql.append("from tab_hgwcustomer where username = '" + condition
					+ "' ");
		} else {
			sql.append("from tab_hgwcustomer a，hgwcust_serv_info b where a.user_id = b.user_id "
					+ "and b.serv_type_id = 10 and b.username = '"
					+ condition
					+ "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		list = jt.queryForList(sql.toString());
		return list;
	}

	public List<Map> getDetailsForPage(String device_id, long starttime,
			long endtime, String openState, int curPage_splitPage,
			int num_splitPage) {
		logger.debug("NetByDHCPStopDAO.getDetailsForPage()");
		List<Map> list = new ArrayList<Map>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.service_id,a.time,a.start_time,a.status,a.result_id,a.result_desc");
		sql.append(" from gw_serv_strategy_batch_log a,tab_dhcp_task b ");
		sql.append(" where a.ids_task_id = b.task_id ");
		sql.append(" and a.device_id = " + device_id + " " + " and a.time < "
				+ endtime + " and a.time > " + starttime);
		if (!openState.equals("2")) {
			if (openState.equals("1")) {
				sql.append(" and a.status = 100 and a.result_id = 1");
			} else if (openState.equals("0")) {
				sql.append(" and a.status = 100 and a.result_id <> 1");
			} else {
				sql.append(" and a.status <> 100");
			}
		}

		sql.append(" order by a.time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// 业务名称
				map.put("service_id", "宽带DHCP关闭");
				// 策略状态
				String status = rs.getString("status");
				String result_id = rs.getString("result_id");
				if ("100".equals(status)) {
					map.put("status", "执行完成");
					if ("1".equals(result_id)) {
						map.put("result_id", "成功");
					} else {
						map.put("result_id", "失败");
					}
				} else {
					map.put("status", "等待执行");
					map.put("result_id", "");
				}
				// 将毫秒转换成时间
				try {
					// 定制时间
					long time = StringUtil.getLongValue(rs.getString("time"));
					// 执行时间
					long start_time = StringUtil.getLongValue(rs
							.getString("start_time"));
					map.put("time", new DateTimeUtil(time * 1000).getLongDate());
					if (!"等待执行".equals(map.get("status"))) {
						map.put("start_time", new DateTimeUtil(
								start_time * 1000).getLongDate());
					} else {
						map.put("start_time", "");
					}
				} catch (Exception e) {
					map.put("time", "");
					map.put("start_time", "");
				}
				String result_desc = rs.getString("result_desc");
				if (Global.G_Fault_Map.get(StringUtil
						.getIntegerValue(result_id)) == null) {
					map.put("result_desc", result_desc);
				} else {
					map.put("result_desc",
							Global.G_Fault_Map.get(
									StringUtil.getIntegerValue(result_id))
									.getFaultReason());
				}
				return map;
			}
		});
		return list;
	}

	public int getDetailsCount(String device_id, long starttime, long endtime,
			String openState, int num_splitPage) {
		logger.debug("NetByDHCPStopDAO.getDetailsCount()");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*)");
		sql.append("from gw_serv_strategy_batch_log a,tab_dhcp_task b ");
		sql.append(" where a.ids_task_id = b.task_id ");
		sql.append(" and a.device_id = " + device_id + " " + " and a.time < "
				+ endtime + " and a.time > " + starttime);
		if (!openState.equals("2")) {
			if (openState.equals("1")) {
				sql.append(" and a.status = 100 and a.result_id = 1");
			} else if (openState.equals("0")) {
				sql.append(" and a.status = 100 and a.result_id <> 1");
			} else {
				sql.append(" and a.status <> 100");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();

		int maxPage = 1;
		int total = jt.queryForInt(sql.toString());
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map<String, String>> getCountList() {
		return countList;
	}

	public void setCountList(List<Map<String, String>> countList) {
		this.countList = countList;
	}

	public static void main(String[] args) {
		String deid = "18";
		String d = StringUtil.getStringValue(deid);
		System.out.println(d);
	}
}
