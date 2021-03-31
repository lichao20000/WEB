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
	var editDeviceType = $("input[@name='editDeviceType']").val();
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	setValue();
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
	//form.target = "dataForm";
	form.submit();
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
	 //�������У��޸�֮ǰ�����˵�bug
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
		 document.getElementsByName("gigabitNum")[0].value=""; //ǧ�׿�����
		 document.getElementsByName("mbitNum")[0].value="";    //���׿�����
		 document.getElementsByName("voipNum")[0].value="";    //����������
		 document.getElementsByName("wifi")[0].value="-1";     //��è�Ƿ���wifi
		 document.getElementsByName("is_wifi_double")[0].value="-1"; //�Ƿ�֧��˫Ƶ
		 document.getElementsByName("fusion_ability")[0].value="";   //�ںϹ���
		 document.getElementsByName("terminal_access_method")[0].value=""; //�ۺ��ն˽��뷽ʽ
		 document.getElementsByName("devMaxSpeed")[0].value="";      //��è���֧����������
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
				<div align="center" class="title_bigwhite">�豸�汾</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">��ѯʱ��Ϊ�豸�汾�����ʱ��</td>
				<ms:inArea areaCode="sx_lt" notInMode="true">
					<td align="right"><input type='button' onclick='Add()'
											 value=' �� �� ' class="jianbian" id='idAdd' /></td>
				</ms:inArea>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
					<table class="mytable" width="100%"
						align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">�豸�汾��ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">�豸����</TD>
							<TD align="left" width="35%"><select name="vendor" class="bk"
								onchange="gwShare_change_select('deviceModel','-1')">
							</select></TD>
							<TD align="right" class=column width="15%">�豸�ͺ�</TD>
							<TD width="35%"><select name="device_model" class="bk">
								<option value="-1">==��ѡ����==</option>
							</select></TD>
						</TR>
						<%String InstAreaCity=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">Ӳ���汾</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
								color="#FF0000"></font></TD>
							<TD align="right" class=column width="15%">����汾</TD>
							
							<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
								maxlength=30 class=bk size=20>&nbsp;<% if(!"sx_lt".equals(InstAreaCity)){%><font color="#FF0000">֧�ֺ�ƥ��</font><%}%>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">�Ƿ����</TD>
							<TD align="left" width="35%"><select name="is_check"
								class="bk">
								<option value="-2">==��ѡ��==</option>
								<option value="1">�������</option>
								<option value="-1">δ���</option>
							</select></TD>
							<%
							if(!"ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
							%>
							<TD align="right" class=column width="15%">�豸����</TD>
							<TD width="35%">
							<s:select list="devTypeMap" name="rela_dev_type"
										headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
										listValue="type_name" cssClass="bk"></s:select>
							</TD>
							<%}else {%>
							<TD align="right" class=column width="15%">�豸�汾����</TD>
							<TD width="35%">
								<s:select list="devVersionTypeMap" name="deviceVersionType" listKey="value"
										  listValue="text" cssClass="bk"></s:select>
							</TD>
							<%} %>
						</TR>
		                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">���з�ʽ</TD>
							<TD align="left" width="35%"><select
								name="access_style_relay_id" class="bk">
								<option value="-1">==��ѡ��==</option>
								<option value="1">ADSL</option>
								<option value="2">LAN</option>
								<option value="3">EPON����</option>
								<option value="4">GPON����</option>
								<%
								if("sx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
								%>
									<option value="5">10G-EPON</option>
									<option value="6">XG-PON</option>
								<%} else if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
									<option value="5">10G-EP0N</option>
									<option value="6">XG-PON</option>
									<option value="99">����</option>
								<%}%>
							</select></TD>
							<TD align="right" class=column width="15%">�ն˹��</TD>
							<td width="35%">
							<s:select list="specList" name="spec_id" headerKey="-1"
									headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
									value="spec_id" cssClass="bk"></s:select>
							</td>
						</TR>


						<ms:inArea areaCode="sx_lt" notInMode="false">
							<select name="machineConfig" class="bk" style="display:none">
								<option value="-1">==��ѡ��==</option>
								<option value="1">��</option>
								<option value="2">��</option>
							</select>
						</ms:inArea>

						<ms:inArea areaCode="sx_lt" notInMode="true">
							<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD align="right" class=column width="15%">�Ƿ�֧�ֻ�����������</TD>
								<TD align="left" width="35%">
									<select name="machineConfig" class="bk">
										<option value="-1">==��ѡ��==</option>
										<option value="1">��</option>
										<option value="2">��</option>
									</select>
								</TD>
								<TD align="right" class=column width="15%">�Ƿ�֧��IPV6</TD>
								<TD align="left" width="35%">
									<select name="ipvsix" class="bk">
										<option value="-1">==��ѡ��==</option>
										<option value="1">��</option>
										<option value="2">��</option>
									</select>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								<TD class=column width="15%" align='right'>�汾���濪ʼʱ��</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk >
									<img name="shortDateimg"
										 onClick="WdatePicker({el:document.mainForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										 src="../../images/dateButton.png" width="15" height="12"
										 border="0" alt="ѡ��">
								</TD>
								<TD class=column width="15%" align='right'>�汾�������ʱ��</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk >
									<img name="shortDateimg"
										 onClick="WdatePicker({el:document.mainForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										 src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��">
								</TD>
							</TR>
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="false">
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">�Ƿ�֧��ǧ�׿��</TD>
							<TD align="left" width="35%">
								<select name="mbBroadband" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="2">��</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">�Ƿ�֧��IPV6</TD>
							<TD align="left" width="35%">
								<select name="ipvsix" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="1">��</option>
									<option value="2">��</option>
								</select>
							</TD>
						</TR>
						</ms:inArea>
						
						<% if(!"gs_dx".equals(InstAreaCity) && !"sx_lt".equals(InstAreaCity)){%>
						  <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						       <% if(!"sd_dx".equals(InstAreaCity)){%>
							  <TD align="right" class=column width="15%">�Ƿ�֧�ְ��׿��</TD>
							  <% } else {%>
							  <TD align="right" class=column width="15%">�Ƿ�֧��ǧ�׿��</TD>
							  <% }%>
							  <TD align="left" width="35%">
									<select name="mbBroadband" class="bk">
										<option value="-1">==��ѡ��==</option>
										<option value="1">��</option>
										<option value="2">��</option>
									</select>
							  </TD>
							  <% if("sd_dx".equals(InstAreaCity)){%>
                              <TD align="right" class=column width="15%">�Ƿ�֧�ֲ���</TD>
                              <TD align="left" width="35%">
                                    <select name="isSupSpeedTest_Query" class="bk">
                                        <option value="-1">==��ѡ��==</option>
                                        <option value="1">��</option>
                                        <option value="0">��</option>
                                    </select>
                              </TD>
                              <%} else {%>
	                              <TD align="right" class=column width="15%"></TD>
	                              <TD align="left" width="35%"></TD>
                              <%} %>
						</TR>
						<%} %>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">��ʼʱ��</TD>
							<TD align="left" width="35%"><lk:date id="startTime"
								name="startTime" type="all" /></TD>
							<TD align="right" class=column width="15%">����ʱ��</TD>
							<TD align="left" width="35%"><lk:date id="endTime"
								name="endTime" type="all" /></TD>
						</TR>




						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian style="CURSOR: hand;display: none"
									onclick="javascript:gwShare_queryChange('1');"
									name="gwShare_jiadan" value="�򵥲�ѯ" /> 
								<input type="button"
									class=jianbian style="CURSOR: hand;display:none "
									onclick="javascript:gwShare_queryChange('2');"
									name="gwShare_gaoji" value="�߼���ѯ" /> 
								<input type="button"
									class=jianbian style="CURSOR: hand;display: none"
									onclick="javascript:gwShare_queryChange('3');"
									name="gwShare_import" value="�����ѯ" />
								<ms:inArea areaCode="sx_lt" notInMode="false">
									<input type='button' onclick='Add()'
													 value=' �� �� ' class="jianbian" id='idAdd' />
								</ms:inArea>
								<% if("sd_dx".equals(InstAreaCity)){%>
								    <input type="button"
                                        onclick="javascript:exportExcel()" class=jianbian
                                        name="gwShare_queryButton" value=" �� �� " />
								 <%} %>
								<input type="button"
									onclick="javascript:queryDevice()" class=jianbian
									name="gwShare_queryButton" value=" �� ѯ " /> 
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" �� �� "
									onclick="javascript:queryReset();" />
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
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm" style="background-color: white"
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
				<TABLE  class="mytable" width="100%"
					id="allDatas">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN
							id="DeviceTypeLabel"></SPAN>�豸����</TH>
					</TR>
					<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=1><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">ǧ�׿�����</TD>
						<TD colspan=1>
						<input name="gigabitNum" id = "gigabitNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=3><select name="vendor_add" class="bk"
							onchange="change_model('deviceModel','-1')">
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">�豸�ͺ�</TD>
						<TD colspan=1><select name="device_model_add" class="bk">
							<option value="-1">==��ѡ����==</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">���׿�����</TD>
						<TD colspan=1>
						<input name="mbitNum" id = "mbitNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right">�豸�ͺ�</TD>
						<TD colspan=3><select name="device_model_add" class="bk">
							<option value="-1">==��ѡ����==</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ض��汾</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">����������</TD>
						<TD colspan=1>
						<input name="voipNum" id = "voipNum" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ض��汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="speversion"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">Ӳ���汾</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">���֧������</TD> <!-- ��è���֧���������� -->
						<TD colspan=1>
						<input name="devMaxSpeed" id = "devMaxSpeed" value="" />&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">Ӳ���汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="hard_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����汾</TD>
						<TD colspan=1><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">�Ƿ�֧��wifi</TD> <!-- ��è�Ƿ���wifi -->
						<TD colspan=1><select name="wifi" class="bk">
							<option value="-1" selected>==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="soft_version_add"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�Ƿ����</TD>
						<TD colspan=1><select name="is_check_add" class="bk">
							<option value="-2" selected>==��ѡ��==</option>
							<option value="1">�������</option>
							<option value="-1">δ���</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">wifi�Ƿ�˫Ƶ</TD> <!-- ��è�Ƿ���wifi -->
						<TD colspan=1><select name="is_wifi_double" class="bk">
							<option value="-1" selected>==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�Ƿ����</TD>
						<TD colspan=3><select name="is_check_add" class="bk">
							<option value="-2" selected>==��ѡ��==</option>
							<option value="1">�������</option>
							<option value="-1">δ���</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=1>
						<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
						<TD class=column align="right">�豸�汾����</TD> <!-- ��è�Ƿ���wifi -->
						<TD colspan=1><select name="device_version_type" class="bk">
							<option value="-1" selected>==��ѡ��==</option>
							<option value="1">E8-C</option>
							<option value="2">PON�ں�</option>
							<option value="3">10GPON</option>
							<option value="4">��������</option>
							<option value="5">��������1.0</option>
							<option value="6">��������2.0</option>
							<option value="7">��������3.0</option>
						</select>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<%} else {%>
					<tr style="background-color: white">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=3>
							<s:select list="devTypeMap" name="rela_dev_type_add"
									headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>&ensp;<font color="#FF0000">*</font>
						</TD>
					</tr>
						
					<%} %>
					<%if("ah_lt".equals(InstArea)){%>
					<tr style="background-color: white">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3>
							<s:select list="devVersionTypeMap" name="device_version_type" listKey="value"
									  listValue="text" cssClass="bk"></s:select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</tr>
					<%}%>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">���з�ʽ</TD>
						<TD colspan=1>
						<div id="typeNameList"></div>
						</TD>
						<TD class=column align="right">�ںϹ���</TD> 
						<TD colspan=1>
						<input name="fusion_ability" id = "fusion_ability" value="" />
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">���з�ʽ</TD>
						<TD colspan=3>
						<span id="typeNameList"></span>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} %>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�豸֧�ֵ�Э��</TD>
						<TD colspan=1><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">����SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
						<TD class=column align="right">�ں��ն˽��뷽ʽ</TD> 
						<TD colspan=1>
						<input name="terminal_access_method" id = "terminal_access_method" value="" />
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�豸֧�ֵ�Э��</TD>
						<TD colspan=3><input type="checkbox" width="30%" id="2"
							name="protocol2" value="2">H248 <input type="checkbox"
							width="30%" id="1" name="protocol1" value="1">����SIP <input
							type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
						SIP</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�豸IP֧�ַ�ʽ</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="ipType" value="0">IPV4 
							<input type="radio" width="23%" name="ipType" value="1">IPV4��IPV6
							<input type="radio" width="22%" name="ipType" value="2">DS-Lite
							<input type="radio" width="23%" name="ipType" value="3">LAFT6
							<input type="radio" width="23%" name="ipType" value="4">��IPV6
						</TD>
					</TR>
					
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi����</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >��
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�Ϊ���°汾</TD>
						<TD colspan=3>
							<input type="radio" width="45%" name="isNormal" value="1">��
							<input type="radio" width="45%" name="isNormal" value="0" checked="checked">��
						</TD>
					</TR>
					<%if("nmg_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ն˹��</TD>
						<td colspan=3>
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk"></s:select>
						</td>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ն˹��</TD>
						<td width="35%">
						<s:select list="specList" name="specId" headerKey="-1"
								headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk"></s:select>
						</td>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF">
					<%if("sd_dx".equals(InstArea) || "gs_dx".equals(InstArea) || "sx_lt".equals(InstArea)){%>
					    <TD class=column width="10%" align="right" id="3">�Ƿ�֧��ǧ�׿��</TD>
					<%}else{ %>
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧�ְ��׿��</TD>
					<%} %>
						<TD colspan=3>
							<input type="radio" width="22%" name="mbBroadband_add" value="1">��
							<input type="radio" width="23%" name="mbBroadband_add" value="2">��
						</TD>
					</TR>
					<!-- 2020/11/16 �½������Ƿ�֧���������� -->
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧����������</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="iscloudnet" value="1">��
							<input type="radio" width="23%" name="iscloudnet" value="2">��
						</TD>
					</TR>
					<%} %>
                    <ms:inArea areaCode="sx_lt" notInMode="true">
                    	<%if("nmg_dx".equals(InstArea)){%>
                        <TR bgcolor="#FFFFFF">
                            <TD class=column width="10%" align="right" id="3">����ʱ��</TD>
                            <TD colspan=3><input type="text" name="startOpenDate_add"
                                                   readonly class=bk> <img
                                    name="shortDateimg"
                                    onClick="WdatePicker({el:document.addForm.startOpenDate_add,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="../../images/dateButton.png" width="15" height="12"
                                    border="0" alt="ѡ��"></TD>
                        </TR>
                       	<%}else{ %>
                       	<TR bgcolor="#FFFFFF">
                            <TD class=column width="10%" align="right" id="3">����ʱ��</TD>
                            <TD width="35%"><input type="text" name="startOpenDate_add"
                                                   readonly class=bk> <img
                                    name="shortDateimg"
                                    onClick="WdatePicker({el:document.addForm.startOpenDate_add,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="../../images/dateButton.png" width="15" height="12"
                                    border="0" alt="ѡ��"></TD>
                        </TR>
                       	<%} %>
                    </ms:inArea>
					<% if(!"gs_dx".equals(InstArea) && !"sx_lt".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧��QOE����</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_qoe_add" value="1">��
							<input type="radio" width="23%" name="is_qoe_add" value="2">��
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧�ֻ�����������</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="machineConfig_add" value="1">��
							<input type="radio" width="23%" name="machineConfig_add" value="2">��
						</TD>
					</TR>
					<%} %>
					<%if("gs_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">wifiҵ���·�ͨ��ʵ����</TD>
						<TD colspan=3>
						<select name="ssid_instancenum" class="bk">
							<option value="" selected>==��ѡ��==</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="7">7</option>
							<option value="11">11</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">H248�����ʶ��</TD>
						<TD colspan=3>
						<select name="hvoip_port" class="bk">
							<option value="" selected>==��ѡ��==</option>
							<option value="A0">A0</option>
							<option value="A1">A1</option>
						</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<%} %>
					
					<%
					if("js_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧��awifi��ͨ</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_awifi_add" value="1">��
							<input type="radio" width="23%" name="is_awifi_add" value="2">��
						</TD>
					</TR>
					<%} %>
					
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="isMulticastTr">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧���鲥</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_multicast_add" value="1" checked="checked" id="is_multicast_add1">��
							<input type="radio" width="23%" name="is_multicast_add" value="2">��
						</TD>
					</TR>
					<%} %>
					
					<%if("js_dx".equals(InstArea) || "xj_dx".equals(InstAreaCity)){%>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧�ַ������</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_speedtest" value="1" >��
							<input type="radio" width="23%" name="is_speedtest" value="0"  checked="checked"  id="is_speedtest1">��
						</TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF" id="isEsurfing" style="display:none;">
						<TD class=column width="10%" align="right" id="3">�Ƿ���������</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_esurfing" value="1" >��
							<input type="radio" width="23%" name="is_esurfing" value="0"  checked="checked"  id="is_esurfing1">��
						</TD>
					</TR>
					<%if("hb_lt".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�Ƿ�֧��ǧ��</TD>
						<TD colspan=3><select name="gigabit_port" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�汾����</TD>
						<TD colspan=3><select name="version_feature" class="bk">
							<option value="0">��ͨ</option>
							<option value="2">ȫ·��</option>
							<option value="1">����</option>
							
						</select></TD>
					</TR>
					<!-- 20200512 ���������������� -->
					<%} else if ("jx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="gbBroadband_add" style="display:none;">
						<TD class=column width="10%" align="right" id="3">֧������</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="gbBroadband_add" value="2" checked="checked">����
							<input type="radio" width="23%" name="gbBroadband_add" value="1" >ǧ��
							<input type="radio" width="23%" name="gbBroadband_add" value="3" >����
						</TD>
					</TR>
					<%} else {%>
					<TR bgcolor="#FFFFFF" id="gbBroadband_add" style="display:none;">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧��ǧ�׿��</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="gbBroadband_add" value="1" >��
							<input type="radio" width="23%" name="gbBroadband_add" value="2" >��
						</TD>
					</TR>
					<%} %>
					<% if("gs_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">E8-C</option>
							<option value="2">PON�ں�</option>
							<option value="3">10GPON</option>
							<option value="4">��������</option>
							<option value="5">��������1.0</option>
							<option value="6">��������2.0</option>
							<option value="7">��������3.0</option>
						</select></TD>
					</TR>
					<%} %>
					
					<% if("sx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="" >==��ѡ��==</option>
							<option value="1">e8-C</option>
							<option value="2">a8-C</option>
							<option value="3">��������1.0</option>
							<option value="4">��������2.0</option>
							<option value="5">��������3.0</option>
							<option value="6">�ں��ն�</option>
							<option value="7">ǧ������</option>
						</select></TD>
					</TR>
					<%} %>
					
					<%if("xj_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">E8C</option>
							<option value="2">��������1.0</option>
							<option value="3">��������2.0</option>
							<option value="4">�ں�����</option>
							<option value="5">��������3.0</option>
							<option value="6">A8-C</option>
							<option value="7">ǧ������</option>
							<option value="8">E8-B</option>
							<option value="99">����</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�Ƿ�֧��wifi</TD>
						<TD colspan=3><select name="wifi" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi֧��Ƶ��</TD>
						<TD colspan=3><select name="wifi_frequency" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">2.4G</option>
							<option value="2">2.4G/5G</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi֧�ֵ�����Э��</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >��
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi֧�������������(MB/S)</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="download_max_wifi"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�Ƿ���ǧ�׿ڰ汾����</TD>
						<TD colspan=3>
						<select name="gigabit_port" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">ǧ�׿�����Щ</TD>
						<TD colspan=3><select name="gigabit_port_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">lan1</option>
							<option value="2">lan1\lan2</option>
							<option value="3">lan1\lan2\lan3\lan4</option>
							<%if("xj_dx".equals(InstArea)){%>
								<option value="4">ȫ��</option>
							<%}%>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">lan�ڵ������������(MB/S)</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="download_max_lan"
							maxlength=30 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�Ƿ�֧�ְ�����</TD>
						<TD colspan=3>
						  <select name="is_security_plugin" class="bk" onchange="disableSecurityPlugin()">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��</option>
							<option value="0">��</option>
						</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�������汾</TD>
						<TD colspan=3>
						  <select name="security_plugin_type" class="bk" disabled="disabled">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��Դ</option>
							<option value="2">����</option>
							<option value="3">ȫ��</option>
						</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">��Դ����</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="power"
							maxlength=30 class=bk size=20>&nbsp;<font>���磺12V1.0A</font> </TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�ն˵�����ʱ��</TD>
						<TD colspan=3><input type="text" name="terminal_access_time"
						readonly class=bk> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.addForm.terminal_access_time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��"></TD>
					</TR>
					<%} %>
					
					<%if("jl_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">E8C</option>
							<option value="2">��������1.0</option>
							<option value="3">��������2.0</option>
							<option value="4">��������3.0</option>
							<option value="5">�ں��ն�</option>
						</select></TD>
					</TR>
					<%} %>
					
					<%if("sd_dx".equals(InstArea)){%>
					   <TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">��������1.0</option>
							<option value="2">��������2.0</option>
							<option value="3">��������3.0</option>
							<option value="4">��������4.0</option>
							<option value="5">��������5.0</option>
							<option value="6">E8C</option>
							<option value="7">�ں�����</option>
							<option value="8">��������</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
                        <TD class=column width="10%" align="right" id="3">�Ƿ�֧�ֲ���</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="is_speedtest" value="1" >��
                            <input type="radio" width="23%" name="is_speedtest" value="0" >��
                            <input type="radio" width="23%" name="is_speedtest" value="-1" style="display:none">
                        </TD>
                    </TR>
					<%} %>
					
					<%if("nx_dx".equals(InstArea)){%>
						<TR bgcolor="#FFFFFF" id="">
							<TD class=column align="right">�豸�汾����</TD>
							<TD colspan=3><select name="device_version_type" class="bk">
								<option value="-1" >==��ѡ��==</option>
								<option value="1">E8-C</option>
								<option value="2">PON�ں�</option>
								<option value="3">10GPON</option>
								<option value="4">10EPON</option>
								<option value="5">XGPON</option>
								<option value="6">XEPON</option>
								<option value="7">��������</option>
								<option value="8">��������1.0</option>
								<option value="9">��������2.0</option>
								<option value="10">��������3.0</option>
								<option value="11">��������4.0</option>
							</select></TD>
						</TR>
					<%} %>
					
					<%if("jx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="isSupSpeedTest">
						<TD class=column width="10%" align="right" id="3">�Ƿ�֧�ֲ���</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_speedtest" value="1" >��
							<input type="radio" width="23%" name="is_speedtest" value="0"  checked="checked"  id="is_speedtest1">��
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">wifi����</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="wifi_ability" checked="checked" value="0" >��
							<input type="radio" width="23%" name="wifi_ability" value="1" >802.11b
							<input type="radio" width="23%" name="wifi_ability" value="2" >802.11b/g
							<input type="radio" width="23%" name="wifi_ability" value="3" >802.11b/g/n
							<input type="radio" width="23%" name="wifi_ability" value="4" >802.11b/g/n/ac
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="isNewVersion">
						<TD class=column width="10%" align="right" id="3">�����������°汾</TD>
						<TD colspan=3>
							<input type="radio" width="22%" name="is_newVersion" value="1" >��
							<input type="radio" width="23%" name="is_newVersion" value="0"  checked="checked"  id="is_newVersion">��
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">�豸�汾����</TD>
						<TD colspan=3><select name="device_version_type" class="bk">
							<option value="-1" >==��ѡ��==</option>
							<option value="1">E8-C</option>
							<option value="2">PON�ں�</option>
							<option value="3">10GPON</option>
							<option value="4">10EPON</option>
							<option value="5">XGPON</option>
							<option value="6">XEPON</option>
							<option value="7">��������</option>
							<option value="8">��������1.0</option>
							<option value="9">��������2.0</option>
							<option value="10">��������3.0</option>
							<option value="11">��������4.0</option>
						</select></TD>
					</TR>
					<%} %>
					<!-- sx_dx -->
					<% if("sx_dx".equals(InstArea)){%>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">H248�����·�����</TD>
						<TD colspan=3><select name="hvoip_type" class="bk">
							<option value="" >==��ѡ��==</option>
							<option value="1">voip_both</option>
							<option value="2">voip_H248</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<TD class=column align="right">sip�����·�����</TD>
						<TD colspan=3><select name="svoip_type" class="bk">
							<option value="" >==��ѡ��==</option>
							<option value="1">voip_both</option>
							<option value="2">voip_sip</option>
						</select></TD>
					</TR>
					<%} %>
					<TR bgcolor="#FFFFFF" id="reasonTr" style= "display:none;">
						<TD class=column width="10%" align="right" id="3">����ԭ��</TD>
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
							onclick="javascript:save()" value=" �� �� " class=jianbian>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add"> <input
							type="hidden" name="devicetype_id" value=""></TD>
					</TR>

				</TABLE>
				</TD>
			</TR>

		</TABLE>  
		
		<!-- �༭�豸����part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="editDeviceTypeTable" style="display: none">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="allDatas">
					<TR>
						<TH colspan="4" align="center">�༭�豸����</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�豸����</TD>
						<TD colspan=3>
							<s:select list="devTypeMap" name="rela_dev_type_edit"
									headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
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
								<INPUT TYPE="button" onclick="javascript:saveEditDeviceType()" value=" �� �� " class=jianbian>&nbsp;&nbsp;
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
		
		<!-- ���part -->
		<FORM NAME="checkForm" id="checkForm" METHOD="post" ACTION="/ex">
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="checkTable" style="display: none;">
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"  style="height:20px;line-height: 20px;">
					
						<TR>
							<TH colspan="4" align="center"><SPAN id="actLabel">����豸����</SPAN></TH>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">�豸�ͺ�</TD>
							<TD colspan=3 id="shebeixinghao">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">ԭ����汾
							<input type="checkbox" id="checkAllVersions"/>
							</TD>
							<TD colspan=3 id="oldVersions">
								
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">Ŀ������汾</TD>
							<TD colspan=3 id="mbrjbanben">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">��Ӧ��ϵ����</TD>
							<TD colspan=3>
								<select name="relationType" id="relationType">
									<option value="1">��ͨ�������</option>
								</select>
							</TD>
						</TR>
						<TR>
							<td colspan="4" CLASS=green_foot align="right">
								<input type="button" id="checkBut" value=" �� �� " class="jianbian">
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

	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
	
	form.submit();
	
	showAddPart(false);
	showCheckPart(false);
}

