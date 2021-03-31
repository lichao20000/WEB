<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

function downloadDevInfo(area_id){
	var cityId = $("#city_Id").val();
	if(cityId!=null&&cityId!==""&&cityId!=="00"&&cityId!=="-1"&&cityId!=="1"){
		area_id = cityId;
	}
	var url = "<s:url value='/gwms/resource/batchConfigNodeACT!downloadDevInfo.action'/>?"
		+ "&cityId=" + area_id;
	document.getElementById("mainForm").action = url;
	document.getElementById("mainForm").submit();
	document.getElementById("mainForm").reset();

}

</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center"  >
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999"  style="overflow-y: auto" >
					<tr bgcolor="#FFFFFF">
						<th>属地</th>
						<th>支持wifi终端量</th>
						<th>使用终端wifi数量</th>
						<th>采集终端的占比</th>
					</tr>
					<s:iterator value="taskList">
						<tr bgcolor="#FFFFFF">
							<td width="14%">
								<s:if test="''==city">
								其他
								</s:if>
								<s:else>
								<s:property value="city" />
								</s:else>
							</td>
							<td width="14%">
								<s:property value="sum" />
							</td>
							<td width="14%">
								<a  href="javascript:downloadDevInfo('<s:property value="area_id" />')" ><s:property value="sum1" /></a>
							</td>
							<td width="14%">
								<s:property value="devper" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
	<FORM method="post" action="" id="mainForm" name="mainForm" >
		<input type="hidden" name="startOpenDate" id="startOpenDate" value="<s:property value="startOpenDate" />"/>
		<input type="hidden" name="endOpenDate" id="endOpenDate" value="<s:property value="endOpenDate" />"/>
		<input type="hidden" name="city_Id" id="city_Id" value="<s:property value="cityId" />"/>
	</FORM>
</body>
</html>
