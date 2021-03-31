<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
//V1版本
function ifupdatapmV1() {
	if (confirm('表达式已经定义过，确认要重新定义？'))
	{
		parent.frm.action="testDevPerDef_v1.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//V2版本
function ifupdatapmV2() {
	if (confirm('表达式已经定义过，确认要重新定义？'))
	{
		parent.frm.action="testDevPerDef_v2.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//V3版本
function ifupdatapmV3() {
	if (confirm('表达式已经定义过，确认要重新定义？'))
	{
		parent.frm.action="testDevPerDef_v3.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//不支持SNMP
function IsConfig(){
	alert("该设备未经认证,不支持SNMP!");
	return;
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>

	<%
		String device_id=request.getParameter("device_id");
		String isconfig=request.getParameter("isconfig");
		DevPerDef dpd = new DevPerDef(request);
		//经过认证的设备
		if(isconfig!=null && "true".equals(isconfig)){
			boolean b = dpd.is_Pmbeing(device_id);
			int V=dpd.QueryVersion(device_id);//获得版本信息
			if (b) {//是否已经配置过
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				if(V==1){
					out.println("ifupdatapmV1();");		
				}else if(V==2){
					out.println("ifupdatapmV2();");
				}else{
					out.println("ifupdatapmV3();");
				}
				out.println("</SCRIPT>");
			}else {
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				if(V==1){//V1版本
					out.println("parent.frm.action=\"testDevPerDef_v1.jsp\";");
				}else if(V==2){//V2版本
					out.println("parent.frm.action=\"testDevPerDef_v2.jsp\";");
				}else{//V3版本
					out.println("parent.frm.action=\"testDevPerDef_v3.jsp\";");
				}	
				out.println("parent.frm.submit();");
				out.println("</SCRIPT>");
			}
		}else{//判断是否经过认证,即认证表中有无数据!
			boolean flg=dpd.isConfigV3(device_id);
			if(!flg){
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				out.println("IsConfig();");
				out.println("</SCRIPT>");
			}else{
				out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				out.println("parent.frm.action=\"testPmIsBeing_v3.jsp?isconfig=true\";");
				out.println("parent.frm.submit();");
				out.println("</SCRIPT>");
			}
		}
	%>