//��ѯ
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

	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceTypeInfo!exportExcel.action'/>?editDeviceType="+editDeviceType;

	form.submit();

	showAddPart(false);
	showCheckPart(false);
}





/**�ж��ܷ�����Ϊ�淶�汾*/
function changeToNormal(){
        var device_model = $("select[@name='device_model_add']").val();
        var url = '<s:url value="/itms/resource/deviceTypeInfo!isNormalVersion.action"/>';
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
   var instArea = $('#instArea').val();
	if("js_dx"==instArea){
		var reason = $("textarea[@name='reason']").val();
	   if(reason == '' || reason == null){
		   alert("��ѡ�񶨰�ԭ��");
	  		return false;
	   }
	}
	if("nmg_dx"==instArea)
	{
		var isSupportWifi = $("select[@name='wifi']").val();
		var wifiAbility = $("input[@name='wifi_ability']:checked").val();
		if("0" == isSupportWifi && "0" != wifiAbility)
		{
			alert("�豸��֧��wifi,���޸�wifi������");
	  		return false;
		}
		var gigabitNum = $("input[@name='gigabitNum']").val();
		var mbitNum = $("input[@name='mbitNum']").val();
		var voipNum = $("input[@name='voipNum']").val();
		var devMaxSpeed = $("input[@name='devMaxSpeed']").val();
		if(!IsNumber(gigabitNum,"ǧ�׿�����")) return false;
		if(!IsNumber(mbitNum,"���׿�����")) return false;
		if(!IsNumber(voipNum,"�����ڿ�����")) return false;
		if(!IsNumber(devMaxSpeed,"���֧������")) return false;
		var wifi = $("select[@name='wifi']").val();
		var is_wifi_double = $("select[@name='is_wifi_double']").val();
		var device_version_type = $("select[@name='device_version_type']").val();
		if('-1' == wifi || null == wifi)
		{
			alert("��ѡ���Ƿ�֧��wifi��");
	  		return false;
		}
		if('-1' == is_wifi_double || null == is_wifi_double)
		{
			alert("��ѡ��wifi�Ƿ�˫Ƶ��");
	  		return false;
		}
		if('-1' == device_version_type || null == device_version_type)
		{
			alert("��ѡ���豸�汾���ͣ�");
	  		return false;
		}
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
			alert("��ѡ���豸����");
			return false;
	}
	//alert(instArea);
	if("jx_dx"!=instArea){
	  	var type_id = $("select[@name='type_id']").val();
	  	if(type_id=="-1"||type_id=="")
	  	{
	    	alert("��ѡ�����з�ʽ");
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
     
   	 //�Ƿ�֧��awifi
   	 var is_awifi = "";
	 if("js_dx"==instArea){
     	is_awifi = $("input[@name='is_awifi_add']:checked").val();
	 }


     //�Ƿ�֧���鲥
     var is_multicast = "";
	 if("xj_dx"==instArea){
     	is_multicast = $("input[@name='is_multicast_add']:checked").val();
	 }
	 
     var is_speedtest="";
   	 //�Ƿ�֧�ַ������
	 if("js_dx"==instArea || "jx_dx"==instArea || "sd_dx"==instArea || "xj_dx"==instArea){
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
	 var iscloudnet = 0;// �Ƿ�֧����������
     
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
		device_version_type = $("select[@name='device_version_type']").val();
	 }
  	 
 	<%
	  if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) || 
			  "nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	var wifi_ability = $("input[@name='wifi_ability']:checked").val();
	<% }%>
	
	//NMDX-REQ-ITMS-20200320-HXM-001(���ɵ���ITMS�ն�������˼����ݴ���)  
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
		gigabitNum	=  $("input[@name='gigabitNum']").val();  //ǧ�׿�����
		mbitNum	=	$("input[@name='mbitNum']").val();    //���׿�����
		voipNum	=	$("input[@name='voipNum']").val();    //����������
		wifi	=	$("select[@name='wifi']").val();   //��è�Ƿ���wifi
		is_wifi_double	=	$("select[@name='is_wifi_double']").val();  //�Ƿ�֧��˫Ƶ
		fusion_ability	=	$("input[@name='fusion_ability']").val();   //�ںϹ���
		terminal_access_method	=	$("input[@name='terminal_access_method']").val();  //�ۺ��ն˽��뷽ʽ
		devMaxSpeed	=	$("input[@name='devMaxSpeed']").val();     //��è���֧����������
		/* res_type_id	=	$("input[@name='res_type_id']").val();      //��Դ�ͺ�ID
		res_vendor	=	$("input[@name='res_vendor']").val();		 //��Դ����
		res_type	=	$("input[@name='res_type']").val(); 	 //��Դ�ͺ�
		remark	=	$("input[@name='remark']").val(); 			 //��ע */
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

function saveEditDevVersionType(){
	var deviceVersionType = $("select[@name='dev_version_type_edit']").val();
	if(rela_dev_type == "-1" || rela_dev_type == "")
	{
		alert("��ѡ���豸�汾�ͺţ�");
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