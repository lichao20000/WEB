package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("serial")
public class DeviceInfoExportACT extends splitPageAction implements ServletRequestAware, SessionAware{

	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoExportACT.class);
	private Cursor cursor = null;
	private HttpServletRequest request;
	private Map fields = null;
	private Map session;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private String city_id;
	private String rela_dev_type_id;
	private String devicetype_id;
	private String gw_type;
	private String isBind;
	private String vendor_id;
	private String startTime;
	private String endTime;
	private String is_normal;
	private String deviceModelId;
	private String devicetype;
	private String citynext;
	private String recentStartTime;
	private String recentEndTime;
	private String cityIdSelect;
	private String is_esurfing;
	private String ip_type;
	private String gbbroadband;
	private String is_speedTest;
	private String is_ableUser;
	/**
	 * 机顶盒厂商列表
	 */
	private String stb_m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from stb_tab_vendor";
	/**
	 * 厂商列表
	 */
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";

	
	public String exportByEdition()
	{
		logger.debug("parseExcel");
		fileName = "设备导出清单";
		title = new String[11];
		column = new String[11];
		int i = 0;
		title[i] = "设备厂商";
		column[i] = "vendor_id";
		i =i+1;
		title[i] = "型号";
		column[i] = "devicemodel";
		i =i+1;
		title[i] = "软件版本";
		column[i] = "softwareversion";
		if(Global.HBLT.equals(Global.instAreaShortName)) {
			i =i+1;
			title[i] = "上级属地";
			column[i] = "city_name";
		}else{
			i =i+1;
			title[i] = "属地";
			column[i] = "city_parentname";
		}
		i =i+1;
		title[i] = "设备序列号"; 
		column[i] = "device_serialnumber";
		if(Global.NXDX.equals(Global.instAreaShortName)) {
			i =i+1;
			title[i] = "LOID";
			column[i] = "loid";
		} 
		if(!Global.NXDX.equals(Global.instAreaShortName) && !Global.HBLT.equals(Global.instAreaShortName)) {
			i =i+1;
			title[i] = "管理域";
			column[i] = "area_id";
		}
		if(Global.JLDX.equals(Global.instAreaShortName)) {
			i =i+1;
			title[i] = "LOID";
			column[i] = "loid";
			i =i+1;
			title[i] = "宽带账号(IPTV账号)";
			column[i] = "serv_account";
			i =i+1;
			title[i] = "设备MAC";
			column[i] = "cpe_mac";
		}
		data = parseExcel();
		return "excel";
	}
	
	public List<Map> parseExcel(){
		logger.warn("gw_type:{}",gw_type);
		String gw_type = request.getParameter("gw_type");
		Cursor cursor = getDeviceListByVenderExcel(request);
		Map fields = cursor.getNext();
		Map area_Map = getAreaIdMapName();
		Map city_Map = CityDAO.getCityIdCityNameMap();
		Map cityPidMap = CityDAO.getCityIdPidMap();
		Map venderMap = getOUIDevMap(gw_type);
		String delStr = "DelDevice('?')";
		String editStr = "EditDevice('?')";
		String detailStr = "DetailDevice('?')";

		String devicetype_id = null;
		String devicemodel = null;
		String softwareversion = null;
		Map deviceTypeMap = new HashMap();
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select device_model,softwareversion,devicetype_id ");
		}else{
			psql.append("select * ");
		}
		if("4".equals(gw_type)){
			psql.append("from stb_tab_devicetype_info a,stb_gw_device_model b ");
		}else{
			psql.append("from tab_devicetype_info a,gw_device_model b ");
		}
		psql.append("where a.device_model_id=b.device_model_id");
		
		Cursor cursorTmp= DataSetBean.getCursor(psql.getSQL());
		List<Map> resultList = new ArrayList<Map>();
		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null){
			devicemodel = (String)fieldTmp.get("device_model");
			softwareversion = (String)fieldTmp.get("softwareversion");
			devicetype_id = (String)fieldTmp.get("devicetype_id");
			deviceTypeMap.put(devicetype_id,devicemodel+","+softwareversion);
			fieldTmp = cursorTmp.getNext();
		}
		String device_id = null;
		String device_id_ex = null;
		String strOper = null;
		String city_id = null;
		String city_name = null;
		String city_parentname = null;
	    while (fields != null) {
	    	Map<String,String> resultMap = new HashMap<String,String>();
	    	devicetype_id = (String)fields.get("devicetype_id"); 
	    	devicemodel = "";
			softwareversion = "";
			if(!devicetype_id.equals("-1")){
			
				String tmp = (String)deviceTypeMap.get(devicetype_id);
				if(tmp.split(",").length>1){
					if(tmp != "" && null != tmp){
						String[] aa = tmp.split(",");
						devicemodel = aa[0];
						softwareversion = aa[1];
					}}
					else
					{
						if(tmp != "" && null != tmp){
							String[] aa = tmp.split(",");
							devicemodel = aa[0];
							softwareversion = "";
						}
					}
			}
	    	String loid=String.valueOf(fields.get("loid"));
	    	String serv_account=String.valueOf(fields.get("serv_account"));
	    	//增加设备MAC一列
	    	String cpe_mac = String.valueOf(fields.get("cpe_mac"));
			device_id = (String)fields.get("device_id");
	        device_id = device_id.replaceAll("\\+", "%2B");
	        device_id = device_id.replaceAll("&", "%26");
	        device_id = device_id.replaceAll("#", "%23");
			device_id_ex = (String)fields.get("device_id_ex");
			city_id = (String)fields.get("city_id");
			city_name = (String)city_Map.get(city_id);
			city_name = city_name == null ? "&nbsp;" : city_name;
			city_parentname = (String)city_Map.get(cityPidMap.get(city_id));
			city_parentname = city_parentname == null ? "&nbsp;" : city_parentname;
			
			resultMap.put("vendor_id", com.linkage.commons.util.StringUtil.getStringValue(venderMap.get(fields.get("vendor_id"))));
			resultMap.put("devicemodel", devicemodel);
			resultMap.put("softwareversion", softwareversion);
			resultMap.put("city_name", city_name);
			if(Global.HBLT.equals(Global.instAreaShortName))
			{
				resultMap.put("city_parentname", city_parentname);
			}
			resultMap.put("device_serialnumber", (String)fields.get("device_serialnumber"));
			if(Global.NXDX.equals(Global.instAreaShortName))
			{
				resultMap.put("loid", loid);
			}else
			{
				if(!Global.HBLT.equals(Global.instAreaShortName))
				{
					resultMap.put("area_id", com.linkage.commons.util.StringUtil.getStringValue(area_Map.get((String)fields.get("area_id"))));
				}
			}
			//管理域
			//strData += "<TD>" + area_Map.get((String)fields.get("area_id")) + "</TD>";
			if(Global.JLDX.equals(Global.instAreaShortName))
			{
				resultMap.put("loid", loid);
				resultMap.put("serv_account", serv_account);
				resultMap.put("cpe_mac", cpe_mac);
			}
	        fields = cursor.getNext();
	        resultList.add(resultMap);
	    }
	    logger.warn("resultList.size:{}",resultList.size());
	    return resultList;
	}
	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap(String gw_type)
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		if ("4".equals(gw_type))
		{
			cursor = DataSetBean.getCursor(stb_m_Vendor_SQL);
		}
		else
		{
			cursor = DataSetBean.getCursor(m_Vendor_SQL);
		}
		Map fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String ouiName = (String) fields.get("vendor_add");
				if (ouiName != null && !"".equals(ouiName))
				{
					ouiMap.put((String) fields.get("vendor_id"), ouiName);
				}
				else
				{
					ouiMap.put((String) fields.get("vendor_id"),
							(String) fields.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}
	/**
	 * 读取设备资源表,获取设备信息 导出Excel
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceListByVenderExcel(HttpServletRequest request)
	{
		// 1. 家庭网关设备 2.企业网关设备
		String use_state = request.getParameter("use_state");
		String gw_type_Str = request.getParameter("gw_type");
		String rela_dev_type_id = request.getParameter("rela_dev_type_id");
		String fileter = "";
		if (gw_type_Str == null || gw_type_Str.equals(""))
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter += "&gw_type=" + gw_type;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (StringUtil.IsEmpty(use_state))
		{
			use_state = (String) session.getAttribute("use_state");
		}
		String startTime = request.getParameter("startTime");
		if (null == startTime || "null".equals(startTime))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if (null == endTime || "null".equals(endTime))
		{
			endTime = "";
		}
		String recentStartTime = request.getParameter("recentStartTime");
		if (null == recentStartTime || "null".equals(recentStartTime))
		{
			recentStartTime = "";
		}
		String recentEndTime = request.getParameter("recentEndTime");
		if (null == recentEndTime || "null".equals(recentEndTime))
		{
			recentEndTime = "";
		}
		/*江西电信新增查询条件*/
		String is_esurfing=StringUtil.getStringValue(request.getParameter("is_esurfing"));
		String ip_type=StringUtil.getStringValue(request.getParameter("ip_type"));
		String gbbroadband=StringUtil.getStringValue(request.getParameter("gbbroadband"));
		String is_speedTest=StringUtil.getStringValue(request.getParameter("is_speedTest"));
		String is_ableUser=StringUtil.getStringValue(request.getParameter("is_ableUser"));
		ArrayList list = new ArrayList();

		String sqlDevice = "";
		if (Global.JLDX.equals(Global.instAreaShortName)
				&& 4 == gw_type)
		{
			StringBuffer sb=new StringBuffer();
			if(DBUtil.GetDB()==3){
				//TODO wait
				sb.append("select a.devicetype_id,a.cpe_mac,a.device_id,a.device_id_ex,"); 
				sb.append("a.city_id,a.vendor_id,a.device_serialnumber,a.area_id,b.loid,b.serv_account ");
			}else{
				sb.append("select a.*,b.loid,b.serv_account ");
			}
			sb.append("from stb_tab_gw_device a ");
			sb.append("left join stb_tab_customer b on a.customer_id=b.customer_id,stb_tab_devicetype_info t ");
			sb.append("where a.devicetype_id=t.devicetype_id and a.device_status=1 ");
			sb.append("and a.gw_type="+ gw_type);
			if ("0".equals(use_state)){
				sb.append(" and a.serv_account is null");
			}else if ("1".equals(use_state)){
				sb.append(" and a.serv_account is not null");
			}
			
			sqlDevice=sb.toString();
		}
		else if (Global.JLDX.equals(Global.instAreaShortName)
				&& 1 == gw_type)
		{
			StringBuffer sb=new StringBuffer();
			if ("0".equals(use_state))
			{
				if(DBUtil.GetDB()==3){
					sb.append("select devicetype_id,loid,serv_account,cpe_mac,device_id,");
					sb.append("device_id_ex,city_id,vendor_id,device_serialnumber,area_id ");
				}else{
					sb.append("select a.*,t.* ");
				}
				sb.append("from tab_gw_device a,tab_devicetype_info t ");
				sb.append("where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.gw_type=1");
			}
			else if ("1".equals(use_state))
			{
				if(DBUtil.GetDB()==3){
					//TODO wait
					sb.append("select devicetype_id,cpe_mac,device_id,device_id_ex,");
					sb.append("city_id,vendor_id,device_serialnumber,area_id,");
				}else{
					sb.append("select a.*,t.*,");
				}
				sb.append("b.username as loid,c.username as serv_account ");
				sb.append("from tab_gw_device a,tab_hgwcustomer b,hgwcust_serv_info c,tab_devicetype_info t ");
				sb.append("where a.devicetype_id=t.devicetype_id and a.device_status=1 ");
				sb.append("and a.gw_type=1 and a.device_id=b.device_id and b.user_id=c.user_id ");
				sb.append("and c.serv_type_id = 10");
			}
			else
			{
				if(DBUtil.GetDB()==3){
					//TODO wait
					sb.append("select devicetype_id,cpe_mac,device_id,device_id_ex,");
					sb.append("city_id,vendor_id,device_serialnumber,area_id,");
				}else{
					sb.append("select  a.*,t.*,");
				}
				sb.append("b.username as loid,c.username as serv_account ");
				sb.append("from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id ");
				sb.append("left join hgwcust_serv_info c on (b.user_id=c.user_id and c.serv_type_id=10),tab_devicetype_info t ");
				sb.append("where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.gw_type=1");
			}
			
			sqlDevice=sb.toString();
		}
		else if (gw_type == 4)
		{
			StringBuffer sb=new StringBuffer();
			if(DBUtil.GetDB()==3){
				//TODO wait
				sb.append("select devicetype_id,loid,serv_account,cpe_mac,device_id,");
				sb.append("device_id_ex,city_id,vendor_id,device_serialnumber,area_id ");
			}else{
				sb.append("select * ");
			}
			if(Global.HBLT.equals(Global.instAreaShortName))
			{
				sb.append("from stb_tab_gw_device a,stb_gw_devicestatus b,stb_tab_devicetype_info t ");
				sb.append("where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id ");
				sb.append("and a.device_status=1 and a.gw_type="+ gw_type);
			}
			else
			{
				sb.append("from stb_tab_gw_device a,stb_tab_devicetype_info t ");
				sb.append("where a.devicetype_id=t.devicetype_id ");
				sb.append("and a.device_status=1 and a.gw_type="+ gw_type);
			}
			sqlDevice=sb.toString();
		}
		else if (Global.NXDX.equals(Global.instAreaShortName))
		{
			StringBuffer sb=new StringBuffer();
			if(DBUtil.GetDB()==3){
				//TODO wait
				sb.append("select devicetype_id,serv_account,cpe_mac,");
				sb.append("device_id,device_id_ex,city_id,vendor_id,");
				sb.append("device_serialnumber,area_id,b.username as loid ");
			}else{
				sb.append("select a.*,t.*,b.username as loid ");
			}
			sb.append("from tab_gw_device a,tab_hgwcustomer b,tab_devicetype_info t ");
			sb.append("where a.devicetype_id=t.devicetype_id and a.device_status = 1 ");
			if (1 == gw_type) {
				sb.append("and a.gw_type = 1 and a.device_id = b.device_id ");
				sb.append("and a.customer_id is not null and a.cpe_allocatedstatus = 1");
			}
			else if (2 == gw_type) {
				sb.append("and a.gw_type=2 and a.device_id=b.device_id and a.cpe_allocatedstatus=1");
			}
			sqlDevice=sb.toString();
		}
		else if (Global.JXDX.equals(Global.instAreaShortName)) 
		{
			StringBuffer sqlCount=new StringBuffer();
			
			if(DBUtil.GetDB()==3){
				//TODO wait
				sqlCount.append("select devicetype_id,loid,serv_account,cpe_mac,device_id,");
				sqlCount.append("device_id_ex,city_id,vendor_id,device_serialnumber,area_id ");
			}else{
				sqlCount.append("select * ");
			}
			sqlCount.append("from tab_gw_device a,tab_devicetype_info t ");
			
			//查询是否天翼网关，是否支持测速，是否支持千兆 需要加上关联表tab_device_version_attribute
			if (!StringUtil.IsEmpty(is_esurfing) || !StringUtil.IsEmpty(gbbroadband) 
					|| !StringUtil.IsEmpty(is_speedTest)) {
				sqlCount.append(" ,tab_device_version_attribute c ");
			}
			
			//recentStartTime recentEndTime
			if (!StringUtil.IsEmpty(recentStartTime) || !StringUtil.IsEmpty(recentEndTime)) {
				sqlCount.append(" ,gw_devicestatus e ");
			}
			
			//查询是否有效用户 是：需要加上关联tab_hgwcustomer，hgwcust_serv_info
			if ("1".equals(is_ableUser)) {
				if (1 == gw_type) {
					sqlCount.append(",tab_hgwcustomer b,");
					sqlCount.append("(select distinct user_id from hgwcust_serv_info where serv_type_id in (10,11,14)) d ");
				}else {
					sqlCount.append(",tab_egwcustomer b,");
					sqlCount.append("(select distinct user_id from egwcust_serv_info where serv_type_id in (10,11,14)) d ");
				}
			}
			sqlCount.append(" where a.devicetype_id=t.devicetype_id ");
			
			//查询是否天翼网关，是否支持测速，是否支持千兆 关联条件 
			if (!StringUtil.IsEmpty(is_esurfing)||  !StringUtil.IsEmpty(gbbroadband) 
					|| !StringUtil.IsEmpty(is_speedTest)) {
				sqlCount.append(" and t.devicetype_id=c.devicetype_id ");
			}
			
			if (!StringUtil.IsEmpty(recentStartTime) || !StringUtil.IsEmpty(recentEndTime)) {
				sqlCount.append(" and a.device_id=e.device_id ");
			}
			
			//查询是否有效用户 是：关联条件
			if ("1".equals(is_ableUser)) {
				sqlCount.append(" and a.device_id = b.device_id and b.user_id = d.user_id");
			}
			sqlCount.append(" and a.device_status=1 and a.customer_id is not null and a.gw_type="+ gw_type);
			sqlDevice=sqlCount.toString();
		}
		else
		{
			StringBuffer sb=new StringBuffer();
			if(DBUtil.GetDB()==3){
				sb.append("select devicetype_id,loid,serv_account,cpe_mac,device_id,");
				sb.append("device_id_ex,city_id,vendor_id,device_serialnumber,area_id ");
			}else{
				sb.append("select * ");
			}
			sb.append("from tab_gw_device a,tab_devicetype_info t ");
			sb.append("where a.devicetype_id=t.devicetype_id and a.device_status=1 ");
			sb.append("and a.customer_id is not null and a.gw_type="+ gw_type);
			
			sqlDevice=sb.toString();
		}
		
		// 查找设备条件
		String str_devicetype_id = request.getParameter("devicetype_id");
		// 修改
		String devicetype = request.getParameter("devicetype");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String cityId = request.getParameter("city_id");// 修改处
		String cityIdSelect = request.getParameter("cityIdSelect");
		String citynext = request.getParameter("citynext");
		String str_deviceModelId = request.getParameter("deviceModelId");
		String is_normal = request.getParameter("is_normal");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (Global.JLDX.equals(Global.instAreaShortName))
		{
			if ("0".equals(use_state))
			{
				sqlDevice += " and a.cpe_allocatedstatus = 0";
			}
			else if ("1".equals(use_state))
			{
				sqlDevice += " and a.cpe_allocatedstatus = 1";
			}
		}
		if (Global.JLDX.equals(Global.instAreaShortName)
				&& gw_type == 4)
		{
			if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal)
					&& !"0".equals(is_normal))
			{
				sqlDevice += " and t.is_normal =" + is_normal;
				fileter += "&is_normal=" + is_normal;
			}
		}else if(Global.JXDX.equals(Global.instAreaShortName))
		{
			StringBuffer sqlCount=new StringBuffer();
			//是否支持Ipv6
			if (!StringUtil.IsEmpty(ip_type)) {
				sqlCount.append(" and t.ip_type = "+ip_type+" ");
				fileter += "&ip_type=" + ip_type;
			}
			
			//查询是否天翼网关，是否支持测速，是否支持千兆 关联条件 
			if (!StringUtil.IsEmpty(is_esurfing)) {
				sqlCount.append(" and c.is_tyGate="+is_esurfing+" ");
				fileter += "&is_esurfing=" + is_esurfing;
			}
			if (!StringUtil.IsEmpty(gbbroadband)) {
				sqlCount.append(" and c.gbbroadband="+gbbroadband+" ");
				fileter += "&gbbroadband=" + gbbroadband;
			}
			if (!StringUtil.IsEmpty(is_speedTest)) {
				sqlCount.append(" and c.is_speedTest="+is_speedTest+" ");
				fileter += "&is_speedTest=" + is_speedTest;
			}
			
		    //最近上报时间
			if (!StringUtil.IsEmpty(recentStartTime)) {
				sqlCount.append(" and  e.last_time>"+recentStartTime+" ");
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (!StringUtil.IsEmpty(recentEndTime)) {
				sqlCount.append(" and  e.last_time<"+recentEndTime+" ");
				fileter += "&recentEndTime=" + recentEndTime;
			}
			
			//查询是否有效用户 是：关联条件
			if ("1".equals(is_ableUser)) {
				sqlCount.append("  and a.cpe_allocatedstatus = 1 ");
				fileter += "&is_ableUser=" + is_ableUser;
			}else if ("0".equals(is_ableUser)) {
				//否：加上cpe_allocatedstatus = 0
				sqlCount.append(" and a.cpe_allocatedstatus = 0 ");
				fileter += "&is_ableUser=" + is_ableUser;
			}
			
			sqlDevice+=sqlCount.toString();
		}
		else
		{
			if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal)
					&& !"null".equals(is_normal))
			{
				sqlDevice += " and t.is_normal =" + is_normal;
				fileter += "&is_normal=" + is_normal;
			}
		}
		if ((rela_dev_type_id != null) && (!rela_dev_type_id.equals("-1"))
				&& (!rela_dev_type_id.equals("")) && (!rela_dev_type_id.equals("null")))
		{
			sqlDevice = sqlDevice + " and t.rela_dev_type_id =" + rela_dev_type_id;
			fileter = fileter + "&rela_dev_type_id=" + rela_dev_type_id;
		}
		if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals("") && !str_devicetype_id.equals("null"))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			fileter += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals("") && !str_vendor_id.equals("null"))// 修
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter += "&vendor_id=" + str_vendor_id;
		}
		if (str_deviceModelId != null && !str_deviceModelId.equals("-1")
				&& !str_deviceModelId.equals("") && !str_deviceModelId.equals("null"))
		{
			sqlDevice += " and a.device_model_id ='" + str_deviceModelId + "' ";
		}
		if (null != startTime && !"".equals(startTime))
		{
			sqlDevice += " and a.complete_time >= " + startTime;
			fileter += "&startTime=" + startTime;
		}
		if (null != endTime && !"".equals(endTime))
		{
			sqlDevice += " and a.complete_time <= " + endTime;
			fileter += "&endTime=" + endTime;
		}
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			logger.warn("recentStartTime : " + recentStartTime + " " + "recentEndTime : " + recentEndTime);
			if (null != recentStartTime && !"".equals(recentStartTime))
			{
				sqlDevice += " and b.last_time >= " + recentStartTime;
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (null != recentEndTime && !"".equals(recentEndTime))
			{
				sqlDevice += " and b.last_time <= " + recentEndTime;
				fileter += "&recentEndTime=" + recentEndTime;
			}
		}
		if (Global.JLDX.equals(Global.instAreaShortName)
				&& gw_type == 4)
		{
			if (str_deviceModelId != null && !str_deviceModelId.equals("-1")
					&& !str_deviceModelId.equals("") && !str_deviceModelId.equals("null"))
			{
				sqlDevice += " and a.device_model_id ='" + str_deviceModelId + "' ";
			}
			if ((devicetype != null) && (!devicetype.equals(""))
					&& (!devicetype.equals("-1")) && (!devicetype.equals("null")))
			{
				sqlDevice += " and a.devicetype_id =" + devicetype;
				fileter += "&devicetype_id=" + devicetype;
			}
			// 如果为-1，则展示当前用户地市下所有设备信息
			if ("-1".equals(str_city_id))
			{
				// 如果为-1，则展示当前用户地市下所有设备信息
				str_city_id = curUser.getCityId();
				List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
				sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
			}
			else
			{
				List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(cityId)
						+ "') ";
			}
		}
		if (Global.HBLT.equals(Global.instAreaShortName))
		{
			String city_temp = str_city_id;
			if(gw_type == 4)
			{
				if ("-1".equals(str_city_id))
				{
					// 如果为-1，则展示当前用户地市下所有设备信息
					str_city_id = curUser.getCityId();
					List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
					sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
				}
				else
				{
					// hb_lt除了省中心外不选二级属地时其余均展示所有信息，选了二级属地时展示具体信息
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)) && ("-1".equals(citynext) || "00".equals(citynext)))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(city_temp);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(city_temp)
							+ "') ";
					}
					fileter += "&cityIdSelect=" + cityIdSelect;
					fileter += "&citynext=" + citynext;
				}
			}
			else if (gw_type == 1) //河北联通家庭网关按版本统计，点击地区对应的数字显示的结果不对代码修改 
			{
				if ("-1".equals(str_city_id))
				{
					// 如果为-1，则展示当前用户地市下所有设备信息
					str_city_id = curUser.getCityId();
					List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
					logger.warn("cityList.size ：   "+cityList.size());
					sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
				}
				else
				{
					// hb_lt除了省中心外不选二级属地时其余均展示所有信息，选了二级属地时展示具体信息
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)))
					{
						
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(city_temp)
								+ "') ";
					}
					else
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(city_temp);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					fileter += "&cityIdSelect=" + cityIdSelect;
					fileter += "&citynext=" + citynext;
				}
			}
			
		}
		else
		{
			if (null == str_city_id || "".equals(str_city_id))
			{
				str_city_id = curUser.getCityId();
			}
			if (str_city_id.equals(curUser.getCityId()))
			{
				sqlDevice += " and a.city_id = '" + str_city_id + "' ";
			}
			else
			{
				String type = curUser.getCityId();
				if ("00".equals(str_city_id))
				{
					sqlDevice += " and a.city_id = '00' ";
					fileter += "&city_id=" + str_city_id;
				}
				else
				{
					String city_temp = str_city_id;
					if ("-1".equals(str_city_id))
					{
						// 如果为-1，则展示当前用户地市下所有设备信息
						str_city_id = curUser.getCityId();
						List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList)
								+ ") ";
					}
					else if (str_city_id.length() == 3)
					{
						if ("00".equals(type))
						{
							List cityList = CityDAO
									.getAllNextCityIdsByCityPid(str_city_id);
							sqlDevice += " and a.city_id in ("
									+ StringUtils.weave(cityList) + ") ";
						}
						else
						{
							sqlDevice += " and a.city_id in ('" + str_city_id + "') ";
						}
					}
					else
					{
						sqlDevice += " and a.city_id in (" + str_city_id + ") ";
					}
				}
			}
		}
		if (Global.JLDX.equals(Global.instAreaShortName)
				&& gw_type == 4)
		{
			fileter += "&city_id=" + cityId;
		}
		else
		{
			fileter += "&city_id=" + str_city_id;
		}
		logger.warn("sqlDevice:{}",sqlDevice);
		PrepareSQL sql = new PrepareSQL(sqlDevice);
		cursor = DataSetBean.getCursor(sql.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		return cursor;
	}
	
	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	public Map getAreaIdMapName()
	{
		String sql = "select area_id,area_name from tab_area";
		Map result = new HashMap();
		PrepareSQL psql = new PrepareSQL(sql);
		cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		while (fields != null)
		{
			result.put(fields.get("area_id"), fields.get("area_name"));
			fields = cursor.getNext();
		}
		return result;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		
	}
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public Map getFields() {
		return fields;
	}

	public void setFields(Map fields) {
		this.fields = fields;
	}

	public Map getSession() {
		return session;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getRela_dev_type_id() {
		return rela_dev_type_id;
	}

	public void setRela_dev_type_id(String rela_dev_type_id) {
		this.rela_dev_type_id = rela_dev_type_id;
	}

	public String getDevicetype_id() {
		return devicetype_id;
	}

	public void setDevicetype_id(String devicetype_id) {
		this.devicetype_id = devicetype_id;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getIsBind() {
		return isBind;
	}

	public void setIsBind(String isBind) {
		this.isBind = isBind;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIs_normal() {
		return is_normal;
	}

	public void setIs_normal(String is_normal) {
		this.is_normal = is_normal;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getCitynext() {
		return citynext;
	}

	public void setCitynext(String citynext) {
		this.citynext = citynext;
	}

	public String getRecentStartTime() {
		return recentStartTime;
	}

	public void setRecentStartTime(String recentStartTime) {
		this.recentStartTime = recentStartTime;
	}

	public String getRecentEndTime() {
		return recentEndTime;
	}

	public void setRecentEndTime(String recentEndTime) {
		this.recentEndTime = recentEndTime;
	}

	public String getCityIdSelect() {
		return cityIdSelect;
	}

	public void setCityIdSelect(String cityIdSelect) {
		this.cityIdSelect = cityIdSelect;
	}

	public String getIs_esurfing() {
		return is_esurfing;
	}

	public void setIs_esurfing(String is_esurfing) {
		this.is_esurfing = is_esurfing;
	}

	public String getIp_type() {
		return ip_type;
	}

	public void setIp_type(String ip_type) {
		this.ip_type = ip_type;
	}

	public String getGbbroadband() {
		return gbbroadband;
	}

	public void setGbbroadband(String gbbroadband) {
		this.gbbroadband = gbbroadband;
	}

	public String getIs_speedTest() {
		return is_speedTest;
	}

	public void setIs_speedTest(String is_speedTest) {
		this.is_speedTest = is_speedTest;
	}

	public String getIs_ableUser() {
		return is_ableUser;
	}

	public void setIs_ableUser(String is_ableUser) {
		this.is_ableUser = is_ableUser;
	}

	public String getStb_m_Vendor_SQL() {
		return stb_m_Vendor_SQL;
	}

	public void setStb_m_Vendor_SQL(String stb_m_Vendor_SQL) {
		this.stb_m_Vendor_SQL = stb_m_Vendor_SQL;
	}

	public String getM_Vendor_SQL() {
		return m_Vendor_SQL;
	}

	public void setM_Vendor_SQL(String m_Vendor_SQL) {
		this.m_Vendor_SQL = m_Vendor_SQL;
	}

}
