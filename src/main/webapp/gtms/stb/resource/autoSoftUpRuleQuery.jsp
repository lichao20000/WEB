<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ROM�汾�Զ��������Բ�ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script type="text/javascript">
function query()
{
	document.selectForm.submit();
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];
//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";
function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

// ���̸ı䴥��
function vendorChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getDeviceModelS.action'/>";
	
	$.post(url,{
		vendorId:vendorId
		},function(ajax){
			$("select[@name='deviceModelId']").empty();
			$("select[@name='deviceModelId']").append("<option value='-1'>��ѡ���ͺ�</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					
					for(var i=0;i<lineData.length;i++){
						var opv = lineData[i].split("$");
						var optionValue = "<option value='"+opv[0]+"'>"+opv[1]+"</option>";
						$("select[@name='deviceModelId']").append(optionValue);
					}
				}
			}
			
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>������汾</option>");
			
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>��ѡ��Ӳ���汾</option>");
		});
}

//�ͺŸı䴥��
function deviceModelChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getHardwareversionS.action'/>";
	$.post(url,{
		deviceModelId:deviceModelId
		},function(ajax){
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>��ѡ��Ӳ���汾</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>";
						$("select[@name='hardwareversion']").append(optionValue);
					}
				}
			}
			  
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>��ѡ��Ӳ���汾</option>");
		});
}

//Ӳ���汾�ı䴥��
function hardwareversionChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var hardwareversion = $("select[@name='hardwareversion']").val();
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getSoftwareversionS.action'/>";
	
	$.post(url,{
		deviceModelId:deviceModelId,
		hardwareversion:hardwareversion
		},function(ajax){
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>��ѡ������汾</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"'>"+lineData[i]+"</option>";
						$("select[@name='softwareversion']").append(optionValue);
					}
				}
			}
		});
}
</script>
</head>

<body>
<form id="form" name="selectForm" target="dataForm"
	action="<s:url value='/gtms/stb/resource/autoSoftUpRule!queryRuleList.action'/>" >
	<input type="hidden" name="showType" value="<s:property value='showType'/>">
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">������ROM�汾�Զ��������Բ�ѯ</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr>
						<th colspan="4">������ROM�汾�Զ��������Բ�ѯ</th>
					</tr>
					<TR>
						<TD class=column width="15%" align='right'>����</TD>
						<TD width="35%">
							<s:select list="vendorList" name="vendorId"
								headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
								listValue="vendor_add" value="vendorId" cssClass="bk"
								onchange="vendorChange()" theme="simple">
							</s:select>
						</TD>
						<TD class=column width="15%" align='right'>�ͺ�</TD>
						<TD width="35%">
							<select name="deviceModelId" onchange="deviceModelChange()">
								<option value="-1">��ѡ���ͺ�</option>
							</select>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>Ӳ���汾</TD>
						<TD width="35%">
							<select name="hardwareversion" onchange="hardwareversionChange()">
								<option value="-1">��ѡ��Ӳ���汾</option>
							</select>
						</TD>
						<TD class=column width="15%" align='right'>����汾</TD>
						<TD width="35%">
							<select name="softwareversion" onchange="selectVersion()">
								<option value="-1">��ѡ������汾</option>
							</select>
						</TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
						</td> 
					</TR>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" 
					height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
