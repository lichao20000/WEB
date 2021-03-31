
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%
	request.setCharacterEncoding("gbk");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="javascript://" onSubmit="return CheckForm();">
			<input type="hidden" name="oui" value="001F8F"/>
			<input type="hidden" name="type" value="1"/>
			<input type="hidden" name="account" value="025qxq"/>
			<input type="hidden" name="password" value="025qxq"/>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									业务工单管理
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="5" align="center">
													业务开通
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													设备序列号:
												</TD>
												<TD align="left" colspan="4">
													<input id="t1" type="text" name="serial_dev" value="4E200001F8F9BBC47" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													业务类别:
												</TD>
												<TD align="left" colspan="4">
													<input id="MediaFtp" type="radio" onClick="doChange(this);" name="type_" checked/><label for="MediaFtp">MediaFtp</label>
													<input id="DDNS" type="radio" onClick="doChange(this);" name="type_"/><label for="DDNS">DDNS</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													业务类型:
												</TD>
												<TD align="left" colspan="4">
													<select name="b_type">
														<option value="-1" selected>==请选择==</option>
														<option value="B1">业务开通</option>
														<option value="B2">业务暂停</option>
														<option value="B3">业务恢复</option>
														<option value="B4">业务参数修改</option>
														<option value="B5">业务关闭</option>
														<option value="B6">业务模块升级</option></select>
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none">
												<TD colspan="5" height="25" align="center" CLASS="green_foot">共享参数
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none">
												<TD height="25" align="right" CLASS="green_foot" style="padding-right:40px">↓
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数名称
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数类型
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数值
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数状态
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none" bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													<input type="checkbox" name="c_server"/>
												</TD>
												<TD align="center">SERVER
												</TD>
												<TD align="center">String
												</TD>
												<TD align="center">
													<input type="text" name="server" value="192.168.28.115" size="20"/>
												</TD>
												<TD align="center">1
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none">
												<TD colspan="5" height="25" align="center" CLASS="green_foot">个性化参数
												</TD>
											</TR>
											<TR isShow="DDNS" style="display:none">
												<TD height="25" align="right" CLASS="green_foot" style="padding-right:40px">↓
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数名称
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数类型
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数值
												</TD>
												<TD height="25" align="center" CLASS="green_foot">参数状态
												</TD>
											</TR>
											<TR isShow="DDNS" style="display:none" bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													<input type="checkbox" name="c_username"/>
												</TD>
												<TD align="center">UserID
												</TD>
												<TD align="center">String
												</TD>
												<TD align="center">
													<input type="text" name="username" value="lc" size="20"/>
												</TD>
												<TD align="center">10100
												</TD>
											</TR>
											<TR isShow="DDNS" style="display:none" bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													<input type="checkbox" name="c_pass"/>
												</TD>
												<TD align="center">Password
												</TD>
												<TD align="center">String
												</TD>
												<TD align="center">
													<input type="text" name="pass" value="123456" size="20"/>
												</TD>
												<TD align="center">10100
												</TD>
											</TR>
											<TR isShow="DDNS" style="display:none" bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													<input type="checkbox" name="c_domainname"/>
												</TD>
												<TD align="center">DomainName
												</TD>
												<TD align="center">String
												</TD>
												<TD align="center">
													<input type="text" name="domainname" value="abc.test.holdfast.com.cn" size="20"/>
												</TD>
												<TD align="center">10100
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none" bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													<input type="checkbox" name="c_server2"/>
												</TD>
												<TD align="center">server
												</TD>
												<TD align="center">String
												</TD>
												<TD align="center">
													<input type="text" name="server2" value="192.168.28.1" size="20"/>
												</TD>
												<TD align="center">10100
												</TD>
											</TR>
											<TR>
												<TD colspan="5" height="25" align="right" CLASS="green_foot">
													<IFRAME ID="childFrm" SRC="" STYLE="display:none"></IFRAME>
													<INPUT TYPE="button" value=" 发 送 " onClick="doCommit(document.forms[0]);" class="btn">&nbsp;
													<INPUT TYPE="reset" value=" 重 置 " class="btn">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function checkForm() {
	with(document.forms[0]) {
		if (serial_dev.value == "") {
			alert("设备序列号不能为空！");
			return false;
		}
		if (b_type.value == "-1") {
			alert("请选择业务类型！");
			return false;
		}
	}
	return true;
}

function doCommit(o) {
	if (!checkForm()) return false;
	var param = "?oui=" + o.oui.value + "&serial_dev=" + o.serial_dev.value + "&type=" + o.type.value + "&b_name=" + o.b_type.options[o.b_type.selectedIndex].innerText + "&b_type=" + o.b_type.value + "&account=" + o.account.value + "&password=" + o.password.value;
	if (o.type.value == 2) {
		if (o.c_server.checked == true) param += "&server=" + o.server.value;
		if (o.c_username.checked == true) param += "&username=" + o.username.value;
		if (o.c_pass.checked == true) param += "&pass=" + o.pass.value;
		if (o.c_domainname.checked == true) param += "&domainname=" + o.domainname.value;
		if (o.c_server2.checked == true) param += "&server2=" + o.server2.value;
	}
	//alert(param);
	document.all("childFrm").src = "business_open_save.jsp" + param + "&refresh=" + new Date().getTime();
	return true;
}

function doChange(o) {
	if (o.id == "DDNS") document.forms[0].type.value = 2;
	else document.forms[0].type.value = 1;
	showChg(o.id);
}

function showChg(s) {
	var tb1 = document.getElementById("tb1");
	var trs = tb1.getElementsByTagName("tr");
	for (var i=0; i<trs.length; i++) {
		if (trs[i].isShow == s || trs[i].isShow == null) {
			trs[i].style.display = "";
		} else {
			trs[i].style.display = "none";
		}
	}
}
//-->
</SCRIPT>