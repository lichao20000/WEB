<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>策略配置执行情况统计</title>
<%@ include file="../toolbar.jsp"%>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<script language="JavaScript">
<!--
function checkForm(){
	var stime = strTime2Second(document.frm.startTime.value);
	var etime = strTime2Second(document.frm.endTime.value);
	//var cityid = $("input[@name='cityId']").val();
	//var serviceid = $("input[@name='serviceId']").val();
	var cityid = document.frm.cityId.value;
	var servTypeId = document.frm.serviceId.value;
	var url = "strategyConfigStat!statExecute.action";
	$.post(url,{
      startTime:stime,
      endTime:etime,
      cityId:cityid,
      serviceId:servTypeId
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
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td align="center">
		<table width="95%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">报表统计</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="95%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<TH colspan="4">策略配置统计报表</TH>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align=right>开始时间</td>
				<td><input type="text" name="startTime" value='2008-01-01' readonly
					class=bk> <img name="endimg"
					onclick="new WdatePicker(document.frm.startTime,'%Y-%M-%D',false,'whyGreen')"
					src="../images/search.gif" width="15" height="12" border="0"
					alt="选择">(YYYY-MM-DD)</td>
				<td class=column align=right>结束时间</td>
				<td><input type="text" name="endTime" value='' readonly
					class=bk> <img name="endimg"
					onclick="new WdatePicker(document.frm.endTime,'%Y-%M-%D',false,'whyGreen')"
					src="../images/search.gif" width="15" height="12" border="0"
					alt="选择">(YYYY-MM-DD)</td>
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
				<td class=column align=right>配置业务类型</td>
				<td>
					<select name="serviceId" class="bk">
						<option value="11">IPTV业务Qos配置</option>
						<!-- <option value="5">软件升级配置</option>  -->
					</select>
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan=4 align=right>
					<input type="button" value="统 计" onclick="checkForm();">
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