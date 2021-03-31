
package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.DeviceServiceDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-10
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceServiceBIO
{

	private DeviceServiceDAO dao;
	private Map<String, String> versionMap;
	private Map<String, String> vendorMap;
	private Map<String, String> modelMap;

	/**
	 * @param startOpenDate1
	 *            开始时间
	 * @param endOpenDate1
	 *            结束时间
	 * @param vendorId
	 *            厂商id
	 * @param modelId
	 *            型号id
	 * @param deviceTypeId
	 *            软件版本id
	 * @param rela_dev_type_id
	 *            设备类型
	 * @param gw_type
	 *            1:家庭网关设备 2:企业网关设备
	 * @param city_id
	 *            属地
	 * @param isprt
	 *            1：代表导出操作 0：代表查询操作
	 * @return
	 */
	public String getHtmlDeviceByEdition(String startOpenDate1, String endOpenDate1,
			String vendorId, String modelId, String deviceTypeId,
			String rela_dev_type_id, String gw_type, String city_id, String isprt)
	{
		// 厂商列表
		versionMap = dao.getVendor();
		// 版本列表
		vendorMap = dao.getVendorMap();
		// 型号列表
		modelMap = dao.getDeviceModel();
		// 软件版本统计，查询
		StringBuffer sbTable = new StringBuffer();
		if ("1".equals(isprt))
		{
			sbTable.append("<TABLE border=1 cellspacing=1 cellpadding=2 width='100%'>");
			sbTable.append("<caption>版本统计优化信息列表</caption>");
		}
		else
		{
			sbTable.append("<TABLE  class='listtable' id='listTable' >");
			sbTable.append("<caption>版本统计优化信息列表</caption>");
		}
		List<String> tempList = dao.getVendorList(gw_type, city_id);
		// 格式为：（devicetype_id,devicetype_id,devicetype_id）
		StringBuffer edtionIdBuffer = new StringBuffer();
		edtionIdBuffer.append("(");
		// 同一个设备版本小于20的版本id
		for (int i = 0; i < tempList.size(); i++)
		{
			if (null == tempList || tempList.size() == 0)
			{
				return "<TR><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			}
			String devicetype_id = StringUtil.getStringValue(tempList.get(i));
			if (i + 1 == tempList.size())
			{
				edtionIdBuffer.append("" + devicetype_id);
			}
			else
			{
				edtionIdBuffer.append("" + devicetype_id + ",");
			}
		}
		edtionIdBuffer.append(" )");
		Map<String, Map> mapService = dao.getEditionIdNameMap(vendorId, modelId,
				deviceTypeId, rela_dev_type_id, edtionIdBuffer.toString());
		if (null == mapService)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		sbTable.append("<thead><TR>");
		sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>软件版本</TH>");
		List cityList = new ArrayList();
		List<Map<String, String>> cityMapList = dao.getCityList(city_id);
		if (cityMapList.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		else
		{
			for (int i = 0; i < cityMapList.size(); i++)
			{
				cityList.add(cityMapList.get(i).get("city_id"));
				sbTable.append("<TH nowrap>" + cityMapList.get(i).get("city_name")
						+ "</TH>");
			}
		}
		sbTable.append("<TH>小计</TH></TR></thead>");
		Map cityListMap = dao.getNextCityList(cityList);
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Iterator cityIt = cityMap.keySet().iterator();
		Map cityTotalMap = new HashMap();
		while (cityIt.hasNext())
		{
			cityTotalMap.put(cityIt.next(), "0");
		}
		// 将统计信息放入对应的map中
		String tmp = "0";
		long vendortmp = 0;
		// 所有设备资源统计
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		List<Map> mapServiceList = dao.getServiceCityNum(gw_type, startOpenDate1,
				endOpenDate1, vendorId, modelId, deviceTypeId, rela_dev_type_id,
				edtionIdBuffer.toString());
		if (mapServiceList.size() > 0)
		{
			for (int i = 0; i < mapServiceList.size(); i++)
			{
				if (cityMap.get(mapServiceList.get(i).get("city_id")) != null)
				{
					// 将业务+属地==>num 作为映射存入map中
					mapServiceCityNum.put(mapServiceList.get(i).get("city_id") + "_"
							+ mapServiceList.get(i).get("softwareversion"),
							mapServiceList.get(i).get("num"));
					tmp = StringUtil.getStringValue(mapServiceList.get(i).get("num"));
					if ((tmp == null) || "".equals(tmp))
					{
						tmp = "0";
					}
					total += Long.parseLong(tmp);
					// 按厂商累加，将小计信息放入cityTotalMap sssss
					vendortmp = Long.parseLong(tmp)
							+ Long.parseLong((String) cityTotalMap.get(mapServiceList
									.get(i).get("city_id")));
					cityTotalMap.put(mapServiceList.get(i).get("city_id"),
							String.valueOf(vendortmp));
				}
			}
		}
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext())
		{
			// 厂商的KEY
			String key = (String) itVender.next();
			// key：厂商；value：设备型号（map<key:设备型号id,value:软件版本<list:<软件版本 id>>>）
			// 设备型号Map
			Map valueMap = (Map) mapService.get(key);
			int size = 0;
			// 设备型号
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				size = size + ((List) valueMap.get((String) itDevicetype.next())).size();
			}
			mapServiceSize.put(key, size);
		}
		// 开始遍历属地,输出统计数据
		Iterator it = mapService.keySet().iterator();
		while (it.hasNext())
		{
			String key = StringUtil.getStringValue(it.next());
			// 同个厂商版本的个数 key为厂商id
			sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key)
					+ "' class=column align='right'>" + versionMap.get(key) + "</TD>");
			Map valueMap = (Map) mapService.get(key);
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				String keyModel = StringUtil.getStringValue(itDevicetype.next());
				List softwareversionList = (List) valueMap.get(keyModel);
				// 同个厂商同个型号版本的个数
				sbTable.append("<TD nowrap rowspan='" + softwareversionList.size()
						+ "' class=column align='right'>" + modelMap.get(keyModel)
						+ "</TD>");
				for (int i = 0; i < softwareversionList.size(); i++)
				{
					String softwareversion = String.valueOf(softwareversionList.get(i));
					sbTable.append(getVendorStatRowHtml(softwareversion, "1", cityList,
							mapServiceCityNum, gw_type, city_id, keyModel, key,
							cityListMap, isprt));
				}
			}
		}
		// total为 同一个地方所有版本的个数
		String num = getSubTotal(cityTotalMap, cityList, total, gw_type, city_id, "",
				isprt).toString();
		sbTable.append("<TR><td nowrap align='right' class=column colspan=3>小计</td>");
		sbTable.append(num);
		if (!"1".equals(isprt))
		{
			sbTable.append("<TR><td nowrap align='left' class=column height=30 colspan=21><b><a href=javaScript:excelDevice('1') alt='导出当前页数据到Excel中'>&nbsp;&nbsp;&nbsp;导出到Excel</a></b></td><TR>");
		}
		// Clear Resouce
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		sbTable.append("</table>");
		return sbTable.toString();
	}

	private String getVendorStatRowHtml(String softwareversion, String type,
			List cityList, Map mapServiceCityNum, String gw_type, String userCityID,
			String keyModel, String key, Map cityListMap, String isprt)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','','" + gw_type + "')";
		String jsVendor = "detailVendor('?','#','$','" + gw_type + "')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			if ("1".equals(isprt))
			{
				sb.append("<TD nowrap  class=column align='right'>" + softwareversion
						+ "</TD>");
			}
			else
			{
				sb.append("<TD nowrap  class=column align='right'><a href=\"javascript:onclick="
						+ jsVendor.replaceAll("\\?", key).replaceAll("\\#", keyModel)
								.replaceAll("\\$", softwareversion)
						+ "\">"
						+ softwareversion + "</a></TD>");
			}
		}
		else
		{
			sb.append("<TR ><TD nowrap class=column align='right'>" + softwareversion
					+ "</TD>");
		}
		while (it.hasNext())
		{
			city_id = StringUtil.getStringValue(it.next());
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				if (userCityID.equals(city_id))
				{
					num = StringUtil.getStringValue(mapServiceCityNum.get(city_id + "_"
							+ softwareversion));
				}
				else
				{
					num = StringUtil.getStringValue(getCityVendorCount(cityAll,
							softwareversion, mapServiceCityNum));
				}
				if (num != null && !"0".equals(num) && !"".equals(num))
				{
					total += Long.parseLong(num);
					if ("1".equals(isprt))
					{
						sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
					}
					else
					{
						sb.append("<TD bgcolor=#ffffff align=center><a href=\"javascript:onclick="
								+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#",
										softwareversion) + "\">" + num + "</a></TD>");
					}
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
		if (total == 0)
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
		}
		else
		{
			if ("1".equals(isprt))
			{
				sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
			}
			else
			{
				sb.append("<TD bgcolor=#ffffff align=center><a href=\"javascript:onclick="
						+ jsStr.replaceAll("\\#", softwareversion)
								.replaceAll("\\?", "-1") + "\">" + total + "</TD>");
			}
		}
		sb.append("</TR>");
		return sb.toString();
	}

	private StringBuffer getSubTotal(Map cityTotalMap, List cityList, long total,
			String gw_type, String userCityID, String softwareversion, String isprt)
	{
		String jsStr = "detail('?','#','$','" + gw_type + "')";
		StringBuffer sbTable = new StringBuffer();
		long totalNum = 0;
		Iterator it = cityList.iterator();
		while (it.hasNext())
		{
			String city = StringUtil.getStringValue(it.next());
			long num = 0;
			if (userCityID.equals(city))
			{
				num = Long.parseLong((String) cityTotalMap.get(city));
			}
			else
			{
				/**
				 * modify by gaoyi
				 */
				List childList = CityDAO.getAllNextCityIdsByCityPid(city);
				for (int i = 0; i < childList.size(); i++)
				{
					num += Long.parseLong(StringUtil.getStringValue(cityTotalMap
							.get(childList.get(i))));
				}
				childList = null;
			}
			String num_str = StringUtil.getStringValue(num);
			if ("0".equals(num_str) || "1".equals(isprt))
			{
				sbTable.append("<TD bgcolor=#ffffff align=center>" + num_str + "</TD>");
			}
			else
			{
				sbTable.append("<TD bgcolor=#ffffff align=center><a href=\"javascript:onclick="
						+ jsStr.replaceAll("\\?", city)
								.replaceAll("\\#", softwareversion).replaceAll("\\$", "")
						+ "\" >" + num_str + "</a></TD>");
			}
			totalNum += Long.parseLong(num_str);
		}
		if ("1".equals(isprt))
		{
			sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum + "</td></tr>");
		}
		else
		{
			sbTable.append("<td bgcolor=#ffffff align=center><a href=\"javascript:onclick="
					+ jsStr.replaceAll("\\?", "-1").replaceAll("\\#", softwareversion)
							.replaceAll("\\$", "") + "\">" + totalNum + "</a></td></tr>");
		}
		return sbTable;
	}

	public String detailVendor(String startOpenDate1, String endOpenDate1,
			String vendorId, String modelId, String softwareversion,
			String rela_dev_type_id, String gw_type, String city_id, String isprt)
	{
		// 厂商列表
		versionMap = dao.getVendor();
		// 版本列表
		vendorMap = dao.getVendorMap();
		// 型号列表
		modelMap = dao.getDeviceModel();
		// 软件版本统计，查询
		StringBuffer sbTable = new StringBuffer();
		if ("1".equals(isprt))
		{
			sbTable.append("<TABLE border=1 cellspacing=1 cellpadding=2 width='100%'>");
			sbTable.append("<caption>软件版本明细列表</caption>");
		}
		else
		{
			sbTable.append("<TABLE  class='listtable' id='listTable' >");
			sbTable.append("<caption>软件版本明细列表</caption>");
		}
		List<String> tempList = dao.getVendorList(gw_type, city_id);
		// 格式为：（devicetype_id,devicetype_id,devicetype_id）
		StringBuffer edtionIdBuffer = new StringBuffer();
		edtionIdBuffer.append("(");
		// 同一个设备版本小于20的版本id
		for (int i = 0; i < tempList.size(); i++)
		{
			if (null == tempList || tempList.size() == 0)
			{
				return "<TR><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			}
			String devicetype_id = StringUtil.getStringValue(tempList.get(i));
			if (i + 1 == tempList.size())
			{
				edtionIdBuffer.append("" + devicetype_id);
			}
			else
			{
				edtionIdBuffer.append("" + devicetype_id + ",");
			}
		}
		edtionIdBuffer.append(" )");
		Map<String, ArrayList<String>> mapService = dao.getEditionIdNameMapVendor(
				vendorId, modelId, softwareversion, rela_dev_type_id,
				edtionIdBuffer.toString());
		if (null == mapService)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		sbTable.append("<thead><TR>");
		sbTable.append("<TH>软件版本</TH><TH>硬件版本</TH>");
		List cityList = new ArrayList();
		List<Map<String, String>> cityMapList = dao.getCityList(city_id);
		if (cityMapList.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		else
		{
			for (int i = 0; i < cityMapList.size(); i++)
			{
				cityList.add(cityMapList.get(i).get("city_id"));
				sbTable.append("<TH nowrap>" + cityMapList.get(i).get("city_name")
						+ "</TH>");
			}
		}
		sbTable.append("<TH>小计</TH></TR></thead>");
		Map cityListMap = dao.getNextCityList(cityList);
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Iterator cityIt = cityMap.keySet().iterator();
		Map cityTotalMap = new HashMap();
		while (cityIt.hasNext())
		{
			cityTotalMap.put(cityIt.next(), "0");
		}
		// 将统计信息放入对应的map中
		String tmp = "0";
		long vendortmp = 0;
		// 所有设备资源统计
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		List<Map> mapServiceList = dao.getEditionIdNameMapVendorTotal(gw_type,
				startOpenDate1, endOpenDate1, vendorId, modelId, softwareversion,
				rela_dev_type_id, edtionIdBuffer.toString());
		if (mapServiceList.size() > 0)
		{
			for (int i = 0; i < mapServiceList.size(); i++)
			{
				if (cityMap.get(mapServiceList.get(i).get("city_id")) != null)
				{
					// 将业务+属地==>num 作为映射存入map中
					mapServiceCityNum.put(mapServiceList.get(i).get("city_id") + "_"
							+ mapServiceList.get(i).get("hardwareversion"),
							mapServiceList.get(i).get("num"));
					tmp = StringUtil.getStringValue(mapServiceList.get(i).get("num"));
					if ((tmp == null) || "".equals(tmp))
					{
						tmp = "0";
					}
					total += Long.parseLong(tmp);
					// 按厂商累加，将小计信息放入cityTotalMap sssss
					vendortmp = Long.parseLong(tmp)
							+ Long.parseLong((String) cityTotalMap.get(mapServiceList
									.get(i).get("city_id")));
					cityTotalMap.put(mapServiceList.get(i).get("city_id"),
							String.valueOf(vendortmp));
				}
			}
		}
		// 开始遍历属地,输出统计数据
		Iterator<Entry<String, ArrayList<String>>> it = mapService.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, ArrayList<String>> entry = it.next();
			String key = entry.getKey();
			ArrayList<String> hwList = entry.getValue();
			// 同个厂商版本的个数 key为厂商id
			sbTable.append("<TR ><TD nowrap rowspan='" + hwList.size()
					+ "' class=column align='right'>" + key + "</TD>");
			for (int i = 0; i < hwList.size(); i++)
			{
				String hardwareversion = hwList.get(i);
				sbTable.append(getVendorDetail(key, hardwareversion, cityList,
						mapServiceCityNum, gw_type, city_id, cityListMap, "1", isprt));
			}
		}
		// total为 同一个地方所有版本的个数
		String num = getSubTotal(cityTotalMap, cityList, total, gw_type, city_id,
				softwareversion, isprt).toString();
		sbTable.append("<TR><td nowrap align='right' class=column colspan=2>小计</td>");
		sbTable.append(num);
		if (!"1".equals(isprt))
		{
			String jsVendorPrt = "excelDeviceVendor('?','#','$','" + gw_type + "')";
			sbTable.append("<TR><td nowrap align='left' class=column height=30 colspan=17><b><a href=\"javascript:onclick="
					+ jsVendorPrt.replaceAll("\\?", vendorId).replaceAll("\\#", modelId)
							.replaceAll("\\$", softwareversion)
					+ "\">"
					+ "&nbsp;&nbsp;&nbsp;导出到Excel" + "</a></b></td><TR>");
		}
		// Clear Resouce
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		sbTable.append("</table>");
		return sbTable.toString();
	}

	// 软件版本明细
	private String getVendorDetail(String softwareversion, String hardwareversion,
			List cityList, Map mapServiceCityNum, String gw_type, String userCityID,
			Map cityListMap, String type, String isprt)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','$','" + gw_type + "')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			sb.append("<TD nowrap  class=column align='right'>" + hardwareversion
					+ "</TD>");
		}
		else
		{
			sb.append("<TR ><TD nowrap class=column align='right'>" + hardwareversion
					+ "</TD>");
		}
		while (it.hasNext())
		{
			city_id = StringUtil.getStringValue(it.next());
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				if (userCityID.equals(city_id))
				{
					num = StringUtil.getStringValue(mapServiceCityNum.get(city_id + "_"
							+ hardwareversion));
				}
				else
				{
					num = StringUtil.getStringValue(getCityVendorCount(cityAll,
							hardwareversion, mapServiceCityNum));
				}
				if (num != null && !"0".equals(num) && !"".equals(num))
				{
					total += Long.parseLong(num);
					if ("1".equals(isprt))
					{
						sb.append("<TD bgcolor=#ffffff align=center>" + num + "</a></TD>");
					}
					else
					{
						sb.append("<TD bgcolor=#ffffff align=center><a href=\"javascript:onclick="
								+ jsStr.replaceAll("\\?", city_id)
										.replaceAll("\\#", softwareversion)
										.replaceAll("\\$", hardwareversion)
								+ "\">"
								+ num
								+ "</a></TD>");
					}
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
		if (total == 0)
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
		}
		else
		{
			if ("1".equals(isprt))
			{
				sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
			}
			else
			{
				sb.append("<TD bgcolor=#ffffff align=center><a href=\"javascript:onclick="
						+ jsStr.replaceAll("\\#", hardwareversion)
								.replaceAll("\\?", "-1") + "\">" + total + "</TD>");
			}
		}
		sb.append("</TR>");
		return sb.toString();
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
		long tmp = 0;
		long tmpValue = 0;
		Iterator it = cityAll.listIterator();
		while (it.hasNext())
		{
			tmpCity = (String) it.next();
			tmp = StringUtil.getLongValue((tmpMap.get(tmpCity + "_" + editionId)));
			if (tmp != 0)
			{
				tmpValue = tmp;
				total += tmpValue;
			}
		}
		return String.valueOf(total);
	}

	public Map<String,String> getVendor()
	{
		return dao.getVendor();
	}

	/**
	 * 导出版本明细
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param softwareversion
	 * @param hardwareversion
	 * @param gw_type
	 * @param city_id
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> deviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id, int curPage_splitPage, int num_splitPage)
	{
		return dao.deviceVendorList(startOpenDate1, endOpenDate1, vendorId,
				softwareversion, hardwareversion, gw_type, city_id, curPage_splitPage,
				num_splitPage);
	}

	/**
	 * 获取最大的数据量
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param softwareversion
	 * @param hardwareversion
	 * @param gw_type
	 * @param city_id
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countDeviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id, int curPage_splitPage, int num_splitPage)
	{
		return dao.countDeviceVendorList(startOpenDate1, endOpenDate1, vendorId,
				softwareversion, hardwareversion, gw_type, city_id, curPage_splitPage,
				num_splitPage);
	}

	/**
	 * 导出版本明细EXCEL
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param softwareversion
	 * @param hardwareversion
	 * @param gw_type
	 * @param city_id
	 * @return
	 */
	public List<Map> excelDeviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id)
	{
		return dao.excelDeviceVendorList(startOpenDate1, endOpenDate1, vendorId,
				softwareversion, hardwareversion, gw_type, city_id);
	}

	public DeviceServiceDAO getDao()
	{
		return dao;
	}

	public void setDao(DeviceServiceDAO dao)
	{
		this.dao = dao;
	}
}
