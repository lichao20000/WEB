<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>iTV����ҳ��</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//----------------------------����-����-�ͺ�-�汾-�豸�б�-------------------------------
var chekType = 3;
function ShowDialog(valu){
	//3�������ͺţ�1:�û����͵绰��2���豸���кź�IP,4�����û�����
	var queryType = valu;
	chekType = queryType;
	//alert(chekType);
	if(queryType==3){
		$("tr[@id='userQuery']").hide();
		$("tr[@id='deviceQuery']").hide();
		$("tr[@id='importUserQuery']").hide();
		$("tr[@id='importUserQuery2']").hide();
		$("tr[@id='space']").hide();

		$("tr[@id='query']").show();
		$("tr[@id='cityQuery1']").show();
		$("tr[@id='cityQuery2']").show();
	}else if(queryType==1){
		$("tr[@id='cityQuery1']").hide();
		$("tr[@id='cityQuery2']").hide();
		$("tr[@id='deviceQuery']").hide();
		$("tr[@id='importUserQuery']").hide();
		$("tr[@id='importUserQuery2']").hide();
		$("tr[@id='space']").hide();

		$("tr[@id='userQuery']").show();
		$("tr[@id='query']").show();
		$("div[@id='div_device']").html("���ȸ���������ѯ�豸��");

		//ѡ���л���ʱ��Ѱ�ť�ÿ��� add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = false;


	}else if(queryType==2){
		$("tr[@id='cityQuery1']").hide();
		$("tr[@id='cityQuery2']").hide();
		$("tr[@id='userQuery']").hide();
		$("tr[@id='importUserQuery']").hide();
		$("tr[@id='importUserQuery2']").hide();
		$("tr[@id='space']").hide();

		$("tr[@id='deviceQuery']").show();
		$("tr[@id='query']").show();

		$("div[@id='div_device']").html("���ȸ���������ѯ�豸��");

		//ѡ���л���ʱ��Ѱ�ť�ÿ��� add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = false;
	}else if(queryType==4){
		$("tr[@id='cityQuery1']").hide();
		$("tr[@id='cityQuery2']").hide();
		$("tr[@id='userQuery']").hide();
		$("tr[@id='deviceQuery']").hide();
		$("tr[@id='query']").hide();

		$("tr[@id='importUserQuery']").show();
		$("tr[@id='space']").show();
		$("tr[@id='importUserQuery2']").show();
		$("div[@id='div_device']").html("���ȸ���������ѯ�豸��");

		//ѡ���л���ʱ��Ѱ�ť�ÿ��� add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = false;
	}
}

function showDesc() {
	$("tr[@id='importUserQuery2']").toggle();
}

function selectFailed(devInfo) {

	document.frm.alldevice.checked=false;
	//����selectAll����
	selectAll(devInfo);
	document.frm.failedDevice.checked=true;

	devObj = document.all(devInfo);
	if(!devObj) return;

	obj = event.srcElement;

	if(obj.checked){
		if(typeof(devObj) == "object" ) {
			if(typeof(devObj.length) != "undefined") {
				for(var i=0; i<devObj.length; i++){
					val = devObj[i].value;
					if(val.split("|")[9] != "1") {
						devObj[i].checked = true;
					}
				}
				document.frm.allSelected.value="1";
			} else {
				val = devObj.value;
				//alert("1:" + val);
				if(val.split("|")[9] != "1") {
					devObj.checked = true;
				}
			}
		}

	}else{
		if(typeof(devObj) == "object" ) {
			if(typeof(devObj.length) != "undefined") {
				for(var i=0; i<devObj.length; i++){
					val = devObj[i].value;
					if(val.split("|")[9] != "1") {
						devObj[i].checked = false;
					}

				}
				document.frm.allSelected.value="0";
			} else {
				val = devObj.value;
				//alert("2:" + val);
				if(val.split("|")[9] == "1") {
					devObj.checked = false;
				}
			}
		}
	}


}

