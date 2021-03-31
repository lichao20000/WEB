<%--
@author: HEMC
@date  : 2007-1-16 
@desc  :设置topo层，操作tab_accounts表中字段parentid
--%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String parentid = request.getParameter("parentid");
if(user.setParentID(parentid) > 0){
    out.println("true");
}else{
    out.println("false");
}

%>