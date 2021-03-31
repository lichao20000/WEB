<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>iTV配置页面无线配置</title>
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
		alert("6位序列号或ip地址必填");
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
		alert("请选择设备！");
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
		alert("请选择设备！");
		return false;
	}

	var vpi = document.frm.vpi.value;
	var vci = document.frm.vci.value;
	var devObj = document.all("device_id"); 
	var needSoft = document.frm.needUpSoftware.value;
	var devSelected = false;
	//alert(vpi+vci+devObj);
	if(vpi=="" || vci==""){
		alert("请您输入pvc");
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
						工单管理</td>
						<td nowrap><img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;查询方式： 
							<input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType" checked>按设备
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
											iTV策略批量配置
										</TH>
									</TR>
									<tr id="deviceQuery" bgcolor="#FFFFFF" style="display:">
										<td align="right">设备序列号：</td>
										<td align="left"><input type="text" name="deviceSerialnumber">&nbsp;&nbsp;最后六位</td>
										<td align="right">IP地址：</td>
										<td align="left"><input type="text" name="ipAddress"></td>
									</tr>
									<tr id="query" style="display:">
										<td colspan="4" align="right" CLASS="green_foot">
											<input type="button" value="查 询" onclick="queryDevice();">
										</td>
									</tr>
									<TR bgcolor="#FFFFFF">
										<TD align="right">
											设备列表：
										</TD>
										<TD colspan="3">
											<div id="div_device" style="width:100%; height:100px; z-index:1; top: 100px; overflow:scroll">
											</div>
										</TD>
									</TR>									
									<TR id="wlan_1" style="display:">
										<TH colspan="4" align="center">
											iTV无线配置
										</TH>
									</TR>
									
									<TR id="wlan_2" bgcolor="#FFFFFF" style="display:">
										<TD align="right" width="15%">
											SSID2：
										</TD>
										<TD>
											iTV + 序列号最后五位
											<input type="hidden" name="ssid2" value="">
										</TD>
										<TD align="right" width="15%">
											WPA认证密钥：
										</TD>
										<TD>
											<input type="text" name="wpaPw" value="1111111111" class="bk">
										</TD>
										<!--iTV无线策略方式  -->
										<input type="hidden" name="wlanstrategy_type" value="0">
										<!-- 认证方式： -->
										<input type="hidden" name="auWay" value="WPA">
										<!--VPI/VCI  -->
										<input type="hidden" name="vpi" size="2" value="8" class="bk">
										<input type="hidden" name="vci" size="3" value="85" class="bk">
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="right" CLASS="green_foot">
											<INPUT TYPE="reset" value=" 重 置 " class=btn>
											&nbsp;
											<input type="hidden" name="wanType" value="1">
											<input type="hidden" name="needUpSoftware" value="1">
											<INPUT TYPE="button" name="submitForm" value=" 订 制 " class=btn onclick="checkForm()">
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