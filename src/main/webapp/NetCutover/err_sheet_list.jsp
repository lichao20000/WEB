<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.Map,java.util.List"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<%--
	zhaixf(3412) 2008-04-11
	req:NXDX_ITMS-BUG-Liul-20080411-001
--%>
<%
request.setCharacterEncoding("GBK");

String str_gather_id = request.getParameter("gather_id");
if(str_gather_id == null ){
	str_gather_id = "";
}
DeviceAct deviceAct = new DeviceAct();
String gatherList = deviceAct.getGatherList(session,str_gather_id,"",true);
UserRes userRes = (UserRes) session.getAttribute("curUser");
String stroffset = request.getParameter("offset");            


String user_city_id = userRes.getCityId();
String city_id = request.getParameter("city_id");
String ifcontainChild = request.getParameter("ifcontainChild");
String username = request.getParameter("username")==null?"":request.getParameter("username");
String device_serialnumber = request.getParameter("device_serialnumber")==null?"":request.getParameter("device_serialnumber");
String start_Time = request.getParameter("start_Time")==null?"":request.getParameter("start_Time");
String end_Time = request.getParameter("end_Time")==null?"":request.getParameter("end_Time");
if (city_id == null) {
	city_id = user_city_id;
}
if (ifcontainChild == null) {
	ifcontainChild = "1";
}
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList = City.getSelfAndNextLayer(true, city_id, "");

List list =  sheetManage.getErr_SheetList(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);

//DeviceResDataFilter deviceFilter = new DeviceResDataFilter(cursor, "device_id");
//cursor = (Cursor)deviceFilter.doFilter(userRes);
Map fields = cursor.getNext();

String[] arrStyle = new String[11];
arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
arrStyle[1] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
arrStyle[2] = "class=trOut  onmouseover='this.className=\"trOver\"' onmouseout='this.className=\"trOut\"'";
arrStyle[3] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
arrStyle[4] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
arrStyle[5] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
arrStyle[6] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
arrStyle[7] = "class=trOutchense onmouseover='this.className=\"trOverchense\"' onmouseout='this.className=\"trOutchense\"'";
arrStyle[8] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
arrStyle[9] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
arrStyle[10] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";


Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

//判别是否苏州

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//----------------------属地过滤 add by YYS 2006-9-24 ----------------
var user_city_id = "<%=user_city_id%>";//用户所属属地编号
var city_id = "<%=city_id%>";//用户所选中的属地编号
var stroffset = <%=stroffset%>;
function showChild(parname){
	var obj = event.srcElement;
        
	if(parname=='city_id'){
		city_id = obj.options[obj.selectedIndex].value;
		if(city_id!=-1){
			if(user_city_id != city_id){
				document.all("childFrm1").src = "cityFilter.jsp?city_id="+city_id;
			}else{
				document.all("my_city<%=city_id%>").innerHTML = "";
				//document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
			}
			document.all("hid_city_id").value = city_id;
		}else
			alert("请选择一个有效的属地");
	}
	
	if(parname == "gather_id"){
		filte();
	}
}

function Myreload(){
	window.location.href="err_sheet_list.jsp";//重新访问本身
	//location.reload();
}

function filte(){
	var ifcontainChild;
	for(var i=0;i<document.all("ifcontainChild").length;i++){
     	if(document.all("ifcontainChild")[i].checked){
        	ifcontainChild=document.all("ifcontainChild")[i].value;
        	break;
     	}
   	}
	var username=$("input[@name='username']").val();
	var device_serialnumber=$("input[@name='device_serialnumber']").val();
	var start_Time=$("input[@name='start_Time']").val();
	var end_Time=$("input[@name='end_Time']").val();
	window.location.href="?city_id="+city_id+"&ifcontainChild="+ifcontainChild+"&gather_id=" +  document.frm.gather_id.value+"&username="+username+"&device_serialnumber="+device_serialnumber+"&start_Time="+start_Time+"&end_Time="+end_Time;
}
//--------------------------------------------------------------------

var sheet_id,receive_time,gather_id;
function initParam(o){
	var curObj = o;
	
	while(curObj.tagName != "TR"){
		curObj = curObj.parentElement;
	}

	TR_parames = curObj.parames;
	arrParame = TR_parames.split(",");
	sheet_id = arrParame[0];
	receive_time = arrParame[1];
	gather_id = arrParame[2];
	//alert(TR_parames)
}

