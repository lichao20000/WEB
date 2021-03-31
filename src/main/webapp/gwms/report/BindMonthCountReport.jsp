<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>绑定率月报表</title>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jsDate/WdatePicker.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript">

/*-----------------------------------------------------
//功能  :	页面初始化
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
$(function(){
	//queryData('<s:property value="cityId"/>',strDate2Second('<s:property value="endData"/>'));
});

/*-----------------------------------------------------
//函数名:		doQuery
//功能  :	针对查询按钮的功能
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function doQuery(){
	$("input[@name='button']").attr("disabled", true);
	queryData($("input[@name='cityId']").val(),strDate2Second($("input[@name='endDataInt']").val()));
	$("input[@name='button']").attr("disabled", false);
}

/*-----------------------------------------------------
//函数名:		queryData
//参数  :	cityId-->属地ID（类似‘00’）
			endDataInt-->时间偏移到秒
//功能  :	针对具有子属地的属地提供查询
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function queryData(cityId,endDataInt){
	$("div[@id='resultData']").html("正在获取统计数据...");
	startProgress();
	var url = "<s:url value='/gwms/report/bindMonthCount!getReportData.action'/>";
	$.post(url,{
		cityId:cityId,
		endDataInt:endDataInt
	},function(ajax){
	    $("div[@id='resultData']").html("");
		$("div[@id='resultData']").append(ajax);
		stopProgress();
	});	
}

/*-----------------------------------------------------
//函数名:		queryData
//参数  :	cityId-->属地ID（类似‘00’）
			endDataInt-->时间偏移到秒
//功能  :	导出excel查询
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function queryDataForExcel(cityId,endDataInt){
	var url = "<s:url value='/gwms/report/bindMonthCount!getReportData.action'/>";
	document.reportForm.action = url+"?reportType=excel&cityId="+cityId+"&endDataInt="+endDataInt;
	document.reportForm.method = "post";
	document.reportForm.submit();
}

/*-----------------------------------------------------
//函数名:		openUser
//参数  :	cityId-->属地ID（类似‘00’）
			endDataInt-->时间偏移到秒
//功能  :	针对查询的界面显示用户列表
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openUser(cityId,endDataInt,gwType){
	openNewWindow("../../Report/UserList4State.jsp?city_id="+cityId+"&endTime="+endDataInt+"&gw_type="+gwType);
}

/*-----------------------------------------------------
//函数名:		openDevice
//参数  :	cityId-->属地ID（类似‘00’）
			endDataInt-->时间偏移到秒
//功能  :	针对查询的界面显示设备列表
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openDevice(cityId,endDataInt,gwType){
	openNewWindow("../..//Report/DeviceList4State.jsp?city_id="+cityId+"&endTime="+endDataInt+"&binddate=binddate");
}

/*-----------------------------------------------------
//函数名:		openNewWindow
//参数  :	surl-->需要打开窗口的url
//功能  :	根据传入参数弹出窗口
//返回值:		
//说明  :	
//描述  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openNewWindow(surl){
	window.open(surl,"","left=20,top=20,height=450,width=700,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

/*-----------------------------------------------------
//函数名:		strDate2Second
//参数  :	dateStr-->时间字符串(2009-02-12)
//功能  :	日期字符串转为时间秒(1970-01-01 00:00:00)
//返回值:		long 秒
//说明  :	
//描述  :	Create 2009-9-17 of By qxq
------------------------------------------------------*/
function strDate2Second(dateStr){
	var temp = dateStr.split('-')
	var reqReplyDate = new Date();
	reqReplyDate.setYear(temp[0]);
    reqReplyDate.setMonth(temp[1]-1);
    reqReplyDate.setDate(temp[2]);
	return Math.floor(reqReplyDate.getTime()/1000);
}

</SCRIPT>
	
</head>
<body>
<form name="frm" action="" method="post">
<br>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
 		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">绑定率月报表</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"/></td>
					</tr>
				</table>
			</td>
		</tr>
 		<tr>
  			<td>
  	 			<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
			  	 	<tr>
			  	 		<TH colspan="4">绑定率月报表</TH>
			  	 	</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%" align="right">统计时间</td>
						<td colspan="3" align="left">
							<input type="text" size="10" name="endDataInt" value="<s:property value="endData"/>" readonly class=bk/>
							<img name="endimg" onclick="new WdatePicker(document.frm.endDataInt,'%Y-%M-%D',false,'whyGreen')" 
								src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="选择"/>(YYYY-MM-DD)
							&nbsp;&nbsp; <font color="red">统计时间为截止显示时间的前一天！</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column colspan="4" align="right">
							<input type="button" name="button" value=" 统  计 " onclick="doQuery()" class=jianbian/>
							<input type="hidden" value="<s:property value="cityId"/>" name="cityId"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height = "20">
			</td>
		</tr>
		<tr>
			<td align="center">
				<div id="resultData" ></div>
				<div id="progress"></div>
			</td>
		</tr>
	</TABLE>
</form>
<form name="reportForm"></form>
</body>
</html>
<%@ include file="../foot.jsp"%>
