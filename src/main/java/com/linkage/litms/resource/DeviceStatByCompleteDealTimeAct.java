
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;

/**
 * 根据用户上线的时间查询设备版本信息
 * 
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @since 2018年10月12日
 * @category com.linkage.litms.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceStatByCompleteDealTimeAct {

	private static Logger logger = LoggerFactory.getLogger(DeviceStatByCompleteDealTimeAct.class);
	private PrepareSQL pSQL;
	// 开始时间
	private String completeStartTime = "";
	// 结束时间
	private String completeEndTime = "";
	private String gw_type = "";
	// 版本ID
	private String device_model_id = "";
	// 厂商ID
	private String vendor_id = "";
	// 查询结果信息列表
	private List<Map> restartDevMap;
	private Cursor cursor = null;

	public DeviceStatByCompleteDealTimeAct() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 创建分组统计结果
	 * 
	 * @param request
	 * @return
	 */
	public String createResult(HttpServletRequest request) {
		// 开始时间
		completeStartTime = (String) request.getParameter("completeStartTime");
		// 结束时间
		completeEndTime = (String) request.getParameter("completeEndTime");
		gw_type = (String) request.getParameter("gw_type");
		logger.debug("开始查询:{}",completeStartTime);
		StringBuffer sql = new StringBuffer();
		sql.append(" select  count(t.device_id) as num,  b.vendor_add, b.vendor_id,a.device_model,a.device_model_id  from tab_gw_device t  ");
		sql.append(" left join gw_device_model a on a.device_model_id=t.device_model_id ");
		sql.append(" left join tab_vendor b on a.vendor_id=b.vendor_id  left join tab_hgwcustomer c on t.device_id=c.device_id ");
		sql.append(" where  t.device_type='e8-c' and t.cpe_allocatedstatus='1' ");
		// 判断时间
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sql.append(" and t.complete_time > '" + completeStartTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sql.append(" and t.complete_time < '" + completeEndTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sql.append(" and c.dealdate > '" + completeStartTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sql.append(" and c.dealdate < '" + completeEndTime + "' ");
		}
		sql.append(" group by a.device_model,b.vendor_add,b.vendor_id,a.device_model_id  having count(1)>200 order by vendor_add ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cursor = DataSetBean.getCursor(psql.toString());
		Map fields = null;
		fields = cursor.getNext();
		restartDevMap = new ArrayList();
		while (fields != null) {
			Map cityDevice = new HashMap();
			cityDevice.put("vendor_add", fields.get("vendor_add"));
			cityDevice.put("vendor_id", fields.get("vendor_id"));
			cityDevice.put("device_model", fields.get("device_model"));
			cityDevice.put("device_model_id", fields.get("device_model_id"));
			cityDevice.put("num", fields.get("num"));
			fields = cursor.getNext();
			restartDevMap.add(cityDevice);
		}
		fields = null;
		// 遍历
		logger.warn("restartDevMap:{}", restartDevMap);
		if (null == restartDevMap || restartDevMap.isEmpty()) {
			return "<TR><TD>系统中暂无任何业务!</TD></TR>";
		}
		// 输出表头
		StringBuffer sbTable = new StringBuffer();
		sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>数量</TH>");
		// 判断厂商有哪些,存储该厂商对应的型号
		Map<String, Map> vendorMap = new HashMap<String, Map>();
		// 存储同一厂商的数量，用于厂商的行判断
		Map<String, Integer> vendorSizeMap = new HashMap<String, Integer>();
		for (Map map : restartDevMap) {
			if (null == vendorSizeMap.get(StringUtil.getStringValue(map.get("vendor_add")))) {
				vendorSizeMap.put(StringUtil.getStringValue(map.get("vendor_add")), 1);
			}
			else {
				vendorSizeMap.put(StringUtil.getStringValue(map.get("vendor_add")),
						vendorSizeMap.get(map.get("vendor_add")) + 1);
			}
			Map<String, String> devicetypeMap = null;
			if (null == vendorMap.get(StringUtil.getStringValue(map.get("vendor_id"))+"#"+StringUtil.getStringValue(map.get("vendor_add")))) {
				devicetypeMap = new HashMap<String, String>();
			}
			else {
				devicetypeMap = vendorMap.get(StringUtil.getStringValue(map.get("vendor_id"))+"#"+StringUtil.getStringValue(map.get("vendor_add")));
			}
			// 存储型号+版本map
			devicetypeMap.put(StringUtil.getStringValue(map.get("device_model_id"))+"#"+StringUtil.getStringValue(map.get("device_model")), (String) map.get("num"));
			// 存储厂商+版本map
			vendorMap.put(StringUtil.getStringValue(map.get("vendor_id"))+"#"+StringUtil.getStringValue(map.get("vendor_add")), devicetypeMap);
		}
		// 拼接
		int vendor_i = 0;
		for (Entry<String, Map> entryS : vendorMap.entrySet()) {
			// 某个厂商size
			vendor_i += 1;
			int vendorsize = entryS.getValue().size();
			Integer vendorIntSize = vendorSizeMap.get(entryS.getKey().split("#")[1]);
			sbTable.append("<tr>");
			sbTable.append("<td rowspan=" + vendorIntSize + ">");
			sbTable.append(entryS.getKey().split("#")[1]);
			sbTable.append("</td>");
			Map<String, Map> devicetype = entryS.getValue();
			int device_i = 0;
			for (Entry<String, Map> entryd : devicetype.entrySet()) {
				sbTable.append("<td>");
				sbTable.append(entryd.getKey().split("#")[1]);
				sbTable.append("</td>");
				String vendor_id = entryS.getKey().split("#")[0];
				String device_model_id = entryd.getKey().split("#")[0];
				sbTable.append("<td><a href=javascript:onclick=detail('"
		+vendor_id+"','"+device_model_id+"','"+ completeStartTime + "','"+completeEndTime+ "','"+ gw_type + "') >" + entryd.getValue() + "</a>");
				sbTable.append("</td>");
				sbTable.append("</tr>");
			}
		}
		logger.debug("生成结束：{}",sbTable.toString());
		return sbTable.toString();
	}
	
	public List showDetail(HttpServletRequest request){
		// 开始时间
		logger.warn("开始查询明细");
		completeStartTime = (String) request.getParameter("completeStartTime");
		
		// 结束时间
		completeEndTime = (String) request.getParameter("completeEndTime");
		
		
		gw_type = (String) request.getParameter("gw_type");
		if(StringUtil.IsEmpty(gw_type)){
			gw_type = "1";
		}
		// 版本ID
		device_model_id = (String) request.getParameter("device_model_id");
		// 厂商ID
		vendor_id = (String) request.getParameter("vendor_id");
		
		String fileter = "";
		StringBuffer sqldetail = new StringBuffer();
		sqldetail.append(" select  t.device_id,  b.vendor_add, b.vendor_id,a.device_model,a.device_model_id,t.device_id,t.city_id,t.devicetype_id,p.softwareversion,t.device_serialnumber from tab_gw_device t  ");
		sqldetail.append(" left join gw_device_model a on a.device_model_id=t.device_model_id ");
		sqldetail.append(" left join tab_vendor b on a.vendor_id=b.vendor_id left join tab_devicetype_info p on p.devicetype_id = t.devicetype_id left join tab_hgwcustomer c on t.device_id=c.device_id ");
		sqldetail.append(" where  t.device_type='e8-c' and t.cpe_allocatedstatus='1' ");
		if (!StringUtil.IsEmpty(device_model_id)) {
			sqldetail.append(" and t.device_model_id = '" + device_model_id +"' ");
			fileter = fileter + "&device_model_id=" + device_model_id;
		}
		if (!StringUtil.IsEmpty(vendor_id)) {
			sqldetail.append(" and t.vendor_id = '" + vendor_id +"' ");
			fileter = fileter + "&vendor_id=" + vendor_id;
		}
		// 判断时间
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sqldetail.append(" and t.complete_time > '" + completeStartTime + "' ");
			fileter = fileter + "&completeStartTime=" + completeStartTime;
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sqldetail.append(" and t.complete_time < '" + completeEndTime + "' ");
			fileter = fileter + "&completeEndTime=" + completeEndTime;
		}
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sqldetail.append(" and c.dealdate > '" + completeStartTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sqldetail.append(" and c.dealdate < '" + completeEndTime + "' ");
		}
		sqldetail.append(" order by vendor_add ");
		// 分页
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		String sqlDevice = sqldetail.toString();
		ArrayList list = new ArrayList();
		list.clear();
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		this.cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar); 
		list.add(this.cursor);
		return list;
	}
	
	public Cursor getDetailExcel(HttpServletRequest request){
		// 开始时间
		logger.warn("开始导出明细");
		completeStartTime = (String) request.getParameter("completeStartTime");
		// 结束时间
		completeEndTime = (String) request.getParameter("completeEndTime");
		
		gw_type = (String) request.getParameter("gw_type");
		if(StringUtil.IsEmpty(gw_type)){
			gw_type = "1";
		}
		// 版本ID
		device_model_id = (String) request.getParameter("device_model_id");
		// 厂商ID
		vendor_id = (String) request.getParameter("vendor_id");
		
		StringBuffer sqldetail = new StringBuffer();
		sqldetail.append(" select  t.device_id,  b.vendor_add, b.vendor_id,a.device_model,a.device_model_id,t.device_id,t.city_id,t.devicetype_id,p.softwareversion,t.device_serialnumber from tab_gw_device t  ");
		sqldetail.append(" left join gw_device_model a on a.device_model_id=t.device_model_id ");
		sqldetail.append(" left join tab_vendor b on a.vendor_id=b.vendor_id left join tab_devicetype_info p on p.devicetype_id = t.devicetype_id left join tab_hgwcustomer c on t.device_id=c.device_id ");
		sqldetail.append(" where  t.device_type='e8-c' and t.cpe_allocatedstatus='1' ");
		if (!StringUtil.IsEmpty(device_model_id)) {
			sqldetail.append(" and t.device_model_id = '" + device_model_id +"' ");
		}
		if (!StringUtil.IsEmpty(vendor_id)) {
			sqldetail.append(" and t.vendor_id = '" + vendor_id +"' ");
		}
		// 判断时间
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sqldetail.append(" and t.complete_time > '" + completeStartTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sqldetail.append(" and t.complete_time < '" + completeEndTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeStartTime)) {
			sqldetail.append(" and c.dealdate > '" + completeStartTime + "' ");
		}
		if (!StringUtil.IsEmpty(completeEndTime)) {
			sqldetail.append(" and c.dealdate < '" + completeEndTime + "' ");
		}
		sqldetail.append(" order by vendor_add ");
		String sqlDevice = sqldetail.toString();
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		this.cursor = DataSetBean.getCursor(
				sqlDevice, DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		return cursor;
	}
}
