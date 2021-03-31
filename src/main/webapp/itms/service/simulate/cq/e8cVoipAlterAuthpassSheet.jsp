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
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var usertid = $("select[@name='usertid']");
		//loid
		if (!IsNull(loid.val(), "用户ID(逻辑ID)")) {
			loid.focus();
			return false;
		}
		//受理时间
		if (!IsNull(ordertime.val(), "开工单日期")) {
			ordertime.focus();
			return false;
		}

		document.frm.submit();
	}
	
	
	function changetype(){
		debugger;
		var protocoltype = $("#protocoltype").val();
		if(protocoltype=="0"){
			$("#siptab").show();
			$("#H248tab").hide();
		}
		else{
			$("#siptab").hide();
			$("#H248tab").show();
		}
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
											value='14'> <input
											type="hidden" name="operateType"
											value='12'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>voip信息</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" width="20%">受理时间</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>账号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>


									<!-- 业务参数 -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户ID(逻辑ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>客户号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="customerid" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>设备ID号</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="deviceid" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>终端语音端口号</TD>
										<TD width="30%">
										<select name="usertid" class="bk">
											<option value="000">端口1</option>
											<option value="001">端口2</option>
											<option value="002">端口3</option>
											<option value="003">端口4</option>
											<option value="004">端口5</option>
											<option value="005">端口6</option>
											<option value="006">端口7</option>
											<option value="007">端口8</option>
											<option value="008">端口9</option>
											<option value="009">端口10</option>
											<option value="010">端口11</option>
											<option value="011">端口12</option>
											<option value="012">端口13</option>
											<option value="013">端口14</option>
											<option value="014">端口15</option>
											<option value="015">端口16</option>
											<option value="016">端口17</option>
											<option value="017">端口18</option>
											<option value="018">端口19</option>
											<option value="019">端口20</option>
											<option value="020">端口21</option>
											<option value="021">端口22</option>
											<option value="022">端口23</option>
											<option value="023">端口24</option>
											<option value="024">端口25</option>
											<option value="025">端口26</option>
											<option value="026">端口27</option>
											<option value="027">端口28</option>
											<option value="028">端口29</option>
											<option value="029">端口30</option>
											<option value="030">端口31</option>
											<option value="031">端口32</option>
										</select>
										<font color="#FF0000">*</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>软交换用户名</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authusername" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>软交换密码</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authpassword" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>用户电话号码</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="sipuri" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									</TABLE>
									<!-- sip -->
									
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
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