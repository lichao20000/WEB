/**
 * 业务操作相关类 与tab_service tab_hgwcustomer相关
 * 
 * @author 联创科技 1.0
 */

package com.linkage.litms.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;
import com.sun.tools.corba.se.idl.constExpr.And;

/**
 * 业务操作相关类 与tab_service tab_hgwcustomer相关
 */
public class ServiceActHblt
{

	private static Logger logger = LoggerFactory.getLogger(ServiceAct.class);
	private Cursor cursor = null;
	private Map fields = null;
	private PrepareSQL pSQL;
	private Map cityTotalMap = new HashMap();
	private Map cityListMap = new HashMap();
	private Map versionMap = null;
	private Map vendorMap = null;
	private Map modelMap = null;
	// 同一个设备版本小于20的版本id
	private String edtionIdStr = null;
	private List cityIdList = new ArrayList();
	/**
	 * 按属地和业务统计用户设备
	 */
	private String m_ServiceState = "select a.serv_type_id,b.city_id,count(distinct b.device_id) as num"
			+ " from tab_hgwcustomer a,tab_gw_device b where a.device_id = b.device_id and b.device_status =1 and b.gw_type=? and a.serv_type_id in(select serv_type_id from tab_gw_serv_type where type=? ) "
			+ " group by a.serv_type_id,b.city_id";
	private String m_out_ServiceState = "select serv_type_id,city_id,count(username) as num"
			+ " from tab_hgwcustomer where service_id in(select service_id from tab_service where flag=0 ) "
			+ " group by serv_type_id,city_id";
	// private String m_vendorDevice =
	// "select count(device_id) as num,oui,city_id from tab_gw_device where oui in(select vendor_id from tab_vendor) and device_status = 1 and gw_type=? group by oui,city_id";
	private String m_vendorDevice = "select count(device_id) as num,vendor_id,city_id from tab_gw_device where device_status = 1 and customer_id is not null and gw_type=? group by vendor_id,city_id";
	private String m_vendorDevice_stb = "select count(device_id) as num,vendor_id,city_id from stb_tab_gw_device where device_status = 1 and gw_type=? group by vendor_id,city_id";
	private String m_editionDevice = "select count(device_id) as num,devicetype_id,city_id from tab_gw_device where device_status = 1 and gw_type=? ";
	/**
	 * 按属地统计用户设备
	 */
	private String m_CityDevice = "select city_id,count(device_id) as num "
			+ " from tab_gw_device where device_status =1 and gw_type=? "
			+ " group by city_id";
	/**
	 * service_id service_name 关系映射
	 */
	private String m_ServiceInfo = "select service_id,service_name from tab_service";
	private String m_ServiceInfo_List = "select serv_type_id,serv_type_name from tab_gw_serv_type where type = ?";
	private String m_VendorInfo = "select vendor_id,vendor_name from tab_vendor";
	private String m_VendorInfo_stb = "select vendor_id,vendor_name from stb_tab_vendor";
	// private String m_EditionInfo =
	// "select devicetype_id,device_model from tab_devicetype_info";
	private String m_EditionInfo = "select a.vendor_id,device_model,devicetype_id from tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id order by  a.vendor_id,device_model,devicetype_id";

	public ServiceActHblt()
	{
		pSQL = new PrepareSQL();
	}

