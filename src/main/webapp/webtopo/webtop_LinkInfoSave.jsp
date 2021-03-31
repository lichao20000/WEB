<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	String link_id			= request.getParameter("link_id");
	String from_id			= request.getParameter("from_id");
	String to_id			= request.getParameter("to_id");
	String from_port_name	= request.getParameter("from_port_name");
	String to_port_name		= request.getParameter("to_port_name");
	String from_port_index	= request.getParameter("from_port_index");
	String to_port_index	= request.getParameter("to_port_index");
	String vpn_auto_id		= request.getParameter("vpn_auto_id");

	RemoteDB.LinkInfo linkInfo	= new RemoteDB.LinkInfo();
	linkInfo.vpn_auto_id		= vpn_auto_id;
	linkInfo.from_id			= from_id;
	linkInfo.to_id				= to_id;
	linkInfo.from_port_name		= from_port_name;
	linkInfo.to_port_name		= to_port_name;
	linkInfo.from_port_index	= from_port_index;
	linkInfo.to_port_index		= to_port_index;
	
	boolean flag = false;
	VpnScheduler vpnScheduler = new VpnScheduler();
	if(vpnScheduler.modifyLinkInfo(link_id,linkInfo)){
		flag = true;
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var flag = <%=flag%>;
	if(flag){
		alert("修改成功!");
	}else{
		alert("修改失败,请重试!");
	}
//-->
</SCRIPT>