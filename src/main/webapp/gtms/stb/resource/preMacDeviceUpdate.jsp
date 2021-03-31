<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>MAC前缀反推机顶盒信息编辑</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>

<%
request.setCharacterEncoding("GBK");
String preMacId = request.getParameter("preMacId");
String pre_mac = request.getParameter("pre_mac");
String vendor_name = request.getParameter("vendor_name");
String vendor_id = request.getParameter("vendor_id");
String device_model = request.getParameter("device_model");
String device_model_id = request.getParameter("device_model_id");
String hardwareversion = request.getParameter("hardwareversion");
String softwareversion = request.getParameter("softwareversion");
//String platform_id = request.getParameter("platform_id");
//String platform_name = request.getParameter("platform_name");
%>

<script type="text/javascript">
$(function(){
	var vendor_id=<%=vendor_id%>;
	initVendor(vendor_id);
});

function initVendor(vendor_id){
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getVendors.action'/>";
	$.post(url,{},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@name='vendorId']").empty();
					var optionValue = "<option value='-1' >请选择厂商</option>";
					$("select[@name='vendorId']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var optionValue;
						if(xValue==vendor_id){
							optionValue = "<option value='"+xValue+"' selected>"+xText+"</option>  ";
						}else{
							optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
						}
						
						$("select[@name='vendorId']").append(optionValue);
					}
				}else{
					$("select[@name='vendorId']").empty();
					var optionValue = "<option value='-1' >请选择厂商</option>";
					$("select[@name='vendorId']").append(optionValue);
				}
			}else{
				$("select[@name='vendorId']").empty();
				var optionValue = "<option value='-1' >查无厂商数据</option>";
				$("select[@name='vendorId']").append(optionValue);
			}
		});
}

//厂商改变触发
function vendorChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getDeviceModelS.action'/>";
	
	$.post(url,{
		vendorId:vendorId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@name='deviceModelId']").empty();
					var optionValue = "<option value='-1' >请选择型号</option>";
					$("select[@name='deviceModelId']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
						$("select[@name='deviceModelId']").append(optionValue);
					}
				}else{
					$("select[@name='deviceModelId']").empty();
					var optionValue = "<option value='-1' >请选择型号</option>";
					$("select[@name='deviceModelId']").append(optionValue);
				}
			}else{
				$("select[@name='deviceModelId']").empty();
				var optionValue = "<option value='-1' >请选择型号</option>";
				$("select[@name='deviceModelId']").append(optionValue);
			}
			
			$("select[@name='softwareversion']").empty();
			var optionValue1 = "<option value='-1' >请选择软件版本</option>";
			$("select[@name='softwareversion']").append(optionValue1);
			
			$("select[@name='hardwareversion']").empty();
			var optionValue2 = "<option value='-1' >请选择硬件版本</option>";
			$("select[@name='hardwareversion']").append(optionValue2);
		});
}

// 型号改变触发
function deviceModelChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getSoftwareversionS.action'/>";
	
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@name='softwareversion']").empty();
					var optionValue = "<option value='-1' >请选择软件版本</option>";
					$("select[@name='softwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@name='softwareversion']").append(optionValue);
					}
				}else{
					$("select[@name='softwareversion']").empty();
					var optionValue = "<option value='-1' >请选择软件版本</option>";
					$("select[@name='softwareversion']").append(optionValue);
				}
			}else{
				$("select[@name='softwareversion']").empty();
				var optionValue = "<option value='-1' >请选择软件版本</option>";
				$("select[@name='softwareversion']").append(optionValue);
			}
			
			$("select[@name='hardwareversion']").empty();
			var optionValue1 = "<option value='-1' >请选择硬件版本</option>";
			$("select[@name='hardwareversion']").append(optionValue1);
		});
}

//软件版本改变触发
function softwareversionChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var softwareversion = $("select[@name='softwareversion']").val();
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getHardwareversionS.action'/>";
	
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		softwareversion:softwareversion
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@name='hardwareversion']").empty();
					var optionValue = "<option value='-1' >请选择硬件版本</option>";
					$("select[@name='hardwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@name='hardwareversion']").append(optionValue);
					}
				}else{
					$("select[@name='hardwareversion']").empty();
					var optionValue = "<option value='-1' >请选择硬件版本</option>";
					$("select[@name='hardwareversion']").append(optionValue);
				}
			}else{
				$("select[@name='hardwareversion']").empty();
				var optionValue = "<option value='-1' >请选择硬件版本</option>";
				$("select[@name='hardwareversion']").append(optionValue);
			}
		});
}

function save()
{
	var preMacId=<%=preMacId%>;
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var softwareversion = $("select[@name='softwareversion']").val();
	var hardwareversion = $("select[@name='hardwareversion']").val();
	var preMac=Trim($("input[@name='preMac']").val());
	
	if("-1" == vendorId){
		alert("请选择厂商！");
		return false;
	}
	if("-1" == deviceModelId){
		alert("请选择型号！");
		return false;
	}
	if("-1" == softwareversion){
		alert("请选择软件版本！");
		return false;
	}
	if("-1" == hardwareversion){
		alert("请选择硬件版本！");
		return false;
	}
	if("" == preMac){
		alert("MAC前缀不能为空！");
		return false;
	}else if(!reg_verify(preMac)){
		alert("MAC格式不对！");
		return false;
	}
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!edit.action'/>";
	$.post(url, {
		preMacId:preMacId,
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		hardwareversion : hardwareversion,
		softwareversion : softwareversion,
		preMac : preMac
		}, function(ajax) {
			if("1" == ajax){
				alert("MAC前缀不存在，无法修改！");
			}else{
				alert("修改成功！");
				window.close();
			}
	});
}

function reg_verify(addr){
    var reg = /^[\dA-F]+$/;

    if(addr.match(reg)) {
        return true;
    } else {
         return false;
    }
}
</script>
</head>

<body>
<form id="form">
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<th colspan="4">MAC前缀反推机顶盒信息编辑</th>
				</tr>
				<TR>
					<TD class=column width="30%" align='right'>厂商</TD>
					<TD width="35%">
						<select name="vendorId" class="bk" onchange="vendorChange()">
							<option value=<%=vendor_id%> selected="selected"><%=vendor_name%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>型号</TD>
					<TD width="35%">
						<select name="deviceModelId" class="bk" onchange="deviceModelChange()">
							<option value=<%=device_model_id%> selected="selected"><%=device_model%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>软件版本</TD>
					<TD width="35%">
						<select name="softwareversion" class="bk" onchange="softwareversionChange()">
							<option value=<%=softwareversion%> selected="selected"><%=softwareversion%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>硬件版本</TD>
					<TD width="35%">
						<select name="hardwareversion" class="bk">
							<option value=<%=hardwareversion%> selected="selected"><%=hardwareversion%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>MAC前缀</TD>
					<TD width="70%">
						<input type="text" name="preMac" id="preMac" class="bk" value="<%=request.getParameter("pre_mac")%>" size="20">
						<font color="red">格式为：11BBCCDD55，英文字母大写且不带:</font>
						<input type="hidden" name="preMacId" id="preMacId" class="bk" value="<%=request.getParameter("preMacId")%>">
					</TD>
				</TR>
				<TR>
					<td colspan="2" align="center" class=foot>	
						<button onclick="save()">&nbsp;保存&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="javascript:window.close();">&nbsp;取消&nbsp;</button>
					</td>
				</TR>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
