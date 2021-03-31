<%--
业务手工工单
Author: Jason
Version: 1.0.0
Date: 2009-09-25
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<title>家庭网关客户添加编辑</title>
<script type="text/javascript">
//页面为编辑还是添加
var isAdd = '<s:property value="isAdd"/>';
//添加用户时判断是否用户已经存在
var userExists = '0';
$(function(){

	var typeName = '<s:property value="typeName"/>';
	$("select[name='typeName']").val(typeName);
});

function checkUser(){
	var username = $.trim($("input[@name='username']").val());
	if(username!=""){
		if(isAdd == '1'){
		var url = "<s:url value='/gwms/resource/hgwcustManage!checkUser.action'/>";
		$.post(url,{
			username:encodeURIComponent(username)
		},function(ajax){
			//alert(ajax);
			if(ajax == '1'){
				userExists = '1';
				$("div[@id='isOk']").show();
				$("div[@id='isOk']").html("");
				$("div[@id='isOk']").append("用户账号已存在");
			}else{
				userExists = '-1';
				$("div[@id='isOk']").html("");
			}
		});
		}
	}

}
function usernameChange(){
	userExists = '0';
}

//接入方式
function chgAccessType(v){
	if(1 == v){
		$("td[@id='id_pvc1']").show();
		$("td[@id='id_pvc2']").show();
		$("td[@id='id_vlan1']").hide();
		$("td[@id='id_vlan2']").hide();
	}else{
		$("td[@id='id_pvc1']").hide();
		$("td[@id='id_pvc2']").hide();
		$("td[@id='id_vlan1']").show();
		$("td[@id='id_vlan2']").show();
	}
}

//上网方式
function chgnetType(v){
	if(2 == v){
		//路由
		$("tr[@id='id_routed']").show();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}else if(3 == v){
		//静态IP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").show();
		$("tr[@id='id_static2']").show();
	}else{
		//桥接和DHCP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}
}

function getOfficeList(){
	var cityId = $("select[@name='cityId']").val();
	if('' == cityId || '-1' == cityId){
		alert("请选择属地！");
		return false;
	}
	var officeName = $.trim($("input[@name='officeName']").val());
	if(officeName==""){
		alert("请输入要匹配的局向名称！");
		$("input[@name='officeName']").focus();
		return false;
	}
	var url = "<s:url value='/gwms/resource/hgwcustManage!getOfficeListByName.action'/>";
	$.post(url,{
		cityId:cityId,
		officeName:encodeURIComponent(officeName)
	},function(ajax){
		//alert(ajax);
		$("div[@id='div_office']").html("");
		$("div[@id='div_office']").html(ajax);
		$("div[@id='div_office']").append("&nbsp;<font color='#FF0000'>*</font>");
	});
}

//提交检查
function CheckForm(){

	var userId = $("input[@name='userId']");
	var username = $("input[@name='username']");
	var passwd = $("input[@name='passwd']");
	var cityId = $("select[@name='cityId']");
	var officeId = $("select[@name='officeId']");
	var accessType = $("select[@name='accessType']");
	var netType = $("select[@name='netType']");
	var vlanid = $("input[@name='vlanid']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var ip = $("input[@name='ip']");
	var gateway = $("input[@name='gateway']");
	var mask = $("input[@name='mask']");
	var dns = $("input[@name='dns']");
	var dealdate = $("input[@name='dealdate']").val();
	if(isAdd=='1'){
		if(userExists == '0'){
			alert("用户账号未检测！");
			username.focus();
			return false;
		}
	}
	if(userExists == '1'){
		alert("用户账号已存在");
		return false;
	}

	if(!IsNull(username.val(), "用户账号或专线号")){
		username.focus();
		return false;
	}
	//接入方式判断
	if(1 == accessType.val()){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(vlanid.val(),"VlanID")){
			vlanid.focus();
			return false;
		}
	}

	//上网方式
	if(2 == netType.val()){
		//路由
		if(!IsNull(passwd.val(),"用户密码")){
			passwd.focus();
			return false;
		}
	}else if(3 == netType.val()){
		//静态IP
		if(!IsIPAddr2(ip.val(),"IP地址")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"网关")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"子网掩码")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}

	if('' == cityId.val() || '-1' == cityId.val()){
		alert("属地不能为空");
		return false;
	}

	//if('' == officeId.val() || 'xuanze' == officeId.val()){
	//	alert("局向不能为空");
	//	return false;
	//}

	$("input[@name='dealdate']").val(strTime2Second(dealdate));

	userExists = '1';
}

