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
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var param = "city_id=" + city_id;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&type=state";
	param += "&tt=" + new Date().getTime();
	
	CreateAjaxReq("UserInstReportData.jsp",param,showResult,showError);
}

function showResult(response){
	$("QueryData").innerHTML = response.responseText;
	showProcess(false);
	InitDetail();
}
function showError(response){
	_debug.innerHTML = response.responseText;
}
function InitDetail(){
	var spanArr = $$('span[_type="detail"]');
	var shDetail = "showDetail";
	var js = null;
	spanArr.each(function(item){
		js = shDetail + "('" + item._dealstaff + "','" + item._city_id + "')";
		item.innerHTML = "<a href=javascript:// onclick=\"" + js + "\">详细数据</a>";
		js = null;
	});
	js = null;
	shDetail = null;
	spanArr = null;
}
function showDetail(staff,city_id){
	mouse_y = event.clientY;
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var param = "city_id=" + city_id;
	param += "&dealstaff=" + staff;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&type=detail";
	param += "&tt=" + new Date().getTime();

    CreateAjaxReq("UserInstReportData.jsp",param,showDetailResult,showError);
	
	//_divDetail.innerHTML = "正在生成详细资料...";
}
function showDetailResult(response){
	showProcess(false);
	var _divDetail = $("divDetail");
	_divDetail.show();
	//_divDetail.style.left = event.X;
	_divDetail.style.top = mouse_y + document.body.scrollTop;
	_divDetail.innerHTML = response.responseText;
}
function showProcess(flag){
	flag ? $("msg").show() : $("msg").hide();
}
function CloseDetail(){
	$("divDetail").hide();
	showProcess(false);
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
	
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var param = "city_id=" + city_id;
	param +="&offset=" + offset;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&type=state";
	param += "&tt=" + new Date().getTime();
	
	CreateAjaxReq("UserInstReportData.jsp",param,showResult,showError);
	
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
						现场安装统计
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
					<td class=column>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column colspan=4 align=right><input type="button" value=" 统 计 " onclick="doQuery()"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<div id="QueryData" align=center>统计数据</div>
		</td>
	</tr>
</TABLE>
<div id="msg" style="display:none">正在操作....</div>
<div id="divDetail" style="position:absolute;z-index:255;top:200px;left:100px;width:70%;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none"></div>
<div id=_debug></div>
<%
	DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>