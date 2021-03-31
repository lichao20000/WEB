<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map,java.util.HashMap"%>
<jsp:useBean id="DeviceActAhdx" scope="request" class="com.linkage.litms.resource.DeviceActAhdx"/>
<jsp:useBean id="DeviceStatByCompleteDealTimeAct" scope="request"	class="com.linkage.litms.resource.DeviceStatByCompleteDealTimeAct" />
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=deviceEdition.xls" );
//修改
String gw_type = request.getParameter("gw_type");
String completeStartTime = request.getParameter("completeStartTime");
String dealStartTime =  request.getParameter("dealStartTime");
// 结束时间
String completeEndTime = request.getParameter("completeEndTime");
String dealEndTime = request.getParameter("dealEndTime");
String vendor_id = request.getParameter("vendor_id");//厂商
String device_model_id = request.getParameter("device_model_id");//型号
if(null==completeStartTime || "null".equals(completeStartTime)){
	completeStartTime = "";
}
if(null==completeEndTime || "null".equals(completeEndTime)){
	completeEndTime = "";
}
if(null==dealStartTime || "null".equals(dealStartTime)){
	dealStartTime = "";
}
if(null==dealEndTime || "null".equals(dealEndTime)){
	dealEndTime = "";
}
String is_normal=request.getParameter("is_normal");
if(null==is_normal || "null".equals(is_normal)){
	is_normal="0";
}
//导出
String prt_Page="DeviceListByCompleteDealTime_prt.jsp?vendor_id="+ vendor_id+"&device_model_id="+device_model_id+"&gw_type="+gw_type+"&completeStartTime="+completeStartTime+"&dealStartTime="+dealStartTime+"&completeEndTime="+completeEndTime+"&dealEndTime="+dealEndTime;

String strData = "";
Cursor cursor = DeviceStatByCompleteDealTimeAct.getDetailExcel(request);
Map fields = cursor.getNext();
Map area_Map = DeviceActAhdx.getAreaIdMapName();
Map city_Map = CityDAO.getCityIdCityNameMap();
Map cityPidMap = CityDAO.getCityIdPidMap();
Map venderMap = DeviceActAhdx.getOUIDevMap(gw_type);
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?','"+gw_type+"')";
//add by benyp
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = new HashMap();
Cursor cursorTmp;
if("4".equals(gw_type)){
	String sql1 = "select * from stb_tab_devicetype_info a, stb_gw_device_model b where a.device_model_id = b.device_model_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql1 = "select device_model, softwareversion, devicetype_id from stb_tab_devicetype_info a, stb_gw_device_model b where a.device_model_id = b.device_model_id";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
	psql.getSQL();
	cursorTmp = DataSetBean.getCursor(sql1);
}else{
	String sql2 = "select * from tab_devicetype_info a, gw_device_model b where a.device_model_id = b.device_model_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql2 = "select device_model, softwareversion, devicetype_id from tab_devicetype_info a, gw_device_model b where a.device_model_id = b.device_model_id";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql2);
	psql.getSQL();
	cursorTmp = DataSetBean.getCursor(sql2);
}

		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null){
			devicemodel = (String)fieldTmp.get("device_model");
			softwareversion = (String)fieldTmp.get("softwareversion");
			devicetype_id = (String)fieldTmp.get("devicetype_id");
			deviceTypeMap.put(devicetype_id,devicemodel+","+softwareversion);
			fieldTmp = cursorTmp.getNext();
		}



if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
	String city_parentname = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		city_parentname = (String)city_Map.get(cityPidMap.get(city_id));
		city_parentname = city_parentname == null ? "&nbsp;" : city_parentname;
		devicetype_id = (String)fields.get("devicetype_id"); 
		//-1为设备型号是“请选择”状态（设备导入时给的默认值）
		devicemodel = "";
		softwareversion = "";
		if(!devicetype_id.equals("-1")){
		
			String tmp = (String)deviceTypeMap.get(devicetype_id);
			if(tmp.split(",").length>1){
			if(tmp != "" && null != tmp){
				String[] aa = tmp.split(",");
				devicemodel = aa[0];
				softwareversion = aa[1];
			}}
			else
			{
				if(tmp != "" && null != tmp){
					String[] aa = tmp.split(",");
					devicemodel = aa[0];
					softwareversion = "";
				}
			}
		}
		strData += "<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		
        fields = cursor.getNext();
    }
}
%>
<HTML>


<HEAD>
<TITLE>设备统计导入数据</TITLE>


<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">


<style>

TD{

  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;

}

</style>


</HEAD>




<BODY>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td><b>设备资源列表</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>设备厂商</TH>
							<TH>型号</TH>
							<th>软件版本</th>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>管理域</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
</TD></TR>
</TABLE>
</BODY>
</HTML>