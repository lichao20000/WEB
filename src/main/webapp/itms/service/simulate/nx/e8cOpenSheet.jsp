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
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("select[@name='orderType']");
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//�豸����
	if('' == _userType.val() || '-1' == _userType.val()){
		alert("��ѡ���豸����");
		_userType.val();
		return false;
	}
	//�û��˺�
	if(!IsNull(_username.val(), "�û��˺�")){
		_username.focus();
		return false;
	}
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//������ʽ
	if('' == _orderType.val() || '-1' == _orderType.val()){
		alert("��ѡ�񶩵�����");
		_orderType.focus();
		return false;
	}
	
	document.frm.submit();
}

</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateNxNewSheet!sendSheet.action'/>"
	 method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="querytable">
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
							value='<s:property value="operateType" />'>
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD align="center"><font size="2"><b>E8-C������Ϣ</b></font></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">����ʱ��</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
							<TD class=column align="right" nowrap>�߼�SN</TD>
						<TD><INPUT TYPE="text" NAME="username" maxlength=50 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>

					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">�û�����</TD>
						<TD width="30%"><select name="userType" class="bk">
							<option selected value="1" >==��ͥ����==</option>
							<option   value="2">==��ҵ����==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
						<!-- <TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" id="officeId">�����־</TD>
						<TD id="id_pvc2"><INPUT TYPE="text" NAME="officeId"
							maxlength=50 class=bk value=""></TD>
						<TD class=column align="right" id="zoneId">С����־</TD>
						<TD id="id_vlan2"><INPUT TYPE="text" NAME="zoneId" class=bk
							value="<s:property value="vlanid"/>"></TD>
					</TR> -->
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���뷽ʽ</TD>
						<TD width="30%"><select name="orderType" class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1">==ADSL==</option>
							<option value="2">==LAN==</option>
							<option value="3">==PON==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align='right' nowrap>��ϵ��</TD>
						<TD><INPUT TYPE='text' NAME='linkman' class=bk value="">&nbsp;</TD>
					</TR>
					<TR id="id_static2" bgcolor="#FFFFFF">
						<TD class=column align="right">֤������</TD>
						<TD><INPUT TYPE="text" NAME="credNo" maxlength=20 class=bk
							value=""></TD>
						<TD class=column align='right' nowrap>��ϵ�绰</TD>
						<TD><INPUT TYPE='text' NAME='linkphone' class=bk value="">&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">Email</TD>
						<TD><INPUT TYPE="text" NAME="linkEmail" class=bk maxlength=20
							value=""> &nbsp;</TD>
						<TD class=column align="right">�ֻ�</TD>
						<TD><INPUT TYPE="text" NAME="linkMobile" class=bk
							maxlength=20 value=""> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ͻ�Id</TD>
						<TD><INPUT TYPE="text" NAME="customerId" class=bk maxlength=20
							value=""> &nbsp;</TD>
						<TD class=column align="right">�ͻ��ʺ�</TD>
						<TD><INPUT TYPE="text" NAME="customerAccount" class=bk
							maxlength=20 value=""> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
					<TD class=column align="right">�ͻ�����</TD>
						<TD><INPUT TYPE="password" NAME="customerPwd" class=bk
							maxlength=20 value=""> &nbsp;</TD>
						<TD class=column align="right">��ͥסַ</TD>
						<TD >
							<textarea name="homeAddr" cols="30" rows="2"></textarea> 
							</TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;���͹���&nbsp;
						</button></TD>
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