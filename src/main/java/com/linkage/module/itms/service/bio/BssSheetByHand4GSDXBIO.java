package com.linkage.module.itms.service.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.utils.XMLFormatUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.dao.BssSheetByHand4GSDXDAO;
import com.linkage.module.itms.service.obj.SheetObj;


public class BssSheetByHand4GSDXBIO 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4GSDXBIO.class);
	private BssSheetByHand4GSDXDAO dao ;
	private BssSheetByHand4GSDXBIOServ serv;
	
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
				if (StringUtil.IsEmpty(hashMap.get("passwd"))) {
					obj.setNetPasswd("");
				}else {
					obj.setNetPasswd(Base64.decode(hashMap.get("passwd")));
				}
				obj.setNetVlanId(hashMap.get("vlanid"));
				obj.setNetWanType(hashMap.get("wan_type"));
				obj.setNetIpaddress(StringUtil.getStringValue(hashMap,"ipaddress",""));
				obj.setNetIpmask(StringUtil.getStringValue(hashMap,"ipmask",""));
				obj.setNetGateway(StringUtil.getStringValue(hashMap,"gateway",""));
				obj.setNetIpdns(StringUtil.getStringValue(hashMap,"adsl_ser",""));
				obj.setStandNetIpdns(getIpTypeReverse(StringUtil.getStringValue(hashMap,"ip_type","")));
			}
			//共享wifi
			if("20".equals(hashMap.get("serv_type_id")))
			{
				obj.setWifiServTypeId("23");
				obj.setWifiOperateId("2");
				obj.setWifiUsername(hashMap.get("username"));
				obj.setWifiPassword(hashMap.get("passwd"));
				obj.setWifiVlanId(hashMap.get("vlanid"));
				obj.setWifiWanType(hashMap.get("wan_type"));
			}
			
			//VOIP
			if("14".equals(hashMap.get("serv_type_id")))
			{
				List<HashMap<String, String>> paramInfo_list = this.dao.getVoipParaInfo(hashMap.get("user_id"), userType, "14");
		        if (paramInfo_list==null) {
					continue;
				}
				
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
			//云网超宽带
			if(Global.GSDX.equals(Global.instAreaShortName)
					&& "47".equals(hashMap.get("serv_type_id")))
			{
				obj.setCloudNetServTypeId("47");
				obj.setCloudNetOperateId("Z");
				obj.setCloudNetAccount(StringUtil.getStringValue(hashMap,"username",""));
				obj.setCloudNetPass(StringUtil.getStringValue(hashMap,"passwd",""));
				obj.setCloudNetVlanId(StringUtil.getStringValue(hashMap,"vlanid",""));


				Map cloudNetMap = dao.getCloudNetInfoMap(hashMap.get("user_id"));
				if(null != cloudNetMap && cloudNetMap.size() > 0)
				{
					obj.setCloudNetUpBandwidth(StringUtil.getStringValue(cloudNetMap,"ipoe_upbandwidth",""));
					obj.setCloudNetDownBandwidth(StringUtil.getStringValue(cloudNetMap,"ipoe_downbandwidth",""));
					obj.setCloudNetDscp(StringUtil.getStringValue(cloudNetMap,"ipoe_dscp",""));
					String appType = StringUtil.getStringValue(cloudNetMap,"app_type","");
					String appTypes[] = appType.split(",");
					appType = "";
					if(null != appTypes && appTypes.length > 0 )
					{
						for (String appTypeTmp : appTypes) {
							if(!"".equals(appTypeTmp) && isNumeric(appTypeTmp))
							{
								appType = appType + appTypeTmp + ",";
							}
						}
					}
					obj.setCloudNetAppType(appType);
				}
				else
				{
					obj.setCloudNetUpBandwidth("");
					obj.setCloudNetDownBandwidth("");
					obj.setCloudNetDscp("");
					obj.setCloudNetAppType("");
				}
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
		obj.setSpecId(StringUtil.getStringValue(userInfo,"spec_id",""));
		
		if ("2".equals(userType))
		{
			obj.setCustomerId(StringUtil.getStringValue(userInfo,"customer_id",""));
			obj.setCustomerAccount(StringUtil.getStringValue(userInfo,"linkman",""));
		}
		
	}

	public String doBusiness(SheetObj obj, UserRes curUser)
	{
		logger.debug("BssSheetByHand4AHBIO.doBusiness(SheetObj obj)");
		String returnResult ="";
		boolean result = dao.isLoidExists(obj.getLoid(), obj.getUserType());
		if(result)
		{
			//修改资料工单
			returnResult=serv.changeUserInfo(obj);
		}else {
			//新增资料工单
			returnResult=serv.doUserInfo(obj);
		}
		logger.warn("资料----"+returnResult);
		StringBuffer buffer = new StringBuffer();
		buffer.append("资料工单处理结果："+serv.getMessage(serv.getResult(returnResult)));
		
		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),Integer.valueOf(obj.getUserOperateId()), 
							"1".equals(serv.getResult(returnResult)) ? 1:0, serv.getMessage(serv.getResult(returnResult)));
		if ("1".equals(serv.getResult(returnResult)))
		{
			try
			{
				Thread.sleep(1000);
				
				if (!StringUtil.IsEmpty(obj.getSipServTypeId()))
				{
					String sipResult = serv.doSipVoip(obj);
					logger.warn("SIP----"+sipResult);
					buffer.append(";\nSIP语音工单处理结果："+serv.getMessage(serv.getResult(sipResult)));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getSipServTypeId()),Integer.valueOf(obj.getSipOperateId()), 
							"1".equals(serv.getResult(sipResult)) ? 1:0, serv.getMessage(serv.getResult(sipResult)));
				}
				
				if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
				{
					String voipResult = serv.doH248VoipSheet(obj);
					logger.warn("H248----"+voipResult);
					buffer.append(";\nH248语音工单处理结果："+serv.getMessage(serv.getResult(voipResult)));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getHvoipServTypeId()),Integer.valueOf(obj.getHvoipOperateId()), 
							"1".equals(serv.getResult(voipResult)) ? 1:0, serv.getMessage(serv.getResult(voipResult)));
				}
				
				if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
				{
					String netResult = serv.doNetSheetResult(obj);
					logger.warn("NET----"+netResult);
					buffer.append(";\n宽带业务工单处理结果："+serv.getMessage(serv.getResult(netResult)));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()), 
							"1".equals(serv.getResult(netResult)) ? 1:0, serv.getMessage(serv.getResult(netResult)));
				}
				
				if (!StringUtil.IsEmpty(obj.getWifiServTypeId()))
				{
					String wifiResult = serv.doWifiSheetResult(obj);
					logger.warn("wifi----"+wifiResult);
					buffer.append(";\n共享wifi业务工单处理结果："+serv.getMessage(serv.getResult(wifiResult)));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getWifiServTypeId()),Integer.valueOf(obj.getWifiOperateId()), 
							"1".equals(serv.getResult(wifiResult)) ? 1:0, serv.getMessage(serv.getResult(wifiResult)));
				}
				
				if (!StringUtil.IsEmpty(obj.getIptvServTypeId()))
				{
					String itvResult = serv.doItvSheetResult(obj);
					logger.warn("IPTV----"+itvResult);
					buffer.append(";\nITV业务工单处理结果："+serv.getMessage(serv.getResult(itvResult)));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getIptvServTypeId()),Integer.valueOf(obj.getIptvOperateId()), 
							"1".equals(serv.getResult(itvResult)) ? 1:0, serv.getMessage(serv.getResult(itvResult)));
				}

				if (!StringUtil.IsEmpty(obj.getCloudNetServTypeId()))
				{
					String cloudNetResult = serv.doCloudNetSheetResult(obj);
					logger.warn("cloudNet----"+cloudNetResult);
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap = JSON.parseObject(cloudNetResult, resultMap.getClass());
					buffer.append(";\n云网超宽带业务工单处理结果："+StringUtil.getStringValue(resultMap.get("RstMsg")));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getIptvServTypeId()),Integer.valueOf(obj.getIptvOperateId()),
							"0".equals(StringUtil.getStringValue(resultMap.get("RstCode"))) ? 1:0, StringUtil.getStringValue(resultMap.get("RstMsg")));
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
	
	public String queryNetParam(SheetObj obj) {
		return dao.queryNetParam(obj);
	}
	

	public String delH248VoipSheetResult(SheetObj sheetObj) {
		return serv.delH248VoipSheetResult(sheetObj);
	}

	public String delNetSheetResult(SheetObj sheetObj) {
		return serv.delNetSheetResult(sheetObj);
	}
	
	public String delWifiSheetResult(SheetObj sheetObj) {
		return serv.delWifiSheetResult(sheetObj);
	}

	public String delCloudNetSheetResult(SheetObj sheetObj) {
		return serv.delCloudNetSheetResult(sheetObj);
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
	
	
	public BssSheetByHand4GSDXDAO getDao(){
		return dao;
	}

	public void setDao(BssSheetByHand4GSDXDAO dao){
		this.dao = dao;
	}

	public BssSheetByHand4GSDXBIOServ getServ() {
		return serv;
	}

	public void setServ(BssSheetByHand4GSDXBIOServ serv) {
		this.serv = serv;
	}

	public static void main(String[] args) {
		String xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:dealOrderResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"com.ailk.module.gtms.soup\"><dealOrderReturn href=\"#id0\"/></ns1:dealOrderResponse><multiRef id=\"id0\" soapenc:root=\"0\" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"xsd:int\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">1</multiRef></soapenv:Body></soapenv:Envelope>";
		List xmlToList = XMLFormatUtil.xmlToList(xml);
		System.err.println(xmlToList);
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement(); 
			String  result = rootElt.element("Body").elementTextTrim("multiRef");
			System.out.println(result);
		}
		catch (Exception e)
		{
			logger.error("exception:"+e);
			e.printStackTrace();
		} 
	}
	public boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches())
		{
			return false;
		}
		return true;
	}

}
