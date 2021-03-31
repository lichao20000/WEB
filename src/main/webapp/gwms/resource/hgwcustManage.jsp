<%--
ҵ���ֹ�����
Author: Jason
Version: 1.0.0
Date: 2009-09-25
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<title>��ͥ���ؿͻ���ӱ༭</title>
<script type="text/javascript">
//ҳ��Ϊ�༭�������
var isAdd = '<s:property value="isAdd"/>';
//����û�ʱ�ж��Ƿ��û��Ѿ�����
var userExists = '0';
$(function(){

	var typeName = '<s:property value="typeName"/>';
	$("select[name='typeName']").val(typeName);
});

function checkUser(){
	var username = $.trim($("input[@name='username']").val());
	if(username!=""){
		if(isAdd == '1'){
		var url = "<s:url value='/gwms/resource/hgwcustManage!checkUser.action'/>";
		$.post(url,{
			username:encodeURIComponent(username)
		},function(ajax){
			//alert(ajax);
			if(ajax == '1'){
				userExists = '1';
				$("div[@id='isOk']").show();
				$("div[@id='isOk']").html("");
				$("div[@id='isOk']").append("�û��˺��Ѵ���");
			}else{
				userExists = '-1';
				$("div[@id='isOk']").html("");
			}
		});
		}
	}

}
function usernameChange(){
	userExists = '0';
}

//���뷽ʽ
function chgAccessType(v){
	if(1 == v){
		$("td[@id='id_pvc1']").show();
		$("td[@id='id_pvc2']").show();
		$("td[@id='id_vlan1']").hide();
		$("td[@id='id_vlan2']").hide();
	}else{
		$("td[@id='id_pvc1']").hide();
		$("td[@id='id_pvc2']").hide();
		$("td[@id='id_vlan1']").show();
		$("td[@id='id_vlan2']").show();
	}
}

//������ʽ
function chgnetType(v){
	if(2 == v){
		//·��
		$("tr[@id='id_routed']").show();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}else if(3 == v){
		//��̬IP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").show();
		$("tr[@id='id_static2']").show();
	}else{
		//�ŽӺ�DHCP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}
}

function getOfficeList(){
	var cityId = $("select[@name='cityId']").val();
	if('' == cityId || '-1' == cityId){
		alert("��ѡ�����أ�");
		return false;
	}
	var officeName = $.trim($("input[@name='officeName']").val());
	if(officeName==""){
		alert("������Ҫƥ��ľ������ƣ�");
		$("input[@name='officeName']").focus();
		return false;
	}
	var url = "<s:url value='/gwms/resource/hgwcustManage!getOfficeListByName.action'/>";
	$.post(url,{
		cityId:cityId,
		officeName:encodeURIComponent(officeName)
	},function(ajax){
		//alert(ajax);
		$("div[@id='div_office']").html("");
		$("div[@id='div_office']").html(ajax);
		$("div[@id='div_office']").append("&nbsp;<font color='#FF0000'>*</font>");
	});
}

//�ύ���
function CheckForm(){

	var userId = $("input[@name='userId']");
	var username = $("input[@name='username']");
	var passwd = $("input[@name='passwd']");
	var cityId = $("select[@name='cityId']");
	var officeId = $("select[@name='officeId']");
	var accessType = $("select[@name='accessType']");
	var netType = $("select[@name='netType']");
	var vlanid = $("input[@name='vlanid']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var ip = $("input[@name='ip']");
	var gateway = $("input[@name='gateway']");
	var mask = $("input[@name='mask']");
	var dns = $("input[@name='dns']");
	var dealdate = $("input[@name='dealdate']").val();
	if(isAdd=='1'){
		if(userExists == '0'){
			alert("�û��˺�δ��⣡");
			username.focus();
			return false;
		}
	}
	if(userExists == '1'){
		alert("�û��˺��Ѵ���");
		return false;
	}

	if(!IsNull(username.val(), "�û��˺Ż�ר�ߺ�")){
		username.focus();
		return false;
	}
	//���뷽ʽ�ж�
	if(1 == accessType.val()){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(vlanid.val(),"VlanID")){
			vlanid.focus();
			return false;
		}
	}

	//������ʽ
	if(2 == netType.val()){
		//·��
		if(!IsNull(passwd.val(),"�û�����")){
			passwd.focus();
			return false;
		}
	}else if(3 == netType.val()){
		//��̬IP
		if(!IsIPAddr2(ip.val(),"IP��ַ")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"����")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"��������")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}

	if('' == cityId.val() || '-1' == cityId.val()){
		alert("���ز���Ϊ��");
		return false;
	}

	//if('' == officeId.val() || 'xuanze' == officeId.val()){
	//	alert("������Ϊ��");
	//	return false;
	//}

	$("input[@name='dealdate']").val(strTime2Second(dealdate));

	userExists = '1';
}

