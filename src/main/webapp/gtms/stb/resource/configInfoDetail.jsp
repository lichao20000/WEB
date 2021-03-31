<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
	/**
	 * 
	 *
	 * @author wuchao
	 * @version 1.0
	 * @since 2011-3-23 15:01:27
	 * 
	 * <br>版权：南京联创科技 网管开发部
	 * 
	 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒历史配置信息查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

</head>
<body>
<TABLE width="98%" class="querytable" align="center">
	<TR>
		<TH colspan="4" class="title_1">机顶盒历史配置信息查询</TH>
	</TR>
	<TR>
		<TD class="title_2" width="15%">设备序列号</TD> 
		<TD width="85%" colspan="3"><s:property value="dataDetail.device_serialnumber"/></TD>
	</TR>
	<TR>
		<TD class="title_2" width="15%">设备型号</TD> 
		<TD width="35%"><s:property value="dataDetail.device_model"/></TD>
		<TD class="title_2" width="15%">属地</TD>
		<TD width="35%"><s:property value="dataDetail.city_name"/></TD>
	</TR>
     <TR >
		<TD class="title_2" >设备ip</TD>
		<TD><s:property value="dataDetail.ip"/></TD>
		<TD class="title_2" >子网掩码</TD>
		<TD><s:property value="dataDetail.mask"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >网关</TD>
		<TD><s:property value="dataDetail.gateway"/></TD>
		<TD class="title_2" >DNS服务器</TD>
		<TD><s:property value="dataDetail.dns"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >MAC</TD>
		<TD><s:property value="dataDetail.mac"/></TD>
		<TD class="title_2" >接入方式</TD>
		<TD><s:property value="dataDetail.address_type"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >PPPOE账号</TD>
		<TD width=140><s:property value="dataDetail.pppoe_id"/></TD>
		<TD class="title_2" >PPPOE密码</TD>
		<TD><s:property value="dataDetail.pppoe_pwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >DHCP账号</TD>
		<TD><s:property value="dataDetail.iptv_dhcp_username"/></TD>
		<TD class="title_2" >DHCP密码</TD>
		<TD><s:property value="dataDetail.iptv_dhcp_password"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >业务账号</TD>
		<TD><s:property value="dataDetail.user_id"/></TD>
		<TD class="title_2" >业务密码</TD>
		<TD><s:property value="dataDetail.user_pwd"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >认证服务器地址</TD>
		<TD ><s:property value="dataDetail.auth_url"/></TD>
		<TD class="title_2" >更新服务器地址</TD>
		<TD><s:property value="dataDetail.auto_update_serv"/></TD>
	</TR>
     <TR >
		<TD class="title_2" >网管地址</TD>
		<TD><s:property value="dataDetail.networkAddress"/></TD>
		<TD class="title_2" >屏显模式</TD>
		<TD><s:property value="dataDetail.aspect_ratio"/></TD>
	</TR>
	<TR >
		<TD class="title_2" >视频输出制式</TD>
		<TD><s:property value="dataDetail.composite_video_standard"/></TD>
		<TD class="title_2" >软件版本</TD>
		<TD><s:property value="dataDetail.software_version"/></TD>
	</TR>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</TABLE>

</body>