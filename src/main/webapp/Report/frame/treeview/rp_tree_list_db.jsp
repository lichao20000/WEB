<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@page import="com.linkage.litms.tree.Tree"%>
<?xml version="1.0" encoding="GBK" ?>
<tree id="0">
<%
String userId = user.getAccount();
String tree_parent_id = request.getParameter("tree_id");
String role_id = request.getParameter("role_id");
Tree Tree = new Tree();
//String iterXML = Tree.getDroitXmlByRoleId(tree_parent_id,role_id,true,userId); 
String iterXML =Tree.getReportModuleTreeXML(role_id); 
out.println(iterXML);
Tree = null;
iterXML = null;
%>
</tree>
