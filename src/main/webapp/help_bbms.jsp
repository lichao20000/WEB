<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");

    
	String strVersion = LipossGlobals.getLipossProperty("Version");
%>

<%@page import="com.linkage.litms.LipossGlobals"%>

<html>
<head>
<title><%=LipossGlobals.getLipossName()%></title>

</head>
<body bgcolor="#FFFFFF">
<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td colspan="2" align="center" colspan="2"><img src="./images/help_top_bbms.jpg" width="490" height="80"></td>
	</tr>
</table>
<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td height="10"></td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">��˾�������Ƽ�(�Ͼ�)���޹�˾</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">ϵͳ���ƣ�����BBMS�����ն˹���ϵͳ���</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">ϵͳ��ƣ� BBMS</td>
	</tr>
	<tr>
		<td align="left" style="font-size:9pt;" colspan="2">�汾��V<%=strVersion%></td>
	</tr>
	<tr>
		<td height="10"></td>
	</tr>
	<tr>
		<td align="center" colspan="2"><textarea cols=50 rows=6 name=a2 style="background-color:FFFFFF">����BBMS�����ն˹���ϵͳ��BizBox Management System�����BBMS���ǿ��ҵ�����ƽ̨��һ����ϵͳ��ͨ����BOSS������ҵ��ϵͳ�Ľ�����ʵ�ֶԿ���ն˵ļ��й����ҵ���ͳһ���𡣲��û���DSL��̳TR-069 �ܹ���Զ�̹�����������ʵ����ҵ���غͶ��������ն��豸���Զ����á�״̬�����ܼ��ӡ�������ϡ����/�̼������ȹ��ܡ�</textarea></td>
	</tr>
	<tr>
		<td height="10"></td>
	</tr>
</table>
<div style="background:url(./images/help_down.jpg);width=490;height=80">
	<table width="10" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td>
			<table width="400" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
					<div>
						<img src="./images/linkage_logo.jpg" width="80" height="60">
					</div>
				</td>			
				<td align="left" style="font-size:9pt;">��Ȩ���� (C) 2009-2010 Linkage Corp.<input type="button" value=" ȷ�� " style="height:20pt;font-size:10pt;" onclick="javascript:window.close()">
				</td>
			</tr>
			</table>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
