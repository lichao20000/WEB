<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>最新版本统计</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	//query('<s:property value="cityId" />',strTime2Second2('<s:property value="addTime" />'))
});
var gw_type = '<s:property value="gw_type"/>';
//提交查询
function queryData(){
	
	var cityId = $("select[@name='cityId']");
	if(""==cityId.val() || null==cityId.val()){
		alert("请选择属地!");
		return false;
	}
	
	var addTime = $("input[@name='add_time']");
	if(""==addTime.val() || null==addTime.val()){
		alert("请填写查询时间!");
		return false;
	}
	var add_time = strTime2Second2(addTime.val());
	var intToday=Math.floor((new Date()).getTime()/1000);
	if(add_time>intToday){
		alert("查询时间不能大于系统当前时间!");
		return false;
	}
	
	query(cityId.val(),add_time);
}

//格式check之后的数据提交查询
function query(cityId,add_time){
	var url = "gwms/report/devicetypeNewestFind!getData.action";
	var dataList=$("div[@id='dataList']");
	dataList.html("数据正在统计中...");
	$("input[@name='button']").attr("disabled", true);
	$.post(url,{
		cityId:cityId,
		add_time:add_time,
		gw_type:gw_type
    },function(mesg){
		
    	dataList.html(mesg);
    	$("input[@name='button']").attr("disabled", false);
    });
	 
}

function queryDataForExcel(cityId,add_time){
	var url = "gwms/report/devicetypeNewestFind!getData.action?isReport=excel&cityId="+cityId+"&add_time="+add_time;
	url += "&gw_type="+gw_type;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

//将YYYY-dd-MM 字符串格式转换为 long 型
function strTime2Second2(dateStr){
	
	var temp = dateStr.split('-')
	var reqReplyDate = new Date();
	reqReplyDate.setYear(temp[0]);
    reqReplyDate.setMonth(temp[1]-1);
    reqReplyDate.setDate(temp[2]);
    reqReplyDate.setHours(0);
    reqReplyDate.setMinutes(0);
    reqReplyDate.setSeconds(0);
    
	return Math.floor(reqReplyDate.getTime()/1000);
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="selectForm" action="<s:url value="/gwms/report/deviceOnline!getDayReport.action"/>" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align=center>
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						最新版本统计
					</td>
					<td>
						<img src="../../images/attention_2.gif" width="15" height="12">		
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td >
			<table border=0 cellspacing=0 cellpadding=0 align="center" width="100%">
				<tr>
					<TD bgcolor="#999999">
						<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center" width="100%">
							<tr leaf="simple">
								<th colspan="4">最新版本统计</th>
							</tr>
							<tr bgcolor="#FFFFFF" style="display:">
								<td align=right width="15%">
									选择属地
								</td>
								<td  align=left width="35%">
									<s:select label="选择属地" list="cityList" name="cityId" listKey="city_id" listValue="city_name"
            							emptyOption="true"  value="cityId"/>
								</td>
								<td  align=right width="15%">
									查询时间
								</td>
								<td  align=left  width="35%">
									<input type="text" name="add_time" value='<s:property value="addTime" />' readonly class=bk>
									<!-- 
									<img name="shortDateimg"
										onclick="new WdatePicker(document.selectForm.add_time,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="选择">
									 -->
									 <img name="shortDateimg"
											onClick="WdatePicker({el:document.selectForm.add_time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择" />
									 (YYYY-MM-DD)
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" style="display:">
								<td align=right colspan="4" class="green_foot">
									<input type="button" name ="button" value=" 查 询 " class=jianbian onclick="javascript:queryData();"/>
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<TR >
		<TD >
			<div id="dataList" >
				<!--  数据正在统计中...-->
			</div>
		</TD>
	</TR>
</TABLE>
</form>
<form name="frm2"></form>
</body>
<%@ include file="../foot.jsp"%>