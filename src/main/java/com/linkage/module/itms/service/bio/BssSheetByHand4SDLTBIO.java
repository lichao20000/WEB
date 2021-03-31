package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.ArrayList;
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
//import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4SDLTDAO;
import com.linkage.module.itms.service.obj.SheetObj;


public class BssSheetByHand4SDLTBIO {
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4SDLTBIO.class);
	
	private BssSheetByHand4SDLTDAO dao ;
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");
	
	public List<HashMap<String, String>> getSpecId() {
		List<HashMap<String, String>> list= dao.getSpecId();
		List<HashMap<String, String>> lis=new ArrayList<HashMap<String, String>>();
		if(list!=null){
			for(Map<String, String> map:list){
				HashMap<String, String> m=new HashMap<String, String>();
				String lNum=StringUtil.getStringValue(map, "lan_num", "");
				String wNum=StringUtil.getStringValue(map, "wlan_num", "");
				String spec_name=StringUtil.getStringValue(map, "spec_name", "");
				String id=StringUtil.getStringValue(map, "id", "");
				
				m.put("spec_name", spec_name);
				m.put("spec",lNum+"|"+wNum+"|"+id+"("+spec_name+")");
				
				lis.add(m);
				m=null;
				map=null;
			}
		}
		return lis;
	}
	
	public SheetObj checkLoid(String loid,String userType,List<Map<String, String>> cityList)
	{
		logger.warn("checkLoid() loid="+loid+"  userType="+userType);
		String city_id=null;
		StringBuffer cityId=new StringBuffer("");
		if(cityList!=null){
			for(Map<String, String> map:cityList){
				cityId.append("'"+StringUtil.getStringValue(map, "city_id", "")).append("',");
			}
			city_id=cityId.deleteCharAt(cityId.length()-1).toString();
		}
		
		Map<String, String> userInfo = dao.getUserInfo(loid,city_id);
		SheetObj obj = null;
		if(userInfo != null)
		{
			Map<String, String> servInfo = dao.getServInfo(loid,city_id);
			obj = new SheetObj();
			obj.setUserType(userType);
			userInfoToObj(userInfo,servInfo,obj);
			
		}
		cityId = null;
		city_id = null;
		return obj;
	}
	
	private void userInfoToObj(Map<String,String> userInfo,Map<String,String> servInfo,SheetObj obj)
	{
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
		
		obj.setNetServTypeId("22");
		obj.setNetUsername(StringUtil.getStringValue(servInfo,"username",""));
//		obj.setNetPasswd(Base64.decode(StringUtil.getStringValue(servInfo,"passwd","")));
		obj.setNetPasswd(StringUtil.getStringValue(servInfo,"passwd",""));
		obj.setNetVlanId(StringUtil.getStringValue(servInfo,"vlanid",""));
		obj.setNetWanType(StringUtil.getStringValue(servInfo,"wan_type",""));
		obj.setNetIpaddress(StringUtil.getStringValue(servInfo,"ipaddress",""));
		obj.setNetGateway(StringUtil.getStringValue(servInfo,"gateway",""));
		obj.setNetIpdns(StringUtil.getStringValue(servInfo,"adsl_ser",""));
	}

	public String doBusiness(SheetObj obj, UserRes curUser){
		if("1".equals(obj.getUserOperateId())){
			//先发送用户资料工单，后发送宽带业务工单
			return sendUserinfoMsg(obj,curUser)+";\n"+sendNetSheetResultMsg(obj,curUser);
		}else{
			return sendNetSheetResultMsg(obj,curUser)+";\n"+sendUserinfoMsg(obj,curUser);
		}
	}
	
	/**
	 * 发送用户资料工单
	 * @param obj
	 * @param curUser
	 * @return
	 */
	public String sendUserinfoMsg(SheetObj obj, UserRes curUser){
		String resultMessage = doUserInfo(obj);
		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),Integer.valueOf(obj.getUserOperateId()),
				"000".equals(getMessage(resultMessage, "resultCode")) ? 1:0, getMessage(resultMessage, "resultDes"));
		return "资料工单处理结果："+getMessage(resultMessage, "resultDes");
	}
	
	/**
	 * 发送宽带工单
	 * @param obj
	 * @param curUser
	 * @return
	 */
	public String sendNetSheetResultMsg(SheetObj obj, UserRes curUser){
		String netResult = doNetSheetResult(obj);
		dao.addHandSheetLog(obj,curUser,Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()),
				"000".equals(getMessage(netResult, "resultCode")) ? 1:0,getMessage(netResult,"resultDes"));
		return "宽带业务工单处理结果："+getMessage(netResult, "resultDes");
		
	}
	
	
	
