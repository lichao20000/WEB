<%@page import="com.linkage.litms.system.Operator"%>
<%@page import="java.util.Vector"%>
<%@page import="com.linkage.litms.system.dbimpl.DbOperator"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>

<%
String role_id_str = request.getParameter("role_id");
int role_id=0;
if(role_id_str!= null) role_id = Integer.parseInt(role_id_str);

DbOperator dboperator = new DbOperator();
Vector list = dboperator.getPermessionByRoleid(role_id);
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Operator>");
try{
	Operator op;

	for(int i=0; i<list.size(); i++){
		op = (Operator)list.get(i);
		out.println("<OperatorNode oid=\""+op._oid+"\" poid=\""+op._poid+"\" layer=\""+op._layer+"\" name=\""+op._name+"\" remark=\""+op._remark+"\" />");
	}

}
catch(Exception e){
	//out.println("create xml file error");
}
out.println("</Operator>");
%>