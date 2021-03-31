<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");

    
	String strVersion = LipossGlobals.getLipossProperty("Version");
%>

<%@page import="com.linkage.litms.LipossGlobals"%>

<html>
<head>
<title><%=LipossGlobals.getLipossName()%></title>

</head>
<body bgcolor="#FFFFFF">
<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td colspan="2" align="center" colspan="2"><img src="./images/help_top_bbms.jpg" width="490" height="80"></td>
	</tr>
</table>
<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td height="10"></td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">公司：联创科技(南京)有限公司</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">系统名称：联创BBMS定制终端管理系统软件</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">系统简称： BBMS</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">版本：V<%=strVersion%></td>
	</tr>
	<tr>
		<td height="10"></td>
	</tr>
	<tr>
		<td align="center" colspan="2"><textarea cols=50 rows=6 name=a2 style="background-color:FFFFFF">联创BBMS定制终端管理系统（BizBox Management System，简称BBMS）是宽带业务管理平台的一个子系统，通过与BOSS及其他业务系统的交互，实现对宽带终端的集中管理和业务的统一部署。采用基于DSL论坛TR-069 架构的远程管理技术，可以实现企业网关和多种类型终端设备的自动配置、状态及性能监视、故障诊断、软件/固件升级等功能。</textarea></td>
	</tr>
	<tr>
		<td height="10"></td>
	</tr>
</table>
<div style="background:url(./images/help_down.jpg);width=490;height=80">
	<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td>
			<table width="400" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
					<div>
						<img src="./images/linkage_logo.jpg" width="80" height="60">
					</div>
				</td>			
				<td align="left" style="font-size:9pt;">版权所有 (C) 2009-2010 Linkage Corp.<input type="button" value=" 确定 " style="height:20pt;font-size:10pt;" onclick="javascript:window.close()">
				</td>
			</tr>
			</table>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
