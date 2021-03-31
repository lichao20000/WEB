<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>天翼网关版本统一率报表</title>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript">

// 导出当前地市详情数据
function queryDetailDataForExcel(cityId,startTime,endTime,isNewVersion,gwType,isExcludeUpgrade,recent_start_Time,recent_end_Time){
	var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!getTyDetailCountList.action'/>";
	document.detailForm.action = url+"?reportType=excel&cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime+"&isNewVersion="+isNewVersion+"&gwType="+gwType+"&isExcludeUpgrade="+isExcludeUpgrade+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
	document.detailForm.method = "post";
	document.detailForm.submit();
// 	var reportType="excel";
// 	$.post(url, {
// 		cityId : cityId,
// 		startTime : startTime,
// 		endTime : endTime,
// 		reportType : reportType,
// 		isNewVersion : isNewVersion
// 		}, function(ajax) {
// 	});
}
</SCRIPT>

</head>
<body>
<form name="detailForm" action="" method="post">
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>天翼网关版本一致率报表</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>厂家</th>
						<th>型号</th>
						<th>硬件版本</th>
						<th>软件版本</th>
						<th>数量</th>
					</tr>

					<s:iterator value="countList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<strong><s:property value="vendor_add"/></strong>
							</td>
							<td>
								<s:property value="device_model"/>
							</td>
							<td>
				 				<s:property value="hardwareversion"/>
							</td>
							<td>
								<s:property value="softwareversion"/>
							</td>
							<td>
								<s:property value="countNum"/>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDetailDataForExcel('<s:property value="cityId"/>',
						 												'<s:property value="startTime"/>',
						 												'<s:property value="endTime"/>',
						 												'<s:property value="isNewVersion"/>',
						 												'<s:property value="gwType"/>',
						 												'<s:property value="isExcludeUpgrade"/>',
						 												'<s:property value="recent_start_Time"/>',
																 		'<s:property value="recent_end_Time"/>');">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
						<td class=column1 align="right">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
