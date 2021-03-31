<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet"%>
<%@page import="com.linkage.litms.paramConfig.ParamManage"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
String instance = request.getParameter("instance");
String keyword = request.getParameter("keyword");
String paramvalue = request.getParameter("paramvalue");
String action = request.getParameter("action");
String forwardJSP = "paramInstanceConfig.jsp";
if(null==keyword)
{
	keyword = "";
}
String[] params = null;
//参数实例增加及配置
if("add".equals(action))
{
	params = new String[3];
	params[0]=instance;
	params[1]=keyword;
	params[2]=paramvalue;
}
//实例删除配置
else if("delete".equals(action))
{
	params = new String[2];
	params[0]=instance;
	params[1]=keyword;
	forwardJSP ="paramInstanceDel.jsp";
}
//命令参数实例配置
else if("cmd".equals(action))
{
	params = new String[2];
	params[0]=instance;
	params[1]=paramvalue;
	forwardJSP ="paramCMDConfig.jsp";
}
//配置维护帐号
else if("account".equals(action))
{
	params = new String[1];
	String param1 = request.getParameter("param1");
	params[0]=param1;
	forwardJSP ="paramConfigAccount.jsp";
}

//入库
Sheet[] sheetObject = ParamManage.sheetOperation_1(request,params);
Sheet[] sheetObj = null;

String strSql = "";
String errorSheet="";

// 将sheet_id 拼接，传给计划任务执行
String execu_type = request.getParameter("excute_type");
if(execu_type.equals("1")){
	String sheetArr_list = "";
	for(int i=0;i<sheetObject.length;i++){
		Sheet sh_sheet =sheetObject[i];
		if(i==0){
			sheetArr_list = sh_sheet.sheet_id;
		}else{
			sheetArr_list +="," + sh_sheet.sheet_id;
		}	
	}

	response.sendRedirect("../Resource/SheetExeRuleForm.jsp?sheetArr=" + sheetArr_list);
	return;
}

//入库成功
if(null!=sheetObject)
{
    //入库成功通知后台
	sheetObj = ParamManage.sheetInform(request,sheetObject);	
	if(sheetObj !=null && sheetObj.length>0){
		if(sheetObj.length == 1){
			Sheet sh_sheet = sheetObj[0];
			if(sh_sheet.sheet_id.equals("XXX")){			
				response.sendRedirect("excuError.jsp?returnJsp="+forwardJSP);
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
		response.sendRedirect("excuError.jsp?returnJsp="+forwardJSP);
		return;
	}
}
else
{
	response.sendRedirect("excuError.jsp?msg=DBerror&returnJsp="+forwardJSP);
	return;
}



%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
	var page = "../software/config_sheetList.jsp?tmpSql=" + tmpSql + "";
	//document.all("childFrm").src = page;
	location.href="resultByRunAfter.action?goback=../paramConfig/paramInstanceConfig.jsp&gobefore=../NetCutover/WorkSheetView_device.jsp";
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
	page = "../NetCutover/sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;

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