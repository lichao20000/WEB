<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
request.setCharacterEncoding("GBK");
			
String vpiid = request.getParameter("vpiid");
String vciid = request.getParameter("vciid");
// PVC 
String strPVC = "PVC:" + vpiid + "/" + vciid;
String ConnectionType = request.getParameter("ConnectionType");
String _LanInterface = request.getParameter("_LanInterface");


String ServiceList = request.getParameter("ServiceList");


// 软件升级的参数数组
String[] ArrHandSheet = {strPVC,ConnectionType,_LanInterface,ServiceList};

//---------------------------------------------------------------------------------

//入库成功，返回sheet数组
int flag = sheetManage.changeDevice(request);
Sheet[] sheetObject = sheetManage.insertDB(request,ArrHandSheet);

if(sheetObject == null || flag ==0 ){
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
	var page = "config_sheetList.jsp?tmpSql=" + tmpSql + "";
	//document.all("childFrm").src = page;
	
	location.href="resultByRunAfter.action?goback=../NetCutover/jt_work_hand_changdevice.jsp&gobefore=../NetCutover/WorkSheetView_device.jsp";
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