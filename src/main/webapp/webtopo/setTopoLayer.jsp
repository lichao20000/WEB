<%--
@author: HEMC
@date  : 2007-1-16 
@desc  :����topo�㣬����tab_accounts�����ֶ�parentid
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