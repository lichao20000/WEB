<%--
	默认webtopo跳到指定拓扑层次
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
//根据type判断指向哪个topo
String type = request.getParameter("type");
//默认topo层  1：网络视图
if (type == null)
	type = "1";
int iType = Integer.parseInt(type);
//得到tab_accounts表中得到parent_id，默认topo层。
//parent_id为：1/jinan/0,1/zibo/0,1/2345。
String parentid = user.getParentID();
//当parentid值不为空
//当前用户的parentid为空时，就需要任何处理
if(parentid == null || parentid.equals("") || parentid.equals("null") || parentid.equals("-1")){
	switch(iType){
		case 1:
			parentid = "1/0";
			break;
		case 2:
			parentid = "2/0";
			break;
		case 3:
			parentid = "3/0";
			break;
		case 4:
			parentid = "4/0";
			break;
	}
}
String _page = "main_map.jsp";
String isvip = request.getParameter("isvip");
if(isvip != null && isvip.equals("true"))
	_page = "main_vip.jsp";

response.sendRedirect("./"+ _page +"?type="+ type +"&pid=" + parentid);
%>