<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=devicetype.xls" );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>最新版本统计展现</title>

</head>
<body>
	<table border=1 cellspacing=1 cellpadding=2 width="100%" align="center">
		<tr >
			<td class=column1 height="25" >
				<table border=1 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr >
						<td>
							<strong>
								最新版本统计
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
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
							<td nowrap rowspan="<s:property value="childInt"/>" align='right'>
								<s:property value="vendor_name"/>
							</td>
							<s:iterator value="childList">
								<td nowrap rowspan="<s:property value="childInt"/>" align='right'>
									<s:property value="device_model"/>
								</td>
								<s:iterator value="num" var="num">
									<s:iterator status="typeList">
										<td ><s:property /></td>
									</s:iterator>
								</tr>
								</s:iterator>
							</s:iterator>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
