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
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html xmlns:s="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<lk:res />

<%
    String isJs = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

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
	var editDeviceType = $("input[@name='editDeviceType']").val();
	// 普通方式提交
	var form = document.getElementById("mainForm");
	setValue();
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
	//form.target = "dataForm";
	form.submit();
}

//添加
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
	 
	 if("gs_dx"==instArea){
		 disableDeviceType("");
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
	 //甘肃特有，修复之前其他人的bug
	 if("gs_dx"==instArea)
	 {
		 document.getElementsByName("ssid_instancenum")[0].value=""; 
		 document.getElementsByName("hvoip_port")[0].value="";
	 }
	 if("sx_dx"==instArea)
	 {
		 document.getElementsByName("hvoip_type")[0].value="";
		 document.getElementsByName("svoip_type")[0].value="";
	 }
	 if("sx_dx"==instArea || "gs_dx" ==  instArea || "xj_dx" == instArea
			 || "jl_dx" ==instArea  || "sd_dx" == instArea || "ah_lt" == instArea)
	 {
		 document.getElementsByName("device_version_type")[0].value="0";
	 }
	 if("nmg_dx"==instArea)
	 {
		 document.getElementsByName("device_version_type")[0].value="-1";
		 document.getElementsByName("gigabitNum")[0].value=""; //千兆口数量
		 document.getElementsByName("mbitNum")[0].value="";    //百兆口数量
		 document.getElementsByName("voipNum")[0].value="";    //语音口数量
		 document.getElementsByName("wifi")[0].value="-1";     //光猫是否有wifi
		 document.getElementsByName("is_wifi_double")[0].value="-1"; //是否支持双频
		 document.getElementsByName("fusion_ability")[0].value="";   //融合功能
		 document.getElementsByName("terminal_access_method")[0].value=""; //综合终端接入方式
		 document.getElementsByName("devMaxSpeed")[0].value="";      //光猫最大支持上网速率
	 }
	 
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

	<%
      if("sd_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
    %>
        document.getElementsByName("isSupSpeedTest")[0].value="-1";
    <%
      }
    %>

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
<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%>
<body>
<style>
table tr td input[type="text"],
table tr td select{
	width: 225px
}
.mytable{
	border-top: solid 1px #999;
	border-right: solid 1px #999;
}
.mytable th, .mytable td{
 	border-bottom: solid 1px #999;
 	border-left: solid 1px #999;
}

</style>
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
				<div align="center" class="title_bigwhite">设备版本</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">查询时间为设备版本的添加时间</td>
				<ms:inArea areaCode="sx_lt" notInMode="true">
					<td align="right"><input type='button' onclick='Add()'
											 value=' 增 加 ' class="jianbian" id='idAdd' /></td>
				</ms:inArea>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
					<table class="mytable" width="100%"
						align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">设备版本查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">设备厂商</TD>
							<TD align="left" width="35%"><select name="vendor" class="bk"
								onchange="gwShare_change_select('deviceModel','-1')">
							</select></TD>
							<TD align="right" class=column width="15%">设备型号</TD>
							<TD width="35%"><select name="device_model" class="bk">
								<option value="-1">==请选择厂商==</option>
							</select></TD>
						</TR>
						<%String InstAreaCity=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">硬件版本</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000"></font></TD>
							<TD align="right" class=column width="15%">软件版本</TD>
							
							<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
								maxlength=30 class=bk size=20>&nbsp;<% if(!"sx_lt".equals(InstAreaCity)){%><font color="#FF0000">支持后匹配</font><%}%>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">是否审核</TD>
							<TD align="left" width="35%"><select name="is_check"
								class="bk">
								<option value="-2">==请选择==</option>
								<option value="1">经过审核</option>
								<option value="-1">未审核</option>
							</select></TD>
							<%
							if(!"ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
							%>
							<TD align="right" class=column width="15%">设备类型</TD>
							<TD width="35%">
							<s:select list="devTypeMap" name="rela_dev_type"
										headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
										listValue="type_name" cssClass="bk"></s:select>
							</TD>
							<%}else {%>
							<TD align="right" class=column width="15%">设备版本类型</TD>
							<TD width="35%">
								<s:select list="devVersionTypeMap" name="deviceVersionType" listKey="value"
										  listValue="text" cssClass="bk"></s:select>
							</TD>
							<%} %>
						</TR>
		                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">上行方式</TD>
							<TD align="left" width="35%"><select
								name="access_style_relay_id" class="bk">
								<option value="-1">==请选择==</option>
								<option value="1">ADSL</option>
								<option value="2">LAN</option>
								<option value="3">EPON光纤</option>
								<option value="4">GPON光纤</option>
								<%
								if("sx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
								%>
									<option value="5">10G-EPON</option>
									<option value="6">XG-PON</option>
								<%} else if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
									<option value="5">10G-EP0N</option>
									<option value="6">XG-PON</option>
									<option value="99">其它</option>
								<%}%>
							</select></TD>
							<TD align="right" class=column width="15%">终端规格</TD>
							<td width="35%">
							<s:select list="specList" name="spec_id" headerKey="-1"
									headerValue="请选择终端规格" listKey="id" listValue="spec_name"
									value="spec_id" cssClass="bk"></s:select>
							</td>
						</TR>


						<ms:inArea areaCode="sx_lt" notInMode="false">
							<select name="machineConfig" class="bk" style="display:none">
								<option value="-1">==请选择==</option>
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
						</ms:inArea>

						<ms:inArea areaCode="sx_lt" notInMode="true">
							<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD align="right" class=column width="15%">是否支持机顶盒零配置</TD>
								<TD align="left" width="35%">
									<select name="machineConfig" class="bk">
										<option value="-1">==请选择==</option>
										<option value="1">是</option>
										<option value="2">否</option>
									</select>
								</TD>
								<TD align="right" class=column width="15%">是否支持IPV6</TD>
								<TD align="left" width="35%">
									<select name="ipvsix" class="bk">
										<option value="-1">==请选择==</option>
										<option value="1">是</option>
										<option value="2">否</option>
									</select>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD class=column width="15%" align='right'>版本定版开始时间</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk >
									<img name="shortDateimg"
										 onClick="WdatePicker({el:document.mainForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										 src="../../images/dateButton.png" width="15" height="12"
										 border="0" alt="选择">
								</TD>
								<TD class=column width="15%" align='right'>版本定版结束时间</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk >
									<img name="shortDateimg"
										 onClick="WdatePicker({el:document.mainForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										 src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择">
								</TD>
							</TR>
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="false">
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">是否支持千兆宽带</TD>
							<TD align="left" width="35%">
								<select name="mbBroadband" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1">是</option>
									<option value="2">否</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">是否支持IPV6</TD>
							<TD align="left" width="35%">
								<select name="ipvsix" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1">是</option>
									<option value="2">否</option>
								</select>
							</TD>
						</TR>
						</ms:inArea>
						
						<% if(!"gs_dx".equals(InstAreaCity) && !"sx_lt".equals(InstAreaCity)){%>
						  <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						       <% if(!"sd_dx".equals(InstAreaCity)){%>
							  <TD align="right" class=column width="15%">是否支持百兆宽带</TD>
							  <% } else {%>
							  <TD align="right" class=column width="15%">是否支持千兆宽带</TD>
							  <% }%>
							  <TD align="left" width="35%">
									<select name="mbBroadband" class="bk">
										<option value="-1">==请选择==</option>
										<option value="1">是</option>
										<option value="2">否</option>
									</select>
							  </TD>
							  <% if("sd_dx".equals(InstAreaCity)){%>
                              <TD align="right" class=column width="15%">是否支持测速</TD>
                              <TD align="left" width="35%">
                                    <select name="isSupSpeedTest_Query" class="bk">
                                        <option value="-1">==请选择==</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                              </TD>
                              <%} else {%>
	                              <TD align="right" class=column width="15%"></TD>
	                              <TD align="left" width="35%"></TD>
                              <%} %>
						</TR>
						<%} %>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">开始时间</TD>
							<TD align="left" width="35%"><lk:date id="startTime"
								name="startTime" type="all" /></TD>
							<TD align="right" class=column width="15%">结束时间</TD>
							<TD align="left" width="35%"><lk:date id="endTime"
								name="endTime" type="all" /></TD>
						</TR>




						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian style="CURSOR: hand;display: none"
									onclick="javascript:gwShare_queryChange('1');"
									name="gwShare_jiadan" value="简单查询" /> 
								<input type="button"
									class=jianbian style="CURSOR: hand;display:none "
									onclick="javascript:gwShare_queryChange('2');"
									name="gwShare_gaoji" value="高级查询" /> 
								<input type="button"
									class=jianbian style="CURSOR: hand;display: none"
									onclick="javascript:gwShare_queryChange('3');"
									name="gwShare_import" value="导入查询" />
								<ms:inArea areaCode="sx_lt" notInMode="false">
									<input type='button' onclick='Add()'
													 value=' 增 加 ' class="jianbian" id='idAdd' />
								</ms:inArea>
								<% if("sd_dx".equals(InstAreaCity)){%>
								    <input type="button"
                                        onclick="javascript:exportExcel()" class=jianbian
                                        name="gwShare_queryButton" value=" 导 出 " />
								 <%} %>
								<input type="button"
									onclick="javascript:queryDevice()" class=jianbian
									name="gwShare_queryButton" value=" 查 询 " /> 
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" 重 置 "
									onclick="javascript:queryReset();" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm" style="background-color: white"
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
				<TABLE  class="mytable" width="100%"
					id="allDatas">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
							id="DeviceTypeLabel"></SPAN>设备类型</TH>
					</TR>
					<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">设备厂商</TD>
						<TD colspan=1><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">千兆口数量</TD>
						<TD colspan=1>
						<input name="gigabitNum" id = "gigabitNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">设备厂商</TD>
						<TD colspan=3><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">设备型号</TD>
						<TD colspan=1><select name="device_model_add" class="bk">
							<option value="-1">==请选择厂商==</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">百兆口数量</TD>
						<TD colspan=1>
						<input name="mbitNum" id = "mbitNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">设备型号</TD>
						<TD colspan=3><select name="device_model_add" class="bk">
							<option value="-1">==请选择厂商==</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">特定版本</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">语音口数量</TD>
						<TD colspan=1>
						<input name="voipNum" id = "voipNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">特定版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">硬件版本</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">最大支持速率</TD> <!-- 光猫最大支持上网速率 -->
						<TD colspan=1>
						<input name="devMaxSpeed" id = "devMaxSpeed" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">硬件版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">软件版本</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">是否支持wifi</TD> <!-- 光猫是否有wifi -->
						<TD colspan=1><select name="wifi" class="bk">
							<option value="-1" selected>==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">软件版本</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">是否审核</TD>
						<TD colspan=1><select name="is_check_add" class="bk">
							<option value="-2" selected>==请选择==</option>
							<option value="1">经过审核</option>
							<option value="-1">未审核</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">wifi是否双频</TD> <!-- 光猫是否有wifi -->
						<TD colspan=1><select name="is_wifi_double" class="bk">
							<option value="-1" selected>==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">是否审核</TD>
						<TD colspan=3><select name="is_check_add" class="bk">
							<option value="-2" selected>==请选择==</option>
							<option value="1">经过审核</option>
							<option value="-1">未审核</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">设备类型</TD>
						<TD colspan=1>
						<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
						<TD class=column align="right">设备版本类型</TD> <!-- 光猫是否有wifi -->
						<TD colspan=1><select name="device_version_type" class="bk">
							<option value="-1" selected>==请选择==</option>
							<option value="1">E8-C</option>
							<option value="2">PON融合</option>
							<option value="3">10GPON</option>
							<option value="4">政企网关</option>
							<option value="5">天翼网关1.0</option>
							<option value="6">天翼网关2.0</option>
							<option value="7">天翼网关3.0</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} else {%>
					<tr style="background-color: white">
						<TD class=column align="right">设备类型</TD>
						<TD colspan=3>
							<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>&ensp;<font color="#FF0000">*</font>
						</TD>
					</tr>
						
					<%} %>
					<%if("ah_lt".equals(InstArea)){%>
					<tr style="background-color: white">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3>
							<s:select list="devVersionTypeMap" name="device_version_type" listKey="value"
									  listValue="text" cssClass="bk"></s:select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</tr>
					<%}%>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">上行方式</TD>
						<TD colspan=1>
						<div id="typeNameList"></div>
						</TD>
						<TD class=column align="right">融合功能</TD> 
						<TD colspan=1>
						<input name="fusion_ability" id = "fusion_ability" value="" />
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">上行方式</TD>
						<TD colspan=3>
						<span id="typeNameList"></span>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">设备支持的协议</TD>
						<TD colspan=1><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">软交换SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
						<TD class=column align="right">融合终端接入方式</TD> 
						<TD colspan=1>
						<input name="terminal_access_method" id = "terminal_access_method" value="" />
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">设备支持的协议</TD>
						<TD colspan=3><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">软交换SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">设备IP支持方式</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="ipType" value="0">IPV4 
							<input type="radio" width="23%" name="ipType" value="1">IPV4和IPV6
							<input type="radio" width="22%" name="ipType" value="2">DS-Lite
							<input type="radio" width="23%" name="ipType" value="3">LAFT6
							<input type="radio" width="23%" name="ipType" value="4">纯IPV6
						</TD>
					</TR>
					
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi能力</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >无
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否为最新版本</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="isNormal" value="1">是
							<input type="radio" width="45%" name="isNormal" value="0" checked="checked">否
						</TD>
					</TR>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">终端规格</TD>
						<td colspan=3>
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="请选择终端规格" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk"></s:select>
						</td>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">终端规格</TD>
						<td width="35%">
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="请选择终端规格" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk"></s:select>
						</td>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
					<%if("sd_dx".equals(InstArea) || "gs_dx".equals(InstArea) || "sx_lt".equals(InstArea)){%>
					    <TD class=column width="10%" align="right" id="3">是否支持千兆宽带</TD>
					<%}else{ %>
						<TD class=column width="10%" align="right" id="3">是否支持百兆宽带</TD>
					<%} %>
						<TD colspan=3>
							<input type="radio" width="22%" name="mbBroadband_add" value="1">是
							<input type="radio" width="23%" name="mbBroadband_add" value="2">否
						</TD>
					</TR>
					<!-- 2020/11/16 新疆新增是否支持云网超宽 -->
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否支持云网超宽</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="iscloudnet" value="1">是
							<input type="radio" width="23%" name="iscloudnet" value="2">否
						</TD>
					</TR>
					<%} %>
                    <ms:inArea areaCode="sx_lt" notInMode="true">
                    	<%if("nmg_dx".equals(InstArea)){%>
                        <TR bgcolor="#FFFFFF">
                            <TD class=column width="10%" align="right" id="3">定版时间</TD>
                            <TD colspan=3><input type="text" name="startOpenDate_add"
                                                   readonly class=bk> <img
                                    name="shortDateimg"
                                    onClick="WdatePicker({el:document.addForm.startOpenDate_add,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="../../images/dateButton.png" width="15" height="12"
                                    border="0" alt="选择"></TD>
                        </TR>
                       	<%}else{ %>
                       	<TR bgcolor="#FFFFFF">
                            <TD class=column width="10%" align="right" id="3">定版时间</TD>
                            <TD width="35%"><input type="text" name="startOpenDate_add"
                                                   readonly class=bk> <img
                                    name="shortDateimg"
                                    onClick="WdatePicker({el:document.addForm.startOpenDate_add,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="../../images/dateButton.png" width="15" height="12"
                                    border="0" alt="选择"></TD>
                        </TR>
                       	<%} %>
                    </ms:inArea>
					<% if(!"gs_dx".equals(InstArea) && !"sx_lt".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否支持QOE功能</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_qoe_add" value="1">是
							<input type="radio" width="23%" name="is_qoe_add" value="2">否
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否支持机顶盒零配置</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="machineConfig_add" value="1">是
							<input type="radio" width="23%" name="machineConfig_add" value="2">否
						</TD>
					</TR>
					<%} %>
					<%if("gs_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">wifi业务下发通道实例号</TD>
						<TD colspan=3>
						<select name="ssid_instancenum" class="bk">
							<option value="" selected>==请选择==</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="7">7</option>
							<option value="11">11</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">H248物理标识口</TD>
						<TD colspan=3>
						<select name="hvoip_port" class="bk">
							<option value="" selected>==请选择==</option>
							<option value="A0">A0</option>
							<option value="A1">A1</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} %>
					
					<%
					if("js_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">是否支持awifi开通</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_awifi_add" value="1">是
							<input type="radio" width="23%" name="is_awifi_add" value="2">否
						</TD>
					</TR>
					<%} %>
					
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="isMulticastTr">
						<TD class=column width="10%" align="right" id="3">是否支持组播</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_multicast_add" value="1" checked="checked" id="is_multicast_add1">是
							<input type="radio" width="23%" name="is_multicast_add" value="2">否
						</TD>
					</TR>
					<%} %>
					
					<%if("js_dx".equals(InstArea) || "xj_dx".equals(InstAreaCity)){%>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
						<TD class=column width="10%" align="right" id="3">是否支持仿真测速</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_speedtest" value="1" >是
							<input type="radio" width="23%" name="is_speedtest" value="0"  checked="checked"  id="is_speedtest1">否
						</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF" id="isEsurfing" style="display:none;">
						<TD class=column width="10%" align="right" id="3">是否天翼网关</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_esurfing" value="1" >是
							<input type="radio" width="23%" name="is_esurfing" value="0"  checked="checked"  id="is_esurfing1">否
						</TD>
					</TR>
					<%if("hb_lt".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">是否支持千兆</TD>
						<TD colspan=3><select name="gigabit_port" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">版本特性</TD>
						<TD colspan=3><select name="version_feature" class="bk">
							<option value="0">普通</option>
							<option value="2">全路由</option>
							<option value="1">测速</option>
							
						</select></TD>
					</TR>
					<!-- 20200512 江西电信新增万兆 -->
					<%} else if ("jx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="gbBroadband_add" style="display:none;">
						<TD class=column width="10%" align="right" id="3">支持速率</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="gbBroadband_add" value="2" checked="checked">百兆
							<input type="radio" width="23%" name="gbBroadband_add" value="1" >千兆
							<input type="radio" width="23%" name="gbBroadband_add" value="3" >万兆
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="gbBroadband_add" style="display:none;">
						<TD class=column width="10%" align="right" id="3">是否支持千兆宽带</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="gbBroadband_add" value="1" >是
							<input type="radio" width="23%" name="gbBroadband_add" value="2" >否
						</TD>
					</TR>
					<%} %>
					<% if("gs_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">E8-C</option>
							<option value="2">PON融合</option>
							<option value="3">10GPON</option>
							<option value="4">政企网关</option>
							<option value="5">天翼网关1.0</option>
							<option value="6">天翼网关2.0</option>
							<option value="7">天翼网关3.0</option>
						</select></TD>
					</TR>
					<%} %>
					
					<% if("sx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="" >==请选择==</option>
							<option value="1">e8-C</option>
							<option value="2">a8-C</option>
							<option value="3">天翼网关1.0</option>
							<option value="4">天翼网关2.0</option>
							<option value="5">天翼网关3.0</option>
							<option value="6">融合终端</option>
							<option value="7">千兆网关</option>
						</select></TD>
					</TR>
					<%} %>
					
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">E8C</option>
							<option value="2">天翼网关1.0</option>
							<option value="3">天翼网关2.0</option>
							<option value="4">融合网关</option>
							<option value="5">天翼网关3.0</option>
							<option value="6">A8-C</option>
							<option value="7">千兆网关</option>
							<option value="8">E8-B</option>
							<option value="99">其它</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">是否支持wifi</TD>
						<TD colspan=3><select name="wifi" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi支持频率</TD>
						<TD colspan=3><select name="wifi_frequency" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">2.4G</option>
							<option value="2">2.4G/5G</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi支持的无线协议</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >无
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi支持最大下载速率(MB/S)</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="download_max_wifi"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">是否有千兆口版本类型</TD>
						<TD colspan=3>
						<select name="gigabit_port" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">千兆口有哪些</TD>
						<TD colspan=3><select name="gigabit_port_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">lan1</option>
							<option value="2">lan1\lan2</option>
							<option value="3">lan1\lan2\lan3\lan4</option>
							<%if("xj_dx".equals(InstArea)){%>
								<option value="4">全部</option>
							<%}%>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">lan口的最大下载速率(MB/S)</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="download_max_lan"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">是否支持安审插件</TD>
						<TD colspan=3>
						  <select name="is_security_plugin" class="bk" onchange="disableSecurityPlugin()">
							<option value="-1" >==请选择==</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">安审插件版本</TD>
						<TD colspan=3>
						  <select name="security_plugin_type" class="bk" disabled="disabled">
							<option value="-1" >==请选择==</option>
							<option value="1">溯源</option>
							<option value="2">净网</option>
							<option value="3">全部</option>
						</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">电源功率</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="power"
							maxlength=30 class=bk size=20>&nbsp;<font>例如：12V1.0A</font> </TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">终端的入网时间</TD>
						<TD colspan=3><input type="text" name="terminal_access_time"
						readonly class=bk> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.addForm.terminal_access_time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择"></TD>
					</TR>
					<%} %>
					
					<%if("jl_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">E8C</option>
							<option value="2">天翼网关1.0</option>
							<option value="3">天翼网关2.0</option>
							<option value="4">天翼网关3.0</option>
							<option value="5">融合终端</option>
						</select></TD>
					</TR>
					<%} %>
					
					<%if("sd_dx".equals(InstArea)){%>
					   <TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">天翼网关1.0</option>
							<option value="2">天翼网关2.0</option>
							<option value="3">天翼网关3.0</option>
							<option value="4">天翼网关4.0</option>
							<option value="5">天翼网关5.0</option>
							<option value="6">E8C</option>
							<option value="7">融合网关</option>
							<option value="8">政企网关</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
                        <TD class=column width="10%" align="right" id="3">是否支持测速</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="is_speedtest" value="1" >是
                            <input type="radio" width="23%" name="is_speedtest" value="0" >否
                            <input type="radio" width="23%" name="is_speedtest" value="-1" style="display:none">
                        </TD>
                    </TR>
					<%} %>
					
					<%if("nx_dx".equals(InstArea)){%>
						<TR bgcolor="#FFFFFF" id="">
							<TD class=column align="right">设备版本类型</TD>
							<TD colspan=3><select name="device_version_type" class="bk">
								<option value="-1" >==请选择==</option>
								<option value="1">E8-C</option>
								<option value="2">PON融合</option>
								<option value="3">10GPON</option>
								<option value="4">10EPON</option>
								<option value="5">XGPON</option>
								<option value="6">XEPON</option>
								<option value="7">政企网关</option>
								<option value="8">天翼网关1.0</option>
								<option value="9">天翼网关2.0</option>
								<option value="10">天翼网关3.0</option>
								<option value="11">天翼网关4.0</option>
							</select></TD>
						</TR>
					<%} %>
					
					<%if("jx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
						<TD class=column width="10%" align="right" id="3">是否支持测速</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_speedtest" value="1" >是
							<input type="radio" width="23%" name="is_speedtest" value="0"  checked="checked"  id="is_speedtest1">否
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi能力</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >无
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="isNewVersion">
						<TD class=column width="10%" align="right" id="3">天翼网关最新版本</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_newVersion" value="1" >是
							<input type="radio" width="23%" name="is_newVersion" value="0"  checked="checked"  id="is_newVersion">否
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">设备版本类型</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==请选择==</option>
							<option value="1">E8-C</option>
							<option value="2">PON融合</option>
							<option value="3">10GPON</option>
							<option value="4">10EPON</option>
							<option value="5">XGPON</option>
							<option value="6">XEPON</option>
							<option value="7">政企网关</option>
							<option value="8">天翼网关1.0</option>
							<option value="9">天翼网关2.0</option>
							<option value="10">天翼网关3.0</option>
							<option value="11">天翼网关4.0</option>
						</select></TD>
					</TR>
					<%} %>
					<!-- sx_dx -->
					<% if("sx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">H248语音下发场景</TD>
						<TD colspan=3><select name="hvoip_type" class="bk">
							<option value="" >==请选择==</option>
							<option value="1">voip_both</option>
							<option value="2">voip_H248</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">sip语音下发场景</TD>
						<TD colspan=3><select name="svoip_type" class="bk">
							<option value="" >==请选择==</option>
							<option value="1">voip_both</option>
							<option value="2">voip_sip</option>
						</select></TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF" id="reasonTr" style= "display:none;">
						<TD class=column width="10%" align="right" id="3">定版原因</TD>
						<TD colspan=3>
							<textarea rows="3" cols="60" name="reason"></textarea>&nbsp;<font color="#FF0000">*</font>
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
		
		<!-- 编辑设备类型part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="editDeviceTypeTable" style="display: none">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="allDatas">
					<TR>
						<TH colspan="4" align="center">编辑设备类型</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">设备类型</TD>
						<TD colspan=3>
							<s:select list="devTypeMap" name="rela_dev_type_edit"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk">
							</s:select>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
						<TR bgcolor="#FFFFFF">
							<TD align="right" CLASS=green_foot>
								<INPUT TYPE="button" onclick="javascript:saveEditDeviceType()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		<input type='hidden' id="updateId" value="-1" />
		<input type='hidden' name="gw_type" value="<s:property value="gw_type" />" />
		<input type='hidden' name="editDeviceType" value="<s:property value="editDeviceType" />" />
		</FORM>
		
		<!-- 审核part -->
		<FORM NAME="checkForm" id="checkForm" METHOD="post" ACTION="/ex">
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="checkTable" style="display: none;">
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"  style="height:20px;line-height: 20px;">
					
						<TR>
							<TH colspan="4" align="center"><SPAN id="actLabel">审核设备类型</SPAN></TH>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">设备型号</TD>
							<TD colspan=3 id="shebeixinghao">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">原软件版本
							<input type="checkbox" id="checkAllVersions"/>
							</TD>
							<TD colspan=3 id="oldVersions">
								
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">目标软件版本</TD>
							<TD colspan=3 id="mbrjbanben">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">对应关系类型</TD>
							<TD colspan=3>
								<select name="relationType" id="relationType">
									<option value="1">普通软件升级</option>
								</select>
							</TD>
						</TR>
						<TR>
							<td colspan="4" CLASS=green_foot align="right">
								<input type="button" id="checkBut" value=" 保 存 " class="jianbian">
								<input type="hidden" id="thisDeviceTypeId">
								<input type="hidden" id="sameAsOld">
							</td>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</TABLE>
<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
</body>
<%@ include file="/foot.jsp"%>

<!-- 审核部分form的表单提交处理 -->
<script type="text/javascript">
	// 全选，反选
	$("#checkAllVersions").click(function(){
		if($(this).attr("checked") == true){
			$("input[name='version']").attr("checked",true);
		}
		else {
			$("input[name='version']").attr("checked", false);
		}
	});

	// 点击保存按钮
	$('#checkBut').click(function(){
		var relationType = $('#relationType').val();
		
		// 目标版本
		var thisDeviceTypeId = $('#thisDeviceTypeId').val();
		
		// 选中的原先版本
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
			alert('未勾选原版本，请勾选');
			return; 
		}
		
		// 如果有重复的，弹出框，让用户勾选是否继续保存
		if($('#sameAsOld').val() == '1'){
			var x = confirm('有原版本和目标版本一样，是否继续保存');
			// 如果不继续保存，直接返回
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
				alert("成功");
				// 普通方式提交
				var form = window.document.getElementById("mainForm");
				form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType"+editDeviceType;
				form.submit();
			}else{
				alert("失败");
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





//查询
function queryDevice()
{
	trimAll();
	var editDeviceType = $("input[@name='editDeviceType']").val();
	var url = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var is_check = $("select[@name='is_check']").val();
	var rela_dev_type = $("select[@name='rela_dev_type']").val();

    var isSupSpeedTest = "";
    var InstAreaCity = $('#InstAreaCity').val();
    if("sd_dx" == InstAreaCity )
    {
        isSupSpeedTest  = $("select[@name='isSupSpeedTest_Query']").val();
    }

	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
	
	form.submit();
	
	showAddPart(false);
	showCheckPart(false);
}

//查询
function exportExcel()
{
	trimAll();
	var editDeviceType = $("input[@name='editDeviceType']").val();
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var is_check = $("select[@name='is_check']").val();
	var rela_dev_type = $("select[@name='rela_dev_type']").val();

    var isSupSpeedTest = "";
    var InstAreaCity = $('#InstAreaCity').val();
    isSupSpeedTest  = $("select[@name='isSupSpeedTest_Query']").val();

	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!exportExcel.action'/>?editDeviceType="+editDeviceType;

	form.submit();

	showAddPart(false);
	showCheckPart(false);
}





/**判断能否设置为规范版本*/
function changeToNormal(){
        var device_model = $("select[@name='device_model_add']").val();
        var url = '<s:url value="/itms/resource/deviceTypeInfo!isNormalVersion.action"/>';
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
   var instArea = $('#instArea').val();
	if("js_dx"==instArea){
		var reason = $("textarea[@name='reason']").val();
	   if(reason == '' || reason == null){
		   alert("请选择定版原因！");
	  		return false;
	   }
	}
	if("nmg_dx"==instArea)
	{
		var isSupportWifi = $("select[@name='wifi']").val();
		var wifiAbility = $("input[@name='wifi_ability']:checked").val();
		if("0" == isSupportWifi && "0" != wifiAbility)
		{
			alert("设备不支持wifi,请修改wifi能力！");
	  		return false;
		}
		var gigabitNum = $("input[@name='gigabitNum']").val();
		var mbitNum = $("input[@name='mbitNum']").val();
		var voipNum = $("input[@name='voipNum']").val();
		var devMaxSpeed = $("input[@name='devMaxSpeed']").val();
		if(!IsNumber(gigabitNum,"千兆口数量")) return false;
		if(!IsNumber(mbitNum,"百兆口数量")) return false;
		if(!IsNumber(voipNum,"语音口口数量")) return false;
		if(!IsNumber(devMaxSpeed,"最大支持速率")) return false;
		var wifi = $("select[@name='wifi']").val();
		var is_wifi_double = $("select[@name='is_wifi_double']").val();
		var device_version_type = $("select[@name='device_version_type']").val();
		if('-1' == wifi || null == wifi)
		{
			alert("请选择是否支持wifi！");
	  		return false;
		}
		if('-1' == is_wifi_double || null == is_wifi_double)
		{
			alert("请选择wifi是否双频！");
	  		return false;
		}
		if('-1' == device_version_type || null == device_version_type)
		{
			alert("请选择设备版本类型！");
	  		return false;
		}
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
	var instArea = $('#instArea').val();
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
	if(rela_dev_type=="-1"||rela_dev_type==""){
			alert("请选择设备类型");
			return false;
	}
	//alert(instArea);
	if("jx_dx"!=instArea){
	  	var type_id = $("select[@name='type_id']").val();
	  	if(type_id=="-1"||type_id=="")
	  	{
	    	alert("请选择上行方式");
	    	return false;
	  	}
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
     var machineConfig = "";

  	 var is_QOE = "";
     if("gs_dx"==instArea || "sx_lt" == instArea){
    	 machineConfig = "1";
         is_QOE = "1";
     }
     else
     {
     	 machineConfig = $("input[@name='machineConfig_add']:checked").val();
       	 is_QOE = $("input[@name='is_qoe_add']:checked").val();
     }
     var startOpenDate = "2020-02-17 20:30:00";
     if("sx_lt" != instArea){
        startOpenDate = $("input[@name='startOpenDate_add']").val();
     }
     var mbBroadband = $("input[@name='mbBroadband_add']:checked").val();
     
   	 //是否支持awifi
   	 var is_awifi = "";
	 if("js_dx"==instArea){
     	is_awifi = $("input[@name='is_awifi_add']:checked").val();
	 }


     //是否支持组播
     var is_multicast = "";
	 if("xj_dx"==instArea){
     	is_multicast = $("input[@name='is_multicast_add']:checked").val();
	 }
	 
     var is_speedtest="";
   	 //是否支持仿真测速
	 if("js_dx"==instArea || "jx_dx"==instArea || "sd_dx"==instArea || "xj_dx"==instArea){
		 is_speedtest=$("input[@name='is_speedtest']:checked").val();
	 }
	 var is_newVersion="";
   	 //是否支持天翼网关,是否天翼网关最新版本
   	 if("jx_dx"==instArea)
   		 {
   			is_esurfing=$("input[@name='is_esurfing']:checked").val();
   			is_newVersion=$("input[@name='is_newVersion']:checked").val();
   		 }
     // 定版原因
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
	 var iscloudnet = 0;// 是否支持云网超宽
     
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
 		 iscloudnet = $("input[@name='iscloudnet']:checked").val();
 		 if(iscloudnet == null || iscloudnet == "" || iscloudnet == -1){
 			iscloudnet = 0;
  		 }
 	 }

 	 
 	if("jl_dx"==instArea || "sd_dx"==instArea || "gs_dx"==instArea||"jx_dx"==instArea ||"nx_dx"==instArea || "ah_lt" == instArea){
		 device_version_type = $("select[@name='device_version_type']").val();
	 }
 	
 	
 	var ssid_instancenum="";
  	 //wifi业务下发通道实例号
  	var hvoip_port="";
  	 //H248物理标识口
  	 if("gs_dx"==instArea)
  		 {
  			ssid_instancenum=$("select[@name='ssid_instancenum']").val();
  			hvoip_port=$("select[@name='hvoip_port']").val();
  		 }
  	 
  	var hvoip_type="";
 	 //H248语音下发场景
 	var svoip_type="";
 	 //sip语音下发场景
 	 //sx_dx
 	 if("sx_dx"==instArea)
	 {
		hvoip_type=$("select[@name='hvoip_type']").val();
		svoip_type=$("select[@name='svoip_type']").val();
		device_version_type = $("select[@name='device_version_type']").val();
	 }
  	 
 	<%
	  if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) || 
			  "nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	var wifi_ability = $("input[@name='wifi_ability']:checked").val();
	<% }%>
	
	//NMDX-REQ-ITMS-20200320-HXM-001(内蒙电信ITMS终端能力审核及数据传送)  
	var gigabitNum	= ""; 
	var mbitNum	=	"";
	var voipNum	=	"";
	var wifi	=	"";
	var is_wifi_double	="";
	var fusion_ability	=	"";
	var terminal_access_method	=	"";
	var devMaxSpeed	=	"";
	var res_type_id	=	"";
	var res_vendor	=	"";
	var res_type	=	"";
	var remark	=	"";
	
	if("nmg_dx" == instArea)
	{
		device_version_type = $("select[@name='device_version_type']").val();
		gigabitNum	=  $("input[@name='gigabitNum']").val();  //千兆口数量
		mbitNum	=	$("input[@name='mbitNum']").val();    //百兆口数量
		voipNum	=	$("input[@name='voipNum']").val();    //语音口数量
		wifi	=	$("select[@name='wifi']").val();   //光猫是否有wifi
		is_wifi_double	=	$("select[@name='is_wifi_double']").val();  //是否支持双频
		fusion_ability	=	$("input[@name='fusion_ability']").val();   //融合功能
		terminal_access_method	=	$("input[@name='terminal_access_method']").val();  //综合终端接入方式
		devMaxSpeed	=	$("input[@name='devMaxSpeed']").val();     //光猫最大支持上网速率
		/* res_type_id	=	$("input[@name='res_type_id']").val();      //资源型号ID
		res_vendor	=	$("input[@name='res_vendor']").val();		 //资源厂商
		res_type	=	$("input[@name='res_type']").val(); 	 //资源型号
		remark	=	$("input[@name='remark']").val(); 			 //备注 */
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
		gigabitNum : gigabitNum,
		mbitNum : mbitNum,
		voipNum : voipNum ,
		is_wifi_double : is_wifi_double,
		fusion_ability : fusion_ability,
		terminal_access_method :terminal_access_method ,
		devMaxSpeed : devMaxSpeed,
		
		<%}%>
		<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%> 
		is_security_plugin : is_security_plugin,
		security_plugin_type : security_plugin_type,
		wifi_ability : wifi_ability,
		iscloudnet : iscloudnet,
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
		if(ajax.indexOf("成功") != -1)
		{
			// 普通方式提交
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
	     alert("请选择设备型号！");
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
		if(ajax.indexOf("成功") != -1)
		{
			var form = document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
			form.submit();
		}
	});
	showEditDeviceTypePart(false);
}

function saveEditDevVersionType(){
	var deviceVersionType = $("select[@name='dev_version_type_edit']").val();
	if(rela_dev_type == "-1" || rela_dev_type == "")
	{
		alert("请选择设备版本型号！");
		return false;
	}
	var deviceTypeId = $("input[@id='updateId']").val();
	var editDeviceType = $("input[@name='editDeviceType']").val();
	var url = "<s:url value='/itms/resource/deviceTypeInfo!updateDevVersionType.action'/>";
	$.post(url,{
		deviceTypeId : deviceTypeId,
		deviceVersionType : deviceVersionType
	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("成功") != -1)
		{
			var form = document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
			form.submit();
		}
	});
	showEditDeviceTypePart(false);
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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}

function disableDeviceType(tag){
	$("select[@name='rela_dev_type_add']").attr("disabled",tag);
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

//隐藏页面下面的编辑设备类型区域
function showEditDeviceTypePart(tag)
{
	if(tag){
		$("table[@id='editDeviceTypeTable']").show();
	}else{
		$("table[@id='editDeviceTypeTable']").hide();
	}
}

// 隐藏页面下面的添加区域
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}
// 隐藏页面下面的审核区域
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