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
});
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _devType = $("select[@name='devType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("input[@name='orderType']");
	
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _voipUsername = $("input[@name='voipUsername']");
	var _voipPasswd = $("input[@name='voipPasswd']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	
	var _registrarServer = $("input[@name='registrarServer']");
	var _registrarServerPort = $("input[@name='registrarServerPort']");
	var _standRegistrarServer = $("input[@name='standRegistrarServer']");
	var _standRegistrarServerPort = $("input[@name='standRegistrarServerPort']");
	
	var _outboundProxy = $("input[@name='outboundProxy']");
	var _outboundProxyPort = $("input[@name='outboundProxyPort']");
	var _standOutboundProxy = $("input[@name='standOutboundProxy']");
	var _standOutboundProxyPort = $("input[@name='standOutboundProxyPort']");
	var _linePort = $("select[@name='linePort']");
	var _protocol = $("select[@name='protocol']");
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//�豸����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ���豸����");
		_orderType.focus();
		return false;
	}
	//�û��˺�
	if(!IsNull(_username.val(), "�û��˺�")){
		_username.focus();
		return false;
	}
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//VOIP��֤�˺�
	if(!IsNull(_voipUsername.val(), "VOIP��֤�˺�")){
		_voipUsername.focus();
		return false;
	}
	//VOIP��֤����
	if(!IsNull(_voipPasswd.val(), "VOIP��֤����")){
		_voipPasswd.focus();
		return false;
	}
	//��SIP��������ַ
	if(!IsNull(_sipIp.val(), "��SIP��������ַ")){
		_sipIp.focus();
		return false;
	}
	//��SIP�������˿�
	if(!IsNull(_sipPort.val(), "��SIP�������˿�")){
		_sipPort.focus();
		return false;
	}
	//��SIP��������ַ
	if(!IsNull(_standSipIp.val(), "��SIP��������ַ")){
		_standSipIp.focus();
		return false;
	}
	//��SIP�������˿�
	if(!IsNull(_standSipPort.val(), "��SIP�������˿�")){
		_standSipPort.focus();
		return false;
	}
	
	//��RegistrarServer
	if(!IsNull(_registrarServer.val(), "��RegistrarServer")){
		_registrarServer.focus();
		return false;
	}
	//��RegistrarServerPort
	if(!IsNull(_registrarServerPort.val(), "��RegistrarServerPort")){
		_registrarServerPort.focus();
		return false;
	}
	//��RegistrarServer
	if(!IsNull(_standRegistrarServer.val(), "��RegistrarServer")){
		_standRegistrarServer.focus();
		return false;
	}
	//��RegistrarServerPort
	if(!IsNull(_standRegistrarServerPort.val(), "��RegistrarServerPort")){
		_standRegistrarServerPort.focus();
		return false;
	}//��OutboundProxy
	if(!IsNull(_outboundProxy.val(), "��OutboundProxy")){
		_outboundProxy.focus();
		return false;
	}
	//��OutboundProxyPort
	if(!IsNull(_outboundProxyPort.val(), "��OutboundProxyPort")){
		_outboundProxyPort.focus();
		return false;
	}
	//��OutboundProxy
	if(!IsNull(_standOutboundProxy.val(), "��OutboundProxy")){
		_standOutboundProxy.focus();
		return false;
	}
	//��OutboundProxyPort
	if(!IsNull(_standOutboundProxyPort.val(), "��OutboundProxyPort")){
		_standOutboundProxyPort.focus();
		return false;
	}
	
	//ҵ��绰����
	if(!IsNull(_voipTelepone.val(), "ҵ��绰����")){
		_voipTelepone.focus();
		return false;
	}
	//�����˿�
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("��ѡ�������˿�");
		_linePort.focus();
		return false;
	}
	//Э������
	if('' == _protocol.val() || '-1' == _protocol.val()){
		alert("��ѡ��Э������");
		_protocol.focus();
		return false;
	}
	if(hasUsername == 0){
		alert("��������ȷ��LOID");
		return false;
	}
	document.frm.submit();
}
var hasUsername = 0;
var gwType = 1;
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
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url, {
		gw_type : gwType,
		username : _username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1")
		{
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
			$("tr[@id='vlanid']").css("display",none);
			$("tr[@id='pvc']").css("display","none");
		}
		else
		{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}
</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateSheet!sendSheet.action'/>"
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
								<TD align="center"><font size="2"><b>VOIP������Ϣ</b></font></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����ʱ��</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>LOID</TD>
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD><INPUT TYPE="text" id="username" NAME="username" disabled="disabled"
							onblur="checkUserInfo()" maxlength=50 class=bk value="����ѡ���豸���� ">&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD><INPUT TYPE="text" id="username" NAME="username" disabled="disabled"
							onblur="checkUserInfo()" maxlength=50 class=bk value="����ѡ���û����� ">&nbsp;
							<font id="usernameDiv" color="#FF0000">* </font></TD>
						</s:else>

					</TR>

					<TR bgcolor="#FFFFFF">
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" width="20%">�豸����</TD>
						</s:if>
						<s:else>
							<TD class=column align="right" width="20%">�û�����</TD>
						</s:else>
						<TD width="30%"><select name="devType" class="bk" onchange="change(this)">
							<option  value="-1">==��ѡ��==</option>
							<option  value="1">==��ͥ����==</option>
							<option  value="2">==��ҵ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>VOIP��֤�˺�</TD>
						<TD><INPUT TYPE="text" NAME="voipUsername" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>VOIP��֤����</TD>
						<TD><INPUT TYPE="text" NAME="voipPasswd" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��SIP��������ַ</TD>
						<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��SIP�������˿�</TD>
						<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��SIP��������ַ</TD>
						<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��SIP�������˿�</TD>
						<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��RegistrarServer</TD>
						<TD><INPUT TYPE="text" NAME="registrarServer" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��RegistrarServerPort</TD>
						<TD><INPUT TYPE="text" NAME="registrarServerPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��RegistrarServer</TD>
						<TD><INPUT TYPE="text" NAME="standRegistrarServer" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��RegistrarServerPort</TD>
						<TD><INPUT TYPE="text" NAME="standRegistrarServerPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��OutboundProxy</TD>
						<TD><INPUT TYPE="text" NAME="outboundProxy" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��OutboundProxyPort</TD>
						<TD><INPUT TYPE="text" NAME="outboundProxyPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��OutboundProxy</TD>
						<TD><INPUT TYPE="text" NAME="standOutboundProxy" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>��OutboundProxyPort</TD>
						<TD><INPUT TYPE="text" NAME="standOutboundProxyPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>ҵ��绰����</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>�����˿�</TD>
						<TD><select name="linePort" class=bk>
							<option value="-1">==��ѡ�������˿�==</option>
							<option value="V1">V1</option>
							<option value="V2">V2</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>SIPЭ������</TD>
						<TD colspan="3"><select name="protocol" class=bk>
							<option value="-1">==��ѡ��Э������==</option>
							<option value="1">����</option>
							<option value="0">IMS</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
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