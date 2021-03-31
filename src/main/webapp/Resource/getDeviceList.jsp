<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
List list = DeviceAct.getDeviceInfoList(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map city_Map = DeviceAct.getCityMap(request);
String strData = "";
if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String city_id = null;
	String city_name = null;
	String device_id = null;
	while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		strData += "<TR><TD class=column2 align='center' ><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		strData += "<TD class=column2 width=240 nowrap>" + (String)fields.get("device_name") + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("oui") + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "</TR>";
		fields = cursor.getNext();
	}
}
%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
   <TR><TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH width=60><input type=checkbox onclick="SelectAll(this,'chkCheck')">全选</TH>
							<TH>设备名称</TH>
							<TH>OUI</TH>
							<TH>属地</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
   </TD></TR>
</TABLE> 
<%
DeviceAct = null;
cursor = null;
fields = null;
if(city_Map != null)
	city_Map.clear();
city_Map = null;
%>