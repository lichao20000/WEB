<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�⹦�ʲɼ������ѯ</title>
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

	function ToExcel() {
		parent.ToExcel();
	}
	
	$(function(){
		$("button[@name='button']",parent.document).attr("disabled", false);
		$("#QueryData",parent.document).html("");
	});
	
	
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>�⹦�ʲɼ������ѯ</caption>
		<thead>
			<tr>
				<th>���</th>
				<th>����</th>
				<th>����</th>
				<th>�߼�ID</th>
				<th>����</th>
				<th>�ն��ͺ�</th>
				<th>�豸���к�</th>
				<th>���͹⹦��</th>
				<th>���չ⹦��</th>
				<th>�ɼ�ʱ��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="rlist!=null">
				<s:if test="rlist.size()>0">
					<s:iterator value="rlist">
						<tr bgcolor="#FFFFFF" align="center">
							<td class="column1"><s:property value="index"/></td>
							<td class="column1"><s:property value="parentName"/></td>
							<td class="column1"><s:property value="cityName"/></td>
							<td class="column1"><s:property value="username"/></td>
							<td class="column1"><s:property value="vendorName"/></td>
							<td class="column1"><s:property value="device_model"/></td>
							<td class="column1"><s:property value="deviceSn"/></td>
							<td class="column1"><s:property value="txPower"/></td>
							<td class="column1"><s:property value="rxPower"/></td>
							<td class="column1"><s:property value="colDate"/></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="10" align="left" style="background-color: white;">
							<font color="red">û����زɼ���Ϣ!</font>
						</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>û����زɼ���Ϣ!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages
						url="/itms/resource/VersionQuery!powerByQuery.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="10" align="right">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>