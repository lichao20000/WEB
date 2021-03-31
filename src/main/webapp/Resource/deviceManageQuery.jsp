<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%
String start_time = new DateTimeUtil().getDate();
String city_id = curUser.getCityId();
SelectCityFilter CityFilter = new SelectCityFilter(request);
String strCityList = CityFilter.getAllSubCitiesBox(city_id, false,city_id, "per_city", true); 
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<script type="text/javascript" language="javaScript">
function query(){
	if(document.frm.per_city.value=='-1'){
	    alert('请选择属地！');
	    return false;
	}
	$("input[@name='button']").attr("disabled", true);
	document.frm.hidstart.value=document.frm.start_time.value + " " + document.frm.start_ms.value;
	document.frm.hidend.value=document.frm.end_time.value + " " + document.frm.end_ms.value;
	var page = "deviceManageData.jsp?start_time=" 
		+ strTime2Second(document.frm.hidstart.value)
		+ "&end_time=" + strTime2Second(document.frm.hidend.value)
		+ "&city_id=" + document.frm.per_city.value
		+ "&username=" + document.frm.username.value;
	
	document.all("childFrm").src = page;
	$("input[@name='button']").attr("disabled", false);
}

function queryDataForPrint(username,city,start_time,end_time){
	var url = "deviceManageDataPrint.jsp?start_time=" 
		+ start_time
		+ "&end_time=" + end_time
		+ "&city=" +city
		+ "&username=" + username;
	window.open(url,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
}

function queryDataForExcel(username,city,start_time,end_time){
	var url = "deviceManageDataExcel.jsp?start_time=" 
		+ start_time
		+ "&end_time=" + end_time
		+ "&city=" +city
		+ "&username=" + username;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPdf(username,city,start_time,end_time){
	var url = "deviceManageData.action?start_time=" 
		+ start_time
		+ "&end_time=" + end_time
		+ "&city=" +city
		+ "&username=" + username;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}

//-->
</script>

<%@ include file="../head.jsp"%>
<form name="frm" action="" method="POST">
	<table width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height=20></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							异常终端
						</TD>
						<td></td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR BGCOLOR=#ffffff><TH colspan="4">设备统计</TH></TR>
					<TR BGCOLOR=#ffffff>
						<TD class=column align="right">属地</TD>
						<TD>
							<%=strCityList%>
						</TD>
						<TD class=column align="right">用户帐号</TD>
						<TD>
							<input type="text" name="username" class="bk">
						</TD>
					</TR>
					<TR BGCOLOR=#ffffff>
						<TD class=column width="15%" align="right">开始时间</TD>
						<TD nowrap width="35%">
							<input type="text" name="start_time" id="start_time" class=bk value="<%=start_time %>" readonly class="form_kuang">
							<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2" style="cursor:pointer">
							<input type="text" name="start_ms" id="start_ms" class=bk value="00:00:00" class="form_kuang" size="10">
							<input type="hidden" name="hidstart" value="">
							<font color="#FF0000">*</font>
						</TD>
						<TD class=column width="15%" align="right">结束时间</TD>
						<TD nowrap width="35%">
							<input type="text" name="end_time" id="end_time" class=bk value="<%=start_time %>" readonly class="form_kuang">
							<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button3" style="cursor:pointer">
							<input type="text" name="end_ms" id="end_ms" class=bk value="23:59:59" class="form_kuang" size="10">
							<input type="hidden" name="hidend" value="">
							<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR BGCOLOR=#ffffff>
						<td colspan="4" align="right">
							<input type="button" name="button" class="btn" value=" 统 计 " onclick="query()" style="cursor:pointer"/>
						</td>
					</TR>
				</table>
			</td>
		</tr>
		<tr><td height="30"></td></tr>
		<tr>
			<td>
				<div style="display:none" id="showResult"></div>
			</td>
		</tr>
	</table>
	
	<div style="display:none"><iframe id="childFrm"></iframe></div>
</form>
<form name="frm2"></form>
<%@ include file="../foot.jsp"%>
