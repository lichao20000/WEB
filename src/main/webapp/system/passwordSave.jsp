<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="userManage" scope="request" class="com.linkage.litms.system.dbimpl.DbUserManager"/>

<%
request.setCharacterEncoding("GBK");
String strMsg = userManage.userInfoDoAct(request);
//String strAction = request.getParameter("action");
//String changePwdEnable = LipossGlobals.getLipossProperty("changePwd.changePwdEnable");
String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<html>
<body>
	<SCRIPT LANGUAGE="JavaScript">
		var host = window.location.host;
        var href = window.location.href;
		var pathName = window.location.pathname;
		var path = pathName.substring(0,pathName.lastIndexOf("/"));
		var pathWeb = path.substring(0,path.lastIndexOf("/"));		
		var url = "http://" + host + path + "/ModifyPwdForm.jsp";
		var loginUrl = "http://" + host + "/itms/login.jsp";	
		if(path.indexOf("lims") != -1 ){
		loginUrl = "http://" + host + pathWeb + "/itv/login.jsp";	
		}
		var mesg = '<%=strMsg%>';
        if(href.indexOf("https") != -1)
        {
          url = "https://" + host + path + "/ModifyPwdForm.jsp";
          loginUrl = "https://" + host + "/itms/login.jsp";
        }
		alert(mesg);
		if("用户保存操作成功！" == mesg || "操作成功！" == mesg){
		        top.location.href=loginUrl;
		} else {
			window.location.href = url;
		}
		
	</SCRIPT>
</body>
</html>
