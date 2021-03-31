<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />

<title>智能网关API权限管理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	function getApiPluginList() {
		var classifyName = $.trim($("input[@name='classifyName']").val());
		var creator = $.trim($("input[@name='creator']").val());
		//document.getElementById("queryButton").disabled = true;
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/resource/apiPlugin!getApiPluginList.action'/>";
		frm.submit();
	}
	function showAdd(){
		var strpage = "itmsAPIPluginAdd.jsp";
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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
</script>
</head>
<body>
	<form name="frm" id="frm" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">插件API权限分类</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类名称：</td>
				<td width="35%" align="left"><input name="classifyName"
					type="text" class="bk" /></td>
				<td width="15%" align="right" class=column>创建者：</td>
				<td width="35%" align="left"><input name="creator" type="text"
					class="bk" /></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column align="right" width="15%">开始时间：</td>
				<td width="35%"><lk:date id="starttime" name="starttime"
						defaultDate="%{starttime}" /></td>
				<td class=column align="right" width="15%">结束时间：</td>
				<td width="35%"><lk:date id="endtime" name="endtime"
						defaultDate="%{endtime}" /></td>
			</tr>
			<tr bgcolor="#ffffff">
				<td class=column align="right">分类状态</td>
				<td colspan="3"><select name='status' class="select"
					style="width: 85px;">
						<option value="1" selected>生效</option>
						<option value="2">失效</option>
				</select></td>
			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="新增"
					onclick="showAdd()" /><input type="button" value="查询"
					onclick="getApiPluginList()" /></td>
			</tr>

		</table>
	</form>
	<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>