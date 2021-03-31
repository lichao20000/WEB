<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<style type="text/css">
.bk {
	BACKGROUND-COLOR: #f1f1f1;
	BORDER-BOTTOM: #eeeeee 1px solid;
	BORDER-LEFT: #333333 1px solid;
	BORDER-RIGHT: #eeeeee 1px solid;
	BORDER-TOP: #333333 1px solid;
	COLOR: #888888;
	FONT-FAMILY: "����", "Arial"
}
</style>
<%
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<SCRIPT LANGUAGE="JavaScript">
var area = '<%=area%>';

function refresh(){
	window.location.href=window.location.href;
}


function checkSerialno() {

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
		getDeviceInfo();

}
function getDeviceInfo(){

	var deviceSN = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	$("div[@id='div_device']").html("���ڲ�ѯ�����Ե�....");
	$("input[@name='sort']").attr("disabled", true);
	var gw_type	= $("input[@name='gw_type']").val();
	var url = "<s:url value='/gwms/resource/updateDevCityByIP!getDeviceInfo.action'/>";
	if(deviceSN=="�������6λ"){
		deviceSN="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
	$.post(url,{
		deviceSN:deviceSN,
		loopbackIp:loopbackIp,
		gw_type:gw_type
	},function(ajax){
		deviceClear();
	    $("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
		$("input[@name='sort']").attr("disabled", false);
	});
	document.all("tr_deviceinfo").style.display="";
}

function getUserinstInfo(){

	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	var url = "<s:url value='/gwms/resource/itmsInst!getDeviceInfo.action'/>";
	if(device_serialnumber=="�������6λ"){
		device_serialnumber="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
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


function CheckForm(){

	var deviceId = $("input[@name='deviceId']").val();

	if ("" == deviceId ){
		alert('����ѡ��һ���豸��');
		return false;
	}

	var instArea = $("input[@name='instArea']").val();
	if("js_dx"==instArea){
		$("input[@name='username']").val($.trim($("input[@name='userName']").val()));
	}
	if("ehome_self"==$.trim($("input[@name='typename']").val())){
		alert('���û�ʹ���Ա��նˣ������!');
		return false;
	}

	<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT() && !LipossGlobals.inArea("nx_lt")){%>
	if($.trim($("input[@name='deviceType']").val())!=$.trim($("input[@name='typename']").val())){
		alert('�û���ͨ���ն���������󶨵��ն����Ͳ�һ�£������!');
		return false;
	}
	<%} %>

	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}

	document.frm.action="itmsInst.action";
	document.frm.submit();
}

function updateCity(){

	var deviceId = $("input[@name='deviceId']").val();

	if ("" == deviceId ){
		alert('����ѡ��һ���豸��');
		return false;
	}

	if (!confirm('��ȷ���Ƿ������������?')){
		return false;
	}

	document.frm.action="<s:url value='/gwms/resource/updateDevCityByIP!updateCity.action'/>";
	document.frm.submit();
}

function checkUsernameEmpty() {
	if (($.trim(document.frm.userName.value) == ""||$.trim(document.frm.userName.value)=="�����˺�")){
		alert('�������˺ţ�');
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
	if(userName=="�����˺�"){
		userName="";
	}

	var gw_type	= $("input[@name='gw_type']").val();
	var nameType = $.trim($("select[@name='nameType']").val());
	$("div[@id='div_user']").html("���ڲ�ѯ�����Ե�....");
	$("input[@name='next']").attr("disabled", true);
	var url = "<s:url value='/gwms/resource/itmsInst!getInstUserInfo.action'/>";
	$.post(url,{
		userName:userName,
		nameType:nameType,
		gw_type:gw_type
	},function(ajax){
		userClear();
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
		$("input[@name='next']").attr("disabled", false);
	});
	document.all("tr_userinfo").style.display="";
}
//add qixueqi
//���ѡ���豸ʱ��Ҫ�ύ������
function deviceOnclick(device_id,city_id,oui,device_serialnumber,IpCity_id,cpe_allocatedstatus,flag,manage,device_type){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='oui']").val(oui);
	$("input[@name='deviceNo']").val(device_serialnumber);
	$("input[@name='deviceType']").val(device_type);
	$("input[@name='IpCityId']").val(IpCity_id);
	$("input[@name='isBind']").val(cpe_allocatedstatus);
	$("input[@name='flag']").val(flag);
	$("div[@id='div_update']").html("");
	$("div[@id='div_update']").css("display","");
	document.all("queryuser").style.display="none";
	document.all("tr_userinfo").style.display="none";
	if(manage=="1"){

		if("1"==flag){
			$("div[@id='div_update']").html("<input type='button' class=jianbian name='save_btn' value=' �� �� �� �� ' onclick='updateCity()'/>");
		}else{
			if(cpe_allocatedstatus=="1")
			{
				if(area == 'sx_lt') {
					$("div[@id='div_update']").html("<font color='red' size='3'>���豸�Ѱ󶨣���ȷ���豸��Ϣ�Ƿ���ȷ���������°����Ƚ��</font>");
				}else{
					$("div[@id='div_update']").html("<font color='red' size='3'>���豸����Ҫ������������������豸��ͬ������������Ա�󶨣�����ϵ����������Ա���н�����</font>");
				}
			}else{
				document.all("queryuser").style.display="";
			}
		}
		//else if("3"==flag){
		//	$("div[@id='div_update']").html("<font color='red' size='3'>���豸IPû�ж�Ӧ�����أ�</font>");
		//}else{
		//	$("div[@id='div_update']").html("<font color='red' size='3'>���豸δ�ϱ����ϱ�ʧ�ܣ�</font>");
		//}
	}else{
		$("div[@id='div_update']").html("<font color='red' size='3'>���豸���޷����й����������</font>");
	}
}

//add qixueqi
//���²�ѯ�豸ʱ�����ѡ����豸
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='oui']").val("");
	$("input[@name='deviceNo']").val("");
	$("input[@name='deviceType']").val("");
	$("input[@name='IpCityId']").val("");
	$("input[@name='isBind']").val("");
	$("input[@name='flag']").val("");
	$("div[@id='div_update']").css("display","none");
	document.all("tr_userinfo").style.display="none";
	document.all("queryuser").style.display="none";
}

