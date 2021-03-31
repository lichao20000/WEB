<%--
Author		: lizhaojun
Date		: 2007-4-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%
    request.setCharacterEncoding("GBK");
    Date dt = new Date();
    long lms = dt.getTime();
%>

<%@ include file="../head.jsp"%>

<%@page import="java.util.Date"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var lms_now = parseInt("<%=lms%>");	//当前时间
var node_number = 30;		//显示节点数
var subobj_cur=null;		//当前选中的子节点对象;
var objcn_cur = null;		//当前节点的className
var IsPoll = true;//false; //是否轮询

// ----------------------屏蔽树状日期结构隐藏 modify by YYS 2006-10-9 --------------
//将原树状日期结构替换为下拉框形式

//生成节点HTML add by YYS 2006-10-9
//将原树状日期结构替换为下拉框形式
// ------------------------------------------------------------------------------
function initOselect(){
	var sHtml = "";
	var dt,year,month,day;
	var lms_cur = parseInt(lms_now);
	//初始化所有节点
	for(var i=0;i<node_number;i++){
		dt = new Date(lms_cur);
		year	= dt.getYear();
		month	= dt.getMonth()+1;
		day		= dt.getDate();
		addOption(year,month,day);
		lms_cur = lms_cur - 24*3600*1000;
	}
	
	//更具服务器当前时间初始化工单查询页面
	dt = new Date(lms_now);
	year	= dt.getYear();
	month	= dt.getMonth()+1;
	day		= dt.getDate();
	dt = new Date(month+"/"+day+"/"+year);
	refreshData(dt.getTime()/1000);
}

function addOption(year,month,day){
	var nodeStr = year+"-"+month+"-"+day;
	var dt = new Date(month+"/"+day+"/"+year);
	var lms = dt.getTime()/1000;
	
	var oSselect = document.all("queryDate");
	var oOption = document.createElement("OPTION");
	oSselect.options.add(oOption);
	oOption.innerText = nodeStr;
	oOption.value = lms;
}

function refreshData(lms_now){
	
	var obj;
	var lms_start;

	if(lms_now==null){
		//alert("null");
		obj = event.srcElement;
		lms_start = obj.options[obj.selectedIndex].value;
	}else{//页面初始化时传参使用
		//alert(lms_now);
		lms_start = lms_now;
	}
	var page = "workSheet_xml_jiangsu.jsp?start="+lms_start+"&refresh="+Math.random();
	if(IsPoll){
		page += "&polltime="+getPolltime();
	}
	
	showMsgDlg();
	document.all("childFrm").src = page;

}

function getPolltime(){
	var o = document.frm;
	for(var i=0;i<o.polltime.length;i++){
		if(o.polltime[i].checked)
			return o.polltime[i].value;
	}
	return 5;
}


function reloadtime(v){
	page = document.all("childFrm").src;
	pos = page.indexOf("&polltime=");
	tmp = page.substring(0,pos);
	page = tmp + "&polltime="+ v;
	//alert(page)
	showMsgDlg();
	document.all("childFrm").src = page;
}

function reloadfilter(){

	var obj = document.frm2.filterstatus;
	var s,s1,s2;
	page = document.all("childFrm").src;
	//替换刷新随机数
	pos1 = page.indexOf("&refresh=");
	pos2 = page.indexOf("&polltime=");
	s1 = page.substring(0,pos1);
	
	if(pos2==-1) s2="";
	else s2=page.substring(pos2,page.length);
	
	if(obj[0].checked){
		s="-1";
	}
	else if (obj[1].checked){
		s="1";
	}
	else if (obj[2].checked){
		s="0";
	}
	else{
		s="-1";
	}
	
	page = s1+"&refresh="+Math.random()+"&filter="+ s + s2;

	document.all("childFrm").src = page;
	//alert(document.all("childFrm").src);
}

function TreeMouseOver(obj){
	objcn_cur = obj.className;
	obj.className="clsMouseOver";
}

function TreeMouseOut(obj){
	if(obj.className=="clsCurrentHasFocus") return;
	obj.className=objcn_cur;
}

function TreeMouseDown(obj){
	obj.className="clsMouseDown";
}

