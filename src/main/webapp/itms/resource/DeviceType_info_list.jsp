<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�汾��ѯ</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	var instArea = window.parent.document.getElementById("instArea").value;
	if("js_dx"==instArea){
		$('.check_no_js').css('display','none');
	}else{
		$('.check_js').css('display','none');
	}
	
	var editDeviceType = $("input[@name='editDeviceType']").val();
	if("jl_dx"==instArea){
		editDeviceType = 1;
	}
	if(editDeviceType=="1"){
		$('.check_editDy').css('display','none');
	}
	
});

$(function(){
	parent.dyniframesize();
});
</script>
</head>
<style>
.listtable tr:hover{
	background-color: #8dbbdb;
}
</style>
<body>
	<input type='hidden' name="editDeviceType" value="<s:property value="editDeviceType" />" />
	<table class="listtable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>�豸����</th>
				<th>�豸�ͺ�</th>
				<th>�ض��汾</th>
				<th>Ӳ���汾</th>
				<th>����汾</th>
				<th>�Ƿ�Ϊ���°汾</th>
				<th>�Ƿ����</th>
				<%
				if(!"ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
				%>
				<th>�豸����</th>
				<% }else{%>
				<th>�豸�汾����</th>
				<%}%>
				<th>���з�ʽ</th>
				<th>�ն˹��</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="deviceList!=null">
				<s:if test="deviceList.size()>0">
					<s:iterator value="deviceList">
						<tr>
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="specversion" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td>
								<s:if test="1==is_normal">
									��
								</s:if> 
								<s:else>
									��
								</s:else>
							</td>
							<td>
								<s:if test="is_check==1">
										�������
								</s:if>
								<s:else>
									<ms:inArea areaCode="gs_dx" notInMode="true">
									   <font color='red'>δ���</font>
									</ms:inArea>
									<ms:inArea areaCode="gs_dx" notInMode="false">
									<a href="javascript:gotoWhiteList('<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="devicetype_id" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />'
																)"><font color='red'>δ���</font></a>
									</ms:inArea>
								</s:else>
							</td>
							<td>
								<!--
									<s:if test="rela_dev_type_id==1">
										e8-b
									</s:if> <s:if test="rela_dev_type_id==2">
										e8-c
									</s:if> 
									<s:if test="rela_dev_type_id==3">A8-B</s:if>
									<s:if test="rela_dev_type_id==4">A8-C</s:if>
								-->
							<%if(!"ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<s:property value="rela_dev_type_name" />
							<% }else{%>
								<s:property value="devVersionType" />
							<%}%>
							</td>
							<td><s:property value="type_name" /></td>
							<td><s:property value="specName" /></td>
							<td>
								<a href="javascript:delDevice('<s:property value="devicetype_id" />',
																'<s:property value="gw_type" />')">ɾ��</a>|
								<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
								<span class="check_no_js">
								<%if("nmg_dx".equals(InstArea)){%>
									<a href="javascript:editDeviceNew('<s:property value="devicetype_id" />',
																	'<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="specversion" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="is_check" />',
																	'<s:property value="rela_dev_type_id" />',
																	'<s:property value="type_id" />',
																	'<s:property value="ip_type"/>',
																	'<s:property value="is_normal"/>',
																	'<s:property value="spec_id"/>',
																	'<s:property value="zeroconf" />',
																	'<s:property value="versionttime" />',
																	'<s:property value="mbbroadband" />',
																	'<s:property value="ip_model_type" />',
																	'<s:property value="reason" />',
																	'<s:property value="is_awifi" />',
																	'<s:property value="is_multicast" />',
																	'<s:property value="is_esurfing" />',
																	'<s:property value="device_version_type" />',
																	'<s:property value="wifi" />',
																	'<s:property value="wifi_frequency" />',
																	'<s:property value="download_max_wifi" />',
																	'<s:property value="gigabit_port" />',
																	'<s:property value="gigabit_port_type" />',
																	'<s:property value="download_max_lan" />',
																	'<s:property value="power" />',
																	'<s:property value="terminal_access_time" />',
																	'<s:property value="is_speedTest" />',
																	'<s:property value="wifi_ability" />',
																	'<s:property value="is_qoe" />',
																	'<s:property value="version_feature" />',
																	'<s:property value="is_security_plugin" />',
																	'<s:property value="security_plugin_type" />',
																	'<s:property value="gbbroadband" />',
																	'<s:property value="is_highversion" />',
																	'<s:property value="ssid_instancenum" />',
																	'<s:property value="hvoip_port" />',
																	'<s:property value="hvoip_type" />',
																	'<s:property value="svoip_type" />',
																	'<s:property value="gigabitNum" />',
																	'<s:property value="mbitNum" />',
																	'<s:property value="voipNum" />',
																	'<s:property value="is_wifi_double" />',
																	'<s:property value="fusion_ability" />',
																	'<s:property value="terminal_access_method" />',
																	'<s:property value="devMaxSpeed" />',
																)">�༭</a>|
									<%} else if("ah_lt".equals(InstArea)){ %>
									<a href="javascript:editDeviceNew('<s:property value="devicetype_id" />',
																	'<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="specversion" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="is_check" />',
																	'<s:property value="rela_dev_type_id" />',
																	'<s:property value="type_id" />',
																	'<s:property value="ip_type"/>',
																	'<s:property value="is_normal"/>',
																	'<s:property value="spec_id"/>',
																	'<s:property value="zeroconf" />',
																	'<s:property value="versionttime" />',
																	'<s:property value="mbbroadband" />',
																	'<s:property value="ip_model_type" />',
																	'<s:property value="reason" />',
																	'<s:property value="is_awifi" />',
																	'<s:property value="is_multicast" />',
																	'<s:property value="is_esurfing" />',
																	'<s:property value="device_version_type" />',
																	'<s:property value="wifi" />',
																	'<s:property value="wifi_frequency" />',
																	'<s:property value="download_max_wifi" />',
																	'<s:property value="gigabit_port" />',
																	'<s:property value="gigabit_port_type" />',
																	'<s:property value="download_max_lan" />',
																	'<s:property value="power" />',
																	'<s:property value="terminal_access_time" />',
																	'<s:property value="is_speedTest" />',
																	'<s:property value="wifi_ability" />',
																	'<s:property value="is_qoe" />',
																	'<s:property value="version_feature" />',
																	'<s:property value="is_security_plugin" />',
																	'<s:property value="security_plugin_type" />',
																	'<s:property value="gbbroadband" />',
																	'<s:property value="is_highversion" />',
																	'<s:property value="ssid_instancenum" />',
																	'<s:property value="hvoip_port" />',
																	'<s:property value="hvoip_type" />',
																	'<s:property value="svoip_type" />'
																)">�༭</a>
																<%} else { %>
									<a href="javascript:editDeviceNew('<s:property value="devicetype_id" />',
																	'<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="specversion" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="is_check" />',
																	'<s:property value="rela_dev_type_id" />',
																	'<s:property value="type_id" />',
																	'<s:property value="ip_type"/>',
																	'<s:property value="is_normal"/>',
																	'<s:property value="spec_id"/>',
																	'<s:property value="zeroconf" />',
																	'<s:property value="versionttime" />',
																	'<s:property value="mbbroadband" />',
																	'<s:property value="ip_model_type" />',
																	'<s:property value="reason" />',
																	'<s:property value="is_awifi" />',
																	'<s:property value="is_multicast" />',
																	'<s:property value="is_esurfing" />',
																	'<s:property value="device_version_type" />',
																	'<s:property value="wifi" />',
																	'<s:property value="wifi_frequency" />',
																	'<s:property value="download_max_wifi" />',
																	'<s:property value="gigabit_port" />',
																	'<s:property value="gigabit_port_type" />',
																	'<s:property value="download_max_lan" />',
																	'<s:property value="power" />',
																	'<s:property value="terminal_access_time" />',
																	'<s:property value="is_speedTest" />',
																	'<s:property value="wifi_ability" />',
																	'<s:property value="is_qoe" />',
																	'<s:property value="version_feature" />',
																	'<s:property value="is_security_plugin" />',
																	'<s:property value="security_plugin_type" />',
																	'<s:property value="gbbroadband" />',
																	'<s:property value="is_highversion" />',
																	'<s:property value="ssid_instancenum" />',
																	'<s:property value="hvoip_port" />',
																	'<s:property value="hvoip_type" />',
																	'<s:property value="svoip_type" />',
																	
																	'<s:property value="gigabitNum" />',
																	'<s:property value="mbitNum" />',
																	'<s:property value="voipNum" />',
																	'<s:property value="is_wifi_double" />',
																	'<s:property value="fusion_ability" />',
																	'<s:property value="terminal_access_method" />',
																	'<s:property value="devMaxSpeed" />',
																	'<s:property value="iscloudnet" />'
																)">�༭</a>| 			
																<!--�����༭�� �½� ����ֶ�iscloudnet
																���� ����editDeviceNew()�������gigabitNum�� devMaxSpeed������ҲҪ�ڴ˷�����ȫ��
																��Ȼ������������������ֶΣ����ǲ�Ӱ������ʡ�ݣ���Ϊ����ʡ�ݼ�ʹû������ֶ�Ҳ���ᱨ��ֻ��Ϊ�� -->				
																<%}
																if(!"ah_lt".equals(InstArea)){ %>
                                        <a href="javascript:checkDevice('<s:property value="devicetype_id" />')">���</a>
								                                <%} %>
								</span>
								<span class="check_js">
									<a href="javascript:editDevice('<s:property value="devicetype_id" />',
																	'<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="specversion" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="is_check" />',
																	'<s:property value="rela_dev_type_id" />',
																	'<s:property value="type_id" />',
																	'<s:property value="ip_type"/>',
																	'<s:property value="is_normal"/>',
																	'<s:property value="spec_id"/>',
																	'<s:property value="zeroconf" />',
																	'<s:property value="versionttime" />',
																	'<s:property value="mbbroadband" />',
																	'<s:property value="ip_model_type" />',
																	'<s:property value="reason" />',
																	'<s:property value="is_qoe" />',
																	'<s:property value="is_awifi" />',
																	'<s:property value="is_speedtest" />'
																)">���1-�༭</a>| 
									<a href="javascript:checkDeviceJS('<s:property value="devicetype_id" />',
																		'<s:property value="vendor_id" />',
																		'<s:property value="device_model_id" />',
																		'<s:property value="device_model" />',
																		'<s:property value="softwareversion" />'
																)">���2-���������Ӧ��ϵ</a>
								</span>|
								<span class="check_editDy">
									<a href="javascript:editDeviceType('<s:property value="devicetype_id" />',
																	'<s:property value="rela_dev_type_id" />'
																	)">�༭�豸����</a>|
								</span>
								<a href="javascript:detailDevice('<s:property value="devicetype_id" />',
																'<s:property value="spec_id" />')">��ϸ��Ϣ</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>û�в�ѯ������豸��</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ������豸��</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/deviceTypeInfo!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
