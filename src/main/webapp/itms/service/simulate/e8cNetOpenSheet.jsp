<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSSģ�⹤��</title>
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
		//����ʱ��
		if (!IsNull(_dealdate.val(), "����ʱ��")) {
			_dealdate.focus();
			return false;
		}
		//����
		if ('' == _cityId.val() || '-1' == _cityId.val()) {
			alert("��ѡ������");
			_cityId.focus();
			return false;
		}
		//����˺�
		if (!IsNull(_netUsername.val(), "����˺�")) {
			_netUsername.focus();
			return false;
		}
		if (!IsNull(_vlanId.val(), "VLANID")) {
			_vlanId.focus();
			return false;
		}
		if ('' == _useriptype.val() || '-1' == _useriptype.val()) {
			alert("��ѡ���û�IP����");
			_useriptype.focus();
			return false;
		}
		if ('' == _wlantype.val() || '-1' == _wlantype.val()) {
			alert("��ѡ��������ʽ");
			_wlantype.focus();
			return false;
		}
		if ('3' == _wlantype.val()) {
			if (!IsNull(_ipaddress.val(), "IP��ַ")) {
				_ipaddress.focus();
				return false;
			}
			if (!IsNull(_code.val(), "����")) {
				_code.focus();
				return false;
			}
			if (!IsNull(_netway.val(), "����")) {
				_netway.focus();
				return false;
			}
			if (!IsNull(_dns.val(), "DNS")) {
				_dns.focus();
				return false;
			}
		}
		if ('' == _userType.val() || '-1' == _userType.val()) {
			alert("��ѡ������");
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
													<TD align="center"><font size="2"><b>����������Ϣ</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">����ʱ��</TD>
										<TD width="30%"><input type="text" name="dealdate"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
										<!-- <TD class=column align="right" nowrap>LOID</TD>
										<TD><INPUT TYPE="text" id="username" NAME="username"
											onblur="checkUsername()" maxlength=50 class=bk
											disabled="disabled" value="����ѡ���û�����">&nbsp; <font
											color="#FF0000">* </font>
										<div id="usernameDiv"></div></TD> -->
										<TD class=column align="right" width="20%">����</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="��ѡ������" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>

									<%-- <TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">�û�����</TD>
										<TD width="30%"><select name="userType" class="bk"
											onchange="change(this)">
												<option value="-1">==��ѡ��==</option>
												<option value="1">==��ͥ����==</option>
												<option value="2">==��ҵ����==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										
									</TR> --%>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>����˺�</TD>
										<TD><INPUT TYPE="text" NAME="netUsername" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<!-- 										<TD class=column align="right" nowrap>�������</TD>
										<TD><INPUT TYPE="text" NAME="netPassword" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD> -->
										<TD class=column align="right" nowrap>VLANID</TD>
										<TD><INPUT TYPE="text" NAME="vlanId" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>�û�IP����</TD>
										<TD width="30%"><select name="useriptype" class="bk">
												<option value="-1">==��ѡ��==</option>
												<option value="0">==������ջ==</option>
												<option value="1">==����˫ջ==</option>
												<option value="2">==˽����ջ==</option>
												<option value="3">==˽��˫ջ==</option>
												<option value="4">==DS-Lite==</option>
												<option value="5">==��V6==</option>
												<option value="6">==LAFT6==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>������ʽ</TD>
										<TD width="30%"><select name="wlantype" class="bk"
											onchange="change_select()">
												<option value="-1">==��ѡ��==</option>
												<option value="1">==�Ž�==</option>
												<option value="2">==·��==</option>
												<option value="3">==��̬IP==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="dis1" style="display: none;">
										<TD class=column align="right" nowrap>IP��ַ</TD>
										<TD><INPUT TYPE="text" NAME="ipaddress" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>����</TD>
										<TD><INPUT TYPE="text" NAME="code" maxlength=20 class=bk
											value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="dis2" style="display: none;">
										<TD class=column align="right" nowrap>����</TD>
										<TD><INPUT TYPE="text" NAME="netway" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">*</font></TD>
										<TD class=column align="right" nowrap>DNS</TD>
										<TD><INPUT TYPE="text" NAME="dns" maxlength=20 class=bk
											value="">&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">�����ʶ</TD>
										<TD width="30%"><select name="userType" class="bk"
											onchange="change(this)">
												<option value="-1">==��ѡ��==</option>
												<option value="1">==����==</option>
												<option value="2">==�����==</option>
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
												onclick="CheckForm()">&nbsp;���͹���&nbsp;</button></TD>
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