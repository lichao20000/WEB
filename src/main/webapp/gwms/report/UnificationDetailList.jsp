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
function queryDetailDataForExcelJXDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
		startTime,endTime,recent_start_Time,recent_end_Time,hardwareversion,is_highversion){
	var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!querydetailListJXDX.action'/>";
	document.detailForm.action = url+"?reportType=excel&gwType="+gwType+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId+"&isExcludeUpgrade="+isExcludeUpgrade+"&startTime="+startTime+"&endTime="+endTime+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time+"&hardwareversion="+hardwareversion+"&is_highversion="+is_highversion;
	document.detailForm.method = "post";
	document.detailForm.submit();
}
</SCRIPT>

</head>
<body>
	<form name="detailForm" action="" method="post">
		<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>主流天翼网关统一率报表</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>厂商</th>
						<th>型号</th>
						<th>硬件版本</th>
						<th>软件版本</th>
						<th>数量</th>
					</tr>

					<s:iterator value="countList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<s:property value="vendor_add"/>
							</td>
							<td  class=column>
								<s:property value="device_model"/>
							</td>
							<td  class=column>
								<s:property value="hardwareversion"/>
							</td>
							<td class=column>
								<s:property value="softwareversion"/>
							</td>
							<td class=column>
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
						 	<a href="javascript:queryDetailDataForExcelJXDX('<s:property value="gwType"/>',
																 '<s:property value="vendorId"/>',
																 '<s:property value="deviceModelId"/>',
																 '<s:property value="isExcludeUpgrade"/>',
																 '<s:property value="startTime"/>',
																 '<s:property value="endTime"/>',
																 '<s:property value="recent_start_Time"/>',
																 '<s:property value="recent_end_Time"/>',
																 '<s:property value="hardwareversion"/>',
																 '<s:property value="is_highversion"/>');">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
