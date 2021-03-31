package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
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
 * 江西电信ITMS 家庭网关光猫协商速率不匹配月变化报表 业务操作相关类 与 tab_lan_speed_report,
 * tab_batch_gather_lan_speed 相关
 */
public class LanSpeedReportAct {
	
	private Cursor cursor = null;
	private Map fields = null;
	private PrepareSQL pSQL;
	
	private String lanSpeedChangeSql = " select bb.city_name,count(bb.city_name) num from (select distinct b.device_id,b.city_name " +
			" from tab_batch_gather_lan_speed a,tab_lan_speed_report b,tab_hgwcustomer c,hgwcust_serv_info d,tab_netacc_spead e " +
			" where a.device_id=b.device_id and a.device_id=c.device_id and d.user_id=c.user_id and b.username=c.username and d.serv_type_id=10 " +
			" and a.max_bit_rate!='Auto' and a.max_bit_rate is not null and to_number(replace(a.max_bit_rate,'M',''))>100 " +
			" and d.username=e.username and e.DOWNLINK>100 " +
			" and to_number(b.MAX_BIT_RATE)<=100 and a.GATHER_TIME >= ? and b.gather_time >= ? and b.gather_time <= ?) bb group by bb.city_name ";
	
	
	public LanSpeedReportAct() 
	{
		pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			lanSpeedChangeSql = " select bb.city_name,count(bb.city_name) num from (select distinct b.device_id,b.city_name " +
					" from tab_batch_gather_lan_speed a,tab_lan_speed_report b,tab_hgwcustomer c,hgwcust_serv_info d,tab_netacc_spead e " +
					" where a.device_id=b.device_id and a.device_id=c.device_id and d.user_id=c.user_id and b.username=c.username and d.serv_type_id=10 " +
					" and a.max_bit_rate!='Auto' and a.max_bit_rate is not null and CAST(replace(a.max_bit_rate,'M','') AS SIGNED INTEGER)>100 " +
					" and d.username=e.username and e.DOWNLINK>100 " +
					" and CAST(b.MAX_BIT_RATE AS SIGNED)<=100 and a.GATHER_TIME >= ? and b.gather_time >= ? and b.gather_time <= ?) bb group by bb.city_name ";
		}
	}

	/**
	 * 家庭网关光猫协商速率不匹配月变化数据
	 * 
	 * @return cursor
	 */
	public Cursor getLanSpeedChange() 
	{
		Map timesMap = getTimes();
		pSQL.setSQL(lanSpeedChangeSql);
		pSQL.setLong(1, StringUtil.getLongValue(timesMap.get("time1")));
		pSQL.setLong(2, StringUtil.getLongValue(timesMap.get("time2")));
		pSQL.setLong(3, StringUtil.getLongValue(timesMap.get("time3")));
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public Map getTimes()
	{
		String timeSql = " select to_char((SELECT TO_NUMBER(TO_DATE(TO_CHAR(SYSDATE,'YYYY-MM')||'-01 00:00:00','YYYY-MM-DD HH24:MI:SS')-TO_DATE('1970-01-01 8:0:0','YYYY-MM-DD HH24:MI:SS')) FROM DUAL)*24*60*60) as time1, " + 
				" to_char((SELECT TO_NUMBER(TO_DATE(TO_CHAR(add_months(sysdate,-1),'YYYY-MM')||'-01 00:00:00','YYYY-MM-DD HH24:MI:SS')-TO_DATE('1970-01-01 8:0:0','YYYY-MM-DD HH24:MI:SS')) FROM DUAL)*24*60*60) as time2, " + 
		        " to_char((SELECT TO_NUMBER(TO_DATE(TO_CHAR(LAST_DAY(add_months(sysdate,-1)),'YYYY-MM-DD')||' 23:59:59','YYYY-MM-DD HH24:MI:SS')-TO_DATE('1970-01-01 8:0:0','YYYY-MM-DD HH24:MI:SS')) FROM DUAL)*24*60*60) as time3 from dual ";
				
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			timeSql = " select UNIX_TIMESTAMP(date_sub(date_sub(date_format(now(),'%y-%m-%d 00:00:00'),interval extract( day from now())-1 day),interval 0 month)) as time1, " +
					" UNIX_TIMESTAMP(date_sub(date_sub(date_format(now(),'%y-%m-%d 00:00:00'),interval extract( day from now())-1 day),interval 1 month)) as time2, " +
					" UNIX_TIMESTAMP(date_sub(date_sub(date_format(now(),'%y-%m-%d 00:00:00'),interval extract( day from now())-1 day),interval 0 month)) as time3 from DUAL";
		}
		PrepareSQL psql = new PrepareSQL(timeSql);
		
		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 家庭网关光猫协商速率不匹配月变化数据统计
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlLanSpeedChange(HttpServletRequest request) 
	{
		StringBuffer sbTable = new StringBuffer();
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH nowrap>数量</TH>");
		sbTable.append("</TR>");

		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0) 
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getLanSpeedChange();
		fields = cursor.getNext();
		Map mapCityNum = new HashMap();
		while (fields != null) 
		{
			// 将业务+属地==>num 作为映射存入map中
			mapCityNum.put(fields.get("city_name"), fields.get("num"));
			fields = cursor.getNext();
		}
		Cursor cityList = getCityList(request);
		Map cityField = cityList.getNext();

		while (cityField != null) 
		{
			sbTable.append(getSerStatePrint((String) cityField.get("city_id"), cityMap, mapCityNum));
			cityField = cityList.getNext();
		}
		int total = StringUtil.getIntegerValue(mapCityNum.get("total"));
		String jsStr = "detail('?')";
		if (total == 0) 
		{
			sbTable.append("<TR ><TD nowrap class=column align='center'>合计</TD><TD bgcolor=#ffffff align='center'>0</TD></TR>");
		}
		else
		{
			sbTable.append("<TR ><TD nowrap class=column align='center'>合计</TD>" +
					"<TD bgcolor=#ffffff align=center><a href=javascript:onclick=" + jsStr.replaceAll("\\?", "all")	+ ">" + total + "</a></TD></TR>");
		}
		
		mapCityNum.clear();
		mapCityNum = null;
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
	 * @param mapCityNum
	 * @return
	 */
	private String getSerStatePrint(String city_id, Map cityMap, Map mapCityNum) 
	{
		String num = null;
		StringBuffer sb = new StringBuffer();
		String jsStr = "detail('?')";

		// 输出地市名称
		sb.append("<TR ><TD nowrap class=column align='center'>" + cityMap.get(city_id) + "</TD>");
		if (mapCityNum != null) 
		{
			num = (String) mapCityNum.get(cityMap.get(city_id));
			if (num != null && !"0".equals(num))
				sb.append("<TD bgcolor=#ffffff align=center><a href=javascript:onclick=" + jsStr.replaceAll("\\?", city_id)	+ ">" + num + "</a></TD>");
			else
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		} 
		else 
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		
		
		if (mapCityNum.containsKey("total")) 
		{
			mapCityNum.put("total", (StringUtil.getIntegerValue(num) + StringUtil.getIntegerValue(mapCityNum.get("total"))));
		}
		else {
			mapCityNum.put("total", StringUtil.getIntegerValue(num));
		}
		
		return sb.toString();
	}

	/**
	 * 家庭网关光猫协商速率不匹配月变化数据统计 Xls
	 * 
	 * @param request
	 * @return
	 */
	public String getHtmlLanSpeedChangeXls(HttpServletRequest request) 
	{
		StringBuffer sbTable = new StringBuffer();
		sbTable.append("<TR>");
		sbTable.append("<TH width=\"120\" nowrap>地市</TH><TH nowrap>数量</TH>");
		sbTable.append("</TR>");

		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0) 
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getLanSpeedChange();
		fields = cursor.getNext();
		Map mapCityNum = new HashMap();
		while (fields != null) 
		{
			mapCityNum.put(fields.get("city_name"), fields.get("num"));
			fields = cursor.getNext();
		}
		Cursor cityList = getCityList(request);
		Map cityField = cityList.getNext();

		while (cityField != null) 
		{
			sbTable.append(getSerStatePrintXls((String) cityField.get("city_id"), cityMap, mapCityNum));
			cityField = cityList.getNext();
		}
		
		sbTable.append("<TR ><TD nowrap class=column align='center'>合计</TD><TD bgcolor=#ffffff align=center>" + StringUtil.getIntegerValue(mapCityNum.get("total")) + "</TD></TR>");
		
		mapCityNum.clear();
		mapCityNum = null;
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
	private String getSerStatePrintXls(String city_id, Map cityMap, Map mapCityNum) 
	{
		ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String num = null;
		StringBuffer sb = new StringBuffer();
		// 输出地市名称
		sb.append("<TR ><TD nowrap align='center'>" + cityMap.get(city_id) + "</TD>");
		if (mapCityNum != null) 
		{
			num = (String) mapCityNum.get(cityMap.get(city_id));
			if (num != null && !"0".equals(num))
				sb.append("<TD bgcolor=#ffffff align=center>" + num + "</TD>");
			else
				sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		} 
		else if (mapCityNum == null) 
		{
			sb.append("<TD bgcolor=#ffffff align=center>0</TD>");
		}
		
		if (mapCityNum.containsKey("total")) 
		{
			mapCityNum.put("total", (StringUtil.getIntegerValue(num) + StringUtil.getIntegerValue(mapCityNum.get("total"))));
		}
		else {
			mapCityNum.put("total", StringUtil.getIntegerValue(num));
		}
		
		cityAll = null;
		sb.append("</TR>");
		return sb.toString();
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
			m_cityList = "select city_id,city_name from tab_city where parent_id in(" + StringUtils.weave(list) + ") or city_id in(" + StringUtils.weave(list) + ") order by city_id";
		} 
		else 
		{
			m_cityList = "select city_id,city_name from tab_city where parent_id='" + city_id + "' or city_id='" + city_id + "' order by city_id";
		}
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.getSQL();
		return DataSetBean.getCursor(m_cityList);
	}

}
