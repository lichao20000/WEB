<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<%
	/**
		 *  
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2010-5-21
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
	
	function DetailToExcel(cityId,accessstyle,starttime,endtime) {
		var page="<s:url value='/itms/report/deviceCountReport!countDeviceDetailToExcel.action'/>?"
			+ "&starttime=" + starttime
			+ "&endtime=" + endtime
			+ "&accessstyle=" + accessstyle
			+ "&cityId=" + cityId;
		document.all("childFrm").src=page;
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			统计结果
		</caption>
		<thead>
			<tr>
				<th>属地</th>
				<th>入网时间</th>
				<th>设备厂商</th>
				<th>型号</th>
				<th>终端规格</th>
				<th>LOID</th>
				<th>用户账号</th>
				<th>软件版本</th>
				<th>设备序列号</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="detailList">
				<tr bgcolor="#FFFFFF">
					<td class=column1><s:property value="city_name"/></td>
					<td class=column1><s:property value="time"/></td>
					<td class=column1><s:property value="vendor_name"/></td>
					<td class=column1><s:property value="device_model"/></td>
					<td class=column1><s:property value="device_type"/></td>
					<td class=column1><s:property value="loid"/></td>
					<td class=column1><s:property value="username"/></td>
					<td class=column1><s:property value="softwareversion"/></td>
					<td class=column1><s:property value="device_serialnumber"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tr>
			<td colspan="9" align="right">
				<lk:pages url="/itms/report/deviceCountReport!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tfoot>
			<tr>
				<td colspan="9">
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表s'
						style='cursor: hand'
						onclick="DetailToExcel('<s:property value="cityId"/>','<s:property value="accessstyle"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
				</td>
			</tr>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="9">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>