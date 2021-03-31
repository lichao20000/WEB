<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" src="Js/rightMenu.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
@import url(../css/css_ico.css);
body {
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
}
a{margin-bottom:-1px;}
.style2 {	font-size: 10pt;
	font-weight: bold;
}
.style1 {
	font-size: 10pt;
	font-weight: bold;
}
-->
</style>
<html>
<body onclick="parent.document_click()">
<table width=100% style="margin-left: 5px;margin-top: 10px;">
	<tr>
		<td width="50%">
		<table width="97%" border="1" cellpadding="0" cellspacing="0"
			class="table">
			<tr class="tab_title">
				<td colspan="5" class="title_white"><s:property
					value="deviceInfoMap.device_name" /></td>
			</tr>
			<tr class="tr_white">
				<td class="tr_yellow" >设备 IP</td>
				<td class="text" colspan=4><s:property value="deviceInfoMap.loopback_ip" /></td>
			</tr>
			<tr class="tr_glory">
				<td class="tr_yellow">设备型号</td>
				<td class="text" colspan=4><s:property value="deviceInfoMap.device_model" /></td>
			</tr>
			<tr class="trOver_blue">
                <td class="tr_yellow"> 运行概况</td>
                <td class="text">CPU</td>
                <td class="text">内存</td>
                <td class="text">连接数</td>
                <td class ="text">网络时延</td>
            </tr>
			<tr class="tr_glory">
			    <td class="tr_yellow">&nbsp;</td>
				<td class="tr_green">
				    <s:if test="deviceInfoMap.cpu_util!=null">
				        <s:url var="link" value="/securitygw/SgwPerformance.action">
							<s:param name="device_id" value="device_id"></s:param>
							<s:param name="desc" value="deviceInfoMap.desc"></s:param>
							<s:param name="class1" value="1"></s:param>
							<s:param name="time" value="time"></s:param>
				       	</s:url>
				 		<s:a href="%{link}">
				      		<s:property value="deviceInfoMap.cpu_util" />%
				      	</s:a>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td class="tr_green">
					<s:if test="deviceInfoMap.mem_util!=null">
					     <s:url var="link" value="/securitygw/SgwPerformance.action">
							<s:param name="device_id" value="device_id"></s:param>
							<s:param name="desc" value="deviceInfoMap.desc"></s:param>
							<s:param name="class1" value="2"></s:param>
							<s:param name="time" value="time"></s:param>
				       	</s:url>
				 		<s:a href="%{link}">
						<s:property value="deviceInfoMap.mem_util" />%
						</s:a>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td class="tr_green">
					<s:if test="deviceInfoMap.connection_util!=null">
					    <s:url var="link" value="/securitygw/SgwPerformance.action">
							<s:param name="device_id" value="device_id"></s:param>
							<s:param name="desc" value="deviceInfoMap.desc"></s:param>
							<s:param name="class1" value="8"></s:param>
							<s:param name="time" value="time"></s:param>
				       	</s:url>
				 		<s:a href="%{link}">
							<s:property value="deviceInfoMap.connection_util" />个

						</s:a>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
				<td class="tr_green">
					<s:if test="deviceInfoMap.ping_value!=null">
					    <s:url var="link" value="/securitygw/SgwPerformance.action">
							<s:param name="device_id" value="device_id"></s:param>
							<s:param name="desc" value="deviceInfoMap.desc"></s:param>
							<s:param name="class1" value=""></s:param>
							<s:param name="time" value="time"></s:param>
				       	</s:url>
				 		<s:a href="%{link}">
							<s:property value="deviceInfoMap.ping_value" />ms
						</s:a>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
			</tr>
			<tr class="tr_white">
				<td class="tr_yellow">当前状态</td>
				<td class="" colspan="4">
				<table width="89" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<s:if test="deviceInfoMap.severity>=4">
							<td class="back_red"><a class="black" href="#" >Major</a></td>
						</s:if>
						<s:elseif test="deviceInfoMap.severity==3">
							<td class="back_org"><a class="black" href="#" >Minor</a></td>
						</s:elseif>
						<s:elseif test="deviceInfoMap.severity==2">
							<td class="back_yel"><a class="black" href="#" >Warning</a></td>
						</s:elseif>
						<s:else>
							<td class="back_green"><a class="black" href="#">Normal</a></td>
						</s:else>
					</tr>
				</table>
				</td>
			</tr>
			<tr class="tr_glory">
				<td class="tr_yellow">备注</td>
				<td class="text" colspan=4>
				<s:url var="link" value="/securitygw/entSecStat!redirect.action">
						<s:param name="deviceId" value="device_id"></s:param>
						<s:param name="customerName" value="deviceInfoMap.desc"></s:param>
				</s:url>
				<s:a href="%{link}">
				<s:property value="deviceInfoMap.desc" />
				</s:a>
				</td>
			</tr>
		</table>
		</td>
		<td>
		<table width="99%" border="1" cellpadding="0" cellspacing="0"
			class="table">
			<tr class="tab_title">
				<td class="title_white">维护管理</td>
			</tr>
			<tr class="tr_white">
				<td nowrap class="text">
				 <s:url var="link" value="/securitygw/SgwPerformance.action">
						<s:param name="device_id" value="device_id"></s:param>
						<s:param name="desc" value="deviceInfoMap.desc"></s:param>
						<s:param name="class1" value="1"></s:param>
						<s:param name="time" value="time"></s:param>
				 </s:url>
				 <s:a href="%{link}">
				    <img
					src="<s:url value="/model_vip/images/bullet_main_02.gif"/>"
					width="17" height="13" border="0"> 设备CPU性能报表 </s:a></td>
			</tr>
			<tr class="tr_glory">
				<td nowrap class="text">
				<s:url var="link" value="/securitygw/SgwPerformance.action">
						<s:param name="device_id" value="device_id"></s:param>
						<s:param name="desc" value="deviceInfoMap.desc"></s:param>
						<s:param name="class1" value="2"></s:param>
						<s:param name="time" value="time"></s:param>
				 </s:url>
				 <s:a href="%{link}">
				 <img
					src="<s:url value="/model_vip/images/bullet_main_02.gif"/>"
					width="17" height="13" border="0"> 设备内存性能报表 </s:a></td>
			</tr>
			<tr class="tr_white">
				<td nowrap class="text">
				<s:url var="link" value="/securitygw/SgwPerformance.action">
						<s:param name="device_id" value="device_id"></s:param>
						<s:param name="desc" value="deviceInfoMap.desc"></s:param>
						<s:param name="class1" value="8"></s:param>
						<s:param name="time" value="time"></s:param>
				 </s:url>
				 <s:a href="%{link}">
				 <img
					src="<s:url value="/model_vip/images/bullet_main_02.gif"/>"
					width="17" height="13" border="0"> 设备连接数报表 </s:a></td>
			</tr>
			<tr class="tr_glory">
				<td nowrap class="text">
				<s:url var="link" value="/securitygw/SgwPerformance.action">
						<s:param name="device_id" value="device_id"></s:param>
						<s:param name="desc" value="deviceInfoMap.desc"></s:param>
						<s:param name="time" value="time"></s:param>
				 </s:url>
				 <s:a href="%{link}">
				 <img
					src="<s:url value="/model_vip/images/bullet_main_02.gif"/>"
					width="17" height="13" border="0"> 网络时延报表</s:a></td>
			</tr>
			<tr class="tr_white">
				<td nowrap class="text">&nbsp;</td>
			</tr>
			<tr class="tr_white">
				<td nowrap class="text">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr class="tab_title">
		<td colspan="2" class="title_white"><s:if test="class1==1">设备CPU性能报表</s:if><s:elseif
			test="class1==2">设备内存性能报表</s:elseif><s:elseif test="class1==8">设备连接数报表</s:elseif><s:elseif test="class1==null">网络时延报表</s:elseif></td>
	</tr>
	<!-- 性能数据的展示 -->
	<s:if test="class1!=null">
	<tr>
		<td colspan=2>
		<div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr class="trOver_blue">
				<td class="text" align=center>日报表</td>
				<td width="6%"><a href="#"><img
					src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
					border="0">TOP</a></td>
			</tr>
			<tr>
				<td colspan=2 align=center><img id="day"
					src="<s:url value="/securitygw/SgwPerformance!getPerformanceChart.action"><s:param name="device_id" value="device_id"/><s:param name="class1" value="class1"/><s:param name="reportType" value="1"/><s:param name="time" value="time"/></s:url>" />
				</td>
		</table>
		</div>
		<div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr class="trOver_blue">
				<td class="text" align=center>周报表</td>
				<td width="6%"><a href="#"><img
					src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
					border="0">TOP</a></td>
			</tr>
			<tr>
				<td colspan=2 align=center><img id="week"
					src="<s:url value="/securitygw/SgwPerformance!getPerformanceChart.action"><s:param name="device_id" value="device_id"/><s:param name="class1" value="class1"/><s:param name="reportType" value="2"/></s:url>" />
				</td>
			</tr>
		</table>
		</div>
		<div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr class="trOver_blue">
				<td class="text" align=center>月报表</td>
				<td width="6%"><a href="#"><img
					src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
					border="0">TOP</a></td>
			</tr>
			<tr>
				<td colspan=2 align=center><img id="month"
					src="<s:url value="/securitygw/SgwPerformance!getPerformanceChart.action"><s:param name="device_id" value="device_id"/><s:param name="class1" value="class1"/><s:param name="reportType" value="3"/></s:url>" />
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	</s:if>
	<!-- 网络时延的展示 -->
	<s:else>
	<tr>
	<td colspan=2>
		<table width="99.5%" border="1" cellpadding="0" cellspacing="0"	class="table" style="margin-top: 10px">
				<tr class="trOver_blue">
					<td colspan="12" class="text" align=center>
						<b>设备<s:property value="deviceInfoMap.device_name"/>(<s:property value="deviceInfoMap.loopback_ip"/>)当天时延达标统计</b>
					</td>
				</tr>
				<tr class="tr_yellow">
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="PACK_SIZE"></s:text> <br>
					（<s:text name="BYTE"></s:text>）</td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="SEND_PACKNUM1"></s:text> <br>
					（<s:text name="P_BAO_NUM"></s:text>）</td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="ACCEPT_PACKNUM1"></s:text> <br>
					（<s:text name="P_BAO_NUM"></s:text>）</td>
					<td colspan="4" class="tr_yellow text" nowrap >
					<s:text name="TIMELATER_ACCEPTPNUM"></s:text></td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="AVG_TIME_LATER"></s:text> <br>
					（ms）</td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="MAX_TIME_LATER"></s:text> <br>
					（ms）</td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="MIN_TIME_LATER"></s:text> <br>
					（ms）</td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="TIME_LATER_MAKE"></s:text></td>
					<td rowspan="2" class="tr_yellow text" nowrap >
					<s:text name="TIME_LATER"></s:text><s:text name="LOSE_PACK_RATE"></s:text>
					</td>
				</tr>
				<tr class="tr_yellow">
					<td class="tr_yellow text">
					Pn1</td>
					<td class="tr_yellow text">
					Pn2</td>
					<td class="tr_yellow text">
					Pn3</td>
					<td class="tr_yellow text">
					Pn4</td>
				</tr>
				<s:iterator value="netDelayList">
					<tr class="tr_white">
						<s:iterator>
							<td class="text"><s:property /></td>
						</s:iterator>
					</tr>
				</s:iterator>
				<tr class="tr_white">
					<td colspan="12" nowrap class="text">
						<span style="width=5%"><s:text name="REMARK"></s:text>：</span>
						<span style="width=45%">Pn1：<s:text name="TIME_LATER"></s:text>≤<s:text name="ACCEPT_VALUE"></s:text>(<s:text name="TIMELATER_ACCEPTPNUM"></s:text>)</span>
						<span style="width=50%">Pn2：<s:text name="ACCEPT_VALUE"></s:text><<s:text name="TIME_LATER"></s:text>≤<s:text name="ACCEPT_VALUE"></s:text>150%</span>
						<br>
						<span style="width=5%">&nbsp;</span>
						<span style="width=45%">&nbsp;&nbsp;Pn3：<s:text name="ACCEPT_VALUE"></s:text>150%<<s:text name="TIME_LATER"></s:text>≤<s:text name="ACCEPT_VALUE"></s:text>300%</span>
						<span style="width=50%">&nbsp;Pn4：<s:text name="ACCEPT_VALUE"></s:text>300%<<s:text name="TIME_LATER"></s:text></span>
					</td>
				</tr>
				<tr>
				<td colspan="12" align=center>
				<img src="<s:url value="/Report/NetDelayChartAction!netDelay.action"><s:param name="device_ip" value="deviceInfoMap.loopback_ip"/></s:url>"/>
				</td>
				</tr>
		</table>

		</td>
		</tr>
	</s:else>
</table>
</body>
</html>
