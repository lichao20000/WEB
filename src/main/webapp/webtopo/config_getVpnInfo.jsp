<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ page import ="com.linkage.litms.vipms.action.BaseNodeAct,
				  com.linkage.litms.vipms.action.BaseCircuitAct,
				  com.linkage.litms.vipms.action.BaseServerAct" %>

<%
request.setCharacterEncoding("GBK");
String strSQL  = "";
String method = request.getParameter("method");
String key    = request.getParameter("key");
String strChildList = "";
String strChildList1 ="";
String strChildList2="<select class=bk name=\"circuit\"><option value=\"-1\">请先选择业务</option></select>";

Cursor cursor;
if(method.equals("serviceList"))
{
	BaseServerAct bsa=new BaseServerAct();
	cursor=bsa.getServerCursor(key);
	strChildList = FormUtil.createListBox(cursor, "servid" ,"servname", true, "","service");
}
else if(method.equals("nodeList"))
{
	BaseNodeAct bna=new BaseNodeAct();
	cursor=bna.getNodeCursor(key);
	strChildList = FormUtil.createListBox(cursor, "nodeid" ,"nodename", false, "","node");
}
else if(method.equals("circuitList"))
{
	BaseCircuitAct bca=new BaseCircuitAct();
	cursor=bca.getCirCursor(key);
	strChildList = FormUtil.createListBox(cursor, "cirid" ,"cirname", false, "","circuit");
}
else if(method.equals("two"))
{
	BaseServerAct bsa=new BaseServerAct();
	cursor=bsa.getServerCursor(key);
	strChildList = FormUtil.createListBox(cursor, "servid" ,"servname", true, "","service");
	
	BaseNodeAct bna=new BaseNodeAct();
	cursor=bna.getNodeCursor(key);
	strChildList1 = FormUtil.createListBox(cursor, "nodeid" ,"nodename", false, "","node");
}
else if(method.equals("all"))
{
	BaseServerAct bsa=new BaseServerAct();
	cursor=bsa.getServerCursor(key);
	strChildList = FormUtil.createListBox(cursor, "servid" ,"servname", true, "","service");

	BaseNodeAct bna=new BaseNodeAct();
	cursor=bna.getNodeCursor(key);
	strChildList1 = FormUtil.createListBox(cursor, "nodeid" ,"nodename", false, "","node");

	String serviceid=request.getParameter("serviceid");

	BaseCircuitAct bca=new BaseCircuitAct();
	cursor=bca.getCirCursor(serviceid);
	strChildList2 = FormUtil.createListBox(cursor, "cirid" ,"cirname", false, "","circuit");
}


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="dolphin">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="取得子串">
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>

<SPAN ID="child1"><%=strChildList1%></SPAN>

<SPAN ID="child2"><%=strChildList2%>
	
</SPAN>

</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var method="<%=method%>";
	
	if(method =="two")
	{
		parent.document.all("serviceList").innerHTML = child.innerHTML;
		parent.document.all("nodeList").innerHTML = child1.innerHTML;
		parent.document.all("circuitList").innerHTML=child2.innerHTML;	
	}
	else if(method=="all")
	{
		parent.document.all("serviceList").innerHTML = child.innerHTML;
		parent.document.all("nodeList").innerHTML = child1.innerHTML;
		parent.document.all("circuitList").innerHTML=child2.innerHTML;	
		parent.isSel=1;
	}
	else
	{
		parent.document.all("<%=method%>").innerHTML = child.innerHTML;
	}
//-->
</SCRIPT>

</BODY>
</HTML>