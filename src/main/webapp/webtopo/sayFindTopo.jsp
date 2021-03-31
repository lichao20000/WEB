<%@ include file="../timelater.jsp"%>
<%@page import="RemoteDB.ControlManager"%>
<%@page import="com.linkage.litms.common.corba.CorbaInstFactory"%>
<%@page import="RemoteDB.DB"%>
<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

//选中采集机
String[] gatherArray=request.getParameterValues("process");

//拓扑类型
String type = request.getParameter("type");

//拓扑发现类型（add/增量发现、reset/重新发现）
String oper = request.getParameter("oper");

long temp_number = -1;

//通知重新进行拓扑发现
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
