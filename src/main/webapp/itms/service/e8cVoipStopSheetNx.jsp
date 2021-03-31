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
	var _dealdate = $("input[@name='dealdate']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _operateType = $("input[@name='operateType']");  
	
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
	// ҵ��绰����
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
		dealdate:_dealdate.val(),    // IP��ȡ��ʽ
		username:_username.val(),    // LOID
		cityId:_cityId.val(),        // ����
		voipTelepone:_voipTelepone.val(),   // ҵ��绰����
		operateType:_operateType.val()
	},function(ajax){
		alert(ajax);
		window.location.reload();
	});
	
}
var hasUsername = 0;
function checkUserInfo(){
	var _username = $("input[@name='username']").val();
	var url = "<s:url value='/itms/service/simulateSheetNx!checkUsername.action'/>";
	$.post(url, {
		username : _username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1"){
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
			//$("tr[@id='vlanid']").css("display",none);
			//$("tr[@id='pvc']").css("display","none");
		}else{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}

</script>
</head>
<body>
<!-- <form name="frm" action="<s:url value='/itms/service/simulateSheetNx!sendSheet.action'/>" method="post"> -->
<form name="frm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
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
						<TD class=column align="right" width="20%">��������ʱ��</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg" onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
					<TD class=column align="right" nowrap>�߼�SN</TD>
						<TD><INPUT TYPE="text" NAME="username" onblur="checkUserInfo()" maxlength=50 class=bk
							value="">&nbsp; <font id="usernameDiv" color="#FF0000">* </font></TD>
					</TR>

					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>ҵ��绰����</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;��&nbsp;&nbsp;��&nbsp;
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