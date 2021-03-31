<?xml version="1.0" encoding="GBK"?>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@page import="com.linkage.litms.tree.Tree"%>
<tree id="0">
<%//String parent_id = "0";
Tree Tree = new Tree();
String role_id = "" + user.getRoleId();
//String iterXML = Tree.getXMLTreeItem(parent_id);
String iterXML = Tree.getSystemRootTreeItemXML(role_id);  
out.println(iterXML);
iterXML = null;%>
</tree>
