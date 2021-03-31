<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=FailReasonCount.xls" );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>错误码统计展现</title>

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
					<s:iterator value="data" var="map1">
						<tr bgcolor="#FFFFFF">
							<td nowrap class=column align='right'>
								<s:property value="failReason"/>
							</td>
							<s:iterator value="cityList" var="map2">
								<td  class=column align="center">
									<s:iterator value="map1" var="map1id" status="st">
									    <s:if test="key.equals(city_id)">
												<s:property value="value" />
										</s:if>
									</s:iterator>
								</td>
							</s:iterator>
						 </tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
