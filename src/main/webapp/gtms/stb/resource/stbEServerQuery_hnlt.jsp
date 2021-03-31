<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����й�����Ϣ��ѯ</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function queryEServerList()
{
	var mac = trim($("input[name=mac]").val());
	if (!("" == mac) && !validateMac(mac)){
		return;
	}
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbEServerQuery!queryEServerList.action'/>";
	frm.submit();
}

function toExcel()
{
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbEServerQuery!toExcel.action'/>";
	frm.submit();
}

function validateMac(mac) 
{
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{12}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 12) {  
        alert("MAC��ַֻ��A-F��ĸ�����������,��ʽ��AA11BB22AAFF");  
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

function dyniframesize() 
{
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) 
	{
		if (document.getElementById) 
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) 
			{
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
							�����й�����Ϣ��ѯ
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> 
							��ʼʱ��ͽ���ʱ��ֱ�Ϊ������������<font color="red">*</font>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">�����й�����Ϣ��ѯ</th>
					</tr>
					<tr>
						<td align="right" class=column width="10%">������ʼʱ��</td>
						<td width="35%">
							<input type="text" name="startTime" readonly value="<s:property value='startTime'/>" />
							 <img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
						<td align="right" class="column" width="10%">��������ʱ��</td>
						<td>
							<input type="text" name="endTime" readonly value="<s:property value='endTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="10%">ҵ���˺�</td>
						<td width="35%">
							<input type="text" name="servAccount" class="bk" value="">
						</td>
						<td align="right" class=column width="10%">������MAC</td>
						<td align="left">
							<input type="text" name="mac" class="bk" value="">
							<font color="red">*MAC��ַֻ����A-F��ĸ�����������,��ʽ��AA11BB22AAFF</font>
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="10%">����</td>
						<td width="35%">
							<input type="text" name="grid" class="bk" value="">
						</td>
						<td align="right" class=column width="10%">����Ա</td>
						<td align="left">
							<input type="text" name="opertor" class="bk" value="">
						</td>
					</tr>
					
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:queryEServerList();" 
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