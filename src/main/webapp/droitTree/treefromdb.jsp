<?xml version="1.0" encoding="GBK"?>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@page import="com.linkage.litms.tree.Tree"%>
<tree id="0">
<% 
//String parent_id = "0";
Tree Tree = new Tree();
//String iterXML = Tree.getXMLTree(parent_id); 
String iterXML = Tree.getSystemRootTreeXML(); 
out.println(iterXML);
iterXML = null;

/*
String strSQL = "select item_id, item_nm from tree where item_parent_id="+ parent_id;
List Ltree = JdbcUtils.query(strSQL);
Map tree = null;
for(int k=0;k<Ltree.size();k++){
	tree = (Map)Ltree.get(k);
	out.println("<item id='"+ tree.get("item_id") +"' text='"+ tree.get("item_nm") +"'></item>");
}

//clear
Ltree.clear();
Ltree = null;
tree = null;
strSQL = null;
*/
%>
</tree>
