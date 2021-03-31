
package com.linkage.module.ids.bio;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.dao.IdsDeviceQueryDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-17
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class IdsDeviceQueryBIO
{

	private static Logger logger = LoggerFactory.getLogger(IdsDeviceQueryBIO.class);
	
	IdsDeviceQueryDAO dao = new IdsDeviceQueryDAO();
	private Map<String, String> cityMap = null;
	private Map<String, String> rstCodeMap = new HashMap<String, String>();

	public IdsDeviceQueryBIO()
	{
		rstCodeMap.put("0", "成功");
		rstCodeMap.put("1", "数据格式错误");
		rstCodeMap.put("2", "客户端类型非法");
		rstCodeMap.put("3", "接口类型非法");
		rstCodeMap.put("1000", "未知错误");
		rstCodeMap.put("1001", "查询类型非法");
		rstCodeMap.put("1002", "查无此客户");
		rstCodeMap.put("1003", "未绑定设备");
		rstCodeMap.put("1004", "查无此设备");
		rstCodeMap.put("1005", "设备序列号非法");
		rstCodeMap.put("1006", "查到多台设备,请输入更多位序列号或完整序列号进行查询");
		rstCodeMap.put("1007", "属地非法");
		rstCodeMap.put("1008", "绑定关系不匹配");
	}

	public String queryIdsDevice(String queryField, String queryParam, String city_id)
	{
		StringBuffer backBuffer = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		String searchType = "";
		String userParam = "";
		
		//queryField==6 代表设备序列号，
		if ("6".equals(queryField))
		{
			searchType = "2";
			queryField = "1";
			userParam = "0";// 由原来的空 改为 0 无实际意义，由于接口综调接口模块 入库的sql报了字段为空，报错。
		}
		else
		{
			searchType = "1";
			userParam = queryParam;
			queryParam = "0";// 由原来的空 改为 0 无实际意义，由于接口综调接口模块 入库的sql报了字段为空，报错。
		}
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");	
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <SearchType>").append(searchType)
				.append("</SearchType>      \n");
		inParam.append("		<UserInfoType>").append(queryField)
				.append("</UserInfoType>		\n");
		inParam.append("		<UserName>").append(userParam).append("</UserName>	\n");
		inParam.append("		<DevSN>").append(queryParam).append("</DevSN>		\n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>		\n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("设备信息xml:" + inParam.toString());
		// 地址webservice
		Map cm = Global.G_CityId_CityName_Map;
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		logger.warn("LipossGlobals.getLipossProperty(IDSServiceUri);"+url);
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(),
				"deviceinfo");
		logger.warn("设备回参：" + callBack);
		cityMap = Global.G_CityId_CityName_Map;
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String rstMsg = root.elementTextTrim("RstMsg");
			Element Param = root.element("Param");
			if (null != Param)
			{
				if ("0".equals(rstCode))
				{
					String UserName = Param.elementTextTrim("UserName");
					String DevOUI = Param.elementTextTrim("DevOUI");
					String DevSN = Param.elementTextTrim("DevSN");
					String BindType = Param.elementTextTrim("BindType");
					String ip = Param.elementTextTrim("ip");
					String vendor = Param.elementTextTrim("vendor");
					String DevModel = Param.elementTextTrim("DevModel");
					String HandwareVersion = Param.elementTextTrim("HandwareVersion");
					String SoftwareVersion = Param.elementTextTrim("SoftwareVersion");
					if(Global.HBLT.equals(Global.instAreaShortName)){
						city_id = Param.elementTextTrim("cityId");
					}
					backBuffer.append(DevSN).append("#");
					backBuffer.append(DevOUI).append("#");
					backBuffer.append(BindType).append("#");
					backBuffer.append(ip).append("#");
					backBuffer.append(city_id).append("#");
					backBuffer.append(cityMap.get(city_id));
				}
				else
				{
					return "1#" + rstMsg;
				}
			}
		}
		catch (DocumentException e)
		{
			logger.warn("xml查询设备信息出错:" + e.getMessage());
			return "1#webservice链接出错";
		}
		return "0#" + backBuffer.toString();
	}

	/**
	 * 查询wan通道信息
	 * @param queryField
	 * @param queryParam
	 * @param city_id
	 * @return
	 */
	public String queryWanDetail(String queryField, String queryParam, String city_id)
	{
		StringBuffer backBuffer = new StringBuffer();
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		DateTimeUtil dd = new DateTimeUtil();
		String id = StringUtil.getStringValue(dd.getLongTime());
		String searchType = "";
		String userParam = "";
		
		//queryField==6 代表设备序列号，
		if ("6".equals(queryField))
		{
			searchType = "2";
			queryField = "1";
			userParam = "";
		}
		else
		{
			searchType = "1";
			userParam = queryParam;
			queryParam = "";
		}
		
		StringBuffer wanXml = new StringBuffer();
		wanXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		wanXml.append("<root>										\n");
		wanXml.append("	<CmdID>").append(id).append("</CmdID >						\n");
		wanXml.append("	<CmdType>CX_01</CmdType>					\n");
		wanXml.append("	<ClientType>5</ClientType>						\n");
		wanXml.append("	<Param>									\n");
		wanXml.append("     <SearchType>").append(searchType)
				.append("</SearchType>      \n");
		wanXml.append("		<UserInfoType>").append(queryField)
				.append("</UserInfoType>		\n");
		wanXml.append("		<UserName>").append(userParam).append("</UserName>		\n");
		wanXml.append("		<DevSN>").append(queryParam).append("</DevSN>		\n");
		wanXml.append("		<CityId>").append(city_id).append("</CityId>		\n");
		wanXml.append("	</Param>								\n");
		wanXml.append("</root>										\n");
		logger.warn("wanxml：" + wanXml.toString());
		String wanBack = WSClientUtil.callItmsService(url, wanXml.toString(),
				"queryWanConn");
		logger.warn("wan回参信息：" + wanBack.toString());
		SAXReader reader = new SAXReader();
		Document wanDocument = null;
		try
		{
			wanDocument = reader.read(new StringReader(wanBack));
			Element wanroot = wanDocument.getRootElement();
			String wanrstCode = wanroot.elementTextTrim("RstCode");
			String wanRstMsg = wanroot.elementTextTrim("RstMsg");
			Element wanParam = wanroot.element("Param");
			if ("0".equals(wanrstCode))
			{
				StringBuffer tr069Intfs = new StringBuffer();
				StringBuffer voipIntfs = new StringBuffer();
				StringBuffer internetIntfs = new StringBuffer();
				Element tr069 = wanroot.element("tr069Intfs");
				Element voip = wanroot.element("voipIntfs");
				Element internet = wanroot.element("internetIntfs");
				
				List<Element> ifs = (List<Element>)wanroot.elements("internetIntfs");
				
				List<Element> trs = (List<Element>)wanroot.elements("tr069Intfs");
				// 上网通道
				List<Element> vis = (List<Element>)wanroot.elements("voipIntfs");
				
				if(null!=ifs && ifs.size()>0){
					for (int i = 0; i < ifs.size(); i++)
					{
						Element element = ifs.get(i);
						if(null != element){
							if(Global.HBLT.equals(Global.instAreaShortName)){
								internetIntfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("￥").append(StringUtil.getStringValue(element.attributeValue("conn_type"))).append("￥").append(wanroot.elementTextTrim("pppoe_name")).append("^");
								logger.warn("internetIntfs="+internetIntfs.toString());
							}
							else{
								internetIntfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("^");
							}
						}
					}
				}
				

				if(null!=trs && trs.size()>0){
					for (int i = 0; i < trs.size(); i++)
					{
						Element element = trs.get(i);
						if(null != element){
							tr069Intfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("^");
						}
					}
				}
				
				
				if(null!=vis && vis.size()>0){
					for (int i = 0; i < trs.size(); i++)
					{
						Element element = vis.get(i);
						if(null != element){
							voipIntfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("^");
						}
					}
				}
				
				backBuffer.append(getStr(internetIntfs.toString())).append("#");
				backBuffer.append(getStr(voipIntfs.toString())).append("#");
				backBuffer.append(getStr(tr069Intfs.toString()));
				
				logger.warn("返回参数："+backBuffer.toString());
				
			}
			else
			{
				return "1#" + wanRstMsg;
			}
		}
		catch (DocumentException e)
		{
			logger.warn("获取wan通信信息:" + e.getMessage());
			return "1#获取wan通信信息出错";
		}
		return "0#" + backBuffer.toString();
	}
	/**
	 * 查询wan通道信息
	 * @param queryField
	 * @param queryParam
	 * @param city_id
	 * @return
	 */
	public String queryWanDetailForAH(String queryField, String queryParam, String city_id)
	{
		StringBuffer backBuffer = new StringBuffer();
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		DateTimeUtil dd = new DateTimeUtil();
		String id = StringUtil.getStringValue(dd.getLongTime());
		String searchType = "";
		String userParam = "";
		
		//queryField==6 代表设备序列号，
		if ("6".equals(queryField))
		{
			searchType = "2";
			queryField = "1";
			userParam = "";
		}
		else
		{
			searchType = "1";
			userParam = queryParam;
			queryParam = "";
		}
		
		StringBuffer wanXml = new StringBuffer();
		wanXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		wanXml.append("<root>										\n");
		wanXml.append("	<CmdID>").append(id).append("</CmdID >						\n");
		wanXml.append("	<CmdType>CX_01</CmdType>					\n");
		wanXml.append("	<ClientType>5</ClientType>						\n");
		wanXml.append("	<Param>									\n");
		wanXml.append("     <SearchType>").append(searchType)
				.append("</SearchType>      \n");
		wanXml.append("		<UserInfoType>").append(queryField)
				.append("</UserInfoType>		\n");
		wanXml.append("		<UserName>").append(userParam).append("</UserName>		\n");
		wanXml.append("		<DevSN>").append(queryParam).append("</DevSN>		\n");
		wanXml.append("		<CityId>").append(city_id).append("</CityId>		\n");
		wanXml.append("	</Param>								\n");
		wanXml.append("</root>										\n");
		logger.warn("wanxml：" + wanXml.toString());
		String wanBack = WSClientUtil.callItmsService(url, wanXml.toString(),
				"queryWanConn");
		logger.warn("wan回参信息：" + wanBack.toString());
		SAXReader reader = new SAXReader();
		Document wanDocument = null;
		try
		{
			wanDocument = reader.read(new StringReader(wanBack));
			Element wanroot = wanDocument.getRootElement();
			String wanrstCode = wanroot.elementTextTrim("RstCode");
			String wanRstMsg = wanroot.elementTextTrim("RstMsg");
			Element wanParam = wanroot.element("Param");
			if ("0".equals(wanrstCode))
			{
				StringBuffer tr069Intfs = new StringBuffer();
				StringBuffer voipIntfs = new StringBuffer();
				StringBuffer internetIntfs = new StringBuffer();
				Element tr069 = wanroot.element("tr069Intfs");
				Element voip = wanroot.element("voipIntfs");
				Element internet = wanroot.element("internetIntfs");
				
				List<Element> ifs = (List<Element>)wanroot.elements("internetIntfs");
				
				List<Element> trs = (List<Element>)wanroot.elements("tr069Intfs");
				// 上网通道
				List<Element> vis = (List<Element>)wanroot.elements("voipIntfs");
				
				if(null!=ifs && ifs.size()>0){
					for (int i = 0; i < ifs.size(); i++)
					{
						Element element = ifs.get(i);
						if(null != element){
							internetIntfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("￥").append(StringUtil.getStringValue(element.attributeValue("conn_type"))).append("^");
						}
					}
				}
				

				if(null!=trs && trs.size()>0){
					for (int i = 0; i < trs.size(); i++)
					{
						Element element = trs.get(i);
						if(null != element){
							tr069Intfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("^");
						}
					}
				}
				
				
				if(null!=vis && vis.size()>0){
					for (int i = 0; i < trs.size(); i++)
					{
						Element element = vis.get(i);
						if(null != element){
							voipIntfs.append(StringUtil.getStringValue(element.getTextTrim())).append("￥").append(StringUtil.getStringValue(element.attributeValue("vlanid"))).append("^");
						}
					}
				}
				
				backBuffer.append(getStr(internetIntfs.toString())).append("#");
				backBuffer.append(getStr(voipIntfs.toString())).append("#");
				backBuffer.append(getStr(tr069Intfs.toString()));
				
				logger.warn("返回参数："+backBuffer.toString());
				
			}
			else
			{
				return "1#" + wanRstMsg;
			}
		}
		catch (DocumentException e)
		{
			logger.warn("获取wan通信信息:" + e.getMessage());
			return "1#获取wan通信信息出错";
		}
		return "0#" + backBuffer.toString();
	}
	
	//截取字符串，去除最后的一个分割字符^
	private String getStr(String str){
		if(!StringUtil.IsEmpty(str)){
			return str.substring(0, str.length()-1);
		}else{
			return "";
		}
		
	}

	
	public IdsDeviceQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(IdsDeviceQueryDAO dao)
	{
		this.dao = dao;
	}

	public List<Map<String, String>> queryList(String idsShare_queryField,
			String idsShare_queryParam, String city_id) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = dao.queryList(idsShare_queryField, idsShare_queryParam, city_id);
		List<Map<String, String>> listshow=new ArrayList<Map<String, String>>();
		for(int i=0;i<list.size();i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			String city_name = "";
			map.put("device_serialnumber", list.get(i).get("device_serialnumber"));
			map.put("oui", list.get(i).get("oui"));
			map.put("cpe_currentstatus", list.get(i).get("cpe_currentstatus"));
			map.put("adsl_hl", list.get(i).get("adsl_hl"));
			map.put("city_id", list.get(i).get("city_id"));
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

	public List<Map<String, String>> queryListForSDDX(String idsShare_queryField, String idsShare_queryParam) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = dao.queryListForSDDX(idsShare_queryField, idsShare_queryParam);
		List<Map<String, String>> listshow=new ArrayList<Map<String, String>>();
		for(int i=0;i<list.size();i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			String city_name = "";
			map.put("device_serialnumber", list.get(i).get("device_serialnumber"));
			map.put("oui", list.get(i).get("oui"));
			map.put("cpe_currentstatus", list.get(i).get("cpe_currentstatus"));
			map.put("adsl_hl", list.get(i).get("adsl_hl"));
			map.put("city_id", list.get(i).get("city_id"));
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
	
	public String queryPPPOE(String idsShare_queryParam) {
		Map<String, String> map = dao.queryPPPOE(idsShare_queryParam);
		String username = StringUtil.getStringValue(map, "username", "");
		String cityId = StringUtil.getStringValue(map, "city_id", "");
		String userId = StringUtil.getStringValue(map, "user_id", "");
		Map<String, String> mapRate = dao.getPPPOERate(userId, cityId);
		
		return username + "&*&" + StringUtil.getStringValue(mapRate, "net_account", "") 
				+ "&*&" + StringUtil.getStringValue(mapRate, "net_password", "")
				+ "&*&" + StringUtil.getStringValue(mapRate, "test_rate", "");
	}
	
	public String queryDevList(String idsShare_queryField,String idsShare_queryParam, String city_id) {
		StringBuffer backBuffer = null;
		try
		{
			List<Map<String, String>> devList = dao.queryDevList(idsShare_queryField, idsShare_queryParam, city_id);
			backBuffer = new StringBuffer();
			if(null == devList || devList.isEmpty() || devList.size() < 1 || null == devList.get(0)){
				return "1#设备不存在宽带业务或者不支持测速";
			}else 
				if(devList.size() > 1){
					return "1#存在多个设备,请输全查询信息";
				}else{
					Map<String, String> maps = devList.get(0);
					int is_speed = StringUtil.getIntValue(maps, "is_speedtest"); 
					if(is_speed == 0){
						return "1#设备不支持测速";
					}
					
					String downlink = StringUtil.getStringValue(maps, "downlink");
					if(StringUtil.IsEmpty(downlink)){
						return "1#签约速率不存在,设备不支持测速";
					}
					int rate = getTestRate(StringUtil.getDoubleValue(downlink));
					Map<String,String> testUsr = dao.getTestUser(rate);
					if(null == testUsr || testUsr.isEmpty()){
						return "1#设备不支持测速,对应测速专用账号不存在";
					}
					String username = StringUtil.getStringValue(testUsr, "username");
					String pwd = StringUtil.getStringValue(testUsr, "password");
					cityMap = Global.G_CityId_CityName_Map;
					
					backBuffer.append(StringUtil.getStringValue(maps, "device_id")).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "device_serialnumber")).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "oui")).append("||");
					backBuffer.append(downlink).append("||");
					backBuffer.append(username).append("||");
					backBuffer.append(pwd).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "city_id")).append("||");
					backBuffer.append(cityMap.get(StringUtil.getStringValue(maps, "city_id"))).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "loid")).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "username")).append("||");
					backBuffer.append(StringUtil.getStringValue(maps, "wan_type"));
					return "0||"+backBuffer.toString();
					
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("queryDevList err:{}",e.getMessage());
			return "1#系统异常";
		}
	}
	
	public int getTestRate(double downlink){
		int ret = 100;
		if(downlink > 100 && downlink <= 200){
			ret = 200;
		}else
			if(downlink > 200 && downlink <= 300){
				ret = 300;
			}else
				if(downlink > 300 && downlink <= 500){
					ret = 500;
				}else
					if(downlink > 500){
						ret = 1000;
					}
		logger.warn("ret : {}",ret);
		return ret;
	}
	
}
