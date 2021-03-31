<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPoE失败原因统计列表</title>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function ListToExcel(starttime,endtime,cityId,operType){
	var page = "<s:url value='/ids/pppoeCount!queryByPPPoEExcel.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&operType=" + operType;
	document.all("childFrm").src=page;		
}

function openCus(starttime,endtime,cityId,operType){
	var page="<s:url value='/ids/pppoeCount!queryByPPPoEList.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&operType=" + operType;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
</script>
</head>
<body>
<table class="listtable">
	<caption>
		PPPoE失败原因统计列表
	</caption>
	<thead>
		<tr>
			<th width="10%">
				属地
			</th>
			<th width="30%">
				ERROR_ISP_TIME_OUT
			</th>
			<th width="30%">
				ERROR_AUTHENTICATION_FAILURE
			</th>
			<th width="30%">
				ERROR_ISP_DISCONNECT
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list.size()>0">
			<s:iterator value="list">
				<tr bgcolor="#FFFFFF">
					<td>
							<s:property value="cityName" />
					</td>
					<td>
						<a href="javascript:openCus('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','1');">
							<s:property value="timeoutNum" /> </a>
					</td>
					<td>
						<a href="javascript:openCus('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','2');">
							<s:property value="failureNum" /> </a>
					</td>
					<td>
						<a href="javascript:openCus('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','3');">
							<s:property value="disConnectNum" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ListToExcel('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','<s:property value="operType"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" name="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../foot.jsp"%>
