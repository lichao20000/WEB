package com.linkage.module.gtms.report.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.StatisticsNXBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;

@SuppressWarnings("serial")
public class StatisticsNXACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	// 转换的 时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 装换的时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;

	// 属地列表
	private List<Map<String, String>> cityList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> presetList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> devList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> data;
	private String[] title;
	private String[] column;
	private String fileName;
	private String ajax;

	private StatisticsNXBIO bio;

	private Cursor cursor = null;

	private Map<String,String> fields = null;

	private Map<String,String> cityTotalMap = new HashMap<String,String>();
	private Map<String,ArrayList<String>> cityListMap = new HashMap<String,ArrayList<String>>();

	private String htmlList = "";

	/*********************************************************************/
	public String init() {
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	@SuppressWarnings("unchecked")
	public String queryList() {
		setTime();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String userCityID = curUser.getCityId();
		StringBuffer sb = new StringBuffer();

		// 初始化地市小计
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		Iterator<String> cityIt = cityMap.keySet().iterator();
		while (cityIt.hasNext())
		{
			cityTotalMap.put(cityIt.next(), "0");
		}
		StringBuffer sqlCount = new StringBuffer();

		sqlCount.append("select count(*) as num,a.city_id ,c.vendor_name,d.device_model ");
		sqlCount.append("from tab_gw_device a,tab_devicetype_info b, tab_vendor c,gw_device_model d ");
		sqlCount.append("where a.devicetype_id=b.devicetype_id and b.vendor_id = c.vendor_id and b.device_model_id = d.device_model_id and a.device_type = 'e8-b'");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sqlCount.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (null != startOpenDate1 && !"".equals(startOpenDate1))
		{
			sqlCount.append(" and a.complete_time>");
			sqlCount.append(startOpenDate1);
		}
		if (null != endOpenDate1 && !"".equals(endOpenDate1))
		{
			sqlCount.append(" and a.complete_time<");
			sqlCount.append(endOpenDate1);
		}
		sqlCount.append(" group by a.city_id ,c.vendor_name,d.device_model ");
		PrepareSQL psqlCount = new PrepareSQL(sqlCount.toString());
		psqlCount.getSQL();
		cursor = DataSetBean.getCursor(sqlCount.toString());


		Map<String,String> cityCount = null;
		Map<String,Map<String,String>> model = null;
		Map<String,Map<String,Map<String,String>>> result = new HashMap<String,Map<String,Map<String,String>>>();
		fields = cursor.getNext();
		long vendortmp = 0;

		while (fields != null)
		{
			String vendor_name = fields.get("vendor_name");
			String device_model = fields.get("device_model");
			String city_id = fields.get("city_id");
			String num = fields.get("num");

			if(null == result.get(vendor_name))
			{
				model = new HashMap<String,Map<String,String>>();
			}
			else
			{
				model = result.get(vendor_name);
			}

			if(null == model.get(device_model))
			{
				cityCount = new HashMap<String,String>();
			}
			else
			{
				cityCount = model.get(device_model);
			}

			cityCount.put(city_id, num);
			model.put(device_model, cityCount);
			result.put(vendor_name, model);

			// 按厂商累加，将小计信息放入cityTotalMap sssss
			vendortmp = Long.parseLong(num)
					+ Long.parseLong(StringUtil.getStringValue(cityTotalMap, fields.get("city_id"), "0"));
			cityTotalMap.put(fields.get("city_id"),
					String.valueOf(vendortmp));

			fields = cursor.getNext();
		}
		if (null == result || result.size() == 0) {
			htmlList = "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无任何业务!</TD></TR>";
			return "list";
		}

		// 输出表头
		sb.append("<TR>");
		sb.append("<TH nowrap>厂商</TH><TH>设备型号</TH>");
		// 在从数据库中 统计 业务属地对应的数据
		// 属地MAP 当前用户看到本身及其下属地市
		List<String> cityList = new ArrayList<String>();
		cursor = getCityList(city_id);
		fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				cityList.add(fields.get("city_id"));
				sb.append("<TH nowrap>" + fields.get("city_name") + "</TH>");
				fields = cursor.getNext();
			}
		} else {
			htmlList = "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
			return "list";
		}

		sb.append("<TH nowrap>小计</TH></TR>");

		// 初始化地市信息
		getNextCityList(cityList);

		Iterator<String> itVender = result.keySet().iterator();
		while (itVender.hasNext()) {
			String key =  itVender.next();
			Map<String,Map<String,String>> modelMap =  result.get(key);
			int size = modelMap.size();
			sb.append("<TR ><TD nowrap rowspan='" + size
					+ "' class=column align='right'>" + key
					+ "</TD>");

			Iterator<String> itMode = modelMap.keySet().iterator();
			while(itMode.hasNext())
			{
				String keyModel = itMode.next();
				Map<String,String> ctMap = modelMap.get(keyModel);
				sb.append("<TD"
						+ " class=column align='right'>" + keyModel + "</TD>");
				sb.append(getEditionStatePrint(cityList, ctMap, userCityID));
			}
		}

