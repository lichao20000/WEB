<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������������б�</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function detail(taskId,type) {
		parent.detail(taskId,type);
	}
	
	function ToExcel(){
		parent.toExcel();
	}

</script>
</head>
<body>
	<table class="listtable" id="listTable">
	<caption>��������������</caption>
	<thead>
		<tr>
			<th align="center">����</th>
			<th align="center">�����ɹ�����</th>
			<th align="center">����ʧ������</th>
			<th align="center">��������</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="restartDevList!=null">
			<s:if test="restartDevList.size()>0">
				<s:iterator value="restartDevList">
						<tr>
							<td align="center"><s:property value="time"/></td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="taskId" />','succ')"><s:property value="succ"/></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="taskId" />','fail')"><s:property value="fail"/></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="taskId" />','total')"><s:property value="total"/></a>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>û����ز�ѯ��Ϣ��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>û����ز�ѯ��Ϣ</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="7" align="right"><lk:pages
						url="/itms/report/batchRestart!qryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		 <tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="javascript:ToExcel()"></td>
			</tr> 
		</tfoot>
	</table>
</body>
</html>