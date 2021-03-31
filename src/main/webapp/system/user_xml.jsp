<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="com.linkage.litms.system.UserMap"%>
<%@ page import="com.linkage.commons.util.StringUtil"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("GBK");
String strSQL = (String)session.getValue("accountSQL");
if (LipossGlobals.inArea("ah_dx")) {
	strSQL = "select atl.*, dep.depart_name from (" + strSQL + ") atl left join tab_department dep on atl.per_dep_oid = dep.depart_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select atl.acc_loginname, atl.per_acc_oid, atl.per_dep_oid, atl.per_name, atl.per_email, " +
				"atl.acc_login_ip, atl.per_jobtitle, dep.depart_name " +
				"from (" + strSQL + ") atl left join tab_department dep on atl.per_dep_oid = dep.depart_id";
	}
}
int offset = Integer.parseInt(request.getParameter("offset"));
int pagelen = Integer.parseInt(request.getParameter("pagelen"));

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor= DataSetBean.getCursor(strSQL,offset,pagelen);
int count = cursor.getRecordSize();
Map fields = cursor.getNext();
out.clear();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
//out.println("<?xml-stylesheet href=\"viewdata.xsl\" type=\"text/xsl\" title=\"default stylesheet\"?>");
out.println("<Output Name=\"user_list\" Title=\"用户列表\" IsSelect=\"true\" ColCount=\"8\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"登录名\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields2\" remark=\"部门\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields3\" remark=\"名称\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields4\" remark=\"Email\" datatype=\"String\" />");	
	out.println("<Column fieldname=\"fields5\" remark=\"锁定状态\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields6\" remark=\"登录IP\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields7\" remark=\"分公司\" datatype=\"String\" />");	
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";
	String username="";
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";
		username = (String)fields.get("acc_loginname");
		
		out.println("<Row primarykey=\""+ fields.get("per_acc_oid") +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ username +"\" />");
		if (LipossGlobals.inArea("ah_dx")) {
			//out.println("<fields2 value=\""+ StringUtil.getStringValue(fields, "depart_name", "未知") +"\" />");
			if ("".equals(fields.get("depart_name")) || null == fields.get("depart_name")) {
				out.println("<fields2 value=\"未知\" />");
			}
			else {
				out.println("<fields2 value=\"" + fields.get("depart_name") + "\" />");
			}
		}
		else {
			out.println("<fields2 value=\""+ fields.get("per_dep_oid") +"\" />");
		}
		out.println("<fields3 value=\""+ fields.get("per_name") +"\" />");
		out.println("<fields4 value=\""+ fields.get("per_email") +"\" />");		
		if(UserMap.getInstance().checkIsLockUser(username)) {
			out.println("<fields5 value='锁定' />");	
		} else {
			out.println("<fields5 value='正常' />");
		}	
		out.println("<fields6 value=\""+ fields.get("acc_login_ip") +"\" />");
		out.println("<fields7 value=\""+ fields.get("per_jobtitle") +"\" />");	
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");
%>
