
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
	request.setCharacterEncoding("gbk");
	DeviceAct deviceAct = new DeviceAct();
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
				<input type="hidden" name="status" value="10100"/>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									操作员权限同步
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
													操作员信息变更
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													域ID:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="area_id" value="1" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													操作员:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="login" value="admin" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													操作员密码:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="password" value="" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													状态:
												</TD>
												<TD align="left" colspan="4">
													<input id="s1" name="status_" type="radio" onclick="changeStatus('10100');" checked/><label for="s1">启用</label>&nbsp;
													<input id="s2" name="status_" type="radio" onclick="changeStatus('10103');"/><label for="s2">禁用</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													权限:<br><br>(全选)<input type="checkbox" name="selAll" onClick="selectAll(this)" checked/>
												</TD>
												<TD id="td1" align="left" colspan="4">
													<fieldSet>
														<legend>中间件软件配置<input type="checkbox" onClick="selectSome(this, 'rjpz');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="rjpz" id="10001" name="permit" type="checkbox" checked/><label for="10001">中间件模块配置</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjpz" id="10002" name="permit" type="checkbox" checked/><label for="10002">中间件模块参数配置</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjpz" id="10003" name="permit" type="checkbox" checked/><label for="10003">核心软件模块配置</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>中间件网关管理<input type="checkbox" onClick="selectSome(this, 'wggl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="wggl" id="20001" name="permit" type="checkbox" checked/><label for="20001">中间件网关查询</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20002" name="permit" type="checkbox" checked/><label for="20002">中间件配置文件生成</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20003" name="permit" type="checkbox" checked/><label for="20003">网关中间件日志上传</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20004" name="permit" type="checkbox" checked/><label for="20004">网关OUI导入</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>网关中间件软件管理<input type="checkbox" onClick="selectSome(this, 'rjgl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="rjgl" id="30001" name="permit" type="checkbox" checked/><label for="30001">网关中间件软件管理</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30002" name="permit" type="checkbox" checked/><label for="30002">网关中间件参数管理</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30004" name="permit" type="checkbox" checked/><label for="30004">网关中间件状态查询</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30005" name="permit" type="checkbox" checked/><label for="30005">网关核心软件升级</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30006" name="permit" type="checkbox" checked/><label for="30006">中间件操作任务查询</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30007" name="permit" type="checkbox" checked/><label for="30007">中间件网关组管理</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30008" name="permit" type="checkbox" checked/><label for="30008">网关中间件批量处理</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30009" name="permit" type="checkbox" checked/><label for="30009">批量操作任务查询</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>中间件故障管理<input type="checkbox" onClick="selectSome(this, 'gzgl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="gzgl" id="40001" name="permit" type="checkbox" checked/><label for="40001">中间件平台错误日志</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40002" name="permit" type="checkbox" checked/><label for="40002">网关中间件错误上报</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40003" name="permit" type="checkbox" checked/><label for="40003">中间件错误代码导入</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40004" name="permit" type="checkbox" checked/><label for="40004">网关操作日志</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>中间件系统配置<input type="checkbox" onClick="selectSome(this, 'xtpz');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="xtpz" id="50001" name="permit" type="checkbox" checked/><label for="50001">系统参数配置</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="xtpz" id="50002" name="permit" type="checkbox" checked/><label for="50002">硬件版本参数配置</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="xtpz" id="50003" name="permit" type="checkbox" checked/><label for="50003">软件包管理</label>
																</td>
															</tr>
														</table>
													</fieldSet>
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
function selectSome(o, v) {
	var p = document.getElementsByName("permit");
	if (p != null) {
		for (var i=0; i<p.length; i++) {
			if (p[i].isSel == v) {
				p[i].checked = o.checked;
			}
		}
	}
}

function selectAll(o) {
	var p = document.getElementsByName("permit");
	var q = document.getElementsByName("part");
	if (p != null) {
		for (var i=0; i<p.length; i++) {
			p[i].checked = o.checked;
		}
	}
	if (q != null) {
		for (var i=0; i<q.length; i++) {
			q[i].checked = o.checked;
		}
	}
}

function changeStatus(v) {
	document.forms[0].status.value = v;
}

function checkForm() {
	with(document.forms[0]) {
		if (area_id.value == "") {
			alert("域ID不能为空！");
			return false;
		}
		if (login.value == "-1") {
			alert("操作员不能为空！");
			return false;
		}
		if (password.value == "") {
			alert("操作员密码不能为空！");
			return false;
		}
	}
	return true;
}

function doCommit(o) {
	if (!checkForm()) return false;
	var param = "?area_id=" + o.area_id.value + "&login=" + o.login.value + "&password=" + o.password.value + "&status=" + o.status.value;
	var permit_ = "";
	var p = document.getElementsByName("permit");
	if (p != null) {
		var tmp = "";
		for (var i=0; i<p.length; i++) {
			if (p[i].checked == true) {
				if (tmp != "") {
					permit_ += tmp;
				}
				permit_ += p[i].id;
				tmp = ",";
			}
		}
	}
	param += "&permit=" + permit_;
	//alert(param);
	document.all("childFrm").src = "user_info_change_save.jsp" + param + "&refresh=" + new Date().getTime();
	return true;
}

//-->
</SCRIPT>