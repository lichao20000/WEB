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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;

/**
 * 百兆口报表走临时表统计
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-10-12
 * @category com.linkage.litms.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ServiceSpeedUserActQuality
{
	
	private static Logger logger = LoggerFactory.getLogger(ServiceSpeedUserActQuality.class);
	
	private Cursor cursor = null;
	@SuppressWarnings("rawtypes")
	private Map fields = null;
	private PrepareSQL pSQL;
	@SuppressWarnings("rawtypes")
	private Map cityTotalMap = new HashMap();
	@SuppressWarnings("rawtypes")
	private Map cityListMap = new HashMap();
	@SuppressWarnings("rawtypes")
	private Map versionMap = null;
	@SuppressWarnings("rawtypes")
	private Map vendorMap = null;
	@SuppressWarnings("rawtypes")
	private List cityIdList = new ArrayList();

	public ServiceSpeedUserActQuality()
	{
		pSQL = new PrepareSQL();
	}
	
	/**
	 * 获取当前用户的下级属地
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	/**
	 * 将厂商信息放入MAP中
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	 * 将软件版本等信息放入MAP中
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
					+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id ";
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
	 * 获取当前用户的下级属地
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getCity(String city_id)
	{
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
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
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		logger.warn("getHtmlDeviceByEditionAHDX... " + gw_type_Str);
		int gw_type = Integer.parseInt(gw_type_Str);
		StringBuffer sbTable = new StringBuffer();
		getVendorMap(gw_type);
		String vendorId = request.getParameter("vendorId");
		String modelId = request.getParameter("modelId");
		String deviceTypeId = request.getParameter("deviceTypeId");
		String no_query = request.getParameter("no_query");
		String city_id = request.getParameter("city_id");// 修改处
		String is_esurfing = request.getParameter("is_esurfing");// 天翼网关更改的地方 jiangkun
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		String mbBroadband = StringUtil.getStringValue(request.getParameter("mbBroadband"));
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
			String is_normal = request.getParameter("is_normal");
			// 判断是否有业务 改
			String rela_dev_type_id = request.getParameter("rela_dev_type_id");
			String access_style_relay_id = request.getParameter("access_style_relay_id");
			Map mapService = getEditionMap(vendorId, modelId, deviceTypeId, 
					rela_dev_type_id, access_style_relay_id, userCityID, gw_type,
					is_esurfing,mbBroadband);
			if (null == mapService)
			{
				return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			}
			// 输出表头
			sbTable.append("<TR>");
			if (4 == gw_type)
			{
				sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH>");
			}
			else
			{
				if(Global.XJDX.equals(Global.instAreaShortName)
						|| Global.JXDX.equals(Global.instAreaShortName))
				{
					sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH>");
				}
				else
				{
					sbTable.append("<TH nowrap>厂商</TH><TH>设备型号</TH><TH>软件版本</TH><TH>硬件版本</TH><TH style='word-break:keep-all'>是否规范版本</TH>");
				}
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
			StringBuffer sql1 = new StringBuffer();
			if (4 == gw_type)
			{
				sql1.append("select count(a.device_id) as num,a.devicetype_id,a.city_id from stb_tab_gw_device a,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 ");
				pSQL.setSQL(sql1.toString());
			}
			else
			{
				sql1.append(" select count(a.device_id) as num,a.devicetype_id ,a.city_id from tab_gw_device a,tab_netacc_spead c,hgwcust_serv_info d, tab_hgwcustomer e ,"
						+ " tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id = e.device_id and e.user_id = d.user_id and d.username = c.username and c.downlink >= 200"
						+ " and a.device_status = 1 and a.customer_id is not null and a.gw_type=1 ");
				pSQL.setSQL(sql1.toString());
			}
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
			if (null != mbBroadband && !"null".equals(mbBroadband)
					&& !"-1".equals(mbBroadband))
			{
				sql.append(" and t.mbbroadband=" + mbBroadband + " ");
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
			sql.append(" group by a.devicetype_id,a.city_id ");
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
							tmpRow = getEditionStatePrint(devicetypeStr, "1", cityList,
									mapServiceCityNum, gw_type, userCityID);
						}
						else
						{
							tmpRow = getVendorStatRowHtml(devicetypeStr, "1", cityList,
									mapServiceCityNum, gw_type, userCityID, isBind);
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
			/*String num = "";
			num = getSubTotal(cityList, isPrt, total, gw_type, userCityID).toString();
			// 输出小计信息
			if (4 == gw_type)
			{
				sbTable.append("<tr><td nowrap align='right' class=column colspan=4>小计</td>");
			}
			else
			{
				sbTable.append("<tr><td nowrap align='right' class=column colspan=5>小计</td>");
			}
			sbTable.append(num);*/
			mapServiceCityNum.clear();
			mapServiceCityNum = null;
			cityMap = null;
		}
		logger.warn("end .... ");
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
	@SuppressWarnings({ "rawtypes", "unused" })
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
	 * 版本信息列表Map<vendor_id,Map<device_model,List<devicetype_id>>>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getEditionMap(String vendorId, String modelId, String deviceTypeId,
			 String rela_dev_type_id, String access_style_relay_id,
			String userCityID, int gw_type, String is_esurfing, String mbBroadband)
	{
		StringBuffer bf = new StringBuffer();
		if (4 == gw_type)
		{
			bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
					+ "stb_tab_devicetype_info a,stb_gw_device_model b where a.device_model_id=b.device_model_id ");
		}
		else
		{
			bf.append(" select count(a.device_id) as num,a.devicetype_id,a.vendor_id,b.device_model from tab_gw_device a,gw_device_model b,"
					+ " tab_netacc_spead c,hgwcust_serv_info d, tab_hgwcustomer e ,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_model_id=b.device_model_id "
					+ " and a.device_id = e.device_id and e.user_id = d.user_id and d.username = c.username and c.downlink >= 200 and a.device_status = 1 and a.customer_id is not null and a.gw_type=1 ");
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
		if (null != mbBroadband && !"null".equals(mbBroadband)
				&& !"-1".equals(mbBroadband))
		{
			bf.append(" and t.mbbroadband=" + mbBroadband + " ");
		}
		bf.append(" group by a.devicetype_id,a.vendor_id,b.device_model order by  a.vendor_id,b.device_model,a.devicetype_id ");
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
	 * 将各地市的下级属地放入MAP中
	 * 
	 * @param cityList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	@SuppressWarnings("rawtypes")
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
					if (4 != gw_type && (!Global.XJDX.equals(Global.instAreaShortName)
							|| Global.JXDX.equals(Global.instAreaShortName)))
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
	 * 输出地市以及地市与业务对应的用户统计数量
	 * 
	 * @param vendor_id
	 *            版本id
	 * @param type
	 * @param cityList
	 * @param mapServiceCityNum
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getVendorStatRowHtml(String devicetypeStr, String type, List cityList,
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
			message = StringUtil.getStringValue(versionMap, devicetypeStr, "");
			String[] tmp = message.split("\\|");
			if (tmp != null && tmp.length > 2)
			{
				sb.append("<TD nowrap class=column align='right'>" + tmp[2] + "</TD>");
				if (tmp.length > 4)
				{
					// 硬件版本
					sb.append("<TD nowrap class=column align='right'>" + tmp[4] + "</TD>");
					// 是否规范
					if (4 != gw_type && (!Global.XJDX.equals(Global.instAreaShortName)
							|| Global.JXDX.equals(Global.instAreaShortName)))
					{
						sb.append("<TD nowrap class=column align='right'>" + tmp[3]
								+ "</TD>");
					}
				}
				else
				{
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
			message = (String) vendorMap.get(devicetypeStr);
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
					sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick="
							+ jsStr.replaceAll("\\?", city_id).replaceAll("\\#",
									devicetypeStr) + ">" + num + "</a></TD>");
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
					+ jsStr.replaceAll("\\#", devicetypeStr).replaceAll("\\?", "-1") + ">"
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
	 * 将传入的属地列表中所有数据相加
	 * 
	 * @param cityAll
	 * @param editionId
	 * @param tmpMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
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
	
}
