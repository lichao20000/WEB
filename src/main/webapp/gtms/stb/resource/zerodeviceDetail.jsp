<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置机顶盒详细信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>
</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="5" class="title_1">设备〖<s:property value="deviceDetailMap.device_serialnumber"/>〗详细信息</TH>
	</TR>
	<TR height="20">
		<TD class="title_3" colspan=5>设备基本信息</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>设备ID</TD>
		<TD width="25%"><s:property value="deviceDetailMap.device_id"/></TD>
		<TD class="title_2" >设备型号</TD>
		<TD width="40%"><s:property value="deviceDetailMap.device_model"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>设备厂商(OUI)</TD>
		<TD><s:property value="deviceDetailMap.vendor_add"/>(<s:property value="deviceDetailMap.oui"/>)</TD>
		<TD class="title_2" >序列号</TD>
		<TD><s:property value="deviceDetailMap.device_serialnumber"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>硬件版本</TD>
		<TD><s:property value="deviceDetailMap.hardwareversion"/></TD>
		<TD class="title_2" >特别版本</TD>
		<TD><s:property value="deviceDetailMap.specversion"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>软件版本</TD>
		<TD><s:property value="deviceDetailMap.softwareversion"/></TD>
		<TD class="title_2" >设备类型</TD>
		<TD><s:property value="deviceDetailMap.device_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>最大包数</TD>
		<TD width=140><s:property value="deviceDetailMap.maxenvelopes"/></TD>
		<TD class="title_2" >MAC 地址</TD>
		<TD><s:property value="deviceDetailMap.cpe_mac"/></TD>
	</TR>
	<TR >
		<TD class="title_3"  colspan=5>设备动态信息</TD>
	</TR>
	<!-- <TR >
		<TD class="title_2" >设备在线状态</TD>
		<TD ><s:property value="deviceDetailMap.online_status"/></TD>
		<TD class="title_2" ></TD>
		<TD ></TD>
	</TR> -->
	<TR >
		<TD class="title_2" colspan=2>设备在线状态</TD>
		<TD>
			<span id='onlineStatus'><s:property value="deviceDetailMap.online_status"/></span>&nbsp;&nbsp;&nbsp;
			<button name="onlineStatusGet" type="button" onclick="getOnlineStatus(<s:property value='gw_type'/>)" >检测在线状态</button>
		</TD>
		<!--
		<TD class="title_2" >设备状态</TD>
		<TD><s:property value="deviceDetailMap.status"/></TD> -->
		<TD class="title_2" >最近连接时间</TD>
		<TD><s:property value="deviceDetailMap.last_time"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>设备属地</TD>
		<TD><s:property value="deviceDetailMap.city_name"/></TD>
		<TD class="title_2" >IP地址</TD>
		<TD><s:property value="deviceDetailMap.loopback_ip"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>注册系统时间</TD>
		<TD><s:property value="deviceDetailMap.complete_time"/></TD>
		<s:if test="%{instAreaName=='xj_dx'}">
			<TD class="title_2" >机顶盒资产</TD>
			<TD><s:property value="deviceDetailMap.isTelDev"/></TD>
		</s:if>
		<s:if test="%{instAreaName=='jx_dx'}">
			<TD class="title_2" >IPV6地址</TD>
			<TD><s:property value="deviceDetailMap.loopback_ip_six"/></TD>
		</s:if>
		<s:else>
			<TD class="title_2" ></TD>
			<TD></TD>
		</s:else>
	</TR>
	<TR >
		<TD class="title_3"  colspan="5">当前配置参数</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>CPE用户名</TD>
		<TD><s:property value="deviceDetailMap.cpe_username"/></TD>
		<TD class="title_2" >CPE密码</TD>
		<TD><s:property value="deviceDetailMap.cpe_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>ACS用户名</TD>
		<TD><s:property value="deviceDetailMap.acs_username"/></TD>
		<TD class="title_2" >ACS密码</TD>
		<TD><s:property value="deviceDetailMap.acs_passwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2><% if("CUC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){ out.print("联通");}else if("CTC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){out.print("电信");}else{out.print("移动");}%>维护账号</TD>
		<TD><s:property value="deviceDetailMap.x_com_username"/></TD>
		<TD class="title_2" ><% if("CUC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){ out.print("联通");}else if("CTC".equalsIgnoreCase(LipossGlobals.getLipossProperty("telecom"))){out.print("电信");}else{out.print("移动");}%>维护密码</TD>
		<TD><s:property value="deviceDetailMap.x_com_passwd"/></TD>
	</TR>
	<%--
	<TR >
		<TD class="title_3"  colspan="5">机顶盒历史配置信息</TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>采集时间</TD>
		<TD><s:property value="deviceDetailMap.time"/></TD>
		<TD class="title_2" >接入方式</TD>
		<TD><s:property value="deviceDetailMap.addressingType"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>PPPOE帐号</TD>
		<TD><s:property value="deviceDetailMap.PPPOE"/></TD>
		<TD class="title_2" >PPPOE密码</TD>
		<TD><s:property value="deviceDetailMap.PPPoEPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>DHCP账号</TD>
		<TD><s:property value="deviceDetailMap.DHCPID"/></TD>
		<TD class="title_2" >DHCP密码</TD>
		<TD><s:property value="deviceDetailMap.DHCPPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>业务账号</TD>
		<TD><s:property value="deviceDetailMap.userID"/></TD>
		<TD class="title_2" >业务密码</TD>
		<TD><s:property value="deviceDetailMap.userPassword"/></TD>
	</TR>
	<TR >
		<TD class="title_2"  colspan=2>认证服务器地址</TD>
		<TD><s:property value="deviceDetailMap.authURL"/></TD>
		<TD class="title_2" >更新服务器地址</TD>
		<TD><s:property value="deviceDetailMap.autoUpdateServer"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>屏显模式</TD>
		<TD><s:property value="deviceDetailMap.aspectRatio"/></TD>
		<TD class="title_2" >视频输出制式</TD>
		<TD><s:property value="deviceDetailMap.compositeVideo"/></TD>
	</TR>--%>
	<TR >
		<TD class="title_3" colspan=5>用户基本信息</TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>业务帐号</TD>
		<TD><s:property value="deviceDetailMap.serv_account"/></TD>
		<TD class="title_2" >客户名称</TD>
		<TD><s:property value="deviceDetailMap.cust_name"/></TD>
	</TR>
	<TR >
		<TD class="title_2" colspan=2>接入类型</TD>
		<TD colspan=3><s:property value="deviceDetailMap.addressing_type"/></TD>
	</TR>

	<s:if test="instAreaName=='sd_lt'">
		<tr colspan=5>
			<table width="98%" class="querytable" align="center" >
				<TR height="20">
					<TD class="title_3" colspan=7>工单详情</TD>
				</TR>
				<TR>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV宽带接入帐号</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">IPTV账号</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">浏览器默认域名1</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">浏览器默认域名2</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">MAC地址</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">STB业务接入方式</TD>
					<TD style="background-color:#F4F4FF;color:#000000;text-align:center;">LOID</TD>
				</TR>
				<s:if test="zerogwInfoList.size>0">
					<s:iterator var="list" value="zerogwInfoList">
						<TR>
							<td style="text-align:center;"><s:property value="#list.pppoe_user" /></td>
							<td style="text-align:center;"><s:property value="#list.serv_account" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url1" /></td>
							<td style="text-align:center;"><s:property value="#list.browser_url12" /></td>
							<td style="text-align:center;"><s:property value="#list.cpe_mac" /></td>
							<td style="text-align:center;"><s:property value="#list.addressing_type" /></td>
							<td style="text-align:center;"><s:property value="#list.username" /></td>
						</TR>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=7>
							<div style="text-align: center">查询无数据</div>
						</td>
					</tr>
				</s:else>
			</table>
		</tr>
	</s:if>
	<s:elseif test="instAreaName=='jl_dx'">
	</s:elseif>
	<s:elseif test="instAreaName=='jx_dx'">
	<tr colspan=5>
	<table width="98%" class="querytable" align="center" border=0 cellspacing=0 cellpadding=0>
		<TR height="20">
			<TD class="title_3" colspan=11>零配置配置信息</TD>
		</TR>
		<TR style="background-image: url(../../../images/green_title_bg.jpg);" class="zero_3_table">
		    <td align="center">属地</td>
		    <td align="center">流程编号</td>
		    <td align="center">业务账号</td>
			<td align="center">设备序列号</td>
			<td align="center">开始时间</td>
			<td align="center">结束时间</td>
			<td align="center">业务类型</td>
			<td align="center">绑定方式</td>
			<td align="center">状态</td>
			<td align="center" colspan="2">零配置配置结果</td>
		</TR>
		<s:if test="zerogwInfoList.size>0">
			<s:iterator var="list" value="zerogwInfoList">
				<tr  class="zero_3_table">
				    <td><s:property value="#list.city_name" /></td>
				    <td><s:property value="#list.buss_id" /></td>
				    <td><s:property value="#list.serv_account" /></td>
					<td><s:property value="#list.device_serialnumber" /></td>
					<td><s:property value="#list.start_time" /></td>
					<td><s:property value="#list.fail_time" /></td>
					<td><s:property value="#list.bind_type" /></td>
					<td><s:property value="#list.bind_way" /></td>
					<td><s:property value="#list.fail_reason_id" /></td>
					<td colspan="2"><s:property value="#list.fail_reason_id" />,<s:property value="#list.reason_desc" />(返回值[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr  class="zero_3_table">
				<td colspan="11">
					<div style="text-align: center">查询无数据</div>
				</td>
			</tr>
		</s:else>
		<s:if test="zerogwInfoList.size==3">
			<tr>
				<td colspan="11" align="center">
				    <input type="hidden" name="deviceId" value='<s:property value="deviceId"/>'/>
					<a id="moreZeroConfig" href="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!querygwZeroDetailPage.action?deviceId="/><s:property value="deviceId"/>"
					 target="moreDataForm">查看更多历史配置</a>
					 <a id="backZeroConfig" style="display: none;" href="javascript:void(0)" onclick="backZeroConfig()">收起更多历史配置</a>
				</td>
			</tr>
		</s:if>
	</table>
	</tr>
	<iframe id="moreDataForm" name="moreDataForm" width="98%" frameborder="0" scrolling="no" align="center" style="display: none;"></iframe>
	</s:elseif>
	<s:else>
	<s:if test="instAreaName!='hb_lt' && instAreaName!='sx_lt'">
		<TR height="20">
			<TD class="title_3" colspan=5>零配置配置信息</TD>
		</TR>
		<TR>
			<TD class="title_2">业务帐号</TD>
			<TD class="title_2" colspan="2">时间</TD>
			<TD class="title_2" colspan="2">零配置配置结果</TD>
		</TR>
		<s:if test="zerogwInfoList.size>0">
			<s:iterator var="list" value="zerogwInfoList">
				<tr>
					<td><s:property value="#list.serv_account" /></td>
					<td colspan="2"><s:property value="#list.fail_time" /></td>
					<td colspan="2"><s:property value="#list.reason_desc" />(返回值[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="5">
					<div style="text-align: center">查询无数据</div>
				</td>
			</tr>
		</s:else>
	</s:if>
	</s:else>
	<TR>
		<TD colspan="5" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
	<input type="hidden" id="device_id" value="<s:property value='deviceDetailMap.device_id'/>" />
</TABLE>
<script type="text/javascript">
function getOnlineStatus(gw_type)
{
	//$("input[@name='time_start']")
	$("span#onlineStatus").html("<font color='blue'>正在获取设备在线状态</font>");

	var device_id = document.getElementById("device_id").value;
	var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!testOnlineStatus.action'/>";
	var result = "";

	$.post(url, {
		deviceId:device_id,
		gw_type:gw_type
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

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["moreDataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize()
{
	$(".zero_3_table").css("display","none");
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//如果用户的浏览器是NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
    			//如果用户的浏览器是IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		    tempobj.style.display="block"
		}
	}

	// 隐藏更多历史链接，显示收起更多历史链接
	$("#moreZeroConfig").css("display","none");
	$("#backZeroConfig").css("display","");
}

function backZeroConfig(){
	$("#moreZeroConfig").css("display","");
	$("#backZeroConfig").css("display","none");
	$("#moreDataForm").css("display","none");
	$(".zero_3_table").css("display","");
}
</script>
</body>
