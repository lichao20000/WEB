package com.linkage.module.itms.service.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.dao.BssSheetByHand4JXLTDAO;
import com.linkage.module.itms.service.obj.SheetObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BssSheetByHand4JXLTBIO 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4JXLTBIO.class);
	private BssSheetByHand4JXLTDAO dao ;
	private BssSheetByHand4JXLTBIOServ serv;
	
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
					obj.setNetPasswd(hashMap.get("passwd"));
				}
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
		StringBuffer buffer = new StringBuffer();
		try
		{
			Thread.sleep(1000);
			
			if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
			{

				String voipResult = "";
				if(Global.ZJLT.equals(Global.instAreaShortName))
				{
					voipResult = serv.doH248VoipSheetZjlt(obj);

				}
				else
				{
					voipResult = serv.doH248VoipSheet(obj);
				}
				logger.warn("H248----"+voipResult);
				buffer.append("H248语音工单处理结果："+serv.getMessage(serv.getResult(voipResult))+"\n");
				dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getHvoipServTypeId()),Integer.valueOf(obj.getHvoipOperateId()), 
						"1".equals(serv.getResult(voipResult)) ? 1:0, serv.getMessage(serv.getResult(voipResult)));
			}
			
			if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
			{

				String netResult = "";
				if(Global.ZJLT.equals(Global.instAreaShortName))
				{
					netResult = serv.doNetSheetResultZjlt(obj);

				}
				else
				{
					netResult = serv.doNetSheetResult(obj);
				}

				logger.warn("NET----"+netResult);
				buffer.append("宽带业务工单处理结果："+serv.getMessage(serv.getResult(netResult)));
				dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()), 
						"1".equals(serv.getResult(netResult)) ? 1:0, serv.getMessage(serv.getResult(netResult)));
			}
			
		}
		catch (Exception e)
		{
			logger.error("doBusiness 页面发送工单失败,err:" + e);
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String delH248VoipSheetResult(SheetObj sheetObj) {
		if(Global.ZJLT.equals(Global.instAreaShortName))
		{
			return serv.delH248VoipSheetResultZjlt(sheetObj);

		}
		else
		{
			return serv.delH248VoipSheetResult(sheetObj);
		}

	}

	public String delNetSheetResult(SheetObj sheetObj) {
		if(Global.ZJLT.equals(Global.instAreaShortName))
		{
			return serv.delNetSheetResultZjlt(sheetObj);

		}
		else
		{
			return serv.delNetSheetResult(sheetObj);
		}

	}
	
	public BssSheetByHand4JXLTDAO getDao(){
		return dao;
	}

	public void setDao(BssSheetByHand4JXLTDAO dao){
		this.dao = dao;
	}

	public BssSheetByHand4JXLTBIOServ getServ() {
		return serv;
	}

	public void setServ(BssSheetByHand4JXLTBIOServ serv) {
		this.serv = serv;
	}

}
