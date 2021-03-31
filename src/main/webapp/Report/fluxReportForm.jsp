<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>流量性能统计</title>
<%
	/**
		 * 流量性能统计
		 *
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-10
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">

function showChild(target){
	if (target == 'device_model'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDeviceModelInfo.action"/>",
			data: "vendor_id="+$("select[@name='vendor_id']").val(),
			success:
				function(data)
				{
					$("#modelSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#modelSelect").html("<select name='device_model' class='bk' onchange=showChild('device')><option value='-1'>==没有可选择的设备型号==</option></select>");
				}
		});
	}
	else if (target == 'device'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDeviceInfo.action"/>",
			data: "vendor_id="+$("select[@name='vendor_id']").val()+"&device_model_id="+$("select[@name='device_model_id']").val(),
			success:
				function(data)
				{
					$("#deviceSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#deviceSelect").html("<select name='device_id' class='bk' onchange=showChild('port')><option value='-1'>==没有可选择的设备==</option></select>");
				}
		});
	}
	else if (target == 'port'){
		$.ajax({
			type: "POST",
			url: "<s:url value="/flux/FluxReport!getDevicePortInfo.action"/>",
			data: "device_id="+$("select[@name='device_id']").val(),
			success:
				function(data)
				{
					$("#portSelect").html(data);
				},
			erro:
				function(xmlR,msg,other){
					$("#portSelect").html("没有可以选择的端口(该设备没有进行流量配置，或没有配置成功)");
				}
		});
	}
}

//全选端口
function selectPort(){
	//存在端口
	if (document.frm.devicePort != null){
		//多个端口
		if (typeof(document.frm.devicePort.value) == 'undefined'){
			for (var i=0;i<document.frm.devicePort.length;i++){
				document.frm.devicePort[i].checked = document.frm.selectPorts.checked;
			}
		}
		//一个端口
		else{
			document.frm.devicePort.checked = document.frm.selectPorts.checked;
		}
	}
}
//全选类型
function selectKind(){
	for (var i=0;i<document.frm.kind.length;i++){
		document.frm.kind[i].checked = document.frm.selectKinds.checked;
	}
}

function selectPortInfo(){
	if (document.frm.devicePort != null){
		//多个端口
		if (typeof(document.frm.devicePort.value) == 'undefined'){
			for (var i=0;i<document.frm.devicePort.length;i++){
				if (document.frm.devicePort[i].checked){
					//显示在列表中
					var optValue = document.frm.devicePort[i].value;
					var optText = document.frm.device_id.options[document.frm.device_id.options.selectedIndex].text
									+ document.frm.devicePort[i].view;
					addInfoOPtion(optValue, optText);
				}
			}
		}
		//一个端口
		else{
			if (document.frm.devicePort.checked){
				//显示在列表中
				var optValue = document.frm.devicePort[i].value;
					var optText = document.frm.device_id.options[document.frm.device_id.options.selectedIndex].text
									+ document.frm.devicePort[i].view;
				addInfoOPtion(optValue, optText);
			}
			else{
				alert('请选择端口！');
			}
		}
	}
	else{
		alert('没有可以选择的端口！(该设备没有进行流量配置，或没有配置成功)');
	}
}

//向已选择列表中增加
function addInfoOPtion(optValue, optText){
	var oOption = document.createElement("OPTION");
	oOption.text = optText;
	oOption.value = optValue;
	for (var i=0;i<document.frm.selectInfo.length;i++){
		if (document.frm.selectInfo[i].value == optValue){
			return false;
		}
	}
	document.frm.selectInfo.options.add(oOption);
}

//删除已选择的设备端口
function dselectPortInfo(){
	var obj = document.frm.selectInfo;

	if (obj.selectedIndex != -1){
		obj.remove(obj.selectedIndex);
		obj.focus();
	}
	else{
		alert('请选择需要删除的端口！');
	}
}

//提交数据查询
function getData(){
	//统计时间
	if (document.frm.day.value == ""){
		alert('请选择统计日期！');
		document.frm.day.focus();
		return false;
	}
	//选择的统计类型
	var flag = true;
	for (var j=0;j<document.frm.kind.length;j++){
		if (document.frm.kind[j].checked){
			flag = false;
		}
	}
	if (flag){
		alert('请选择需要统计的性能！');
		return false;
	}
	//已选择的设备端口
	var str = "";
	var strInfo = "";
	for (var i=0;i<document.frm.selectInfo.length;i++){
		str += document.frm.selectInfo[i].value + ";";
		strInfo += document.frm.selectInfo[i].text + ";";
	}
	if (str == ""){
		alert('请选择需要统计的设备端口！');
		document.frm.selectInfo.focus();
		return false;
	}
	document.frm.devicePortList.value = str;
	document.frm.devicePortListInfo.value = strInfo;

	document.frm.submit();
}
</script>
</head>

<body>
<form action="<s:url value="/flux/FluxReport!getFluxReportInfo.action"/>" name="frm" method="post">
<input type="hidden" name="devicePortList" value="">
<input type="hidden" name="devicePortListInfo" value="">
<table border=0 cellspacing=0 cellpadding=0 width="95%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						流量性能统计
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">多个设备流量性能统计日报表
					</td>
					<td align="left">
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#000000">
				<tr><th colspan="2">流量性能统计日报表</th></tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" width="15%" nowrap>请选择统计日期：</td>
					<td width="85%"  nowrap>
						<INPUT TYPE="text" NAME="day" class=bk readonly=true value="<s:property value="day"/>">
		  				<input TYPE="button" value="▼" class="btn" onclick="new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>请选择需要统计的性能<br><input type="checkbox" name="selectKinds" onclick="selectKind()">全选</td>
					<td>
						<input type="checkbox" name="kind" value="ifinoctetsbps">流入速率/bps
						<input type="checkbox" name="kind" value="ifinerrorspps">流入错包率
						<input type="checkbox" name="kind" value="Ifindiscardspps">流入丢包率
						<input type="checkbox" name="kind" value="Ifinucastpktspps">流入广播包速率
						<br>
						<input type="checkbox" name="kind" value="ifoutoctetsbps">流出速率/bps
						<input type="checkbox" name="kind" value="ifouterrorspps">流出错包率
						<input type="checkbox" name="kind" value="Ifoutdiscardspps">流出丢包率
						<input type="checkbox" name="kind" value="Ifoutucastpktspps">流出广播包速率
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>请选择设备厂商：</td>
					<td>
						<s:property value="vendorList" escapeHtml="false"/>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>请选择设备型号：</td>
					<td>
						<div id="modelSelect">
							<select name="device_model" class="bk" onchange="showChild('device')">
								<option value="-1">==请先选择厂商==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>请选择设备：</td>
					<td>
						<div id="deviceSelect">
							<select name="device_id" class="bk" onchange="showChild('port')">
								<option value="-1">==请先选择设备型号==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>请选择设备端口：<br><input type="checkbox" name="selectPorts" onclick="selectPort()">全选</td>
					<td>
						<div id="portSelect">
							请先选择设备
						</div>
						<input type="button" value="点击选择设备端口" onclick="selectPortInfo()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class="column" nowrap>已选择的设备和端口</td>
					<td>
						<select name="selectInfo" size="10">
						</select>
						<input type="button" value="删除选择的端口" onclick="dselectPortInfo()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2"><input type="button" value=" 统 计 " onclick="getData()"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