function selectAll(elmID){
	document.frm.failedDevice.checked=false;

	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
				document.frm.allSelected.value="1";
			} else {
				t_obj.checked = true;
			}
		}

	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
				document.frm.allSelected.value="0";
			} else {
				t_obj.checked = false;
			}
		}
	}
}

function showChild(selectname){
	if(selectname == 'vendor_name'){
		queryModel(selectname);
	}else{
		queryVersion(selectname);
	}
}

function init(){
	queryCity();
	queryVendor();
	//�޸����غ�û���Զ�������ѯ�˵���ʾ��Bug add by �Ŵ�(zhangcong) 2011-06-07
	var radios = document.getElementsByName("checkType");
	//ѡ�е����ļ�
	if(radios[0].checked)
	{
		ShowDialog(4);
	}
	//ѡ���û��ʺ�
	if(radios[1].checked)
	{
		ShowDialog(1);
	}
	//ѡ���豸���к�
	if(radios[2].checked)
	{
		ShowDialog(2);
	}
	//for(var i = 0;i < radios.length ; i ++)
	//{
		////if(radios[i].checked)
		//{
			//��ʾ��Ӧ�Ĳ�ѯ��ʽ
			//ShowDialog(i + 1);
			//break;
		//}
		////alert(i + "  :  " + radios[i].checked);
	//}
}

function queryCity(){
	var url = "devVenderModelAction!getCityList.action";
	$.post(url,{
    },function(mesg){
    	//alert(mesg);
    	document.getElementById("cityList").innerHTML = mesg;
    });
}

function queryVendor(){
	var url = "devVenderModelAction!getVendorList.action";
	$.post(url,{
    },function(mesg){
    	//alert(mesg);
    	document.getElementById("vendorList").innerHTML = mesg;
    });
}
function queryModel(selectname){
	var vend = document.all(selectname).value;
	var url = "devVenderModelAction!getDeviceModelList.action";
	$.post(url,{
		vendorname:vend
    },function(mesg){
    	document.getElementById("modelList").innerHTML = mesg;
    });
}
function queryVersion(selectname){
	var vend = document.all("vendor_name").value;
	var model = document.all(selectname).value;
	var url = "devVenderModelAction!getVersionList.action";
	$.post(url,{
		vendorname:vend,
		deviceModel:model
    },function(mesg){
    	document.getElementById("versionList").innerHTML = mesg;
    });
}
function queryDevice(){
	//var gw_type = $("#gw_type").val();
	var gw_type = $("input[@name='gw_type']").val();
	//���ò�ѯ״̬��Ϣ add by zhangcong@ 2011-06-09
	$("div[@id='div_device']").html("���ڲ�ѯ�豸�����Ժ�...");

	var uname = document.all("username");
	var utelephone = document.all("telephone");

	var devSerial = document.all("deviceSerialnumber");
	var ipaddress = document.all("ipAddress");

	var city = document.all("city_id");
	var vendname = document.all("vendor_name");
	var device_model = document.all("device_model");
	var device_model_value = "-1";
	var device_type = document.all("softwareversion");
	var device_type_value = "-1";

	var url = "devVenderModelAction!getDeviceInfoList.action";
	//alert(chekType);
	if(chekType == 3){
		var ucity_id = city.value;
		//���δѡ����豸�汾
		if(device_type){
			device_type_value = device_type.value;
		}
		//���δѡ����豸�ͺ�
		if(device_model){
			device_model_value = device_model.value;
		}
		$.post(url,{
			checkType:3,
			city_id:ucity_id,
			vendorname:vendname.value,
			deviceModel:device_model_value,
			softwareversion:device_type_value,
			gwType:gw_type
   		},function(mesg){
   			document.getElementById("div_device").innerHTML = mesg;
   		});
		//}
	}else if (chekType == 2){
		var devSerialnumber = devSerial.value;
		var ip = ipaddress.value;
		if((devSerialnumber.trim() == "" || devSerialnumber.length < 5) && ip.trim() == ""){
			alert("5λ���кŻ�ip��ַ����");
			devSerial.focus();
			return false;
		}
		$.post(url,{
			checkType:2,
			deviceSerialnumber:devSerialnumber,
			ipAddress:ip,
			//TEMP
			gwType:gw_type
    	},function(mesg){
    		document.getElementById("div_device").innerHTML = mesg;
    	});
		//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = true;
	}else {
		var usname = uname.value;
		var usphone = utelephone.value;
		if(usname.trim() == "" && usphone.trim() == ""){
			alert("�û�����绰��ַ������һ��");
			uname.focus();
			return false;
		}
		$.post(url,{
			checkType:1,
			username:usname,
			telephone:usphone,
			gwType:gw_type
    	},function(mesg){
    		document.getElementById("div_device").innerHTML = mesg;
    	});
		//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = true;
	}
}

