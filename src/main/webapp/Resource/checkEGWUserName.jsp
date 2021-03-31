<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>

<%
String username = request.getParameter("username");

String sql = "select * from tab_egwcustomer where username = '" + username + "'";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select username from tab_egwcustomer where username = '" + username + "'";
}
PrepareSQL psql = new PrepareSQL(sql);
psql.getSQL();
Cursor cursorTmp = DataSetBean.getCursor(sql);
	if (cursorTmp.getNext() != null){
		out.println("<font color='red'>该用户名已被注册</font>");
	}
 %>

