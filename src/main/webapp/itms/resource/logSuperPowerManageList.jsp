<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����Ȩ����־����</title>
<%
	/**
	 * ����Ȩ����־����
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-08-07
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
	
	function ToExcel(){
		parent.ToExcel();
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>����Ȩ����־������Ϣ</caption>
	<thead>
		<tr>
			<th>Ȩ������</th>
			<th>������</th>
			<th>��������</th>
			<th>����ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="logList!=null">
			<s:if test="logList.size()>0">
				<s:iterator value="logList">
						<tr>
							<td><s:property value="authName" /></td>
							<td><s:property value="userName" /></td>
							<td><s:property value="operDesc" /></td>
							<td><s:property value="operTime" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû�г���Ȩ����־��Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="4" align="right">
		 	<lk:pages
				url="/itms/resource/logSuperPowerManage!getLogInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		<tr>
			<td colspan="4" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>