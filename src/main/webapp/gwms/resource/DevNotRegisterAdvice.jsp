<%--
�豸δע��Ĵ�����
Author: ��ɭ��
Version: 1.0.0
Date: 2010-4-14
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<table border=0 cellspacing=1 cellpadding=1 width="95%" align="center"
	bgcolor="#999999">
	<tr>
		<td class="green_title">
			<font size="4">�豸δע��Ĵ�����</font>
		</td>
	</tr>

	<tr bgcolor="#FFFFFF">
		<td>
			<s:if test='instArea == "js_dx"'>
				<table border=0 cellspacing=0 cellpadding=0 width="100%"
					align="center" bgcolor="#999999">

					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 1. </font>
						</td>
						<td>
							<font size="2">
								ͨ����������Ա�˺ŵ�½�豸����ʹ��ԭʼ����(nE7jA%5m)�Ƿ��ܹ���¼������޷���¼��˵���ն�������ע�ᵽ��ITMSϵͳ�����δ���޸��������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 2.</font>
						</td>
						<td>
							<font size="2">
								�鿴Զ�̹����ITMS��URL��http://devacs.edatahome.com:9090/ACS-server/ACS�����Ƿ���ȷ�������ȷ������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 3.</font>
						</td>
						<td>
							<font size="2">
								��¼�ն�ͨ�������硿-��������á��鿴�豸���Ƿ����8/46��PVCͨ����ҵ�����ͣ�TR069��DHCP·�ɽ��뷽ʽ�������ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 4.</font>
						</td>
						<td>
							<font size="2">
								�豸�ϵ���������״̬�鿴(��״̬��-���������Ϣ���˵�)��TR069��ͨ���Ƿ��Ѿ���ȡ��10.��˽����ַ��DNS�Ƿ��ȡ���������ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 5.</font>
						</td>
						<td>
							<font size="2"> ���ն���
								ͨ��������-��ά�����˵�pingITMSϵͳ��ACS������DNS�ĵ�ַ��192.168.0.3���鿴�豸��ϵͳ֮��������Ƿ�ɴ�����ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2">6.</font>
						</td>
						<td>
							<font size="2"> �������ã�DSLAM�Ƿ���ж�PVC���졢BAS�Ƿ�����ITMS�򡢵�ַ�أ�DNS��
								192.168.0.3�Ƿ����ã������ȷ������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 7.</font>
						</td>
						<td>
							<font size="2"> ���м�ͥ�����ն˵������������ϱ��������δ�������ϱ�������һ���� </font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2">8.</font>
						</td>
						<td>
							<font size="2"> ������Ҫ��ϵ���Ҳ鿴�豸���⡣</font>
						</td>
					</tr>
				</table>
			</s:if>
			<s:elseif test='instArea == "xj_dx"'>
				<table border=0 cellspacing=0 cellpadding=0 width="100%"
					align="center" bgcolor="#999999">


					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 1. </font>
						</td>
						<td>
							<font size="2">
								ͨ����������Ա�˺ŵ�½�豸����ʹ��ԭʼ����(nE7jA%5m)�Ƿ��ܹ���¼������޷���¼��˵���ն�������ע�ᵽ��ITMSϵͳ�����δ���޸��������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 2.</font>
						</td>
						<td>
							<font size="2">
								�鿴Զ�̹����ITMS��URL��http://devacs.edatahome.com:9090/ACS-server/ACS�����Ƿ���ȷ�������ȷ������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 3.</font>
						</td>
						<td>
							<font size="2">
								��¼�ն�ͨ�������硿-��������á��鿴�豸���Ƿ����8/46��PVCͨ����ҵ�����ͣ�TR069��DHCP·�ɽ��뷽ʽ��(�½�������VLAN��3960)�������ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 4.</font>
						</td>
						<td>
							<font size="2">
								�豸�ϵ���������״̬�鿴(��״̬��-���������Ϣ���˵�)��TR069��ͨ���Ƿ��Ѿ���ȡ��10.��˽����ַ��DNS�Ƿ��ȡ���������ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2"> 5.</font>
						</td>
						<td>
							<font size="2"> ���ն���
								ͨ��������-��ά�����˵�pingITMSϵͳ��ACS������DNS�ĵ�ַ��10.0.1.16���鿴�豸��ϵͳ֮��������Ƿ�ɴ�����ȷ������һ����
							</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td valign="top">
							<font size="2">6.</font>
						</td>
						<td>
							<font size="2"> �������ã�DSLAM�Ƿ���ж�PVC���졢BAS�Ƿ�����ITMS�򡢵�ַ�أ�DNS��
								10.0.1.16�Ƿ����ã������ȷ������һ����</font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2"> 7.</font>
						</td>
						<td>
							<font size="2"> ���м�ͥ�����ն˵������������ϱ��������δ�������ϱ�������һ���� </font>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" height="10">
						<td colspan="2"></td>
					</tr>
					<tr bgcolor="#FFFFFF" valign="top">
						<td>
							<font size="2">8.</font>
						</td>
						<td>
							<font size="2"> ������Ҫ��ϵ���Ҳ鿴�豸���⡣</font>
						</td>
					</tr>
				</table>

			</s:elseif>
		</td>
	</tr>
</table>
