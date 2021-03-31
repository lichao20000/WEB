<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.webtopo.DeviceResourceInfo"%>
<jsp:useBean id="DeviceAct" scope="request"	class="com.linkage.litms.resource.DeviceAct" />
<%@page import="com.linkage.litms.resource.FileSevice"%>
<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
	String className=request.getParameter("className");
	String title=request.getParameter("title");

	//通过DeviceResourceInfo从数据库获得设备信息
	DeviceResourceInfo deviceInfo_A = new DeviceResourceInfo();
	Cursor cursor = deviceInfo_A.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	String deviceName= (String)myMap.get("device_name");
	String loopbackip= (String)myMap.get("loopback_ip");
	
	HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id);
	String gather_id = (String)deviceInfo.get("gather_id");
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	/*FileSevice fs = new FileSevice();
	
	
	fs.getAllParamNames(device_id, ior, gather_id);
	
	ArrayList<String> allpppoeNames = fs.returnPPPoENames();
	ArrayList<String> allipNames = fs.returnIPNames();
	ArrayList<String> allwlanNames = fs.returnWLANNames();
	ArrayList<String> alldhcpeNames = fs.returnDHCPNames();
	
	session.setAttribute("allpppoeNames", allpppoeNames);
	session.setAttribute("allipNames", allipNames);
	session.setAttribute("allwlanNames", allwlanNames);
	session.setAttribute("alldhcpeNames", alldhcpeNames);*/
	
