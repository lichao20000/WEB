<jsp:useBean id="fileSevice" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.DeviceAct,java.util.*"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%@page import="ACS.Sheet"%>

<%
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	//String devicetype_id = request.getParameter("devicetype_id");
	String service_id = request.getParameter("connType");
	
	String username = request.getParameter("RouteAccount");
	String passwd = request.getParameter("RoutePasswd");
	String vpiid = request.getParameter("vpiid");
	String vciid = request.getParameter("vciid");
	
	String pvc = vpiid +"/"+ vciid;
	//根据设备 由数据库中查询获得采集点
	DeviceAct act = new DeviceAct();
	HashMap deviceInfo= act.getDeviceInfo(device_id);
	String gather_id = (String)deviceInfo.get("gather_id");
	String devicetype_id = (String)deviceInfo.get("devicetype_id");
	
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	//resultVal = fileSevice.chgBusiness(device_id, gather_id, ior, request);
	String connNodeAndpppNode = fileSevice.getInternetPVCNode(device_id, gather_id, ior, pvc);
	
	String wconnNode = connNodeAndpppNode.split(",")[0];
	String wpppConnNode = connNodeAndpppNode.split(",")[1];
	
	if (null == wconnNode || "-1".equals(wconnNode)) {
		response.sendRedirect("excuError.jsp?msg=connError&returnJsp=chgBusiness.jsp");
		return;
	} else if (null == wconnNode || "".equals(wconnNode)) {
		response.sendRedirect("excuError.jsp?msg=noNodeError&returnJsp=chgBusiness.jsp");
		return;
	}
	
	int parasNum = 0;
	String countTemplateParas = sheetManage.countTemplateParas(devicetype_id, service_id);
	if (null != countTemplateParas && !"".equals(countTemplateParas)) {
		parasNum = Integer.parseInt(countTemplateParas);
	}
	
	if (0 == parasNum) {
		response.sendRedirect("excuError.jsp?msg=noTemplateError&returnJsp=chgBusiness.jsp");
		return;
	}
	
	String[] ArrHandSheet = null;
	ArrHandSheet = new String[parasNum];
	if (parasNum == 5) {
		ArrHandSheet[0] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice." + wconnNode+ ".";
		ArrHandSheet[1] = "PVC:" + pvc;
		ArrHandSheet[2] = username;
		ArrHandSheet[3] = passwd;
		ArrHandSheet[4] = "";
	} else {
		ArrHandSheet[0] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+ wconnNode +".WANPPPConnection."+ wpppConnNode +".ConnectionType";
		ArrHandSheet[1] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+ wconnNode +".WANPPPConnection."+ wpppConnNode +".Username";
		ArrHandSheet[2] = username;
		ArrHandSheet[3] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+ wconnNode +".WANPPPConnection."+ wpppConnNode +".Password";
		ArrHandSheet[4] = passwd;
		ArrHandSheet[5]= "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."+ wconnNode +".WANPPPConnection."+ wpppConnNode +".Enable";
	}
	

	int flag = 1;
	//入库成功，返回sheet数组
	Sheet[] sheetObject = sheetManage.insertDB_chgBusiness(device_id,service_id, devicetype_id, ArrHandSheet);
	
	
	if(sheetObject == null || flag == 0){
		response.sendRedirect("excuError.jsp?msg=DBerror&returnJsp=chgBusiness.jsp");
		return;
	}

	// 开始调用corba接口
	Sheet[] sheetObj = paramManage.sheetInform(request,sheetObject);
	//Sheet[] sheetObj = sheetObject;
	//sheetObj[0].sheet_id = "123";
	
	String strSql = "";
	String errorSheet="";
	if(sheetObj !=null && sheetObj.length>0){
		if(sheetObj.length == 1){
			Sheet sh_sheet = sheetObj[0];
			if(sh_sheet.sheet_id.equals("XXX")){
				response.sendRedirect("excuError.jsp?returnJsp=chgBusiness.jsp");
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
		response.sendRedirect("excuError.jsp?returnJsp=chgBusiness.jsp");
		return;
	}

%>

<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
    var service_id = "<%= service_id%>";
    var username = "<%= username%>";
    ar passwd = "<%= passwd%>";
    
	//var page = "config_sheetList.jsp?tmpSql=" + tmpSql + "&service_id=" + service_id + "&username=" + username + "&passwd=" + passwd;
	
	//document.all("childFrm").src = page;
	location.href="resultByRunAfter.action?goback=../diagnostic/chgBusiness.jsp&gobefore=../NetCutover/WorkSheetView_device.jsp";
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
	alert("成功找到，并开始重建！");
}else{
	alert("工单『<%=errorSheet%>』操作的设备正在执行！");
}
init();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>