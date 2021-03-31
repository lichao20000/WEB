<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct_Copy" />
<%
List list = DeviceAct.getDeviceInfoList(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
StringBuffer sb = new StringBuffer(); 
if (fields == null) {
    sb.append("<TR><TD class=column COLSPAN=5 HEIGHT=30>该系统没有设备资源</TD></TR>");
} else {
	String cpe_currentupdatetime = null;
	DateTimeUtil dateTimeUtil = null;
	while (fields != null) {
		cpe_currentupdatetime = (String)fields.get("last_time");
		if(cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")){
			dateTimeUtil = new DateTimeUtil(Long.parseLong(cpe_currentupdatetime)*1000);
			cpe_currentupdatetime = dateTimeUtil.getLongDate();
			dateTimeUtil = null;
		}
		sb.append("<TR><TD class=column2 >" + (String)fields.get("device_name") + "</TD>");
		sb.append("<TD class=column2 >" + (String)fields.get("oui") + "</TD>");
		sb.append("<TD class=column2 nowrap>" + (String)fields.get("device_serialnumber") + "</TD>");
		sb.append("<TD class=column2 align='center'>" + (String)fields.get("retrycount") + "</TD>");
		sb.append("<TD class=column2 align='center'>" + cpe_currentupdatetime + "</TD></TR>");
		fields = cursor.getNext();
	}
	sb.append("<TR><TD class=column COLSPAN=5 align=right>" + strBar + "</TD></TR>");
}
%>
<%@ include file="../head.jsp"%>
<BR>
<BR>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
 						<td width="174" nowrap align="center" class="title_bigwhite">
							设备与系统交互记录
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			 <TD bgcolor=#999999>
			 	<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH nowrap>设备名称</TH>
						<TH nowrap>OUI</TH>
						<TH nowrap>设备序列号</TH>
						<TH nowrap>设备连接次数</TH>
						<TH nowrap>设备最近更新时间</TH>
					</TR>
					<%=sb.toString()%>
				</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</TD>
</TR>
</TABLE>
<%
DeviceAct = null;
%>

<%@ include file="../foot.jsp"%>