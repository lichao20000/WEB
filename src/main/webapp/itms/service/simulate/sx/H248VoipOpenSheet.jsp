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
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	var _linePort = $("select[@name='linePort']");
	
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
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
	//�����˿ڡ��ն������ʶ��
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("��ѡ�������˿�");
		_linePort.focus();
		return false;
	}
	
	if(hasUsername == 0){
		alert("��������ȷ���߼�SN");
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
	var url = "<s:url value='/itms/service/simulateSxNewSheet!checkUsername.action'/>";
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
	action="<s:url value='/itms/service/simulateSxNewSheet!sendSheet.action'/>"
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
						<TD class=column align="right" nowrap>�߼�SN</TD>
						<TD><INPUT TYPE="text" id="username" NAME="username" 
							onblur="checkUserInfo()" maxlength=50 class=bk >&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>

					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">�û�����</TD>
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
							<option  value="1">==��ͥ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					
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
						<TD class=column align="right" nowrap>�����˿�</TD>
						<TD><select name="linePort" class="bk">
							<option value="-1">==��ѡ�������˿�==</option>
							<option value="A1">==A1==</option>
							<option value="A2">==A2==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>EID</TD>
						<TD><INPUT TYPE="text" NAME="voipEid" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>IP��ַ</TD>
						<TD >
							<input type="text" name="hvoipIpaddress"  class="bk" value="" />
						 &nbsp; <font id="ipaddressDiv" color="#FF0000"> </font></TD>
						<TD class=column align="right" nowrap>����</TD>
						<TD>
							<input type="text" name="hvoipIpmask"  class="bk" value="" />
						 &nbsp;<font id="codeDiv" color="#FF0000"> </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>����</TD>
						<TD colspan ="3">
							<input type="text" name="hvoipGateway"  class="bk" value="" />
						 &nbsp; <font id="netwayDiv" color="#FF0000"> </font></TD>
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