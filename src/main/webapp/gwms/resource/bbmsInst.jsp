<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<style type="text/css">

.bk {
	BACKGROUND-COLOR: #f1f1f1; BORDER-BOTTOM: #eeeeee 1px solid; BORDER-LEFT: #333333 1px solid; BORDER-RIGHT: #eeeeee 1px solid; BORDER-TOP: #333333 1px solid; COLOR: #888888; FONT-FAMILY: "宋体","Arial"
}
</style>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
	//是否判断


function refresh(){
	window.location.href=window.location.href;
}


function checkSerialno(){
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(device_serialnumber=="至少最后6位"){
		device_serialnumber="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	if(device_serialnumber==""&&loopbackIp==""){
		alert("请至少输入设备序列号和设备IP中的一项！");
		document.frm.device_serialnumber.focus();
		return false;
	}
	if(device_serialnumber!=""){
		if(device_serialnumber.length < 6){
            alert("请输入至少最后6位设备序列号 !");
            document.frm.device_serialnumber.focus();
            return false;        
	    }
	}
	if(loopbackIp!=""){
		if(!IsIPAddr2(loopbackIp,"设备IP")){
			document.frm.loopbackIp.focus();
			return false;
		}
	}
	
	getUserinstInfo();
	
}
function getUserinstInfo(){
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(device_serialnumber=="至少最后6位"){
		device_serialnumber="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	var url = "<s:url value='/gwms/resource/bbmsInst!getDeviceInfo.action'/>";
	$.post(url,{
		device_serialnumber:device_serialnumber,
		loopbackIp:loopbackIp
	},function(ajax){	
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
	document.all("tr_deviceinfo").style.display="";
}


//针对选中设备时需要提交的数据
function deviceOnclick(device_id,city_id,oui,device_serialnumber){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='oui']").val(oui);
	$("input[@name='deviceNo']").val(device_serialnumber);
}


//重新查询设备时清空已选择的设备
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='oui']").val("");
	$("input[@name='deviceNo']").val("");
}


//针对选中用户时需要提交的数据
function userOnclick(user_id,userName,city_id,customer_id,deviceId,oui,deviceSN){
	$("input[@name='userId']").val(user_id);
	$("input[@name='username']").val(userName);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='customerId']").val(customer_id);
	$("input[@name='oldDeviceId']").val(deviceId);
	
	var instArea = $("input[@name='instArea']").val();
	
	$("div[@id='div_bind']").css("display","");
	if(""==deviceId){
		//if(""==deviceSN){
			$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' 开 始 绑  定 ' onclick='CheckForm()'/>");
		//}else{
		//	$("div[@id='div_bind']").html("<font color='red'>该用户账号存在异常，请联系管理员！</font>");
		//}
	}else{
		if("xj_dx"==instArea){
			$("div[@id='div_bind']").html("<table border=0 cellspacing=0 cellpadding=0 width='45%'><tr><td  colspan='2' align='center'><font color='red'>该用户账号已绑定，请确认输入账号是否正确！<br>如需要继续请点击开始绑定，原绑定设备将要被解绑！</font></td></tr><tr><td align='center' width='50%'><input type='button' class=jianbian name='save_btn' value=' 开 始 绑 定 ' onclick='imposeInst()'/></td><td align='center'><input type='button' class=jianbian name='save_btn' value=' 取 消 ' onclick='userClear()'/></td></tr></table>");
		}else{
			$("div[@id='div_bind']").html("<font color='red'>该用户账号已绑定，请确认输入账号是否正确，如需要重新绑定请先解绑！</font>");
		}	
	}
}

function imposeInst(){

	var deviceId = $("input[@name='deviceId']").val();
	
	if ("" == deviceId ){
		alert('请先选择一个设备！');
		return false;
	}
	
	var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceNo']").val();
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}
	
	document.frm.action="bbmsInst!imposeInst.action";
	document.frm.submit();	
}


