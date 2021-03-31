<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>开机广告下发结果查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">


	/*------------------------------------------------------------------------------
	 //函数名:		deviceSelect_change_select
	 //参数  :	type 
	 vendor      加载设备厂商
	 deviceModel 加载设备型号
	 devicetype  加载设备版本
	 //功能  :	加载页面项（厂商、型号级联）
	 //返回值:		
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function gwShare_change_select(type, selectvalue) {
		switch (type) {
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/showAdver!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			if ("-1" == vendorId) {
				$("select[@name='device_model']").html(
						"<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='device_version']").html(
						"<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url, {
				vendorId : vendorId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='device_model']"),
						selectvalue);
				$("select[@name='device_version']").html(
						"<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/gtms/stb/resource/showAdver!getSoftVersion.action'/>";
			var deviceModelId = $("select[@name='device_model']").val();
			if ("-1" == deviceModelId) {
				$("select[@name='gwShare_devicetypeId']").html(
						"<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url, {
				device_model : deviceModelId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='device_version']"),
						selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
		}
	}

	//解析查询设备型号返回值的方法
	function gwShare_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for (var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("设备型号检索失败！");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}

	/*------------------------------------------------------------------------------
	 //函数名:		checkip
	 //参数  :	str 待检查的字符串
	 //功能  :	根据传入的参数判断是否为合法的IP地址
	 //返回值:		true false
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function checkip(str) {
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		return pattern.test(str);
	}

	/*------------------------------------------------------------------------------
	 //函数名:		trim
	 //参数  :	str 待检查的字符串
	 //功能  :	根据传入的参数进行去掉左右的空格
	 //返回值:		trim（str）
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	/*------------------------------------------------------------------------------
	 //函数名:		queryAdverList
	 //功能  :	根据传入的参数查询广告下发结果
	 //返回值:		调整界面
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryAdverList() {
		var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']")
				.val());
		var loopbackIp = trim($("input[@name='loopbackIp']").val());
		var servAccount = trim($("input[@name='servAccount']").val());
		if ("" != servAccount) {
			$("input[@name='servAccount']").val(servAccount);
		}
		if ("" != deviceSerialnumber && deviceSerialnumber.length < 6) {
			alert("设备序列号请至少输入最后6位！");
			return false;
		} else {
			$("input[@name='deviceSerialnumber']").val(deviceSerialnumber);
		}
		if ("" != loopbackIp && !checkip(loopbackIp)) {
			alert("请输入正确的IP地址！");
			return false;
		} else {
			$("input[@name='loopbackIp']").val(loopbackIp);
		}
		document.selectForm.queryButton.disabled = true;
		document.selectForm.submit();
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
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
<body>
	<form name="selectForm" method="post"
		action="<s:url value='/gtms/stb/resource/showAdver!adverResultList.action'/>" target="dataForm">
		<table width="98%" align="center" class="querytable">
			<tr>
				<td><img height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> 您当前的位置：开机广告下发结果查询</td>
			</tr>
		</table>
		<table width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">开机广告下发结果查询</th>
			</tr>
			<tr id="tr21" STYLE="display:">
				<td width="10%" class="title_2">厂商</TD>
				<td width="40%"><s:select list="vendorList" name="vendorId"
						headerKey="-1" headerValue="==请选择==" listKey="vendor_id"
						listValue="vendor_add" value="vendorId" cssClass="bk"
						onchange="gwShare_change_select('deviceModel','-1');"
						theme="simple">
					</s:select></td>

				<td width="10%" class="title_2">型号</td>
				<td width="40%"><select name="device_model" class="bk"
					onchange="gwShare_change_select('devicetype','-1')">
						<option value="-1">请先选择厂商</option>
				</select></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2" align="center">版本</td>
				<td width="40%"><select name="device_version" class="bk">
						<option value="-1">请先选择设备型号</option>
				</select></td>

				<td width="10%" class="title_2">设备序列号</td>
				<td width="40%"><input type="text" name="deviceSerialnumber" value=""
					size="20" maxlength="40" class="bk" /></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2">最近一次上报IP</td>
				<td width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></td>
				<td width="10%" class="title_2">设备MAC地址</td>
				<td width="40%"><input type="text" name="cpeMac" value=""
					size="20" maxlength="20" class="bk" /></td>
			</tr>

			<tr id="tr22" STYLE="display:">
				<td width="10%" class="title_2">业务账号</td>
				<td width="40%"><input type="text" name="servAccount" value=""
					size="20" maxlength="40" class="bk" /></td>
				<td width="10%" class="title_2">下发状态</td>
				<td width="40%"><select name="faultType"
					id="addressingType" class="bk">
						<option value="-1">===请选择===</option>
						<option value="success">成功,需要重启</option>
						<option value="fail">失败</option>
						<option value="notIssued">未下发</option>
				</select></td>
			</tr>
			<tr id="tr23" STYLE="display:">
				<td width="10%" class="title_2">策略名称</td>
				<td width="40%"><input type="text" name="taskName" value=""
					size="23" maxlength="23" class="bk" /></td>
			</tr>
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button name="queryButton" align="right"
							onclick="return queryAdverList()">查 询</button>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<br>
<br>