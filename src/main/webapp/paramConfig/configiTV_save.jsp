<%@ include file="../timelater.jsp"%>
<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg="";

strMsg = StrategyBean.configiTV_SAVE(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	window.location.href = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">网关客户资源操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('./configiTV_strategy_search.jsp')" value=" IPTV配置查询 " class=btn >
						<INPUT TYPE="button" NAME="cmdBack" onclick="history.go(-1)" value=" 返 回 " class=btn>
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