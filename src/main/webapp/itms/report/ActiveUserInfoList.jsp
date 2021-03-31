<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ÿ�»�Ծ�û�ͳ��</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

%>

<script type="text/javascript">			
$(function() {
	parent.dyniframesize();
});

function ToExcel() {
	parent.ToExcel();
}
</script>
</head>

<body>
<table  class="listtable" id="listTable">
				<caption>�����л�Ծ�û�ͳ�� </caption>
				<thead>
							<tr>
							<th>���������к�</th>
							<th>IPTV�˺�</th>
							<th>����</th>
							<th>�ͺ�</th>
							<th>����</th>
							<th>Ӳ���汾</th>
							<th>����汾</th>
							<th>���һ������ʱ��</th>
							</tr>
				</thead>
				<tbody>
			<s:if test="activeUserList!=null">
				<s:if test="activeUserList.size()>0">
					<s:iterator value="activeUserList">
						<tr>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="serv_account" /></td>
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="cityName" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="lasttime" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="8">δͳ�Ƶ��û���Ϣ</td>
					</tr>
				</s:else>
						</s:if>
						<s:else>
			<tr>
				<td colspan=5>δͳ�Ƶ��û���Ϣ</td>
			</tr>
		</s:else>
		</tbody>
			<tfoot>
			<tr>
				<td colspan="6"  align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
				</td>
				<td colspan="2"  align="right">
				<lk:pages url="/itms/report/activeUserReport!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
			</tfoot>
	</table>
</body>
</html>