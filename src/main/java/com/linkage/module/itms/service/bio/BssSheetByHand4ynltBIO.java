package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4HBDAO;
import com.linkage.module.itms.service.dao.BssSheetByHand4ynltDAO;
import com.linkage.module.itms.service.obj.SheetObj;


public class BssSheetByHand4ynltBIO {
	
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4ynltBIO.class);
	
	private BssSheetByHand4ynltDAO dao ;
	
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");

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
			}
			//VOIP
			if("14".equals(hashMap.get("serv_type_id")))
			{
				Map<String, String> paramInfo = dao.getVoipParaInfo(hashMap.get("user_id"), userType,"14");
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
					obj.setDeviceName(StringUtil.getStringValue(hashMap,"device_name",""));
					obj.setDscpMark(StringUtil.getStringValue(hashMap, "dscp_mark", ""));
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
		}
		
	}

	/**





				DS-Lite	4


	 * @author zhangsm(67310)

	 * @param ipType
	 * @return
	 */
	private String getIpTypeReverse(String ipType)
	{
		String result = "0";

		if(!StringUtil.IsEmpty(ipType))
		{
			
			if ("1".equals(ipType))
			{
				result = "0"; 
			}
			if("3".equals(ipType))
			{
				result = "1";
			}
			if("2".equals(ipType))
			{
				result = "5";
			}
		}

		return result;
	}

	private String getVoipProtStr(String lineId, String voipType)
	{
		String result = "-1";
		if("2".equals(voipType))
		{
			if("1".equals(lineId))
			{
				result = "A1";
			}
			else if ("2".equals(lineId)) 
			{
				result = "A2"; 
			}
			else if ("3".equals(lineId)) 
			{
				result = "A3"; 
			}
			else if ("4".equals(lineId)) 
			{
				result = "A4"; 
			}
			else if ("5".equals(lineId)) 
			{
				result = "A5"; 
			}
			else if ("6".equals(lineId)) 
			{
				result = "A6"; 
			}
			else if ("7".equals(lineId)) 
			{
				result = "A7"; 
			}
			else if ("8".equals(lineId)) 
			{
				result = "A8"; 
			}
		}
		else 
		{
			if("1".equals(lineId))
			{
				result = "V1";
			}
			else if ("2".equals(lineId)) 
			{
				result = "V2"; 
			}
		}
		return result;
	}

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
		obj.setSpecId(StringUtil.getStringValue(userInfo,"spec_name",""));
		if ("2".equals(userType))
		{
			obj.setCustomerId(StringUtil.getStringValue(userInfo,"customer_id",""));
			obj.setCustomerAccount(StringUtil.getStringValue(userInfo,"linkman",""));
		}
		
	}

	
	public BssSheetByHand4ynltDAO getDao()
	{
		return dao;
	}


	
	public void setDao(BssSheetByHand4ynltDAO dao)
	{  
		this.dao = dao;
	}


	public String doBusiness(SheetObj obj, UserRes curUser)
	{
		logger.debug("BssSheetByHand4AHBIO.doBusiness(SheetObj obj)");
		String resultMessage = doUserInfo(obj);
		StringBuffer buffer = new StringBuffer();
		buffer.append("资料工单处理结果："+getMessage(resultMessage, "resultDes"));
		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),Integer.valueOf(obj.getUserOperateId()), "000".equals(getMessage(resultMessage, "resultCode")) ? 1:0, getMessage(resultMessage, "resultDes"));
		if ("000".equals(getMessage(resultMessage, "resultCode")))
		{
			try
			{
				Thread.sleep(1000);
				if (!StringUtil.IsEmpty(obj.getSipServTypeId()))
				{
					String sipResult = doSipVoip(obj);
					buffer.append(";SIP语音工单处理结果："+getMessage(sipResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getSipServTypeId()),Integer.valueOf(obj.getSipOperateId()), 
							"000".equals(getMessage(sipResult, "resultCode")) ? 1:0, getMessage(sipResult, "resultDes"));
				}
				if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
				{
					String voipResult = doH248VoipSheet(obj);
					buffer.append(";H248语音工单处理结果："+getMessage(voipResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getHvoipServTypeId()),Integer.valueOf(obj.getHvoipOperateId()), 
							"000".equals(getMessage(voipResult, "resultCode")) ? 1:0, getMessage(voipResult, "resultDes"));
				}
				if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
				{
					String netResult = doNetSheetResult(obj);
					buffer.append(";宽带业务工单处理结果："+getMessage(netResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()), 
							"000".equals(getMessage(netResult, "resultCode")) ? 1:0, getMessage(netResult, "resultDes"));
				}
				if (!StringUtil.IsEmpty(obj.getIptvServTypeId()))
				{
					String itvResult = doItvSheetResult(obj);
					buffer.append(";ITV业务工单处理结果："+getMessage(itvResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()), 
							"000".equals(getMessage(itvResult, "resultCode")) ? 1:0, getMessage(itvResult, "resultDes"));
				}
			}
			catch (Exception e)
			{
				logger.error("页面发送工单失败：" + e.getMessage());
			}
		}
		return buffer.toString();
	}
	
	private String doItvSheetResult(SheetObj itvSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	");
		inParam.append("<root>										");
		inParam.append("	<cmdId>").append(itvSheet.getCmdId()).append("</cmdId >						");
		inParam.append("	<authUser>itms</authUser>					");
		inParam.append("	<authPwd>123</authPwd>						");
		inParam.append("	<servTypeId>").append(itvSheet.getIptvServTypeId()).append("</servTypeId>				");
		inParam.append("	<operateId>").append(itvSheet.getIptvOperateId()).append("</operateId>				");
		inParam.append("	<param>									");
		inParam.append("		<dealDate>").append(itvSheet.getDealDate()).append("</dealDate>				");
		inParam.append("		<userType>").append(itvSheet.getUserType()).append("</userType>				");
		inParam.append("		<loid>").append(itvSheet.getLoid()).append("</loid>						");
		inParam.append("		<userName>").append(itvSheet.getIptvUserName()).append("</userName>					");
		inParam.append("		<cityId>").append(itvSheet.getCityId()).append("</cityId>					");
		inParam.append("		<vlanId>").append(itvSheet.getIptvVlanId()).append("</vlanId>			");
		inParam.append("		<iptvNum>").append(itvSheet.getIptvNum()).append("</iptvNum>					");
		inParam.append("		<iptvPort>").append(itvSheet.getIptvPort()).append("</iptvPort>				");
		inParam.append("	</param>								");
		inParam.append("</root>										");
		logger.info("xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果："+returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:"+e.getMessage());
		}
		return returnParam;

	}

	private String getMessage(String xmlStr,String node)
	{
		String result = "";
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(xmlStr);
			Element rootElt = doc.getRootElement(); 
			result = rootElt.elementTextTrim(node);
		}
		catch (Exception e)
		{
			logger.error("exception:"+e.getMessage());
		} 
		return result;
	}

	private String doNetSheetResult(SheetObj netSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	");
		inParam.append("<root>										");
		inParam.append("	<cmdId>").append(netSheet.getCmdId()).append("</cmdId >						");
		inParam.append("	<authUser>itms</authUser>					");
		inParam.append("	<authPwd>123</authPwd>						");
		inParam.append("	<servTypeId>").append(netSheet.getNetServTypeId()).append("</servTypeId>				");
		inParam.append("	<operateId>").append(netSheet.getNetOperateId()).append("</operateId>				");
		inParam.append("	<param>									");
		inParam.append("		<dealDate>").append(netSheet.getDealDate()).append("</dealDate>				");
		inParam.append("		<userType>").append(netSheet.getUserType()).append("</userType>				");
		inParam.append("		<loid>").append(netSheet.getLoid()).append("</loid>						");
		inParam.append("		<userName>").append(netSheet.getNetUsername()).append("</userName>					");
		inParam.append("		<password>").append(netSheet.getNetPasswd()).append("</password>				");
		inParam.append("		<cityId>").append(netSheet.getCityId()).append("</cityId>					");
		inParam.append("		<vlanId>").append(netSheet.getNetVlanId()).append("</vlanId>			");
		inParam.append("		<wanType>").append(netSheet.getNetWanType()).append("</wanType>					");
		inParam.append("		<ipaddress>").append(netSheet.getNetIpaddress()).append("</ipaddress>				");
		inParam.append("		<ipmask>").append(netSheet.getNetIpmask()).append("</ipmask>						");
		inParam.append("		<gateway>").append(netSheet.getNetGateway()).append("</gateway>					");
		inParam.append("		<ipdns>").append(netSheet.getNetIpdns()).append("</ipdns>			");
		inParam.append("		<ipType>").append(netSheet.getStandNetIpdns()).append("</ipType>			");
		inParam.append("	</param>								");
		inParam.append("</root>										");
		logger.info("xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果："+returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:"+e.getMessage());
		}
		return returnParam;

	}


	private String doH248VoipSheet(SheetObj voipSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	");
		inParam.append("<root>										");
		inParam.append("	<cmdId>").append(voipSheet.getCmdId()).append("</cmdId >						");
		inParam.append("	<authUser>itms</authUser>					");
		inParam.append("	<authPwd>123</authPwd>						");
		inParam.append("	<servTypeId>").append(voipSheet.getHvoipServTypeId()).append("</servTypeId>				");
		inParam.append("	<operateId>").append(voipSheet.getHvoipOperateId()).append("</operateId>				");
		inParam.append("	<param>									");
		inParam.append("		<dealDate>").append(voipSheet.getDealDate()).append("</dealDate>		");
		inParam.append("		<userType>").append(voipSheet.getUserType()).append("</userType>		");
		inParam.append("		<loid>").append(voipSheet.getLoid()).append("</loid>		");
		inParam.append("		<voipPhone>").append(voipSheet.getHvoipPhone()).append("</voipPhone>		");
		inParam.append("		<cityId>").append(voipSheet.getCityId()).append("</cityId>		");
		inParam.append("		<regId>").append(voipSheet.getHvoipRegId()).append("</regId>		");
		inParam.append("		<regIdType>").append(voipSheet.getHvoipRegIdType()).append("</regIdType>		");
		inParam.append("		<mgcIp>").append(voipSheet.getHvoipMgcIp()).append("</mgcIp>		");
		inParam.append("		<mgcPort>").append(voipSheet.getHvoipMgcPort()).append("</mgcPort>		");
		inParam.append("		<standMgcIp>").append(voipSheet.getHvoipStandMgcIp()).append("</standMgcIp>		");
		inParam.append("		<standMgcPort>").append(voipSheet.getHvoipStandMgcPort()).append("</standMgcPort>		");
		inParam.append("		<voipPort>").append(voipSheet.getHvoipPort()).append("</voipPort>		");
		inParam.append("		<vlanId>").append(voipSheet.getHvoipVlanId()).append("</vlanId>		");
		inParam.append("		<wanType>").append(voipSheet.getHvoipWanType()).append("</wanType>		");
		inParam.append("		<ipaddress>").append(voipSheet.getHvoipIpaddress()).append("</ipaddress>		");
		inParam.append("		<ipmask>").append(voipSheet.getHvoipIpmask()).append("</ipmask>		");
		inParam.append("		<gateway>").append(voipSheet.getHvoipGateway()).append("</gateway>		");
		inParam.append("		<ipdns>").append(voipSheet.getHvoipIpdns()).append("</ipdns>		");
		inParam.append("		<deviceName>").append(voipSheet.getDeviceName()).append("</deviceName>		");
		inParam.append("		<dscpMark>").append(voipSheet.getDscpMark()).append("</dscpMark>		");
		inParam.append("	</param>								");
		inParam.append("</root>										");
		logger.info("xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果："+returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:"+e.getMessage());
		}
		return returnParam;
	}


	private String doSipVoip(SheetObj sipVoipSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	");
		inParam.append("<root>										");
		inParam.append("	<cmdId>").append(sipVoipSheet.getCmdId()).append("</cmdId >						");
		inParam.append("	<authUser>itms</authUser>					");
		inParam.append("	<authPwd>123</authPwd>						");
		inParam.append("	<servTypeId>").append(sipVoipSheet.getSipServTypeId()).append("</servTypeId>				");
		inParam.append("	<operateId>").append(sipVoipSheet.getSipOperateId()).append("</operateId>				");
		inParam.append("	<param>									");
		inParam.append("		<dealDate>").append(sipVoipSheet.getDealDate()).append("</dealDate>		");
		inParam.append("		<userType>").append(sipVoipSheet.getUserType()).append("</userType>		");
		inParam.append("		<loid>").append(sipVoipSheet.getLoid()).append("</loid>		");
		inParam.append("		<voipPhone>").append(sipVoipSheet.getSipVoipPhone()).append("</voipPhone>		");
		inParam.append("		<cityId>").append(sipVoipSheet.getCityId()).append("</cityId>		");
		inParam.append("		<voipUsername>").append(sipVoipSheet.getSipVoipUsername()).append("</voipUsername>		");
		inParam.append("		<voipPwd>").append(sipVoipSheet.getSipVoipPwd()).append("</voipPwd>		");
		inParam.append("		<proxServ>").append(sipVoipSheet.getSipProxServ()).append("</proxServ>		");
		inParam.append("		<proxPort>").append(sipVoipSheet.getSipProxPort()).append("</proxPort>		");
		inParam.append("		<standProxServ>").append(sipVoipSheet.getSipStandProxServ()).append("</standProxServ>		");
		inParam.append("		<standProxPort>").append(sipVoipSheet.getSipStandProxPort()).append("</standProxPort>		");
		
		inParam.append("		<voipPort>").append(sipVoipSheet.getSipVoipPort()).append("</voipPort>		");
		inParam.append("		<regiServ>").append(sipVoipSheet.getSipRegiServ()).append("</regiServ>		");
		inParam.append("		<regiPort>").append(sipVoipSheet.getSipRegiPort()).append("</regiPort>		");
		inParam.append("		<standRegiServ>").append(sipVoipSheet.getSipStandRegiServ()).append("</standRegiServ>		");
		inParam.append("		<standRegiPort>").append(sipVoipSheet.getSipStandRegiPort()).append("</standRegiPort>		");
		inParam.append("		<outBoundProxy>").append(sipVoipSheet.getSipOutBoundProxy()).append("</outBoundProxy>		");
		inParam.append("		<outBoundPort>").append(sipVoipSheet.getSipOutBoundPort()).append("</outBoundPort>		");
		inParam.append("		<standOutBoundProxy>").append(sipVoipSheet.getSipStandOutBoundProxy()).append("</standOutBoundProxy>		");
		inParam.append("		<standOutBoundPort>").append(sipVoipSheet.getSipStandOutBoundPort()).append("</standOutBoundPort>		");
		
		inParam.append("		<protocol>").append(sipVoipSheet.getSipProtocol()).append("</protocol>		");
		inParam.append("		<vlanId>").append(sipVoipSheet.getSipVlanId()).append("</vlanId>		");
		inParam.append("		<wanType>").append(sipVoipSheet.getSipWanType()).append("</wanType>		");
		inParam.append("		<ipaddress>").append(sipVoipSheet.getSipIpaddress()).append("</ipaddress>		");
		inParam.append("		<ipmask>").append(sipVoipSheet.getSipIpmask()).append("</ipmask>		");
		inParam.append("		<gateway>").append(sipVoipSheet.getSipGateway()).append("</gateway>		");
		inParam.append("		<ipdns>").append(sipVoipSheet.getSipIpdns()).append("</ipdns>		");
		//ims 必须
		inParam.append("		<voipUri>").append(sipVoipSheet.getSipVoipUri()).append("</voipUri>		");
		inParam.append("		<userAgentDomain>").append(sipVoipSheet.getSipUserAgentDomain()).append("</userAgentDomain>		");
		
		inParam.append("	</param>								");
		inParam.append("</root>										");
		logger.warn("xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.info("结果："+returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:"+e.getMessage());
		}
		return returnParam;
	}


	private String doUserInfo(SheetObj userInfoSheet)
	{
		// TODO Auto-generated method stub
		logger.debug("doUserInfo(UserInfoSheet4AHOBJ userInfoSheet)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	");
		inParam.append("<root>										");
		inParam.append("	<cmdId>").append(userInfoSheet.getCmdId()).append("</cmdId >");
		inParam.append("	<authUser>itms</authUser>					");
		inParam.append("	<authPwd>123</authPwd>						");
		inParam.append("	<servTypeId>").append(userInfoSheet.getUserServTypeId()).append("</servTypeId>				");
		inParam.append("	<operateId>").append(userInfoSheet.getUserOperateId()).append("</operateId>				");
		inParam.append("	<param>									");
		inParam.append("		<dealDate>").append(userInfoSheet.getDealDate()).append("</dealDate>				");
		inParam.append("		<userType>").append(userInfoSheet.getUserType()).append("</userType>				");
		inParam.append("		<loid>").append(userInfoSheet.getLoid()).append("</loid>						");
		inParam.append("		<cityId>").append(userInfoSheet.getCityId()).append("</cityId>					");
		inParam.append("		<officeId>").append(userInfoSheet.getOfficeId()).append("</officeId>				");
		inParam.append("		<areaId>").append(userInfoSheet.getAreaId()).append("</areaId>					");
		inParam.append("		<accessStyle>").append(userInfoSheet.getAccessStyle()).append("</accessStyle>			");
		inParam.append("		<linkman>").append(userInfoSheet.getLinkman()).append("</linkman>					");
		inParam.append("		<linkPhone>").append(userInfoSheet.getLinkPhone()).append("</linkPhone>				");
		inParam.append("		<email>").append(userInfoSheet.getEmail()).append("</email>						");
		inParam.append("		<mobile>").append(userInfoSheet.getMobile()).append("</mobile>					");
		inParam.append("		<linkAddress>").append(userInfoSheet.getLinkAddress()).append("</linkAddress>			");
		inParam.append("		<linkmanCredno>").append(userInfoSheet.getLinkmanCredno()).append("</linkmanCredno>		");
		inParam.append("		<customerId>").append(userInfoSheet.getCustomerId()).append("</customerId>			");
		inParam.append("		<customerAccount>").append(userInfoSheet.getCustomerAccount()).append("</customerAccount>	");
		inParam.append("		<customerPwd>").append(userInfoSheet.getCustomerPwd()).append("</customerPwd>			");
		inParam.append("		<specId>").append(userInfoSheet.getSpecId()).append("</specId>					");
		inParam.append("	</param>								");
		inParam.append("</root>										");
		logger.info("xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果："+returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:"+e.getMessage());
		}
		return returnParam;
	}


	public String checkCustomerId(String customerId)
	{
		boolean result = dao.isExistCustomer(customerId);
		if(result)
		{
			//客户id存在
			return "-1";
		}
		else
		{
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
		
		List<HashMap<String, String>> list = dao.getServInfoByVoipPort(userId,userType,sipVoipPort,"14");
		if(null != list && list.size()>0)
		{
			HashMap<String, String> paramMap = list.get(0);

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
				result.append(StringUtil.getStringValue(paramMap, "device_name", "")).append("|");
				result.append(StringUtil.getStringValue(paramMap, "dscp_mark", "")).append("|");
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
			lineId = Integer.valueOf(linePort.substring(linePort.length() - 1, linePort
					.length())) + 1;
		}
		return lineId;
	}

	public List<Map<String, String>> getSpec()
	{
		
		return dao.getSpec();
	}
	
//	public int getLineId(String linePort)
//	{
//		logger.debug("getLineId({})", linePort);
//		int lineId = 1;
//		if ("V1".equals(linePort) || "A1".equals(linePort) || "USER001".equals(linePort))
//		{
//			lineId = 1;
//		}
//		else if ("V2".equals(linePort) || "A2".equals(linePort) || "USER002".equals(linePort))
//		{
//			lineId = 2;
//		}
//		else if ("V3".equals(linePort) || "A3".equals(linePort) || "USER003".equals(linePort))
//		{
//			lineId = 3;
//		}
//		else if ("V4".equals(linePort) || "A4".equals(linePort) || "USER004".equals(linePort))
//		{
//			lineId = 4;
//		}
//		else if ("V5".equals(linePort) || "A5".equals(linePort) || "USER005".equals(linePort))
//		{
//			lineId = 5;
//		}
//		else if ("V6".equals(linePort) || "A6".equals(linePort) || "USER006".equals(linePort))
//		{
//			lineId = 6;
//		}
//		else if ("V7".equals(linePort) || "A7".equals(linePort) || "USER007".equals(linePort))
//		{
//			lineId = 7;
//		}
//		else if ("V8".equals(linePort) || "A8".equals(linePort) || "USER008".equals(linePort))
//		{
//			lineId = 8;
//		}
//		return lineId;
//	}
}
