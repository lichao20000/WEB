<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
		<tr>
			<td colspan="2" >
				<font color="red"><s:property value="pingObj.faultStr" /><font color="red">
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr class='blue_foot'>
			<td align="center" width="50%">��Ӧʱ��</td>
			<td align="center" width="50%">����</td>
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
					<td bgcolor='#FFFFFF' width='4%' nowrap>�豸����</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>·����ת��</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>·�ɱ��</td>
					<td bgcolor='#FFFFFF' width='3%' nowrap>��ת����</td>
					<!-- <td bgcolor='#FFFFFF' width='3%' nowrap>��ת������ַ</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>��ת������</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>��ת����ʱ��</td> -->
					<td bgcolor='#FFFFFF' width='2%' nowrap>��תʱ��ο�ֵ</td>
					<td bgcolor='#FFFFFF' width='2%' nowrap>ר�ҽ���</td>
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
						<td colspan="9">�ɼ�ʧ��</td>
					</tr>
				</s:else>
			</table>
		</ms:inArea>
	</s:else>
</table>
