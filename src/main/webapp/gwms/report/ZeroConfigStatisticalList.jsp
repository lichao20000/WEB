<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置统计</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					零配置统计
				</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#000000">
					<s:iterator value="zeroDataList" var="zeroDataList" status="servSt">
						<s:if test="#servSt.index==0">
							<tr bgcolor="#FFFFFF">
								<th><strong><s:property value="city_name"/></strong></th>
								<th><strong><s:property value="num1"/></strong></th>
								<th><strong><s:property value="num2"/></strong></th>
								<th><strong><s:property value="avg"/></strong></th>
							</tr>
						</s:if>
						<s:else>
							<tr bgcolor="#FFFFFF">
									<s:if test='"true".equals(hasCityId)'>
										<td class=column>
											<a href="javascript:queryData('<s:property value="city_id"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
									 			<s:property value="city_name"/>
											</a>
										</td>
									</s:if>
									<s:else>
										<td class=column><s:property value="city_name"/></td>
									</s:else>

								<s:if test='"0".equals(num1)'>
									<td><s:property value="num1"/></td>
								</s:if>
								<s:else>
									<td>
										<a href="javascript:queryDataBind('<s:property value="city_id"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
									 		<s:property value="num1"/>
										</a>
									</td>
								</s:else>
								<s:if test='"0".equals(num2)'>
									<td><s:property value="num2"/></td>
								</s:if>
								<s:else>
									<td>
										<a href="javascript:queryDataNoBind('<s:property value="city_id"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
									 		<s:property value="num2"/>
										</a>
									</td>
								</s:else>
								<td><s:property value="avg"/></td>
								</tr>
							</s:else>
						</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="right"
					bgcolor="#000000">
					<s:if test='"print".equals(isReport)'>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100">
							 	<a href="javascript:window.print()">打印</a>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100">
							 	<a href="javascript:queryDataForPrint('getDayReport','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/print.gif" border="0" width="16" height="16"></img>
							 	</a>
							 	&nbsp;
							 	<a href="javascript:queryDataForExcel('getDayReport','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/excel.gif" border="0" width="16" height="16"></img>
							 	</a>
							 	&nbsp;
								<a href="javascript:queryDataForPdf('getDayReport','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/pdf.gif" border="0" width="16" height="16"></img>
							 	</a>
							</td>
						</tr>
					</s:else>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
