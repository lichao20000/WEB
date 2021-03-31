</HEAD>
<BODY>
<%@page import="com.linkage.commons.util.StringUtil"%>
<%@page import="com.linkage.litms.common.util.*"%>
<%@page import="com.linkage.litms.common.WebUtil,com.linkage.litms.system.*,com.linkage.litms.LipossGlobals"%>
<%
	
UserRes curUser = WebUtil.getCurrentUser();
User user = curUser.getUser();
String account = user.getAccount();
String pwd = user.getPasswd();
String msg = "";
if(null != account && null != pwd){
	String new_web_url = LipossGlobals.getLipossProperty("newWeb.url");
	if(StringUtil.IsEmpty(new_web_url)){
		msg = "转到新版本web失败，请查看链接配置是否正确";
	}else{
		String requestParam = LoginUtil.encodeUserForNewEdit(account, pwd, "");
		response.sendRedirect(new_web_url + "?requestParam="+requestParam);
	}
}
 
%>
<script LANGUAGE="JavaScript">
</script>
<div text-alin="center><%=msg%></div>
</BODY>