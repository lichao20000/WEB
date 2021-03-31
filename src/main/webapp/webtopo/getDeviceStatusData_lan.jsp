<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.snmpgather.*" %>
<%@ page import="com.linkage.litms.webtopo.snmpgather.InterfaceReadInfo"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="DeviceAct" scope="request"	class="com.linkage.litms.resource.DeviceAct" />
<%@page import="com.linkage.litms.resource.FileSevice"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>联创科技数据综合网管</title>
</head>
<%@ include file="../head.jsp"%>

<%
  	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	String className = request.getParameter("className");
	String timeCounter = request.getParameter("timeCounter");
	//ReadDeviceStatus di = (ReadDeviceStatus) Class.forName("com.linkage.litms.webtopo.snmpgather."+className).newInstance();
	//di.setDevice_ID(device_id);
	HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id);
	String gather_id = (String)deviceInfo.get("gather_id");
	String devicetype_id = (String)deviceInfo.get("devicetype_id");
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	FileSevice fs = new FileSevice();
	Map<String, String> AllNeededNameValueMap = new HashMap<String, String>();
	Map<String, String> onOroffMap = new HashMap<String, String>();
	onOroffMap.put("0","未启用");
	onOroffMap.put("1","启用");
	onOroffMap.put("Up","启用");
	onOroffMap.put("PPPoE_Bridged","桥接");
	onOroffMap.put("IP_Routed","路由");
	onOroffMap.put("Unconfigured","未配置");
	
	//fs.getAllNamePw_webtopo(device_id, gather_id, ior);
	//ArrayList pppoeList = fs.getAllNamesList();
	//String allpppoeNames = fs.getAllpppoeNames();
	
	ArrayList<String> allpppoeNames = (ArrayList<String>)session.getAttribute("allpppoeNames");
	
	//ArrayList<String> allpppoeNames = fs.returnallUserfulParamsNames();
	
	
	
	if (allpppoeNames.size() == 0) {
		
	} else {
		String[] params = new String[allpppoeNames.size()];
		for (int i=0;i<allpppoeNames.size();i++) {
			params[i] = allpppoeNames.get(i);
		}
		fs.getAllNames_ValueMap(device_id, gather_id, params,ior);
		AllNeededNameValueMap = fs.getAllNeededNameValueMap();
		
		
	}
	
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
		String strDataPPPOE = "";
		String isEnable = "0";
		String color = "#FFFFFF";
		String connetionType = "未知";
		String username = "";
		//String allParamNames = null;
		int j = 0;
		if (allpppoeNames.size() == 0 || AllNeededNameValueMap == null) {
			strDataPPPOE += "<TR align=left><TD class=column colspan=11><font class='waitMsgFont'>不能获得对应的PPPoE结点（设备可能无此结点或设备无法连接）！</font></TD></TR>";
		} else {
			strDataPPPOE += "<TR align=left>";
			strDataPPPOE+= "<TH>序号</TH>";
			strDataPPPOE+= "<TH>上网类型</TH>";
			strDataPPPOE+= "<TH>是否启用</TH>";
			strDataPPPOE+= "<TH>宽带账号</TH>";
			strDataPPPOE += "</TR>";
			//取得所有的名字
			//allParamNames = (String)pppoeList.get(pppoeList.size() - 1);
			//String[] aloneNames = allpppoeNames.split("#");
			
			for (int i =0; i < allpppoeNames.size();i = i + 3) {
				
				isEnable = AllNeededNameValueMap.get(allpppoeNames.get(i));
				
				if (null != (String)onOroffMap.get(AllNeededNameValueMap.get(allpppoeNames.get(i+1)))) {
					connetionType = (String)onOroffMap.get(AllNeededNameValueMap.get(allpppoeNames.get(i+1)));
				}
				username = AllNeededNameValueMap.get(allpppoeNames.get(i+2));
				
				strDataPPPOE += "<TR bgcolor='" + color + "'>";
				strDataPPPOE+= "<TD>"+ (++j) +"</TD>";
				strDataPPPOE+= "<TD>"+ connetionType +"</TD>";
				strDataPPPOE+= "<TD>"+(String)onOroffMap.get(isEnable)+"</TD>";
				strDataPPPOE+= "<TD>"+username+"</TD>";
				strDataPPPOE += "</TR>";
			}
				
		}
		
		strDataPPPOE += "<TR align=left><TD bgcolor=\"#FFFFFF\" colspan=11 align=\"right\"><a href=\"#\" onclick=\"reloadPPPoE(1)\">刷新(PPPoE信息)</a></TD></TR>";
		out.println(strDataPPPOE);
		%>
	</table>
</div>
</body>
</html>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.pppoeLayerView) == "object"){
	parent.pppoeLayerView.innerHTML = idLayer.innerHTML;
	parent.closeMsgDlg();
	if (timesCounter != 1) {
		//开始装载下一个
		parent.ipLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在载入数据，请稍候......</font>";
		parent.loadIP();
		//parent.loadDHCP();
	}
}
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		page = "../Visualman/now_getPort.jsp?ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		//alert(page);
		window.open(page);
	}
//-->
</SCRIPT>