/**
 * 在线网元统计功能
 * @author 联创科技 1.0
 */
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class OnlineNetAct {
	private Cursor cursor = null;

	private Map fields = null;
	
	private PrepareSQL pSQL;
	
	private Map cityTotalMap = new HashMap();
	
	private Map cityListMap = new HashMap();
	
	private Map versionMap = null;
	private Map vendorMap = null;

	/**
	 * 按属地和业务统计用户设备
	 */
	private String m_ServiceState = "select a.serv_type_id,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and b.device_status =1 and b.gw_type=? and a.serv_type_id in(select serv_type_id from tab_gw_serv_type where type=? ) "
			+ " group by a.serv_type_id,b.city_id";
	

	private String m_out_ServiceState = "select serv_type_id,city_id,count(username) as num"
			+ " from tab_hgwcustomer where service_id in(select service_id from tab_service where flag=0 ) "
			+ " group by serv_type_id,city_id";

//	private String m_vendorDevice = "select count(device_id) as num,oui,city_id from tab_gw_device where oui in(select vendor_id from tab_vendor) and device_status = 1 and gw_type=? group by oui,city_id";
	private String m_vendorDevice = "select count(device_id) as num,oui,city_id from tab_gw_device " +
			" where device_status = 1 and gw_type=? group by oui,city_id";
	
	private String m_editionDevice = "select count(device_id) as num,devicetype_id,city_id from tab_gw_device where devicetype_id in(select devicetype_id from tab_devicetype_info) and device_status = 1 and gw_type=? group by devicetype_id,city_id";
	//在线网元统计
	private String m_OnlineDevice = "select count(device_id) as num,devicetype_id,city_id from tab_gw_device where devicetype_id in(select devicetype_id from tab_devicetype_info) and device_status = 1 and gw_type=2 and complete_time>=? and complete_time<=? and cpe_currentstatus=1 group by devicetype_id,city_id";

	/**
	 * 按属地统计用户设备
	 */
	private String m_CityDevice = "select city_id,count(device_id) as num "
			+ " from tab_gw_device where device_status =1 and gw_type=? " + " group by city_id";

	/**
	 * service_id service_name 关系映射
	 */
	private String m_ServiceInfo = "select service_id,service_name from tab_service";
	
	private String m_ServiceInfo_List = "select serv_type_id,serv_type_name from tab_gw_serv_type where type = ?";

	private String m_VendorInfo = "select vendor_id,vendor_name from tab_vendor";

//	private String m_EditionInfo = "select devicetype_id,device_model from tab_devicetype_info";
	private String m_EditionInfo = "select devicetype_id,device_model from tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id";

	public OnlineNetAct() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 按属地和业务统计用户设备,暂时不考虑权限过滤
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomer(HttpServletRequest request, int gw_type) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 如果是管理员
		if (curUser.getUser().isAdmin()) {
		} else {// 其他用户，则需要权限过滤
		}
		pSQL.setSQL(m_ServiceState);
		pSQL.setInt(1, gw_type);
		pSQL.setInt(2, gw_type);
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	public Cursor getServiceOutHGWCustomer(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 如果是管理员
		if (curUser.getUser().isAdmin()) {
		} else {// 其他用户，则需要权限过滤
		}
		PrepareSQL psql = new PrepareSQL(m_out_ServiceState);
    	psql.getSQL();
		return DataSetBean.getCursor(m_out_ServiceState);
	}

	/**
	 * 按属地统计用户设备
	 * 
	 * @return Map
	 */
	public Map getCityService(int gw_type) {
		
		pSQL.setSQL(m_CityDevice);
		pSQL.setInt(1, gw_type);
		
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		Map cityDevice = new HashMap();
		while (fields != null) {
			cityDevice.put(fields.get("city_id"), fields.get("num"));
			fields = cursor.getNext();
		}

		return cityDevice;
	}

	/**
	 * service_id service_name 关系映射
	 * 
	 * @return 映射MAP
	 */
	public HashMap getServiceMap() {
		PrepareSQL psql = new PrepareSQL(m_ServiceInfo);
    	psql.getSQL();
		return DataSetBean.getMap(m_ServiceInfo);
	}

	/**
	 * 将service_id和service_name分别放在两个链表中
	 * 
	 * @return
	 */
	public List[] getServiceIdNameList(int gw_type) {
		
		pSQL.setSQL(m_ServiceInfo_List);
		pSQL.setInt(1, gw_type);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		List[] listService = new ArrayList[2];
		listService[0] = new ArrayList();// service_id
		listService[1] = new ArrayList();// service_name
		fields = cursor.getNext();
		while (fields != null) {
			listService[0].add(fields.get("serv_type_id"));
			listService[1].add(fields.get("serv_type_name"));
			fields = cursor.getNext();
		}
		
		return listService;
	}

	/**
	 * 已开通业务的设备统计
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceOnService(HttpServletRequest request, int gw_type) {
		// 先读取所有业务
		List[] listService = getServiceIdNameList(gw_type);
		if (listService == null || listService[0].size() == 0) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		StringBuffer sbTable = new StringBuffer();
		List listServiceId = listService[0];
		List listServiceName = listService[1];

		// 输出表头－－>业务名称
		Iterator it = listServiceName.iterator();
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap></TH>");
		while (it.hasNext()) {
			sbTable.append("<TH nowrap>" + it.next() + "</TH>");
		}
		sbTable.append("</TR>");

		// 在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getServiceOfHGWCustomer(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"service_id" VALUE:num
		Map mapServiceCityNum = new HashMap();

		while (fields != null) {
			// 将业务+属地==>num 作为映射存入map中
			mapServiceCityNum.put(fields.get("city_id") + "_"
					+ fields.get("serv_type_id"), fields.get("num"));
			fields = cursor.getNext();
		}

		//开始遍历属地
		//it = cityMap.keySet().iterator();
		Cursor cityList = getCityList(request);
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null){
			if ("1".equals(isPrt)) {
				sbTable.append(getSerStatePrint((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum, null));
			} else {
				sbTable.append(getSerStateRowHtml((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum, null));
			}
			cityField = cityList.getNext();
		}
		
		// Clear Resouce
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 未开通业务的设备统计
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceOutService(HttpServletRequest request,int gw_type) {
		// 先读取所有业务
		List[] listService = getServiceIdNameList(gw_type);
		if (listService == null || listService[0].size() == 0) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		StringBuffer sbTable = new StringBuffer();
		List listServiceId = listService[0];
		List listServiceName = listService[1];

		// 输出表头－－>业务名称
		Iterator it = listServiceName.iterator();
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap></TH>");
		while (it.hasNext()) {
			sbTable.append("<TH nowrap>" + it.next() + "</TH>");
		}
		sbTable.append("</TR>");

		// 在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}

		devAct = null;
		cursor = getServiceOfHGWCustomer(request, gw_type);
		fields = cursor.getNext();

		// KEY:"city_id"_"service_id" VALUE:num
		Map mapServiceCityNum = new HashMap();

		while (fields != null) {
			// 将业务+属地==>num 作为映射存入map中
			mapServiceCityNum.put(fields.get("city_id") + "_"
					+ fields.get("serv_type_id"), fields.get("num"));
			fields = cursor.getNext();
		}

		// 获取各个地市的设备数
		Map cityDevice = getCityService(gw_type);

		//开始遍历属地
		//it = cityMap.keySet().iterator();
		Cursor cityList = getCityList(request);
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null){
			String city_ID = (String) cityField.get("city_id");
			//String cityNum = (String) cityDevice.get(city_ID);
			String cityNum = "";
			if ("00".equals(city_ID)){
				cityNum = (String) cityDevice.get(city_ID);
			}
			else{
				cityNum = getCityCount(city_ID,cityDevice);
			}
			
			if ("1".equals(isPrt)) {
				sbTable.append(getSerStatePrint(city_ID, cityMap,
						listServiceId, mapServiceCityNum, cityNum));
			} else {
				sbTable.append(getSerStateRowHtml(city_ID, cityMap,
						listServiceId, mapServiceCityNum, cityNum));
			}
			cityField = cityList.getNext();
		}

		// Clear Resouce
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放业务id的列表
	 * @param mapServiceCityNum
	 *            业务id+属地id 映射 统计值
	 * @return
	 */
	private String getSerStateRowHtml(String city_id, Map cityMap,
			List listServiceId, Map mapServiceCityNum, String cityNum) {
		Iterator it = listServiceId.iterator();
		ArrayList<String> cityAll = CityDAO.getNextCityIdsByCityPid(city_id);
		
		String servId = null;
		String num = null;
		StringBuffer sb = new StringBuffer(listServiceId.size() + 2);
		String jsStr = "detail('?','#')";
		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='right'>"
				+ cityMap.get(city_id) + "</TD>");
		while (it.hasNext()) {
			servId = (String) it.next();

			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null) {
				//num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				if ("00".equals(city_id)){
					num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				}
				else{
					num = getCityServiceCount(cityAll,servId,mapServiceCityNum);
				}

				if (cityNum != null) {
					long tmp = 0;
					if (num != null) {
						tmp = Long.parseLong(cityNum) - Long.parseLong(num);
					} else {
						tmp = Long.parseLong(cityNum) - 0;
					}

					num = String.valueOf(tmp);
				}

				if (num != null && !"0".equals(num))
					sb
							.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
									+ jsStr.replaceAll("\\?", city_id)
											.replaceAll("\\#", servId)
									+ ">"
									+ num + "</a></TD>");
				else
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			} else if (mapServiceCityNum == null) {
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		sb.append("</TR>");
		cityAll = null;
		return sb.toString();
	}

	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放业务id的列表
	 * @param mapServiceCityNum
	 *            业务id+属地id 映射 统计值
	 * @return
	 */
	private String getSerStatePrint(String city_id, Map cityMap,
			List listServiceId, Map mapServiceCityNum, String cityNum) {
		Iterator it = listServiceId.iterator();
		ArrayList<String> cityAll = CityDAO.getNextCityIdsByCityPid(city_id);
		
		String servId = null;
		String num = null;
		StringBuffer sb = new StringBuffer(listServiceId.size() + 2);

		// 输出地市名称
		sb.append("<TR ><TD nowrap align='right'>" + cityMap.get(city_id)
				+ "</TD>");
		while (it.hasNext()) {
			servId = (String) it.next();

			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null) {
				//num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				if ("00".equals(city_id)){
					num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				}
				else{
					num = getCityServiceCount(cityAll,servId,mapServiceCityNum);
				}

				if (cityNum != null) {
					long tmp = 0;
					if (num != null) {
						tmp = Long.parseLong(cityNum) - Long.parseLong(num);
					} else {
						tmp = Long.parseLong(cityNum) - 0;
					}

					num = String.valueOf(tmp);
				}

				if (num != null && !"0".equals(num))
					sb.append("<TD bgcolor=#ffffff align=center>" + num
							+ "</TD>");
				else
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			} else if (mapServiceCityNum == null) {
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		cityAll = null;
		if (cityTotalMap != null){
			sb.append("<TD bgcolor=#ffffff align=center>" + cityTotalMap.get(city_id)
					+ "</TD>");
		}
		sb.append("</TR>");
		return sb.toString();
	}
	
	private String getCityServiceCount(ArrayList cityAll,String servId,Map tmpMap){
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		
		Iterator it = cityAll.listIterator();
		
		while (it.hasNext()){
			tmpCity = (String)it.next();
			tmp = (String)tmpMap.get(tmpCity + "_" + servId);
			
			if (tmp != null && !"".equals(tmp)){
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		
		return String.valueOf(total);
	}
	
	private String getCityCount(String city_id,Map tmpMap){
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		ArrayList<String> cityAll = CityDAO.getNextCityIdsByCityPid(city_id);
		
		Iterator it = cityAll.listIterator();
		
		while (it.hasNext()){
			tmpCity = (String)it.next();
			tmp = (String)tmpMap.get(tmpCity);
			
			if (tmp != null && !"".equals(tmp)){
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		cityAll = null;
		return String.valueOf(total);
	}

	/**
	 * 按厂商统计设备数量
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByVendor(HttpServletRequest request) {
		//从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		
		String userCity = curUser.getCityId();
		
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str) {
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		
		StringBuffer sbTable = new StringBuffer();
		
		//初始化厂商信息
		getVendorMap();
		
		// 先读取所有业务
		List[] listService = getVendorIdNameList();
		if (listService == null) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		
		List listServiceId = listService[0];

		//输出表头
		sbTable.append("<TR>");
		sbTable.append("<TH width='20%'>厂商</TH>");
		
		//在从数据库中 统计 业务属地对应的数据
		//属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		cursor = getCityList(request);
		fields = cursor.getNext();
		if (fields != null){
			while(fields != null){
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else{
			return "<TR bgcolor='#FFFFFF'><TD align='center' class=column>系统中暂无属地数据!</TD></TR>";
		}
		
		sbTable.append("<TH nowrap>小计</TH>");
		
		sbTable.append("</TR>");

		//初始化地市信息
		getNextCityList(cityList);

		//初始化地市小计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Iterator cityIt = cityMap.keySet().iterator();
		cityTotalMap = new HashMap();
		while (cityIt.hasNext()){
			cityTotalMap.put(cityIt.next(), "0");
		}
		
		//将统计信息放入对应的map中
		String tmp = "";
		long vendortmp = 0;
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		pSQL.setSQL(m_vendorDevice);
		pSQL.setInt(1, gw_type);
		
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		
		fields = cursor.getNext();
		while (fields != null) {
			if (cityMap.get(fields.get("city_id")) != null){
				//将业务+属地==>num 作为映射存入map中
				mapServiceCityNum.put(fields.get("city_id") + "_"
						+ fields.get("oui"), fields.get("num"));
				
				tmp = (String)fields.get("num");
				if ((tmp ==null) || "".equals(tmp)){
					tmp = "0";
				}			
				//按厂商累加
				vendortmp = Long.parseLong(tmp) + Long.parseLong((String)cityTotalMap.get(fields.get("city_id")));
				
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
				
			}
			fields = cursor.getNext();
		}

		// 开始遍历,输出统计数据
		String isPrt = request.getParameter("isPrt");
		Iterator it = listServiceId.iterator();
		while (it.hasNext()){
			if ("1".equals(isPrt)) {
				sbTable.append(getEditionStatePrint((String)it.next(), 
						"0", cityList, mapServiceCityNum, gw_type));
			} else {
				sbTable.append(getVendorStatRowHtml((String)it.next(), 
						"0", cityList, mapServiceCityNum, gw_type,userCity));
			}
		}
		
		//输出小计信息
		sbTable.append("<tr><td nowrap align='right' class=column>小计</td>");
		sbTable.append(getSubTotal(cityList,isPrt,total,gw_type,userCity));

		// Clear Resouce
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
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
		int gw_type = 2;
		long starttime = 0l;
		long endtime   = 0l;
		
		String time_type = request.getParameter("time_type");
		String start_day = request.getParameter("start_day");
		String city_id = request.getParameter("city_id");
		
		if("day".equals(time_type)){
		    DateTimeUtil date = new DateTimeUtil(start_day);
		    starttime = date.getLongTime();
		    endtime = starttime + 86400;
		}else if("week".equals(time_type)){
		    DateTimeUtil date = new DateTimeUtil(start_day);
		    String firstDate = date.getFirstDayOfWeek("cn");
		    date = new DateTimeUtil(firstDate);
		    
		    starttime = date.getLongTime();
		    endtime   = starttime + 6 * 86400;
		}else if("month".equals(time_type)){
		    DateTimeUtil date = new DateTimeUtil(start_day + "-01");
		    starttime = date.getLongTime();
		    date.getNextMonth(1);
		    date.getNextDate(-1);
		    endtime = date.getLongTime();
		    
		}else if("year".equals(time_type)){
		    DateTimeUtil date = new DateTimeUtil(start_day + "-01-01");
		    starttime = date.getLongTime();
		    date.getNextYear(1);
		    date.getNextDate(-1);
		    endtime = date.getLongTime();
		}
		
		StringBuffer sbTable = new StringBuffer();
		
		//初始化版本信息
		getVersionMap();
		getVendorMap();
		
		// 判断是否有业务
		List[] listService = getEditionIdNameList();
		if (listService == null) {
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		
		List listServiceId = listService[0];

		//输出表头
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH>");
		
		//在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		cursor = getCityList(request);
		fields = cursor.getNext();
		if (fields != null){
			while(fields != null){
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		
		sbTable.append("<TH nowrap>小计</TH></TR>");
		
		//初始化地市信息
		getNextCityList(cityList);
		
		//初始化地市小计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Iterator cityIt = cityMap.keySet().iterator();
		cityTotalMap = new HashMap();
		while (cityIt.hasNext()){
			cityTotalMap.put(cityIt.next(), "0");
		}
		//将统计信息放入对应的map中
		String tmp = "";
		long vendortmp = 0;
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		pSQL.setSQL(m_OnlineDevice);
		pSQL.setLong(1, starttime);
		pSQL.setLong(2, endtime);
		
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		
		fields = cursor.getNext();
		while (fields != null) {
			if (cityMap.get(fields.get("city_id")) != null){
				//将业务+属地==>num 作为映射存入map中
				mapServiceCityNum.put(fields.get("city_id") + "_"
						+ fields.get("devicetype_id"), fields.get("num"));
				
				tmp = (String)fields.get("num");
				if ((tmp ==null) || "".equals(tmp)){
					tmp = "0";
				}
				
				total += Long.parseLong(tmp);
				
				//String tempSQL = "select city";
				//按厂商累加，将小计信息放入cityTotalMap
				vendortmp = Long.parseLong(tmp) + Long.parseLong((String)cityTotalMap.get(fields.get("city_id")));
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
				
			}
			fields = cursor.getNext();
		}
		
		//开始遍历属地,输出统计数据
		String isPrt = request.getParameter("isPrt");
		Iterator it = listServiceId.iterator();
		while (it.hasNext()){
			if ("1".equals(isPrt)) {
				sbTable.append(getEditionStatePrint((String)it.next(), 
						"1", cityList, mapServiceCityNum, gw_type));
			} else {
				sbTable.append(getVendorStatRowHtml((String)it.next(),
						"1", cityList, mapServiceCityNum, gw_type,userCityID));
			}
		}
		
		String num = getSubTotal(cityList,isPrt,total,gw_type,userCityID).toString();
		//输出小计信息
		sbTable.append("<tr><td nowrap align='right' class=column colspan=3>小计</td>");
		sbTable.append(num);
		
		// Clear Resouce
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}
	
	/**
	 * 输出小计信息
	 * @param cityList
	 * @param isPrt
	 * @param total
	 * @return
	 */
	private StringBuffer getSubTotal(List cityList, String isPrt, long total, int gw_type,String userCityID){
		StringBuffer sbTable = new StringBuffer();
		long totalNum = 0;
		Iterator it = cityList.iterator();
		while (it.hasNext()){
			
			String city = (String)it.next();
			long num = 0;
			if (userCityID.equals(city)) {
				num = Long.parseLong((String)cityTotalMap.get(city));
			} else {
				String tempSQL = "select city_id from tab_city where city_id = '" + city + "' or parent_id = '" + city + "'";
				PrepareSQL psql = new PrepareSQL(tempSQL);
		    	psql.getSQL();
				Cursor cursor = DataSetBean.getCursor(tempSQL);
				Map fields = cursor.getNext();
				while (null != fields) {
					num += Long.parseLong((String)cityTotalMap.get(fields.get("city_id")));
					fields = cursor.getNext();
				}
			}
			
			String num_str = String.valueOf(num);
			
			if ("0".equals(num_str)){
				sbTable.append("<TD bgcolor=#ffffff align=center>"
						+ num_str + "</a></TD>");
			}
			else{
				if ("1".equals(isPrt)){
					sbTable.append("<TD bgcolor=#ffffff align=center>"
							+ num_str + "</a></TD>");
				}
				else{
					sbTable.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=detail('" 
							+ city + "','-1',"+gw_type+") >"
							+ num_str + "</a></TD>");
				}
			}
			
			totalNum += Long.parseLong(num_str);			
		}
		
		if ("1".equals(isPrt)){
			sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum +"</td></tr>");
		}
		else{
			sbTable.append("<td bgcolor=#ffffff align=center><a href=javascript:onclick=detail('-1','-1',"+gw_type+")>" 
					+ totalNum +"</a></td></tr>");
		}
		
		return sbTable;
	}
	
	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放业务id的列表
	 * @param mapServiceCityNum
	 *            业务id+属地id 映射 统计值
	 * @return
	 */
	private String getEditionStatePrint(String vendor_id, String type,
			List cityList, Map mapServiceCityNum, int gw_type) {
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		long total = 0;
		String city_id = "";
		
		String message = "";
		
		// 输出业务名称
		if ("1".equals(type)){
			message = (String)versionMap.get(vendor_id);
			
			String[] tmp = message.split(",");
			if (tmp != null && tmp.length >2){
				sb.append("<TR ><TD nowrap class=column align='right'>"
						+ vendorMap.get(tmp[0]) + "</TD>");
				sb.append("<TD nowrap class=column align='right'>"
						+ tmp[1] + "</TD>");
				sb.append("<TD nowrap class=column align='right'>"
						+ tmp[2] + "</TD>");
			}
			else{
				sb.append("<TR ><TD nowrap class=column colspan=3 align='right'></TD>");
			}
		}
		else {
			message = (String)vendorMap.get(vendor_id);
			sb.append("<TR ><TD nowrap class=column align='right'>"
					+ message + "</TD>");
		}
		
		while (it.hasNext()) {
			city_id = (String)it.next();
			
			ArrayList cityAll = (ArrayList)cityListMap.get(city_id);

			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null) {
				if ("00".equals(city_id)){
					num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
				}
				else{
					num = getCityVendorCount(cityAll,vendor_id,mapServiceCityNum);
				}

				if (num != null && !"0".equals(num)){
					if (!"".equals(num)){
						total += Long.parseLong(num);
					}
					
					sb.append("<TD bgcolor=#ffffff align=center>"+ num + "</TD>");
				}
				else{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			} else if (mapServiceCityNum == null) {
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		
		sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
		
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * @param vendor_id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private String getVendorStatRowHtml(String vendor_id, String type,
			List cityList, Map mapServiceCityNum, int gw_type,String userCityID) {
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','"+ gw_type +"')";
		long total = 0;
		String city_id = "";		
		String message = "";
		
		// 输出业务名称
		if ("1".equals(type)){
			message = (String)versionMap.get(vendor_id);
			
			String[] tmp = message.split(",");
			if (tmp != null && tmp.length >2){
				sb.append("<TR ><TD nowrap class=column align='right'>"
						+ vendorMap.get(tmp[0]) + "</TD>");
				sb.append("<TD nowrap class=column align='right'>"
						+ tmp[1] + "</TD>");
				sb.append("<TD nowrap class=column align='right'>"
						+ tmp[2] + "</TD>");
			}
			else{
				sb.append("<TR ><TD nowrap class=column colspan=3 align='right'></TD>");
			}
		}
		else {
			message = (String)vendorMap.get(vendor_id);
			sb.append("<TR ><TD nowrap class=column align='right'>"
					+ message + "</TD>");
		}
		
		while (it.hasNext()) {
			city_id = (String)it.next();
			
			ArrayList cityAll = (ArrayList)cityListMap.get(city_id);

			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null) {
				//num = (String) mapServiceCityNum.get(city_id + "_" + editionId);
				if (userCityID.equals(city_id)){
					num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
				}
				else{
					num = getCityVendorCount(cityAll,vendor_id,mapServiceCityNum);
				}

				if (num != null && !"0".equals(num)){
					if (!"".equals(num)){
						total += Long.parseLong(num);
					}
					
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
									+ jsStr.replaceAll("\\?", city_id)
											.replaceAll("\\#", vendor_id)
									+ ">"
									+ num + "</a></TD>");
				}
				else{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			} else if (mapServiceCityNum == null) {
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		
		if (total == 0){
			sb.append("<TD bgcolor=#ffffff align=center>" + total
						+ "</TD>");
		}
		else{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
						+ jsStr.replaceAll("\\#", vendor_id).replaceAll("\\?", "-1")
						+ ">" + total
						+ "</TD>");
		}
//		sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=customize('"+vendor_id+"')>定制</a></TD>");
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 版本信息列表
	 * 
	 * @return
	 */
	public List[] getEditionIdNameList() {
		cursor = DataSetBean.getCursor(m_EditionInfo);
		fields = cursor.getNext();
		if (fields == null)
			return null;

		List[] listService = new ArrayList[3];
		listService[0] = new ArrayList();// devicetype_id
		listService[1] = new ArrayList();// device_model
		while (fields != null) {
			listService[0].add(fields.get("devicetype_id"));
			listService[1].add(fields.get("device_model"));
			fields = cursor.getNext();
		}

		return listService;
	}
	
	/**
	 * 厂商信息列表
	 * 
	 * @return
	 */
	public List[] getVendorIdNameList() {
		cursor = DataSetBean.getCursor(m_VendorInfo);
		fields = cursor.getNext();
		if (fields == null)
			return null;

		List[] listService = new ArrayList[2];
		listService[0] = new ArrayList();// vendor_id
		listService[1] = new ArrayList();// vendor_name
		while (fields != null) {
			listService[0].add(fields.get("vendor_id"));
			listService[1].add(fields.get("vendor_name"));
			fields = cursor.getNext();
		}

		return listService;
	}
	
	/**
	 * 获取当前用户的下级属地
	 * @param request
	 * @return
	 */
	public Cursor getCityList(HttpServletRequest request){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = request.getParameter("city_id");
		
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
			+city_id+"' or city_id='"+city_id+"' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
    	psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}
	
	/**
	 * 将传入的属地列表中所有数据相加
	 * @param cityAll
	 * @param editionId
	 * @param tmpMap
	 * @return
	 */
	private String getCityVendorCount(ArrayList cityAll,String editionId,Map tmpMap){
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		
		Iterator it = cityAll.listIterator();
		
		while (it.hasNext()){
			tmpCity = (String)it.next();
			
			tmp = (String)tmpMap.get(tmpCity + "_" + editionId);
			
			if (tmp != null && !"".equals(tmp)){
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
			
		}
		
		return String.valueOf(total);
	}
	
	/**
	 * 将各地市的下级属地放入MAP中
	 * @param cityList
	 */
	private void getNextCityList(List cityList){
		Iterator it = cityList.iterator();
		String city_id = "";
		
		while (it.hasNext()) {
			city_id = (String)it.next();
			ArrayList cityAll = CityDAO.getNextCityIdsByCityPid(city_id);
			
			cityListMap.put(city_id, cityAll);
			cityAll = null;
		}
	}
	
	/**
	 * 将软件版本等信息放入MAP中
	 *
	 */
	private void getVersionMap(){
		String sql = "select a.softwareversion,a.devicetype_id,b.device_model,c.oui " +
				" from tab_devicetype_info a, gw_device_model b,tab_vendor_oui c " +
				" where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		
		if (versionMap == null){
			versionMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			
			while(fields != null){
				String version = (String)fields.get("oui") + "," 
								+ (String)fields.get("device_model") + "," 
								+ (String)fields.get("softwareversion");
				
				versionMap.put(fields.get("devicetype_id"), version);
				
				fields = cursor.getNext();
			}
		}
		
	}
	
	/**
	 * 将厂商信息放入MAP中
	 *
	 */
	private void getVendorMap(){
		String sql = "select * from tab_vendor order by vendor_add";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select vendor_add, vendor_id, vendor_name from tab_vendor order by vendor_add";
		}
		if (vendorMap == null){
			vendorMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			
			while(fields != null){
				String vendor_add = (String)fields.get("vendor_add");
				if (vendor_add != null && !"".equals(vendor_add)){
					vendorMap.put(fields.get("vendor_id"), vendor_add + "(" +fields.get("vendor_id") + ")");
				}
				else{
					vendorMap.put(fields.get("vendor_id"), fields.get("vendor_name")+ "(" +fields.get("vendor_id") + ")");
				}
				fields = cursor.getNext();
			}
		}
		
	}
}
