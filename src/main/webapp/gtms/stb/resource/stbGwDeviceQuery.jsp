<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������豸��Ϣ����</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	change_select("vendor","-1");
	change_select("city","-1");
});

function change_select(type,selectvalue){
	switch (type){
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
function gwShare_parseMessage(ajax,field,selectvalue){
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
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
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

function getStbDeviceList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var deviceMac = $("input[name=deviceMac]").val();
	var deviceSn = $("input[name=deviceSn]").val();
	var cityId = $("input[name=cityId]").val();
	var citynext = $("input[name=citynext]").val();
	var servAccount = $("input[name=servAccount]").val();
	if (!("" == deviceSn) && deviceSn.length <6){
		alert("�豸���кŲ�Ϊ�յ�����£������λ��");
		return;
	}
	if (!("" == deviceMac) && false == validateMac(deviceMac)){
		return;
	}
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!getStbDeviceList.action'/>";
	frm.submit();
}

function validateMac(mac) {  
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC��ַֻ��A-F��ĸ�����������,��ʽ��AA:AA:AA:AA:AA:AA��");  
        return false;  
    }
    return true;
}  

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}
function addDevice(){
	var strpage = "<s:url value='/gtms/stb/resource/stbGwDeviceQueryAdd.jsp'/>";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�������豸��Ϣ����</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> ��ʼʱ��ͽ���ʱ��ֱ�Ϊ�û���ӵ�����<font color="red">*</font></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr><th colspan="4">�������豸��Ϣ��ѯ</th></tr>
					<TR>
						<TD align="right" class=column width="15%">�豸���к�</TD>
						<TD width="35%">
							<input type="text" id="deviceSn" name="deviceSn" class="bk" value="">
						</TD>
						<TD align="right" class=column width="15%">MAC��ַ</TD>
						<TD align="left" width="35%">
							<input type="text" id="deviceMac" name="deviceMac" class="bk" value="">
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">�� ��</TD>
						<TD width="35%">
							<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD align="left" width="35%">
							<select name="deviceModelId" class="bk">
								<option value="-1">����ѡ����</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class="column" width="15%">�� ��</TD>
						<TD align="left" width="35%">
							<select name="cityId" id="cityId" class="bk" onchange="change_select('cityid','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class="column" width="15%">�¼�����</TD>
						<TD align="left" width="35%"><select name="citynext" class="bk">
								<option value="-1">����ѡ������</option>
						</select></TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">�״��ϱ���ʼʱ��</TD>
						<TD width="35%">
							<input type="text" name="startTime" readonly
								value="<s:property value='startTime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</TD>
						<TD align="right" class="column" width="15%">�״��ϱ�����ʱ��</TD>
						<TD width="35%">
							<input type="text" name="endTime" readonly
								value="<s:property value='endTime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
							<font color="red">*</font>
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">ҵ���˺�</TD>
						<TD width="35%">
							<input type="text" id="servAccount" name="servAccount" class="bk" value="">
						</TD>
						<TD style="display: none"></TD>
						<TD width="35%" style="display: none">
<!-- 							<input type="text" id="servAccount" name="servAccount" class="bk" value=""> -->
						</TD>
<!-- 						<TD align="right" class="column" width="15%">�״��ϱ�ʱ��</TD> -->
<!-- 						<TD width="35%"> -->
<!-- 							<input type="text" name="startTime" readonly -->
<%-- 								value="<s:property value='startTime'/>" /> <img --%>
<!-- 								name="shortDateimg" -->
<!-- 								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})" -->
<!-- 								src="../../../images/dateButton.png" width="15" height="12" -->
<!-- 								border="0" alt="ѡ��" /> -->
<!-- 											�� -->
<!-- 							<input type="text" name="endTime" readonly -->
<%-- 								value="<s:property value='endTime'/>" /> <img --%>
<!-- 								name="shortDateimg" -->
<!-- 								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})" -->
<!-- 								src="../../../images/dateButton.png" width="15" height="12" -->
<!-- 								border="0" alt="ѡ��" /> -->
<!-- 							<font color="red">*</font> -->
<!-- 						</TD> -->
					</TR>
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:getStbDeviceList();" 
								name="gwShare_queryButton" style="CURSOR:hand"> �� ѯ </button>&nbsp;&nbsp;
<!-- 								<button onclick="javascript:addDevice();"  -->
<!-- 								name="addTemp" style="CURSOR:hand" style="display:" > �� �� </button> -->
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
			<td><iframe id="dataForm" name="dataForm" height="0"
					frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
		</tr>
	</table>
</form>

<!-- 	<div class="content"> -->
<!-- 		<iframe id="dataForm" name="dataForm" height="0" frameborder="0" -->
<!-- 			scrolling="no" width="100%" src=""></iframe> -->
<!-- 	</div> -->
</body>