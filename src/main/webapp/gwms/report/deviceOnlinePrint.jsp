<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

</head>

<body>
	<br>
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<table width="100%">
					<tr>
						<td align="center">
							<strong>
								�ն�����ͳ��
							</strong>
						</td>
						<td class=column1 align="right">
							<a href="javascript:window.print()">��ӡ</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr>
						<s:if test='"none".equals(displaydevice_serialnumber)'>
						</s:if>
						<s:else>
							<th>�豸���к�</th>
						</s:else>
						<s:if test='"none".equals(displaycity_name)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(displayoui)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(displaydevice_type)'>
						</s:if>
						<s:else>
							<th>�ն�����</th>
						</s:else>
						<s:if test='"none".equals(displaylast_time)'>
						</s:if>
						<s:else>
							<th>�������ʱ��</th>
						</s:else>
					</tr>
					<s:iterator value="exceptinList">
						<tr bgcolor="#FFFFFF">
							<s:if test='"none".equals(displaydevice_serialnumber)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="device_serialnumber"/></td>
							</s:else>
							<s:if test='"none".equals(displaycity_name)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="city_name"/></td>
							</s:else>
							<s:if test='"none".equals(displayoui)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="oui"/></td>
							</s:else>
							<s:if test='"none".equals(displaydevice_type)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="device_type"/></td>
							</s:else>
							<s:if test='"none".equals(displaylast_time)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="last_time"/></td>
							</s:else>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="right">
							<strong>
								<s:if test='"1".equals(reportType)'>
									ͳ�����ڣ�<s:property value="hourDataEnd"/>
								</s:if>
								<s:if test='"2".equals(reportType)'>
									ͳ�����ڣ�<s:property value="dayDataEnd"/>
								</s:if>
								<s:if test='"3".equals(reportType)'>
									ͳ�ƽ�ֹʱ�䣺<s:property value="weekDataEnd"/>
								</s:if>
								<s:if test='"4".equals(reportType)'>
									ͳ�ƽ�ֹʱ�䣺<s:property value="monthDataEnd"/>
								</s:if>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
<form name="frm2"></form>
</html>