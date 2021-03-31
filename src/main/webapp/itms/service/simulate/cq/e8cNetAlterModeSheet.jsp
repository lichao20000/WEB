<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>更改模式</title>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var adaccount = $("input[@name='adaccount']");
		/* 业务字段 */
		//loid
		if (""==loid.val()&&""==adaccount.val()) {
			alert("请填写用户ID(逻辑ID)或宽带账号");
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
											value='9'>
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
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>宽带密码</TD>
										<TD width="30%"><INPUT TYPE="password" NAME="adpassword" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" width="20%">受理时间</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>客户号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="customerid" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>连接类型</TD>
										<TD width="30%"><select name="rgmode" class="bk">
												<option value="2">==桥接模式==</option>
												<option value="1">==路由模式==</option>
												<option value="3">==静态IP模式==</option>
												<option value="4">==DHCP==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>宽带 VLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
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