package com.linkage.module.gtms.stb.resource.serv;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.StbEServerQueryDao;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.util.WSClientUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wanghong5
 */
public class StbEServerQueryBio
{
	private static Logger logger = LoggerFactory
			.getLogger(StbEServerQueryBio.class);

	private StbEServerQueryDao dao;


	/**工单类型集*/
	public static final Map<String,String> SERVERTYPEMAP=new HashMap<String,String>();
	static
	{
		SERVERTYPEMAP.put("1","新装");
		SERVERTYPEMAP.put("2","修改");
		SERVERTYPEMAP.put("3","拆机");
		SERVERTYPEMAP.put("7","MAC绑定");
		SERVERTYPEMAP.put("8","机顶盒更换");
		SERVERTYPEMAP.put("21","自动软件升级");
		SERVERTYPEMAP.put("22","新旧账户割接");
		SERVERTYPEMAP.put("23","绑定关系更改申请");
		SERVERTYPEMAP.put("100","解绑");
	}
	
	
	/**
	 * 获取总数
	 */
	public int getQueryCount()
	{
		return dao.getQueryCount();
	}

	/**
	 * 获取工单数据集
	 */
	public List<Map<String, String>> queryEServerList(int curPage_splitPage,
			int num_splitPage, String deviceMac,String servAccount,
			String grid,String opertor,long startTime, long endTime) 
	{
		List<Map<String, String>> list=dao.queryEServerList(curPage_splitPage,num_splitPage,deviceMac,
				servAccount,grid,opertor,startTime,endTime);
	
		if(list!=null && !list.isEmpty()){
			for(Map<String,String> map:list)
			{
				map.put("receive_date", transDate(StringUtil.getLongValue(map,"receive_date")));
				
				String mac=StringUtil.getStringValue(map,"mac");
				if(StringUtil.IsEmpty(mac))
				{
					String sheet_context=StringUtil.getStringValue(map,"sheet_context");
					if(!StringUtil.IsEmpty(sheet_context) && sheet_context.indexOf("<mac>")>-1){
						int s=sheet_context.indexOf("<mac>")+5;
						int e=sheet_context.indexOf("</mac>");
						mac=sheet_context.substring(s,e);
					}
					sheet_context=null;
				}
				
				map.put("mac", mac);
				String server_type=SERVERTYPEMAP.get(StringUtil.getStringValue(map,"type"));
				map.put("server_type",StringUtil.IsEmpty(server_type)? "未知":server_type);
				
				mac=null;
				server_type=null;
			}
		}
		
		return list;
	}

	/**
	 * 获取分页总数
	 */
	public int countEServerList(int num_splitPage,String deviceMac,String servAccount,
			String grid,String opertor,long startTime,long endTime) 
	{
		return dao.countEServerList(num_splitPage,deviceMac,servAccount,
				grid,opertor,startTime,endTime);
	}
	
