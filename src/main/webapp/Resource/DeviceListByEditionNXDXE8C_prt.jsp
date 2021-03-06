<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");


response.setHeader("Content-disposition","attachment; filename=deviceEdition.xls" );

String strData = "";
String gw_type = request.getParameter("gw_type");
Cursor cursor = DeviceAct.getDeviceListByVenderExcelNXDXE8C(request);
Map fields = cursor.getNext();
Map area_Map = DeviceAct.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map cityPidMap = CityDAO.getCityIdPidMap();
Map venderMap = DeviceAct.getOUIDevMap(gw_type);
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?')";

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
    strData = "<TR><TD HEIGHT=30>??????????????????</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
	String city_parentname = null;
    while (fields != null) {
    	
    	devicetype_id = (String)fields.get("devicetype_id"); 
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
    	String loid=String.valueOf(fields.get("loid"));
    	String serv_account=String.valueOf(fields.get("serv_account"));
    	//????????MAC????
    	String cpe_mac = String.valueOf(fields.get("cpe_mac"));
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		city_parentname = (String)city_Map.get(cityPidMap.get(city_id));
		city_parentname = city_parentname == null ? "&nbsp;" : city_parentname;
		strData += "<TD>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		strData += "<TD>" + city_name + "</TD>";
		if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
		{
			strData += "<TD class=column2>" + city_parentname + "</TD>";
		}
		strData += "<TD align=left>" + (String)fields.get("device_serialnumber") + "</TD>";
		//strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//??????
		if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
		{
			strData += "<TD class=column2  >" +"&nbsp;"+ loid + "</TD>";//loid
		}else
		{
			if(!"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
			{
				strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//??????
			}
		}
		//strData += "<TD>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//??????
		if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
		{
			strData += "<TD class=column2  >" +"&nbsp;"+ loid + "</TD>";//loid
			strData += "<TD class=column2 >" +"&nbsp;"+ serv_account + "</TD>";//????????	
			strData += "<TD class=column2 >" +"&nbsp;"+ cpe_mac + "</TD>";//????MAC	
		}
		strData += "</TR>";
        fields = cursor.getNext();
    }
}
%>
<!-- <SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="Ja	vaScript">
</SCRIPT> -->
<HTML>


<HEAD>
<TITLE>????????????????</TITLE>


<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">


<style>

TD{

  FONT-FAMILY: "????", "Tahoma"; FONT-SIZE: 14px;

}

</style>


</HEAD>




<BODY>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td><b>????????????</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>????????</TH>
							<TH>????</TH>
							<th>????????</th>
							<TH>????</TH>
							<%
							if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
							<TH>????????</TH>
							<%} %>
							<TH>??????????</TH>
							<%
							if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
							<TH>LOID</TH>
							<%} %>
							
							<%
							if(!"nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && !"hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
							<TH>??????</TH>
							<%} %>
							<%
							if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
							<TH>LOID</TH>
							<TH>????????(IPTV????)</TH>
							<TH>????MAC</TH>
							<%} %>
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