<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量回填LOID详细查询</title>
<%
	/**
	 * 回填LOID详细查询
	 * 
	 * @author zhangsibei(4174)
	 * @version 1.0
	 * @since 2014年3月3日 
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ListToExcel(startTime, endTime, loid, deviceNumber, status) {
		var page = "<s:url value='/gtms/config/setLoidAction!initSetLoidExcel.action' />?"
				+ "startTime="
				+ startTime
				+ "&endTime="
				+ endTime
				+ "&loid="
				+ loid + "&deviceNumber=" + deviceNumber + "&statu=" + status;
		document.all("childFrm").src = page;
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th>区域</th>
				<th>OUI</th>
				<th>LOID</th>
				<th>设备序列号</th>
				<th>执行日期</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="resultList!=null">
				<s:if test="resultList.size()>0">
					<s:iterator value="resultList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="oui" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="update_time" /></td>
							<td><s:property value="status" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>没有定制任何设备信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>没有定制任何设备信息!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" align="right"><lk:pages
						url="/gtms/config/setLoidAction!initSetLoid.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr>
				<td colspan="6" align="right"><input type="hidden" name="time"
					id="time" value="<s:property value='time' />" /> <IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ListToExcel('<s:property value="startTime"/>','<s:property value="endTime"/>','<s:property value="loid"/>','<s:property value="deviceNumber"/>','<s:property value="statu"/>')"></td>
			</tr>
		<tr STYLE="display: none">
			<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
		</tr>
		</tfoot>
		
	</table>
</body>
</html>