//----------------------------����-����-�ͺ�-�汾-�豸�б�-------------------------------
function chg_auth(enc_value) {
	if (enc_value == "Basic") {
		tr_encrypt_wep_1.style.display="";
		tr_encrypt_wpa_1.style.display="none";
	} else if (enc_value == "WPA") {
		tr_encrypt_wep_1.style.display="none";
		tr_encrypt_wpa_1.style.display="";
	} else {
		tr_encrypt_wep_1.style.display="none";
		tr_encrypt_wpa_1.style.display="none";
	}
}

function chg_conf(enc_value) {
	if (enc_value == "1") {
		$("tr[@id='ctrlConf_1']").show();
		$("tr[@id='ctrlConf_2']").hide();
		//$("tr[@id='wlan_1']").show();
		$("tr[@id='wlan_2']").show();
		//$("tr[@id='wlan_3']").show();
		$("td[@id='titleTH']").html("<font color='blue'>�ڶ�����iTV����+����</font>");
		$("span[@id='desc']").html("&nbsp;&nbsp;<font color='#7f9db9'>˵������ѡ�������豸��ԭ��������Σ��������ý��Ϊ�����ߡ����ߡ�QoS��</font>");

	} else {
		$("tr[@id='ctrlConf_1']").hide();
		$("tr[@id='ctrlConf_2']").show();
		//$("tr[@id='wlan_1']").hide();
		$("tr[@id='wlan_2']").hide();
		//$("tr[@id='wlan_3']").hide();
		$("td[@id='titleTH']").html("<font color='blue'>�ڶ�����iTV����</font>");
		$("span[@id='desc']").html("&nbsp;&nbsp;<font color='#7f9db9'>˵������ѡ�������豸��ԭ��������Σ��������ý��Ϊ�����ߡ�QoS��</font>");
	}
}

function checkForm(){
	var devObj = document.all("device_id");
	var needSoft = document.frm.needUpSoftware.value;
	var devSelected = false;

	if(!devObj){
		alert("��û��ѡ���豸");
		return false;
	}else{
		if(typeof(devObj.length) != "undefined") {
			for(var i=0; i<devObj.length; i++){
				if (devObj[i].checked == true) {
					devSelected = true;
					break;
				}
			}
		} else {
			if (devObj.checked == true) {
				devSelected = true;
			}
		}

		if(devSelected == false){
			alert("��û��ѡ���豸");
			return false;
		}
	}

	/**
	if(needSoft > 0){

	}
	**/
	document.frm.submitForm.disabled = true;
	document.frm.submit();
}

