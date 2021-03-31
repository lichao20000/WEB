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
var hasUsername = 0;
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _voipUsername = $("input[@name='voipTelepone']");
	var _userType = $("select[@name='userType']");
	//受理时间
	if('' == _dealdate.val() || '-1' == _dealdate.val()){
		alert("请选择受理时间");
		_dealdate.focus();
		return false;
	}
	//用户账号
	if('' == _username.val() || '-1' == _username.val()){
		alert("请填写逻辑SN");
		_username.focus();
		return false;
	}
	//检查逻辑SN和业务帐号
	if(_username.val() != ""){
		$.ajax({  
	        url:'<s:url value="/itms/service/bssSheetByHand4jx!checkUsername.action"/>',  
	        type:'POST',  
	        data:'gw_type='+gwType+"&username="+_username.val(),
	        async:false,
	        success:function (data) {  
	        	var relt = data.split("#");
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
	        },
	        error:function(){
	        	alert('error');
	        	return false;
	        }
	    }); 
	}
	if(hasUsername == 0){
		alert("请输入正确的逻辑SN");
		_username.focus();
		return false;
	}
	//用户类型
	if('' == _userType.val() || '-1' == _userType.val()){
		alert("请选择用户类型");
		_userType.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	//VOIP认证账号
	if('' == _voipUsername.val() || '-1' == _voipUsername.val()){
		alert("请填写VOIP认证账号");
		_voipUsername.focus();
		return false;
	}
	if(hasUsername == 0){
		alert("请输入正确的逻辑SN");
		return false;
	}
	document.frm.submit();
}

</script>
</head>
<body>
<form name="frm" action="<s:url value='/itms/service/bssSheetByHand4jx!sendSheet.action'/>"
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
								<TD align="center"><font size="2"><b>VOIP销户信息</b></font></TD>
							
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
						<TD><INPUT TYPE="text" id="username" NAME="username" onblur="checkUserInfo()" maxlength=50 class=bk
					   disabled="disabled"		value="请先选择用户类型">&nbsp; <font id="usernameDiv" color="#FF0000">* </font></TD>
					</TR>

					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">用户类型</TD>
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
							<option  value="-1">==请选择==</option>
							<option  value="1">==家庭网关==</option>
							<option  value="2">==企业网关==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">属地</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="请选择属地" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD  class=column align="right" nowrap>业务电话号码</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="voipTelepone" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;发送工单&nbsp;
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