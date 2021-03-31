<%@page import="java.util.Map"%>
<%@page import="com.linkage.liposs.common.database.Cursor"%>
<%@page import="com.linkage.liposs.common.database.DataSetBean"%>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = "select id,sqlvalue,report_name,pid,layer,report_path from tab_report_manager where ishas=1 order by id";
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
String type = request.getParameter("type");
String sqlvalue="";
String target = "childFrm";
if(type != null && type.equals("admin")){
	sqlvalue="adminTreeView.jsp";
	target = "top.rightPage.viewPage";
}

out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Tree>");
out.println("<TreeView isManage=\"true\" title=\"报表目录\" />");
while(fields != null){
	out.println("<TreeNode title=\""+fields.get("report_name")+"\" href=\""+sqlvalue+"\" id=\""+fields.get("id")+"\" remark=\""+fields.get("report_path")+"\" target=\""+target+"\" pid=\""+fields.get("pid")+"\" layer=\""+fields.get("layer")+"\" ishas=\"1\" />");

	fields = cursor.getNext();
}

if(type != null && type.equals("admin"))
	out.println("<TreeNode title=\"报表审核\" href=\"rp_auditingList.jsp\" id=\"\" remark=\"\" target=\"top.rightPage.viewPage\" pid=\"0\" layer=\"1\" ishas=\"0\" />");
out.println("</Tree>");

fields = null;
cursor = null;
%>
