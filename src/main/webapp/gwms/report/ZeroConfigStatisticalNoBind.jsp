<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ͳ��</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">


</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					������ʧ��ͳ��
				</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<th><strong>�豸���к�</strong></th>
						<th><strong>ע��ʱ��</strong></th>
						<th><strong>����</strong></th>
					</tr>
					<s:iterator value="bindNoDataList">
						<tr bgcolor="#FFFFFF">
							<td><s:property value="device_serialnumber"/></td>
							<td><s:property value="binddate"/></td>
							<td><s:property value="bind_desc"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="right"
					bgcolor="#000000">
					<s:if test='"print".equals(isReport)'>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100">
							 	<a href="javascript:window.print()">��ӡ</a>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100">
							 	<a href="javascript:queryDataForPrint('getNoBindData','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/print.gif" width="16"  border="0" height="16"></img>
							 	</a>
							 	&nbsp;
							 	<s:if test='#session.isReport=="1"'>
							 	<a href="javascript:queryDataForExcel('getNoBindData','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/excel.gif" width="16"  border="0" height="16"></img>
							 	</a>
							 	</s:if>
							 	&nbsp;
								<a href="javascript:queryDataForPdf('getNoBindData','<s:property value="cityId"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
						 			<img src="../../images/pdf.gif" width="16"  border="0" height="16"></img>
							 	</a>
							</td>
						</tr>
					</s:else>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>