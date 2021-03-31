<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<?xml version="1.0"?>
<%@page import="com.linkage.litms.tree.Tree"%>
<%
String id = request.getParameter("id");
%>
<details id="<%=id%>">
<%
/*
String strSQL = "Select item_nm,item_parent_id,item_desc from Tree where item_id="+ id;
List Ltree = JdbcUtils.query(strSQL);
Map tree = null;
if(Ltree.size()>0){
	tree = (Map)Ltree.get(0);
	
	out.println("<name>"+ tree.get("item_nm") +"</name>");
	out.println("<parent_id>"+ tree.get("item_parent_id") +"</parent_id>");
	out.println("<desc>"+ tree.get("item_desc") +"</desc>");
}

//clear
Ltree.clear();
Ltree = null;
tree = null;
strSQL = null;
*/

Tree Tree = new Tree();
out.println(Tree.loadNodeXMLDetail(id));
%>
</details>