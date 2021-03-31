<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.util.FormUtil" %>
<%
request.setCharacterEncoding("GBK");
String strSQL  = "";
String strv_id = request.getParameter("vendor_id");
String strChildList = "";
String pause = request.getParameter("pause");

Cursor cursor;


strSQL = "select devicetype_id,device_model  from tab_devicetype_info where 1=1 ";

if(strv_id!=null)
	strSQL += " and oui='"+strv_id +"'";


out.println(strSQL);
cursor = DataSetBean.getCursor(strSQL);
strChildList = FormUtil.createListBox(cursor, "devicetype_id" ,"device_model", true, "","device_model");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> 取得网域里的设备 </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="dolphin">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="取得子串">
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("strModelList").innerHTML = child.innerHTML;
var pause = "<%=pause%>";
if (pause != null && pause == "true")
{
	parent.isContinue = 1;
}
//-->
</SCRIPT>

</BODY>
</HTML>