<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<%
	request.setCharacterEncoding("GBK");
	String deviceId = request.getParameter("device_id");
%>
<SCRIPT LANGUAGE="JavaScript">
//设备id
var deviceId=<%=deviceId%>;
$(function(){
	deviceResult();
});

//旧数据
var oldParams="";
//设备数据
var devParam="";
//新数据
var newParams="";

function save()
{
	if(!checkParam())
	{
		return false;
	}
    
	$('#save_btn').attr("disabled",true);
	var url = "<s:url value='/gtms/stb/resource/stbSetConfParam!updateConfParam.action'/>";
	$.post(url,{
		deviceId:deviceId,
		newParams:newParams
	},function(ajax){
		alert(ajax);
		if(ajax='保存成功！'){
			window.close();
		}else{
			$('#save_btn').attr("disabled",false);
		}
	});
}

function deviceResult()
{
	var url = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConfParam.action'/>";
	if(deviceId!=""){
		$.post(url,{
			deviceId:deviceId
		},function(ajax){
			if(ajax!="")
			{
				oldParams=ajax;
				var data=ajax.split("#");
				
				devParam=data[0]+"#"+data[1]+"#"+data[2]+"#"+data[3];
				$("td[@id='vendor_name']").html(data[0]);
				$("td[@id='model_name']").html(data[1]);
				$("td[@id='mac']").html(data[2]);
				$("td[@id='serv_account']").html(data[3]);
				
				$("input[@id='auth_server']").val(data[4]);
				$("input[@id='auth_server_bak']").val(data[5]);
				$("input[@id='auth_server_conn_peroid']").val(data[6]);
				$("input[@id='zeroconf_server']").val(data[7]);
				$("input[@id='zeroconfig_server_bak']").val(data[8]);
				$("input[@id='cmd_server']").val(data[9]);
				$("input[@id='cmd_server_bak']").val(data[10]);
				$("input[@id='cmd_server_conn_peroid']").val(data[11]);
				$("input[@id='ntp_server_main']").val(data[12]);
				$("input[@id='ntp_server_bak']").val(data[13]);
				$("td[@id='auto_sleep_mode']").html(getAutoSleepModeSelect(data[14]));
				$("td[@id='auto_sleep_time']").html(getAutoSleepTimeSelect(data[15]));
				$("td[@id='ip_protocol_version_lan']").html(
						getProtocolVersionSelect("ip_protocol_version_lan",data[16]));
				$("td[@id='ip_protocol_version_wifi']").html(
						getProtocolVersionSelect("ip_protocol_version_wifi",data[17]));
				$("td[@id='update_time']").html(data[18]);
				$("input[@id='update_time_h']").val(data[18]);
				$("td[@id='set_stb_time']").html(data[19]);
				$("input[@id='set_stb_time_h']").val(data[19]);
				
				$("td[@id='tdDeviceSn']").html(data[20]);
				$("input[@id='sn_h']").val(data[20]);
				$("td[@id='tdDeviceCityName']").html(data[21]);
				$("input[@id='city_h']").val(data[21]);
			}
		});
	}
}

