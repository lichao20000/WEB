<%
String strAreaName2 = LipossGlobals.getLipossProperty("InstArea.Name");
if(strAreaName2 == null){
	strAreaName2 = "�����Ƽ�";
}
%>
<script type="text/javascript">
<!--
var strAreaName = "<%=strAreaName2%>";
//-->
<%//@ taglib prefix="s" uri="/struts-tags"%>
</script>
<script type="text/javascript" SRC="../Js/inmp/copyright.js"></script>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
//��ʼ�������ϵͳ����
strAreaName2 	= null;
%>