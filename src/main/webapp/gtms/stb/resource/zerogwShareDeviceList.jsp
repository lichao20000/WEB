<!-- zerogwShareDeviceList.jsp -->
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置设备查询</title>
<link href="<s:url value="/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

	$(function(){
		parent.dyniframesize();
	});

	function deviceSelect(thisRadio){
		$("input[@name='txtDeviceId']").val(thisRadio.value);
	}
	
	function comfirmDevice(){
		var childDevice = $("input[@name='txtDeviceId']").val();
		if(""==childDevice || null==childDevice){
			alert("请选择设备！");
			return false;
		}
		var device = childDevice.split("|");
		window.returnValue = new Array(device[0],device[1],device[2],device[3],device[4],device[5]);
		window.close();
	}
	function deviceDetail(deviceId)
	{
		window.open("/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action?deviceId="+deviceId,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function ToExcel() {
		var form = document.getElementById("form");
		form.action = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!zerotoExcel.action'/>?cityId="
			+'<s:property value="cityId"/>'
			+ "&devicetypeId=" + '<s:property value="devicetypeId"/>'
			+ "&queryType=2&noChildCity="+'<s:property value="noChildCity"/>'
			+ "&startLastTime=" + '<s:property value="completeStartTime"/>'
			+ "&endLastTime=" + '<s:property value="completeEndTime"/>'
			+"&vendorId=" + '<s:property value="vendorId"/>'
			+"&deviceModelId=" + '<s:property value="deviceModelId"/>';
		//alert(form.action);
		form.target = "";
		form.submit();
	}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="form" action="" target="" method="post">
<input type="hidden" name="txtDeviceId" value=""/>
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<!-- <th align="center">选择</th> -->
		<th align="center">设备厂商</th>
		<th align="center">设备型号</th>
		<th align="center">软件版本</th>
		<th align="center">属地</th>
		<th align="center">设备序列号</th>
		<th align="center">在线状态</th>
		<th align="center">设备IP</th>
		<th align="center">业务账号</th>
		<th align="center">宽带账号</th>
	</tr>
	<s:iterator value="deviceList">
		<tr bgcolor="#FFFFFF" >
			<!-- 
			<td class=column1>
				<input type="<s:property value="queryResultType"/>" name="radioDeviceId" onclick="javascript:deviceSelect(this);"
					value="<s:property value="device_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|<s:property value="city_id"/>|<s:property value="city_name"/>" /></td>
			 -->
			<td class=column1><s:property value="vendor_add"/></td>
			<td class=column1><s:property value="device_model"/></td>
			<td class=column1><s:property value="softwareversion"/></td>
			<td class=column1><s:property value="city_name"/></td>
			<td class=column1><s:property value="device_serialnumber"/></td>
			<td class=column1><s:property value="online_status"/></td>
			<td class=column1><s:property value="loopback_ip"/></td>
			<td class=column1 ><a class='green_link' href="#" onclick="deviceDetail('<s:property value="device_id"/>')"><s:property value="serv_account"/></a></td>
			<td class=column1 ><s:property value="cust_account"/></td>
		</tr>
	</s:iterator>
	<tr bgcolor="#FFFFFF">
		<td colspan="9" align="right">
			<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="9" align="right" class="green_foot" width="100%">
		<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">
			<!-- <input type="button" onclick="javascript:comfirmDevice();" class=jianbian name="deviceConfirm" value=" 确 定 " /> -->
			<input type="button" onclick="javascript:window.close();" class=jianbian name="canel" value=" 关 闭 " />
		</td>
	</tr>
</table>
</form>
</body>