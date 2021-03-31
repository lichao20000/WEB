<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������������Ϣ</title>
<%
	/**
	 * ��������������Ϣ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2010-09-16
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

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	/**
	function ToExcel(){
			parent.ToExcel();
	}
	*/
	
	function queryDetail(task_id,device_id){
        var page =  "<s:url value='/itms/service/monitorFlowInfoQuery!getePonLanInfo.action'/>?task_id="+task_id+"&device_id="+device_id;
		window.open(page,"","left=20,top=20,width=1000,height=600,resizable=no,scrollbars=yes");
	}
	
	function deleteTask(task_id){
		var url = "<s:url value='/itms/service/monitorFlowInfoQuery!deleteMonitorFlow.action'/>";
		$.post(url,{task_id:task_id},function(ajax){
				alert(ajax);
				parent.query();
			});
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>��������������Ϣ</caption>
	<thead>
		<tr>
			<th>����ID</th>
			<th>�豸���к�</th>
			<th>��ʼʱ��</th>
			<th>����ʱ��</th>
			<th>ʱ���������ӣ�</th>
			<th>�ɼ�����</th>
			<th>�ɼ�״̬</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="monitorList!=null">
			<s:if test="monitorList.size()>0">
				<s:iterator value="monitorList">
						<tr>
							<td><s:property value="task_id" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="start_time" /></td>
							<td><s:property value="end_time" /></td>
							<td><s:property value="interval" /></td>
							<td><s:property value="times" /></td>
							<td><s:property value="status" /></td>
							<td><a href="javaScript:queryDetail('<s:property value="task_id" />','<s:property value="device_id" />')" ><b>�鿴�ɼ����� </b></a>&nbsp;&nbsp;|&nbsp;&nbsp;
								<a href="javaScript:deleteTask('<s:property value="task_id" />')"><b>ɾ��</b></a></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="8" align="right">
		 	<lk:pages
				url="/itms/service/monitorFlowInfoQuery!getMonitorFlowQuery.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
<!--  ����Excel
		<tr>
			<td colspan="8" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
-->
	</tfoot>
</table>
</body>
</html>