//add qixueqi
//���ѡ���û�ʱ��Ҫ�ύ������
function userOnclick(user_id,userName,city_id,deviceId,oui,deviceSN,typeName){
	$("input[@name='userId']").val(user_id);
	$("input[@name='username']").val(userName);
	$("input[@name='typename']").val(typeName);
	$("input[@name='userCityId']").val(city_id);
	$("input[@name='oldDeviceId']").val(deviceId);

	var instArea = $("input[@name='instArea']").val();

	$("div[@id='div_bind']").css("display","");
	if(""==deviceId){
		//if(""==deviceSN){
			$("div[@id='div_bind']").html("<input type='button' class=jianbian name='save_btn' value=' �� ʼ �� �� ' onclick='CheckForm()'/>");
		//}else{
			//$("div[@id='div_bind']").html("<font color='red'>���û��˺Ŵ����쳣������ϵ����Ա��</font>");
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
	if("ehome_self"==$.trim($("input[@name='typename']").val())){
		alert('���û�ʹ���Ա��նˣ������!');
		return false;
	}

	<%  if(!LipossGlobals.isXJDX() && !LipossGlobals.isSXLT()){%>
	if($.trim($("input[@name='deviceType']").val())!=$.trim($("input[@name='typename']").val())){
		alert('�û���ͨ���ն���������󶨵��ն����Ͳ�һ�£������!');
		return false;
	}
	<%} %>

	var message = "��ȷ�ϣ��û��ʺţ�"+$("input[@name='username']").val()+"���豸���кţ�"+$("input[@name='deviceNo']").val();
	if (!confirm(message+'���Ƿ��������?')){
		return false;
	}

	document.frm.action="itmsInst!imposeInst.action";
	document.frm.submit();
}

//add qixueqi
//���²�ѯ�û�ʱʱ�����ѡ����û�
function userClear(){
	$("input[@name='userId']").val("");
	$("input[@name='username']").val("");
	$("input[@name='typename']").val("");
	$("input[@name='userCityId']").val("");
	$("input[@name='oldDeviceId']").val("");
	$("div[@id='div_bind']").css("display","none");
}

function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//�鿴�û���ص���Ϣ
function GoContent(user_id){
	var strpage="../../Resource/HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

//�����õ�ȥ����
function inputFocus(s,message){
	var value = $.trim(s.value);
	s.style.color='#0000cc';
	if(value==''||value==message){

		s.value='';
	}
}

//�����ʧȥ����
function inputBlur(s,message){
	var value = $.trim(s.value);
	if(value==''||value==message){
		s.style.color='#888888';
		s.value=message;
	}
}
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">

	<!-- ������Ҫ�ύ�� -->
	<!-- �豸���� -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="oui" value="" />
	<input type="hidden" name="deviceNo" value="" />
	<input type="hidden" name="deviceType" value="" />
	<!-- �������� -->
	<input type="hidden" name="IpCityId" value="" />
	<input type="hidden" name="isBind" value="" />
	<input type="hidden" name="flag" value="" />
	<!-- �û����� -->
	<input type="hidden" name="userId" value="" />
	<input type="hidden" name="username" value="" />
	<input type="hidden" name="typename" value="" />
	<input type="hidden" name="userCityId" value="" />
	<input type="hidden" name="oldDeviceId" value="">

	<input type="hidden" name="gw_type" value="<s:property value='gw_type' />" />

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15>
			</TD>
		</TR>
		<TR>
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
							�豸��ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="center" width="50%">
							�豸���к�&nbsp;
							<input type="text" size="35" class="bk" name=device_serialnumber
								value="�������6λ" onfocus="inputFocus(this,'�������6λ')"
								onblur="inputBlur(this,'�������6λ')">

						</TD>
						<TD align="center" width="50%">
							�豸IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="����IP" onfocus="inputFocus(this,'����IP')"
								onblur="inputBlur(this,'����IP')">

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
			<TD align="center">
				<div id="div_update" style="display: none"
					style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR id="queryuser" style="display: none">
			<TD bgcolor=#999999>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					align="center">
					<TR bgcolor="#FFFFFF">
						<TH colspan="2">
							�û���ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center" width="50%">
							ѡ������&nbsp;
							<s:property value="nameListHtml" escapeHtml="false"/>
						</TD>
						<TD align="center">
							��ѯֵ&nbsp;
							<input type="text" size="35" name="userName" class="bk"
								value="�����˺�" onfocus="inputFocus(this,'�����˺�')"
								onblur="inputBlur(this,'�����˺�')" />

						</TD>

					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2">
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
<%if(!area.equals("sx_lt")){%>
	<%@ include file="../foot.jsp"%>
<%}%>
