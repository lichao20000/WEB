<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSSģ�⹤��</title>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	$("input[name='ProxyServerPort']").val(5060);
	$("input[name='ProxyServerPort_a']").val(5060);
	$("input[name='RegistrarServerPort']").val(5060);
	$("input[name='RegistrarServerPort_a']").val(5060);
	$("input[name='OutboundProxyPort']").val(5060);
	$("input[name='OutboundProxyPort_a']").val(5060);
	$("input[name='RegistrarServer']").val("nx.ctcims.cn");
	$("input[name='RegistrarServer_a']").val("nx.ctcims.cn");
});
var isHasUsername = 0;
function CheckForm(){
	var servTypeId = $("input[@name='servTypeId']");
	var operateType = $("input[@name='operateType']");
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _bussinessTel = $("input[@name='bussinessTel']");
	var _cityId = $("select[@name='cityId']");
	var _voipAccount = $("input[@name='voipAccount']");
	var _voipPassword = $("input[@name='voipPassword']");
	var _ProxyServer = $("input[@name='ProxyServer']");
	var _ProxyServerPort = $("input[@name='ProxyServerPort']");
	var _ProxyServer_a = $("input[@name='ProxyServer_a']");
	var _ProxyServerPort_a = $("input[@name='ProxyServerPort_a']");
	var _voiceport = $("input[@name='voiceport']");
	var _RegistrarServer = $("input[@name='RegistrarServer']");
	var _RegistrarServerPort = $("input[@name='RegistrarServerPort']");
	var _RegistrarServer_a = $("input[@name='RegistrarServer_a']");
	var _RegistrarServerPort_a = $("input[@name='RegistrarServerPort_a']");
	var _OutboundProxy = $("input[@name='OutboundProxy']");
	var _OutboundProxyPort = $("input[@name='OutboundProxyPort']");
	var _OutboundProxy_a = $("input[@name='OutboundProxy_a']");
	var _OutboundProxyPort_a = $("input[@name='OutboundProxyPort_a']");
	var _agreementType = $("select[@name='agreementType']");
	var _vlanId = $("input[@name='vlanId']");
	var _wlantype = $("select[@name='wlantype']");
	var _ipaddress = $("input[@name='ipaddress']");
	var _code = $("input[@name='code']");
	var _netway = $("input[@name='netway']");
	var _dns = $("input[@name='dns']");
	var _vpi = $("input[@name='vpi']");
	var _vci = $("input[@name='vci']");

	
	var reg = new RegExp("^[0-9]*$");
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//�û�����
	if('' == _userType.val() || '-1' == _userType.val()){
		alert("��ѡ���û�����");
		_userType.focus();
		return false;
	}
	//�߼�SN
	if(!IsNull(_username.val(), "�߼�SN")){
		_username.focus();
		return false;
	}
	//ҵ��绰����
	if(!IsNull(_bussinessTel.val(), "ҵ��绰����")){
		_bussinessTel.focus();
		return false;
	}
	
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//VOIP��֤�ʺ�
	if(!IsNull(_voipAccount.val(), "VOIP��֤�ʺ�")){
		_voipAccount.focus();
		return false;
	}

	
	//VOIP��֤����
	if(!IsNull(_voipPassword.val(), "VOIP��֤����")){
		_voipPassword.focus();
		return false;
	}
	
	//��ProxyServer
	if(!IsNull(_ProxyServer.val(), "��ProxyServer")){
		_ProxyServer.focus();
		return false;
	}
	//��ProxyServerPort
	if(!IsNull(_ProxyServerPort.val(), "��ProxyServerPort")){
		_ProxyServerPort.focus();
		return false;
	}
	if(!reg.test(_ProxyServerPort.val())){ 
		alert("��ProxyServerPort��ʽ����!"); 
		_ProxyServerPort.focus();
		return false;
		}
	//��ProxyServer
	if(!IsNull(_ProxyServer_a.val(), "��ProxyServer")){
		_ProxyServer_a.focus();
		return false;
	}
	//��ProxyServerPort
	if(!IsNull(_ProxyServerPort_a.val(), "��ProxyServerPort")){
		_ProxyServerPort_a.focus();
		return false;
	}
	if(!reg.test(_ProxyServerPort_a.val())){ 
		alert("��ProxyServerPort��ʽ����!"); 
		_ProxyServerPort_a.focus();
		return false;
		}
	//�����˿�
	
	if(_voiceport == -1){
		
		alert("��ѡ�������˿ڣ�");
		$("select[@name='voiceport']").focus();
		return false;
	}
	//��RegistrarServer
	if(!IsNull(_RegistrarServer.val(), "��RegistrarServer")){
		_RegistrarServer.focus();
		return false;
	}
	//��RegistrarServerPort
	if(!IsNull(_RegistrarServerPort.val(), "��RegistrarServerPort")){
		_RegistrarServerPort.focus();
		return false;
	}
	if(!reg.test(_RegistrarServerPort.val())){ 
		alert("��RegistrarServerPort��ʽ����!"); 
		_RegistrarServerPort.focus();
		return false;
		}
	//��RegistrarServer
	if(!IsNull(_RegistrarServer_a.val(), "��RegistrarServer")){
		_RegistrarServer_a.focus();
		return false;
	}
	//��RegistrarServerPort
	if(!IsNull(_RegistrarServerPort_a.val(), "��RegistrarServerPort")){
		_RegistrarServerPort_a.focus();
		return false;
	}
	if(!reg.test(_RegistrarServerPort_a.val())){ 
		alert("��RegistrarServerPort��ʽ����!"); 
		_RegistrarServerPort_a.focus();
		return false;
		}
	//��OutboundProxy
	if(!IsNull(_OutboundProxy.val(), "��OutboundProxy")){
		_OutboundProxy.focus();
		return false;
	}
	//��OutboundProxyPort
	if(!IsNull(_OutboundProxyPort.val(), "��OutboundProxyPort")){
		_OutboundProxyPort.focus();
		return false;
	}
	if(!reg.test(_OutboundProxyPort.val())){ 
		alert("��OutboundProxyPort��ʽ����!"); 
		_OutboundProxyPort.focus();
		return false;
		}
	//��OutboundProxy
	if(!IsNull(_OutboundProxy_a.val(), "��OutboundProxy")){
		_OutboundProxy_a.focus();
		return false;
	}
	//��OutboundProxyPort
	if(!IsNull(_OutboundProxyPort_a.val(), "��OutboundProxyPort")){
		_OutboundProxyPort_a.focus();
		return false;
	}
	if(!reg.test(_OutboundProxyPort_a.val())){ 
		alert("��OutboundProxyPort��ʽ����!"); 
		_OutboundProxyPort_a.focus();
		return false;
		}
	//Э������
	if('' == _agreementType.val() || '-1' == _agreementType.val()){
		alert("��ѡ��Э������");
		_agreementType.focus();
		return false;
	}
	//VLANID
	if(!IsNull(_vlanId.val(), "VLANID")){
		_vlanId.focus();
		return false;
	}
	//������ʽ
	
	if(_wlantype == -1){
		
		alert("��ѡ��������ʽ");
		$("select[@name='wlantype']").focus();
		return false;
	}
		if(_wlantype.val() == 3){
		var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	//Ip��ַ
		if(_ipaddress == ""){
			alert("������Ip��ַ");
			$("input[@name='ipaddress']").focus();
			return false;
		}else{
			if(!myReg.test(_ipaddress.val())){
				alert('��������Ч��IP��ַ!');
				$("input[@name='ipaddress']").focus();
				return false;
			}
		}
	//����
		if(_code == ""){
			alert("����������");
			$("input[@name='code']").focus();
			return false;
		}else{
			if(!myReg.test(_code.val())){
				alert('��������Ч������!');
				$("input[@name='code']").focus();
				return false;
			}
		}
	//����
		if(_netway == ""){
			alert("����������");
			$("input[@name='netway']").focus();
			return false;
		}else{
			if(!myReg.test(_netway.val())){
				alert('��������Ч������!');
				$("input[@name='netway']").focus();
				return false;
			}
		}
	//DNS
		if(_dns == ""){
			alert("������DNS");
			$("input[@name='dns']").focus();
			return false;
		}else{
			if(!myReg.test(_dns.val())){
				alert('��������Ч��DNS!');
				$("input[@name='dns']").focus();
				return false;
			}
		}	
	}
	
	if(isHasUsername == 0){
		alert("��������ȷ���߼�SN");
		return false;
	}
	
	document.frm.submit();
}
var hasUsername = 0;
var gwType = 1;
function changeInfo(obj){
	var value = obj.value;
	if(value==3){
		$("font[@id='ipaddressDiv']").html("<font color=red>*</font>");
		$("font[@id='codeDiv']").html("<font color=red>*</font>");
		$("font[@id='netwayDiv']").html("<font color=red>*</font>");
		$("font[@id='dnsDiv']").html("<font color=red>*</font>");
	}else{
		$("font[@id='ipaddressDiv']").html("<font color=red></font>");
		$("font[@id='codeDiv']").html("<font color=red></font>");
		$("font[@id='netwayDiv']").html("<font color=red></font>");
		$("font[@id='dnsDiv']").html("<font color=red></font>");
	}

	
	
}
function change(obj){
	var value = obj.value;
	if(value==1){
		gwType = 1;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else if(value==2){
		gwType = 2;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else{
		$("#username").val("����ѡ���û�����!");
		$("#username").attr("disabled","disabled");	
		
	}
}
function checkUserInfo(){
	var _username = $("input[@name='username']").val();
	var url = "<s:url value='/itms/service/simulateNxNewSheet!checkUsername.action'/>";
	$.post(url, {
		username : _username,
		gw_type : gwType
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1")
		{
			isHasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");

			/* $("tr[@id='vlanid']").css("display","none");
			$("tr[@id='pvc']").css("display","none"); */
		}
		else
		{
			isHasUsername = 1;
			/* if(relt[1]==1)
			{
				orderType = relt[1];
				 $("tr[@id='pvc']").css("display","");
				$("tr[@id='vlanid']").css("display","none"); 
			}
			else
			{
				$("tr[@id='vlanid']").css("display","");
				$("tr[@id='pvc']").css("display","none");
			} */
			$("font[@id='usernameDiv']").html("<font color=red>*</font>");
		}
		parent.dyniframesize();
	});
}
</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateNxNewSheet!sendSheet.action'/>"
	method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4"><input type="hidden" name="servTypeId"
							value='<s:property value="servTypeId" />'> <input
							type="hidden" name="operateType"
							value='<s:property value="operateType" />'>
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD align="center"><font size="2"><b>SIP����</b></font></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">ҵ������ʱ��</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">�û�����</TD>
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
							<option  value="-1">==��ѡ��==</option>
							<option  value="1">==��ͥ����==</option>
							<option  value="2">==��ҵ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�߼�SN</TD>
						<TD><INPUT TYPE="text" id="username" NAME="username" disabled="disabled"
							onblur="checkUserInfo()" class=bk value="����ѡ���û����� ">&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>ҵ��绰����</TD>
						<TD><INPUT TYPE="text" NAME="bussinessTel" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>VOIP��֤�ʺ�</TD>
						<TD><INPUT TYPE="text" NAME="voipAccount" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">VOIP��֤����</TD>
						<TD width="30%"><INPUT TYPE="password" NAME="voipPassword" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>���ô����������ַ</TD>
						<TD><INPUT TYPE="text" NAME="ProxyServer" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���ô���������˿�</TD>
						<TD><INPUT TYPE="text" NAME="ProxyServerPort" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>���ô����������ַ</TD>
						<TD><INPUT TYPE="text" NAME="ProxyServer_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���ô���������˿�</TD>
						<TD><INPUT TYPE="text" NAME="ProxyServerPort_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>�����˿�</TD>
						<TD width="30%"><select name="voiceport" class="bk">
							<option  value="-1">==��ѡ��==</option>
							<option  value="V1">==V1==</option>
							<option  value="V2">==V2==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����ע���������ַ</TD>
						<TD><INPUT TYPE="text" NAME="RegistrarServer" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>����ע��������˿�</TD>
						<TD><INPUT TYPE="text" NAME="RegistrarServerPort" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����ע���������ַ</TD>
						<TD><INPUT TYPE="text" NAME="RegistrarServer_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>����ע��������˿�</TD>
						<TD><INPUT TYPE="text" NAME="RegistrarServerPort_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���ð󶨷�������ַ</TD>
						<TD><INPUT TYPE="text" NAME="OutboundProxy" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>���ð󶨷������˿�</TD>
						<TD><INPUT TYPE="text" NAME="OutboundProxyPort" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���ð󶨷�������ַ</TD>
						<TD><INPUT TYPE="text" NAME="OutboundProxy_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>���ð󶨷������˿�</TD>
						<TD><INPUT TYPE="text" NAME="OutboundProxyPort_a" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">Э������</TD>
						<TD width="30%"><select name="agreementType" class="bk">
							<option  value="0">==IMS==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>VLANID</TD>
						<TD><INPUT TYPE="text" NAME="vlanId" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>������ʽ</TD>
						<TD>
							<select name="wlantype" class=bk onchange="changeInfo(this)">
								<option value="-1">==��ѡ��==</option>
								<option value="1">==�Ž�==</option>
								<option value="2">==·��==</option>
								<option value="3">==��̬IP==</option>
								<option value="4">==DHCP==</option>
							</select>
						 &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>IP��ַ</TD>
						<TD>
							<input type="text" name="ipaddress"  class="bk" value="" />
						 &nbsp; <font id="ipaddressDiv" color="#FF0000"> </font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>����</TD>
						<TD>
							<input type="text" name="code"  class="bk" value="" />
						 &nbsp;<font id="codeDiv" color="#FF0000"> </font></TD>
						<TD class=column align="right" nowrap>����</TD>
						<TD>
							<input type="text" name="netway"  class="bk" value="" />
						 &nbsp; <font id="netwayDiv" color="#FF0000"> </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>DNS</TD>
						<TD>
							<input type="text" name="dns"  class="bk" value="" />
						 &nbsp; <font id="dnsDiv" color="#FF0000"> </font></TD>
					<TD class=column align="right" nowrap></TD>
						<TD>
							</TD>

					</TR>
					
					
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;���͹���&nbsp;
						</button></TD>
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