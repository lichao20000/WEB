<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>任务配置策略视图</title>
<%
	/**
		 * 任务配置策略视图
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2008-12-18
		 * @category
		 */
%>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">

function queryCustomer(){

	var status              = document.selectForm.status.value;
	var device_serialnumber = document.selectForm.device_serialnumber.value;
	var service_id          = document.selectForm.service_id.value;
	
	parent.frames.dataForm.selectList.status.value = status;
	parent.frames.dataForm.selectList.device_serialnumber.value = device_serialnumber;
	parent.frames.dataForm.selectList.service_id.value = service_id;
	parent.frames.dataForm.selectList.action = "<s:url value="/servStrategy/ServStrategy.action"/>";
	parent.frames.dataForm.selectList.submit();
}

</script>

</head>

<body>
<form name="selectForm" action="">
	<input type="hidden" name="task_id" value="<s:property value="task_id"/>">
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							任务配置策略视图
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">		
						</td>
						<td align="right">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr><th colspan="4"><s:property value="task_name"/>：任务配置策略视图</th></tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="15%">策略状态：</td>
						<td width="35%">
							<select name="status" class="bk">
								<option value="">==请选择==</option>
								<option value="0">==等待执行==</option>
								<option value="1">==预读PVC==</option>
								<option value="2">==预读绑定端口==</option>
								<option value="3">==预读无线==</option>
								<option value="4">==业务下发==</option>
								<option value="100">==执行完成==</option>
							</select>
						</td>
						<td class=column width="15%">业务名称：</td>
						<td width="35%">
							<select name="service_id" class="bk">
								<option value="">==请选择==</option>
								<s:iterator value="service_idLsit">
									<option value="<s:property value="service_id" />">==<s:property value="service_name" />==</option>
								</s:iterator>
							</select>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="15%">设备序列号：</td>
						<td >
							<input type="input" name="device_serialnumber" size="30">
						</td>
						<td colspan="2" align="center" class="green_foot">
							<input type="button" value="查  询" onclick="queryCustomer()">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>