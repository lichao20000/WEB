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
<title>按版本统计设备</title>

</head>
<body>
	<table class="listtable" border=1 cellspacing=0 cellpadding=0>

		<thead>
				<tr>
					<s:iterator value="titleList" var="titleList" status="servSt">
						<th nowrap><s:property /></th>
					</s:iterator>
				</tr>
</thead>
<tbody>
<s:if test="dataList.size()>0">
				<s:iterator value="dataList" var="dataList">
						<tr>
							<td  nowrap rowspan="<s:property value="childInt"/>" align='right'>
								<s:property value="vendor_name"/>
							</td>
							<s:iterator value="childList">
								<td nowrap rowspan="<s:property value="childInt"/>" align='right'>
									<s:property value="device_model"/>
								</td>
								<s:iterator value="num" var="num">
									<s:iterator status="typeList">
										<s:if test="#typeList.getIndex()==0">
											<td align='right'><s:property escapeHtml="false"/></td>
										</s:if>
										<s:else>
											<td  align='right'><s:property escapeHtml="false" /></td>
										</s:else>
									</s:iterator>
									</tr>
								</s:iterator>
								</s:iterator>
					</s:iterator>
	<tr>
				<td colspan="6" align='right'>小计</td>
	<s:iterator value="totalNumList" var="totalNumList">
<td nowrap  align='right'><s:property escapeHtml="false" /></td>
</s:iterator></tr>



</s:if>
<s:else>
			<tr>
				<td colspan=21>系统没有该条件的设备！</td>
			</tr>
		</s:else>

</tbody>

</table>

</body>
</html>
