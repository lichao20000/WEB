<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.Map"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
String start_day=request.getParameter("start_day");
String start_time=request.getParameter("start_time");
String end_day=request.getParameter("end_day");
String end_time=request.getParameter("end_time");
String gw_type=request.getParameter("gw_type");

Map cityMapAll = DeviceAct.getCityMap_All();
//Map userypeMapAll = DeviceAct.getUsertypeMap_All();
Map deviceTypeMapAll = DeviceAct.getDeviceTypeMap_All();

Cursor cursor = DeviceAct.getDeviceInformStatList(request);
Map fields = cursor.getNext();
%>
<DIV id="idView">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
	<TR>
		<TH nowrap>设备OUI</TH>
		<TH nowrap>设备序列号</TH>
		<TH nowrap>设备类型</TH>
		<TH nowrap>交互次数</TH>
		<TH nowrap>操作</TH>
	<%
	 if(null==fields)
	 {
		 out.println("<TR align=left><TD class=column colspan=10>系统没有符合条件的设备!</TD></TR>");
	 }
	 else
	 {
		 String cpe_currentupdatetime = null;
		 DateTimeUtil dateTimeUtil = null;
		 
		 while(null!=fields) {	 
			 String device_serialnumber = (String)fields.get("device_serialnumber");
			 cpe_currentupdatetime = (String)fields.get("time");
				if(cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")){
					dateTimeUtil = new DateTimeUtil(Long.parseLong(cpe_currentupdatetime)*1000);
					cpe_currentupdatetime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
			 out.println("<TR align=center>");
			 out.println("<TD class=column>" + fields.get("oui") +"</TD>");
			 out.println("<TD class=column>" + device_serialnumber +"</TD>");
			 out.println("<TD class=column>"+ deviceTypeMapAll.get(fields.get("gw_type")) +"</TD>");
			 out.println("<TD class=column>"+ fields.get("total") +"</TD>");
			 //out.println("<TD class=column><a href=\"DeviceInformStat_submit.jsp?device_serialnumber="+ fields.get("device_serialnumber") + "\">查看交互记录</a></TD>");
			 out.println("<TD class=column><a href=javascript:// onclick=\"detail('"+ device_serialnumber +"', '" + start_day +"', '" + start_time +"' , '" + end_day +"' , '" + end_time +"')\">查看交互记录</a></TD>");
			 out.println("</TR>");
			 
			 fields = cursor.getNext();
		 }
	 }
	
	//CLEAR
	fields = null;
	cursor = null;
	%>
</TABLE>
</DIV>
<script language="JavaScript">
parent.devListLayer.innerHTML=idView.innerHTML;
parent.document.all("devDetail").style.display = "none";
parent.document.all("devList").style.display ="";
</script>
	


