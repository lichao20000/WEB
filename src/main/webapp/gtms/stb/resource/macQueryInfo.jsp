<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������MAC��ַ��ѯ</title>
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
// 				$("select[@name='deviceModel']").html("<option value='-1'>==����ѡ���豸����==</option>");
// 				break;
// 			}
			$.post(url, {
				vendorId:vendor
			}, function(ajax) {
				doParseMessage(ajax,$("select[@name='deviceModel']"),'deviceModel',vendorName);
			});
			break;
		default:
		$("select[@name='deviceModel']").html("<option value='-1'>==����ѡ���豸����==</option>");
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
			option = "<option value='-1' selected>==��ѡ���豸����==</option>";
		}else{
			option = "<option value='-1' selected>==��ѡ���Ʒ�ͺ�==</option>";
		}
		
		field.append(option);
		//alert(vendorName);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				if(vendorName==xText){
					option = "<option value='"+xValue+"' selected=selected>=="+xText+"==</option>";
				}else{
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
				}
			try{
				field.append(option);
			}catch(e){
				alert("�豸�ͺż���ʧ�ܣ�");
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
			alert("��ѡ����!");
			return;
		}
		if (deviceModel == "��ѡ���Ʒ�ͺ�"||deviceModel=="-1") {
			alert("��ѡ���Ʒ�ͺ�!");
			return;
		}
		if (!mac) {
			alert("�������豸MAC!");
			$("input[@name='mac']").focus();
			return;
		}
		
		<%-- MACУ����򣺳���12λ������+�ַ���д --%>
		var reg = /^[0-9A-Z]*$/;
		if (!reg.test(mac)){
			alert("�豸MACΪ���ֺ���ĸ���");
			$("input[@name='mac']").focus();
			return;
		}
		
		if (mac.length != 12){
			alert("��������豸MAC�������Ȳ�Ϊ12λ������������");
			$("input[@name='mac']").focus();
			return;
		}else{
			var macArr = mac.split("");
            for(var i = 0; i < macArr.length; i++){
                var charMac =  macArr[i];
				if(charMac < 0 || charMac > 'F' ){
					alert("�������mac��ַ����0-F�ڣ�����������");
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

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "first" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
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
		<td class="title_1" colspan="4">������MAC��ַ��ѯ</td>
	</tr>
	<tr align="left">
		<td class="title_2" width="15%">MAC</td>
		<td><input type="text" maxlength="20" name="cpe_mac"
			id="cpe_mac" onkeyup="checkMac(this)" />&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">�����ִ�Сд������MAC�ַ�����������ǰ��λ</font> </td>
		<td class="title_2" width="15%">����</td>
		<td><input type="text" maxlength="50" name="vendor_name"
			id="vendor_name" /></td>
	</tr>
	<tr align="left">
		<td class="title_2" width="15%">�ͺ�</td>
		<td><input type="text" maxlength="50" name="device_model"
			id="device_model" /></td>
		
		<td class="title_2" width="15%">������ʽ</td>
		<td><input type="text" maxlength="50" name="supply_mode"
			id="supply_mode" /></td>
	</tr>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<s:if test='%{isFlag=="1"}'>
			<button type="button" onclick="javaScript:addeditMac('','','','','','','','','1');">����</button>
		</s:if>
		&nbsp;&nbsp;
		<button type="button" onclick="javaScript:Query();" id="queryBtn">��ѯ</button>
		&nbsp;&nbsp;</div>
		</td>
	</tr>
</table>
</form>

 	<!-- ���ݼ�����ʾ -->
	<div style="width:98%;display:none;text-align: center;" id="tip_loading">
			<img src='<s:url value="/images/loading.gif" /> '/>���ڼ�������,�����ĵȴ�......
	</div>
		
<iframe id="first" name="first" height="0" frameborder="0" align="center"  scrolling="no" width="98%" src=""></iframe>

<table>
	<tr height="20">
	</tr>
</table>
<table class="querytable" width="98%" align="center"
	style="display: none" id="addedit">
	<tr>
		<td class="title_1" colspan="4">���/�༭�� 
		<input type="hidden" name="type" id="type"  value="1" />
		<input type="hidden" name="deviceId" id="deviceId"  value="" />
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">����ID</TD>
		<TD width="35%"><input type="text" name="orderId"
			id="orderId" class="bk" value="" size="40" maxlength="20">
		</TD>
		<TD class="title_2" align="center" width="15%">��װ���</TD>
		<TD width="35%"><input type="text" name="packageNo"
			id="packageNo" class="bk" value="" size="40" maxlength="20" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="35%">
		<select name="vendor" id="vendor" class="bk" onchange="getVendor_change_select('deviceModel','')">
		</select><font color="red">*</font>
		</TD>
		<TD class="title_2" align="center" width="15%">������ʽ</TD>
		<TD width="35%"><input type="text" name="supplyMode"
			id="supplyMode" class="bk" value="" size="40" maxlength="20" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">��Ʒ�ͺ�</TD>
		<TD width="35%">
		<select name="deviceModel" id="deviceModel" class="bk">
		</select>
	<font color="red">*</font>
		</TD>
		<TD class="title_2" align="center" width="15%">�豸MAC</TD>
		<TD width="35%"><input type="text" name="mac" onkeyup="transUpperCase()"
			id="mac" class="bk" value="" size="40" maxlength="20" />
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�豸���к�</TD>
		<TD width="85%" colspan="3">
		<input type="text" name="deviceSn"
			id="deviceSn" class="bk" value="" size="40" maxlength="40" />
		</TD>
	</TR>
	<tr>
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<button id="save" onclick="validate()">����</button>
		</div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="foot" align="left">
		<div class="left">
			�豸MAC��ַǰ׺˵���������ŷָ���
		</div>
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">��Ϊ</TD>
		<TD width="85%" colspan="3">
		           000763,000767
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%" colspan="3">
		          0026ED,D0154A,B075D5,4C09B4,C864C7,30F31D
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">��ά</TD>
		<TD width="85%" colspan="3">
		           5CC6D0,A089E4 
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%" colspan="3">
		           FCB0C4,94D723,A89DD2
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%" colspan="3">
		           AC4AFE
		</TD>
	</TR>
</table>
</body>
</html>