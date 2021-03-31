<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function doQuery(){
	$("button[@id='btn']").attr("disabled",true);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("正在努力为您查询，请稍等....");
	var mainForm = document.getElementById("selectForm");
	mainForm.submit();
	
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

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});
</SCRIPT>



</head>

<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>设备查询</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post"
				action="<s:url value='/ids/httpCustomMadeQuery!queryTask.action'/>"
				target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>任务查询</th>
					</tr>


					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">开始时间</td>
						<td width="20%"><input type="text" name="starttime"
							class='bk' readonly value="<s:property value="starttime"/>">
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="选择" /></td>
						<td class=column align=center width="15%">结束时间</td>
						<td width="20%"><input type="text" name="endtime" class='bk'
							readonly value="<s:property value="endtime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="选择" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">名称</td>
						<td><input type="text" name="taskname" class='bk'></td>
						<td class=column align=center width="15%">定制人</td>
						<td><input type="text" name="acc_loginname" class='bk'></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;查&nbsp;询&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在努力为您查询，请稍等....</div>
		</td>
	</tr>
	<tr>
		<td height="80%"><iframe id="dataForm" name="dataForm"
				height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>
<%@ include file="/foot.jsp"%>
</html>