package com.linkage.module.gwms.cao.gw;

import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObjectResponse;
import com.linkage.litms.acs.soap.service.DeleteObjectResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.acs.soap.service.SetParameterValuesResponse;
import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.util.AddWanUtil;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class WanRelatedCAO implements I_CAO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(WanRelatedCAO.class);
	
	private String wanPath = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
	private String pppcPath = ".WANPPPConnection.";
	private String ipcPath = ".WANIPConnection.";
	private String mode = ".X_CT-COM_WANGponLinkConfig.Mode";
	private String vlanIdMark = ".X_CT-COM_WANGponLinkConfig.VLANIDMark";
	private String connectionType = ".ConnectionType";
	private String lanInterface = ".X_CT-COM_LanInterface";
	private String serviceList = ".X_CT-COM_ServiceList";
	private String enable = ".Enable";
	private String dHCPEnable = ".X_CT-COM_LanInterface-DHCPEnable";
	private String multicastVlan = ".X_CT-COM_MulticastVlan";
	
	/** ACS CORBA */
	private ACSCorba acsCorba;
	
	/** ACS CORBA */
	private PreProcessInterface ppCorba;
	
	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;
	
	/**
	 * add new strategy.
	 * 
	 * @param id
	 *            strategy id.
	 * @return
	 */
	public boolean addStrategyToPP(String id) {
		
		logger.debug("addStrategyToPP({})", id);
		
		return ppCorba.processOOBatch(id);
	}

	public int getDataFromSG(String deviceId, int type) {
		
		logger.debug("getDataFromSG({},{})", deviceId, type);
		
		sgCorba = new SuperGatherCorba(String.valueOf(LipossGlobals.getGw_Type(deviceId)));

		return sgCorba.getCpeParams(deviceId, type, 1);
	}

	
	public int delWanConn(WanConnObj wanConnObj, String gw_type) {
		logger.debug("删除WAN连接：{}", wanConnObj.getDevice_id());

		StringBuilder name = new StringBuilder();
		name.append("InternetGatewayDevice");
		name.append("." + "WANDevice");
		name.append("." + wanConnObj.getWan_id());
		name.append("." + "WANConnectionDevice");
		name.append("." + wanConnObj.getWan_conn_id());
		name.append(".");
		String wanConnPath = name.toString();
		logger.debug("删除设备：{}的路径：{}", wanConnObj.getDevice_id(), wanConnPath);
		
		acsCorba = new ACSCorba(gw_type);
		
		return acsCorba.del(wanConnObj.getDevice_id(), wanConnPath);
	}
	
	public int addWanConn(long accOId, String vlanId, String deviceId, String bindPort) {

		acsCorba = new ACSCorba("1");
		AddWanUtil addWan = new AddWanUtil(deviceId, acsCorba);
		String num = addWan.work();
		logger.warn("accOId[{}]管理员新增[{}]设备iptvWan连接，vlanId[{}]新增节点值=[{}]=", new Object[]{accOId, deviceId, vlanId, num});
		String pathTmp = wanPath + num + pppcPath + "1";
		String pathTmpShort = wanPath + num;
		
		RPCObject[] rpcObj = new RPCObject[8];
		rpcObj[0] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmpShort + mode}, new String[]{"2"}, new String[] { "1" });
		rpcObj[1] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmpShort + vlanIdMark}, new String[]{vlanId}, new String[] { "1" });
		rpcObj[2] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + connectionType}, new String[]{"PPPoE_Bridged"}, new String[] { "1" });
		rpcObj[3] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + lanInterface}, new String[]{bindPort}, new String[] { "1" });
		rpcObj[4] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + serviceList}, new String[]{"OTHER"}, new String[] { "1" });
		rpcObj[5] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + enable}, new String[]{"1"}, new String[] { "1" });
		rpcObj[6] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + dHCPEnable}, new String[]{"0"}, new String[] { "1" });
		rpcObj[7] = (SetParameterValues)acsCorba.getSetParameterValues(
				new String[] {pathTmp + multicastVlan}, new String[]{"50"}, new String[] { "1" });
		
		DevRpc[] devRPCArr = acsCorba.getDevRPCArr(deviceId,rpcObj);
		int res = callACS(devRPCArr, 1, accOId, deviceId);
		logger.warn("accOId[{}]管理员新增[{}]设备iptvWan连接，vlanId[{}]新增节点值[{}]，设值结果[{}]", new Object[]{accOId, deviceId, vlanId, num, res});
		
		return res;
	}
	
	/**
	 * @return the sgCorba
	 */
	public SuperGatherCorba getSgCorba() {
		return sgCorba;
	}

	/**
	 * @param sgCorba the sgCorba to set
	 */
	public void setSgCorba(SuperGatherCorba sgCorba) {
		this.sgCorba = sgCorba;
	}

	/**
	 * @return the ppCorba
	 */
	public PreProcessInterface getPpCorba() {
		return ppCorba;
	}

	/**
	 * @param ppCorba the ppCorba to set
	 */
	public void setPpCorba(PreProcessInterface ppCorba) {
		this.ppCorba = ppCorba;
	}

	
	public void setAcsCorba(ACSCorba acsCorba) {
		logger.debug("setAcsCorba({})", acsCorba);

		this.acsCorba = acsCorba;
	}
	private int callACS(DevRpc[] devRPCArr, int rpcType, long accOId, String deviceId)
	{
		int faultCodeTemp = -99;
		List<DevRpcCmdOBJ> list = null;
		try
		{
			logger.warn("[" + deviceId + "] call ACS Corba begin, rpcType[{}]", rpcType);
			list = acsCorba.execRPC(LipossGlobals.getClientId(), rpcType, 1, devRPCArr);
			logger.warn("[{}] [" + deviceId + "] call ACS Corba end, reponse list size:[{}]", list == null ? null : list.size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (list == null)
		{
			logger.warn("accOId[{}][" + deviceId + "] call acs corba returns null. setCode(-9)", accOId);
			return faultCodeTemp;
		}
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> cmdList = list.get(0).getRpcList();
		com.ailk.tr069.devrpc.obj.mq.Rpc obj = null;
		logger.debug("调用ACS返回的 结果状态： {}", list.get(0).getStat());
		logger.debug("调用ACS返回的 cmdList.size() = {}", cmdList.size());
		faultCodeTemp = list.get(0).getStat();
		logger.warn("accOId[{}] [{}] [" + deviceId + "] call acs corba return status:[{}]", accOId, faultCodeTemp);
		for (int i = 0; i < cmdList.size(); i++)
		{
			obj = cmdList.get(i);
			if (obj == null || obj.getValue() == null)
			{
				continue;
			}
			logger.warn("[{}]调用ACS返回的结果 = {}",deviceId, obj.getValue());
			String resp = obj.getValue();
			if (resp == null || "".equals(resp))
			{
			}
			else
			{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapOBJ != null)
				{
					Element element = soapOBJ.getRpcElement();
					if ("SetParameterValuesResponse".equals(obj.getRpcName()))
					{
						SetParameterValuesResponse setParameterValuesResponse = XmlToRpc
								.SetParameterValuesResponse(element);
						if ("1".equals(setParameterValuesResponse.getStatus())
								|| 1 == setParameterValuesResponse.getStatus())
						{
							logger.warn("[{}] setParameterValues需要重启", deviceId);
//							Global.deviceNeedRebootMap.put(deviceId, true);
						}
					}
					if ("AddObjectResponse".equals(obj.getRpcName()))
					{
						AddObjectResponse addObjectResponse = XmlToRpc
								.AddObjectResponse(element);
						if ("1".equals(addObjectResponse.getStatus())
								|| 1 == addObjectResponse.getStatus())
						{
							logger.warn("[{}] addObject 需要重启", deviceId);
//							Global.deviceNeedRebootMap.put(deviceId, true);
						}
					}
					if ("DeleteObjectResponse".equals(obj.getRpcName()))
					{
						DeleteObjectResponse deleteObjectResponse = XmlToRpc
								.DeleteObjectResponse(element);
						if ("1".equals(deleteObjectResponse.getStatus())
								|| 1 == deleteObjectResponse.getStatus())
						{
							logger.warn("[{}] DeleteObject 需要重启", deviceId);
//							Global.deviceNeedRebootMap.put(deviceId, true);
						}
					}
				}
			}
		}
		
		return faultCodeTemp;
	}
	
}