//load
$(document).ready(function(){
	var _cityId = '<s:property value="cityId"/>';
	var _officeId = '<s:property value="officeId"/>';
	var _accessType = '<s:property value="accessType"/>';
	var _netType = '<s:property value="netType"/>';
	var _dealdate = '<s:property value="dealdate"/>';
	$("select[@name='cityId']").val(_cityId);
	if(isAdd == '1'){

	}else{
		//readonly
		$("div[@id='div_title']").html("用户编辑");

		$("input[@id='id_username']").attr("readOnly", true);

		$("select[@name='accessType']").val(_accessType);
		$("select[@name='netType']").val(_netType);
		chgAccessType(_accessType);
		chgnetType(_netType);
		//受理时间
		$("input[@name='dealdate']").val();

	}
})

//-->
</script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<form name="frm" action="hgwcustManage!saveHgwcust.action"
	onsubmit="return CheckForm();" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
						<div id="div_title">用户添加</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> 带' <font color="#FF0000">*</font>'的表单必须填写</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4"><input type="hidden" name="userId"
							value="<s:property value="userId"/>">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<Th align="center"><font size="2">必须填写</font></Th>
								<!-- <TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD> -->
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>用户帐户/专线号</TD>
						<TD><INPUT TYPE="text" id="id_username" NAME="username"
							maxlength=50 class=bk value="<s:property value="username"/>"
							onblur="checkUser();" onchange="usernameChange();">
						&nbsp; <font color="#FF0000">*
						<div id="isOk"></div>
						</font></TD>
						<TD class=column align="right" nowrap>用户密码</TD>
						<TD><INPUT TYPE="text" NAME="passwd" maxlength=30 class=bk
							value="<s:property value="passwd"/>"> &nbsp; <font
							color="#FF0000">*</font>&nbsp;&nbsp;路由方式时必须</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id=tdiid>
						<TD class=column align="right">接入方式</TD>
						<TD><SELECT name="accessType"
							onChange="chgAccessType(this.value);" class=bk>
							<option value="1">ADSL接入</option>
							<option value="2">LAN接入</option>
							<option value="3">光纤接入</option>
						</SELECT> &nbsp; <font color="#FF0000">*</font></TD>

						<TD class=column align="right" id="id_pvc1">VPI/VCI</TD>
						<TD id="id_pvc2"><INPUT TYPE="text" NAME="vpi"
							value="<s:property value="vpi"/>" maxlength=3 size=5 class=bk>
						/ <INPUT TYPE="text" NAME="vci" value="<s:property value="vci"/>"
							maxlength=3 size=5 class=bk> &nbsp; <font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" id="id_vlan1" style="display: none">
						VlanID</TD>
						<TD id="id_vlan2" style="display: none"><INPUT TYPE="text"
							NAME="vlanid" class=bk value="<s:property value="vlanid"/>">
						&nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>上网类型</TD>
						<TD><SELECT name='netType' class=bk
							onChange="chgnetType(this.value);">
							<option value='1'>桥接</option>
							<option value='2'>路由</option>
							<option value='3'>静态IP</option>
							<option value='4'>DHCP</option>
						</SELECT> &nbsp; <font color='red'>*</font> &nbsp;&nbsp;</TD>
						<TD class=column align="right" nowrap>网关类型</TD>

						<TD><select name="typeName">
							<s:iterator var="item" value="typeNameList">
								<option value="<s:property value='type_id'/>"><s:property
									value="type_name" /></option>
							</s:iterator>
						</select> &nbsp; <font color='red'>*</font> &nbsp;&nbsp;

						</TD>

					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>IP地址</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>掩码</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
					</TR>
					<TR id="id_static2" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>网关</TD>
						<TD><input type='text' name='gateway' class=bk
							value="<s:property value="gateway"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>DNS</TD>
						<TD><INPUT TYPE='text' NAME='dns' class=bk
							value="<s:property value="dns"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">属地</TD>
						<TD colspan="3"><select name="cityId" class="bk">
							<option value="-1">==请选择==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">== <s:property
									value="city_name" /> ==</option>
							</s:iterator>
						</select> &nbsp; <font color="#FF0000">*</font></TD>

					</TR>

					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<Th><font size="2">建议填写</font></Th>
								<!-- <TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD> -->
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">最大下行速率</TD>
						<TD><INPUT TYPE="text" NAME="maxDownSpeed" class=bk
							maxlength=20 value="<s:property value="maxDownSpeed"/>">
						&nbsp;</TD>
						<TD class=column align="right">最大上行速率</TD>
						<TD><INPUT TYPE="text" NAME="maxUpSpeed" class=bk
							maxlength=20 value="<s:property value="maxUpSpeed"/>">
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">绑定电话</TD>
						<TD><INPUT TYPE="text" NAME="bindphone" maxlength=15 class=bk
							value="<s:property value="bindphone"/>"></TD>
						<TD class=column align="right">允许用户上网数</TD>
						<TD><INPUT TYPE="text" NAME="maxUserNum" maxlength=5 class=bk
							value="<s:property value="maxUserNum"/>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">证件类型</TD>
						<TD><SELECT name="credType" onChange="" class=bk>
							<option value="1">身份证</option>
							<option value="2">工作证</option>
							<option value="3">军官证</option>
							<option value="4">其他</option>
						</SELECT> &nbsp;</TD>
						<TD class=column align="right">证件号码</TD>
						<TD><INPUT TYPE="text" NAME="credno" maxlength=30 class=bk
							size=20 value="<s:property value="credno"/>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">用户实名</TD>
						<TD><INPUT TYPE="text" NAME="realname" maxlength=20 class=bk
							value="<s:property value="realname"/>"> &nbsp;</TD>
						<TD class=column align="right">受理时间</TD>
						<TD><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<!--
												<img name="shortDateimg"
													onclick="new WdatePicker(document.frm.dealdate,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
													src="../../images/search.gif" width="15" height="12"
													border="0" alt="选择">
												 --> <img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择"> (YYYY-MM-DD HH:mm:ss)</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">联系人姓名</TD>
						<TD><INPUT TYPE="text" NAME="linkman" class=bk
							value="<s:property value="linkman"/>"> &nbsp;</TD>
						<TD class=column align="right">联系电话</TD>
						<TD><INPUT TYPE="text" NAME="linkphone" class=bk
							value="<s:property value="linkphone"/>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">联系人手机</TD>
						<TD><INPUT TYPE="text" NAME="mobile" class=bk
							value="<s:property value="mobile"/>"> &nbsp;</TD>
						<TD class=column align="right">联系人email</TD>
						<TD><INPUT TYPE="text" NAME="email" class=bk
							value="<s:property value="email"/>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">联系人地址</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="linkaddress"
							class=bk value="<s:property value="linkaddress"/>" size=60>
						&nbsp;</TD>
					</TR>

					<TR bgcolor="#FFFFFF" style="display:none">
						<TD colspan="4"><INPUT TYPE="hidden" NAME="gw_type"
							class=bk value="<s:property value="gw_type"/>" size=60>
						&nbsp;</TD>
					</TR>

					<TR>
						<TD colspan="4" align="right" class="green_foot"><INPUT
							TYPE="submit" value=" 保 存 " class="btn"> &nbsp;&nbsp; <INPUT
							TYPE="reset" value=" 重 置 " class="btn"></TD>
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
<%@ include file="../foot.jsp"%>
