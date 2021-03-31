<%--
ҵ���ֹ�����
Author: Jason
Version: 1.0.0
Date: 2009-09-22
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<title>ҵ���ֹ�����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--//

/** ��ѯ�û����豸��Ӧ����Ϣ���·������ã�*/
function searchSheetInfo() {
	var queryDevSn = $("input[@name='queryDevSn']");
	var queryUsername = $("input[@name='queryUsername']");
	var queryServTypeId = $("select[@name='queryServTypeId']");
	if('' == queryUsername.val() && '' == queryDevSn.val()){
		alert("�������û��˺Ż��豸���кŽ��в�ѯ");
		queryUsername.focus();
		return false;
	}
	if('' != queryDevSn.val() && queryDevSn.val().length < 6){
		alert("�豸���к�С��6λ");
		queryDevSn.focus();
		return false;
	}
	var gw_type = $("input[@name='gw_type']").val();
	
	var url = "<s:url value='/gwms/config/serviceManSheet!queryServUserInfo.action'/>";
	$.post(url,{
		devSn:queryDevSn.val(),
		username:queryUsername.val(),
		servTypeId:queryServTypeId.val(),
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_sheetInfo']").html("");
		$("div[@id='div_sheetInfo']").append(ajax);
	})
}

//���뷽ʽ
function chgAccessType(v){
	if(1 == v){
		$("td[@id='id_pvc1']").show();
		$("td[@id='id_pvc2']").show();
		$("td[@id='id_vlan1']").hide();
		$("td[@id='id_vlan2']").hide();
	}else{
		$("td[@id='id_pvc1']").hide();
		$("td[@id='id_pvc2']").hide();
		$("td[@id='id_vlan1']").show();
		$("td[@id='id_vlan2']").show();
	}
}

