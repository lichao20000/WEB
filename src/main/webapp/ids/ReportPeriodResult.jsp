<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD  align="center" >
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TH bgcolor="#ffffff" colspan="4" align="center">上报周期变更定制结果</TH>
		</TR>
		<TR bgcolor="#FFFFFF" width="4%" id="device" class="green_title2">
						<TD class=column align="right" width="15%" nowrap>定制结果：</TD>
						<TD class=column align="left" width="15%"><s:property value="ajax"/></TD>
					</TR>
	</TABLE>
</TD></TR>
</TABLE>