<%--
ģ��BSS���� EVDO��������
Author: Jason
Version: 1.0.0
Date: 2009-10-13
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<title>BSSģ�⹤��</title>
<script type="text/javascript">
<!--//

function CheckForm(){
	var _servTypeId = $("input[@name='servTypeId']");
	var _operateType = $("input[@name='operateType']");
	var _username = $("input[@name='username']");
	var _customerId = $("input[@name='customerId']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("select[@name='orderType']");
	var _dealdate = $("input[@name='dealdate']");

	//ҵ������
	if('' == _servTypeId.val() || '-1' == _servTypeId.val()){
		alert("��ѡ��ҵ������");
		_servTypeId.focus();
		return false;
	}
	//��������
	if('' == _operateType.val() || '-1' == _operateType.val()){
		alert("��ѡ���������");
		_operateType.focus();
		return false;
	}
	//������ʽ
	if('' == _orderType.val() || '-1' == _orderType.val()){
		alert("��ѡ�񶩵�����");
		_orderType.focus();
		return false;
	}
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//�ͻ�ID
	if(!IsNull(_customerId.val(), "�ͻ�ID")){
		_customerId.focus();
		return false;
	}
	//�û��˺Ż�ר�ߺ�
	if(!IsNull(_username.val(), "�û��˺Ż�ר�ߺ�")){
		_username.focus();
		return false;
	}
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//alert(_servTypeId.val());
	//return false;
}

//-->
</script>
</head>
<body>
<form name="frm" action="sendBssSheet!sendSheet.action" onsubmit="return CheckForm();" method="post" target="_self">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'>
				<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">��������</TD>
						<TD width="30%"><select name="orderType" class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1">==��ͨADSL==</option>
							<option value="2">==��ͨLAN==</option>
							<option value="3">==��ͨ����==</option>
							<option value="4">==ר��LAN==</option>
							<option value="5">==ר�߹���==</option>
							<option value="6">==ר��ADSL==</option>
							</select> &nbsp; <font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" width="20%">��������ʱ��</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onclick="new WdatePicker(document.frm.dealdate,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
							src="../../images/search.gif" width="15" height="12" border="0"
							alt="ѡ��">&nbsp; <font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�ͻ�ID</TD>
						<TD >
							<INPUT TYPE="text" NAME="customerId" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align="right">����</TD>
						<TD>
							<select name="cityId" class="bk">
							<option value="-1">==��ѡ��==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
							</s:iterator>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�û��ʻ�/ר�ߺ�</TD>
						<TD colspan="3">
							<INPUT TYPE="text" NAME="username" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
					</TR>
					
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<INPUT TYPE="submit" value=" ���͹��� " class="btn">
						</TD>
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