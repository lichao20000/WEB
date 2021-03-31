<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.Map"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%
Cursor cursor = DeviceAct.getDeviceInformStat(request);
Map fields = cursor.getNext();
%>
<DIV id="idViewSub">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
	<TR>
		<TH nowrap>序号</TH>
		<TH nowrap>管理IP</TH>
		<TH nowrap>连接时间</TH>
	</TR>
	<%
	 if(null==fields)
	 {
		 out.println("<TR align=left><TD class=column colspan=10>系统没有该设备交互信息!</TD></TR>");
	 }
	 else
	 {
		 String cpe_currentupdatetime = null;
		 DateTimeUtil dateTimeUtil = null;
		 int serial = 1;
		 while(null!=fields)
		 {	 
			 cpe_currentupdatetime = (String)fields.get("time");
				if(cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")){
					dateTimeUtil = new DateTimeUtil(Long.parseLong(cpe_currentupdatetime)*1000);
					cpe_currentupdatetime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
			 out.println("<TR align=center>");
			 out.println("<TD class=column>" + serial++ +"</TD>");
			 out.println("<TD class=column>" + fields.get("device_ip") +"</TD>");
			 out.println("<TD class=column>"+ cpe_currentupdatetime +"</TD>");
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
parent.devDetailLayer.innerHTML=idViewSub.innerHTML;
parent.document.all("devDetail").style.display ="";
</script>
	


