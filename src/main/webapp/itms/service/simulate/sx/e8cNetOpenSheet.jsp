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
var orderType = 2;
var isHasUsername = 0;
var gwType = 1;
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _netUsername = $("input[@name='netUsername']");
	var _netPassword = $("input[@name='netPassword']");
	var  wlantype = $.trim($("select[@name='wlantype']").val());
	var _vlanId = $("input[@name='vlanId']");
	//受理时间
	if(!IsNull(_dealdate.val(), "受理时间")){
		_dealdate.focus();
		return false;
	}
	
	//设备类型
	if('' == _userType.val() || '-1' == _userType.val()){
		alert("请选择类型");
		_userType.focus();
		return false;
	}
	
	//用户账号
	if(!IsNull(_username.val(), "用户账号")){
		_username.focus();
		return false;
	}
	
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	//宽带账号
	if(!IsNull(_netUsername.val(), "宽带账号")){
		_netUsername.focus();
		return false;
	}
	
	//宽带密码
	if(!IsNull(_netPassword.val(), "宽带密码")){
		_netPassword.focus();
		return false;
	}
	//上网方式
	if(wlantype == -1){
		alert("请选择上网方式");
		$("select[@name='wlantype']").focus();
		return false;
	}
	//VLAND
	if(!IsNull(_vlanId.val(), "VLAND")){
		_vlanId.focus();
		return false;
	}
	
	if(isHasUsername == 0){
		alert("请输入正确的逻辑SN");
		return false;
	}
	var netPortStr = "";
	var checkBoxNet = document.frm.netPort;
	var portflag = false;
	for(var i=0; i<checkBoxNet.length; i++){
		if(checkBoxNet[i].checked){
			if(portflag){
				netPortStr = netPortStr+",";
			}
			netPortStr = netPortStr + checkBoxNet[i].value;
			portflag = true;	
		}
	}
	if(!portflag)
	{
		alert("宽带绑定端口不可为空。");
		$("input[@name='netPort']").focus();
		return false;
	}
	document.frm.submit();
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
		$("#username").attr("disabled","disabled");	
	}
}
function checkUsername(){
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
			$("div[@id='usernameDiv']").html("<font color=red>"+relt[1]+"</font>");
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
			$("div[@id='usernameDiv']").html("");
		}
		parent.dyniframesize();
	});
}
</script>
</head>
<body>
<form name="frm" action="<s:url value='/itms/service/simulateSxNewSheet!sendSheet.action'/>"
	 method="post">
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
								<TD align="center"><font size="2"><b>上网开户信息</b></font></TD>
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
							<TD class=column align="right" nowrap>逻辑SN</TD>
						<TD><INPUT TYPE="text" id="username" NAME="username" onblur="checkUsername()" maxlength=50 class=bk 
							>&nbsp; <font color="#FF0000">* </font><div id="usernameDiv"></div></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">用户类型</TD>
						<TD width="30%"><select name="userType" class="bk" >
							<option value="1">==家庭网关==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">属地</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="请选择属地" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>宽带账号</TD>
						<TD><INPUT TYPE="text" NAME="netUsername"  maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>宽带密码</TD>
						<TD><INPUT TYPE="text" NAME="netPassword" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>上网方式</TD>
						<TD>
							<select name="wlantype" class=bk>
								<option value="-1">==请选择==</option>
								<option value="1">==桥接==</option>
								<option value="2">==路由==</option>
							</select>
						</TD>
						<TD width="15%" class=column align="right">绑定端口:</TD>
						<TD id="netTD1" width="35%">
							<input type="checkbox" name="netPort" value="L1" class=bk />L1&nbsp;
							<input type="checkbox" name="netPort" value="L2" class=bk />L2&nbsp;
							<input type="checkbox" name="netPort" value="L3" class=bk />L3&nbsp;
							<input type="checkbox" name="netPort" value="L4" class=bk />L4&nbsp;
						&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>VLANID</TD>
						<TD colspan = "3">
							<INPUT TYPE="text" NAME="vlanId" maxlength=5 class=bk value="" >&nbsp; 
							<font color="#FF0000">* </font>
						</TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;发送工单&nbsp;
						</button> </TD>
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