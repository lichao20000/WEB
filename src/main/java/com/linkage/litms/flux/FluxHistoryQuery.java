package com.linkage.litms.flux;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;

/**
 * 
 * @author liuw 2007-5-18 设备流量历史查询
 */
public class FluxHistoryQuery {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(FluxHistoryQuery.class);
	private HttpServletRequest request;

	private String tableName = "";

	DateTimeUtil dateTimeUtil = null;

	Cursor cursor = null;

	Map fields = null;

	long end = 0;

	long start = 0;

	private String strData = "";

	private String condition = new String();

	private HashMap map_realSpeed = new HashMap();

	String[] reDate;

	private PrepareSQL pSQL = new PrepareSQL();

	public FluxHistoryQuery(HttpServletRequest request) {
		this.request = request;
	}

	public String workControl() {
		String reFlux = null;
		String deviceId = "";
		String time = request.getParameter("hidday");
		String kind = request.getParameter("kind");
		deviceId = request.getParameter("dev_id");
		logger.debug("deviceId--->" + deviceId);
		if (deviceId == null || deviceId.equals("")) {
			deviceId = request.getParameter("sourceipValue");
		}
		String portInfo = request.getParameter("getwayValue");
		String getWay = request.getParameter("port");
		String type = request.getParameter("SearchType");
		if (type.equals("1")) {// 日报表
			reFlux = this.getFluxDay(time, kind, deviceId, portInfo, getWay);
		} else if (type.equals("2")) {// 周报表
			reFlux = this.getFluxWeek(time, kind, deviceId, portInfo, getWay);
		} else if (type.equals("3")) {// 月报表
			reFlux = this.getFluxMonth(time, kind, deviceId, portInfo, getWay);
		}
		return reFlux;
	}

