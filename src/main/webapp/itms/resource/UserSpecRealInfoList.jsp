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
	 * e8-cҵ���ѯ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-08-15
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
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>ʵ���ն˹����BSS�ն˹��һ�±ȶ�ҵ����Ϣ</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�û�ʵ���ն˹��</th>
			<th>BSS�ն˹��</th>
			<th>�û���</th>
			<th>ռ��</th>
		</tr>
	</thead>
	<tbody>
	
		<s:if test="userSpecList!=null">
			<s:if test="userSpecList.size()>0">
				<s:iterator value="userSpecList">
				<tr>
					<td><s:property value="city_name"/> </td>
					<td><s:property value="userspec_name"/> </td>
					<td><s:property value="bssspec_name"/> </td>
					<td><s:property value="molecular"/> </td>
					<td><s:property value="pert"/> </td>
				</tr>
				</s:iterator>	
			</s:if>
			<s:else>
				<td colspan=5>û�з��������Ĳ�һ����Ϣ!</td>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>
<script>
</script>

</html>