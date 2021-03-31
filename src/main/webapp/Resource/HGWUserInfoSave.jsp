<%@ include file="../timelater.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("GBK");
String strMsg="";
int i = 0;
String username = request.getParameter("username");
String strAction = request.getParameter("action");
if(strAction.equals("add")){
    String sql = "select * from tab_hgwcustomer where username = '"
			+ username + "'";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select username from tab_hgwcustomer where username = '"
				+ username + "'";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	Cursor cursorTmp = DataSetBean.getCursor(sql);
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null){
		i = 1;
	}
}
if(i==0){
	strMsg = HGWUserInfoAct.HGWUserInfoActExe(request);
}else{
    strMsg = "你输入的用户名已经存在，请重新再输入一个新的用户名！";
}	
//String vpn_id = request.getParameter("vpn_id_hid");
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
						<TH align="center">家庭网关客户资源操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('AddHGWUserInfoForm.jsp')" value=" 添加新用户 " class=btn <%if(i!=0){ %>
                       STYLE="display:none" <%} %>  >
						<INPUT TYPE="button" NAME="cmdBack" onclick="GoList('HGWUserInfoList.jsp')" value=" 用户列表 " class=btn>
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