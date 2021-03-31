
package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.obj.Order;
import com.linkage.module.itms.service.obj.SheetObj;

/**
 * 工单处理类
 */
public class BssSheetByHand4HBBIOServ
{

	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4HBBIOServ.class);
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");

	/**
	 * 开通iptv工单
	 */
	public String doItvSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getIptvServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getIptvOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getIptvUserName()).append("</userName>					\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<vlanId>").append(sheetObj.getIptvVlanId()).append("</vlanId>			\n");
		inParam.append("		<iptvNum>").append(sheetObj.getIptvNum()).append("</iptvNum>					\n");
		inParam.append("		<iptvPort>").append(sheetObj.getIptvPort()).append("</iptvPort>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "iptv-Z");
		}
		return callService("doItvSheetResult", inParam.toString(), false);
	}

	/**
	 * 销户IPTV工单
	 */
	public String delIptvSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getIptvServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getIptvOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getIptvUserName()).append("</userName>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<iptvPort>").append(sheetObj.getIptvPort()).append("</iptvPort>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "iptv-C");
		}
		return callService("销户IPTV工单", inParam.toString(), true);
	}

	/**
	 * 开通vpdn新装工单
	 */
	public String doVpdnSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getVpdnServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getVpdnOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getVpdnUserName()).append("</userName>					\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<iptvNum>").append(sheetObj.getVpdnNum()).append("</iptvNum>					\n");
		inParam.append("		<iptvPort>").append(sheetObj.getVpdnPort()).append("</iptvPort>				\n");
		inParam.append("		<vlanId>").append(sheetObj.getVpdnVlanId()).append("</vlanId>			\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		return callService("doVpdnSheetResult", inParam.toString(), false);
	}

	/**
	 * Vpdn销户工单
	 */
	public String delVpdnSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getVpdnServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getVpdnOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getVpdnUserName()).append("</userName>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<iptvPort>").append(sheetObj.getVpdnPort()).append("</iptvPort>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		return callService("VPDN销户工单", inParam.toString(), true);
	}

	/**
	 * 开通宽带工单
	 */
	public String doNetSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getNetServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getNetOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getNetUsername()).append("</userName>					\n");
		inParam.append("		<password>").append(sheetObj.getNetPasswd()).append("</password>				\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<vlanId>").append(sheetObj.getNetVlanId()).append("</vlanId>			\n");
		inParam.append("		<wanType>").append(sheetObj.getNetWanType()).append("</wanType>					\n");
		inParam.append("		<ipaddress>").append(sheetObj.getNetIpaddress()).append("</ipaddress>				\n");
		inParam.append("		<ipmask>").append(sheetObj.getNetIpmask()).append("</ipmask>						\n");
		inParam.append("		<gateway>").append(sheetObj.getNetGateway()).append("</gateway>					\n");
		inParam.append("		<ipdns>").append(sheetObj.getNetIpdns()).append("</ipdns>			\n");
		inParam.append("		<ipType>").append(sheetObj.getStandNetIpdns()).append("</ipType>			\n");
		inParam.append("		<speed>").append(sheetObj.getNetSpeed()).append("</speed>			\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "wband-Z");
		}
		return callService("doNetSheetResult", inParam.toString(), false);
	}

	/**
	 * 销户宽带工单
	 */
	public String delNetSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getNetServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getNetOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<userName>").append(sheetObj.getNetUsername()).append("</userName>				\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "wband-C");
		}
		return callService("销户宽带工单 ", inParam.toString(), true);
	}

	/**
	 * 开通H248Voip工单
	 */
	public String doH248VoipSheet(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getHvoipServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getHvoipOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>		\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>		\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>		\n");
		inParam.append("		<voipPhone>").append(sheetObj.getHvoipPhone()).append("</voipPhone>		\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>		\n");
		inParam.append("		<regId>").append(sheetObj.getHvoipRegId()).append("</regId>		\n");
		inParam.append("		<regIdType>").append(sheetObj.getHvoipRegIdType()).append("</regIdType>		\n");
		inParam.append("		<mgcIp>").append(sheetObj.getHvoipMgcIp()).append("</mgcIp>		\n");
		inParam.append("		<mgcPort>").append(sheetObj.getHvoipMgcPort()).append("</mgcPort>		\n");
		inParam.append("		<standMgcIp>").append(sheetObj.getHvoipStandMgcIp()).append("</standMgcIp>		\n");
		inParam.append("		<standMgcPort>").append(sheetObj.getHvoipStandMgcPort()).append("</standMgcPort>		\n");
		inParam.append("		<voipPort>").append(sheetObj.getHvoipPort()).append("</voipPort>		\n");
		inParam.append("		<vlanId>").append(sheetObj.getHvoipVlanId()).append("</vlanId>		\n");
		inParam.append("		<wanType>").append(sheetObj.getHvoipWanType()).append("</wanType>		\n");
		inParam.append("		<ipaddress>").append(sheetObj.getHvoipIpaddress()).append("</ipaddress>		\n");
		inParam.append("		<ipmask>").append(sheetObj.getHvoipIpmask()).append("</ipmask>		\n");
		inParam.append("		<gateway>").append(sheetObj.getHvoipGateway()).append("</gateway>		\n");
		inParam.append("		<ipdns>").append(sheetObj.getHvoipIpdns()).append("</ipdns>		\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "voip-Z");
		}
		return callService("doH248VoipSheet", inParam.toString(), false);
	}

	/**
	 * 销户H248Voip工单
	 */
	public String delH248VoipSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getHvoipServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getHvoipOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<voipPhone>").append(sheetObj.getHvoipPhone()).append("</voipPhone>				\n");
		inParam.append("		<voipPort>").append(sheetObj.getHvoipPort()).append("</voipPort>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "voip-C");
		}
		return callService("销户H248Voip工单", inParam.toString(), true);
	}

	/**
	 * 开通SipVoip工单
	 */
	public String doSipVoip(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getSipServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getSipOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>		\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>		\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>		\n");
		inParam.append("		<voipPhone>").append(sheetObj.getSipVoipPhone()).append("</voipPhone>		\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>		\n");
		inParam.append("		<voipUsername>").append(sheetObj.getSipVoipUsername()).append("</voipUsername>		\n");
		inParam.append("		<voipPwd>").append(sheetObj.getSipVoipPwd()).append("</voipPwd>		\n");
		inParam.append("		<proxServ>").append(sheetObj.getSipProxServ()).append("</proxServ>		\n");
		inParam.append("		<proxPort>").append(sheetObj.getSipProxPort()).append("</proxPort>		\n");
		inParam.append("		<standProxServ>").append(sheetObj.getSipStandProxServ()).append("</standProxServ>		\n");
		inParam.append("		<standProxPort>").append(sheetObj.getSipStandProxPort()).append("</standProxPort>		\n");
		inParam.append("		<voipPort>").append(sheetObj.getSipVoipPort()).append("</voipPort>		\n");
		inParam.append("		<regiServ>").append(sheetObj.getSipRegiServ()).append("</regiServ>		\n");
		inParam.append("		<regiPort>").append(sheetObj.getSipRegiPort()).append("</regiPort>		\n");
		inParam.append("		<standRegiServ>").append(sheetObj.getSipStandRegiServ()).append("</standRegiServ>		\n");
		inParam.append("		<standRegiPort>").append(sheetObj.getSipStandRegiPort()).append("</standRegiPort>		\n");
		inParam.append("		<outBoundProxy>").append(sheetObj.getSipOutBoundProxy()).append("</outBoundProxy>		\n");
		inParam.append("		<outBoundPort>").append(sheetObj.getSipOutBoundPort()).append("</outBoundPort>		\n");
		inParam.append("		<standOutBoundProxy>").append(sheetObj.getSipStandOutBoundProxy())
				.append("</standOutBoundProxy>		\n");
		inParam.append("		<standOutBoundPort>").append(sheetObj.getSipStandOutBoundPort())
				.append("</standOutBoundPort>		\n");
		inParam.append("		<protocol>").append(sheetObj.getSipProtocol()).append("</protocol>		\n");
		inParam.append("		<vlanId>").append(sheetObj.getSipVlanId()).append("</vlanId>		\n");
		inParam.append("		<wanType>").append(sheetObj.getSipWanType()).append("</wanType>		\n");
		inParam.append("		<ipaddress>").append(sheetObj.getSipIpaddress()).append("</ipaddress>		\n");
		inParam.append("		<ipmask>").append(sheetObj.getSipIpmask()).append("</ipmask>		\n");
		inParam.append("		<gateway>").append(sheetObj.getSipGateway()).append("</gateway>		\n");
		inParam.append("		<ipdns>").append(sheetObj.getSipIpdns()).append("</ipdns>		\n");
		// ims 必须
		inParam.append("		<voipUri>").append(sheetObj.getSipVoipUri()).append("</voipUri>		\n");
		inParam.append("		<userAgentDomain>").append(sheetObj.getSipUserAgentDomain()).append("</userAgentDomain>		\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		return callService("doSipVoip", inParam.toString(), false);
	}

	/**
	 * 销户SIP VOIP工单
	 */
	public String delSipVoipSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getSipServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getSipOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<voipUsername>").append(sheetObj.getSipVoipUsername()).append("</voipUsername>				\n");
		inParam.append("		<voipPort>").append(sheetObj.getSipVoipPort()).append("</voipPort>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		return callService("销户SipVoip工单", inParam.toString(), true);
	}

	/**
	 * 开通用户工单
	 */
	public String doUserInfo(SheetObj sheetObj)
	{
		logger.debug("doUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getUserServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getUserOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("		<officeId>").append(sheetObj.getOfficeId()).append("</officeId>				\n");
		inParam.append("		<areaId>").append(sheetObj.getAreaId()).append("</areaId>					\n");
		inParam.append("		<accessStyle>").append(sheetObj.getAccessStyle()).append("</accessStyle>			\n");
		inParam.append("		<linkman>").append(sheetObj.getLinkman()).append("</linkman>					\n");
		inParam.append("		<linkPhone>").append(sheetObj.getLinkPhone()).append("</linkPhone>				\n");
		inParam.append("		<email>").append(sheetObj.getEmail()).append("</email>						\n");
		inParam.append("		<mobile>").append(sheetObj.getMobile()).append("</mobile>					\n");
		inParam.append("		<linkAddress>").append(sheetObj.getLinkAddress()).append("</linkAddress>			\n");
		inParam.append("		<linkmanCredno>").append(sheetObj.getLinkmanCredno()).append("</linkmanCredno>		\n");
		inParam.append("		<customerId>").append(sheetObj.getCustomerId()).append("</customerId>			\n");
		inParam.append("		<customerAccount>").append(sheetObj.getCustomerAccount()).append("</customerAccount>	\n");
		inParam.append("		<customerPwd>").append(sheetObj.getCustomerPwd()).append("</customerPwd>			\n");
		inParam.append("		<specId>").append(sheetObj.getSpecId()).append("</specId>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "cpe-Z");
		}
		return callService("doUserInfo", inParam.toString(), false);
	}

	/**
	 * 删除用户资料
	 */
	public String delUserSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append(paramPrex(sheetObj));
		inParam.append("	<servTypeId>").append(sheetObj.getUserServTypeId()).append("</servTypeId>				\n");
		inParam.append("	<operateId>").append(sheetObj.getUserOperateId()).append("</operateId>				\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(sheetObj.getDealDate()).append("</dealDate>				\n");
		inParam.append("		<userType>").append(sheetObj.getUserType()).append("</userType>				\n");
		inParam.append("		<loid>").append(sheetObj.getLoid()).append("</loid>						\n");
		inParam.append("		<cityId>").append(sheetObj.getCityId()).append("</cityId>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		if (Global.JLLT.equals(Global.instAreaShortName))
		{
			return inParamToOrder(sheetObj, inParam.toString(), "cpe-C");
		}
		return callService("销户用户资料工单 ", inParam.toString(), true);
	}
	
	/**
	 * 接口入参固定前缀
	 */
	private String paramPrex(SheetObj sheetObj)
	{
		return new StringBuffer().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n")
				.append("<root>										\n").append("	<cmdId>").append(sheetObj.getCmdId())
				.append("</cmdId>\n").append("	<authUser>itms</authUser>					\n")
				.append("	<authPwd>123</authPwd>						\n").toString();
	}

	/**
	 * @param serv
	 *            接口名
	 * @param inParam
	 *            入参
	 * @param flag
	 *            是否为销户工单
	 * @return 回参
	 */
	public String callService(String serv, String inParam, boolean flag)
	{
		logger.warn("{} 调用接口的入参：" + inParam, serv);
		String returnParam = "";
		String resultDesc = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { inParam });
		}
		catch (Exception e)
		{
			logger.error("{} 调用工单异常，err:" + e, serv);
			e.printStackTrace();
		}
		logger.warn("{} 调用接口的结果：" + returnParam, serv);
		if (flag)
		{
			if (getMessage(returnParam, "resultCode").equals("000"))
			{
				resultDesc = "1";
			}
			else
			{
				resultDesc = getMessage(returnParam, "resultDes");
			}
			return resultDesc;
		}
		return returnParam;
	}

	/**
	 * 解析xml字符串，获取指定节点值
	 */
	public String getMessage(String xmlStr, String node)
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
			logger.error("exception:" + e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析xml字符串，组装为Order对象，调用工单接口
	 */
	public String inParamToOrder(SheetObj sheetObj, String inParam, String orderType)
	{
		String resultDesc = "";
		Document doc = null;
		Order order = new Order();
		StringBuffer returnBuffer = new StringBuffer();
		try
		{
			if (sheetObj != null && inParam != null && !"".equals(inParam) && orderType != null && !"".equals(orderType))
			{
				doc = DocumentHelper.parseText(inParam);
				Element rootElt = doc.getRootElement();
				order.setAd_account(rootElt.element("param").elementText("userName") == null ? ""
						: rootElt.element("param").elementText("userName"));
				order.setAd_userid(sheetObj.getLoid());
				order.setArea_code(sheetObj.getCityId());
				order.setDeviceType(sheetObj.getDeviceType());
				order.setOrder_LSH(System.currentTimeMillis() + "");
				order.setOrder_No(System.currentTimeMillis() + "");
				order.setOrder_Type(orderType);
				order.setService_code(orderType.split("-")[0]);
				order.setUser_Type("1");
				// order.setUser_name(rootElt.elementTextTrim("loid")==null?"":rootElt.elementTextTrim("loid"));
				String vector_argues = "";
				if ("wband-Z".equals(orderType))
				{
					vector_argues = "wband_name=" + sheetObj.getNetUsername() + "^wband_password=" + sheetObj.getNetPasswd()
							+ "^wband_mode=" + sheetObj.getNetWanType() + "^wband_vlan=" + sheetObj.getNetVlanId() 
							+ "^Speed=" + sheetObj.getNetSpeed();
				}
				else if ("iptv-Z".equals(orderType))
				{
					vector_argues = "M_Vlan=" + sheetObj.getMulticastVlan();
				}
				else if ("iptv-C".equals(orderType))
				{
					vector_argues = "M_Vlan=" + sheetObj.getMulticastVlan();
				}
				else if ("voip-Z".equals(orderType))
				{
					String hVoipRegIdType = "IP";
					if(sheetObj.getHvoipRegIdType().equals("1")){
						hVoipRegIdType = "DomainName";
					}
					vector_argues = "WANIPAddress=" + sheetObj.getHvoipIpaddress() + "^voip_vlan=" + sheetObj.getHvoipVlanId()
							+ "^WANDefaultGateway=" + sheetObj.getHvoipGateway() + "^WANSubnetMask=" + sheetObj.getHvoipIpmask()
							+ "^voip_MGCIP=" + sheetObj.getHvoipMgcIp() + "^voip_standbyMGCIP=" + sheetObj.getHvoipStandMgcIp()
							+ "^voip_Domain=" + sheetObj.getHvoipRegId() + "^voip_MIDFormat=" + hVoipRegIdType + "^voip_MGCPort="
							+ sheetObj.getHvoipMgcPort() + "^voip_TID=" + sheetObj.getHvoipPort() + "^voip_Prefix=RTP/";
				}
				else if ("wband-C".equals(orderType))
				{
					vector_argues = "wband_mode=" + sheetObj.getNetWanType() + "^wband_vlan=2618";
				}
				else if ("voip-C".equals(orderType))
				{
					vector_argues = "WANIPAddress=" + sheetObj.getHvoipIpaddress() + "^voip_vlan=" + sheetObj.getHvoipVlanId()
							+ "^WANDefaultGateway=" + sheetObj.getHvoipGateway() + "^voip_MGCIP=" + sheetObj.getHvoipMgcIp()
							+ "^voip_MGCPort=" + sheetObj.getHvoipMgcPort() + "^voip_TID=" + sheetObj.getHvoipPort()
							+ "^voip_Prefix=RTP/";
				}
				order.setVector_argues(vector_argues);
				logger.warn("{} 工单的入参是：" + order.toString(), orderType);
				int returnParam = callRemoteService(endPointReference, order, "dealOrder");
				logger.warn("{} 工单的结果是：" + returnParam, orderType);
				if (!orderType.contains("-C"))
				{
					returnBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
					returnBuffer.append("<root>										\n");
					if (returnParam == 1)
					{
						returnBuffer.append("	<resultCode>000</resultCode>\n");
						returnBuffer.append("	<resultDes>成功</resultDes>\n");
					}
					else
					{
						returnBuffer.append("	<resultCode>111</resultCode>\n");
						returnBuffer.append("	<resultDes>失败</resultDes>\n");
					}
					returnBuffer.append("</root>										\n");
				}
				else
				{
					return returnParam + "";
				}
			}
		}
		catch (Exception e)
		{
			logger.error("{} 调用工单异常，err:" + e, orderType);
			e.printStackTrace();
		}
		logger.warn("{} 调用接口的结果：" + resultDesc, orderType);
		return returnBuffer.toString();
	}

	/**
	 * 发送webService
	 * 
	 * @param url
	 *            发送的url路径
	 * @param inParam
	 *            参数(obj)
	 * @param method
	 *            方法名
	 * @return 调用的方法结果
	 */
	public static int callRemoteService(String url, Object inParam, String method)
	{
		int returnParam = -1;
		Service service = new Service();
		Call call = null;
		try
		{
			call = (Call) service.createCall();
			QName qn = new QName("com.huawei.base.model", "Order");
			call.registerTypeMapping(Order.class, qn, new BeanSerializerFactory(Order.class, qn),
					new BeanDeserializerFactory(Order.class, qn));
			call.setOperationName(new QName(url, method));
			call.setTargetEndpointAddress(new URL(url));
			call.setTimeout(1000 * 5);
			returnParam = ((Integer) call.invoke(new Object[] { inParam })).intValue();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			logger.warn("再试一次：");
			try
			{
				returnParam = ((Integer) call.invoke(new Object[] { inParam })).intValue();
			}
			catch (RemoteException e1)
			{
				logger.warn("第二次失败");
				logger.error(e.getMessage(), e);
			}
		}
		return returnParam;
	}
}
