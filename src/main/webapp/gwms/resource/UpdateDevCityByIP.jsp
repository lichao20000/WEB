<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/prototype.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<!-- add by zhangchy 2011-09-08 ����CheckFormForm.js ԭ���Ǵ�JSP������IsIPAddr2(strValue,msg)�ű�-->
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>

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
<SCRIPT LANGUAGE="JavaScript">
<!--
	//�Ƿ��ж�


function checkSerialno(){
	var deviceSN = $.trim(document.frm.deviceSN.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	if(deviceSN=="�������6λ"){
		deviceSN="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
	if(deviceSN==""&&loopbackIp==""){
		alert("�����������豸���кź��豸IP�е�һ�");
		document.frm.deviceSN.focus();
		return false;
	}
	if(deviceSN!=""){
		if(deviceSN.length < 6){
            alert("�������������6λ�豸���к� !");
            document.frm.deviceSN.focus();
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
	
	var deviceSN = $.trim(document.frm.deviceSN.value);
	var loopbackIp = $.trim(document.frm.loopbackIp.value);
	var url = "<s:url value='/gwms/resource/updateDevCityByIP!getDeviceInfo.action'/>";
	if(deviceSN=="�������6λ"){
		deviceSN="";
	}
	if(loopbackIp=="����IP"){
		loopbackIp="";
	}
	$.post(url,{
		deviceSN:deviceSN,
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
	
	if (!confirm('��ȷ���Ƿ������������?')){
		return false;
	}
	
	document.frm.action="<s:url value='/gwms/resource/updateDevCityByIP!updateCity.action'/>";
	document.frm.submit();	
}




//add qixueqi
//���ѡ���豸ʱ��Ҫ�ύ������
function deviceOnclick(device_id,city_id,IpCity_id,cpe_allocatedstatus,flag){
	$("input[@name='deviceId']").val(device_id);
	$("input[@name='deviceCityId']").val(city_id);
	$("input[@name='IpCityId']").val(IpCity_id);
	$("input[@name='isBind']").val(cpe_allocatedstatus);
	$("input[@name='flag']").val(flag);
	$("div[@id='div_update']").css("display","");
	if("1"==flag){		
		$("div[@id='div_update']").html("<input type='button' class=jianbian name='save_btn' value=' �� �� �� �� ' onclick='CheckForm()'/>");		
	}else if("0"==flag){		
		$("div[@id='div_update']").html("<font color='red' size='3'>���豸����Ҫ��������������</font>");				
	}else if("3"==flag){
		$("div[@id='div_update']").html("<font color='red' size='3'>���豸IPû�ж�Ӧ�����أ�</font>");
	}else{
		$("div[@id='div_update']").html("<font color='red' size='3'>���豸δ�ϱ����ϱ�ʧ�ܣ�</font>");
	}
}

//add qixueqi
//���²�ѯ�豸ʱ�����ѡ����豸
function deviceClear(){
	$("input[@name='deviceId']").val("");
	$("input[@name='deviceCityId']").val("");
	$("input[@name='IpCityId']").val("");
	$("input[@name='isBind']").val("");
	$("input[@name='flag']").val("");
	$("div[@id='div_update']").css("display","none");
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
//-->
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">

	<!-- ������Ҫ�ύ�� -->
	<!-- �豸���� -->
	<input type="hidden" name="deviceId" value="" />
	<input type="hidden" name="deviceCityId" value="" />
	<input type="hidden" name="IpCityId" value="" />
	<input type="hidden" name="isBind" value="" />
	<input type="hidden" name="flag" value="" />

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
							ȫ���ն˲�ѯ
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />
							��ѯ����ע�ᵽITMSϵͳ���ն�
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

						<TD align="center">
							�豸���к�&nbsp;
							<input type="text" size="35" class="bk" name=deviceSN
								value="�������6λ" onfocus="inputFocus(this,'�������6λ')"
								onblur="inputBlur(this,'�������6λ')">
							<font color="red">*</font>
						</TD>
						<TD align="center">
							�豸IP&nbsp;
							<input type="text" size="35" class="bk" name=loopbackIp
								value="����IP" onfocus="inputFocus(this,'����IP')"
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
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th>
							��ѯ���
						</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD>
							<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
							</div>
						</TD>
					</tr>
				</TABLE>
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
	</TABLE>
</FORM>

<br>
<br>
<%@ include file="../foot.jsp"%>
