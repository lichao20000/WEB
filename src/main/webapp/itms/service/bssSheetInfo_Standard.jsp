<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<table class="querytable">
	<TR>
		<th colspan="4">
			BSS������ϸ��Ϣ
		</th>
	</tr>
	<tr style="cursor: hand; background-color: #F0FFFF">
		<td colspan="4" align='left' class="green_title2">
			������Ϣ
		</td>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()>0">
			<TR>
				<TD class=column width="15%" align='right'>
					ҵ������
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).serv_type" />
				</TD>
				<TD class=column width="15%" align='right'>
					��������
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).serv_status" />
				</TD>
			</TR>
			<TR>
				<%if (LipossGlobals.inArea("sd_lt")) { %>
				<TD class=column width="15%" align='right'>
					RMS����ʱ��
				</TD>
				<%}else{ %>
				<TD class=column width="15%" align='right'>
					ITMS����ʱ��
				</TD>
				<%} %>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).opendate" />
				</TD>
				<TD class=column width="15%" align='right'>
					�豸����
				</TD>
				<TD width="35%">
					<s:property value="type_name" />
				</TD>
			</TR>
			<TR>
				<TD class=column width="15%" align='right'>
					LOID
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).username" />
				</TD>
				<TD class=column width="15%" align='right'>
					����
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).city_name" />
				</TD>
			</TR>
			<TR>
				<TD class=column width="15%" align='right'>
					Э������
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).protocol" />
				</TD>
				<TD class=column width="15%" align='right'>
					IP��ȡ��ʽ
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).wan_type" />
				</TD>
			</TR>
			<tr>
				<TD class=column width="15%" align='right'>
					VLANID
				</TD>
				<TD width="85%" colspan="3">
					<s:property value="bssSheetList.get(0).vlanid" />
				</TD>
			</tr>
			<tr>
				<TD class=column width="15%" align='right'>
					�ص���Ϣ
				</TD>
				<TD width="85%" colspan="3">
					0|||00|||�ɹ�
				</TD>
			</tr>
			<s:iterator value="bssSheetList">
				<TR style="cursor: hand; background-color: #F0FFFF">
					<TD width="15%" align='left'>
						�����˿�
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="line_id" />
					</TD>
				</TR>
				<s:if test="protocol!='H248' && protocol != 'IMS H248'">
					<%-- <s:if test='%{error_code=="1"}'> --%>
					<TR>
						<TD class=column width="15%" align='right'>
							VoIP��֤�˺�
						</TD>
						<TD width="35%">
							<s:property value="voip_username" />
						</TD>
						<TD class=column width="15%" align='right'>
							VoIP��֤����
						</TD>
						<%if (LipossGlobals.inArea("cq_dx")) { %>
						<TD width="35%">
							******
						</TD>
						<%}else{ %>
						<TD width="35%">
							<s:property value="voip_passwd" />
						</TD>
						<%} %>
					</tr>
					<tr>
						<TD class=column width="15%" align='right'>
							ҵ��绰����
						</TD>
						<TD width="35%">
							<s:property value="voip_phone" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							���ô����������ַ
						</TD>
						<TD width="35%">
							<s:property value="prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							���ô���������˿�
						</TD>
						<TD width="35%">
							<s:property value="prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							���ô����������ַ
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							���ô���������˿�
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							����ע���������ַ
						</TD>
						<TD width="35%">
							<s:property value="regi_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							����ע��������˿�
						</TD>
						<TD width="35%">
							<s:property value="regi_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							����ע���������ַ
						</TD>
						<TD width="35%">
							<s:property value="stand_regi_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							����ע��������˿�
						</TD>
						<TD width="35%">
							<s:property value="stand_regi_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							���ð󶨷�������ַ
						</TD>
						<TD width="35%">
							<s:property value="out_bound_proxy" />
						</TD>
						<TD class=column width="15%" align='right'>
							���ð󶨷������˿�
						</TD>
						<TD width="35%">
							<s:property value="out_bound_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							���ð󶨷�������ַ
						</TD>
						<TD width="35%">
							<s:property value="stand_out_bound_proxy" />
						</TD>
						<TD class=column width="15%" align='right'>
							���ð󶨷������˿�
						</TD>
						<TD width="35%">
							<s:property value="stand_out_bound_port" />
						</TD>
					</TR>
					<!-- added by zhouli5 -->
					<TR>
						<TD class=column width="15%" align='right'>
							IP��ַ
						</TD>
						<TD width="35%">
							<s:property value="ipaddress" />
						</TD>
						<TD class=column width="15%" align='right'>
							����
						</TD>
						<TD width="35%">
							<s:property value="ipmask" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							����
						</TD>
						<TD width="35%">
							<s:property value="gateway" />
						</TD>
						<TD class=column width="15%" align='right'>
							�ն˱�ʶ����
						</TD>
						<TD width="35%">
							<s:property value="reg_id_type" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							�ն˱�ʶ
						</TD>
						<TD width="35%">
							<s:property value="reg_id" />
						</TD>
						<ms:inArea areaCode="hb_dx" notInMode="false">
						<TD class=column width="15%" align='right'>
							�ն������ʶ
						</TD>
						<TD width="35%">
							<s:property value="voip_port" />
						</TD>
						</ms:inArea>
						<ms:inArea areaCode="cq_dx" notInMode="false">
						<TD class=column width="15%" align='right'>
							DNS��������ַ
						</TD>
						<TD width="35%">
							<s:property value="adsl_ser" />
						</TD>
						</ms:inArea>
					</TR>
					
					<%-- </s:if>
					<s:elseif test='%{error_code=="0"}'>
						<TD colspan="4"  align="center"><s:property value='error_desc'/> </TD>
					</s:elseif> --%>
				</s:if>
				<s:else>
					<s:if test="wan_type != 'DHCP'">
						<TR>
							<TD class=column width="15%" align='right'>
								IP��ַ
							</TD>
							<TD width="35%">
								<s:property value="ipaddress" />
							</TD>
							<TD class=column width="15%" align='right'>
								����
							</TD>
							<TD width="35%">
								<s:property value="ipmask" />
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>
								����
							</TD>
							<TD width="35%">
								<s:property value="gateway" />
							</TD>
							<TD class=column width="15%" align='right'>
								DNSֵ
							</TD>
							<TD width="35%">
								<s:property value="adsl_ser" />
							</TD>
						</TR>
					</s:if>
					<TR>
						<TD class=column width="15%" align='right'>
							��MGC��������ַ
						</TD>
						<TD width="35%">
							<s:property value="prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							��MGC�������˿�
						</TD>
						<TD width="35%">
							<s:property value="prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							��MGC��������ַ
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							��MGC�������˿�
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							�ն˱�ʶ����
						</TD>
						<TD width="35%">
							<s:property value="reg_id_type" />
						</TD>
						<TD class=column width="15%" align='right'>
							�ն˱�ʶ
						</TD>
						<TD width="35%">
							<s:property value="reg_id" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							�ն������ʶ
						</TD>
						<TD width="35%">
							<s:property value="voip_port" />
						</TD>
						<TD class=column width="15%" align='right'>
							ҵ��绰����
						</TD>
						<TD width="35%">
							<s:property value="voip_phone" />
						</TD>
					</TR>
				</s:else>
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