<script>


function detailDevice(devicetype_id,specId){
	parent.showEditDeviceTypePart(false);
	window.open("<s:url value='/itms/resource/deviceTypeInfo!queryDetail.action'/>?deviceTypeId="+devicetype_id+"&detailSpecId="+specId,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}

function gotoWhiteList(vendor_id,device_model_id,device_type_id,hardwareversion,softwareversion){
	window.open("<s:url value='/itms/resource/deviceWhiteList!queryWhiteInfo.action'/>?vendor="+vendor_id+"&device_model="+device_model_id+"&devicetypeId="+device_type_id
			+"&hardwareversion="+hardwareversion+"&softwareversion="+softwareversion,
			"","left=20,top=20,height=650,width=900,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}

//�༭�豸����    (���ɹ������˴� gigabitNum - devMaxSpeed ������js ����֧�ִ��κ�ʵ�ʷ�����һ�£��ʲ�Ӱ������ʡ�ݡ� )
function editDeviceNew(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,
				   is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id,zeroconf,versionttime,
				   mbbroadband,ip_model_type, reason,is_awifi, is_multicast,is_esurfing
				   , device_version_type, wifi, wifi_frequency, download_max_wifi, gigabit_port
				   , gigabit_port_type, download_max_lan, power, terminal_access_time
				   , is_speedtest, wifi_ability, is_qoe, version_feature,is_plugin,plugin_type,gbBroadband,
				   is_highversion,ssid_instancenum,hvoip_port,hvoip_type,svoip_type,gigabitNum,mbitNum,voipNum,
				   is_wifi_double,fusion_ability,terminal_access_method,devMaxSpeed,iscloudnet)
{  
	
  if(is_check=="" ){
	  is_check=-2;
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;	
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;	
    window.parent.document.getElementsByName("specId")[0].value=spec_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	<%
		if("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	window.parent.document.getElementById("device_version_type").value=device_version_type;
	if(is_qoe == 1){
			window.parent.document.getElementsByName("is_qoe_add")[0].checked = "checked";
		}else{
			window.parent.document.getElementsByName("is_qoe_add")[1].checked = "checked";
		}
	<%
	  }
	%>


	if (ip_model_type == 0) {
		window.parent.document.getElementsByName("ipType")[0].checked = "checked";
	} else if (ip_model_type == 1) {
		window.parent.document.getElementsByName("ipType")[1].checked = "checked";
	} else if (ip_model_type == 2) {
		window.parent.document.getElementsByName("ipType")[2].checked = "checked";
	} else  if (ip_model_type == 3){
		window.parent.document.getElementsByName("ipType")[3].checked = "checked";
	}else{
		window.parent.document.getElementsByName("ipType")[4].checked = "checked";
	}
	<%
	  if(!"gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && !"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	if(zeroconf == 1){
		window.parent.document.getElementsByName("machineConfig_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("machineConfig_add")[1].checked = "checked";
	}
	<% 
	  }
	%>
	if(mbbroadband == 1){
		window.parent.document.getElementsByName("mbBroadband_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("mbBroadband_add")[1].checked = "checked";
	}
	<%
	  if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	if(is_awifi == 1){
		window.parent.document.getElementsByName("is_awifi_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("is_awifi_add")[1].checked = "checked";
	}
	<% 
	  }
	%>
	<%
	  if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	parent.disableDeviceType(false);
	window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
	<% 
	  }
	%>
	<%
	  if("sd_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
	if(is_speedtest == '1'){
        window.parent.document.getElementsByName("is_speedtest")[0].checked = "checked";
    }
    else if(is_speedtest == '0'){
        window.parent.document.getElementsByName("is_speedtest")[1].checked = "checked";
    }
    else
    {
        window.parent.document.getElementsByName("is_speedtest")[2].checked = "checked";
    }
	<% 
	  }
	%>

    //����ɽ�������¼������ֶΣ��ʽ����ĵ��ź�ɽ�����Ų𿪡�
	<%
     if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
    %>
    window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
    <%
      }
    %>

	<%
	  if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	if(is_multicast == 1){
		window.parent.document.getElementsByName("is_multicast_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("is_multicast_add")[1].checked = "checked";
	}
	window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;	
	window.parent.document.getElementsByName("wifi")[0].value=wifi;
	
	window.parent.document.getElementsByName("wifi_frequency")[0].value=wifi_frequency;
	if(wifi_ability == 1){
		window.parent.document.getElementsByName("wifi_ability")[1].checked = "checked";
	}
	else if(wifi_ability == 2){
		window.parent.document.getElementsByName("wifi_ability")[2].checked = "checked";
	}
	else if(wifi_ability == 3){
		window.parent.document.getElementsByName("wifi_ability")[3].checked = "checked";
	}
	else if(wifi_ability == 4){
		window.parent.document.getElementsByName("wifi_ability")[4].checked = "checked";
	}
	else {
		window.parent.document.getElementsByName("wifi_ability")[0].checked = "checked";
	}
	window.parent.document.getElementsByName("download_max_wifi")[0].value=download_max_wifi;
	window.parent.document.getElementsByName("gigabit_port")[0].value=gigabit_port;
	window.parent.document.getElementsByName("gigabit_port_type")[0].value=gigabit_port_type;
	window.parent.document.getElementsByName("download_max_lan")[0].value = download_max_lan;
	window.parent.document.getElementsByName("power")[0].value = power;
	 if(terminal_access_time!=null&&terminal_access_time!="")
		{
		window.parent.document.getElementsByName("terminal_access_time")[0].value = terminal_access_time;
		}else
			{
			window.parent.document.getElementsByName("terminal_access_time")[0].value = "";

			}  
	 window.parent.document.getElementsByName("is_security_plugin")[0].value=is_plugin;
	 if(is_plugin == 1){
		 window.parent.document.getElementsByName("security_plugin_type")[0].value=plugin_type;
	 }else{
		 window.parent.document.getElementsByName("security_plugin_type")[0].value=-1;
	 }
	 
	 if(iscloudnet == 1){
		 window.parent.document.getElementsByName("iscloudnet")[0].checked = "checked";
	 }else{
		 window.parent.document.getElementsByName("iscloudnet")[1].checked = "checked";
	 }
	 
	 if(is_speedtest == 1){
		 window.parent.document.getElementsByName("is_speedtest")[0].checked = "checked";
	 }else{
		 window.parent.document.getElementsByName("is_speedtest")[1].checked = "checked";
	 }
	 
	 
	  
  	<% 
	  }
    %>

  	<%
      if(!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) 
    		  && !"nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
    %>
       window.parent.document.getElementsByName("startOpenDate_add")[0].value=versionttime;
  	<%
    	}
	%>
	
  	<%
    if("nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
    %>
     if("1970-01-01 08:00:00" != versionttime){
		  window.parent.document.getElementsByName("startOpenDate_add")[0].value=versionttime;
	    }
	<%
  	}
	%>
	
	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";
	if(is_esurfing==1)
		{
		window.parent.document.getElementsByName("is_esurfing")[0].checked="checked";
		}
	else
		{
		window.parent.document.getElementsByName("is_esurfing")[1].checked="checked";
		}
	
	// JXDX-ITMS-REQ-20181119-WUWF-001(ITMSƽ̨��ͥ�����豸ά������Qoeά������
	<%
	  if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
		if(is_qoe == 1){
			window.parent.document.getElementsByName("is_qoe_add")[0].checked = "checked";
		}else{
			window.parent.document.getElementsByName("is_qoe_add")[1].checked = "checked";
		}
		if(is_highversion == 1){
			window.parent.document.getElementsByName("is_newVersion")[0].checked = "checked";
		}else{
			window.parent.document.getElementsByName("is_newVersion")[1].checked = "checked";
		}
		
		if(is_speedtest == 1){
			window.parent.document.getElementsByName("is_speedtest")[0].checked = "checked";
		}
		else {
			window.parent.document.getElementsByName("is_speedtest")[1].checked = "checked";
		}
		if(wifi_ability == 1){
			window.parent.document.getElementsByName("wifi_ability")[1].checked = "checked";
		}
		else if(wifi_ability == 2){
			window.parent.document.getElementsByName("wifi_ability")[2].checked = "checked";
		}
		else if(wifi_ability == 3){
			window.parent.document.getElementsByName("wifi_ability")[3].checked = "checked";
		}
		else if(wifi_ability == 4){
			window.parent.document.getElementsByName("wifi_ability")[4].checked = "checked";
		}
		else {
			window.parent.document.getElementsByName("wifi_ability")[0].checked = "checked";
		}
		
		// 20200512 ������������
		if(gbBroadband == 1){// ǧ��
			window.parent.$("input:radio[name='gbBroadband_add'][value='1']").attr('checked',true);
		}else if(gbBroadband == 2){// ����
			window.parent.$("input:radio[name='gbBroadband_add'][value='2']").attr('checked',true);
		}else if(gbBroadband == 3){// ����
			window.parent.$("input:radio[name='gbBroadband_add'][value='3']").attr('checked',true);
		}
		/* if(gbBroadband == 1){
			window.parent.$("input:radio[name='gbBroadband_add']:first").attr('checked',true);
		}
		else {
			window.parent.$("input:radio[name='gbBroadband_add']:last").attr('checked',true);
		} */
		window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
	<% 
	  }
	%>
	<%
	  if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
		  window.parent.document.getElementsByName("gigabit_port")[0].value=gigabit_port;	
		  window.parent.document.getElementsByName("version_feature")[0].value=version_feature;	
	 <% 
	 }
	%>
	<%
	if("nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	if(wifi_ability == 1){
		window.parent.document.getElementsByName("wifi_ability")[1].checked = "checked";
	}
	else if(wifi_ability == 2){
		window.parent.document.getElementsByName("wifi_ability")[2].checked = "checked";
	}
	else if(wifi_ability == 3){
		window.parent.document.getElementsByName("wifi_ability")[3].checked = "checked";
	}
	else if(wifi_ability == 4){
		window.parent.document.getElementsByName("wifi_ability")[4].checked = "checked";
	}
	else {
		window.parent.document.getElementsByName("wifi_ability")[0].checked = "checked";
	}
	window.parent.document.getElementsByName("device_version_type")[0].value = device_version_type;
	window.parent.document.getElementsByName("gigabitNum")[0].value = gigabitNum;
	window.parent.document.getElementsByName("mbitNum")[0].value = mbitNum;
	window.parent.document.getElementsByName("voipNum")[0].value = voipNum;
	window.parent.document.getElementsByName("wifi")[0].value = wifi;
	window.parent.document.getElementsByName("is_wifi_double")[0].value = is_wifi_double;
	window.parent.document.getElementsByName("fusion_ability")[0].value =  fusion_ability;
	window.parent.document.getElementsByName("terminal_access_method")[0].value = terminal_access_method;
	window.parent.document.getElementsByName("devMaxSpeed")[0].value = devMaxSpeed;
	<%}%>
	<%
	if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	window.parent.document.getElementsByName("ssid_instancenum")[0].value = ssid_instancenum;
	window.parent.document.getElementsByName("hvoip_port")[0].value = hvoip_port;
	window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
	<%}%>
	<%
	if("sx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	window.parent.document.getElementsByName("hvoip_type")[0].value = hvoip_type;
	window.parent.document.getElementsByName("svoip_type")[0].value = svoip_type;
	window.parent.document.getElementsByName("device_version_type")[0].value=device_version_type;
	<%}%>
	
	window.parent.document.getElementById("actLabel").innerHTML="�༭";
	window.parent.document.getElementsByName("reason")[0].value=reason;	
	
	var edit = $("input[@name='editDeviceType']").val();
	if(edit=="0"){
		parent.disableDeviceType(true);
	}else{
		parent.disableDeviceType(false);
	}
	<%
	  if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) ||
			  "nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	   parent.disableDeviceType(false);
	<% 
	  }
	%>
	parent.showEditDeviceTypePart(false);
	parent.queryTypeName(type_id);
	parent.disableLabel(true);
	parent.showAddPart(true);
	parent.showCheckPart(false);
	parent.getPortAndType(device_type_id);
	
	<%if("nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
	   parent.disableDeviceType(false);
	<%}%>
	
}

