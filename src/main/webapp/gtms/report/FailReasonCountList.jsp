<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title></title>

		<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
	</head>
	<body>
		<table class="listtable" width="100%">
			<thead>
				<tr bgcolor="#FFFFFF">
					<s:iterator value="titleList" var="titleList" status="servSt">
						<th align="center">
							<s:property />
						</th>
					</s:iterator>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="data" var="map1">
					<tr>
						<td nowrap class=column align='right'>
							<s:property value="failReason" />
						</td>
						<s:iterator value="cityList" var="map2">
							<td class=column align="center">
								<s:iterator value="map1" var="map1id" status="st">
									<s:if test="key.equals(city_id)">
										<s:property value="value" />
									</s:if>
								</s:iterator>
							</td>
						</s:iterator>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot>
				<tr>
					<td class=column  colspan='<s:property value="titleList.size()" />'>
						&nbsp;&nbsp;
						<a
							href="javascript:ToExcel('<s:property value="cityId"/>','<s:property value="monthDate"/>');">
							<img src="../../images/excel.gif" border="0" width="16"
								height="16"></img> </a>
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="7">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</body>
</html>