	/**
	 * 获取工单详细信息
	 */
	public Map<String,String> queryEServerInfo(String sheetId)
	{
		Map<String,String> map=dao.queryEServerInfo(sheetId);
		if(map!=null && !map.isEmpty())
		{
			String mac=StringUtil.getStringValue(map,"mac");
			if(StringUtil.IsEmpty(mac))
			{
				String sheet_context=StringUtil.getStringValue(map,"sheet_context");
				if(!StringUtil.IsEmpty(sheet_context) && sheet_context.indexOf("<mac>")>-1){
					int s=sheet_context.indexOf("<mac>")+5;
					int e=sheet_context.indexOf("</mac>");
					mac=sheet_context.substring(s,e);
				}
				sheet_context=null;
			}
			
			map.put("mac", mac);
			map.put("receive_time",transDate(StringUtil.getLongValue(map,"receive_date")));
			map.put("city_name",CityDAO.getCityName(StringUtil.getStringValue(map,"city_id")));
			map.put("server_type",StringUtil.getStringValue(SERVERTYPEMAP,
										StringUtil.getStringValue(map,"type"),"未知"));
		}
		
		return map;
	}

	
	/**
	 * 秒数转成日期
	 */
	public String transDate(long seconds)
	{
		try{
			return new DateTimeUtil(seconds * 1000).getLongDate();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}
	

    public List<Map<String, String>> query(int curPage_splitPage, int num_splitPage, String mac, String servAccount, String status, long start_time, long end_time) {
    	return  dao.query(curPage_splitPage,num_splitPage,mac,servAccount,status,start_time,end_time);
	}

	public int count(int num_splitPage, String mac, String servAccount, String status, long start_time, long end_time) {
		return dao.count(num_splitPage,mac,servAccount,status,start_time,end_time);
	}

	public Map<String, String> queryDetail(String sheetId) {
		return dao.queryDetail(sheetId);
	}

	public String updateStbBindAccChgRecord(String sheetId,String status) {

		return  dao.updateStbBindAccChgRecord(sheetId,status);

	}

	public String bindcheck(String mac,String servaccount)
	{
		String cityId = "";
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		Map cityIdMap=dao.getCity(servaccount);
		if (null != cityIdMap && cityIdMap.size() > 0)
		{
			cityId = StringUtil.getStringValue(cityIdMap.get("city_id"));
		}
		boolean status=mac.contains(":");
		String resultDes = "";
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<cmdId>").append("FromWEB"+"-"+dt.getLongDateChar()).append("</cmdId>						\n");
		inParam.append("	<authUser>").append("stb").append("</authUser>						\n");
		inParam.append("	<authPwd>").append("123").append("</authPwd>						\n");
		inParam.append("	<servTypeId>25</servTypeId>					\n");
		inParam.append("	<operateId>7</operateId>					\n");
		inParam.append("	<param>									\n");
		inParam.append("		<dealDate>").append(dt.getLongDateChar()).append("</dealDate>		\n");
		inParam.append("		<cityId>").append(cityId).append("</cityId>		\n");
		if(status)
		{
			inParam.append("		<mac>").append(mac.replace(":", "").toUpperCase()).append("</mac>		\n");
		}
		else{
			inParam.append("		<mac>").append(mac.toUpperCase()).append("</mac>		\n");
		}
		inParam.append("        <servaccount>").append(servaccount).append("</servaccount>      \n");
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

	public String removebindcheck(String mac)
	{
		StringBuffer inParam = new StringBuffer();
		DateTimeUtil dt = new DateTimeUtil();
		String servAccount = "";
		String cityId = "";
		Map oldServAccInfoMap =dao.getOldServAccInfo(mac);
		if(null != oldServAccInfoMap && oldServAccInfoMap.size() > 0)
		{
			servAccount = StringUtil.getStringValue(oldServAccInfoMap.get("serv_account"));
			cityId = StringUtil.getStringValue(oldServAccInfoMap.get("city_id"));
		}
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
		inParam.append("		<cityId>").append(cityId).append("</cityId>		\n");
		if(status)
		{
			inParam.append("		<mac>").append(mac.replace(":", "").toUpperCase()).append("</mac>		\n");
		}
		else{
			inParam.append("		<mac>").append(mac.toUpperCase()).append("</mac>		\n");
		}
		inParam.append("        <servaccount>").append(servAccount).append("</servaccount>      \n");
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

	public StbEServerQueryDao getDao() {
		return dao;
	}

	public void setDao(StbEServerQueryDao dao) {
		this.dao = dao;
	}

	public void updateStbBindAccChgRecordOperLog(String sheetId, String result) {
		dao.updateStbBindAccChgRecordOperLog(sheetId,result);
	}

	public String getGroupOid(long acc_oid) {
		return  dao.getGroupOid(acc_oid);
	}

    public boolean isbind(String mac) {
		return  dao.isbind(mac);
    }
}
