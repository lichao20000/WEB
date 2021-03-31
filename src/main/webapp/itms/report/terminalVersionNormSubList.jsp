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
	var gw_type = '<s:property value="gw_type"/>';
	function ListToExcel(flag, cityId, accessType, userType, starttime1,
			endtime1, vendorId, deviceModelId, isActive) {
		var page = "<s:url value='/itms/report/terminalVersionNormQuery!getDeviceResultExcel.action'/>?"
				+ "flag="
				+ flag
				+ "&cityId="
				+ cityId
				+ "&accessType="
				+ accessType
				+ "&userType="
				+ userType
				+ "&starttime1="
				+ starttime1
				+ "&endtime1="
				+ endtime1
				+ "&vendorId="
				+ vendorId
				+ "&deviceModelId="
				+ deviceModelId
				+ "&isActive="
				+ isActive + "&gw_type=" + gw_type;
		document.all("childFrm").src = page;
	}

	//查看itms终端相关的信息
	function itmsDeviceInfo(device_id) {
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
</script>

<table class="listtable">
	<caption>设备列表</caption>
	<thead>
		<tr>
			<th>设备厂商</th>
			<th>设备型号</th>
			<th>软件版本</th>
			<th>设备属地</th>
			<th>设备序列号</th>
			<th>LOID</th>
			<th>终端类型</th>
			<th>注册时间</th>
			<th>最近上报时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList.size()>0">
			<s:iterator value="deviceList">
				<tr>
					<td><s:property value="vendorName" /></td>
					<td><s:property value="devicemodel" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="deviceSerialnumber" /></td>
					<td><s:property value="deviceIdEx" /></td>
					<td><s:property value="device_type" /></td>
					<td><s:property value="completeTime" /></td>
					<td><s:property value="lastTime" /></td>
					<td><IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
						ALT="详细信息"
						onclick="itmsDeviceInfo('<s:property value="device_id"/>')"
						style="cursor: hand"></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>系统没有相关的设备信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10"><span style="float: right;"> <lk:pages
						url="/itms/report/terminalVersionNormQuery!queryDeviceList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="flag"/>','<s:property value="cityId"/>',
											'<s:property value="accessType"/>','<s:property value="userType"/>',
											'<s:property value="starttime1"/>','<s:property value="endtime1"/>',
											'<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="isActive"/>')">
				<s:if test='#session.isReport=="1"'>

				</s:if></td>
		</tr>


		<TR>
			<TD align="center" colspan="10">
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
