<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=countRate.xls " );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>返修终端报表统计</title>

</head>
<body>
	<table border=1 cellspacing=1 cellpadding=2 width="100%" align="center">
		<tr >
			<td >
				<table border=1 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr >
						<s:iterator value="titleList" var="titleList" status="servSt">
							<th><s:property /></th>
						</s:iterator>
					</tr>
					<s:iterator value="dataList" var="dataList">
						<tr bgcolor="#FFFFFF">
							<td nowrap rowspan="3" align='right'>
								<s:property value="vendor_name"/>
							</td>
								<s:iterator value="childList" var="childList" status="u">
								<s:if test="#u.index == 0">
									<s:iterator status="typeList">
										<td ><s:property /></td>
									</s:iterator>
								</s:if>
								<s:else>
								<tr>
									<s:iterator status="typeList">
										<td ><s:property /></td>
									</s:iterator>
								</tr>
								</s:else>
								</s:iterator>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
