<%
String strAreaName2 = LipossGlobals.getLipossProperty("InstArea.Name");
if(strAreaName2 == null){
	strAreaName2 = "�����Ƽ�";
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
//��ʼ�������ϵͳ����
strAreaName2 	= null;
%>