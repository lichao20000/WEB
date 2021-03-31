<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" src="/Js/rightMenu.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script language="JavaScript"
	src="<s:url value="/securitygw/js/coolmenu1_0_2.js"/>"></script>
<script language="JavaScript"
	src="<s:url value="/securitygw/js/coolmenu_res.js"/>"></script>

<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>"
	rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_ico.css"/>"
	rel="stylesheet" type="text/css">
<link href="<s:url value="/securitygw/css/menu_list.css"/>" rel="stylesheet" type="text/css">
<script language="JavaScript">
<!--
var deviceid="<s:property value="device_id"/>";
var customname="<s:property value="custom_name"/>";
var imagePath = "<s:url value="/securitygw/images/"/>";
var actionPath =  "<s:url value="/securitygw/"/>";
var isShow = true;
//alert(imagePath+","+actionPath);
window.attachEvent("onload",function(){
	drawMenuBar("idMenuBar");
	window.frames[0].document.body.onclick=function(){document_click();}
});

//-->
</script>
<HTML>
<BODY>
<table width="100%" height="27" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td background="images/menu_back.jpg" id="idMenuBar"></td>
	</tr>
</table>
<iframe id="iframe" frameborder="0" marginwidth="0" marginheight="0" width="100%" height="93%"
	src="<s:url value="%{toUrl}"></s:url>" onclick="document_click()"/>
</BODY>
</HTML>
