<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
function ListToExcel(cityId,starttime1,endtime1,status,
		resultId,isMgr,isSoftUp,vendorId,deviceModelId) 
{
	var page="<s:url value='/itms/report/softUpResult!getDevExcel.action'/>?"
				+ "cityId=" + cityId 
				+ "&starttime1=" +starttime1
				+ "&endtime1=" +endtime1
				+ "&status=" +status
				+ "&resultId=" +resultId
				+ "&isSoftUp=" +isSoftUp
				+ "&isMgr=" +isMgr
				+ "&vendorId="+vendorId
				+ "&deviceModelId="+deviceModelId;
	document.all("childFrm").src=page;
}

//查看itms用户相关的信息
function devInfo(device_id)
{
	var strpage="<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>设备列表</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>厂商</th>
			<th>型号</th>
			<th>版本</th>
			<ms:inArea areaCode="jx_dx" notInMode="false">
				<th>硬件版本</th>
				<th>接入类型</th>
			</ms:inArea>
			<th>设备序列号</th>
			<ms:inArea areaCode="jx_dx" notInMode="false">
				<th>用户账号</th>
				<th>终端支持速率</th>
				<th>注册系统时间</th>
				<th>最近更新时间</th>
			</ms:inArea>
			<th>IP或域</th>
			<th>失败原因</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="vendor_add" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="softwareversion" /></td>
					<ms:inArea areaCode="jx_dx" notInMode="false">
						<td><s:property value="hardwareversion" /></td>
						<td><s:property value="accessTypeStr" /></td>
					</ms:inArea>
					<td><s:property value="device" /></td>
					<ms:inArea areaCode="jx_dx" notInMode="false">
						<td><s:property value="loid" /></td>
						<td><s:property value="terminalRate" /></td>
						<td><s:property value="completeTime" /></td>
						<td><s:property value="lastTime" /></td>
					</ms:inArea>
					<td><s:property value="loopback_ip" /></td>
					<td><s:property value="fault_desc" /></td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
							onclick="devInfo('<s:property value="device_id"/>')" style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<ms:inArea areaCode="jx_dx" notInMode="true">
					<td colspan=8>
						系统没有相关的用户信息!
					</td>
				</ms:inArea>
				<ms:inArea areaCode="jx_dx" notInMode="false">
					<td colspan=14>
						系统没有相关的用户信息!
					</td>
				</ms:inArea>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<ms:inArea areaCode="jx_dx" notInMode="true">
				<td colspan="8">
				<span style="float: right;">
					<lk:pages url="/itms/report/softUpResult!getDev.action" styleClass=""
							  showType="" isGoTo="true" changeNum="true" />
				</span>
					<s:if test='#session.isReport=="1"'>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand' onclick="ListToExcel('<s:property value="cityId"/>',
						'<s:property value="starttime1"/>',
						'<s:property value="endtime1"/>',
						'<s:property value="status"/>',
						'<s:property value="resultId"/>',
						'<s:property value="isMgr"/>',
						'<s:property value="isSoftUp"/>',
						'<s:property value="vendorId"/>',
						'<s:property value="deviceModelId"/>')">
					</s:if>
				</td>
			</ms:inArea>
			<ms:inArea areaCode="jx_dx" notInMode="false">
				<td colspan="14">
				<span style="float: right;">
					<lk:pages url="/itms/report/softUpResult!getDev.action" styleClass=""
							  showType="" isGoTo="true" changeNum="true" />
				</span>
					<s:if test='#session.isReport=="1"'>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand' onclick="ListToExcel('<s:property value="cityId"/>',
						'<s:property value="starttime1"/>',
						'<s:property value="endtime1"/>',
						'<s:property value="status"/>',
						'<s:property value="resultId"/>',
						'<s:property value="isMgr"/>',
						'<s:property value="isSoftUp"/>',
						'<s:property value="vendorId"/>',
						'<s:property value="deviceModelId"/>')">
					</s:if>
				</td>
			</ms:inArea>

		</tr>
		<TR>
			<ms:inArea areaCode="jx_dx" notInMode="true">
				<TD align="center" colspan="8">
					<button onclick="javascript:window.close();">&nbsp;关 闭&nbsp;</button>
				</TD>
			</ms:inArea>
			<ms:inArea areaCode="jx_dx" notInMode="false">
				<TD align="center" colspan="14">
					<button onclick="javascript:window.close();">&nbsp;关 闭&nbsp;</button>
				</TD>
			</ms:inArea>

		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
