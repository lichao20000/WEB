<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO����ͳ��</title>

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
								EVDO����ͳ��
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
						<th>����</th>
						<th>�豸����</th>
						<th>EVDO����</th>
						<th>������·����</th>
						<th>����·����</th>
						<th>������·����/EVDO����</th>
						<th>����·����/EVDO����</th>
						<th>EVDO����/�豸����</th>
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
								ͳ�����ڣ�<s:property value="startTime"/>��������<s:property value="endTime"/>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>