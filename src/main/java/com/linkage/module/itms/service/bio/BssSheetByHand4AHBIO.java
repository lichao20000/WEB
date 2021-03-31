
package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.parser.Entity;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.EncryptionUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.service.dao.BssSheetByHand4AHDAO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4AHBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4AHBIO.class);
	private BssSheetByHand4AHDAO dao;

	public SheetObj checkLoid(String loid, String userType)
	{
		boolean result = dao.isLoidExists(loid, userType);
		if (result)
		{
			SheetObj obj = new SheetObj();
			obj.setUserType(userType);
			Map<String, String> userInfo = dao.getUserInfo(loid, userType);
			userInfoToObj(userInfo, obj, userType);
			List<HashMap<String, String>> list = dao.getServInfo(loid, userType);
			servInfoToObj(list, obj, userType);
			return obj;
		}
		else
		{
			// LOID不存在
			return null;
		}
	}

	public String check1(String loid, String userType)
	{
		boolean result = dao.isLoidExists(loid, userType);
		String test="1";
		if (result)
		{
			List<HashMap<String, String>> list = dao.getServInfo(loid, userType);
				for (HashMap<String, String> hashMap : list)
				{
					if ("14".equals(hashMap.get("serv_type_id")))
					{
						Map<String, String> paramInfo = dao.getVoipParaInfo(
								hashMap.get("user_id"), userType, "14");
						if (StringUtil.IsEmpty(paramInfo.get("voip_username")))
						{
							logger.warn("sip无值");
							test="-2";
						}
						else
						{
							test="2";
						}
					}
				}
				logger.warn(("test1"));
				return test;
			}
		else
		{
			test="-1";
			return test;
		}
	}

	public List<Map<String, String>> check(String loid, String userType)
	{
		List<Map<String, String>> listShow = null;
		boolean result = dao.isLoidExists(loid, userType);
		if (result)
		{
			List<HashMap<String, String>> list = dao.getServInfo(loid, userType);
			for (HashMap<String, String> hashMap : list)
			{
				// VOIP
				if ("14".equals(hashMap.get("serv_type_id")))
				{
					listShow = dao.getVoipPara(hashMap.get("user_id"), userType, "14");
				}
				else
				{
				}
			}
			return listShow;
			// servInfoToObj(list,userType);
		}
		else
		{
			// LOID不存在
			return null;
		}
	}

	public List<Map<String, String>> check(String gw_type)
	{
		return dao.check(gw_type);
	}

	/**
	 * @param list
	 * @param obj
	 * @param userType
	 */
	private void servInfoToObj(List<HashMap<String, String>> list, SheetObj obj,
			String userType)
	{
		for (HashMap<String, String> hashMap : list)
		{
			// 宽带
			if ("10".equals(hashMap.get("serv_type_id")))
			{
				obj.setNetServTypeId("22");
				obj.setNetOperateId("2");
				obj.setNetUsername(hashMap.get("username"));
				obj.setNetPasswd(hashMap.get("passwd"));
				obj.setNetVlanId(hashMap.get("vlanid"));
				obj.setNetWanType(hashMap.get("wan_type"));
				obj.setNetIpaddress(hashMap.get("ipaddress"));
				obj.setNetIpmask(hashMap.get("ipmask"));
				obj.setNetGateway(hashMap.get("gateway"));
				obj.setNetIpdns(hashMap.get("adsl_ser"));
				obj.setStandNetIpdns(hashMap.get("stand_dns"));
			}
			// VOIP
			if ("14".equals(hashMap.get("serv_type_id")))
			{
				Map<String, String> paramInfo = dao.getVoipParaInfo(
						hashMap.get("user_id"), userType, "14");
				/*List<Map<String, String>> param = dao.getVoipPara(hashMap.get("user_id"),
						userType, "14");
				List<String> list2 = new ArrayList();
				for (Map<String, String> map : param)
				{
					list2.add(StringUtil.getStringValue(map.get("line_id")));
				}
				list2.add(0, "请选择线路");*/
				// h248
				if (null != paramInfo && "2".equals(paramInfo.get("protocol")))
				{
					obj.setHvoipServTypeId("15");
					obj.setHvoipOperateId("2");
					obj.setHvoipPhone(paramInfo.get("voip_phone"));
					obj.setHvoipRegId(paramInfo.get("reg_id"));
					obj.setHvoipRegIdType(paramInfo.get("reg_id_type"));
					obj.setHvoipMgcIp(paramInfo.get("prox_serv"));
					obj.setHvoipMgcPort(paramInfo.get("prox_port"));
					obj.setHvoipStandMgcIp(paramInfo.get("stand_prox_serv"));
					obj.setHvoipStandMgcPort(paramInfo.get("stand_prox_port"));
					obj.setHvoipPort(getVoipProtStr(paramInfo.get("line_id"),
							paramInfo.get("protocol")));
					obj.setHvoipVlanId(hashMap.get("vlanid"));//
					obj.setHvoipWanType(hashMap.get("wan_type"));
					obj.setHvoipIpaddress(hashMap.get("ipaddress"));
					obj.setHvoipIpmask(hashMap.get("ipmask"));
					obj.setHvoipGateway(hashMap.get("gateway"));
					obj.setHvoipIpdns(hashMap.get("adsl_ser"));
				}
				// ims sip
				if (null != paramInfo
						&& ("0".equals(paramInfo.get("protocol")) || "1".equals(paramInfo
								.get("protocol"))))
				{
					List<String> show = new ArrayList();
					obj.setSipServTypeId("14");
					obj.setSipOperateId("2");
					obj.setSipVoipPhone(paramInfo.get("voip_phone"));// 电话
					obj.setSipVoipUsername(paramInfo.get("voip_username"));// 认证账号
					obj.setSipVoipPwd(paramInfo.get("voip_passwd"));// 认证密码
					obj.setSipProxServ(paramInfo.get("prox_serv"));// 主用代理服务器
					obj.setSipProxPort(paramInfo.get("prox_port"));// 主用代理服务器端口
					obj.setSipStandProxServ(paramInfo.get("stand_prox_serv"));// 备用代理服务器地址
					obj.setSipStandProxPort(paramInfo.get("stand_prox_port"));// 备用代理服务器端口
					if(userType.equals(2))
					{
						obj.setSipVoipPort(voipprotStr(paramInfo.get("line_id")));
					}
					else
					{
						obj.setSipVoipPort(getVoipProtStr(paramInfo.get("line_id"),paramInfo.get("protocol")));
					}
					// obj.setSipVoipPort(getVoipProtStr(paramInfo.get("VoipPort"),paramInfo.get("protocol")));
					obj.setSipRegiServ(paramInfo.get("regi_serv"));// 主用注册服务器
					obj.setSipRegiPort(paramInfo.get("regi_port"));// 主用注册服务器端口
					obj.setSipStandRegiServ(paramInfo.get("stand_regi_serv"));// 备用注册服务器地址
					obj.setSipStandRegiPort(paramInfo.get("stand_regi_port"));// 备用注册服务器端口
					obj.setSipOutBoundProxy(paramInfo.get("out_bound_proxy"));// 主用绑定服务器
					obj.setSipOutBoundPort(paramInfo.get("out_bound_port"));// 主用绑定服务器端口
					obj.setSipStandOutBoundProxy(paramInfo.get("stand_out_bound_proxy"));// 备用绑定服务器地址
					obj.setSipStandOutBoundPort(paramInfo.get("stand_out_bound_port"));// 备用绑定服务器端口
					obj.setSipProtocol(paramInfo.get("protocol"));// 协议类型
					obj.setSipVlanId(hashMap.get("vlanid"));// VLANID
					obj.setSipWanType(hashMap.get("wan_type"));// 上网方式:
					obj.setSipIpaddress(hashMap.get("ipaddress") == null ? "" : hashMap
							.get("ipaddress"));// IP地址：
					obj.setSipIpmask(hashMap.get("ipmask") == null ? "" : hashMap
							.get("ipmask"));// 掩码
					obj.setSipGateway(hashMap.get("gateway") == null ? "" : hashMap
							.get("gateway"));// 网关:
					obj.setSipIpdns(hashMap.get("adsl_ser") == null ? "" : hashMap
							.get("adsl_ser"));
					if ("0".equals(paramInfo.get("protocol")))
					{
						obj.setSipVoipUri(paramInfo.get("uri"));// url
						obj.setSipUserAgentDomain(paramInfo.get("user_agent_domain"));// UserAgentDomain
					}
				}
			}
			// IPTV
			if ("11".equals(hashMap.get("serv_type_id")))
			{
			}
		}
	}
	private String voipprotStr(String lineId)
	{
		String result = "-1";
		if ("1".equals(lineId))
		{
			result = "V1";
		}
		if ("2".equals(lineId))
		{
			result = "V2";
		}
		if ("3".equals(lineId))
		{
			result = "V3";
		}
		if ("4".equals(lineId))
		{
			result = "V4";
		}
		if ("5".equals(lineId))
		{
			result = "V5";
		}
		if ("6".equals(lineId))
		{
			result = "V6";
		}
		if ("7".equals(lineId))
		{
			result = "V7";
		}
		if ("8".equals(lineId))
		{
			result = "V8";
		}
		if ("9".equals(lineId))
		{
			result = "V9";
		}
		if ("10".equals(lineId))
		{
			result = "V10";
		}
		if ("11".equals(lineId))
		{
			result = "V11";
		}
		if ("12".equals(lineId))
		{
			result = "V12";
		}
		if ("13".equals(lineId))
		{
			result = "V13";
		}
		if ("14".equals(lineId))
		{
			result = "V14";
		}
		if ("15".equals(lineId))
		{
			result = "V15";
		}
		if ("16".equals(lineId))
		{
			result = "V16";
		}
		if ("17".equals(lineId))
		{
			result = "V17";
		}
		if ("18".equals(lineId))
		{
			result = "V18";
		}if ("19".equals(lineId))
		{
			result = "V19";
		}
		if ("20".equals(lineId))
		{
			result = "V20";
		}
		if ("21".equals(lineId))
		{
			result = "V21";
		}
		if ("22".equals(lineId))
		{
			result = "V22";
		}
		if ("23".equals(lineId))
		{
			result = "V23";
		}
		if ("24".equals(lineId))
		{
			result = "V24";
		}
		if ("25".equals(lineId))
		{
			result = "V25";
		}
		if ("26".equals(lineId))
		{
			result = "V26";
		}
		if ("27".equals(lineId))
		{
			result = "V27";
		}
		if ("28".equals(lineId))
		{
			result = "V28";
		}
		if ("29".equals(lineId))
		{
			result = "V29";
		}
		if ("30".equals(lineId))
		{
			result = "V30";
		}
		if ("31".equals(lineId))
		{
			result = "V31";
		}
		if ("32".equals(lineId))
		{
			result = "V32";
		}
		
		return result;
	}

	private String getVoipProtStr(String lineId, String voipType)
	{
		String result = "-1";
		if ("2".equals(voipType))
		{
			if ("1".equals(lineId))
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
			if ("1".equals(lineId))
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
		obj.setOfficeId(StringUtil.getStringValue(userInfo, "office_id", ""));
		obj.setAreaId(StringUtil.getStringValue(userInfo, "zone_id", ""));
		obj.setAccessStyle(StringUtil.getStringValue(userInfo, "access_style_id", ""));
		obj.setLinkman(StringUtil.getStringValue(userInfo, "linkman", ""));
		obj.setLinkPhone(StringUtil.getStringValue(userInfo, "linkphone", ""));
		obj.setEmail(StringUtil.getStringValue(userInfo, "email", ""));
		obj.setMobile(StringUtil.getStringValue(userInfo, "mobile", ""));
		obj.setLinkAddress(StringUtil.getStringValue(userInfo, "linkaddress", ""));
		obj.setLinkmanCredno(StringUtil.getStringValue(userInfo, "credno", ""));
		if ("2".equals(userType))
		{
			obj.setCustomerId(StringUtil.getStringValue(userInfo, "customer_id", ""));
			obj.setCustomerAccount(StringUtil.getStringValue(userInfo, "linkman", ""));
		}
	}

	public BssSheetByHand4AHDAO getDao()
	{
		return dao;
	}

	public void setDao(BssSheetByHand4AHDAO dao)
	{
		this.dao = dao;
	}

	public String deleteVoipPort(SheetObj obj, UserRes curUser)
	{
		logger.debug("bio.deleteVoipPort()");
		String resultMessage = this.delVoipPprt(obj);
		dao.addHandSheetLog(obj, curUser, 14, 3, 
				"000".equals(getMessage(resultMessage, "resultCode")) ? 1 : 0,
				getMessage(resultMessage, "resultDes"));
		//String SipVoipPort=obj.getSipVoipPort().replace("==","").replace("==","");
		if ("000".equals(getMessage(resultMessage, "resultCode")))
		{
			return "语音工单线路" +"删除成功";
		}
		else
		{
			return "语音工单线路"+ "删除失败；\n" + getMessage(resultMessage, "resultDes");
		}
	}

	public String doBusiness(SheetObj obj, UserRes curUser, String userType)
	{
		logger.debug("BssSheetByHand4AHBIO.doBusiness(SheetObj obj)");
		String resultMessage = doUserInfo(obj);
		StringBuffer buffer = new StringBuffer();
		buffer.append("资料工单处理结果：" + getMessage(resultMessage, "resultDes"));
		dao.addHandSheetLog(obj, curUser, Integer.valueOf(obj.getUserServTypeId()),
				Integer.valueOf(obj.getUserOperateId()), 
				"000".equals(getMessage(resultMessage, "resultCode")) ? 1 : 0,
				getMessage(resultMessage, "resultDes"));
		if ("000".equals(getMessage(resultMessage, "resultCode")))
		{
			try
			{
				Thread.sleep(1000);
				//if(StringUtil.IsEmpty(obj.getHvoipPhone()))
				if (!StringUtil.IsEmpty(obj.getSipServTypeId()))
				{
					String	sipResult = doSipVoip(obj);
						buffer.append(";\nSIP语音工单处理结果：" + getMessage(sipResult, "resultDes"));
						dao.addHandSheetLog(obj, curUser,
								Integer.valueOf(obj.getSipServTypeId()),
								Integer.valueOf(obj.getSipOperateId()),
								"000".equals(getMessage(sipResult, "resultCode")) ? 1 : 0,
								getMessage(sipResult, "resultDes"));
				}
				if (!StringUtil.IsEmpty(obj.getHvoipServTypeId()))
				{
					String voipResult = doH248VoipSheet(obj);
					buffer.append(";\nH248语音工单处理结果："
							+ getMessage(voipResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser,
							Integer.valueOf(obj.getHvoipServTypeId()),
							Integer.valueOf(obj.getHvoipOperateId()),
							"000".equals(getMessage(voipResult, "resultCode")) ? 1 : 0,
							getMessage(voipResult, "resultDes"));
				}
				if (!StringUtil.IsEmpty(obj.getNetServTypeId()))
				{
					String netResult = doNetSheetResult(obj);
					buffer.append(";\n宽带业务工单处理结果：" + getMessage(netResult, "resultDes"));
					dao.addHandSheetLog(obj, curUser,
							Integer.valueOf(obj.getNetServTypeId()),
							Integer.valueOf(obj.getNetOperateId()),
							"000".equals(getMessage(netResult, "resultCode")) ? 1 : 0,
							getMessage(netResult, "resultDes"));
				}
			}
			catch (Exception e)
			{
				logger.error("页面发送工单失败：" + e.getMessage());
			}
		}
		return buffer.toString();
	}

	private String getMessage(String xmlStr, String node)
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
			logger.error("exception:" + e.getMessage());
		}
		return result;
	}

	private String doNetSheetResult(SheetObj netSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(netSheet.getCmdId())
				.append("</cmdId >						\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(netSheet.getNetServTypeId())
				.append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(netSheet.getNetOperateId())
				.append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(netSheet.getDealDate())
				.append("</dealDate>				\n");
		inParam.append("		<userType>").append(netSheet.getUserType())
				.append("</userType>				\n");
		inParam.append("		<loid>").append(netSheet.getLoid()).append("</loid>						\n");
		inParam.append("		<username>").append(netSheet.getNetUsername())
				.append("</username>					\n");
		inParam.append("		<password>").append(netSheet.getNetPasswd())
				.append("</password>				\n");
		inParam.append("		<cityId>").append(netSheet.getCityId())
				.append("</cityId>					\n");
		inParam.append("		<vlanId>").append(netSheet.getNetVlanId())
				.append("</vlanId>			\n");
		inParam.append("		<wanType>").append(netSheet.getNetWanType())
				.append("</wanType>					\n");
		inParam.append("		<ipaddress>").append(netSheet.getNetIpaddress())
				.append("</ipaddress>				\n");
		inParam.append("		<ipmask>").append(netSheet.getNetIpmask())
				.append("</ipmask>						\n");
		inParam.append("		<gateway>").append(netSheet.getNetGateway())
				.append("</gateway>					\n");
		inParam.append("		<ipdns>").append(netSheet.getNetIpdns())
				.append("</ipdns>			\n");
		inParam.append("		<standIpdns>").append(netSheet.getStandNetIpdns())
				.append("</standIpdns>			\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.info("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String doH248VoipSheet(SheetObj voipSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(voipSheet.getCmdId())
				.append("</cmdId >						\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(voipSheet.getHvoipServTypeId())
				.append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(voipSheet.getHvoipOperateId())
				.append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(voipSheet.getDealDate())
				.append("</dealDate>		\n");
		inParam.append("		<userType>").append(voipSheet.getUserType())
				.append("</userType>		\n");
		inParam.append("		<loid>").append(voipSheet.getLoid()).append("</loid>		\n");
		inParam.append("		<voipPhone>").append(voipSheet.getHvoipPhone())
				.append("</voipPhone>		\n");
		inParam.append("		<cityId>").append(voipSheet.getCityId())
				.append("</cityId>		\n");
		inParam.append("		<regId>").append(voipSheet.getHvoipRegId())
				.append("</regId>		\n");
		inParam.append("		<regIdType>").append(voipSheet.getHvoipRegIdType())
				.append("</regIdType>		\n");
		inParam.append("		<mgcIp>").append(voipSheet.getHvoipMgcIp())
				.append("</mgcIp>		\n");
		inParam.append("		<mgcPort>").append(voipSheet.getHvoipMgcPort())
				.append("</mgcPort>		\n");
		inParam.append("		<standMgcIp>").append(voipSheet.getHvoipStandMgcIp())
				.append("</standMgcIp>		\n");
		inParam.append("		<standMgcPort>").append(voipSheet.getHvoipStandMgcPort())
				.append("</standMgcPort>		\n");
		inParam.append("		<voipPort>").append(voipSheet.getHvoipPort())
				.append("</voipPort>		\n");
		inParam.append("		<vlanId>").append(voipSheet.getHvoipVlanId())
				.append("</vlanId>		\n");
		inParam.append("		<wanType>").append(voipSheet.getHvoipWanType())
				.append("</wanType>		\n");
		inParam.append("		<ipaddress>").append(voipSheet.getHvoipIpaddress())
				.append("</ipaddress>		\n");
		inParam.append("		<ipmask>").append(voipSheet.getHvoipIpmask())
				.append("</ipmask>		\n");
		inParam.append("		<gateway>").append(voipSheet.getHvoipGateway())
				.append("</gateway>		\n");
		inParam.append("		<ipdns>").append(voipSheet.getHvoipIpdns())
				.append("</ipdns>		\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.info("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String doSipVoip(SheetObj sipVoipSheet)
	{
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(sipVoipSheet.getCmdId())
				.append("</cmdId >						\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(sipVoipSheet.getSipServTypeId())
				.append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sipVoipSheet.getSipOperateId())
				.append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sipVoipSheet.getDealDate())
				.append("</dealDate>		\n");
		inParam.append("		<userType>").append(sipVoipSheet.getUserType())
				.append("</userType>		\n");
		inParam.append("		<loid>").append(sipVoipSheet.getLoid()).append("</loid>		\n");
		inParam.append("		<voipPhone>").append(sipVoipSheet.getSipVoipPhone())
				.append("</voipPhone>		\n");
		inParam.append("		<cityId>").append(sipVoipSheet.getCityId())
				.append("</cityId>		\n");
		inParam.append("		<voipUsername>").append(sipVoipSheet.getSipVoipUsername())
				.append("</voipUsername>		\n");
		inParam.append("		<voipPwd>").append(sipVoipSheet.getSipVoipPwd())
				.append("</voipPwd>		\n");
		inParam.append("		<proxServ>").append(sipVoipSheet.getSipProxServ())
				.append("</proxServ>		\n");
		inParam.append("		<proxPort>").append(sipVoipSheet.getSipProxPort())
				.append("</proxPort>		\n");
		inParam.append("		<standProxServ>").append(sipVoipSheet.getSipStandProxServ())
				.append("</standProxServ>		\n");
		inParam.append("		<standProxPort>").append(sipVoipSheet.getSipStandProxPort())
				.append("</standProxPort>		\n");
		inParam.append("		<voipPort>").append(sipVoipSheet.getSipVoipPort())
				.append("</voipPort>		\n");
		inParam.append("		<regiServ>").append(sipVoipSheet.getSipRegiServ())
				.append("</regiServ>		\n");
		inParam.append("		<regiPort>").append(sipVoipSheet.getSipRegiPort())
				.append("</regiPort>		\n");
		inParam.append("		<standRegiServ>").append(sipVoipSheet.getSipStandRegiServ())
				.append("</standRegiServ>		\n");
		inParam.append("		<standRegiPort>").append(sipVoipSheet.getSipStandRegiPort())
				.append("</standRegiPort>		\n");
		inParam.append("		<outBoundProxy>").append(sipVoipSheet.getSipOutBoundProxy())
				.append("</outBoundProxy>		\n");
		inParam.append("		<outBoundPort>").append(sipVoipSheet.getSipOutBoundPort())
				.append("</outBoundPort>		\n");
		inParam.append("		<standOutBoundProxy>")
				.append(sipVoipSheet.getSipStandOutBoundProxy())
				.append("</standOutBoundProxy>		\n");
		inParam.append("		<standOutBoundPort>")
				.append(sipVoipSheet.getSipStandOutBoundPort())
				.append("</standOutBoundPort>		\n");
		inParam.append("		<protocol>").append(sipVoipSheet.getSipProtocol())
				.append("</protocol>		\n");
		inParam.append("		<vlanId>").append(sipVoipSheet.getSipVlanId())
				.append("</vlanId>		\n");
		inParam.append("		<wanType>").append(sipVoipSheet.getSipWanType())
				.append("</wanType>		\n");
		inParam.append("		<ipaddress>").append(sipVoipSheet.getSipIpaddress())
				.append("</ipaddress>		\n");
		inParam.append("		<ipmask>").append(sipVoipSheet.getSipIpmask())
				.append("</ipmask>		\n");
		inParam.append("		<gateway>").append(sipVoipSheet.getSipGateway())
				.append("</gateway>		\n");
		inParam.append("		<ipdns>").append(sipVoipSheet.getSipIpdns())
				.append("</ipdns>		\n");
		// ims 必须
		inParam.append("		<voipUri>").append(sipVoipSheet.getSipVoipUri())
				.append("</voipUri>		\n");
		inParam.append("		<userAgentDomain>")
				.append(sipVoipSheet.getSipUserAgentDomain())
				.append("</userAgentDomain>		\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.info("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String doSipVoip1(SheetObj sipVoipSheet)
	{
		logger.warn("doSipVoip1===========>");
		//logger.warn("line===========>" + line);
		// TODO Auto-generated method stub
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(sipVoipSheet.getCmdId())
				.append("</cmdId >						\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(sipVoipSheet.getSipServTypeId())
				.append("</servTypeId>				\n");
		//if (line != -1 && line != 1000)
		//{
		//	logger.warn("line==!1000");
			inParam.append("	<operateId>").append(1).append("</operateId>				\n");
	//	}
		/*if (lineId != 0 && lineId != -1 && lineId != 1000)
		{
			logger.warn("lineId==!1000");
			inParam.append("	<operateId>").append(2).append("</operateId>				\n");
		}
		else
		{
			inParam.append("	<operateId>").append(1).append("</operateId>				\n");
		}*/
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sipVoipSheet.getDealDate())
				.append("</dealDate>		\n");
		inParam.append("		<userType>").append(sipVoipSheet.getUserType())
				.append("</userType>		\n");
		inParam.append("		<loid>").append(sipVoipSheet.getLoid()).append("</loid>		\n");
		inParam.append("		<voipPhone>").append(sipVoipSheet.getSipVoipPhone())
				.append("</voipPhone>		\n");
		inParam.append("		<cityId>").append(sipVoipSheet.getCityId())
				.append("</cityId>		\n");
		inParam.append("		<voipUsername>").append(sipVoipSheet.getSipVoipUsername())
				.append("</voipUsername>		\n");
		inParam.append("		<voipPwd>").append(sipVoipSheet.getSipVoipPwd())
				.append("</voipPwd>		\n");
		inParam.append("		<proxServ>").append(sipVoipSheet.getSipProxServ())
				.append("</proxServ>		\n");
		inParam.append("		<proxPort>").append(sipVoipSheet.getSipProxPort())
				.append("</proxPort>		\n");
		inParam.append("		<standProxServ>").append(sipVoipSheet.getSipStandProxServ())
				.append("</standProxServ>		\n");
		inParam.append("		<standProxPort>").append(sipVoipSheet.getSipStandProxPort())
				.append("</standProxPort>		\n");
		inParam.append("		<voipPort>").append(sipVoipSheet.getSipVoipPort())
				.append("</voipPort>		\n");
		/*if (line != -1 && line != 1000)
		{
			inParam.append("		<lineId>").append(line).append("</lineId>		\n");
		}
		else
		{
			inParam.append("		<lineId>").append(lineId).append("</lineId>		\n");
		}*/
		inParam.append("		<regiServ>").append(sipVoipSheet.getSipRegiServ())
				.append("</regiServ>		\n");
		inParam.append("		<regiPort>").append(sipVoipSheet.getSipRegiPort())
				.append("</regiPort>		\n");
		inParam.append("		<standRegiServ>").append(sipVoipSheet.getSipStandRegiServ())
				.append("</standRegiServ>		\n");
		inParam.append("		<standRegiPort>").append(sipVoipSheet.getSipStandRegiPort())
				.append("</standRegiPort>		\n");
		inParam.append("		<outBoundProxy>").append(sipVoipSheet.getSipOutBoundProxy())
				.append("</outBoundProxy>		\n");
		inParam.append("		<outBoundPort>").append(sipVoipSheet.getSipOutBoundPort())
				.append("</outBoundPort>		\n");
		inParam.append("		<standOutBoundProxy>")
				.append(sipVoipSheet.getSipStandOutBoundProxy())
				.append("</standOutBoundProxy>		\n");
		inParam.append("		<standOutBoundPort>")
				.append(sipVoipSheet.getSipStandOutBoundPort())
				.append("</standOutBoundPort>		\n");
		inParam.append("		<protocol>").append(sipVoipSheet.getSipProtocol())
				.append("</protocol>		\n");
		inParam.append("		<vlanId>").append(sipVoipSheet.getSipVlanId())
				.append("</vlanId>		\n");
		inParam.append("		<wanType>").append(sipVoipSheet.getSipWanType())
				.append("</wanType>		\n");
		inParam.append("		<ipaddress>").append(sipVoipSheet.getSipIpaddress())
				.append("</ipaddress>		\n");
		inParam.append("		<ipmask>").append(sipVoipSheet.getSipIpmask())
				.append("</ipmask>		\n");
		inParam.append("		<gateway>").append(sipVoipSheet.getSipGateway())
				.append("</gateway>		\n");
		inParam.append("		<ipdns>").append(sipVoipSheet.getSipIpdns())
				.append("</ipdns>		\n");
		// ims 必须
		inParam.append("		<voipUri>").append(sipVoipSheet.getSipVoipUri())
				.append("</voipUri>		\n");
		inParam.append("		<userAgentDomain>")
				.append(sipVoipSheet.getSipUserAgentDomain())
				.append("</userAgentDomain>		\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.info("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String delVoipPprt(SheetObj voipObj)
	{
		logger.warn("voipObj.getSipVoipPort()====>"+voipObj.getSipVoipPort());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		inParam.append("<root>");
		inParam.append("<cmdId>").append(voipObj.getCmdId()).append("</cmdId>");
		inParam.append("<authUser>itms</authUser>");
		inParam.append("<authPwd>123</authPwd>");
		inParam.append("<servTypeId>14</servTypeId>");
		inParam.append("<operateId>3</operateId>");
		inParam.append("<param>");
		inParam.append("<dealDate>").append(voipObj.getDealDate()).append("</dealDate>");
		inParam.append("<userType>").append(voipObj.getUserType()).append("</userType>");
		inParam.append("<loid>").append(voipObj.getLoid()).append("</loid>");
		inParam.append("<cityId>").append(voipObj.getCityId()).append("</cityId>");
		inParam.append("<voipUsername>").append(voipObj.getSipVoipUsername())
				.append("</voipUsername>");
		inParam.append("<voipPort>").append(voipObj.getSipVoipPort())
				.append("</voipPort>");
		// inParam.append("<lineid>").append(lineId).append("</lineid>");
		inParam.append("</param>");
		inParam.append("</root>");
		logger.info("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String doUserInfo(SheetObj userInfoSheet)
	{
		logger.warn("doUserInfo====>方法");
		// TODO Auto-generated method stub
		logger.debug("doUserInfo(UserInfoSheet4AHOBJ userInfoSheet)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append(userInfoSheet.getCmdId()).append("</cmdId >\n");
		inParam.append("	<authUser>itms</authUser>					\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>").append(userInfoSheet.getUserServTypeId())
				.append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(userInfoSheet.getUserOperateId())
				.append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(userInfoSheet.getDealDate())
				.append("</dealDate>				\n");
		inParam.append("		<userType>").append(userInfoSheet.getUserType())
				.append("</userType>				\n");
		inParam.append("		<loid>").append(userInfoSheet.getLoid())
				.append("</loid>						\n");
		inParam.append("		<cityId>").append(userInfoSheet.getCityId())
				.append("</cityId>					\n");
		inParam.append("		<officeId>").append(userInfoSheet.getOfficeId())
				.append("</officeId>				\n");
		inParam.append("		<areaId>").append(userInfoSheet.getAreaId())
				.append("</areaId>					\n");
		inParam.append("		<accessStyle>").append(userInfoSheet.getAccessStyle())
				.append("</accessStyle>			\n");
		inParam.append("		<linkman>").append(userInfoSheet.getLinkman())
				.append("</linkman>					\n");
		inParam.append("		<linkPhone>").append(userInfoSheet.getLinkPhone())
				.append("</linkPhone>				\n");
		inParam.append("		<email>").append(userInfoSheet.getEmail())
				.append("</email>						\n");
		inParam.append("		<mobile>").append(userInfoSheet.getMobile())
				.append("</mobile>					\n");
		inParam.append("		<linkAddress>").append(userInfoSheet.getLinkAddress())
				.append("</linkAddress>			\n");
		inParam.append("		<linkmanCredno>").append(userInfoSheet.getLinkmanCredno())
				.append("</linkmanCredno>		\n");
		inParam.append("		<customerId>").append(userInfoSheet.getCustomerId())
				.append("</customerId>			\n");
		inParam.append("		<customerAccount>").append(userInfoSheet.getCustomerAccount())
				.append("</customerAccount>	\n");
		inParam.append("		<customerPwd>").append(userInfoSheet.getCustomerPwd())
				.append("</customerPwd>			\n");
		inParam.append("		<specId>").append(userInfoSheet.getSpecId())
				.append("</specId>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.info("xml:" + inParam.toString());
		String returnParam = "";
		try
		{
			// final String endPointReference =
			// "http://172.16.7.68:7070/EServer4WS/services/GtmsAxis";
			final String endPointReference = LipossGlobals
					.getLipossProperty("webServiceUri");
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		logger.warn("doUserInfo===出口==>");
		return returnParam;
	}

	public String checkCustomerId(String customerId)
	{
		boolean result = dao.isExistCustomer(customerId);
		if (result)
		{
			// 客户id存在
			return "-1";
		}
		else
		{
			return "000";
		}
	}

	public String getServInfo(String loid, String userType, String sipVoipPort)
	{
		Map<String, String> map = dao.getUserInfo(loid, userType);
		String userId = "";
		StringBuffer result = new StringBuffer();
		if (null != map && map.size() > 0)
		{
			userId = StringUtil.getStringValue(map, "user_id");
		}
		String lineId = String.valueOf(getLineId(sipVoipPort));
		logger.warn("getServInfo()=======sipVoipPort=="+sipVoipPort);
		List<HashMap<String, String>> list = dao.getServInfoByVoipPort(userId, userType,lineId,
				"14");
		if (null != list && list.size() > 0)
		{
			HashMap<String, String> paramMap = list.get(0);
			// 企业网管之前是h248协议
			if ("2".equals(userType))
			{
				/*
				 * result.append("2").append("|");
				 * result.append(paramMap.get("voip_phone")).append("|");
				 * result.append(paramMap.get("reg_id")).append("|");
				 * result.append(paramMap.get("reg_id_type")).append("|");
				 * result.append(paramMap.get("prox_serv")).append("|");
				 * result.append(paramMap.get("prox_port")).append("|");
				 * result.append(paramMap.get("stand_prox_serv")).append("|");
				 * result.append(paramMap.get("stand_prox_port")).append("|");
				 * result.append(paramMap.get("vlanid")).append("|");
				 * result.append(paramMap.get("wan_type")).append("|");
				 * result.append(paramMap.get("ipaddress")).append("|");
				 * result.append(paramMap.get("ipmask")).append("|");
				 * result.append(paramMap.get("gateway")).append("|");
				 * result.append(paramMap.get("adsl_ser")).append("|");
				 */
				result.append("2").append("|");
				result.append(paramMap.get("voip_phone")).append("|");
				result.append(paramMap.get("voip_username")).append("|");
				result.append(paramMap.get("voip_passwd")).append("|");
				result.append(paramMap.get("prox_serv")).append("|");
				result.append(paramMap.get("prox_port")).append("|");
				result.append(paramMap.get("stand_prox_serv")).append("|");
				result.append(paramMap.get("stand_prox_port")).append("|");// 7
				result.append(paramMap.get("regi_serv")).append("|");
				result.append(paramMap.get("regi_port")).append("|");
				result.append(paramMap.get("stand_regi_serv")).append("|");
				result.append(paramMap.get("stand_regi_port")).append("|");
				result.append(paramMap.get("out_bound_proxy")).append("|");
				result.append(paramMap.get("out_bound_port")).append("|");
				result.append(paramMap.get("stand_out_bound_proxy")).append("|");
				result.append(paramMap.get("stand_out_bound_port")).append("|");// 15
				result.append(paramMap.get("vlanid")).append("|");
				result.append(paramMap.get("wan_type")).append("|");
				result.append(paramMap.get("ipaddress")).append("|");
				result.append(paramMap.get("ipmask")).append("|");
				result.append(paramMap.get("gateway")).append("|");
				result.append(paramMap.get("adsl_ser")).append("|");
				result.append(paramMap.get("uri")).append("|");
				result.append(paramMap.get("user_agent_domain")).append("|");
				result.append(paramMap.get("protocol"));
			}
			else
			{
				result.append("2").append("|");
				result.append(paramMap.get("voip_phone")).append("|");
				result.append(paramMap.get("voip_username")).append("|");
				result.append(paramMap.get("voip_passwd")).append("|");
				result.append(paramMap.get("prox_serv")).append("|");
				result.append(paramMap.get("prox_port")).append("|");
				result.append(paramMap.get("stand_prox_serv")).append("|");
				result.append(paramMap.get("stand_prox_port")).append("|");// 7
				result.append(paramMap.get("regi_serv")).append("|");
				result.append(paramMap.get("regi_port")).append("|");
				result.append(paramMap.get("stand_regi_serv")).append("|");
				result.append(paramMap.get("stand_regi_port")).append("|");
				result.append(paramMap.get("out_bound_proxy")).append("|");
				result.append(paramMap.get("out_bound_port")).append("|");
				result.append(paramMap.get("stand_out_bound_proxy")).append("|");
				result.append(paramMap.get("stand_out_bound_port")).append("|");// 15
				result.append(paramMap.get("vlanid")).append("|");
				result.append(paramMap.get("wan_type")).append("|");
				result.append(paramMap.get("ipaddress")).append("|");
				result.append(paramMap.get("ipmask")).append("|");
				result.append(paramMap.get("gateway")).append("|");
				result.append(paramMap.get("adsl_ser")).append("|");
				result.append(paramMap.get("uri")).append("|");
				result.append(paramMap.get("user_agent_domain")).append("|");
				result.append(paramMap.get("protocol"));
			}
			logger.warn("result==========>" + result.toString());
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
		if ("V1".equals(linePort) || "A1".equals(linePort) || "USER001".equals(linePort))
		{
			lineId = 1;
		}
		else if ("V2".equals(linePort) || "A2".equals(linePort) || "USER002".equals(linePort))
		{
			lineId = 2;
		}
		else if ("V3".equals(linePort) || "A3".equals(linePort) || "USER003".equals(linePort))
		{
			lineId = 3;
		}
		else if ("V4".equals(linePort) || "A4".equals(linePort) || "USER004".equals(linePort))
		{
			lineId = 4;
		}
		else if ("V5".equals(linePort) || "A5".equals(linePort) || "USER005".equals(linePort))
		{
			lineId = 5;
		}
		else if ("V6".equals(linePort) || "A6".equals(linePort) || "USER006".equals(linePort))
		{
			lineId = 6;
		}
		else if ("V7".equals(linePort) || "A7".equals(linePort) || "USER007".equals(linePort))
		{
			lineId = 7;
		}
		else if ("V8".equals(linePort) || "A8".equals(linePort) || "USER008".equals(linePort))
		{
			lineId = 8;
		}
		else if ("V9".equals(linePort) || "A9".equals(linePort) || "USER009".equals(linePort))
		{
			lineId = 9;
		}
		else if ("V10".equals(linePort) || "A10".equals(linePort) || "USER010".equals(linePort))
		{
			lineId = 10;
		}
		else if ("V11".equals(linePort) || "A11".equals(linePort) || "USER011".equals(linePort))
		{
			lineId = 11;
		}
		else if ("V12".equals(linePort) || "A12".equals(linePort) || "USER012".equals(linePort))
		{
			lineId = 12;
		}
		else if ("V13".equals(linePort) || "A13".equals(linePort) || "USER013".equals(linePort))
		{
			lineId = 13;
		}
		else if ("V14".equals(linePort) || "A14".equals(linePort) || "USER014".equals(linePort))
		{
			lineId =14;
		}
		else if ("V15".equals(linePort) || "A15".equals(linePort) || "USER015".equals(linePort))
		{
			lineId = 15;
		}
		else if ("V16".equals(linePort) || "A16".equals(linePort) || "USER016".equals(linePort))
		{
			lineId = 16;
		}
		else if ("V17".equals(linePort) || "A17".equals(linePort) || "USER017".equals(linePort))
		{
			lineId = 17;
		}
		else if ("V18".equals(linePort) || "A18".equals(linePort) || "USER018".equals(linePort))
		{
			lineId = 18;
		}
		else if ("V19".equals(linePort) || "A19".equals(linePort) || "USER019".equals(linePort))
		{
			lineId = 19;
		}
		else if ("V20".equals(linePort) || "A20".equals(linePort) || "USER020".equals(linePort))
		{
			lineId = 20;
		}
		else if ("V21".equals(linePort) || "A21".equals(linePort) || "USER021".equals(linePort))
		{
			lineId = 21;
		}
		else if ("V22".equals(linePort) || "A22".equals(linePort) || "USER022".equals(linePort))
		{
			lineId = 22;
		}
		else if ("V23".equals(linePort) || "A23".equals(linePort) || "USER023".equals(linePort))
		{
			lineId =23;
		}
		else if ("V24".equals(linePort) || "A24".equals(linePort) || "USER024".equals(linePort))
		{
			lineId = 24;
		}
		else if ("V25".equals(linePort) || "A25".equals(linePort) || "USER025".equals(linePort))
		{
			lineId = 25;
		}
		else if ("V26".equals(linePort) || "A26".equals(linePort) || "USER026".equals(linePort))
		{
			lineId = 26;
		}
		else if ("V27".equals(linePort) || "A27".equals(linePort) || "USER027".equals(linePort))
		{
			lineId = 27;
		}
		else if ("V28".equals(linePort) || "A28".equals(linePort) || "USER028".equals(linePort))
		{
			lineId = 28;
		}
		else if ("V29".equals(linePort) || "A29".equals(linePort) || "USER029".equals(linePort))
		{
			lineId = 29;
		}
		else if ("V30".equals(linePort) || "A30".equals(linePort) || "USER030".equals(linePort))
		{
			lineId = 30;
		}
		else if ("V31".equals(linePort) || "A31".equals(linePort) || "USER031".equals(linePort))
		{
			lineId = 31;
		}
		else if ("V32".equals(linePort) || "A32".equals(linePort) || "USER032".equals(linePort))
		{
			lineId = 32;
		}
		return lineId;
	}
}
