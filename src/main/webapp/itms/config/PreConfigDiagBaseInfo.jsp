<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
	</SCRIPT>


<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
	<tr align="left">
		<td colspan="4" class="green_title_left">
			������Ϣ
		</td>
	</tr>
	<TR bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">
			�û��ʺ�:
		</TD>
		<TD width="35%">
			<s:property value="baseInfoMap.username" />
		</TD>
		<TD class=column align="right" width="15%">
			�豸���к�:
		</TD>
		<TD width="35%">
			<s:property value="baseInfoMap.deviceSN" />
		</TD>
	</TR>

	<TR bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap>
			�Ƿ�ȷ��:
		</TD>
		<TD>
			<s:property value="baseInfoMap.userline" />
		</TD>
		<TD class=column align="right">
			�ն˻�Ծ״̬:
		</TD>
		<TD>
			<s:property value="baseInfoMap.huoyue" />
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap>
			�ն���ƽ̨��������״̬:
		</TD>
		<TD>
			<s:property value="baseInfoMap.onlinestatus" />
			<input type="hidden" value="<s:property value="baseInfoMap.onlinestatus" />" name="onlinestatus1">
		</TD>
		<TD class=column align="right" ></TD>
		<TD></TD>
	</TR>
</TABLE>
