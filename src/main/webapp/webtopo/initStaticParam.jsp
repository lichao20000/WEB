<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String allobjs = request.getParameter("curLayerObjList");
String[] arr = new String[0];
if(allobjs!=null && !allobjs.equals("")){
	arr = allobjs.split(",");
}
session.setAttribute("allobjs",arr);
%> 
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.fnStartRefresh();
//-->
</SCRIPT>