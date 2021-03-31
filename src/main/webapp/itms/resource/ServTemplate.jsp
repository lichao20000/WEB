<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>

	<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<lk:res />
	<style type="text/css">
		table, th, td {
	    border: none;
	    border-collapse: inherit !important;
	}
	</style>
<SCRIPT LANGUAGE="JavaScript">


//-----------------ajax----------------------------------------
  var request = false;
  var portNumber=1;
  var addPortNumber=0;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
   
   //ajaxһ��ͨ�÷���
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        		object.innerHTML = request.responseText;
			} else{
				alert("status is " + request.status);
			}
		}
	}

function Init(){
	// ��ʼ������
	/* 
	gwShare_queryChange("2");
	var editDeviceType = $("input[@name='editDeviceType']").val(); */
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/servTemplate!queryList.action'/>";
	form.submit();
	dyniframesize();
}

//���
function Add() {
	var instArea = $('#instArea').val();
	portNumber=1;
	
	var protocol2 = $("input[@name='protocol2']");
	var protocol1 = $("input[@name='protocol1']");
	var protocol0 = $("input[@name='protocol0']");
	 protocol2.attr("checked",false);
	 protocol1.attr("checked",false);
	 protocol0.attr("checked",false);
	 
	 if("xj_dx"==instArea){
	 $("#is_multicast_add1").attr("checked",true);
	 }
	 
	 var port_name =document.getElementsByName("port_name");
	 var j=port_name.length;
	  
	 if(port_name.length>0){
		  for(var i=0;i<j;i++){
			  var tr=port_name[0].parentNode.parentNode;
        	  var tbody=tr.parentNode;
               tbody.removeChild(tr);   
         }
	 }

	document.all("DeviceTypeLabel").innerHTML = "";
	clearData();
	queryTypeName("");
	
	disableLabel(false);
	showAddPart(true);
	showCheckPart(false)
	
	 document.getElementsByName("is_check_add")[0].value=-2;
	 document.getElementsByName("rela_dev_type_add")[0].value=-1;
	 document.getElementsByName("ssid_instancenum")[0].value="";
	 document.getElementsByName("hvoip_port")[0].value="";
	 document.getElementsByName("hvoip_type")[0].value="";
	 document.getElementsByName("svoip_type")[0].value="";
}

function showChild(vendor_id,device_mode_id){
//	var oui = document.all(vendor_id).value;
//	var url = "getDeviceModel.jsp?oui=" + oui + "&device_mode=" + device_mode;
	var vendorId = document.all(vendor_id).value;
	var url = "getDeviceModel.jsp?vendor_id=" + vendorId + "&device_mode_id=" + device_mode_id;
	var divObj = document.getElementById("deviceModel");
	sendRequest("post",url,divObj);
}

function clearData()
{
	document.getElementsByName("vendor_add")[0].value=-1;
	change_model('deviceModel','-1');
	
	//window.setTimeout('callLater("'+device_model_id+'")',100);
	document.getElementsByName("speversion")[0].value="";
	document.getElementsByName("hard_version_add")[0].value="";	
	document.getElementsByName("soft_version_add")[0].value="";	
	document.getElementsByName("is_check_add")[0].value=0;	
	document.getElementsByName("rela_dev_type_add")[0].value=1;	
	document.getElementsByName("reason")[0].value="";	
	
	document.getElementById("updateId").value="-1";
	<%
	  if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	document.getElementsByName("device_version_type")[0].value="-1";	
	document.getElementsByName("wifi")[0].value="-1";
	
	document.getElementsByName("wifi_frequency")[0].value="-1";	
	document.getElementsByName("download_max_wifi")[0].value="";
	document.getElementsByName("gigabit_port")[0].value="-1";
	document.getElementsByName("gigabit_port_type")[0].value="-1";
	document.getElementsByName("download_max_lan")[0].value = "";
	document.getElementsByName("power")[0].value = "";
	document.getElementsByName("terminal_access_time")[0].value = "";
	<% 
	  }
  	%>
  	
  	<%
 	  if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
		document.getElementsByName("device_version_type")[0].value="-1";	
	<% 
	  }
	%>
	document.getElementById("actLabel").innerHTML="���";
	
	
	
}

