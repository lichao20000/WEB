<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition","attachment; filename=stbDeviceCount.xls" );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>山东联通RMS平台机顶盒设备统计</title>
</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align=center>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							机顶盒设备统计
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table border="1" cellspacing=0 cellpadding=0 width="95%" align=center>
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
						<s:property value="vendorName" />
					</td>
					<s:iterator value="cityList" var="map2">
						<td align="center">
							<s:iterator value="map1" var="map1id" status="st">
								<s:if test="key.equals(city_id)">
									<s:property value="value" />
								</s:if>
							</s:iterator>
						</td>
					</s:iterator>
					<td align="center">
						<s:property value="total" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>
