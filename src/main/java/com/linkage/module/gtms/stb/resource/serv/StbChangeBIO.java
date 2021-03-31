package com.linkage.module.gtms.stb.resource.serv;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.StbChangeDAO;
import com.linkage.module.ids.util.WSClientUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-2-5
 */
public class StbChangeBIO
{
	private StbChangeDAO dao;
	private static Logger logger = LoggerFactory
			.getLogger(StbChangeBIO.class);
	
	public StbChangeDAO getDao() {
		return dao;
	}

	public void setDao(StbChangeDAO dao) {
		this.dao = dao;
	}

	public String changeStbMac(String cityId, String servAccount, String deviceMac, String oldMac,String grid,String loginAccountName)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		String resultDes = "";
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>stb</authUser>						\n");
		inParam.append("	<authPwd>123</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>8</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<cityId>").append(cityId).append("</cityId>		\n");
		inParam.append("		<mac>").append(deviceMac.toUpperCase()).append("</mac>		\n");
		inParam.append("		<oldMac>").append(oldMac.toUpperCase()).append("</oldMac>		\n");
		inParam.append("        <servaccount>").append(servAccount).append("</servaccount>      \n");

		inParam.append("        <grid>").append(grid).append("</grid>      \n");
		inParam.append("        <opertor>").append(loginAccountName).append("</opertor>      \n");

		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn(inParam.toString());
		final String url = LipossGlobals.getLipossProperty("HnItmsService");
		logger.warn("url=="+url);
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), "call");
		logger.warn(callBack.toString());
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(new StringReader(callBack));
			Element element = document.getRootElement();
			@SuppressWarnings("unused")
			String sheeted = element.elementTextTrim("sheeted");
			@SuppressWarnings("unused")
			String resultCode = element.elementTextTrim("resultCode");
			resultDes = element.elementTextTrim("resultDes");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return resultDes;
	}

	/**
	 * 获取用户列表
	 * @param
	 * @param servAccount
	 * @param deviceMac
	 * @param cityId 
	 * @return
	 */
	public List<Map<String, String>> getMac(String servAccount, String deviceMac, String cityId) {
		return dao.getMac(servAccount, deviceMac, cityId);
	}
	
	public Map<String, String> queryCustomer(String cityId, String servAccount,String deviceMac,String oldMac) {
		return dao.queryCustomer("0010000".equals(cityId)?"00":cityId,servAccount,
				deviceMac.replaceAll(":","").toUpperCase(),oldMac.replaceAll(":","").toUpperCase());
	}

	public void addLogUnBind(long user_id, String user_ip, String cityId,
			String servAccount, String deviceMac, String ajax,
			Map<String, String> map) {
		dao.addLogUnBind(user_id,user_ip,"0010000".equals(cityId)?"00":cityId,
				deviceMac.replaceAll(":","").toUpperCase(),servAccount,ajax,map);
	}

	public int countMac(int curPage_splitPage, int num_splitPage,
			String servAccount, String deviceMac) {
		return dao.countMac(curPage_splitPage, num_splitPage, servAccount, deviceMac);
	}

	public int getQueryCount() {
		return dao.getQueryCount();
	}

	
}
