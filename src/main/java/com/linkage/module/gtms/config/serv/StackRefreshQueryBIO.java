package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gtms.config.dao.StackRefreshQueryDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

import ACS.DevRpc;
import ACS.Rpc;


public class StackRefreshQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(StackRefreshQueryBIO.class);
	
	private  StackRefreshQueryDAO dao;
	public static String SERV_LIST_INTERNET = "INTERNET";
	public static String SERV_LIST_TR069 = "TR069";
	public static String SERV_LIST_VOIP = "VOIP";
	public static String SERV_LIST_OTHER = "OTHER";
	public static int GATHER_WAN = 2;
	private ACSCorba acsCorba = new ACSCorba();
	
	
	public Map<String,String>  queryStackRefreshDetail(String gw_type,String deviceId){
		Map<String, String> map=new LinkedHashMap<String, String>();		
		int int_flag = getConnectionFlag(deviceId);
		if(int_flag != 1){
			map.put("code", "0");
			map.put("msg", "设备不在线");
 			return map;
		}
		acsCorba = new ACSCorba(gw_type);
		
		Map<String,String> wanConnDeviceMap = this.getPingInterface(deviceId,gw_type);
		String warnWay="";
		if(wanConnDeviceMap == null || wanConnDeviceMap.isEmpty())
		{
			logger.warn("deviceId:"+deviceId +"没有获取到WAN通道信息，结束线程");
			map.put("code", "0");
			map.put("msg", "没有获取到WAN通道信息");
			return map;
		}
		else//成功解析结果
		{
			for(String key: wanConnDeviceMap.keySet())
			{
				if(key.startsWith("INTERNET"))
				{
					if(LipossGlobals.inArea(Global.NXDX)){
						if (key.split("###")[1].equals("81")) {
							warnWay=wanConnDeviceMap.get(key);
						}
					}else {
						warnWay=wanConnDeviceMap.get(key);//默认取的是第一个
					}
				}
			}
		}
		logger.warn("warnWay:"+warnWay);
		if (StringUtil.IsEmpty(warnWay)) {
			map.put("code", "0");
			map.put("msg", "没有获取到WAN通道信息");
			return map;
		}
		
		String[] nodes = warnWay.split(",");
		String[] gatherPath = new String[nodes.length*2];
		for (int i = 0; i < nodes.length; i++) {
			gatherPath[i*2]=nodes[i]+"X_CT-COM_IPMode";
			gatherPath[i*2+1]=nodes[i]+"X_CT-COM_IPv6PrefixDelegationEnabled";
		}
		
		ArrayList<ParameValueOBJ> objLlist = acsCorba.getValue(deviceId, gatherPath);
		if (null == objLlist || objLlist.size() == 0) {
			map.put("code", "0");
			map.put("msg", "没有采集到设备节点");
			return map;
		}
		for (int i = 0; i < objLlist.size(); i++) {
			String value = objLlist.get(i).getValue();
			String name = objLlist.get(i).getName();
			if (StringUtil.IsEmpty(name) && StringUtil.IsEmpty(value)) {
				continue;
			}
			map.put(name, value);
		}
		if (map == null || map.isEmpty()) {
			map.put("code", "0");
			map.put("msg", "没有采集到设备节点");
			return map;
		}
		map.put("code", "1");
		logger.warn(map.toString());
		return map;
	}
	
	
	public Map<String,String> getPingInterface(String device_id, String gw_type)
	{
		logger.debug("getPingInterface({},{})",new Object[]{device_id,gw_type});
		
		Map<String,String> restMap = new HashMap<String, String>();
		
		String value = "";
		
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type);
		// 获取Wan
		// 1、调用采集,采集InternetGatewayDevice.WANDevice下节点
		int irt = sgCorba.getCpeParams(device_id, GATHER_WAN, 1);
		logger.warn("[{}]调用采集获取Wan的结果：" + irt,device_id);
		String errorMsg = "";
		if (irt != 1)
		{
			errorMsg = "调用采集失败";
			logger.warn("[{}]"+errorMsg,device_id);
		}
		else
		{
			// 2、从数据库获取wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = dao.getWanConnIds(device_id);
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				errorMsg = "没有获取到Wan接口";
				logger.warn("[{}]"+errorMsg,device_id);
			}
			else
			{

				for (Map map : wanConnIds)
				{
					String wan_conn_id = StringUtil.getStringValue(map.get("wan_conn_id"));
					String conn_type = StringUtil.getStringValue(map.get("conn_type"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map.get("serv_list"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					if (sessType.equals("1"))
					{
						value = wanConnDevice + wan_conn_id + ".WANPPPConnection."
								+ wan_conn_sess_id + ".";
					}
					else if (sessType.equals("2"))
					{
						value = wanConnDevice + wan_conn_id + ".WANIPConnection."
								+ wan_conn_sess_id + ".";
					}
					else
					{
						logger.warn("[{}]sessType值不对：" + sessType,device_id);
						continue;
					}
					if(SERV_LIST_INTERNET.equals(serv_list))
					{
						String oldValue=restMap.get(SERV_LIST_INTERNET+"###"+vlanid+"###"+conn_type)==null?"":restMap.get(SERV_LIST_INTERNET+"###"+vlanid+"###"+conn_type);
						if (oldValue.equals("")) {
							restMap.put(SERV_LIST_INTERNET+"###"+vlanid+"###"+conn_type, value);
						}else {
							restMap.put(SERV_LIST_INTERNET+"###"+vlanid+"###"+conn_type, value+","+oldValue);
						}
					}
					else if (SERV_LIST_VOIP.equals(serv_list))
					{
						String oldValue=restMap.get(SERV_LIST_VOIP+"###"+vlanid+"###"+conn_type)==null?"":restMap.get(SERV_LIST_VOIP+"###"+vlanid+"###"+conn_type);
						if (oldValue.equals("")) {
							restMap.put(SERV_LIST_VOIP+"###"+vlanid+"###"+conn_type, value);
						}else {
							restMap.put(SERV_LIST_VOIP+"###"+vlanid+"###"+conn_type, value+","+oldValue);
						}
					}
					else if (SERV_LIST_TR069.equals(serv_list))
					{
						String oldValue=restMap.get(SERV_LIST_TR069+"###"+vlanid+"###"+conn_type)==null?"":restMap.get(SERV_LIST_TR069+"###"+vlanid+"###"+conn_type);
						if (oldValue.equals("")) {
							restMap.put(SERV_LIST_TR069+"###"+vlanid+"###"+conn_type, value);
						}else {
							restMap.put(SERV_LIST_TR069+"###"+vlanid+"###"+conn_type, value+","+oldValue);
						}					}
					else if (SERV_LIST_OTHER.equals(serv_list))
					{
						String oldValue=restMap.get(SERV_LIST_OTHER+"###"+vlanid+"###"+conn_type)==null?"":restMap.get(SERV_LIST_OTHER+"###"+vlanid+"###"+conn_type);
						if (oldValue.equals("")) {
							restMap.put(SERV_LIST_OTHER+"###"+vlanid+"###"+conn_type, value);
						}else {
							restMap.put(SERV_LIST_OTHER+"###"+vlanid+"###"+conn_type, value+","+oldValue);
						}					}
					else
					{
						logger.warn("[{}]serv_list值不对：" + serv_list,device_id);
						continue;
					}

				}
			
			}
		}
		logger.warn("[{}]restMap: " + restMap.toString(),device_id);
		return restMap;
	}
	
	public int getConnectionFlag(String device_id){
		logger.debug("getConnectionFlag({})", device_id);
		
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
	
	public StackRefreshQueryDAO getDao()
	{
		return dao;
	}
	
	public void setDao(StackRefreshQueryDAO dao)
	{
		this.dao = dao;
	}
}
