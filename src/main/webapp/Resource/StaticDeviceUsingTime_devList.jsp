<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
Map cityMapAll = DeviceAct.getCityMap_All();
Map userypeMapAll = DeviceAct.getUsertypeMap_All();
Map deviceTypeMapAll = DeviceAct.getDeviceTypeMap_All();

%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page
	import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil,com.linkage.litms.Global"%>
<%@page import="java.util.Map,java.util.List"%>
<%@ include file="../head.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	List list = DeviceAct.getDeviceListUseingTime(request);
	//Map fields = cursor.getNext();
	//List list = DeviceAct.getSheetList_bss(request);
	String strBar = String.valueOf(list.get(0));
	Cursor cursor = (Cursor) list.get(1);
	Map fields = cursor.getNext();
	String strData = "";
	String detailStr = "DetailDevice('?')";
	
	if (null != fields) {
		String complete_time = null;
		DateTimeUtil dateTimeUtil = null;
		String resultStr = null;
		String result = null;
		String device_id = null;
//		String cpe_currentstatus = null;
		while (null != fields) {
			String device_serialnumber = (String) fields.get("device_serialnumber");
			complete_time = (String) fields.get("complete_time");
			device_id = (String) fields.get("device_id");
			if (complete_time != null && !complete_time.equals("")) {
				dateTimeUtil = new DateTimeUtil(Long.parseLong(complete_time) * 1000);
				complete_time = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}
//			cpe_currentstatus = (String) fields.get("cpe_currentstatus");

//			cpe_currentstatus = cpe_currentstatus.equals("1") ? "在网" : "脱网";

			strData += "<TR align='center'>";

			strData += "<TD class=column>" + fields.get("oui")
					+ "</TD>";
			strData += "<TD class=column>" + device_serialnumber
					+ "</TD>";

			strData += "<TD class=column>"
					+ cityMapAll.get(fields.get("city_id")) + "</TD>";
			strData += "<TD class=column>"
					+ deviceTypeMapAll.get(fields.get("gw_type"))
					+ "</TD>";
			strData += "<TD class=column>"
				+ complete_time
				+ "</TD>";
			
			String strOper = "&nbsp;&nbsp;<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">详细信息</a>";
			
			strData += "<TD class=column>" + strOper + "</TD>";

			strData += "</TR>";
			fields = cursor.getNext();
		}
		strData += "<TR><TD class=column COLSPAN=10 align=right nowrap>" + strBar + "</TD></TR>";
	} else {
		strData = "<TR align=left><TD class=column colspan=10>系统没有符合条件的设备!</TD></TR>";
	}
%>
<SCRIPT LANGUAGE="JavaScript">
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</SCRIPT>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="ssidTable">
					<TR>
						<TH colspan="10">企业网关按时间统计信息</TH>
					</TR>
					<TR>
						<TH nowrap>设备OUI</TH>
						<TH nowrap>设备序列号</TH>
						<TH nowrap>属地</TH>
						<TH nowrap>设备类型</TH>
						<TH nowrap>首次连接时间</TH>
						<TH nowrap>详细信息</TH>
					</TR>
					<%=strData%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm NAME="childFrm" SRC=""
			STYLE="display: none"></IFRAME> &nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>

<%
	DeviceAct = null;
	//CLEAR
	fields = null;
	cursor = null;
%>