//重新查询用户时时清空已选择的用户
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='username']").val("");
	$("input[@name='userCityId']").val("");
	$("input[@name='customerId']").val("");
	$("input[@name='oldDeviceId']").val("");
	$("div[@id='div_bind']").css("display","none");
}

function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();
	
	if ("" == deviceId ){
		alert('请先选择一个设备！');
		return false;
	}
	
	var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceNo']").val();
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}
	
	document.frm.action="bbmsInst.action";
	document.frm.submit();	
}

function checkUsernameEmpty() {
	if ($.trim(document.frm.userName.value) == ""||$.trim(document.frm.userName.value)=="完整账号"){
		alert('请填写用户宽带帐号/专线号');
		document.frm.userName.focus();
		return false;
	}
	return true;
}
function nextstep(){
	if (!checkUsernameEmpty()) {
		return;
	}
	var userName = $.trim(document.frm.userName.value);
	var url = "<s:url value='/gwms/resource/bbmsInst!getInstUserCustomerInfo.action'/>";
	$.post(url,{
		userName:userName
	},function(ajax){
		userClear();	
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});	
	document.all("tr_userinfo").style.display="";
}

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//查看用户相关的信息
function GoContent(user_id){
	var strpage="../../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//输入框得到去焦点
function inputFocus(s,message){
	//s.css({"BACKGROUND-COLOR": "#f1f1f1", "BORDER-BOTTOM": "#eeeeee 1px solid", "BORDER-LEFT": "#333333 1px solid", "BORDER-RIGHT": "#eeeeee 1px solid", "BORDER-TOP": "#333333 1px solid", "COLOR": "#000000", "FONT-FAMILY": "'宋体','Arial'"});
	var value = s.value;
	s.style.color='#0000cc';
	if(value==''||value==message){
		
		s.value='';
	}
}

//输入框失去焦点
function inputBlur(s,message){
	var value = s.value;
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
//-->
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">
	<!-- 隐藏需要提交项 -->
	<!-- 设备部分 -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="oui" value="" />
	<input type="hidden" name="deviceNo" value="" />
	<!-- 用户部分 -->
	<input type="hidden" name="userId" value="" />
	<input type="hidden" name="username" value="" />
	<input type="hidden" name="userCityId" value="" />
	<input type="hidden" name="customerId" value="" />
	<input type="hidden" name="oldDeviceId" value="">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15>

				&nbsp;
			</TD>
		</TR>

		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							手工安装
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							请先查询设备，然后查询用户，选择正确的设备与用户进行安装！
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TH colspan="2">
							设备相关信息
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">
							设备序列号&nbsp;
							<input type="text" size="35" class="bk" name=device_serialnumber
								value="至少最后6位"
								onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">
							<font color="red">*</font>
						</TD>
						<TD align="center">
							设备IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="完整IP"
								onfocus="inputFocus(this,'完整IP')"
								onblur="inputBlur(this,'完整IP')">
							<font color="red">*</font>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2">
							<input type="button" class=jianbian name="sort" value=" 查  询 "
								onclick="checkSerialno()" />
						</td>
					</tr>

				</TABLE>
			</TD>
		</TR>

		<TR style="display: none" id="tr_deviceinfo">
			<TD>
				<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>

		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center" bgcolor="#999999">
					<tr>
						<th colspan="5">
							用户相关信息
						</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD align="center">
							用户帐户&nbsp;
							<input type="text" size="35" name="userName" class="bk"
								value="完整账号"
								onfocus="inputFocus(this,'完整账号')"
								onblur="inputBlur(this,'完整账号')" />
							<font color="red">*</font>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column">
							<input type="button" class=jianbian name="next"
								onclick="nextstep()" value=" 下一步 " />
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR style="display: none" id="tr_userinfo">
			<TD>
				<div id="div_user" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD align="center">
				<div id="div_bind" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
	</TABLE>

</FORM>



<br>
<br>
<%@ include file="../foot.jsp"%>
