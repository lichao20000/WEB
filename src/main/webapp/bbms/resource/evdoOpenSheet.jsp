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
	var _uimImsi = $("input[@name='uimImsi']");
	var _dataEsn = $("input[@name='dataEsn']");
	var _serialCode = $("select[@name='serialCode']");
	var _vci = $("input[@name='vci']");
	var _vpi = $("input[@name='vpi']");
	var _vlanid = $("input[@name='vlanid']");
	var _custType = $("select[@name='custType']");
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
	//�û��˺Ż�ר�ߺ�
	if(!IsNull(_username.val(), "�û��˺Ż�ר�ߺ�")){
		_username.focus();
		return false;
	}
	//PVC��VLAN
	if('1' == _orderType.val() || '6' == _orderType.val() || 1 == _orderType.val() || 6 == _orderType.val()){
		if(!IsNumber(_vpi.val(),"vpi")){
			_vpi.focus();
			return false;
		}
		if(!IsNumber(_vci.val(),"vci")){
			_vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(_vlanid.val(),"VlanID")){
			_vlanid.focus();
			return false;
		}
	}
	//���ݿ�ESN����
	if(!IsNull(_dataEsn.val(), "���ݿ�ESN����")){
		_dataEsn.focus();
		return false;
	}
	//UIM��IMSI����
	if(!IsNull(_uimImsi.val(), "UIM��IMSI����")){
		_uimImsi.focus();
		return false;
	}
	//�ײ�
	if('' == _serialCode.val() || '-1' == _serialCode.val()){
		alert("��ѡ���ײ�");
		_serialCode.focus();
		return false;
	}
	//�ͻ�ID
	if(!IsNull(_customerId.val(), "�ͻ�ID")){
		_customerId.focus();
		return false;
	}
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//��ҵ���
	if('' == _custType.val() || '-1' == _custType.val()){
		alert("��ѡ����ҵ���");
		_custType.focus();
		return false;
	}
/**
	//�ͻ�����
	if(!IsNull(_customerId.val(), "�ͻ�����")){
		_customerId.focus();
		return false;
	}
	**/
}

