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
	function change_select(){
		var url = "<s:url value='/itms/service/BssSheetDelACT!getIptvPort.action'/>";
		var iptvUsername = $("select[@name='iptvUsername']").val();
		var loid = $("input[@name='loid']").val();
		if("-1"==iptvUsername){
			return;
		}
		$.post(url,{
			iptvUsername:iptvUsername,
			loid:loid
		},function(ajax){
			$("input[@name='iptvLanPort']").val(ajax);
		});
	}
	function CheckForm() {
		var _iptvLanPort = $("select[@name='iptvLanPort']");
		var _cityId = $("select[@name='cityId']");
		var _iptvUsername = $("select[@name='iptvUsername']");
		//端口
		if ('' == _iptvLanPort.val() || '-1' == _iptvLanPort.val()) {
			alert("请选择端口");
			_sipVoipPort.focus();
			return false;
		}
		//属地
		if ('' == _cityId.val() || '-1' == _cityId.val()) {
			alert("请选择属地");
			_cityId.focus();
			return false;
		}
		//VOIP认证账号
		if (!IsNull(_iptvUsername.val(), "Iptv认证账号")) {
			_iptvUsername.focus();
			return false;
		}
		document.frm.submit();
	}
</script>
</head>
<body>
	<form name="frm"
		action="<s:url value='/itms/service/BssSheetDelACT!sendSheet.action'/>"
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
											type="hidden" name="loid"
											value='<s:property value="loid" />'>
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>Iptv销户信息</b></font></TD>

												</TR>
											</TABLE></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>IPTV接入账号</TD>
										<TD><s:select list="iptvUsernameList" name="iptvUsername"
												headerKey="-1" headerValue="请选择IPTV接入账号" cssClass="bk" onchange="change_select()"></s:select>
												&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD class=column align="right" width="20%">属地</TD>
										<TD width="30%"><s:select list="cityList" name="cityId"
												headerKey="-1" headerValue="请选择属地" listKey="city_id"
												listValue="city_name" value="cityId" cssClass="bk"></s:select>
											&nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									<TR id="id_static1" bgcolor="#FFFFFF">
										<TD class=column align="right" nowrap>IPTV端口</TD>
										<TD><!--<s:select list="iptvLanPortList" name="iptvLanPort"
												headerKey="-1" headerValue="请选择端口" cssClass="bk"></s:select>
												&nbsp; <font color="#FF0000">*
											</font>-->
											<input type="text" name="iptvLanPort" class=bk value="">
												&nbsp;<font color="#FF0000">*</font>
										</TD>
										<TD class=column align="right" nowrap></TD>
										<TD></TD>
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