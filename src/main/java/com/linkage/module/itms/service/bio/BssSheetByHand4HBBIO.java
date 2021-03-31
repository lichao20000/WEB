package com.linkage.module.itms.service.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.dao.BssSheetByHand4HBDAO;
import com.linkage.module.itms.service.obj.SheetObj;


public class BssSheetByHand4HBBIO 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4HBBIO.class);
	private BssSheetByHand4HBDAO dao ;
	private BssSheetByHand4HBBIOServ serv;
	
	public SheetObj checkLoid(String loid,String userType)
	{
		boolean result = dao.isLoidExists(loid, userType);
		if(result)
		{
			SheetObj obj = new SheetObj();
			obj.setUserType(userType);
			Map<String, String> userInfo = dao.getUserInfo(loid, userType);
			userInfoToObj(userInfo,obj,userType);
			List<HashMap<String, String>> list = dao.getServInfo(loid, userType);
			servInfoToObj(list,obj,userType);
			return obj;
		}
		else
		{
			//LOID不存在
			return null;
		}
	}
	
	private void servInfoToObj(List<HashMap<String, String>> list, SheetObj obj, String userType)
	{
		for (HashMap<String, String> hashMap : list)
		{
			//宽带
			if("10".equals(hashMap.get("serv_type_id")))
			{
				obj.setNetServTypeId("22");
				obj.setNetOperateId("2");
				obj.setNetUsername(hashMap.get("username"));
				obj.setNetPasswd(Base64.decode(hashMap.get("passwd")));
				obj.setNetVlanId(hashMap.get("vlanid"));
				obj.setNetWanType(hashMap.get("wan_type"));
				obj.setNetIpaddress(StringUtil.getStringValue(hashMap,"ipaddress",""));
				obj.setNetIpmask(StringUtil.getStringValue(hashMap,"ipmask",""));
				obj.setNetGateway(StringUtil.getStringValue(hashMap,"gateway",""));
				obj.setNetIpdns(StringUtil.getStringValue(hashMap,"adsl_ser",""));
				obj.setStandNetIpdns(getIpTypeReverse(StringUtil.getStringValue(hashMap,"ip_type","")));
				obj.setNetSpeed(dao.getSpeed(hashMap.get("username")));
			}
			//VOIP
			if("14".equals(hashMap.get("serv_type_id")))
			{
				List<HashMap<String, String>> paramInfo_list = this.dao.getVoipParaInfo(hashMap.get("user_id"), userType, "14");
		        for (HashMap<String, String> paramInfo : paramInfo_list)
		        {
					//h248
					if(null != paramInfo && "2".equals(paramInfo.get("protocol")))
					{
						obj.setHvoipServTypeId("15");
						obj.setHvoipOperateId("2");
						obj.setHvoipPhone(StringUtil.getStringValue(paramInfo,"voip_phone",""));
						obj.setHvoipRegId(StringUtil.getStringValue(paramInfo,"reg_id",""));
						obj.setHvoipRegIdType(StringUtil.getStringValue(paramInfo,"reg_id_type",""));
						obj.setHvoipMgcIp(StringUtil.getStringValue(paramInfo,"prox_serv",""));
						obj.setHvoipMgcPort(StringUtil.getStringValue(paramInfo,"prox_port",""));
						obj.setHvoipStandMgcIp(StringUtil.getStringValue(paramInfo,"stand_prox_serv",""));
						obj.setHvoipStandMgcPort(StringUtil.getStringValue(paramInfo,"stand_prox_port",""));
						obj.setHvoipPort(StringUtil.getStringValue(paramInfo,"voip_port",""));
						obj.setHvoipVlanId(StringUtil.getStringValue(hashMap,"vlanid",""));//
						obj.setHvoipWanType(StringUtil.getStringValue(hashMap,"wan_type",""));
						obj.setHvoipIpaddress(StringUtil.getStringValue(hashMap,"ipaddress",""));
						obj.setHvoipIpmask(StringUtil.getStringValue(hashMap,"ipmask",""));
						obj.setHvoipGateway(StringUtil.getStringValue(hashMap,"gateway",""));
						obj.setHvoipIpdns(StringUtil.getStringValue(hashMap, "adsl_ser", ""));
					}
					//ims sip
					if(null != paramInfo && ("0".equals(paramInfo.get("protocol")) || "1".equals(paramInfo.get("protocol"))))
					{
						obj.setSipServTypeId("14");
						obj.setSipOperateId("2");
						obj.setSipVoipPhone(StringUtil.getStringValue(paramInfo,"voip_phone",""));
						obj.setSipVoipUsername(StringUtil.getStringValue(paramInfo,"voip_username",""));
						obj.setSipVoipPwd(StringUtil.getStringValue(paramInfo,"voip_passwd",""));
						obj.setSipProxServ(StringUtil.getStringValue(paramInfo,"prox_serv",""));
						obj.setSipProxPort(StringUtil.getStringValue(paramInfo,"prox_port",""));
						obj.setSipStandProxServ(StringUtil.getStringValue(paramInfo,"stand_prox_serv",""));
						obj.setSipStandProxPort(StringUtil.getStringValue(paramInfo,"stand_prox_port",""));
						obj.setSipVoipPort(StringUtil.getStringValue(paramInfo,"voip_port",""));
						obj.setSipRegiServ(StringUtil.getStringValue(paramInfo,"regi_serv",""));
						obj.setSipRegiPort(StringUtil.getStringValue(paramInfo,"regi_port",""));
						obj.setSipStandRegiServ(StringUtil.getStringValue(paramInfo,"stand_regi_serv",""));
						obj.setSipStandRegiPort(StringUtil.getStringValue(paramInfo,"stand_regi_port",""));
						obj.setSipOutBoundProxy(StringUtil.getStringValue(paramInfo,"out_bound_proxy",""));
						obj.setSipOutBoundPort(StringUtil.getStringValue(paramInfo,"out_bound_port",""));
						obj.setSipStandOutBoundProxy(StringUtil.getStringValue(paramInfo,"stand_out_bound_proxy",""));
						obj.setSipStandOutBoundPort(StringUtil.getStringValue(paramInfo,"stand_out_bound_port",""));
						obj.setSipProtocol(StringUtil.getStringValue(paramInfo,"protocol",""));
						
						obj.setSipVlanId(StringUtil.getStringValue(hashMap,"vlanid",""));
						obj.setSipWanType(StringUtil.getStringValue(hashMap,"wan_type",""));
						obj.setSipIpaddress(hashMap.get("ipaddress")==null ? "" : hashMap.get("ipaddress"));
						obj.setSipIpmask(hashMap.get("ipmask")==null ? "" : hashMap.get("ipmask"));
						obj.setSipGateway(hashMap.get("gateway")==null ? "" :hashMap.get("gateway"));
						obj.setSipIpdns(hashMap.get("adsl_ser")== null ? "" :hashMap.get("adsl_ser"));
						if ("0".equals(paramInfo.get("protocol")))
						{
							obj.setSipVoipUri(paramInfo.get("uri"));
							obj.setSipUserAgentDomain(paramInfo.get("user_agent_domain"));
						}
					}
				}
			}
			//IPTV
			if("11".equals(hashMap.get("serv_type_id")))
			{
				obj.setIptvServTypeId("21");
				obj.setIptvOperateId("2");
				obj.setIptvUserName(StringUtil.getStringValue(hashMap,"username",""));
				obj.setIptvPort(StringUtil.getStringValue(hashMap,"bind_port",""));
				obj.setIptvNum(StringUtil.getStringValue(hashMap,"serv_num",""));
				obj.setIptvVlanId(StringUtil.getStringValue(hashMap,"vlanid",""));
			}
			//VPDN
			if(Global.HBDX.equals(Global.instAreaShortName) && 
					"28".equals(hashMap.get("serv_type_id")))
			{
				obj.setVpdnServTypeId("23");
				obj.setVpdnOperateId("2");
				obj.setVpdnUserName(StringUtil.getStringValue(hashMap,"username",""));
				obj.setVpdnPort(StringUtil.getStringValue(hashMap,"bind_port",""));
				obj.setVpdnNum(StringUtil.getStringValue(hashMap,"serv_num",""));
				obj.setVpdnVlanId(StringUtil.getStringValue(hashMap,"vlanid",""));
			}
		}
		
	}

	private String getIpTypeReverse(String ipType)
	{
		String result = "0";

		if ("1".equals(ipType)){
			result = "0"; 
		}else if("2".equals(ipType)){
			result = "5";
		}else if("3".equals(ipType)){
			result = "1";
		}

		return result;
	}
