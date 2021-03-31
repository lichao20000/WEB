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
	function ListToExcel(cityId, flag) {
		var time = $("input[@name='time']").val();
		var page = "<s:url value='/itms/report/mothTerminalOrder!getMothTerminalOrderExcel.action'/>?"
				+ "city_id=" + cityId + "&flag=" + flag;
		+"&time=" + time;
		document.all("childFrm").src = page;
	}
	function DetailDevice(device_id) {
		var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	//查看itms用户相关的信息
	function itmsUserInfo(user_id) {
		var strpage = "<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id="
				+ user_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
</script>

<table class="listtable">

	<caption>用户列表</caption>
	<thead>
		<tr>
			<th>用户账号</th>
			<th>属地</th>
			<th>用户来源</th>
			<th>开户时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="username" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="user_type" /></td>
					<td><s:property value="opendate" /></td>
					<td><IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
						ALT="详细信息" onclick="itmsUserInfo('<s:property value="user_id"/>')"
						style="cursor: hand"></td>
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
						url="/itms/report/mothTerminalOrder!getDevListForWbdTerminal.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span></td>
		</tr>
		<tr>
			<td colspan="5" align="right"><input type="hidden" name="time"
				id="time" value="<s:property value='time' />" /> <IMG
				SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="flag" />')">
			</td>
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
