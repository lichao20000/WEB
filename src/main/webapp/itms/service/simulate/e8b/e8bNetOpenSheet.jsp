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
var orderType = 2;
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _orderType = $("select[@name='orderType']");
	var _devType = $("select[@name='devType']");
	var _cityId = $("select[@name='cityId']");
	var _netUsername = $("input[@name='netUsername']");
	var _netPassword = $("input[@name='netPassword']");
	var _vlanId = $("input[@name='vlanId']");
	var _vpi = $("input[@name='vpi']");
	var _vci = $("input[@name='vci']");
	//alert(_dealdate.val());
	//����ʱ��
	if(!IsNull(_dealdate.val(), "����ʱ��")){
		_dealdate.focus();
		return false;
	}
	//������ʽ
	if('' == _orderType.val() || '-1' == _orderType.val()){
		alert("��ѡ�񶩵�����");
		_orderType.focus();
		return false;
	}
	//�豸����
	if('' == _devType.val() || '-1' == _devType.val()){
		alert("��ѡ���豸����");
		_devType.focus();
		return false;
	}
	
	//����
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	//����˺�
	if(!IsNull(_netUsername.val(), "����˺�")){
		_netUsername.focus();
		return false;
	}
	
	//�������
	if(!IsNull(_netPassword.val(), "�������")){
		_netPassword.focus();
		return false;
	}
	
	if(orderType != 1)
	{
		//vlanId
		if(!IsNull(_vlanId.val(), "VLANID") && orderType != 1){
			_vlanId.focus();
			return false;
		}
	}
	
	//PVC
	if(orderType == 1)
	{
		if(!IsNull(_vpi.val()+_vci.val(), "PVC")){
			_vpi.focus();
			return false;
		}
	}
	
	document.frm.submit();
}
function orderTypeChange(){
	var order_Type = $("select[@name='orderType']").val();
	if(order_Type==1)
	{
		$("tr[@id='pvc']").css("display","");
		$("tr[@id='vlanid']").css("display","none");
	}
	else
	{
		$("tr[@id='vlanid']").css("display","");
		$("tr[@id='pvc']").css("display","none");
	}
	parent.dyniframesize();
}
</script>
</head>
<body>
<form name="frm" action="<s:url value='/itms/service/simulateSheet!sendSheet.action'/>"
	 method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
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
								<TD align="center"><font size="2"><b>e8b����������Ϣ</b></font></TD>
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
							<TD class=column align="right" width="20%">���뷽ʽ</TD>
						<TD width="30%"><select name="orderType" onchange="orderTypeChange()"  class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1">==ADSL==</option>
							<option value="2">==LAN==</option>
							<option value="3">==PON==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR>

					<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">�豸����</TD>
						<TD width="30%"><select name="devType" class="bk">
							<option selected value="e8b">==E8-B==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">����</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="��ѡ������" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>����˺�</TD>
						<TD><INPUT TYPE="text" NAME="netUsername"  maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>�������</TD>
						<TD><INPUT TYPE="text" NAME="netPassword" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR id="vlanid" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" nowrap>VLANID</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="vlanId" maxlength=5 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					
					<TR id="pvc" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" nowrap>PVC</TD>
						<TD><INPUT TYPE="text" NAME="vpi" size="3" maxlength=5 class=bk
							value="8">&nbsp;/&nbsp;<INPUT TYPE="text" NAME="vci" size="3" maxlength=5 class=bk
							value="35">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>ADSL�󶨵绰</TD>
						<TD><INPUT TYPE="text" NAME="adslPhone"  maxlength=20 class=bk
							value=""></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�豸oui</TD>
						<TD><INPUT TYPE="text" NAME="oui"  maxlength=20 class=bk
							value=""></TD>
						<TD class=column align="right" nowrap>�豸���к�</TD>
						<TD><INPUT TYPE="text" NAME="deviceSn" maxlength=20 class=bk
							value=""></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�����������</TD>
						<TD><INPUT TYPE="text" NAME="maxDownSpeed"  maxlength=20 class=bk
							value=""></TD>
						<TD class=column align="right" nowrap>�����������</TD>
						<TD><INPUT TYPE="text" NAME="maxUpSpeed" maxlength=20 class=bk
							value=""></TD>
					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�����û�������</TD>
						<TD><INPUT TYPE="text" NAME="maxNetNum"  maxlength=20 class=bk
							value=""></TD>
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
						<TD class=column align="right">��ͥסַ</TD>
						<TD colspan="3">
							<textarea name="homeAddr" cols="30" rows="2"></textarea> 
							
							</TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;���͹���&nbsp;
						</button> </TD>
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