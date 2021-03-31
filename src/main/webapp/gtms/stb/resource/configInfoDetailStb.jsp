<%@ include file="../../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务配置详细信息</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/commFunction.js"/>"></script>
<script type="text/javascript">
	function decodePassword() {
		var isReturn = false;
		var configXml = $("td[@id='temp']").text();
		var pwdStart = configXml.indexOf("<Password>");
		if (pwdStart > 0) {
			var pwdEnd = configXml.indexOf("</Password>");
			if (pwdEnd > pwdStart) {
				var password = configXml.substring(pwdStart + 10, pwdEnd);
				if (password) {
					isReturn = true;
					var url = "configInfoDetailOper.jsp?password=" + password;
					document.all("childFrm").src = url;
				}
			}
		}
		if (!isReturn) {
			$("td[@id='td_password']").text("密码为空");
		}

		<%-- 记录日志延迟2秒执行，不然在密码计算生成之前获取的密码为空 --%>
		setTimeout(logSuperAuth, 2000);
	}

	<%-- 2.用户点击了密码明文按钮，则记录超级权限日志 --%>
	function logSuperAuth(){
		superAuthLog('ShowNetSrcPwd',
				'查看[<s:property value="deviceSN" />]设备的超级密码['+ $("td[@id='td_password']").text() +']');
	}

</script>

</head>
<body>
	<table class="querytable">
		<TR>
			<th colspan="4">设备 <s:property value="deviceSN" /> 配置详情
			</th>
		</tr>
		<s:if test="configList != null">
			<s:if test="configList.size()>0">
				<s:iterator value="configList">
					<TR>
						<TD class=column width="15%" align='right'>策略ID</TD>
						<ms:inArea areaCode="sx_lt" >
							<TD width="35%" colspan=3><s:property value="id" /></TD>
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							<TD width="35%"><s:property value="id" /></TD>
							<TD class=column width="15%" align='right'>工单ID</TD>
							<TD width="35%"><s:property value="sheet_id" /></TD>
						</ms:inArea>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>业务ID</TD>
						<TD width="85%" colspan="3"><s:property value="service_id" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>结果详细描述</TD>
						<TD width="85%" colspan="3"><s:property value="fault_desc" />
						</TD>
					</TR>
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<TR>
						<TD class=column width="15%" align='right'>错误原因</TD>
						<TD width="85%" colspan="3"><s:property value="fault_reason" /></TD>
					</TR>
					</ms:inArea>
					<TR>
						<TD class=column width="15%" align='right'>策略参数信息</TD>
						<TD width="85%" colspan="3" id="temp"><s:property value="sheet_para" escapeHtml="false" /></TD>
					</TR>
					<tr>
						<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统没有配置信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>系统没有配置信息!</td>
			</tr>
		</s:else>
	</table>
	<%  if(LipossGlobals.isSXLT()) {%>
	<table class="listtable" >
		<s:if test="doServStatusList!=null">
		<thead>
			<tr>
				<th>业务参数名称</th>
				<th>业务参数取值</th>
				<th>命令</th>
				<th>执行结果</th>
			</tr>
		</thead>
			<s:if test="doServStatusList.size()>0">
				<s:iterator value="doServStatusList">
					<tr>
						<td><s:property value="name" /></td>
						<td style="word-break:break-all" width="400px" ><s:property value="value" /></td>
						<td><s:property value="type"/></td>
						<td><s:property value="result" /></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
	</table>
	<%}%>
</body>
