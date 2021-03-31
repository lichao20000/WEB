package com.linkage.module.gtms.config.dao;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class SetQOEDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(SetQOEDAO.class);
	/**
	 * ACS Corba
	 */
	private ACSCorba acsCorba = new ACSCorba();
	public Map queryQOE(String deviceIds){
		Map returnMap = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append("select a.device_id ,a.oui,a.device_serialnumber,b.username as loid from tab_gw_device a ,tab_hgwcustomer b ");
		sb.append(" where a.device_id = b.device_id ");
		sb.append(" and a.device_id in('");
		sb.append(deviceIds).append(")");
		PrepareSQL sql = new PrepareSQL(sb.toString());
		Map map = jt.queryForMap(sql.getSQL());
		return map;
	}

	public Map doQueryQOE(String deviceStr){
		int int_flag  = testConnection(deviceStr);
		Map map = new HashMap();
		if(int_flag == 1){
			StringBuffer sb = new StringBuffer();
			sb.append("select a.device_serialnumber sn,b.username loid from tab_gw_device a ,tab_hgwcustomer b ");
			sb.append(" where a.device_id = b.device_id ");
			sb.append(" and a.device_id = '");
			sb.append(deviceStr);
			PrepareSQL sql = new PrepareSQL(sb.toString());
			List<Map> lt = jt.queryForList(sql.getSQL());
			if(null!=lt.get(0)&&lt.get(0).size()>0){
				map = lt.get(0);
				String TestDownloadStatus = "";
				String MonitorStatus = "";
				acsCorba = new ACSCorba("1");
				String node = "InternetGatewayDevice.DeviceInfo.X_CT-COM_Qoe.TestDownloadStatus";
				String node_a = "InternetGatewayDevice.DeviceInfo.X_CT-COM_Qoe.MonitorStatus";
				String[] arr= new String[2];
				arr[0] = node;
				arr[1] = node_a;
				deviceStr=deviceStr.substring(0, deviceStr.lastIndexOf("'"));
				ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceStr, arr);
				if(null!=result){
					MonitorStatus = result.get(0).getValue();
					TestDownloadStatus = result.get(1).getValue();
					map.put("MonitorStatus", MonitorStatus);
					map.put("TestDownloadStatus", TestDownloadStatus);
					map.put("deviceId", deviceStr);
					return map;
				}else{
					map.clear();
					map.put("res", "-2");//***********
					return map;
				}
			}else{
				map.put("res", "1");
				return map;
			}
		}else {
			map.put("res", String.valueOf(int_flag));
			return map;
		}
	}

	public Map doConfigQOE(long task_id,long acc_oid, long add_time, String service_id,
			String strategy_type, String set_enable, String set_url,String deviceStr,String do_type) {
		String res = "";
		ArrayList<String> sqllist = new ArrayList<String>();
		Map returnMap = new HashMap();
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_qoe_task(task_id,acc_oid,add_time,service_id,strategy_type,set_enable,set_url) values(?,?,?,?,?,?,?)");
		psql.setLong(1, task_id);
		psql.setLong(2, acc_oid);
		psql.setLong(3, add_time);
		psql.setInt(4, Integer.parseInt(service_id));
		psql.setInt(5, Integer.parseInt(strategy_type));
		psql.setString(6, set_enable);
		psql.setString(7, set_url);
		StringBuffer sb = new StringBuffer();
		sb.append("select a.device_id ,a.oui,a.device_serialnumber,b.username as loid from tab_gw_device a ,tab_hgwcustomer b ");
		sb.append(" where a.device_id = b.device_id ");
		sb.append(" and a.device_id in('");
		sb.append(deviceStr).append(")");
		PrepareSQL sql = new PrepareSQL(sb.toString());
		List<Map<String, String>> lt = jt.queryForList(sql.getSQL());
		String device_serialnumber = "";
		String loid = "";

		String deviceId = "";
		if (null != lt && lt.size() > 0) {
			sqllist.add(psql.getSQL());
			for (Map<String, String> map : lt) {
				psql = new PrepareSQL(" insert into tab_qoe_task_dev(task_id,device_id,service_id,oui,device_serialnumber,loid) values(?,?,?,?,?,?)");
				psql.setLong(1, task_id);
				psql.setString(2, StringUtil.getStringValue(map.get("device_id")));
				psql.setInt(3, Integer.parseInt(service_id));
				psql.setString(4, StringUtil.getStringValue(map.get("oui")));
				device_serialnumber = StringUtil.getStringValue(map.get("device_serialnumber"));
				deviceId = StringUtil.getStringValue(map.get("device_id"));
				psql.setString(5, device_serialnumber);
				loid = StringUtil.getStringValue(map.get("loid"));
				psql.setString(6, loid);
				sqllist.add(psql.getSQL());
			}
			int[] doflags = jt.batchUpdate((String[]) sqllist.toArray(new String[0]));
			if(doflags.length==sqllist.size()){
				if("'".equals(String.valueOf(deviceStr.charAt(deviceStr.length()-1)))){
					deviceStr=deviceStr.substring(0, deviceStr.lastIndexOf("'"));
				}
				String[] array = deviceStr.split("','");
				if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
						array, service_id, 
						new String[] { StringUtil.getStringValue(Long.valueOf(task_id)) })) {
					logger.warn("调用后台预读模块成功[{}]", deviceStr);
					res = "1";
					if("single_on".equals(do_type)){
						returnMap = refreshMsg(deviceId,returnMap);
						returnMap.put("deviceId", deviceId);
						returnMap.put("sn", device_serialnumber);
						returnMap.put("loid", loid);
					}else{
						returnMap.put("res", "success");
					}
				} else {
					logger.warn("调用后台预读模块失败[{}]", deviceStr);
					returnMap.put("res", "-5");
				}
			}
		}else{
			//设备未绑定用户
			returnMap.put("res", "1");
		}
		return returnMap;
	}

	public Map refreshMsg(String deviceId,Map returnMap){
		int int_flag  = testConnection(deviceId);
		if(int_flag == 1){
			acsCorba = new ACSCorba("1");
			String node = "InternetGatewayDevice.DeviceInfo.X_CT-COM_Qoe.TestDownloadStatus";
			String node_a = "InternetGatewayDevice.DeviceInfo.X_CT-COM_Qoe.MonitorStatus";
			String[] arr= new String[2];
			arr[0] = node;
			arr[1] = node_a;
			String TestDownloadStatus = "";
			String MonitorStatus = "";
			ArrayList<ParameValueOBJ> result = acsCorba.getValue(deviceId, arr);
			if(null!=result){
				MonitorStatus = result.get(0).getValue();
				TestDownloadStatus = result.get(1).getValue();
				returnMap.put("MonitorStatus", MonitorStatus);
				returnMap.put("TestDownloadStatus", TestDownloadStatus);
			}else{
				//设备在线但调用corba返回为空
				returnMap.put("res", "-2");//***********
			}
		}else {
			returnMap.put("TestDownloadStatus", String.valueOf(int_flag));
			returnMap.put("MonitorStatus", "");
		}
		return returnMap;
	}

	public String queryDevice(String gwShare_queryField,
			String gwShare_queryParam) {
		List list = null;
		Map<String, Object> map = null;
		String flag = "0";
		if ("deviceSn".equals(gwShare_queryField)) {
			String sub = gwShare_queryParam.substring(gwShare_queryParam
					.length() - 6);
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id  as queryid from tab_gw_device a"
							+ " where  a.device_serialnumber like '%"
							+ gwShare_queryParam + "' and a.dev_sub_sn = '"
							+ sub + "'");
			list = jt.queryForList(psql.getSQL());
		}
		if ("username".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id as queryid from tab_hgwcustomer a "
							+ "where a.username = '" + gwShare_queryParam + "'");
			list = jt.queryForList(psql.getSQL());
		}
		if ("kdname".equals(gwShare_queryField)) {
			PrepareSQL psql = new PrepareSQL(
					"select a.device_id as queryid from tab_hgwcustomer a,hgwcust_serv_info b "
							+ "where b.serv_type_id = 10 and b.username = '"
							+ gwShare_queryParam
							+ "' and a.user_id = b.user_id");
			list = jt.queryForList(psql.getSQL());
		}
		if (null != list && list.size() > 0) {
			if (list.size() > 1 && ("kdname".equals(gwShare_queryField)||"username".equals(gwShare_queryField))) {
				flag = "4";
			} else {
				map = (Map) list.get(0);
				if (null != map) {
					String queryId = StringUtil.getStringValue(map
							.get("queryid"));
					if (queryId.length() > 0) {
						String deviceId = StringUtil.getStringValue(map
								.get("queryid"));
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
					|| "kdname".equals(gwShare_queryField)) {
				flag = "3";
			}
		}
		return flag;
	}

	public String isQoe(String deviceId) {
		String is_qoe = "0";
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select b.is_qoe from tab_gw_device a,tab_devicetype_info b where a.devicetype_id = b.devicetype_id"
						+ " and a.device_id = '" + deviceId + "'");
		map = jt.queryForMap(psql.getSQL());

		if (null != map) {
			is_qoe = StringUtil.getStringValue(map.get("is_qoe"));
			if (is_qoe.equals("1")) {
				is_qoe = "1";
			} else {
				is_qoe = "0";
			}
		}
		return is_qoe;
	}

	public String isBindUser(String deviceId) {
		logger.debug("SetQOEDAO-->isBindUser({})",
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
	public int testConnection(String device_id)
	{
		if(device_id.contains("'")){
			device_id=device_id.substring(0, device_id.lastIndexOf("'"));
		}
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
}
