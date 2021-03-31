
package com.linkage.litms.resource;

import java.util.ArrayList;
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
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.resource.ServiceAct;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-3
 * @category com.linkage.module.gtms.stb.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StbDeviceReport
{

	private static Logger logger = LoggerFactory.getLogger(StbDeviceReport.class);
	private Map vendorMap = null;
	private Cursor cursor = null;
	private Cursor cursorCity = null;
	private Cursor cursorCityNum = null;
	private Cursor cursorVendorNum = null;
	private Map fieldCitys = null;
	private Map fields = null;
	private Map fieldsVendorNum = null;
	private Map fieldsCityNum = null;
	private String m_VendorInfo_stb_linux = "select distinct b.vendor_id,b.vendor_name from stb_gw_device_model a,stb_tab_vendor b where a.vendor_id=b.vendor_id and a.stbsys_type = 1";
	private String m_VendorInfo_stb = "select vendor_id,vendor_name from stb_tab_vendor";
	private String m_vendorDevice_stb = "select count(a.device_id) as num,a.vendor_id,a.city_id,b.stbsys_type from stb_tab_gw_device a,stb_gw_device_model b,stb_tab_vendor c ,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.device_status = 1 group by a.vendor_id,a.city_id,b.stbsys_type";
	private String m_city_num_stb = "select count(a.device_id) as num ,a.city_id from stb_tab_gw_device a ,stb_gw_device_model b,stb_tab_vendor c ,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.device_status = 1 group by a.city_id";
	private String m_VendorNum_stb = "select count(a.device_id) as num,a.vendor_id,b.stbsys_type from stb_tab_gw_device a,stb_gw_device_model b,stb_tab_vendor c,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.device_status = 1 group by a.vendor_id,b.stbsys_type";
	private Map cityListMap = new HashMap();
	// 一级属地Map
	private Map mainCityListMap = new HashMap();
	private Map cityTotalMap = new HashMap();
	private PrepareSQL pSQL;
	private PrepareSQL pSQLCityNum;
	private PrepareSQL pSQLVendorNum;

	public StbDeviceReport()
	{
		pSQL = new PrepareSQL();
		pSQLCityNum = new PrepareSQL();
		pSQLVendorNum = new PrepareSQL();
	}

	/**
	 * 经分报表（按linux和安卓统计）
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlDeviceByLinuxAndAndroid(HttpServletRequest request)
	{
		logger.warn("getHtmlDeviceByLinuxAndAndroid....");
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCity = curUser.getCityId();
		StringBuffer sbTable = new StringBuffer();
		// 初始化厂商信息
		getVendorMap();
		// 先读取所有业务
		List[] listService = getVendorIdNameList();
		if (listService == null)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		List listServiceId = listService[0];
		int andRoidThCount = listServiceId.size();
		List[] listServiceLinux = getVendorIdLinuxNameList();
		if (listServiceLinux == null)
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
		}
		List listServiceLinuxId = listServiceLinux[0];
		int linuxCount = listServiceLinuxId.size();
		// 输出经分报表表头
		sbTable.append("<TR>");
		sbTable.append("<TH rowspan='3' >地市</TH>");
		sbTable.append("<TH rowspan='3' >用户累计达到</TH>");
		sbTable.append("<TH rowspan='3' >装通用户合计</TH>");
		sbTable.append("<TH colspan='" + linuxCount + "' >Linux</TH>");
		sbTable.append("<TH colspan='" + andRoidThCount + "' >安卓</TH>");
		sbTable.append("</TR>");
		sbTable.append("<TR>");
		String message = "";
		Iterator itFirst = listServiceLinuxId.iterator();
		Iterator itSecond = listServiceId.iterator();
		// 遍历两次，判断Linux+安卓
		while (itFirst.hasNext())
		{
			message = (String) vendorMap.get(itFirst.next());
			sbTable.append("<TH nowrap align='right'>" + message + "</TD>");
		}
		while (itSecond.hasNext())
		{
			message = (String) vendorMap.get(itSecond.next());
			sbTable.append("<TH nowrap align='right'>" + message + "</TD>");
		}
		sbTable.append("</TR>");
		sbTable.append("<TR>");
		for (int i = 1; i <= (andRoidThCount + linuxCount); i++)
		{
			sbTable.append("<TH nowrap align='right'>装通</TD>");
		}
		sbTable.append("</TR>");
		// 经分报表表头拼接完毕
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List cityList = new ArrayList();
		cursorCity = getCityList(request);
		fieldCitys = cursorCity.getNext();
		while (fieldCitys != null)
		{
			cityList.add(fieldCitys.get("city_id"));
			mainCityListMap.put(fieldCitys.get("city_id"), fieldCitys.get("city_name"));
			fieldCitys = cursorCity.getNext();
		}
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
		String tmp = "";
		long vendortmp = 0;
		long total = 0;
		// 将每个city_id的设备及厂商统计信息放入对应的map中
		Map mapServiceCityNum = new HashMap();
		pSQL.setSQL(m_vendorDevice_stb);
		cursor = DataSetBean.getCursor(pSQL.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		fields = cursor.getNext();
		while (fields != null)
		{
			if (cityMap.get(fields.get("city_id")) != null)
			{
				// 将厂商+属地+类型（linux/安卓）==>num 作为映射存入map中
				mapServiceCityNum.put(
						fields.get("city_id") + "_" + fields.get("vendor_id") + "_"
								+ fields.get("stbsys_type"), fields.get("num"));
				tmp = (String) fields.get("num");
				if ((tmp == null) || "".equals(tmp))
				{
					tmp = "0";
				}
			}
			fields = cursor.getNext();
		}
		pSQLCityNum.setSQL(m_city_num_stb);
		cursorCityNum = DataSetBean.getCursor(pSQLCityNum.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		fieldsCityNum = cursorCityNum.getNext();
		while (fieldsCityNum != null)
		{
			if (cityMap.get(fieldsCityNum.get("city_id")) != null)
			{
				tmp = (String) fieldsCityNum.get("num");
				if ((tmp == null) || "".equals(tmp))
				{
					tmp = "0";
				}
				// 按厂商累加
				vendortmp = Long.parseLong(tmp)
						+ Long.parseLong((String) cityTotalMap.get(fieldsCityNum.get("city_id")));
				cityTotalMap.put(fieldsCityNum.get("city_id"), String.valueOf(vendortmp));
			}
			fieldsCityNum = cursorCityNum.getNext();
		}
		// 开始遍历,输出统计数据
		String isPrt = request.getParameter("isPrt");
		logger.warn("whether is download : " + cityTotalMap);
		Iterator itCity = cityList.iterator();
		while (itCity.hasNext())
		{
			sbTable.append(getVendorStatRowHtml((String) itCity.next(), cityList,
					listServiceId,listServiceLinuxId, mapServiceCityNum, userCity));
		}
		// 输出小计信息
		String num = getSubTotalAll(userCity);
		sbTable.append("<tr><td nowrap align='right' class=column>合计</td>");
		sbTable.append("<TD bgcolor=#ffffff align=center>" + num + "</a></TD>");
		sbTable.append("<TD bgcolor=#ffffff align=center>" + num + "</a></TD>");
		sbTable.append(getVendorNum(listServiceId,listServiceLinuxId));
		// Clear Resouce
		listServiceId.clear();
		listServiceId = null;
		mapServiceCityNum.clear();
		mapServiceCityNum = null;
		cityMap = null;
		cityTotalMap = null;
		cursor = null;
		cursorCity = null;
		cursorCityNum = null;
		cursorVendorNum = null;
		fields = null;
		fieldCitys = null;
		fieldsVendorNum = null;
		fieldsCityNum = null;
		mainCityListMap = null;
		cityListMap = null;
		return sbTable.toString();
	}

	/**
	 * 生产厂商用户数量（对应linux+安卓） Linux为1，安卓为2.
	 * 
	 * @return
	 */
	private StringBuffer getVendorNum(List listServiceId, List listServiceLinuxId)
	{
		logger.warn("getVendorNumStart................................................");
		pSQLVendorNum.setSQL(m_VendorNum_stb);
		cursorVendorNum = DataSetBean.getCursor(
				pSQLVendorNum.getSQL(),
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
		fieldsVendorNum = cursorVendorNum.getNext();
		logger.warn("fieldsVendorNum num is :  " + fieldsVendorNum);
		// 将厂商和类型所对应的设备数量放入map中
		Map mapServiceCityNum = new HashMap();
		while (fieldsVendorNum != null)
		{
			// 将厂商+类型（linux/安卓）==>num 作为映射存入map中
			mapServiceCityNum.put(fieldsVendorNum.get("vendor_id") + "_"
					+ fieldsVendorNum.get("stbsys_type"), fieldsVendorNum.get("num"));
			fieldsVendorNum = cursorVendorNum.getNext();
		}
		String num = null;
		StringBuffer sb = new StringBuffer();
		Iterator itFirst = listServiceLinuxId.iterator();
		Iterator itSecond = listServiceId.iterator();
		// 拼接合计数量（Linux和安卓）
		while (itFirst.hasNext())
		{
			num = (String) mapServiceCityNum.get(itFirst.next() + "_" + "1");
			if (null != num && !"0".equals(num))
			{
				sb.append("<TD nowrap class=column align=center>" + num + "</TD>");
			}
			else
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		while (itSecond.hasNext())
		{
			num = (String) mapServiceCityNum.get(itSecond.next() + "_" + "0");
			if (null != num && !"0".equals(num))
			{
				sb.append("<TD nowrap class=column align=center>" + num + "</TD>");
			}
			else
			{
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
			}
		}
		return sb;
	}

	/**
	 * 将厂商信息放入MAP中
	 */
	private void getVendorMap()
	{
		String sql;
		sql = "select * from stb_tab_vendor order by vendor_add";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select vendor_add, vendor_id, vendor_name from stb_tab_vendor order by vendor_add";
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
	 * 厂商信息列表
	 * 
	 * @return
	 */
	public List[] getVendorIdNameList()
	{
		PrepareSQL psql = new PrepareSQL(m_VendorInfo_stb);
		psql.getSQL();
		cursor = DataSetBean.getCursor(
				m_VendorInfo_stb,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
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
	 * Linux厂商信息列表
	 * 
	 * @return
	 */
	public List[] getVendorIdLinuxNameList()
	{
		PrepareSQL psql = new PrepareSQL(m_VendorInfo_stb_linux);
		psql.getSQL();
		cursor = DataSetBean.getCursor(
				m_VendorInfo_stb_linux,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
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
		logger.warn("current user city_id : ...   " + city_id);
		String m_cityList = "";
		m_cityList = "select city_id,city_name from tab_city where parent_id='" + city_id
				+ "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
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

	private String getLinuxAndAndroidHtml()
	{
		return null;
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
	private String getVendorStatRowHtml(String cityId, List cityList, List listServiceId, List listServiceLinuxId,
			Map mapServiceCityNum, String userCityID)
	{
		Iterator itFirst = listServiceLinuxId.iterator();
		Iterator itSecond = listServiceId.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		String message = "";
		String vendor_id = "";
		message = (String) mainCityListMap.get(cityId);
		sb.append("<TR><TD nowrap class=column align='right'>" + message + "</TD>");
		// 拼接用户累计达到
		sb.append(getSubTotal(cityId, userCityID));
		// 拼接装通用户合计
		sb.append(getSubTotal(cityId, userCityID));
		// 先拼linux机顶盒总数量
		while (itFirst.hasNext())
		{
			vendor_id = (String) itFirst.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(cityId);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				if (userCityID.equals(cityId))
				{
					num = (String) mapServiceCityNum.get(cityId + "_" + vendor_id + "_"
							+ "1");
				}
				else
				{
					num = getCityVendorCount(cityAll, vendor_id, mapServiceCityNum, "1");
				}
				if (null != num && !"0".equals(num))
				{
					sb.append("<TD nowrap class=column align=center>" + num + "</TD>");
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
		// 再拼安卓机顶盒数量
		while (itSecond.hasNext())
		{
			vendor_id = (String) itSecond.next();
			ArrayList cityAll = (ArrayList) cityListMap.get(cityId);
			// 根据{属地_业务id} 得到 统计数量 得到同一属地不同版本的个数
			if (mapServiceCityNum != null)
			{
				// 先拼linux总数量
				if (userCityID.equals(cityId))
				{
					num = (String) mapServiceCityNum.get(cityId + "_" + vendor_id + "_"
							+ "0");
				}
				else
				{
					num = getCityVendorCount(cityAll, vendor_id, mapServiceCityNum, "0");
				}
				if (num != null && !"0".equals(num))
				{
					sb.append("<TD nowrap class=column align=center>" + num + "</TD>");
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
		sb.append("</TR>");
		return sb.toString();
	}

	/**
	 * 根据Linux/android将传入的属地列表中所有数据相加
	 * 
	 * @param cityAll
	 * @param editionId
	 * @param tmpMap
	 * @return
	 */
	private String getCityVendorCount(ArrayList cityAll, String editionId, Map tmpMap,
			String typeLinuxAndroid)
	{
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		Iterator it = cityAll.listIterator();
		while (it.hasNext())
		{
			tmpCity = (String) it.next();
			tmp = (String) tmpMap.get(tmpCity + "_" + editionId + "_" + typeLinuxAndroid);
			if (tmp != null && !"".equals(tmp))
			{
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		return String.valueOf(total);
	}

	/**
	 * 输出小计信息
	 * 
	 * @param cityList
	 * @param isPrt
	 * @param total
	 * @return
	 */
	// TODO
	private StringBuffer getSubTotal(String city, String userCityID)
	{
		StringBuffer sbTable = new StringBuffer();
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
			sbTable.append("<TD bgcolor=#ffffff align=center>" + num_str + "</a></TD>");
		}
		else
		{
			sbTable.append("<TD bgcolor=#ffffff align=center>" + num_str + "</a></TD>");
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
	private String getSubTotalAll(String userCityID)
	{
		long num = 0;
		/**
		 * modify by qixueqi
		 */
		List childList = CityDAO.getAllNextCityIdsByCityPid(userCityID);
		for (int i = 0; i < childList.size(); i++)
		{
			num += Long.parseLong((String) cityTotalMap.get(childList.get(i)));
		}
		childList = null;
		String num_str = String.valueOf(num);
		return num_str;
	}
}
