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
   
   //ajax一个通用方法
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
	// 初始化厂家
	
	gwShare_queryChange("2");
	
	// 普通方式提交
	var form = document.getElementById("mainForm");
	 setValue();
	form.action = "<s:url value="/itms/resource/deviceTypeExpInfo!queryList.action"/>";
	//form.target = "dataForm";
	form.submit();
}

//添加
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
	
	document.getElementById("actLabel").innerHTML="添加";
	
	
	
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
	
var trcomp="<tr bgcolor='#FFFFFF'><td bgcolor='#FFFFFF' align='right'>端口信息</td><td>端口名称: <input type='text' name='port_name' size=20 class=bk /> &nbsp;&nbsp;端口路径：<input type='text' name='port_dir' size=30 class=bk/> &nbsp;&nbsp;</td>";
trcomp=trcomp+  "<td>端口类型：<select name='port_type' class=bk><option value='1'>语音</option><option value='2'>WLAN</option><option value='3'>LAN</option></select> &nbsp;&nbsp;端口描述：<input type='text' name='port_desc' size=25 class=bk/> &nbsp;&nbsp;</td><td><input type='button' onclick='javascript:deleteCurrentRow(this)' class='jianbian' value=' 删 除' /></td>";
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
		if(ajax.indexOf("成功") != -1)
		{
			// 普通方式提交
			var url = "<s:url value="/itms/resource/deviceTypeExpInfo!queryDeviceList.action?gw_type=1"/>";
			window.location.href = url;
		}
	})
}
		