/*
	private String getVoipProtStr(String lineId, String voipType)
	{
		String result = "-1";
		if("2".equals(voipType))
		{
			if("1".equals(lineId)){
				result = "A1";
			}else if ("2".equals(lineId)) {
				result = "A2"; 
			}else if ("3".equals(lineId)) {
				result = "A3"; 
			}else if ("4".equals(lineId)) {
				result = "A4"; 
			}else if ("5".equals(lineId)) {
				result = "A5"; 
			}else if ("6".equals(lineId)) {
				result = "A6"; 
			}else if ("7".equals(lineId)) {
				result = "A7"; 
			}else if ("8".equals(lineId)) {
				result = "A8"; 
			}
		}
		else 
		{
			if("1".equals(lineId)){
				result = "V1";
			}else if ("2".equals(lineId)) {
				result = "V2"; 
			}
		}
		return result;
	}
*/
	private void userInfoToObj(Map<String, String> userInfo, SheetObj obj, String userType)
	{
		obj.setUserOperateId("2");
		obj.setCityId(StringUtil.getStringValue(userInfo, "city_id", ""));
		obj.setOfficeId(StringUtil.getStringValue(userInfo,"office_id",""));
		obj.setAreaId(StringUtil.getStringValue(userInfo,"zone_id",""));
		obj.setAccessStyle(StringUtil.getStringValue(userInfo,"access_style_id",""));
		obj.setLinkman(StringUtil.getStringValue(userInfo,"linkman",""));
		obj.setLinkPhone(StringUtil.getStringValue(userInfo,"linkphone",""));
		obj.setEmail(StringUtil.getStringValue(userInfo,"email",""));
		obj.setMobile(StringUtil.getStringValue(userInfo,"mobile",""));
		obj.setLinkAddress(StringUtil.getStringValue(userInfo,"linkaddress",""));
		obj.setLinkmanCredno(StringUtil.getStringValue(userInfo,"credno",""));
		
		if ("2".equals(userType))
		{
			obj.setCustomerId(StringUtil.getStringValue(userInfo,"customer_id",""));
			obj.setCustomerAccount(StringUtil.getStringValue(userInfo,"linkman",""));
		}
		
	}

	public String doBusiness(SheetObj obj, UserRes curUser)
	{
		logger.debug("BssSheetByHand4AHBIO.doBusiness(SheetObj obj)");
		String resultMessage = serv.doUserInfo(obj);
		StringBuffer buffer = new StringBuffer();
		buffer.append("资料工单处理结果："+serv.getMessage(resultMessage, "resultDes"));
		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),Integer.valueOf(obj.getUserOperateId()), 
							"000".equals(serv.getMessage(resultMessage, "resultCode")) ? 1:0, serv.getMessage(resultMessage, "resultDes"));
		if ("000".equals(serv.getMessage(resultMessage, "resultCode")))
		{
			try
			{
				Thread.sleep(1000);
				
				if (!StringUtil.IsEmpty(obj.getSipServTypeId()))
				{
					String sipResult = serv.doSipVoip(obj);
					buffer.append(";\nSIP语音工单处理结果："+serv.getMessage(sipResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getSipServTypeId()),Integer.valueOf(obj.getSipOperateId()), 
							"000".equals(serv.getMessage(sipResult, "resultCode")) ? 1:0, serv.getMessage(sipResult, "resultDes"));
				}
				
				if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
				{
					String voipResult = serv.doH248VoipSheet(obj);
					buffer.append(";\nH248语音工单处理结果："+serv.getMessage(voipResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getHvoipServTypeId()),Integer.valueOf(obj.getHvoipOperateId()), 
							"000".equals(serv.getMessage(voipResult, "resultCode")) ? 1:0, serv.getMessage(voipResult, "resultDes"));
				}
				
				if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
				{
					String netResult = serv.doNetSheetResult(obj);
					buffer.append(";\n宽带业务工单处理结果："+serv.getMessage(netResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()), 
							"000".equals(serv.getMessage(netResult, "resultCode")) ? 1:0, serv.getMessage(netResult, "resultDes"));
				}
				
				if (!StringUtil.IsEmpty(obj.getIptvServTypeId()))
				{
					String itvResult = serv.doItvSheetResult(obj);
					buffer.append(";\nITV业务工单处理结果："+serv.getMessage(itvResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getIptvServTypeId()),Integer.valueOf(obj.getIptvOperateId()), 
							"000".equals(serv.getMessage(itvResult, "resultCode")) ? 1:0, serv.getMessage(itvResult, "resultDes"));
				}
				
				if (Global.HBDX.equals(Global.instAreaShortName) 
						&& !StringUtil.IsEmpty(obj.getVpdnServTypeId()))
				{
					String itvResult = serv.doVpdnSheetResult(obj);
					buffer.append(";\nVPDN业务工单处理结果："+serv.getMessage(itvResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getVpdnServTypeId()),Integer.valueOf(obj.getVpdnOperateId()), 
							"000".equals(serv.getMessage(itvResult, "resultCode")) ? 1:0, serv.getMessage(itvResult, "resultDes"));
				}
			}
			catch (Exception e)
			{
				logger.error("doBusiness 页面发送工单失败,err:" + e);
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
	
	public String delVpdnSheetResult(SheetObj sheetObj) {
		return serv.delVpdnSheetResult(sheetObj);
	}

	public String delH248VoipSheetResult(SheetObj sheetObj) {
		return serv.delH248VoipSheetResult(sheetObj);
	}

	public String delNetSheetResult(SheetObj sheetObj) {
		return serv.delNetSheetResult(sheetObj);
	}

	public String delUserSheetResult(SheetObj sheetObj) {
		return serv.delUserSheetResult(sheetObj);
	}

	public String delSipVoipSheetResult(SheetObj sheetObj) {
		return serv.delSipVoipSheetResult(sheetObj);
	}

	public String delIptvSheetResult(SheetObj sheetObj) {
		return serv.delIptvSheetResult(sheetObj);
	}
	
	public String checkCustomerId(String customerId)
	{
		boolean result = dao.isExistCustomer(customerId);
		if(result){
			//客户id存在
			return "-1";
		}else{
			return "000";
		}
	}

	public String getServInfo(String loid, String userType, String sipVoipPort)
	{
		Map<String, String> map = dao.getUserInfo(loid,userType);
		String userId = "";
		StringBuffer result = new StringBuffer();
		if (null != map && map.size() > 0) 
		{
			userId = StringUtil.getStringValue(map, "user_id");
		}
//		String lineId = String.valueOf(getLineId(sipVoipPort));
		List<HashMap<String, String>> list = dao.getServInfoByVoipPort(userId,userType,sipVoipPort,"14");
		if(null != list && list.size()>0)
		{
			HashMap<String, String> paramMap = list.get(0);

//			if("2".equals(userType))
			if("2".equals(paramMap.get("protocol")))
			{
				result.append("2").append("|");
				result.append(StringUtil.getStringValue(paramMap, "voip_phone", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "reg_id", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "reg_id_type", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "prox_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "prox_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_prox_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_prox_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "vlanid", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "wan_type", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "ipaddress", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "ipmask", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "gateway", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "adsl_ser", "")).append("|");
			}
			else
			{
				result.append("2").append("|");
				result.append(StringUtil.getStringValue(paramMap, "voip_phone", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "voip_username", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "voip_passwd", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "prox_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "prox_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_prox_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_prox_port", "")).append("|");//7
				result.append(StringUtil.getStringValue(paramMap, "regi_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "regi_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_regi_serv", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_regi_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "out_bound_proxy", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "out_bound_port", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_out_bound_proxy", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "stand_out_bound_port", "")).append("|");//15
				result.append(StringUtil.getStringValue(paramMap, "vlanid", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "wan_type", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "ipaddress", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "ipmask", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "gateway", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "adsl_ser", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "uri", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "user_agent_domain", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "protocol", ""));
			}
			return result.toString();
		}
		else 
		{
			return "-1";
		}
	}
	
	public int getLineId(String linePort)
	{
		logger.debug("getLineId({})", linePort);
		int lineId = 1;
		
		if (!StringUtil.IsEmpty(linePort))
		{
			lineId = Integer.valueOf(linePort.substring(linePort.length() - 1, 
										linePort.length())) + 1;
		}
		return lineId;
	}
	
/*	public int getLineId(String linePort)
	{
		logger.debug("getLineId({})", linePort);
		int lineId = 1;
		if ("V1".equals(linePort) || "A1".equals(linePort) || "USER001".equals(linePort)){
			lineId = 1;
		}else if ("V2".equals(linePort) || "A2".equals(linePort) || "USER002".equals(linePort)){
			lineId = 2;
		}else if ("V3".equals(linePort) || "A3".equals(linePort) || "USER003".equals(linePort)){
			lineId = 3;
		}else if ("V4".equals(linePort) || "A4".equals(linePort) || "USER004".equals(linePort)){
			lineId = 4;
		}else if ("V5".equals(linePort) || "A5".equals(linePort) || "USER005".equals(linePort)){
			lineId = 5;
		}else if ("V6".equals(linePort) || "A6".equals(linePort) || "USER006".equals(linePort)){
			lineId = 6;
		}else if ("V7".equals(linePort) || "A7".equals(linePort) || "USER007".equals(linePort)){
			lineId = 7;
		}else if ("V8".equals(linePort) || "A8".equals(linePort) || "USER008".equals(linePort)){
			lineId = 8;
		}
		return lineId;
	}
*/
	
	
	public BssSheetByHand4HBDAO getDao(){
		return dao;
	}

	public void setDao(BssSheetByHand4HBDAO dao){
		this.dao = dao;
	}

	public BssSheetByHand4HBBIOServ getServ() {
		return serv;
	}

	public void setServ(BssSheetByHand4HBBIOServ serv) {
		this.serv = serv;
	}

	
}
