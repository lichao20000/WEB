<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>在线设备统计</title>
<%@ include file="../toolbar.jsp"%>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<script language="JavaScript">
<!--
function checkForm(){
	if (document.frm.shortDate.value == "") {
		alert("请选择日期！");
		return;
	}
	var shortdate = strTime2Second(document.frm.shortDate.value);
	var shortdatesrc = document.frm.shortDate.value;
	var cityid = document.frm.cityId.value;
	var url = "onlineDevStat!statExecute.action";
	$.post(url,{
	  shortDate:shortdate,
      cityId:cityid,
      reportType:0,
      shortDateSrc:shortdatesrc
    },function(mesg){
    	var htmlMesg = init(mesg);
    	//$("div[@id='resultData']").innerHTML = mesg;
    	document.all("resultData").innerHTML = htmlMesg;
    });
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
				<td><input type="radio" name="reportType" value='0' checked/>日报表 &nbsp;&nbsp;
				    <input type="radio" name="reportType" value='1'/>月报表
				</td>
				<td class=column align=right>日期</td>
				<td><input type="text" name="shortDate" value='' readonly class=bk> <img name="shortDateimg"
					onclick="new WdatePicker(document.frm.shortDate,'%Y-%M-%D',false,'whyGreen')"
					src="../images/search.gif" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
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
				<td class=column align=right></td>
				<td>
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan=4 align=right>
					<input type="button" value=" 统 计 " onclick="checkForm();">
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
<%@ include file="../foot.jsp"%>
</body>
</html>