$(function(){
	Init();
});
	
	
function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	//document.getElementById("startTime").value=year+"-"+month+"-"+day+" 00:00:00";
	//document.getElementById("endTime").value=year+"-"+month+"-"+day+" 23:59:59";
	//document.selectForm.startTime.value = year+"-"+month+"-"+day+" 00:00:00";
	//document.selectForm.endTime.value = year+"-"+month+"-"+day+" 23:59:59";
	document.getElementById("startTime").value="";
	document.getElementById("endTime").value="";
}

function addDevice()
{     
	var returnVal = window.open("<s:url value='/itms/resource/servTemplate!queryDetail4Add.action' />","","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
	/* if(typeof(returnVal)=='undefined'){
		console.log("==undefined");
		returnVal = window.returnValue;
		deviceResult(returnVal);
		//return;
	}else{
		console.log("!=undefined");
		deviceResult(returnVal);
	} */
}

function deviceResult(returnVal){
	Init();
}

function  deleteCurrentRow(obj)
{
  var isDelete=confirm("���Ҫɾ����");  
if(isDelete){
 var tr=obj.parentNode.parentNode;   
 var tbody=tr.parentNode;   
 tbody.removeChild(tr);   
 portNumber--;
}
}

</SCRIPT>
<script type="text/javascript">
$(function(){
	var instArea = $('#instArea').val();
	if("js_dx"==instArea){
		$('#reasonTr').show();
	}
	if("xj_dx"==instArea){
		$('#isMulticastTr').show();
	}
	if("jx_dx"==instArea)
		{
		$('#isEsurfing').show();
		$('#gbBroadband_add').show();
		}
})
</script>
</head>
<%-- <%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%> --%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">ģ�����</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">�༭ģ����Ϣ</td>
				<td align="right"></td>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=1px cellspacing=0 cellpadding=0 width="98%" align="center" style="border-bottom-width: 10px;border-top-width: 10px;border-left-width: 10px;border-right-width: 10px;border-color: red;">
			<tr>
				<td bgcolor=#999999>
					<table border=1px cellspacing=1 cellpadding=2 width="100%" style="border-bottom-width: 10px;border-top-width: 10px;border-left-width: 10px;border-right-width: 10px;border-color: red;"
						align="center">
						<tr>
							<input type="hidden" name="paraValue" id='paraValue' />
							<th colspan="4" id="gwShare_thTitle">ҵ��ģ���ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD class="column text" nowrap align="center" width="15%">ģ������:</TD>
							<TD colspan="1" class="column text" nowrap width="35%">
								<INPUT TYPE="text" NAME="name" class=bk maxlength=30 style="font-size: 12px;" size=20>
							</TD>
							<TD class="column text" nowrap align="center" width="15%">ҵ������:</TD>
							<TD colspan="1" class="column text" align="left">
								<select name="serv" id="serv" class=bk style="color: black;font-size: 12px">
									<option value="" style="color: black;font-size: 12px">ȫ ��</option>
									<option value="10" style="color: black;font-size: 12px">�� ��</option>
									<option value="11" style="color: black;font-size: 12px">IPTV</option>
									<option value="14" style="color: black;font-size: 12px">�� ��</option>
									<option value="69" style="color: black;font-size: 12px">TR069</option>
								</select>
							</TD>
						</TR>
						
						
						
						<%-- <tr style="border-color: red;border-width: 10px;border-collapse:inherit;">
							<input type="hidden" name="paraValue" id='paraValue' />
							<th colspan="4" id="gwShare_thTitle" style="border-collapse:inherit;">ҵ��ģ���ѯ</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="right" width="15%" style="border-collapse:inherit;">
								ģ������:
							</td>
							<td align="left" width="30%" style="border-collapse:inherit;">
								<INPUT TYPE="text" NAME="name"
									maxlength=60 class=bk size=20>
							</td>
	
							<td align="right" width="15%" style="border-collapse:inherit;">
								ҵ������:
							</td>
							<TD align="left" width="30%" style="border-collapse:inherit;"><select name="serv" id="serv" class="bk">
								<option value="">ȫ ��</option>
								<option value="10">�� ��</option>
								<option value="11">IPTV</option>
								<option value="14">�� ��</option>
								</select></TD>
						</tr> --%>
					
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" onclick="javascript:addDevice()" class=jianbian name="add_btn" value=" �� �� " />
								<input type="button" onclick="javascript:queryDevice()" class=jianbian value=" �� ѯ " /> 
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
	
	<!-- չʾ���part -->
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#FFFFFF id="idData"><iframe id="dataForm"
				name="dataForm" height="0" frameborder="0" scrolling="no"
				width="100%" src=""></iframe></TD>
		</TR>
	</TABLE>
		
	</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>

<!-- ��˲���form�ı��ύ���� -->
<script type="text/javascript">
	// ȫѡ����ѡ
	$("#checkAllVersions").click(function(){
		if($(this).attr("checked") == true){
			$("input[name='version']").attr("checked",true);
		}
		else {
			$("input[name='version']").attr("checked", false);
		}
	});

	// ������水ť
	$('#checkBut').click(function(){
		var relationType = $('#relationType').val();
		
		// Ŀ��汾
		var thisDeviceTypeId = $('#thisDeviceTypeId').val();
		
		// ѡ�е�ԭ�Ȱ汾
		var checkedObj = $('input:checkbox[name="version"]:checked');
		var checkedVersions = '';
		$('#sameAsOld').val('0');
		checkedObj.each(function() {
			var isCheck = this.value + ','; 
			checkedVersions = checkedVersions + isCheck;
			if(this.value == thisDeviceTypeId){
				$('#sameAsOld').val('1');
			}
		});
		if(checkedVersions == ''){
			alert('δ��ѡԭ�汾���빴ѡ');
			return; 
		}
		
		// ������ظ��ģ����������û���ѡ�Ƿ��������
		if($('#sameAsOld').val() == '1'){
			var x = confirm('��ԭ�汾��Ŀ��汾һ�����Ƿ��������');
			// ������������棬ֱ�ӷ���
			if(x == false){
				return;
			}
		}
		var editDeviceType = $("input[@name='editDeviceType']").val();
		var url = "<s:url value="/itms/resource/deviceTypeInfo!checkDeviceType.action"/>";
		$.post(url,{
			relationType:relationType,
			oldVersionDeviceTypeIds:checkedVersions,
			deviceTypeId:thisDeviceTypeId
		},function(ajax){
			if(ajax == "1"){
				alert("�ɹ�");
				// ��ͨ��ʽ�ύ
				var form = window.document.getElementById("mainForm");
				form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType"+editDeviceType;
				form.submit();
			}else{
				alert("ʧ��");
			}
		});
		
		
		
	});
</script>

<SCRIPT LANGUAGE="JavaScript">


function queryReset(){
	reset();
}

function  reset(){
	
	    document.mainForm.vendor.value="-1";
	    document.mainForm.device_model.value="-1";
		document.mainForm.hard_version.value="";
		document.mainForm.soft_version.value="";
		document.mainForm.startTime.value="";
		document.mainForm.endTime.value="";
		document.mainForm.is_check.value="-2";
		document.mainForm.rela_dev_type.value="-1"; 
		$("select[@name='access_style_relay_id']").val(-1);
		
}





//��ѯ
function queryDevice()
{
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/servTemplate!queryList.action'/>";
	form.submit();
}

function CheckForm(){   
   /* temp =document.all("vendor_add").value;
   if(temp=="-1" || temp=="")
   {
     alert("��ѡ���̣�");
     return false;
   }
   temp =document.all("device_model_add").value;
   if(temp=="-1" || temp=="")
   {
     alert("��ѡ���豸�ͺţ�");
     return false;
   }
 
   temp = document.all("hard_version_add").value;
   if(temp=="")
   {
     alert("����дӲ���汾��");
     return false;
   }
   temp = document.all("soft_version_add").value;
   if(temp=="")
   {
     alert("����д����汾��");
     return false;
   }  

   temp = document.all("is_check_add").value;

   if(temp==""  || temp=="-2")
   {
     	alert("��ѡ���Ƿ���ˣ�");
   		return false;
   }

   var instArea = $('#instArea').val();
	if("js_dx"==instArea){
		var reason = $("textarea[@name='reason']").val();
	   if(reason == '' || reason == null){
		   alert("��ѡ�񶨰�ԭ��");
	  		return false;
	   }
	} */
   return true;
}

//�����豸�ͺ�
function change_model(type,selectvalue){
	switch (type){
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor_add']").val();
			if("-1"==vendorId){
				$("select[@name='device_model_add']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model_add']"),selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

function save()
{
	trimAll();
	if(!CheckForm())
		return;
	var url = "<s:url value="/itms/resource/deviceTypeInfo!addDevType.action"/>";
	var vendor = $("select[@name='vendor_add']").val();
	var device_model = $("select[@name='device_model_add']").val();
	var speversion = $("input[@name='speversion']").val();
	var hard_version = $("input[@name='hard_version_add']").val();
	var soft_version = $("input[@name='soft_version_add']").val();
	var is_check = $("select[@name='is_check_add']").val();
	var rela_dev_type = $("select[@name='rela_dev_type_add']").val();
	if(rela_dev_type=="-1"||rela_dev_type=="")
			{
		alert("��ѡ���豸����");
		return false;
			}
  	var type_id = $("select[@name='type_id']").val();
  	if(type_id=="-1"||rela_dev_type=="")
  	{
    	alert("��ѡ�����з�ʽ");
    	return false;
  	}
    var deviceTypeId = $("input[@id='updateId']").val();
	var typeId = $("select[@name='type_id']").val();
	var protocol2 = $("input[@name='protocol2']");
	var protocol1 = $("input[@name='protocol1']");
	var protocol0 = $("input[@name='protocol0']");
    var is_esurfing=$("input[@name='is_esurfing']:checked").val();
    var gbBroadband_add=$("input[@name='gbBroadband_add']:checked").val();
	var ipType = $("input[@name='ipType']:checked").val();
	var isNormal = $("input[@name='isNormal']:checked").val();
	var gw_type = $("input[@name='gw_type']").val();
	var editDeviceType = $("input[@name='editDeviceType']").val();
	var specId = $("select[@name='specId']").val();
	
    var servertype="";

    if (protocol2.attr("checked") == true) {
		servertype=servertype+"2";
	} 
	if (protocol1.attr("checked") == true) {
		if(servertype == ""){
			servertype="1";
		}
		else{
			servertype=servertype+"@1";
		}
		
	} 
	if (protocol0.attr("checked") == true) {
		if(servertype == ""){
			servertype="0";
		}
		else{
			servertype=servertype+"@0";
		}
		
	} 
	
  var port_name =document.getElementsByName("port_name");
     var portInfo="";
     var allPort="";
	 var instArea = $('#instArea').val();
     if(port_name.length>0)
         {
    	 var port_dir =document.getElementsByName("port_dir");
         var port_type =document.getElementsByName("port_type");
         var port_desc =document.getElementsByName("port_desc");
       
         
         for(var i=0;i<port_name.length;i++){
             if(port_name[i].value==""){
            	 alert("�˿����Ʋ���Ϊ��");
            	 return;
             }
             if(port_dir[i].value==""){
            	 alert("�˿�·������Ϊ��");
            	 return;
             }
             if(port_type[i].value==""){
            	 alert("�˿����Ͳ���Ϊ��");
            	 return;
             }
             portInfo=portInfo+ port_name[i].value+"@";
        	 portInfo=portInfo+port_dir[i].value+"@";
        	 portInfo=portInfo+port_type[i].value+"@";
        	 portInfo=portInfo+port_desc[i].value+"#";
        }
     }
     var machineConfig = "";
     if("gs_dx"==instArea){
    	 machineConfig = "1";
     }
     else
     {
     	 machineConfig = $("input[@name='machineConfig_add']:checked").val();
     }
     var mbBroadband = $("input[@name='mbBroadband_add']:checked").val();
     var startOpenDate = $("input[@name='startOpenDate_add']").val();
   	 //�Ƿ�֧��awifi
   	 var is_awifi = "";
	 if("js_dx"==instArea){
     	is_awifi = $("input[@name='is_awifi_add']:checked").val();
	 }
     var is_QOE = $("input[@name='is_qoe_add']:checked").val();
     //�Ƿ�֧���鲥
     var is_multicast = "";
	 if("xj_dx"==instArea){
     	is_multicast = $("input[@name='is_multicast_add']:checked").val();
	 }
	 var is_speedtest="";
    
   	 //�Ƿ�֧�ַ������
	 if("js_dx"==instArea || "jx_dx"==instArea){
		 is_speedtest=$("input[@name='is_speedtest']:checked").val();
	 }
	 var is_newVersion="";
   	 //�Ƿ�֧����������,�Ƿ������������°汾
   	 if("jx_dx"==instArea)
   		 {
   			is_esurfing=$("input[@name='is_esurfing']:checked").val();
   			is_newVersion=$("input[@name='is_newVersion']:checked").val();
   		 }
     // ����ԭ��
     var reason = $("textarea[@name='reason']").val();
     var device_version_type = "";
		var wifi = "";
		var wifi_frequency = "";
		var gigabit_port = "";
		var gigabit_port_type = "";
		var download_max_wifi = "";
		var download_max_lan = "";
		var power = "";
		var terminal_access_time = ""; 
		var version_feature = "";
		var is_security_plugin = 0;
		var security_plugin_type = 0;
		
     if("hb_lt"==instArea){
    	 gigabit_port = $("select[@name='gigabit_port']").val();
    	 version_feature = $("select[@name='version_feature']").val();
     }
 	 if("xj_dx"==instArea){
 		 device_version_type = $("select[@name='device_version_type']").val();
 		 wifi = $("select[@name='wifi']").val();
 		 wifi_frequency = $("select[@name='wifi_frequency']").val();
 		 var wifi_ability = $("input[@name='wifi_ability']:checked").val();
 		 gigabit_port = $("select[@name='gigabit_port']").val();
 		 gigabit_port_type = $("select[@name='gigabit_port_type']").val();
 		 download_max_wifi = $("input[@name='download_max_wifi']").val();
 		 download_max_lan = $("input[@name='download_max_lan']").val();
 		 power = $("input[@name='power']").val();
 		 terminal_access_time = $("input[@name='terminal_access_time']").val();
 		 is_security_plugin = $("select[@name='is_security_plugin']").val();
 		 if(is_security_plugin == null || is_security_plugin == "" || is_security_plugin == -1){
 			is_security_plugin = 0;
 		 }
 		 security_plugin_type = $("select[name='security_plugin_type']").val();
 		 if(security_plugin_type == null || security_plugin_type == "" || security_plugin_type == -1){
 			security_plugin_type = 0;
  		 }
 	 }
 	if("jl_dx"==instArea || "sd_dx"==instArea || "gs_dx"==instArea){
		 device_version_type = $("select[@name='device_version_type']").val();
	 }
 	
 	
 	var ssid_instancenum="";
  	 //wifiҵ���·�ͨ��ʵ����
  	var hvoip_port="";
  	 //H248�����ʶ��
  	 if("gs_dx"==instArea)
  		 {
  			ssid_instancenum=$("select[@name='ssid_instancenum']").val();
  			hvoip_port=$("select[@name='hvoip_port']").val();
  		 }
  	 
  	var hvoip_type="";
 	 //H248�����·�����
 	var svoip_type="";
 	 //sip�����·�����
 	 //sx_dx
 	 if("sx_dx"==instArea)
 		 {
 			hvoip_type=$("select[@name='hvoip_type']").val();
 			svoip_type=$("select[@name='svoip_type']").val();
 		 }
  	
  	 
  	 
  	 
 	<%
	  if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) || 
			  "nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	var wifi_ability = $("input[@name='wifi_ability']:checked").val();
	<% }%>
	$.post(url,{
		deviceTypeId:deviceTypeId,
		vendor:vendor,
		device_model:device_model,
		hard_version:encodeURIComponent(hard_version),
		speversion:encodeURIComponent(speversion),
		soft_version:encodeURIComponent(soft_version),
		is_check:is_check,
		typeId:typeId,
		rela_dev_type:rela_dev_type,
		servertype:servertype,
		portInfo:encodeURIComponent(portInfo),
		ipType : ipType,
		isNormal:isNormal,
		gw_type:gw_type,
		editDeviceType:editDeviceType,
		mbBroadband:mbBroadband,
		startOpenDate:startOpenDate,
		machineConfig:machineConfig,
		is_awifi:is_awifi,
		is_QOE:is_QOE,
		is_multicast:is_multicast,
		specId:specId,
		is_speedtest:is_speedtest,
		reason:reason,
		is_esurfing:is_esurfing,
		gbBroadband:gbBroadband_add,
		device_version_type : device_version_type,
		wifi : wifi,
		wifi_frequency : wifi_frequency,
		download_max_wifi : download_max_wifi,
		gigabit_port : gigabit_port,
		gigabit_port_type : gigabit_port_type,
		download_max_lan : download_max_lan,
		power : power,
		<%
		  if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		%>
		ssid_instancenum:ssid_instancenum,
		hvoip_port:hvoip_port,
		<%}%>
		//sx_dx
		<%
		  if("sx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		%>
		hvoip_type:hvoip_type,
		svoip_type:svoip_type,
		<%}%>
		<%if( "nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		wifi_ability: wifi_ability,
		<%}%>
		<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%> 
		is_security_plugin : is_security_plugin,
		security_plugin_type : security_plugin_type,
		wifi_ability : wifi_ability,
		<%}%>
		<%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		version_feature: version_feature,
		<%}%>
		<%
		  if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		%>
		terminal_access_time : terminal_access_time,
		is_newVersion : is_newVersion,
		wifi_ability: wifi_ability
		<% }
		else{%>
		terminal_access_time : terminal_access_time
		<% }%>
	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("�ɹ�") != -1)
		{
			// ��ͨ��ʽ�ύ
			var form = document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>?editDeviceType="+editDeviceType;
			//reset();
			 var port_name =document.getElementsByName("port_name");
			 var j=port_name.length;
			  
			 if(port_name.length>0){
				  for(var i=0;i<j;i++){
					  var tr=port_name[0].parentNode.parentNode;
	            	  var tbody=tr.parentNode;
		               tbody.removeChild(tr);   
		         }
			 }
			 
			 form.submit();

		}
	});
	 
	showAddPart(false);
}

