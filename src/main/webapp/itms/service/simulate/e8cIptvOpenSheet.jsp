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
		var _cityId = $("select[@name='cityId']");
		var _iptvUsername = $("input[@name='iptvUsername']");
		var _servNum = $("select[@name='servNum']");
		var _username = $("input[@name='username']");
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
		//IPTV���������˺�
		if (!IsNull(_iptvUsername.val(), "IPTV���������˺�")) {
			_iptvUsername.focus();
			return false;
		}

		//IPTV����
		if ('' == _servNum.val() || '-1' == _servNum.val()) {
			alert("��ѡ��IPTV����");
			_servNum.focus();
			return false;
		}
		document.frm.submit();
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
											value='<s:property value="operateType" />'><input
											type="hidden" name="username"
											value='<s:property value="loid" />'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>IPTV������Ϣ</b></font></TD>
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
					<TD><INPUT TYPE="text" id="username" NAME="username" onblur="checkUserInfo()" maxlength=50 class=bk
						disabled="disabled" 	value="����ѡ���û�����">&nbsp; <font id="usernameDiv" color="#FF0000">* </font></TD> -->
										<TD class=column align="right" nowrap>����</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="��ѡ������" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>

									<%-- <TR  bgcolor="#FFFFFF">
					<TD class=column align="right" width="20%">�û�����</TD>
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
							<option value="-1">==��ѡ��==</option>
							<option value="1">==��ͥ����==</option>
							<option value="2">==��ҵ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR> --%>

									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>IPTV�����˺�</TD>
										<TD><INPUT TYPE="text" NAME="iptvUsername" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD class=column align="right" nowrap>IPTV����</TD>
										<TD><s:property value="nameListHtml" escapeHtml="false" />&nbsp;
										  <font color="#FF0000">*</font></TD>
									</TR>
									<!-- <TR  bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��ͨ�˿�</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="iptvLanPort" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>

					</TR> -->
									<TR>
										<TD colspan="4" align="right" class="green_foot">

											<button onclick="CheckForm()">&nbsp;���͹���&nbsp;</button>
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