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
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var usertid = $("select[@name='usertid']");
		//loid
		if (!IsNull(loid.val(), "�û�ID(�߼�ID)")) {
			loid.focus();
			return false;
		}
		//����ʱ��
		if (!IsNull(ordertime.val(), "����������")) {
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
													<TD align="center"><font size="2"><b>voip��Ϣ</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" width="20%">����ʱ��</TD>
										<TD width="30%"><input type="text" name="ordertime"
											value='<s:property value="dealdate" />' readonly class=bk>
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
										<TD width="20%" class=column align="right" nowrap>�˺�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="adaccount" maxlength=50
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>


									<!-- ҵ����� -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û�ID(�߼�ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�ͻ���</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="customerid" maxlength=20
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�豸ID��</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="deviceid" maxlength=200
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�ն������˿ں�</TD>
										<TD width="30%">
										<select name="usertid" class="bk">
											<option value="000">�˿�1</option>
											<option value="001">�˿�2</option>
											<option value="002">�˿�3</option>
											<option value="003">�˿�4</option>
											<option value="004">�˿�5</option>
											<option value="005">�˿�6</option>
											<option value="006">�˿�7</option>
											<option value="007">�˿�8</option>
											<option value="008">�˿�9</option>
											<option value="009">�˿�10</option>
											<option value="010">�˿�11</option>
											<option value="011">�˿�12</option>
											<option value="012">�˿�13</option>
											<option value="013">�˿�14</option>
											<option value="014">�˿�15</option>
											<option value="015">�˿�16</option>
											<option value="016">�˿�17</option>
											<option value="017">�˿�18</option>
											<option value="018">�˿�19</option>
											<option value="019">�˿�20</option>
											<option value="020">�˿�21</option>
											<option value="021">�˿�22</option>
											<option value="022">�˿�23</option>
											<option value="023">�˿�24</option>
											<option value="024">�˿�25</option>
											<option value="025">�˿�26</option>
											<option value="026">�˿�27</option>
											<option value="027">�˿�28</option>
											<option value="028">�˿�29</option>
											<option value="029">�˿�30</option>
											<option value="030">�˿�31</option>
											<option value="031">�˿�32</option>
										</select>
										<font color="#FF0000">*</font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�����û���</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authusername" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>��������</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authpassword" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û��绰����</TD>
										<TD colspan="3"><INPUT TYPE="text" NAME="sipuri" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									</TABLE>
									<!-- sip -->
									
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
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