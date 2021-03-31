<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*,
				com.linkage.litms.warn.*" %>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<html>
<head>
<title>用户状态管理---线路状态</title>
<%
	
	String device_serialnumber = request.getParameter("device_serialnumber");
	if(device_serialnumber == null){
		device_serialnumber = "";
	}
	String device = "";
	String warntype = "无";
	String statusuptime = "";
	StringBuilder strHTML = new StringBuilder();
	Cursor sor = UserWarnAct.getUserLinkStat(request);
	Map fields = null;
	if(sor != null && sor.getRecordSize() > 0){
		while((fields = sor.getNext()) != null){
			//用户帐号	状态上报时刻	CPU负载（%）	剩余物理内存（KB）	www可用空间（%）
			device = fields.get("oui")+"-"+fields.get("device_serialnumber");
			if("1".equals(fields.get("warntype")))
				warntype = "有";
			
			//设备上报时间，时间处理
			statusuptime = (String)fields.get("statusuptime");
			DateTimeUtil dtu = new DateTimeUtil(statusuptime + "000");
			
			strHTML.append("<tr bgcolor=#ffffff>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append(device);
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr >");
			strHTML.append(dtu.getDate() + " " + dtu.getTime());
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append(fields.get("cpu_used"));
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append(fields.get("free_mem"));
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append(fields.get("www_space"));
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append(warntype);
			strHTML.append("</td>");
			
			strHTML.append("<td class=column align='center' nobr>");
			strHTML.append("<a target='blank' href = 'userLinkStatDetail.jsp?device_id="
					+fields.get("device_id")+"&device="+device+"'>详细信息</a>");
			strHTML.append("</td>");
			
			strHTML.append("</tr>");
		}
	}else{
		strHTML.append("<tr bgcolor='#ffffff' ><td align=center colspan=7>没有线路连接状态数据</td></tr>");
	}
%>

<script type="text/javascript">
<!--
	function query(){
		obj = document.frm;
		device_serialnumber = obj.device_serialnumber.value;
		window.location.href = "userLinkStat.jsp?device_serialnumber="+device_serialnumber;
	}
//-->
</script>
</head>
<%@ include file="../head.jsp"%>
<body>
<form name="frm" action="" method="post">
	<table width="95%"  border=0 cellspacing=0 cellpadding=0  align="center">
		<tr><td height="20"></td></tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							用户状态管理
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="4">线路状态监控</TH></TR>
					<tr BGCOLOR=#ffffff>
						<TD class=column align=right width="25%">设备信息</TD>
      					<TD class=column nowrap width="25%">
      						<input type="text" name="device_serialnumber" value="<%=device_serialnumber%>">
      					</TD>
      					<TD class=column nowrap width="50%" colspan="2">
      					&nbsp;&nbsp;&nbsp;
      					<input type="button" onclick="query()" value=" 查 询 "></TD>
      				<tr BGCOLOR=#ffffff class=green_foot>	
      					<td align="right" colspan="4">
      					<a href="#" onclick="query()">刷 新</a>
      					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<tr>
						<TH>设备信息</TH>
						<TH>状态上报时刻</TH>
						<TH>CPU负载(%)</TH>
						<TH>剩余物理内存(KB)</TH>
						<TH>www可用空间(%)</TH>
						<TH>设备是否有告警</TH>
						<TH>操 作</TH>
					</tr>
					<%=strHTML %>
				</TABLE>
			</TD>
		</TR>
		<tr><td height="20"></td></tr>
	</table>

</form>
</body>
</html>
<%@ include file="../foot.jsp"%>