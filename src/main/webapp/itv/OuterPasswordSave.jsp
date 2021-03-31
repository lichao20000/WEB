<%@page import="com.linkage.litms.system.User"%>
<%@page import="com.linkage.litms.system.UserMap"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@page import="com.linkage.litms.common.util.Encoder"%>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.util.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="userManage" scope="request" class="com.linkage.litms.system.dbimpl.DbUserManager"/>

<%
request.setCharacterEncoding("GBK");
String strMsg = userManage.modifyPwdOutOfSys(request);
//String strAction = request.getParameter("action");
//String changePwdEnable = LipossGlobals.getLipossProperty("changePwd.changePwdEnable");
%>

<html>
<body>
	<SCRIPT LANGUAGE="JavaScript">
		var host = window.location.host;
		var href = window.location.href;
		var pathName = window.location.pathname;
		var path = pathName.substring(0,pathName.lastIndexOf("/"));
		var pathWeb = path.substring(0,path.lastIndexOf("/"));		
		var url = "http://" + host + path + "/OuterModifyPwdForm.jsp";	
		var loginUrl = "";
		if(path.indexOf("lims") != -1){
			loginUrl = "http://" + host + pathWeb + "/itv/login.jsp";	
		}else{			
			loginUrl="http://" + host + pathWeb + "/login.jsp";	
		}
		if(href.indexOf("https") != -1)
		{
			if(path.indexOf("lims") != -1){
				loginUrl = "https://" + host + pathWeb + "/itv/login.jsp";	
			}else{			
				loginUrl="https://" + host + pathWeb + "/login.jsp";	
			}
		}
		
		var mesg = "<%=strMsg%>";
		window.location.href = url;
		alert(mesg);
		if("用户保存操作成功！" == mesg || "操作成功！" == mesg){
			window.parent.location.href = loginUrl;
		} else {
			window.location.href = url;
		}
		
	</SCRIPT>
</body>
</html>
