<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.FormUtil" %>
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
request.setCharacterEncoding("GBK");
String strSQL  = "";
String strv_id = request.getParameter("event_oid");
String strChildList = "";

Cursor cursor;
strSQL = "select distinct b.event_oid ,a.event_attr_name,a.event_attr_oid  from event_attr_def a ,event_def b,event_attr_relation c where b.event_oid=c.event_oid and c.event_attr_oid = a.event_attr_oid";
if(strv_id!=null)
	strSQL += " and b.event_oid='" + strv_id + "'";
cursor = DataSetBean.getCursor(strSQL);
strChildList = FormUtil.createListBox(cursor, "event_attr_oid" ,"event_attr_name", false, "","event_name");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
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
parent.document.all("eventlist").innerHTML = child.innerHTML;
//-->
</SCRIPT>

</BODY>
</HTML>