	/**
	 * 按属地和业务统计用户设备,暂时不考虑权限过滤
	 * 
	 * @return cursor
	 */
	public Cursor getServiceOfHGWCustomer(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 如果是管理员
		if (curUser.getUser().isAdmin())
		{
		}
		else
		{// 其他用户，则需要权限过滤
		}
		pSQL.setSQL(m_ServiceState);
		pSQL.setInt(1, gw_type);
		pSQL.setInt(2, gw_type);
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	public Cursor getServiceOutHGWCustomer(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 如果是管理员
		if (curUser.getUser().isAdmin())
		{
		}
		else
		{// 其他用户，则需要权限过滤
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
	public Map getCityService(int gw_type)
	{
		pSQL.setSQL(m_CityDevice);
		pSQL.setInt(1, gw_type);
		cursor = DataSetBean.getCursor(pSQL.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		fields = cursor.getNext();
		Map cityDevice = new HashMap();
		while (fields != null)
		{
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
	public HashMap getServiceMap()
	{
		return DataSetBean.getMap(m_ServiceInfo);
	}

	/**
	 * 将service_id和service_name分别放在两个链表中
	 * 
	 * @return
	 */
	public List[] getServiceIdNameList(int gw_type)
	{
		pSQL.setSQL(m_ServiceInfo_List);
		pSQL.setInt(1, gw_type);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		List[] listService = new ArrayList[2];
		listService[0] = new ArrayList();// service_id
		listService[1] = new ArrayList();// service_name
		fields = cursor.getNext();
		while (fields != null)
		{
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
	public String getHtmlDeviceOnService(HttpServletRequest request, int gw_type)
	{
		// 先读取所有业务
		List[] listService = getServiceIdNameList(gw_type);
		if (listService == null || listService[0].size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		StringBuffer sbTable = new StringBuffer();
		List listServiceId = listService[0];
		List listServiceName = listService[1];
		// 输出表头－－>业务名称
		Iterator it = listServiceName.iterator();
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap></TH>");
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>" + it.next() + "</TH>");
		}
		sbTable.append("</TR>");
		// 在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getServiceOfHGWCustomer(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"service_id" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 将业务+属地==>num 作为映射存入map中
			mapServiceCityNum.put(
					fields.get("city_id") + "_" + fields.get("serv_type_id"),
					fields.get("num"));
			fields = cursor.getNext();
		}
		// 开始遍历属地
		// it = cityMap.keySet().iterator();
		Cursor cityList = getCityList(request);
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getSerStatePrint((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum, null));
			}
			else
			{
				sbTable.append(getSerStateRowHtml((String) cityField.get("city_id"),
						cityMap, listServiceId, mapServiceCityNum, null));
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
	public String getHtmlDeviceOutService(HttpServletRequest request, int gw_type)
	{
		// 先读取所有业务
		List[] listService = getServiceIdNameList(gw_type);
		if (listService == null || listService[0].size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		StringBuffer sbTable = new StringBuffer();
		List listServiceId = listService[0];
		List listServiceName = listService[1];
		// 输出表头－－>业务名称
		Iterator it = listServiceName.iterator();
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap></TH>");
		while (it.hasNext())
		{
			sbTable.append("<TH nowrap>" + it.next() + "</TH>");
		}
		sbTable.append("</TR>");
		// 在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getServiceOfHGWCustomer(request, gw_type);
		fields = cursor.getNext();
		// KEY:"city_id"_"service_id" VALUE:num
		Map mapServiceCityNum = new HashMap();
		while (fields != null)
		{
			// 将业务+属地==>num 作为映射存入map中
			mapServiceCityNum.put(
					fields.get("city_id") + "_" + fields.get("serv_type_id"),
					fields.get("num"));
			fields = cursor.getNext();
		}
		// 获取各个地市的设备数
		Map cityDevice = getCityService(gw_type);
		// 开始遍历属地
		// it = cityMap.keySet().iterator();
		Cursor cityList = getCityList(request);
		String isPrt = request.getParameter("isPrt");
		Map cityField = cityList.getNext();
		while (cityField != null)
		{
			String city_ID = (String) cityField.get("city_id");
			// String cityNum = (String) cityDevice.get(city_ID);
			String cityNum = "";
			if ("00".equals(city_ID))
			{
				cityNum = (String) cityDevice.get(city_ID);
			}
			else
			{
				cityNum = getCityCount(city_ID, cityDevice);
			}
			if ("1".equals(isPrt))
			{
				sbTable.append(getSerStatePrint(city_ID, cityMap, listServiceId,
						mapServiceCityNum, cityNum));
			}
			else
			{
				sbTable.append(getSerStateRowHtml(city_ID, cityMap, listServiceId,
						mapServiceCityNum, cityNum));
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
	private String getSerStateRowHtml(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum, String cityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String servId = null;
		String num = null;
		StringBuffer sb = new StringBuffer(listServiceId.size() + 2);
		String jsStr = "detail('?','#')";
		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='right'>" + cityMap.get(city_id)
				+ "</TD>");
		while (it.hasNext())
		{
			servId = (String) it.next();
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				// num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				}
				else
				{
					num = getCityServiceCount(cityAll, servId, mapServiceCityNum);
				}
				if (cityNum != null)
				{
					long tmp = 0;
					if (num != null)
					{
						tmp = Long.parseLong(cityNum) - Long.parseLong(num);
					}
					else
					{
						tmp = Long.parseLong(cityNum) - 0;
					}
					num = String.valueOf(tmp);
				}
				if (num != null && !"0".equals(num))
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#", servId)
							+ ">" + num + "</a></TD>");
				else
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
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
	private String getSerStatePrint(String city_id, Map cityMap, List listServiceId,
			Map mapServiceCityNum, String cityNum)
	{
		Iterator it = listServiceId.iterator();
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String servId = null;
		String num = null;
		StringBuffer sb = new StringBuffer(listServiceId.size() + 2);
		// 输出地市名称
		sb.append("<TR ><TD nowrap align='right'>" + cityMap.get(city_id) + "</TD>");
		while (it.hasNext())
		{
			servId = (String) it.next();
			// 根据{属地_业务id} 得到 统计数量
			if (mapServiceCityNum != null)
			{
				// num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				if ("00".equals(city_id))
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + servId);
				}
				else
				{
					num = getCityServiceCount(cityAll, servId, mapServiceCityNum);
				}
				if (cityNum != null)
				{
					long tmp = 0;
					if (num != null)
					{
						tmp = Long.parseLong(cityNum) - Long.parseLong(num);
					}
					else
					{
						tmp = Long.parseLong(cityNum) - 0;
					}
					num = String.valueOf(tmp);
				}
				if (num != null && !"0".equals(num))
					sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
				else
					sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
			else if (mapServiceCityNum == null)
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		cityAll = null;
		if (cityTotalMap != null)
		{
			sb.append("<TD bgcolor=#ffffff align=center>" + cityTotalMap.get(city_id)
					+ "</TD>");
		}
		sb.append("</TR>");
		return sb.toString();
	}

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

	private String getCityCount(String city_id, Map tmpMap)
	{
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
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
		cityAll = null;
		return String.valueOf(total);
	}

	/**
	 * 获取属地
	 */
	public String getCityId(Cursor cursor)
	{
		StringBuffer bf = new StringBuffer();
		List listShow = new ArrayList();
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		/* cursor = getCityList(request); */
		fields = cursor.getNext();
		if (null != fields)
		{
			while (null != fields)
			{
				String city_id = String.valueOf(fields.get("city_id"));
				String city_name = String.valueOf(fields.get("city_name"));
				if (null != city_id && city_id != "")
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
	 * 按厂商统计设备数量
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByVendor(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCity = curUser.getCityId();
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		StringBuffer sbTable = new StringBuffer();
		// 初始化厂商信息
		getVendorMap(gw_type);
		// 先读取所有业务
		List[] listService = getVendorIdNameList(gw_type_Str);
		if (listService == null)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		List listServiceId = listService[0];
		// 输出表头
		sbTable.append("<TR>");
		sbTable.append("<TH width='20%'>厂商</TH>");
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		cursor = getCityList(request);
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				cityList.add(fields.get("city_id"));
				sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		}
		else
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center' class=column>系统中暂无属地数据!</TD></TR>";
		}
		sbTable.append("<TH nowrap>小计</TH>");
		sbTable.append("</TR>");
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
		if ("4".equals(gw_type_Str))
		{
			pSQL.setSQL(m_vendorDevice_stb);
		}
		else
		{
			if (Global.NXDX.equals(Global.instAreaShortName))
			{
				pSQL.setSQL("select count(a.device_id) as num,a.vendor_id,a.city_id from tab_gw_device a,tab_hgwcustomer b ,tab_devicetype_info t where a.device_status = 1 and a.devicetype_id=t.devicetype_id  and a.device_id = b.device_id  and b.username is not null and a.cpe_allocatedstatus = 1  and a.customer_id is not null and gw_type=? group by a.vendor_id,a.city_id");
			}
			else
			{
				pSQL.setSQL(m_vendorDevice);
			}
		}
		if (null != isBind && !"null".equals(isBind) && !"0".equals(isBind)
				&& !"".equals(isBind) && !"undefined".equals(isBind))
		{
			pSQL.setSQL("select count(device_id) as num,vendor_id,city_id from tab_gw_device where device_status = 1 and gw_type=? and customer_id is not null group by vendor_id,city_id");
		}
		pSQL.setInt(1, gw_type);
		cursor = DataSetBean.getCursor(pSQL.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		fields = cursor.getNext();
		while (fields != null)
		{
			if (cityMap.get(fields.get("city_id")) != null)
			{
				// 将业务+属地==>num 作为映射存入map中
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("vendor_id"),
						fields.get("num"));
				tmp = (String) fields.get("num");
				if ((tmp == null) || "".equals(tmp))
				{
					tmp = "0";
				}
				// 按厂商累加
				vendortmp = Long.parseLong(tmp)
						+ Long.parseLong((String) cityTotalMap.get(fields.get("city_id")));
				cityTotalMap.put(fields.get("city_id"), String.valueOf(vendortmp));
			}
			fields = cursor.getNext();
		}
		// 开始遍历,输出统计数据
		String isPrt = request.getParameter("isPrt");
		Iterator it = listServiceId.iterator();
		while (it.hasNext())
		{
			if ("1".equals(isPrt))
			{
				sbTable.append(getEditionStatePrint((String) it.next(), "0", cityList,
						mapServiceCityNum, gw_type, userCity));
			}
			else
			{
				sbTable.append(getVendorStatRowHtml((String) it.next(), "0", cityList,
						mapServiceCityNum, gw_type, userCity, isBind));
			}
		}
		// 输出小计信息
		sbTable.append("<tr><td nowrap align='right' class=column>小计</td>");
		sbTable.append(getSubTotal(cityList, isPrt, total, gw_type, userCity));
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
	 * 按型号统计设备（明细）
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByModelDetail(HttpServletRequest request)
	{
		logger.warn("getHtmlDeviceByModelDetail()");
		StringBuffer sbTable = new StringBuffer();
		String time = request.getParameter("countTime");
		String fileter = "";
		if (time == null)
		{
			return sbTable.toString();
		}
		long starttime = getTimeForMonth("start", time);
		long endtime = getTimeForMonth("end", time);
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		fileter += "&gw_type=" + gw_type_Str;
		int gw_type = Integer.parseInt(gw_type_Str);
		// 初始化地市信息
		DeviceAct devAct = new DeviceAct();
		HashMap initCityMap = devAct.getCityMap_All();// devAct.getCityMap(request);
		// 初始化厂商信息
		getVendorMap(gw_type);
		// 初始化型号信息
		getModelMap();
		String cityId = request.getParameter("cityId");
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			fileter += "&vendorId=" + vendorId;
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			fileter += "&modelId=" + modelId;
		}
		if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
		{
			fileter += "&cityId=" + cityId;
		}
		fileter += "&countTime=" + time;
		String isPrt = request.getParameter("isPrt");
		Map cityMap = new HashMap();
		if (cityId != null && !"".equals(cityId) && !"-1".equals(cityId))
		{
			cityMap = CityDAO.getNextCityMapByCityPid(cityId);
		}
		else
		{
			cityMap = CityDAO.getNextCityMapByCityPid("00");
		}
		String stroffset = request.getParameter("offset");
		int offset = 0;
		int pagelen = 50;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		List list = getModelDetailIdNameMap(vendorId, modelId, cityMap, starttime,
				endtime, gw_type, offset, pagelen, fileter, isPrt);
		String strBar = (String) list.get(0);
		Map mapService = (Map) list.get(1);
		if (mapService == null || mapService.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH>LOID</TH><TH>宽带账号</TH><TH>语音账号1</TH><TH>语音账号2</TH><TH>厂家</TH><TH>型号</TH><TH>数量</TH><TH>终端入网时间</TH>");
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itCity = mapService.keySet().iterator();
		List<String> sortList = new ArrayList<String>();
		while (itCity.hasNext())
		{
			String key = (String) itCity.next();
			List tmpList = (List) mapService.get(key);
			int size = 0;
			if (tmpList != null)
			{
				size = tmpList.size();
			}
			mapServiceSize.put(key, size);
			sortList.add(key);
		}
		Collections.sort(sortList);
		for (int j = 0; j < sortList.size(); j++)
		{
			String key = sortList.get(j);
			// 同个厂商版本的个数 key为厂商id
			sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key)
					+ "' class=column align='right'>" + initCityMap.get(key) + "</TD>");
			List deviceList = (List) mapService.get(key);
			for (int i = 0; i < deviceList.size(); i++)
			{
				Map tmpmap = (Map) deviceList.get(i);
				String user_id = StringUtil.getStringValue(tmpmap, "user_id");
				String[] account = getAccountFromUserId(user_id);
				String vendor = (String) vendorMap.get(StringUtil.getStringValue(tmpmap,
						"vendor_id"));
				if (vendor.indexOf("(") > -1)
				{
					vendor = vendor.substring(0, vendor.indexOf("("));
				}
				sbTable.append("<TD nowrap class=column align='right'>"
						+ StringUtil.getStringValue(tmpmap, "loid") + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[0]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[1]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[2]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + vendor
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>"
						+ modelMap.get(StringUtil.getStringValue(tmpmap,
								"device_model_id")) + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + 1 + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>"
						+ getDateString(StringUtil.getStringValue(tmpmap, "time"))
						+ "</TD></TR>");
			}
		}
		sbTable.append("<TR><TD class=column COLSPAN=9 align=right>" + strBar
				+ "</TD></TR>");
		return sbTable.toString();
	}

	/**
	 * 按型号统计设备（汇总跳转的明细）
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceListByModel(HttpServletRequest request)
	{
		logger.warn("getHtmlDeviceListByModel()");
		StringBuffer sbTable = new StringBuffer();
		String countTime = request.getParameter("countTime");
		String fileter = "";
		if (countTime == null)
		{
			return sbTable.toString();
		}
		long starttime = getTimeForMonth("start", countTime);
		long endtime = getTimeForMonth("end", countTime);
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		fileter += "&gw_type=" + gw_type_Str;
		int gw_type = Integer.parseInt(gw_type_Str);
		// 初始化地市信息
		DeviceAct devAct = new DeviceAct();
		HashMap initCityMap = devAct.getCityMap(request);
		HashMap initCityAllMap = devAct.getCityMap_All();
		// 初始化厂商信息
		getVendorMap(gw_type);
		// 初始化型号信息
		getModelMap();
		String cityId = request.getParameter("cityId");
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			fileter += "&vendorId=" + vendorId;
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			fileter += "&modelId=" + modelId;
		}
		if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
		{
			fileter += "&cityId=" + cityId;
		}
		fileter += "&countTime=" + countTime;
		HashMap cityMap = new HashMap();
		if (cityId != null && !"".equals(cityId) && !"-1".equals(cityId))
		{
			cityMap.put(cityId, cityId);
		}
		else
		{
			cityMap = initCityMap;
		}
		StringBuffer bf = new StringBuffer();
		ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
		bf.append("select b.device_id,c.username,b.city_id,b.vendor_id,b.device_model_id,b.complete_time,c.user_id from "
				+ "tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id and b.gw_type = "
				+ gw_type);
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			bf.append(" and b.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			bf.append(" and b.device_model_id='" + modelId + "' ");
		}
		if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
		{
			bf.append(" and b.city_id in ('" + cityStr + "')");
		}
		bf.append(" and b.complete_time < " + endtime);
		bf.append(" and b.complete_time > " + starttime);
		bf.append(" order by b.vendor_id,b.device_model_id ");
		logger.warn("bf.toString:{}", bf.toString());
		List<Map> dataList = new ArrayList<Map>();
		PrepareSQL psql = new PrepareSQL(bf.toString());
		psql.getSQL();
		String isPrt = request.getParameter("isPrt");
		String strBar = "";
		if ("1".equals(isPrt))
		{
			cursor = DataSetBean.getCursor(bf.toString());
		}
		else
		{
			String stroffset = request.getParameter("offset");
			int pagelen = 50;
			int offset;
			if (stroffset == null)
				offset = 1;
			else
				offset = Integer.parseInt(stroffset);
			QueryPage qryp = new QueryPage();
			qryp.initPage(bf.toString(), offset, pagelen);
			strBar = qryp.getPageBar(fileter);
			cursor = DataSetBean.getCursor(bf.toString(), offset, pagelen);
		}
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String vendor_id = String.valueOf(fields.get("vendor_id"));
				String device_model_id = String.valueOf(fields.get("device_model_id"));
				String loid = String.valueOf(fields.get("username"));
				long time = Long.parseLong(String.valueOf(fields.get("complete_time")));
				String user_id = String.valueOf(fields.get("user_id"));
				String city_id = String.valueOf(fields.get("city_id"));
				Map tmp = new HashMap();
				tmp.put("vendor_id", vendor_id);
				tmp.put("device_model_id", device_model_id);
				tmp.put("loid", loid);
				tmp.put("time", time);
				tmp.put("user_id", user_id);
				tmp.put("city_id", city_id);
				dataList.add(tmp);
				fields = cursor.getNext();
			}
		}
		if (dataList == null)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH>LOID</TH><TH>宽带账号</TH><TH>语音账号1</TH><TH>语音账号2</TH><TH>厂家</TH><TH>型号</TH><TH>数量</TH><TH>终端入网时间</TH>");
		for (int j = 0; j < dataList.size(); j++)
		{
			Map tmpmap = dataList.get(j);
			String user_id = StringUtil.getStringValue(tmpmap, "user_id");
			String[] account = getAccountFromUserId(user_id);
			String vendor = (String) vendorMap.get(StringUtil.getStringValue(tmpmap,
					"vendor_id"));
			if (vendor.indexOf("(") > -1)
			{
				vendor = vendor.substring(0, vendor.indexOf("("));
			}
			sbTable.append("<TR ><TD nowrap  class=column align='right'>"
					+ initCityAllMap.get(StringUtil.getStringValue(tmpmap, "city_id"))
					+ "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>"
					+ StringUtil.getStringValue(tmpmap, "loid") + "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>" + account[0]
					+ "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>" + account[1]
					+ "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>" + account[2]
					+ "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>" + vendor + "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>"
					+ modelMap.get(StringUtil.getStringValue(tmpmap, "device_model_id"))
					+ "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>" + 1 + "</TD>");
			sbTable.append("<TD nowrap class=column align='right'>"
					+ getDateString(StringUtil.getStringValue(tmpmap, "time"))
					+ "</TD></TR>");
		}
		sbTable.append("<TR><TD class=column COLSPAN=9 align=right>" + strBar
				+ "</TD></TR>");
		return sbTable.toString();
	}

	/**
	 * 按型号统计设备（汇总跳转的明细）
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceListByModelBak(HttpServletRequest request)
	{
		logger.warn("getHtmlDeviceListByModel()");
		StringBuffer sbTable = new StringBuffer();
		String countTime = request.getParameter("countTime");
		String fileter = "";
		if (countTime == null)
		{
			return sbTable.toString();
		}
		long starttime = getTimeForMonth("start", countTime);
		long endtime = getTimeForMonth("end", countTime);
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		fileter += "&gw_type=" + gw_type_Str;
		int gw_type = Integer.parseInt(gw_type_Str);
		// 初始化地市信息
		DeviceAct devAct = new DeviceAct();
		HashMap initCityMap = devAct.getCityMap(request);
		// 初始化厂商信息
		getVendorMap(gw_type);
		// 初始化型号信息
		getModelMap();
		String cityId = request.getParameter("cityId");
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			fileter += "&vendorId=" + vendorId;
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			fileter += "&modelId=" + modelId;
		}
		if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
		{
			fileter += "&cityId=" + cityId;
		}
		fileter += "&countTime=" + countTime;
		HashMap cityMap = new HashMap();
		if (cityId != null && !"".equals(cityId) && !"-1".equals(cityId))
		{
			cityMap.put(cityId, cityId);
		}
		else
		{
			cityMap = initCityMap;
		}
		Map mapService = new HashMap();// getModelDetailIdNameMap(vendorId,
		// modelId,cityMap,starttime,endtime,gw_type);
		StringBuffer bf = new StringBuffer();
		ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
		bf.append("select a.device_id,a.username,a.city_id,a.vendor_id,a.device_model_id,a.complete_time,a.user_id from "
				+ "(select * from tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id and b.gw_type = 1) a where a.gw_type = "
				+ gw_type);
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			bf.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			bf.append(" and a.device_model_id='" + modelId + "' ");
		}
		if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
		{
			bf.append(" and a.city_id in ('" + cityStr + "')");
		}
		bf.append(" and a.complete_time < " + endtime);
		bf.append(" and a.complete_time > " + starttime);
		bf.append(" order by vendor_id,device_model_id ");
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(bf.toString(), offset, pagelen);
		String strBar = qryp.getPageBar(fileter);
		logger.warn("bf.toString:{}", bf.toString());
		PrepareSQL psql = new PrepareSQL(bf.toString());
		psql.getSQL();
		cursor = DataSetBean.getCursor(bf.toString(), offset, pagelen);
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String vendor_id = String.valueOf(fields.get("vendor_id"));
				String device_model_id = String.valueOf(fields.get("device_model_id"));
				String loid = String.valueOf(fields.get("username"));
				long time = Long.parseLong(String.valueOf(fields.get("complete_time")));
				String user_id = String.valueOf(fields.get("user_id"));
				List<Map> dataList = null;
				if (null == mapService.get(cityId))
				{
					dataList = new ArrayList<Map>();
				}
				else
				{
					dataList = (List<Map>) mapService.get(cityId);
				}
				Map tmp = new HashMap();
				tmp.put("vendor_id", vendor_id);
				tmp.put("device_model_id", device_model_id);
				tmp.put("loid", loid);
				tmp.put("time", time);
				tmp.put("user_id", user_id);
				dataList.add(tmp);
				mapService.put(cityId, dataList);
				fields = cursor.getNext();
			}
		}
		if (mapService == null)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH>LOID</TH><TH>宽带账号</TH><TH>语音账号1</TH><TH>语音账号2</TH><TH>厂家</TH><TH>型号</TH><TH>数量</TH><TH>终端入网时间</TH>");
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		Iterator itCity = mapService.keySet().iterator();
		List<String> sortList = new ArrayList<String>();
		while (itCity.hasNext())
		{
			String key = (String) itCity.next();
			List tmpList = (List) mapService.get(key);
			int size = 0;
			if (tmpList != null)
			{
				size = tmpList.size();
			}
			mapServiceSize.put(key, size);
			sortList.add(key);
		}
		Collections.sort(sortList);
		for (int j = 0; j < sortList.size(); j++)
		{
			String key = sortList.get(j);
			// 同个厂商版本的个数 key为厂商id
			sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key)
					+ "' class=column align='right'>" + initCityMap.get(key) + "</TD>");
			List deviceList = (List) mapService.get(key);
			for (int i = 0; i < deviceList.size(); i++)
			{
				Map tmpmap = (Map) deviceList.get(i);
				String user_id = StringUtil.getStringValue(tmpmap, "user_id");
				String[] account = getAccountFromUserId(user_id);
				sbTable.append("<TD nowrap class=column align='right'>"
						+ StringUtil.getStringValue(tmpmap, "loid") + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[0]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[1]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + account[2]
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>"
						+ vendorMap.get(StringUtil.getStringValue(tmpmap, "vendor_id"))
						+ "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>"
						+ modelMap.get(StringUtil.getStringValue(tmpmap,
								"device_model_id")) + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>" + 1 + "</TD>");
				sbTable.append("<TD nowrap class=column align='right'>"
						+ getDateString(StringUtil.getStringValue(tmpmap, "time"))
						+ "</TD></TR>");
			}
		}
		sbTable.append("<TR><TD class=column COLSPAN=9 align=right>" + strBar
				+ "</TD></TR>");
		return sbTable.toString();
	}

	/**
	 * 按型号统计设备数量 汇总
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByModel(HttpServletRequest request)
	{
		logger.warn("getHtmlDeviceByModel()");
		StringBuffer sbTable = new StringBuffer();
		String time = request.getParameter("countTime");
		if (time == null)
		{
			return sbTable.toString();
		}
		long starttime = getTimeForMonth("start", time);
		long endtime = getTimeForMonth("end", time);
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		// 初始化地市信息
		DeviceAct devAct = new DeviceAct();
		HashMap initCityMap = devAct.getCityMap(request);
		// 初始化厂商信息
		getVendorMap(gw_type);
		// 初始化型号信息
		getModelMap();
		String cityId = request.getParameter("cityId");
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		HashMap cityMap = new HashMap();
		if (cityId != null && !"".equals(cityId) && !"-1".equals(cityId))
		{
			cityMap.put(cityId, cityId);
		}
		else
		{
			cityMap = initCityMap;
		}
		Map mapService = getModelIdNameMap(vendorId, modelId, cityMap, starttime,
				endtime, gw_type);
		cityMap = null;
		if (null == mapService || mapService.size() == 0)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH>厂家</TH><TH>型号</TH><TH>数量</TH><TH>月份（终端入网月份）</TH>");
		Map<String, Integer> mapServiceSize = new HashMap<String, Integer>();
		List<String> sortList = new ArrayList<String>();
		Iterator itCity = mapService.keySet().iterator();
		while (itCity.hasNext())
		{
			String key = (String) itCity.next();
			Map valueMap = (Map) mapService.get(key);
			int size = 0;
			Iterator itDevicemodel = valueMap.keySet().iterator();
			while (itDevicemodel.hasNext())
			{
				size = size + ((Map) valueMap.get(itDevicemodel.next())).size();
			}
			mapServiceSize.put(key, size);
			sortList.add(key);
		}
		String isPrt = request.getParameter("isPrt");
		Collections.sort(sortList);
		for (int j = 0; j < sortList.size(); j++)
		{
			String key = sortList.get(j);
			// 同个厂商版本的个数 key为厂商id
			sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key)
					+ "' class=column align='right'>" + initCityMap.get(key) + "</TD>");
			Map valueMap = (Map) mapService.get(key);
			Iterator itDevicevendor = valueMap.keySet().iterator();
			while (itDevicevendor.hasNext())
			{
				String keyVendor = (String) itDevicevendor.next();
				Map devicemodelMap = (Map) valueMap.get(keyVendor);
				String vendor = (String) vendorMap.get(keyVendor);
				if (vendor.indexOf("(") > -1)
				{
					vendor = vendor.substring(0, vendor.indexOf("("));
				}
				// 同个厂商同个型号版本的个数
				sbTable.append("<TD nowrap rowspan='" + devicemodelMap.size()
						+ "' class=column align='right'>" + vendor + "</TD>");
				Iterator modelIT = devicemodelMap.keySet().iterator();
				while (modelIT.hasNext())
				{
					String keyModel = (String) modelIT.next();
					String jsStr = "detail('?','#','%','" + gw_type + "')";
					long num = (Long) devicemodelMap.get(keyModel);
					sbTable.append("<TD nowrap class=column align='right'>"
							+ modelMap.get(keyModel) + "</TD>");
					if ("1".equals(isPrt))
					{
						sbTable.append("<TD nowrap class=column bgcolor=#ffffff align=center>"
								+ num + "</TD>");
						sbTable.append("<TD nowrap class=column align='right'>"
								+ time.replace("-", "") + "</TD></TR>");
					}
					else
					{
						sbTable.append("<TD nowrap class=column bgcolor=#ffffff align=center><a href=javascript:onclick="
								+ jsStr.replaceAll("\\?", key)
										.replaceAll("\\#", keyVendor)
										.replaceAll("\\%", keyModel)
								+ ">"
								+ num
								+ "</a></TD>");
						sbTable.append("<TD nowrap class=column align='right'>" + time
								+ "</TD></TR>");
					}
				}
			}
		}
		return sbTable.toString();
	}

	/**
	 * 按版本统计设备数量
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByEdition(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCityID = curUser.getCityId();
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
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String sqlid = request.getParameter("sqlid");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		StringBuffer sbTable = new StringBuffer();
		getVendorMap(gw_type);
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String is_esurfing = request.getParameter("is_esurfing");// 天翼网关更改的地方 jiangkun
		logger.debug("deviceTypeId:{}", deviceTypeId);
		String no_query = request.getParameter("no_query");
		String city_id = request.getParameter("city_id");// 修改处
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
		String use_state = request.getParameter("use_state");
		// hb_lt增加三级属地过滤
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& 4 == gw_type)
		{
			String citynext = request.getParameter("citynext");
			logger.warn("citynext is .. ：" + citynext);
			if (null == citynext || "null".equals(citynext))
			{
				citynext = "";
			}
			if ((!citynext.isEmpty()) && (!"-1".equals(citynext)))
			{
				city_id = citynext;
			}
		}
		if (no_query == null || no_query.trim().equalsIgnoreCase(""))
		{
			// 初始化版本信息
			getVersionMap(gw_type);
			// 在从数据库中 统计 业务属地对应的数据
			// 属地MAP 当前用户看到本身及其下属地市
			List list = new ArrayList();
			if (gw_type == 4)
			{
				cursor = getCity(city_id);
			}
			else
			{
				cursor = getCityList(request);
			}
			fields = cursor.getNext();
			if (fields != null)
			{
				while (fields != null)
				{
					list.add(fields.get("city_id"));
					fields = cursor.getNext();
				}
			}
			StringBuffer citySql = new StringBuffer();
			citySql.append(" select city_id from tab_city where 1=1");
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				citySql.append(" and city_id in (");
				for (int i = 0; i < list.size(); i++)
				{
					String edtionId = StringUtil.getStringValue(list.get(i));
					if (i + 1 == list.size())
					{
						citySql.append("'" + edtionId + "')");
					}
					else
					{
						citySql.append("'" + edtionId + "',");
					}
				}
				citySql.append(" or parent_id in( ");
				for (int i = 0; i < list.size(); i++)
				{
					String edtionId = StringUtil.getStringValue(list.get(i));
					if (i + 1 == list.size())
					{
						citySql.append("'" + edtionId + "')");
					}
					else
					{
						citySql.append("'" + edtionId + "',");
					}
				}
			}
			String sqlForCityId = citySql.toString();
			PrepareSQL idCount = new PrepareSQL(sqlForCityId);
			idCount.getSQL();
			cursor = DataSetBean.getCursor(sqlForCityId);
			fields = cursor.getNext();
			while (fields != null)
			{
				cityIdList.add(StringUtil.getStringValue(fields.get("city_id")));
				fields = cursor.getNext();
			}
			StringBuffer sqlCount = new StringBuffer();
			if (4 == gw_type)
			{
				sqlCount.append(" select count(a.device_id) as num, a.devicetype_id from stb_tab_gw_device a,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 and 1=1");
			}
			else
			{
				sqlCount.append(" select count(a.device_id) as num, a.devicetype_id from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 and a.customer_id is not null and a.gw_type="
						+ gw_type + " and 1=1");
			}
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				sqlCount.append(" and a.city_id in (");
				for (int i = 0; i < cityIdList.size(); i++)
				{
					String cityId = StringUtil.getStringValue(cityIdList.get(i));
					if (i + 1 == cityIdList.size())
					{
						sqlCount.append("'" + cityId + "')");
					}
					else
					{
						sqlCount.append("'" + cityId + "',");
					}
				}
			}
			String is_normal = request.getParameter("is_normal");
			// 是否规范
			if (null != is_normal && !"null".equals(is_normal) && !"-1".equals(is_normal)
					&& !"true".equals(is_normal))
			{
				sqlCount.append(" and t.is_normal=" + is_normal);
			}
			/*
			 * JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 0未用 1在用
			 */
			if (4 == gw_type)
			{
				if ("1".equals(use_state))
				{
					sqlCount.append(" and a.cpe_allocatedstatus=1 and a.serv_account is not null ");
				}
				else if ("0".equals(use_state))
				{
					sqlCount.append(" and a.cpe_allocatedstatus=0 and serv_account is null ");
				}
			}
			sqlCount.append(" group by a.devicetype_id ");
			PrepareSQL psqlCount = new PrepareSQL(sqlCount.toString());
			psqlCount.getSQL();
			cursor = DataSetBean.getCursor(sqlCount.toString());
			StringBuffer edtionIdBuffer = new StringBuffer();
			edtionIdBuffer.append("(");
			fields = cursor.getNext();
			List tempList = new ArrayList();
			while (fields != null)
			{
				String num = StringUtil.getStringValue(fields.get("num"));
				if (Integer.parseInt(num) > 0)
				{
					String devicetype_id = StringUtil.getStringValue(fields
							.get("devicetype_id"));
					tempList.add(devicetype_id);
				}
				fields = cursor.getNext();
			}
			for (int i = 0; i < tempList.size(); i++)
			{
				if (null == tempList || tempList.size() == 0)
				{
					return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
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
			edtionIdStr = edtionIdBuffer.toString();
			// 判断是否有业务 改
			String rela_dev_type_id = request.getParameter("rela_dev_type_id");
			String access_style_relay_id = request.getParameter("access_style_relay_id");
			Map mapService = getEditionMap(vendorId, modelId, deviceTypeId, edtionIdStr,
					rela_dev_type_id, access_style_relay_id, userCityID, gw_type,
					is_esurfing);
			if (null == mapService)
			{
				return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			}
			// 输出表头
			sbTable.append("<TR>");
			if (4 == gw_type)
			{
				sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>版本</TH>");
			}
			else
			{
				sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH><TH style='word-break:keep-all'>是否规范版本</TH>");
			}
			// 在从数据库中 统计 业务属地对应的数据
			// 属地MAP 当前用户看到本身及其下属地市
			List cityList = new ArrayList();
			if (gw_type == 4)
			{
				cursor = getCity(city_id);
			}
			else
			{
				cursor = getCityList(request);
			}
			fields = cursor.getNext();
			if (fields != null)
			{
				while (fields != null)
				{
					cityList.add(fields.get("city_id"));
					sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
					fields = cursor.getNext();
				}
			}
			else
			{
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
			long total = 0;
			Map mapServiceCityNum = new HashMap();
			String sql1;
			if (4 == gw_type)
			{
				sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from stb_tab_gw_device a, stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status = 1 ";
				pSQL.setSQL(sql1);
			}
			else
			{
				sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 and a.customer_id is not null and a.gw_type=? ";
				pSQL.setSQL(sql1);
				pSQL.setInt(1, gw_type);
			}
			StringBuffer sql = new StringBuffer();
			sql.append(pSQL.getSQL());
			// 是否规范
			if (null != is_normal && !"null".equals(is_normal) && !"-1".equals(is_normal)
					&& !"true".equals(is_normal))
			{
				sql.append(" and t.is_normal=" + is_normal);
			}
			/*
			 * JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 1在用 0未用
			 */
			if (4 == gw_type)
			{
				if ("1".equals(use_state))
				{
					sql.append(" and a.cpe_allocatedstatus=1 and a.serv_account is not null ");
				}
				else if ("0".equals(use_state))
				{
					sql.append(" and a.cpe_allocatedstatus=0 and serv_account is null ");
				}
			}
			if (null != startTime && !"".equals(startTime))
			{
				sql.append(" and a.complete_time>");
				sql.append(startTime);
			}
			if (null != endTime && !"".equals(endTime))
			{
				sql.append(" and a.complete_time<");
				sql.append(endTime);
			}
			if (Global.HBLT.equals(Global.instAreaShortName)
					&& 4 == gw_type)
			{
				logger.warn("recentStartTime : " + recentStartTime + " "
						+ "recentEndTime : " + recentEndTime);
				if (null != recentStartTime && !"".equals(recentStartTime))
				{
					sql.append(" and b.last_time>");
					sql.append(recentStartTime);
				}
				if (null != recentEndTime && !"".equals(recentEndTime))
				{
					sql.append(" and b.last_time<");
					sql.append(recentEndTime);
				}
			}
			if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId)
					&& vendorId != "")// 修改
			{
				sql.append(" and a.vendor_id='" + vendorId + "' ");
			}
			if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
			{
				sql.append(" and a.device_model_id='" + modelId + "' ");
			}
			if (null != deviceTypeId && !"null".equals(deviceTypeId)
					&& !"-1".equals(deviceTypeId))
			{
				sql.append(" and a.devicetype_id=" + deviceTypeId + " ");
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
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				sql.append(" and a.city_id in (");
				for (int i = 0; i < cityIdList.size(); i++)
				{
					String cityId = StringUtil.getStringValue(cityIdList.get(i));
					if (i + 1 == cityIdList.size())
					{
						sql.append("'" + cityId + "')");
					}
					else
					{
						sql.append("'" + cityId + "',");
					}
				}
			}
			// 天翼网关更改的地方 jiangkun
			if (Global.JXDX.equals(Global.instAreaShortName)
					&& is_esurfing.equals("1"))
			{
				sql.append(" and c.is_tyGate=" + is_esurfing);
			}
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
					total += Long.parseLong(tmp);
					// 按厂商累加，将小计信息放入cityTotalMap sssss
					vendortmp = Long.parseLong(tmp)
							+ Long.parseLong((String) cityTotalMap.get(fields
									.get("city_id")));
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
					size = size + ((List) valueMap.get(itDevicetype.next())).size();
				}
				mapServiceSize.put(key, size);
			}
			// 开始遍历属地,输出统计数据
			String isPrt = request.getParameter("isPrt");
			String cityIdhblt = "";
			String cityNextIdhblt = "";
			if (Global.HBLT.equals(Global.instAreaShortName))
			{
				cityIdhblt = request.getParameter("city_id");
				cityNextIdhblt = request.getParameter("citynext");
			}
			Iterator it = mapService.keySet().iterator();
			StringBuffer sbTableVendor = null;//
			StringBuffer sbTableModel = null;//
			int vendorSize = 0;//
			int modelSize = 0;//
			while (it.hasNext())
			{
				String key = (String) it.next();
				sbTableVendor = new StringBuffer();//
				// 同个厂商版本的个数 key为厂商id
				/*
				 * sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key) +
				 * "' class=column align='right'>" + vendorMap.get(key) + "</TD>");
				 */
				Map valueMap = (Map) mapService.get(key);
				Iterator itDevicetype = valueMap.keySet().iterator();
				vendorSize = ((Integer) mapServiceSize.get(key)).intValue();//
				while (itDevicetype.hasNext())
				{
					String keyModel = (String) itDevicetype.next();
					List devicetypeList = (List) valueMap.get(keyModel);
					modelSize = devicetypeList.size();
					sbTableModel = new StringBuffer();
					// 同个厂商同个型号版本的个数
					/*
					 * sbTable.append("<TD nowrap rowspan='" + devicetypeList.size() +
					 * "' class=column align='right'>" + keyModel + "</TD>");
					 */
					for (int i = 0; i < devicetypeList.size(); i++)
					{
						String devicetypeStr = String.valueOf(devicetypeList.get(i));
						String tmpRow = "";
						if ("1".equals(isPrt))
						{
							tmpRow = getEditionStatePrintHblt(devicetypeStr, "1",
									cityList, mapServiceCityNum, gw_type, userCityID,
									cityIdhblt, cityNextIdhblt);
						}
						else
						{
							tmpRow = getVendorStatRowHtmlHblt(devicetypeStr, "1",
									cityList, mapServiceCityNum, gw_type, userCityID,
									isBind, cityIdhblt, cityNextIdhblt);
						}
						if ((!"".equals(tmpRow)) && (tmpRow != null))
						{
							sbTableModel.append(tmpRow);
							tmpRow = "";
						}
						else
						{
							modelSize--;
							vendorSize--;
						}
					}
					if ((sbTableModel.length() > 0) && (!"".equals(sbTableModel))
							&& (sbTableModel != null))
					{
						sbTableVendor.append(
								"<TD nowrap rowspan='" + modelSize
										+ "' class=column align='right'>" + keyModel
										+ "</TD>").append(sbTableModel);
					}
					sbTableModel = null;
				}
				if ((sbTableVendor.length() > 0) && (!"".equals(sbTableVendor))
						&& (sbTableVendor != null))
				{
					sbTable.append(
							"<TR ><TD nowrap rowspan='" + vendorSize
									+ "' class=column align='right'>"
									+ this.vendorMap.get(key) + "</TD>").append(
							sbTableVendor);
				}
				sbTableVendor = null;
			}
			String num = "";
			// hb_lt增加三级属地过滤
			if (Global.HBLT.equals(Global.instAreaShortName)
					&& 4 == gw_type)
			{
				String citynext = request.getParameter("citynext");
				String status = "0";
				if (null == citynext || "null".equals(citynext))
				{
					citynext = "-1";
				}
				num = getSubTotalHB(cityList, isPrt, total, gw_type, userCityID,
						cityIdhblt, cityNextIdhblt, status).toString();
			}
			else
			{
				// total为 同一个地方所有版本的个数
				num = getSubTotal(cityList, isPrt, total, gw_type, userCityID).toString();
			}
			// 输出小计信息
			if (4 == gw_type)
			{
				if (Global.HBLT.equals(Global.instAreaShortName))
				{
					sbTable.append("<tr><td nowrap align='right' class=column colspan=3>小计</td>");
				}
				else
				{
					sbTable.append("<tr><td nowrap align='right' class=column colspan=4>小计</td>");
				}
			}
			else
			{
				sbTable.append("<tr><td nowrap align='right' class=column colspan=5>小计</td>");
			}
			sbTable.append(num);
			// Clear Resouce
			mapServiceCityNum.clear();
			mapServiceCityNum = null;
			cityMap = null;
		}
		cursor = null;
		fields = null;
		return sbTable.toString();
	}

	/**
	 * 按IPV6开通查询
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByV6Status(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCityID = curUser.getCityId();
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
		// 从request中取出gw_type 1:家庭网关设备 
		String gw_type_Str = request.getParameter("gw_type");
		String sqlid = request.getParameter("sqlid");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		StringBuffer sbTable = new StringBuffer();
		getVendorMap(gw_type);
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String is_esurfing = request.getParameter("is_esurfing");// 天翼网关更改的地方 jiangkun
		logger.debug("deviceTypeId:{}", deviceTypeId);
		String no_query = request.getParameter("no_query");
		String city_id = request.getParameter("city_id");// 修改处
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
		String use_state = request.getParameter("use_state");
		// hb_lt增加三级属地过滤
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& 1 == gw_type)
		{
			String citynext = request.getParameter("citynext");
			logger.warn("citynext is .. ：" + citynext);
			if (null == citynext || "null".equals(citynext))
			{
				citynext = "";
			}
			if ((!citynext.isEmpty()) && (!"-1".equals(citynext)))
			{
				city_id = citynext;
			}
		}
		if (no_query == null || no_query.trim().equalsIgnoreCase(""))
		{
			// 初始化版本信息
			getVersionMap(gw_type);
			// 在从数据库中 统计 业务属地对应的数据
			// 属地MAP 当前用户看到本身及其下属地市
			List list = new ArrayList();
			cursor = getCity(city_id);
			fields = cursor.getNext();
			if (fields != null)
			{
				while (fields != null)
				{
					list.add(fields.get("city_id"));
					fields = cursor.getNext();
				}
			}
			StringBuffer citySql = new StringBuffer();
			citySql.append(" select city_id from tab_city where 1=1");
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				citySql.append(" and city_id in (");
				for (int i = 0; i < list.size(); i++)
				{
					String edtionId = StringUtil.getStringValue(list.get(i));
					if (i + 1 == list.size())
					{
						citySql.append("'" + edtionId + "')");
					}
					else
					{
						citySql.append("'" + edtionId + "',");
					}
				}
				citySql.append(" or parent_id in( ");
				for (int i = 0; i < list.size(); i++)
				{
					String edtionId = StringUtil.getStringValue(list.get(i));
					if (i + 1 == list.size())
					{
						citySql.append("'" + edtionId + "')");
					}
					else
					{
						citySql.append("'" + edtionId + "',");
					}
				}
			}
			String sqlForCityId = citySql.toString();
			PrepareSQL idCount = new PrepareSQL(sqlForCityId);
			idCount.getSQL();
			cursor = DataSetBean.getCursor(sqlForCityId);
			fields = cursor.getNext();
			while (fields != null)
			{
				cityIdList.add(StringUtil.getStringValue(fields.get("city_id")));
				fields = cursor.getNext();
			}
			StringBuffer sqlCount = new StringBuffer();
			
			sqlCount.append(" select count(a.device_id) as num, a.devicetype_id from tab_gw_device a,tab_devicetype_info t,tab_net_serv_param d,tab_hgwcustomer e,hgwcust_serv_info h" +
					" where e.device_id = a.device_id and e.user_id = d.user_id and a.devicetype_id=t.devicetype_id and h.user_id =e.user_id  and a.device_status = 1 " +
					"and d.ip_type = 3 and a.customer_id is not null and h.serv_type_id = 10 and h.open_status = 1 and a.gw_type="
						+ gw_type + " and 1=1");
			
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				sqlCount.append(" and a.city_id in (");
				for (int i = 0; i < cityIdList.size(); i++)
				{
					String cityId = StringUtil.getStringValue(cityIdList.get(i));
					if (i + 1 == cityIdList.size())
					{
						sqlCount.append("'" + cityId + "')");
					}
					else
					{
						sqlCount.append("'" + cityId + "',");
					}
				}
			}
			String is_normal = request.getParameter("is_normal");
			// 是否规范
			if (null != is_normal && !"null".equals(is_normal) && !"-1".equals(is_normal)
					&& !"true".equals(is_normal))
			{
				sqlCount.append(" and t.is_normal=" + is_normal);
			}
			/*
			 * JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 0未用 1在用
			 */
			if (4 == gw_type)
			{
				if ("1".equals(use_state))
				{
					sqlCount.append(" and a.cpe_allocatedstatus=1 and a.serv_account is not null ");
				}
				else if ("0".equals(use_state))
				{
					sqlCount.append(" and a.cpe_allocatedstatus=0 and serv_account is null ");
				}
			}
			sqlCount.append(" group by a.devicetype_id ");
			PrepareSQL psqlCount = new PrepareSQL(sqlCount.toString());
			psqlCount.getSQL();
			cursor = DataSetBean.getCursor(sqlCount.toString());
			StringBuffer edtionIdBuffer = new StringBuffer();
			edtionIdBuffer.append("(");
			fields = cursor.getNext();
			List tempList = new ArrayList();
			while (fields != null)
			{
				String num = StringUtil.getStringValue(fields.get("num"));
				if (Integer.parseInt(num) > 0)
				{
					String devicetype_id = StringUtil.getStringValue(fields
							.get("devicetype_id"));
					tempList.add(devicetype_id);
				}
				fields = cursor.getNext();
			}
			for (int i = 0; i < tempList.size(); i++)
			{
				if (null == tempList || tempList.size() == 0)
				{
					return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
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
			edtionIdStr = edtionIdBuffer.toString();
			// 判断是否有业务 改
			String rela_dev_type_id = request.getParameter("rela_dev_type_id");
			String access_style_relay_id = request.getParameter("access_style_relay_id");
			Map mapService = getEditionMap(vendorId, modelId, deviceTypeId, edtionIdStr,
					rela_dev_type_id, access_style_relay_id, userCityID, gw_type,
					is_esurfing);
			if (null == mapService)
			{
				return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			}
			// 输出表头
			sbTable.append("<TR>");
			if (4 == gw_type)
			{
				sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>版本</TH>");
			}
			else
			{
				sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH>");
			}
			// 在从数据库中 统计 业务属地对应的数据
			// 属地MAP 当前用户看到本身及其下属地市
			List cityList = new ArrayList();
			
			cursor = getCity(city_id);
			
			
			fields = cursor.getNext();
			if (fields != null)
			{
				while (fields != null)
				{
					cityList.add(fields.get("city_id"));
					sbTable.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
					fields = cursor.getNext();
				}
			}
			else
			{
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
			long total = 0;
			Map mapServiceCityNum = new HashMap();
			String sql1;
			
			sql1 = "select count(a.device_id) as num,a.devicetype_id,a.city_id from tab_gw_device a,tab_devicetype_info t,tab_net_serv_param d,tab_hgwcustomer e,hgwcust_serv_info h"
					+" where e.device_id = a.device_id and e.user_id = d.user_id and e.user_id = h.user_id "
					+" and d.ip_type = 3 and a.devicetype_id=t.devicetype_id and a.device_status = 1 "
					+" and a.customer_id is not null and h.serv_type_id = 10 and h.open_status = 1 and a.gw_type=? ";
			pSQL.setSQL(sql1);
			pSQL.setInt(1, gw_type);
			
			StringBuffer sql = new StringBuffer();
			sql.append(pSQL.getSQL());
			// 是否规范
			if (null != is_normal && !"null".equals(is_normal) && !"-1".equals(is_normal)
					&& !"true".equals(is_normal))
			{
				sql.append(" and t.is_normal=" + is_normal);
			}
			
			if (null != startTime && !"".equals(startTime))
			{
				sql.append(" and a.complete_time>");
				sql.append(startTime);
			}
			if (null != endTime && !"".equals(endTime))
			{
				sql.append(" and a.complete_time<");
				sql.append(endTime);
			}
			
			if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId)
					&& vendorId != "")// 修改
			{
				sql.append(" and a.vendor_id='" + vendorId + "' ");
			}
			if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
			{
				sql.append(" and a.device_model_id='" + modelId + "' ");
			}
			if (null != deviceTypeId && !"null".equals(deviceTypeId)
					&& !"-1".equals(deviceTypeId))
			{
				sql.append(" and a.devicetype_id=" + deviceTypeId + " ");
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
			if (!StringUtil.IsEmpty(userCityID) && !"00".equals(userCityID))
			{
				sql.append(" and a.city_id in (");
				for (int i = 0; i < cityIdList.size(); i++)
				{
					String cityId = StringUtil.getStringValue(cityIdList.get(i));
					if (i + 1 == cityIdList.size())
					{
						sql.append("'" + cityId + "')");
					}
					else
					{
						sql.append("'" + cityId + "',");
					}
				}
			}
			
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
					total += Long.parseLong(tmp);
					// 按厂商累加，将小计信息放入cityTotalMap sssss
					vendortmp = Long.parseLong(tmp)
							+ Long.parseLong((String) cityTotalMap.get(fields
									.get("city_id")));
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
					size = size + ((List) valueMap.get(itDevicetype.next())).size();
				}
				mapServiceSize.put(key, size);
			}
			// 开始遍历属地,输出统计数据
			String isPrt = request.getParameter("isPrt");
			String cityIdhblt = "";
			String cityNextIdhblt = "";
			if (Global.HBLT.equals(Global.instAreaShortName))
			{
				cityIdhblt = request.getParameter("city_id");
				cityNextIdhblt = request.getParameter("citynext");
			}
			Iterator it = mapService.keySet().iterator();
			StringBuffer sbTableVendor = null;//
			StringBuffer sbTableModel = null;//
			int vendorSize = 0;//
			int modelSize = 0;//
			while (it.hasNext())
			{
				String key = (String) it.next();
				sbTableVendor = new StringBuffer();//
				// 同个厂商版本的个数 key为厂商id
				/*
				 * sbTable.append("<TR ><TD nowrap rowspan='" + mapServiceSize.get(key) +
				 * "' class=column align='right'>" + vendorMap.get(key) + "</TD>");
				 */
				Map valueMap = (Map) mapService.get(key);
				Iterator itDevicetype = valueMap.keySet().iterator();
				vendorSize = ((Integer) mapServiceSize.get(key)).intValue();//
				while (itDevicetype.hasNext())
				{
					String keyModel = (String) itDevicetype.next();
					List devicetypeList = (List) valueMap.get(keyModel);
					modelSize = devicetypeList.size();
					sbTableModel = new StringBuffer();
					// 同个厂商同个型号版本的个数
					/*
					 * sbTable.append("<TD nowrap rowspan='" + devicetypeList.size() +
					 * "' class=column align='right'>" + keyModel + "</TD>");
					 */
					for (int i = 0; i < devicetypeList.size(); i++)
					{
						String devicetypeStr = String.valueOf(devicetypeList.get(i));
						String tmpRow = "";
						if ("1".equals(isPrt))
						{
							tmpRow = getV6StatePrintHblt(devicetypeStr, "1",
									cityList, mapServiceCityNum, gw_type, userCityID,
									cityIdhblt, cityNextIdhblt);
						}
						else
						{
							tmpRow = getV6StatRowHtmlHblt(devicetypeStr, "1",
									cityList, mapServiceCityNum, gw_type, userCityID,
									isBind, cityIdhblt, cityNextIdhblt);
						}
						if ((!"".equals(tmpRow)) && (tmpRow != null))
						{
							sbTableModel.append(tmpRow);
							tmpRow = "";
						}
						else
						{
							modelSize--;
							vendorSize--;
						}
					}
					if ((sbTableModel.length() > 0) && (!"".equals(sbTableModel))
							&& (sbTableModel != null))
					{
						sbTableVendor.append(
								"<TD nowrap rowspan='" + modelSize
										+ "' class=column align='right'>" + keyModel
										+ "</TD>").append(sbTableModel);
					}
					sbTableModel = null;
				}
				if ((sbTableVendor.length() > 0) && (!"".equals(sbTableVendor))
						&& (sbTableVendor != null))
				{
					sbTable.append(
							"<TR ><TD nowrap rowspan='" + vendorSize
									+ "' class=column align='right'>"
									+ this.vendorMap.get(key) + "</TD>").append(
							sbTableVendor);
				}
				sbTableVendor = null;
			}
			String num = "";
			
			String citynext = request.getParameter("citynext");
			String status = "0";
			if (null == citynext || "null".equals(citynext))
			{
				citynext = "-1";
			}
			num = getSubTotalHB(cityList, isPrt, total, gw_type, userCityID,
					cityIdhblt, cityNextIdhblt, status).toString();
			
			
			
				
			sbTable.append("<tr><td nowrap align='right' class=column colspan=3>小计</td>");
				
				
			
			sbTable.append(num);
			// Clear Resouce
			mapServiceCityNum.clear();
			mapServiceCityNum = null;
			cityMap = null;
		}
		cursor = null;
		fields = null;
		return sbTable.toString();
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
			int gw_type, String userCityID)
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
					sbTable.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=detail('"
							+ city + "','-1'," + gw_type + ") >" + num_str + "</a></TD>");
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
			if ("0".equals(totalNum))
			{
				sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum
						+ "</td></tr>");
			}
			else
			{
				if ("1".equals(isPrt))
				{
					sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum
							+ "</td></tr>");
				}
				else
				{
					sbTable.append("<td bgcolor=#ffffff align=center><a href=javascript:onclick=detail('-1','-1',"
							+ gw_type + ")>" + totalNum + "</a></td></tr>");
				}
			}
		}
		return sbTable;
	}

	/**
	 * 输出小计信息hb_lt
	 * 
	 * @param cityList
	 * @param isPrt
	 * @param total
	 * @return
	 */
	private StringBuffer getSubTotalHB(List cityList, String isPrt, long total,
			int gw_type, String userCityID, String cityIdhblt, String cityNextIdhblt,String status)
	{
		StringBuffer sbTable = new StringBuffer();
		long totalNum = 0;
		Iterator it = cityList.iterator();
		while (it.hasNext())
		{
			String city = (String) it.next();
			long num = 0;
			if (("-1".equals(cityIdhblt) || "00".equals(cityIdhblt))
					&& ("-1".equals(cityNextIdhblt) || "00".equals(cityNextIdhblt)))
			{
				if ("00".equals(city))
				{
					num = Long.parseLong((String) cityTotalMap.get(city));
				}
				else
				{
					List childList = CityDAO.getAllNextCityIdsByCityPid(city);
					for (int i = 0; i < childList.size(); i++)
					{
						num += Long
								.parseLong((String) cityTotalMap.get(childList.get(i)));
					}
					childList = null;
				}
			}
			else
			{
				num = Long.parseLong((String) cityTotalMap.get(city));
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
					sbTable.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=detail('"
							+ city + "','-1'," + gw_type + ") >" + num_str + "</a></TD>");
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
			if ("0".equals(totalNum))
			{
				sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum
						+ "</td></tr>");
			}
			else
			{
				if ("1".equals(isPrt))
				{
					sbTable.append("<td bgcolor=#ffffff align=center>" + totalNum
							+ "</td></tr>");
				}
				else
				{
					sbTable.append("<td bgcolor=#ffffff align=center><a href=javascript:onclick=detail('-1','-1',"
							+ gw_type + ")>" + totalNum + "</a></td></tr>");
				}
			}
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
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, vendor_id, "");
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
				if (tmp.length > 4)
				{
					// 硬件版本
					sb.append("<TD nowrap class=column align='right'>" + tmp[4] + "</TD>");
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
				else
				{
					// hb_lt软件硬件版本合为版本
					if (!Global.HBLT.equals(Global.instAreaShortName))
					{
						// 硬件版本
						sb.append("<TD nowrap class=column align='right'></TD>");
					}
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
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
		if (total == 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * hb_lt输出地市以及地市与业务对应的用户统计数量(三级地市统计方法)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放业务id的列表
	 * @param mapServiceCityNum
	 *            业务id+属地id 映射 统计值
	 * @return
	 */
	private String getEditionStatePrintHblt(String vendor_id, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String cityIdhblt,
			String cityNextIdhblt)
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
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, vendor_id, "");
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
				if (tmp.length > 4)
				{
					// 硬件版本
					sb.append("<TD nowrap class=column align='right'>" + tmp[4] + "</TD>");
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
				else
				{
					// hb_lt软件硬件版本合为版本
					if (!Global.HBLT.equals(Global.instAreaShortName))
					{
						// 硬件版本
						sb.append("<TD nowrap class=column align='right'></TD>");
					}
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
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
				if (("-1".equals(cityIdhblt) || "00".equals(cityIdhblt))
						&& ("-1".equals(cityNextIdhblt) || "00".equals(cityNextIdhblt)))
				{
					if ("00".equals(city_id))
					{
						num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
					}
					else
					{
						num = getCityVendorCount(cityAll, vendor_id, mapServiceCityNum);
					}
				}
				else
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
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
		if (total == 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * hb_lt输出地市以及地市与业务对应的用户统计数量(三级地市统计方法)
	 * 
	 * @param city_id
	 * @param cityName
	 * @param listServiceId
	 *            存放业务id的列表
	 * @param mapServiceCityNum
	 *            业务id+属地id 映射 统计值
	 * @return
	 */
	private String getV6StatePrintHblt(String vendor_id, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String cityIdhblt,
			String cityNextIdhblt)
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
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, vendor_id, "");
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
				if (("-1".equals(cityIdhblt) || "00".equals(cityIdhblt))
						&& ("-1".equals(cityNextIdhblt) || "00".equals(cityNextIdhblt)))
				{
					if ("00".equals(city_id))
					{
						num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
					}
					else
					{
						num = getCityVendorCount(cityAll, vendor_id, mapServiceCityNum);
					}
				}
				else
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + vendor_id);
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
		if (total == 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}

	
	/**
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param vendor_id
	 *            版本id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private String getVendorStatRowHtml(String vendor_id, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String isBind)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','" + gw_type + "','" + isBind + "')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, vendor_id, "");
			logger.debug("getVendorStatRowHtml=>type(1)=>message:" + message);
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
				if (tmp.length > 4)
				{
					// 硬件版本
					sb.append("<TD nowrap class=column align='right'>" + tmp[4] + "</TD>");
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
				else
				{
					// hb_lt软件硬件版本合为版本
					if (!Global.HBLT.equals(Global.instAreaShortName))
					{
						// 硬件版本
						sb.append("<TD nowrap class=column align='right'></TD>");
					}
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
			}
			else
			{
				sb.append("<TR ><TD nowrap class=column align='right'></TD>");
			}
		}
		else
		{
			logger.debug("getVendorStatRowHtml=>type(!1)=>message:" + message);
			message = (String) vendorMap.get(vendor_id);
			sb.append("<TR ><TD nowrap class=column align='right'>" + message + "</TD>");
		}
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				// num = (String) mapServiceCityNum.get(city_id + "_" +
				// editionId);
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
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#",
									vendor_id) + ">" + num + "</a></TD>");
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
			// return "";
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
					+ jsStr.replaceAll("\\#", vendor_id).replaceAll("\\?", "-1") + ">"
					+ total + "</TD>");
		}
		// sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=customize('"+vendor_id+"')>定制</a></TD>");
		if (total <= 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * hb_lt输出地市以及地市与业务对应的用户统计数量(三级地市统计方法)
	 * 
	 * @param vendor_id
	 *            版本id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private String getVendorStatRowHtmlHblt(String devicetypeId, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String isBind,
			String cityIdhblt, String cityNextIdhblt)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','" + gw_type + "','" + isBind + "')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, devicetypeId, "");
			logger.debug("getVendorStatRowHtml=>type(1)=>message:" + message);
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
				if (tmp.length > 4)
				{
					// 硬件版本
					sb.append("<TD nowrap class=column align='right'>" + tmp[4] + "</TD>");
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
				else
				{
					// hb_lt软件硬件版本合为版本
					if (!Global.HBLT.equals(Global.instAreaShortName))
					{
						// 硬件版本
						sb.append("<TD nowrap class=column align='right'></TD>");
					}
					// 是否规范
					if (4 != gw_type)
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
			}
			else
			{
				sb.append("<TR ><TD nowrap class=column align='right'></TD>");
			}
		}
		else
		{
			logger.debug("getVendorStatRowHtml=>type(!1)=>message:" + message);
			message = (String) vendorMap.get(devicetypeId);
			sb.append("<TR ><TD nowrap class=column align='right'>" + message + "</TD>");
		}
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				if (("-1".equals(cityIdhblt) || "00".equals(cityIdhblt))
						&& ("-1".equals(cityNextIdhblt) || "00".equals(cityNextIdhblt)))
				{
					if ("00".equals(city_id))
					{
						num = (String) mapServiceCityNum.get(city_id + "_" + devicetypeId);
					}
					else
					{
						num = getCityVendorCount(cityAll, devicetypeId, mapServiceCityNum);
					}
				}
				else
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + devicetypeId);
				}
				if (num != null && !"0".equals(num))
				{
					if (!"".equals(num))
					{
						total += Long.parseLong(num);
					}
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#",
									devicetypeId) + ">" + num + "</a></TD>");
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
			// return "";
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
					+ jsStr.replaceAll("\\#", devicetypeId).replaceAll("\\?", "-1") + ">"
					+ total + "</TD>");
		}
		if (total <= 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * hb_lt输出地市以及地市与业务对应的用户统计数量(三级地市统计方法)
	 * 
	 * @param vendor_id
	 *            版本id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	private String getV6StatRowHtmlHblt(String devicetypeId, String type, List cityList,
			Map mapServiceCityNum, int gw_type, String userCityID, String isBind,
			String cityIdhblt, String cityNextIdhblt)
	{
		Iterator it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?','#','" + gw_type + "','" + isBind + "')";
		long total = 0;
		String city_id = "";
		String message = "";
		// 输出业务名称
		if ("1".equals(type))
		{
			// message = (String) versionMap.get(vendor_id);
			message = StringUtil.getStringValue(versionMap, devicetypeId, "");
			logger.debug("getVendorStatRowHtml=>type(1)=>message:" + message);
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
			message = (String) vendorMap.get(devicetypeId);
			sb.append("<TR ><TD nowrap class=column align='right'>" + message + "</TD>");
		}
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				if (("-1".equals(cityIdhblt) || "00".equals(cityIdhblt))
						&& ("-1".equals(cityNextIdhblt) || "00".equals(cityNextIdhblt)))
				{
					if ("00".equals(city_id))
					{
						num = (String) mapServiceCityNum.get(city_id + "_" + devicetypeId);
					}
					else
					{
						num = getCityVendorCount(cityAll, devicetypeId, mapServiceCityNum);
					}
				}
				else
				{
					num = (String) mapServiceCityNum.get(city_id + "_" + devicetypeId);
				}
				if (num != null && !"0".equals(num))
				{
					if (!"".equals(num))
					{
						total += Long.parseLong(num);
					}
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#",
									devicetypeId) + ">" + num + "</a></TD>");
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
			// return "";
		}
		else
		{
			sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
					+ jsStr.replaceAll("\\#", devicetypeId).replaceAll("\\?", "-1") + ">"
					+ total + "</TD>");
		}
		if (total <= 0)
		{
			return "";
		}
		sb.append("</TR>");
		return sb.toString();
	}
	
	/**
	 * 版本信息列表
	 * 
	 * @return
	 */
	public List[] getEditionIdNameList()
	{
		cursor = DataSetBean.getCursor(m_EditionInfo);
		fields = cursor.getNext();
		if (fields == null)
			return null;
		List[] listService = new ArrayList[3];
		listService[0] = new ArrayList();// devicetype_id
		listService[1] = new ArrayList();// device_model
		while (fields != null)
		{
			listService[0].add(fields.get("devicetype_id"));
			listService[1].add(fields.get("device_model"));
			fields = cursor.getNext();
		}
		return listService;
	}

	/**
	 * 版本信息列表Map<vendor_id,Map<device_model,List<devicetype_id>>>
	 * 
	 * @return
	 */
	public Map getEditionIdNameMap(String vendorId, String modelId, String deviceTypeId,
			String edtionIdStr, String rela_dev_type_id, String access_style_relay_id)
	{
		StringBuffer bf = new StringBuffer();
		bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
				+ "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
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
		bf.append(" and a.devicetype_id  in " + edtionIdStr
				+ " order by  a.vendor_id,device_model,devicetype_id ");
		logger.debug("bf.toString:{}", bf.toString());
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
	 * 版本信息列表Map<vendor_id,Map<device_model,List<devicetype_id>>>
	 * 
	 * @return
	 */
	public Map getEditionMap(String vendorId, String modelId, String deviceTypeId,
			String edtionIdStr, String rela_dev_type_id, String access_style_relay_id,
			String userCityID, int gw_type, String is_esurfing)
	{
		StringBuffer bf = new StringBuffer();
		if (4 == gw_type)
		{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "stb_tab_devicetype_info a,stb_gw_device_model b where a.device_model_id=b.device_model_id ");
		}
		else if (Global.JXDX.equals(Global.instAreaShortName)
				&& null != is_esurfing && "" != is_esurfing && is_esurfing.equals("1"))
		{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "tab_devicetype_info a,gw_device_model b,tab_device_version_attribute c where a.device_model_id=b.device_model_id and a.devicetype_id=c.devicetype_id");
		}
		else
		{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
		}
		// 天翼网关修改的地方
		if (Global.JXDX.equals(Global.instAreaShortName)
				&& is_esurfing.equals("1"))
		{
			bf.append(" and c.is_tyGate=" + is_esurfing);
		}
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
		// if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
		// {
		// bf.append(" and a.devicetype_id  in " + edtionIdStr
		// + " order by  a.vendor_id,device_model,devicetype_id ");
		// }
		bf.append(" order by  a.vendor_id,device_model,devicetype_id ");
		logger.debug("bf.toString:{}", bf.toString());
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
	 * 型号信息列表Map<city_id,Map<vendor_name,List<device_model_id>>>
	 * 
	 * @return
	 */
	public Map getModelIdNameMap(String vendorId, String modelId, Map cityMap,
			long starttime, long endtime, int gw_type)
	{
		logger.warn("getModelIdNameMap({},{},cityMap{},{})", new Object[] { vendorId,
				modelId, starttime, endtime });
		Map<String, Map> mapService = new HashMap<String, Map>();
		if (cityMap != null)
		{
			Set<String> key = cityMap.keySet();
			for (Iterator it = key.iterator(); it.hasNext();)
			{
				String cityId = (String) it.next();
				StringBuffer bf = new StringBuffer();
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
				// bf.append("select count(a.device_model_id) as num,a.city_id,a.vendor_id,a.device_model_id from (select * from tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id) a where gw_type = "
				// + gw_type);
				bf.append("select count(b.device_model_id) as num,b.city_id,b.vendor_id,b.device_model_id from tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id and b.gw_type = 1 ");
				if (null != vendorId && !"null".equals(vendorId)
						&& !"-1".equals(vendorId))
				{
					bf.append(" and b.vendor_id='" + vendorId + "' ");
				}
				if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
				{
					bf.append(" and b.device_model_id='" + modelId + "' ");
				}
				if (null != cityId && !"null".equals(cityId) && !"-1".equals(cityId))
				{
					bf.append(" and b.city_id in ('" + cityStr + "')");
				}
				bf.append(" and b.complete_time < " + endtime);
				bf.append(" and b.complete_time > " + starttime);
				bf.append(" group by b.city_id,b.vendor_id,b.device_model_id");
				bf.append(" order by b.city_id,b.vendor_id,b.device_model_id ");
				logger.warn("bf.toString:{}", bf.toString());
				PrepareSQL psql = new PrepareSQL(bf.toString());
				psql.getSQL();
				cursor = DataSetBean.getCursor(bf.toString());
				fields = cursor.getNext();
				if (fields == null)
					continue;// return null;
				while (fields != null)
				{
					String vendor_id = String.valueOf(fields.get("vendor_id"));
					String device_model_id = String
							.valueOf(fields.get("device_model_id"));
					long num = Long.parseLong(String.valueOf(fields.get("num")));
					Map<String, Map> vendoridMap = null;
					if (null == mapService.get(cityId))
					{
						vendoridMap = new HashMap<String, Map>();
					}
					else
					{
						vendoridMap = mapService.get(cityId);
					}
					Map<String, Long> devicemodelMap = null;
					if (null == vendoridMap.get(vendor_id))
					{
						devicemodelMap = new HashMap<String, Long>();
					}
					else
					{
						devicemodelMap = vendoridMap.get(vendor_id);
					}
					long total = num;
					long tmp = 0;
					if (null == devicemodelMap.get(device_model_id))
					{
						total = num;
						devicemodelMap.put(device_model_id, total);
					}
					else
					{
						tmp = devicemodelMap.get(device_model_id);
						total += tmp;
						devicemodelMap.put(device_model_id, total);
					}
					devicemodelMap.put(device_model_id, total);
					vendoridMap.put(vendor_id, devicemodelMap);
					mapService.put(cityId, vendoridMap);
					fields = cursor.getNext();
				}
			}
		}
		return mapService;
	}

	/**
	 * 型号(明细)信息列表Map<city_id,Map<vendor_name,List<device_model_id>>>
	 * 
	 * @return
	 */
	public List getModelDetailIdNameMap(String vendorId, String modelId, Map cityMap,
			long starttime, long endtime, int gw_type, int offset, int pagelen,
			String fileter, String isPrt)
	{
		logger.warn("getModelDetailIdNameMap({},{},cityMap,{},{},{},{},{})",
				new Object[] { vendorId, modelId, starttime, endtime, gw_type, offset,
						pagelen, isPrt });
		List list = new ArrayList();
		String strBar = "";
		Map<String, List<Map>> mapService = new HashMap<String, List<Map>>();
		List<Map> dataList = new ArrayList();
		if (cityMap != null)
		{
			Set<String> key = cityMap.keySet();
			List<String> cityList = new ArrayList<String>();
			String cityAll = "";
			for (Iterator it = key.iterator(); it.hasNext();)
			{
				String cityId = (String) it.next();
				ArrayList cityArray = CityDAO.getNextCityIdsByCityPid(cityId);// CityDAO.getAllNextCityIdsByCityPid(cityId);
				String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
				cityAll += cityStr + "','";
				cityList.add(cityStr);
			}
			Map pidMap = CityDAO.getCityIdPidMap();
			cityAll = cityAll.substring(0, cityAll.lastIndexOf("','"));
			StringBuffer bf = new StringBuffer();
			bf.append("select b.city_id,b.device_id,c.username,b.city_id,b.vendor_id,b.device_model_id,b.complete_time,c.user_id from "
					+ "tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id and b.gw_type = "
					+ gw_type);
			if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
			{
				bf.append(" and b.vendor_id='" + vendorId + "' ");
			}
			if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
			{
				bf.append(" and b.device_model_id='" + modelId + "' ");
			}
			bf.append(" and b.city_id in ('" + cityAll + "')");
			bf.append(" and b.complete_time < " + endtime);
			bf.append(" and b.complete_time > " + starttime);
			bf.append(" order by b.vendor_id,b.device_model_id,b.city_id ");
			logger.warn("bf.toString:{}", bf.toString());
			PrepareSQL psql = new PrepareSQL(bf.toString());
			psql.getSQL();
			if ("1".equals(isPrt))
			{
				cursor = DataSetBean.getCursor(bf.toString());
			}
			else
			{
				QueryPage qryp = new QueryPage();
				qryp.initPage(bf.toString(), offset, pagelen);
				strBar = qryp.getPageBar(fileter);
				cursor = DataSetBean.getCursor(bf.toString(), offset, pagelen);
			}
			fields = cursor.getNext();
			while (fields != null)
			{
				String city_id = String.valueOf(fields.get("city_id"));
				String vendor_id = String.valueOf(fields.get("vendor_id"));
				String device_model_id = String.valueOf(fields.get("device_model_id"));
				String loid = String.valueOf(fields.get("username"));
				long time = Long.parseLong(String.valueOf(fields.get("complete_time")));
				String user_id = String.valueOf(fields.get("user_id"));
				String city = (String) pidMap.get(city_id);
				if ("00".equals(city_id) || "00".equals(city) && !"00".equals(city_id))
				{
					city = city_id;
				}
				if (null == mapService.get(city))
				{
					dataList = new ArrayList<Map>();
				}
				else
				{
					dataList = mapService.get(city);
				}
				Map tmp = new HashMap();
				tmp.put("vendor_id", vendor_id);
				tmp.put("device_model_id", device_model_id);
				tmp.put("loid", loid);
				tmp.put("time", time);
				tmp.put("user_id", user_id);
				tmp.put("city_id", city);
				dataList.add(tmp);
				mapService.put(city, dataList);
				fields = cursor.getNext();
			}
		}
		list.add(strBar);
		list.add(mapService);
		return list;
	}

	/**
	 * 型号(明细)信息列表Map<city_id,Map<vendor_name,List<device_model_id>>>
	 * 
	 * @return
	 */
	public List getModelDetailIdNameMapBak(String vendorId, String modelId, Map cityMap,
			long starttime, long endtime, int gw_type, int offset, int pagelen,
			String fileter)
	{
		logger.warn("getModelDetailIdNameMap({},{},cityMap{},{})", new Object[] {
				vendorId, modelId, starttime, endtime });
		List list = new ArrayList();
		String strBar = "";
		Map<String, List<Map>> mapService = new HashMap<String, List<Map>>();
		if (cityMap != null)
		{
			Set<String> key = cityMap.keySet();
			List<String> cityList = new ArrayList<String>();
			String cityAll = "";
			// for (Iterator it = key.iterator(); it.hasNext();) {
			// String cityId = (String) it.next();
			// logger.warn("cityID==" + cityId);
			// ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			// cityAll += cityStr + "','";
			// cityList.add(cityStr);
			// }
			for (Iterator it = key.iterator(); it.hasNext();)
			{
				String cityId = (String) it.next();
				ArrayList cityArray = CityDAO.getNextCityIdsByCityPid(cityId);// CityDAO.getAllNextCityIdsByCityPid(cityId);
				String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
				cityAll += cityId + "','";
				cityList.add(cityStr);
			}
			cityAll = cityAll.substring(0, cityAll.lastIndexOf("','"));
			StringBuffer bf = new StringBuffer();
			bf.append("select a.city_id,a.device_id,a.username,a.city_id,a.vendor_id,a.device_model_id,a.complete_time,a.user_id from "
					+ "(select * from tab_gw_device b,tab_hgwcustomer c where b.device_id = c.device_id and b.gw_type = 1) a where a.gw_type = "
					+ gw_type);
			if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
			{
				bf.append(" and a.vendor_id='" + vendorId + "' ");
			}
			if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
			{
				bf.append(" and a.device_model_id='" + modelId + "' ");
			}
			bf.append(" and a.city_id in ('" + cityAll + "')");
			// bf.append(" and a.complete_time < " + endtime);
			// bf.append(" and a.complete_time > " + starttime);
			bf.append(" order by vendor_id,device_model_id ");
			QueryPage qryp = new QueryPage();
			qryp.initPage(bf.toString(), offset, pagelen);
			strBar = qryp.getPageBar(fileter);
			logger.warn("bf.toString:{}", bf.toString());
			PrepareSQL psql = new PrepareSQL(bf.toString());
			psql.getSQL();
			cursor = DataSetBean.getCursor(bf.toString(), offset, pagelen);
			fields = cursor.getNext();
			while (fields != null)
			{
				String city_id = String.valueOf(fields.get("city_id"));
				String vendor_id = String.valueOf(fields.get("vendor_id"));
				String device_model_id = String.valueOf(fields.get("device_model_id"));
				String loid = String.valueOf(fields.get("username"));
				long time = Long.parseLong(String.valueOf(fields.get("complete_time")));
				String user_id = String.valueOf(fields.get("user_id"));
				List<Map> dataList = null;
				String cityId = "";
				for (String city : cityList)
				{
					if (city.indexOf(city_id) > -1)
					{
						cityId = city.split("','")[0];
					}
				}
				if (null == mapService.get(cityId))
				{
					dataList = new ArrayList<Map>();
				}
				else
				{
					dataList = mapService.get(cityId);
				}
				Map tmp = new HashMap();
				tmp.put("vendor_id", vendor_id);
				tmp.put("device_model_id", device_model_id);
				tmp.put("loid", loid);
				tmp.put("time", time);
				tmp.put("user_id", user_id);
				dataList.add(tmp);
				mapService.put(cityId, dataList);
				fields = cursor.getNext();
			}
		}
		list.add(strBar);
		list.add(mapService);
		return list;
	}

	/**
	 * 厂商信息列表
	 * 
	 * @return
	 */
	public List[] getVendorIdNameList(String gwType)
	{
		PrepareSQL psql = new PrepareSQL(m_VendorInfo);
		if ("4".equals(gwType))
		{
			psql = new PrepareSQL(m_VendorInfo_stb);
		}
		psql.getSQL();
		if ("4".equals(gwType))
		{
			cursor = DataSetBean.getCursor(
					m_VendorInfo_stb,
					DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
							this.getClass().getName()));
		}
		else
		{
			cursor = DataSetBean.getCursor(
					m_VendorInfo,
					DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
							this.getClass().getName()));
		}
		fields = cursor.getNext();
		if (fields == null)
			return null;
		List[] listService = new ArrayList[2];
		listService[0] = new ArrayList();// vendor_id
		listService[1] = new ArrayList();// vendor_name
		while (fields != null)
		{
			listService[0].add(fields.get("vendor_id"));
			listService[1].add(fields.get("vendor_name"));
			fields = cursor.getNext();
		}
		return listService;
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
		if (Global.CQDX.equals(Global.instAreaShortName))
		{
			List list = new ArrayList();
			String[] city = city_id.split(",");
			for (int i = 0; i < city.length; i++)
			{
				list.add(city[i]);
			}
			m_cityList = "select city_id,city_name from tab_city where parent_id in("
					+ StringUtils.weave(list) + ") or city_id in("
					+ StringUtils.weave(list) + ") order by city_id";
		}
		else
		{
			m_cityList = "select city_id,city_name from tab_city where parent_id='"
					+ city_id + "' or city_id='" + city_id + "' order by city_id";
		}
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
		/*
		 * HttpSession session = request.getSession(); UserRes curUser = (UserRes)
		 * session.getAttribute("curUser"); String city_id = curUser.getCityId();
		 */
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

	private void getNextCity(List cityList)
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
	private void getVersionMap(int gw_type)
	{
		// String sql =
		// "select * from tab_devicetype_info order by manufacturer";
		String sql;
		if (4 == gw_type)
		{
			sql = "select a.softwareversion,a.devicetype_id,b.device_model,c.vendor_id,a.hardwareversion "
					+ " from stb_tab_devicetype_info a, stb_gw_device_model b,stb_tab_vendor c "
					+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		}
		else
		{
			sql = "select a.softwareversion,a.devicetype_id,b.device_model,c.vendor_id,a.hardwareversion,a.is_normal "
					+ " from tab_devicetype_info a, gw_device_model b,tab_vendor c "
					+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		}
		if (versionMap == null)
		{
			versionMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				String normalResult = "1".equals(fields.get("is_normal")) ? "是" : "否";
				String version = "";
				if (Global.HBLT.equals(Global.instAreaShortName)
						&& 4 == gw_type)
				{
					version = (String) fields.get("vendor_id") + "|"
							+ (String) fields.get("device_model") + "|"
							+ (String) fields.get("hardwareversion") + "("
							+ (String) fields.get("softwareversion") + ")";
				}
				else
				{
					version = (String) fields.get("vendor_id") + "|"
							+ (String) fields.get("device_model") + "|"
							+ (String) fields.get("softwareversion") + "|" + normalResult
							+ "|" + (String) fields.get("hardwareversion");
				}
				versionMap.put(fields.get("devicetype_id"), version);
				fields = cursor.getNext();
			}
		}
	}

	/**
	 * 将厂商信息放入MAP中
	 */
	private void getVendorMap(int gw_type)
	{
		String sql;
		if (4 == gw_type)
		{
			sql = "select * from stb_tab_vendor order by vendor_add";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select vendor_add, vendor_id, vendor_name from stb_tab_vendor order by vendor_add";
			}
		}
		else
		{
			sql = "select * from tab_vendor order by vendor_add";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select vendor_add, vendor_id, vendor_name from tab_vendor order by vendor_add";
			}
		}
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
	 * 根据user_id获取宽带语音账号
	 */
	private String[] getAccountFromUserId(String user_id)
	{
		String[] tmp = new String[3];
		String netAccount = "";
		String voip1Account = "";
		String voip2Account = "";
		// String sql =
		// "select serv_type_id,username from hgwcust_serv_info where user_id = " +
		// user_id
		// + " and serv_type_id in (10,14)";
		String sql = "select a.serv_type_id,a.username,b.line_id,b.voip_phone from hgwcust_serv_info a left join tab_voip_serv_param b on a.user_id = b.user_id where a.user_id = "
				+ user_id + " and serv_type_id in (10,14)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		int i = 0;
		while (fields != null)
		{
			String serv_type_id = (String) fields.get("serv_type_id");
			String username = (String) fields.get("username");
			String line_id = (String) fields.get("line_id");
			String voip_phone = (String) fields.get("voip_phone");
			if ("10".equals(serv_type_id))
			{
				netAccount = username;
			}
			if ("14".equals(serv_type_id))
			{
				if ("1".equals(line_id))
				{
					voip1Account = voip_phone;
				}
				else if ("2".equals(line_id))
				{
					voip2Account = voip_phone;
				}
			}
			fields = cursor.getNext();
		}
		tmp[0] = netAccount;
		tmp[1] = voip1Account;
		tmp[2] = voip2Account;
		return tmp;
	}

	/**
	 * 将型号信息放入MAP中
	 */
	private void getModelMap()
	{
		String sql = "select * from gw_device_model order by device_model_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_model_id, device_model from gw_device_model order by device_model_id";
		}
		if (modelMap == null)
		{
			modelMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				String vendor_add = (String) fields.get("device_model_id");
				modelMap.put(fields.get("device_model_id"), fields.get("device_model"));
				fields = cursor.getNext();
			}
		}
	}

	public String getCityHtml(HttpServletRequest request, String cityid)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_cityList);
		fields = cursor.getNext();
		StringBuffer bf = new StringBuffer();
		if (null != fields)
		{
			bf.append("<option value='-1'>==请选择==</option>");
			while (null != fields)
			{
				String cityId = String.valueOf(fields.get("city_id"));
				String cityName = String.valueOf(fields.get("city_name"));
				if (null != cityId && cityId.equals(cityid))
				{
					bf.append("<option value='" + cityId + "' selected>==" + cityName
							+ "==</option>");
				}
				else
				{
					bf.append("<option value='" + cityId + "'>==" + cityName
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

	public String getVendorHtml(String vendorId, String gw_type)
	{
		logger.debug("getVendorHtml({})", vendorId);
		StringBuffer bf = new StringBuffer();
		if ("4".equals(gw_type))
		{
			PrepareSQL psql = new PrepareSQL(
					"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
			psql.getSQL();
			cursor = DataSetBean
					.getCursor("select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		}
		else
		{
			PrepareSQL psql = new PrepareSQL(
					"select vendor_id,vendor_name, vendor_add from tab_vendor");
			psql.getSQL();
			cursor = DataSetBean
					.getCursor("select vendor_id,vendor_name, vendor_add from tab_vendor");
		}
		fields = cursor.getNext();
		if (null != fields)
		{
			// bf.append("<select name=\"vendorId\" class=\"bk\" onchange=\"change_select('deviceModel')\">");
			bf.append("<option value='-1'>==请选择==</option>");
			while (null != fields)
			{
				String vendorId_ = String.valueOf(fields.get("vendor_id"));
				String vendorname_ = fields.get("vendor_add") + "("
						+ fields.get("vendor_name") + ")";
				if (null != vendorId && vendorId.equals(vendorId_))
				{
					bf.append("<option value='" + vendorId_ + "' selected>=="
							+ vendorname_ + "==</option>");
				}
				else
				{
					bf.append("<option value='" + vendorId_ + "'>==" + vendorname_
							+ "==</option>");
				}
				fields = cursor.getNext();
			}
			bf.append("</select>");
		}
		else
		{
			// bf.append("<select name=\"vendorId\" class=\"bk\" onchange=\"change_select('deviceModel')\">");
			bf.append("<option value='-1'>==请选择==</option>");
			// bf.append("</select>");
		}
		return bf.toString();
	}

	public String getModelHtml(String vendorId, String modelId, String gw_type)
	{
		logger.debug("getModelHtml({})", vendorId);
		StringBuffer bf = new StringBuffer();
		if (null == vendorId || "-1".equals(vendorId))
		{
			// bf.append("<select name=\"modelId\" class=\"bk\" onchange=\"change_select('deviceType')\">");
			bf.append("<option value='-1'>==请先选择厂商==</option>");
			// bf.append("</select>");
			return bf.toString();
		}
		else
		{
			if ("4".equals(gw_type))
			{
				PrepareSQL psql = new PrepareSQL(
						" select a.device_model_id,a.device_model "
								+ " from stb_gw_device_model a where a.vendor_id='"
								+ vendorId + "'");
				psql.getSQL();
				cursor = DataSetBean
						.getCursor(" select a.device_model_id,a.device_model "
								+ " from stb_gw_device_model a where a.vendor_id='"
								+ vendorId + "'");
			}
			else
			{
				PrepareSQL psql = new PrepareSQL(
						" select a.device_model_id,a.device_model "
								+ " from gw_device_model a where a.vendor_id='"
								+ vendorId + "'");
				psql.getSQL();
				cursor = DataSetBean
						.getCursor(" select a.device_model_id,a.device_model "
								+ " from gw_device_model a where a.vendor_id='"
								+ vendorId + "'");
			}
			fields = cursor.getNext();
			if (null != fields)
			{
				// bf.append("<select name=\"modelId\" class=\"bk\" onchange=\"change_select('deviceType')\">");
				bf.append("<option value='-1'>==请选择==</option>");
				while (null != fields)
				{
					String modelId_ = String.valueOf(fields.get("device_model_id"));
					String model_ = String.valueOf(fields.get("device_model"));
					if (null != modelId && modelId.equals(modelId_))
					{
						bf.append("<option value='" + modelId_ + "' selected>==" + model_
								+ "==</option>");
					}
					else
					{
						bf.append("<option value='" + modelId_ + "'>==" + model_
								+ "==</option>");
					}
					fields = cursor.getNext();
				}
				// bf.append("</select>");
			}
			else
			{
				// bf.append("<select name=\"modelId\" class=\"bk\" onchange=\"change_select('deviceType')\">");
				bf.append("<option value='-1'>==请选择==</option>");
				// bf.append("</select>");
			}
		}
		return bf.toString();
	}

	public String getDeviceTypeHtml(String modelId, String deviceTypeId, String gw_type)
	{
		logger.debug("getDeviceTypeHtml({},{})", modelId, deviceTypeId);
		StringBuffer bf = new StringBuffer();
		if (null == modelId || "-1".equals(modelId))
		{
			// bf.append("<select name=\"deviceTypeId\" class=\"bk\">");
			bf.append("<option value='-1'>==请先选择型号==</option>");
			// bf.append("</select>");
			return bf.toString();
		}
		else
		{
			if ("4".equals(gw_type))
			{
				PrepareSQL psql = new PrepareSQL(
						" select a.devicetype_id,a.softwareversion"
								+ " from stb_tab_devicetype_info a where a.device_model_id='"
								+ modelId + "'");
				psql.getSQL();
				cursor = DataSetBean
						.getCursor(" select a.devicetype_id,a.softwareversion"
								+ " from stb_tab_devicetype_info a where a.device_model_id='"
								+ modelId + "'");
			}
			else
			{
				PrepareSQL psql = new PrepareSQL(
						" select a.devicetype_id,a.softwareversion"
								+ " from tab_devicetype_info a where a.device_model_id='"
								+ modelId + "'");
				psql.getSQL();
				cursor = DataSetBean
						.getCursor(" select a.devicetype_id,a.softwareversion"
								+ " from tab_devicetype_info a where a.device_model_id='"
								+ modelId + "'");
			}
			fields = cursor.getNext();
			if (null != fields)
			{
				// bf.append("<select name=\"deviceTypeId\" class=\"bk\">");
				bf.append("<option value='-1'>==请选择==</option>");
				while (null != fields)
				{
					String deviceTypeId_ = String.valueOf(fields.get("devicetype_id"));
					String softwareversion_ = String.valueOf(fields
							.get("softwareversion"));
					if (null != deviceTypeId && deviceTypeId.equals(deviceTypeId_))
					{
						bf.append("<option value='" + deviceTypeId_ + "' selected>=="
								+ softwareversion_ + "==</option>");
					}
					else
					{
						bf.append("<option value='" + deviceTypeId_ + "'>=="
								+ softwareversion_ + "==</option>");
					}
					fields = cursor.getNext();
				}
				// bf.append("</select>");
			}
			else
			{
				// bf.append("<select name=\"deviceTypeId\" class=\"bk\">");
				bf.append("<option value='-1'>==请选择==</option>");
				// bf.append("</select>");
			}
		}
		return bf.toString();
	}

	public long getTimeForMonth(String flag, String time)
	{
		long t = System.currentTimeMillis() / 1000;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		try
		{
			date = new SimpleDateFormat("yyyy-MM").parse(time);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		if (flag.equals("start"))
		{
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t = cal.getTimeInMillis() / 1000;
		}
		else if (flag.equals("end"))
		{
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t = cal.getTimeInMillis() / 1000 - 1;
		}
		return t;
	}

	public String getDateString(String time)
	{
		long tmp = System.currentTimeMillis();
		if (time != null)
		{
			tmp = Long.parseLong(time) * 1000;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tmp);
		c.getTime();
		return df.format(c.getTime());
	}

	public static void main(String args[])
	{
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put("00", "00");
		tmp.put("0100", "0100");
		tmp.put("0200", "0200");
		tmp.put("0300", "0300");
		tmp.put("0400", "0400");
		for (Map.Entry<String, String> entry : tmp.entrySet())
		{
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
		}
		System.out.println("--------");
		Iterator<Map.Entry<String, String>> it = tmp.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
		}
		Set<String> key = tmp.keySet();
		for (Iterator its = key.iterator(); its.hasNext();)
		{
			String s = (String) its.next();
			System.out.println(s + "==" + tmp.get(s));
		}
		List list = new ArrayList();
		list.add("0200");
		list.add("0300");
		list.add("00");
		list.add("0400");
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
		String flag = "end";
		long t = System.currentTimeMillis() / 1000;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		try
		{
			date = new SimpleDateFormat("yyyy-MM").parse("2014-09");
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		if (flag.equals("start"))
		{
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t = cal.getTimeInMillis() / 1000;
		}
		else if (flag.equals("end"))
		{
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			t = cal.getTimeInMillis() / 1000 - 1;
		}
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
				.getTime()));
		System.out.println(t);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
				.getTime()));
		System.out.println(cal.getTimeInMillis() / 1000 + "--");
		String times = "1417068575";
		System.out.println(new ServiceAct().getDateString(times));
	}
}
