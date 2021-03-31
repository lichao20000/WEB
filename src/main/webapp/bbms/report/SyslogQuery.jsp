<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<%@ include file="../../head.jsp"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="../../Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="../../Js/CheckFormForm.js"/>"></SCRIPT>
<script language="JavaScript">
$(function(){
	gwShare_setGaoji();
});


function doQuery(){  
	//alert("11");
	var device_id = $.trim($("input[@name='device_id']").val());
	//var tdDeviceSn = $.trim($("input[@name='tdDeviceSn']").val());
	//var stat_time = $.trim($("input[@name='stat_time']").val());
	//var logType = $.trim($("select[@name='logType']").val());
	//alert(device_id);
	if(device_id!=""){
		if(device_id < 6){
           	alert("请先查询设备!");
           	$("input[@name='device_id']").focus();
            return false;        
	    }
	}
		
    //if(stat_time==""){
    //	alert("请输入查询时间");
    //	//alert("请输入查询开始时间");
	//	return false;
    //}
    
    //if(end_time==""){
    //	alert("请输入查询结束时间");
	//	return false;
    //}
    
    //alert(tdDeviceSn);
    
	//alert($("input[@id='tdDeviceSn']").val());
	//alert("22");
	frm.submit();
	//alert("33");
	return true;
	//alert("44");
	
	
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    
    var url = '<s:url value='/bbms/report/SyslogQuery.action'/>'; 
	$.post(url,{
		tdDeviceId:device_id,
		tdDeviceSn:tdDeviceSn,
		stat_time:stat_time,
		logType:logType
	},function(ajax){	
	    $("div[@id='dataForm']").html("");
		$("div[@id='dataForm']").append(ajax);
	});
	
    //return true;
    
   
	
    
}

function deviceResult(returnVal){
	
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSnShow']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSnShow']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("input[@id='tdDeviceSn']").val(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
		$("input[@id='tdDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
	}
	
	$("tr[@id='trUserData']").show();
	var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
	$.post(url,{
		device_id:returnVal[2][0][0]
	},function(ajax){	
	    $("div[@id='UserData']").html("");
		$("div[@id='UserData']").append(ajax);
	});
	
}


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//如果用户的浏览器是NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//如果用户的浏览器是IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 
</script>

<%@ include file="../../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">Syslog日志查询</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
						<div id="selectDevice">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						</div>
						</td>
					</TR>
					<tr>
						<td bgcolor="#FFFFFF" colspan="4">
						<FORM NAME="frm" METHOD="POST" ACTION="<s:url value="/bbms/report/SyslogQuery.action"/>" target="dataForm">
							<input id="tdDeviceId" name="tdDeviceId" type="hidden" value="">
							<input id="tdDeviceSn" name="tdDeviceSn" type="hidden" value="">
						<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
							<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
											设备属地
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column width="15%">
											设备序列号
											<input type="hidden" name="textDeviceId" value="">
									</td>
									
									<td id="tdDeviceSnShow">
									</td>
							</TR>
							<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
							</tr>
							<TR bgcolor="#FFFFFF">
								<td class=column width="15%" align="right">查询日期&nbsp;</td>
								<td width="35%"><input type="text" name="stat_time"
									class='bk' readonly value="<s:property value='stat_time'/>">
								<img name="shortDateimg"
									onclick="WdatePicker({el:document.frm.stat_time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="<s:url value='../../images/dateButton.png'/>" width="15"
									height="12" border="0" alt="选择"></td>
								<td class=column width="15%" align="right">日志类型&nbsp;</td>
								<td width="35%">
								<select name='logType' class='bk'>
									<s:iterator value="logTypes" status="status">
										<option value="<s:property value='logTypes[#status.index].type_id'/>"><s:property value='logTypes[#status.index].type_name'/></option>
									</s:iterator>
								</select>
								</td>
							</TR>
							<tr align="right" CLASS="green_foot">
								<td colspan="4"><INPUT TYPE="button" value=" 查 询 日 志 "
									class=jianbian onclick="doQuery()"> &nbsp;&nbsp; <INPUT
									TYPE="hidden" name="action" value="add"> &nbsp;&nbsp;</td>
							</tr>
						</table>
						</FORM>
						</td>
					</tr>
					<tr id="trData">
						<td colspan="4" valign="top" class=column>
							<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</TD>
	</TR>
	<tr>
			<td bgcolor=#ffffff>
				&nbsp;
			</td>
	</tr>
</TABLE>
<script language="JavaScript">
//初始化时间  开启 by zhangcong 2011-06-02
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	document.getElementById("stat_time").value = year+"-" + month + "-" + day;
</script>
<%@ include file="/foot.jsp"%>
