<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ն�״̬չ��</title>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="99.9%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					<s:if test='"1".equals(reportType)'>
						�ն�״̬Сʱͳ��
					</s:if>
					<s:if test='"2".equals(reportType)'>
						�ն�״̬��ͳ��
					</s:if>
					<s:if test='"3".equals(reportType)'>
						�ն�״̬��ͳ��
					</s:if>
					<s:if test='"4".equals(reportType)'>
						�ն�״̬��ͳ��
					</s:if>
				</strong>
			</td>
			<td class=column1 align="right">
				<a href="javascript:window.print()">��ӡ</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<s:iterator value="reportResult" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<s:if test="#servSt.getIndex()==0">
								<s:iterator var="city">
									<th><s:property /></th>
								</s:iterator>
							</s:if>
							<s:else>
								<s:iterator var="city" status="citySt">
									<s:if test="#citySt.getIndex()==0">
										<td  class=column><s:property /></td>
									</s:if>
									<s:else>
										<td  bgcolor=#ffffff align=center><s:property /></td>
									</s:else>
								</s:iterator>
							</s:else>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
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
</html>
