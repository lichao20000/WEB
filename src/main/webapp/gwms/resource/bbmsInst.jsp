<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<style type="text/css">

.bk {
	BACKGROUND-COLOR: #f1f1f1; BORDER-BOTTOM: #eeeeee 1px solid; BORDER-LEFT: #333333 1px solid; BORDER-RIGHT: #eeeeee 1px solid; BORDER-TOP: #333333 1px solid; COLOR: #888888; FONT-FAMILY: "����","Arial"
}
</style>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
	//�Ƿ��ж�


function refresh(){
	window.location.href=window.location.href;
}


function checkSerialno(){
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(device_serialnumber=="�������6λ"){
		device_serialnumber="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
	if(device_serialnumber==""&&loopbackIp==""){
		alert("�����������豸���кź��豸IP�е�һ�");
		document.frm.device_serialnumber.focus();
		return false;
	}
	if(device_serialnumber!=""){
		if(device_serialnumber.length < 6){
            alert("�������������6λ�豸���к� !");
            document.frm.device_serialnumber.focus();
            return false;        
	    }
	}
	if(loopbackIp!=""){
		if(!IsIPAddr2(loopbackIp,"�豸IP")){
			document.frm.loopbackIp.focus();
			return false;
		}
	}
	
	getUserinstInfo();
	
}
function getUserinstInfo(){
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(device_serialnumber=="�������6λ"){
		device_serialnumber="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
	var url = "<s:url value='/gwms/resource/bbmsInst!getDeviceInfo.action'/>";
	$.post(url,{
		device_serialnumber:device_serialnumber,
		loopbackIp:loopbackIp
	},function(ajax){	
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
	document.all("tr_deviceinfo").style.display="";
}


//���ѡ���豸ʱ��Ҫ�ύ������
function deviceOnclick(device_id,city_id,oui,device_serialnumber){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='oui']").val(oui);
	$("input[@name='deviceNo']").val(device_serialnumber);
}


//���²�ѯ�豸ʱ�����ѡ����豸
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='oui']").val("");
	$("input[@name='deviceNo']").val("");
}


//���ѡ���û�ʱ��Ҫ�ύ������
function userOnclick(user_id,userName,city_id,customer_id,deviceId,oui,deviceSN){
	$("input[@name='userId']").val(user_id);
	$("input[@name='username']").val(userName);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='customerId']").val(customer_id);
	$("input[@name='oldDeviceId']").val(deviceId);
	
	var instArea = $("input[@name='instArea']").val();
	
	$("div[@id='div_bind']").css("display","");
	if(""==deviceId){
		//if(""==deviceSN){
			$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' �� ʼ ��  �� ' onclick='CheckForm()'/>");
		//}else{
		//	$("div[@id='div_bind']").html("<font color='red'>���û��˺Ŵ����쳣������ϵ����Ա��</font>");
		//}
	}else{
		if("xj_dx"==instArea){
			$("div[@id='div_bind']").html("<table border=0 cellspacing=0 cellpadding=0 width='45%'><tr><td  colspan='2' align='center'><font color='red'>���û��˺��Ѱ󶨣���ȷ�������˺��Ƿ���ȷ��<br>����Ҫ����������ʼ�󶨣�ԭ���豸��Ҫ�����</font></td></tr><tr><td align='center' width='50%'><input type='button' class=jianbian name='save_btn' value=' �� ʼ �� �� ' onclick='imposeInst()'/></td><td align='center'><input type='button' class=jianbian name='save_btn' value=' ȡ �� ' onclick='userClear()'/></td></tr></table>");
		}else{
			$("div[@id='div_bind']").html("<font color='red'>���û��˺��Ѱ󶨣���ȷ�������˺��Ƿ���ȷ������Ҫ���°����Ƚ��</font>");
		}	
	}
}

function imposeInst(){

	var deviceId = $("input[@name='deviceId']").val();
	
	if ("" == deviceId ){
		alert('����ѡ��һ���豸��');
		return false;
	}
	
	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	
	document.frm.action="bbmsInst!imposeInst.action";
	document.frm.submit();	
}


