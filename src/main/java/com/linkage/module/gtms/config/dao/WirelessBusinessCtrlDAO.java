package com.linkage.module.gtms.config.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class WirelessBusinessCtrlDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(WirelessBusinessCtrlDAO.class);

	public Map<String, String> checkUserExsists(String deviceId, String gwType) {
		logger.debug("checkUserExsists({},{})", deviceId, gwType);
		Map map = null;
		StringBuffer sql = new StringBuffer();
		if (gwType.equals(Global.GW_TYPE_ITMS)) {
			sql.append("select spec_id from  tab_hgwcustomer  ");
			sql.append("  where device_id='" + deviceId + "'");
		} else if (gwType.equals(Global.GW_TYPE_BBMS)) {
			sql.append("select spec_id from  tab_egwcustomer   ");
			sql.append("  where device_id='").append(deviceId).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		List list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			map = (Map) list.get(0);
		}
		return map;
	}

	public String getSpecName(String specId) {
		String specName = "";
		PrepareSQL psql = new PrepareSQL(
				"select spec_name from tab_bss_dev_port where id = " + specId);
		Map<String, String> map = jt.queryForMap(psql.getSQL());
		if (null != map) {
			specName = StringUtil.getStringValue(map.get("spec_name"));
		}
		return specName;
	}

	public void doConfig (long userId, List<String> list, String gwType,
			String serviceId, String strategy_type, String vlanIdMark,
			String ssid, long time, String wireless_port, String buss_level,
			String channel, String awifi_type) {

		ArrayList<String> sqllist = new ArrayList<String>();
		//

		PrepareSQL psql = new PrepareSQL(
				"insert into tab_wirelesst_task(task_id,acc_oid,add_time,service_id,vlan_id,ssid,strategy_type,wireless_port,buss_level,channel,wireless_type) values(?,?,?,?,?,?,?,?,?,?,?)");
		psql.setLong(1, time);
		psql.setLong(2, userId);
		psql.setLong(3, time);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		psql.setInt(5, StringUtil.getIntegerValue(vlanIdMark));
		psql.setString(6, ssid);
		psql.setString(7, strategy_type);
		psql.setInt(8, StringUtil.getIntegerValue(wireless_port));
		psql.setInt(9, StringUtil.getIntegerValue(buss_level));
		if ("".equals(channel) || null == channel) {
			psql.setString(10, "");
		} else {
			psql.setString(10, channel);
		}
		psql.setInt(11, StringUtil.getIntegerValue(awifi_type));
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
				psql = new PrepareSQL(" insert into tab_wirelesst_task_dev(task_id,device_id,oui,device_serialnumber,loid,result_id,status) values(?,?,?,?,?,?,?)");
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


	public String doConfig4Special(long userId, String deviceId, String gwType,
			String serviceId, String strategy_type, String vlanIdMark,
			String ssid, long time, String wireless_port, String buss_level,
			String channel, String awifi_type,long task_id) {

		String res = "1";
		ArrayList<String> sqllist = new ArrayList<String>();

		PrepareSQL psql = new PrepareSQL(
				"insert into tab_wirelesst_task(task_id,acc_oid,add_time,service_id,vlan_id,ssid,strategy_type,wireless_port,buss_level,channel,wireless_type) values(?,?,?,?,?,?,?,?,?,?,?)");
		psql.setLong(1, task_id);
		psql.setLong(2, userId);
		psql.setLong(3, time);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		psql.setInt(5, StringUtil.getIntegerValue(vlanIdMark));
		psql.setString(6, ssid);
		psql.setString(7, strategy_type);
		psql.setInt(8, StringUtil.getIntegerValue(wireless_port));
		psql.setInt(9, StringUtil.getIntegerValue(buss_level));
		if ("".equals(channel) || null == channel) {
			psql.setString(10, "");
		} else {
			psql.setString(10, channel);
		}
		psql.setInt(11, StringUtil.getIntegerValue(awifi_type));

		StringBuffer sb = new StringBuffer();
		sb.append("select a.device_id ,a.oui,a.device_serialnumber,b.username as loid from tab_gw_device a ,tab_hgwcustomer b ");
		sb.append(" where a.device_id = b.device_id ");
		sb.append(" and a.device_id in('");
		sb.append(deviceId).append("')");

		PrepareSQL sql = new PrepareSQL(sb.toString());
		List<Map<String, String>> lt = jt.queryForList(sql.getSQL());
		if (null != lt && lt.size() > 0) {
			sqllist.add(psql.getSQL());
			for (Map<String, String> map : lt) {
				psql = new PrepareSQL(" insert into tab_wirelesst_task_dev(task_id,device_id,oui,device_serialnumber,loid,result_id,status) values(?,?,?,?,?,?,?)");
				psql.setLong(1, task_id);
				psql.setString(2, StringUtil.getStringValue(map.get("device_id")));
				psql.setString(3, StringUtil.getStringValue(map.get("oui")));
				psql.setString(4, StringUtil.getStringValue(map.get("device_serialnumber")));
				psql.setString(5, StringUtil.getStringValue(map.get("loid")));
				psql.setInt(6, 0);
				psql.setInt(7, 0);

				sqllist.add(psql.getSQL());
			}
			jt.batchUpdate((String[]) sqllist.toArray(new String[0]));
		}else{
			res = "-1";//未绑定用户
		}

		return res;

	}


	public String isHaveStrategy(String deviceId) {
		logger.debug("WirelessbusinessCtrDAO-->isHaveStrategy({})",
				new Object[] { deviceId });
		String strategy = "";
		PrepareSQL psql = new PrepareSQL(
				"select id from gw_serv_strategy_batch where device_id = '"
						+ deviceId + "' and service_id = 2003 and status = 0 ");// and
																				// status
																				// =
																				// 0
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0) {
			strategy = "已有关闭业务策略";
		}
		return strategy;
	}

	public String isBindUser(String deviceId) {
		logger.debug("WirelessbusinessCtrDAO-->isBindUser({})",
				new Object[] { deviceId });
		String flag = "0";
		PrepareSQL psql = new PrepareSQL(
				"select customer_id from tab_gw_device where device_id = '"
						+ deviceId + "' and customer_id is not null");
		List list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			flag = "1";
		}
		return flag;
	}

	public String queryDevice(String gwShare_queryField,
			String gwShare_queryParam) {
		logger.debug("WirelessbusinessCtrDAO-->isBind({},{})", new Object[] {
				gwShare_queryField, gwShare_queryParam });
		List list = null;
		Map<String, Object> map = null;
		String flag = "0";
		if ("deviceSn".equals(gwShare_queryField)) {
			String sub = gwShare_queryParam.substring(gwShare_queryParam
					.length() - 6);
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id,a.customer_id as queryid from tab_gw_device a"
							+ " where  a.device_serialnumber like '%"
							+ gwShare_queryParam + "' and a.dev_sub_sn = '"
							+ sub + "'");
			list = jt.queryForList(psql.getSQL());
		}
		if ("username".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id,a.username,a.device_id as queryid from tab_hgwcustomer a "
							+ "where a.username = '" + gwShare_queryParam + "'");
			list = jt.queryForList(psql.getSQL());
		}
		if ("kdname".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id,b.username,a.device_id as queryid from tab_hgwcustomer a,hgwcust_serv_info b "
							+ "where b.serv_type_id = 10 and b.username = '"
							+ gwShare_queryParam
							+ "' and a.user_id = b.user_id");
			list = jt.queryForList(psql.getSQL());
		}
		if ("voipPhoneNum".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id,b.voip_phone,a.device_id as queryid from tab_hgwcustomer a,tab_voip_serv_param b "
							+ "where b.voip_phone = '"
							+ gwShare_queryParam
							+ "' and a.user_id = b.user_id");
			list = jt.queryForList(psql.getSQL());
		}
		if (null != list && list.size() > 0) {
			if (list.size() > 1
					&& ("voipPhoneNum".equals(gwShare_queryField) || "kdname"
							.equals(gwShare_queryField))) {
				flag = "4";
			} else {
				map = (Map) list.get(0);
				if (null != map) {
					String queryId = StringUtil.getStringValue(map
							.get("queryid"));
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
			}
		} else {
			if ("deviceSn".equals(gwShare_queryField)) {
				flag = "0";
			} else if ("username".equals(gwShare_queryField)
					|| "kdname".equals(gwShare_queryField)
					|| "voipPhoneNum".equals(gwShare_queryField)) {
				flag = "5";
			}
		}
		return flag;
	}

	public List getDetailsForPage(String deviceIds, String flag,
			String awifi_type) throws SQLException {
		logger.debug("WirelessBusinessCtrlDAO.getDetailsForPage()--start");
		List list = null;
		String serviceId = "";
		if (flag.equals("1")) {
			serviceId = "2001";
		} else if (flag.equals("0")) {
			serviceId = "2003";
		}
		StringBuffer sql = new StringBuffer();
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			sql.append("select * from (select a.service_id,a.end_time,a.start_time,a.status,a.result_id,a.result_desc ");
			sql.append("from gw_serv_strategy_batch a,tab_wirelesst_task b ");
			sql.append("where a.ids_task_id = b.task_id and b.wireless_type = "
					+ awifi_type + " and a.device_id = '" + deviceIds
					+ "' and a.service_id = " + serviceId + " order by b.task_id desc ) where rownum< 2");
		}else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			sql.append("select top 1 a.service_id,a.end_time,a.start_time,a.status,a.result_id,a.result_desc ");
			sql.append("from gw_serv_strategy_batch a,tab_wirelesst_task b ");
			sql.append("where a.ids_task_id = b.task_id and b.wireless_type = "
					+ awifi_type + " and a.device_id = '" + deviceIds
					+ "' and a.service_id = " + serviceId + " order by b.task_id");
		}else if (DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append("select a.service_id,a.end_time,a.start_time,a.status,a.result_id,a.result_desc ");
			sql.append("from gw_serv_strategy_batch a,tab_wirelesst_task b ");
			sql.append("where a.ids_task_id = b.task_id and b.wireless_type = "
					+ awifi_type + " and a.device_id = '" + deviceIds
					+ "' and a.service_id = " + serviceId + " order by b.task_id limit 1");
		}else{
			sql.append("select * from (select a.service_id,a.end_time,a.start_time,a.status,a.result_id,a.result_desc ");
			sql.append("from gw_serv_strategy_batch a,tab_wirelesst_task b ");
			sql.append("where a.ids_task_id = b.task_id and b.wireless_type = "
					+ awifi_type + " and a.device_id = '" + deviceIds
					+ "' and a.service_id = " + serviceId + " order by b.task_id desc ) where rownum< 2");
		}
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		list = jt.queryForList(psql.getSQL());
		List<Map<String, String>> rlist = new ArrayList<Map<String, String>>();
		if (list != null && list.size() > 0) {
			Map map = (Map) list.get(0);
			Map<String, String> queryMap = new HashMap<String, String>();
			if (null != map && !map.isEmpty()) {
				// 业务名称
				String service_id = StringUtil
						.getStringValue(map, "service_id");
				if ("2001".equals(service_id)) {
					if ("1".equals(awifi_type)) {
						queryMap.put("service_id", "awifi无线业务开通");
					} else if ("2".equals(awifi_type)) {
						queryMap.put("service_id", "校园网无线业务开通");
					} else {
						queryMap.put("service_id", "无线专线业务开通");
					}
				} else {
					if ("1".equals(awifi_type)) {
						queryMap.put("service_id", "awifi无线业务关闭");
					} else {
						queryMap.put("service_id", "校园网无线业务关闭");
					}
				}// 策略状态
				String status = StringUtil.getStringValue(map, "status");
				String result_id = StringUtil.getStringValue(map, "result_id");
				if ("100".equals(status)) {
					queryMap.put("status", "执行完成");
					if ("1".equals(result_id)) {
						queryMap.put("result_id", "成功");
					} else {
						queryMap.put("result_id", "失败");
					}
				} else {
					queryMap.put("status", "等待执行");
					queryMap.put("result_id", "");
				}
				if (Global.G_Fault_Map.get(StringUtil.getIntValue(map,
						"result_id")) == null) {
					queryMap.put("result_desc",
							StringUtil.getStringValue(map, "result_desc"));
				} else {
					queryMap.put(
							"result_desc",
							Global.G_Fault_Map.get(
									StringUtil.getIntValue(map, "result_id"))
									.getFaultReason());
				}
				// queryMap.put("result_desc",StringUtil.getStringValue(map,
				// "result_desc"));
				// 将毫秒转换成时间
				try {
					// 定制时间
					long start_time = StringUtil
							.getLongValue(map, "start_time");
					// 执行时间
					long end_time = StringUtil.getLongValue(map, "end_time");
					queryMap.put("start_time", new DateTimeUtil(
							start_time * 1000).getLongDate());
					if (StringUtil.IsEmpty(StringUtil.getStringValue(map,
							"end_time"))) {
						queryMap.put("end_time", "");
					} else {
						queryMap.put("end_time", new DateTimeUtil(
								end_time * 1000).getLongDate());
					}
				} catch (Exception e) {
					queryMap.put("end_time", "");
					queryMap.put("start_time", "");
				}
				rlist.add(queryMap);
				return rlist;
			}
		}
		return rlist;
	}

	public String isAwifi(String deviceId) {
		String is_awifi = "0";

		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select b.is_awifi from tab_gw_device a,tab_devicetype_info b where a.devicetype_id = b.devicetype_id"
						+ " and a.device_id = '" + deviceId + "'");
		map = jt.queryForMap(psql.getSQL());

		if (null != map) {
			is_awifi = StringUtil.getStringValue(map.get("is_awifi"));
			if (is_awifi.equals("1")) {
				is_awifi = "1";
			} else {
				is_awifi = "0";
			}
		}
		return is_awifi;
	}


	/**
	 * 业务下发内容预先记录
	 * @param taskId 主键
	 * @param accOid 用户Id
	 * @param operType  //1:业务下发;2:批量无线业务开通（通用）;3:批量无线开通;4:批量无线关闭
	 * @param servType  //0:全业务;10:宽带业务;11:IPTV业务;14:语音业务;2001:无线开通;2003:无线关闭
	 * @param operStatus  //0:未处理;1:已处理;-1:处理失败
	 * @param gwType  //1:家庭网关;2:企业网关
	 * @param deviceId
	 * @param addTime
	 * @return
	 * @author jianglp(75508)
	 * @date 2016/7/25
	 * @return the number of rows affected
	 */
	public int addBatOptTask(long taskId, long accOid, int operType, int servType,
			int operStatus, int gwType, String deviceId, long addTime){
		logger.debug("WirelessBusinessCtrlDao.addBatOptTask()---start:"+taskId);
		String strSQL = "insert into batch_operation_task ("
				+"task_id,acc_oid,oper_type,serv_type,oper_status,gw_type,device_id," +
				"add_time) values (?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, taskId);
		psql.setLong(2,accOid);
		psql.setInt(3, operType);
		psql.setInt(4, servType);
		psql.setInt(5, operStatus);
		psql.setInt(6, gwType);
		psql.setString(7,deviceId);
		psql.setLong(8, addTime);
		int res=jt.update(psql.getSQL());
		logger.debug("addBatOptTask:"+res);
		return res;
	}

	/**
	 * 业务下发内容预先记录
	 * @param bodList Map里面需要非空的参数：
	 * @author jianglp(75508)
	 * @date 2016/7/25
	 * @return an array of the number of rows affected by each statement
	 */
	public int[] addBatOptDevByBatch(List<Map> bodList){
		logger.debug("WirelessBusinessCtrlDao.addBatOptDevByBatch1()---start:");
		String strSQL = "insert into batch_operation_dev("
				+"task_id,device_id,service_id,strategy_type,ssid,vlan_id,wireless_port," +
				"buss_level,channel,wireless_type,add_time,do_status" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] sqlArr=new String[bodList.size()];
		PrepareSQL psql = new PrepareSQL(strSQL);
		for(int i=0;i<bodList.size();i++){
			Map bod=bodList.get(i);
			psql.setLong(1, StringUtil.getLongValue(bod, "taskId"));
			psql.setString(2,StringUtil.getStringValue(bod, "deviceId"));
			psql.setInt(3, StringUtil.getIntValue(bod,"serviceId"));
			psql.setString(4, StringUtil.getStringValue(bod,"stategyType"));
			psql.setString(5, StringUtil.getStringValue(bod,"ssid"));
			psql.setInt(6, StringUtil.getIntValue(bod,"vlanId"));
			psql.setInt(7, StringUtil.getIntValue(bod,"wirelessPort"));
			psql.setInt(8, StringUtil.getIntValue(bod,"bussLevel"));
			psql.setString(9, StringUtil.getStringValue(bod,"channel"));
			psql.setInt(10, StringUtil.getIntValue(bod,"wirelessType"));
			psql.setLong(11, StringUtil.getLongValue(bod,"addTime"));
			psql.setInt(12, StringUtil.getIntValue(bod,"doStatus"));
			sqlArr[i]=psql.getSQL();
		}
		int res[]=jt.batchUpdate(sqlArr);
		return res;
	}


	/**
	 * 业务下发内容预先记录
	 * @param taskIdList 主键列表
	 * @param deviceIdList 设备号列表
	 * @param serviceId
	 * @param strategyType
	 * @param vlanId
	 * @param wirelessPort
	 * @param bussLevel
	 * @param channel
	 * @param wirelessType
	 * @param addTime
	 * @param doStatus
	 * @author jianglp(75508)
	 * @date 2016/7/25
	 * @return an array of the number of rows affected by each statement
	 */
	public int[] addBatOptDevByBatch(
		List<String>deviceIdList,
		long taskId,
		int serviceId,
		String strategyType,
		String ssid,
		String vlanId,
		int wirelessPort,
		int bussLevel,
		String channel,
		int wirelessType,
		long addTime,
		int doStatus
		){
		logger.debug("WirelessBusinessCtrlDao.addBatOptDevByBatch2()---start:");
		String strSQL = "insert into batch_operation_dev("
				+"task_id,device_id,service_id,strategy_type,ssid,vlan_id,wireless_port," +
				"buss_level,channel,wireless_type,add_time,do_status" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] sqlArr=new String[deviceIdList.size()];
		PrepareSQL psql = new PrepareSQL(strSQL);
		for(int i=0;i<deviceIdList.size();i++){
			psql.setLong(1, StringUtil.getLongValue(taskId));
			psql.setString(2,StringUtil.getStringValue(deviceIdList.get(i)));
			psql.setInt(3, StringUtil.getIntegerValue(serviceId));
			psql.setString(4, StringUtil.getStringValue(strategyType));
			psql.setString(5, StringUtil.getStringValue(ssid));
			psql.setInt(6, StringUtil.getIntegerValue(vlanId));
			psql.setInt(7,StringUtil.getIntegerValue(wirelessPort));
			psql.setInt(8, StringUtil.getIntegerValue(bussLevel));
			psql.setString(9,StringUtil.getStringValue(channel));
			psql.setInt(10, StringUtil.getIntegerValue(wirelessType));
			psql.setLong(11, StringUtil.getLongValue(addTime));
			psql.setInt(12,StringUtil.getIntegerValue(doStatus));
			sqlArr[i]=psql.getSQL();
		}
		int res[]=jt.batchUpdate(sqlArr);
		return res;
	}
}
