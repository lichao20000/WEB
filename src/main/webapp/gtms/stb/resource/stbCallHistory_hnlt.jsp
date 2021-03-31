<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒上报记录查询</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function query()
{
	var mac = trim($("input[name=mac]").val());
	var ip = trim($("input[name=login_ip]").val());
	if ((""!=mac && !validateMac(mac)) 
			|| (""!=ip && !validateIp(ip))){
		return;
	}
	
	var stime=$("input[name=startTime]").val();
	var etime=$("input[name=endTime]").val();
	if(stime=="" || etime==""){
		alert("请选择查询时间段！");
		return;
	}
	
	var s=stime.substring(0,7);
	var e=etime.substring(0,7);
	if(s!=e){
		alert("禁止跨月查询，请重新选择时间段！");
		return;
	}
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbCallHistoryACT!query.action'/>";
	frm.submit();
	$("tr[@id='resultData']").show();
}

function validateMac(mac) 
{
    mac = mac.toUpperCase();
    var expre = /[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}/;  
    var regexp = new RegExp(expre); 
    if (!regexp.test(mac) || mac.length != 12) {  
        alert("MAC地址只能A-F字母或者数字组成,样式：AA11BB22CC33！");  
        return false;  
    }
    return true;
}

function validateIp(ip) 
{
    var expre = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;    
    var regexp = new RegExp(expre);  
    if (!regexp.test(ip)) {  
        alert("输入的ip不符合规范！");  
        return false;  
    }
    return true;
}

function trim(str){
    return str.replace(/(^\s*)|(\s*$)/g,"");
}



//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
 var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(window).resize(function() {
	dyniframesize();
});

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="frm" id="frm" target="dataForm">
<input type="hidden" name="table" readonly value="<s:property value='table'/>" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						机顶盒设备上报记录查询
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" class="querytable" align="center">
				<tr>
					<th colspan="4">机顶盒设备上报记录查询</th>
				</tr>
				<tr>
					<td align="right" class=column width="15%">请求业务账号</td>
					<td width="35%">
						<input type="text" id="request_username" name="request_username" class="bk" value="">
					</td>
					<td align="right" class=column width="15%">返回业务账号</td>
					<td align="left" width="35%">
						<input type="text" id="result_username" name="result_username" class="bk" value="">
					</td>
				</tr>
				<tr>
					<td align="right" class=column width="15%">机顶盒MAC</td>
					<td width="35%">
						<input type="text" id="mac" name="mac" class="bk" value="">
					</td>
					<td align="right" class=column width="15%">请求IP地址</td>
					<td align="left" width="35%">
						<input type="text" id="login_ip" name="login_ip" class="bk" value="">
					</td>
				</tr>
				<tr>
					<td align="right" class=column width="15%">请求开始时间</td>
					<td width="35%">
						<input type="text" name="startTime" readonly value="<s:property value='startTime'/>" />
						 <img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
					</td>
					<td align="right" class="column" width="15%">请求结束时间</td>
					<td width="35%">
						<input type="text" name="endTime" readonly value="<s:property value='endTime'/>" /> 
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="right" class="foot" width="100%">
						<div align="right">
							<button onclick="javascript:query();" name="gwShare_queryButton" style="CURSOR:hand"> 
								查 询 
							</button>&nbsp;&nbsp;
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="25" id="resultStr"></td>
	</tr>
	<tr id="resultData" style="display: none">
		<td>
			<iframe id="dataForm" name="dataForm" height="530" frameborder="0" scrolling="yes" width="100%" src=""></iframe>
		</td>
	</tr>
</table>
</form>
</body>