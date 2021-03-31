package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class QualityServiceAction {

	private static Logger logger = LoggerFactory.getLogger(QualityServiceAction.class);
	private Cursor cursor = null;
	private Map fields = null;
	private PrepareSQL pSQL;
	private Map cityTotalMap = new HashMap();
	private Map cityListMap = new HashMap();
	private Map versionMap = null;
	private Map vendorMap = null;
	// 同一个设备版本小于20的版本id
	private String edtionIdStr = null;
//	private List cityIdList = new ArrayList();

	public QualityServiceAction() {
		pSQL = new PrepareSQL();
		getVendorMap(1);
		getVersionMap(1);
	}

	/**
	 * 获取属地
	 */
	public String getCityId(Cursor cursor) {
		StringBuffer bf = new StringBuffer();
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		/*cursor = getCityList(request);*/
		fields = cursor.getNext();
		if (null != fields)
		{
			while (null != fields)
			{
				String city_id = String.valueOf(fields.get("city_id"));
				String city_name = String.valueOf(fields.get("city_name"));
				if (null != city_id && city_id!="")
				{
					bf.append("<option value='" + city_id + "' >==" + city_name
							+ "==</option>");
				}
				else
				{
					bf.append("<option value='" + city_id + "'>==" + city_name
							+ "==</option>");
				}
				fields = cursor.getNext();
			}
		}
		else
		{
			bf.append("<option value='-1'>==请选择==</option>");
		}
		return bf.toString();
	}
	
	/**
	 * 按版本统计设备数量
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByEdition(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCityID = curUser.getCityId();
		
		String startTime = getRightTime(request.getParameter("start_Time"), request.getParameter("startTime"));
		String endTime = getRightTime(request.getParameter("end_Time"), request.getParameter("endTime"));

		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
//		String sqlid = request.getParameter("sqlid");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (null == gw_type_Str)
		{
			gw_type_Str ="1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		
		
		
//		getVendorMap(gw_type);
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String is_esurfing = request.getParameter("is_esurfing");//天翼网关更改的地方  jiangkun 
		logger.debug("deviceTypeId:{}", deviceTypeId);
//		String city_id = request.getParameter("city_id");//修改处
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
//		String use_state = request.getParameter("use_state");
		
		// 初始化版本信息
//		getVersionMap(gw_type);
		String is_normal = request.getParameter("is_normal");

		// 判断是否有业务 改
		String rela_dev_type_id = request.getParameter("rela_dev_type_id");
		String access_style_relay_id = request.getParameter("access_style_relay_id");
		Map mapService = getEditionMap(vendorId, modelId, deviceTypeId, edtionIdStr,
				rela_dev_type_id, access_style_relay_id, userCityID,gw_type,is_esurfing);
		if (null == mapService) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		
		// 输出表头
		StringBuffer sbTable = new StringBuffer();
		sbTable.append("<TR>");
//		sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH><TH style='word-break:keep-all'>是否规范版本</TH>");
		sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH>");
		
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
//		if(gw_type == 4) {
//			cursor = getCity(city_id);
//		}
//		else {
			cursor = getCityList(request);
//		}
		fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		sbTable.append("<TH nowrap>小计</TH></TR>");
		getNextCityList(cityList);
		// 初始化地市小计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Iterator cityIt = cityMap.keySet().iterator();
		cityTotalMap = new HashMap();
		while (cityIt.hasNext())
		{
			cityTotalMap.put(cityIt.next(), "0");
		}
		// 将统计信息放入对应的map中
		String tmp = "";
		long vendortmp = 0;
//		long total = 0;
		Map mapServiceCityNum = new HashMap();
//		String sql1;
//		if(4 == gw_type){
//			if(Global.HBLT.equals(Global.instAreaShortName))
//			{
//				sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from stb_tab_gw_device a, stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status = 1 ";
//				pSQL.setSQL(sql1);
//			}
//			else
//			{
//				sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from stb_tab_gw_device a,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 ";
//				pSQL.setSQL(sql1);
//			}
//		}
		String tablename = "tab_gw_device";
		if(Global.HBDX.equals(Global.instAreaShortName)) {
			tablename = "REPORT_GW_DEVICE_2018";
		}
//		if("1".equals(is_esurfing)) {
//			sql1="select count(a.device_id) as num,a.devicetype_id,a.city_id from " +
//					" tab_gw_device a, tab_devicetype_info t ,tab_device_version_attribute c " +
//					" where a.devicetype_id = t.devicetype_id and t.devicetype_id=c.devicetype_id " +
//					" and a.device_status = 1 and a.customer_id is not null and a.gw_type=? ";
//			pSQL.setSQL(sql1);
//			pSQL.setInt(1, gw_type);
//		}
//		else { 
		String sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from " +
				tablename + " a, tab_devicetype_info t " +
				" where a.devicetype_id = t.devicetype_id and a.device_status = 1 " +
				" and a.customer_id is not null and a.gw_type=? ";
		pSQL.setSQL(sql1);
		pSQL.setInt(1, gw_type);
//		}


		StringBuffer sql = new StringBuffer();
		sql.append(pSQL.getSQL());
		// 是否规范
		if (null != is_normal && !"null".equals(is_normal) && !"-1".equals(is_normal)
				&& !"true".equals(is_normal))
		{
			sql.append(" and t.is_normal=" + is_normal);
		}

		// add in JXDX-ITMS-REQ-20180926-WUWF-001(ITMS平台集团光网质量提升报表一需求)
		// 获取质量提升查询time对应的sql
		sql.append(getSearchTimeSql(startTime, endTime));


		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId)&& vendorId !="")//修改
		{
			sql.append(" and a.vendor_id='" + vendorId + "' ");
		}
		else {
			sql.append(" and a.vendor_id in (select vendor_id from tab_quality_temporary group by vendor_id )");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			sql.append(" and a.device_model_id='" + modelId + "' ");
		}
		else {
			sql.append(" and a.device_model_id in (select device_model_id from tab_quality_temporary group by device_model_id )");
		}
		if (null != deviceTypeId && !"null".equals(deviceTypeId)
				&& !"-1".equals(deviceTypeId))
		{
			sql.append(" and a.devicetype_id=" + deviceTypeId + " ");
		}
		else {
			sql.append(" and a.devicetype_id in (select devicetype_id from tab_quality_temporary group by devicetype_id )");
		}
		// 改
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			sql.append(" and t.rela_dev_type_id=" + rela_dev_type_id);
		}
		if (null != access_style_relay_id && !"null".equals(access_style_relay_id)
				&& !"-1".equals(access_style_relay_id))
		{
			sql.append(" and t.access_style_relay_id=" + access_style_relay_id);
		}
//		if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
//		{
//			sql.append(" and a.city_id in (");
//			for (int i = 0; i < cityIdList.size(); i++)
//			{
//				String cityId = StringUtil.getStringValue(cityIdList.get(i));
//				if (i + 1 == cityIdList.size())
//				{
//					sql.append("'" + cityId + "')");
//				}
//				else
//				{
//					sql.append("'" + cityId + "',");
//				}
//			}
//		}
		//天翼网关更改的地方  jiangkun
//		if("1".equals(is_esurfing)) {
//			sql.append(" and c.is_tyGate="+is_esurfing);
//		}
		sql.append(" group by a.devicetype_id,a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql.toString());
		
		fields = cursor.getNext();
		while (fields != null)
		{
			if (cityMap.get(fields.get("city_id")) != null)
			{
				// 将业务+属地==>num 作为映射存入map中
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("devicetype_id"),
						fields.get("num"));
				tmp = (String) fields.get("num");
				if ((tmp == null) || "".equals(tmp))
				{
					tmp = "0";
				}
//				total += Long.parseLong(tmp);
				// 按厂商累加，将小计信息放入cityTotalMap sssss
				vendortmp = Long.parseLong(tmp) + Long.parseLong((String) cityTotalMap.get(fields.get("city_id")));
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
			}
			fields = cursor.getNext();
		}
		
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext()) {
			String key = (String) itVender.next();
			Map valueMap = (Map) mapService.get(key);
			int size = 0;
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext()) {
				size = size + ((List) valueMap.get(itDevicetype.next())).size();
			}
			mapServiceSize.put(key, size);
		}
		// 开始遍历属地,输出统计数据
//		String isPrt = request.getParameter("isPrt");

		Iterator it = mapService.keySet().iterator();
//		StringBuffer sbTableVendor = null;//
//		StringBuffer sbTableModel = null;//
//		int vendorSize = 0;//
//		int modelSize = 0;//
		while (it.hasNext()) {
			
			String key = (String) it.next();
//	        sbTableVendor = new StringBuffer();//

			Map valueMap = (Map) mapService.get(key);
			Iterator itDevicetype = valueMap.keySet().iterator();
//			vendorSize = ((Integer)mapServiceSize.get(key)).intValue();//
			while (itDevicetype.hasNext()) {
				
				String keyModel = (String) itDevicetype.next();
				List devicetypeList = (List) valueMap.get(keyModel);
//				modelSize = devicetypeList.size();
//		        sbTableModel = new StringBuffer();

				for (int i = 0; i < devicetypeList.size(); i++) {

					String devicetypeStr = String.valueOf(devicetypeList.get(i));
//					String tmpRow = "";

					getVendorStatRowHtml(devicetypeStr, keyModel, cityList, mapServiceCityNum, gw_type, userCityID,
									isBind, key);
						
				}
			}
		}
		
		if (dataList == null || dataList.isEmpty()) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		// 给数据排序
		Collections.sort(dataList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return (Integer)o2.get("total") - (Integer)o1.get("total");
			}
        });
		String tdbeg = "<TD nowrap class=column align='right'>";
		String tdend = "</TD>";
		for (Map<String, Object> dataMap : dataList) {
			sbTable.append("<TR>")
				   .append(tdbeg).append(StringUtil.getStringValue(dataMap, "vendorName", "")).append(tdend)
				   .append(tdbeg).append(StringUtil.getStringValue(dataMap, "modelName", "")).append(tdend)
				   .append(tdbeg).append(StringUtil.getStringValue(dataMap, "softwareversion", "")).append(tdend)
				   .append(tdbeg).append(StringUtil.getStringValue(dataMap, "hardwareversion", "")).append(tdend)
//				   .append(tdbeg).append(StringUtil.getStringValue(dataMap, "normalResult", "")).append(tdend)
				   .append(StringUtil.getStringValue(dataMap, "dataStr", ""))
				   .append("</TR>");
		}

		// Clear Resouce
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 吉林质量提升统计报表
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByEditionJLDX(HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		String userCityID = curUser.getCityId();
		
		String startTime = getRightTime(request.getParameter("start_Time"), request.getParameter("startTime"));
		String endTime = getRightTime(request.getParameter("end_Time"), request.getParameter("endTime"));

//		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
//		String gw_type_Str = request.getParameter("gw_type");
//		if (StringUtil.IsEmpty(gw_type_Str)) {
//			gw_type_Str ="1";
//		}
//		int gw_type = Integer.parseInt(gw_type_Str);
		
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");

		// 输出表头
		StringBuffer sbTable = new StringBuffer();
		sbTable.append("<TR>");
//		sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH><TH>数量</TH>");
		sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>硬件版本</TH><TH>数量</TH>");
		
		StringBuffer sb = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sb.append("select num, vendor_id, device_model_id, hardwareversion from (");
		}
		else {
			sb.append("select top 10 * from (");
		}
		sb.append(" select count(a.device_id) as num, t.vendor_id, t.device_model_id, t.hardwareversion from ");
		sb.append(" tab_gw_device a, tab_vendor b, gw_device_model c, tab_devicetype_info t ");
		sb.append(" where a.devicetype_id = t.devicetype_id and t.vendor_id = b.vendor_id and t.device_model_id = c.device_model_id");
		sb.append(getSearchTimeSql(startTime, endTime));
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sb.append(" and t.vendor_id='" + vendorId + "' ");
		}
		else {
			sb.append(" and t.vendor_id in (select vendor_id from tab_quality_temporary group by vendor_id )");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sb.append(" and t.device_model_id='" + modelId + "' ");
		}
		else {
			sb.append(" and t.device_model_id in (select device_model_id from tab_quality_temporary group by device_model_id )");
		}
		if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)) {
			sb.append(" and t.devicetype_id =" + deviceTypeId + " ");
		}
		else {
			sb.append(" and t.devicetype_id in (select devicetype_id from tab_quality_temporary group by devicetype_id )");
		}
		sb.append(" and a.device_status = 1 and a.customer_id is not null ");
		sb.append(" and a.gw_type = 1 group by t.vendor_id, t.device_model_id, t.hardwareversion) as tablename order by num desc ");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sb.append(" limit 10");
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.getSQL();
//		cursor = DataSetBean.getCursor(sb.toString());
		ArrayList<Map<String, String>> list = DataSetBean.executeQuery(sb.toString(), null);
//		fields = cursor.getNext();
		
		if (null == list || list.isEmpty()) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>没有查询到相应记录!</TD></TR>";
		}
		
		String tdbeg = "<TD nowrap class=column align='right'>";
		String tdend = "</TD>";
//		String jsStr = "detail('?','#','" + 1 + "','" + null + "')";
		for (Map<String, String> map : list) {
			
			String num = StringUtil.getStringValue(map, "num", "");
			
			if (StringUtil.IsEmpty(num) || "0".equals(num)) {
				num = "0";
			}
			String vendorId_ = StringUtil.getStringValue(map, "vendor_id", "");
			String deviceModelId = StringUtil.getStringValue(map, "device_model_id", "");
			String vendorName = StringUtil.getStringValue(vendorMap, vendorId_, "");
			String deviceModel = StringUtil.getStringValue(versionMap, vendorId_ + "#" + deviceModelId, "");
			sbTable.append("<TR>")
				   .append(tdbeg).append(vendorName).append(tdend)
				   // 型号
				   .append(tdbeg).append(deviceModel).append(tdend)
				   // 软件版本
//				   .append(tdbeg).append(tmp[2]).append(tdend)
				   // 硬件版本
				   .append(tdbeg).append(StringUtil.getStringValue(map, "hardwareversion", "")).append(tdend)
				   // 数量
//				   .append(tdbeg).append("<a href=javascript:onclick="
//						   + jsStr.replaceAll("\\?", "null").replaceAll("\\#", devicetypeId) + ">" + num + "</a>").append(tdend)
				   .append(tdbeg).append(num).append(tdend)

				   .append("</TR>");
		}
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	/**
	 * add in JXDX-ITMS-REQ-20180926-WUWF-001(ITMS平台集团光网质量提升报表一需求)
	 * 获取质量提升查询time对应的sql
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private String getSearchTimeSql(String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.IsEmpty(startTime)) {
			sb.append(" and a.complete_time").append(" > ").append(startTime);
		}
		if (!StringUtil.IsEmpty(endTime)) {
			sb.append(" and a.complete_time").append(" < ").append(endTime);
		}
		return sb.toString();
	}

	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param devicetype_id 版本id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private void getVendorStatRowHtml(String devicetype_id, String modelName, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String isBind, String vendor_id) {
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','" + gw_type + "','" + isBind + "')";
		int total = 0;
		String city_id = "";
		
		// 厂商名称
		String vendorName = StringUtil.getStringValue(vendorMap, vendor_id, "");
		
		// 输出业务名称
		String message = StringUtil.getStringValue(versionMap, devicetype_id, "");
		logger.debug("getVendorStatRowHtml => message:" + message);
		String[] tmp = message.split("\\|");
		
		if (StringUtil.IsEmpty(message) || tmp == null || tmp.length < 4) {
			logger.warn("错误message:[{}], 过滤不统计", message);
			return;
		}
		// 软件版本
		String softwareversion = tmp[2];
		// 硬件版本
		String hardwareversion = tmp.length == 5 ? tmp[4]:"";
		// 是否规范
		String normalResult = tmp[3];
			
		while (it.hasNext()) {
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null) {
				if (userCityID.equals(city_id)) {
					num = (String) mapServiceCityNum.get(city_id + "_" + devicetype_id);
				}
				else {
					num = getCityVendorCount(cityAll, devicetype_id, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num)) {
					
					if (!"".equals(num)) {
						total += Integer.parseInt(num);
					}
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#", devicetype_id) + ">" + num + "</a></TD>");
				}
				else {
					
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null) {
				
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		
		// 总数为0,过滤该数据不统计
		if (total == 0) {
			return;
		}
		// 拼接总数
		sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
				+ jsStr.replaceAll("\\#", devicetype_id).replaceAll("\\?", "-1") + ">"
				+ total + "</TD></TR>");
		
		// 将一行结果加入list
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vendorName", vendorName);
		map.put("modelName", modelName);
		map.put("softwareversion", softwareversion);
		map.put("hardwareversion", hardwareversion);
		map.put("normalResult", normalResult);
		map.put("total", total);
		map.put("dataStr", sb.toString());
		dataList.add(map);
	}
	
	/**
	 * 版本信息列表Map<vendor_id,Map<device_model,List<devicetype_id>>>
	 * 
	 * @return
	 */
	public Map getEditionMap(String vendorId, String modelId, String deviceTypeId,
			String edtionIdStr, String rela_dev_type_id, String access_style_relay_id,
			String userCityID,int gw_type,String is_esurfing)
	{
		StringBuffer bf = new StringBuffer();
		
		if("1".equals(is_esurfing))
		{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "tab_devicetype_info a,gw_device_model b,tab_device_version_attribute c where a.device_model_id=b.device_model_id and a.devicetype_id=c.devicetype_id");
		}
		else{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
		}
		//天翼网关修改的地方
//		if("1".equals(is_esurfing))
//		{
//			bf.append(" and c.is_tyGate="+is_esurfing);
//		}
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			bf.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			bf.append(" and b.device_model_id='" + modelId + "' ");
		}
		if (null != deviceTypeId && !"null".equals(deviceTypeId)
				&& !"-1".equals(deviceTypeId))
		{
			bf.append(" and a.devicetype_id=" + deviceTypeId + " ");
		}
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			bf.append(" and a.rela_dev_type_id=" + rela_dev_type_id);
		}
		if (null != access_style_relay_id && !"null".equals(access_style_relay_id)
				&& !"-1".equals(access_style_relay_id))
		{
			bf.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
//		if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
//		{
//			bf.append(" and a.devicetype_id  in " + edtionIdStr
//					+ " order by  a.vendor_id,device_model,devicetype_id ");
//		}
		bf.append(" order by  a.vendor_id,device_model,devicetype_id ");
		logger.debug("bf.toString:{}", bf.toString());
		PrepareSQL psql = new PrepareSQL(bf.toString());
		psql.getSQL();
		cursor = DataSetBean.getCursor(bf.toString());
		fields = cursor.getNext();
		if (fields == null) {
			return null;
		}
		Map<String, Map> mapService = new HashMap<String, Map>();
		while (fields != null)
		{
			String vendor_id = String.valueOf(fields.get("vendor_id"));
			String device_model = String.valueOf(fields.get("device_model"));
			String devicetype_id = String.valueOf(fields.get("devicetype_id"));
			Map<String, List> modelMap = null;
			if (null == mapService.get(vendor_id))
			{
				modelMap = new HashMap<String, List>();
			}
			else
			{
				modelMap = mapService.get(vendor_id);
			}
			List<String> devicetypeList = null;
			if (null == modelMap.get(device_model))
			{
				devicetypeList = new ArrayList<String>();
			}
			else
			{
				devicetypeList = modelMap.get(device_model);
			}
			devicetypeList.add(devicetype_id);
			modelMap.put(device_model, devicetypeList);
			mapService.put(vendor_id, modelMap);
			fields = cursor.getNext();
		}
		return mapService;
	}

	
	/**
	 * 获取当前用户的下级属地
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getCityList(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String m_cityList="";
//		if("cq_dx".equals(Global.instAreaShortName))
//		{
//			List list=new ArrayList();
//			String[] city=city_id.split(",");
//			for(int i=0;i<city.length;i++)
//			{
//				list.add(city[i]);
//			}
//			m_cityList = "select city_id,city_name from tab_city where parent_id in("
//					+ StringUtils.weave(list) + ") or city_id in(" + StringUtils.weave(list) + ") order by city_id";
//		}else
//		{
		 m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "' order by city_id";
//		 }
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}
	/**
	 * 获取当前用户的下级属地
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getCity(String city_id)
	{
		/*HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();*/
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}
	/**
	 * 将传入的属地列表中所有数据相加
	 * 
	 * @param cityAll
	 * @param editionId
	 * @param tmpMap
	 * @return
	 */
	private String getCityVendorCount(ArrayList cityAll, String editionId, Map tmpMap)
	{
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		Iterator it = cityAll.listIterator();
		while (it.hasNext())
		{
			tmpCity = (String) it.next();
			tmp = (String) tmpMap.get(tmpCity + "_" + editionId);
			if (tmp != null && !"".equals(tmp))
			{
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		return String.valueOf(total);
	}

	/**
	 * 将各地市的下级属地放入MAP中
	 * 
	 * @param cityList
	 */
	private void getNextCityList(List cityList)
	{
		Iterator it = cityList.iterator();
		String city_id = "";
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
			cityListMap.put(city_id, cityAll);
			cityAll = null;
		}
	}

	/**
	 * 将软件版本等信息放入MAP中
	 */
	private void getVersionMap(int gw_type) {
		String sql = "select a.softwareversion, a.devicetype_id, b.device_model, c.vendor_id, "
				+ " a.hardwareversion, a.is_normal, c.vendor_name, a.device_model_id "
				+ " from tab_devicetype_info a, gw_device_model b,tab_vendor c "
				+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		if (versionMap == null) {
			versionMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null) {
				String normalResult = "1".equals(fields.get("is_normal")) ? "是" : "否";
				String version = (String) fields.get("vendor_id") + "|"
							+ (String) fields.get("device_model") + "|"
							+ (String) fields.get("softwareversion") + "|" 
							+ normalResult + "|" 
							+ (String) fields.get("hardwareversion");
				versionMap.put(fields.get("devicetype_id"), version);
				versionMap.put(fields.get("vendor_id") + "#" + fields.get("device_model_id"), fields.get("device_model"));
				fields = cursor.getNext();
			}
		}
	}

	/**
	 * 将厂商信息放入MAP中
	 */
	private void getVendorMap(int gw_type) {
		String sql = "select * from tab_vendor order by vendor_add";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select vendor_add, vendor_id, vendor_name from tab_vendor order by vendor_add";
		}

		if (vendorMap == null) {
			vendorMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null) {
				String vendor_add = (String) fields.get("vendor_add");
				if (vendor_add != null && !"".equals(vendor_add)) {
					vendorMap.put(fields.get("vendor_id"),
							vendor_add + "(" + fields.get("vendor_id") + ")");
				}
				else {
					vendorMap.put(fields.get("vendor_id"), fields.get("vendor_name")
							+ "(" + fields.get("vendor_id") + ")");
				}
				fields = cursor.getNext();
			}
		}
	}

	// 质量提升创建的临时表
	private String tableName = "tab_quality_temporary";

	/**
	 * 获取厂商下拉
	 * @param vendorId
	 * @param gw_type
	 * @return
	 */
	public String getVendorHtml() {
		StringBuffer bf = new StringBuffer("<option value='-1'>==请选择==</option>");

		String sql = "select a.vendor_id, a.vendor_name, a.vendor_add " +
				"from tab_vendor a, (select vendor_id from " + tableName + " group by vendor_id) b " + 
				"where a.vendor_id = b.vendor_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		
		if (null == fields) {
			return bf.toString();
		}
		while (null != fields) {
			String vendorId_ = String.valueOf(fields.get("vendor_id"));
			String vendorname_ = fields.get("vendor_add") + "(" + fields.get("vendor_name") + ")";
			
			bf.append("<option value='" + vendorId_ + "'>==" + vendorname_ + "==</option>");
			fields = cursor.getNext();
		}
		return bf.toString();
	}

	public String getString(String str) {
		if (str == null || "null".equals(str) || "".equals(str) || str.length() == 0) {
			return "";
		}
		return str;
	}
	
	/**
	 * 获取时间
	 * @param time1
	 * @param time2
	 * @return
	 */
	public String getRightTime(String time1, String time2) {
		if (StringUtil.IsEmpty(getString(time1))) {
			return "";
		}
		return getString(time2);
	}

	/**
	 * 获取设备型号下拉
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId) {
		String sql = "select a.device_model_id, a.device_model " +
				"from gw_device_model a, (select device_model_id from " + tableName 
				+ " where vendor_id = '" + vendorId +"' group by device_model_id) b " + 
				"where a.device_model_id = b.device_model_id";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		
		if (null == fields) {
			return "";
		}
		StringBuffer bf = new StringBuffer();
		while (null != fields) {
			bf.append(fields.get("device_model_id"))
				.append("$")
				.append(fields.get("device_model"))
				.append("#");
			fields = cursor.getNext();
		}
		return bf.deleteCharAt(bf.length() - 1).toString();
	}

	/**
	 * 获取软件版本下拉
	 * @param deviceModelId
	 * @return
	 */
	public String getDevicetype(String deviceModelId) {
		String sql = "select a.devicetype_id, a.softwareversion, a.hardwareversion " +
				"from tab_devicetype_info a, (select devicetype_id from " + tableName 
				+ " where device_model_id = '" + deviceModelId +"' group by devicetype_id) b " + 
				"where a.devicetype_id = b.devicetype_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		
		if (null == fields) {
			return "";
		}
		StringBuffer bf = new StringBuffer();
		while (null != fields) {
			bf.append(fields.get("devicetype_id"))
				.append("$")
				.append(fields.get("hardwareversion"))
				.append("(")
				.append(fields.get("softwareversion"))
				.append(")")
				.append("#");
			fields = cursor.getNext();
		}
		return bf.deleteCharAt(bf.length() - 1).toString();
	}
}
