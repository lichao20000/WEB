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

<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%
UserRes curUser = (UserRes)session.getAttribute("curUser");

String model = LipossGlobals.getLipossProperty("SetupModel");
if(model==null)
{
	model="";
}
else
{
	model="/"+model;
}
if(curUser == null)
{
	//将ITMS与机顶盒融合，仍保留原有两套登录页面。判断方式是liposs_cfg.xml中有“ITV”字样，则就默认为使用itv登录方式
	String serverName = LipossGlobals.getLipossName();
	if (serverName != null && serverName.toUpperCase().contains("ITV"))
	{
		response.sendRedirect(request.getContextPath() + "/itv/");
		return;
	}
	else
	{
		String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/", 2))+model+"/login.jsp";
		out.println("<script type=\"text/javascript\">top.window.location.href=\""+ url +"\";</script>");
		return;
	}
}

User user = curUser.getUser();
//功能节点编码（为toolbar.jsp界面导航提供参数）
//String temp_TreeItemId = request.getQueryString();
String temp_TreeItemId = request.getParameter("tmp_treeitemid");

String logResult = "成功";
try
{
	// 20200716 吉林联通使用过滤器 ailk-itms-web/src/main/java/com/linkage/litms/common/filter/OperationLogFilter.java
	if(!"jl_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		//logResult = Encoder.toISO(logResult);
		//记日志
		LogItem.getInstance().writeItemLog(request,1,"Web","",logResult);
	}
}
catch(Exception e)
{
	//不处理
}


//刷新在线时间
String onlineUserName = user.getAccount();
if(null!=onlineUserName&&!"".equals(onlineUserName))
{
	UserMap.getInstance().updateOnlineUser(onlineUserName);
}
%>


<%!
public boolean IsAdmin(String r_name,String _city){
	if(!r_name.toLowerCase().equals("system") && !r_name.equals("系统管理员") || _city.length()>0)
		return false;
	else 
		return true;
}
%>
