<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
int result = 0;
String strResult = "";
String strSQL = "";
request.setCharacterEncoding("GBK");
String strAction = request.getParameter("action");
String groupid = request.getParameter("groupid");
if (strAction.equals("delete")) { // 删除操作
		strSQL = "delete from pping_group_conf where groupid="+groupid;
		result = DataSetBean.executeUpdate(strSQL);
}
if (result>0) {
	strResult = "删除成功!";
} else {
	strResult = "删除失败!";
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function GoList(page){
	this.location = page;
	}
//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">删除操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%=strResult%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('PowerPingConfig.jsp')" value=" 列 表 " class="jianbian">
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class="jianbian">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>