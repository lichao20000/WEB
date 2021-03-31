<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>光猫批量重启设备信息</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
	String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
function ToExcel() 
{
	var gw_type=$.trim($("input[@name='gw_type']").val());
	var startOpenDate1 =  $.trim($("input[@name='startOpenDate1']").val());
	var endOpenDate1 = $.trim($("input[@name='endOpenDate1']").val());
	var gwShare_vendorId = $.trim($("input[@name='gwShare_vendorId']").val());
	var gwShare_deviceModelId = $.trim($("input[@name='gwShare_deviceModelId']").val());
	var gwShare_devicetypeId = $.trim($("input[@name='gwShare_devicetypeId']").val());
	
	var page="<s:url value='/itms/resource/DevBatchRestartQuery!toDetailExcel.action'/>"
			+ "?gw_type=" + gw_type
			+ "&startOpenDate1=" + startOpenDate1
			+ "&endOpenDate1=" + endOpenDate1
			+ "&gwShare_vendorId="+gwShare_vendorId
			+ "&gwShare_deviceModelId="+gwShare_deviceModelId
			+ "&gwShare_devicetypeId="+gwShare_devicetypeId;
	document.all("childFrm").src=page;
}

function ToExcel4NX()
{
	var gw_type=$.trim($("input[@name='gw_type']").val());
	var startOpenDate1 =  $.trim($("input[@name='startOpenDate1']").val());
	var endOpenDate1 = $.trim($("input[@name='endOpenDate1']").val());
	var gwShare_vendorId = $.trim($("input[@name='gwShare_vendorId']").val());
	var gwShare_deviceModelId = $.trim($("input[@name='gwShare_deviceModelId']").val());
	var gwShare_devicetypeId = $.trim($("input[@name='gwShare_devicetypeId']").val());
	var city_id = $.trim($("input[@name='city_id']").val());

	var page="<s:url value='/itms/resource/DevBatchRestartQuery!toDetailExcel.action'/>"
			+ "?gw_type=" + gw_type
			+ "&startOpenDate1=" + startOpenDate1
			+ "&endOpenDate1=" + endOpenDate1
			+ "&gwShare_vendorId="+gwShare_vendorId
			+ "&gwShare_deviceModelId="+gwShare_deviceModelId
			+ "&gwShare_devicetypeId="+gwShare_devicetypeId
			+ "&city_id="+city_id;
	document.all("childFrm").src=page;
}

function devDetail(device_id)
{
	var page="<s:url value='../../Resource/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
</script>
</head>

<body>
<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
<input type="hidden" name="startOpenDate1" value="<s:property value='startOpenDate1'/>">
<input type="hidden" name="endOpenDate1" value="<s:property value='endOpenDate1'/>">
<input type="hidden" name="gwShare_vendorId" value="<s:property value='gwShare_vendorId'/>">
<input type="hidden" name="gwShare_deviceModelId" value="<s:property value='gwShare_deviceModelId'/>">
<input type="hidden" name="gwShare_devicetypeId" value="<s:property value='gwShare_devicetypeId'/>">
<input type="hidden" name="city_id" value="<s:property value='city_id'/>">

<table class="listtable" id="listTable">
	<caption>光猫批量重启设备信息 </caption>
	<thead>
		<tr>
			<th>厂家</th>
			<th>型号</th>
			<th>软件版本</th>
			<th>属地</th>
			<th>设备序列号</th>
			<%if("4".equals(gw_type)){ %>
				<th>业务账号</th>
			<%}else{ %>	
				<th>LOID</th>
			<%}%>
			<th>重启结果</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
					<tr>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="softwareversion" /></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="loid" /></td>
						<td><s:property value="restart_status" /></td>
						<td>
							<a href="javascript:onclick=devDetail('<s:property value="device_id"/>');">
							详细信息
							</a>
						</td>
					</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">没有查到设备信息</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8" align="right">
				<lk:pages url="/itms/resource/DevBatchRestartQuery!detail.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<tr>
			<td colspan="8" align="right">
				<ms:inArea areaCode="nx_dx" notInMode="true">
					<img SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
						 style='cursor: hand' onclick="ToExcel()" >
				</ms:inArea>
				<ms:inArea areaCode="nx_dx" notInMode="false">
				<img SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					 style='cursor: hand' onclick="ToExcel4NX()" >
				</ms:inArea>

			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="8">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
</body>
</html>