function softShow(valu){
	if(valu == "1"){
		document.getElementById("softUp").style = "display:";
	}else{
		document.getElementById("softUp").style = "display:none";
	}
}
</SCRIPT>
</head>
<body onload="init()">
<form name="frm" action="itvConfig!doStatery.action" method="post" onsubmit="return checkForm();">
<input type="hidden"  name="gw_type" value='<s:property value="gw_type" escapeHtml="false"/>' />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="text">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite" nowrap>
						iTV������������</td>
						<td nowrap><img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;��ѯ��ʽ��
							<input type="radio" value="4" onclick="ShowDialog(this.value)" name="checkType" checked>���ļ�����
							<!-- <input type="radio" value="3" onclick="ShowDialog(this.value)" name="checkType">�����غ��ͺ�&nbsp;&nbsp;-->
							<input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">���û�&nbsp;&nbsp;
							<input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">���豸

						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
						<TR>
							<TD bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR>
										<TH colspan="4" align="center">
											iTV������������
										</TH>
									</TR>
									<tr id="cityQuery1" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">���أ�</td>
										<td align="left" width="30%"><div id="cityList"></div></td>
										<td align="right">�豸���̣�</td>
										<td align="left"><div id="vendorList"></div></td>
									</tr>
									<tr id="cityQuery2" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">�豸�ͺţ�</td>
										<td align="left" width="30%"><div id="modelList"></div></td>
										<td align="right">�豸�汾��</td>
										<td align="left"><div id="versionList"></div></td>
									</tr>
									<tr id="userQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">�û�����</td>
										<td align="left" width="30%"><input type="text" name="username"></td>
										<td align="right">�绰��</td>
										<td align="left"><input type="text" name="telephone"></td>
									</tr>
									<tr id="deviceQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right">�豸���к�(���к�)��</td>
										<td align="left"><input type="text" name="deviceSerialnumber">&nbsp;&nbsp;�����λ</td>
										<td align="right">IP��ַ��</td>
										<td align="left"><input type="text" name="ipAddress"></td>
									</tr>
									<tr id="importUserQuery" bgcolor="#FFFFFF"  style="display:">
										<td align="right">�ύ�ļ���</td>
										<td colspan="3">
											<div id="importUsername">
												<iframe name="loadForm" FRAMEBORDER=0 SCROLLING=NO src="ItvImportUsername.jsp" height="30" width="100%">
												</iframe>
											</div>
										</td>
									</tr>

									<tr id="importUserQuery2" bgcolor="#FFFFFF">
										<td align="right">ע�����
										<td colspan="3">
										<font color="#7f9db9">
										1����Ҫ������ļ���ʽΪExcel��
										 <br>
										2���ļ��ĵ�һ��Ϊ�����У����û��˺š�
										 <br>
										3���ļ�ֻ��һ�С�
										 <br>
										4���ļ�������������100��,�糬��100�У�ֻ����ǰ100�����ݡ�
										</font>
										</td>
									</tr>
									<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
									<tr id="space" style="display:none" bgcolor="#FFFFFF">
										<td colspan="4" align="right" CLASS="green_foot">
											&nbsp;
										</td>
									</tr>
									<tr id="query" style="display:none" bgcolor="#FFFFFF">
										<td colspan="4" align="right" CLASS="green_foot">
											<input type="button" class=jianbian name=queryBtn value=" �� ѯ " onclick="queryDevice();">
											&nbsp;
										</td>
									</tr>
									<TR bgcolor="#FFFFFF" id="deviceListTrId" style="display:">
										<TD align="right">
											<input type="hidden" name="allSelected" value="0">
											ȫѡ
											<INPUT TYPE="radio" name="selDEV" onclick="selectAll('device_id')" id="alldevice">

											<br>
											δ�ɹ�
											<INPUT TYPE="radio" name="selDEV" onclick="selectFailed('device_id')" id="failedDevice" checked>

										</TD>

										<TD colspan="3">
											<div id="div_device">
												���ȸ���������ѯ�豸��
											</div>
										</TD>
									</TR>

									<TR bgcolor="#FFFFFF">
										<TD HEIGHT=20 colspan="4">&nbsp;</TD>
									</TR>

									<TR>
										<TH colspan="4" align="center">
											����ѡ��
										</TH>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="15%">
												���÷�ʽ��
										</TD>
										<TD colspan="3">
											<!--
											<input type="radio" name="confWlan" value="0" onclick="chg_conf(this.value)" checked>��������
											<input type="radio" name="confWlan" value="1" onclick="chg_conf(this.value)" style="margin-left: 50px;" >��������+����
											 -->

											<SELECT name="confWlan" class="bk" onchange="chg_conf(this.value)" style="width:150px">
												<OPTION value="0">��������</OPTION>
												<OPTION value="1">��������+����</OPTION>
											</SELECT>

											<SPAN id="desc">&nbsp;&nbsp;<font color='#7f9db9'>˵������ѡ�������豸��ԭ��������Σ��������ý��Ϊ�����ߡ�QoS��</font></SPAN>
										</TD>


									</TR>

									<tr bgcolor="#FFFFFF" id="ctrlConf_1" bgcolor="#FFFFFF" style="display:none">
										<td align="right">�󶨶˿ڣ�</td>
										<td align="left">LAN2�ڣ�WLAN2��</td>
										<td align="right">�������ͣ�</td>
										<td align="left">�Ž�
									</tr>
									<tr bgcolor="#FFFFFF" id="ctrlConf_2" bgcolor="#FFFFFF">
										<td align="right">�󶨶˿ڣ�</td>
										<td align="left">LAN2��</td>
										<td align="right">�������ͣ�</td>
										<td align="left">�Ž�</td>
									</tr>

									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="center">
											<font color="blue">��һ�����������</font>
										</TD>
									</TR>
									<tr bgcolor="#FFFFFF" id="softUp">

										<TD align="right" width="15%">����������Է�ʽ��</TD>
										<TD width="30%">
											<s:property value="softStrategyHTML" escapeHtml="false"/>
										</TD>

										<td align="right" width="15%">�������Ŀ��汾��</td>
										<!-- <td align="left" width="30%"><s:property value="goalVersionHTML" escapeHtml="false"/></td> -->
										<td align="left" width="30%">�����豸�Զ�ƥ��</td>

									</tr>

									<TR bgcolor="#FFFFFF">
										<TD id="titleTH" colspan="4" align="center">
											<font color="blue">�ڶ�����iTV����</font>
										</TD>
									</TR>

									<tr bgcolor="#FFFFFF">

										<TD align="right" width="15%">iTV���Է�ʽ��</TD>
										<TD width="30%">
											<s:property value="itvLstrategyHTML" escapeHtml="false"/>
										</TD>

										<td align="right">PVC/VLANID��</td>
										<td align="left">ADSL����:8/85||LAN����:43||EPON����:43</td>

									</tr>

									<!--
									<TR id="wlan_1" bgcolor="#FFFFFF" style="display:none">
										<TD colspan="4" align="center">
											iTV��������
										</TD>
									</TR>
									 -->
									<TR id="wlan_2" bgcolor="#FFFFFF" style="display:none">
										<TD align="right" width="15%">
											SSID2��
										</TD>
										<TD>
											iTV + ���к������λ
											<input type="hidden" name="ssid2" value="">
										</TD>

										<TD align="right" width="15%">
											WPA��֤��Կ��
										</TD>
										<TD>
											���к�����λ
											<input type="hidden" name="wpaPw" value="">
										</TD>
									</TR>

									<!--
										<TR id="wlan_3" bgcolor="#FFFFFF" style="display:none">
											<TD align="right" width="15%">
													��֤��ʽ��
											</TD>
											<TD>
												WPA��֤
												<input type="hidden" name="auWay" value="WPA">
											</TD>
										</TR>
									-->

									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="right" CLASS="green_foot">

											<!-- ��ʾ�Ž� -->
											<input type="hidden" name="wanType" value="1">
											<input type="hidden" name="needUpSoftware" value="1">
											<input type="hidden" name="auWay" value="WPA">
											<input type="hidden" name="goalVersion" value="1">

											<input TYPE="button" name="submitForm" value=" �� �� " class=jianbian onclick="checkForm()">
											&nbsp;
											<!-- <INPUT TYPE="reset" value=" �� �� " class=btn> -->

										</TD>
									</TR>
								</table>
							</td>
						</TR>
					</table>
				</td>
			<tr>
		</table>
	</TD>
  </TR>
  <tr><td height=20></td></tr>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>
</html>
