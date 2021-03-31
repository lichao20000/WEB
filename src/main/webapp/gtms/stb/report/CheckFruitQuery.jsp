<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>机顶盒检测结果查询</title>
<script type="text/javascript">
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

	function doQuery() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/gtms/stb/resource/CheckFruitQuery!Query.action'/>";
		form.submit();
	}
	function time()
	{
		var starttime = $.trim($("input[@name='starttime']").val());
		var endtime = $.trim($("input[@name='endtime']").val());
		var url = "<s:url value='/gtms/stb/resource/CheckFruitQuery!bijiao.action' />";
		$.post(url,{
			starttime:starttime,
			endtime:endtime
	    },function(ajax){
	    	if(ajax==1)
	    		{
	    		doQuery();
	    		}
	    	else
	    		{
	    		alert("时间跨度过长，开始时间与结束时间间隔1月内！");
	    		}
	    });	
	}

</script>
</head>
<body>
	<form name="frm" id="frm" method="post" target="dataForm">
	<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								机顶盒检测结果查询</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			<td>
			<table class="querytable" >
				<TR >
					<TD class=column width="15%" align='right'>业务帐号:</TD>
					<TD width="35%"><input type="text" maxlength="50" name="user_id"
						id="user_id" /></TD>
					<TD class=column width="15%" align='right'>MAC:</TD>
					<TD width="35%"><input type="text" maxlength="50" name="mac" id="mac" /></td>
				</TR>
				<TR>
							<TD class=column width="15%" align='right'>开始时间:</TD>
							<TD width="35%">
							<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> </TD>
							<TD class=column width="15%" align='right'>结束时间:</TD>
							<TD width="35%">
							<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> </TD>
				</TR>
				<TR>
				<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="time()">&nbsp;查 询&nbsp;</button>
							</td>
						</TR>
			</table>
			</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25" id="configInfoEm" style="display: none"></td>
			</tr>
			<tr>
				<td id="configInfo"></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
	</form>
</body>
</html>