
package com.linkage.module.gtms.stb.resource.serv;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.userMessageManageDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-30
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class userMessageManageBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(userMessageManageBIO.class);
	private userMessageManageDAO dao;
	private int maxPage_splitPage;
/**
 * 添加
 * @param authUser
 * @param authPwd
 * @param dealDate
 * @param platformType
 * @param userGroupID
 * @param iptvBindPhone
 * @param cityId
 * @param stbuser
 * @param stbpwd
 * @param mac
 * @param stbaccessStyle
 * @param servaccount
 * @param servpwd
 * @param stbuptyle
 * @return
 */
	public String adduserMessage(String authUser, String authPwd, String dealDate,
			String platformType, String userGroupID, String iptvBindPhone, String cityId,
			String stbuser, String stbpwd, String mac, String stbaccessStyle,
			String servaccount, String servpwd, String stbuptyle)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil(dealDate);
		String resultDes = "";
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>").append("stb").append("</authUser>						\n");
		inParam.append("	<authPwd>").append("111").append("</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>1</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<platformType>").append(platformType)
				.append("</platformType>		\n");
		inParam.append("		<userGroupID>").append(userGroupID)
				.append("</userGroupID>		\n");
		inParam.append("		<iptvBindPhone>").append(iptvBindPhone)
				.append("</iptvBindPhone>		\n");
		inParam.append("		<cityId>").append(cityId).append("</cityId>		\n");
		inParam.append("		<stbuser>").append(stbuser).append("</stbuser>		\n");
		inParam.append("		<stbpwd>").append(stbpwd).append("</stbpwd>		\n");
		if(!StringUtil.IsEmpty(mac))
		{
			boolean status=mac.contains(":");
			if(status)
			{
				inParam.append("		<mac>").append(mac.replace(":", "").toUpperCase()).append("</mac>		\n");
			}
			else{
				inParam.append("		<mac>").append(mac.toUpperCase()).append("</mac>		\n");
			}			
		}else
		{
				inParam.append("		<mac>").append(mac).append("</mac>		\n");
		}
		inParam.append("        <stbaccessStyle>").append("1")
				.append("</stbaccessStyle>      \n");
		inParam.append("        <servaccount>").append(servaccount)
				.append("</servaccount>      \n");
		inParam.append("        <servpwd>").append(servpwd).append("</servpwd>      \n");
		inParam.append("        <stbuptyle>").append("FTTH")
				.append("</stbuptyle>      \n");
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
			String sheeted = element.elementTextTrim("sheeted");
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
 * 修改
 * @param authUser
 * @param authPwd
 * @param platformType
 * @param userGroupID
 * @param iptvBindPhone
 * @param cityId
 * @param stbuser
 * @param stbpwd
 * @param mac
 * @param stbaccessStyle
 * @param servaccount
 * @param servpwd
 * @param stbuptyle
 * @return
 */
	public String updateuserMessage(String authUser, String authPwd, 
			String platformType, String userGroupID, String iptvBindPhone, String cityId,
			String stbuser, String stbpwd, String mac, String stbaccessStyle,
			String servaccount, String servpwd, String stbuptyle)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		String resultDes = "";
		String cmdId = StringUtil.getStringValue(dt.getLongDateChar());
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>").append("stb").append("</authUser>						\n");
		inParam.append("	<authPwd>").append("123").append("</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>2</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<platformType>").append(platformType)
				.append("</platformType>		\n");
		inParam.append("		<userGroupID>").append(userGroupID)
				.append("</userGroupID>		\n");
		inParam.append("		<iptvBindPhone>").append(iptvBindPhone)
				.append("</iptvBindPhone>		\n");
		inParam.append("		<cityId>").append(cityId).append("</cityId>		\n");
		inParam.append("		<stbuser>").append(stbuser).append("</stbuser>		\n");
		inParam.append("		<stbpwd>").append(stbpwd).append("</stbpwd>		\n");
		if(!StringUtil.IsEmpty(mac))
		{
			boolean status=mac.contains(":");
			if(status)
			{
				inParam.append("		<mac>").append(mac.replace(":", "").toUpperCase()).append("</mac>		\n");
			}
			else{
				inParam.append("		<mac>").append(mac.toUpperCase()).append("</mac>		\n");
			}			}else
		{
				inParam.append("		<mac>").append(mac).append("</mac>		\n");
		}		inParam.append("        <stbaccessStyle>").append("1")
				.append("</stbaccessStyle>      \n");
		inParam.append("        <servaccount>").append(servaccount)
				.append("</servaccount>      \n");
		inParam.append("        <servpwd>").append(servpwd).append("</servpwd>      \n");
		inParam.append("        <stbuptyle>").append("FTTH")
				.append("</stbuptyle>      \n");
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
			String sheeted = element.elementTextTrim("sheeted");
			String resultCode = element.elementTextTrim("resultCode");
			resultDes = element.elementTextTrim("resultDes");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return resultDes;
	}
	public List<Map> queryplatform()
	{
		return dao.queryplatform();
	}
	public List<Map> queryCustomerGroup()
	{
		return dao.queryCustomerGroup();
	}
	public String deleteuserMessage(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		String resultDes = "";
		List<Map> list=dao.queryList1(cityId, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage, num_splitPage); 
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>").append(String.valueOf(list.get(0).get("authUser"))).append("</authUser>						\n");
		inParam.append("	<authPwd>").append(String.valueOf(list.get(0).get("authPwd"))).append("</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>3</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<iptvBindPhone>").append(String.valueOf(list.get(0).get("iptvBindPhone")))
				.append("</iptvBindPhone>		\n");
		inParam.append("		<cityId>").append(String.valueOf(list.get(0).get("cityId"))).append("</cityId>		\n");
		inParam.append("		<servaccount>").append(String.valueOf(list.get(0).get("servaccount"))).append("</servaccount>		\n");
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
			String sheeted = element.elementTextTrim("sheeted");
			String resultCode = element.elementTextTrim("resultCode");
			resultDes = element.elementTextTrim("resultDes");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return resultDes;
	}
	public List<Map> queryList(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage,String stbuser)
	{
		logger.debug("bio=queryList({},{},{},{},{},{},{},{})", new Object[] { cityId, starttime, endtime , servaccount, platformType
				, MAC, userGroupID, stbaccessStyle});
		maxPage_splitPage = dao.getquerypaging(cityId, starttime, endtime, servaccount,
				platformType, MAC, userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage,
				num_splitPage,stbuser);
		return dao.queryList(cityId, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage, num_splitPage,stbuser);
	}
	public List<Map> queryList1(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage)
	{
		maxPage_splitPage = dao.getquerypaging1(cityId, starttime, endtime, servaccount,
				platformType, MAC, userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage,
				num_splitPage);
		return dao.queryList1(cityId, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage, num_splitPage);
	}
	/**
	 * 查询属地
	 * @return
	 */
	public String getCity(String cityId){
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})",cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}
	
	public String  test(String city_id)
	{
		String str="";
		if(city_id.equals("00"))
		{
			str="00";
			return str;
		}else
		{
			List<Map> list=dao.parentid(city_id);
			str=String.valueOf(list.get(0).get("parent_id"));
			if(str.equals("00"))
			{
				return city_id;
			}else
			{
				return str+","+city_id;
			}
		}
		
		
	} 
	/**
	 * 查询上级属地是否是省中心
	 * @param cityId
	 * @return
	 */
	public String getAllPcityIdByCityId(String cityId)
	{
		Map<String, String> cityMap = CityDAO.getCityIdPidMap();
		String city_id=cityMap.get(cityId);
		StringBuffer bf = new StringBuffer();
		bf.append(city_id).append("#").append(cityId);
		return bf.toString();
	}
	public String getplatformType()
	{
		List<Map> list=dao.queryplatform();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			if(i>0){
				bf.append("#");
			}
			bf.append(list.get(i).get("platform_id"));
			bf.append("$");
			bf.append(list.get(i).get("platform_name"));
		}
		return bf.toString();
	}
	public String getuserGroupID()
	{
		List<Map> list=dao.queryCustomerGroup();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			if(i>0){
				bf.append("#");
			}
			bf.append(list.get(i).get("group_id"));
			bf.append("$");
			bf.append(list.get(i).get("group_name"));
		}
		return bf.toString();
		
	}
	
	/**
	 * 获取用户信息
	 */
	public Map<String,String> getCustomerInfo(String servaccount) {
		return dao.getCustomerInfo(servaccount);
	}
	
	/**
	 * 记录添加用户日志
	 */
	public void addLogInsert(Map<String,String> customerMap,long user_id, String user_ip, String ajax,
			String platformType, String userGroupID, String iptvBindPhone,
			String city_id, String stbuser, String stbpwd, String mac,
			String servaccount, String servpwd) {
		dao.addLogInsert(customerMap,user_id,user_ip,ajax, platformType, userGroupID,
				iptvBindPhone, city_id, stbuser, stbpwd, mac, servaccount,
				servpwd);
		
	}
	
	/**
	 * 记录修改用户日志
	 */
	public void addLogUpdate(Map<String,String> customerMap,long user_id, String user_ip, String ajax,
			String authUser, String authPwd, String platformType,
			String userGroupID, String iptvBindPhone, String city_id,
			String stbuser, String stbpwd, String mac, String stbaccessStyle,
			String servaccount, String servpwd, String stbuptyle1) {
		dao.addLogUpdate(customerMap,user_id,user_ip,ajax,authUser, authPwd, platformType, userGroupID, iptvBindPhone, city_id, stbuser, stbpwd, mac, stbaccessStyle, servaccount, servpwd, stbuptyle1);
		
	}
	
	/**
	 * 记录删除用户日志
	 * @param customerMap 
	 */
	public void addLogDelete(Map<String, String> customerMap, long user_id, String user_ip, String ajax,
			String servaccount) {
		dao.addLogDelete(customerMap,user_id,user_ip,ajax,servaccount);
	}
	
	
	
	public userMessageManageDAO getDao()
	{
		return dao;
	}

	public void setDao(userMessageManageDAO dao)
	{
		this.dao = dao;
	}

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	public String cleanAccountPwd(String servaccount) {
		return dao.cleanAccountPwd(servaccount);
	}
	public void addLogcleanAccountPwd(Map<String, String> customerMap,
			long user_id, String user_ip, String ajax, String servaccount) {
		dao.addLogcleanAccountPwd(customerMap,user_id,user_ip,ajax,servaccount);
		
	}
	
	
	
	
}
