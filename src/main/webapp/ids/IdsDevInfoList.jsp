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

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function ListToExcel(taskId) {
		var page = "<s:url value='/ids/idsStatusQuery!getDevInfoExcel.action'/>?"
				+ "taskId="+ taskId;
		document.all("childFrm").src = page;
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>oui</th>
			<th>�豸���к�</th>
			<th>�·����</th>
			<th>״̬</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList!=null">
			<s:if test="devList.size()>0">
				<s:iterator value="devList">
						<tr>
							<td><s:property value="oui" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="result_id" /></td>
							<td><s:property value="status" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>û���豸��Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="4" align="right">
		 	<lk:pages
				url="/ids/idsStatusQuery!getDevInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		<tr>
			<td colspan="4">
			<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="taskId"/>')">
			</td>
		</tr>
	</tfoot>
	
	<tr STYLE="display: none">
		<td colspan="4"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
</body>
</html>