<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<style type="text/css">
.bk {
	BACKGROUND-COLOR: #f1f1f1;
	BORDER-BOTTOM: #eeeeee 1px solid;
	BORDER-LEFT: #333333 1px solid;
	BORDER-RIGHT: #eeeeee 1px solid;
	BORDER-TOP: #333333 1px solid;
	COLOR: #888888;
	FONT-FAMILY: "宋体", "Arial"
}
</style>
<%
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<SCRIPT LANGUAGE="JavaScript">
var area = '<%=area%>';

function refresh(){
	window.location.href=window.location.href;
}


function checkSerialno() {

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
		getDeviceInfo();

}
function getDeviceInfo(){

	var deviceSN = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	$("div[@id='div_device']").html("正在查询，请稍等....");
	$("input[@name='sort']").attr("disabled", true);
	var gw_type	= $("input[@name='gw_type']").val();
	var url = "<s:url value='/gwms/resource/updateDevCityByIP!getDeviceInfo.action'/>";
	if(deviceSN=="至少最后6位"){
		deviceSN="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
	$.post(url,{
		deviceSN:deviceSN,
		loopbackIp:loopbackIp,
		gw_type:gw_type
	},function(ajax){
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
		$("input[@name='sort']").attr("disabled", false);
	});
	document.all("tr_deviceinfo").style.display="";
}

function getUserinstInfo(){

	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	var url = "<s:url value='/gwms/resource/itmsInst!getDeviceInfo.action'/>";
	if(device_serialnumber=="至少最后6位"){
		device_serialnumber="";
	}
	if(loopbackIp=="完整IP"){
		loopbackIp="";
	}
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


function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();

	if ("" == deviceId ){
		alert('请先选择一个设备！');
		return false;
	}

	var instArea = $("input[@name='instArea']").val();
	if("js_dx"==instArea){
		$("input[@name='username']").val($.trim($("input[@name='userName']").val()));
	}
	if("ehome_self"==$.trim($("input[@name='typename']").val())){
		alert('该用户使用自备终端，不予绑定!');
		return false;
	}

	<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT() && !LipossGlobals.inArea("nx_lt")){%>
	if($.trim($("input[@name='deviceType']").val())!=$.trim($("input[@name='typename']").val())){
		alert('用户开通的终端类型与待绑定的终端类型不一致，不予绑定!');
		return false;
	}
	<%} %>

	var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceNo']").val();
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}

	document.frm.action="itmsInst.action";
	document.frm.submit();
}

function updateCity(){

	var deviceId = $("input[@name='deviceId']").val();

	if ("" == deviceId ){
		alert('请先选择一个设备！');
		return false;
	}

	if (!confirm('请确认是否进行属地修正?')){
		return false;
	}

	document.frm.action="<s:url value='/gwms/resource/updateDevCityByIP!updateCity.action'/>";
	document.frm.submit();
}

function checkUsernameEmpty() {
	if (($.trim(document.frm.userName.value) == ""||$.trim(document.frm.userName.value)=="完整账号")){
		alert('请输入账号！');
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
	if(userName=="完整账号"){
		userName="";
	}

	var gw_type	= $("input[@name='gw_type']").val();
	var nameType = $.trim($("select[@name='nameType']").val());
	$("div[@id='div_user']").html("正在查询，请稍等....");
	$("input[@name='next']").attr("disabled", true);
	var url = "<s:url value='/gwms/resource/itmsInst!getInstUserInfo.action'/>";
	$.post(url,{
		userName:userName,
		nameType:nameType,
		gw_type:gw_type
	},function(ajax){
		userClear();
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
		$("input[@name='next']").attr("disabled", false);
	});
	document.all("tr_userinfo").style.display="";
}
//add qixueqi
//针对选中设备时需要提交的数据
function deviceOnclick(device_id,city_id,oui,device_serialnumber,IpCity_id,cpe_allocatedstatus,flag,manage,device_type){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='oui']").val(oui);
	$("input[@name='deviceNo']").val(device_serialnumber);
	$("input[@name='deviceType']").val(device_type);
	$("input[@name='IpCityId']").val(IpCity_id);
	$("input[@name='isBind']").val(cpe_allocatedstatus);
	$("input[@name='flag']").val(flag);
	$("div[@id='div_update']").html("");
	$("div[@id='div_update']").css("display","");
	document.all("queryuser").style.display="none";
	document.all("tr_userinfo").style.display="none";
	if(manage=="1"){

		if("1"==flag){
			$("div[@id='div_update']").html("<input type='button' class=jianbian name='save_btn' value=' 属 地 修 正 ' onclick='updateCity()'/>");
		}else{
			if(cpe_allocatedstatus=="1")
			{
				if(area == 'sx_lt') {
					$("div[@id='div_update']").html("<font color='red' size='3'>该设备已绑定，请确认设备信息是否正确，如需重新绑定请先解绑！</font>");
				}else{
					$("div[@id='div_update']").html("<font color='red' size='3'>该设备不需要进行属地修正！如果设备被同本地网其他人员绑定，请联系本地网管理员进行解绑操作</font>");
				}
			}else{
				document.all("queryuser").style.display="";
			}
		}
		//else if("3"==flag){
		//	$("div[@id='div_update']").html("<font color='red' size='3'>该设备IP没有对应的属地！</font>");
		//}else{
		//	$("div[@id='div_update']").html("<font color='red' size='3'>该设备未上报或上报失败！</font>");
		//}
	}else{
		$("div[@id='div_update']").html("<font color='red' size='3'>该设备你无法进行管理或修正！</font>");
	}
}

