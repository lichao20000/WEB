<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����а汾�ļ�����</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>"  rel="stylesheet"  type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	change_select("vendor","-1");
});

function addFilePath(){
	var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeFilePathAdd.jsp'/>?operateType=add";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
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
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			var instArea=$("input[@name='instArea']").val();
			if(instArea=="hn_lt"){
				var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			}else{
				var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			}
			
			if("-1"==deviceModelId){
				if(instArea=="hn_lt"){
					$("select[@name='hardVersion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				}else{
					$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				}
				
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				if(instArea=="hn_lt"){
					gwShare_parseMessage(ajax,$("select[@name='hardVersion']"),selectvalue);
				}else{
					gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
				}
			});
			break;	
		case "soft_version":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getSoftVersion.action"/>";
			var hardVersion = $("select[@name='hardVersion']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==hardVersion){
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				break;
			}
			
			$.post(url,{
				gwShare_hardwareVersion:hardVersion,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
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

function getFilePathList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeFilePathList.action'/>";
	frm.submit();
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
<form name="frm" id="frm" method="post" target="dataForm">
<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
<TABLE width="98%" class="querytable" align="center">
	<tr>
		<td colspan="4" class="title_1">�����а汾�ļ���ѯ</td>
	</tr>
	<TR>
		<TD align="right" class="title_2" width="15%">�� ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
		</TD>
	</TR>
	<s:if test="instArea=='hn_lt'">
	<TR>	
		<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
		<TD width="35%">
			<select name="hardVersion" class="bk" onchange="change_select('soft_version','-1')">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk"">
				<option value="-1">����ѡӲ���汾</option>
			</select>
		</TD>
	</TR>
	</s:if>
	<s:else>
	<TR>	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk"">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%"></TD>
		<TD width="35%"></TD>	
	</TR>
	</s:else>
	
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<% if (LipossGlobals.inArea("jx_dx") ) { %>
				<input type="button" onclick="javascript:addFilePath();" 
				name="addFilePath_jx" style="CURSOR:hand" style="display:" class="jianbian" value="�� ��" > 
				<%} %>
				<input type="button" onclick="javascript:getFilePathList();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" class="jianbian" value="�� ѯ" > 
				<input type="button" onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" class="jianbian" value="�� ��"> 
			</div>
		</td>
	</tr>
</TABLE>
</form>
<br>
<div>
	<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
</div>
</body>