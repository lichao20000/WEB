<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>光猫无线关闭信息查询</title>
<link href="<s:url value='/css3/global.css'/>" rel="stylesheet" type="text/css">
<link href="<s:url value='/css3/c_table.css'/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/My97DatePicker/WdatePicker.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQeuryExtend-linkage.js'/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function queryList()
{
	var sn = trim($("input[name=sn]").val());
	if (sn!='' && sn!=null){
		if(sn.length<6){
			alert("请至少输入最后6位设备序列号进行查询！");
			document.frm.sn.focus();
			return false;
		}
	}
	
	var taskName=trim($("input[id=taskName]").val());
	$("input[name=task_name]").val(encodeURI(taskName));//翻页中文乱码，加密
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/config/operatSSID!queryList.action'/>";
	frm.submit();
}

function toExcel()
{
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/config/operatSSID!toExcel.action'/>";
	frm.submit();
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
function dyniframesize() 
{
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) 
	{
		if (document.getElementById) 
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) 
			{
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
<form name="frm" id="frm" target="dataForm" onsubmit="return false;" method="post">
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							光猫iTV无线关闭结果查询
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> 
							开始时间和结束时间分别为操作设备的日期<font color="red">*</font>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">光猫iTV无线关闭结果查询</th>
					</tr>
					<tr>
						<td align="right" class=column width="10%">开始时间</td>
						<td width="35%">
							<input type="text" name="startTime" readonly value="<s:property value='startTime'/>" />
							 <img name="shortDateimg" width="15" height="12" border="0"
								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" alt="选择" />
						</td>
						<td align="right" class="column" width="10%">结束时间</td>
						<td>
							<input type="text" name="endTime" readonly value="<s:property value='endTime'/>" /> 
							<img name="shortDateimg" width="15" height="12" border="0"
								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/dateButton.png'/>" alt="选择" />
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="10%">设备序列号</td>
						<td width="35%">
							<input type="text" name="sn" class="bk" value="">
						</td>
						<td align="right" class=column width="10%">任务名称</td>
						<td align="left">
							<input type="text" id="taskName" class="bk" value="">
							<input type="hidden" name="task_name" class="bk" value="">
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="10%">LOID</td>
						<td width="35%">
							<input type="text" name="loid" class="bk" value="">
						</td>
						<td align="right" class=column width="10%">宽带账号</td>
						<td align="left">
							<input type="text" name="user_name" class="bk" value="">
						</td>
					</tr>
					
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:queryList();" style="CURSOR:hand"
									>&nbsp;查&nbsp;询&nbsp;</button>&nbsp;&nbsp;
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" height="0" 
					frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>