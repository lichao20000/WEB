package com.linkage.module.gtms.stb.resource.serv;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.action.removeBindPageACT;
import com.linkage.module.gtms.stb.resource.dao.removeBindPageDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.util.WSClientUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-2
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class removeBindPageBIO
{
	
	private static Logger logger = LoggerFactory.getLogger(removeBindPageACT.class);
	private removeBindPageDAO dao;
	public String removebindcheck(String authUser, String authPwd, String endtime,String cityId,String mac,
			String servaccount,String loginAccountName)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		List<Map> list=dao.getcity(mac, servaccount,cityId);
		boolean status=mac.contains(":");
		String resultDes = "";
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>").append("stb").append("</authUser>						\n");
		inParam.append("	<authPwd>").append("111").append("</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>100</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<cityId>").append(list.get(0).get("city_id")).append("</cityId>		\n");
		if(status)
		{
			inParam.append("		<mac>").append(mac.replace(":", "").toUpperCase()).append("</mac>		\n");
		}
		else{
			inParam.append("		<mac>").append(mac.toUpperCase()).append("</mac>		\n");
		}
		inParam.append("        <servaccount>").append(servaccount).append("</servaccount>      \n");

		inParam.append("        <grid>").append(cityId).append("</grid>      \n");
		inParam.append("        <opertor>").append(loginAccountName).append("</opertor>      \n");

		inParam.append("	</param>								\n");
		inParam.append("</root>										\n");
		logger.warn(inParam.toString());
		final String url = LipossGlobals.getLipossProperty("HnItmsService");
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), "call");
		logger.warn(callBack.toString());
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(new StringReader(callBack));
			Element element = document.getRootElement();
//			String sheeted = element.elementTextTrim("sheeted");
//			String resultCode = element.elementTextTrim("resultCode");
			resultDes = element.elementTextTrim("resultDes");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return resultDes;
	}
	
	public List<Map> getMacList(String mac,String servaccount,String city_id)
	{
		List<Map> list=dao.getMacList(mac, servaccount,city_id);
		List<Map> listshow=new ArrayList<Map>();
		for(int i=0;i<list.size();i++)
		{
			Map map = new HashMap();
			String city_name = "";
			map.put("mac", list.get(i).get("cpe_mac"));
			map.put("servaccount", list.get(i).get("serv_account"));
			map.put("sn", list.get(i).get("sn"));
			if (!StringUtil.IsEmpty(String.valueOf(list.get(i).get("city_id"))))
			{
				city_name = StringUtil.getStringValue(CityDAO.getCityIdCityNameMap()
						.get(String.valueOf(list.get(i).get("city_id"))));
			}
			map.put("city_name", city_name);
			listshow.add(map);
		}
		return listshow;
	}
	
	
	public void addLogUnBind(long user_id, String user_ip, String city_id,
			String mac, String servaccount) {
		dao.addLogUnBind(user_id,user_ip,"0010000".equals(city_id)?"00":city_id,mac,servaccount);
	}
	
	public removeBindPageDAO getDao()
	{
		return dao;
	}
	
	public void setDao(removeBindPageDAO dao)
	{
		this.dao = dao;
	}

	
}
