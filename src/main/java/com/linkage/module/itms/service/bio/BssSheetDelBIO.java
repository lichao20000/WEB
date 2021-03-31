package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.service.dao.BssSheetDelDao;
import com.linkage.module.itms.service.obj.SheetObj;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-6
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BssSheetDelBIO
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetDelBIO.class);
	
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");
	
	private BssSheetDelDao dao;
	
	public List<Map<String,String>> getSipVoipMessage(String loid)
	{
		return dao.querySipVoipMessage(loid);
	}
	
	public List<Map<String,String>> getH248VoipMessage(String loid)
	{
		return dao.queryH248VoipMessage(loid);
	}
	
	public List<Map<String,String>> getIptvMessage(String loid)
	{
		return dao.queryIptvMessage(loid);
	}
	
	public List<Map<String,String>> getIptvPort(String iptvUsername, String loid)
	{
		return dao.getIptvPort(iptvUsername,loid);
	}
	
	public int qyeryLoid(String loid) {
		return dao.qyeryLoid(loid);
	}
	
	/**
	 * 销户IPTV工单
	 * @param userInfoSheet
	 * @return
	 */
	public String getIptvSheetResult(SheetObj userInfoSheet)
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
		inParam.append("		<userName>").append(userInfoSheet.getIptvUserName()).append("</userName>						\n");
		inParam.append("		<cityId>").append(userInfoSheet.getCityId()).append("</cityId>					\n");
		inParam.append("		<iptvPort>").append(userInfoSheet.getIptvPort()).append("</iptvPort>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("销户IPTV工单 xml:"+inParam.toString());
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
			logger.error("销户IPTV工单err:"+e.getMessage());
		}
		logger.warn("调用销户IPTV工单接口的结果："+returnParam);
		return returnParam;
	}
	
	/**
	 * 销户SIP VOIP工单
	 * @param userInfoSheet
	 * @return
	 */
	public String getSipVoipSheetResult(SheetObj userInfoSheet)
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
		inParam.append("		<voipUsername>").append(userInfoSheet.getSipVoipUsername()).append("</voipUsername>				\n");
		inParam.append("		<voipPort>").append(userInfoSheet.getSipVoipPort()).append("</voipPort>					\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("销户SipVoip工单 xml:"+inParam.toString());
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
			logger.error("销户SipVoip工单err:"+e.getMessage());
		}
		logger.warn("调用销户SipVoip工单接口的结果："+returnParam);
		return returnParam;
	}
	
	/**
	 * 销户H248Voip工单
	 * @param userInfoSheet
	 * @return
	 */
	public String getH248VoipSheetResult(SheetObj userInfoSheet)
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
		inParam.append("		<voipPhone>").append(userInfoSheet.getHvoipPhone()).append("</voipPhone>				\n");
		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn("销户H248Voip工单 xml:"+inParam.toString());
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
			logger.error("销户H248Voip工单err:"+e.getMessage());
		}
		logger.warn("调用销户H248Voip工单接口的结果："+returnParam);
		return returnParam;
	}

	
	public BssSheetDelDao getDao()
	{
		return dao;
	}

	
	public void setDao(BssSheetDelDao dao)
	{
		this.dao = dao;
	}
	
}
