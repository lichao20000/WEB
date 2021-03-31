
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

/**
 * 按速率统计用户报表
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-10-8
 * @category com.linkage.litms.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ServiceSpeedAct
{

	private static Logger logger = LoggerFactory.getLogger(ServiceSpeedAct.class);
	private Cursor cursor = null;
	private Cursor cursorCity = null;
	@SuppressWarnings("rawtypes")
	private Map fields = null;
	@SuppressWarnings("rawtypes")
	private Map fieldsCity = null;
	@SuppressWarnings("unused")
	private String m_ServiceSpeed = "select a.downlink,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_netacc_spead a, tab_gw_device b, hgwcust_serv_info c ,tab_hgwcustomer d where a.username = c.username "
			+ " and c.user_id = d.user_id and d.device_id = b.device_id and b.device_status =1 and b.gw_type=? and a.downlink in('100.0','200.0') "
			+ " group by a.downlink,b.city_id";
	private String m_ServiceSpeedJX = "select a.downlink,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_netacc_spead a, tab_gw_device b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where a.username = c.username "
			+ " and c.user_id = d.user_id and d.device_id = b.device_id and b.devicetype_id = t.devicetype_id and b.device_status =1 and b.gw_type=1 "
			;
	private String m_ServiceSpeedNX = "select a.downlink,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_netacc_spead a, tab_gw_device b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_devicetype_info t where a.username = c.username "
			+ " and c.user_id = d.user_id and d.device_id = b.device_id and b.devicetype_id = t.devicetype_id and b.device_status =1 and b.gw_type=1 "
			;
	private String m_ServiceSpeedXJ = "select a.downlink,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_netacc_spead a, tab_gw_device b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where a.username = c.username "
			+ " and c.user_id = d.user_id and d.device_id = b.device_id and b.devicetype_id = t.devicetype_id and b.device_status =1 and b.gw_type=1 "
			;
	private String m_ServiceSpeedHB = "select a.downlink,b.city_id,count(1) as num"
			+ " from tab_netacc_spead a, REPORT_GW_DEVICE_2018 b, REPORT_HGW_SERV_INFO_2018 c ,REPORT_HGWCUSTOMER_2018 d ,tab_device_version_attribute t where a.username = c.username "
			+ " and c.user_id = d.user_id and d.device_id = b.device_id and b.devicetype_id = t.devicetype_id and b.device_status =1 and b.gw_type=1 "
			;
	private String m_ServiceCountCityJX = "select b.city_id,count(distinct b.device_id) as num"
			+ " from tab_gw_device b, tab_hgwcustomer d ,tab_device_version_attribute t where "
			+ " d.device_id = b.device_id and b.device_status =1 and b.devicetype_id = t.devicetype_id  and b.gw_type=1 ";
		

	public ServiceSpeedAct()
	{
	}

	/**
	 * 根据速率统计用户
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceOnSpeedService(HttpServletRequest request, int gw_type)
	{
		logger.warn("开始统计...");
		String sbTable = "";
		if(Global.XJDX.equals(Global.instAreaShortName))
		{
			sbTable = getHtmlDeviceOnSpeedServiceXJ(request, gw_type);
		}
		else if(Global.JXDX.equals(Global.instAreaShortName))
		{
			sbTable = getHtmlDeviceOnSpeedServiceJX(request, gw_type);
		}
		else if(Global.NXDX.equals(Global.instAreaShortName))
		{
			sbTable = getHtmlDeviceOnSpeedServiceNX(request, gw_type);
		}
		else if(Global.HBDX.equals(Global.instAreaShortName))
		{
			sbTable = getHtmlDeviceOnSpeedServiceHB(request, gw_type);
		}
		else
		{
			sbTable = getHtmlDeviceOnSpeedServiceOther(request, gw_type);
		}
		return sbTable;
	}

	/**
	 * 根据速率统计用户
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHtmlDeviceOnSpeedServiceOther(HttpServletRequest request, int gw_type)
	{
		StringBuffer sbTable = new StringBuffer();
		List<Double> listServiceId = new ArrayList<Double>();
		listServiceId = getSpeedList();
		Collections.sort(listServiceId);  
		logger.warn("speedList is " + listServiceId);
		// 表头
		sbTable.append("<TR><TH nowrap>地市</TH>");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>"+ String.valueOf(it.next()) + "兆用户数</TH>");
		}
		sbTable.append("<TH nowrap>总数</TH></TR>");
		// 在从数据库中 统计 业务属地对应的数据属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		// 按属地统计用户
		cursorCity = getCountByCity(request, gw_type);
		fieldsCity = cursorCity.getNext();
		Map mapServiceCityNumCity = new HashMap();
		while (fieldsCity != null)
		{
			// 将属地==>num 作为映射存入map中
			mapServiceCityNumCity.put(fieldsCity.get("city_id"), fieldsCity.get("num"));
			fieldsCity = cursorCity.getNext();
		}
		// 按属地和速率统计用户设备
		cursor = getServiceOfHGWCustomerJX(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"speed" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 统计100兆以上的速率用户数量
			if (Double.parseDouble((String) fields.get("downlink")) >= 100.0)
			{
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("downlink"),
						fields.get("num"));
			}
			fields = cursor.getNext();
		}
		// 开始遍历属地
		Cursor cityList = getCityList(request);
		// 是否需要导出
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSpeedCount((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum, mapServiceCityNumCity));
			}
			else
			{
				sbTable.append(getSpeedCountHtml((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum, mapServiceCityNumCity));
			}
			cityField = cityList.getNext();
		}
		// Clear Resource
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		mapServiceCityNumCity.clear();
		mapServiceCityNumCity = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}
	
	/**
	 * 根据速率统计用户(江西电信)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHtmlDeviceOnSpeedServiceJX(HttpServletRequest request, int gw_type)
	{
		StringBuffer sbTable = new StringBuffer();
		List<Double> listServiceId = new ArrayList<Double>();
		listServiceId = getSpeedList();
		Collections.sort(listServiceId);  
		logger.warn("speedList is " + listServiceId);
		// 表头
		sbTable.append("<TR><TH nowrap>地市</TH>");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>"+ String.valueOf(it.next()) + "兆用户数</TH>");
		}
		sbTable.append("<TH nowrap>总数</TH></TR>");
		// 在从数据库中 统计 业务属地对应的数据属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		// 按属地统计用户
		cursorCity = getCountByCity(request, gw_type);
		fieldsCity = cursorCity.getNext();
		Map mapServiceCityNumCity = new HashMap();
		while (fieldsCity != null)
		{
			// 将属地==>num 作为映射存入map中
			mapServiceCityNumCity.put(fieldsCity.get("city_id"), fieldsCity.get("num"));
			fieldsCity = cursorCity.getNext();
		}
		// 按属地和速率统计用户设备
		cursor = getServiceOfHGWCustomerJX(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"speed" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 统计100兆以上的速率用户数量
			if (Double.parseDouble((String) fields.get("downlink")) >= 100.0)
			{
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("downlink"),
						fields.get("num"));
			}
			fields = cursor.getNext();
		}
		// 开始遍历属地
		Cursor cityList = getCityList(request);
		// 是否需要导出
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSpeedCount((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum, mapServiceCityNumCity));
			}
			else
			{
				sbTable.append(getSpeedCountHtml((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum, mapServiceCityNumCity));
			}
			cityField = cityList.getNext();
		}
		// Clear Resource
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		mapServiceCityNumCity.clear();
		mapServiceCityNumCity = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}
	
	/**
	 * 根据速率统计用户(宁夏电信)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHtmlDeviceOnSpeedServiceNX(HttpServletRequest request, int gw_type)
	{
		StringBuffer sbTable = new StringBuffer();
		List<Double> listServiceId = new ArrayList<Double>();
		listServiceId = getSpeedList();
		Collections.sort(listServiceId);  
		logger.warn("speedList is " + listServiceId);
		// 表头
		sbTable.append("<TR><TH nowrap>地市</TH>");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>"+ String.valueOf(it.next()) + "兆用户数</TH>");
		}
		sbTable.append("</TR>");
		// 在从数据库中 统计 业务属地对应的数据属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		// 按属地和速率统计用户设备
		cursor = getServiceOfHGWCustomerNX(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"speed" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 统计100兆以上的速率用户数量
			if (Double.parseDouble((String) fields.get("downlink")) >= 100.0)
			{
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("downlink"),
						fields.get("num"));
			}
			fields = cursor.getNext();
		}
		// 开始遍历属地
		Cursor cityList = getCityList(request);
		// 是否需要导出
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSpeedCountNX((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum));
			}
			else
			{
				sbTable.append(getSpeedCountHtmlNX((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum));
			}
			cityField = cityList.getNext();
		}
		// Clear Resource
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
	 * 根据速率统计用户（新疆电信）
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHtmlDeviceOnSpeedServiceXJ(HttpServletRequest request, int gw_type)
	{
		StringBuffer sbTable = new StringBuffer();
		List<Double> listServiceId = new ArrayList<Double>();
		listServiceId = getSpeedList();
		Collections.sort(listServiceId);  
		logger.warn("XJ speedList is " + listServiceId);
		// 表头
		sbTable.append("<TR><TH nowrap>地市</TH>");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>"+ String.valueOf(it.next()) + "兆用户数</TH>");
		}
		sbTable.append("<TH nowrap>总数</TH></TR>");
		// 在从数据库中 统计 业务属地对应的数据属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		// 按属地和速率统计用户设备
		cursor = getServiceOfHGWCustomerXJ(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"speed" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 统计100兆以上的速率用户数量
			if (Double.parseDouble((String) fields.get("downlink")) >= 100.0)
			{
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("downlink"),
						fields.get("num"));
			}
			fields = cursor.getNext();
		}
		// 开始遍历属地
		Cursor cityList = getCityList(request);
		// 是否需要导出
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSpeedCountXJ((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum));
			}
			else
			{
				sbTable.append(getSpeedCountHtmlXJ((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum));
			}
			cityField = cityList.getNext();
		}
		// Clear Resource
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
	 * 根据速率统计用户（湖北电信）
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHtmlDeviceOnSpeedServiceHB(HttpServletRequest request, int gw_type)
	{
		StringBuffer sbTable = new StringBuffer();
		List<Double> listServiceId = new ArrayList<Double>();
		listServiceId = getSpeedList();
		Collections.sort(listServiceId);  
		logger.warn("XJ speedList is " + listServiceId);
		// 表头
		sbTable.append("<TR><TH nowrap>地市</TH>");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>"+ String.valueOf(it.next()) + "兆用户数</TH>");
		}
		sbTable.append("</TR>");
		// 在从数据库中 统计 业务属地对应的数据属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		// 按属地和速率统计用户设备
		cursor = getServiceOfHGWCustomerHB(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"speed" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 统计100兆以上的速率用户数量
			if (Double.parseDouble((String) fields.get("downlink")) >= 100.0)
			{
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("downlink"),
						fields.get("num"));
			}
			fields = cursor.getNext();
		}
		// 开始遍历属地
		Cursor cityList = getCityList(request);
		// 是否需要导出
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSpeedCountNX((String) cityField.get("city_id"), cityMap,
						listServiceId, mapServiceCityNum));
			}
			else
			{
				sbTable.append(getSpeedCountHtmlNX((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum));
			}
			cityField = cityList.getNext();
		}
		// Clear Resource
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
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCountHtml(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum, Map mapServiceCityNumCity)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		String totalnum = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#')";
		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='right'>" + cityMap.get(city_id)
				+ "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#", speedId)
							+ ">" + num + "</a></TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		if ("00".equals(city_id))
		{
			totalnum = (String) mapServiceCityNumCity.get(city_id);
		}
		else
		{
			totalnum = getCityCount(cityAll, mapServiceCityNumCity);
		}
		if (totalnum != null && !"0".equals(totalnum))
		{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
					+ jsStr.replaceAll("\\?", city_id) + ">" + totalnum + "</a></TD>");
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 输出地市以及地市与业务对应的用户统计数量(宁夏电信)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCountHtmlNX(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#')";
		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='right'>" + cityMap.get(city_id)
				+ "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#", speedId)
							+ ">" + num + "</a></TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 输出地市以及地市与业务对应的用户统计数量(xj_dx,nx_dx)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCountHtmlXJ(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		long totalnum = 0;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#')";
		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='right'>" + cityMap.get(city_id)
				+ "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					totalnum = totalnum + Long.parseLong(num);
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#", speedId)
							+ ">" + num + "</a></TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		if (totalnum != 0)
		{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
					+ jsStr.replaceAll("\\?", city_id) + ">" + totalnum + "</a></TD>");
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * 输出地市以及地市与速率对应的用户统计数量(江西)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCount(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum, Map mapServiceCityNumCity)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		String totalnum = null;
		StringBuffer sb = new StringBuffer();
		// 输出地市名称
		sb.append("<TR ><TD nowrap align='right'>" + cityMap.get(city_id) + "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		if ("00".equals(city_id))
		{
			totalnum = (String) mapServiceCityNumCity.get(city_id);
		}
		else
		{
			totalnum = getCityCount(cityAll, mapServiceCityNumCity);
		}
		if (totalnum != null && !"0".equals(totalnum))
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + totalnum + "</TD>");
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 输出地市以及地市与速率对应的用户统计数量(宁夏电信)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCountNX(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		StringBuffer sb = new StringBuffer();
		// 输出地市名称
		sb.append("<TR ><TD nowrap align='right'>" + cityMap.get(city_id) + "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 输出地市以及地市与速率对应的用户统计数量(新疆、湖北)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放速率的列表
	 * @param mapServiceCityNum
	 *            速率+属地id 映射 统计值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSpeedCountXJ(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String speedId = null;
		String num = null;
		long totalnum = 0;
		StringBuffer sb = new StringBuffer();
		// 输出地市名称
		sb.append("<TR ><TD nowrap align='right'>" + cityMap.get(city_id) + "</TD>");
		while (it.hasNext())
		{
			speedId = String.valueOf(it.next());
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + speedId);
				}
				else
				{
					num = getCityServiceCount(cityAll, speedId, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					totalnum = totalnum + Long.parseLong(num);
					sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
				}
				else
				{
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
				}
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		if (totalnum != 0)
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + totalnum + "</TD>");
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	private String getCityServiceCount(ArrayList cityAll, String servId, Map tmpMap)
	{
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		Iterator it = cityAll.listIterator();
		while (it.hasNext())
		{
			tmpCity = (String) it.next();
			tmp = (String) tmpMap.get(tmpCity + "_" + servId);
			if (tmp != null && !"".equals(tmp))
			{
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		return String.valueOf(total);
	}

	@SuppressWarnings("rawtypes")
	private String getCityCount(ArrayList cityAll, Map tmpMap)
	{
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		Iterator it = cityAll.listIterator();
		while (it.hasNext())
		{
			tmpCity = (String) it.next();
			tmp = (String) tmpMap.get(tmpCity);
			if (tmp != null && !"".equals(tmp))
			{
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		return String.valueOf(total);
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
		String m_cityList = "";
		m_cityList = "select city_id,city_name from tab_city where parent_id='" + city_id
				+ "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}

	/**
	 * 按属地和速率统计用户设备(江西)
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomerJX(HttpServletRequest request, int gw_type)
	{
		String gbbroadband = request.getParameter("gbbroadband");
		StringBuffer sql = new StringBuffer();
		sql.append(m_ServiceSpeedJX);
		sql.append(" and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )");
		if (null != gbbroadband && !"null".equals(gbbroadband)
				&& !"-1".equals(gbbroadband))
		{
			sql.append(" and t.gbbroadband=" + gbbroadband + " ");
		}
		sql.append(" group by a.downlink,b.city_id ");
		logger.info("sql count is ... " + sql);
		return DataSetBean.getCursor(sql.toString());
	}
	
	/**
	 * 按属地和速率统计用户设备(宁夏)
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomerNX(HttpServletRequest request, int gw_type)
	{
		String gbbroadband = request.getParameter("gbbroadband");
		StringBuffer sql = new StringBuffer();
		sql.append(m_ServiceSpeedNX);
		sql.append(" and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )");
		if (null != gbbroadband && !"null".equals(gbbroadband)
				&& !"-1".equals(gbbroadband))
		{
			sql.append(" and t.mbbroadband=" + gbbroadband + " ");
		}
		sql.append(" group by a.downlink,b.city_id ");
		logger.info("sql count is ... " + sql);
		return DataSetBean.getCursor(sql.toString());
	}
	
	/**
	 * 按属地和速率统计用户设备(新疆)
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomerXJ(HttpServletRequest request, int gw_type)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(m_ServiceSpeedXJ);
		sql.append(" and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )");
		sql.append(" and t.gbbroadband=" + 1 + " ");
		sql.append(" group by a.downlink,b.city_id ");
		logger.info("sql count is ... " + sql);
		return DataSetBean.getCursor(sql.toString());
	}
	
	/**
	 * 按属地和速率统计用户设备(湖北)
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomerHB(HttpServletRequest request, int gw_type)
	{
		String gbbroadband = request.getParameter("gbbroadband");
		StringBuffer sql = new StringBuffer();
		sql.append(m_ServiceSpeedHB);
		sql.append(" and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )");
		if (null != gbbroadband && !"null".equals(gbbroadband)
				&& !"-1".equals(gbbroadband))
		{
			sql.append(" and t.gbbroadband=" + gbbroadband + " ");
		}
		sql.append(" group by a.downlink,b.city_id ");
		logger.info("sql count is ... " + sql);
		return DataSetBean.getCursor(sql.toString());
	}

	/**
	 * 按属地统计用户
	 * 
	 * @return cursor
	 */
	public Cursor getCountByCity(HttpServletRequest request, int gw_type)
	{
		String gbbroadband = request.getParameter("gbbroadband");
		StringBuffer sql = new StringBuffer();
		sql.append(m_ServiceCountCityJX);
		sql.append(" and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )");
		if (null != gbbroadband && !"null".equals(gbbroadband)
				&& !"-1".equals(gbbroadband))
		{
			sql.append(" and t.gbbroadband=" + gbbroadband + " ");
		}
		sql.append(" group by b.city_id");
		logger.info("sql count with city is ... " + sql);
		return DataSetBean.getCursor(sql.toString());
	}

	/**
	 * 取得所有city_id和city_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getCityMap(HttpServletRequest request)
	{
		HashMap cityMap = new HashMap();
		cityMap.clear();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String[] cityid = city_id.split(",");
		List list = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			list.add(cityid[i]);
		}
		String sql = "select city_id,city_name from tab_city where parent_id in("
				+ StringUtils.weave(list) + ") or city_id in(" + StringUtils.weave(list)
				+ ") order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				cityMap.put((String) fields.get("city_id"),
						(String) fields.get("city_name"));
				fields = cursor.getNext();
			}
		}
		return cityMap;
	}
	
	/**
	 * 取得大于100.0兆的speed List集合
	 * 
	 * @param request
	 * @return ArrayList
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Double> getSpeedList()
	{
		List<Double> speedList = new ArrayList<Double>();
		String sql = "select distinct downlink from tab_netacc_spead";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		double downlink = 0.0d;
		while (fields != null)
		{
			downlink = Double.parseDouble((String) fields.get("downlink"));
			if(downlink >= 100.0)
			{
				speedList.add(downlink);
			}
			fields = cursor.getNext();
		}
		return speedList;
	}
}