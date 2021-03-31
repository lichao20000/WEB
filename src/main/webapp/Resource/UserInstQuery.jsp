<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%@ include file="../head.jsp"%>
<style>
.#msg{background:#68af02;color:#fff;left:50%;top:0px;position:absolute;margin-left:-25%;margin-left:expression(-this.offsetWidth/2);font-size:12px;text-align:center;padding:0 28px;height:20px;line-height:20px;white-space:nowrap;}
.errmsg {background:#ef8f00;}
</style>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<script language="JavaScript">
<!--
var mouse_y = 200;
function CreateAjaxReq(url,param,successFunc,errorFunc){
	showProcess(true);
	var myAjax
		= new Ajax.Request(
							url,
							{
								method:"post",
								parameters:param,
								onFailure:errorFunc,
								onSuccess:successFunc
							 }
						  );
}
function doQuery(){
	var city_id = "";
	if(document.all("city_id") == null || document.all("city_id").value == "" || document.all("city_id").value < 0){
		alert("请选择属地");
		document.all("city_id").focus();
		return false;
	}else{
		city_id = document.all("city_id").value;
	}
	if(document.all("device_serialnumber").value.trim().length < 6 && document.all("device_serialnumber").value.trim().length > 0){
 		alert("请输入至少最后6位设备序列号 !");
		document.all("device_serialnumber").focus();
		return;        
    }
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var username = $("username").value;
	var deviceserialnumber = $("device_serialnumber").value;
	var param = "city_id=" + city_id;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&username=" + username;
	param += "&tt=" + new Date().getTime();
	param += "&device_serialnumber=" + deviceserialnumber;
	
	CreateAjaxReq("UserInstQueryData.jsp",param,showResult,showError);
}
function showResult(response){
	$("QueryData").innerHTML = response.responseText;
	showProcess(false);
}
function showError(response){
	_debug.innerHTML = response.responseText;
}
function showProcess(flag){
	flag ? $("msg").show() : $("msg").hide();
}
function ToExcel() {
		var city_id = "";
	if(document.all("city_id") == null || document.all("city_id").value == "" || document.all("city_id").value < 0){
		alert("请选择属地");
		document.all("city_id").focus();
		return false;
	}else{
		city_id = document.all("city_id").value;
	}
	if(document.all("device_serialnumber").value.trim().length < 6 && document.all("device_serialnumber").value.trim().length > 0){
 		alert("请输入至少最后6位设备序列号 !");
		document.all("device_serialnumber").focus();
		return;        
    }
	
	var page="UserInstQueryToExcel.jsp?";
	page += "start_time=" + $("start_time").value;
	page += "&end_time=" + $("end_time").value;
	page += "&username=" + $("username").value;
	page += "&city_id=" + city_id;
	page += "&device_serialnumber=" + $("device_serialnumber").value;
	
	document.all("childFrm").src=page;
	//window.open(page);
}

function goPage(offset){
	
	var city_id = "";
	if(document.all("city_id") == null || document.all("city_id").value == "" || document.all("city_id").value < 0){
		alert("请选择属地");
		document.all("city_id").focus();
		return false;
	}else{
		city_id = document.all("city_id").value;
	}
	var page="&offset="+offset
	page += "&start_time=" + $("start_time").value;
	page += "&end_time=" + $("end_time").value;
	page += "&username=" + $("username").value;
	page += "&city_id=" + city_id;
	CreateAjaxReq("UserInstQueryData.jsp",page,showResult,showError);
	//document.all("childFrm").src = page;
	
}
//-->
</script>
<%
request.setCharacterEncoding("GBK");
String strCityList = DeviceAct.getCityListAll(false,curUser.getCityId(),"",request);
DateTimeUtil dateTimeUtil = new DateTimeUtil();
String end_time = dateTimeUtil.getDate();
dateTimeUtil.getNextDate(-1);
String start_time = dateTimeUtil.getDate();

%>
<br>
<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" align=center>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						现场安装信息查询
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
				<tr bgcolor=#ffffff>
					<td class=column align=center>开始时间</td>
					<td>
						<input type="text" id="start_time" name="start_time" class=bk value="<%=start_time%> 00:00:00">
						<input type="button" value="▼" class=btn onClick="showCalendar('all',event)" name="button2">
					</td>
					<td class=column align=center>结束时间</td>
					<td>
						<input type="text" id="end_time" name="end_time" class=bk value="<%=end_time%> 23:59:59">
						<input type="button" value="▼" class=btn onClick="showCalendar('all',event)" name="button2">
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center>属  地</td>
					<td><%=strCityList%></td>
					<td class=column>用户账号</td>
					<td><input type="text" id="username" name="username"></td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center>设备序列号: </td>
					<td>
						<input type="text" id="device_serialnumber" name="device_serialnumber" class="bk" size="30">&nbsp;&nbsp;(请输入至少最后6位.)
					</td>
					<td class=column colspan=2 align=right><input type="button" value="查 询" onclick="doQuery()"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<div id="QueryData" align=center>查询数据</div>
		</td>
	</tr>
	<TR>
		<td align=right>
			<a href="#" name="export" onclick="ToExcel()">导出到Excel</a>
		</td>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
<div id="msg" style="display:none">正在操作....</div>
<div id="divDetail" style="position:absolute;z-index:255;top:200px;left:100px;width:70%;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none"></div>
<div id=_debug></div>
<%
	DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>