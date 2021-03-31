<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����������汾�޸�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<%
	request.setCharacterEncoding("GBK");
	String devicetype_id_old = request.getParameter("devicetype_id_old");
	String vendorName = request.getParameter("vendorName");
	String deviceModel = request.getParameter("deviceModel");
	String sourceDeviceTypeName = request.getParameter("sourceDeviceTypeName");
	String goalDeviceTypeName = request.getParameter("goalDeviceTypeName");
	String tempName = request.getParameter("tempName");
	tempName=java.net.URLDecoder.decode(tempName,"utf-8");
	String device_model_id = request.getParameter("device_model_id");
	String belongName = request.getParameter("belongName");
	String hardwareversion = request.getParameter("hardwareversion");
	String belong = request.getParameter("belong");
	String valid = request.getParameter("valid");
%>

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
	<tr><td colspan="2" class="title_1"  >�����������汾�޸�</td></tr>
	<TR >
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" disabled="disabled">
				<option value="-1" selected="selected"><%=vendorName%></option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" disabled="disabled">
				<option value=<%=device_model_id%> selected="selected"><%=deviceModel%></option>
			</select>
		</TD>
	</TR>
	<% if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) { %>
		<TR>
			<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
			<TD width="35%">
				<select name="belong" class="bk" disabled="disabled">
					<option value="1" selected="selected"><%=hardwareversion%></option>
				</select>
			</TD>
		</TR>
	<% } %>
	<TR>	
		<TD align="right" class="title_2" width="15%">ԭ�汾</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk" disabled="disabled">
				<option value=<%=devicetype_id_old%> selected="selected"><%=sourceDeviceTypeName%></option>
			</select>
		</TD>	
	</TR>
	<TR >	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value=<%=devicetype_id_old%> selected="selected"><%=goalDeviceTypeName%></option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">�����������</TD>
		<TD width="35%">
			<select name="temp_id" class="bk" disabled="disabled">
				<option value="1" selected="selected"><%=tempName%></option>
			</select>
		</TD>
	</TR>
	<% if (LipossGlobals.inArea("hn_lt")) { %>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">����ƽ̨</TD>
		<TD width="35%">
			<select name="platformId" id="platformId" class="bk">
				<option value=<%=belong%> selected="selected"><%=belongName%></option>
			</select>
		</TD>
	</TR>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">�Ƿ���Ч</TD>
		<TD width="35%">
			<select name="valid" class="bk">
			<% if ("��Ч".equals(valid)) { %>
				<option value="1" selected="selected">��Ч</option>
				<option value="0">ʧЧ</option>
			<% }else{ %>
				<option value="0" selected="selected">ʧЧ</option>
				<option value="1">��Ч</option>
			<% } %>
			</select>
		</TD>
	</TR>
	<% } %>
	
	
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:updateTemp();" 
				name="addButton" style="CURSOR:hand" style="display:" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	if("hn_lt"==instAreaName){
		$("tr[id=tr_belong]").attr("style","display: display"); 
		intPlatformType();
	}else{
		$("tr[id=tr_belong]").attr("style","display: none"); 
	}
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getDevicetype.action'/>";
	$.post(url,{
		gwShare_deviceModelId:deviceModelId
	},function(ajax){
		gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),<%=devicetype_id_old%>);
	});
});
function updateTemp(){
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var platformId = $.trim($("select[@name='platformId']").val());
	var valid = $.trim($("select[@name='valid']").val());
	
	if(source_devicetypeId == goal_devicetypeId){
		alert("ԭʼ�汾��Ŀ��汾һ�£�������ѡ��");
		return;
	}
	if("-1" == goal_devicetypeId){
		alert("��ѡ��Ŀ��汾��");
		return;
	}
	if("-1" == platformId){
		alert("��ѡ������ƽ̨��");
		return;
	}
	if("-1" == valid){
		alert("��ѡ���Ƿ���Ч��");
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!modifyUpgradeTemp.action'/>";
	$.post(url,{
		source_devicetypeId : source_devicetypeId,
		platformId : platformId,
		goal_devicetypeId : goal_devicetypeId,
		valid:valid
	},function(ajax){
		if("1"== ajax){
			alert("�޸ĳɹ���");
		}else{
			alert("�޸�ʧ�ܣ�");
		}
		window.close();
	});
}

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
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

function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				gwShare_parseMessage(ajax,$("select[@name='platformId']"),<%=belong%>);
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='0' >��ѡ��ƽ̨����</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}
</SCRIPT>