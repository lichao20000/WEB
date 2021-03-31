<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
/**
 * E8-C�ն�ʹ�����
 * 
 * @author gaoyi
 * @version 1.0
 * @since 2013-12-18
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
	function openCity(city_id){
			parent.openCity(city_id);
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>E8-C�ն�ͳ����� </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�ն�����</th>
			<th>�����ն�����</th>
			<th>2+1���</th>
			<th>4+2���</th>
			<th>�����ն���</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="terminalList!=null">
			<s:if test="terminalList.size()>0">
				<s:iterator value="terminalList">
						<tr>
							<td>
							<a href="javaScript:openCity('<s:property value="city_id" />');"><s:property value="city_name" /></a>
							</td>
							<td><s:property value="allNum" /></td>
							<td><s:property value="newNum" /></td>
							<td><s:property value="twoNum" /></td>
							<td><s:property value="fourNum" /></td>
							<td><s:property value="egwNum" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>ϵͳû��e8-c�ն���Ϣ !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
 
	<tfoot>
		<!-- 
			<tr>
				<td colspan="6" align="right">
			 	<lk:pages
					url="/itms/resource/TerminalE8CInfo!getTerminalE8CInfo.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		-->
		<tr>
			<td colspan="6" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>