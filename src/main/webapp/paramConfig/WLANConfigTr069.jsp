<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
%>
<SCRIPT LANGUAGE="JavaScript">
var checkvalue ="<%=device_id%>";

function CheckForm(){
 	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("请选择设备！");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			checkvalue = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  checkvalue = oselect[i].value;
			  num++;
			  break;
			}
		}

 	}
 	if(num ==0){
		alert("请选择设备！");
		return false;
	}
	return true;
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: 宋体"></span>
		</td>
	</tr>
</table>
</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION="">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											参数实例管理
										</div>
									</td>
									<td><img src="../images/attention_2.gif" width="15"
										height="12">开启和关闭无线。
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF" id="wlan_type" style="display:none">
								    <TD align="right">
										配置方式:
									</TD>
									<TD colspan=3>
										<input type="radio" name="type" checked id="rd1" class=btn><label for="rd1">TR069</label>&nbsp;
										<input type="radio" name="type" id="rd2" class=btn><label for="rd2">SNMP</label>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_status" style="display:none">
									<td colspan="4" valign="top" class=column>
										<div id="div_ssid" style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_new_ssid" style="display:none">
									<td colspan="4" valign="top" class=column>
										<div id="div_ssid_new" style="width: 100%; height: 120px; z-index: 1; top: 100px;">
											<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>
												<TR bgcolor="#FFFFFF" id="wlan_add_title">
													<td height="30" colspan="8" align="left" bgcolor='#FFFFFF'>
														新建SSID属性设置 (提示：如新SSID名称与现有某SSID名称相同，将删除原相同名称的SSID）
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" id="wlan_add">
													<td align='center' id="new_wlan_idx" bgcolor='#FFFFFF' width='40' nowrap>
													</TD>
													<td align='center' id="new_ssid_idx" bgcolor='#FFFFFF' width='40' nowrap>
													</TD>
													<td align='center' bgcolor='#FFFFFF' width='300' nowrap>
														<input type="text" name="new_ssid_name"/>
													</td>
													<td  align='center' bgcolor='#FFFFFF' width='150' nowrap>
														<input type="radio" name="new_ssid_open" checked id="nso1"/><label for="nso1">开启</label>
														<input type="radio" name="new_ssid_open" id="nso2"/><label for="nso2">关闭</label>
													</td>
													<td  align='center' bgcolor='#FFFFFF' width='150' nowrap>
														<input type="radio" name="new_ssid_hide" id="nsh1"/><label for="nsh1">广播</label>
														<input type="radio" name="new_ssid_hide" checked id="nsh2"/><label for="nsh2">隐藏</label>
													</td>
													<td align='center' bgcolor='#FFFFFF' width='50'>
														<input type="text" name="new_ssid_channel" value="0" size=2 maxlength=2 />
													</td>
													<td align='center' bgcolor='#FFFFFF' width='150' nowrap>
														<select name="new_ssid_model">
															<option value="a">802.11a
															<option value="b">802.11b
															<option value="g">802.11g
															<option value="b,g" selected>802.11b/g</select>
													</td>
													<td align='left' bgcolor='#FFFFFF' style='padding-left:30' nowrap>	
														<select name="new_ssid_power">
															<option value="1" selected>1级(最大)
															<option value="2">2级
															<option value="3">2级
															<option value="4">2级
															<option value="5">5级(最小)</select>
													</td>
												</TR>
											</table>
										</div>
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="this.blur();ExecMod();" value=" 设 置 " class=btn >
													<INPUT TYPE="button" id="bt_add" style="display:none" onclick="this.blur();createSSIDSave();" value="新建(提交)" class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="this.blur();getStatus();" value=" 获 取 " class=btn >
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
					<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
						<TH colspan="4">操作结果</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
						<td colspan="4" valign="top" class=column>
						<div id="div_ping"
							style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
						</td>
					</TR>
							</table>
						</td>
					</tr>
					<TR>
						<TD HEIGHT=20>&nbsp;</TD>
					</TR>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm4 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var MAX_SSID = 32;
var MIN_SSID_INDEX = 1;
var MAX_SSID_INDEX = 1024;
var log_msg = "";

var disp = 1;
var max_idx = 1;//新SSID的索引号
var ssid_num = 0;
var ssid_idx_arr = null;
var wlan_idx_arr = new Array();

//给String类增加trim方法，过滤空格
// Trim() , Ltrim() , RTrim()

String.prototype.Trim = function() 
{ 
return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 
String.prototype.LTrim = function() 
{ 
return this.replace(/(^\s*)/g, ""); 
} 
String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
} 

//是否是正整数 for Java
function isPosInt(str) {
	var MAX_VALUE = 2147483647;
	var re = /^[1-9]\d*[0-9]?$/g;
	if(str.test(re, str) && str <= MAX_VALUE) {
		return true;
	}
	return false;
}
//是否是整数 for Java
function isPosInt_(str) {
	var MAX_VALUE = 2147483647;
	var re = /^[1-9]\d*[0-9]?$/g;
	if(str.test(re, str) && str <= MAX_VALUE) {
		return true;
	}
	return false;
}
String.prototype.test = function(re, str) {
	return re.test(str);
}

	/**
	 *	新建一个SSID
	 */
	function createSSID(wlan_id) {
		//alert(wlan_id);
		if (ssid_idx_arr == null) {
			//alert("ssid is null");
			max_idx = 1;
		} else {
			var len = ssid_idx_arr.length;
			for (var j=0; j<len; j++) {
				var ts = ssid_idx_arr[j];
				if (ts != null) {
					var ts_arr = ts.split(",");
					if (ts_arr != null) {
						//alert(ts_arr);
						if (ts_arr[0] == wlan_id) {
							if (ts_arr.length == 1) {
								max_idx = 1;
							}
							for (var k=1; k<ts_arr.length; k++) {
								if (ts_arr[k] > k) {
									max_idx = k;
								} else if (k == (ts_arr.length - 1)) {
									max_idx = k + 1;
								}
							}
							break;
						}
					}
				}
			}
		}
		showById("wlan_new_ssid");
		showById("bt_add");
		document.getElementById("new_wlan_idx").innerText = wlan_id;
		document.getElementById("new_ssid_idx").innerText = max_idx;
		//document.getElementById("bt_add").value = "新建(提交)";
		//document.getElementById("bt_add").onclick = createSSIDSave;
	}

	function createSSIDSave() {
		if (!check2()) return false;
		log_msg = "";
		log_msg += "新建SSID： ID为" + id("new_wlan_idx").innerText + "_" + max_idx + "；" + "名称为" + document.forms[0].new_ssid_name.value + "；" + (document.getElementById("nso1").checked ? "开启；" : "关闭；") + (document.getElementById("nsh2").checked ? "隐藏；" : "关闭；") + "信道：" + document.forms[0].new_ssid_channel.value + "；发射功率：" + document.forms[0].new_ssid_power.value + "；模式：" + document.forms[0].new_ssid_model.value + "；";

  		page = "WLANConfigTr069Save.jsp?device_id=" + checkvalue + "&log_msg=" + log_msg + "&new_wlan_idx=" + id("new_wlan_idx").innerText + "&new_ssid_idx=" + max_idx + "&open=" + (document.getElementById("nso1").checked ? "1" : "0") + "&hide=" + (document.getElementById("nsh2").checked ? "1" : "0") + "&name=" + document.forms[0].new_ssid_name.value + "&channel=" + document.forms[0].new_ssid_channel.value + "&power=" + document.forms[0].new_ssid_power.value + "&model=" + document.forms[0].new_ssid_model.value + "&oid_type=1&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&action_type=3&refresh=" + new Date().getTime();
		//alert(page);
		document.all("div_ping").innerHTML = "正在添加新的SSID...";
		document.all("childFrm").src = page;
	}
	
	function resetTo() {
		disp = 0;
		getStatus();
	}

	function check2() {
		var m = document.forms[0].new_ssid_name.value;
		var n = document.forms[0].new_ssid_channel.value;
		if (m.Trim() == "") {
			alert("SSID名称不能为空");
			return false;
		}
		if (n.Trim() == "") {
			alert("信道不能为空");
			return false;
		}
		if (!isPosInt_(n) && n != "0") {
			alert("信道应该是整数");
			return false;
		}
		return true;
	}

	function check3() {
		var m = document.forms[0].new_ssid_name.value;
		if (m.Trim() == "") {
			alert("SSID名称不能为空");
			return false;
		}
		return true;
	}

	function getStatus() {
		hideById("wlan_new_ssid");
		//document.getElementById("bt_add").value = " 新 建 ";
		//document.getElementById("bt_add").onclick = createSSID;
		document.getElementById("tr001").style.display = "";
		document.getElementById("tr002").style.display = "";
  		if(CheckForm()){
  			page = "WLANConfigTr069Save.jsp?device_id=" + checkvalue + "&oid_type=1&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&action_type=1&refresh=" + new Date().getTime();
			if (disp == 1) {
				document.all("div_ping").innerHTML = "正在获取无线配置信息...";
			}
			//alert(page);
			document.all("childFrm").src = page;
		}else{
			return false;
		}
	}

     function ExecMod(){
  		if(CheckForm()){
			//if (!check3()) return false;
			var param = getParam();
			if (null == param || "" == param) {
				alert("当前没有可配置项。");
				return false;
			}
			//alert(param);
  			page = "WLANConfigTr069Save.jsp?device_id=" + checkvalue + "&log_msg=" + log_msg + "&param=" + param + "&oid_type=1&action_type=2&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&refresh=" + new Date().getTime();
			//alert(page);
			document.all("div_ping").innerHTML = "正在设置无线状态...";
			document.all("childFrm").src = page;  		 
		}else{
			return false;
		}
	 }

function getParam() {
	var idx_arr = getIdxArr();
	if (null == idx_arr) {
		return null;
	}
	var param = initParam(idx_arr);
	return param;
}

function initParam(idx_arr) {	
	log_msg = "";
	if (null == idx_arr) {
		return null;
	}
	var id_name = "_1_1";
	var id_open = "_2_1";
	var id_hide = "_3_2";
	var id_channel = "_4_1";
	var id_model = "_5_1";
	var id_power = "_6_1";
	var id_del_ssid = "_7_1";
	var id_del_wlan = "_8_1";
	var p = "";
	//var logArr = new Array();

	for (var i=0; i<idx_arr.length; i++) {
		var s = "";
		var idh = idx_arr[i];
		var wlan_idh = idh.substring(0,1);
		if (id(idh + id_name) == null) {
			s += idh + "$" + "-1";
			s += "$" + "-1";
			s += "$" + "-1";
			s += "$" + "-1";
			s += "$" + "-1";
			s += "$" + "-1";
			s += "$" + "-1";
			s += "$" + (id(wlan_idh + id_del_wlan).checked ? "1" : "0");
			if (id(wlan_idh + id_del_wlan).checked) {
				log_msg += "WLAN模块 " + wlan_idh + "：删除；";
			}
		} else {
			s += idh + "$" + id(idh + id_name).value;
			s += "$" + (id(idh + id_open).checked ? "1" : "0");
			s += "$" + (id(idh + id_hide).checked ? "1" : "0");
			s += "$" + id(idh + id_channel).value;
			s += "$" + id(idh + id_model).value;
			s += "$" + id(idh + id_power).value;
			s += "$" + (id(idh + id_del_ssid).checked ? "1" : "0");
			s += "$" + (id(wlan_idh + id_del_wlan).checked ? "1" : "0");
			if (id(wlan_idh + id_del_wlan).checked) {
				log_msg += "WLAN模块 " + wlan_idh + "：删除；";
			} else {
				if (id(idh + id_del_ssid).checked) {
					log_msg += "SSID " + idh + "：删除；";
				} else {
					log_msg += "SSID " + idh + "；" + (id(idh + id_open).checked ? "开启；" : "关闭；") + (id(idh + id_hide).checked ? "隐藏；" : "广播；") + "信道：" + id(idh + id_channel).value + "；模式：" + id(idh + id_model).value + "；发射功率：" + id(idh + id_power).value + "；";
				}
			}
		}
		//alert(s);
		if ("" == p) {
			p += s;
		} else {
			p += "|" + s;
		}
	}
	return p;
	//alert(p);
}

function id(id) {
	return document.getElementById(id);
}

function getIdxArr() {
	if (ssid_num == 0) {
		return null;
	}
	var idx_arr = new Array();//"wlan_ssid" "1_1"
	for (var i=0; i<ssid_num; i++) {
		var s_arr = ssid_idx_arr[i].split(",");
		if (null == s_arr) {
			continue;
		}
		var wlan = s_arr[0];
		var len_ = s_arr.length;
		if (1 == len_) {
			idx_arr.push(wlan + "_0");
			continue;
		} else {
			for (var j=1; j<len_; j++) {
				idx_arr.push(wlan + "_" + s_arr[j]);
			}
		}		
	}
	//alert(idx_arr);
	return idx_arr;
}

	function setStatus(idx_list, result) {
		//var sHtml = "当前 SSID 总数为：";
		//alert(idx_list);
		if (idx_list != null && !("" == idx_list)) {
			ssid_idx_arr = idx_list.split("|");
			
			if (ssid_idx_arr != null) {
				ssid_num = ssid_idx_arr.length;
				for (var i=0; i<ssid_num; i++) {
					var s = ssid_idx_arr[i];
					wlan_idx_arr.push(s.substring(0,1));
				}
			} else {
				ssid_num = 0;
			}
		} else {
			ssid_num = 0;
		}
		//alert(wlan_idx_arr);
		//alert(ssid_idx_arr[0]);
		//sHtml += ssid_num;
		if (disp == 0) {
			disp = 1;
			return;
		}

		document.all("div_ping").innerHTML = (result == "false") ? "获取失败。" : "获取成功。";
		showById("bt_set");
		//showById("bt_add");
		showById("wlan_status");

	}

	function showById(id) {
		document.getElementById(id).style.display = "";
	}

	function hideById(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>