//-->
</script>
</head>
<body>
<form name="frm" action="sendBssSheet!sendSheet.action" onsubmit="return CheckForm();" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4">
						<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'>
						<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">ҵ���������</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
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
					
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�û��ʻ�/ר�ߺ�</TD>
						<TD>
							<INPUT TYPE="text" NAME="username" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align='right' nowrap>�û�����</TD>
						<TD><input type='text' name='passwd' class=bk
							value="<s:property value="passwd"/>" maxlength=50>&nbsp;
						</TD>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" id="id_pvc1">VPI/VCI</TD>
						<TD id="id_pvc2"><INPUT TYPE="text" NAME="vpi"
							value="<s:property value="vpi"/>" maxlength=3 size=5 class=bk>/
						<INPUT TYPE="text" NAME="vci" value="<s:property value="vci"/>"
							maxlength=3 size=5 class=bk>&nbsp; <font color="#FF0000">* ADSL��ʽʱ����</font>
						</TD>
						<TD class=column align="right" id="id_vlan1" >VlanID</TD>
						<TD id="id_vlan2" ><INPUT TYPE="text"
							NAME="vlanid" class=bk value="<s:property value="vlanid"/>">&nbsp;
						<font color="#FF0000">*</font></TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>IP��ַ</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>">&nbsp; 
						</TD>
						<TD class=column align='right' nowrap>����</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>">&nbsp; 
						</TD>
					</TR>
					<TR id="id_static2" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>����</TD>
						<TD><input type='text' name='gateway' class=bk
							value="<s:property value="gateway"/>">&nbsp;
						</TD>
						<TD class=column align='right' nowrap>DNS</TD>
						<TD><INPUT TYPE='text' NAME='dns' class=bk
							value="<s:property value="dns"/>">&nbsp; 
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�����������</TD>
						<TD><INPUT TYPE="text" NAME="maxDownSpeed" class=bk
							maxlength=20 value="<s:property value="maxDownSpeed"/>">
						&nbsp;</TD>
						<TD class=column align="right">�����������</TD>
						<TD><INPUT TYPE="text" NAME="maxUpSpeed" class=bk
							maxlength=20 value="<s:property value="maxUpSpeed"/>">
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�����û�������</TD>
						<TD><INPUT TYPE="text" NAME="maxUserNum" maxlength=5 class=bk
							value="<s:property value="maxUserNum"/>"></TD>
						<TD class=column align="right">�ײ�����</TD>
						<TD>
							<select NAME="serialCode" class="bk">
								<option value="-1">==��ѡ��==</option>
								<option value="TC001">==XTa==</option>
								<option value="TC002">==XTa+==</option>
								<option value="TC003">==XTb==</option>
								<option value="TC004">==XTb+==</option>
								<option value="TC005">==����-Aϵͳ==</option>
								<option value="TC006">==����-Aϵͳ==</option>
							</select>
							&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<!-- �豸��Ϣ -->
					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">�豸�������</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>OUI</TD>
						<TD><input type='text' name='oui' class=bk
							value="<s:property value="oui"/>" maxlength=50>
						</TD>
						<TD class=column align='right' nowrap>���к�</TD>
						<TD><INPUT TYPE='text' NAME='devSn' class=bk
							value="<s:property value="devSn"/>" maxlength=50>
						</TD>
					</TR>
					<!-- 
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align='right' nowrap>�ն�����</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>">&nbsp;</TD>
						<TD class=column align='right' nowrap>�豸�ͺ�</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>">&nbsp;</TD>
					</TR>
					 -->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">���ݿ�ESN����</TD>
						<TD><INPUT TYPE="text" NAME="dataEsn" class=bk
							value="<s:property value="dataEsn"/>" maxlength=50>
						&nbsp;<font color="#FF0000">* </font></TD>
						<TD class=column align="right">���ݿ��ͺ�</TD>
						<TD><INPUT TYPE="text" NAME="dataModel" class=bk
							value="<s:property value="email"/>" maxlength=50> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">UIM��IMSI����</TD>
						<TD><INPUT TYPE="text" NAME="uimImsi" class=bk
							value="<s:property value="uimImsi"/>" maxlength=50>
						&nbsp;<font color="#FF0000">* </font></TD>
						<TD class=column align="right">UIM��ICCID����</TD>
						<TD><INPUT TYPE="text" NAME="uimIccid" class=bk
							value="<s:property value="uimIccid"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">UIM���ֻ�����</TD>
						<TD><INPUT TYPE="text" NAME="uimMobile" class=bk
							value="<s:property value="uimMobile"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">����ģʽ</TD>
						<TD><select NAME="workMode" class=bk>
								<option value="2">==����ģʽ==</option>
								<option value="1">==����ģʽ==</option>
							</select>
						&nbsp;</TD>
					</TR>
					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">�ͻ��������</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�ͻ�ID</TD>
						<TD >
							<INPUT TYPE="text" NAME="customerId" maxlength=50 class=bk 
								value="">&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align="right">����</TD>
						<TD><select name="cityId" class="bk">
							<option value="-1">==��ѡ��==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">==<s:property
									value="city_name" />==</option>
							</s:iterator>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ͻ�����</TD>
						<TD><INPUT TYPE="text" NAME="customerName" maxlength=50
							class=bk value="<s:property value="customerName"/>">
						&nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right">��ҵ���</TD>
						<TD>
							<select name="custType" class="bk">
								<option value="-1">==��ѡ��==</option>
								<s:iterator value="custTypeList">
								<option value="<s:property value="cust_type_name" />">==<s:property
									value="cust_type_name" />==</option>
								</s:iterator>
							</select>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�ͻ���ַ</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="customerAddr" class=bk
							value="<s:property value="customerAddr"/>" maxlength=200 size=100>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ҵ�˺�</TD>
						<TD><INPUT TYPE="text" NAME="customerAccount" class=bk
							value="<s:property value="customerAccount"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">��ҵ����</TD>
						<TD><INPUT TYPE="text" NAME="customerPasswd" class=bk
							value="<s:property value="customerPasswd"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ϵ������</TD>
						<TD><INPUT TYPE="text" NAME="linkman" class=bk
							value="<s:property value="linkman"/>" maxlength=50>
						&nbsp;</TD>
						<TD class=column align="right">��ϵ�绰</TD>
						<TD><INPUT TYPE="text" NAME="linkphone" class=bk
							value="<s:property value="linkphone"/>" maxlength=50>
						&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ϵ���ֻ�</TD>
						<TD><INPUT TYPE="text" NAME="mobile" class=bk
							value="<s:property value="mobile"/>" maxlength=50> &nbsp;</TD>
						<TD class=column align="right">��ϵ��email</TD>
						<TD><INPUT TYPE="text" NAME="email" class=bk
							value="<s:property value="email"/>" maxlength=50> &nbsp;</TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<INPUT TYPE="submit" value=" ���͹��� " class="btn">
							<input type="hidden" name="devType" value="">
							<input type="hidden" name="devModel" value="">
							<input type="hidden" name="officeId" value="">
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