function saveEditDeviceType(){
	var rela_dev_type = $("select[@name='rela_dev_type_edit']").val();
	   if(rela_dev_type=="-1" || rela_dev_type=="")
	   {
	     alert("��ѡ���豸�ͺţ�");
	     return false;
	   }
    var deviceTypeId = $("input[@id='updateId']").val();
	var gw_type = $("input[@name='gw_type']").val();
	var editDeviceType = $("input[@name='editDeviceType']").val();
	var url = "<s:url value='/itms/resource/deviceTypeInfo!updateDeviceType.action'/>";
	$.post(url,{
		gw_type:gw_type,
		editDeviceType:editDeviceType,
		deviceTypeId:deviceTypeId,
		rela_dev_type:rela_dev_type
	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("�ɹ�") != -1)
		{
			var form = document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
			form.submit();
		}
	});
	showEditDeviceTypePart(false);
}


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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}

function disableDeviceType(tag){
	$("select[@name='rela_dev_type_add']").attr("disabled",tag);
}

// ĳЩ�ֶβ�����༭
function disableLabel(tag)
{
	$("select[@name='vendor_add']").attr("disabled",tag);
	$("select[@name='device_model_add']").attr("disabled",tag);
	//�����жϣ����Ϊ�գ�������༭ modify by zhangcong@2011-09-26
	//if(trim(document.all("speversion").value) == '')
	//{
		$("input[@name='speversion']").attr("disabled",false);
	//}else
	//{
	//	$("input[@name='speversion']").attr("disabled",true);
	//}
	$("input[@name='hard_version_add']").attr("disabled",tag);
	$("input[@name='soft_version_add']").attr("disabled",tag);

	$("input[@name='speversion']").attr("disabled",tag);
}

