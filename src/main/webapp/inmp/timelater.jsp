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
	//��ITMS��������ںϣ��Ա���ԭ�����׵�¼ҳ�档�жϷ�ʽ��liposs_cfg.xml���С�ITV�����������Ĭ��Ϊʹ��itv��¼��ʽ
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
//���ܽڵ���루Ϊtoolbar.jsp���浼���ṩ������
//String temp_TreeItemId = request.getQueryString();
String temp_TreeItemId = request.getParameter("tmp_treeitemid");

String logResult = "�ɹ�";
try
{
	//logResult = Encoder.toISO(logResult);
	//����־
	LogItem.getInstance().writeItemLog(request,1,"Web","",logResult);
}
catch(Exception e)
{
	//������
}


//ˢ������ʱ��
String onlineUserName = user.getAccount();
if(null!=onlineUserName&&!"".equals(onlineUserName))
{
	UserMap.getInstance().updateOnlineUser(onlineUserName);
}
%>


<%!
public boolean IsAdmin(String r_name,String _city){
	if(!r_name.toLowerCase().equals("system") && !r_name.equals("ϵͳ����Ա") || _city.length()>0)
		return false;
	else 
		return true;
}
%>