//		// total为 同一个地方所有版本的个数
		String num = getSubTotal(cityList, userCityID).toString();
				// 输出小计信息

		sb.append("<tr><td nowrap align='right' class=column colspan=2>小计</td>");
		sb.append(num);

		cityMap = null;
		cursor = null;
		fields = null;
		htmlList = sb.toString();
		return "list";
	}

	/**
	 * 输出小计信息
	 *
	 * @param cityList
	 * @param isPrt
	 * @param total
	 * @return
	 */
	private StringBuffer getSubTotal(List<String> cityList, String userCityID) {
		StringBuffer sbTable = new StringBuffer();
		long totalNum = 0;
		Iterator<String> it = cityList.iterator();
		while (it.hasNext()) {

			String city = (String) it.next();
			long num = 0;
			if (userCityID.equals(city)) {
				num = Long.parseLong(cityTotalMap.get(city));
			} else {
				List<String> childList = CityDAO.getAllNextCityIdsByCityPid(city);
				for (int i = 0; i < childList.size(); i++) {
					num += Long.parseLong(cityTotalMap.get(childList.get(i)));
				}
				childList = null;
			}

			String num_str = String.valueOf(num);

			if ("0".equals(num_str)) {
				sbTable.append("<TD bgcolor='#ffffff' align='center'>" + num_str
						+ "</a></TD>");
			} else
			{
				sbTable.append("<TD bgcolor='#ffffff' align='center'>"
							+ num_str + "</a></TD>");
			}

			totalNum += Long.parseLong(num_str);
		}

		sbTable.append("<td bgcolor='#ffffff' align='center'>" + totalNum
					+ "</td></tr>");

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

	private String getEditionStatePrint( List<String> cityList, Map<String,String> cityNum, String userCityID) {
		Iterator<String> it = cityList.iterator();
		String num = null;
		StringBuffer sb = new StringBuffer();
		long total = 0;
		String city_id = "";
		while (it.hasNext()) {
			city_id = it.next();
			ArrayList<String> cityAll =  cityListMap.get(city_id);
			// 根据{属地_业务id} 得到 统计数量
			if (cityNum != null) {
				if (userCityID.equals(city_id)) {
					num = (String) cityNum.get(city_id);
				} else {
					num = getCityVendorCount(cityAll, cityNum);
				}

				if (num != null && !"0".equals(num)) {
					if (!"".equals(num)) {
						total += Long.parseLong(num);
					}

					sb.append("<TD bgcolor='#ffffff' align='center'>" + num
							+ "</TD>");
				} else {
					sb.append("<TD bgcolor='#ffffff' align='center'>0</TD>");
				}
			} else if (cityNum == null) {
				sb.append("<TD bgcolor='#ffffff' align='center'>0</TD>");
			}
		}

		sb.append("<TD bgcolor='#ffffff' align='center'>" + total + "</TD>");

		sb.append("</TR>");
		return sb.toString();
	}


	/**
	 * 获取当前用户的下级属地
	 *
	 * @param request
	 * @return
	 */
	public Cursor getCityList(String city_id) {
		String m_cityList = "select city_id,city_name from tab_city ";
//		if(!"00".equals(city_id))
//		{
			m_cityList += "where parent_id='"+ city_id + "' or city_id='" + city_id + "' order by city_id";
//		}
//		else
//		{
//			m_cityList += "where parent_id='"+ city_id + "'  order by city_id";
//		}
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
	private String getCityVendorCount(ArrayList<String> cityAll, Map<String,String> tmpMap) {
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;

		Iterator<String> it = cityAll.listIterator();

		while (it.hasNext()) {
			tmpCity =  it.next();

			tmp =  tmpMap.get(tmpCity);

			if (tmp != null && !"".equals(tmp)) {
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
	private void getNextCityList(List<String> cityList) {
		Iterator<String> it = cityList.iterator();
		String city_id = "";
		while (it.hasNext()) {
			city_id = (String) it.next();
			ArrayList<String> cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
			cityListMap.put(city_id, cityAll);
			cityAll = null;
		}
	}



	private void setTime() {
		DateTimeUtil dt = null;
		if (startOpenDate == null || "".equals(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getPresetList() {
		return presetList;
	}

	@SuppressWarnings("rawtypes")
	public void setPresetList(List<Map> presetList) {
		this.presetList = presetList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevList() {
		return devList;
	}

	@SuppressWarnings("rawtypes")
	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getHtmlList() {
		return htmlList;
	}

	public void setHtmlList(String htmlList) {
		this.htmlList = htmlList;
	}

	public StatisticsNXBIO getBio() {
		return bio;
	}

	public void setBio(StatisticsNXBIO bio) {
		this.bio = bio;
	}

}
