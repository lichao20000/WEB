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

	<caption>������ͳ����Ϣ�굥</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>LOID</th>
			<th>�û��ն˹��</th>
			<th>BSS����ʱ��</th>
			<th>�ͻ�����</th>
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
				<td colspan="5">ϵͳû����ص��豸��Ϣ!</td>
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
				SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="userType"/>','<s:property value="user"/>')"></td>
		</tr>

		<TR>
			<TD align="center" colspan="5">
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
