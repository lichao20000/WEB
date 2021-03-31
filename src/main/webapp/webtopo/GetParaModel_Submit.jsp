<%--
FileName	: GetParaModel_Submit.jsp
Author		: 
Date		: 
Note		: 
--%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import = "java.util.HashMap" %>
<jsp:useBean id="ParameterAct" scope="request" class="com.linkage.litms.webtopo.ParameterAct"/>

<%@ include file="../timelater.jsp"%>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	HashMap paraMap = ParameterAct.getParaMap(request);
	String strData = "";
	//从paraMap中取值用
	int i = 0;
	String doesWritable = "";
	if (paraMap.get(i+"") == null) {
		strData += "<TR bgcolor=#FFFFFF>";
		strData += "<TD colspan='2'>获取参数信息失败!</TD>";
		strData += "</TR>";
	} else {
		while (paraMap.get(i+"") != null) {
			strData += "<TR bgcolor=#FFFFFF>";
			strData += "<TD>"+ ((String)paraMap.get(i+"")).split(",")[0] + "</TD>";
			if ("0".equals(((String)paraMap.get(i+"")).split(",")[1])) {
				doesWritable = "否";
			} else if("1".equals(((String)paraMap.get(i+"")).split(",")[1])) {
				doesWritable = "是";
			}
			strData += "<TD>"+ doesWritable + "</TD>";
			strData += "</TR>";
			i++;
		}
	}
	
%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" id="myTable">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR>
		<TD width="100%" height="250" valign="top" >
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor="#000000">
			<TR class="green_title"> 
			  <TH>参数名</TH>
			  <TH>是否可写</TH>
			</TR>
			<%= strData%>
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

