<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.io.PrintWriter"%>
<%
request.setCharacterEncoding("GBK");
String strMsg="";
strMsg = fileManage.fileAct(request);

String area = LipossGlobals.getLipossProperty("InstArea.ShortName");

// 山西联通
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
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">文件操作提示信息</TH>
					</TR>
					<TR  height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=foot align="right">
							
							&nbsp;&nbsp;<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('file_list.jsp')" value=" 列 表 " class=btn>
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