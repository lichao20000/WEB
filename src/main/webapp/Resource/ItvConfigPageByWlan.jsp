<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>iTV����ҳ����������</title>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function queryDevice(){
	
	var devSerial = document.all("deviceSerialnumber");
	var ipaddress = document.all("ipAddress");
	
	var url = "devVenderModelAction!getDeviceInfoList.action";
	// gw_type
	var gw_type = $("input[@name='gw_type']").val();
	var devSerialnumber = devSerial.value;
	var ip = ipaddress.value;
	if((devSerialnumber.trim() == "" || devSerialnumber.length < 6) && ip.trim() == ""){
		alert("6λ���кŻ�ip��ַ����");
		devSerial.focus();
		return false;
	}
	$.post(url,{
		checkType:2,
		deviceSerialnumber:devSerialnumber,
		ipAddress:ip,
		gwType:gw_type
   	},function(mesg){
   		document.getElementById("div_device").innerHTML = mesg;
   	});
}

function checkForm(){

	var oselect = document.all("device_id");
	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	}
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			num = 1;
		}
	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				num++;
			}
		}
	}
	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}

	var vpi = document.frm.vpi.value;
	var vci = document.frm.vci.value;
	var devObj = document.all("device_id"); 
	var needSoft = document.frm.needUpSoftware.value;
	var devSelected = false;
	//alert(vpi+vci+devObj);
	if(vpi=="" || vci==""){
		alert("��������pvc");
		return false;
	}
	document.frm.submitForm.disabled = true;
	document.frm.submit();
}

</SCRIPT>
</head>
<body>
<form name="frm" action="itvConfigByWlan!doStatery.action" method="post" onsubmit="return checkForm();">
<input type="hidden" name="gw_type" value="<s:property value='gw_type' />" />
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
						��������</td>
						<td nowrap><img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;��ѯ��ʽ�� 
							<input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType" checked>���豸
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
									<tr id="deviceQuery" bgcolor="#FFFFFF" style="display:">
										<td align="right">�豸���кţ�</td>
										<td align="left"><input type="text" name="deviceSerialnumber">&nbsp;&nbsp;�����λ</td>
										<td align="right">IP��ַ��</td>
										<td align="left"><input type="text" name="ipAddress"></td>
									</tr>
									<tr id="query" style="display:">
										<td colspan="4" align="right" CLASS="green_foot">
											<input type="button" value="�� ѯ" onclick="queryDevice();">
										</td>
									</tr>
									<TR bgcolor="#FFFFFF">
										<TD align="right">
											�豸�б�
										</TD>
										<TD colspan="3">
											<div id="div_device" style="width:100%; height:100px; z-index:1; top: 100px; overflow:scroll">
											</div>
										</TD>
									</TR>									
									<TR id="wlan_1" style="display:">
										<TH colspan="4" align="center">
											iTV��������
										</TH>
									</TR>
									
									<TR id="wlan_2" bgcolor="#FFFFFF" style="display:">
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
											<input type="text" name="wpaPw" value="1111111111" class="bk">
										</TD>
										<!--iTV���߲��Է�ʽ  -->
										<input type="hidden" name="wlanstrategy_type" value="0">
										<!-- ��֤��ʽ�� -->
										<input type="hidden" name="auWay" value="WPA">
										<!--VPI/VCI  -->
										<input type="hidden" name="vpi" size="2" value="8" class="bk">
										<input type="hidden" name="vci" size="3" value="85" class="bk">
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="right" CLASS="green_foot">
											<INPUT TYPE="reset" value=" �� �� " class=btn>
											&nbsp;
											<input type="hidden" name="wanType" value="1">
											<input type="hidden" name="needUpSoftware" value="1">
											<INPUT TYPE="button" name="submitForm" value=" �� �� " class=btn onclick="checkForm()">
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