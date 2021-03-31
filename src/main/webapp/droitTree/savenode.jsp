<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@page import="com.linkage.litms.common.util.RandomGUID"%>
<%@page import="com.linkage.litms.tree.Tree"%>
<%
String tree_id = request.getParameter("tree_id");
String tree_name = request.getParameter("tree_name");
String tree_parent_id = request.getParameter("tree_parent_id");
String tree_desc = request.getParameter("tree_desc");

long role_id = user.getRoleId();
Tree Tree = new Tree();
if(tree_id.equals("-1")){
	tree_id = new RandomGUID().toString();
	
	if(Tree.createTreeNode(tree_id,tree_name,tree_parent_id,tree_desc,role_id)){
		out.print("<script>top.doUpdateItem('"+ tree_id +"','"+ tree_name +"');</script>");
	}
}else{
	if(Tree.updateTreeNode(tree_id,tree_name,tree_parent_id,tree_desc)){
		out.print("<script>top.doUpdateItem('"+ tree_id +"','"+ tree_name +"');</script>");
	}
}

%>
