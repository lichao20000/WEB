<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒MAC地址查询</title>
<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
$.ajaxSetup({ async : false });
	function getVendor_change_select(type,vendorName){
		switch(type){
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/macQueryInfo!getDeviceModelList.action'/>";
			var vendor = $("select[@name='vendor']").val();
// 			if(-1==vendor){
// 				$("select[@name='deviceModel']").html("<option value='-1'>==请先选择设备厂商==</option>");
// 				break;
// 			}
			$.post(url, {
				vendorId:vendor
			}, function(ajax) {
				doParseMessage(ajax,$("select[@name='deviceModel']"),'deviceModel',vendorName);
			});
			break;
		default:
		$("select[@name='deviceModel']").html("<option value='-1'>==请先选择设备厂商==</option>");
		var url = "<s:url value='/gtms/stb/resource/macQueryInfo!getVendorList.action'/>";
		$.post(url, {
			
		},
		function(ajax) {
			doParseMessage(ajax,$("select[@name='vendor']"),'vendor',vendorName);
		//	$.ajaxSetup({ async : true });
		});
		break;
		}
		
	}
	
	function doParseMessage(ajax,field,type,vendorName){
		if(""==ajax){
			return;
		}
		var lineData=ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		if(type=="vendor"){
			option = "<option value='-1' selected>==请选择设备厂商==</option>";
		}else{
			option = "<option value='-1' selected>==请选择产品型号==</option>";
		}
		
		field.append(option);
		//alert(vendorName);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
				//根据每组value和text标记的值创建一个option对象
				if(vendorName==xText){
					option = "<option value='"+xValue+"' selected=selected>=="+xText+"==</option>";
				}else{
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
				}
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
	}
	

	function checkMac(macObj){
		macObj.value = macObj.value.toUpperCase();
		var val = macObj.value;
		if (val && val.length < 4){
			document.getElementById("queryBtn").disabled = true;
		}
		else{
			document.getElementById("queryBtn").disabled = false;
		}
	}
	
	function Query(){
		$("#first").hide();
		<%--$("#tip_loading").show();--%>
		document.selectForm.action = "<s:url value='/gtms/stb/resource/macQueryInfo!query.action'/>";
		document.selectForm.submit();
	}
	
	function addeditMac(orderId,packageNo,vendorName,supplyMode,deviceModel,mac,deviceSn,deviceId,type){
		$("table[@id='addedit']").show();
		getVendor_change_select('',vendorName);
		if(type=="1"){
			$("input[@name='type']").val("1");
			$("input[@name='orderId']").val("");
			$("input[@name='packageNo']").val("");
			$("input[@name='vendorName']").val("");
			$("input[@name='supplyMode']").val("");
			$("input[@name='deviceModel']").val("");
			document.getElementById("mac").readOnly=false;
			$("input[@name='mac']").val("");
			$("input[@name='deviceSn']").val("");
		}else{
			getVendor_change_select('deviceModel',deviceModel);
			$("input[@name='type']").val(type);
			$("input[@name='orderId']").val(orderId);
			$("input[@name='packageNo']").val(packageNo);
			$("select[@name='vendorName']").attr('1','selected');
			$("input[@name='supplyMode']").val(supplyMode);
			//$("select[@name='deviceModel']").val(deviceModel);
			$("input[@name='mac']").val(mac);
			document.getElementById("mac").readOnly=true;
			$("input[@name='deviceSn']").val(deviceSn);
			$("input[@name='deviceId']").val(deviceId);
			var filed = $("#vendor]");
		}
	}
	
	function validate(){
		var type = $("input[@name='type']").val();
		var orderId = $("input[@name='orderId']").val();
		var packageNo = $("input[@name='packageNo']").val();
		var vendorId = $("select[@name='vendor']").val();
		var vendorName=$("#vendor  option:selected").text();
		vendorName=vendorName.replace(new RegExp(/(=)/g),"");
		var supplyMode = $("input[@name='supplyMode']").val();
		var deviceModel = $("#deviceModel  option:selected").text();
		deviceModel=deviceModel.replace(new RegExp(/(=)/g),"");
		var deviceId = $("input[@name='deviceId']").val(deviceId);
		var mac = $("input[@name='mac']").val();
		var deviceSn = $("input[@name='deviceSn']").val();
		if (vendorId == "-1") {
			alert("请选择厂商!");
			return;
		}
		if (deviceModel == "请选择产品型号"||deviceModel=="-1") {
			alert("请选择产品型号!");
			return;
		}
		if (!mac) {
			alert("请输入设备MAC!");
			$("input[@name='mac']").focus();
			return;
		}
		
		<%-- MAC校验规则：长度12位、数字+字符大写 --%>
		var reg = /^[0-9A-Z]*$/;
		if (!reg.test(mac)){
			alert("设备MAC为数字和字母组合");
			$("input[@name='mac']").focus();
			return;
		}
		
		if (mac.length != 12){
			alert("您输入的设备MAC正常长度不为12位，请重新输入");
			$("input[@name='mac']").focus();
			return;
		}else{
			var macArr = mac.split("");
            for(var i = 0; i < macArr.length; i++){
                var charMac =  macArr[i];
				if(charMac < 0 || charMac > 'F' ){
					alert("您输入的mac地址不在0-F内，请重新输入");
					$("input[@name='mac']").focus();
					return;
				}
			}
		}
		$("#save").attr('disabled', false);
		
		var url = "<s:url value='/gtms/stb/resource/macQueryInfo!validateMAC.action'/>";
		$.post(url, {
			vendorId : vendorId,
			cpe_mac : mac
		}, function(ajax) {
			if(ajax!="true"){
				alert(ajax);
				return;
			}else{
				ExecMod(orderId,packageNo,vendorName,supplyMode,deviceModel,mac,deviceSn,deviceId,type);
			}
		});
		
	}
	
	function ExecMod(orderId,packageNo,vendorName,supplyMode,deviceModel,mac,deviceSn,deviceId,type) {
		
		url = "<s:url value='/gtms/stb/resource/macQueryInfo!addedit.action'/>";
		$.post(url, {
			orderId : orderId,
			packageNo : packageNo,
			vendorName : vendorName,
			supplyMode : supplyMode,
			deviceModel : deviceModel,
			mac : mac,
			deviceSn : deviceSn,
			deviceId : deviceId,
			type : type
		}, function(ajax) {
			alert(ajax);
			Query();
			$("#save").attr('disabled', false);
		});
	}
	
	function transUpperCase(){
		var mac = $("input[@name='mac']").val();
		if (mac){
			$("input[@name='mac']").val(mac.toUpperCase());
		}
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "first" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
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
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
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
	
</script>
</head>
<body>
<form id="selectForm" name="selectForm" action="<s:url value='/gtms/stb/resource/macQueryInfo!query.action'/>" target="first"
	method="post">
<table class="querytable" align="center"  width="98%"
	 id="tabs">
	<tr>
		<td class="title_1" colspan="4">机顶盒MAC地址查询</td>
	</tr>
	<tr align="left">
		<td class="title_2" width="15%">MAC</td>
		<td><input type="text" maxlength="20" name="cpe_mac"
			id="cpe_mac" onkeyup="checkMac(this)" />&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">不区分大小写连续的MAC字符，至少输入前四位</font> </td>
		<td class="title_2" width="15%">厂商</td>
		<td><input type="text" maxlength="50" name="vendor_name"
			id="vendor_name" /></td>
	</tr>
	<tr align="left">
		<td class="title_2" width="15%">型号</td>
		<td><input type="text" maxlength="50" name="device_model"
			id="device_model" /></td>
		
		<td class="title_2" width="15%">供货方式</td>
		<td><input type="text" maxlength="50" name="supply_mode"
			id="supply_mode" /></td>
	</tr>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<s:if test='%{isFlag=="1"}'>
			<button type="button" onclick="javaScript:addeditMac('','','','','','','','','1');">新增</button>
		</s:if>
		&nbsp;&nbsp;
		<button type="button" onclick="javaScript:Query();" id="queryBtn">查询</button>
		&nbsp;&nbsp;</div>
		</td>
	</tr>
</table>
</form>

 	<!-- 数据加载提示 -->
	<div style="width:98%;display:none;text-align: center;" id="tip_loading">
			<img src='<s:url value="/images/loading.gif" /> '/>正在加载数据,请耐心等待......
	</div>
		
<iframe id="first" name="first" height="0" frameborder="0" align="center"  scrolling="no" width="98%" src=""></iframe>

<table>
	<tr height="20">
	</tr>
</table>
<table class="querytable" width="98%" align="center"
	style="display: none" id="addedit">
	<tr>
		<td class="title_1" colspan="4">添加/编辑区 
		<input type="hidden" name="type" id="type"  value="1" />
		<input type="hidden" name="deviceId" id="deviceId"  value="" />
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">订单ID</TD>
		<TD width="35%"><input type="text" name="orderId"
			id="orderId" class="bk" value="" size="40" maxlength="20">
		</TD>
		<TD class="title_2" align="center" width="15%">包装箱号</TD>
		<TD width="35%"><input type="text" name="packageNo"
			id="packageNo" class="bk" value="" size="40" maxlength="20" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="35%">
		<select name="vendor" id="vendor" class="bk" onchange="getVendor_change_select('deviceModel','')">
		</select><font color="red">*</font>
		</TD>
		<TD class="title_2" align="center" width="15%">供货方式</TD>
		<TD width="35%"><input type="text" name="supplyMode"
			id="supplyMode" class="bk" value="" size="40" maxlength="20" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">产品型号</TD>
		<TD width="35%">
		<select name="deviceModel" id="deviceModel" class="bk">
		</select>
	<font color="red">*</font>
		</TD>
		<TD class="title_2" align="center" width="15%">设备MAC</TD>
		<TD width="35%"><input type="text" name="mac" onkeyup="transUpperCase()"
			id="mac" class="bk" value="" size="40" maxlength="20" />
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">设备序列号</TD>
		<TD width="85%" colspan="3">
		<input type="text" name="deviceSn"
			id="deviceSn" class="bk" value="" size="40" maxlength="40" />
		</TD>
	</TR>
	<tr>
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<button id="save" onclick="validate()">保存</button>
		</div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="foot" align="left">
		<div class="left">
			设备MAC地址前缀说明：（逗号分隔）
		</div>
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">华为</TD>
		<TD width="85%" colspan="3">
		           000763,000767
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">中兴</TD>
		<TD width="85%" colspan="3">
		          0026ED,D0154A,B075D5,4C09B4,C864C7,30F31D
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">创维</TD>
		<TD width="85%" colspan="3">
		           5CC6D0,A089E4 
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">长虹</TD>
		<TD width="85%" colspan="3">
		           FCB0C4,94D723,A89DD2
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">海信</TD>
		<TD width="85%" colspan="3">
		           AC4AFE
		</TD>
	</TR>
</table>
</body>
</html>