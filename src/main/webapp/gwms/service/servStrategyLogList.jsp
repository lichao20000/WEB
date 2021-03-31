<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ�����ò�����ͼ</title>
<%
	/**
		 * ҵ�����ò�����ͼ
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2008-12-18
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>

</head>

<body>

<table class="listtable">
	<caption>
		ͳ�ƽ��
	</caption>
	<thead>
		<tr>
			<th>�û��ʺ�</th>
			<th>�豸���к�</th>
			<th>ҵ������</th>
			<!-- <th>����</th> -->
			
			<th>����״̬</th>
			<th>���Խ��</th>
			<!--<th>�������</th>-->
			<th>����ʱ��</th>
			<th>ִ��ʱ��</th>
			<th>���Է�ʽ</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="servStrategyList">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="username"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<td class=column1><s:property value="serv_type_name"/></td>
				<!--<td class=column1><s:property value="oui"/></td> -->
				
				<td class=column1><s:property value="status"/></td>
				<td class=column1><s:property value="result_id"/></td>
				<!--<td class=column1><s:property value="result_desc"/></td>-->
				<td class=column1><s:property value="time"/></td>
				<td class=column1><s:property value="start_time"/></td>
				<td class=column1><s:property value="type_name"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8" align="right">
				<lk:pages url="/gwms/service/servStrategyLog!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</tfoot>
</body>
</html>