	/**
	 * 日报表
	 * 
	 * @param time
	 * @param kind
	 * @param device_id
	 * @param port_info
	 * @param getway
	 * @return
	 */
	public String getFluxDay(String time, String kind, String device_id,
			String port_info, String getway) {
		logger.debug("====日报表====" + time);
		dateTimeUtil = new DateTimeUtil(Long.parseLong(time.trim()) * 1000);
		String[] fluxType = kind.split(",");
		start = Long.parseLong(time);
		end = start + 24 * 3600;
		tableName = "flux_data_" + dateTimeUtil.getYear() + "_"
				+ dateTimeUtil.getMonth() + "_" + dateTimeUtil.getDay();
		logger.debug("表名====>" + tableName);
		String strDay = "select " + fluxType[0] + "/1000000 as value1,"
				+ fluxType[1]
				+ "/1000000 as value2,collecttime,device_id from " + tableName
				+ " where device_id='" + device_id + "' and  port_info='"
				+ port_info + "' and getway=" + getway + " and collecttime >="
				+ start + " and collecttime<=" + end + " order by collecttime";
		PrepareSQL psql = new PrepareSQL(strDay);
    	psql.getSQL();
    	cursor = DataSetBean.getCursor(strDay);
		fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				reDate = StringUtils.secondToDateStr(Integer
						.parseInt((String) fields.get("collecttime")));
				strData += "<TR align=center><td bgcolor=#ffffff>"
						+ this.getDeviceInfo((String) fields.get("device_id"))
						+ "</td>";
				strData += "<td bgcolor=#ffffff>" + fields.get("value1")
						+ "</td>";
				strData += "<td bgcolor=#ffffff>" + fields.get("value2")
						+ "</td>";
				strData += "<td bgcolor=#ffffff>" + reDate[0] + "年" + reDate[1]
						+ "月" + reDate[2] + "日" + " " + reDate[3] + ":"
						+ reDate[4] + ":" + reDate[5] + "</td></TR>";
				fields = cursor.getNext();
			}
		} else {
			strData += "<TR><td colspan=4 bgcolor=#ffffff>没有数据</td></TR>";
		}
		return strData;
	}

	/**
	 * 周报表
	 * 
	 * @param time
	 * @param kind
	 * @param device_id
	 * @param port_info
	 * @param getway
	 * @return
	 */
	public String getFluxWeek(String time, String kind, String device_id,
			String port_info, String getway) {
		logger.debug("====周报表====" + time);
		String Q_time = "";
		int cnt = 0;
		String lastWeek;
		if (this.isCurrWeek(time)) {
			Q_time = time;
			cnt = dateTimeUtil.getNoDayOfWeek(Q_time);
		} else {
			lastWeek = dateTimeUtil.getLastDayOfWeek(time);
			Q_time = String.valueOf(FluxHistoryQuery.DateToSecond(lastWeek));
			cnt = 7;
		}
		long t = Long.parseLong(Q_time);
		dateTimeUtil = new DateTimeUtil(
				Long.parseLong(Q_time.trim()) * 1000 + 86400);
		String[] fluxType = kind.split(",");
		logger.debug("查询" + cnt + "张表");
		for (int i = 1; i <= cnt; i++) {
			start = t;
			end = start + 24 * 3600;
			tableName = "flux_data_" + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth() + "_" + dateTimeUtil.getDay();
			dateTimeUtil.getNextDate(-1);
			t = t - 24 * 3600;
			logger.debug("tableName===>" + tableName + "   " + start
					+ "  " + end);

			String strWeek = "select " + fluxType[0] + "/1000000 as value1,"
					+ fluxType[1]
					+ "/1000000 as value2,collecttime,device_id from "
					+ tableName + " where device_id='" + device_id
					+ "' and  port_info='" + port_info + "' and getway="
					+ getway + " and collecttime >=" + start
					+ " and collecttime<=" + end;
			PrepareSQL psql = new PrepareSQL(strWeek);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(strWeek);
			fields = cursor.getNext();
			if (fields != null) {
				while (fields != null) {
					reDate = StringUtils.secondToDateStr(Integer
							.parseInt((String) fields.get("collecttime")));
					strData += "<TR><td bgcolor=#ffffff>"
							+ this.getDeviceInfo((String) fields
									.get("device_id")) + "</td>";
					strData += "<td bgcolor=#ffffff>" + fields.get("value1")
							+ "</td>";
					strData += "<td bgcolor=#ffffff>" + fields.get("value2")
							+ "</td>";
					strData += "<td bgcolor=#ffffff>" + reDate[0] + "年"
							+ reDate[1] + "月" + reDate[2] + "日" + " "
							+ reDate[3] + ":" + reDate[4] + ":" + reDate[5]
							+ "</td></TR>";
					fields = cursor.getNext();
				}
			} else {
				strData += "<TR><td colspan=4 bgcolor=#ffffff>没有数据</td></TR>";
			}
		}
		return strData;
	}

	/**
	 * 月报表
	 * 
	 * @param time
	 * @param kind
	 * @param device_id
	 * @param port_info
	 * @param getway
	 * @return
	 */
	public String getFluxMonth(String time, String kind, String device_id,
			String port_info, String getway) {
		logger.debug("====月报表====" + time);
		String queryDate = this.isCurrMonth(time);
		long t = Long.parseLong(queryDate);
		dateTimeUtil = new DateTimeUtil(
				Long.parseLong(queryDate.trim()) * 1000 + 86400);
		String[] fluxType = kind.split(",");
		int cnt = dateTimeUtil.getDay();
		for (int i = 1; i <= cnt; i++) {
			start = t;
			end = start + 24 * 3600;
			tableName = "flux_data_" + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth() + "_" + dateTimeUtil.getDay();
			dateTimeUtil.getNextDate(-1);
			t = t - 24 * 3600;
			logger.debug("tableName===>" + tableName + "   " + start
					+ "  " + end);
			String strMonth = "select " + fluxType[0] + "/1000000 as value1,"
					+ fluxType[1]
					+ "/1000000 as value2,collecttime,device_id from "
					+ tableName + " where device_id='" + device_id
					+ "' and  port_info='" + port_info + "' and getway="
					+ getway + " and collecttime >=" + start
					+ " and collecttime<=" + end;
			PrepareSQL psql = new PrepareSQL(strMonth);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(strMonth);
			fields = cursor.getNext();
			if (fields != null) {
				while (fields != null) {
					reDate = StringUtils.secondToDateStr(Integer
							.parseInt((String) fields.get("collecttime")));
					strData += "<TR><td bgcolor=#ffffff>"
							+ this.getDeviceInfo((String) fields
									.get("device_id")) + "</td>";
					strData += "<td bgcolor=#ffffff>" + fields.get("value1")
							+ "</td>";
					strData += "<td bgcolor=#ffffff>" + fields.get("value2")
							+ "</td>";
					strData += "<td bgcolor=#ffffff>" + reDate[0] + "年"
							+ reDate[1] + "月" + reDate[2] + "日" + " "
							+ reDate[3] + ":" + reDate[4] + ":" + reDate[5]
							+ "</td></TR>";
					fields = cursor.getNext();
				}
			} else {
				strData += "<td colspan=4 bgcolor=#ffffff>没有数据</td>";
			}
		}
		return strData;
	}

	/**
	 * get设备名称
	 * 
	 * @param deviceId
	 * @return
	 */
	public String getDeviceInfo(String deviceId) {
		String reDeviceName = "";
		String strDeviceInfo = "select device_name from tab_deviceresource where device_id='"
				+ deviceId + "'";
		PrepareSQL psql = new PrepareSQL(strDeviceInfo);
    	psql.getSQL();
		Map deviceMap = DataSetBean.getRecord(strDeviceInfo);
		reDeviceName = (String) deviceMap.get("device_name");
		return reDeviceName;
	}

	public boolean isCurrWeek(String date) {
		logger.debug("date===>" + date);
		// 传入的日期是否是当前周
		dateTimeUtil = new DateTimeUtil(Long.parseLong(date.trim()) * 1000);
		String lastWeek = dateTimeUtil.getLastDayOfWeek(date);
		logger.debug("lastWeek===>" + lastWeek);
		long lastDay = FluxHistoryQuery.DateToSecond(lastWeek);
		logger.debug("lastDay===>" + lastDay);
		long currDay = FluxHistoryQuery.CurrDateToSecond();
		logger.debug("currDay===>" + currDay);
		if (lastDay <= currDay) {
			return false;
		} else {
			return true;
		}
	}

	public String isCurrMonth(String date) {
		logger.debug("date===>" + date);
		// 传入的日期是否是当前月
		String reDate = "";
		dateTimeUtil = new DateTimeUtil(Long.parseLong(date.trim()) * 1000);
		String lastMonth = dateTimeUtil.getLastDayOfMonth();
		logger.debug("lastWeek===>" + lastMonth);
		long lastDay = FluxHistoryQuery.DateToSecond(lastMonth);
		logger.debug("lastDay===>" + lastDay);
		long currDay = FluxHistoryQuery.CurrDateToSecond();
		logger.debug("currDay===>" + currDay);
		String[] tmp1 = StringUtils.secondToDateStr(Integer.parseInt(String
				.valueOf(lastDay)));
		String[] tmp2 = StringUtils.secondToDateStr(Integer.parseInt(String
				.valueOf(currDay)));
		if (tmp1[1].equals(tmp2[1])) {
			reDate = String.valueOf(currDay);

		} else {
			reDate = String.valueOf(lastDay);
		}
		return reDate;
	}

	/**
	 * 输入一个日期，获得该日期的0点0分0秒的秒数
	 * 
	 * @param date
	 * @return
	 */
	public static long DateToSecond(String date) {
		long currSecond = 0;
		java.sql.Date start = java.sql.Date.valueOf(date);
		currSecond = start.getTime() / 1000;
		return currSecond;
	}

	/**
	 * 获得当前日期的秒数，0点0分0秒
	 * 
	 * @return
	 */
	public static long CurrDateToSecond() {

		long currSecond = 0;
		Date currDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(currDate);
		java.sql.Date start = java.sql.Date.valueOf(today);
		currSecond = start.getTime() / 1000;
		return currSecond;
	}
	
	
	/**
	 * 设备流量查询for新疆
	 * 
	 * @return
	 */
	public Cursor getDevicePortFlux_XJ(String cityid)
	{
		String start = (String) request.getParameter("hidstart");// 日期的秒数
		String expressionName = (String) request.getParameter("checkedName");// 查询字段
		String strday = (String) request.getParameter("start");// 日期型2007-6-19
		String stype = (String) request.getParameter("SearchType");
		String restype = (String) request.getParameter("deviceType");
		String ifcontainChild = (String) request.getParameter("ifcontainChild");
		String hid_city_id = "";
		hid_city_id = (String) request.getParameter("hid_city_id");
		if (hid_city_id.equals("") || hid_city_id == null)
		{
			hid_city_id = cityid;
		}
		String[] strday_tmp = strday.split("-");
		String tablename = "";
		String startTime = "";
		String endTime = "";
		DateTimeUtil startDate = null;

		switch (Integer.parseInt(stype))
		{
			case 1:
				break;
			case 2:
				// 日报表
				startTime = start;
				endTime = String.valueOf(Long.parseLong(startTime) + 86400);
				tablename = "flux_day_stat" + "_" + strday_tmp[0] + "_"
								+ strday_tmp[1];
				break;
			case 3:
				// 周报表
				startDate = new DateTimeUtil(Long.parseLong(start) * 1000);
				startTime = String.valueOf(new DateTimeUtil(startDate.getFirstDayOfWeek("US")).getLongTime());
				startDate = new DateTimeUtil(Long.parseLong(start) * 1000);
				endTime = String.valueOf(new DateTimeUtil(startDate.getLastDayOfWeek("US")).getLongTime()+86400);
				tablename = "flux_week_stat" + "_" + strday_tmp[0];
				break;
			case 4:
				// 月报表
				startDate = new DateTimeUtil(Long.parseLong(start) * 1000);
				startTime = String.valueOf(new DateTimeUtil(startDate
								.getFirtDayOfMonth()).getLongTime());				
				startDate = new DateTimeUtil(Long.parseLong(start)*1000);
				endTime = String.valueOf(new DateTimeUtil(startDate.getLastDayOfMonth()).getLongTime()+86400);
				tablename = "flux_month_stat" + "_" + strday_tmp[0];
				break;
		}

		return this.getPortFluxInfo(restype, tablename, startTime, endTime,
						expressionName, hid_city_id, ifcontainChild);

	}

	public Cursor getPortFluxInfo(String restype, String tablename,
					String startTime, String endTime, String expressionName,
					String hcityid, String ifchild)
	{

		String[] _expressionName = expressionName.split(",");
		String tmp = "";
		String strExpressionName = "";
		String queryExpressionName = "";
		for (int i = 0; i < _expressionName.length; i++)
		{
			if (_expressionName[i].equals("u1"))
			{
				tmp = "ifinoctetsbps";
			}
			else if (_expressionName[i].equals("u2"))
			{
				tmp = "ifoutoctetsbps";
			}
			else if (_expressionName[i].equals("u3"))
			{
				tmp = "ifinoctetsbpsmax";
			}
			else if (_expressionName[i].equals("u4"))
			{
				tmp = "ifoutoctetsbpsmax";
			}
			else
			{
				tmp = _expressionName[i];
			}
			strExpressionName += tmp + ",";
		}
		queryExpressionName = strExpressionName.substring(0, strExpressionName
						.lastIndexOf(","));
		logger.debug("queryExpressionName------>" + queryExpressionName);
		String device_id = "";
		String ifindex = "";
		String ifportip = "";
		String ifname = "";
		String ifdescr = "";
		String ifnamedefined = "";
		String getway = "";
		String if_real_speed = "";
		String key = "";		

		//整理数据库中的设备端口流量配置信息
		String allDeviceId_tmp = this.getDeviceId(restype, hcityid, ifchild);
		String allDeviceId = allDeviceId_tmp.substring(0, allDeviceId_tmp
						.lastIndexOf(","));
		Cursor cursor_port_list = this.getPortInfo(restype, hcityid, ifchild);
		Map map_port_list = cursor_port_list.getNext();
		while (map_port_list != null)
		{
			device_id = (String) map_port_list.get("device_id");
			ifindex = (String) map_port_list.get("ifindex");
			ifportip = (String) map_port_list.get("ifportip");
			ifname = (String) map_port_list.get("ifname");
			ifdescr = (String) map_port_list.get("ifdescr");
			ifnamedefined = (String) map_port_list.get("ifnamedefined");
			getway = (String) map_port_list.get("getway");
			if_real_speed = (String) map_port_list.get("if_real_speed");						

			switch (Integer.parseInt(getway))
			{
				case 1:
					key = device_id + "/" + ifindex + "/" + getway;
					break;				
				case 2:
					key = device_id + "/" + ifdescr + "/" + getway;
					break;				
				case 3:
					key = device_id + "/" + ifname + "/" + getway;
					break;
				case 4:
					key = device_id + "/" + ifnamedefined + "/" + getway;
					break;
				case 5:
					key = device_id + "/" + ifportip + "/" + getway;
					break;
				default:
					key="";
				    break;
			}
			
			
			BasePortInfo bpi = new BasePortInfo();
			if("null".equalsIgnoreCase(ifindex))
			{
				ifindex="";
			}
			bpi.setIfindex(ifindex);			
			if("null".equalsIgnoreCase(ifdescr))
			{
				ifdescr="";
			}
			bpi.setIfdescr(ifdescr);
			if("null".equalsIgnoreCase(ifname))
			{
				ifname="";
			}
			bpi.setIfname(ifname);
			if("null".equalsIgnoreCase(ifportip))
			{
				ifportip="";
			}
			bpi.setIfportip(ifportip);
			if("null".equalsIgnoreCase(ifnamedefined))
			{
				ifnamedefined="";
			}
			bpi.setIfnamedefined(ifnamedefined);
			bpi.setIf_real_speed(if_real_speed);
			
			//gateway超过5的情况，这条设备端口丢弃
			if("".equals(key))
			{
				map_port_list = cursor_port_list.getNext();
				continue;
			}
			map_realSpeed.put(key, bpi);
			map_port_list = cursor_port_list.getNext();
		}
	    //CLEAR
		map_port_list = null;
		cursor_port_list = null;
		
		
		//整理流量报表中的端口流量数据，找到上面的key对应的流量数据
		String PortFluxInfoSQL = "select distinct(device_id) device_id,port_info,getway,? from ? "
			+ "where device_id in (?) and collecttime>=? and collecttime<? order by device_id,getway,port_info";
		pSQL.setSQL(PortFluxInfoSQL);
		pSQL.setStringExt(1, queryExpressionName, false);
		pSQL.setStringExt(2, tablename, false);
		pSQL.setStringExt(3, allDeviceId, false);		
		pSQL.setStringExt(4, startTime, false);
		pSQL.setStringExt(5, endTime, false);		
		logger.debug("getPortFluxInfo_SQL:" + pSQL.getSQL());
		
		Cursor fluxCursor = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("fluxCursor_size:"+fluxCursor.getRecordSize());
		Map fluxFields = fluxCursor.getNext();
		String portinfo="";
		Cursor resultCursor = new Cursor();	
		long time = new DateTimeUtil().getLongTime();
		while(null!=fluxFields)
		{
			device_id = (String) fluxFields.get("device_id");
			portinfo =(String)fluxFields.get("port_info");
			getway = (String)fluxFields.get("getway");
			if(map_realSpeed.containsKey(device_id+"/"+portinfo+"/"+getway))
			{
				resultCursor.add(fluxFields);
			}
			else
			{
				logger.debug(time+"  key:"+device_id+"/"+portinfo+"/"+getway+"  isn't find in flux_interfacedeviceport");
			}
			
			fluxFields = fluxCursor.getNext();			
		}
		
		
		//clear
		fluxCursor =null;	
		
		logger.debug("resultCursor_size:"+resultCursor.getRecordSize());
		return resultCursor;
	}
	
	public HashMap getMap_realSpeed() {
		return map_realSpeed;
	}

	/**
	 * 获取对应的device_id 核心：1,2 bas：3,4 汇聚：5,6,7,8,9
	 * 
	 * @param resourceType
	 * @return
	 */
	public String getDeviceId(String resourceType, String hcityid,
			String ifchild) {
		String deviceId = "";
		String resourceId = "";
		String cityId = "";
		SelectCityFilter scf = new SelectCityFilter();
		switch (Integer.parseInt(resourceType)) {
		case 1:
			resourceId = "1,2";
			break;
		case 2:
			resourceId = "5,6,7,8,9";
			break;
		case 3:
			resourceId = "3,4";
			break;
		}
		if (hcityid == null || hcityid.equals("") || hcityid.equals("00")) {
			cityId = "select city_id from tab_city";
		} else {
			if (ifchild.equals("1")) {
				cityId = scf.getAllSubCityIds(hcityid, true);
			} else {
				cityId = scf.getAllSubCityIds(hcityid, false);
			}
		}
		// logger.debug("属地级别："+cityId);
		String deviceIdSQL = "select device_id"
				+ " from tab_deviceresource where resource_type_id in("
				+ resourceId + ") and city_id in(" + cityId + ")";
		PrepareSQL psql = new PrepareSQL(deviceIdSQL);
    	psql.getSQL();
		Cursor cursorDeviceId = DataSetBean.getCursor(deviceIdSQL);
		Map fieldsDeviceId = cursorDeviceId.getNext();
		while (fieldsDeviceId != null) {
			deviceId += "'" + fieldsDeviceId.get("device_id") + "',";
			fieldsDeviceId = cursorDeviceId.getNext();
		}
		return deviceId;
	}

	/**
	 * 获取设备对应的端口信息
	 * 
	 * @param resType
	 * @return
	 */
	public Cursor getPortInfo(String resType, String hcityid, String ifchild) {
		String deviceId = this.getDeviceId(resType, hcityid, ifchild);
		String deviceIdtmp = deviceId.substring(0, deviceId.lastIndexOf(","));
		String portInfoSQL = "select device_id,ifindex,ifportip,ifname,ifdescr,ifnamedefined,getway,"
				+ "if_real_speed from flux_interfacedeviceport where device_id in("
				+ deviceIdtmp + ")";
		PrepareSQL psql = new PrepareSQL(portInfoSQL);
    	psql.getSQL();
		return DataSetBean.getCursor(portInfoSQL);
	}
}
