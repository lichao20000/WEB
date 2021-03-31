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


function ListToExcel(cityId,starttime) {
	var time = $("input[@name='time']").val();
	var sttime = $("input[@name='starttime']").val();
	var device_type = $("input[@name='device_type']").val();
	var page="<s:url value='/itms/report/activeTerminal!getDeviceListForTimeExcel.action'/>?"
		+ "city_id=" + cityId 
		+ "&startOpenDate=" + sttime
		+ "&endOpenDate=" +time
		+ "&device_type=" +device_type;
	document.all("childFrm").src=page;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>
		活跃终端数
	</caption>
	<thead>
		<tr>
			<th>
				属地
			</th>
			<th>
				设备厂商
			</th>
			<th>
				型号
			</th>
			<th>
				软件版本
			</th>
			<th>
				设备序列号
			</th>
			<th>
				域名或IP
			</th>
			<th>
				绑定账号
			</th>
			<th>
				宽带账号
			</th>
			
			<th>
				操作
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="loopback_ip" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="broadband" />
					</td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
							onclick="DetailDevice('<s:property value="device_id"/>')"
							style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">
					系统没有相关的设备信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<span style="float: right;"> <lk:pages
						url="/itms/report/activeTerminal!getDeviceListForTime.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<input type="hidden" name="starttime" id="time1" value='<s:property value='startOpenDate'/>' />
					<input type="hidden" name="time" id="time" value='<s:property value='endOpenDate'/>' />
					<input type="hidden" name="device_type" id="device_type" value='<s:property value='device_type'/>' />
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="city_id"/>')">
				</s:if>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="8">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
