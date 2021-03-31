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
	function ListToExcel(cityId, startOpenDate, endOpenDate, userType, user) {
		var page = "<s:url value='/itms/report/moreBroadbandBusinessQuery!getDevExcel.action' />?"
				+ "cityId="
				+ cityId
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&userType="
				+ userType
				+ "&user=" + user;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">

	<caption>多宽带务统计信息详单</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>LOID</th>
			<th>用户终端规格</th>
			<th>BSS受理时间</th>
			<th>客户类型</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="username" /></td>
					<td><s:property value="spec_id" /></td>
					<td><s:property value="opendate" /></td>
					<td><s:property value="cust_type_id" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="5">系统没有相关的设备信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="5"><span style="float: right;"> <lk:pages
						url="/itms/report/moreBroadbandBusinessQuery!getDevListForWbdTerminal.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span></td>
		</tr>
		<tr>
			<td colspan="5" align="right"><input type="hidden" name="time"
				id="time" value="<s:property value='time' />" /> <IMG
				SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="userType"/>','<s:property value="user"/>')"></td>
		</tr>

		<TR>
			<TD align="center" colspan="5">
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
