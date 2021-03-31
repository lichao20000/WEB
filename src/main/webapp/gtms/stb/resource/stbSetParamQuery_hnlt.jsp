<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function deviceResult(returnVal)
{
	var deviceId = "";
	for(var i=0;i<returnVal[2].length;i++)
	{
		deviceId = returnVal[2][i][0];
		$("tr[@id='trDeviceResult']").css("display","");
		$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		
		$("tr[@id='trDeviceVMResult']").css("display","");
		$("tr[@id='trDeviceAccountResult']").css("display","");
		$("tr[@id='trDeviceAuthServer']").css("display","");
		$("tr[@id='trDeviceAuthServerConnPeroid']").css("display","");
		$("tr[@id='trDeviceZeroConfServer']").css("display","");
		$("tr[@id='trDeviceCmdServer']").css("display","");
		$("tr[@id='trDeviceNetServer']").css("display","");
		$("tr[@id='trDeviceAutoSleep']").css("display","");
		$("tr[@id='trDeviceIpProtocolVersion']").css("display","");
		$("tr[@id='trDeviceUpdateTime']").css("display","");
	}
	
	$("tr[@id='zeroConfShow']").show();
	var url = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConfParam.action'/>";
	if(deviceId!=""){
		$.post(url,{
			deviceId:deviceId
		},function(ajax){
			if(ajax!="")
			{
				var data=ajax.split("#");
				
				$("td[@id='vendor_name']").html(data[0]);
				$("td[@id='model_name']").html(data[1]);
				$("td[@id='mac']").html(data[2]);
				$("td[@id='serv_account']").html(data[3]);
				$("td[@id='auth_server']").html(data[4]);
				$("td[@id='auth_server_bak']").html(data[5]);
				if(data[6]==""){
					$("td[@id='auth_server_conn_peroid']").html("");
				}else{
					$("td[@id='auth_server_conn_peroid']").html(data[6]+"��");
				}
				
				$("td[@id='zeroconf_server']").html(data[7]);
				$("td[@id='zeroconfig_server_bak']").html(data[8]);
				$("td[@id='cmd_server']").html(data[9]);
				$("td[@id='cmd_server_bak']").html(data[10]);
				if(data[11]==""){
					$("td[@id='cmd_server_conn_peroid']").html("");
				}else{
					$("td[@id='cmd_server_conn_peroid']").html(data[11]+"��");
				}
				
				$("td[@id='ntp_server_main']").html(data[12]);
				$("td[@id='ntp_server_bak']").html(data[13]);
				
				if(data[14]=="0"){
					$("td[@id='auto_sleep_mode']").html("��");
				}else if(data[14]=="1"){
					$("td[@id='auto_sleep_mode']").html("��");
				}else{
					$("td[@id='auto_sleep_mode']").html("");
				}
				
				$("td[@id='auto_sleep_time']").html(getSecondTime(data[15]));
				$("td[@id='ip_protocol_version_lan']").html(getProtocolVersion(data[16]));
				$("td[@id='ip_protocol_version_wifi']").html(getProtocolVersion(data[17]));
				
				$("td[@id='update_time']").html(data[18]);
				$("td[@id='set_stb_time']").html(data[19]);
			}
		});
	}
}

//ƴ��ʱ����
function getSecondTime(time)
{
	if(time==""){
		return "";
	}else if(time=="3600"){
		return "1Сʱ";
	}else if(time=="1800"){
		return "30����";
	}else if(time=="600"){
		return "10����";
	}else if(time=="300"){
		return "5����";
	}else if(time=="180"){
		return "3����";
	}else if(time=="60"){
		return "1����";
	}else if(time=="30"){
		return "30��";
	}else{
		return time+"��";
	}
}

//��ȡ��������
function getProtocolVersion(type)
{
	if(type=="1"){
		return "IPv4";
	}else if(type=="2"){
		return "IPv6";
	}else if(type=="3"){
		return "IPv4/v6";
	}else{
		return "";
	}
}
</SCRIPT>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã���̨�豸�����·����ò�ѯ
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</tr>

	<TR id="zeroConfShow" style="display: none">
		<TD>
			<table width="100%" class="querytable">
				<TR>
					<TH colspan="4" class="title_1">�������·��������ò�ѯ</TH>
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
					
				<TR id="trDeviceAuthServer" style="display: none">
					<td nowrap class="title_2" width="15%">����֤�����ַ</td>
					<td id="auth_server" width="35%"></td>
					<td nowrap class="title_2" width="15%">����֤�����ַ</td>
					<td id="auth_server_bak" width="35%"></td>
				</TR>
				<TR id="trDeviceAuthServerConnPeroid" style="display: none">
					<td nowrap class="title_2" width="15%">��֤������������</td>
					<td id="auth_server_conn_peroid" width="35%"></td>
					<td nowrap class="title_2" width="15%">���������������</td>
					<td id="cmd_server_conn_peroid" width="35%"></td>
				</TR>
				<TR id="trDeviceZeroConfServer" style="display: none">
					<td nowrap class="title_2" width="15%">����������ַ</td>
					<td id="zeroconf_server" width="35%"></td>
					<td nowrap class="title_2" width="15%">����������ַ</td>
					<td id="zeroconfig_server_bak" width="35%"></td>
				</TR>
				<TR id="trDeviceCmdServer" style="display: none">
					<td nowrap class="title_2" width="15%">����������ַ</td>
					<td id="cmd_server" width="35%"></td>
					<td nowrap class="title_2" width="15%">����������ַ</td>
					<td id="cmd_server_bak" width="35%"></td>
				</TR>
				<TR id="trDeviceNetServer" style="display: none">
					<td nowrap class="title_2" width="15%">��ʱ��ͬ�������ַ</td>
					<td id="ntp_server_main" width="35%"></td>
					<td nowrap class="title_2" width="15%">��ʱ��ͬ�������ַ</td>
					<td id="ntp_server_bak" width="35%"></td>
				</TR>
				
				<TR id="trDeviceAutoSleep" style="display: none">
					<td nowrap class="title_2" width="15%">�Զ���������</td>
					<td id="auto_sleep_mode" width="35%"></td>
					<td nowrap class="title_2" width="15%">�Զ������ر�ʱ��</td>
					<td id="auto_sleep_time" width="35%"></td>
				</TR>
				<TR id="trDeviceIpProtocolVersion" style="display: none">
					<td nowrap class="title_2" width="15%">������������</td>
					<td id="ip_protocol_version_lan" width="35%"></td>
					<td nowrap class="title_2" width="15%">������������</td>
					<td id="ip_protocol_version_wifi" width="35%"></td>
				</TR>
				<TR id="trDeviceUpdateTime" style="display: none">
					<td nowrap class="title_2" width="15%">����޸�ʱ��</td>
					<td id="update_time" width="35%"></td>
					<td nowrap class="title_2" width="15%">����������·���Ч��ʱ��</td>
					<td id="set_stb_time" width="35%"></td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