function createWork(){
	page = "AddDeviceForm_New.jsp?product_id="+ product_id +"&sheet_id="+ sheet_id +"&servtype="+ servtype;
	
	//alert(page);
	var win = window.showModalDialog(page,window,"status:no;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:400px;dialogWidth:780px");
	
	if(win == 1){
		refreshPage();
	}
}

function refreshPage(){
	this.location.reload();
}

function autoWork(){
//	alert(worksheet_id);
	document.all("childFrm1").src="sheet_reExec.jsp?sheet_id=" + sheet_id + "&receive_time="+ receive_time +"&gather_id=" + gather_id;
}

function handWork(){
	//打开新窗口，传递参数
	var handAction=window.showModalDialog("./showHandResult.jsp?work97_id="+sheet_id+"&t="+ new Date().getTime() + "&receive_time=" + receive_time,window,				"status:no;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:500px;dialogWidth:600px");
	if(typeof(handAction)=="undefined") return;
	if(handAction.indexOf("true") == 0){

		document.all("childFrm").src="Err_adslWork_Exec.jsp?worksheet_id=" + worksheet_id + "&work=hand&source=" + source + "&do=success&worksheet_status="+ worksheet_status +"&content="+ handAction.substring(handAction.indexOf(";")+1) +"&t="+ new Date().getTime() + "&receive_time=" + receive_time;

	}else if(handAction.indexOf("false") == 0){

		document.all("childFrm").src="Err_adslWork_Exec.jsp?worksheet_id=" + worksheet_id + "&work=hand&source=" + source + "&do=error&worksheet_status="+ worksheet_status +"&content="+ handAction.substring(handAction.indexOf(";")+1) +"&t="+ new Date().getTime() + "&receive_time=" + receive_time;


	}
	else{
		this.location.reload();
	}
}

