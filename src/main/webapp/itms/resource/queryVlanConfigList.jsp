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

	function exportList() {
		parent.exportList();
	}
	
	$(function(){
		$("button[@name='button']",parent.document).attr("disabled", false);
		$("#QueryData",parent.document).html("");
	});
	
	
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>��ͥ�������ò�ѯ</caption>
		<thead>
			<tr>
				<th>���</th>
				<th>����</th>
				<th>����</th>
				<th>�߼�ID</th>
				<th>����˺�</th>
				<th>����</th>
				<th>�豸���к�</th>
				<th>LAN 1</th>
				<th>LAN 2</th>
				<th>LAN 3</th>
				<th>LAN 4</th>
				<th>�Ƿ����쳣�ӿ�</th>
				<th>�ɼ�ʱ��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="vlanConfigList!=null">
				<s:if test="vlanConfigList.size()>0">
					<s:iterator value="vlanConfigList">
						<tr bgcolor="#FFFFFF" align="center">
							<td class="column1"><s:property value="index"/></td>
							<td class="column1"><s:property value="parentName"/></td>
							<td class="column1"><s:property value="cityName"/></td>
							<td class="column1"><s:property value="username"/></td>
							<td class="column1"><s:property value="netAccount"/></td>
							<td class="column1"><s:property value="vendorName"/></td>
							<td class="column1"><s:property value="deviceSn"/></td>
							<td class="column1"><s:property value="lan1"/></td>
							<td class="column1"><s:property value="lan2"/></td>
							<td class="column1"><s:property value="lan3"/></td>
							<td class="column1"><s:property value="lan4"/></td>
							<td class="column1"><s:property value="isErrPort"/></td>
							<td class="column1"><s:property value="gatherTime"/></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="13" align="left" style="background-color: white;">
							<font color="red">û����زɼ���Ϣ!</font>
						</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=13>û����زɼ���Ϣ!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="13" align="right">[ ͳ������ : <s:property value='queryCount'/> ]&nbsp;
					<lk:pages
						url="/itms/resource/queryVlanConfig!queryVlanConfigList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="13" align="right">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="exportList()">
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>