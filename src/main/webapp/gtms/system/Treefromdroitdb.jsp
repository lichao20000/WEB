<?xml version="1.0" encoding="GBK"?>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@page import="com.linkage.module.gtms.system.action.SuperRoleManager"%>
<tree id="0">
<%
String parent_id = "0";
String relation_type = request.getParameter("relation_type");
SuperRoleManager srm = new SuperRoleManager();
//String iterXML = Tree.getXMLTreeItem(parent_id);
String iterXML = srm.getSystemRootTreeItemXML(parent_id,relation_type);  
out.println(iterXML);
iterXML = null;
%>
</tree>
