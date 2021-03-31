<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����а汾�ļ��޸�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>

<%
	request.setCharacterEncoding("GBK");
	String path_id = request.getParameter("path_id");
	String vendorName = request.getParameter("vendorName");
	String vendor_id = request.getParameter("vendor_id");
	String deviceModel = request.getParameter("deviceModel");
	String goalDeviceTypeName = request.getParameter("goalDeviceTypeName");
	String goal_devicetype_id = request.getParameter("goal_devicetype_id");
	String device_model_id = request.getParameter("device_model_id");
	
	String hard_version = request.getParameter("hard_version");
	String version_path = request.getParameter("version_path");
	String special_path = request.getParameter("special_path");
	String dcn_path = request.getParameter("dcn_path");
%>

<style>
span{
	position:static;
	border:0;
}
</style>
</head>
<body>
<input type="hidden" name="PathId" value=<%=path_id%> >
<input type="hidden" name="newPathId" value="-1" >
<input type="hidden" name="deviceModelId" value=<%=device_model_id%> >

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1" id="gwShare_thTitle">�����а汾�ļ��޸�</td></tr>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">�� ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" disabled="disabled">
				<option value=<%=vendor_id%> selected="selected"><%=vendorName%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" disabled="disabled">
				<option value=<%=device_model_id%> selected="selected"><%=deviceModel%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
		<TD width="35%">
			<select name="hard_version" class="bk" disabled="disabled">
				<option value=<%=hard_version%> selected="selected"><%=hard_version%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="soft_version" class="bk" onchange="change_select('version_path','-1')">
				<option value=<%=goal_devicetype_id%> selected="selected"><%=goalDeviceTypeName%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">����·��</TD>
		<TD width="35%">
			<select name="version_path" class="bk" onchange="change_select('special_path','-1')">
				<option value=<%=version_path%> selected="selected"><%=version_path%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">ר��·��</TD>
		<TD width="35%">
			<select name="special_path" class="bk" onchange="change_select('dcn_path','-1')">
				<option value=<%=special_path%> selected="selected"><%=special_path%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">DCN·��</TD>
		<TD width="35%">
			<select name="dcn_path" class="bk" >
				<option value=<%=path_id%> selected="selected"><%=dcn_path%></option>
			</select>
		</TD>
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:updateTemp();" 
				name="addButton" style="CURSOR:hand" style="display:" > �� �� </button>
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	change_select("soft_version","-1");
});

function change_select(type,selectvalue){
	switch (type){
	case "soft_version":
		var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getSoftVersion.action"/>";
		var hardVersion = $("select[@name='hard_version']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		if("-1"==hardVersion){
			$("select[@name='soft_version']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
			break;
		}
		
		$.post(url,{
			gwShare_hardwareVersion:hardVersion,
			gwShare_deviceModelId:deviceModelId
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='soft_version']"),selectvalue);
		});
		break;
	case "version_path":
		var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVersionPath.action"/>";
		var soft_version = $("select[@name='goal_devicetypeId']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		if("-1"==soft_version){
			$("select[@name='version_path']").html("<option value='-1'>==����ѡ��Ŀ��汾==</option>");
			break;
		}
		
		$.post(url,{
			gwShare_vendorId:vendorId,
			gwShare_deviceModelId:deviceModelId
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='version_path']"),selectvalue);
		});
		break;
	case "special_path":
		var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getSpecialPath.action"/>";
		var version_path = $("select[@name='version_path']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		if("-1"==version_path){
			$("select[@name='special_path']").html("<option value='-1'>==����ѡ����·��==</option>");
			break;
		}
		
		$.post(url,{
			gwShare_vendorId:vendorId,
			gwShare_deviceModelId:deviceModelId,
			version_path:version_path
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='special_path']"),selectvalue);
		});
		break;
	case "dcn_path":
		var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDcnPath.action"/>";
		var special_path = $("select[@name='special_path']").val();
		var deviceModelId = $("select[@name='deviceModelId']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var version_path = $("select[@name='version_path']").val();
		if("-1"==special_path){
			$("select[@name='dcn_path']").html("<option value='-1'>==����ѡ��ר��·��==</option>");
			break;
		}
		
		$.post(url,{
			gwShare_vendorId:vendorId,
			gwShare_deviceModelId:deviceModelId,
			version_path:version_path,
			special_path:special_path
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='dcn_path']"),selectvalue);
		});
		var dcn_path = $.trim($("select[@name='dcn_path']").val());
		$("input[@name='newPathId']").val(dcn_path.split("|")[0]);
		break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

function updateTemp(){
	var PathId = $.trim($("input[@name='PathId']").val());
	var newPathId = $.trim($("input[@name='newPathId']").val());
	var deviceTypeId = $.trim($("select[@name='soft_version']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var vendorId=$.trim($("select[@name='vendorId']").val());
	
	if ("-1" == deviceTypeId){
		alert("��ѡ��Ŀ��汾");
		return;
	}
	
	var version_path = $.trim($("select[@name='version_path']").val());
	if ("-1" == version_path){
		alert("��ѡ����·��");
		return;
	}
	
	var special_path = $.trim($("select[@name='special_path']").val());
	if ("-1" == special_path){
		alert("��ѡ��ר��·��");
		return;
	}
	
	var dcn_path = $.trim($("select[@name='dcn_path']").val());
	if ("-1" == dcn_path){
		alert("��ѡ��DCN��·��");
		return;
	}
	
	if("-1"==newPathId){
		alert("�����Ѵ��ڣ�");
		return;
	}else{
		newPathId=dcn_path.split("|")[0];
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!modifyUpgradeFilePath.action'/>";
	$.post(url,{
		pathId : PathId,
		pathIdNew : newPathId,
		deviceModelId : deviceModelId,
		goal_devicetypeId : deviceTypeId,
		vendorId:vendorId
	},function(ajax){
		if("1"== ajax){
			alert("�޸ĳɹ���");
		}else if("-1"== ajax){
			alert("�����Ѵ��ڣ�");
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
</SCRIPT>