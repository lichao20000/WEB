<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>IPTV开启ssid2</title>
	<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	<link href="../../css/listview.css" rel="stylesheet" type="text/css">
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
</head>
<div id="paramAddDiv">
	<table border=0 cellspacing=1 cellpadding=1 width="100%" bgcolor=#999999>
		<s:if test='specId==""'>
			<TR bgcolor="#FFFFFF" height="25">
				<td width="70%" colspan="4">
					<strong>未绑定用户,无法操作。</strong>
				</td>
			</TR>
		</s:if>
		<s:elseif test='specId=="2"||specId==3'>
			<TR bgcolor="#FFFFFF">
				<td width="70%" colspan="4">
					<strong>设备不支持此功能,无法操作。</strong>
				</td>
			</TR>
		</s:elseif>
		<TR bgcolor="#FFFFFF" height="25">
			<TD colspan="4" align="right" class="green_foot">
				<s:if test='specId==""||specId=="2"||specId=="3"'>
				</s:if>
				<s:else>
					<button type="button" id="doButton" onclick="doExecute();"
						class=btn>
						&nbsp;开&nbsp;启 &nbsp;
					</button>
				</s:else>
			</TD>
		</TR>
		<TR bgcolor="#FFFFFF">
			<TD colspan="4" align="left" class="green_foot">
				<div id="resultDIV" />
			</TD>
		</TR>
	</table>
</div>