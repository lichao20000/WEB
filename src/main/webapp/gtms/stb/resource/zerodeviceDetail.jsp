<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����û�������ϸ��Ϣ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>
</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="5" class="title_1">�豸��<s:property value="deviceDetailMap.device_serialnumber"/>����ϸ��Ϣ</TH>
	</TR>
	<TR height="20">
		<TD class="title_3" colspan=5>�豸������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>�豸ID</TD>
		<TD width="25%"><s:property value="deviceDetailMap.device_id"/></TD>
		<TD class="title_2" >�豸�ͺ�</TD>
		<TD width="40%"><s:property value="deviceDetailMap.device_model"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>�豸����(OUI)</TD>
		<TD><s:property value="deviceDetailMap.vendor_add"/>(<s:property value="deviceDetailMap.oui"/>)</TD>
		<TD class="title_2" >���к�</TD>
		<TD><s:property value="deviceDetailMap.device_serialnumber"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>Ӳ���汾</TD>
		<TD><s:property value="deviceDetailMap.hardwareversion"/></TD>
		<TD class="title_2" >�ر�汾</TD>
		<TD><s:property value="deviceDetailMap.specversion"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>����汾</TD>
		<TD><s:property value="deviceDetailMap.softwareversion"/></TD>
		<TD class="title_2" >�豸����</TD>
		<TD><s:property value="deviceDetailMap.device_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>������</TD>
		<TD width=140><s:property value="deviceDetailMap.maxenvelopes"/></TD>
		<TD class="title_2" >MAC ��ַ</TD>
		<TD><s:property value="deviceDetailMap.cpe_mac"/></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=5>�豸��̬��Ϣ</TD>
	</TR>
	<!-- <TR >
		<TD class="title_2" >�豸����״̬</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR> -->
	<TR >
		<TD class="title_2" colspan=2>�豸����״̬</TD>
		<TD>
			<span id='onlineStatus'><s:property value="deviceDetailMap.online_status"/></span>&nbsp;&nbsp;&nbsp;
			<button name="onlineStatusGet" type="button" onclick="getOnlineStatus(<s:property value='gw_type'/>)" >�������״̬</button>
		</TD>
		<!--
		<TD class="title_2" >�豸״̬</TD>
		<TD><s:property value="deviceDetailMap.status"/></TD> -->
		<TD class="title_2" >�������ʱ��</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>�豸����</TD>
		<TD><s:property value="deviceDetailMap.city_name"/></TD>
		<TD class="title_2" >IP��ַ</TD>
		<TD><s:property value="deviceDetailMap.loopback_ip"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>ע��ϵͳʱ��</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<s:if test="%{instAreaName=='xj_dx'}">
			<TD class="title_2" >�������ʲ�</TD>
			<TD><s:property value="deviceDetailMap.isTelDev"/></TD>
		</s:if>
		<s:if test="%{instAreaName=='jx_dx'}">
			<TD class="title_2" >IPV6��ַ</TD>
			<TD><s:property value="deviceDetailMap.loopback_ip_six"/></TD>
		</s:if>
		<s:else>
			<TD class="title_2" ></TD>
			<TD></TD>
		</s:else>
	</TR>
	<TR >
		<TD class="title_3"  colspan="5">��ǰ���ò���</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>CPE�û���</TD>
		<TD><s:property value="deviceDetailMap.cpe_username"/></TD>
		<TD class="title_2" >CPE����</TD>
		<TD><s:property value="deviceDetailMap.cpe_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>ACS�û���</TD>
		<TD><s:property value="deviceDetailMap.acs_username"/></TD>
		<TD class="title_2" >ACS����</TD>
		<TD><s:property value="deviceDetailMap.acs_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2><% if("CUC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){ out.print("��ͨ");}else if("CTC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){out.print("����");}else{out.print("�ƶ�");}%>ά���˺�</TD>
		<TD><s:property value="deviceDetailMap.x_com_username"/></TD>
		<TD class="title_2" ><% if("CUC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){ out.print("��ͨ");}else if("CTC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){out.print("����");}else{out.print("�ƶ�");}%>ά������</TD>
		<TD><s:property value="deviceDetailMap.x_com_passwd"/></TD>
	</TR>
	<%--
	<TR >
		<TD class="title_3"  colspan="5">��������ʷ������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>�ɼ�ʱ��</TD>
		<TD><s:property value="deviceDetailMap.time"/></TD>
		<TD class="title_2" >���뷽ʽ</TD>
		<TD><s:property value="deviceDetailMap.addressingType"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>PPPOE�ʺ�</TD>
		<TD><s:property value="deviceDetailMap.PPPOE"/></TD>
		<TD class="title_2" >PPPOE����</TD>
		<TD><s:property value="deviceDetailMap.PPPoEPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>DHCP�˺�</TD>
		<TD><s:property value="deviceDetailMap.DHCPID"/></TD>
		<TD class="title_2" >DHCP����</TD>
		<TD><s:property value="deviceDetailMap.DHCPPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>ҵ���˺�</TD>
		<TD><s:property value="deviceDetailMap.userID"/></TD>
		<TD class="title_2" >ҵ������</TD>
		<TD><s:property value="deviceDetailMap.userPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>��֤��������ַ</TD>
		<TD><s:property value="deviceDetailMap.authURL"/></TD>
		<TD class="title_2" >���·�������ַ</TD>
		<TD><s:property value="deviceDetailMap.autoUpdateServer"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>����ģʽ</TD>
		<TD><s:property value="deviceDetailMap.aspectRatio"/></TD>
		<TD class="title_2" >��Ƶ�����ʽ</TD>
		<TD><s:property value="deviceDetailMap.compositeVideo"/></TD>
	</TR>--%>
	<TR >
		<TD class="title_3" colspan=5>�û�������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>ҵ���ʺ�</TD>
		<TD><s:property value="deviceDetailMap.serv_account"/></TD>
		<TD class="title_2" >�ͻ�����</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>��������</TD>
		<TD colspan=3><s:property value="deviceDetailMap.addressing_type"/></TD>
	</TR>

	<s:if test="instAreaName=='sd_lt'">
		<tr colspan=5>
			<table width="98%" class="querytable" align="center" >
				<TR height="20">
					<TD class="title_3" colspan=7>��������</TD>
				</TR>
				<TR>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV��������ʺ�</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV�˺�</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">�����Ĭ������1</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">�����Ĭ������2</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">MAC��ַ</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">STBҵ����뷽ʽ</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">LOID</TD>
				</TR>
				<s:if test="zerogwInfoList.size>0">
					<s:iterator var="list" value="zerogwInfoList">
						<TR>
							<td style="text-align:center;"><s:property value="#list.pppoe_user" /></td>
							<td style="text-align:center;"><s:property value="#list.serv_account" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url1" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url12" /></td>
							<td style="text-align:center;"><s:property value="#list.cpe_mac" /></td>
							<td style="text-align:center;"><s:property value="#list.addressing_type" /></td>
							<td style="text-align:center;"><s:property value="#list.username" /></td>
						</TR>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=7>
							<div style="text-align: center">��ѯ������</div>
						</td>
					</tr>
				</s:else>
			</table>
		</tr>
	</s:if>
	<s:elseif test="instAreaName=='jl_dx'">
	</s:elseif>
	<s:elseif test="instAreaName=='jx_dx'">
	<tr colspan=5>
	<table width="98%" class="querytable" align="center" border=0 cellspacing=0 cellpadding=0>
		<TR height="20">
			<TD class="title_3" colspan=11>������������Ϣ</TD>
		</TR>
		<TR style="background-image: url(../../../images/green_title_bg.jpg);" class="zero_3_table">
		    <td align="center">����</td>
		    <td align="center">���̱��</td>
		    <td align="center">ҵ���˺�</td>
			<td align="center">�豸���к�</td>
			<td align="center">��ʼʱ��</td>
			<td align="center">����ʱ��</td>
			<td align="center">ҵ������</td>
			<td align="center">�󶨷�ʽ</td>
			<td align="center">״̬</td>
			<td align="center" colspan="2">���������ý��</td>
		</TR>
		<s:if test="zerogwInfoList.size>0">
			<s:iterator var="list" value="zerogwInfoList">
				<tr  class="zero_3_table">
				    <td><s:property value="#list.city_name" /></td>
				    <td><s:property value="#list.buss_id" /></td>
				    <td><s:property value="#list.serv_account" /></td>
					<td><s:property value="#list.device_serialnumber" /></td>
					<td><s:property value="#list.start_time" /></td>
					<td><s:property value="#list.fail_time" /></td>
					<td><s:property value="#list.bind_type" /></td>
					<td><s:property value="#list.bind_way" /></td>
					<td><s:property value="#list.fail_reason_id" /></td>
					<td colspan="2"><s:property value="#list.fail_reason_id" />,<s:property value="#list.reason_desc" />(����ֵ[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr  class="zero_3_table">
				<td colspan="11">
					<div style="text-align: center">��ѯ������</div>
				</td>
			</tr>
		</s:else>
		<s:if test="zerogwInfoList.size==3">
			<tr>
				<td colspan="11" align="center">
				    <input type="hidden" name="deviceId" value='<s:property value="deviceId"/>'/>
					<a id="moreZeroConfig" href="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!querygwZeroDetailPage.action?deviceId="/><s:property value="deviceId"/>"
					 target="moreDataForm">�鿴������ʷ����</a>
					 <a id="backZeroConfig" style="display: none;" href="javascript:void(0)" onclick="backZeroConfig()">���������ʷ����</a>
				</td>
			</tr>
		</s:if>
	</table>
	</tr>
	<iframe id="moreDataForm" name="moreDataForm" width="98%" frameborder="0" scrolling="no" align="center" style="display: none;"></iframe>
	</s:elseif>
	<s:else>
	<s:if test="instAreaName!='hb_lt' && instAreaName!='sx_lt'">
		<TR height="20">
			<TD class="title_3" colspan=5>������������Ϣ</TD>
		</TR>
		<TR>
			<TD class="title_2">ҵ���ʺ�</TD>
			<TD class="title_2" colspan="2">ʱ��</TD>
			<TD class="title_2" colspan="2">���������ý��</TD>
		</TR>
		<s:if test="zerogwInfoList.size>0">
			<s:iterator var="list" value="zerogwInfoList">
				<tr>
					<td><s:property value="#list.serv_account" /></td>
					<td colspan="2"><s:property value="#list.fail_time" /></td>
					<td colspan="2"><s:property value="#list.reason_desc" />(����ֵ[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="5">
					<div style="text-align: center">��ѯ������</div>
				</td>
			</tr>
		</s:else>
	</s:if>
	</s:else>
	<TR>
		<TD colspan="5" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
	<input type="hidden" id="device_id" value="<s:property value='deviceDetailMap.device_id'/>" />
</TABLE>
<script type="text/javascript">
function getOnlineStatus(gw_type)
{
	//$("input[@name='time_start']")
	$("span#onlineStatus").html("<font color='blue'>���ڻ�ȡ�豸����״̬</font>");

	var device_id = document.getElementById("device_id").value;
	var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
	var result = "";

	$.post(url, {
		deviceId:device_id,
		gw_type:gw_type
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

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["moreDataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize()
{
	$(".zero_3_table").css("display","none");
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

	// ���ظ�����ʷ���ӣ���ʾ���������ʷ����
	$("#moreZeroConfig").css("display","none");
	$("#backZeroConfig").css("display","");
}

function backZeroConfig(){
	$("#moreZeroConfig").css("display","");
	$("#backZeroConfig").css("display","none");
	$("#moreDataForm").css("display","none");
	$(".zero_3_table").css("display","");
}
</script>
</body>
