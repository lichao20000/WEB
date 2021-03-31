<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function retum()
{
	window.history.go(-1);
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=40>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="450" border=0 cellspacing=1 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=1 cellspacing=0 cellpadding=2 width="100%">
					<TR>
						<TH align="center" colspan="2">操作提示信息</TH>
					</TR>
						<TR height="50">
							<TD align=center valign=middle class=column width="20%">
								<img src="<s:url value="/images/sorry.gif"/>" width="64" height="64">
							</TD>
							<TD class=column>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"><font color="#33689C" size="2pt"><strong> 禁止重复提交，请返回 ！</strong></font>
							</TD>
							
						</TR>
					<TR>
						<TD class=foot align="right" colspan="2">
							<button onclick="retum();"> 返 回 </button>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>