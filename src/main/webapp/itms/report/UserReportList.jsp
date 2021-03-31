<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量导入查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.isNotShow();
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}

</script>
</head>
<body>
				<s:if test="userList.size()>0">
<table class="listtable" id="listTable">
		<caption>批量导入查询</caption>
		<thead>
			<tr>
				<TH align="center">原始文件内容</TH>
				<TH align="center">属地</TH>
				<TH align="center">用户LOID</TH>
				<TH align="center">宽带账号</TH>
				<TH align="center">ITV账号</TH>
				<TH align="center">语音账号</TH>
			</tr>
		</thead>
		<tbody>
			
					<s:iterator value="userList">
						<tr>
							<td ><s:property value="content" /></td>
								<td ><s:property value="city_name" /></td>
								<td ><s:property value="loid" /></td>
								<td ><s:property value="net_account" /></td>
								<td ><s:property value="itv_account" /></td>
								<td ><s:property value="voip_account" /></td>
						</tr>
					</s:iterator>
				
		</tbody>
		<tfoot>
		<tr>
							<td colspan="6" align="right"><lk:pages
									url="/itms/report/exportUser!goPage.action" styleClass=""
									showType="" isGoTo="true" changeNum="true" /></td>
						</tr>
						</tfoot>
		
	</table>
	<div style=' float: left'><IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand; float: left' onclick="ToExcel()"></div>
	</s:if>
			<s:else>
				系统没有匹配到相应的数据！
			</s:else>
</body>
</html>