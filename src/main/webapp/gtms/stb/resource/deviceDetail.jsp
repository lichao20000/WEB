<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒详细信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">设备〖<s:property value="deviceDetailMap.device_serialnumber"/>〗详细信息</TH>
	</TR>
	<TR height="20">
		<TD class="title_3" colspan=4>设备基本信息</TD>
	</TR>
	<TR >
		<TD class="title_2" >设备ID</TD>
		<TD width="25%"><s:property value="deviceDetailMap.device_id"/></TD>
		<TD class="title_2" >设备型号</TD>
		<TD width="40%"><s:property value="deviceDetailMap.device_model"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >设备厂商</TD>
		<TD><s:property value="deviceDetailMap.vendor_add"/></TD>
		<TD class="title_2" >序列号</TD>
		<TD><s:property value="deviceDetailMap.device_serialnumber"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >硬件版本</TD>
		<TD><s:property value="deviceDetailMap.hardwareversion"/></TD>
		<TD class="title_2" >特别版本</TD>
		<TD><s:property value="deviceDetailMap.specversion"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >软件版本</TD>
		<TD><s:property value="deviceDetailMap.softwareversion"/></TD>
		<TD class="title_2" >设备类型</TD>
		<TD><s:property value="deviceDetailMap.device_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >最大包数</TD>
		<TD width=140><s:property value="deviceDetailMap.maxenvelopes"/></TD>
		<TD class="title_2" >MAC 地址</TD>
		<TD><s:property value="deviceDetailMap.cpe_mac"/></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>设备动态信息</TD>
	</TR>
	<%-- <TR >
		<TD class="title_2" >设备在线状态</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR> --%>
	<TR >
		<TD class="title_2" >设备在线状态</TD>
		<TD colspan=3>
			<span id='onlineStatus'><s:property value="deviceDetailMap.online_status"/></span>&nbsp;&nbsp;&nbsp;
			<button name="onlineStatusGet" type="button" onclick="getOnlineStatus()" >检测在线状态</button>
		</TD>
	</TR>

	<TR >
		<TD class="title_2" >设备属地</TD>
		<TD><s:property value="deviceDetailMap.city_name"/></TD>
		<TD class="title_2" >IP地址</TD>
		<TD><s:property value="deviceDetailMap.loopback_ip"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >注册系统时间</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<TD class="title_2" >最近连接时间</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
	<%-- 
	<TR >
		<TD class="title_3"  colspan="4">当前配置参数</TD>
	</TR>
	<TR >
		<TD class="title_2" >CPE用户名</TD>
		<TD><s:property value="deviceDetailMap.cpe_username"/></TD>
		<TD class="title_2" >CPE密码</TD>
		<TD><s:property value="deviceDetailMap.cpe_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >ACS用户名</TD>
		<TD><s:property value="deviceDetailMap.acs_username"/></TD>
		<TD class="title_2" >ACS密码</TD>
		<TD><s:property value="deviceDetailMap.acs_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >电信维护账号</TD>
		<TD><s:property value="deviceDetailMap.x_com_username"/></TD>
		<TD class="title_2" >电信维护密码</TD>
		<TD><s:property value="deviceDetailMap.x_com_passwd"/></TD>
	</TR>
	 --%>
	<TR >
		<TD class="title_3"  colspan="4">机顶盒历史配置信息</TD>
	</TR>
	<TR >
		<TD class="title_2" >采集时间</TD>
		<TD><s:property value="deviceDetailMap.time"/></TD>
		<TD class="title_2" >接入方式</TD>
		<TD><s:property value="deviceDetailMap.addressingType"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >PPPOE帐号</TD>
		<TD><s:property value="deviceDetailMap.PPPOE"/></TD>
		<TD class="title_2" >PPPOE密码</TD>
		<TD><s:property value="deviceDetailMap.PPPoEPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >DHCP账号</TD>
		<TD><s:property value="deviceDetailMap.DHCPID"/></TD>
		<TD class="title_2" >DHCP密码</TD>
		<TD><s:property value="deviceDetailMap.DHCPPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >业务账号</TD>
		<TD><s:property value="deviceDetailMap.userID"/></TD>
		<TD class="title_2" >业务密码</TD>
		<TD><s:property value="deviceDetailMap.userPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >认证服务器地址</TD>
		<TD><s:property value="deviceDetailMap.authURL"/></TD>
		<TD class="title_2" >更新服务器地址</TD>
		<TD><s:property value="deviceDetailMap.autoUpdateServer"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >屏显模式</TD>
		<TD><s:property value="deviceDetailMap.aspectRatio"/></TD>
		<TD class="title_2" >视频输出制式</TD>
		<TD><s:property value="deviceDetailMap.compositeVideo"/></TD>
	</TR>
	<TR >
		<TD class="title_3" colspan=4>用户基本信息</TD>
	</TR>
	<TR >
		<TD class="title_2" >业务帐号</TD>
		<TD><s:property value="deviceDetailMap.serv_account"/></TD>
		<TD class="title_2" >客户名称</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
	<input type="hidden" id="device_id" value="<s:property value='deviceDetailMap.device_id'/>" />
</TABLE>
<script type="text/javascript">
function getOnlineStatus()
{
	//$("input[@name='time_start']")
	$("span#onlineStatus").html("<font color='blue'>正在获取设备在线状态</font>");
	
	var device_id = document.getElementById("device_id").value;
	var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
	var result = "";
	
	$.post(url, {
		deviceId : device_id
	}, function(ajax) {
		var flag = parseInt(ajax);
		if(flag == 1){
			result = "<font color='green'>设备在线(实时)</font>";
		}else if (flag == 0){
			result = "<font color='red'>设备下线(实时)</font>";
		}
		else if (flag == -1){
			result = "<font color='red'>设备下线(实时)</font>";
		}
		else if (flag == -2){
			result = "<font color='red'>设备下线(实时)</font>";
		}
		else if (flag == -3){
			result = "<font color='green'>设备在线(实时)</font>";
		}
		else if (flag == -4){
			result = "<font color='red'>设备下线(实时)</font>";
		}
		else {
			result = "<font color='red'>设备下线(实时)</font>";
		}
		$("span#onlineStatus").html(result);
	});
	
}

</script>
</body>