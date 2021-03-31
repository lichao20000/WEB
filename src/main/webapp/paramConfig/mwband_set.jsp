<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct,java.util.HashMap,com.linkage.litms.system.UserRes,
com.linkage.litms.system.User,com.linkage.litms.common.database.DataSetBean, com.linkage.litms.paramConfig.ParamInfoCORBA"%>

<%

	String device_id = request.getParameter("device_id");
	String newMunValue = request.getParameter("newMunValue");
	String gather_id = null;
	
	UserRes curUser = (UserRes) session.getAttribute("curUser");
	User user = curUser.getUser();
	
	DeviceAct act = new DeviceAct();
	HashMap<String, String> deviceInfo= act.getDeviceInfo(device_id);
	gather_id = (String)deviceInfo.get("gather_id");
	
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	
	if ("".equals(ior) || "".equals(device_id) || "".equals(gather_id) || "".equals(newMunValue) 
		|| null == ior || null == device_id || null == gather_id || null == newMunValue) {
		out.println("-2");
		return;
	}
	
	ParamInfoCORBA paramInfoCorba = new ParamInfoCORBA();
	
	String[] params_name = new String[2];
	String[] params_value = new String[2];
	String[] para_type_id = new String[2];
	
	params_name[0] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.Mode";
	params_value[0]= "1";
	para_type_id[0] = "2";
	
	params_name[1] = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber";
	params_value[1]= newMunValue;
	para_type_id[1] = "2";
	
	
	boolean booleanMaxUserNum = paramInfoCorba.setParamValue_multi(params_name, params_value, para_type_id, device_id, gather_id, ior);
	
	
	//int flag = paramTreeObject.setParaValueFlag(para_name,ior,device_id,gather_id,para_value);
	if(booleanMaxUserNum){
		
		int code = DataSetBean.executeUpdate("update gw_mwband set total_number=" + newMunValue + " where device_id='" + device_id + "'");
		%>
		<SCRIPT LANGUAGE="JavaScript">	
			alert("设置成功!");
			parent.closeMsgDlg();
			parent.inspireBtns();
            parent.setInput(<%=newMunValue%>);
		</SCRIPT>
		<%
		
		
	}else{
		//out.println("-1");
		%>
		<SCRIPT LANGUAGE="JavaScript">	
			alert("设置失败!");
			parent.closeMsgDlg();
			parent.inspireBtns();
		</SCRIPT>

		<%
	}
	
	//out.print("telecomadmin" + para_value);
%>