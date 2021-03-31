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
	function CheckForm() {
		var _dealdate = $("input[@name='dealdate']");
		var _username = $("input[@name='username']");
		var _cityId = $("select[@name='cityId']");
		var _netUsername = $("input[@name='netUsername']");
		var _vlanId = $("input[@name='vlanId']");
		var _ipaddress = $("input[@name='ipaddress']");
		var _code = $("input[@name='code']");
		var _dns = $("input[@name='dns']");
		var _netway = $("input[@name='netway']");
		var _useriptype = $("select[@name='useriptype']");
		var _wlantype = $("select[@name='wlantype']");
		var _userType = $("select[@name='userType']");
		//受理时间
		if (!IsNull(_dealdate.val(), "受理时间")) {
			_dealdate.focus();
			return false;
		}
		//属地
		if ('' == _cityId.val() || '-1' == _cityId.val()) {
			alert("请选择属地");
			_cityId.focus();
			return false;
		}
		//宽带账号
		if (!IsNull(_netUsername.val(), "宽带账号")) {
			_netUsername.focus();
			return false;
		}
		if (!IsNull(_vlanId.val(), "VLANID")) {
			_vlanId.focus();
			return false;
		}
		if ('' == _useriptype.val() || '-1' == _useriptype.val()) {
			alert("请选择用户IP类型");
			_useriptype.focus();
			return false;
		}
		if ('' == _wlantype.val() || '-1' == _wlantype.val()) {
			alert("请选择上网方式");
			_wlantype.focus();
			return false;
		}
		if ('3' == _wlantype.val()) {
			if (!IsNull(_ipaddress.val(), "IP地址")) {
				_ipaddress.focus();
				return false;
			}
			if (!IsNull(_code.val(), "掩码")) {
				_code.focus();
				return false;
			}
			if (!IsNull(_netway.val(), "网关")) {
				_netway.focus();
				return false;
			}
			if (!IsNull(_dns.val(), "DNS")) {
				_dns.focus();
				return false;
			}
		}
		if ('' == _userType.val() || '-1' == _userType.val()) {
			alert("请选择类型");
			_userType.focus();
			return false;
		}
		document.frm.submit();
	}

	function change_select() {
		var wlantype = $("select[@name='wlantype']");
		if ('3' == wlantype.val()) {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "");
			$("tr[@id='dis2']").css("display", "");
		} else {
			$("input[@name='ipaddress']").val("");
			$("input[@name='code']").val("");
			$("input[@name='dns']").val("");
			$("input[@name='netway']").val("");
			$("tr[@id='dis1']").css("display", "none");
			$("tr[@id='dis2']").css("display", "none");
		}
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
											value='<s:property value="operateType" />'> <input
											type="hidden" name="username"
											value='<s:property value="loid" />'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>上网开户信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">受理时间</TD>
										<TD width="30%"><input type="text" name="dealdate"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
										<!-- <TD class=column align="right" nowrap>LOID</TD>
										<TD><INPUT TYPE="text" id="username" NAME="username"
											onblur="checkUsername()" maxlength=50 class=bk
											disabled="disabled" value="请先选择用户类型">&nbsp; <font
											color="#FF0000">* </font>
										<div id="usernameDiv"></div></TD> -->
										<TD class=column align="right" width="20%">属地</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="请选择属地" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>

									<%-- <TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">用户类型</TD>
										<TD width="30%"><select name="userType" class="bk"
											onchange="change(this)">
												<option value="-1">==请选择==</option>
												<option value="1">==家庭网关==</option>
												<option value="2">==企业网关==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										
									</TR> --%>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>宽带账号</TD>
										<TD><INPUT TYPE="text" NAME="netUsername" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<!-- 										<TD class=column align="right" nowrap>宽带密码</TD>
										<TD><INPUT TYPE="text" NAME="netPassword" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD> -->
										<TD class=column align="right" nowrap>VLANID</TD>
										<TD><INPUT TYPE="text" NAME="vlanId" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>用户IP类型</TD>
										<TD width="30%"><select name="useriptype" class="bk">
												<option value="-1">==请选择==</option>
												<option value="0">==公网单栈==</option>
												<option value="1">==公网双栈==</option>
												<option value="2">==私网单栈==</option>
												<option value="3">==私网双栈==</option>
												<option value="4">==DS-Lite==</option>
												<option value="5">==纯V6==</option>
												<option value="6">==LAFT6==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>上网方式</TD>
										<TD width="30%"><select name="wlantype" class="bk"
											onchange="change_select()">
												<option value="-1">==请选择==</option>
												<option value="1">==桥接==</option>
												<option value="2">==路由==</option>
												<option value="3">==静态IP==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="dis1" style="display: none;">
										<TD class=column align="right" nowrap>IP地址</TD>
										<TD><INPUT TYPE="text" NAME="ipaddress" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>掩码</TD>
										<TD><INPUT TYPE="text" NAME="code" maxlength=20 class=bk
											value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="dis2" style="display: none;">
										<TD class=column align="right" nowrap>网关</TD>
										<TD><INPUT TYPE="text" NAME="netway" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>DNS</TD>
										<TD><INPUT TYPE="text" NAME="dns" maxlength=20 class=bk
											value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">宽带标识</TD>
										<TD width="30%"><select name="userType" class="bk"
											onchange="change(this)">
												<option value="-1">==请选择==</option>
												<option value="1">==多宽带==</option>
												<option value="2">==单宽带==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" width="20%"></TD>
										<TD width="30%"></TD>
									</TR>
									<!--<TR id="vlanid" bgcolor="#FFFFFF" style="display: none">
										<TD class=column align="right" nowrap>VLANID</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="vlanId"
											maxlength=5 class=bk value="">&nbsp; <font
											color="#FF0000">* </font></TD>
									</TR>
									<TR id="pvc" bgcolor="#FFFFFF" style="display: none">
										<TD class=column align="right" nowrap>PVC</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="vpi" size="3"
											maxlength=5 class=bk value="8">&nbsp;/&nbsp;<INPUT
											TYPE="text" NAME="vci" size="3" maxlength=5 class=bk
											value="35">&nbsp; <font color="#FF0000">* </font></TD>
									</TR> -->
									<TR>
										<TD colspan="4" align="right" class="green_foot"><button
												onclick="CheckForm()">&nbsp;发送工单&nbsp;</button></TD>
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