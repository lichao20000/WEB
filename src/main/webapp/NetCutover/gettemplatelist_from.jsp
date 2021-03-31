<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");
String strSQL  = "";
String strv_id = request.getParameter("devicetype_id");
String strChildList = "";
Cursor cursor;
strSQL = "select distinct  a.devicetype_id, b.template_id, b.template_name,b.devicetype_id  from tab_devicetype_info a,tab_template b where a.devicetype_id=b.devicetype_id";
if(strv_id!=null)
	strSQL += " and a.devicetype_id=" + strv_id + "";
    cursor = DataSetBean.getCursor(strSQL);
    strChildList = FormUtil.createListBox(cursor, "template_id" ,"template_name", true, "", "");  




    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<HTML>
<HEAD>
<TITLE> 取得告警属性 </TITLE>
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
parent.document.all("devicelist").innerHTML = child.innerHTML;
//-->
</SCRIPT>

</BODY>
</HTML>