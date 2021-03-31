<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.tree.Tree"%>
<%
String tree_id = request.getParameter("tree_id");
Tree Tree = new Tree();
if(Tree.deleteTreeNode(tree_id)){
		out.print("<script>top.doDeleteTreeItem('"+ tree_id +"');</script>");
}

%>