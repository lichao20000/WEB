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
var isHasUsername = 0;
function CheckForm(){
	var servTypeId = $("input[@name='servTypeId']");
	var operateType = $("input[@name='operateType']");
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _linePort = $("select[@name='linePort']");
	

	
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
	
	
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//�����˿ڡ��ն������ʶ��
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("��ѡ�������˿�");
		_linePort.focus();
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
								<TD align="center"><font size="2"><b>SIP�����޸Ĺ���</b></font></TD>
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
							<option  value="1">==��ͥ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�߼�SN</TD>
						<TD><INPUT TYPE="text" id="username" NAME="username" 
							onblur="checkUserInfo()"  class=bk >&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>VOIP����֤�ʺ�</TD>
						<TD><INPUT TYPE="text" NAME="voipAccount" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" width="20%">VOIP��֤����</TD>
						<TD width="30%"><INPUT TYPE="password" NAME="voipPassword" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">VOIP����֤�˺�</TD>
						<TD width="30%"><INPUT TYPE="text" NAME="voipOldAccount" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>�����˿�</TD>
						<TD><select name="linePort" class="bk">
							<option value="-1">==��ѡ�������˿�==</option>
							<option value="V1">==V1==</option>
							<option value="V2">==V2==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">Э������</TD>
						<TD width="30%"><select name="agreementType" class="bk">
							<option  value="0">==IMS==</option>
							<option  value="1">==���� SIP==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>IMS�绰����</TD>
						<TD><INPUT TYPE="text" NAME="voipUri" 
							class=bk value=""></TD>
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