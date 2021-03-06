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
		var _cityId = $("select[@name='cityId']");
		var _voipUsername = $("input[@name='voipUsername']");
		var _voipPasswd = $("input[@name='voipPasswd']");
		var _voipTelepone = $("input[@name='voipTelepone']");
		var _linePort = $("select[@name='linePort']");
		var _username = $("input[@name='username']");
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
		//VOIP认证账号
		if (!IsNull(_voipUsername.val(), "VOIP认证账号")) {
			_voipUsername.focus();
			return false;
		}
		//VOIP认证密码
		if (!IsNull(_voipPasswd.val(), "VOIP认证密码")) {
			_voipPasswd.focus();
			return false;
		}
		//设备类型
		if ('' == _cityId.val() || '-1' == _cityId.val()) {
			alert("请选择设备类型");
			_orderType.focus();
			return false;
		}
		//用户账号
		if (!IsNull(_username.val(), "用户账号")) {
			_username.focus();
			return false;
		}
		//业务电话号码
		if (!IsNull(_voipTelepone.val(), "业务电话号码")) {
			_voipTelepone.focus();
			return false;
		}
		//语音端口
		if ('' == _linePort.val() || '-1' == _linePort.val()) {
			alert("请选择语音端口");
			_linePort.focus();
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
													<TD align="center"><font size="2"><b>VOIP开户信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									<s:if test="%{count < 1}">
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" width="20%"
												style="text-align: center;">用户【<s:property value="loid" />】不能开通语音业务
											</TD>
										</TR>
									</s:if>
									<s:else>
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" width="20%">受理时间</TD>
											<TD width="30%"><input type="text" name="dealdate"
												value='<s:property value="dealdate" />' readonly class=bk>
												<img name="shortDateimg"
												onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
												src="../../images/dateButton.png" width="15" height="12"
												border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
											<TD class=column align="right" width="20%">属地</TD>
											<TD width="30%"><s:select list="cityList" name="cityId"
													headerKey="-1" headerValue="请选择属地" listKey="city_id"
													listValue="city_name" value="cityId" cssClass="bk"></s:select>
												&nbsp; <font color="#FF0000">*</font></TD>

										</TR>
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" nowrap>VOIP认证账号</TD>
											<TD><INPUT TYPE="text" NAME="voipUsername" maxlength=20
												class=bk value="">&nbsp; <font color="#FF0000">*
											</font></TD>
											<TD class=column align="right" nowrap>VOIP认证密码</TD>
											<TD><INPUT TYPE="text" NAME="voipPasswd" maxlength=20
												class=bk value="">&nbsp; <font color="#FF0000">*
											</font></TD>
										</TR>
										<TR bgcolor="#FFFFFF">
											<TD class=column align="right" nowrap>业务电话号码</TD>
											<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
												class=bk value="">&nbsp; <font color="#FF0000">*
											</font></TD>
											<TD class=column align="right" nowrap>语音端口</TD>
											<TD><s:property value="nameListHtml" escapeHtml="false" />&nbsp;
												<font color="#FF0000">*</font></TD>
										</TR>
										<TR>
											<TD colspan="4" align="right" class="green_foot"><button
													onclick="CheckForm()">&nbsp;发送工单&nbsp;</button></TD>
										</TR>
									</s:else>

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
