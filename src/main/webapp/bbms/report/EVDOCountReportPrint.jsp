<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO网关统计</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">


</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr>
			<td class=column1 align="right">
				<a href="javascript:window.print()">打印</a>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="center" height="25">
				<strong>
					EVDO网关统计
				</strong>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
					<tr>
						<TD>属地</TD>
						<TD>设备总数</TD>
						<TD>EVDO总数</TD>
						<TD>备用链路总数</TD>
						<TD>主链路总数</TD>
						<TD>备用链路总数/EVDO总数</TD>
						<TD>主链路总数/EVDO总数</TD>
						<TD>EVDO总数/设备总数</TD>
					</tr>
					<s:iterator value="reportResult">
						<tr bgcolor="#FFFFFF">
							<td class=column><s:property value="city_name"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="device_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="evdo_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="standby_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="main_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="standby_evdo"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="main_evdo"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="evdo_device"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
					<tr bgcolor="#FFFFFF">
						<td class=column1  colspan="12" align="right">
							<strong>
								统计日期：<s:property value="startTime"/>————<s:property value="endTime"/>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>