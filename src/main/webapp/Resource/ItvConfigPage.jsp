<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>iTV配置页面</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//----------------------------属地-厂商-型号-版本-设备列表-------------------------------
var chekType = 3;
function ShowDialog(valu){
	//3：属地型号，1:用户名和电话，2：设备序列号和IP,4：按用户导入
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
		$("div[@id='div_device']").html("请先根据条件查询设备！");

		//选择切换的时候把按钮置可用 add by zhangcong 2011-06-07
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

		$("div[@id='div_device']").html("请先根据条件查询设备！");

		//选择切换的时候把按钮置可用 add by zhangcong 2011-06-07
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
		$("div[@id='div_device']").html("请先根据条件查询设备！");

		//选择切换的时候把按钮置可用 add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = false;
	}
}

function showDesc() {
	$("tr[@id='importUserQuery2']").toggle();
}

function selectFailed(devInfo) {

	document.frm.alldevice.checked=false;
	//调用selectAll方法
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
	//修复返回后没有自动调整查询菜单显示的Bug add by 张聪(zhangcong) 2011-06-07
	var radios = document.getElementsByName("checkType");
	//选中导入文件
	if(radios[0].checked)
	{
		ShowDialog(4);
	}
	//选中用户帐号
	if(radios[1].checked)
	{
		ShowDialog(1);
	}
	//选中设备序列号
	if(radios[2].checked)
	{
		ShowDialog(2);
	}
	//for(var i = 0;i < radios.length ; i ++)
	//{
		////if(radios[i].checked)
		//{
			//显示对应的查询方式
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
	//设置查询状态信息 add by zhangcong@ 2011-06-09
	$("div[@id='div_device']").html("正在查询设备，请稍后...");

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
		//如果未选择的设备版本
		if(device_type){
			device_type_value = device_type.value;
		}
		//如果未选择的设备型号
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
			alert("5位序列号或ip地址必填");
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
		//按钮按下的时候把按钮置灰 add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = true;
	}else {
		var usname = uname.value;
		var usphone = utelephone.value;
		if(usname.trim() == "" && usphone.trim() == ""){
			alert("用户名或电话地址必须填一个");
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
		//按钮按下的时候把按钮置灰 add by zhangcong 2011-06-07
		document.frm.queryBtn.disabled = true;
	}
}

//----------------------------属地-厂商-型号-版本-设备列表-------------------------------
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
		$("td[@id='titleTH']").html("<font color='blue'>第二步：iTV有线+无线</font>");
		$("span[@id='desc']").html("&nbsp;&nbsp;<font color='#7f9db9'>说明：此选项无论设备上原先配置如何，最终配置结果为“有线、无线、QoS”</font>");

	} else {
		$("tr[@id='ctrlConf_1']").hide();
		$("tr[@id='ctrlConf_2']").show();
		//$("tr[@id='wlan_1']").hide();
		$("tr[@id='wlan_2']").hide();
		//$("tr[@id='wlan_3']").hide();
		$("td[@id='titleTH']").html("<font color='blue'>第二步：iTV有线</font>");
		$("span[@id='desc']").html("&nbsp;&nbsp;<font color='#7f9db9'>说明：此选项无论设备上原先配置如何，最终配置结果为“有线、QoS”</font>");
	}
}

