package com.linkage.litms.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;

public class LanSpeedReportDeviceAct
{
	private PrepareSQL pSQL = null;
	private Cursor cursor = null;
	private Map fields = null;
	
	/**
	 * 机顶盒厂商列表
	 */
	private String stb_m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from stb_tab_vendor";

	/**
	 * 厂商列表
	 */
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
	
	
	/**
     * 导出数据
     */
    private List<Map<String, Object>> data;
    /**
     * 导出文件列标题
     */
    private String[] title;
    /**
     * 导出文件列
     */
    private String[] column;
    /**
     * 导出文件名
     */
    private String fileName;
    
	// 属地id
	private String cityId;
	// 查询开始时间
	private String starttime;
	// 查询结束时间
	private String endtime;
	// 家庭网关类型
	private String gwType;
	
	private List<Map<String, Object>> dataList;
	
	String sqlDevice = " select distinct a.DEVICE_ID,a.LAN_PORT,a.MAX_BIT_RATE,a.STATUS,a.GATHER_TIME,b.city_name,c.username,e.vendor_name,f.device_model, " +
			" kk.gbbroadband,d.device_serialnumber,dd.wan_type,ee.DOWNLINK,dd.username netusername " +
			" from tab_batch_gather_lan_speed a,tab_lan_speed_report b,tab_hgwcustomer c,tab_gw_device d,tab_vendor e,gw_device_model f,tab_device_version_attribute kk,hgwcust_serv_info dd,tab_netacc_spead ee " +
			" where a.device_id=b.device_id and a.max_bit_rate!='Auto' and a.max_bit_rate is not null and to_number(replace(a.max_bit_rate,'M',''))>100 and b.username=c.username and " +
			" a.device_id=c.device_id and c.device_id=d.device_id and dd.user_id=c.user_id and f.VENDOR_ID=e.VENDOR_ID and " +
			" dd.username=ee.username and dd.serv_type_id=10 and ee.DOWNLINK>100 and " +
			" d.DEVICE_MODEL_ID=f.DEVICE_MODEL_ID  and kk.DEVICETYPE_ID=d.DEVICETYPE_ID  and to_number(b.MAX_BIT_RATE)<=100 and " +
			" a.GATHER_TIME >= ? and b.gather_time >= ? and b.gather_time <= ? ";
	
