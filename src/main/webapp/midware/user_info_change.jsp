
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
									����ԱȨ��ͬ��
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
													����Ա��Ϣ���
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													��ID:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="area_id" value="1" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													����Ա:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="login" value="admin" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													����Ա����:
												</TD>
												<TD align="left" colspan="4">
													<input type="text" name="password" value="" size="30"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													״̬:
												</TD>
												<TD align="left" colspan="4">
													<input id="s1" name="status_" type="radio" onclick="changeStatus('10100');" checked/><label for="s1">����</label>&nbsp;
													<input id="s2" name="status_" type="radio" onclick="changeStatus('10103');"/><label for="s2">����</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													Ȩ��:<br><br>(ȫѡ)<input type="checkbox" name="selAll" onClick="selectAll(this)" checked/>
												</TD>
												<TD id="td1" align="left" colspan="4">
													<fieldSet>
														<legend>�м���������<input type="checkbox" onClick="selectSome(this, 'rjpz');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="rjpz" id="10001" name="permit" type="checkbox" checked/><label for="10001">�м��ģ������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjpz" id="10002" name="permit" type="checkbox" checked/><label for="10002">�м��ģ���������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjpz" id="10003" name="permit" type="checkbox" checked/><label for="10003">�������ģ������</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>�м�����ع���<input type="checkbox" onClick="selectSome(this, 'wggl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="wggl" id="20001" name="permit" type="checkbox" checked/><label for="20001">�м�����ز�ѯ</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20002" name="permit" type="checkbox" checked/><label for="20002">�м�������ļ�����</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20003" name="permit" type="checkbox" checked/><label for="20003">�����м����־�ϴ�</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="wggl" id="20004" name="permit" type="checkbox" checked/><label for="20004">����OUI����</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>�����м���������<input type="checkbox" onClick="selectSome(this, 'rjgl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="rjgl" id="30001" name="permit" type="checkbox" checked/><label for="30001">�����м���������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30002" name="permit" type="checkbox" checked/><label for="30002">�����м����������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30004" name="permit" type="checkbox" checked/><label for="30004">�����м��״̬��ѯ</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30005" name="permit" type="checkbox" checked/><label for="30005">���غ����������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30006" name="permit" type="checkbox" checked/><label for="30006">�м�����������ѯ</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30007" name="permit" type="checkbox" checked/><label for="30007">�м�����������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30008" name="permit" type="checkbox" checked/><label for="30008">�����м����������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="rjgl" id="30009" name="permit" type="checkbox" checked/><label for="30009">�������������ѯ</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>�м�����Ϲ���<input type="checkbox" onClick="selectSome(this, 'gzgl');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="gzgl" id="40001" name="permit" type="checkbox" checked/><label for="40001">�м��ƽ̨������־</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40002" name="permit" type="checkbox" checked/><label for="40002">�����м�������ϱ�</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40003" name="permit" type="checkbox" checked/><label for="40003">�м��������뵼��</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="gzgl" id="40004" name="permit" type="checkbox" checked/><label for="40004">���ز�����־</label>
																</td>
															</tr>
														</table>
													</fieldSet><br>
													<fieldSet>
														<legend>�м��ϵͳ����<input type="checkbox" onClick="selectSome(this, 'xtpz');" name="part" checked/></legend>
														<table>
															<tr>
																<td>
																	<input isSel="xtpz" id="50001" name="permit" type="checkbox" checked/><label for="50001">ϵͳ��������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="xtpz" id="50002" name="permit" type="checkbox" checked/><label for="50002">Ӳ���汾��������</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input isSel="xtpz" id="50003" name="permit" type="checkbox" checked/><label for="50003">���������</label>
																</td>
															</tr>
														</table>
													</fieldSet>
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none">
												<TD colspan="5" height="25" align="center" CLASS="green_foot">�������
												</TD>
											</TR>
											<TR isShow="NONE" style="display:none">
												<TD height="25" align="right" CLASS="green_foot" style="padding-right:40px">��
												</TD>
												<TD height="25" align="center" CLASS="green_foot">��������
												</TD>
												<TD height="25" align="center" CLASS="green_foot">��������
												</TD>
												<TD height="25" align="center" CLASS="green_foot">����ֵ
												</TD>
												<TD height="25" align="center" CLASS="green_foot">����״̬
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
												<TD colspan="5" height="25" align="center" CLASS="green_foot">���Ի�����
												</TD>
											</TR>
											<TR isShow="DDNS" style="display:none">
												<TD height="25" align="right" CLASS="green_foot" style="padding-right:40px">��
												</TD>
												<TD height="25" align="center" CLASS="green_foot">��������
												</TD>
												<TD height="25" align="center" CLASS="green_foot">��������
												</TD>
												<TD height="25" align="center" CLASS="green_foot">����ֵ
												</TD>
												<TD height="25" align="center" CLASS="green_foot">����״̬
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
													<INPUT TYPE="button" value=" �� �� " onClick="doCommit(document.forms[0]);" class="btn">&nbsp;
													<INPUT TYPE="reset" value=" �� �� " class="btn">
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
			alert("��ID����Ϊ�գ�");
			return false;
		}
		if (login.value == "-1") {
			alert("����Ա����Ϊ�գ�");
			return false;
		}
		if (password.value == "") {
			alert("����Ա���벻��Ϊ�գ�");
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