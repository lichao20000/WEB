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
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			var instArea=$("input[@name='instArea']").val();
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			
			if("-1"==deviceModelId){
				$("select[@name='hardVersion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='hardVersion']"),selectvalue);
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
	var version_path = $.trim($("select[@name='version_path']").val());
	var special_path = $.trim($("select[@name='special_path']").val());
	var dcn_path = $.trim($("select[@name='dcn_path']").val());
	
	if ("-1" == goal_devicetypeId){
		alert("��ѡ��Ŀ��汾");
		return;
	}
	if ("-1" == version_path){
		alert("��ѡ����·��");
		return;
	}
	if ("-1" == special_path){
		alert("��ѡ��ר��·��");
		return;
	}
	if ("-1" == dcn_path){
		alert("��ѡ��DCN·��");
		return;
	}

	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!addUpgradeFilePath.action'/>";
	$.post(url,{
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		goal_devicetypeId : goal_devicetypeId,
		pathId:dcn_path.split("|")[0]
	},function(ajax){
		if("1"== ajax){
			alert("�����ɹ���");
		}else if("-1"==ajax){
			alert("�����Ѵ��ڣ�");
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
		<TD align="right" class="title_2" width="15%">�� ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
		<TD width="35%">
			<select name="hardVersion" class="bk" onchange="change_select('soft_version','-1')">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk" onchange="change_select('version_path','-1')">
				<option value="-1">����ѡӲ���汾</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">����·��</TD>
		<TD width="35%">
			<select name="version_path" class="bk" onchange="change_select('special_path','-1')">
				<option value="-1">����ѡĿ��汾</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">ר��·��</TD>
		<TD width="35%">
			<select name="special_path" class="bk" onchange="change_select('dcn_path','-1')">
				<option value="-1">����ѡ����·��</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">DCN·��</TD>
		<TD width="35%">
			<select name="dcn_path" class="bk" >
				<option value="-1">����ѡר��·��</option>
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