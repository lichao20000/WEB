<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>recycle or cancel ip</title>
</head>
<body>
<!-- 需要展示列表 -->
<s:if test="stat==0">
	<!-- 省级用户 -->
	<s:if test="userstat==0">
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr class="green_title">
				<td colspan="3" align=center >以下节点已经分配</td>
			</tr>
			<tr class="green_title">
				<td >IP地址</td>
				<td >掩码位数</td>
				<td >分配地市</td>
			</tr>
			<s:iterator value="result">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="subnet" /></td>
					<td><s:property value="inetmask" /></td>
					<td><s:property value="city_name" /></td>
				</tr>
			</s:iterator>
		</table>
	</s:if>
	<!-- 地市用户 -->
	<s:else>
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr class="green_title">
				<td colspan="4">一下节点已经分配</td>
			</tr>
			<tr class="green_title">
				<td class="blue_title">IP地址</td>
				<td class="blue_title">掩码位数</td>
				<td class="blue_title">地址个数</td>
				<td class="blue_title">分配用户</td>
			</tr>
			<s:iterator value="result">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="subnet" /></td>
					<td><s:property value="inetmask" /></td>
					<td><s:property value="addrnum" /></td>
					<td><s:property value="usernamezw" /></td>
				</tr>
			</s:iterator>
		</table>
	</s:else>
	<p></p>
	<div style="width: 98%; margin-left: 1%;">
	<a
		href="<s:url value="/hgwipMgSys/assignIP!cancelSubNet.action"><s:param value="attr" name="attr"/><s:param name="stat" value="0"/></s:url>">确认取消该子网的划分</a>
	</div>
</s:if>
<script type="text/javascript">
var status =<s:property value="stat"/>;
if(status==1)
{
alert("取消子网划分成功！");
parent.left.reload("<s:property value="attr"/>");
}
else if(status==2)
{
alert("取消子网失败，请稍候再试！");
parent.left.reload("<s:property value="attr"/>");
}
</script>
</body>
</html>
<%@ include file="../foot.jsp"%>