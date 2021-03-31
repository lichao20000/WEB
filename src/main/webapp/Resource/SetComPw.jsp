<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
	String device_id = request.getParameter("device_id");
	String para_name  ="";
	String telecom = LipossGlobals.getLipossProperty("telecom");
    if(telecom.equals("CUC")){
    	para_name = "InternetGatewayDevice.X_CU_Function.Web.AdminPassword";
    }else{
    	para_name = "InternetGatewayDevice.DeviceInfo.X_CT-COM_TeleComAccount.Password";
    }
	
	String para_value = request.getParameter("pwValue");
	
	if ("".equals(para_name) || "".equals(device_id) || "".equals(para_value) 
		|| null == para_name || null == device_id || null == para_value) {
		out.println("-2");
		return;
	}
	paramTreeObject.setGwType(LipossGlobals.getGw_Type(device_id));
	int flag = paramTreeObject.setParaValueFlag(para_name,device_id,para_value);
	if(flag == 1){
		
		int code = DataSetBean.executeUpdate("update tab_gw_device set x_com_passwd='" + para_value + "' where device_id='" + device_id + "'");
		if (code > 0) {
			out.println("1");
		} else {
			out.println("0");
		}
		
	}else{
		out.println("-1");
	}
	
	//out.print("telecomadmin" + para_value);
%>