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
	
	//���LOID,����
	function checkLoid()
	{
		var loid = $("input[@name='loid']").val().trim();
		if("" == loid){
			return false;
		}
		
		var url = "<s:url value='/itms/service/simulateSheet4cq!checkLoid4CQ.action'/>";
		$.post(url,{
			"loid":loid
		},function(ajax){
			if("0" == ajax)
			{
				$("select[@name='usertype']").val("0");
				$("select[@name='devicewan']").val('4');
			}
			else
			{
				var relt = ajax.split("|");
				var usertype = $("select[@name='usertype']");
				var devicewan = $("select[@name='devicewan']");
				devicewan.val(relt[0]);
				usertype.val(relt[2]);
			}
		}); 

	}
	
	
	function CheckForm() {
		var loid = $("input[@name='loid']");
		var ordertime = $("input[@name='ordertime']");
		var cityId = $("select[@name='cityId']");
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
		//����
		if ('' == cityId.val() || '-1' == cityId.val()) {
			alert("��ѡ������");
			cityId.focus();
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
											value='15'> 
											<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
												BORDER="0">
												<TR>
													<TD align="center"><font size="2"><b>voip��Ϣ</b></font></TD>
												</TR>
											</TABLE></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>Э������</TD>
										<TD colspan="3"><select name="protocoltype" id="protocoltype" class="bk" onchange="changetype()">
												<option value="0">==IMS SIP==</option>
												<!-- <option value="1">==SS SIP==</option> -->
												<option value="2">==SS H.248==</option>
												<option value="3">==IMS H.248==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
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

									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>���ŷ�ʽ</TD>
										<TD width="30%"><select name="orderself" class="bk">
												<option value="0">==δѡ��==</option>
												<option value="0">==�û�������װ==</option>
												<option value="1">==������Ա����==</option>
										</select> &nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>�豸��������</TD>
										<TD width="30%"><select name="devicewan" class="bk">
											<!-- onchange="change_select()"> -->
												<option value="4">==GPON==</option>
												<option value="3">==EPON==</option>
												<option value="1">==ADSL2+==</option>
												<option value="2">==LAN==</option>
												<option value="5">==VDSL2==</option>
												<option value="6">==EPONE==</option>
												<option value="7">==GPONE==</option>
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û�����</TD>
										<TD colspan="3"><select name="usertype" class="bk">
												<option value="0">==�����û�==</option>
												<option value="1">==�����û�==</option>
												<!-- <option value="3">==������STB==</option> -->
										</select> &nbsp; <font color="#FF0000">*</font></TD>
									</TR>
									
									<!-- ҵ����� -->
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�û�ID(�߼�ID)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="loid" maxlength=100
											class=bk value="" onblur="checkLoid()">&nbsp; <font color="#FF0000">*
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>VOIP UVLAN</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="vlanid" maxlength=10
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>DNS��������ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="imsdnsip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>DNS���÷�������ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="imsdnsbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>MG IP��ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>MG ����</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgmask" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>MG����IP</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mggw" maxlength=32
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
									
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="H248tab" style="display: none;">
									<!-- H.248 -->
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>H.248 MGע������(H.248Э��)</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="mgDomain" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�绰����</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="PhonenumberSip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����������IP��ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�����������˿�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����������IP��ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
										<TD width="20%" class=column align="right" nowrap>�����������˿�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="ssbakport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">
										</font></TD>
									</TR>
									
									</TABLE>
									<!-- H.248 -->
									
									<!-- sip -->
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="siptab" >
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>SIP������������ַ������</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
										<TD width="20%" class=column align="right" nowrap>SIP�����������˿ں�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����SIP��������ַ</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverbakip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
										<TD width="20%" class=column align="right" nowrap>����SIP�������˿�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="proxyserverbakport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000">
											</font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����ע�������IP��ַ��������</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="registrarserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>����ע��������˿�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="registrarserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>����ע�������IP��ַ��������</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="standbyregistrarserverip" maxlength=32
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>����ע��������˿�</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="standbyregistrarserverport" maxlength=6
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
									</TR>
									
									
									<TR bgcolor="#FFFFFF">
										<TD width="20%" class=column align="right" nowrap>�������û���</TD>
										<TD width="30%"><INPUT TYPE="text" NAME="authusername" maxlength=100
											class=bk value="">&nbsp; <font color="#FF0000"></font></TD>
										<TD width="20%" class=column align="right" nowrap>����������</TD>
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