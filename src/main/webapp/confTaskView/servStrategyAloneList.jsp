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
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>

</head>

<body>
	<form name="selectList" action=""/>
		<input type="hidden" name="task_id" value="<s:property value="task_id"/>">
		<input type="hidden" name="status" value="<s:property value="status"/>">
		<input type="hidden" name="device_serialnumber" value="<s:property value="device_serialnumber"/>">
		<input type="hidden" name="service_id" value="<s:property value="service_id"/>">
	</form>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#999999">
					<tr>
						<th>ҵ���ʺ�</th>
						<th>�豸���к�</th>
						<th>ҵ������</th>
						<!-- <th>����</th> -->
						
						<th>����״̬</th>
						<th>���Խ��</th>
						<!--<th>�������</th>-->
						<th>����ʱ��</th>
						<th>ִ��ʱ��</th>
					</tr>
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
							
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="8" align="right">
				<lk:pages url="/servStrategy/ServStrategyAlone!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>