<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	function hostIndex(indexI)
	{
		var countI = parseInt(indexI)+1;
		document.write("��aaa");
	}
</SCRIPT>

<table class="listtable" width="100%" align="center">
	<s:if test='pingObj.result=="0"'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="2">
				<font color="red"><s:property value="pingObj.faultStr" /><font color="red">
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr bgcolor='#FFFFFF'>
			<td bgcolor='#FFFFFF' nowrap>��Ӧʱ��</td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.responseTime"/>ms</td>
		</tr>
		<tr bgcolor='#FFFFFF'>
			<td bgcolor='#FFFFFF' nowrap>����</td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.numberOfRouteHops"/></td>
		</tr>

		<tr bgcolor='#FFFFFF'>
			<td bgcolor='#FFFFFF' nowrap colspan="2" align="left">
				<table class="listtable" width="98%" align="left">
				<tr class='blue_foot'>
					<td bgcolor='#FFFFFF' colspan=4>ÿһ������ϸ���</td>
				</tr>
				<tr class='blue_foot'>
					<td bgcolor='#FFFFFF' nowrap>IP��ַ</td>
					<td bgcolor='#FFFFFF' nowrap>���ʱ��</td>
					<td bgcolor='#FFFFFF' nowrap>��Сʱ��</td>
					<td bgcolor='#FFFFFF' nowrap>ƽ��ʱ��</td>
				</tr>
				<s:if test="pingObj.hopHostI.size()>0" >
					<s:iterator value="pingObj.hopHostI" var="HopIP" status="stad">
						<tr class='blue_foot'>
							<td bgcolor='#FFFFFF' nowrap><s:property value="#HopIP.hop_host"/></td>
							<td bgcolor='#FFFFFF' nowrap><s:property value="#HopIP.max_response_time"/></td>
							<td bgcolor='#FFFFFF' nowrap><s:property value="#HopIP.min_response_time"/></td>
							<td bgcolor='#FFFFFF' nowrap><s:property value="#HopIP.avg_response_time"/></td>
						</tr>
					</s:iterator>
				</s:if>
				</table>
			</td>
		</tr>
	</s:else>
</table>