%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function initDeviceStatus() {
	
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_lan.jsp?device_id="+device_id+"&refresh="+Math.random();
		document.all("childFrm_A").src=page;
	}
	
	function loadPort()
	{
		idLayerView.style.width = document.body.clientWidth;
		idLayerView.style.display = "";
		idLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在载入数据，请稍候......</font>";
		//adslLayerView.style.width = document.body.clientWidth;
		//adslLayerView.style.display = "";
		//adslLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在等待上一步“端口”相关信息载入...</font>";
		pppoeLayerView.style.width = document.body.clientWidth;
		pppoeLayerView.style.display = "";
		pppoeLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在等待上一步“端口”相关信息载入...</font>";
		ipLayerView.style.width = document.body.clientWidth;
		ipLayerView.style.display = "";
		ipLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在等待上一步“PPPoE”相关信息载入...</font>";
		wlanLayerView.style.width = document.body.clientWidth;
		wlanLayerView.style.display = "";
		wlanLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在等待上一步“静态IP”相关信息载入...</font>";
		dhcpLayerView.style.width = document.body.clientWidth;
		dhcpLayerView.style.display = "";
		dhcpLayerView.innerHTML = "&nbsp;&nbsp;<font class='waitMsgFont'>正在等待上一步“WLAN”相关信息载入...</font>";
		
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm").src=page;
	}
	
	function reloadPort_DO(timeCounter) {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData.jsp?device_id="+device_id+"&className="+className+"&timeCounter="+timeCounter+"&refresh="+Math.random();
		document.all("childFrm").src=page;
	}
	
	function loadADSL() {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_adsl.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm1").src=page;
	}
	
	function reloadADSL_DO(timeCounter) {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_adsl.jsp?device_id="+device_id+"&timeCounter="+timeCounter+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm1").src=page;
	}
	
	function loadPPPoE() {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_lan.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm2").src=page;
	}
	
	function reloadPPPoE_DO(timeCounter) {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_lan.jsp?device_id="+device_id+"&timeCounter="+timeCounter+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm2").src=page;
	}
	
	function loadIP() {
	var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_ip.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm3").src=page;
	}
	function reloadIP_DO(timeCounter) {
	var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_ip.jsp?device_id="+device_id+"&timeCounter="+timeCounter+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm3").src=page;
	}
	
	function loadWLAN() {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_wlan.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm4").src=page;
	}
	function reloadWLAN_DO(timeCounter) {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_wlan.jsp?device_id="+device_id+"&timeCounter="+timeCounter+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm4").src=page;
	}
	
	function loadDHCP() {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_dhcp.jsp?device_id="+device_id+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm5").src=page;
	}
	function reloadDHCP_DO(timeCounter) {
		var device_id="<%=device_id%>";
		var className="<%=className%>";
		var page="getDeviceStatusData_dhcp.jsp?device_id="+device_id+"&timeCounter="+timeCounter+"&className="+className+"&refresh="+Math.random();
		document.all("childFrm5").src=page;
	}
	
	//存在上次点击的行对象
	var _srcObj;
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		if(_srcObj != null){
			_srcObj.className="";
		}
		var obj = event.srcElement.parentElement;
		var tableObj = obj.parentElement;
		
		obj.className="trOver";
		_srcObj = obj;

		page = "../Visualman/now_getPort.jsp?device_id="+<%=device_id%>+"&ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		window.open(page);
	}
	
	function tableToExcel() {   
		  window.clipboardData.setData("Text",document.all('theObjTable').outerHTML); 
		  try 
		  { 
		  var ExApp = new ActiveXObject("Excel.Application") 
		  var ExWBk = ExApp.workbooks.add() 
		  var ExWSh = ExWBk.worksheets(1) 
		  ExApp.DisplayAlerts = false 
		  ExApp.visible = true 
		  }
		  catch(e) 
		  { 
		  alert("您的电脑没有安装Microsoft Excel软件！") 
		  return false 
		  }   
		  ExWBk.worksheets(1).Paste; 
	}

  function tableToWord() {   
	  var oWD = new ActiveXObject("Word.Application"); 
	  var oDC = oWD.Documents.Add("",0,1); 
	  var oRange =oDC.Range(0,1); 
	  var sel = document.body.createTextRange(); 
	  sel.moveToElementText(theObjTable); 
	  sel.select(); 
	  sel.execCommand("Copy"); 
	  oRange.Paste(); 
	  oWD.Application.Visible = true; 
  }   
  
  function CellAreaExcel() 
	{
	 var oXL = new ActiveXObject("Excel.Application"); 
	 var oWB = oXL.Workbooks.Add(); 
	 var oSheet = oWB.ActiveSheet; 
	 var Lenr = theObjTable.rows.length;
	 for (i=0;i<Lenr;i++)
	 { 
	  var Lenc = theObjTable.rows(i).cells.length; 
	  for (j=0;j<Lenc;j++) 
	  {
	   oSheet.Cells(i+1,j+1).value = theObjTable.rows(i).cells(j).innerText; 
	  } 
	 } 
	 oXL.Visible = true; 
	}
	
	function reloadPort(timeCounter) {
		//idLayerView.innerHTML = "&nbsp;&nbsp;<font>正在载入数据，请稍候......</font>";
		showMsgDlg("端口");
		//window.childFrm.location.reload();
		reloadPort_DO(timeCounter);
		//closeMsgDlg();
	}
	
	function reloadADSL(timeCounter) {
		//adslLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据，请稍候......";
		showMsgDlg("ADSL");
		reloadADSL_DO(timeCounter);
		//closeMsgDlg();
	}
	
	function reloadPPPoE(timeCounter) {
		//pppoeLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据，请稍候......";
		showMsgDlg("PPPoE");
		reloadPPPoE_DO(timeCounter);
		//window.childFrm2.location.reload();
		//closeMsgDlg();
	}
	
	function reloadIP(timeCounter) {
		//ipLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据，请稍候......";
		showMsgDlg("静态IP");
		reloadIP_DO(timeCounter);
		//window.childFrm3.location.reload();
		//closeMsgDlg();
	}
	
	function reloadWLAN(timeCounter) {
		//wlanLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据，请稍候......";
		showMsgDlg("WLAN");
		reloadWLAN_DO(timeCounter);
		//window.childFrm4.location.reload();
		//closeMsgDlg();
	}
	
	function reloadDHCP(timeCounter) {
		//dhcpLayerView.innerHTML = "&nbsp;&nbsp;正在载入数据，请稍候......";
		showMsgDlg("DHCP");
		reloadDHCP_DO(timeCounter);
		//window.childFrm5.location.reload();
		//closeMsgDlg();
	}
	
	function showMsgDlg(strMsg){
	strMsg = "正在重新载入“"+strMsg+"”相关数据，请稍候......";
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
	document.all.txtLoading.innerText=strMsg;
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

//-->
</SCRIPT>

<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: 宋体"></span>
		</td>
	</tr>
</table>
</center>
</div>

<body onload="loadPort();">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="theObjTable">
	  <tr>
		<td bgcolor="#FFFFFF">       
			<table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			  <tr>
				<td width="162" align="center"  class="title_bigwhite"><%=title%></td>
				<td align="left"><span class="text">&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> 以下是设备<%= deviceName %>:&nbsp;&nbsp;<%= loopbackip%> 的<%=title%> </span></td>         
			  </tr>
			</table>
			<table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">端口信息</TD>
			  </tr>
			  <tr>
				<td bgcolor="#FFFFFF"><DIV id="idLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>
			   <!-- 
			   <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">ADSL信息</TD>
			  </tr>
			   <tr>
				<td bgcolor="#FFFFFF"><DIV id="adslLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>
			  -->
			  <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">PPPoE信息</TD>
			  </tr>
			    <tr>
				<td bgcolor="#FFFFFF"><DIV id="pppoeLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>	
			 
			  <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">静态IP信息</TD>
			  </tr>
			    <tr>
				<td bgcolor="#FFFFFF"><DIV id="ipLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>	
			 
			  <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">WLAN信息</TD>
			  </tr>
			    <tr>
				<td bgcolor="#FFFFFF"><DIV id="wlanLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>	
			 
			  <tr>
				<TD style='border:1px solid #999999;' class="column8" colspan="10">DHCP信息</TD>
			  </tr>
			    <tr>
				<td bgcolor="#FFFFFF"><DIV id="dhcpLayerView" style="overflow:auto;width:100%;display:none;"></div></td>
			  </tr>	
			  
			</table>
		</td>
	  </tr>
	</table>
	<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
		<tr>
			<td width="23%" align=right class="foot">
				<!-- <input type="button" name="cmdQuery" value="导出到Word" class="jianbian" onclick="tableToWord()"> -->
				<!-- <input type="button" name="cmdQuery" value="导出到Excel" class="jianbian" onclick="tableToExcel()"> -->
				<input name="button" type="button" class="jianbian" value=" 刷 新 " onClick="javascript:window.location.reload()">
				<input name="button" type="button" class="jianbian" value=" 关 闭 " onClick="javascript:window.close()">
			</td>
	  </tr>
  </table>
  <IFRAME name="childFrm_A" ID=childFrm_A SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm" ID=childFrm SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm1" ID=childFrm1 SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm2" ID=childFrm2 SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm3" ID=childFrm3 SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm4" ID=childFrm4 SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  <IFRAME name="childFrm5" ID=childFrm5 SRC="" style="overflow:auto;width:100%;display:none;"></IFRAME>
  
</body>