//�༭ �����ڱ༭ҳ������ device_version_type ��ߵ�������
function editDeviceAH(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,
				   is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id,zeroconf,versionttime,
				   mbbroadband,ip_model_type, reason,is_awifi, is_multicast,is_esurfing
				   , device_version_type, wifi, wifi_frequency, download_max_wifi, gigabit_port
				   , gigabit_port_type, download_max_lan, power, terminal_access_time
				   , is_speedtest, wifi_ability, is_qoe, version_feature,is_plugin,plugin_type,gbBroadband,
				   is_highversion,ssid_instancenum,hvoip_port,hvoip_type,svoip_type,devVersionType)
{

  if(is_check=="" ){
	  is_check=-2;
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;
    window.parent.document.getElementsByName("specId")[0].value=spec_id;
	window.parent.document.getElementById("updateId").value=device_type_id;
	window.parent.document.getElementById("device_version_type").value=devVersionType;
	if (ip_model_type == 0) {
		window.parent.document.getElementsByName("ipType")[0].checked = "checked";
	} else if (ip_model_type == 1) {
		window.parent.document.getElementsByName("ipType")[1].checked = "checked";
	} else if (ip_model_type == 2) {
		window.parent.document.getElementsByName("ipType")[2].checked = "checked";
	} else  if (ip_model_type == 3){
		window.parent.document.getElementsByName("ipType")[3].checked = "checked";
	}else{
		window.parent.document.getElementsByName("ipType")[4].checked = "checked";
	}

	if(mbbroadband == 1){
		window.parent.document.getElementsByName("mbBroadband_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("mbBroadband_add")[1].checked = "checked";
	}

	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";
	if(is_esurfing==1)
		{
		window.parent.document.getElementsByName("is_esurfing")[0].checked="checked";
		}
	else
		{
		window.parent.document.getElementsByName("is_esurfing")[1].checked="checked";
		}

	window.parent.document.getElementById("actLabel").innerHTML="�༭";
	window.parent.document.getElementsByName("reason")[0].value=reason;

	var edit = $("input[@name='editDeviceType']").val();
	if(edit=="0"){
		parent.disableDeviceType(true);
	}else{
		parent.disableDeviceType(false);
	}
	parent.showEditDeviceTypePart(false);
	parent.queryTypeName(type_id);
	parent.disableLabel(true);
	parent.showAddPart(true);
	parent.showCheckPart(false);
	parent.getPortAndType(device_type_id);

}

//�༭�豸����
function editDevice(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,
				   is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id,zeroconf,versionttime,
				   mbbroadband,ip_model_type, reason,is_qoe,is_awifi,is_speedtest)
{     
  if(is_check=="" ){
	  is_check=-2;
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;	
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;	
    window.parent.document.getElementsByName("specId")[0].value=spec_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	if (ip_model_type == 0) {
		window.parent.document.getElementsByName("ipType")[0].checked = "checked";
	} else if (ip_model_type == 1) {
		window.parent.document.getElementsByName("ipType")[1].checked = "checked";
	} else if (ip_model_type == 2) {
		window.parent.document.getElementsByName("ipType")[2].checked = "checked";
	} else  if (ip_model_type == 3){
		window.parent.document.getElementsByName("ipType")[3].checked = "checked";
	}else{
		window.parent.document.getElementsByName("ipType")[4].checked = "checked";
	}
	
	if(mbbroadband == 1){
		window.parent.document.getElementsByName("mbBroadband_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("mbBroadband_add")[1].checked = "checked";
	}
	if(zeroconf == 1){
		window.parent.document.getElementsByName("machineConfig_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("machineConfig_add")[1].checked = "checked";
	}
	<%
	  if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
	%>
	if(is_awifi == 1){
		window.parent.document.getElementsByName("is_awifi_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("is_awifi_add")[1].checked = "checked";
	}
	<% 
	  }
  	%>
	if(is_qoe == 1){
		window.parent.document.getElementsByName("is_qoe_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("is_qoe_add")[1].checked = "checked";
	}
  	<%
    	if(!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
  	%>
		window.parent.document.getElementsByName("startOpenDate_add")[0].value=versionttime;
  	<%
    	}
    %>
	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";

	if(is_speedtest == 1){
		window.parent.document.getElementsByName("is_speedtest")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("is_speedtest")[1].checked = "checked";
	}
	
	window.parent.document.getElementById("actLabel").innerHTML="�༭";
	window.parent.document.getElementsByName("reason")[0].value=reason;	
	
	var edit = $("input[@name='editDeviceType']").val();
	if(edit=="0"){
		parent.disableDeviceType(true);
	}else{
		parent.disableDeviceType(false);
	}
	parent.showEditDeviceTypePart(false);
	parent.queryTypeName(type_id);
	parent.disableLabel(true);
	parent.showAddPart(true);
	parent.showCheckPart(false);
	
}

//���༭�豸����
function editDeviceType(device_type_id,rela_dev_type_id){  
	if(rela_dev_type_id=="" ){
		rela_dev_type_id=-1;
	}

	window.parent.document.getElementsByName("rela_dev_type_edit")[0].value=rela_dev_type_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	parent.showEditDeviceTypePart(true);
	parent.showAddPart(false);
	parent.showCheckPart(false);
}

//ɾ���豸�ͺ�
function delDevice(id,gw_type)
{
	parent.showEditDeviceTypePart(false);
	if(!delWarn())
		return;
	var url = "<s:url value='/itms/resource/deviceTypeInfo!deleteDevice.action'/>";
	
	$.post(url,{
		deleteID:id,
		gw_type:gw_type
	},function(ajax){
		var result = parseInt(ajax);
		if(result == 0)
		{
			alert("���豸�汾��������豸��������ɾ����");
			return;
		}
		if(result > 0)
		{
			alert("�豸�ͺ�ɾ���ɹ�");
			
			// ��ͨ��ʽ�ύ
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>";
			//�½����Ų����ò���
			<%
			  if(!"xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			%>
				parent.reset();	
		    <% 
			  }
		    %>
            //form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("�豸�ͺ�ɾ��ʧ�ܣ�");
		}
	});
}

function delWarn(){
	if(confirm("���Ҫɾ�����豸�汾��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function toExcel(taskId,upResult){
	var url =  "<s:url value='/itms/resource/deviceTypeInfo!exportList.action'/>?deviceTypeId="+devicetype_id;
	document.all("childFrm").src=url;
}

function checkDevice(id)
{
	var editDeviceType = $("input[@name='editDeviceType']").val();
	parent.showEditDeviceTypePart(false);
	var url = "<s:url value='/itms/resource/deviceTypeInfo!updateIsCheck.action'/>";
	$.post(url,{
		deviceTypeId:id
	},function(ajax){
		var result = parseInt(ajax);
		if(result > 0)
		{
			alert("����豸�ɹ�");
			
			// ��ͨ��ʽ�ύ
			var form = window.parent.document.getElementById("mainForm");
			//form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>";
			//form.target = "dataForm";
			<%
			  if("nmg_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			%>
			   form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>?editDeviceType="+editDeviceType;
		    <% 
			  }else{
		    %>
				form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>";
		    <%  
			  }
			 %>
			
			//form.action = "<s:url value='/itms/resource/deviceTypeInfo!queryList.action'/>";

			form.submit();
		}
		else
		{
			alert("����豸ʧ��");
		}
	});
}
// ���յ�����˰�ť
function checkDeviceJS(devicetype_id, vendor_id, device_model_id, device_model, softwareversion)
{
	parent.showEditDeviceTypePart(false);
	window.parent.window.showAddPart(false);
	window.parent.window.showCheckPart(true);
	window.parent.document.getElementById("thisDeviceTypeId").value = devicetype_id;
	window.parent.document.getElementById("shebeixinghao").innerHTML = device_model;
	window.parent.document.getElementById("mbrjbanben").innerHTML = softwareversion;
	
	var url = "<s:url value='/itms/resource/deviceTypeInfo!queryByVendorIdAndDeviceModelId.action'/>";
	$.post(url,{
		deviceTypeId:devicetype_id,
		vendor:vendor_id,
		device_model:device_model_id
		
	},function(ajax){
		// alert(ajax);
		window.parent.document.getElementById("oldVersions").innerHTML=ajax;
	});
}
</script>
</html>