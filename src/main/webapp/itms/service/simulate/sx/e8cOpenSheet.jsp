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
	var _deviceType = $("select[@name='deviceType']");
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
		alert("��ѡ����뷽ʽ");
		_orderType.focus();
		return false;
	}
	//�ն�����
	if('' == _deviceType.val() || '-1' == _deviceType.val()){
		alert("��ѡ���ն�����");
		_deviceType.focus();
		return false;
	}
	document.frm.submit();
}

</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateSxNewSheet!sendSheet.action'/>"
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
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">���뷽ʽ</TD>
						<TD width="30%"><select name="orderType" class="bk">
							<!-- <option value="-1">==��ѡ��==</option>
							<option value="1">==ADSL==</option>
							<option value="2">==LAN==</option>
							<option value="3">==PON==</option> -->
							<option value="4">==GPON==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align='right' nowrap>��ϵ��</TD>
						<TD><INPUT TYPE='text' NAME='linkman' class=bk value="">&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ͥסַ</TD>
						<TD>
							<textarea name="homeAddr" cols="30" rows="2"></textarea> 
							</TD>
						<TD class=column align="right" nowrap width="15%">�ն�����:</TD>
						<TD width="35%" >
							<SELECT name="deviceType" class="bk" >
							<!-- <OPTION value="1">e8-b</OPTION> -->
							<OPTION value="2">e8-c</OPTION>
							</SELECT>
							&nbsp;<font color="#FF0000">*</font>
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