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
		msg = "ת���°汾webʧ�ܣ���鿴���������Ƿ���ȷ";
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