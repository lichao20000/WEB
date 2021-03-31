<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet,com.linkage.litms.common.database.*,com.linkage.litms.Global"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%--
	zhaixf(3412) 2008-04-11
	req:XJDX_ITMS-BUG-20080506-XXF-001
--%>
<%
request.setCharacterEncoding("GBK");

//----------------------------手工工单文件数组-----------------------------------------------
//				paraList.add("PVC:" + userObj.vpiid + "/" + userObj.vciid);
//				paraList.add("IP_Routed");
//				paraList.add(userObj.username);
//				paraList.add(userObj.passwd);
//				paraList.add("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1."
//						+ ",InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2."
//						+ ",InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3."
//						+ ",InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4."
//						+ ",InternetGatewayDevice.LANDevice.1.WLANConfiguration.1");
//				paraList.add("INTERNET");
//				sheet.setSheet_para(paraList); 
//
//业务ID
		// 业务类型id
String serv_type_id = request.getParameter("some_service");

// 操作类型ID
String oper_type_id = request.getParameter("oper_type");

String wan_type = request.getParameter("wan_type");

String service_id = Global.G_Service_Info_Id_Map.get(serv_type_id + "#" + oper_type_id + "#" + wan_type);


if(service_id == null || service_id.equals("")){
	
	service_id = Global.G_Service_Info_Id_Map.get(serv_type_id + "#" + oper_type_id + "#-1");
	
}
String vpiid = request.getParameter("vpiid");
String vciid = request.getParameter("vciid");
String vlanid= request.getParameter("vlanid");

String username=request.getParameter("username");
String passwd = request.getParameter("pwd");
// PVC 
String strPVC = "PVC:" + vpiid + "/" + vciid;
//wan connection
String wan_value_1 = request.getParameter("wan_value_1");
// WAN ppp connection 

//modified by yanhj 2007-12-29
wan_value_1 = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+wan_value_1 + ".";
//wan_value_1 = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+wan_value_1;
//modified end 2007-12-29
//静态IP上网方式
String ipaddress = request.getParameter("ipaddress");
String ipmask = request.getParameter("ipmask");
String gateway = request.getParameter("gateway");
String adsl_ser = request.getParameter("adsl_ser");

String[] list_LanInterface = request.getParameterValues("LanInterface");
String _LanInterface = "";
if(list_LanInterface != null ){
		for(int i=0;i<list_LanInterface.length;i++){
			if(i == 0){
				_LanInterface = list_LanInterface[i];
			} else {
				_LanInterface+="," + list_LanInterface[i];
			}
		}
}

String ssid1_username = request.getParameter("ssid1_username");
String ssid1_passwd = request.getParameter("ssid1_passwd");
String ssid2_username = request.getParameter("ssid2_username");
String ssid2_passwd = request.getParameter("ssid2_passwd");



// 软件升级的参数数组
String[] ArrHandSheet = null;
//2101 LAN IPTV开户
if(service_id.equals("1001") || service_id.equals("1101") || service_id.equals("1011")){ //开户,增加DHCP方式1011
	ArrHandSheet = new String[2];
	ArrHandSheet[0] = strPVC;
	ArrHandSheet[1] = _LanInterface;
} else if (service_id.equals("2101")) {
	ArrHandSheet = new String[2];
	ArrHandSheet[0] = vlanid;
	ArrHandSheet[1] = _LanInterface;
	
} else if(service_id.equals("1003") || service_id.equals("1103")){
	ArrHandSheet = new String[1];         //销户
	ArrHandSheet[0] = wan_value_1;
} else if(service_id.equals("1008")){
	ArrHandSheet = new String[4];
	ArrHandSheet[0] = strPVC;
	ArrHandSheet[1] = username;
	ArrHandSheet[2] = passwd;		
	ArrHandSheet[3] = _LanInterface;
} else if(service_id.equals("1010")){//静态IP开户
	ArrHandSheet = new String[6];
	ArrHandSheet[0] = strPVC;
	ArrHandSheet[1] = ipaddress;
	ArrHandSheet[2] = ipmask;
	ArrHandSheet[3] = gateway;	
	ArrHandSheet[4] = adsl_ser;
	ArrHandSheet[5] = _LanInterface;
}  else if(service_id.equals("2001")){//桥接开户
	ArrHandSheet = new String[2];
	ArrHandSheet[0] = vlanid;
	ArrHandSheet[1] = _LanInterface;
} else if(service_id.equals("2008")){//路由开户
		ArrHandSheet = new String[6];
		ArrHandSheet[0] = vlanid;
		ArrHandSheet[1] = username;
		ArrHandSheet[2] = passwd;
		ArrHandSheet[3] = _LanInterface;
} else{                               //暂停和复机
	ArrHandSheet = new String[1];                                 
	ArrHandSheet[0] = wan_value_1 + "WANDSLLinkConfig.Enable";	
}                                 

//---------------------------------------------------------------------------------
//更新用户表，把用户表的业务更形成操作的业务。

//int flag = sheetManage.updateHgwcustomer(request);


//入库成功，返回sheet数组
Sheet[] sheetObject = sheetManage.insertDB(request,ArrHandSheet);

if(sheetObject == null){
	response.sendRedirect("excuError.jsp?msg=DBerror&returnJsp=jt_Work_handForm.jsp");
	return;
}

// 开始调用corba接口
Sheet[] sheetObj = paramManage.sheetInform(request,sheetObject);

String strSql = "";
String errorSheet="";
if(sheetObj !=null && sheetObj.length>0){
	if(sheetObj.length == 1){
		Sheet sh_sheet = sheetObj[0];
		if(sh_sheet.sheet_id.equals("XXX")){
			response.sendRedirect("excuError.jsp?returnJsp=jt_Work_handForm.jsp");
			return;
		}else{
			if(sh_sheet.time == -3){
				errorSheet = sh_sheet.sheet_id;			
			}
			strSql = (String)sheetManage.getSql(sheetObj);
		}		
	}else{
		for(int i=0;i<sheetObj.length;i++){
			Sheet  sheet = sheetObj[i];
			if(sheet.time == -3){
				if(errorSheet.equals("")){
					errorSheet = sheet.sheet_id;	
				}else{
					errorSheet += "|" + sheet.sheet_id;
				}
			}
		}
		strSql = (String)sheetManage.getSql(sheetObj);
	}
}else{
	response.sendRedirect("excuError.jsp?returnJsp=jt_Work_handForm.jsp");
	return;
}


%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
	var page = "config_sheetList.jsp?tmpSql=" + tmpSql + "&service_id=" + "<%=service_id%>";
	document.all("childFrm").src = page;
	showMsgDlg();
}
function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
function doDbClick(o){
	parames = o.parames;
	arrParame = parames.split(",");
	sheet_id = arrParame[0];
	receive_time = arrParame[1];
	gather_id = arrParame[2];
	page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;

	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: 宋体">请稍等······</span>
		</td>
	</tr>
</table>
</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<FORM name="frm" action="" >
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR><td>
			<Div id="idList">
			</Div>
			</td>
		</TR>
	</TABLE>
</TD></TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
var erSheet = "<%=errorSheet%>";
if(erSheet == ""){
	alert("调用后台接口成功！");
}else{
	alert("工单『<%=errorSheet%>』操作的设备正在执行！");
}
init();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>