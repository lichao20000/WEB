<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.tree.Tree"%>
<?xml version="1.0"?>
<%
String tree_id = request.getParameter("id"); 
String tree_parent_id = request.getParameter("parent_id");

Tree Tree = new Tree();
String id_out = null;

if(Tree.dropProcessor(tree_parent_id,tree_id)){
	id_out = tree_id;
}else{
	id_out = "-1";
}
%>
<succeedded id="<%=id_out%>"/>