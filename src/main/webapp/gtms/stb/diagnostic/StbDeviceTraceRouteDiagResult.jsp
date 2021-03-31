<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	function hostIndex(indexI)
	{
		var countI = parseInt(indexI)+1;
		document.write("：aaa");
	}
</SCRIPT>

<table class="listtable" width="100%" align="center">
	<s:if test='pingObj.result=="0"'>
		<tr>
			<td colspan="2" >
				<font color="red"><s:property value="pingObj.faultStr" /><font color="red">
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr class='blue_foot'>
			<td align="center" width="50%">响应时间</td>
			<td align="center" width="50%">跳数</td>
		</tr>
		<tr>
			<td align="center" width="50%"><s:property value="pingObj.responseTime"/>ms</td>
			<td align="center" width="50%"><s:property value="pingObj.numberOfRouteHops"/></td>
		</tr>
		<s:if test="pingObj.hopHostI.size()>0">
			<s:iterator value="pingObj.hopHostI" var="HopIP" status="stad">
				<tr class='blue_foot'>
					<td colspan="2">
						<s:property value="HopIP"/>
					</td>
				</tr>
			</s:iterator>
		</s:if>

		<ms:inArea areaCode="sx_lt" notInMode="false">
			<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>
				<tr class='blue_foot'>
					<td bgcolor='#FFFFFF' width='4%' nowrap>设备名称</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>路由跳转数</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>路由编号</td>
					<td bgcolor='#FFFFFF' width='3%' nowrap>跳转主机</td>
					<!-- <td bgcolor='#FFFFFF' width='3%' nowrap>跳转主机地址</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>跳转错误码</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>跳转往返时间</td> -->
					<td bgcolor='#FFFFFF' width='2%' nowrap>跳转时间参考值</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>专家建议</td>
				</tr>
				<s:if test="routeHopsData!=null && routeHopsData.size()>0">
				<s:iterator value="routeHopsData">
				<tr class='blue_foot'>
					<td>
						<s:property value="pingObj.oui"/>-<s:property value="pingObj.sn"/>
					</td>
					<td>
						<s:property value="pingObj.numberOfRouteHops"/>
					</td>
					<td><s:property value="id"/></td>
					<td><s:property value="hopHost"/></td>
					<%-- <td><s:property value="hopHostAddress"/></td>
					<td><s:property value="hopErrorCode"/></td>
					<td><s:property value="hopRTTimes"/></td> --%>
					<td><s:property value="ex_bias"/></td>
					<td><s:property value="ex_desc"/></td>
				</tr>
				</s:iterator>
				</s:if>
				<s:else>
					<tr class='blue_foot' >
						<td colspan="9">采集失败</td>
					</tr>
				</s:else>
			</table>
		</ms:inArea>
	</s:else>
</table>
