<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<script type="text/javascript">
	function ListToExcel(cityId,startOpenDate,endOpenDate) {
		var page = "<s:url value='/itms/report/presetEquipmentNotImportQuery!getDevExcel.action' />";
			+ "cityId=" + cityId 
			+ "&startOpenDate=" +startOpenDate
			+ "&endOpenDate=" +endOpenDate;
		document.all("childFrm").src=page;
	}

</script>

<table class="listtable">

	<caption>预置受限制设备信息详单</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>OUI</th>
			<th>设备序列号</th>
			<th>厂家</th>
			<th>设备型号</th>
			<th>上报日期</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="oui" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="vendor_name" /></td>
					<td><s:property value="device_model_name" /></td>
					<td><s:property value="complete_time" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="6">系统没有相关的设备信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6"><span style="float: right;"> <lk:pages
						url="/itms/report/presetEquipmentNotImportQuery!getDevListForWbdTerminal.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span></td>
		</tr>
		<tr>
			<td colspan="6" align="right"><input type="hidden" name="time"
				id="time" value="<s:property value='time' />" /> <IMG
				SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>')"></td>
		</tr>

		<TR>
			<TD align="center" colspan="6">
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