//load
$(document).ready(function(){
	var _cityId = '<s:property value="cityId"/>';
	var _officeId = '<s:property value="officeId"/>';
	var _accessType = '<s:property value="accessType"/>';
	var _netType = '<s:property value="netType"/>';
	var _dealdate = '<s:property value="dealdate"/>';
	$("select[@name='cityId']").val(_cityId);
	if(isAdd == '1'){

	}else{
		//readonly
		$("div[@id='div_title']").html("�û��༭");

		$("input[@id='id_username']").attr("readOnly", true);

		$("select[@name='accessType']").val(_accessType);
		$("select[@name='netType']").val(_netType);
		chgAccessType(_accessType);
		chgnetType(_netType);
		//����ʱ��
		$("input[@name='dealdate']").val();

	}
})

//-->
</script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<form name="frm" action="hgwcustManage!saveHgwcust.action"
	onsubmit="return CheckForm();" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
						<div id="div_title">�û����</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> ��' <font color="#FF0000">*</font>'�ı�������д</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4"><input type="hidden" name="userId"
							value="<s:property value="userId"/>">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<Th align="center"><font size="2">������д</font></Th>
								<!-- <TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD> -->
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>�û��ʻ�/ר�ߺ�</TD>
						<TD><INPUT TYPE="text" id="id_username" NAME="username"
							maxlength=50 class=bk value="<s:property value="username"/>"
							onblur="checkUser();" onchange="usernameChange();">
						&nbsp; <font color="#FF0000">*
						<div id="isOk"></div>
						</font></TD>
						<TD class=column align="right" nowrap>�û�����</TD>
						<TD><INPUT TYPE="text" NAME="passwd" maxlength=30 class=bk
							value="<s:property value="passwd"/>"> &nbsp; <font
							color="#FF0000">*</font>&nbsp;&nbsp;·�ɷ�ʽʱ����</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id=tdiid>
						<TD class=column align="right">���뷽ʽ</TD>
						<TD><SELECT name="accessType"
							onChange="chgAccessType(this.value);" class=bk>
							<option value="1">ADSL����</option>
							<option value="2">LAN����</option>
							<option value="3">���˽���</option>
						</SELECT> &nbsp; <font color="#FF0000">*</font></TD>

						<TD class=column align="right" id="id_pvc1">VPI/VCI</TD>
						<TD id="id_pvc2"><INPUT TYPE="text" NAME="vpi"
							value="<s:property value="vpi"/>" maxlength=3 size=5 class=bk>
						/ <INPUT TYPE="text" NAME="vci" value="<s:property value="vci"/>"
							maxlength=3 size=5 class=bk> &nbsp; <font color="#FF0000">*</font>
						</TD>
						<TD class=column align="right" id="id_vlan1" style="display: none">
						VlanID</TD>
						<TD id="id_vlan2" style="display: none"><INPUT TYPE="text"
							NAME="vlanid" class=bk value="<s:property value="vlanid"/>">
						&nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>��������</TD>
						<TD><SELECT name='netType' class=bk
							onChange="chgnetType(this.value);">
							<option value='1'>�Ž�</option>
							<option value='2'>·��</option>
							<option value='3'>��̬IP</option>
							<option value='4'>DHCP</option>
						</SELECT> &nbsp; <font color='red'>*</font> &nbsp;&nbsp;</TD>
						<TD class=column align="right" nowrap>��������</TD>

						<TD><select name="typeName">
							<s:iterator var="item" value="typeNameList">
								<option value="<s:property value='type_id'/>"><s:property
									value="type_name" /></option>
							</s:iterator>
						</select> &nbsp; <font color='red'>*</font> &nbsp;&nbsp;

						</TD>

					</TR>
					<TR id="id_static1" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>IP��ַ</TD>
						<TD><input type='text' name='ip' class=bk
							value="<s:property value="ip"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>����</TD>
						<TD><INPUT TYPE='text' NAME='mask' class=bk
							value="<s:property value="mask"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
					</TR>
					<TR id="id_static2" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align='right' nowrap>����</TD>
						<TD><input type='text' name='gateway' class=bk
							value="<s:property value="gateway"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>DNS</TD>
						<TD><INPUT TYPE='text' NAME='dns' class=bk
							value="<s:property value="dns"/>"> &nbsp; <font
							color='#FF0000'>*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">����</TD>
						<TD colspan="3"><select name="cityId" class="bk">
							<option value="-1">==��ѡ��==</option>
							<s:iterator value="cityList">
								<option value="<s:property value="city_id" />">== <s:property
									value="city_name" /> ==</option>
							</s:iterator>
						</select> &nbsp; <font color="#FF0000">*</font></TD>

					</TR>

					<TR class="green_title">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<Th><font size="2">������д</font></Th>
								<!-- <TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD> -->
							</TR>
						</TABLE>
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
						<TD class=column align="right">�󶨵绰</TD>
						<TD><INPUT TYPE="text" NAME="bindphone" maxlength=15 class=bk
							value="<s:property value="bindphone"/>"></TD>
						<TD class=column align="right">�����û�������</TD>
						<TD><INPUT TYPE="text" NAME="maxUserNum" maxlength=5 class=bk
							value="<s:property value="maxUserNum"/>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">֤������</TD>
						<TD><SELECT name="credType" onChange="" class=bk>
							<option value="1">���֤</option>
							<option value="2">����֤</option>
							<option value="3">����֤</option>
							<option value="4">����</option>
						</SELECT> &nbsp;</TD>
						<TD class=column align="right">֤������</TD>
						<TD><INPUT TYPE="text" NAME="credno" maxlength=30 class=bk
							size=20 value="<s:property value="credno"/>"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">�û�ʵ��</TD>
						<TD><INPUT TYPE="text" NAME="realname" maxlength=20 class=bk
							value="<s:property value="realname"/>"> &nbsp;</TD>
						<TD class=column align="right">����ʱ��</TD>
						<TD><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<!--
												<img name="shortDateimg"
													onclick="new WdatePicker(document.frm.dealdate,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
													src="../../images/search.gif" width="15" height="12"
													border="0" alt="ѡ��">
												 --> <img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��"> (YYYY-MM-DD HH:mm:ss)</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ϵ������</TD>
						<TD><INPUT TYPE="text" NAME="linkman" class=bk
							value="<s:property value="linkman"/>"> &nbsp;</TD>
						<TD class=column align="right">��ϵ�绰</TD>
						<TD><INPUT TYPE="text" NAME="linkphone" class=bk
							value="<s:property value="linkphone"/>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ϵ���ֻ�</TD>
						<TD><INPUT TYPE="text" NAME="mobile" class=bk
							value="<s:property value="mobile"/>"> &nbsp;</TD>
						<TD class=column align="right">��ϵ��email</TD>
						<TD><INPUT TYPE="text" NAME="email" class=bk
							value="<s:property value="email"/>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��ϵ�˵�ַ</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="linkaddress"
							class=bk value="<s:property value="linkaddress"/>" size=60>
						&nbsp;</TD>
					</TR>

					<TR bgcolor="#FFFFFF" style="display:none">
						<TD colspan="4"><INPUT TYPE="hidden" NAME="gw_type"
							class=bk value="<s:property value="gw_type"/>" size=60>
						&nbsp;</TD>
					</TR>

					<TR>
						<TD colspan="4" align="right" class="green_foot"><INPUT
							TYPE="submit" value=" �� �� " class="btn"> &nbsp;&nbsp; <INPUT
							TYPE="reset" value=" �� �� " class="btn"></TD>
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
<%@ include file="../foot.jsp"%>
