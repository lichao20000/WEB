<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>MACǰ׺���ƻ�������Ϣ�༭</title>
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
					var optionValue = "<option value='-1' >��ѡ����</option>";
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
					var optionValue = "<option value='-1' >��ѡ����</option>";
					$("select[@name='vendorId']").append(optionValue);
				}
			}else{
				$("select[@name='vendorId']").empty();
				var optionValue = "<option value='-1' >���޳�������</option>";
				$("select[@name='vendorId']").append(optionValue);
			}
		});
}

//���̸ı䴥��
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
					var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
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
					var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
					$("select[@name='deviceModelId']").append(optionValue);
				}
			}else{
				$("select[@name='deviceModelId']").empty();
				var optionValue = "<option value='-1' >��ѡ���ͺ�</option>";
				$("select[@name='deviceModelId']").append(optionValue);
			}
			
			$("select[@name='softwareversion']").empty();
			var optionValue1 = "<option value='-1' >��ѡ������汾</option>";
			$("select[@name='softwareversion']").append(optionValue1);
			
			$("select[@name='hardwareversion']").empty();
			var optionValue2 = "<option value='-1' >��ѡ��Ӳ���汾</option>";
			$("select[@name='hardwareversion']").append(optionValue2);
		});
}

// �ͺŸı䴥��
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
					var optionValue = "<option value='-1' >��ѡ������汾</option>";
					$("select[@name='softwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@name='softwareversion']").append(optionValue);
					}
				}else{
					$("select[@name='softwareversion']").empty();
					var optionValue = "<option value='-1' >��ѡ������汾</option>";
					$("select[@name='softwareversion']").append(optionValue);
				}
			}else{
				$("select[@name='softwareversion']").empty();
				var optionValue = "<option value='-1' >��ѡ������汾</option>";
				$("select[@name='softwareversion']").append(optionValue);
			}
			
			$("select[@name='hardwareversion']").empty();
			var optionValue1 = "<option value='-1' >��ѡ��Ӳ���汾</option>";
			$("select[@name='hardwareversion']").append(optionValue1);
		});
}

//����汾�ı䴥��
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
					var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
					$("select[@name='hardwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@name='hardwareversion']").append(optionValue);
					}
				}else{
					$("select[@name='hardwareversion']").empty();
					var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
					$("select[@name='hardwareversion']").append(optionValue);
				}
			}else{
				$("select[@name='hardwareversion']").empty();
				var optionValue = "<option value='-1' >��ѡ��Ӳ���汾</option>";
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
		alert("��ѡ���̣�");
		return false;
	}
	if("-1" == deviceModelId){
		alert("��ѡ���ͺţ�");
		return false;
	}
	if("-1" == softwareversion){
		alert("��ѡ������汾��");
		return false;
	}
	if("-1" == hardwareversion){
		alert("��ѡ��Ӳ���汾��");
		return false;
	}
	if("" == preMac){
		alert("MACǰ׺����Ϊ�գ�");
		return false;
	}else if(!reg_verify(preMac)){
		alert("MAC��ʽ���ԣ�");
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
				alert("MACǰ׺�����ڣ��޷��޸ģ�");
			}else{
				alert("�޸ĳɹ���");
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
					<th colspan="4">MACǰ׺���ƻ�������Ϣ�༭</th>
				</tr>
				<TR>
					<TD class=column width="30%" align='right'>����</TD>
					<TD width="35%">
						<select name="vendorId" class="bk" onchange="vendorChange()">
							<option value=<%=vendor_id%> selected="selected"><%=vendor_name%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>�ͺ�</TD>
					<TD width="35%">
						<select name="deviceModelId" class="bk" onchange="deviceModelChange()">
							<option value=<%=device_model_id%> selected="selected"><%=device_model%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>����汾</TD>
					<TD width="35%">
						<select name="softwareversion" class="bk" onchange="softwareversionChange()">
							<option value=<%=softwareversion%> selected="selected"><%=softwareversion%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>Ӳ���汾</TD>
					<TD width="35%">
						<select name="hardwareversion" class="bk">
							<option value=<%=hardwareversion%> selected="selected"><%=hardwareversion%></option>
						</select>
					</TD>
				</TR>
				<TR>
					<TD class=column width="30%" align='right'>MACǰ׺</TD>
					<TD width="70%">
						<input type="text" name="preMac" id="preMac" class="bk" value="<%=request.getParameter("pre_mac")%>" size="20">
						<font color="red">��ʽΪ��11BBCCDD55��Ӣ����ĸ��д�Ҳ���:</font>
						<input type="hidden" name="preMacId" id="preMacId" class="bk" value="<%=request.getParameter("preMacId")%>">
					</TD>
				</TR>
				<TR>
					<td colspan="2" align="center" class=foot>	
						<button onclick="save()">&nbsp;����&nbsp;</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button onclick="javascript:window.close();">&nbsp;ȡ��&nbsp;</button>
					</td>
				</TR>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
