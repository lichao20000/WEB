<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>VOIP H.248ģ�⹤��</title>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
function CheckForm(){
	var _wanType = $("select[@name='wanType']");	// IP��ȡ��ʽ
	var _username = $("input[@name='username']");   // LOID
	var _devType = $("select[@name='devType']");    // �豸����
	var _cityId = $("select[@name='cityId']");      // ����
	//var _vlanid = $("input[@name='vlanid']");       // vlanid
	//var _vpiid = $("input[@name='vpiid']");
	//var _vciid = $("input[@name='vciid']");
	var _ipaddress = $("input[@name='ipaddress']"); // IP��ַ
	var _ipmask = $("input[name='ipmask']");        //����
	var _gateway = $("input[name='gateway']");      // ����
	var _adslSer = $("input[name='adslSer']");      // DNSֵ
	var _sipIp = $("input[@name='sipIp']");         // ��MGC��������ַ
	var _sipPort = $("input[@name='sipPort']");     // ��MGC�������˿�
	var _standSipIp = $("input[@name='standSipIp']");      // ��MGC��������ַ
	var _standSipPort = $("input[@name='standSipPort']");  // ��MGC�������˿�
	var _regIdType = $("select[@name='regIdType']");       // �ն˱�ʶ����
	var _regId = $("input[@name='regId']");                // �ն˱�ʶ
	var _voipPort = $("select[@name='voipPort']");         // �ն������ʶ
	var _voipTelepone = $("input[@name='voipTelepone']");  // ҵ��绰����
	var _operateType = $("input[@name='operateType']");   
	
	//IP��ȡ��ʽ
	if(_wanType.val() == "-1"){
		alert("��ѡ��IP��ȡ��ʽ");
		_wanType.focus();
		return false;
	}
	//�û��˺�(LOID)
	if(!IsNull(_username.val(), "�û��˺�")){
		_username.focus();
		return false;
	}
	//�豸����
	if('' == _devType.val() || '-1' == _devType.val()){
		alert("��ѡ���豸����");
		_devType.focus();
		return false;
	}
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	// _vlanid
	//if(!IsNull(_vlanid.val(), "vlanid")){
	//	_vlanid.focus();
	//	return false;
	//}
	// vpiid
	//if(!IsNull(_vpiid.val(), "PVC")){
	//	_vpiid.focus();
	//	return false;
	//}
	// vciid
	//if(!IsNull(_vciid.val(), "PVC")){
	//	_vciid.focus();
	//	return false;
	//}
	
	// �� IP��ȡ��ʽ Ϊ"STATIC"ʱ IP��ַ�����롢���ܡ�DNSֵ��Ϊ������
	if(_wanType.val() == "3"){ 
		//IP��ַ
		if(!IsNull(_ipaddress.val(), "IP��ַ")){
			_ipaddress.focus();
			return false;
		}else if(!reg_verify(_ipaddress.val())){
			alert("������Ϸ���IP��ַ��");
			_ipaddress.focus();
			return false;
		}
		// ����
		if(!IsNull(_ipmask.val(), "����")){
			_ipmask.focus();
			return false;
		}else if(!IsMask(_ipmask.val())){
			_ipmask.focus();
			return false;
		}
		// ����
		if(!IsNull(_gateway.val(), "����")){
			_gateway.focus();
			return false;
		}else if(!reg_verify(_gateway.val())){
			alert("������Ϸ������ص�ַ��");
			_gateway.focus();
			return false;
		}
		// DNSֵ
		if(!IsNull(_adslSer.val(), "DNSֵ")){
			_adslSer.focus();
			return false;
		}else if(!reg_verify(_adslSer.val())){
			alert("������Ϸ���DNS��ַ��");
			_adslSer.focus();
			return false;
		}
	}
	
	//��MGC��������ַ
	if(!IsNull(_sipIp.val(), "��MGC��������ַ")){
		_sipIp.focus();
		return false;
	}
	//else if(!reg_verify(_sipIp.val())){
	//	alert("������Ϸ�����MGC��������ַ��");
	//	_sipIp.focus();
	//	return false;
	//}
	//��MGC�������˿�
	if(!IsNull(_sipPort.val(), "��MGC�������˿�")){
		_sipPort.focus();
		return false;
	}else if(!IsNumber(_sipPort.val(),"��MGC�������˿�")){
		_sipPort.focus();
		return false;
	}
	//��MGC��������ַ
	if(!IsNull(_standSipIp.val(), "��MGC��������ַ")){
		_standSipIp.focus();
		return false;
	}
	//else if(!reg_verify(_standSipIp.val())){
	//	alert("������Ϸ��ı�MGC��������ַ��");
	//	_standSipIp.focus();
	//	return false;
	//}
	//��MGC�������˿�
	if(!IsNull(_standSipPort.val(), "��MGC�������˿�")){
		_standSipPort.focus();
		return false;
	}else if(!IsNumber(_standSipPort.val(),"��MGC�������˿�")){
		_standSipPort.focus();
		return false;
	}
	//�ն˱�ʶ����
	if('' == _regIdType.val() || '-1' == _regIdType.val()){
		alert("��ѡ���ն˱�ʶ����");
		_regIdType.focus();
		return false;
	}
	//�ն˱�ʶ
	if(!IsNull(_regId.val(), "�ն˱�ʶ")){
		_regId.focus();
		return false;
	}
	// �ն������ʶ
	if('' == _voipPort.val() || '-1' == _voipPort.val()){
		alert("��ѡ���ն������ʶ");
		_voipPort.focus();
		return false;
	}
	//ҵ��绰����
	if(!IsNull(_voipTelepone.val(), "ҵ��绰����")){
		_voipTelepone.focus();
		return false;
	}
	if(hasUsername == 0){
		alert("��������ȷ��LOID");
		return false;
	}
	//document.frm.submit();
	var url = "<s:url value='/itms/service/simulateSheetNx!sendSheet.action'/>";
	
	$.post(url,{
		wanType:_wanType.val(),     // IP��ȡ��ʽ
		username:_username.val(),   // LOID
		devType:_devType.val(),     // �豸����
		cityId:_cityId.val(),       // ����
		//vlanid:_vlanid.val(),       // vlanid
		//vpiid:_vpiid.val(),
		//vciid:_vciid.val(),
		ipaddress:_ipaddress.val(),  // IP��ַ
		ipmask:_ipmask.val(),        //����
		gateway:_gateway.val(),      // ����
		adslSer:_adslSer.val(),      // DNSֵ
		mgcIp:_sipIp.val(),          // ��MGC��������ַ
		mgcPort:_sipPort.val(),      // ��MGC�������˿�
		standMgcIp:_standSipIp.val(),      // ��MGC��������ַ
		standMgcPort:_standSipPort.val(),  // ��MGC�������˿�
		regIdType:_regIdType.val(),        // �ն˱�ʶ����
		regId:encodeURIComponent(_regId.val()), // �ն˱�ʶ
		voipPort:_voipPort.val(),        	    // �ն������ʶ
		voipTelepone:_voipTelepone.val(),   // ҵ��绰����
		operateType:_operateType.val()
	},function(ajax){
		alert(ajax);
		window.location.reload();
		//window.close();
	});
	
}

/* reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
������򷵻�true�����򣬷���false��*/
function reg_verify(addr){
	//������ʽ
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)) {
    	//IP��ַ��ȷ
        return true;
    } else {
    	//IP��ַУ��ʧ��
         return false;
    }
}




var hasUsername = 0;

function checkUserInfo(){
	var _username = $("input[@name='username']").val();
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url, {
		username : _username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1"){
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
			//$("tr[@id='vlanid']").css("display","none");
			//$("tr[@id='pvc']").css("display","none");
		}else{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}

function hiddenElements(){
	var _wanType = $("select[@name='wanType']");	// IP��ȡ��ʽ
	
	if(_wanType.val() == "-1" || _wanType.val() == "3"){
		$("tr[@id='tr01']").css("display","");
		$("tr[@id='tr02']").css("display","");
	}else{
		$("tr[@id='tr01']").css("display","none");
		$("tr[@id='tr02']").css("display","none");
	}
}

function checkInfo(){
	var _regIdType = $("select[@name='regIdType']");       // �ն˱�ʶ����
	var _regId = $("input[@name='regId']");                // �ն˱�ʶ
	
	if("0" == _regIdType.val() && "" != _regId.val()){
		if(!reg_verify(_regId.val())){
			alert("������Ϸ���IP��ַ��");
			_regId.focus();
			return false;
		}
	}
}


</script>
</head>
<body onLoad="hiddenElements()">
<!-- <form name="frm" action="<s:url value='/itms/service/simulateSheetNx!sendSheet.action'/>" method="post"> -->
<form name="frm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4">
						
							<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
							<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
							
							<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
								<TR>
									<TD><font size="2">VOIP(H248)������Ϣ</font></TD>
									<TD align="right"><IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">IP��ȡ��ʽ</TD>
						<TD width="29%">
							<select name="wanType" class="bk" onChange="hiddenElements()">
								<option value="-1">==��ѡ���������==</option>
								<option value="3">==STATIC==</option>
								<option value="4">==DHCP==</option>
							</select>&nbsp;<font color="#FF0000">*</font>
					  </TD>
						<TD class=column align="right" nowrap>LOID</TD>
						<TD><INPUT TYPE="text" NAME="username"
							onblur="checkUserInfo()" maxlength=50 class=bk value="">&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>

					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">�豸����</TD>
						<TD width="29%"><select name="devType" class="bk">
							<option selected value="e8c">==E8-C==</option>
					  </select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="31%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
					  &nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF" style="display:none"> <!-- �˴���tr��ʱ���� -->
						<TD class=column align="right" nowrap>vlanid</TD>
						<TD><INPUT TYPE="text" NAME="vlanid" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>PVC</TD>
						<TD><INPUT NAME="vpiid" TYPE="text" title="vpiid"
							class=bk value="" size="8">&nbsp;/&nbsp;<INPUT NAME="vciid" TYPE="text" title="vciid"
							class=bk value="" size="8">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr01" style="display: none">
						<TD class=column align="right" nowrap>IP��ַ</TD>
						<TD><INPUT TYPE="text" NAME="ipaddress" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>����</TD>
						<TD><INPUT TYPE="text" NAME="ipmask" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr02" style="display: none">
						<TD class=column align="right" nowrap>����</TD>
						<TD><INPUT TYPE="text" NAME="gateway" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>DNSֵ</TD>
						<TD><INPUT TYPE="text" NAME="adslSer" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��MGC��������ַ</TD>
						<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��MGC�������˿�</TD>
						<TD><INPUT TYPE="text" NAME="sipPort" maxlength=5 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��MGC��������ַ</TD>
						<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��MGC�������˿�</TD>
						<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=5
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�ն˱�ʶ����</TD>
						<TD><select name="regIdType" class="bk">
							<option value="-1">==��ѡ���ʶ����==</option>
							<option value="0">==IP��ַ==</option>
							<option value="1">==����==</option>
							<option value="2">==�豸��==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>�ն˱�ʶ</TD>
						<TD><INPUT TYPE="text" NAME="regId" maxlength=20 onblur="checkInfo()"
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�ն������ʶ</TD>
						<TD><select name="voipPort" class="bk">
							<option value="-1">==��ѡ�������ʶ==</option>
							<option value="A1">==A1==</option>
							<option value="A2">==A2==</option>
							<option value="AL1">==AL1==</option>
							<option value="AL2">==AL2==</option>
							<option value="AG58900">==AG58900==</option>
							<option value="AG58901">==AG58901==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>ҵ��绰����</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<button onClick="CheckForm()">&nbsp;��&nbsp;&nbsp;��&nbsp;</button>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
</html>