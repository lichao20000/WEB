<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������ѯ</title>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/inmp/jQuerySplitPage-linkage.js"/>"></script>
	

<script type="text/javascript">
	$(function() {
		parent.isNotShow();
		parent.dyniframesize();
		parent.buttonShow();
	});

	function ToExcel() {
		parent.ToExcel();
	}
	
	function test(){
		parent.query();
	}

</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>���������ѯ</caption>
		<thead>
			<tr>
				<TH align="center">ԭʼ�ļ�����</TH>
				<TH align="center">����</TH>
				<TH align="center">�豸���к�</TH>
				<TH align="center">�û�LOID</TH>
				<TH align="center">����</TH>
				<TH align="center">�ͺ�</TH>
				<TH align="center">Ӳ���汾</TH>
				<TH align="center">����汾</TH>
				<TH align="center">�ն˹��</TH>
				<TH align="center">�û����</TH>
			</tr>
		</thead>
		<tbody>
			<s:if test="retResult != null">
				<s:if test="retResult.size()>0">
					<s:iterator value="retResult">
						<tr>
							<td><s:property value="default" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="vendor" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="dev_spec_id" /></td>
							<td><s:property value="user_spec_id" /></td>
						</tr>
					</s:iterator>
				</s:if>
			</s:if>
		</tbody>
		<tr>
			<td colspan="10" align="right">
				<lk:pages url="/inmp/resource/importQuery!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tfoot>
			<tr>
				<td colspan="10" align="right"><IMG
					SRC="<s:url value="/images/inmp/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>