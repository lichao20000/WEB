<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ն��滻��ͳ�Ʊ���</title>
<%
/**
 * �ն��滻��ͳ�Ʊ���
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
</script>
</head>
<body>

<table class="listtable" id="listTable">
	<caption>�ն��滻��ͳ����� </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�ն˸�����</th>
			<th>�ն�����</th>
			<th>�ն��滻��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="terminalList!=null">
			<s:if test="terminalList.size()>0">
				<s:iterator value="terminalList">
						<tr>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="oper_num" /></td>
							<td><s:property value="all_num" /></td>
							<td><s:property value="percentage" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû���ն��滻��ͳ����Ϣ !</td>
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
			<td colspan="4" align="right"><IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()"  />
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>