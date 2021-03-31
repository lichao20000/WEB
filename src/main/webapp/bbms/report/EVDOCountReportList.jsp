<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO网关统计</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">


</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr >
			<td class=column1 height="25">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" >
					<tr align="center" >
						<td>
							<strong>
								EVDO网关统计
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
					<tr bgcolor="#FFFFFF">
						<th>属地</th>
						<th>设备总数</th>
						<th>EVDO总数</th>
						<th>备用链路总数</th>
						<th>主链路总数</th>
						<th>备用链路总数/EVDO总数</th>
						<th>主链路总数/EVDO总数</th>
						<th>EVDO总数/设备总数</th>
					</tr>
					<s:iterator value="reportResult">
						<tr bgcolor="#FFFFFF">
							<td class=column>
								<s:if test='"true".equals(haschild)'>
									<a href="javascript:queryData('<s:property value="city_id"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
								 		<s:property value="city_name"/>
								 	</a>
								 </s:if>
								 <s:else>
								 	<s:property value="city_name"/>
								 </s:else>
							</td>
							<td  bgcolor=#ffffff align=center><s:property value="device_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="evdo_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="standby_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="main_count"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="standby_evdo"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="main_evdo"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="evdo_device"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForPrint('<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
					 			<img src="../../images/print.gif" width="16"  border="0" height="16"></img>
						 	</a>
						 	&nbsp;
						 	<a href="javascript:queryDataForExcel('<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
					 			<img src="../../images/excel.gif" width="16"  border="0" height="16"></img>
						 	</a>
						 	&nbsp;
							<a href="javascript:queryDataForPdf('<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
					 			<img src="../../images/pdf.gif" width="16"  border="0" height="16"></img>
						 	</a>
						</td>
						<td class=column1  colspan="12" align="right">
							<strong>
								统计日期：<s:property value="startTime"/>————<s:property value="endTime"/>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>