//	public String doBusiness(SheetObj obj, UserRes curUser)
//	{
//		String resultMessage = doUserInfo(obj);
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("资料工单处理结果："+getMessage(resultMessage, "resultDes"));
//		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),Integer.valueOf(obj.getUserOperateId()),
//				"000".equals(getMessage(resultMessage, "resultCode")) ? 1:0, getMessage(resultMessage, "resultDes"));
//
////		if ("000".equals(getMessage(resultMessage, "resultCode")))
////		{
//			try
//			{
//				Thread.sleep(1000);
//				
//				if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
//				{
//					String netResult = doNetSheetResult(obj);
//					buffer.append(";\n宽带业务工单处理结果："+getMessage(netResult, "resultDes"));
//					dao.addHandSheetLog(obj,curUser,Integer.valueOf(obj.getNetServTypeId()),Integer.valueOf(obj.getNetOperateId()),
//							"000".equals(getMessage(netResult, "resultCode")) ? 1:0,getMessage(netResult,"resultDes"));
//				}
//			}
//			catch (Exception e)
//			{
//				logger.error("页面发送工单失败：" + e.getMessage());
//			}
////		}
//		return buffer.toString();
//	}

	/**
	 * 用户资料工单
	 * @param userInfoSheet
	 * @return
	 */
	private String doUserInfo(SheetObj userInfoSheet)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(userInfoSheet.getCmdId()).append("</cmdId>\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(userInfoSheet.getUserServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(userInfoSheet.getUserOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(userInfoSheet.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(userInfoSheet.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(userInfoSheet.getLoid()).append("</loid>						\n");
		inParam.append("		<cityId>").append(userInfoSheet.getCityId()).append("</cityId>					\n");
		inParam.append("		<officeId>").append(userInfoSheet.getOfficeId()).append("</officeId>				\n");
		inParam.append("		<areaId>").append(userInfoSheet.getAreaId()).append("</areaId>					\n");
		inParam.append("		<accessStyle>").append(userInfoSheet.getAccessStyle()).append("</accessStyle>			\n");
		inParam.append("		<linkman>").append(userInfoSheet.getLinkman()).append("</linkman>					\n");
		inParam.append("		<linkPhone>").append(userInfoSheet.getLinkPhone()).append("</linkPhone>				\n");
		inParam.append("		<email>").append(userInfoSheet.getEmail()).append("</email>						\n");
		inParam.append("		<mobile>").append(userInfoSheet.getMobile()).append("</mobile>					\n");
		inParam.append("		<linkAddress>").append(userInfoSheet.getLinkAddress()).append("</linkAddress>			\n");
		inParam.append("		<linkmanCredno>").append(userInfoSheet.getLinkmanCredno()).append("</linkmanCredno>		\n");
		inParam.append("		<customerId>").append(userInfoSheet.getCustomerId()).append("</customerId>			\n");
		inParam.append("		<customerAccount>").append(userInfoSheet.getCustomerAccount()).append("</customerAccount>	\n");
		inParam.append("		<customerPwd>").append(userInfoSheet.getCustomerPwd()).append("</customerPwd>			\n");
		inParam.append("		<specId>").append(userInfoSheet.getSpecId()).append("</specId>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("用户资料工单 xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			
		}
		catch (Exception e)
		{
			logger.error("用户资料工单err:"+e.getMessage());
		}
		logger.warn("调用用户资料工单接口的结果："+returnParam);
		return returnParam;
	}
	
	/**
	 * 宽带业务工单
	 * @param netSheet
	 * @return
	 */
	private String doNetSheetResult(SheetObj netSheet)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(netSheet.getCmdId()).append("</cmdId>						\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(netSheet.getNetServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(netSheet.getNetOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(netSheet.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(netSheet.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(netSheet.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(netSheet.getNetUsername()).append("</userName>					\n");
		inParam.append("		<password>").append(netSheet.getNetPasswd()).append("</password>				\n");
		inParam.append("		<cityId>").append(netSheet.getCityId()).append("</cityId>					\n");
		inParam.append("		<vlanId>").append(netSheet.getNetVlanId()).append("</vlanId>			\n");
		inParam.append("		<wanType>").append(netSheet.getNetWanType()).append("</wanType>					\n");
		inParam.append("		<ipaddress>").append(netSheet.getNetIpaddress()).append("</ipaddress>				\n");
		inParam.append("		<ipmask>").append(netSheet.getNetIpmask()).append("</ipmask>						\n");
		inParam.append("		<gateway>").append(netSheet.getNetGateway()).append("</gateway>					\n");
		inParam.append("		<ipdns>").append(netSheet.getNetIpdns()).append("</ipdns>			\n");
		inParam.append("		<bindPort>").append(netSheet.getNetPort()).append("</bindPort>			\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("宽带工单 xml:"+inParam.toString());
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			
		}
		catch (Exception e)
		{	
			logger.error("宽带工单结果err:"+e.getMessage());
		}
		logger.warn("调用宽带工单接口的结果："+returnParam);
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
			logger.error("getMessage()--exception:"+e.getMessage());
		} 
		return result;
	}

	public BssSheetByHand4SDLTDAO getDao()
	{
		return dao;
	}
	
	public void setDao(BssSheetByHand4SDLTDAO dao)
	{
		this.dao = dao;
	}

}
