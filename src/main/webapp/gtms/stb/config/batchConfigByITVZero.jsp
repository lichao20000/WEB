<!DOCTYPE html PUBLIC "-//W3C//Dtd HTML 4.01 transitional//EN" "http://www.w3.org/tr/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
 			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

//ϵͳ����
var instAreaName = <%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>;
//ȫѡ
function selectAllModel(checkOBJ){
	if(checkOBJ.checked){
		 $("input[@name='deviceModelId']").attr("checked",true); 
	}else{
		$("input[@name='deviceModelId']").attr("checked",false);
	}
}
/**
**���ݳ��̻�ȡ�豸�ͺţ����Ը�ѡ�����ʽ���ֳ���
**/ 
function getdeviceModel(){
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getDeviceModel.action'/>";
	if(vendorId!="-1"){
		$("div[@id='adaptModelType']").html("");
		$.post(url,{
			vendorId:vendorId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					var checkboxtxt = "<input type='checkbox' name='allDeviceModel' onclick='javascript:selectAllModel(this);getSoftVersion()'>ȫѡ &nbsp;&nbsp;&nbsp;";
					$("div[@id='adaptModelType']").append(checkboxtxt);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"' onclick='getSoftVersion()'>"+xText+"  ";
						$("div[@id='adaptModelType']").append(checkboxtxt);
					}
				}else{
					$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
				}
			}else{
				$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
			}
		});
	}else{
		$("div[@id='adaptModelType']").html("��ѡ����");
	}
}
//ȫѡ
function selectAllSoft(checkOBJ){
	if(checkOBJ.checked){
		 $("input[@name='deviceTypeId']").attr("checked",true); 
	}
	else{
		$("input[@name='deviceTypeId']").attr("checked",false);
	}
}
/**
**�����豸�ͺŻ�ȡ����汾�����Ը�ѡ�����ʽ���ֳ���
**/ 
function getSoftVersion(){
	var deviceModelIds ="";
	$("input[@name='deviceModelId'][@checked]").each(function(){ 
    	deviceModelIds += $(this).val()+",";
    });
	if(deviceModelIds!=""){
		deviceModelIds=deviceModelIds.substring(0,deviceModelIds.length-1);
	}
	var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getSoftVersion.action'/>";
	if(deviceModelIds!=""){
		$("div[@id='softVersion']").html("");
		$.post(url,{
			deviceModelIds : deviceModelIds
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					var checkboxSoft ="<input type='checkbox' name='allSoftVersion' onclick='javascript:selectAllSoft(this)'>ȫѡ &nbsp;&nbsp;&nbsp;";
					$("div[@id='softVersion']").append(checkboxSoft);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						checkboxSoft = "<input type='checkbox' name='deviceTypeId' value='"+xValue+"'>"+xText+"  ";
						$("div[@id='softVersion']").append(checkboxSoft);
					}
				}else{
					$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ����汾��");
				}
			}else{
				$("div[@id='softVersion']").append("���ͺ�û�ж�Ӧ����汾��");
			}
		});
	}else{
		$("div[@id='softVersion']").html("��ѡ���ͺ�");
	}
}
//����
function ExecMod(){
	//������
	var taskName =  $("#taskName").val();
	//����
	var cityId = $("select[@name='cityId']").val();
	//����
	var vendorId = $("select[@name='vendorId']").val();
	//�ͺ�
	var deviceModelIds = "";
	//�汾
	var deviceTypeIds ="";
	//�����ڵ�·��
    var paramNodePath = ""; 
    //����ֵ
	var paramValue = "";
    //��������
	var paramType = "";
	$("input[@name='deviceModelId'][@checked]").each(function(){ 
    	deviceModelIds += $(this).val()+",";
    });
    $("input[@name='deviceTypeId'][@checked]").each(function(){ 
    	deviceTypeIds += $(this).val()+",";
    });
	if("" == trim(taskName)){
		alert("����������Ϊ�գ�");
		return false; 
	} 
	if("" == cityId || "-1" == cityId){
		alert("��ѡ������");
		return false;
	}
    if(deviceModelIds==""){
		alert("��ѡ�������ͺ�!");
		return false;
	}
    if(deviceModelIds.length > 0){
    	$("input[@name='deviceModelIds']").attr("value",deviceModelIds.substring(0,deviceModelIds.length-1));
    }
    if(deviceTypeIds==""){
		alert("��ѡ������汾!");
		return false;
	}
    if(deviceTypeIds.length>0){
    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
    }
    if(!checkForm()){
    	return false;
    }
	//��������	
    for(var i = 0; i <= 3; i++){
		paramNodePath += "," + $("#paramNodePath" + i).val();
		paramValue += "," + $("#paramValue" + i).val();
		paramType += "," + $("#paramType" + i).val();
    }
    $("input[@name='paramNodePath']").attr("value",paramNodePath);
 	$("input[@name='paramValue']").attr("value",paramValue);
	$("input[@name='paramType']").attr("value",paramType);
	$("form[@name='batchexform']").attr("action","batchCustomNodeConfig!doConfigByITV.action");
	$("form[@name='batchexform']").submit();
	
	$("#ExecModBtn").attr("disabled", true);
	setTimeout("disableOK()",2000);
}

