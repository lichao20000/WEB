<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<SCRIPT LANGUAGE="JavaScript">
<!--
	//�Ƿ��ж�

	function refresh() {
		window.location.href = window.location.href;
	}

	function checkSerialno() {
		var device_serialnumber = $
				.trim(document.frm.device_serialnumber.value);
		var loopbackIp = $.trim(document.frm.loopbackIp.value);
		if (device_serialnumber == "�������6λ") {
			device_serialnumber = "";
		}
		if (loopbackIp == "����IP") {
			loopbackIp = "";
		}
		if (device_serialnumber == "" && loopbackIp == "") {
			alert("�����������豸���кź��豸IP�е�һ�");
			document.frm.device_serialnumber.focus();
			return false;
		}
		if (device_serialnumber != "") {
			if (device_serialnumber.length < 6) {
				alert("�������������6λ�豸���к� !");
				document.frm.device_serialnumber.focus();
				return false;
			}
		}
		if (loopbackIp != "") {
			if (!IsIPAddr2(loopbackIp, "�豸IP")) {
				document.frm.loopbackIp.focus();
				return false;
			}
		}
		getDeviceInfo();

	}
	function getDeviceInfo() {

		var deviceSN = $.trim(document.frm.device_serialnumber.value);
		var loopbackIp = $.trim(document.frm.loopbackIp.value);
		$("div[@id='div_device']").html("���ڲ�ѯ�����Ե�....");
		var url = "<s:url value='/gwms/resource/updateDevCityByIP!getAhDeviceInfo.action'/>";
		if (deviceSN == "�������6λ") {
			deviceSN = "";
		}
		if (loopbackIp == "����IP") {
			loopbackIp = "";
		}
		$.post(url, {
			deviceSN : deviceSN,
			loopbackIp : loopbackIp 
		}, function(ajax) {
			//deviceClear();
			$("div[@id='div_device']").html("");
			$("div[@id='div_device']").append(ajax); 
		});
		document.all("tr_deviceinfo").style.display = "";
	}

	function updateAhCity() {
		var updeviceId = $("input[@name='updeviceId']").val();
		alert(updeviceId);
		$("div[@id='div_user']").html("�����޸ģ����Ե�....");
		var url = "<s:url value='/gwms/resource/updateDevCityByIP!getAhDeviceInfoByDev.action'/>";
		$("div[@id='div_update']").html("");
		$.post(url, {
			updeviceId : updeviceId 
		}, function(ajax) {
			//deviceClear();
			$("div[@id='div_user']").html("");
			$("div[@id='div_user']").append(ajax);
		});
		document.all("tr_userinfo").style.display = "";
		
	}

	//add qixueqi
	//���ѡ���豸ʱ��Ҫ�ύ������
	function deviceOnclick(device_id, city_id, device_serialnumber,
			cpe_allocatedstatus) {
		$("input[@name='updeviceId']").val(device_id);
		$("input[@name='deviceCityId']").val(city_id);
		$("input[@name='deviceNo']").val(device_serialnumber);
		$("input[@name='isBind']").val(cpe_allocatedstatus);
		$("div[@id='div_update']").html("");
		$("div[@id='div_update']").css("display", "");
		document.all("tr_userinfo").style.display = "none";

		if (cpe_allocatedstatus == "1") {
			$("div[@id='div_update']")
					.html(
							"<font color='red' size='3'>���豸����Ҫ������������������豸��ͬ������������Ա�󶨣�����ϵ����������Ա���н�����</font>");
		} else {
			var updeviceId = $("input[@name='updeviceId']").val();
			var url = "<s:url value='/gwms/resource/updateDevCityByIP!getAhDeviceInfoByDev.action'/>";
			$("div[@id='div_update']").html("");
			$.post(url, {
				updeviceId : updeviceId 
			}, function(ajax) {
				//deviceClear();
				$("div[@id='div_user']").html("");
				$("div[@id='div_user']").append(ajax);
			});
			document.all("tr_userinfo").style.display = "";
		}

	}

	//�����õ�ȥ����
	function inputFocus(s, message) {
		var value = $.trim(s.value);
		s.style.color = '#0000cc';
		if (value == '' || value == message) {

			s.value = '';
		}
	}

	//�����ʧȥ����
	function inputBlur(s, message) {
		var value = $.trim(s.value);
		if (value == '' || value == message) {
			s.style.color = '#888888';
			s.value = message;
		}
	}
//-->
</SCRIPT>


<FORM NAME="frm" METHOD="post" ACTION="">

	<!-- ������Ҫ�ύ�� -->
	<!-- �豸���� -->
	<input type="hidden" name="updeviceId" value="" /> 
	<input type="hidden" name="deviceCityId" value="" /> 
	<input type="hidden" name="deviceNo" value="" /> 
	<!-- �������� -->
	<input type="hidden" name="isBind" value="" /> 

	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR>
			<TD HEIGHT=15></TD>
		</TR>
		<TR>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">�����޸�</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> ���Ȳ�ѯ�豸��Ȼ����豸���������޸ģ����ܶ��Ѱ��豸���������޸ģ�</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#FFFFFF" align="left">
						<TH colspan="2">�豸���ر��</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="center" width="50%">�豸���к�&nbsp; <input type="text"
							size="35" class="bk" name=device_serialnumber value="�������6λ"
							onfocus="inputFocus(this,'�������6λ')"
							onblur="inputBlur(this,'�������6λ')">

						</TD>
						<TD align="center" width="50%">�豸IP&nbsp; <input type="text"
							size="35" class="bk" name=loopbackIp value="����IP"
							onfocus="inputFocus(this,'����IP')" onblur="inputBlur(this,'����IP')">

						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td align="right" class="column" colspan="2"><input
							type="button" class=jianbian name="sort" value=" ��  ѯ "
							onclick="checkSerialno()" /></td>
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
			<TD HEIGHT=30></TD>
		</TR>
		<TR>
			<TD align="center">
				<div id="div_update" style="display: none"
					style="width: 100%; z-index: 1; top: 100px"></div>
			</TD>
		</TR>

		<TR style="display: none" id="tr_userinfo">
			<TD>
				<div id="div_user" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=30></TD>
		</TR>

	</TABLE>
</FORM>

<br>
<br>
<%@ include file="../foot.jsp"%>
