package com.linkage.module.gwms.util;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObject;
import com.linkage.litms.acs.soap.service.AddObjectResponse;
import com.linkage.litms.acs.soap.service.Reboot;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class AddWanUtil {
	static Logger logger = LoggerFactory.getLogger(AddWanUtil.class);
	private String deviceId = null;
	private int faultCode = 0;
	private ArrayList<Rpc> rpcList = null;
	private boolean IS_NEED_REBOOT = false;
	private String num = null;
	private ACSCorba acsCorba = new ACSCorba();

	public AddWanUtil(String _deviceId, ACSCorba acsCorba) {
		super();
		this.deviceId = _deviceId;
		this.acsCorba = acsCorba;
	}

	public String work() {
		businessConfig();
		return num;
	}

	private void businessConfig() {
		configWan();
		callACS();
	}

	private void configWan() {
		rpcList = new ArrayList<Rpc>();
		Rpc rpc = null;
		rpc = new Rpc();
		rpc.rpcName = "AddObject";
		rpc.rpcValue = getAddObjectWAN("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.").toRPC();
		rpcList.add(rpc);
		
		rpc = new Rpc();
		rpc.rpcName = "AddObject";
		rpc.rpcValue = getAddObjectWAN("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.{j}.WANPPPConnection.").toRPC();
		rpcList.add(rpc);
		
	}
	
	
	private AddObject getAddObjectWAN(String paramName)
	{
		AddObject addObject = new AddObject();
		paramName = paramName.replace("{i}", "1");
		paramName = paramName.replace("{j}", "%1-InstanceNumber%");
		addObject.setObjectName(paramName);
		addObject.setParameterKey("ITMS+");
		return addObject;
	}
	
	private void callACS() {
		if (rpcList == null || rpcList.size() <= 0) {
			logger.warn("[{}]没有要下发的配置，不需要配置", deviceId);
			faultCode = 1;
			IS_NEED_REBOOT = false;
		} else {
			Rpc[] rpcArr = new Rpc[rpcList.size()];
			for (int i = 0; i < rpcList.size(); i++) {
				rpcList.get(i).rpcId = StringUtil.getStringValue(i + 1);
				rpcArr[i] = rpcList.get(i);
				logger.warn("[{}]corbar param , rpcList.get(i).rpcId : " + rpcList.get(i).rpcId, deviceId);
				logger.warn("[{}]corbar param , rpcList.get(i).rpcName : " + rpcList.get(i).rpcName, deviceId);
				logger.warn("[{}]corbar param , rpcList.get(i).rpcValue : " + rpcList.get(i).rpcValue, deviceId);
			}
			DevRpc devRPC = new DevRpc();
			devRPC.devId = deviceId;
			devRPC.rpcArr = rpcArr;
			DevRpc[] devRPCArr = new ACS.DevRpc[] { devRPC };
			List<DevRpcCmdOBJ> list = null;
			int rpcType = 1;
			try {
				logger.warn("[" + deviceId + "] call ACS Corba begin, rpcType[{}]", rpcType);
				list = acsCorba.execRPC(LipossGlobals.getClientId(), rpcType, 1, devRPCArr);
				logger.warn("[" + deviceId + "] call ACS Corba end, reponse list size:[{}]", list == null ? null : list.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (list == null) {
				logger.warn("[" + deviceId + "] call acs corba returns null. setCode(-9)");
				faultCode = -9;
				return;
			}
			List<com.ailk.tr069.devrpc.obj.mq.Rpc> cmdList = list.get(0).getRpcList();
			com.ailk.tr069.devrpc.obj.mq.Rpc obj = null;
			String desc = "";
			int faultCodeTemp = list.get(0).getStat();
			logger.warn("[" + deviceId + "] call acs corba return status:[{}]", faultCodeTemp);
			for (int i = 0; i < cmdList.size(); i++) {
				obj = cmdList.get(i);
				if (obj == null || obj.getValue() == null) {
					continue;
				}
				logger.warn("调用ACS返回的结果 = {}", obj.getValue());
				desc += obj.getValue();// 执行结果xml报文
				String resp = obj.getValue();
				if (resp == null || "".equals(resp)) {
				} else {
					SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
					if (soapOBJ != null) {
						Element element = soapOBJ.getRpcElement();
						
						if(null==num || num.trim().length()==0){
							num = element.elementText("InstanceNumber");
						}
						
						if ("AddObjectResponse".equals(obj.getRpcName())) {
							AddObjectResponse addObjectResponse = XmlToRpc
									.AddObjectResponse(element);
							if ("1".equals(addObjectResponse.getStatus())
									|| 1 == addObjectResponse.getStatus()) {
								logger.warn("[{}] addObject 需要重启", deviceId);
								IS_NEED_REBOOT = true;
							}
						}
					}
				}
			}
			// 更新数据库
			faultCode = faultCodeTemp;

			logger.warn("faultCode:[{}]", faultCode);
			logger.warn("IS_NEED_REBOOT:[{}]", IS_NEED_REBOOT);

			if (faultCode == 1) {// success
				if (IS_NEED_REBOOT == true) {
					logger.warn("[" + deviceId + "]设备需要重启，暂不重启！");
//					logger.warn("[" + deviceId + "]重启设备！");
//					Reboot reboot = new Reboot();
//					reboot.setCommandKey("Reboot");
//					DevRpc[] devRpcArr = new DevRpc[1];
//					devRpcArr[0] = new DevRpc();
//					devRpcArr[0].devId = deviceId;
//					Rpc[] rpcRebootArr = new Rpc[1];
//					rpcRebootArr[0] = new Rpc();
//					rpcRebootArr[0].rpcId = "1";
//					rpcRebootArr[0].rpcName = "Reboot";
//					rpcRebootArr[0].rpcValue = reboot.toRPC();
//					devRpcArr[0].rpcArr = rpcRebootArr;
//					AcsCorba acsCorba = new AcsCorba();
//					List<DevRpcCmdOBJ> respArr = acsCorba.execRPC(
//							Global.CLIENT_ID, 1, 1, devRpcArr);
//					logger.warn("respArr:[{}]", respArr);
				}
			}
		}
	}

}
