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
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.isNotShow();
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}

</script>
</head>
<body>
				<s:if test="userList.size()>0">
<table class="listtable" id="listTable">
		<caption>���������ѯ</caption>
		<thead>
			<tr>
				<TH align="center">ԭʼ�ļ�����</TH>
				<TH align="center">����</TH>
				<TH align="center">�û�LOID</TH>
				<TH align="center">����˺�</TH>
				<TH align="center">ITV�˺�</TH>
				<TH align="center">�����˺�</TH>
			</tr>
		</thead>
		<tbody>
			
					<s:iterator value="userList">
						<tr>
							<td ><s:property value="content" /></td>
								<td ><s:property value="city_name" /></td>
								<td ><s:property value="loid" /></td>
								<td ><s:property value="net_account" /></td>
								<td ><s:property value="itv_account" /></td>
								<td ><s:property value="voip_account" /></td>
						</tr>
					</s:iterator>
				
		</tbody>
		<tfoot>
		<tr>
							<td colspan="6" align="right"><lk:pages
									url="/itms/report/exportUser!goPage.action" styleClass=""
									showType="" isGoTo="true" changeNum="true" /></td>
						</tr>
						</tfoot>
		
	</table>
	<div style=' float: left'><IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand; float: left' onclick="ToExcel()"></div>
	</s:if>
			<s:else>
				ϵͳû��ƥ�䵽��Ӧ�����ݣ�
			</s:else>
</body>
</html>