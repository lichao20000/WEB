<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet"%>
<jsp:useBean id="updateManage" scope="request" class="com.linkage.litms.software.UpdateManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

String[] device_list = request.getParameterValues("device_id");
//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_list[0]);
String strGatherId = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

String execu_type = request.getParameter("excute_type");


//策略定制,判断这个策略是否合法
if("2".equals(execu_type))
{
	boolean operResult =updateManage.isAllowConfig(request);
	if(!operResult)
	{
		response.sendRedirect("./configStock_jiangsu.jsp?operResult=notAllow");
		return;
	}
}

//入库成功，返回sheet数组
Sheet[] sheetObject = updateManage.sheetAct(request,devicetype_id);
String task_id = "";
// 将sheet_id 拼接，传给计划任务执行

task_id = updateManage.autoUpdateAct(sheetObject,request);

String strSql = updateManage.getSql(task_id);

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
	var page = "config_sheetList_jiangsu.jsp?tmpSql=" + tmpSql;
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

//查看任务详细信息
function showDetail(task_id,task_name,oui,device_serialnumber){
	var page;
	
	if (typeof(oui)== "undefined"){
		page = "../NetCutover/updateTaskDetail.jsp?task_id="+ task_id + "&task_name=" + task_name;
	}
	else {
		page = "../NetCutover/updateTaskDetail.jsp?task_id="+ task_id + "&task_name=" + task_name 
				+ "&oui=" + oui + "&device_serialnumber=" + device_serialnumber;
	}
	
	window.open(page,"","left=70,top=130,width=900,height=500,resizable=yes,scrollbars=yes");
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

init();

//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>