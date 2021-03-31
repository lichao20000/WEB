<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����豸��Ϣ��ѯ</title>
<%
	/**
	 * �����豸��Ϣ��ѯ
	 * 
	 * @author zhangsb2
	 * @version 1.0
	 * @since 2013-10-21
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th>����</th>
				<th>������</th>
				<th>LOID</th>
				<th>�豸���к�</th>
				<th>���͹⹦��</th>
				<th>���չ⹦��</th>
				<th>���յ�ַ</th>
				<th>OLT����</th>
				<th>OLTIP</th>
				<th>PON�˿�</th>
				<th>���ִ���</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="infoList!=null">
				<s:if test="infoList.size()>0">
					<s:iterator value="infoList">
						<tr>
							<td><s:property value="area_name" /></td>
							<td><s:property value="subarea_name" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="tx_power" /></td>
							<td><s:property value="rx_power" /></td>
							<td><s:property value="linkaddress" /></td>
							<td><s:property value="olt_name" /></td>
							<td><s:property value="olt_ip" /></td>
							<td><s:property value="pon_id" /></td>
							<td><s:property value="count_num" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>û���豸��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right"><lk:pages
						url="ids/DegradationPathsQuery!getDegradationPathsInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>

		<tr STYLE="display: none">
			<td colspan="11"><iframe id="childFrm" src=""></iframe></td>
		</tr>
	</table>
</body>
</html>