<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
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

</script>
<%--
	/**
	 * description
	 * @author zszhao6(Ailk No.78987)
	 * @version 1.0
	 * @since 2018-11-12
	 * @copyright Ailk NBS-Network Mgt. RD Dept.
	 * 
	 */
 --%>
	<title>��ͥ��������������������ѯ</title>
	
	<style type="text/css">
td {
text-align:center; /*����ˮƽ����*/
vertical-align:middle;/*���ô�ֱ����*/
}
</style>
</head>
<body>
<table  class="listtable" id="listTable">
				<caption>������� </caption>
				<thead>
							<tr>
							<th>����ID</th>
							<th>�豸���к�</th>
							<th>����״̬ </th>
							<th>����ִ��״̬ </th>
							<th>��ʼʱ��</th>
							<th>����ʱ��</th>
							</tr>
				</thead>
		<tbody>
			<s:if test="servStragegyCounts!=null">
				<s:if test="servStragegyCounts.size()>0">
					<s:iterator value="servStragegyCounts">
						<tr>
							<td><s:property value="strategyId"/></td>
							<td><s:property value="device_serialnumber"/></td>
							<td><s:property value="status"/></td>
							<td><s:property value="result"/></td>
							<td><s:property value="startTime"/></td>
							<td><s:property value="endTime"/></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="8">δͳ�Ƶ�������Ϣ</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5>δͳ�Ƶ�������Ϣ</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8" align="right"><lk:pages
						url="/servStrategy/ServStrategy!getStrategyCounts.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>