<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>安全设备性能、安全列表</title>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet"
	type="text/css" />
<link href="<s:url value="/model_vip/css/tablecss.css"/>"
	rel="stylesheet" type="text/css" />
<link href="<s:url value="/model_vip/css/css_ico.css"/>"
	rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-size: 12px;
}
-->
</style>
<script type="text/javascript">
function GoPage(p)
{
	if(p=='ser')
	{
	$("#ser").show();
	$("#per").hide();
	$("#serbu").removeClass("button").addClass("buttonover default");
	$("#perbu").removeClass("buttonover default").addClass("button");
	}
	else
	{
	$("#ser").hide();
	$("#per").show();
	$("#perbu").removeClass("button").addClass("buttonover default");
	$("#serbu").removeClass("buttonover default").addClass("button");
	}
}
</script>
</head>
<body>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	
	<tr>
		<td>
		<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite" nowrap>
					安全网关
				</td>
				<td nowrap>
					<input type="radio" value="1" onclick="GoPage('ser')" id="qt1" name="queryType" checked><label for="qt1">安全服务</label>&nbsp;&nbsp;
                       <input type="radio" value="2" onclick="GoPage('per')" id="qt2" name="queryType"><label for="qt2">设备性能</label>&nbsp;&nbsp;
				</td>
			</tr>
		</table>
		</td>
	</tr>
					
</table>
<table width="99%" border="1" cellpadding="0" cellspacing="0"
	class="table" id="ser" >
	<tr class="tab_title">
		<th>用户名称</th>
		<th>设备名称</th>
		<th>设备IP</th>
		<th>设备序列号</th>
		<th>病毒</th>
		<th>垃圾邮件</th>
		<th>攻击事件</th>
		<th>过虑</th>
		<th>健康值</th>
		<s:iterator value="sgwList" status="status">
			<tr
				class="<s:property value="#status.even==true?'tr_white':'tr_glory'"/>">
				<td class="text"><a
					href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>"><s:property
					value="customname" /></a></td>
				<td class="text"><s:property value="device_name" /></td>
				<td class="text"><s:property value="loopback_ip" /></td>
				<td class="text"><s:property value="device_serialnumber" /></td>
				<td class="tr_green"><a class="black" href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=virus"><s:property
					value="virustimes" /></a></td>
				<td class="tr_green"><a class="black" href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=ashmail"><s:property
					value="ashmailtimes" /></a></td>
				<td class="tr_green"><a class="black" href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=attack"><s:property
					value="attacktimes" /></a></td>
				<td class="tr_green"><a class="black" href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=filter"><s:property
					value="filtertimes" /></a></td>
				<td class="text">100%</td>
			</tr>
		</s:iterator>
</table>
<table width="99%" border="1" cellpadding="0" cellspacing="0"
	class="table" id="per" style="display: none;">
	<tr class="tab_title">
		<th>用户名称</th>
		<th>设备名称</th>
		<th>设备IP</th>
		<th>设备序列号</th>
		<th>CPU</th>
		<th>内存</th>
		<th>连接数</th>
		<th>网络时延</th>
		<th>状态</th>
		<s:iterator value="sgwList" status="status">
			<tr
				class="<s:property value="#status.even==true?'tr_white':'tr_glory'"/>">
				<td class="text"><a
					href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>"><s:property
					value="customname" /></a></td>
				<td class="text"><s:property value="device_name" /></td>
				<td class="text"><s:property value="loopback_ip" /></td>
				<td class="text"><s:property value="device_serialnumber" /></td>
				<td class="tr_green"><a class="black"
					href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=cpu"><s:property
					value="(cpu_util==null || cpu_util=='-1.00')?'':cpu_util+'%'" /></a></td>
				<td class="tr_green"><a class="black"
					href="<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id=<s:property value="device_id"/>&custom_name=<s:property value="customname"/>&toUrl=mem"><s:property
					value="(mem_util==null || mem_util=='-1.00')?'':mem_util+'%'" /></a></td>
				<td class="tr_green"><a class="black"
					href="<s:url value="/model_vip/Goto!goSubMenu.action"><s:param name="device_id" value="device_id"/><s:param name="custom_name" value="customname"/><s:param name="toUrl">cons</s:param></s:url>"><s:property
					value="(connection_util==null || connection_util=='-1')?'':connection_util" /></a></td>
				<td class="tr_green"><a class="black" href="<s:url value="/model_vip/Goto!goSubMenu.action"><s:param name="device_id" value="device_id"/><s:param name="custom_name" value="customname"/><s:param name="toUrl">ping</s:param></s:url>">
					<s:property value="(ping_value ==null || ping_value=='-1')?'':ping_value"/></a></td>
				<td class="text"><s:if test="severity>3">
					<img src="<s:url value="/model_vip/images/gaojin_red.gif"/>"
						width="12" height="12" border="0">
				</s:if> <s:elseif test="severity>2">
					<img src="<s:url value="/model_vip/images/gaojin_orange.gif"/>"
						width="12" height="12" border="0" />
				</s:elseif> <s:elseif test="severity>1">
					<img src="<s:url value="/model_vip/images/gaojin_yellow.gif"/>"
						width="12" height="12" border="0" />
				</s:elseif> <s:else>
					<img src="<s:url value="/model_vip/images/gaojin_green.gif"/>"
						width="12" height="12" border="0" />
				</s:else></td>
			</tr>
		</s:iterator>
</table>
</td>
</tr>
<tr>
	<td></td>
	<td height="8"></td>
</tr>
</table>
</body>
</html>