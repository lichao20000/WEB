<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.util.CommonCursor"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%--
生成下拉框
@desc:webtopo中查询设备
@author : Hemc
@date :2006-12-19
--%>
<%
	Cursor cursor = null;
	Map fields = null;
	StringBuffer sb =  new StringBuffer();
	// type 0:厂商 1:设备类型 2:设备数据
	String type = request.getParameter("type");
	type = type == null ? "0" : type;
	//设备厂商
	if(type.equals("0")){
		cursor = CommonCursor.getVendorCursor();
		fields = cursor.getNext();
		sb.append("<select id='_VendorType' class='column' onChange='changeVendorType(this.value)'>");
 		sb.append("<option value='-1'>选择厂商</option>");
		while(fields != null){
			String vendor_add = (String)fields.get("vendor_add");
			if (vendor_add != null && !"".equals(vendor_add)){
				sb.append("<option value='" + fields.get("vendor_id") + "'>" + vendor_add + "(" + fields.get("vendor_id") + ")</option>");
			}
			else {
				sb.append("<option value='" + fields.get("vendor_id") + "'>" + fields.get("vendor_name") + "(" + fields.get("vendor_id") + ")</option>");
			}
			
			fields = cursor.getNext();
		}
		sb.append("</select>");
	}
	//设备序列号
	else if(type.equals("1")){
		String vendor_id = request.getParameter("vendor_id");
		DeviceAct act = new DeviceAct();
		cursor = act.getDeviceSerailByOUI(vendor_id);
		fields = cursor.getNext();
		sb.append("<select class='column' onChange='changeDeviceType(this.value)'>");
		sb.append("<option value='-1'>选择设备序列号</option>");
		while(fields != null){
			sb.append("<option value='" + fields.get("device_serialnumber") + "'>" + fields.get("device_serialnumber") + "</option>");
			fields = cursor.getNext();
		}
		sb.append("</select>");
	}else if(type.equals("2")){
		String device_model = request.getParameter("device_model");
		long area_id = user.getAreaId();
		String sqlRes = "select res_id from tab_gw_res_area where res_type=1 and area_id=" + area_id;
		String sqlDev = "select a.device_id,a.device_name,a.loopback_ip from tab_gw_device a where a.device_id in (" + sqlRes + " ) and a.device_model='" + device_model + "'";
		cursor = DataSetBean.getCursor(sqlDev);
		fields = cursor.getNext();
		sb.append("<select class='column' onChange='findDevObjLocation(this.value)'>");
		sb.append("<option value='-1'>选择设备</option>");
		while(fields != null){
			sb.append("<option value='1/gw/" + fields.get("device_id") + "'>" + fields.get("device_name") + "</option>");
			fields = cursor.getNext();
		}
		sb.append("</select>");
	}
	else if(type.equals("3"))
	{
		cursor = CommonCursor.getVendorCursor();
		fields = cursor.getNext();
		sb.append("<select id='_VendorType' class='column' onChange='relateDeviceModel(this.value)'>");
 		sb.append("<option value='-1'>选择厂商</option>");
		while(fields != null){
			String vendor_add = (String)fields.get("vendor_add");
			if (vendor_add != null && !"".equals(vendor_add)){
				sb.append("<option value='" + fields.get("vendor_id") + "'>" + vendor_add + "(" + fields.get("vendor_id") + ")</option>");
			}
			else {
				sb.append("<option value='" + fields.get("vendor_id") + "'>" + fields.get("vendor_name") + "(" + fields.get("vendor_id") + ")</option>");
			}
			
			fields = cursor.getNext();
		}
		sb.append("</select>");
	}
	else if(type.equals("4"))
	{
		String oui = request.getParameter("vendor_id");
		DeviceAct act = new DeviceAct();
		cursor = act.getDeviceModelByOUI(oui);
		fields = cursor.getNext();
		sb.append("<select  class='column' onChange='relateDevice(this.value)'>");
 		sb.append("<option value='-1'>选择设备型号</option>");
		while(fields != null){
			sb.append("<option value='" + fields.get("device_model") + "'>" + fields.get("device_model") + "</option>");
			fields = cursor.getNext();
		}
		sb.append("</select>");
	}
	out.print(sb.toString());
%>
