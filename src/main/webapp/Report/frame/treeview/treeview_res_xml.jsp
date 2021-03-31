<%@page import="java.util.Map"%>
<%@page import="com.linkage.liposs.common.database.Cursor"%>
<%@page import="com.linkage.liposs.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
//String role_id = (String)session.getAttribute("Role");
String role_id = String.valueOf(user.getRoleId());
long[] roleid = user.getRole_Id();
String strRoleId = "(";
for(int i =0;i< roleid.length;i++){
	if(i == 0){
		strRoleId += String.valueOf(roleid[i]);
	}else{
		strRoleId +="," + String.valueOf(roleid[i]);
	}	
}
strRoleId +=")";
/*
Role role = new RoleSyb(Integer.parseInt(String.valueOf(user.getRoleId())));
String role_name = role.getRoleName();
role = null;
*/
String rootid = request.getParameter("rootid");
//使某个用户只能看到他自己定制的报表：  xiaoxf
String user_id=user.getAccount();
if(rootid==null) rootid = "1";
String strSQL;
if(role_id.trim().equals("1"))
	strSQL = "select * from tab_report_manager where (rootid="+rootid+" or rootid=-1) and report_stat=1 and user_id in('"+user_id+"','admin') order by id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select public_stat, sqlvalue, report_name, id, report_path, pid, layer, ishas " +
				" from tab_report_manager where (rootid="+rootid+" or rootid=-1) and report_stat=1 and user_id in('"+user_id+"','admin') order by id";

	}
	//strSQL = "select * from tab_report_manager where (rootid="+rootid+" or rootid=-1) and report_stat=1 order by id";
else{
	strSQL = "select * from tab_report_manager where (rootid="+rootid
			+" or rootid=-1) and report_stat=1 and (ishas=1 or (ishas=0 and id in (select report_id from tab_report_role where role_id in "
			+ strRoleId +"))) and user_id in('"+user_id+"','admin') order by id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select public_stat, sqlvalue, report_name, id, report_path, pid, layer, ishas " +
				" from tab_report_manager where (rootid="+rootid
				+" or rootid=-1) and report_stat=1 and (ishas=1 or (ishas=0 and id in (select report_id from tab_report_role where role_id in "
				+ strRoleId +"))) and user_id in('"+user_id+"','admin') order by id";
	}

	//strSQL = "select * from tab_report_manager where (rootid="+rootid+" or rootid=-1) and report_stat=1 and (ishas=1 or (ishas=0 and id in (select report_id from tab_report_role where role_id in "+ strRoleId +"))) order by id";
}

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();

String title="";
if(rootid.equals("1")) title = "资源统计";
else if(rootid.equals("2")) title = "考核指标";
else if(rootid.equals("3")) title = "性能指标";
else if(rootid.equals("4")) title = "CEO视图";

out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Tree>");
out.println("<TreeView isManage=\"false\" title=\""+ title +"\" id=\""+rootid+"\"/>");
String type = "",tmp,sqlvalue,url;
String[] arr;
while(fields != null){
	tmp = (String)fields.get("public_stat");
	sqlvalue = (String)fields.get("sqlvalue");
	if(sqlvalue.indexOf("%%") != -1)
		arr = sqlvalue.split("%%");//modify 2005-5-11 by yuht 把'@'分隔符改为'%%'
	else 
		arr = sqlvalue.split("@"); //兼容老以前订阅的报表,南京局除外

	if(arr.length>1) url=arr[1];
	else if(arr.length==1) url=arr[0];
	else url="";
	if(tmp.equals("-1")) type=" type=\"private\"";
	else type=" type=\"public\"";
	out.println("<TreeNode "+type+" title=\""+fields.get("report_name")+"\" href=\""+url+"\" id=\""+fields.get("id")+"\" remark=\""+fields.get("report_path")+"\" target=\"top.rightPage.viewPage\" pid=\""+fields.get("pid")+"\" layer=\""+fields.get("layer")+"\" ishas=\""+fields.get("ishas")+"\" />");

	fields = cursor.getNext();
}
out.println("</Tree>");

fields = null;
cursor = null;
%>
