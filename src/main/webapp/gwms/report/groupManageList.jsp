<%@ page import="com.linkage.module.gwms.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%
String status = StringUtil.getStringValue(request.getParameter("status"));
String cityId = StringUtil.getStringValue(request.getParameter("cityId"));
%>

<title>������������Ϣ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});


	function ToExcel(){
		var cityId = '<%=cityId%>';
		var status = '<%=status%>';
		var page="<s:url value='/gwms/report/groupManage!getDetailExcel.action'/>";
		page+="?cityId="+cityId+"&status="+status;
		document.all("childFrm").src=page;
	}
</script>

<table class="listtable">
	<caption>
		ͳ�ƽ��
	</caption>
	<thead>
		<tr>
			<th>OUI</th>
			<th>�ն����к�</th>
			<th>�ն�Ψһ��ʶ</th>
			<th>����˺�</th>
			<th>�ն˳���</th>
			<th>�ͺ�����</th>
			<th>�û�����</th>
			<th>�û�סַ</th>
			<th>��ϵ�绰</th>
			<th>�û���ע</th>
			<th>�ն�Ӳ���汾</th>
			<th>����汾</th>
			<th>IP��ַ</th>
			<th>�ն˹�����·��</th>
			<th>��ע</th>
			<th>�ն�ע��״̬</th>
			<th>�߼�ID</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="data">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="oui"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<td class=column1><s:property value="osn"/></td>
				<td class=column1><s:property value="username"/></td>
				<td class=column1><s:property value="vendor_name"/></td>
				<td class=column1><s:property value="device_model"/></td>
				<td class=column1><s:property value="linkman"/></td>
				<td class=column1><s:property value="linkaddress"/></td>
				<td class=column1><s:property value="mobile"/></td>
				<td class=column1><s:property value="userremark"/></td>
				<td class=column1><s:property value="hardwareversion"/></td>
				<td class=column1><s:property value="softwareversion"/></td>
				<td class=column1><s:property value="loopback_ip"/></td>
				<td class=column1><s:property value="city_name"/></td>
				<td class=column1><s:property value="remark"/></td>
				<td class=column1><s:property value="openstatus"/></td>
				<td class=column1><s:property value="loid"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="17" align="right">
				<lk:pages url="/gwms/report/groupManage!getGroupManageList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tr>
			<td colspan="17" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					 style='cursor: hand' onclick="ToExcel()"></td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="17">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
