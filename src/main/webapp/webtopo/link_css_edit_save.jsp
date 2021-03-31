<%--
Author		: yanhj
Date		: 2006-10-13
Desc		: edit the css of link.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.Scheduler"%>
<%
	request.setCharacterEncoding("GBK");

	String action = request.getParameter("action");
	String link_id = request.getParameter("link_id");
	String link_weight = request.getParameter("link_weight");
	String link_color = request.getParameter("link_color");
	String sql = "", msg = "";
	int flag_db = -1;
	int flag_corba = -1;

	if(action.equals("add")) {
		sql = "insert into tab_line_css (link_id,link_weight,link_color) values('";
		sql += link_id + "','" + link_weight + "','" + link_color + "')";
	} else if (action.equals("upd")) {
		sql = "update tab_line_css set link_weight='" + link_weight + "',link_color='" + link_color + "'";
		sql += " where link_id='" + link_id + "'";
	}
	flag_db = DataSetBean.executeUpdate(sql);
	if(flag_db >= 0) { 
		msg = "保存数据库成功! ";
		Scheduler scheduler = new Scheduler();
//		try {
			flag_corba = scheduler.InformSaveTopo(-1);
			msg += "通知后台成功! ";
//		} catch (Exception e) {
//			msg += "通知后台失败! ";
//		}
	} else {
		msg = "保存数据库失败! ";
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	alert("<%=msg%>");
//-->
</SCRIPT>

