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
<link href="<s:url value="/css/css_green.css"/>"  rel="stylesheet"  type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<%-- <script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script> --%>
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	change_select("vendor","-1");
	if("hn_lt"==instAreaName){
		intPlatformType();
	}
});

//����汾�ı䴥��
function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='-1' >��ѡ��ƽ̨����</option>";
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
					var optionValue = "<option value='-1' >��ѡ��ƽ̨����</option>";
					$("select[@id='platformId']").append(optionValue);
				}
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='-1' >��ѡ��ƽ̨����</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}
/*------------------------------------------------------------------------------
//������:		deviceSelect_change_select
//����  :	type 
	            vendor      �����豸����
	            deviceModel �����豸�ͺ�
	            devicetype  �����豸�汾
//����  :	����ҳ������̡��ͺż�����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function change_select(type,selectvalue){
	switch (type){
		case "platform":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
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

/*------------------------------------------------------------------------------
//������:		deviceSelect_parseMessage
//����  :	ajax 
            	������XXX$XXX#XXX$XXX
            field
            	��Ҫ���ص�jquery����
//����  :	����ajax���ز���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
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

function getTempList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var temp_id = $.trim($("select[@name='temp_id']").val());
	var hardwareversion = $.trim($("select[@name='hardwareversion']").val());
	var platformId = $.trim($("select[@name='platformId']").val());
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeTempList.action'/>";
	frm.submit();
}

function addTemp()
{
	var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeVersionAdd.jsp'/>?operateType=add";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
/*------------------------------------------------------------------------------
//������:		trim
//����  :	str �������ַ���
//����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
//����ֵ:		trim��str��
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
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
<form name="frm" id="frm" target="dataForm" method="post">

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">�����������汾��ѯ</td></tr>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
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
	<%if (LipossGlobals.inArea("hn_lt")) {%>
		<TR>	
			<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">����ѡ���豸�ͺ�</option>
				</select>
			</TD>	
			<TD align="right" class="title_2" width="15%">����ƽ̨</TD>
			<TD width="35%">
				<select name="platformId" id="platformId" class="bk">
					<option value="-1">==��ѡ��==</option>
				</select>
			</TD>
		</TR>	
	<%
		}
	%>
	<%if (LipossGlobals.inArea("xj_dx")) {%>
		<TR>	
			<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">����ѡ���豸�ͺ�</option>
				</select>
			</TD>	
			<TD align="right" class="title_2" width="15%"></TD>
			<TD width="35%"></TD>
		</TR>
	
	<%
		}
	%>
	
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">ԭ�汾</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>	
		<TD align="right" class="title_2" width="15%">Ŀ��汾</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr24" >
		<% if (!LipossGlobals.inArea("jx_dx")) { %>
		<TD align="right" class="title_2" width="15%">�����������</TD>
		<TD width="35%">
			<select name="temp_id" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="1" selected="selected">��ͨ�������</option>
				<option value="2">ҵ������������</option>
				<option value="3">��ҵ������������</option>
			</select>
		</TD>
		<% } %>
		<% if (LipossGlobals.inArea("hn_lt")) { %>
		<TD align="right" class="title_2" width="15%">�Ƿ���Ч</TD>
		<TD width="35%">
			<select name="valid" class="bk">
				<option value="-1">ȫ��</option>
				<option value="1">��Ч</option>
				<option value="0">ʧЧ</option>
			</select>
		</TD>
		<% }else{ %>
		<TD align="right" class="title_2" width="15%"></TD>
		<TD width="35%"></TD>
		<% } %>
	</TR>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<% if (LipossGlobals.inArea("jx_dx")|| LipossGlobals.inArea("xj_dx")) { %>
				<input type="button" onclick="javascript:addTemp();" 
				name="addTemp_Config" style="CURSOR:hand" style="display:" class="jianbian" value="�� ��">
				<%} %>
				<input type="button" onclick="javascript:getTempList();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" class="jianbian" value="�� ѯ">
				<input type="button" onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" class="jianbian" value="�� ��"> 
			</div>
		</td>
	</tr>
</TABLE>
</form>

<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>

</body>