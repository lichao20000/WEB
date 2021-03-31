<%@ page contentType="text/html;charset=GBK"%>
<%@page import="wangsenbo.HttpCeShi"%>
<%
HttpCeShi ceshi = new HttpCeShi();
String sdad = ceshi.cccc(request);
System.out.println(sdad);
%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gbk">

</HEAD>

<BODY>
<%=sdad %>
</BODY>



</HTML>
