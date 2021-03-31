<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����������汾����</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	change_select("vendor","-1");
	if("hn_lt"==instAreaName){
		$("tr[id=tr_belong]").attr("style","display: display"); 
		intPlatformType();
	}else{
		$("tr[id=tr_belong]").attr("style","display: none"); 
	}
	if("jx_dx"==instAreaName){
		$("tr[id=tr_type]").attr("style","display: none"); 
	}
});

function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='0' >��ѡ��ƽ̨����</option>";
					$("select[@id='platformId']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
						$("select[@id='platformId']").append(optionValue);
					}
				}else{
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='0' >��ѡ��ƽ̨����</option>";
					$("select[@id='platformId']").append(optionValue);
				}
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='0' >��ѡ��ƽ̨����</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}

function change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
			});
			break;
		case "hardwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='hardwareversion']"),selectvalue);
			});
			break;
		case "softwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetypeByHardVersion.action"/>";
			var hardwareversion = $("select[@name='hardwareversion']").val();
			if("-1"==hardwareversion){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				break;
			}
			$.post(url,{
				gwShare_hardwareVersion : hardwareversion
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

function checkDeviceTypeId(){
	var source_devicetypeId = $("select[@name='source_devicetypeId']").val();
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!checkDeviceTypeId.action'/>";
	$.post(url,{
		source_devicetypeId : source_devicetypeId
	},function(ajax){
		if("1"== ajax){
			alert("��ǰԭʼ�汾�Ѵ��ڣ�������ѡ��");
			$('#addButton').attr("disabled",true);
		}else{
			$('#addButton').attr("disabled",false);
		}
	});
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

function insertTemp()
{
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var temp_id = $.trim($("select[@name='temp_id']").val());
	var valid = -1;
	var platformId = 0;
	if("hn_lt"==instAreaName){
		platformId = $.trim($("select[@name='platformId']").val());
		if(platformId == "-1"){
			alert("��ѡ��ҵ��ƽ̨��");
			return;
		}
		
		valid = $.trim($("select[@name='valid']").val());
		if(valid == "-1"){
			alert("��ѡ���Ƿ���Ч��");
			return;
		}
	}
	if(source_devicetypeId == goal_devicetypeId){
		alert("ԭʼ�汾��Ŀ��汾һ�£�������ѡ��");
		return;
	}
	
	if(source_devicetypeId == "-1"|| "-1" == goal_devicetypeId){
		alert("��ѡ��ԭʼ�汾��Ŀ��汾��");
		return;
	}
	if(temp_id == "-1"){
		alert("��ѡ������������ͣ�");
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!addUpgradeTemp.action'/>";
	if("hn_lt"==instAreaName)
	{
		$.post(url,{
			vendorId : vendorId,
			deviceModelId : deviceModelId,
			source_devicetypeId : source_devicetypeId,
			goal_devicetypeId : goal_devicetypeId,
			platformId : platformId,
			temp_id : temp_id,
			valid:valid
		},function(ajax){
			if("1"== ajax){
				alert("�����ɹ���");
			}else{
				alert("����ʧ�ܣ�");
			}
			window.close();
		});
	}
	else
	{
		$.post(url,{
			vendorId : vendorId,
			deviceModelId : deviceModelId,
			source_devicetypeId : source_devicetypeId,
			goal_devicetypeId : goal_devicetypeId,
			platformId : platformId,
			temp_id : temp_id
		},function(ajax){
			if("1"== ajax){
				alert("�����ɹ���");
			}else{
				alert("����ʧ�ܣ�");
			}
			window.close();
		});
	}
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
	<tr><td colspan="2" class="title_1" >�����������汾����</td></tr>
	<TR >
		<TD align="right" class="title_2" width="15%">�� ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<%
				if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) {
			%>
				<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1')">
				<option value="-1">����ѡ����</option>
			</select>
			<%
				}else{
			%>
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
			<%
				}
			%>
		</TD>
	</TR>
	<%
		if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) {
	%>
		<TR>	
			<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">����ѡ���豸�ͺ�</option>
				</select>
			</TD>	
		</TR>	
	<%
		}
	%>
	<TR>	
		<TD align="right" class="title_2" width="15%">ԭ�汾</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk" onchange="checkDeviceTypeId()">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>	
	</TR>
	<TR >	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
	</TR>
	<TR id="tr_type">
		<TD align="right" class="title_2" width="15%">�����������</TD>
		<TD width="35%">
			<select name="temp_id" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="1">��ͨ�������</option>
				<option value="2">ҵ������������</option>
				<option value="3">��ҵ������������</option>
			</select>
		</TD>
	</TR>
	
	<% if (LipossGlobals.inArea("hn_lt")) { %>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">����ƽ̨</TD>
		<TD width="35%">
			<select name="platformId" id="platformId" class="bk">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
	</TR>
	
	
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">�Ƿ���Ч</TD>
		<TD width="35%">
			<select name="valid" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="1">��Ч</option>
				<option value="0">ʧЧ</option>
			</select>
		</TD>
	</TR>
	<% } %>
		
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertTemp();" 
				name="addButton" id="addButton" style="CURSOR:hand" style="display:" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>