	/**
	 * 构造函数
	 */
	public LanSpeedReportDeviceAct()
	{
		pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = " select distinct a.DEVICE_ID,a.LAN_PORT,a.MAX_BIT_RATE,a.STATUS,a.GATHER_TIME,b.city_name,c.username,e.vendor_name,f.device_model, " +
					" kk.gbbroadband,d.device_serialnumber,dd.wan_type,ee.DOWNLINK,dd.username netusername " +
					" from tab_batch_gather_lan_speed a,tab_lan_speed_report b,tab_hgwcustomer c,tab_gw_device d,tab_vendor e,gw_device_model f,tab_device_version_attribute kk,hgwcust_serv_info dd,tab_netacc_spead ee " +
					" where a.device_id=b.device_id and a.max_bit_rate!='Auto' and a.max_bit_rate is not null and CAST(replace(a.max_bit_rate,'M','') AS SIGNED)>100 and b.username=c.username and " +
					" a.device_id=c.device_id and c.device_id=d.device_id and dd.user_id=c.user_id and f.VENDOR_ID=e.VENDOR_ID and " +
					" dd.username=ee.username and dd.serv_type_id=10 and ee.DOWNLINK>100 and " +
					" d.DEVICE_MODEL_ID=f.DEVICE_MODEL_ID  and kk.DEVICETYPE_ID=d.DEVICETYPE_ID  and CAST(b.MAX_BIT_RATE AS SIGNED)<=100 and " +
					" a.GATHER_TIME >= ? and b.gather_time >= ? and b.gather_time <= ? ";
		}
	}

	/**
	 * 取当前时间(秒数)
	 */
	DateTimeUtil dtu = new DateTimeUtil();
	long nowTime = dtu.getLongTime();

	
	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoListByService(HttpServletRequest request, int gw_type)
	{
		ArrayList list = new ArrayList();
		Map cityIdMapName = getCityIdMapName();
		String city_name = (String) cityIdMapName.get(request.getParameter("city_id"));
		Map timesMap = getTimes();
		
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.setLong(1, StringUtil.getLongValue(timesMap.get("time1")));
		psql.setLong(2, StringUtil.getLongValue(timesMap.get("time2")));
		psql.setLong(3, StringUtil.getLongValue(timesMap.get("time3")));
		if (!StringUtil.IsEmpty(city_name)) {
			psql.append(" and b.city_name='" + city_name + "'");
		}
		
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		
		QueryPage qryp = new QueryPage();
		qryp.initPage(psql.getSQL(), offset, pagelen);
		cursor = DataSetBean.getCursor(psql.getSQL(), offset, pagelen);
		String strBar = qryp.getPageBar("&gw_type=" + gw_type + "&city_id=" + request.getParameter("city_id"));
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	
	/**
	 * 获取属地与名称映射关系
	 * 
	 * @return
	 */
	public Map getCityIdMapName()
	{
		Map result = new HashMap();
		PrepareSQL psql = new PrepareSQL(" select city_id, city_name from tab_city ");
		cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		while (fields != null)
		{
			result.put(fields.get("city_id"), fields.get("city_name"));
			fields = cursor.getNext();
		}
		return result;
	}
	
	/**
	 * 家庭网关光猫协商速率不匹配月变化数据统计 Xls
	 * 
	 * @param request
	 * @return
	 */
	public String toEXcel(HttpServletRequest request, int gw_type) 
	{
		Map cityIdMapName = getCityIdMapName();
		String city_name = (String) cityIdMapName.get(request.getParameter("city_id"));
		Map timesMap = getTimes();
		
		StringBuffer sbTable = new StringBuffer();
		
		sbTable.append("<TR>");
		sbTable.append("<TH nowrap>地市</TH><TH nowrap>LAN口当前速率</TH><TH nowrap>签约速率</TH><TH nowrap>LAN速率采集时间</TH><TH nowrap>厂家</TH>" +
				       "<TH nowrap>DEVICE_MODEL</TH><TH nowrap>设备序列号</TH><TH nowrap>LOID</TH><TH nowrap>KD</TH><TH nowrap>宽带模式</TH>");
		sbTable.append("</TR>");

		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap(request);
		if (cityMap.size() == 0) 
		{
			return "<TR bgcolor='#FFFFFF'><TD align='center'class=column>系统中暂无属地数据!</TD></TR>";
		}
		devAct = null;
		cursor = getLanSpeedChange(sqlDevice, city_name, timesMap);
		fields = cursor.getNext();
		Map mapCityNum = new HashMap();
		
		while (fields != null) 
		{
			sbTable.append(getSerStatePrintXls(fields));
			
			fields = cursor.getNext();
		}
		mapCityNum.clear();
		mapCityNum = null;
		cityMap = null;
		cursor = null;
		fields = null;
		return sbTable.toString();
	}
	
	/**
	 * 家庭网关光猫协商速率不匹配月变化数据
	 * 
	 * @return cursor
	 */
	public Cursor getLanSpeedChange(String sqlDevice, String city_name, Map timesMap) 
	{
		pSQL.setSQL(sqlDevice);
		pSQL.setLong(1, StringUtil.getLongValue(timesMap.get("time1")));
		pSQL.setLong(2, StringUtil.getLongValue(timesMap.get("time2")));
		pSQL.setLong(3, StringUtil.getLongValue(timesMap.get("time3")));
		if (!StringUtil.IsEmpty(city_name)) {
			pSQL.append(" and b.city_name='" + city_name + "'");
		}
		return DataSetBean.getCursor(pSQL.getSQL());
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
	private String getSerStatePrintXls(Map fields) 
	{
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long date_temp = Long.valueOf((String)fields.get("gather_time"));  
		String gather_time = sdf.format(new Date(date_temp * 1000L));  

		String max_bit_rate = (String)fields.get("max_bit_rate"); 
		String downlink = (String)fields.get("downlink");
		String city_name = (String)fields.get("city_name");
		String vendor_name = (String)fields.get("vendor_name"); 
		String device_model = (String)fields.get("device_model"); 
		String gbbroadband = "1".equals((String)fields.get("gbbroadband")) ? "是" : "否"; 
		String device_serialnumber = (String)fields.get("device_serialnumber");
		String wan_type = "1".equals((String)fields.get("wan_type")) ? "桥接" : ("2".equals((String)fields.get("wan_type")) ? "路由" : "其他"); 
		String username = (String)fields.get("username"); 
		String netusername = (String)fields.get("netusername"); 
		
		sb.append("<TR><TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + city_name + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + max_bit_rate + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + downlink + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + gather_time + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + vendor_name + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + device_model + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + device_serialnumber + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + username + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + netusername + "</TD>");
		sb.append("<TD class=column2 style=\"vnd.ms-excel.numberformat:@\">" + wan_type + "</TD>");
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
	
	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap(String gw_type)
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		if ("4".equals(gw_type))
		{
			cursor = DataSetBean.getCursor(stb_m_Vendor_SQL);
		}
		else
		{
			cursor = DataSetBean.getCursor(m_Vendor_SQL);
		}
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				String ouiName = (String) fields.get("vendor_add");
				if (ouiName != null && !"".equals(ouiName))
				{
					ouiMap.put((String) fields.get("vendor_id"), ouiName);
				}
				else
				{
					ouiMap.put((String) fields.get("vendor_id"),
							(String) fields.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
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


	
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}
	
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
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

}
