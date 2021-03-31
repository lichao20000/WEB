<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<body>
<TABLE width="98%" class="querytable" align="center">
	<s:if test="resultList!=null && resultList.size()>0">
		<TR>
			<TH colspan="4" class="title_1">设备基本信息</TH>
		</TR>
		<TR>
			<td nowrap class="title_2" width="15%">设备序列号</td>
			<td width="35%">
				<s:property value="resultList.get(0).device_serialnumber" />
			</td>
			<td nowrap class="title_2" width="15%">属地</td>
			<td width="35%">
				<s:property value="resultList.get(0).city_name" />
			</td>
		</TR>
		<TR>
			<td nowrap class="title_2" width="15%">设备厂商</td>
			<td width="35%">
				<s:property value="resultList.get(0).vendor_name" /></td>
			<td nowrap class="title_2" width="15%">型号</td>
			<td width="35%">
				<s:property value="resultList.get(0).device_model" /></td>
		</TR>
		<TR>
			<td nowrap class="title_2" width="15%">MAC地址</td>
			<td width="35%">
				<s:property value="resultList.get(0).cpe_mac" /></td>
			<td nowrap class="title_2" width="15%">业务账号</td>
			<td width="35%">
				<s:property value="resultList.get(0).serv_account" /></td>
		</TR>
		<TR>
			<td nowrap class="title_2" width="15%">硬件版本</td>
			<td width="35%">
				<s:property value="resultList.get(0).hardwareversion" /></td>
			<td nowrap class="title_2" width="15%">软件版本</td>
			<td width="35%">
				<s:property value="resultList.get(0).softwareversion" /></td>
		</TR>
	</s:if>
</TABLE>
</br>
<ms:inArea areaCode="hn_lt" notInMode="true">
<TABLE width="98%" class="listtable" align="center">
	<thead>
		<TR>
			<TH colspan="6" class="title_1">版本升级结果</TH>
		</TR>
		<tr bgcolor="#FFFFFF">
			<th align="center">旧软件版本</th>
			<th align="center">目标软件版本</th>
			<th align="center">开始时间</th>
			<th align="center">结束时间</th>
			<th align="center">升级路径</th>
			<th align="center">升级版本名</th>
			<th align="center">升级结果</th>
		</tr>
	</thead>
	<tbody>
	<s:if test="resultList!=null && resultList.size()>0">
		<s:iterator value="resultList">
			<TR>
				<td><s:property value="softwareversion_old" /></td>
				<td><s:property value="softwareversion_new" /></td>
				<td><s:property value="start_time" /></td>
				<td><s:property value="end_time" /></td>
				<td><s:property value="version_path" /></td>
				<td><s:property value="version_name" /></td>
				<td><s:property value="result" /></td>
			</TR>
		</s:iterator>
	</s:if>
	<s:else>
			<tr>
				<td colspan="8"><font color="red">此设备没有升级记录</font></td>
			</tr>
	</s:else>
	</tbody>
</TABLE>
</ms:inArea>

<ms:inArea areaCode="hn_lt" notInMode="false">
<TABLE width="98%" class="listtable" align="center">
	<thead>
		<TR>
			<TH colspan="6" class="title_1">版本升级结果</TH>
		</TR>
		<tr bgcolor="#FFFFFF">
			<th align="center" width="15%">升级版本名</th>
			<th align="center" width="10%">策略定制时间</th>
			<th align="center" width="10%">开始升级时间</th>
			<th align="center" width="10%">升级结束时间</th>
			<th align="center" width="30%">版本路径</th>
			<th align="center" width="10%">升级结果</th>
		</tr>
	</thead>
	<tbody>
	<s:if test="resultList!=null && resultList.size()>0">
		<s:iterator value="resultList">
			<TR>
				<td align="center"><s:property value="version_name" /></td>
				<td align="center"><s:property value="time" /></td>
				<td align="center"><s:property value="start_time" /></td>
				<td align="center"><s:property value="end_time" /></td>
				<td align="center"><s:property value="version_path" /></td>
				<td align="center"><s:property value="result" /></td>
			</TR>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="6"><font color="red">此设备没有升级记录</font></td>
		</tr>
	</s:else>
	</tbody>
</TABLE>
</ms:inArea>
</body>
</html>