//检测参数
function checkParam()
{
	if(deviceId==""){
		alert("请查询设备！");
		return false;
    }
    
	var auth_server_conn_peroid=trim($("input[@id='auth_server_conn_peroid']").val());
	var cmd_server_conn_peroid=trim($("input[@id='cmd_server_conn_peroid']").val());
    if(!isNum(auth_server_conn_peroid) || !isNum(cmd_server_conn_peroid)){
		alert("心跳周期请输入整数秒！");
		return false;
	}
    
    var ntp_server_main=trim($("input[@id='ntp_server_main']").val());
    var ntp_server_bak=trim($("input[@id='ntp_server_bak']").val());
    if(!isIP(ntp_server_main) || !isIP(ntp_server_bak)){
    	alert("主/备时钟同步服务地址必须满足IP格式！");
    	return false;
    }
    
    var auth_server=trim($("input[@id='auth_server']").val());
    var auth_server_bak=trim($("input[@id='auth_server_bak']").val());
    var zeroconf_server=trim($("input[@id='zeroconf_server']").val());
   	var zeroconfig_server_bak=trim($("input[@id='zeroconfig_server_bak']").val());
    var cmd_server=trim($("input[@id='cmd_server']").val());
    var cmd_server_bak=trim($("input[@id='cmd_server_bak']").val());
    
    if(auth_server.indexOf("#")>-1 
    		|| auth_server_bak.indexOf("#")>-1 
    		|| zeroconf_server.indexOf("#")>-1 
    		|| zeroconfig_server_bak.indexOf("#")>-1 
    		|| cmd_server.indexOf("#")>-1 
    		|| cmd_server_bak.indexOf("#")>-1 ){
    	alert("服务地址禁止含有'#'字符！");
    	return false;
    }
    
    var auto_sleep_mode=trim($("select[@name='auto_sleep_mode']").val());
    var auto_sleep_time=trim($("select[@name='auto_sleep_time']").val());
    if(auto_sleep_mode=='1' && auto_sleep_time=='-1'){
    	alert("请选择自动待机关闭时长！");
    	return false;
    }
    
    if(auto_sleep_mode=="-1"){
    	auto_sleep_mode="";
    }
    if(auto_sleep_time=="-1"){
    	auto_sleep_time="";
    }
    
    var ip_lan=trim($("select[@name='ip_protocol_version_lan']").val());
    var ip_wifi=trim($("select[@name='ip_protocol_version_wifi']").val());
    if(ip_lan=="-1"){
    	ip_lan="";
    }
    if(ip_wifi=="-1"){
    	ip_wifi="";
    }
    
    newParams=devParam+"#"+auth_server+"#"+auth_server_bak
				+"#"+auth_server_conn_peroid
				+"#"+zeroconf_server+"#"+zeroconfig_server_bak
				+"#"+cmd_server+"#"+cmd_server_bak
				+"#"+cmd_server_conn_peroid
				+"#"+ntp_server_main+"#"+ntp_server_bak
				+"#"+auto_sleep_mode+"#"+auto_sleep_time
				+"#"+ip_lan+"#"+ip_wifi
				+"#"+trim($("input[@id='update_time_h']").val())
				+"#"+trim($("input[@id='set_stb_time_h']").val());
    
	var p="#"+trim($("input[@id='sn_h']").val())+"#"+trim($("input[@id='city_h']").val());
	if(oldParams==(newParams+p))
	{
		alert("请至少修改一个参数后再保存！");
		return false;
	}
	newParams+="#end";
	return true;
}

//ip格式校验
function isIP(ip)
{
	if(ip!=null && ip!="")
	{	
		//正则表达式
		var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;
		return ip.match(reg);
	}
	return true;
}

//判断是否是正整数
function isNum(s)
{
    if(s!=null && s!="")
    {
        var re = /\d*/i; //\d表示数字,*表示匹配多个数字
        var r = s.match(re);
        return (r==s)?true:false;
    }
    return true;
}

//去空格
function trim(str)
{
    return str.replace(/(^\s*)|(\s*$)/g,"");
}

//组装待机开关下拉框
function getAutoSleepModeSelect(type)
{
	var str="<select name='auto_sleep_mode'><option value='-1' >请选择</option>";
	if(type=="0"){
		str+="<option value='1'>开</option><option value='0' selected>关</option></select>";
	}else if(type=="1"){
		str+="<option value='1' selected>开</option><option value='0'>关</option></select>";
	}else{
		str+="<option value='1'>开</option><option value='0'>关</option></select>";
	}
	return str;
}

//组装待机时长下来框
function getAutoSleepTimeSelect(type)
{
	var str="<select name='auto_sleep_time'><option value='-1'>请选择</option>";
	if(type=="3600"){
		str+="<option value='3600' selected>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}else if(type=="1800"){
		str+="<option value='3600'>1小时</option><option value='1800' selected>30分钟</option>"
				+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
				+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
				+"<option value='30'>30秒</option></select>";
	}else if(type=="600"){
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600' selected>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}else if(type=="300"){
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300' selected>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}else if(type=="180"){
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180' selected>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}else if(type=="60"){
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60' selected>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}else if(type=="30"){
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30' selected>30秒</option></select>";
	}else{
		str+="<option value='3600'>1小时</option><option value='1800'>30分钟</option>"
			+"<option value='600'>10分钟</option><option value='300'>5分钟</option>"
			+"<option value='180'>3分钟</option><option value='60'>1分钟</option>"
			+"<option value='30'>30秒</option></select>";
	}
	return str;
}

//组装网络类型下来框
function getProtocolVersionSelect(selectName,type)
{
	var str="<select name='"+selectName+"'><option value='-1'>请选择</option>";
	if(type=="1"){
		str+="<option value='1' selected>IPv4</option><option value='2'>IPv6</option>"
				+"<option value='3'>IPv4/v6</option></select>";
	}else if(type=="2"){
		str+="<option value='1'>IPv4</option><option value='2' selected>IPv6</option>"
				+"<option value='3'>IPv4/v6</option></select>";
	}else if(type=="3"){
		str+="<option value='1'>IPv4</option><option value='2'>IPv6</option>"
			+"<option value='3' selected>IPv4/v6</option></select>";
	}else{
		str+="<option value='1'>IPv4</option><option value='2'>IPv6</option>"
			+"<option value='3'>IPv4/v6</option></select>";
	}
	return str;
}
</SCRIPT>