function disableOK(){
	$("#ExecModBtn").attr("disabled","");
}

function checkForm(){
	var result = true;
	for(var i = 0; i <= 3; i++){
		//��������
		var tempPath = $("#paramNodePath" + i).val();
		var tempValue = $("#paramValue" + i).val();
		var tempType = $("#paramType" + i).val();
		if(tempPath == ""){
			alert("��" + (i+1) + "�������ڵ�·��Ϊ�գ�");
			result = false;
			return false;
		}
		else if(tempValue == ""){
			alert("��" + (i + 1) + "������ֵΪ�գ�");
			result = false;
			return false;
		}
		else if(tempType == "-1"){
			alert("��" + ( i + 1) + "����������Ϊ�գ�");
			result = false;
			return false;
		}
	}
	return result;
}
function softdivcl(){
	$("div[@id='div_css']").hide();
}
//ȥ���ո�
function trim(str){
    	return str.replace(/(^\s*)|(\s*$)/g,"");
}
</script>
</head>
<body>
	<table width="98%" align="center" class="querytable">
		<tr>
			<td>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				����ǰ��λ�ã�ITV�������·��ڵ�
			</td>
		</tr>
	</table>
	<br>
	<s:form action="batchCustomNodeConfig!initBatchCfgByITV.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="" target="dataForm">
		<input type="hidden" name="deviceModelIds" value=""/>
		<input type="hidden" name="paramNodePath" value="">
		<input type="hidden" name="paramValue" value="">
		<input type="hidden" name="paramType" value="">
		<input type="hidden" name="deviceTypeIds" value="">
		<input type="hidden" name="macSG" value="">
		<input type="hidden" name="ipSG"   value="">
		<input type="hidden" name="ipCheck" value="">
		<input type="hidden" name="macCheck" value="">
		<input type="hidden" name="btnValue4MAC" value="">
		<input type="hidden" name="btnValue4IP" value="">
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					ITV�������·��ڵ�
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">��������</td>
				<td colspan="3"> <input type="text" id="taskName" name="taskName" width="500"> </td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					����
				</td>
			<td width="85%" colspan="3">
					<s:select list="cityList" name="cityId" headerKey="-1"
						headerValue="��ѡ������" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple"></s:select>
				</td>
		    </tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					����
				</td>
				<td width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="getdeviceModel();"
						theme="simple">
				</s:select>
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					�ͺ�
				</td>
				<td width="85%" colspan="3">
					<div id="adaptModelType">
						 ��ѡ����
					</div>
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					����汾
				</td>
				<td width="85%" colspan="3">
					<div id="softVersion">
						  ��ѡ���ͺ�
					</div>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					�����ڵ�·��
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					����ֵ
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue0" name="paramValue0" value=""/>
				</td>
				<td  class="title_2" width="15%">
					��������
				</td>
				<td  width="35%">
					<select name="paramType0" id="paramType0" class="bk">
					 	<option value="-1">==��ѡ��==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					�����ڵ�·��
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath1" name="paramNodePath1" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					����ֵ
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue1" name="paramValue1" value=""/>
				</td>
				<td  class="title_2" width="15%">
					��������
				</td>
				<td width="35%" >
					<select name="paramType1" id="paramType1" class="bk">
					 	<option value="-1">==��ѡ��==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					�����ڵ�·��
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath2" name="paramNodePath2" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					����ֵ
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue2" name="paramValue2" value=""/>
				</td>
				<td  class="title_2" width="15%">
					��������
				</td>
				<td width="35%" >
					<select name="paramType2" id="paramType2" class="bk">
					 	<option value="-1">==��ѡ��==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					</select>
				</td>
			</tr>
			
			<tr>
				<td width="15%" class="title_2">
					�����ڵ�·��
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath3" name="paramNodePath3" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					����ֵ
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue3" name="paramValue3" value=""/>
				</td>
				<td  class="title_2" width="15%">
					��������
				</td>
				<td width="35%" >
					<select name="paramType3" id="paramType3" class="bk">
					 	<option value="-1">==��ѡ��==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button onclick="ExecMod()" id="ExecModBtn">
							�·�
						</button>
					</div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align="left" class="foot">
					<div id="resultDIV" ></div>
				</td>
			</tr>
		</table>
	</s:form>
	<div>
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
		scrolling="no" width="100%" src=""></iframe>
	</div>
	<br>  
	<br>
	<div id="divDetail"
		style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
	<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 250px; width: 55%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
		<table class="querytable" align="center" width="100%">
			<tr>
				<td width="30%" id="ventd" class="title_2" align="center"> 
				</td>
				<td id="softVershow" class="title_2" align="center">	
				</td>
			</tr>
			<tr>
				<td colspan="2" class="title_2" align="center">
					<button name="softdivtbn" onclick="softdivcl()">
						�ر�
					</button>
				</td>
			</tr>
		</table>				
	</div>
</body>
</html>