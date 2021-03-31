<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>最新版本统计展现</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">



</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr bgcolor="#FFFFFF">
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
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr bgcolor="#FFFFFF">
						<s:iterator value="titleList" var="titleList" status="servSt">
							<th><s:property /></th>
						</s:iterator>
					</tr>
					<s:iterator value="dataList" var="dataList">
						<tr bgcolor="#FFFFFF">
							<td nowrap class=column rowspan="<s:property value="childInt"/>" align='right'>
								<s:property value="vendor_name"/>
							</td>
							<s:iterator value="childList">
								<td nowrap class=column rowspan="<s:property value="childInt"/>" align='right'>
									<s:property value="device_model"/>
								</td>
								<s:iterator value="num" var="num">
									<s:iterator status="typeList">
										<s:if test="#typeList.getIndex()==0">
											<td class=column><s:property /></td>
										</s:if>
										<s:else>
											<td bgcolor="#FFFFFF"><s:property /></td>
										</s:else>
									</s:iterator>
								</tr>
								</s:iterator>
							</s:iterator>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr >
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr bgcolor="#FFFFFF">
						<td class=column>
							&nbsp;&nbsp;
							<a href="javascript:queryDataForExcel('<s:property value="cityId"/>','<s:property value="add_time"/>');">
					 			<img src="../../images/excel.gif"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
