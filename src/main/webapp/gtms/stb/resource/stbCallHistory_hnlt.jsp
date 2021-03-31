<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������ϱ���¼��ѯ</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function query()
{
	var mac = trim($("input[name=mac]").val());
	var ip = trim($("input[name=login_ip]").val());
	if ((""!=mac && !validateMac(mac)) 
			|| (""!=ip && !validateIp(ip))){
		return;
	}
	
	var stime=$("input[name=startTime]").val();
	var etime=$("input[name=endTime]").val();
	if(stime=="" || etime==""){
		alert("��ѡ���ѯʱ��Σ�");
		return;
	}
	
	var s=stime.substring(0,7);
	var e=etime.substring(0,7);
	if(s!=e){
		alert("��ֹ���²�ѯ��������ѡ��ʱ��Σ�");
		return;
	}
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbCallHistoryACT!query.action'/>";
	frm.submit();
	$("tr[@id='resultData']").show();
}

function validateMac(mac) 
{
    mac = mac.toUpperCase();
    var expre = /[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}[A-F\d]{2}/;  
    var regexp = new RegExp(expre); 
    if (!regexp.test(mac) || mac.length != 12) {  
        alert("MAC��ַֻ��A-F��ĸ�����������,��ʽ��AA11BB22CC33��");  
        return false;  
    }
    return true;
}

function validateIp(ip) 
{
    var expre = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;    
    var regexp = new RegExp(expre);  
    if (!regexp.test(ip)) {  
        alert("�����ip�����Ϲ淶��");  
        return false;  
    }
    return true;
}

function trim(str){
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
<input type="hidden" name="table" readonly value="<s:property value='table'/>" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�������豸�ϱ���¼��ѯ
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" class="querytable" align="center">
				<tr>
					<th colspan="4">�������豸�ϱ���¼��ѯ</th>
				</tr>
				<tr>
					<td align="right" class=column width="15%">����ҵ���˺�</td>
					<td width="35%">
						<input type="text" id="request_username" name="request_username" class="bk" value="">
					</td>
					<td align="right" class=column width="15%">����ҵ���˺�</td>
					<td align="left" width="35%">
						<input type="text" id="result_username" name="result_username" class="bk" value="">
					</td>
				</tr>
				<tr>
					<td align="right" class=column width="15%">������MAC</td>
					<td width="35%">
						<input type="text" id="mac" name="mac" class="bk" value="">
					</td>
					<td align="right" class=column width="15%">����IP��ַ</td>
					<td align="left" width="35%">
						<input type="text" id="login_ip" name="login_ip" class="bk" value="">
					</td>
				</tr>
				<tr>
					<td align="right" class=column width="15%">����ʼʱ��</td>
					<td width="35%">
						<input type="text" name="startTime" readonly value="<s:property value='startTime'/>" />
						 <img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
					</td>
					<td align="right" class="column" width="15%">�������ʱ��</td>
					<td width="35%">
						<input type="text" name="endTime" readonly value="<s:property value='endTime'/>" /> 
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="right" class="foot" width="100%">
						<div align="right">
							<button onclick="javascript:query();" name="gwShare_queryButton" style="CURSOR:hand"> 
								�� ѯ 
							</button>&nbsp;&nbsp;
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="25" id="resultStr"></td>
	</tr>
	<tr id="resultData" style="display: none">
		<td>
			<iframe id="dataForm" name="dataForm" height="530" frameborder="0" scrolling="yes" width="100%" src=""></iframe>
		</td>
	</tr>
</table>
</form>
</body>