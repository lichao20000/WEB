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
	
	var areamName = "<s:property value='instAreaName' />";
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("input[@name='orderType']");
	
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _regIdType = $("select[@name='regIdType']");
	var _regId = $("input[@name='regId']");
	//var _voipPasswd = $("input[@name='voipPasswd']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	var _linePort = $("select[@name='linePort']");
	var _voipPort = $("select[@name='voipPort']");
	
	var areaName = $("input[@name='areaName']").val();
	
	
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//�û�����
	
	if("nmg_dx"==areamName){
		if('' == _userType.val() || '-1' == _userType.val()){
			alert("��ѡ���豸����");
			_userType.focus();
			return false;
		}
	}else{
		if('' == _userType.val() || '-1' == _userType.val()){
			alert("��ѡ���û�����");
			_userType.focus();
			return false;
		}
	}
	
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	
	
	
	

	if("nmg_dx"==areamName){
		
		//��SIP��������ַ
		if(!IsNull(_sipIp.val(), "����MGC��ַ")){
			_sipIp.focus();
			return false;
		}
		//��SIP�������˿�
		if(!IsNull(_sipPort.val(), "����MGC�˿�")){
			_sipPort.focus();
			return false;
		}
		//��SIP��������ַ
		if(!IsNull(_standSipIp.val(), "����MGC��ַ")){
			_standSipIp.focus();
			return false;
		}
		//��SIP�������˿�
		if(!IsNull(_standSipPort.val(), "����MGC�˿�")){
			_standSipPort.focus();
			return false;
		}
	}else{
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
	}
	
	//ҵ��绰����
	if(!IsNull(_voipTelepone.val(), "ҵ��绰����")){
		_voipTelepone.focus();
		return false;
	}
	
	if('' == _regIdType.val() || '-1' == _regIdType.val()){
		alert("��ѡ���ն˱�ʶ����");
		_regIdType.focus();
		return false;
	}
	
	//�����˿ڡ��ն������ʶ��
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("��ѡ�������˿�");
		_linePort.focus();
		return false;
	}
	
	//�ն˱�ʶ
	if(!IsNull(_regId.val(), "�ն˱�ʶ")){
		_regId.focus();
		return false;
	}
	
	if("nmg_dx"==areaName){
		var _netType = $("select[@name='netType']");
		var _ipAddress = $("input[@name='ipAddress']");
		var _mask = $("input[@name='mask']");
		var _gateway = $("input[@name='gateway']");
		var _dsn = $("input[@name='dsn']");
		
		//������ʽ
		if(!IsNull(_netType.val(), "������ʽ")){
			_netType.focus();
			return false;
		}
		//ip��ַ
		if(!IsNull(_ipAddress.val(), "IP��ַ")){
			_ipAddress.focus();
			return false;
		}
		//����
		if(!IsNull(_mask.val(), "����")){
			_mask.focus();
			return false;
		}
		//����
		if(!IsNull(_gateway.val(), "����")){
			_gateway.focus();
			return false;
		}
		//DSN
		if(!IsNull(_dsn.val(), "DSN")){
			_dsn.focus();
			return false;
		}
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
								<TD align="center"><font size="2"><b>H248 VOIP������Ϣ</b></font></TD>
								<input type="hidden" name="areaName" id="areaName" value='<s:property value='instAreaName'/>' />
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
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
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
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" nowrap>����MGC��ַ</TD>
							<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>����MGC�˿�</TD>
							<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD class=column align="right" nowrap>��SIP��������ַ</TD>
							<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>��SIP�������˿�</TD>
							
							<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" nowrap>����MGC��ַ</TD>
							<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>����MGC�˿�</TD>
							<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD class=column align="right" nowrap>��SIP��������ַ</TD>
							<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>��SIP�������˿�</TD>
							<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:else>
						
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
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD><select name="linePort" class="bk">
							<option value="-1">==��ѡ�������ʶ==</option>
							<option value="A0">==A0==</option>
							<option value="A1">==A1==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						</s:if>
						<s:else>
							<TD><select name="linePort" class="bk">
							<option value="-1">==��ѡ�������ʶ==</option>
							<option value="A1">==A1==</option>
							<option value="A2">==A2==</option>
							<option value="AL1">==AL1==</option>
							<option value="AL2">==AL2==</option>
							<option value="AG58900">==AG58900==</option>
							<option value="AG58901">==AG58901==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						</s:else>
					
						<TD class=column align="right" nowrap>ҵ��绰����</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<s:if test='%{instAreaName=="nmg_dx"}'>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>������ʽ</TD>
							<TD><select name="netType" class="bk">
								<option value="-1">==��ѡ��������ʽ==</option>
								<option value="3">==��̬IP==</option>
							</select> &nbsp;<font color="#FF0000">*</font></TD>
							<TD class=column align="right" nowrap>IP��ַ</TD>
							<TD><INPUT TYPE="text" NAME="ipAddress" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>����</TD>
							<TD><INPUT TYPE="text" NAME="mask" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>����</TD>
							<TD><INPUT TYPE="text" NAME="gateway" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" width="20%" nowrap>DNS</TD>
							<TD  colspan="3"><INPUT TYPE="text" NAME="dsn" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
					</s:if>
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