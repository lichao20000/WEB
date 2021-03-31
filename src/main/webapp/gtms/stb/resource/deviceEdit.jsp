<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������Ϣ�༭</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("select[@name='cityId']").val('<s:property value="deviceDetailMap.city_id"/>');
});

function check_mac(e1)
{
     chkstr=e1;
         var pattern="/^([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}|([0-9A-Fa-f]{2})(:[0-9A-Fa-f]{2}){5}/";
     eval("var pattern=" + pattern);
     var add_p1 = pattern.test(chkstr);
   
     if(add_p1==false)
     {
     alert("�������MAC��ַ����ȷ��");
     }
     return add_p1;
}


function edit(){
	var deviceId = trim($("input[@name='deviceId']").val());
	var cityId = trim($("select[@name='cityId']").val());
	var cpeMac = trim($("input[@name='cpeMac']").val());
	var deviceIp = trim($("input[@name='deviceIp']").val());
	if(""!=cpeMac){
		$("input[@name='cpeMac']").val(cpeMac);
		if(!check_mac(cpeMac)){
			return false;
		}
	}
	
	if(""==deviceIp){
		alert("������IP��ַ��");
		return false;
	}else{
		if(!checkip(deviceIp)){
			alert("��������ȷ��IP��ַ��");
			return false;
		}else{
			$("input[@name='deviceIp']").val(deviceIp);
		}
	}
	var url = '<s:url value="/gtms/stb/resource/gwDeviceQueryStb!edit.action"/>';
	$.post(url,{
		deviceId:deviceId,
		cityId:cityId,
		cpeMac:cpeMac,
		deviceIp:deviceIp
	},function(ajax){
	    alert(ajax);
	    window.close();
	});
}


/*------------------------------------------------------------------------------
//������:		checkip
//����  :	str �������ַ���
//����  :	���ݴ���Ĳ����ж��Ƿ�Ϊ�Ϸ���IP��ַ
//����ֵ:		true false
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function checkip(str){
	var pattern=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	return pattern.test(str);
}

/*------------------------------------------------------------------------------
//������:		trim
//����  :	str �������ַ���
//����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
//����ֵ:		trim��str��
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}

</SCRIPT>

</head>
<body>
<form name="selectForm"
		action="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!edit.action"/>">
		<input type="hidden" name="deviceId" value="<s:property value="deviceDetailMap.device_id"/>"/>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">�豸��<s:property value="deviceDetailMap.device_serialnumber"/>���༭</TH>
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
		<TD><input type="text" name="cpeMac" value="<s:property value="deviceDetailMap.cpe_mac"/>" maxlength="17"/> </TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>�豸��̬��Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" >�豸����״̬</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR>

	<TR >
		<TD class="title_2" >�豸����</TD>
		<TD>
			<select name="cityId" >
				<s:iterator value="cityList">
					<option value="<s:property value="city_id"/>">
						<s:property value="city_name"/>
					</option>
				</s:iterator>
			</select>
		</TD>
		<TD class="title_2" >IP��ַ</TD>
		<TD><input type="text" name="deviceIp" value="<s:property value="deviceDetailMap.loopback_ip"/>" maxlength="15"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ע��ϵͳʱ��</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<TD class="title_2" >�������ʱ��</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
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
	<TR >
		<TD class="title_3" colspan=4>�û�������Ϣ</TD>
	</TR>
	<TR >
		<TD class="title_2" >�û��ʺ�</TD>
		<TD><s:property value="deviceDetailMap.cust_account"/></TD>
		<TD class="title_2" >�ͻ�����</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:edit();"> �� �� </button>
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
</TABLE>
</form>
</body>