function checkForm(){
	var devObj = document.all("device_id");
	var needSoft = document.frm.needUpSoftware.value;
	var devSelected = false;

	if(!devObj){
		alert("您没有选择设备");
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
			alert("您没有选择设备");
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
						iTV策略批量配置</td>
						<td nowrap><img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;查询方式：
							<input type="radio" value="4" onclick="ShowDialog(this.value)" name="checkType" checked>按文件导入
							<!-- <input type="radio" value="3" onclick="ShowDialog(this.value)" name="checkType">按属地和型号&nbsp;&nbsp;-->
							<input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">按用户&nbsp;&nbsp;
							<input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">按设备

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
									<tr id="cityQuery1" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">属地：</td>
										<td align="left" width="30%"><div id="cityList"></div></td>
										<td align="right">设备厂商：</td>
										<td align="left"><div id="vendorList"></div></td>
									</tr>
									<tr id="cityQuery2" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">设备型号：</td>
										<td align="left" width="30%"><div id="modelList"></div></td>
										<td align="right">设备版本：</td>
										<td align="left"><div id="versionList"></div></td>
									</tr>
									<tr id="userQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right" width="15%">用户名：</td>
										<td align="left" width="30%"><input type="text" name="username"></td>
										<td align="right">电话：</td>
										<td align="left"><input type="text" name="telephone"></td>
									</tr>
									<tr id="deviceQuery" bgcolor="#FFFFFF" style="display:none">
										<td align="right">设备序列号(序列号)：</td>
										<td align="left"><input type="text" name="deviceSerialnumber">&nbsp;&nbsp;最后五位</td>
										<td align="right">IP地址：</td>
										<td align="left"><input type="text" name="ipAddress"></td>
									</tr>
									<tr id="importUserQuery" bgcolor="#FFFFFF"  style="display:">
										<td align="right">提交文件：</td>
										<td colspan="3">
											<div id="importUsername">
												<iframe name="loadForm" FRAMEBORDER=0 SCROLLING=NO src="ItvImportUsername.jsp" height="30" width="100%">
												</iframe>
											</div>
										</td>
									</tr>

									<tr id="importUserQuery2" bgcolor="#FFFFFF">
										<td align="right">注意事项：
										<td colspan="3">
										<font color="#7f9db9">
										1、需要导入的文件格式为Excel。
										 <br>
										2、文件的第一行为标题行，即用户账号。
										 <br>
										3、文件只有一列。
										 <br>
										4、文件的行数不超过100行,如超过100行，只解析前100行数据。
										</font>
										</td>
									</tr>
									<!-- 添加一个隐藏的占位栏，保持界面风格一致 -->
									<tr id="space" style="display:none" bgcolor="#FFFFFF">
										<td colspan="4" align="right" CLASS="green_foot">
											&nbsp;
										</td>
									</tr>
									<tr id="query" style="display:none" bgcolor="#FFFFFF">
										<td colspan="4" align="right" CLASS="green_foot">
											<input type="button" class=jianbian name=queryBtn value=" 查 询 " onclick="queryDevice();">
											&nbsp;
										</td>
									</tr>
									<TR bgcolor="#FFFFFF" id="deviceListTrId" style="display:">
										<TD align="right">
											<input type="hidden" name="allSelected" value="0">
											全选
											<INPUT TYPE="radio" name="selDEV" onclick="selectAll('device_id')" id="alldevice">

											<br>
											未成功
											<INPUT TYPE="radio" name="selDEV" onclick="selectFailed('device_id')" id="failedDevice" checked>

										</TD>

										<TD colspan="3">
											<div id="div_device">
												请先根据条件查询设备！
											</div>
										</TD>
									</TR>

									<TR bgcolor="#FFFFFF">
										<TD HEIGHT=20 colspan="4">&nbsp;</TD>
									</TR>

									<TR>
										<TH colspan="4" align="center">
											参数选择
										</TH>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="15%">
												配置方式：
										</TD>
										<TD colspan="3">
											<!--
											<input type="radio" name="confWlan" value="0" onclick="chg_conf(this.value)" checked>配置有线
											<input type="radio" name="confWlan" value="1" onclick="chg_conf(this.value)" style="margin-left: 50px;" >配置有线+无线
											 -->

											<SELECT name="confWlan" class="bk" onchange="chg_conf(this.value)" style="width:150px">
												<OPTION value="0">配置有线</OPTION>
												<OPTION value="1">配置有线+无线</OPTION>
											</SELECT>

											<SPAN id="desc">&nbsp;&nbsp;<font color='#7f9db9'>说明：此选项无论设备上原先配置如何，最终配置结果为“有线、QoS”</font></SPAN>
										</TD>


									</TR>

									<tr bgcolor="#FFFFFF" id="ctrlConf_1" bgcolor="#FFFFFF" style="display:none">
										<td align="right">绑定端口：</td>
										<td align="left">LAN2口，WLAN2口</td>
										<td align="right">连接类型：</td>
										<td align="left">桥接
									</tr>
									<tr bgcolor="#FFFFFF" id="ctrlConf_2" bgcolor="#FFFFFF">
										<td align="right">绑定端口：</td>
										<td align="left">LAN2口</td>
										<td align="right">连接类型：</td>
										<td align="left">桥接</td>
									</tr>

									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="center">
											<font color="blue">第一步：软件升级</font>
										</TD>
									</TR>
									<tr bgcolor="#FFFFFF" id="softUp">

										<TD align="right" width="15%">软件升级策略方式：</TD>
										<TD width="30%">
											<s:property value="softStrategyHTML" escapeHtml="false"/>
										</TD>

										<td align="right" width="15%">软件升级目标版本：</td>
										<!-- <td align="left" width="30%"><s:property value="goalVersionHTML" escapeHtml="false"/></td> -->
										<td align="left" width="30%">根据设备自动匹配</td>

									</tr>

									<TR bgcolor="#FFFFFF">
										<TD id="titleTH" colspan="4" align="center">
											<font color="blue">第二步：iTV有线</font>
										</TD>
									</TR>

									<tr bgcolor="#FFFFFF">

										<TD align="right" width="15%">iTV策略方式：</TD>
										<TD width="30%">
											<s:property value="itvLstrategyHTML" escapeHtml="false"/>
										</TD>

										<td align="right">PVC/VLANID：</td>
										<td align="left">ADSL上行:8/85||LAN上行:43||EPON上行:43</td>

									</tr>

									<!--
									<TR id="wlan_1" bgcolor="#FFFFFF" style="display:none">
										<TD colspan="4" align="center">
											iTV无线配置
										</TD>
									</TR>
									 -->
									<TR id="wlan_2" bgcolor="#FFFFFF" style="display:none">
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
											序列号最后八位
											<input type="hidden" name="wpaPw" value="">
										</TD>
									</TR>

									<!--
										<TR id="wlan_3" bgcolor="#FFFFFF" style="display:none">
											<TD align="right" width="15%">
													认证方式：
											</TD>
											<TD>
												WPA认证
												<input type="hidden" name="auWay" value="WPA">
											</TD>
										</TR>
									-->

									<TR bgcolor="#FFFFFF">
										<TD colspan="4" align="right" CLASS="green_foot">

											<!-- 表示桥接 -->
											<input type="hidden" name="wanType" value="1">
											<input type="hidden" name="needUpSoftware" value="1">
											<input type="hidden" name="auWay" value="WPA">
											<input type="hidden" name="goalVersion" value="1">

											<input TYPE="button" name="submitForm" value=" 订 制 " class=jianbian onclick="checkForm()">
											&nbsp;
											<!-- <INPUT TYPE="reset" value=" 重 置 " class=btn> -->

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
