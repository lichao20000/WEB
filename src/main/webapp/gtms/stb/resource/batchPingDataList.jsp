<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ؼ���б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
	
	function exportExcel() {
		var form = parent.document.getElementById("selectForm");
		var temp = form.action;
		var target =  form.target;
		form.action = "<s:url value='/gtms/stb/resource/batchPingAction!exportExcel.action'/>";
		form.target = "";
		form.submit();
		form.action=temp
		form.target=target;
	}
</SCRIPT>
</head>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td>
			<table width="100%" height="10" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">
						<IMG SRC="<s:url value='/images/excel.gif'/>" WIDTH="16" HEIGHT="16" BORDER="0" onclick="exportExcel()" ALT="������EXCEL" style="cursor:hand">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="listtable" width="100%" align="center">
				<thead>
					<tr>
					<th align="center">�豸ip</th>
					<th align="center">�豸����</th>
					<th align="center" width="8%">����</th>
					<th align="center" width="6%">���</th>
					<th align="center" width="12%">�������</th>
					<th align="center">����ʱ��</th>
					<th align="center" width="12%">�ɹ���</th>
					<th align="center">ʧ����</th>
					<th align="center">������</th>
					<th align="center">��С��Ӧʱ��(ms)</th>
					<th align="center">ƽ����Ӧʱ��(ms)</th>
					<th align="center">�����Ӧʱ��(ms)</th>
				</thead>
				<tbody>
					<s:iterator value="dataList">
						<tr bgcolor="#FFFFFF">
						    <td><s:property value="device_ip"/></td>
							<td><s:property value="device_name"/></td>
							<td><s:property value="city_name"/></td>
							<td><s:property value="result"/></td>
							<td><s:property value="result_desc"/></td>
							<td><s:property value="operate_time"/></td>
							<td><s:property value="succes_num"/></td> 
							<td><s:property value="fail_num"/></td>
							<td><s:property value="packet_loss_rate"/></td>
							<td><s:property value="min_response_time"/></td> 
							<td><s:property value="avg_response_time"/></td> 
							<td><s:property value="max_response_time"/></td> 
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#999999" style="border:0px;">
				<tr bgcolor="#FFFFFF">
					<td colspan="12" align="right" nowrap="nowrap">
					<lk:pages url="/gtms/stb/resource/batchPingAction!queryDataList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>