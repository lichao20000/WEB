<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ѯ���չ��</title>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="99.9%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					<s:if test='"1".equals(reportType)'>
						����Сʱͳ��
					</s:if>
					<s:if test='"2".equals(reportType)'>
						������ͳ��
					</s:if>
					<s:if test='"3".equals(reportType)'>
						������ͳ��
					</s:if>
					<s:if test='"4".equals(reportType)'>
						������ͳ��
					</s:if>
				</strong>
			</td>
			<td class=column1 align="right">
				<a href="javascript:window.print()">��ӡ</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<th>����</th>
						<s:if test='"none".equals(sunm1display)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(sunm2display)'>
						</s:if>
						<s:else>
							<th>��ͣ</th>
						</s:else>
						<s:if test='"none".equals(sunm3display)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(sunm4display)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(sunm5display)'>
						</s:if>
						<s:else>
							<th>��������</th>
						</s:else>
						<s:if test='"none".equals(sunm6display)'>
						</s:if>
						<s:else>
							<th>�����˺�</th>
						</s:else>
						<s:if test='"none".equals(sunm7display)'>
						</s:if>
						<s:else>
							<th>�����豸</th>
						</s:else>
						<s:if test='"none".equals(sunm8display)'>
						</s:if>
						<s:else>
							<th>����IP</th>
						</s:else>		
						<th>ͳ�ƹ�������</th>
						<th>�ɹ�������</th>
						<th>�ɹ���(%)</th>
					</tr>
					<s:iterator value="reportResult">
						<tr bgcolor="#FFFFFF">
							<td class=column>
								<s:property value="city_name"/>
							</td>
							
							<s:if test='"none".equals(sunm1display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum1"/></td>
							</s:else>
							<s:if test='"none".equals(sunm2display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum2"/></td>
							</s:else>
							<s:if test='"none".equals(sunm3display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum3"/></td>
							</s:else>
							<s:if test='"none".equals(sunm4display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum4"/></td>
							</s:else>
							<s:if test='"none".equals(sunm5display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum5"/></td>
							</s:else>
							<s:if test='"none".equals(sunm6display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum6"/></td>
							</s:else>
							<s:if test='"none".equals(sunm7display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum7"/></td>
							</s:else>
							<s:if test='"none".equals(sunm8display)'>
							</s:if>
							<s:else>
								<td  bgcolor=#ffffff align=center><s:property value="sum8"/></td>
							</s:else>			
							
							<td  bgcolor=#ffffff align=center><s:property value="sum9"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="sum10"/></td>
							<td  bgcolor=#ffffff align=center><s:property value="sum11"/></td>
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
						<td class=column1  colspan="12" align="right">
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