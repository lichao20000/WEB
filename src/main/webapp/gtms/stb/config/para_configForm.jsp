<%@ include file="../../../timelater.jsp"%>
<link rel="stylesheet" href="../../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_ico.css" type="text/css">
<%@page import="java.util.Map"%>
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
if(action.equals("addChild")){//?????ӽڵ?
	//
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	Map paraMap = paramTreeBIO.getParaMap(para_name,device_id);
	
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
	int flag = paramTreeBIO.addPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.delNodeAll();");
		out.println("parent.getChild();");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('???Ӳ???ʵ??ʧ?ܣ??????????ӣ?');");
	}
}else if(action.equals("setValue")){
	int flag = paramTreeBIO.setParaValueFlag(para_name,device_id,para_value);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('????ʵ?????óɹ???');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('????ʵ??????ʧ?ܣ??????????ã?');");
	}
}else if(action.equals("delValue")){
	int flag = paramTreeBIO.delPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("parent.delNode();");
		out.println("alert('????ʵ??ɾ???ɹ???');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('????ʵ??ɾ??ʧ?ܣ??????????ã?');");
	}
}else if(action.equals("writeableConfig")){
	
	int flag = paramTreeBIO.configUserWritable(para_name,device_id,request);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('???óɹ???');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('????ʧ?ܣ??????????ã?');");
	}
}else if(action.equals("getValue")){
	
	Map paraValueMap = paramTreeBIO.getParaValueMap(para_name,device_id);
	if(paraValueMap == null){
		out.println("parent.closeMsgDlg();");
		out.println("alert('??ȡ????ֵʧ?ܣ??????»?ȡ??')");
	}else{
		String paraValue = paramTreeBIO.getParaVlue(paraValueMap);
		if(paraValue.indexOf("XXX")== -1){	
			out.println("parent.document.frm.para_value.value='" + paraValue + "';");
			out.println("parent.closeMsgDlg();");
			out.println("alert('?ɹ???ȡ????ֵ??')");
		}else{
			out.println("parent.closeMsgDlg();");
			out.println("alert('??ȡ????ֵʧ?ܣ??????»?ȡ??')");
		}
	}
}

out.println("</script>");

%>