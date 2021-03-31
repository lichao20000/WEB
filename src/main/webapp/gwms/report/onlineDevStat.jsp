<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>在线设备统计</title>
<%@ include file="../../toolbar.jsp"%>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<script language="JavaScript">

function checkForm(){
	//if (document.frm.shortDate.value == "") {
		//alert("请选择日期！");
		//return;
	//}
	$("input[@name='button']").attr("disabled", true);
	var shortdate = strTime2Second(document.frm.shortDate.value);
	var shortdatesrc = document.frm.shortDate.value;
	var cityid = document.frm.cityId.value;
	var reportType = document.frm.reportTypetxt.value;
	var chartType = document.frm.chartType.value;
	var time_point = document.frm.time_point.value;
	var url = "onlineDevStat!statExecute.action";
	
	$.post(url,{
	  shortDate:shortdate,
      cityId:cityid,
      reportType:reportType,
      chartType:chartType,
      time_point:time_point,
      shortDateSrc:shortdatesrc
    },function(mesg){
    	var htmlMesg = init(mesg);
    	//$("div[@id='resultData']").innerHTML = mesg;
    	document.all("resultData").innerHTML = htmlMesg;
    	$("input[@name='button']").attr("disabled",false);
    });
	
}

function queryDataForExcel(shortDate,cityId,reportType,chartType,time_point){
	var url = "onlineDevStat!excel.action";	
	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPdf(shortDate,cityId,reportType,chartType,time_point){
	var url = "onlineDevStat!pdf.action";	
	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(shortDate,cityId,reportType,chartType,time_point){
	var url = "onlineDevStat!statExecute.action";	
	
	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	url = url+"&isReport=print"
	
	window.open(url,"","left=40,top=40,width=920,height=660,resizable=yes,scrollbars=yes");
}

function init(strHtml){
	var htmlStr = strHtml;
	htmlStr = htmlStr.replace("<table>", "<table width=\"95%\" align=\"center\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" bgcolor=\"#999999\">");
	htmlStr = htmlStr.replace(new RegExp("<td>","gm"), "<td class=column>");
	return htmlStr;
}
//strTime:yyyy-MM-dd
function strTime2Second(strTime){
	if(strTime == "" || typeof(strTime) == "undefined"){
		return 0;
	}
	var arrayTime = strTime.split("-");
	var second = (new Date(arrayTime[0], arrayTime[1]-1, arrayTime[2])).getTime()/1000;
	return second;
}

function show(type){
	document.frm.reportTypetxt.value = type;
	if("0"==type){
		this.month.style.display="none";
	}
	else{
		this.month.style.display="";
	}
}

//-->
</script>
<body>
<form name="frm" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="780" align=center>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td align="center">
		<table width="780" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">在线设备统计报表</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="780" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<TH colspan="4">在线设备统计报表</TH>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=right>报表类型</td>
				<td><input type="radio" name="reportType" value='0' onclick="show(this.value)" checked/>日报表 &nbsp;&nbsp;
				    <input type="radio" name="reportType" value='1' onclick="show(this.value)" />月报表
				    <input type="hidden" value="0" name="reportTypetxt" />
				</td>
				<td class=column align=right>日期</td>
				<td><input type="text" name="shortDate" value='' readonly class=bk> <img name="shortDateimg"
					onclick="new WdatePicker(document.frm.shortDate,'%Y-%M-%D',false,'whyGreen')"
					src="../../images/search.gif" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=right>属 地</td>
				<td><select name="cityId" class="bk">
					<s:iterator value="cityList">
						<OPTION value='<s:property value="city_id"/>'>
							<s:property value="city_name" />
						</OPTION>
					</s:iterator>
				</select></td>
				<td class=column align=right>报表类型</td>
				<td>
					<select name="chartType" class="bk">
						<option value="0">总视图</option>
						<option value="1">详细视图</option>
					</select>
				</td>
			</tr>
			<tr bgcolor=#ffffff id="month" STYLE="display:none">
				<td class=column align=right>时刻点</td>
				<td>
					<select name="time_point" class="bk">
						<option value="0">0点</option>
						<option value="1">1点</option>
						<option value="2">2点</option>
						<option value="3">3点</option>
						<option value="4">4点</option>
						<option value="5">5点</option>
						<option value="6">6点</option>
						<option value="7">7点</option>
						<option value="8">8点</option>
						<option value="9">9点</option>
						<option value="10">10点</option>
						<option value="11">11点</option>
						<option value="12">12点</option>
						<option value="13">13点</option>
						<option value="14">14点</option>
						<option value="15">15点</option>
						<option value="16">16点</option>
						<option value="17">17点</option>
						<option value="18">18点</option>
						<option value="19">19点</option>
						<option value="20">20点</option>
						<option value="21">21点</option>
						<option value="22">22点</option>
						<option value="23">23点</option>
					</select>
				</td>
				<td class=column align=right></td>
				<td></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan=4 align=right>
					<input type="button" name="button" value=" 统 计 " onclick="checkForm();">
					<input type="hidden" name="gwType" value="<s:property value="gwType"/>">
					<!-- <input type="hidden" name="serviceId" value="1101">  -->
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
	<tr>
		<td align="center">
			<div id="resultData"></div>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</TABLE>
</form>
<form name="frm2" action=""></form>
</body>
</html>
<br>
<br>
<%@ include file="../foot.jsp"%>