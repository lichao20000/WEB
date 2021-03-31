<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@ page import="java.util.Map" %>
<%
Map<String,String> data = (Map)request.getAttribute("data");
String category = data.get("category");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������޸�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
	
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';

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
<form name="frm" id="frm" >

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1" >�������޸�</td></tr>
	<tr>
		<TD align="right" class="column" width="15%">�豸���к�</TD>
		<TD width="35%">
			<input type="hidden" name="deviceId" value=<s:property value="data.deviceId" /> >
			<input type="text" id="deviceSn" name="deviceSn" class="bk" value="<s:property value="data.deviceSerialnumber" />"> <font color="red">*�����������λ</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">OUI</TD>
		<TD width="35%">
			<input type="text" id="oui" name="oui" class="bk" value="<s:property value="data.oui" />"> <font color="red">*���Ȳ�������λ</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">MAC��ַ</TD>
		<TD width="35%">
			<input type="text" id="deviceMac" name="deviceMac" class="bk" value="<s:property value="data.cpe_mac" />" disabled="disabled"> 
			<font color="red">*MAC��ַֻ��A-F��ĸ�����������,��ʽ��AA:AA:AA:AA:AA:AA��</font>
		</TD>
	</tr>
	<TR >
		<TD align="right" class="column" width="15%">�� ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1','2')">
				<option value="<s:property value="data.vendorId" />">==��ѡ��==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1','2')">
				<option value="<s:property value="data.deviceModelId" />">����ѡ����</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="column" width="15%">Ӳ���汾</TD>
		<TD width="35%">
			<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1','2')">
				<option value="<s:property value="data.hardwareversion" />">����ѡ���豸�ͺ�</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>	
	<TR>	
		<TD align="right" class="column" width="15%">����汾</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk">
				<option value="<s:property value="data.devicetypeId" />">����ѡ��Ӳ���汾</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">����</TD>
		<TD width="35%">
			<select name="cityId" class="bk" onchange="change_select('cityid','-1','2')">
				<option value="<s:property value="data.parentcity" />">==��ѡ��==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">�¼�����</TD>
		<TD width="35%">
			<select name="citynext" class="bk">
				<option value="<s:property value="data.citynext" />">==��ѡ��==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR id="category" style="display:none">
		<TD align="right" class="column" width="15%">���</TD>
		<TD width="35%">
			<select name="category" class="bk">
				<option value="0" <%=("0".equals(category))?"selected":""%> >== �� ==</option>
				<option value="1" <%=("1".equals(category))?"selected":""%> >== �� ==</option>
				<option value="2" <%=("1".equals(category))?"selected":""%> >== δ֪ ==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertDevice();" 
				name="addButton" id="addButton" style="CURSOR:hand" style="display:" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>
</body>
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';

$(function(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var hardwareversion = $.trim($("select[@name='hardwareversion']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var citynext = $.trim($("select[@name='citynext']").val());
	change_select("vendor",vendorId,"1");
	change_select("deviceModel",deviceModelId,"1");
	change_select("hardwareversion",hardwareversion,"1");
	change_select("softwareversion",devicetypeId,"1");
	change_select("city",cityId,"1");
	change_select("cityid",citynext,"1");
	//������ͨ �������л������ܿ�
	if("hn_lt"==instAreaName)
	{
		$("tr[id*=category]").attr("style","display:");
		var category = $.trim($("select[@name='category']").val());
	}
});

function change_select(type,selectvalue,isChange){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
				$("select[@name='citynext']").html("<option value='-1'>==����ѡ������==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='citynext']").html("<option value='-1'>==����ѡ������==</option>");
				break;
			}
			$.post(url, {
				citynext : cityId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='citynext']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				if("2" == isChange){
					$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
					$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				}
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				if("2" == isChange){
					$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");		
				}
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
				break;
			}
			$.post(url,{
				gwShare_hardwareVersion : hardwareversion
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
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

function insertDevice(){
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var deviceMac = $("input[name=deviceMac]").val();
	var deviceSn = $("input[name=deviceSn]").val();
	var oui = $("input[name=oui]").val();
	var citynext = $("select[name=citynext]").val();
	var category = $("select[name=category]").val();
	if ("-1" == vendorId){
		alert("��ѡ���̣�");
		return;
	}
	if ("-1" == deviceModelId){
		alert("��ѡ���ͺţ�");
		return;
	}
	if ("-1" == source_devicetypeId){
		alert("��ѡ������汾��");
		return;
	}
	if ("-1" == cityId){
		alert("��ѡ�����أ�");
		return;
	}
	if ("" == deviceMac){
		alert("MAC��ַ����Ϊ��");
		return;
	}
	if(false == validateMac(deviceMac)){
		return;
	}
	if ("" == oui || oui.length >6){
		alert("oui����Ϊ���ҳ��Ȳ�����6λ");
		return;
	}
	if ("" == deviceSn || deviceSn.length <6){
		alert("�豸���кŲ���Ϊ���ҳ��Ȳ�С��6λ");
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!modifyDevice.action'/>";
	$.post(url,{
		deviceId : deviceId,
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		deviceTypeId : source_devicetypeId,
		cityId : cityId,
		citynext : citynext,
		deviceMac : deviceMac,
		oui : oui,
		deviceSn : deviceSn,
		category : category
	},function(ajax){
		if("2"== ajax){
			alert("�豸���кŻ�mac��ַ�Ѵ��ڣ�");
		}else if("1"== ajax){
			alert("�޸ĳɹ���");
			window.close();
		}else{
			alert("�޸�ʧ�ܣ�");
		}
	});
}

function validateMac(mac) {  
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC��ַ������зǷ��ַ�������");  
        return false;  
    }
    return true;
} 

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}
</SCRIPT>