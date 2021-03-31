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
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
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
/**
	function sendRequest2(method, url, object,sparam){
		request.open(method, url, true);
		request.onreadystatechange = function(){
			refreshPage(object);
			doSomething(sparam);
		};
		request.send(null);
	}
	**/
//---------------------------------------------------------------

function Init(){
	// ��ʼ������
	
	gwShare_queryChange("2");
	
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	 setValue();
	form.action = "<s:url value="/itms/resource/deviceTypeExpInfo!queryList.action"/>";
	//form.target = "dataForm";
	form.submit();
}

//���
function Add() {
	portNumber=1;
	
	var protocol2 = $("input[@name='protocol2']");
	var protocol1 = $("input[@name='protocol1']");
	var protocol0 = $("input[@name='protocol0']");
	 protocol2.attr("checked",false);
	 protocol1.attr("checked",false);
	 protocol0.attr("checked",false);


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
	
	 document.getElementsByName("is_check_add")[0].value=-2;
	 document.getElementsByName("rela_dev_type_add")[0].value=-1;
	
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
	
	document.getElementById("updateId").value="-1";
	
	document.getElementById("actLabel").innerHTML="���";
	
	
	
}

$(function(){
	Init();
	setValue();
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

function  addCurrentRow()
{
	
var trcomp="<tr bgcolor='#FFFFFF'><td bgcolor='#FFFFFF' align='right'>�˿���Ϣ</td><td>�˿�����: <input type='text' name='port_name' size=20 class=bk /> &nbsp;&nbsp;�˿�·����<input type='text' name='port_dir' size=30 class=bk/> &nbsp;&nbsp;</td>";
trcomp=trcomp+  "<td>�˿����ͣ�<select name='port_type' class=bk><option value='1'>����</option><option value='2'>WLAN</option><option value='3'>LAN</option></select> &nbsp;&nbsp;�˿�������<input type='text' name='port_desc' size=25 class=bk/> &nbsp;&nbsp;</td><td><input type='button' onclick='javascript:deleteCurrentRow(this)' class='jianbian' value=' ɾ ��' /></td>";
$("#allDatas tr:last-child").after(trcomp);
portNumber++;

}


function batchImport()
{
	$("input[@name='gwShare_queryType']").val("3");
	$("tr[@id='gwShare_tr31']").css("display","");
	$("tr[@id='gwShare_tr32']").css("display","");
	$("input[@name='gwShare_import']").css("display","");
	$("input[@name='gwShare_batch']").css("display","none");
	$("input[@name='gwShare_queryButton']").css("display","none");
	$("tr[@id='deviceType21']").css("display","none");
}

function batchSave()
{
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	var url = "<s:url value="/itms/resource/deviceTypeExpInfo!batchImport.action"/>";
    $.post(url,{
    	gwShare_fileName:gwShare_fileName
	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("�ɹ�") != -1)
		{
			// ��ͨ��ʽ�ύ
			var url = "<s:url value="/itms/resource/deviceTypeExpInfo!queryDeviceList.action?gw_type=1"/>";
			window.location.href = url;
		}
	})
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

</head>
<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">�豸�汾</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">��ѯʱ��Ϊ�豸�汾�����ʱ��</td>
				<td align="right"><input type="button"
							class=jianbian style="CURSOR: hand" 
							onclick="batchImport();"
							name="gwShare_batch" value="��������" />
				<input type='button' onclick='Add()'
					value=' �� �� ' class="jianbian" id='idAdd' /></td>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">�豸�汾��ѯ</th>
					</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr31" style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="../../gwms/share/FileUpload.jsp" height="20" width="100%">
							</iframe>
							<input type="hidden" id="gwShare_fileName" name=gwShare_fileName />
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="3" CLASS="green_foot">
					1����Ҫ������ļ���ʽ����Excel,��xls��ʽ ��
					 <br>
					2���ļ��ĵ�һ��Ϊ�����У����豸����	�豸�ͺš��ض��汾��Ӳ���汾������汾���Ƿ���ˡ��豸���͡����з�ʽ���ն˹��
					 <br>
					3���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
					</td>
				</tr>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD align="left" width="35%"><select name="vendor" class="bk"
							onchange="gwShare_change_select('deviceModel','-1')">
						</select></TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD width="35%"><select name="device_model" class="bk">
							<option value="-1">==��ѡ����==</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">Ӳ���汾</TD>
						<TD align="left" width="35%"><INPUT TYPE="text"
							NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
							color="#FF0000"></font></TD>
						<TD align="right" class=column width="15%">����汾</TD>
						<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">֧�ֺ�ƥ��</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">�Ƿ����</TD>
						<TD align="left" width="35%"><select name="is_check"
							class="bk">
							<option value="-2">==��ѡ��==</option>
							<option value="1">�������</option>
							<option value="-1">δ����</option>
						</select></TD>
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD width="35%">
						<s:select list="devTypeMap" name="rela_dev_type"
									headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">��ʼʱ��</TD>
						<TD align="left" width="35%"><lk:date id="startTime"
							name="startTime" type="all" /></TD>
						<TD align="right" class=column width="15%">����ʱ��</TD>
						<TD align="left" width="35%"><lk:date id="endTime"
							name="endTime" type="all" /></TD>
					</TR>

	                    <TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">���з�ʽ</TD>
						<TD align="left" width="35%"><select
							name="access_style_relay_id" class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1">ADSL</option>
							<option value="2">LAN</option>
							<option value="3">EPON����</option>
							<option value="4">GPON����</option>
						</select></TD>
						<TD align="right" class=column width="15%">�ն˹��</TD>
						<td width="35%">
						<s:select list="specList" name="spec_id" headerKey="-1"
								headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
								value="spec_id" cssClass="bk"></s:select>
						</td>
						</TD>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian style="CURSOR: hand"
							style="display: none" onclick="javascript:batchSave();"
							name="gwShare_import" value="����" />  <input type="button"
							onclick="javascript:queryDevice()" class=jianbian
							name="gwShare_queryButton" value=" �� ѯ " /> <input type="button"
							class=jianbian name="gwShare_reButto" value=" �� �� "
							onclick="javascript:queryReset();" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		<FORM id="addForm" name="addForm" target="" method="post" action="">
		<!-- ��Ӻͱ༭part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center"
			id="addTable" style="display: none">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"
					id="allDatas">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN
							id="DeviceTypeLabel"></SPAN>�豸����</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=3><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select> <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">�豸�ͺ�</TD>
						<TD colspan=3><select name="device_model_add" class="bk">
							<option value="-1">==��ѡ����==</option>
						</select> <font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ض��汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">Ӳ���汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�Ƿ����</TD>
						<TD colspan=3><select name="is_check_add" class="bk">
							<option value="-2" selected>==��ѡ��==</option>
							<option value="1">�������</option>
							<option value="-1">δ����</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=3>
						<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">���з�ʽ</TD>
						<TD colspan=3>
						<div id="typeNameList"></div>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�豸֧�ֵ�Э��</TD>
						<TD colspan=3><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">����SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�豸IP֧�ַ�ʽ</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="ipType" value="0">IPV4 
							<input type="radio" width="45%" name="ipType" value="1">IPV4��IPV6
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�����Ϊ�淶�汾</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="isNormal" value="1" onclick="changeToNormal();">��
							<input type="radio" width="45%" name="isNormal" value="0" checked="checked">��
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ն˹��</TD>
						<td width="35%">
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk"></s:select>
						</td>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
					<TR bgcolor="#FFFFFF">

						<TD align="right" CLASS=green_foot><INPUT TYPE="button"
							onclick="javascript:save()" value=" �� �� " class=jianbian>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add"> <input
							type="hidden" name="devicetype_id" value=""></TD>
					</TR>

				</TABLE>
				</TD>
			</TR>

		</TABLE>  
		<input type='hidden' id="updateId" value="-1" />
		<input type='hidden' name="gw_type" value="<s:property value="gw_type" />" />
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</TABLE>
</body>
<%@ include file="/foot.jsp"%>
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
	trimAll();
	
	var url = "<s:url value="/itms/resource/deviceTypeExpInfo!queryList.action"/>";
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var is_check = $("select[@name='is_check']").val();
	var rela_dev_type = $("select[@name='rela_dev_type']").val();
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/resource/deviceTypeExpInfo!queryList.action"/>";
	
	form.submit();
	
	showAddPart(false);
}

function toExcel()
{
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var is_check = $("select[@name='is_check']").val();
	var access_style_relay_id = $("select[@name='access_style_relay_id']").val();
	var rela_dev_type = $("select[@name='rela_dev_type']").val();
	var spec_id = $("select[@name='spec_id']").val();
    var url = "<s:url value='/itms/resource/deviceTypeExpInfo!exportBatchExcelList.action'/>?vendor="+vendor+"&device_model="+device_model+"&hard_version="+hard_version+"&soft_version="+soft_version+"&is_check="+is_check+"&rela_dev_type="+rela_dev_type+"&access_style_relay_id="+access_style_relay_id+"&spec_id="+spec_id;
   
    document.all("childFrm").src=url;
}

/**�ж��ܷ�����Ϊ�淶�汾*/
function changeToNormal(){
        var device_model = $("select[@name='device_model_add']").val();
        var url = '<s:url value="/itms/resource/deviceTypeExpInfo!isNormalVersion.action"/>';
        $.post(url,{
		device_model:device_model
    },function(ajax){
    	if("1"==ajax){
            alert("���豸�ͺ��Ѿ��й淶�汾"); 
            document.getElementsByName("isNormal")[1].checked="checked";
        }  
   	});
}

function doSomething(devicetype_id){
	var url = "updateDeviceType.jsp?devicetype_id=";
	document.all("childFrm").src = url + devicetype_id;
}
function CheckForm(){   
   temp =document.all("vendor_add").value;
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
	var url = "<s:url value="/itms/resource/deviceTypeExpInfo!addDevType.action"/>";
	var vendor = $("select[@name='vendor_add']").val();
	var device_model = $("select[@name='device_model_add']").val();
	var speversion = $("input[@name='speversion']").val();
	var hard_version = $("input[@name='hard_version_add']").val();
	var soft_version = $("input[@name='soft_version_add']").val();
	var is_check = $("select[@name='is_check_add']").val();
	var rela_dev_type = $("select[@name='rela_dev_type_add']").val();
    var deviceTypeId = $("input[@id='updateId']").val();
	var typeId = $("select[@name='type_id']").val();
	var protocol2 = $("input[@name='protocol2']");
	var protocol1 = $("input[@name='protocol1']");
	var protocol0 = $("input[@name='protocol0']");
	
	var ipType = $("input[@name='ipType']:checked").val();
	var isNormal = $("input[@name='isNormal']:checked").val();
	var gw_type = $("input[@name='gw_type']").val();
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
		specId:specId
		
	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("�ɹ�") != -1)
		{
			// ��ͨ��ʽ�ύ
			var form = document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeExpInfo!queryList.action"/>";
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

// ����ҳ��������������
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
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
	var url = "<s:url value="/itms/resource/deviceTypeExpInfo!getTypeNameList.action"/>";
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
   var url = "<s:url value="/itms/resource/deviceTypeExpInfo!getPortAndType.action"/>";
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

</SCRIPT>
</html>