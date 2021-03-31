<%@ include file="../../../timelater.jsp"%>
<link rel="stylesheet" href="../../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_ico.css" type="text/css">
<%@page import="java.util.Map"%>
<%@page import="com.linkage.module.gwms.Global"%>
<jsp:useBean id="paramTreeBIO" scope="request" class="com.linkage.module.gtms.stb.config.bio.ParamTreeBIO"/>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String action = request.getParameter("action");

String para_name = request.getParameter("param_name");

String device_id = request.getParameter("device_id");

String para_value = request.getParameter("para_value");

if(para_value != null){
	para_value = para_value.replace("%26", "&");
	para_value = para_value.replace("%2B", "+");
}
String gw_type = request.getParameter("gw_type");

if(null==gw_type){
	gw_type = com.linkage.litms.LipossGlobals.getGw_Type(device_id);
}
out.println("<script language=javascript>");
String paraContainKey = "";
paramTreeBIO.setGwType(gw_type);
if(action.equals("addChild")){//添加子节点
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	Map paraMap = paramTreeBIO.getParaWithCode(para_name,device_id);
	int code = Integer.parseInt(String.valueOf(paraMap.get("code")));
	// 故障描述
	String fault_desc = "";
	if(Global.G_Fault_Map.get(code) != null){
		fault_desc = Global.G_Fault_Map.get(code).getFaultDesc();
		fault_desc = fault_desc.equals("") ? "未知错误" : fault_desc;
	}
	fault_desc = "获取参数失败\\r\\n原因：" + fault_desc;
	
	if(code != 1){
		out.println("parent.resultMsg("+code+",'"+fault_desc+"');");
	}else{
		paraMap.remove("code");
		if(paraMap.size() == 0){
			out.println("parent.resultMsg("+code+",'家庭网关设备由于自身原因，没有返回子节点信息');");
		}else{
			out.println("parent.resultMsg("+code+",'获取参数成功');");
			int i=0;
			while(paraMap.get(i+"")!=null){
				String strTitle= (String)paraMap.get(i+""); // hint_param + "," + writable
				String strText =  strTitle.substring(para_name.length(),strTitle.indexOf(",")); // param
				
				out.println("parent.addsub('"+ strText +"','"+ strTitle +"');");
				paraContainKey += "|" + strText;
				i++;
			}
		}
		
	}
	out.println("parent.closeMsgDlg();");
}else if(action.equals("newChild")){
	int flag = paramTreeBIO.addPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.delNodeAll();");
		out.println("parent.getChild();");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('添加参数实例失败，请重新添加！');");
	}
}else if(action.equals("setValue")){
	int code = paramTreeBIO.setParaValue(para_name,device_id,para_value);
	if(code == 1){
		out.println("parent.resultMsg("+code+",'修改成功');");
	}else{
		// 故障描述
		String fault_desc = "";
		if(Global.G_Fault_Map.get(code) != null){
			fault_desc = Global.G_Fault_Map.get(code).getFaultDesc();
			fault_desc = fault_desc.equals("") ? "未知错误" : fault_desc;
		}
		fault_desc = "修改失败\\r\\n原因：" + fault_desc;
		
		out.println("parent.resultMsg("+code+",'"+fault_desc+"');");
	}
	out.println("parent.closeMsgDlg();");
}else if(action.equals("delValue")){
	int flag = paramTreeBIO.delPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("parent.delNode();");
		out.println("alert('参数实例删除成功！');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('参数实例删除失败，请重新配置！');");
	}
}else if(action.equals("writeableConfig")){
	
	int flag = paramTreeBIO.configUserWritable(para_name,device_id,request);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('配置成功！');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('配置失败，请重新配置！');");
	}
}else if(action.equals("getValue")){
	
	Map paraValueMap = paramTreeBIO.getParaValueWithCode(para_name,device_id);
	int code = Integer.parseInt(String.valueOf(paraValueMap.get("code")));
	// 故障描述
	String fault_desc = "";
	if(Global.G_Fault_Map.get(code) != null){
		fault_desc = Global.G_Fault_Map.get(code).getFaultDesc();
		fault_desc = fault_desc.equals("") ? "未知错误" : fault_desc;
	}
	
	fault_desc = "获取参数值失败\\r\\n原因：" + fault_desc;
	
	if(code != 1){
		out.println("parent.resultMsg("+code+",'"+fault_desc+"');");
	}else{
		paraValueMap.remove("code");
		String paraValue = paramTreeBIO.getParaVlue(paraValueMap);
		if(paraValue.indexOf("XXX")== -1){	
			out.println("parent.document.frm.para_value.value='" + paraValue + "';");
			out.println("parent.resultMsg("+code+",'获取参数值成功');");
		}else{
			out.println("parent.resultMsg("+code+",'"+fault_desc+"');");
		}
	}
	out.println("parent.closeMsgDlg();");
}

out.println("</script>");

%>