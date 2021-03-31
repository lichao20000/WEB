<%--
FileName	: WLANConfigTr069_xj.jsp
Date		: 2008年12月26日
Desc		: WLAN配置.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});


function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='device_id_old_']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		}
	}
	
function query2db()
{
	var device_id=document.all("device_id_old_").value;
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}
	var page="";
	page="showWLANParam.jsp?device_id="+device_id+ "&flag_boolean=true&refresh="+Math.random();
	document.all("childFrm1").src = page;
}

function query2device()
{
	var device_id=document.all("device_id_old_").value;
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}
	var page="";
	page="showWLANParam.jsp?device_id="+device_id+ "&flag_boolean=false&refresh="+Math.random();
	document.all("childFrm1").src = page;
}

function edit(device_id,lan_id,lan_wlan_id,gather_time,ap_enable,powerlevel,enable,ssid,hide,beacontype,wep_key,wpa_key)
{
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	
	for (var i=0; i<trs.length; i++) {
		if ( "all"==trs[i].isShow || "edit"==trs[i].isShow) {
			trs[i].style.display = "";
		}else{
			trs[i].style.display = "none";
		}
		
		if ( "Basic"==beacontype ) {
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else if ( "WPA"==beacontype ) {
			if ( "wpa_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wep_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else{
			if ( "wpa_key"==trs[i].isShow||"wep_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}
		}
	}
	
	var checkradios = document.all("hide");
	
	for(var i=0;i<checkradios.length;i++)
    {
      if(hide==checkradios[i].value)
	  {
	    checkradios[i].checked=true;
	    break;
	  }
    }
    
	document.all("device_id_").value = device_id;
	document.all("lan_id_").value = lan_id;
	document.all("lan_wlan_id_").value =lan_wlan_id;
	
	document.all("gather_time").value =gather_time;
	document.all("ap_enable").value =ap_enable;
	document.all("powerlevel").value =powerlevel;
	document.all("enable").value =enable;
	

	document.all("ssid").value =ssid;
	document.all("beacontype").value =beacontype;
	
	document.all("ssid_old").value =ssid;
	document.all("hide_old").value =hide;
	document.all("beacontype_old").value =beacontype;

	document.all("wep_key").value =wep_key;
	document.all("wep_key_sec").value =wep_key;
	document.all("wep_key_old").value =wep_key;
	
	document.all("wpa_key").value =wpa_key;
	document.all("wpa_key_sec").value =wpa_key;
	document.all("wpa_key_old").value =wpa_key;
}

function delWlan_conn(ths, wlan_conn){
	//alert(ths);
	if(!confirm("确实要从设备上删除该结点吗？")){
		return false;
	}
	var aWlan_conn = wlan_conn.split("|");
	if(!typeof(aWlan_conn) || !typeof(aWlan_conn.length)){
		alert("删除的结点不存在");
		return false;
	}
	var obj = ths;
	var device_id = aWlan_conn[0];
	var lan_id = aWlan_conn[1];
	var lan_wlan_id = aWlan_conn[2];
	var url = "manageWlan!delWlan.action";
	//document.all("pvc_del").isDisabled = true;
	obj.disabled = true;
	//alert(device_id+wan_id+wan_conn_id);
	$.post(url,{
      device_id:device_id,
      lan_id:lan_id,
      lan_wlan_id:lan_wlan_id
    },function(mesg){
    	alert(mesg);
    	obj.disabled = false;
    });
}

function editWlanConn(e){
	
	var device_id=document.all("device_id_").value;
	var lan_id=document.all("lan_id_").value;
	var lan_wlan_id=document.all("lan_wlan_id_").value;
	
	var checkradios = document.all("hide");
	var hide;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    hide=checkradios[i].value;
	    break;
	  }
    }
	
	var gather_time = document.all("gather_time").value;
	var ap_enable = document.all("ap_enable").value;
	var powerlevel = document.all("powerlevel").value;
	var enable = document.all("enable").value;
	
	var ssid=document.all("ssid").value;
	var beacontype=document.all("beacontype").value;
	var wep_key=document.all("wep_key").value;
	var wep_key_sec=document.all("wep_key_sec").value;
	var wpa_key=document.all("wpa_key").value;
	var wpa_key_sec=document.all("wpa_key_sec").value;
	
	var ssid_old=document.all("ssid_old").value;
	var hide_old=document.all("hide_old").value;
	var beacontype_old=document.all("beacontype_old").value;
	var wep_key_old=document.all("wep_key_old").value;
	var wpa_key_old=document.all("wpa_key_old").value;
	
	ssid = ssid.replace(/(^\s*)|(\s*$)/g, ""); 
	
	if(""==ssid||null==ssid||"null"==ssid){
		alert("请填写SSID！");
		return false;
	}
	
	if("Basic"==beacontype){
		
		if(""==wep_key||null==wep_key||"null"==wep_key){
			alert("请填写WEP模式密钥！");
			return false;
		}
		
		if(wep_key!=wep_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
	}
	
	if("WPA"==beacontype){
		
		if(""==wpa_key||null==wpa_key||"null"==wpa_key){
			alert("请填写WPA模式密钥！");
			return false;
		}
		
		if(wpa_key!=wpa_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
	}
	
	e.disabled = true;
	
	var url = "manageWlan!edit.action";
	
	$.post(url,{
		device_id:device_id,
		lan_id:lan_id,
		lan_wlan_id:lan_wlan_id,
		gather_time:gather_time,
		ap_enable:ap_enable,
		powerlevel:powerlevel,
		enable:enable,
		ssid:ssid,
		hide:hide,
		beacontype:beacontype,
		wep_key:wep_key,
		wpa_key:wpa_key,
		ssid_old:ssid_old,
		hide_old:hide_old,
		beacontype_old:beacontype_old,
		wep_key_old:wep_key_old,
		wpa_key_old:wpa_key_old
    },function(mesg){
    	alert(mesg);
    	e.disabled = false;
    });
}

function chg_auth(enc_value) {

	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	
	for (var i=0; i<trs.length; i++) {
		if ( "Basic"==enc_value ) {
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else if ( "WPA"==enc_value ) {
			if ( "wpa_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wep_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else{
			if ( "wpa_key"==trs[i].isShow||"wep_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}
		}
	}
}

function add(device_id) {
	document.all("device_id_").value = device_id;
	document.all("beacontype").value = "None";
	
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	
	for (var i=0; i<trs.length; i++) {
		if ( "all"==trs[i].isShow || "add"==trs[i].isShow) {
			trs[i].style.display = "";
		}else{
			trs[i].style.display = "none";
		}
	}
	
	var checkradios = document.all("hide");
	
	for(var i=0;i<checkradios.length;i++)
    {
      if("0"==checkradios[i].value)
	  {
	    checkradios[i].checked=true;
	    break;
	  }
    }

	document.all("ssid").value = "";

	document.all("wep_key").value = "";
	document.all("wep_key_sec").value = "";;
	
	document.all("wpa_key").value = "";;
	document.all("wpa_key_sec").value = "";;
}

function addWlanConn(e) {
	
	var device_id=document.all("device_id_").value;
	
	var checkradios = document.all("hide");
	var hide;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    hide=checkradios[i].value;
	    break;
	  }
    }
	
	var ssid=document.all("ssid").value;
	var beacontype=document.all("beacontype").value;
	var wep_key=document.all("wep_key").value;
	var wep_key_sec=document.all("wep_key_sec").value;
	var wpa_key=document.all("wpa_key").value;
	var wpa_key_sec=document.all("wpa_key_sec").value;
	
	ssid = ssid.replace(/(^\s*)|(\s*$)/g, ""); 
	
	if(""==ssid||null==ssid||"null"==ssid){
		alert("请填写SSID！");
		return false;
	}
	
	if("Basic"==beacontype){
		
		if(""==wep_key||null==wep_key||"null"==wep_key){
			alert("请填写WEP模式密钥！");
			return false;
		}
		
		if(wep_key!=wep_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
	}
	if("WPA"==beacontype){
		
		if(""==wpa_key||null==wpa_key||"null"==wpa_key){
			alert("请填写WPA模式密钥！");
			return false;
		}
		
		if(wpa_key!=wpa_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
	}
	
	e.disabled = true;
	
	var url = "manageWlan!add.action";
	
	$.post(url,{
		device_id:device_id,
		ssid:ssid,
		hide:hide,
		beacontype:beacontype,
		wep_key:wep_key,
		wpa_key:wpa_key
    },function(mesg){
    	alert(mesg);
    	e.disabled = false;
    });
}


</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post">
	<input type="hidden" name="device_id_old_" />
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite" nowrap>
							WLAN配置
						</td>
						<td nowrap>
							<img src="../images/attention_2.gif" width="15" height="12">
						</td>
					</tr>
				</table>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
										<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column>
										设备属地
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column>
										设备序列号
									</td>
									<td id="tdDeviceSn">
									</td>

								</TR>
								<TR>
									<TD colspan="4" align="right" CLASS="green_foot">
										<INPUT TYPE="button" value="数据库中查询" class=btn
											onclick="query2db()">
										&nbsp;
										<INPUT TYPE="button" value="设备实时获取" class=btn
											onclick="query2device()">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
				<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr>
						<TD bgcolor=#999999>
							<div id="div_WLAN_PARAM">
								<span></span>
							</div>
						</td>
					</tr>
				</TABLE>
				<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<input type="hidden" name="device_id_" />
					<input type="hidden" name="lan_id_" />
					<input type="hidden" name="lan_wlan_id_" />
					<input type="hidden" name="gather_time" />
					<input type="hidden" name="ap_enable" />
					<input type="hidden" name="powerlevel" />
					<input type="hidden" name="enable" />
					<TR>
						<TD bgcolor=#999999>
							<TABLE id="tb2" border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR isShow="edit" STYLE="display: none">
									<TH align="center" colspan=4 valign="center">
										<B><span id="actLabel">修改</span>
										</B>
									</TH>
								</TR>
								<TR isShow="add" STYLE="display: none">
									<TH align="center" colspan=4 valign="center">
										<B><span id="actLabel">新增</span>
										</B>
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
									<TD width="40%" class=column align="right">
										SSID
									</TD>
									<TD width="60%" colspan=3>
										<input type="hidden" name="ssid_old">
										<INPUT TYPE="text" NAME="ssid" maxlength=25 class=bk size=25>
										<font color="red">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
									<TD class=column align="right">
										是否隐藏
									</TD>
									<TD colspan=3>
										<input type="hidden" name="hide_old">
										<input type="radio" value="0" name="hide" />
										否
										<input type="radio" value="1" name="hide" />
										是
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
									<TD class=column align="right">
										加密安全模式
									</TD>
									<TD colspan=3>
										<input type="hidden" name="beacontype_old">
										<SELECT name="beacontype" class="bk"
											onchange="chg_auth(this.value)">
											<OPTION value="None">
												None
											</OPTION>
											<OPTION value="Basic">
												Basic
											</OPTION>
											<OPTION value="WPA">
												WPA
											</OPTION>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
									<TD align="right">
										WEP模式密钥:
									</TD>
									<TD>
										<input type="hidden" name="wep_key_old">
										<input type="text" name="wep_key" class="bk" maxlength=10
											size=10>
										<font color="red">*请输入十位数字、字母！</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
									<TD align="right">
										再一次输入WEP模式密钥:
									</TD>
									<TD>
										<input type="text" name="wep_key_sec" class="bk" maxlength=10
											size=10>
										<font color="red">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
									<TD align="right">
										WPA模式密钥:
									</TD>
									<TD>
										<input type="hidden" name="wpa_key_old">
										<input type="text" name="wpa_key" class="bk" maxlength=10
											size=10>
										<font color="red">*请输入十位数字、字母！</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
									<TD align="right">
										再一次输入WPA模式密钥:
									</TD>
									<TD>
										<input type="text" name="wpa_key_sec" class="bk" maxlength=10
											size=10>
										<font color="red">*</font>
									</TD>
								</TR>
								<TR isShow="edit" STYLE="display: none">
									<TD colspan="4" align="right" class=green_foot>
										<INPUT TYPE="button" value=" 更 新 "
											onclick="editWlanConn(this)" class=btn>
										&nbsp;&nbsp;
									</TD>
								</TR>
								<TR isShow="add" STYLE="display: none">
									<TD colspan="4" align="right" class=green_foot>
										<INPUT TYPE="button" value=" 新 增 " onclick="addWlanConn(this)"
											class=btn>
										&nbsp;&nbsp;
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
				<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