function initialize(){
	initOselect();

	//initTree();
	//idList.style.width = idHelp.offsetWidth + "px";
	//idList.style.height = idTree.offsetHeight - 28 +"px";
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

function wsState(){
	var rows = idTable.rows;
	var total = 0;
	var success =0;
	var excute=0;
	for(var i=1;i<rows.length;i++){
		if(rows[i].cells.length<2) continue;
		total += 1;
		v = parseInt(rows[i].value);
		
		if(v == 1){
			success += 1;
		}else{
			excute +=1;
		}
	}
	idDesc.innerHTML = "总任务数：<b>"+ total +"</b>  已完成任务数：<b><font color=green>"+ success 
	+"</font></b>   未完成任务数：<b><font color=red>"
	+ excute +"</font></b>";
}

//查看任务详细信息
function showDetail(task_id,task_name,oui,device_serialnumber){
	var page;
	
	if (typeof(oui)== "undefined"){
		page = "updateTaskDetail.jsp?task_id="+ task_id + "&task_name=" + task_name;
	}
	else {
		page = "updateTaskDetail.jsp?task_id="+ task_id + "&task_name=" + task_name 
				+ "&oui=" + oui + "&device_serialnumber=" + device_serialnumber;
	}
	
	window.open(page,"","left=70,top=130,width=900,height=500,resizable=yes,scrollbars=yes");
}
//-->
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
<link rel="stylesheet" href="../css/tree.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">请稍等······</span></td>
	</tr>
</table>
</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<!-- <TR><TD HEIGHT=20>&nbsp;</TD></TR> -->
	<TR>
		<TD align="center">
		<TABLE border=0 cellspacing=2 cellpadding=4 width="98%">
			<TR>
				<!-- ----------------------树状日期结构隐藏 modify by YYS 2006-10-9 -------------- -->
				<!--
				<TD width="180" bgcolor="#F1F1F1" valign=top><span class="clsMouseOver" style="width:179">工单视图</span>				
				<div id="idTree" style="overflow:auto;height:365;padding:4px 5px;"></div>				
				</TD>
				-->
				<!-- ----------------------------------------------------------------------------- -->
				<TD align=center valign=top>
				<TABLE border=0 cellpadding=0 cellspacing=0 width="100%">
					<TR bgcolor="">
						<TD id="idDesc">&nbsp;</TD>
					</TR>
					<TR>
						<TD WIDTH="783" valign=top>
						<TABLE>
							<TR WIDTH="" HEIGHT="">
								<!-- ----------------------屏蔽树状日期结构由下拉框代替 add by YYS 2006-10-9 -------------- -->
								<td>请选择任务制定日期：&nbsp;
								<select name="queryDate" onchange="refreshData()"></select>&nbsp;&nbsp;
								</td>
								<!-- -------------------------------------------------------------------------------------- -->
								<TD>
								<TABLE id="idHelp">
									<TR>
										<TD>
										<FORM name="frm" style="margin:0px">刷新频率选择：&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="3"
											onclick="reloadtime(this.value)">3分钟&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="5" checked
											onclick="reloadtime(this.value)">5分钟&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="10"
											onclick="reloadtime(this.value)">10分钟&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="15"
											onclick="reloadtime(this.value)">15分钟&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="30"
											onclick="reloadtime(this.value)">30分钟</FORM>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR id="idFilterStatus">
						<TD height=10>
						<FORM name="frm2" style="margin:0px">显示选择：&nbsp;&nbsp; <INPUT
							TYPE="radio" NAME="filterstatus" value="-1" checked>所有&nbsp; <INPUT
							TYPE="radio" NAME="filterstatus" value="1">已完成&nbsp; <INPUT
							TYPE="radio" NAME="filterstatus" value="0">未完成&nbsp; <INPUT
							TYPE="button" NAME="btnFilter" value=" 过 滤 "
							onclick="reloadfilter()" class=jianbian></FORM>
						</TD>
					</TR>					
					<TR>
						<TD valign=top>
						<div id="idList" style="overflow:auto;width:783px;height:300px;"></div>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript" src="Js/rightMenu.js"></SCRIPT>
<DIV class=cMenu id=ie5menu onclick=jumptoie5() onmouseout=lowlightie5()
	onmouseover=highlightie5()>
<DIV id='idMnu1' class=menuitems url='javascript:click_obj(0)'>重新激活</DIV>
<DIV id='idMnu2' class=menuitems url='javascript:click_obj(1)'>再次发送</DIV>
<!-- <DIV id='idMnu4' class=menuitems url='javascript:click_obj(3)'>添加新设备</DIV>-->
<DIV class=menuhr>
<hr style='width:100%'>
</DIV>
<DIV id='idMnu3' class=menuitems url=javascript:click_obj(2)>查看详细信息</DIV>
</DIV>
<SCRIPT LANGUAGE="JavaScript">
<!--
if (document.all && window.print){
    ie5menu.className="cMenu"
    document.body.onclick=hidemenuie5
}
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
initialize();
//-->
</SCRIPT>
