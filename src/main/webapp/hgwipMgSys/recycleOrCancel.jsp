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
<!-- ��Ҫչʾ�б� -->
<s:if test="stat==0">
	<!-- ʡ���û� -->
	<s:if test="userstat==0">
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr class="green_title">
				<td colspan="3" align=center >���½ڵ��Ѿ�����</td>
			</tr>
			<tr class="green_title">
				<td >IP��ַ</td>
				<td >����λ��</td>
				<td >�������</td>
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
	<!-- �����û� -->
	<s:else>
		<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
			<tr class="green_title">
				<td colspan="4">һ�½ڵ��Ѿ�����</td>
			</tr>
			<tr class="green_title">
				<td class="blue_title">IP��ַ</td>
				<td class="blue_title">����λ��</td>
				<td class="blue_title">��ַ����</td>
				<td class="blue_title">�����û�</td>
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
		href="<s:url value="/hgwipMgSys/assignIP!cancelSubNet.action"><s:param value="attr" name="attr"/><s:param name="stat" value="0"/></s:url>">ȷ��ȡ���������Ļ���</a>
	</div>
</s:if>
<script type="text/javascript">
var status =<s:property value="stat"/>;
if(status==1)
{
alert("ȡ���������ֳɹ���");
parent.left.reload("<s:property value="attr"/>");
}
else if(status==2)
{
alert("ȡ������ʧ�ܣ����Ժ����ԣ�");
parent.left.reload("<s:property value="attr"/>");
}
</script>
</body>
</html>
<%@ include file="../foot.jsp"%>