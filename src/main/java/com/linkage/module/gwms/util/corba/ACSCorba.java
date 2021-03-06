/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.gwms.util.corba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObject;
import com.linkage.litms.acs.soap.service.AddObjectResponse;
import com.linkage.litms.acs.soap.service.DeleteObject;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.AddOBJ;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;

/**
 * CORBA operation for ACS.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 21, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class ACSCorba
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ACSCorba.class);
	
	private String gw_type;
	
	private int stat = -9;  // add by zhangchy 20130124 ???ACS??????ACS??????????????????
	
	public ACSCorba(){
		
	}
	
	public ACSCorba(String gw_type){
		this.gw_type = gw_type;
	}
	

	/**
	 * GetParameterValues
	 * 
	 * @param deviceId
	 * @param arr
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getValue(String deviceId, String name)
	{
		logger.debug("getValue({},{}): ", deviceId, name);
		ArrayList<ParameValueOBJ> objList = null;
		if (deviceId == null || name == null)
		{
			logger.debug("deviceId == null");
			return objList;
		}
		return getValue(deviceId, new String[] { name });
	}

	/**
	 * GetParameterValues
	 * 
	 * @param deviceId
	 * @param arr
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getValue(String deviceId, String[] arr)
	{
		logger.debug("getValue({},{}): ", deviceId, arr);
		ArrayList<ParameValueOBJ> objList = null;
		if (deviceId == null || arr == null || arr.length == 0)
		{
			logger.debug("deviceId == null");
			return objList;
		}
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(arr);

		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "GetParameterValues";
		rpcArr[0].rpcValue = getParameterValues.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> respArr = execRPC(deviceId,devRpcArr);
		if (respArr == null || respArr.get(0) == null)
		{
			logger.debug("-9");
			return objList;
		}
		String resp = getRespStr(respArr, "GetParameterValuesResponse");
		
		Fault fault = null;
		GetParameterValuesResponse getParameterValuesResponse = null;
		try
		{
			SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
			if (soapOBJ == null)
			{
				return objList;
			}
			fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
		}
		catch (Exception e)
		{
			logger.debug("{}", e.getMessage());
		}
		if (fault != null)
		{
			logger.warn("setValue({})={}", deviceId, fault.getDetail().getFaultString());
			return objList;
		}
		try
		{
			SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
			if (soapOBJ == null)
			{
				return objList;
			}
			getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ
					.getRpcElement());
		}
		catch (Exception e)
		{
			logger.debug("{}", e.getMessage());
		}
		if (getParameterValuesResponse != null)
		{
			objList = new ArrayList<ParameValueOBJ>();
			ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
					.getParameterList();
			if(parameterValueStructArr==null){
				return null;
			}
			ParameValueOBJ obj = null;
			for (int i = 0; i < parameterValueStructArr.length; i++)
			{
				obj = new ParameValueOBJ();
				obj.setName(parameterValueStructArr[i].getName());
				obj.setValue(parameterValueStructArr[i].getValue().para_value);
				obj.setType(parameterValueStructArr[i].getValue().para_type_id);
				objList.add(obj);
			}
		}
		return objList;
	}

	/**
	 * SetParameterValues
	 * 
	 * @param deviceId
	 * @param obj
	 * @return
	 *            <li>0,1:??????</li>
	 *            <li>-7:??????????????????</li>
	 *            <li>-6:??????????????????</li>
	 *            <li> -1:??????????????????</li>
	 *            <li>-9:??????????????????</li>
	 *            <li>??????:TR069??????</li>
	 */
	public int setValue(String deviceId, ParameValueOBJ obj)
	{
		logger.debug("setValue({},{}): ", deviceId, obj);
		int flag = -1;
		if (deviceId == null || obj == null)
		{
			logger.debug("deviceId == null");
			return flag;
		}
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(obj);
		return setValue(deviceId, objList);
	}

	/**
	 * SetParameterValues
	 * 
	 * @param deviceId
	 * @param objList
	 * @return
	 *            <li>0,1:??????</li>
	 *            <li>-7:??????????????????</li>
	 *            <li>-6:??????????????????</li>
	 *            <li> -1:??????????????????</li>
	 *            <li>-9:??????????????????</li>
	 *            <li>??????:TR069??????</li>
	 */
	public int setValue(String deviceId, ArrayList<ParameValueOBJ> objList)
	{
		logger.debug("setValue({},{}): ", deviceId, objList);
		int flag = -9;
		if (deviceId == null || objList == null || objList.size() == 0)
		{
			logger.debug("deviceId == null");
			return flag;
		}
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[objList
				.size()];
		AnyObject anyObject = null;
		for (int i = 0; i < objList.size(); i++)
		{
			parameterValueStruct[i] = new ParameterValueStruct();
			parameterValueStruct[i].setName(objList.get(i).getName());
			anyObject = new AnyObject();
			anyObject.para_type_id = objList.get(i).getType();
			anyObject.para_value = objList.get(i).getValue();
			parameterValueStruct[i].setValue(anyObject);
		}
		setParameterValues.setParameterList(parameterValueStruct);
		setParameterValues.setParameterKey("GWMS");
		
		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> respArr = execRPC(deviceId,devRpcArr);
		if (respArr == null || respArr.get(0) == null)
		{
			logger.debug("flag = -9");
			flag = -9;
			return flag;
		}
		flag = respArr.get(0).getStat();
		logger.debug("flag = " + flag);
		return flag;
	}
	
	public String getRespStr(List<DevRpcCmdOBJ> devRPCRep,String cmdName){
		logger.debug("getRespStr()");
		String rpcRes = null;
		if (null != devRPCRep) {
			if (null != devRPCRep.get(0)) {
				ArrayList<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0).getRpcList();
				
				stat = devRPCRep.get(0).getStat();  // add by zhangchy 20130124 ???ACS??????ACS??????????????????
				
				if (null != rpcList && !rpcList.isEmpty()) {
					for(com.ailk.tr069.devrpc.obj.mq.Rpc rpc : rpcList){
						if(rpc.getRpcName() != null && rpc.getRpcName().equals(cmdName)){
							rpcRes = rpc.getValue();
							break;
						}
					}
				} else {
					logger.warn("ACS Reponse devRPCRep.get(0).getRpcList() is NULL...");
				}
			} else {
				logger.warn("ACS Reponse devRPCRep.get(0) is NULL...");
			}
		} else {
			logger.warn("ACS Reponse DevRPCRep is NULL...");
		}
		logger.debug("getRespStr(): return " + rpcRes);
		return rpcRes;
	}
	/**
	 * AddObject
	 * 
	 * @param deviceId
	 * @param name
	 * @return
	 *            <li>0,1:??????</li>
	 *            <li>-1:??????????????????</li>
	 *            <li>-2:??????????????????</li>
	 *            <li> -3:??????????????????</li>
	 *            <li>-9:??????????????????</li>
	 *            <li>??????:TR069??????</li>
	 */
	public AddOBJ add(String deviceId, String name)
	{
		logger.debug("add({},{}): ", deviceId, name);
		AddOBJ obj = new AddOBJ();
		if (deviceId == null || name == null)
		{
			logger.debug("deviceId == null");
			obj.setStatus(-9);
			return obj;
		}
		AddObject addObject = new AddObject();
		addObject.setObjectName(name);
		addObject.setParameterKey("GWMS");
		
		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "AddObject";
		rpcArr[0].rpcValue = addObject.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> respArr = execRPC(deviceId, devRpcArr);
		if (respArr == null || respArr.get(0) == null)
		{
			logger.debug("flag = -9");
			obj.setStatus(-9);
			return obj;
		}
		int flag = respArr.get(0).getStat();
		if(flag == 1)
		{
			String resp = getRespStr(respArr,"AddObjectResponse");
	
			obj.setStatus(respArr.get(0).getStat());
			AddObjectResponse addObjectResponse = null;
			try
			{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapOBJ == null)
				{
					return obj;
				}
				addObjectResponse = XmlToRpc.AddObjectResponse(soapOBJ.getRpcElement());
			}
			catch (Exception e)
			{
				logger.debug("{}", e.getMessage());
			}
			if (addObjectResponse == null)
			{
				logger.debug("flag = -9");
				obj.setStatus(-9);
			}
			else
			{
				obj.setStatus(addObjectResponse.getStatus());
				obj.setInstance(addObjectResponse.getInstanceNumber());
			}
		}
		else
		{
			obj.setStatus(flag);
		}
		return obj;
	}

	/**
	 * DeleteObject
	 * 
	 * @param deviceId
	 * @param name
	 *            TR069??????
	 * @return
	 *            <li>0,1:??????</li>
	 *            <li>-1:??????????????????</li>
	 *            <li>-2:??????????????????</li>
	 *            <li> -3:??????????????????</li>
	 *            <li>-9:??????????????????</li>
	 *            <li>??????:TR069??????</li>
	 */
	public int del(String deviceId, String name)
	{
		logger.debug("setValue({},{}): ", deviceId, name);
		int flag = -9;
		if (deviceId == null || name == null)
		{
			logger.debug("deviceId == null");
			return flag;
		}
		DeleteObject rpc = new DeleteObject();
		rpc.setObjectName(name);
		rpc.setParameterKey("GWMS");
		
		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "DeleteObject";
		rpcArr[0].rpcValue = rpc.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> respArr = execRPC(deviceId, devRpcArr);
		if (respArr == null || respArr.get(0) == null)
		{
			logger.debug("flag = -9");
			flag = -9;
			return flag;
		}
		flag = respArr.get(0).getStat();
		logger.debug("flag = " + flag);
		return flag;
	}

	/**
	 * ??????ACS???RPC??????
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return DevRPCRep[]
	 */
	public List<DevRpcCmdOBJ> execRPC(String deviceId, DevRpc[] devRpcArr)
	{
		logger.debug("execRPC({},{}): ", deviceId, devRpcArr);
		if (null == deviceId || null == devRpcArr)
		{
			logger.warn("null == deviceId");
			return null;
		}
		return execRPC(devRpcArr);
	}

	/**
	 * ??????ACS???RPC??????
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return DevRPCRep[]
	 */
	private List<DevRpcCmdOBJ> execRPC(DevRpc[] devRpcArr)
	{
		logger.debug("execRPC({}):", devRpcArr);
		if (null == devRpcArr || devRpcArr.length == 0)
		{
			logger.warn("pnull == devRPCArr");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		AcsCorbaDAO acsCorbaDAO = new AcsCorbaDAO(Global.getPrefixName(gw_type)+Global.SYSTEM_ACS);
		devRPCRep = acsCorbaDAO.execRPC(LipossGlobals.getClientId(), Global.RpcCmd_Type,
				Global.Priority_Hig, devRpcArr);
		return devRPCRep;
	}
	
	/**
	 * ??????ACS???RPC??????
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return DevRPCRep[]
	 */
	public List<DevRpcCmdOBJ> execRPC(DevRpc[] devRpcArr,int rpcType)
	{
		logger.debug("execRPC({}):", devRpcArr);
		if (null == devRpcArr || devRpcArr.length == 0)
		{
			logger.warn("pnull == devRPCArr");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		AcsCorbaDAO acsCorbaDAO = new AcsCorbaDAO(Global.getPrefixName(gw_type)+Global.SYSTEM_ACS);
		devRPCRep = acsCorbaDAO.execRPC(LipossGlobals.getClientId(), rpcType,
				Global.Priority_Hig, devRpcArr);
		return devRPCRep;
	}

	/**
	 * ??????DevRPC[],????????????????????????
	 * 
	 * @param deviceId
	 *            ??????id
	 * @param stringArr
	 *            ????????????????????????
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return DevRPC[]
	 */
//	private DevRPC[] getDevRpcArray(String deviceId, String[] stringArr)
//	{
//		logger.debug("getDevRpcArray({},{}):", deviceId, stringArr);
//		DevRPC[] devRPCArr = null;
//		if (null != deviceId)
//		{
//			String sqlDevInfo = "select loopback_ip, cr_port"
//					+ ",cr_path, acs_username, acs_passwd, device_id"
//					+ ",oui,device_serialnumber from tab_gw_device"
//					+ " where device_id=?";
//			PrepareSQL psql = new PrepareSQL(sqlDevInfo);
//			psql.setString(1, deviceId);
//			Map devMap = DataSetBean.getRecord(psql.getSQL());
//			if (null != devMap && false == devMap.isEmpty())
//			{
//				devRPCArr = new DevRPC[1];
//				devRPCArr[0] = new DevRPC();
//				devRPCArr[0].DeviceId = String.valueOf(devMap.get("device_id"));
//				devRPCArr[0].OUI = String.valueOf(devMap.get("oui"));
//				devRPCArr[0].SerialNumber = String.valueOf(devMap
//						.get("device_serialnumber"));
//				devRPCArr[0].ip = String.valueOf(devMap.get("loopback_ip"));
//				devRPCArr[0].port = Integer.valueOf(devMap.get("cr_port").toString());
//				devRPCArr[0].path = String.valueOf(devMap.get("cr_path"));
//				devRPCArr[0].username = String.valueOf(devMap.get("acs_username"));
//				devRPCArr[0].passwd = String.valueOf(devMap.get("acs_passwd"));
//				devRPCArr[0].rpcArr = stringArr;
//			}
//		}
//		return devRPCArr;
//	}

	/**
	 * ??????ACS??? 1?????? 0??????
	 * 
	 * @param infoType = 1    ??????
		chgType=1    ??????
		chgType=0    ??????
		data = device_id
		 
		chgType    ??????????????????
		chgType=1    ?????????????????????
		chgType=0    ????????????
		data =  version_id
		
		 
		chgType    ACS????????????
		chgType=1     ????????????    tab_auth(access_flag)
		data=""           

	 * @author Jason(3412)
	 * @date 2009-7-29
	 * @return int
	 */
	public int chgInfo(int chgType, int infoType, String data)
	{
		logger.debug("chgInfo({},{},{})", new Object[]{chgType, infoType,data});
		return new AcsCorbaDAO(Global.getPrefixName(gw_type)+Global.SYSTEM_ACS).chgInfo(chgType, infoType, data);
	}
	
	
	
	/**
	 * ??????????????????????????????
	 * 
	 * @param request
	 * @return
	 */
	public HashMap getParaMap(String paraV, String device_id)
	{
		// ???????????????????????????
		HashMap paraMap = new HashMap();
		paraMap.clear();
		
		paraMap.put("faultCode", "-9");
		
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		SoapOBJ soapOBJ = null;
		List<String> pisList = null;
		ParameterInfoStruct[] pisArr = null;
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
			return paraMap;
		}
		DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(0);
		if (devRpcCmdOBJ == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
			return paraMap;
		}
		// ????????????
		ArrayList<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdOBJ
				.getRpcList();
		
		paraMap.put("faultCode", devRpcCmdOBJ.getStat()+"");
		
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0)
		{
			logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", device_id);
			return paraMap;
		}
		GetParameterNamesResponse[] getParameterNamesResponseArr = new GetParameterNamesResponse[rpcCmdObjList
				.size()];
		for (int j = 0; j < rpcCmdObjList.size(); j++)
		{
			com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(j);
			if ("GetParameterNamesResponse".equals(acsRpcCmdObj.getRpcName()))
			{
				String resp = acsRpcCmdObj.getValue();
				Fault fault = null;
				if (resp == null || "".equals(resp))
				{
					logger.debug("DevRpcCmdOBJ.value == null");
				}
				else
				{
					soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
					if (soapOBJ != null)
					{
						fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
						if (fault != null)
						{
							logger.warn("setValue({})={}", device_id, fault.getDetail()
									.getFaultString());
						}
						else
						{
							getParameterNamesResponseArr[j] = XmlToRpc
									.GetParameterNamesResponse(soapOBJ.getRpcElement());
						}
					}
				}
			}
		}
		for (int k = 0; k < getParameterNamesResponseArr.length; k++)
		{
			pisList = new ArrayList<String>();
			if (null == getParameterNamesResponseArr[k])
			{
				logger.warn("getParameterNamesResponseArr[k]??????");
			}
			else
			{
				pisArr = getParameterNamesResponseArr[k].getParameterList();
				if (null != pisArr)
				{
					String name = null;
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						String writable = pisArr[i].getWritable();
						paraMap.put(i + "", name + "," + writable);
					}
				}
			}
		}
		// ???????????????????????????
		return paraMap;
	}
	
	
	
	
	/**
	 * ??????device_id???????????????1???DevRPC????????????
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return DevRpc[]
	 */
	public DevRpc[] getDevRPCArr(String device_id, RPCObject rpcObject)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null)
		{
			stringRpcValue = "";
			stringRpcName = "";
		}
		else
		{
			stringRpcValue = rpcObject.toRPC();
			stringRpcName = rpcObject.getClass().getSimpleName();
		}
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		devRPCArr[0].rpcArr = new Rpc[1];
		devRPCArr[0].rpcArr[0] = new Rpc();
		devRPCArr[0].rpcArr[0].rpcId = "1";
		devRPCArr[0].rpcArr[0].rpcName = stringRpcName;
		devRPCArr[0].rpcArr[0].rpcValue = stringRpcValue;
		return devRPCArr;
	}
	
	
	
	
	/**
	 * ????????????DevRPC[]????????????corba??????????????????
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return List<DevRpcCmdOBJ>
	 */
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr, int rpcType)
	{
		logger.debug("getDevRPCResponse(devRPCArr)");
		if (devRPCArr == null)
		{
			logger.error("devRPCArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, rpcType);
		return devRPCRep;
	}
	
	
	
	/**
	 * add by zhangchy 2013-01-21
	 * 
	 * ?????????????????????????????????
	 * 
	 * @param paraMap
	 * @return
	 */
	public String getString(Map paraMap)
	{
		String strNode = "";
		if (paraMap != null)
		{
			int i = 0;
			while (paraMap.get(i + "") != null)
			{
				if (i == 0)
				{
					strNode = (String) paraMap.get(i + "");
				}
				else
				{
					strNode += "|" + (String) paraMap.get(i + "");
				}
				i++;
			}
		}
		return strNode;
	}
	
	
	/**
	 * add by zhangchy 2013-01-22
	 * 
	 * ???ACS ????????????????????????????????????
	 * 
	 * @param device_id
	 * @param gw_type
	 * @return
	 */
	public int testConnection(String device_id, String gw_type)
	{

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
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}

	
	public int getStat() {
		return stat;
	}

	
	public void setStat(int stat) {
		this.stat = stat;
	}

	/**
	 * ????????????
	 * 
	 * @param deviceIdArr
	 *            ??????ID??????
	 * @author alex(yanhj@)
	 * @date 2010-4-14
	 * @return int
	 *         <UL>
	 *         <li>-1:????????????</li>
	 *         <li>0:????????????</li>
	 *         <li>1:????????????</li>
	 *         </UL>
	 *         
	 *   ????????????????????????TR069????????????????????????????????????????????? by zhangchy 2012-03-15
	 *         
	 */
//	public int chgDev(String[] deviceIdArr, String[] devSnArr, String[] ouiArr)
//	{
//		logger.debug("chgDev({})", deviceIdArr);
//		logger.debug("deviceIdArr" + deviceIdArr.length);
//		int r = 0;
//		Dev[] devArr = new Dev[deviceIdArr.length];
//		if (null == deviceIdArr || null == devSnArr || null == ouiArr
//				|| deviceIdArr.length == 0)
//		{
//			logger.debug("null = deviceIdArr");
//			return r;
//		}
//		for (int i = 0; i < deviceIdArr.length; i++)
//		{
//			logger.debug("deviceIdArr====>" + deviceIdArr[i]);
//			logger.debug("devSnArr====>" + devSnArr[i]);
//			logger.debug("ouiArr====>" + ouiArr[i]);
//			if (deviceIdArr[i] == null || devSnArr[i] == null || ouiArr[i] == null)
//			{
//				logger.debug("null == deviceIdArr");
//				return r;
//			}
//			devArr[i] = new Dev();
//			devArr[i].devId = deviceIdArr[i];
//			devArr[i].devOui = ouiArr[i];
//			devArr[i].devSn = devSnArr[i];
//		}
//		RPCManager acsCorbaRpcManager = ACSDistributeUtil
//				.getRPCManagerBydeviceId(deviceIdArr[0]);
//		try
//		{
//			acsCorbaRpcManager.chgDev(LipossGlobals.getClientId(), devArr);
//		}
//		catch (Exception e)
//		{
//			logger.debug("CORBA ACS Error:{},Rebind.", e.getMessage());
//			acsCorbaRpcManager = ACSDistributeUtil
//					.getRPCManagerBydeviceIdSecond(deviceIdArr[0]);
//			try
//			{
//				acsCorbaRpcManager.chgDev(LipossGlobals.getClientId(), devArr);
//			}
//			catch (Exception e1)
//			{
//				logger.error("CORBA ACS Error,Rebind.");
//				acsCorbaRpcManager = ACSDistributeUtil
//						.getRPCManagerBydeviceIdThird(deviceIdArr[0]);
//				try
//				{
//					acsCorbaRpcManager.chgDev(LipossGlobals.getClientId(), devArr);
//				}
//				catch (Exception e2)
//				{
//					logger.error("CORBA ACS Error,Rebind.");
//					r = -1;
//					return r;
//				}
//			}
//		}
//		r = 1;
//		return r;
//	}

	/**
	 * ????????????
	 * 
	 * @param deviceId
	 *            ??????ID
	 * @author alex(yanhj@)
	 * @date 2010-4-14
	 * @return int
	 *         <UL>
	 *         <li>-1:????????????</li>
	 *         <li>0:????????????</li>
	 *         <li>1:????????????</li>
	 *         </UL>
	 */
//	public int chgDev(String deviceId)
//	{
//		logger.debug("chgDev({})", deviceId);
//		return this.chgDev(new String[] { deviceId });
//	}
	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @date 2009-7-8
	 * @param deviceId
	 * @param gatherId
	 * @param path
	 * @param nextLevel
	 * @return
	 */
	public ArrayList<String> getParamNamesPath(String deviceId,
			String path, int nextLevel)
	{
		ArrayList<String> pislist = null;
		ParameterInfoStruct[] pisArr = null;
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(path);
		getParameterNames.setNextLevel(nextLevel);
		DevRpc[] realTimeObj = getDevRPCArr(deviceId, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = invokeAcsCorba(realTimeObj);
		Element element = dealDevRPCResponse("GetParameterNamesResponse",
				devRpcCmdOBJList, deviceId);
		if (element == null)
		{
			return null;
		}
		GetParameterNamesResponse getParameterNamesResponse = null;
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		if (getParameterNamesResponse != null)
		{
			pisArr = getParameterNamesResponse.getParameterList();
		}
		if(pisArr!=null && pisArr.length>0){
			pislist = new ArrayList<String>();
			for (int i = 0; i < pisArr.length; i++)
			{
				pislist.add(pisArr[i].getName());
			}
		}
		// ????????????
		getParameterNames = null;
		realTimeObj = null;
		getParameterNamesResponse = null;
		pisArr = null;
		
		return pislist;
	}
	/**
	 * @param devRPCArr
	 * @return
	 */
	private List<DevRpcCmdOBJ> invokeAcsCorba(DevRpc[] devRPCArr)
	{
		List<DevRpcCmdOBJ> list = this.execRPC(LipossGlobals.getClientId(), 1, 1, devRPCArr);
		if(list == null || list.isEmpty())
		{
			return null;
		}
		
		if(list.get(0).getRpcList() == null || list.get(0).getRpcList().isEmpty())
		{
			logger.warn("[{}] call  acscorba paraList:[{}]", devRPCArr[0].devId, list.get(0).getRpcList());
			return null;
		}
		
		return list;
	}
	public List<DevRpcCmdOBJ> execRPC(String clientId, int i, int j,
			DevRpc[] devRPCArr) {
		logger.debug("execRPC()");
//		return new AcsCorbaDAO(Global.SYSTEM_NAME + SVR_OBJECT_NAME).execRPC(
//				Global.CLIENT_ID, i, j, devRPCArr);
		if (true)
		{
			try
			{
				if(devRPCArr!=null&&devRPCArr.length>0){
					logger.warn("[{}]??????acs??????  500 ??????",devRPCArr[0].devId);
				}
				Thread.sleep(500);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		List<DevRpcCmdOBJ> list = null;
		try
		{
			logger.warn("[{}]??????acs??????,systemKey={},devRPCArr={},devRPCArr.size={}",
					new Object[]{devRPCArr[0].devId,Global.getPrefixName(gw_type)+Global.SYSTEM_ACS,devRPCArr,devRPCArr.length});
			AcsCorbaDAO acsCorbaDAO = new AcsCorbaDAO(Global.getPrefixName(gw_type)+Global.SYSTEM_ACS);
			list = acsCorbaDAO.execRPC(LipossGlobals.getClientId(), i, j, devRPCArr);
			logger.warn("[{}]??????acs?????????list.size={}",devRPCArr[0].devId,list.size());
		}
		catch (Exception e)
		{
			logger.error("[{}]??????ac????????????",devRPCArr[0].devId);
			e.printStackTrace();
		}
		return list;
	}	
	/**
	 * ?????????????????????????????????RPC????????????
	 * 
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	private Element dealDevRPCResponse(String stringRpcName,
			List<DevRpcCmdOBJ> devRpcCmdOBJList, String deviceId)
	{
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.error("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(0);
		if (devRpcCmdOBJ == null)
		{
			logger.error("[{}]DevRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdOBJ.getRpcList();
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0)
		{
			logger.error("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(0);
		if (acsRpcCmdObj == null)
		{
			logger.error("[{}]ACSRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		if (stringRpcName == null)
		{
			logger.error("[{}]stringRpcName?????????", deviceId);
			return null;
		}
		if (stringRpcName.equals(acsRpcCmdObj.getRpcName()))
		{
			String resp = acsRpcCmdObj.getValue();
			logger.debug("[{}]???????????????{}", deviceId, resp);
			if (resp == null || "".equals(resp))
			{
				logger.error("[{}]DevRpcCmdOBJ.value == null", deviceId);
			}
			else
			{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapOBJ != null)
				{
					return soapOBJ.getRpcElement();
				}
			}
		}
		return null;
	}
	/**
	 * ?????????????????????????????????????????????i
	 * 
	 * @param paramPath
	 * @return
	 */
	public List<String> getIList(String deviceId,String paramPath)
	{
		logger.warn("[{}]????????????{}", deviceId, paramPath);
		Map<String, String> connMap = getParaTreeMap(deviceId,paramPath);
		if (null == connMap || 0 == connMap.size())
		{
			logger.warn("[{}]????????????{}??????", deviceId, paramPath);
			
			try
			{
				logger.warn("[{}]??????3s???????????????[{}]", deviceId, paramPath);
				Thread.sleep(3000);
				connMap = getParaTreeMap(deviceId,paramPath);
				if (null == connMap || 0 == connMap.size())
				{
					logger.warn("[{}]??????????????????{}?????????", deviceId, paramPath);
					return null;
				}
			}
			catch (Exception e)
			{
				logger.error("[{}]????????????{}??????????????????" + e.getMessage(), deviceId, paramPath);
				return null;
			}
		}
		logger.warn("[{}]getIListMap({})=[{}]", new Object[]{deviceId, paramPath, connMap});
		// ???????????????pvc??????
		List<String> jList = new ArrayList<String>();
		Set<String> set = connMap.keySet();
		Iterator<String> iterator = set.iterator();
		// ?????????????????????????????????????????????
		while (iterator.hasNext())
		{
			String name = iterator.next();
			String value = connMap.get(name);
			if (null == value)
			{
				continue;
			}
			// ??????????????????
			value = value.substring(0, value.indexOf(","));
			if (null != value && !"".equals(value) && !paramPath.equals(value))
			{
				// ????????????????????????????????????
				try {
					jList.add(value.substring(paramPath.length(), value.lastIndexOf(".")));
				} catch (StringIndexOutOfBoundsException e) {
					logger.error("[{}]getIListMap???????????????[{}]", deviceId, value);
				}
				
			}
		}
		// ??????connMap
		connMap = null;
		return jList;
	}
	/**
	 * GetParameterValues
	 * 
	 * @param deviceId
	 * @param arr
	 * @return
	 */
	public Map<String, String> getParaTreeMap(String deviceId, String arrPath)
	{
		logger.debug("getParaTreeMap({},{}): ", deviceId, arrPath);
		Map<String, String> paramMap = new HashMap<String, String>();
		if (deviceId == null || arrPath == null || arrPath.length() == 0)
		{
			logger.debug("deviceId == null");
			return paramMap;
		}
		
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(arrPath);
		getParameterNames.setNextLevel(1);

		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "GetParameterValues";
		rpcArr[0].rpcValue = getParameterNames.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> respArr = execRPC(deviceId,devRpcArr);
		if (null == respArr || respArr.isEmpty())
		{
			return paramMap;
		}
		// ???????????????????????????
		
		String setRes = respArr.get(0).getRpcList().get(0).getValue();
		
		
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
		if (soapOBJ == null)
		{
			return null;
		}
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(soapOBJ
				.getRpcElement());
		// ????????????XML??????,??????????????????
		if (null != getParameterNamesResponse)
		{
			ParameterInfoStruct[] pisArr = getParameterNamesResponse.getParameterList();
			if (null != pisArr)
			{
				String name = null;
				for (int i = 0; i < pisArr.length; i++)
				{
					name = pisArr[i].getName();
					String writable = pisArr[i].getWritable();
					paramMap.put(i + "", name + "," + writable);
				}
			}
			// ??????pisArr
			pisArr = null;
		}
		// ????????????
		getParameterNames = null;
		devRpcArr = null;
		getParameterNamesResponse = null;
		return paramMap;
	}
	/**
	 * ????????????????????????Map(tr069)
	 * 
	 * @param para_name
	 * @return Map paramMap
	 */
	public Map<String, String> getParaValueMap(String deviceId,String[] para_name)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.clear();
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(para_name);
		DevRpc[] devRPCArr = getDevRPCArr(deviceId,getParameterValues);
		// ???NULL??????????????????
		if (devRPCArr == null)
			return paramMap;
		try
		{
			List<DevRpcCmdOBJ> list = invokeAcsCorba(deviceId,devRPCArr);
			// ???????????????????????????
			if (list == null)
			{
				return paramMap;
			}
			String setRes = list.get(0).getRpcList().get(0).getValue(); 
			getParameterValues = null;
			devRPCArr = null;
			GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
			try
			{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
				if (soapOBJ == null)
				{
					return null;
				}
				getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ
						.getRpcElement());
			}
			catch (Exception e)
			{
				logger.error("[{}]???????????????????????????????????????????????????", deviceId);
			}
			if (null != getParameterValuesResponse)
			{
				ParameterValueStruct[] pisArr = getParameterValuesResponse
						.getParameterList();
				if (pisArr != null)
				{
					String name = null;
					String value = null;
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						value = pisArr[i].getValue().para_value;
						paramMap.put(name, value);
						logger.warn("getParaValueMap-Name:{}, Value={}", name, value);
					}
				}
				// ??????pisArr, getParameterValuesResponse
				pisArr = null;
				getParameterValuesResponse = null;
			}
			else
			{
				return paramMap;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return paramMap;
	}
	/**
	 * @param devRPCArr
	 * @return
	 */
	private List<DevRpcCmdOBJ> invokeAcsCorba(String deviceId,DevRpc[] devRPCArr)
	{
		List<DevRpcCmdOBJ> list = execRPC(LipossGlobals.getClientId(), 1, 1, devRPCArr);
		if(list == null || list.isEmpty())
		{
			logger.debug("[{}] call  acscorba response list:[{}]", deviceId, list);
			return null;
		}
		
		if(list.get(0).getRpcList() == null || list.get(0).getRpcList().isEmpty())
		{
			logger.warn("[{}] call  acscorba paraList:[{}]", deviceId, list.get(0).getRpcList());
			return null;
		}
		
		return list;
	}
	
	public SetParameterValues getSetParameterValues(String[] paramNames,
			String[] paramValues, String[] paramTypeIds)
	{
		if (paramNames.length == paramValues.length
				&& paramValues.length == paramTypeIds.length)
		{
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] parameterListArr = new ParameterValueStruct[paramNames.length];
			for (int i = 0; i < paramNames.length; i++)
			{
				parameterListArr[i] = getParameterValueStruct(paramNames[i],
						paramValues[i], paramTypeIds[i]);
			}
			setParameterValues.setParameterList(parameterListArr);
			setParameterValues.setParameterKey("ITMS+");
			return setParameterValues;
		}
		return null;
	}
	
	private ParameterValueStruct getParameterValueStruct(String paramName,
			String paramValue, String paramTypeId)
	{
		ParameterValueStruct parameter = new ParameterValueStruct();
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = paramValue;
		anyObject.para_type_id = paramTypeId;
		parameter = new ParameterValueStruct();
		parameter.setName(paramName);
		parameter.setValue(anyObject);
		return parameter;
	}
	
	/**
	 * ??????device_id????????????1???DevRPC????????????
	 * 
	 * @param device_id
	 *            ??????id
	 * @param rpcObject
	 *            ----GetParameterValues/GetParameterNames/SetParameterValues
	 * @return
	 */
	public DevRpc[] getDevRPCArr(String deviceId,RPCObject[] rpcObject)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[rpcObject.length];
		for (int i = 0; i < rpcObject.length; i++)
		{
			Rpc rpc = new Rpc();
			rpc.rpcId = StringUtil.getStringValue(i + 1);
			if (rpcObject[i] == null)
			{
				rpc.rpcName = "";
				rpc.rpcValue = "";
			}
			else
			{
				rpc.rpcName = rpcObject[i].getClass().getSimpleName();
				rpc.rpcValue = rpcObject[i].toRPC();
			}
			rpcArr[i] = rpc;
		}
		devRPCArr[0].rpcArr = rpcArr;
		return devRPCArr;
	}
}
