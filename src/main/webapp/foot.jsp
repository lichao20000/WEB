<%
String strAreaName2 = LipossGlobals.getLipossProperty("InstArea.Name");
if(strAreaName2 == null){
	strAreaName2 = "联创科技";
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var strAreaName = "<%=strAreaName2%>";
//-->
<%//@ taglib prefix="s" uri="/struts-tags"%>
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/copyright.js"></SCRIPT>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
//开始清除部分系统参数
strAreaName2 	= null;
%>