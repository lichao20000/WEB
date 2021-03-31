<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="querytable">

	<TR>
		<th colspan="4">
			IPSEC������ϸ��Ϣ
		</th>
	</tr>
	<s:if test="ipsecSheetList!=null">
		<s:if test="ipsecSheetList.size()>0">
			<s:iterator value="ipsecSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						�ͻ�����˺�
					</TD>
					<TD width="35%">
						<s:property value="username" />
					</TD>
					<TD class=column width="15%" align='right'>
						ҵ������
					</TD>
					<TD width="35%">
						<s:property value="serv_type_id" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					ҵ��״̬
					</TD>
					<TD width="35%">
						<s:property value="serv_status" />
					</TD>
					<TD class=column width="15%" align='right'>
						�Ƿ�����IPSec
					</TD>   
					<TD width="35%">
						<s:property value="enable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						����id
					</TD>
					<TD width="35%">
						<s:property value="request_id" />
					</TD>
					<TD class=column width="15%" align='right'>
						IPSec����
					</TD>
					<TD width="35%">
						<s:property value="ipsec_type" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						PC -to-Siteģʽ�¶Զ�����
					</TD>
					<TD width="35%">
						<s:property value="remote_domain" />
					</TD>
					<TD class=column width="15%" align='right'>
						�Զ�����
					</TD>
					<TD width="35%" colspan="3">
						<s:property value="remote_subnet" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						��������
					</TD>
					<TD width="35%">
						<s:property value="local_subnet" />
					</TD>
					<TD class=column width="15%" align='right'>
						Site-to-Siteģʽ�¶Զ�IP��ַ
					</TD>
					<TD width="35%" colspan="3">
						<s:property value="remote_ip" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKEЭ�̷�ʽ
					</TD>
					<TD width="35%">
							<s:property value="exchange_mode" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE��֤�㷨
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_auth_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE��֤����
					</TD>
					<TD width="35%">
							<s:property value="ike_auth__method" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE�����㷨
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_encryption_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKE DH��
					</TD>
					<TD width="35%">
							<s:property value="ike_dhgroup" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE�������
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_idtype" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					IKE��������
					</TD>
					<TD width="35%">
							<s:property value="ike_localname" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE�Զ�����
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_remotename" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IKEԤ������Կ
					</TD>
					<TD width="35%">
							<s:property value="ike_presharekey" />
					</TD>
					<TD class=column width="15%" align='right'>
					Ipsec��װ���ĵĳ��ӿ�
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_out_interface" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						Ipsec��װģʽ
					</TD>
					<TD width="35%">
							<s:property value="ipsec_encapsulation_mode" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPSec��ȫЭ��
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_transform" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPsec��֤�㷨
					</TD>
					<TD width="35%">
							<s:property value="esp_auth_algorithem" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPsec�����㷨
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="esp_encrypt_algorithm" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPSec DH��
					</TD>
					<TD width="35%">
							<s:property value="ipsec_pfs" />
					</TD>
					<TD class=column width="15%" align='right'>
					IKE SA��������
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ike_saperiod" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						IPsec SAʱ����������
					</TD>
					<TD width="35%">
							<s:property value="ipsec_satime_period" />
					</TD>
					<TD class=column width="15%" align='right'>
					IPsec SA������������
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="ipsec_satraffic_period" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						AH��֤�㷨
					</TD>
					<TD width="35%">
							<s:property value="ah_auth_algorithm" />
					</TD>
					<TD class=column width="15%" align='right'>
					DPDʹ��
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="dpd_enable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						DPD����ʱ��
					</TD>
					<TD width="35%">
							<s:property value="dpd_threshold" />
					</TD>
					<TD class=column width="15%" align='right'>
					δ�յ�DPD��Ӧ���ٴγ��Լ�
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="dpd_retry" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					����ʱ��
					</TD>
					<TD width="35%">
							<s:property value="open_date" />
					</TD>
					<TD class=column width="15%" align='right'>
					����ʱ��
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="updatetime" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					���ʱ��
					</TD>
					<TD width="35%">
							<s:property value="completedate" />
					</TD>
					<TD class=column width="15%" align='right'>
					��ͨ״̬
					</TD>
					<TD width="35%" colspan="3">
					<s:property value="open_status" />
					</TD>
				</TR>
				<!-- <tr>
					<TD class=column width="15%" align='right'>
						�ص���Ϣ
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||�ɹ�
					</TD>
				</tr> -->
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					������Ϣ�����ڻ����!
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>
				�û���Ϣ����!
			</td>
		</tr>
	</s:else>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">�ر�</a>
		</td>
	</TR>
</table>
