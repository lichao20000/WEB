<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>更改密码</title>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var adaccount = $("input[@name='adaccount']");
		var adpassword = $("input[@name='adpassword']");
		/* 业务字段 */
		//loid
		if (!IsNull(loid.val(), "用户ID(逻辑ID)")) {
			loid.focus();
			return false;
		}
		if (!IsNull(adaccount.val(), "宽带账号")) {
			adaccount.focus();
			return false;
		}
		if (!IsNull(adpassword.val(), "Pppoe密码")) {
			adpassword.focus();
			return false;
		}
		
		
		document.frm.submit();
	}

</script>
</head>
<body>
	<form name="frm"
		action="<s:url value='/itms/service/simulateSheet4cq!sendSheet4CQ.action'/>"
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
											value='22'> <input
											type="hidden" name="operateType"
											value='10'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>宽带信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>Pppoe密码</TD>
										<TD width="30%"><INPUT TYPE="password" NAME="adpassword" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
									</TR>
									
									
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