//add qixueqi
//重新查询设备时清空已选择的设备
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='oui']").val("");
	$("input[@name='deviceNo']").val("");
	$("input[@name='deviceType']").val("");
	$("input[@name='IpCityId']").val("");
	$("input[@name='isBind']").val("");
	$("input[@name='flag']").val("");
	$("div[@id='div_update']").css("display","none");
	document.all("tr_userinfo").style.display="none";
	document.all("queryuser").style.display="none";
}

//add qixueqi
//针对选中用户时需要提交的数据
function userOnclick(user_id,userName,city_id,deviceId,oui,deviceSN,typeName){
	$("input[@name='userId']").val(user_id);
	$("input[@name='username']").val(userName);
	$("input[@name='typename']").val(typeName);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='oldDeviceId']").val(deviceId);

	var instArea = $("input[@name='instArea']").val();

	$("div[@id='div_bind']").css("display","");
	if(""==deviceId){
		//if(""==deviceSN){
			$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' 开 始 绑 定 ' onclick='CheckForm()'/>");
		//}else{
			//$("div[@id='div_bind']").html("<font color='red'>该用户账号存在异常，请联系管理员！</font>");
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
	if("ehome_self"==$.trim($("input[@name='typename']").val())){
		alert('该用户使用自备终端，不予绑定!');
		return false;
	}

	<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT()){%>
	if($.trim($("input[@name='deviceType']").val())!=$.trim($("input[@name='typename']").val())){
		alert('用户开通的终端类型与待绑定的终端类型不一致，不予绑定!');
		return false;
	}
	<%} %>

	var message = "请确认！用户帐号："+$("input[@name='username']").val()+"，设备序列号："+$("input[@name='deviceNo']").val();
	if (!confirm(message+'！是否继续保存?')){
		return false;
	}

	document.frm.action="itmsInst!imposeInst.action";
	document.frm.submit();
}

//add qixueqi
//重新查询用户时时清空已选择的用户
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='username']").val("");
	$("input[@name='typename']").val("");
	$("input[@name='userCityId']").val("");
	$("input[@name='oldDeviceId']").val("");
	$("div[@id='div_bind']").css("display","none");
}

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//查看用户相关的信息
function GoContent(user_id){
	var strpage="../../Resource/HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

//输入框得到去焦点
function inputFocus(s,message){
	var value = $.trim(s.value);
	s.style.color='#0000cc';
	if(value==''||value==message){

		s.value='';
	}
}

//输入框失去焦点
function inputBlur(s,message){
	var value = $.trim(s.value);
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">

	<!-- 隐藏需要提交项 -->
	<!-- 设备部分 -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="oui" value="" />
	<input type="hidden" name="deviceNo" value="" />
	<input type="hidden" name="deviceType" value="" />
	<!-- 属地修正 -->
	<input type="hidden" name="IpCityId" value="" />
	<input type="hidden" name="isBind" value="" />
	<input type="hidden" name="flag" value="" />
	<!-- 用户部分 -->
	<input type="hidden" name="userId" value="" />
	<input type="hidden" name="username" value="" />
	<input type="hidden" name="typename" value="" />
	<input type="hidden" name="userCityId" value="" />
	<input type="hidden" name="oldDeviceId" value="">

	<input type="hidden" name="gw_type" value="<s:property value='gw_type' />" />

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15>
			</TD>
		</TR>
		<TR>
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
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="center" width="50%">
							设备序列号&nbsp;
							<input type="text" size="35" class="bk" name=device_serialnumber
								value="至少最后6位" onfocus="inputFocus(this,'至少最后6位')"
								onblur="inputBlur(this,'至少最后6位')">

						</TD>
						<TD align="center" width="50%">
							设备IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="完整IP" onfocus="inputFocus(this,'完整IP')"
								onblur="inputBlur(this,'完整IP')">

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
			<TD align="center">
				<div id="div_update" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR id="queryuser" style="display: none">
			<TD bgcolor=#999999>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center">
					<TR bgcolor="#FFFFFF">
						<TH colspan="2">
							用户查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center" width="50%">
							选择条件&nbsp;
							<s:property value="nameListHtml" escapeHtml="false"/>
						</TD>
						<TD align="center">
							查询值&nbsp;
							<input type="text" size="35" name="userName" class="bk"
								value="完整账号" onfocus="inputFocus(this,'完整账号')"
								onblur="inputBlur(this,'完整账号')" />

						</TD>

					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2">
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
<%if(!area.equals("sx_lt")){%>
	<%@ include file="../foot.jsp"%>
<%}%>
