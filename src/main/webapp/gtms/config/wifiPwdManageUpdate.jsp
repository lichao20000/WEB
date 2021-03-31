<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- <table class="querytable"> -->
<table border=0 cellspacing=1 cellpadding=1 width="50%" class="listtable">

	<TR bgcolor="#FFFFFF">
		<th colspan="4">
			WIFI账号密码修改
		</th>
	</tr>
		<TR bgcolor="#FFFFFF">
			<TD class=column width="15%" align='right'>
				宽带账号
			</TD>
			<TD width="85%" colspan="3">
				<s:property value="wifiname" />
			</TD>
		</TR>
		<TR bgcolor="#FFFFFF">
			<TD class=column width="15%" align='right'>
				宽带密码
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
												value=" 保存 " style="CURSOR: hand" class=btn
												onclick="wifiupdatePwd()" /> 
			<!-- <a href="javascript:wifiupdatePwd()">保存</a> -->
		</td>
	</TR>
</table>