//���²�ѯ�û�ʱʱ�����ѡ����û�
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='username']").val("");
	$("input[@name='userCityId']").val("");
	$("input[@name='customerId']").val("");
	$("input[@name='oldDeviceId']").val("");
	$("div[@id='div_bind']").css("display","none");
}

function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();
	
	if ("" == deviceId ){
		alert('����ѡ��һ���豸��');
		return false;
	}
	
	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}
	
	document.frm.action="bbmsInst.action";
	document.frm.submit();	
}

function checkUsernameEmpty() {
	if ($.trim(document.frm.userName.value) == ""||$.trim(document.frm.userName.value)=="�����˺�"){
		alert('����д�û�����ʺ�/ר�ߺ�');
		document.frm.userName.focus();
		return false;
	}
	return true;
}
function nextstep(){
	if (!checkUsernameEmpty()) {
		return;
	}
	var userName = $.trim(document.frm.userName.value);
	var url = "<s:url value='/gwms/resource/bbmsInst!getInstUserCustomerInfo.action'/>";
	$.post(url,{
		userName:userName
	},function(ajax){
		userClear();	
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});	
	document.all("tr_userinfo").style.display="";
}

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//�鿴�û���ص���Ϣ
function GoContent(user_id){
	var strpage="../../Resource/EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

//�����õ�ȥ����
function inputFocus(s,message){
	//s.css({"BACKGROUND-COLOR": "#f1f1f1", "BORDER-BOTTOM": "#eeeeee 1px solid", "BORDER-LEFT": "#333333 1px solid", "BORDER-RIGHT": "#eeeeee 1px solid", "BORDER-TOP": "#333333 1px solid", "COLOR": "#000000", "FONT-FAMILY": "'����','Arial'"});
	var value = s.value;
	s.style.color='#0000cc';
	if(value==''||value==message){
		
		s.value='';
	}
}

//�����ʧȥ����
function inputBlur(s,message){
	var value = s.value;
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
//-->
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">
	<!-- ������Ҫ�ύ�� -->
	<!-- �豸���� -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="oui" value="" />
	<input type="hidden" name="deviceNo" value="" />
	<!-- �û����� -->
	<input type="hidden" name="userId" value="" />
	<input type="hidden" name="username" value="" />
	<input type="hidden" name="userCityId" value="" />
	<input type="hidden" name="customerId" value="" />
	<input type="hidden" name="oldDeviceId" value="">

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15>

				&nbsp;
			</TD>
		</TR>

		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�ֹ���װ
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							���Ȳ�ѯ�豸��Ȼ���ѯ�û���ѡ����ȷ���豸���û����а�װ��
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF">
						<TH colspan="2">
							�豸�����Ϣ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">
							�豸���к�&nbsp;
							<input type="text" size="35" class="bk" name=device_serialnumber
								value="�������6λ"
								onfocus="inputFocus(this,'�������6λ')"
								onblur="inputBlur(this,'�������6λ')">
							<font color="red">*</font>
						</TD>
						<TD align="center">
							�豸IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="����IP"
								onfocus="inputFocus(this,'����IP')"
								onblur="inputBlur(this,'����IP')">
							<font color="red">*</font>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2">
							<input type="button" class=jianbian name="sort" value=" ��  ѯ "
								onclick="checkSerialno()" />
						</td>
					</tr>

				</TABLE>
			</TD>
		</TR>

		<TR style="display: none" id="tr_deviceinfo">
			<TD>
				<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>

		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center" bgcolor="#999999">
					<tr>
						<th colspan="5">
							�û������Ϣ
						</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD align="center">
							�û��ʻ�&nbsp;
							<input type="text" size="35" name="userName" class="bk"
								value="�����˺�"
								onfocus="inputFocus(this,'�����˺�')"
								onblur="inputBlur(this,'�����˺�')" />
							<font color="red">*</font>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column">
							<input type="button" class=jianbian name="next"
								onclick="nextstep()" value=" ��һ�� " />
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR style="display: none" id="tr_userinfo">
			<TD>
				<div id="div_user" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR>
			<TD align="center">
				<div id="div_bind" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
	</TABLE>

</FORM>



<br>
<br>
<%@ include file="../foot.jsp"%>
