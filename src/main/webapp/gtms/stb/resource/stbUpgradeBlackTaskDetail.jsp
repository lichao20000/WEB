<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置机顶盒列表</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
		
</SCRIPT>

</head>
<body>
<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
			<th align="center" width="8%">属地</th>
			<th align="center">设备厂商</th>
			<th align="center">设备型号</th>
			<th align="center" width="12%">软件版本</th>
			<th align="center" width="12%">设备序列号</th>
			<th align="center">业务帐号</th>
			<th align="center">MAC</th>
			<th align="center">设备IP</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
				<td><s:property value="city_name"/></td>
			    <td><s:property value="vendor_add"/></td>
				<td><s:property value="device_model"/></td>
				<td><s:property value="softwareversion"/></td>
				<td><s:property value="device_serialnumber"/></td>
				<td><s:property value="serv_account"/></td>
				<td><s:property value="cpe_mac"/></td> 
				<td><s:property value="loopback_ip"/></td>
			</tr>
		</s:iterator>
	</tbody>
	
	<tfoot>
		<tr bgcolor="#FFFFFF">
				<td colspan="12" align="right" nowrap="nowrap">
					<lk:pages url="/gtms/stb/resource/stbUpgradeBlackList!getdetailList.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</td> 
		</tr>
		<tr STYLE="display: none">
			<td nowrap="nowrap">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</tfoot>
</table>
</body>