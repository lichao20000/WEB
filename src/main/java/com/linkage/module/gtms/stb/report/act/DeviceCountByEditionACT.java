/**
 *
 */

package com.linkage.module.gtms.stb.report.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class DeviceCountByEditionACT extends splitPageAction
{

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceCountByEditionACT.class);
	private Map versionMap = null;
	private Map vendorMap = null;
	private Cursor cursor = null;
	private PrepareSQL pSQL;
	private Map cityTotalMap = new HashMap();
	private Map fields = null;
	private Map cityListMap = new HashMap();
	// private String m_editionDevice =
	private String m_editionDevice = "select count(device_id) as num,devicetype_id,city_id from stb_tab_gw_device where devicetype_id in(select devicetype_id from stb_tab_devicetype_info) and device_status = 1 ";
	private long devicenum = 0;
	private String devicetypeIdStr;
	private String cityId;

	public DeviceCountByEditionACT()
	{
		pSQL = new PrepareSQL();
	}

	public String getDevicetypeId(HttpServletRequest request)
	{
		String cityId = request.getParameter("cityId");
		if (null == cityId || "-1".equals(cityId) || "".equals(cityId))
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		List<String> cityIds = new ArrayList<String>();
		cityIds = CityDAO.getAllNextCityIdsByCityPid(cityId);
		String cityIdStr = "(";
		for (int i = 0; i < cityIds.size(); i++)
		{
			if ((i + 1) == cityIds.size())
			{
				cityIdStr += "'" + StringUtil.getStringValue(cityIds.get(i)) + "')";
			}
			else
			{
				cityIdStr += "'" + StringUtil.getStringValue(cityIds.get(i)) + "',";
			}
		}
		String m_editionNum = "select count(device_id) as num,devicetype_id from stb_tab_gw_device where devicetype_id in(select devicetype_id from stb_tab_devicetype_info) and device_status = 1 and city_id  in "
				+ cityIdStr + " group by devicetype_id";
		PrepareSQL psql = new PrepareSQL(m_editionNum.toString());
		cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		devicetypeIdStr = " in (";
		while (fields != null)
		{
			int num = StringUtil.getIntegerValue(fields.get("num"));
			if (num > 0)
			{
				String devicetype_id = StringUtil.getStringValue(fields
						.get("devicetype_id"));
				devicetypeIdStr = devicetypeIdStr + devicetype_id + ",";
			}
			fields = cursor.getNext();
		}
		devicetypeIdStr = devicetypeIdStr.substring(0, devicetypeIdStr.length() - 1);
		devicetypeIdStr += ")";
		return devicetypeIdStr;
	}

	/**
	 * 按版本统计设备数量
	 *
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByEdition(HttpServletRequest request)
	{
		String starttime = "";
		String endtime = "";
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		StringBuffer sbTable = new StringBuffer();
		// 初始化版本信息
		getVersionMap();
		getVendorMap();
		getDevicetypeId(request);
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String cityId = request.getParameter("cityId");
		String bindType = request.getParameter("bindType");
		logger.debug("deviceTypeId:{}", deviceTypeId);
		// 判断是否有业务
		// Map mapService = getEditionIdNameMap(vendorId, modelId, deviceTypeId);
		Map mapService = getEditionIdNameMap(vendorId, modelId, deviceTypeId,
				devicetypeIdStr);
		if (null == mapService)
		{
			return "<TR><TD>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		sbTable.append("<thead>");
		sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>软件版本(硬件版本)</TH>");
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		if (null == cityId || "-1".equals(cityId) || "".equals(cityId))
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		cursor = getCityList(request, cityId);
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else
		{
			return "<TR><TD>系统中暂无属地数据!</TD></TR>";
		}
		sbTable.append("<TH>小计</TH></thead>");
		// 初始化地市信息
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
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		pSQL.setSQL(m_editionDevice);
		// pSQL.setInt(1, gw_type);
		StringBuffer sql = new StringBuffer();
		sql.append(pSQL.getSQL());
		if (null != starttime && !"".equals(starttime))
		{
			sql.append(" and complete_time>");
			sql.append(starttime);
		}
		if (null != endtime && !"".equals(endtime))
		{
			sql.append(" and complete_time<");
			sql.append(endtime);
		}
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and vendor_id='" + vendorId + "' ");
		}
		else
		{
			vendorId = "-1";
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			sql.append(" and device_model_id='" + modelId + "' ");
		}
		else
		{
			modelId = "-1";
		}
		if (null != deviceTypeId && !"null".equals(deviceTypeId)
				&& !"-1".equals(deviceTypeId))
		{
			sql.append(" and devicetype_id=" + deviceTypeId);
		}
		else
		{
			deviceTypeId = "-1";
		}
		sql.append(" and devicetype_id " + devicetypeIdStr
				+ " group by devicetype_id,city_id");
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
				total += Long.parseLong(tmp);
				// 按厂商累加，将小计信息放入cityTotalMap
				vendortmp = Long.parseLong(tmp)
						+ Long.parseLong((String) cityTotalMap.get(fields.get("city_id")));
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
			}
			fields = cursor.getNext();
		}
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext())
		{
			String key = (String) itVender.next();
			Map valueMap = (Map) mapService.get(key);
			int size = 0;
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				size = size + ((List) valueMap.get((String) itDevicetype.next())).size();
			}
			mapServiceSize.put(key, size);
		}
		// 开始遍历属地,输出统计数据
		String isPrt = request.getParameter("isPrt");
		Iterator it = mapService.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key)
					+ "' class=column align='right'>" + vendorMap.get(key) + "</TD>");
			Map valueMap = (Map) mapService.get(key);
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				String keyModel = (String) itDevicetype.next();
				List devicetypeList = (List) valueMap.get(keyModel);
				sbTable.append("<TD nowrap rowspan='" + devicetypeList.size()
						+ "' class=column align='right'>" + keyModel + "</TD>");
				for (int i = 0; i < devicetypeList.size(); i++)
				{
					String devicetypeStr = String.valueOf(devicetypeList.get(i));
					if ("1".equals(isPrt))
					{
						sbTable.append(getEditionStatePrint(devicetypeStr, "1", cityList,
								mapServiceCityNum, gw_type, cityId));
					}
					else
					{
						sbTable.append(getVendorStatRowHtml(devicetypeStr, "1", cityList,
								mapServiceCityNum, gw_type, cityId,bindType));
					}
				}
			}
		}
		String num = getSubTotal(cityList, isPrt, total, gw_type, cityId, vendorId,
				modelId, deviceTypeId,bindType).toString();
		// 输出小计信息
		sbTable.append("<tr><td colspan=3><div class='right'><b>小计</b></div></td>");
		sbTable.append(num);
		// Clear Resouce
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 按版本统计新装设备（用户）数量
	 *
	 * @param request
	 * @return
	 */
	public String getHtmlNewDeviceByEdition(HttpServletRequest request)
	{
		// 转换时间
		DateTimeUtil dt = null;
		String starttime = request.getParameter("starttime");
		String starttime1;
		if (null == starttime || "null".equals(starttime) || "".equals(starttime))
		{
			dt = new DateTimeUtil();
			starttime = dt.getFirtDayOfMonth();
			dt = new DateTimeUtil(starttime);
			long start_time = dt.getLongTime();
			dt = new DateTimeUtil(start_time * 1000);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		String endtime = request.getParameter("endtime");
		String endtime1;
		if (null == endtime || "null".equals(endtime) || "".equals(endtime))
		{
			dt = new DateTimeUtil();
			endtime = dt.getDate();
			dt = new DateTimeUtil(endtime);
			long end_time = dt.getLongTime();
			dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
			endtime1 = String.valueOf(dt.getLongTime());
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		StringBuffer sbTable = new StringBuffer();
		// 初始化版本信息
		getVersionMap();
		getVendorMap();
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String cityId = request.getParameter("cityId");
		String bindType = request.getParameter("bindType");
		logger.debug("deviceTypeId:{}", deviceTypeId);
		// 判断是否有业务
		Map mapService = getEditionIdNameMap(vendorId, modelId, deviceTypeId,
				devicetypeIdStr);
		if (null == mapService)
		{
			return "<TR><TD>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		sbTable.append("<thead>");
		sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>软件版本(硬件版本)</TH>");
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		if (null == cityId || "-1".equals(cityId) || "".equals(cityId))
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		cursor = getCityList(request, cityId);
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else
		{
			return "<TR><TD>系统中暂无属地数据!</TD></TR>";
		}
		sbTable.append("<TH>小计</TH></thead>");
		// 初始化地市信息
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
		long total = 0;
		Map mapServiceCityNum = new HashMap();
		pSQL.setSQL(m_editionDevice);
		// pSQL.setInt(1, gw_type);
		StringBuffer sql = new StringBuffer();
		sql.append(pSQL.getSQL());
		if (null != starttime1 && !"".equals(starttime1))
		{
			sql.append(" and complete_time>=");
			sql.append(starttime1);
		}
		if (null != endtime1 && !"".equals(endtime1))
		{
			sql.append(" and complete_time<=");
			sql.append(endtime1);
		}
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and vendor_id='" + vendorId + "' ");
		}
		else
		{
			vendorId = "-1";
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			sql.append(" and device_model_id='" + modelId + "' ");
		}
		else
		{
			modelId = "-1";
		}
		if (null != deviceTypeId && !"null".equals(deviceTypeId)
				&& !"-1".equals(deviceTypeId))
		{
			sql.append(" and devicetype_id=" + deviceTypeId + " ");
		}
		else
		{
			deviceTypeId = "-1";
		}
		//JXDX-REQ-ITV-20191128-WWF-002(ITV终端管理平台新用户按版本统计页面增加绑定关系需求
		if (null != bindType && !"null".equals(bindType) && !"-1".equals(bindType))
		{
			sql.append(" and cpe_allocatedstatus=" + bindType);
		}
		else
		{
			bindType = "-1";
		}
		sql.append(" group by devicetype_id,city_id");
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
				total += Long.parseLong(tmp);
				// 按厂商累加，将小计信息放入cityTotalMap
				vendortmp = Long.parseLong(tmp)
						+ Long.parseLong((String) cityTotalMap.get(fields.get("city_id")));
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
			}
			fields = cursor.getNext();
		}
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext())
		{
			String key = (String) itVender.next();
			Map valueMap = (Map) mapService.get(key);
			int size = 0;
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				size = size + ((List) valueMap.get((String) itDevicetype.next())).size();
			}
			mapServiceSize.put(key, size);
		}
		// 开始遍历属地,输出统计数据
		String isPrt = request.getParameter("isPrt");
		Iterator it = mapService.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			Map valueMap = (Map) mapService.get(key);
			Iterator itDevicetype = valueMap.keySet().iterator();
			long vendorRowNum = mapServiceSize.get(key);
			StringBuffer sbTr = new StringBuffer();
			while (itDevicetype.hasNext())
			{
				String keyModel = (String) itDevicetype.next();
				List devicetypeList = (List) valueMap.get(keyModel);
				long devicemodelRowNum = devicetypeList.size();
				StringBuffer sbTd = new StringBuffer();
				for (int i = 0; i < devicetypeList.size(); i++)
				{
					String devicetypeStr = String.valueOf(devicetypeList.get(i));
					String temp = "";
					if ("1".equals(isPrt))
					{
						temp = getEditionStatePrint(devicetypeStr, "1", cityList,
								mapServiceCityNum, gw_type, cityId );
					}
					else
					{
						temp = getVendorStatRowHtml(devicetypeStr, "1", cityList,
								mapServiceCityNum, gw_type, cityId,bindType);
					}
					if (devicenum == 0)
					{
						vendorRowNum = vendorRowNum - 1;
						devicemodelRowNum = devicemodelRowNum - 1;
					}
					else
					{
						sbTd.append(temp);
					}
				}
				if (devicemodelRowNum != 0)
				{
					sbTr.append("<TD nowrap rowspan='" + devicemodelRowNum
							+ "' class=column align='right'>" + keyModel + "</TD>");
					sbTr.append(sbTd);
				}
			}
			if (vendorRowNum != 0)
			{
				sbTable.append("<TR ><TD nowrap rowspan='" + vendorRowNum
						+ "' class=column align='right'>" + vendorMap.get(key) + "</TD>");
				sbTable.append(sbTr);
			}
		}
		String num = getSubTotal(cityList, isPrt, total, gw_type, cityId, vendorId,
				modelId, deviceTypeId,bindType).toString();
		// 输出小计信息
		sbTable.append("<tr><td colspan=3><div class='right'><b>小计</b></div></td>");
		sbTable.append(num);
		// Clear Resouce
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 将软件版本等信息放入MAP中
	 */
	private void getVersionMap()
	{
		String sql = "select a.softwareversion,a.devicetype_id,b.device_model,c.vendor_id,a.hardwareversion "
				+ " from stb_tab_devicetype_info a, stb_gw_device_model b,stb_tab_vendor_oui c "
				+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		if (versionMap == null)
		{
			versionMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				String version = (String) fields.get("vendor_id") + "|"
						+ (String) fields.get("device_model") + "|"
						+ (String) fields.get("softwareversion") + "("
						+ (String) fields.get("hardwareversion") + ")";
				versionMap.put(fields.get("devicetype_id"), version);
				fields = cursor.getNext();
			}
		}
	}

	/**
	 * 将厂商信息放入MAP中
	 */
	private void getVendorMap()
	{
		String sql = "select vendor_add, vendor_id, vendor_name from stb_tab_vendor order by vendor_add";
		if (vendorMap == null)
		{
			vendorMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				String vendor_add = (String) fields.get("vendor_add");
				if (vendor_add != null && !"".equals(vendor_add))
				{
					vendorMap.put(fields.get("vendor_id"),
							vendor_add + "(" + fields.get("vendor_id") + ")");
				}
				else
				{
					vendorMap.put(fields.get("vendor_id"), fields.get("vendor_name")
							+ "(" + fields.get("vendor_id") + ")");
				}
				fields = cursor.getNext();
			}
		}
	}

	/**
	 * 版本信息列表Map<vendor_id,Map<device_model,List<devicetype_id>>>
	 *
	 * @return
	 */
	public Map getEditionIdNameMap(String vendorId, String modelId, String deviceTypeId,
			String devicetypeIdStr)
	{
		StringBuffer bf = new StringBuffer();
		bf.append(" select a.vendor_id,device_model,devicetype_id from "
				+ "stb_tab_devicetype_info a,stb_gw_device_model b where a.device_model_id=b.device_model_id ");
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
			bf.append(" and a.devicetype_id=" + deviceTypeId);
		}
		if (null != devicetypeIdStr && !"null".equals(devicetypeIdStr)
				&& !"".equals(devicetypeIdStr))
		{
			bf.append(" and a.devicetype_id " + devicetypeIdStr);
		}
		bf.append(" order by  a.vendor_id,device_model,devicetype_id ");
		PrepareSQL psql = new PrepareSQL(bf.toString());
		psql.getSQL();
		cursor = DataSetBean.getCursor(bf.toString());
		fields = cursor.getNext();
		if (fields == null)
			return null;
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
				modelMap = (Map) mapService.get(vendor_id);
			}
			List<String> devicetypeList = null;
			if (null == modelMap.get(device_model))
			{
				devicetypeList = new ArrayList<String>();
			}
			else
			{
				devicetypeList = (List) modelMap.get(device_model);
			}
			devicetypeList.add(devicetype_id);
			modelMap.put(device_model, devicetypeList);
			mapService.put(vendor_id, modelMap);
			fields = cursor.getNext();
		}
		return mapService;
	}

	/**
	 * 输出小计信息
	 *
	 * @param cityList
	 * @param isPrt
	 * @param total
	 * @return
	 */
	private StringBuffer getSubTotal(List cityList, String isPrt, long total,
			int gw_type, String userCityID, String vendorId, String modelId,
			String deviceTypeId,String bindType)
	{
		StringBuffer sbTable = new StringBuffer();
		long totalNum = 0;
		Iterator it = cityList.iterator();
		while (it.hasNext())
		{
			String city = (String) it.next();
			long num = 0;
			if (userCityID.equals(city))
			{
				num = Long.parseLong((String) cityTotalMap.get(city));
			}
			else
			{
				/**
				 * modify by qixueqi
				 */
				List childList = CityDAO.getAllNextCityIdsByCityPid(city);
				for (int i = 0; i < childList.size(); i++)
				{
					num += Long.parseLong((String) cityTotalMap.get(childList.get(i)));
				}
				childList = null;
			}
			String num_str = String.valueOf(num);
			if ("0".equals(num_str))
			{
				sbTable.append("<TD bgcolor=#ffffff align=center>" + num_str
						+ "</a></TD>");
			}
			else
			{
				if ("1".equals(isPrt))
				{
					sbTable.append("<TD bgcolor=#ffffff align=center>" + num_str
							+ "</a></TD>");
				}
				else
				{
					if (userCityID.equals(city))
					{
						sbTable.append("<TD bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick=detail('"
								+ city
								+ "',"
								+ vendorId
								+ ","
								+ modelId
								+ ","
								+ deviceTypeId
								+ ","
								+ bindType
								+ ","
								+ gw_type
								+ ",'true') >"
								+ num_str
								+ "</a></TD>");
					}
					else
					{
						sbTable.append("<TD bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick=detail('"
								+ city
								+ "',"
								+ vendorId
								+ ","
								+ modelId
								+ ","
								+ deviceTypeId
								+ ","
								+ bindType
								+ ","
								+ gw_type
								+ ",'false') >"
								+ num_str
								+ "</a></TD>");
					}
				}
			}
			totalNum += Long.parseLong(num_str);
		}
		if ("1".equals(isPrt))
		{
			sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum + "</td></tr>");
		}
		else
		{
			sbTable.append("<td bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick=detail('"
					+ userCityID
					+ "',"
					+ vendorId
					+ ","
					+ modelId
					+ ","
					+ deviceTypeId
					+ ","
					+ bindType
					+ ","
					+ gw_type + ",'false')>" + totalNum + "</a></td></tr>");
		}
		return sbTable;
	}

	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 *
	 * @param vendor_id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private String getVendorStatRowHtml(String devicetypeStr, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID,String bindType)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','-1','-1','#','$','" + gw_type + "','false')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			message = (String) versionMap.get(devicetypeStr);
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
			}
			else
			{
				sb.append("<TR ><TD nowrap class=column align='right'></TD>");
			}
		}
		else
		{
			logger.debug("getVendorStatRowHtml=>type(!1)=>message:" + message);
			message = (String) vendorMap.get(devicetypeStr);
			sb.append("<TR ><TD nowrap class=column align='right'>" + message + "</TD>");
		}
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				// num = (String) mapServiceCityNum.get(city_id + "_" + editionId);
				if (userCityID.equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + devicetypeStr);
				}
				else
				{
					num = getCityVendorCount(cityAll, devicetypeStr, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					if (!"".equals(num))
					{
						total += Long.parseLong(num);
					}
					if (userCityID.equals(city_id))
					{
						sb.append("<TD bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick="
								+ jsStr.replaceAll("\\?", city_id)
										.replaceAll("\\$", bindType)
										.replaceAll("\\#", devicetypeStr)
										.replace("false", "true")
								+ ">"
								+ num
								+ "</a></TD>");
					}
					else
					{
						sb.append("<TD bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick="
								+ jsStr.replaceAll("\\?", city_id)
										.replaceAll("\\$", bindType)
										.replaceAll("\\#",devicetypeStr)+ ">" + num + "</a></TD>");
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
		devicenum = total;
		if (total == 0)
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center><a class='green_link' href=javascript:onclick="
					+ jsStr.replaceAll("\\#", devicetypeStr)
							.replaceAll("\\?", userCityID)
							.replaceAll("\\$", bindType)
							+ ">" + total + "</TD>");
		}
		// sb.append("<TD bgcolor=#ffffff align=center><a class='green_link'
		// href=javascript:onclick=customize('"+vendor_id+"')>定制</a></TD>");
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
	private String getEditionStatePrint(String vendor_id, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			message = (String) versionMap.get(vendor_id);
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
			}
			else
			{
				sb.append("<TR ><TD nowrap class=column align='right'></TD>");
			}
		}
		else
		{
			message = (String) vendorMap.get(vendor_id);
			sb.append("<TR ><TD nowrap class=column align='right'>" + message + "</TD>");
		}
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				if (userCityID.equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
				}
				else
				{
					num = getCityVendorCount(cityAll, vendor_id, mapServiceCityNum);
				}
				if (num != null && !"0".equals(num))
				{
					if (!"".equals(num))
					{
						total += Long.parseLong(num);
					}
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
		sb.append("<TD bgcolor=#ffffff align=center>" + total + "</TD>");
		sb.append("</TR>");
		return sb.toString();
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
	 * 获取当前用户的下级属地
	 *
	 * @param request
	 * @return
	 */
	public Cursor getCityList(HttpServletRequest request, String cityId)
	{
		if (null == cityId || "-1".equals(cityId))
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ cityId + "' or city_id='" + cityId + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}

	public String getDevicetypeIdStr()
	{
		return devicetypeIdStr;
	}

	public void setDevicetypeIdStr(String devicetypeIdStr)
	{
		this.devicetypeIdStr = devicetypeIdStr;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}
}
