<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="com.linkage.litms.system.UserMap"%>
<%
request.setCharacterEncoding("GBK");
String strSQL = (String)session.getValue("accountSQL");
int offset = Integer.parseInt(request.getParameter("offset"));
int pagelen = Integer.parseInt(request.getParameter("pagelen"));

Cursor cursor= DataSetBean.getCursor(strSQL,offset,pagelen);
int count = cursor.getRecordSize();
Map fields = cursor.getNext();

String first = "<table width='100%' border='0' cellspacing='1' cellpadding='3'>";
String end = "</table>";
String body = "<tr><td rowspan='1' colspan='8'>�û��б�</td></tr>";
if(count>0){
	body = body + "<tr>"
			+"<td class='dataHead'></td>"
			+"<td class='dataHead'>��¼��</td>"
			+"<td class='dataHead'>����</td>"
			+"<td class='dataHead'>����</td>"
			+"<td class='dataHead'>Email</td>"
			+"<td class='dataHead'>����״̬</td>"
			+"<td class='dataHead'>��¼IP</td>"
			+"<td class='dataHead'>�ֹ�˾</td>"
	+"</tr>";
	int i = 0;
	String bgClr = "#FFFFFF";
	String username="";
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";
		username = (String)fields.get("acc_loginname");
		body = body + "<tr bgcolor='"+bgClr+"'>"
				+"<td></td>"
				+"<td>"+username+"</td>"
				+"<td>"+fields.get("per_dep_oid")+"</td>"
				+"<td>"+fields.get("per_name")+"</td>"
				+"<td>"+fields.get("per_email")+"</td>";
				 
		if(UserMap.getInstance().checkIsLockUser(username)) {
			body = body + "<td>����</td>";
		} else {
			body = body + "<td>����</td>";
		}
		body = body 
				+"<td>"+fields.get("acc_login_ip") +"</td>"
				+"<td>"+fields.get("per_jobtitle")+"</td>"
		        +"</tr>";
		i++;
		fields = cursor.getNext();
	}
}else{
	body = body + "<tr><td colspan='8'>û�и�������</td></tr>";
}

body = first+body+end;
%>
<%= body%>
