<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- <table class="querytable"> -->
<table border=0 cellspacing=1 cellpadding=1 width="50%" class="listtable">

	<TR bgcolor="#FFFFFF">
		<th colspan="4">
			WIFI�˺������޸�
		</th>
	</tr>
		<TR bgcolor="#FFFFFF">
			<TD class=column width="15%" align='right'>
				����˺�
			</TD>
			<TD width="85%" colspan="3">
				<s:property value="wifiname" />
			</TD>
		</TR>
		<TR bgcolor="#FFFFFF">
			<TD class=column width="15%" align='right'>
				�������
			</TD>
			<TD width="85%" colspan="3">
			<input type="text" id= "wifipwdupdate"  name="wifipwdupdate" value='' size="20" maxlength="100" class=bk />
			</TD>
		</TR bgcolor="#FFFFFF">
		<input type="hidden" id= "deviceidupdate" name="deviceidupdate" value='<s:property value="deviceid"/>' />
		<input type="hidden" id= "wifipathupdate"  name="wifipathupdate" value='<s:property value="wifipath" />' />
	<tr bgcolor="#FFFFFF">
		<TD class=column width="15%" align='right'>
		</TD>
	</tr>
	<TR bgcolor="#FFFFFF">
		<td colspan="4" align="right" class=foot>
		<input type="button" name="button" class=jianbian
												value=" ���� " style="CURSOR: hand" class=btn
												onclick="wifiupdatePwd()" /> 
			<!-- <a href="javascript:wifiupdatePwd()">����</a> -->
		</td>
	</TR>
</table>