function  deleteCurrentRow(obj)
{
  var isDelete=confirm("真的要删除吗？");  
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
				<div align="center" class="title_bigwhite">设备版本</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">查询时间为设备版本的添加时间</td>
				<td align="right"><input type="button"
							class=jianbian style="CURSOR: hand" 
							onclick="batchImport();"
							name="gwShare_batch" value="批量导入" />
				<input type='button' onclick='Add()'
					value=' 增 加 ' class="jianbian" id='idAdd' /></td>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">设备版本查询</th>
					</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr31" style="display:none">
					<td align="right" width="15%">提交文件</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="../../gwms/share/FileUpload.jsp" height="20" width="100%">
							</iframe>
							<input type="hidden" id="gwShare_fileName" name=gwShare_fileName />
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">注意事项</td>
					<td colspan="3" CLASS="green_foot">
					1、需要导入的文件格式限于Excel,即xls格式 。
					 <br>
					2、文件的第一行为标题行，即设备厂商	设备型号、特定版本、硬件版本、软件版本、是否审核、设备类型、上行方式、终端规格。
					 <br>
					3、文件行数不要太多，以免影响性能。
					</td>
				</tr>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">设备厂商</TD>
						<TD align="left" width="35%"><select name="vendor" class="bk"
							onchange="gwShare_change_select('deviceModel','-1')">
						</select></TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD width="35%"><select name="device_model" class="bk">
							<option value="-1">==请选择厂商==</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">硬件版本</TD>
						<TD align="left" width="35%"><INPUT TYPE="text"
							NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
							color="#FF0000"></font></TD>
						<TD align="right" class=column width="15%">软件版本</TD>
						<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">支持后匹配</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">是否审核</TD>
						<TD align="left" width="35%"><select name="is_check"
							class="bk">
							<option value="-2">==请选择==</option>
							<option value="1">经过审核</option>
							<option value="-1">未测试</option>
						</select></TD>
						<TD align="right" class=column width="15%">设备类型</TD>
						<TD width="35%">
						<s:select list="devTypeMap" name="rela_dev_type"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">开始时间</TD>
						<TD align="left" width="35%"><lk:date id="startTime"
							name="startTime" type="all" /></TD>
						<TD align="right" class=column width="15%">结束时间</TD>
						<TD align="left" width="35%"><lk:date id="endTime"
							name="endTime" type="all" /></TD>
					</TR>

	                    <TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class=column width="15%">上行方式</TD>
						<TD align="left" width="35%"><select
							name="access_style_relay_id" class="bk">
							<option value="-1">==请选择==</option>
							<option value="1">ADSL</option>
							<option value="2">LAN</option>
							<option value="3">EPON光纤</option>
							<option value="4">GPON光纤</option>
						</select></TD>
						<TD align="right" class=column width="15%">终端规格</TD>
						<td width="35%">
						<s:select list="specList" name="spec_id" headerKey="-1"
								headerValue="请选择终端规格" listKey="id" listValue="spec_name"
								value="spec_id" cssClass="bk"></s:select>
						</td>
						</TD>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian style="CURSOR: hand"
							style="display: none" onclick="javascript:batchSave();"
							name="gwShare_import" value="导入" />  <input type="button"
							onclick="javascript:queryDevice()" class=jianbian
							name="gwShare_queryButton" value=" 查 询 " /> <input type="button"
							class=jianbian name="gwShare_reButto" value=" 重 置 "
							onclick="javascript:queryReset();" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		<FORM id="addForm" name="addForm" target="" method="post" action="">
		<!-- 添加和编辑part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center"
			id="addTable" style="display: none">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"
					id="allDatas">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
							id="DeviceTypeLabel"></SPAN>设备类型</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">设备厂商</TD>
						<TD colspan=3><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select> <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">设备型号</TD>
						<TD colspan=3><select name="device_model_add" class="bk">
							<option value="-1">==请选择厂商==</option>
						</select> <font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">特定版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">硬件版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">软件版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">是否审核</TD>
						<TD colspan=3><select name="is_check_add" class="bk">
							<option value="-2" selected>==请选择==</option>
							<option value="1">经过审核</option>
							<option value="-1">未测试</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">设备类型</TD>
						<TD colspan=3>
						<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">上行方式</TD>
						<TD colspan=3>
						<div id="typeNameList"></div>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">设备支持的协议</TD>
						<TD colspan=3><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">软交换SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">设备IP支持方式</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="ipType" value="0">IPV4 
							<input type="radio" width="45%" name="ipType" value="1">IPV4和IPV6
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否设置为规范版本</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="isNormal" value="1" onclick="changeToNormal();">是
							<input type="radio" width="45%" name="isNormal" value="0" checked="checked">否
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">终端规格</TD>
						<td width="35%">
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="请选择终端规格" listKey="id" listValue="spec_name"
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
							onclick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
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





//查询
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
	// 普通方式提交
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

/**判断能否设置为规范版本*/
function changeToNormal(){
        var device_model = $("select[@name='device_model_add']").val();
        var url = '<s:url value="/itms/resource/deviceTypeExpInfo!isNormalVersion.action"/>';
        $.post(url,{
		device_model:device_model
    },function(ajax){
    	if("1"==ajax){
            alert("该设备型号已经有规范版本"); 
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
     alert("请选择厂商！");
     return false;
   }
   temp =document.all("device_model_add").value;
   if(temp=="-1" || temp=="")
   {
     alert("请选择设备型号！");
     return false;
   }
 
   temp = document.all("hard_version_add").value;
   if(temp=="")
   {
     alert("请填写硬件版本！");
     return false;
   }
   temp = document.all("soft_version_add").value;
   if(temp=="")
   {
     alert("请填写软件版本！");
     return false;
   }  

   temp = document.all("is_check_add").value;

   if(temp==""  || temp=="-2")
  {
     alert("请选择是否审核！");
   return false;
   }


   
   return true;
}

//更改设备型号
function change_model(type,selectvalue){
	switch (type){
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor_add']").val();
			if("-1"==vendorId){
				$("select[@name='device_model_add']").html("<option value='-1'>==请先选择设备厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model_add']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
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
            	 alert("端口名称不能为空");
            	 return;
             }
             if(port_dir[i].value==""){
            	 alert("端口路径不能为空");
            	 return;
             }
             if(port_type[i].value==""){
            	 alert("端口类型不能为空");
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
		if(ajax.indexOf("成功") != -1)
		{
			// 普通方式提交
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


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

// 某些字段不允许编辑
function disableLabel(tag)
{
	$("select[@name='vendor_add']").attr("disabled",tag);
	$("select[@name='device_model_add']").attr("disabled",tag);
	//增加判断，如果为空，则允许编辑 modify by zhangcong@2011-09-26
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

// 隐藏页面下面的添加区域
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

/** 工具方法 **/
/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
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
/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
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
/*Trim(string):去除字符串两边的空格*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//全部trim
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