<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��������ͳ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<% 
	request.setCharacterEncoding("GBK");
%>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	parent.isButn(true);
});

function ToExcel() {
	parent.ToExcel();
}
</script>
</head>

<body>
<table class="listtable" id="listTable">
	<caption>������ٽ��ͳ��</caption>	
	<thead>
		<tr>
			<th>����</th>
			<th>Ӫҵ��</th>
			<th>LOID</th>
			<th>����˺�</th>
			<th>�û��������</th>
			<th>����</th>
			<th>�ͺ�</th>
			<th>���ٽ��</th>
			<th>���ٽ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="speedRetList!=null && speedRetList.size()>0">
			<s:iterator value="speedRetList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="county_name" /></td>
						<td><s:property value="loid" /></td>
						<td><s:property value="username" /></td>
						<td><s:property value="bandwidth" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="devicemodel" /></td>
						<td><s:property value="speed_ret" />MB/S</td>
						<td><s:property value="result" /></td>
					</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="9">û�в��ٽ����Ϣ</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right">
				<lk:pages url="/itms/resource/speedTest1000M!query.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<tr>
			<td colspan="9" align="right">
				<img SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()" >
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>