//������ʽ
function chgnetType(v){
	if(2 == v){
		//·��
		$("tr[@id='id_routed']").show();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}else if(3 == v){
		//��̬IP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").show();
		$("tr[@id='id_static2']").show();
	}else{
		//�ŽӺ�DHCP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}
}

//�����·�
function doBusinessSheet(){
	var gw_type = $("input[@name='gw_type']").val();
	
	var servTypeId = $("input[@name='servTypeId']").val();
	var userId = $("input[@name='userId']").val();
	var deviceId = $("input[@name='deviceId']").val();
	var cityId = $("input[@name='cityId']").val();
	var officeId = $("input[@name='officeId']").val();
	var accessType = $("select[@name='accessType']").val();
	var netType = $("select[@name='netType']").val();
	var vlanid = $("input[@name='vlanid']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var pppoeUsername = $("input[@name='pppoeUsername']");
	var pppoePasswd = $("input[@name='pppoePasswd']");
	var ip = $("input[@name='ip']");
	var gateway = $("input[@name='gateway']");
	var mask = $("input[@name='mask']");
	var dns = $("input[@name='dns']");
	var voipUsername = $("input[@name='voipUsername']");
	var voipPasswd = $("input[@name='voipPasswd']");
	
	var oui = $("input[@name='oui']");
	var devSn = $("input[@name='devSn']");
	var hasServUser = $("input[@name='hasServUser']");
	var bindPort = "";
	
	if('' == userId || '' == deviceId || '' == servTypeId){
		alert("�û������豸����ҵ������Ϊ��");
		return false;
	}
	//���뷽ʽ�ж�
	if(1 == accessType){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(vlanid.val(),"VlanID")){
			vlanid.focus();
			return false;
		}
	}
	//������ʽ
	if(2 == netType){
		if(!IsNull(pppoeUsername.val(),"PPPoE�˺�")){
			pppoeUsername.focus();
			return false;
		}
		if(!IsNull(pppoePasswd.val(),"PPPoE����")){
			pppoePasswd.focus();
			return false;
		}
	}else if(3 == netType){
		if(!IsIPAddr2(ip.val(),"IP��ַ")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"����")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"��������")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}
	//voipҵ��
	if('14' == servTypeId){
		if('' == officeId || '' == cityId){
			alert("����, ������Ϊ��");
			return false;
		}
		if(!IsNull(voipUsername.val(),"VOIP��֤�û���")){
			voipUsername.focus();
			return false;
		}
		if(!IsNull(voipPasswd.val(),"VOIP��֤����")){
			voipPasswd.focus();
			return false;
		}
	}
	
	//�����·�
	var url = "<s:url value='/gwms/config/serviceManSheet!doService.action'/>";

	$.post(url,{
		deviceId:deviceId,
		servTypeId:servTypeId,
		userId:userId,
		accessType:accessType,
		netType:netType,
		vpi:vpi.val(),
		vci:vci.val(),
		vlanid:vlanid.val(),
		pppoeUsername:pppoeUsername.val(),
		pppoePasswd:pppoePasswd.val(),
		ip:ip.val(),
		gateway:gateway.val(),
		mask:mask.val(),
		dns:dns.val(),
		voipUsername:voipUsername.val(),
		voipPasswd:voipPasswd.val(),
		cityId:cityId,
		officeId:officeId,
		bindPort:bindPort,
		hasServUser:hasServUser.val(),
		oui:oui.val(),
		devSn:devSn.val(),
		gw_type:gw_type
	},function(ajax){
		//alert(ajax);
		$("button[@name='searchBtn']").disabled = false;
		
		$("tr[@id='resultTR1']").show();
		$("td[@id='resultTD1']").html("ִ�н����");
		
		$("div[@id='result1']").html("֪ͨ��̨");
		if (ajax == "1")
		{
			$("div[@id='result1']").append("<FONT COLOR='green'>�ɹ�</FONT>");
		} else {
			$("div[@id='result1']").append("<FONT COLOR='red'>ʧ��</FONT>");
			if(ajax == "-1"){
				$("div[@id='result1']").append("(������Ϣ����ʧ��)");
			}else if (ajax == "-2"){
				$("div[@id='result1']").append("(ҵ���û���Ϣ����ʧ��)");
			}else if (ajax == "-3"){
				$("div[@id='result1']").append("(����û�ж�Ӧ��VOIP��������Ϣ)");
			}
		}
	});
	$("button[@name='searchBtn']").disabled = true;
	$("button[@name='subBtn']").attr("disabled", true);
}
//-->
</script>
</HEAD>
<body>
<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" value="<s:property value='gw_type' />" name="gw_type" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�ֹ�����</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> �ն�ҵ���·�</td>
					</tr>
				</table>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4">�û��豸��ѯ</TH>
					</TR>
					<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: ">

						<TD class=column align="right" nowrap width="15%">ҵ������:</TD>
						<TD width="35%" id="sheetTypeTd"><SELECT name="queryServTypeId"
							class="bk" >
							<OPTION value="10">����ҵ��</OPTION>
							<OPTION value="103">����ҵ��</OPTION>
							<OPTION value="11">IPTVҵ��</OPTION>
							<!-- <OPTION value="14">VOIPҵ��</OPTION>  -->
						</SELECT></TD>
						<TD width="15%" class=column align="right">��������:</TD>
						<TD width="35%" id="operationTypeTd">����</TD>
					</TR>

					<TR bgcolor="#FFFFFF" STYLE="display: ">
						<TD class=column align="right" width="15%">�û��ʺ�:</TD>
						<TD width="35%"><input type="text" name="queryUsername"
							class="bk" 
							value="<s:property value="queryDevSn"/>"></TD>
						<TD class=column align="right" nowrap width="15%">�豸���к�:</TD>
						<TD width="35%"><input type="text" name="queryDevSn" class="bk"
							value="<s:property value="queryDevSn"/>"> <font color='red'> *�����������λ</font>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD colspan="4" class=foot align="right" nowrap>
						<button type="button" name="searchBtn" onclick="searchSheetInfo();">�� ѯ</button>
						</TD>
					</TR>

				</TABLE>
				</TD>
			</TR>

			<TR align="left" id="wanConnInfo" STYLE="display: ">
				<TD colspan="4" bgcolor=#999999>
					<div id="div_sheetInfo"></div>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>