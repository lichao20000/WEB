<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����û������б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
		
	function deviceDelete(deviceSerialnumber){
		if(!confirm("���Ҫ���豸�Ӻ������޳���\n���������ָܻ�������")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!delete.action'/>";
		$.post(url,{
			deviceSerialnumber:deviceSerialnumber
		},function(ajax){
		    alert(ajax);
		    parent.queryDevice();
		});
	}
</SCRIPT>

</head>
<body>
<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
			<th align="center">�豸����</th>
			<th align="center">�豸�ͺ�</th>
			<th align="center" width="12%">����汾</th>
			<th align="center" width="8%">����</th>
			<th align="center" width="12%">�豸���к�</th>
			<th align="center">ҵ���ʺ�</th>
			<th align="center">MAC</th>
			<th align="center">�豸IP</th>
			<th align="center">����</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
			    <td><s:property value="vendor_add"/></td>
				<td><s:property value="device_model"/></td>
				<td><s:property value="softwareversion"/></td>
				<td><s:property value="city_name"/></td>
				<td><s:property value="device_serialnumber"/></td>
				<td><s:property value="serv_account"/></td>
				<td><s:property value="cpe_mac"/></td> 
				<td><s:property value="loopback_ip"/></td>
				<td align="center" nowrap="nowrap">
					<label onclick="javascript:deviceDelete('<s:property value='device_serialnumber'/>');">
						<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='�ָ�' style='cursor:hand'>
					</label>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	
	<tfoot>
		<tr bgcolor="#FFFFFF">
				<td colspan="12" align="right" nowrap="nowrap">
					<lk:pages url="/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceList.action" isGoTo="true" />
				</td> 
		</tr>
		<tr STYLE="display: none">
			<td nowrap="nowrap">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</tfoot>
</table>
</body>