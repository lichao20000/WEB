
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="flex.messaging.util.URLDecoder;"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��Ϣ��ѯ���</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>�豸����</th>
				<th>�豸�ͺ�</th>
				<th>�ض��汾</th>
				<th>Ӳ���汾</th>
				<th>����汾</th>
				<th>OUI</th>
				<th>�豸����</th>
				<th>���з�ʽ</th>
				<th>�ն˹��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
						<tr align="center">
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="specversion" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="oui" /></td>
							<td><s:property value="rela_dev_type_id" /></td>
							<td><s:property value="type_name" /></td>
							<td><s:property value="spec_name" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>ϵͳû���豸��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳû���豸��Ϣ!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="right"><lk:pages
						url="/gtms/stb/resource/DeviceMessageQuery!querymessage.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>