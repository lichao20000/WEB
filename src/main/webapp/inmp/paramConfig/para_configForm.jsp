<%@ include file="../timelater.jsp"%>

<%@page import="java.util.Map"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>

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
paramTreeObject.setGwType(gw_type);
if(action.equals("addChild")){//添加子节点
	//
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	Map paraMap = paramTreeObject.getParaMap(para_name,device_id);
	
	int flag = 0;
	
	if(paraMap != null){
		int i=0;
		while(paraMap.get(i+"")!=null){
			String strTitle= (String)paraMap.get(i+"");
			String strText =  strTitle.substring(para_name.length(),strTitle.indexOf(","));
			out.println("parent.addsub('"+ strText +"','"+ strTitle +"');");
			paraContainKey +="|" + strText;
			flag = 1;
			i++;
		}
	}
	//out.println("parent.createObjectResult("+ flag +");");
	out.println("parent.closeMsgDlg();");
	
		
}else if(action.equals("newChild")){
	int flag = paramTreeObject.addPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.delNodeAll();");
		out.println("parent.getChild();");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('添加参数实例失败，请重新添加！');");
	}
}else if(action.equals("setValue")){
	int flag = paramTreeObject.setParaValueFlag(para_name,device_id,para_value);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('参数实例配置成功！');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('参数实例配置失败，请重新配置！');");
	}
}else if(action.equals("delValue")){
	int flag = paramTreeObject.delPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("parent.delNode();");
		out.println("alert('参数实例删除成功！');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('参数实例删除失败，请重新配置！');");
	}
}else if(action.equals("writeableConfig")){
	
	int flag = paramTreeObject.configUserWritable(para_name,device_id,request);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('配置成功！');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('配置失败，请重新配置！');");
	}
}else if(action.equals("getValue")){
	
	Map paraValueMap = paramTreeObject.getParaValueMap(para_name,device_id);
	if(paraValueMap == null){
		out.println("parent.closeMsgDlg();");
		out.println("alert('获取参数值失败，请重新获取！')");
	}else{
		String paraValue = paramTreeObject.getParaVlue(paraValueMap);
		if(paraValue.indexOf("XXX")== -1){	
			out.println("parent.document.frm.para_value.value='" + paraValue + "';");
			out.println("parent.closeMsgDlg();");
			out.println("alert('成功获取参数值！')");
		}else{
			out.println("parent.closeMsgDlg();");
			out.println("alert('获取参数值失败，请重新获取！')");
		}
	}
}

out.println("</script>");

%>