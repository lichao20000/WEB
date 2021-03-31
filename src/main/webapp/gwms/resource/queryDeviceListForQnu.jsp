<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT type="text/javascript">
	$(function() {
		parent.document.frm.button.disabled = false;
		parent.dyniframesize();
	})

</SCRIPT>

</head>
<body>
	<table width="100%" border=0 cellspacing=1 cellpadding=2
		bgcolor=#999999 id=userTable>
		<tr>
			<td class="green_title" align='center' width="10%">属地</td>
			<td class="green_title" align='center' width="10%">厂商</td>
			<td class="green_title" align='center' width="10%">型号</td>
			<td class="green_title" align='center' width="10%">oui</td>
			<td class="green_title" align='center' width="15%">设备序列号</td>
			<td class="green_title" align='center' width="10%">添加途径</td>
			<td class="green_title" align='center' width="10%">导入时间</td>
		</tr>
		<s:if test="data!=null">
			<s:if test="data.size()>0">
				<s:iterator value="data">
					<tr bgcolor="#ffffff">
						<td class=column nowrap align="center"><s:property
								value="city_id" /></td>
						<td class=column nowrap align="center"><s:property
								value="vendor_name" /></td>
						<td class=column nowrap align="center"><s:property
								value="model_name" /></td>
						<td class=column nowrap align="center"><s:property
								value="oui" /></td>
						<td class=column nowrap align="center"><s:property
								value="device_serialnumber" /></td>
						<td class=column nowrap align="center"><s:property
								value="remark" /></td>
						<td class=column nowrap align="center"><s:property
								value="add_date" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>

				<tr>
					<td colspan=8 align=left class=column>系统没有相关的设备信息!</td>
				</tr>

			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12 align=left class=column>系统没有相关的设备信息!</td>
			</tr>
		</s:else>
		<tr>
			<td align="right" class=column2 colspan="7"><lk:pages
					url="/gwms/resource/queryDeviceForQnu!queryDevice.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</table>
</body>