<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR>
		<TD>
			<table width="100%" class="querytable">
				<TR>
					<TH colspan="4" class="title_1">零配置下发参数配置修改</TH>
				</TR>
				<TR id="trDeviceResult">
					<td nowrap class="title_2" width="15%">
						设备序列号<input type="hidden" id="sn_h">
					</td>
					<td id="tdDeviceSn" width="35%"></td>
					<td nowrap class="title_2" width="15%">
						属地<input type="hidden" id="city_h">
					</td>
					<td id="tdDeviceCityName" width="35%"></td>
				</TR>
				<TR id="trDeviceVMResult">
					<td nowrap class="title_2" width="15%">设备厂商</td>
					<td id="vendor_name" width="35%"></td>
					<td nowrap class="title_2" width="15%">型号</td>
					<td id="model_name" width="35%"></td>
				</TR>
				<TR id="trDeviceAccountResult">
					<td nowrap class="title_2" width="15%">MAC地址</td>
					<td id="mac" width="35%"></td>
					<td nowrap class="title_2" width="15%">业务账号</td>
					<td id="serv_account" width="35%"></td>
				</TR>
					
				<TR id="trDeviceAuthServer">
					<td nowrap class="title_2" width="15%">主认证服务地址</td>
					<td width="35%">
						<input id="auth_server" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
					<td nowrap class="title_2" width="15%">备认证服务地址</td>
					<td width="35%">
						<input id="auth_server_bak" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
				</TR>
				<TR id="trDeviceAuthServerConnPeroid">
					<td nowrap class="title_2" width="15%">认证服务心跳周期</td>
					<td id="auth_server_conn_peroid" width="35%">
						<input id="auth_server_conn_peroid" size="5" maxlength="5"> 秒
					</td>
					<td nowrap class="title_2" width="15%">命令服务心跳周期</td>
					<td id="cmd_server_conn_peroid" width="35%">
						<input id="cmd_server_conn_peroid" size="5" maxlength="5"> 秒
					</td>
				</TR>
				<TR id="trDeviceZeroConfServer">
					<td nowrap class="title_2" width="15%">主零配服务地址</td>
					<td id="zeroconf_server" width="35%">
						<input id="zeroconf_server" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
					<td nowrap class="title_2" width="15%">备零配服务地址</td>
					<td id="zeroconfig_server_bak" width="35%">
						<input id="zeroconfig_server_bak" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
				</TR>
				<TR id="trDeviceCmdServer">
					<td nowrap class="title_2" width="15%">主命令服务地址</td>
					<td id="cmd_server" width="35%">
						<input id="cmd_server" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
					<td nowrap class="title_2" width="15%">备命令服务地址</td>
					<td id="cmd_server_bak" width="35%">
						<input id="cmd_server_bak" size="40" maxlength="128">
						<font color=red>不能输入'#'</font>
					</td>
				</TR>
				<TR id="trDeviceNetServer">
					<td nowrap class="title_2" width="15%">主时钟同步服务地址</td>
					<td id="ntp_server_main" width="35%">
						<input id="ntp_server_main" size="40" maxlength="16">
					</td>
					<td nowrap class="title_2" width="15%">备时钟同步服务地址</td>
					<td id="ntp_server_bak" width="35%">
						<input id="ntp_server_bak" size="40" maxlength="16">
					</td>
				</TR>
				
				<TR id="trDeviceAutoSleep">
					<td nowrap class="title_2" width="15%">自动待机开关</td>
					<td id="auto_sleep_mode" width="35%"></td>
					<td nowrap class="title_2" width="15%">自动待机关闭时长</td>
					<td id="auto_sleep_time" width="35%"></td>
				</TR>
				<TR id="trDeviceIpProtocolVersion">
					<td nowrap class="title_2" width="15%">有线网络类型</td>
					<td id="ip_protocol_version_lan" width="35%"></td>
					<td nowrap class="title_2" width="15%">无线网络类型</td>
					<td id="ip_protocol_version_wifi" width="35%"></td>
				</TR>
				<TR id="trDeviceUpdateTime">
					<td nowrap class="title_2" width="15%">
						最后修改时间<input type="hidden" id="update_time_h">
					</td>
					<td id="update_time" width="35%"></td>
					<td nowrap class="title_2" width="15%">
						最后被零配置下发生效的时间<input type="hidden" id="set_stb_time_h">
					</td>
					<td id="set_stb_time" width="35%"></td>
				</TR>
				
				<TR id="saveButton" bgcolor="#FFFFFF">
					<TD colspan="4" CLASS="foot">
						<div class="right">
							<button id="save_btn" onclick="save()">保 存</button>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="javascript:window.close();"> 关 闭 </button>
						</div>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
