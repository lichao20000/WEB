<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSS模拟工单</title>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _userType = $("select[@name='userType']");
	var _iptvUsername = $("input[@name='iptvUsername']");
	var _servNum = $("input[@name='servNum']");
	//var _iptvLanPort = $("input[@name='iptvLanPort']");
	//受理时间
	if(!IsNull(_dealdate.val(), "受理时间")){
		_dealdate.focus();
		return false;
	}
	//用户账号
	if(!IsNull(_username.val(), "用户账号")){
		_username.focus();
		return false;
	}
	//IPTV宽带接入账号
	if(!IsNull(_iptvUsername.val(), "IPTV宽带接入账号")){
		_iptvUsername.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	
	//IPTV个数
	if(!IsNull(_servNum.val(), "IPTV个数")){
		_servNum.focus();
		return false;
	}
	//开通端口
	//if(!IsNull(_iptvLanPort.val(), "开通端口")){
	//	_iptvLanPort.focus();
	//	return false;
	//}
	if(hasUsername == 0){
		alert("请输入正确的LOID");
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
		$("#username").val("请先选择用户类型!");
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
<form name="frm" action="<s:url value='/itms/service/simulateSheet!sendSheet.action'/>"  method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
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
								<TD align="center"><font size="2"><b>IPTV开户信息</b></font></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">受理时间</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
					<TD class=column align="right" nowrap>LOID</TD>
					<TD><INPUT TYPE="text" id="username" NAME="username" onblur="checkUserInfo()" maxlength=50 class=bk
						disabled="disabled" 	value="请先选择用户类型">&nbsp; <font id="usernameDiv" color="#FF0000">* </font></TD>
					</TR>

					<TR  bgcolor="#FFFFFF">
					<TD class=column align="right" width="20%">用户类型</TD>
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
							<option value="-1">==请选择==</option>
							<option value="1">==家庭网关==</option>
							<option value="2">==企业网关==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
							<TD class=column align="right" nowrap>属地</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="请选择属地" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
						
					</TR>
					
					<TR  bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>IPTV接入账号</TD>
						<TD><INPUT TYPE="text" NAME="iptvUsername" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>IPTV个数</TD>
						<TD><INPUT TYPE="text" NAME="servNum" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<!-- <TR  bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>开通端口</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="iptvLanPort" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						
					</TR> -->
					<TR>
						<TD colspan="4" align="right" class="green_foot">
						
						<button onclick="CheckForm()">
						&nbsp;发送工单&nbsp;
						</button>
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