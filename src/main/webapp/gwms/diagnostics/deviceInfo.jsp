<%--
FileName	: deviceInfo.jsp
Date		: 2009年6月25日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<title>设备信息</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	parent.dyniframesize();
});
function EC(leaf){
	
	if("basicInfo"==leaf){
	
		var m_bShow;
		m_bShow = (this.basicInfo.style.display=="none");
		this.basicInfo.style.display = m_bShow?"":"none";
	
		sobj = document.all("imgbasic");
		if(m_bShow) {	
			sobj.src = "../../images/up_enabled.gif";
		}
		else{
			sobj.src = "../../images/down_enabled.gif";
		}
		parent.dyniframesize();
	}else{
		parent.block();
		var m_bShow;
		m_bShow = (this.bussessInfo.style.display=="none");
		this.bussessInfo.style.display = m_bShow?"":"none";
	
		sobj = document.all("imgbussess");
		var gw_type = $("input[@name='gw_type']").val();
		if(m_bShow) {
			sobj.src = "../../images/up_enabled.gif";
			var url = "<s:url value="/gwms/diagnostics/deviceInfo!getAllInfo.action"/>";
			var deviceId = document.frm.deviceId.value;
			var userId = document.frm.user_id.value;
			$.post(url,{
				deviceId:deviceId,
				userId:userId,
				gw_type:gw_type
		    },function(mesg){
				$("div[@id='divAllInfo']").html(mesg);
				parent.unblock();
				parent.dyniframesize();
		    });
		}
		else{
			sobj.src = "../../images/down_enabled.gif";
			parent.unblock();
			parent.dyniframesize();
		}
	}
}

