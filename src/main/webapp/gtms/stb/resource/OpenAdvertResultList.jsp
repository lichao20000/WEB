<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>�������������ϸ�б�</title>
	<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<style type="text/css">
	/* ѡ������ʽ */
	.select{
		background-color: #E8E8FF;
	}
	</style>
<script type="text/javascript"	src="<s:url value="/lims/itv/zeroconf/js/rightMenu.js"/>"></script>
	<script type="text/javascript">
</script>
</head>
<body>
	<table class="listtable" align="center" width="100%">
		<thead>
			<tr align="center">
				<th width="">����</th>
				<th width="">����</th>
				<th width="">�豸�ͺ�</th>
				<th width="">����汾</th>
				<th width="">�豸���к�</th>
				<th width="">MAC</th>
				<th width="">ҵ���˺�</th>
				<th width="">�豸IP</th>
				<th width="">���뷽ʽ</th>
				<th width="">���ý��</th>
				<th width="">����ʱ��</th>
				<th width="">ִ�в�������</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="list.size>0">
			<s:iterator value="list">
				<tr>
					<td><s:property value="cityName"/></td>
					<td><s:property value="vendorName"/></td>
					<td><s:property value="deviceModel"/></td>
					<td><s:property value="deviceTypeName"/></td>
					<td><s:property value="deviceSerialNumber"/></td>
					<td><s:property value="cpe_mac"/></td>
					<td><s:property value="servAccount"/></td>
					<td><s:property value="loopback_ip"/></td>
					<td><s:property value=""/></td>
					<td><s:property value="result"/></td>
					<td><s:property value="update_time"/></td>
					<td><s:property value="taskName"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="12">�Ҳ����������...</td>
			</tr>
		</s:else>
		</tbody>	
		<tfoot>
		<tr>
			<td colspan="12" align="right"><lk:pages url="/gtms/stb/resource/openAdvertQuery!queryTotalList.action" isGoTo="true" /></td>
		</tr>
	</tfoot>
	</table>
</body>
</html>