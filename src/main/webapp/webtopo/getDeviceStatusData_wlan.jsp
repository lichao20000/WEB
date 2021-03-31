<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.snmpgather.*" %>
<%@ page import="com.linkage.litms.webtopo.snmpgather.InterfaceReadInfo"%>
<%@page import="com.linkage.litms.resource.FileSevice"%>
<jsp:useBean id="DeviceAct" scope="request"	class="com.linkage.litms.resource.DeviceAct" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>联创科技数据综合网管</title>
</head>
<%@ include file="../head.jsp"%>

<%
  	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id);
	String gather_id = (String)deviceInfo.get("gather_id");
	String timeCounter = request.getParameter("timeCounter");
	
	String devicetype_id = (String)deviceInfo.get("devicetype_id");
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	FileSevice fs = new FileSevice();
	Map<String, String> AllNeededNameValueMap = new HashMap<String, String>();
	//fs.getAllwlan_webtopo(device_id, gather_id, ior);
	//ArrayList wlanList = fs.getAllwlanList();
	
	//String allwlanNames = fs.getAllwlanNames();
	
	ArrayList<String> allwlanNames = (ArrayList<String>)session.getAttribute("allwlanNames");//fs.returnWLANNames();
	//ArrayList<String> allpppoeNames = fs.returnallUserfulParamsNames();
	
	if (allwlanNames.size() == 0) {
		
	} else {
		String[] params = new String[allwlanNames.size()];
		for (int i=0;i<allwlanNames.size();i++) {
			params[i] = allwlanNames.get(i);
		}
		fs.getAllNames_ValueMap(device_id, gather_id, params,ior);
		AllNeededNameValueMap = fs.getAllNeededNameValueMap();
		
	}
	Map<String, String> onOroffMap = new HashMap<String, String>();
	onOroffMap.put("0","未启用");
	onOroffMap.put("1","启用");
	onOroffMap.put("Up","启用");
	onOroffMap.put("Down","未启用");
	
	
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
var timesCounter = <%= timeCounter%>;

function setDivStyle(){
	var maxScreenX = window.screen.width;
	var maxScreenY = window.screen.height;

	var w = maxScreenX * 0.9;
	var h = maxScreenY * 0.9 - 350;
	idLayer.style.width = w;
	idLayer.style.height = h+50;
}
//window.onload=setDivStyle;
//-->
</SCRIPT>
<body>
<DIV id="idLayer" style="width:100%;height:100%">
<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text" id="topTable">

		<%
		String strDataIP = "";
		String isEnable = "0";
		String color = "#FFFFFF";
		int j = 0;
		Map pppoeMapTemp = new HashMap();
		if (allwlanNames.size() == 0  || AllNeededNameValueMap == null) {
			strDataIP += "<TR align=left><TD class=column colspan=11><font class='waitMsgFont'>不能获得对应的WLAN结点（设备可能无此结点或设备无法连接）！</font></TD></TR>";
		} else {
			strDataIP += "<TR align=left>";
			strDataIP+= "<TH>序号</TH>";
			strDataIP+= "<TH>是否启用</TH>";
			strDataIP+= "<TH>SSID</TH>";
			strDataIP += "</TR>";
			
			for (int i =0; i < allwlanNames.size();i = i + 2) {
				
				isEnable = AllNeededNameValueMap.get(allwlanNames.get(i));
				
				strDataIP += "<TR bgcolor='" + color + "'>";
				strDataIP+= "<TD>"+ (j+1) +"</TD>";
				strDataIP+= "<TD>"+(String)onOroffMap.get(isEnable)+"</TD>";
				strDataIP+= "<TD>"+AllNeededNameValueMap.get(allwlanNames.get(i+1))+"</TD>";
				strDataIP += "</TR>";
				j = j + 1;
			}
		}
		strDataIP += "<TR align=left><TD bgcolor=\"#FFFFFF\" colspan=11 align=\"right\"><a href=\"#\" onclick=\"reloadWLAN(1)\">刷新(WLAN信息)</a></TD></TR>";
		out.println(strDataIP);
		%>
	</table>
</div>
</body>
</html>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.wlanLayerView) == "object"){
	parent.wlanLayerView.innerHTML = idLayer.innerHTML;
	parent.closeMsgDlg();
	if (timesCounter != 1) {
		//开始装载下一个
		parent.dhcpLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在载入数据，请稍候......</font>";
		parent.loadDHCP();
	}
	
}
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		page = "../Visualman/now_getPort.jsp?ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		//alert(page);
		window.open(page);
	}
//-->
</SCRIPT>