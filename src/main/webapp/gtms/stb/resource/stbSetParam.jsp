<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<%
	request.setCharacterEncoding("GBK");
	String service_id = request.getParameter("service_id");
%>

<SCRIPT LANGUAGE="JavaScript">
var instArea = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
var deviceId = "";
//service_id 8:�ָ�������9:����
var service_id=<%=service_id%>;
$(function(){
	gwShare_setGaoji();
});

function ExecMod()
{
    if(deviceId==""){
		alert("���ѯ�豸��");
		return false;
    }
	
	$('#stbAddStrategy_btn').attr("disabled",true);
	var url = "<s:url value='/gtms/stb/resource/stbSetParamACT!stbAddStrategy.action'/>";
	$.post(url,{
		device_id:deviceId,
		service_id:service_id
	},function(ajax){
		alert(ajax);
		var url = "<s:url value='/gtms/stb/resource/stbSetParam.jsp'/>?service_id="+service_id;
        window.location.href=url;
		$('#stbAddStrategy_btn').attr("disabled",false);
	});
}

function deviceResult(returnVal)
{
	for(var i=0;i<returnVal[2].length;i++){
		deviceId = returnVal[2][i][0];
		devicetypeId = returnVal[2][i][6];
		$("tr[@id='trDeviceResult']").css("display","");
		$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		
		$("tr[@id='trDeviceVMResult']").css("display","");
		$("tr[@id='trDeviceAccountResult']").css("display","");
		$("tr[@id='trDeviceVersionResult']").css("display","");
		
		if("hn_lt"==instArea){
			$("tr[@id='trDeviceApkEpgResult']").css("display","");
			$("tr[@id='trDeviceLonigTimeResult']").css("display","");
			$("tr[@id='trDeviceNetResult']").css("display","");
			$("tr[@id='trDeviceIpResult']").css("display","");
			$("tr[@id='trDeviceNetTypeResult']").css("display","");
			$("tr[@id='trDeviceTypeResult']").css("display","");
		}
	}
	
	$("tr[@id='softwareversion']").show();
	
	var url = "<s:url value='/gtms/stb/resource/stbSetParamACT!getDeviceInfo.action'/>";
	if(deviceId!=""){
		$.post(url,{
			device_id:deviceId
		},
		function(ajax)
		{
			if(ajax!="")
			{
				if(typeof(ajax)&&typeof(ajax.length))
				{
					var deviceData=ajax.split("#");
					
					$("td[@id='vendor_name']").html(deviceData[2]);
					$("td[@id='model_name']").html(deviceData[1]);
					$("td[@id='mac']").html(deviceData[4]);
					$("td[@id='serv_account']").html(deviceData[5]);
					$("td[@id='hard_version']").html(deviceData[6]);
					$("td[@id='soft_version']").html(deviceData[7]);
					
					if("hn_lt"==instArea){
						$("td[@id='apk_version_name']").html(deviceData[8]);
						$("td[@id='epg_version']").html(deviceData[9]);
						$("td[@id='complete_time']").html(deviceData[10]);
						$("td[@id='cpe_currentupdatetime']").html(deviceData[11]);
						$("td[@id='network_type']").html(deviceData[12]);
						$("td[@id='addressing_type']").html(deviceData[13]);
						$("td[@id='loopback_ip']").html(deviceData[14]);
						$("td[@id='public_ip']").html(deviceData[15]);
						$("td[@id='net_type']").html(deviceData[16]);
						$("td[@id='ip_type']").html(deviceData[17]);
						$("td[@id='dev_type']").html(deviceData[18]);
					}
				}
			}
		});
	}else{
		alert("����ѡ���豸��");
	}
}
</SCRIPT>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã���̨�豸���Զ���
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR>
		<td>
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>

	<TR id="softwareversion" style="display: none">
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<table width="100%" class="querytable">
					<TR>
					<%if("9".equals(service_id)){ %>
						<TH colspan="4" class="title_1">�������������Զ���</TH>
					<%}else{ %>
						<TH colspan="4" class="title_1">�����лָ��������ò��Զ���</TH>
					<%} %>
					</TR>
					
					<TR id="trDeviceResult" style="display: none">
						<td nowrap class="title_2" width="15%">�豸���к�</td>
						<td id="tdDeviceSn" width="35%"></td>
						<td nowrap class="title_2" width="15%">����</td>
						<td id="tdDeviceCityName" width="35%"></td>
					</TR>
					<TR id="trDeviceVMResult" style="display: none">
						<td nowrap class="title_2" width="15%">�豸����</td>
						<td id="vendor_name" width="35%"></td>
						<td nowrap class="title_2" width="15%">�ͺ�</td>
						<td id="model_name" width="35%"></td>
					</TR>
					<TR id="trDeviceAccountResult" style="display: none">
						<td nowrap class="title_2" width="15%">MAC��ַ</td>
						<td id="mac" width="35%"></td>
						<td nowrap class="title_2" width="15%">ҵ���˺�</td>
						<td id="serv_account" width="35%"></td>
					</TR>
					<TR id="trDeviceVersionResult" style="display: none">
						<td nowrap class="title_2" width="15%">Ӳ���汾</td>
						<td id="hard_version" width="35%"></td>
						<td nowrap class="title_2" width="15%">����汾</td>
						<td id="soft_version" width="35%"></td>
					</TR>
					
					<ms:inArea areaCode="hn_lt" notInMode="false">
						<TR id="trDeviceApkEpgResult" style="display: none">
							<td nowrap class="title_2" width="15%">APK�汾����</td>
							<td id="apk_version_name" width="35%"></td>
							<td nowrap class="title_2" width="15%">EPG�汾</td>
							<td id="epg_version" width="35%"></td>
						</TR>
						<TR id="trDeviceNetTypeResult" style="display: none">
							<td nowrap class="title_2" width="15%">��ǰ�汾������������</td>
							<td id="net_type" width="35%"></td>
							<td nowrap class="title_2" width="15%">�豸����</td>
							<td id="dev_type" width="35%" colspan="3"></td>
						</TR>
						<TR id="trDeviceLonigTimeResult" style="display: none">
							<td nowrap class="title_2" width="15%">�״ε�½ʱ��</td>
							<td id="complete_time" width="35%"></td>
							<td nowrap class="title_2" width="15%">�����½ʱ��</td>
							<td id="cpe_currentupdatetime" width="35%"></td>
						</TR>
						<TR id="trDeviceNetResult" style="display: none">
							<td nowrap class="title_2" width="15%">��������</td>
							<td id="network_type" width="35%"></td>
							<td nowrap class="title_2" width="15%">��������</td>
							<td id="addressing_type" width="35%"></td>
						</TR>
						<TR id="trDeviceIpResult" style="display: none">
							<td nowrap class="title_2" width="15%">�豸IP</td>
							<td id="loopback_ip" width="35%"></td>
							<td nowrap class="title_2" width="15%">����IP</td>
							<td id="public_ip" width="35%"></td>
						</TR>
						<TR id="trDeviceTypeResult" style="display: none">
							<td nowrap class="title_2" width="15%">IP����</td>
							<td id="ip_type" width="35%"></td>
						</TR>
					</ms:inArea>
					
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" CLASS="foot">
							<div class="right">
								<button id="stbAddStrategy_btn" onclick="ExecMod()">��  ��</button>
							</div>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>
