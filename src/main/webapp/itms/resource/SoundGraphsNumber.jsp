<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ѯ���</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th width="20%">��ͼ�̺�λ��</th>
				<th width="80%">������ͼֵ</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="Date != null ">
			<s:if test="Date.size() > 0">
				<s:iterator value="Date">
					<tr>
						<td>
							<s:property value="remark" />
						</td>
						<td>
							<s:property value="digit_map_value" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
	</tbody>
	</table>
</body>
</html>