function goDetail(){
	page = "Err_adslWork_List_detail.jsp?source=search&worksheet_id="+ worksheet_id +"&sheet_id="+ sheet_id +"&product_id="+ product_id + "&receive_time=" + receive_time;


	window.open(page,new Date().getTime(),"left=20,top=20,width=600,height=500,resizable=yes,scrollbars=yes");
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

function updateBgSetting(){
	document.all("childFrm").src="Err_adslWork_Exec.jsp?work=update&t=" + new Date().getTime();
}

function click_obj(id){
	initParam(event.srcElement);
    switch(id){
        case 0:    
            autoWork();
            break;
        case 1:
            handWork();
            break;
        case 2:
            goDetail();
            break;
        case 3:
            createWork();
            break;
    }

}
function selectAll(oStr){
	var oSelect = document.all(oStr);
	var bln = event.srcElement.checked;
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].checked = bln;
	}
}
function checkAll(oStr){
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		if(oSelect[i].checked) return true;
	}
	return false;
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
function sendAll(){
	if(document.frm.gather_id.value == -1){
		alert("请先用采集点过滤");
		return false;
	}
	if(checkAll('chbError')){
		showMsgDlg();
		document.frm.action = "err_sheet_SendAll.jsp?refresh="+Math.random();
		document.frm.submit();
	}
	else{
		alert("请选择错单");
		return false;
	}
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
<%@ include file="../toolbar.jsp"%>

<form name="frm" method="post" action="" target="childFrm">

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

<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							工单管理
						</td>
						<TD><IMG height=12
						src="../images/attention_2.gif" width=15>&nbsp;<font
						color="#FF0000">注</font>&nbsp;点击查看详细</TD>
					</tr>
				</table>
			</td>
		</tr>
	<TR>
	<TR>
		<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					
					<tr><th align="center" colspan="10" >错单处理</th></tr>
					<tr>
						<td align="left" colspan="10" bgcolor="white">
						请选择属地&nbsp;&nbsp;&nbsp;
						<span id="initCityList"><%=strCityList%></span>
						<span id="my_city<%=city_id%>"></span> &nbsp<!-- ;当前所选属地为:<span
							id=city_name><font color='red'><%=city_name%></font></span> -->
						&nbsp;包含所有下级属地: 
						<input type="radio" name='ifcontainChild' value='1' <%if (ifcontainChild.equals("1")){%>checked<%}%>>是 
						<input type="radio" name='ifcontainChild' value='0' <%if (ifcontainChild.equals("0")){%>checked<%}%>>否
						<input type="hidden" name = 'hid_city_id' value="<%=city_id%>">
						</td>
					</tr>
					<tr>
						<td align="left" colspan="10" bgcolor="white">
						loid:&nbsp;&nbsp;&nbsp;<input type="text" name ='username' value="<%=username%>">
						&nbsp;&nbsp;&nbsp;&nbsp;
						设备序列号:&nbsp;&nbsp;&nbsp;<input type="text" name ='device_serialnumber' value="<%=device_serialnumber%>">
						&nbsp;&nbsp;&nbsp;&nbsp;
						时间：
						<input type="text" name="start_Time" value='<%=start_Time%>' readonly >
						<img name="shortDateimg" onClick="WdatePicker({el:document.frm.start_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../images/dateButton.png" width="15" height="12"
						border="0" alt="选择">&nbsp;~&nbsp;
						<input type="text" name="end_Time" value='<%=end_Time%>' readonly>
						<img name="shortDateimg" onClick="WdatePicker({el:document.frm.end_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../images/dateButton.png" width="15" height="12"
						border="0" alt="选择"> 
						</td>
					</tr>
					<tr>
						<td align="left" bgcolor="white" colspan="10">请选择采集点&nbsp;&nbsp;
						<%=gatherList%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input TYPE="button"
							NAME="reFilter" value=" 返回初始 " onclick="Myreload()"
							>&nbsp; <input TYPE="button" NAME="btnFilter"
							value=" 过 滤 " onclick="filte()" ></td>					
					</tr>
					<tr>
						<th width="20">&nbsp;</th>
						<th width="" nowrap>工单编号</th>
						<th width="" nowrap>属地</th>
						<th width="" nowrap>用户帐户</th>
						<th width="" nowrap>设备序列号</th>
						<th width="" nowrap>执行结果</th>
						<th width="" nowrap>开始时间</th>
						<th nowrap>结束时间</th>
						<th nowrap width="120">失败原因描述</th>
						<th nowrap width="120">处理方式</th>
					</tr>
					<%
					if (fields != null) {
	                	String tmp,tmp_batch;
	                	int iStatus = 0;
	                	while (fields != null) {
                    		tmp = fields.get("sheet_id") + "," + fields.get("receive_time")+"," +  fields.get("gather_id");

		                    tmp_batch = fields.get("sheet_id") + "," + fields.get("receive_time")+ ","+fields.get("gather_id");

                    out.println("<tr "
                                    + arrStyle[iStatus]
                                    + " ondblclick=doDbClick(this) title='双击会显示工单详细信息'  parames='"
                                    + tmp + "' value='"
                                    + fields.get("") + "'>");
                    out.println("<td class=column1><INPUT TYPE='checkbox' NAME=chbError value='"
                                        + tmp_batch + "'></td>");
                    out.println("<td class=column1><nobr>"
                            + fields.get("sheet_id") + "</nobr></td>");
                    tmp = (String) fields.get("city_id");
                    out.println("<td class=column1><nobr>" + cityMap.get(tmp)
                            + "</nobr></td>");

                    out.println("<td class=column1><nobr>"
                            + fields.get("username") + "</nobr></td>");
                    out.println("<td class=column1><nobr>"
                            + fields.get("device_serialnumber") + "</nobr></td>");
                            
                            
                    tmp = (String)fields.get("exec_status");
                    tmp = (String) fields.get("fault_code");                           
                    if(tmp.equals("1")){
                    	tmp = "执行成功";
                    }else if(tmp.equals("-1")){
                    	tmp = "连接不上";
                    }else if(tmp.equals("-2")){
                    	tmp = "连接超时";
                    }else if(tmp.equals("-3")){
                    	tmp = "未找到相关工单";
                    }else if(tmp.equals("-4")){
                     	tmp = "未找到相关设备";                   
                    }else if(tmp.equals("-5")){
                     	tmp = "未找到相关RPC命令";                                         
                    }else if(tmp.equals("-6")){
                     	tmp = "设备正被操作";                       
                    }else if(tmp.equals("-7")){
                     	tmp = "系统参数错误，请联系厂家";                       
                    }else if(tmp.equals("-8")){
                     	tmp = "任务中前一策略失败导致，请重新激活(重新配置)";                       
                    }else if(tmp.equals("-9")){
                     	tmp = "系统内部错误，请联系厂家";                       
                    }else if(tmp.equals("-10")){
                     	tmp = "执行过程中网络或设备异常，请重启";                       
                    }else if(tmp.equals("-11")){
                     	tmp = "设备无法连接或不在线，请重启";                       
                    }else if(tmp.equals("-12")){
                     	tmp = "设备不支持，需要终端厂家处理";                       
                    }else if(tmp.equals("-13")){
                     	tmp = "后台临时处理，如需操作，请在页面重新配置";                       
                    }else if(tmp.equals("9001")){
                     	tmp = "请求拒绝";                       
                    }else if(tmp.equals("9002")){
                     	tmp = "请求拒绝";                       
                    }else if(tmp.equals("9003")){
                     	tmp = "参数不对";                       
                    }else if(tmp.equals("9004")){
                     	tmp = "资源超支";                       
                    }else if(tmp.equals("9005")){
                     	tmp = "节点不对";                       
                    }else if(tmp.equals("9006")){
                     	tmp = "节点类型不对";                       
                    }else if(tmp.equals("9007")){
                     	tmp = "节点值不对";                       
                    }else if(tmp.equals("9008")){
                     	tmp = "节点不可更改";                       
                    }else if(tmp.equals("9009")){
                     	tmp = "通知失败";                       
                    }else if(tmp.equals("9010")||tmp.equals("9014")||tmp.equals("9015")||tmp.equals("9016")||tmp.equals("9017")||tmp.equals("9018")||tmp.equals("9019")){
                     	tmp = "下载失败";                       
                    }else if(tmp.equals("9011")){
                     	tmp = "上传失败";                       
                    }else if(tmp.equals("9012")){
                     	tmp = "文件传输认证失败";                       
                    }else if(tmp.equals("9013")){
                     	tmp = "文件传输协议不支持";                       
                    }else if(tmp.equals("9899")){
                     	tmp = "配置失败,设备不支持";                       
                    }else if(tmp.equals("100000")){
                     	tmp = "设备未知错误";                       
                    }
					out.println("<td class=column1><nobr>"
                            + tmp
                            + "</nobr></td>");
					tmp = (String) fields.get("start_time");
					if(tmp!=null && !tmp.equals("")) {
						tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Long.parseLong((String) fields.get("start_time")));
					}
					 out.println("<td class=column1><nobr>"
                     +  tmp
                     + "</nobr></td>");
                    tmp = (String) fields.get("end_time");
                    if(tmp!=null && !tmp.equals("")){            
                    	tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("end_time")));         
					}
					out.println("<td class=column1><nobr>"
                            +  tmp
                            + "</nobr></td>");                        

                    tmp = (String) fields.get("fault_desc");
                    if (tmp == null || tmp.equals("null"))
                        tmp = "";
                    out.println("<td class=column1><nobr>" + tmp + "</nobr></td>"); 
                                      
                    out.println("<td class=column1><a href='#' onclick=click_obj(0)>重新激活</a></td>");
                    
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
                out
                        .println("<TR><TD class=foot COLSPAN=2><INPUT TYPE='checkbox' NAME=chbAll onclick=selectAll('chbError')> 全选</TD>");
                out
                        .println("<TD class=foot COLSPAN=8 align=left><INPUT TYPE='button' NAME=btnAll value='重新激活选中的错单' class=jianbian onclick='sendAll()'>(批量激活选中的错单)</TD></TR>");
//                out.println("<TR><TD class=column COLSPAN=10 align=right>"
//                        + errList.getStrBar() + "</TD></TR>");
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=10>没有工单记录</td></tr>");
            }
					
			out.println("<TR><TD class=column colspan=10 align=right>"	+ strBar + "</td></tr>");
            %>
				</TABLE>
			</TR>
	<TR>
		<TD HEIGHT=20><IFRAME name=childFrm ID=childFrm SRC=""
			STYLE="display:none"></IFRAME>&nbsp;<span style="display:none"></span></TD>
		<TD><IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME></TD>
	</TR>

</TABLE>
</form>
<%@ include file="../foot.jsp"%>
