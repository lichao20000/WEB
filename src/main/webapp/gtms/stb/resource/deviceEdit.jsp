<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒信息编辑</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("select[@name='cityId']").val('<s:property value="deviceDetailMap.city_id"/>');
});

function check_mac(e1)
{
     chkstr=e1;
         var pattern="/^([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}|([0-9A-Fa-f]{2})(:[0-9A-Fa-f]{2}){5}/";
     eval("var pattern=" + pattern);
     var add_p1 = pattern.test(chkstr);
   
     if(add_p1==false)
     {
     alert("你输入的MAC地址不正确。");
     }
     return add_p1;
}


function edit(){
	var deviceId = trim($("input[@name='deviceId']").val());
	var cityId = trim($("select[@name='cityId']").val());
	var cpeMac = trim($("input[@name='cpeMac']").val());
	var deviceIp = trim($("input[@name='deviceIp']").val());
	if(""!=cpeMac){
		$("input[@name='cpeMac']").val(cpeMac);
		if(!check_mac(cpeMac)){
			return false;
		}
	}
	
	if(""==deviceIp){
		alert("请输入IP地址！");
		return false;
	}else{
		if(!checkip(deviceIp)){
			alert("请输入正确的IP地址！");
			return false;
		}else{
			$("input[@name='deviceIp']").val(deviceIp);
		}
	}
	var url = '<s:url value="/gtms/stb/resource/gwDeviceQueryStb!edit.action"/>';
	$.post(url,{
		deviceId:deviceId,
		cityId:cityId,
		cpeMac:cpeMac,
		deviceIp:deviceIp
	},function(ajax){
	    alert(ajax);
	    window.close();
	});
}


/*------------------------------------------------------------------------------
//函数名:		checkip
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数判断是否为合法的IP地址
//返回值:		true false
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function checkip(str){
	var pattern=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	return pattern.test(str);
}

/*------------------------------------------------------------------------------
//函数名:		trim
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数进行去掉左右的空格
//返回值:		trim（str）
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}

</SCRIPT>

</head>
<body>
<form name="selectForm"
		action="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!edit.action"/>">
		<input type="hidden" name="deviceId" value="<s:property value="deviceDetailMap.device_id"/>"/>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">设备〖<s:property value="deviceDetailMap.device_serialnumber"/>〗编辑</TH>
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
		<TD><input type="text" name="cpeMac" value="<s:property value="deviceDetailMap.cpe_mac"/>" maxlength="17"/> </TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=4>设备动态信息</TD>
	</TR>
	<TR >
		<TD class="title_2" >设备在线状态</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR>

	<TR >
		<TD class="title_2" >设备属地</TD>
		<TD>
			<select name="cityId" >
				<s:iterator value="cityList">
					<option value="<s:property value="city_id"/>">
						<s:property value="city_name"/>
					</option>
				</s:iterator>
			</select>
		</TD>
		<TD class="title_2" >IP地址</TD>
		<TD><input type="text" name="deviceIp" value="<s:property value="deviceDetailMap.loopback_ip"/>" maxlength="15"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >注册系统时间</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<TD class="title_2" >最近连接时间</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
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
	<TR >
		<TD class="title_3" colspan=4>用户基本信息</TD>
	</TR>
	<TR >
		<TD class="title_2" >用户帐号</TD>
		<TD><s:property value="deviceDetailMap.cust_account"/></TD>
		<TD class="title_2" >客户名称</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:edit();"> 提 交 </button>
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</TABLE>
</form>
</body>