<%@ include file="../timelater.jsp"%>
<%@page import="RemoteDB.ControlManager"%>
<%@page import="com.linkage.litms.common.corba.CorbaInstFactory"%>
<%@page import="RemoteDB.DB"%>
<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

//ѡ�вɼ���
String[] gatherArray=request.getParameterValues("process");

//��������
String type = request.getParameter("type");

//���˷������ͣ�add/�������֡�reset/���·��֣�
String oper = request.getParameter("oper");

long temp_number = -1;

//֪ͨ���½������˷���
if(type.equals("5")){
	VpnScheduler vpnscheduler = new VpnScheduler();
	vpnscheduler.getVPNWebTopoManager().I_InformStartVPN(gatherArray);

	//clear
	gatherArray = null;
	vpnscheduler = null;
}else if(type.equals("1")){
	 try {
		 DB db = (DB)CorbaInstFactory.factory("db");
		 
		 String username = user.getAccount();
		 String password = user.getPasswd();
		 String passwordString = db.ConnectToDb(username,
					password);

		 ControlManager control_manager = db.createControlManager(passwordString);
		 
		 if(oper.equals("add")){
			 temp_number = control_manager.InformStartNew(2,0,"",gatherArray);
		 }else if(oper.equals("reset")){
			 temp_number = control_manager.InformStartNew(1,0,"",gatherArray);
		 }
		 
     } catch (Exception e) {
    	 e.printStackTrace();
     }	
}

%>
