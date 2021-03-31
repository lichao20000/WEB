<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������������·�������Ϣ����</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	change_select("vendor","-1");
	change_select("city","-1");
});

function change_select(type,selectvalue)
{
	switch (type)
	{
		case "city":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
			$.post(url, {}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='cityId']"),selectvalue);
				$("select[@name='citynext']").html(
						"<option value='-1'>==����ѡ������==</option>");
			});
			break;
			
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='citynext']").html(
						"<option value='-1'>==����ѡ������==</option>");
				break;
			}
			$.post(url, {
				citynext : cityId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='citynext']"),selectvalue);
			});
			break;
			
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
			});
			break;
			
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
			});
			break;
		
		default:
			alert("δ֪��ѯѡ�");
			break;
	}
}

//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue)
{
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++)
	{
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		
		try{
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	
	if(flag){
		field.attr("value","-1");
	}
}

function getConfParamList()
{
	var deviceMac = trim($("input[name=deviceMac]").val());
	var deviceSn = trim($("input[name=deviceSn]").val());
	
	if (!("" == deviceSn) && deviceSn.length <6){
		alert("�豸���кŲ�Ϊ�յ�����£������λ��");
		return;
	}
	
	if (!("" == deviceMac) && !validateMac(deviceMac)){
		return;
	}
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConfParamList.action'/>";
	frm.submit();
}

function getConParamInfo(deviceId)
{
	var page = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConParamInfo.action'/>?"
					+"deviceId=" + deviceId;
	window.open(page, "","left=200,top=100,width=900,height=300,resizable=yes,scrollbars=yes");
}

function deleteConParamInfo(deviceId,mac)
{
	if(!confirm("ȷ��ɾ���豸["+mac+"]�������ò�����")){
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbSetConfParam!deleteConParamInfo.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
		alert(ajax);
		getConfParamList();
	});
}

function validateMac(mac) 
{
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC��ַֻ��A-F��ĸ�����������,��ʽ��AA:AA:AA:AA:AA:AA��");  
        return false;  
    }
    return true;
}  

function trim(str)
{
     return str.replace(/(^\s*)|(\s*$)/g,"");
}


//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
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
<form name="frm" id="frm" target="dataForm">
	<input type="hidden" name="showType" value=<s:property value="showType"/> >
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�������������·�������Ϣ����</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> 
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">�������������·�������Ϣ��ѯ</th>
					</tr>
					<tr>
						<td align="right" class=column width="15%">�豸���к�</td>
						<td width="35%">
							<input type="text" id="deviceSn" name="deviceSn" class="bk" value="">
						</td>
						<td align="right" class=column width="15%">MAC��ַ</td>
						<td align="left" width="35%">
							<input type="text" id="deviceMac" name="deviceMac" class="bk" value="">
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">�� ��</td>
						<td width="35%">
							<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</td>
						<td align="right" class=column width="15%">�豸�ͺ�</td>
						<td align="left" width="35%">
							<select name="deviceModelId" class="bk">
								<option value="-1">����ѡ����</option>
							</select>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<td align="right" class="column" width="15%">�� ��</td>
						<td align="left" width="35%">
							<select name="cityId" id="cityId" class="bk" onchange="change_select('cityid','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">�¼�����</td>
						<td align="left" width="35%"><select name="citynext" class="bk">
								<option value="-1">����ѡ������</option>
						</select></td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">����޸Ŀ�ʼʱ��</td>
						<td width="35%">
							<input type="text" name="updateStartTime" readonly value="<s:property value='updateStartTime'/>" />
							 <img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.updateStartTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
						<td align="right" class="column" width="15%">����޸Ľ���ʱ��</td>
						<td width="35%">
							<input type="text" name="updateEndTime" readonly value="<s:property value='updateEndTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.updateEndTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">�����Ч��ʼʱ��</td>
						<td width="35%">
							<input type="text" name="setLastStartTime" readonly value="<s:property value='setLastStartTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.setLastStartTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
						<td align="right" class="column" width="15%">�����Ч����ʱ��</td>
						<td width="35%">
							<input type="text" name="setLastEndTime" readonly value="<s:property value='setLastEndTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.setLastEndTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
					</tr>
					
					<tr>
						<td align="right" class=column width="15%">�Զ���������</td>
						<td width="35%">
							<select name='auto_sleep_mode'>
								<option value='-1'>��ѡ��</option>
								<option value='1'>��</option>
								<option value='0'>��</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">�Զ������ر�ʱ��</td>
						<td width="35%">
							<select name='auto_sleep_time'>
								<option value='-1'>��ѡ��</option>
								<option value='3600'>1Сʱ</option>
								<option value='1800'>30����</option>
								<option value='600'>10����</option>
								<option value='300'>5����</option>
								<option value='180'>3����</option>
								<option value='60'>1����</option>
								<option value='30'>30��</option>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">������������</td>
						<td width="35%">
							<select name='ip_protocol_version_lan'>
								<option value='-1'>��ѡ��</option>
								<option value='1'>IPv4</option>
								<option value='2'>IPv6</option>
								<option value='3'>IPv4/v6</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">������������</td>
						<td width="35%">
							<select name='ip_protocol_version_wifi'>
								<option value='-1'>��ѡ��</option>
								<option value='1'>IPv4</option>
								<option value='2'>IPv6</option>
								<option value='3'>IPv4/v6</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right" class=column width="15%">ҵ���˺�</td>
						<td width="35%">
							<input type="text" name="servAccount" class="bk" value="">
						</td>
					</tr>
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:getConfParamList();" 
								name="gwShare_queryButton" style="CURSOR:hand"> �� ѯ </button>&nbsp;&nbsp;
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
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>


</body>