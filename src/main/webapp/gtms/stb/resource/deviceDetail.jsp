<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ϸ��Ϣ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">�豸��<s:property value="deviceDetailMap.device_serialnumber"/>����ϸ��Ϣ</TH>
	</TR>
	<TR height="20">
		<TD class="title_3" colspan=4>�豸������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" >�豸ID</TD>
		<TD width="25%"><s:property value="deviceDetailMap.device_id"/></TD>
		<TD class="title_2" >�豸�ͺ�</TD>
		<TD width="40%"><s:property value="deviceDetailMap.device_model"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >�豸����</TD>
		<TD><s:property value="deviceDetailMap.vendor_add"/></TD>
		<TD class="title_2" >���к�</TD>
		<TD><s:property value="deviceDetailMap.device_serialnumber"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >Ӳ���汾</TD>
		<TD><s:property value="deviceDetailMap.hardwareversion"/></TD>
		<TD class="title_2" >�ر�汾</TD>
		<TD><s:property value="deviceDetailMap.specversion"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >����汾</TD>
		<TD><s:property value="deviceDetailMap.softwareversion"/></TD>
		<TD class="title_2" >�豸����</TD>
		<TD><s:property value="deviceDetailMap.device_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >������</TD>
		<TD width=140><s:property value="deviceDetailMap.maxenvelopes"/></TD>
		<TD class="title_2" >MAC ��ַ</TD>
		<TD><s:property value="deviceDetailMap.cpe_mac"/></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>�豸��̬��Ϣ</TD>
	</TR>
	<%-- <TR >
		<TD class="title_2" >�豸����״̬</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR> --%>
	<TR >
		<TD class="title_2" >�豸����״̬</TD>
		<TD colspan=3>
			<span id='onlineStatus'><s:property value="deviceDetailMap.online_status"/></span>&nbsp;&nbsp;&nbsp;
			<button name="onlineStatusGet" type="button" onclick="getOnlineStatus()" >�������״̬</button>
		</TD>
	</TR>

	<TR >
		<TD class="title_2" >�豸����</TD>
		<TD><s:property value="deviceDetailMap.city_name"/></TD>
		<TD class="title_2" >IP��ַ</TD>
		<TD><s:property value="deviceDetailMap.loopback_ip"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ע��ϵͳʱ��</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<TD class="title_2" >�������ʱ��</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
	<%-- 
	<TR >
		<TD class="title_3"  colspan="4">��ǰ���ò���</TD>
	</TR>
	<TR >
		<TD class="title_2" >CPE�û���</TD>
		<TD><s:property value="deviceDetailMap.cpe_username"/></TD>
		<TD class="title_2" >CPE����</TD>
		<TD><s:property value="deviceDetailMap.cpe_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ACS�û���</TD>
		<TD><s:property value="deviceDetailMap.acs_username"/></TD>
		<TD class="title_2" >ACS����</TD>
		<TD><s:property value="deviceDetailMap.acs_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >����ά���˺�</TD>
		<TD><s:property value="deviceDetailMap.x_com_username"/></TD>
		<TD class="title_2" >����ά������</TD>
		<TD><s:property value="deviceDetailMap.x_com_passwd"/></TD>
	</TR>
	 --%>
	<TR >
		<TD class="title_3"  colspan="4">��������ʷ������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" >�ɼ�ʱ��</TD>
		<TD><s:property value="deviceDetailMap.time"/></TD>
		<TD class="title_2" >���뷽ʽ</TD>
		<TD><s:property value="deviceDetailMap.addressingType"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >PPPOE�ʺ�</TD>
		<TD><s:property value="deviceDetailMap.PPPOE"/></TD>
		<TD class="title_2" >PPPOE����</TD>
		<TD><s:property value="deviceDetailMap.PPPoEPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >DHCP�˺�</TD>
		<TD><s:property value="deviceDetailMap.DHCPID"/></TD>
		<TD class="title_2" >DHCP����</TD>
		<TD><s:property value="deviceDetailMap.DHCPPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ҵ���˺�</TD>
		<TD><s:property value="deviceDetailMap.userID"/></TD>
		<TD class="title_2" >ҵ������</TD>
		<TD><s:property value="deviceDetailMap.userPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >��֤��������ַ</TD>
		<TD><s:property value="deviceDetailMap.authURL"/></TD>
		<TD class="title_2" >���·�������ַ</TD>
		<TD><s:property value="deviceDetailMap.autoUpdateServer"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >����ģʽ</TD>
		<TD><s:property value="deviceDetailMap.aspectRatio"/></TD>
		<TD class="title_2" >��Ƶ�����ʽ</TD>
		<TD><s:property value="deviceDetailMap.compositeVideo"/></TD>
	</TR>
	<TR >
		<TD class="title_3" colspan=4>�û�������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" >ҵ���ʺ�</TD>
		<TD><s:property value="deviceDetailMap.serv_account"/></TD>
		<TD class="title_2" >�ͻ�����</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
	<input type="hidden" id="device_id" value="<s:property value='deviceDetailMap.device_id'/>" />
</TABLE>
<script type="text/javascript">
function getOnlineStatus()
{
	//$("input[@name='time_start']")
	$("span#onlineStatus").html("<font color='blue'>���ڻ�ȡ�豸����״̬</font>");
	
	var device_id = document.getElementById("device_id").value;
	var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
	var result = "";
	
	$.post(url, {
		deviceId : device_id
	}, function(ajax) {
		var flag = parseInt(ajax);
		if(flag == 1){
			result = "<font color='green'>�豸����(ʵʱ)</font>";
		}else if (flag == 0){
			result = "<font color='red'>�豸����(ʵʱ)</font>";
		}
		else if (flag == -1){
			result = "<font color='red'>�豸����(ʵʱ)</font>";
		}
		else if (flag == -2){
			result = "<font color='red'>�豸����(ʵʱ)</font>";
		}
		else if (flag == -3){
			result = "<font color='green'>�豸����(ʵʱ)</font>";
		}
		else if (flag == -4){
			result = "<font color='red'>�豸����(ʵʱ)</font>";
		}
		else {
			result = "<font color='red'>�豸����(ʵʱ)</font>";
		}
		$("span#onlineStatus").html(result);
	});
	
}

</script>
</body>