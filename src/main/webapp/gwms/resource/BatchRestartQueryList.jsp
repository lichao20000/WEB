<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ʱ���������߹�è������������</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
	<table class="listtable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>�����</th>
				<th>��������</th>
				<th>ִ��״̬</th>
				<th>����ʱ��</th>
				<th>ִ��ʱ��</th>
				<th>ִ������</th>
				<th>ִ�гɹ���</th>
				<th>ִ��ʧ����</th>
				<th>��ִ����</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="taskList!=null">
				<s:if test="taskList.size()>0">
					<s:iterator value="taskList">
						<tr>
							<td align="center"><s:property value="task_name" /></td>
							<td align="center"><s:property value="task_desc" /></td>
							<td align="center"><s:property value="task_status" /></td>
							<td align="center"><s:property value="add_time" /></td>
							<td align="center"><s:property value="start_time" /></td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','total')"><s:property value="total" /></a>
							</td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','succ')"><s:property value="succ" /></a>
							</td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','fail')"><s:property value="fail" /></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="task_name" />','wait')"><s:property value="wait" /></a>
							</td>
							<td align="center">
							<!-- δִ�е��������ɾ������ͣ -->
								<s:if test="1==status">
									<a href="javascript:operTask('<s:property value="task_name" />','1')">ɾ��</a>|
									<a href="javascript:operTask('<s:property value="task_name" />','2')">��ͣ</a>
								</s:if> 
								<s:elseif test="-2==status">
								<a href="javascript:operTask('<s:property value="task_name" />','1')">ɾ��</a>|
								  <a href="javascript:operTask('<s:property value="task_name" />','3')">����</a>
								</s:elseif> 
								<s:elseif test="2!=status&&3!=status">
								 <a href="javascript:operTask('<s:property value="task_name" />','1')">ɾ��</a>
								</s:elseif>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=10>û�в�ѯ���������</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>û�в�ѯ���������</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/gwms/resource/batchRestartManagerACT!qryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
			 <tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="javascript:toExcel()"></td>
			</tr> 
		</tfoot>

	</table>
</body>
<script>

function operTask(taskId,operType)
{
	parent.operTask(taskId,operType);
}


function toExcel(){
	parent.toExcel();
}

function detail(taskId,type){
	parent.detail(taskId,type);
}
</script>
</html>