//����ҳ������ı༭�豸��������
function showEditDeviceTypePart(tag)
{
	if(tag){
		$("table[@id='editDeviceTypeTable']").show();
	}else{
		$("table[@id='editDeviceTypeTable']").hide();
	}
}

// ����ҳ��������������
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}
// ����ҳ��������������
function showCheckPart(tag)
{
	if(tag)
		$("#checkTable").show();
	else
		$("#checkTable").hide();
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/** ���߷��� **/
/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//ȫ��trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}

function queryTypeName(typeId){
	var url = "<s:url value="/itms/resource/deviceTypeInfo!getTypeNameList.action"/>";
	$.post(url,{
		typeId:typeId
    },function(mesg){
    	document.getElementById("typeNameList").innerHTML = mesg;
   	});
}

function getPortAndType (deviceTypeId){
	 var port_name =document.getElementsByName("port_name");
	 var j=port_name.length;
	  
	 if(port_name.length>0){
		  for(var i=0;i<j;i++){
			  var tr=port_name[0].parentNode.parentNode;
        	  var tbody=tr.parentNode;
               tbody.removeChild(tr);   
             var alength=document.getElementById("allDatas").rows.length; 
			
		 }
	 }
   var url = "<s:url value="/itms/resource/deviceTypeInfo!getPortAndType.action"/>";
	$.post(url,{
		deviceTypeId:deviceTypeId
    },function(mesg){
    	
        var portArray=mesg.split("#");
    	port=(portArray.length-1)/4;
        portNumber=1;
    	
    	var temp=mesg.split("&");
    	var servType=temp[1].split(",");
    	
    	var protocol2 = $("input[@name='protocol2']");
    	var protocol1 = $("input[@name='protocol1']");
    	var protocol0 = $("input[@name='protocol0']");

    	protocol2.attr("checked",false)
    	protocol1.attr("checked",false)
    	protocol0.attr("checked",false)
    	for(var j=0;j<servType.length;j++){
             if(protocol2.val()==servType[j]){
            	 protocol2.attr("checked",true)
             }
             if(protocol1.val()==servType[j]){
            	 protocol1.attr("checked",true)
             }
             if(protocol0.val()==servType[j]){
            	 protocol0.attr("checked",true)
             }
         }

            var portInfo=temp[0].split("#");
            var port_name =document.getElementsByName("port_name");
            var port_dir =document.getElementsByName("port_dir");
            var port_type =document.getElementsByName("port_type");
            var port_desc =document.getElementsByName("port_desc");
             for (var m=0;m<port;m++){
            	 port_name[m].value=portInfo[m];
            	 port_dir[m].value=portInfo[m+port];
            	 port_type[m].value=portInfo[m+port+port];
            	 port_desc[m].value=portInfo[m+port+port+port];
             }
    	
             document.getElementById("updateId").value=deviceTypeId;
   	});
}

function disableSecurityPlugin(){
	var selVal = $("select[name='is_security_plugin']").val();
	if(selVal == -1 || selVal == 0){
		$("select[name='security_plugin_type']").attr("disabled","disabled");
	}else{
		$("select[name='security_plugin_type']").removeAttr("disabled");
	}
}
</SCRIPT>
</html>