function wideBandNet(){
	var m_bShow;
	m_bShow = (this.trnet.style.display=="none");
	this.trnet.style.display = m_bShow?"":"none";
	sobj = document.all("imgnet");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function cloudBandNet(){
	var m_bShow;
	m_bShow = (this.trnet.style.display=="none");
	this.trcloudnet.style.display = m_bShow?"":"none";
	sobj = document.all("imgcloudnet");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}


function iptvInfo(){
	var m_bShow;
	m_bShow = (this.triptv.style.display=="none");
	this.triptv.style.display = m_bShow?"":"none";
	sobj = document.all("imgiptv");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function voipInfo(){
	var m_bShow;
	m_bShow = (this.trvoip.style.display=="none");
	this.trvoip.style.display = m_bShow?"":"none";
	sobj = document.all("imgvoip");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function wireinfoInfo(){
	var m_bShow;
	m_bShow = (this.trwireinfo.style.display=="none");
	this.trwireinfo.style.display = m_bShow?"":"none";
	sobj = document.all("imgwireinfo");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function ponInfo(){
	var m_bShow;
	m_bShow = (this.trponInfo.style.display=="none");
	this.trponInfo.style.display = m_bShow?"":"none";
	sobj = document.all("imgponInfo");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function tr069Info(){
	var m_bShow;
	m_bShow = (this.trtr069.style.display=="none");
	this.trtr069.style.display = m_bShow?"":"none";
	sobj = document.all("imgtr069");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function lanInfo(){
	var m_bShow;
	m_bShow = (this.trlan.style.display=="none");
	this.trlan.style.display = m_bShow?"":"none";
	sobj = document.all("imglan");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function wlanInfo(){
	var m_bShow;
	m_bShow = (this.trwlan.style.display=="none");
	this.trwlan.style.display = m_bShow?"":"none";
	sobj = document.all("imgwlan");
	if(m_bShow) {
		sobj.src = "../../images/up_enabled.gif";
	}else{
		sobj.src = "../../images/down_enabled.gif";
	}
	parent.dyniframesize();
}

function connectDev(){
	window.open ("deviceInfo/deviceInfo_devStat_wlanDev.jsp", "newwindow", "height=300, width=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}

function detailDevice(deviceId, lanId, lanWlanId){
	var url = "<s:url value='/gwms/diagnostics/deviceInfo!getGwWlanAsso.action?deviceId='/>"+deviceId+"&lanId="+lanId+"&lanWlanId="+lanWlanId;
	window.open(url,"","left=80,top=80,width=650,height=300,resizable=yes,scrollbars=yes");
}

</script>

</head>

<body>
<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" name="gw_type" value='<s:property value="deviceInfoMap.gw_type"/>' />
<input type="hidden" name="deviceId" value="<s:property value="deviceId"/>" />
<input type="hidden" name="user_id" value="<s:property value="deviceInfoMap.user_id"/>" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR >
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" style="background:none;">
						<a href="javascript:EC('basicInfo');" stytle="CURSOR:hand;">
						【基本信息】
						<IMG name="imgbasic" SRC="../../images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<tr id=basicInfo>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=1 width="100%" >
				<tr align="left">
					<td colspan="4" class="green_title_left">用户信息</td>
				</tr>
				<s:if test="deviceInfoMap.gw_type==2">
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">客户名称:</TD>
						<s:if test="deviceInfoMap.customer_name=='N/A' || deviceInfoMap.customer_name=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.customer_name"/></TD>
						</s:else>
						<TD class=column align="right" width="15%">客户属地:</TD>
						<s:if test="deviceInfoMap.city_name=='N/A' || deviceInfoMap.city_name=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.city_name"/></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">联系人:</TD>
						<s:if test="deviceInfoMap.linkman=='N/A' || deviceInfoMap.linkman=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.linkman"/></TD>
						</s:else>
						<TD class=column align="right" width="15%">联系地址:</TD>
						<s:if test="deviceInfoMap.linkphone=='N/A' || deviceInfoMap.linkphone=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.linkphone"/></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">宽带账号:</TD>
						<TD width="35%"><s:property value="deviceInfoMap.username"/></TD>
						<TD class=column align="right" width="15%">DSLAM端口:</TD>
						<s:if test="deviceInfoMap.device_port=='N/A' || deviceInfoMap.device_port=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.device_port"/></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">客户地址:</TD>
						<s:if test="deviceInfoMap.customer_address=='N/A' || deviceInfoMap.customer_address=='null' ">
							<TD width="85%" colspan="3">-</TD>
						</s:if>
						<s:else>
							<TD width="85%" colspan="3"><s:property value="deviceInfoMap.customer_address"/></TD>
						</s:else>
					</TR>
				</s:if>
				<s:else>
					<%-- 屏蔽用户姓名,用户地址，身份证号码，联系人电话
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">用户姓名:</TD>
						<s:if test="deviceInfoMap.linkman=='N/A' || deviceInfoMap.linkman=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.linkman"/></TD>
						</s:else>
						<TD class=column align="right" width="15%">用户住址:</TD>
						<s:if test="deviceInfoMap.linkaddress=='N/A' || deviceInfoMap.linkaddress=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.linkaddress"/></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">身份证号码:</TD>
						<s:if test="deviceInfoMap.credno=='N/A' || deviceInfoMap.credno=='null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.credno"/></TD>
						</s:else>
						<TD class=column align="right" width="15%">联系电话:</TD>
						<s:if test="deviceInfoMap.linkphone=='N/A' || deviceInfoMap.linkphone == 'null' ">
							<TD width="35%">-</TD>
						</s:if>
						<s:else>
							<TD width="35%"><s:property value="deviceInfoMap.linkphone"/></TD>
						</s:else>
					</TR>--%>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">宽带账号:</TD>
						<TD width="35%"><s:property value="deviceInfoMap.kdname"/></TD>
						<TD class=column align="right" width="15%">DSLAM端口:</TD>
						<TD width="35%"><s:property value="deviceInfoMap.device_port"/></TD>
					</TR>
					<TR bgcolor="#FFFFFF"><!-- 增加逻辑ID、公客/商客 -->
						<TD class=column align="right" nowrap width="15%">逻辑ID:</TD>
						<TD width="35%"><s:property value="deviceInfoMap.username"/></TD>
						<TD class=column align="right" width="15%">公客/商客:</TD>
						<TD width="35%">
							<s:if test="deviceInfoMap.username != null">公客</s:if>
							<s:else></s:else>
						</TD>
					</TR>
				</s:else>
				<tr align="left">
					<td colspan="4" class="green_title_left" >终端信息</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">终端厂家:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.vendor_add"/></TD>
					<TD class=column align="right" width="15%">终端型号:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.device_model"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">终端硬件版本:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.hardwareversion"/></TD>
					<TD class=column align="right" width="15%">软件版本:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.softwareversion"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">终端注册状态:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.register"/></TD>
					<TD class=column align="right" width="15%">IP地址:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.loopback_ip"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">终端序列号:</TD>
					<TD width="35%"><s:property value="deviceInfoMap.device_serialnumber"/></TD>
					<TD class=column align="right" width="15%">设备网络侧接口:</TD>
					<TD width="35%">
						<s:if test="deviceInfoMap.access_type==1">
							DSL
						</s:if>
						<s:if test="deviceInfoMap.access_type==2">
							Ethernet
						</s:if>
						<s:if test="deviceInfoMap.access_type==3">
							PON
						</s:if>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">设备类型:</TD>
					<TD width="35%">
						<s:if test="deviceInfoMap.device_type==1">
							标准型e8-B网关							
						</s:if>
						<s:if test="deviceInfoMap.device_type==2">
							普及型e8-B网关							
						</s:if>
						<s:if test="deviceInfoMap.device_type==3">
							标准型e8-C网关							
						</s:if>
						<s:if test="deviceInfoMap.device_type==4">
							AP外置型e8-C网关							
						</s:if>
						<s:if test="deviceInfoMap.device_type==5">
							其他ITMS管理的终端类型						
						</s:if>
					</TD>
					<TD class=column align="right" width="15%">是否机卡分离型:</TD>
					<TD width="35%">
						<s:if test='deviceInfoMap.is_card_apart == "1"'>
							是
						</s:if>
						<s:if test='deviceInfoMap.is_card_apart == "0"'>
							否
						</s:if>
					</TD>
				</TR><TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">设备支持的IP地址类型:</TD>
						<% if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
							<TD width="35%" colspan="1">
							<s:if test="deviceInfoMap.ip_model_type==0">
								IPV4						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==1">
								IPV4+IPV6						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==2">
								DS-Lite						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==3">
								LAFT6						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==4">
								纯IPV6						
							</s:if>
						</TD>
							<TD class=column align="right" nowrap width="15%">是否支持光猫标识:</TD>
							<TD width="35%" colspan="3">
								<s:if test="deviceInfoMap.gigabit_port==1">
									是						
								</s:if>
								<s:else>
									否						
								</s:else>
							</TD>
						<%}else if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
							<TD width="35%">
								<s:if test="deviceInfoMap.ip_model_type==0">
									IPV4						
								</s:if>
								<s:if test="deviceInfoMap.ip_model_type==1">
									IPV4+IPV6						
								</s:if>
								<s:if test="deviceInfoMap.ip_model_type==2">
									DS-Lite						
								</s:if>
								<s:if test="deviceInfoMap.ip_model_type==3">
									LAFT6						
								</s:if>
								<s:if test="deviceInfoMap.ip_model_type==4">
									纯IPV6						
								</s:if>
							</TD>
							<TD class=column align="right" width="15%">是否E8-C百兆改千兆设备:</TD>
							<TD width="35%"><s:property value="deviceInfoMap.is100MTo1000M"/></TD>
						<%}else{%>
						<TD width="35%" colspan="3">
							<s:if test="deviceInfoMap.ip_model_type==0">
								IPV4						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==1">
								IPV4+IPV6						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==2">
								DS-Lite						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==3">
								LAFT6						
							</s:if>
							<s:if test="deviceInfoMap.ip_model_type==4">
								纯IPV6						
							</s:if>
						</TD>
					<%}%>
				</TR>
				
				<!-- GSDX-REQ-ITMS-202000511-LWX-001（终端能力信息故障处理页面补充网关能力信息需求)-已补充建表脚本 -->
				<%if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
				<tr align="left">
					<td colspan="4" class="green_title_left" >网关能力信息</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">光猫类型:</TD>
					<TD width="35%"><s:property value="abilityMap.devicetype"/></TD>
					<TD class=column align="right" width="15%">LAN最大支持速率速率:</TD>
					<TD width="35%"><s:property value="abilityMap.maxspeed"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">LAN口总数量:</TD>
					<TD width="35%"><s:property value="abilityMap.lannum"/></TD>
					<TD class=column align="right" width="15%">WIFI通道数量:</TD>
					<TD width="35%"><s:property value="abilityMap.wifinum"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">百兆LAN口数量:</TD>
					<TD width="35%"><s:property value="abilityMap.flannum"/></TD>
					<TD class=column align="right" width="15%">千兆LAN口数量:</TD>
					<TD width="35%"><s:property value="abilityMap.glannum"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">语言端口数量:</TD>
					<TD width="35%"><s:property value="abilityMap.voipnum"/></TD>
					<TD class=column align="right" width="15%">是否支持拉流测速:</TD>
					<TD width="35%"><s:property value="abilityMap.speedtestdesc"/></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">WIFI频道信息:</TD>
					<TD width="35%"><s:property value="abilityMap.sdfrequencydesc"/></TD>
					<TD class=column align="right" width="15%"></TD>
					<TD width="35%"></TD>
				</TR>
				<%}%>
				
				<!-- 
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="15%">设备下行接口信息:</TD>
					<TD width="35%" colspan=3></TD>
				</TR>
				 -->
			</TABLE>
			<TABLE border=0 cellspacing=1 cellpadding=1 width="100%" style="display: none">
				<TR bgcolor="#FFFFFF">
					<td colspan="6" class="column" align="center" style="font-weight:bold;height:20px">设备下行接口信息:</td>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="10%">WAN口名称:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wan_name"/></TD>
					<TD class=column align="right" width="10%">数量:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wan_num"/></TD>
					<TD class=column align="right" width="10%">处理能力:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wan_can"/></TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="10%">LAN口名称:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.lan_name"/></TD>
					<TD class=column align="right" width="10%">数量:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.lan_num"/></TD>
					<TD class=column align="right" width="10%">处理能力:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.lan_can"/></TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="10%">WLAN口名称:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wlan_name"/></TD>
					<TD class=column align="right" width="10%">数量:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wlan_num"/></TD>
					<TD class=column align="right" width="10%">处理能力:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wlan_can"/></TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="10%">天线类型:</TD>
					<TD width="15%">
						<s:if test="deviceInfoMap.wireless_type==0">
							内置
						</s:if>
						<s:else>
							外置
						</s:else>
					</TD>
					<TD class=column align="right" width="10%">天线根数:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wireless_num"/></TD>
					<TD class=column align="right" width="10%">天线尺寸:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.wireless_size"/></TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" nowrap width="10%">语音口名称:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.voip_name"/></TD>
					<TD class=column align="right" width="10%">数量:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.voip_num"/></TD>
					<TD class=column align="right" width="10%">处理能力:</TD>
					<TD width="15%"><s:property value="deviceInfoMap.voip_can"/></TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%">语音协议:</TD>
					<TD width="15%" colspan=5>
						<s:if test="deviceInfoMap.voip_protocol==0">
							IMS SIP\软交换SIP\H.248
						</s:if>
						<s:if test="deviceInfoMap.voip_protocol==1">
							IMS SIP
						</s:if>
						<s:if test="deviceInfoMap.voip_protocol==2">
							软交换
						</s:if>
						<s:if test="deviceInfoMap.voip_protocol==3">
							H.248
						</s:if>
					</TD>
				</tr>
			</TABLE>
		</td>
	</tr>
	<TR>
		<TD HEIGHT=5>&nbsp;</TD>
	</TR>
	<TR >
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" style="background:none;">
						<a href="javascript:EC('bussessInfo');" stytle="CURSOR:hand;">
						【业务信息】
						<IMG name="imgbussess" SRC="../../images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<tr id=bussessInfo STYLE="display:none">
		<TD>
			<div id="divAllInfo"></div>
		</TD>
	</tr>
</TABLE>
</FORM>
</body>
</html>
