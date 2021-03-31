<%@page import="com.linkage.litms.system.Operator"%>
<%@page import="java.util.Vector"%>
<%@page import="com.linkage.litms.system.dbimpl.DbOperator"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
DbOperator dboperator = new DbOperator();
Vector list = null;
//list = dboperator.getAllOperator();
/*
if(user.getAccount().equals("admin")){
	list = dboperator.getAllOperator();
}else{
	list = dboperator.getPermessionByRoleid(Integer.parseInt(String.valueOf(user.getRoleId())));
}
*/
long[] roleId = user.getRole_Id();
String strRoleId = "(";
for(int i =0;i< roleId.length;i++){
	if(i == 0){
		strRoleId += String.valueOf(roleId[i]);
	}else{
		strRoleId +="," + String.valueOf(roleId[i]);
	}	
}
strRoleId +=")";
list = dboperator.getPermessionByRoleid_1(strRoleId);
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