<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.io.PrintWriter"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<%
request.setCharacterEncoding("GBK");
String strMsg="";
strMsg = (String)serverManage.serverAct(request);

String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
//山西联通文件服务器保存 直接返回数据
if(area.equals("sx_lt")){
	PrintWriter pw = response.getWriter();
	pw.print(strMsg);
	pw.flush();
	pw.close();
	return;
}

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<BR><BR><BR>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">操作提示信息</TH>
					</TR>
					<TR  height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=green_foot align="right">
							<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('server_list.jsp')" value=" 列 表 " class=btn>
							&nbsp;&nbsp;<INPUT TYPE="button" NAME="cmdJump" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>