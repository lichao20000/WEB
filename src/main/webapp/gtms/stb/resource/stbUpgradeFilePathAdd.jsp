<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����а汾�ļ�����</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	change_select("vendor","-1");
});

function change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='filePath']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
			});
			var url2 = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!getPathByModelId.action'/>";
			$.post(url2,{
				deviceModelId : deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='filePath']"),selectvalue);
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

function insertFilePath(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var filePath = $.trim($("select[@name='filePath']").val());
	if ("-1" == goal_devicetypeId || "-1" == filePath ){
		alert("��ѡ��Ŀ��汾���ļ�·��");
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!addUpgradeFilePath.action'/>";
	$.post(url,{
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		goal_devicetypeId : goal_devicetypeId,
		pathId : filePath
	},function(ajax){
		if("1"== ajax){
			alert("�����ɹ���");
		}else{
			alert("����ʧ�ܣ�");
		}
		window.close();
	});
}

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}
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
<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1" >�����а汾�ļ�����</td></tr>
	<TR>
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
	</TR>
	<TR>
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
		</TD>
	</TR>
	
	<TR>	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="title_2" width="15%">����·��</TD>
		<TD width="35%">
			<select name="filePath" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>	
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertFilePath();" 
				name="addButton" style="CURSOR:hand" style="display:" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>