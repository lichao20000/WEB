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
if (strAction.equals("delete")) { // ɾ������
		strSQL = "delete from pping_group_conf where groupid="+groupid;
		result = DataSetBean.executeUpdate(strSQL);
}
if (result>0) {
	strResult = "ɾ���ɹ�!";
} else {
	strResult = "ɾ��ʧ��!";
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
						<TH align="center">ɾ��������ʾ��Ϣ</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%=strResult%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('PowerPingConfig.jsp')" value=" �� �� " class="jianbian">
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class="jianbian">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>