<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ն�״̬ͳ��</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="99.9%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>
					�ն�״̬ͳ��
				</strong>
			</td>
			<td class=column1 align="right" width="10%">
				<a href="javascript:window.print()">��ӡ</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr>
						<th>����</th>
						<th>�Ѱ���</th>
						<th>δ����</th>
						<th>����</th>
					</tr>
					<s:iterator value="printList">
						<tr bgcolor="#FFFFFF">
							<td  bgcolor=#ffffff align=center><s:property value="city_name"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="binded"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="nobind"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="srate"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>