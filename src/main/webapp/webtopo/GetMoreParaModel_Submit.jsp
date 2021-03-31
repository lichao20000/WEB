<%--
FileName	: GetParaModel_Submit.jsp
Author		: 
Date		: 
Note		: 
--%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.*" %>
<jsp:useBean id="ParameterAct" scope="request" class="com.linkage.litms.webtopo.ParameterAct"/>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	Map paraMap = ParameterAct.getMoreParaValue(request);
	String strData = "";
	if (paraMap == null || paraMap.size() == 0) {
		strData += "<TR bgcolor=#FFFFFF>";
		strData += "<TD colspan='2'>获取参数信息失败!</TD>";
		strData += "</TR>";
	} else {
		Set set = paraMap.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)paraMap.get(name);
			strData += "<TR bgcolor=#FFFFFF>";
			strData += "<TD>"+ name + "</TD>";
			strData += "<TD>"+ value + "</TD>";
			strData += "</TR>";
		}
		iterator = null;
		set		 = null;
	}
%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" id="myTable">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR>
		<TD width="100%" height="250" valign="top" >
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor="#000000">
			<TR > 
			  <TH>参数名</TH>
			  <TH>参数值</TH>
			</TR>
			<%=strData%>
			 </TABLE>
		</TD>
	</TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
parent.tableView.style.display="";
parent.tableView.innerHTML = document.all("myTable").innerHTML;
//-->
</SCRIPT>

<%
	ParameterAct = null;
	paraMap = null;
%>