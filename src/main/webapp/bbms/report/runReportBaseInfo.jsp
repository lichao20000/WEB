<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>

<TABLE align="right" border=0 cellspacing=0 cellpadding=0 width="100%" >
	<tr >
	  <td colspan="4"  bgcolor="#999999">
		<table width="100%" border="0" cellpadding="2" cellspacing="1"   >
						<tr align="left" >
							<td  align="left" class="green_title_left" colspan="4">
								���ػ�����Ϣ
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								�豸���к�:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.device_serialnumber" />
							</TD>
							<td class=column align="right" width="15%">
								�豸MAC��ַ:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.cpe_mac"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								����:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.city_name" />
							</TD>
							<td class=column align="right" width="15%">
								����ʺ�:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.oui"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								IP��ַ:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.loopback_ip" />
							</TD>
							<td class=column align="right" width="15%">
								����:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.vendor_name"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								�豸�ͺ�:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.device_model" />
							</TD>
							<td class=column align="right" width="15%">
								�豸����:
							</td>
							<td width="35%" >
								<s:property value="gatewayInfo.device_type"/>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" width="15%">
								����汾��:
							</TD>
							<TD width="35%" >
								<s:property value="gatewayInfo.softwareversion" />
							</TD>
							<TD class="column" align="right" width="15%">
							</TD>
							<TD width="35%" >
							</TD>
						</tr>
			</table>
		</td>
	</tr>
	<tr >
		<td colspan="5" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1" >
				<tr align="left" >
					<td  align="left" class="green_title_left" colspan="5">
						����ҵ����Ϣ
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td   class=column align="center" width="25%">
						ҵ������
					</td>
					<td   class=column align="center" width="25%">
						ҵ���ʺ�
					</td>
					<td   class=column align="center" width="25%">
						����ʱ��
					</td>
					<td   class=column align="center" width="25%">
						ҵ��״̬
					</td>
				</tr>
				<s:if test="servInfo.size()>0">
					<s:iterator value="servInfo">
					<tr bgcolor="#FFFFFF">
						<TD align="center" width="25%" >
							<s:property value="serv_type_name" />
						</TD>
						<TD align="center" width="25%">
							<s:property value="username" />
						</TD>
						<TD align="center" width="25%">
							<s:property value="dealdate" />
						</TD>
						<TD align="center" width="25%">
							<s:if test='"1"==open_status'>�ɹ�</s:if>
							<s:if test='"0"==open_status'>δ��</s:if>
							<s:if test='"-1"==open_status'>ʧ��</s:if>
						</TD>
					</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr  bgcolor="#FFFFFF">
						<td colspan="5" align="center">
							��ʱû������
						</td>
					</tr>
				</s:else>
			</table>
		</td>
	</tr> 
	<tr>
		<td colspan="5" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1"  >
				<tr align="left">
					<td   class="green_title_left" colspan="5">  <!--LAN����Ҫ���� ���棬�������� -->
					   	����������Ϣ
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column  align="center" width="20%">
						 �����������
					</td>
					<td class=column align="center"  width="20%">
						�����������
					</td>
					<td class=column align="center"  width="20%">
						ƽ����������
					</td>
					<td class=column align="center"  width="20%">
						ƽ����������
					</td>
					<td class=column align="center"  width="20%">
						ƽ�������������� 
					</td>
				</tr>
				<s:if test="gatewayFluxInfo.size()>0">
						<s:iterator value="gatewayFluxInfo">
						<tr bgcolor="#FFFFFF">
							<td align="center" width="20%">
								<s:property value="ifinoctetsbpsmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifoutoctetsbpsmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifinoctbps_avgmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="ifoutoctbps_avgmax"/>
							</td>
							<td align="center" width="20%">
								<s:property value="avgmax_rate"/>
							</td>
						</tr>
						</s:iterator>
				</s:if>
				<s:else>
					<tr  bgcolor="#FFFFFF">
						<td colspan="5" align="center">
							��ʱû������
						</td>
					</tr>
				</s:else>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6" bgcolor="#999999">
			<table width="100%" border="0" cellpadding="2"
						cellspacing="1" border="0" >
				<tr align="left">
					<td   class="green_title_left" colspan="7">  
					   	���ظ澯��Ϣ
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="center"  width="15%">
						�澯����
					</td>
					<td class=column align="center"  width="15%">
						�澯����
					</td>
					<td class=column align="center"  width="15%">
						�澯״̬
					</td>
					<td class=column align="center"  width="15%">
						�澯ԭ��
					</td>
					<td class=column align="center"  width="15%">
						����ʱ��
					</td>
					<td class=column align="center"  width="15%">
						�޸�ʱ��
					</td>
					<td class=column align="center"  width="15%">
						�޸�����
					</td>
				</tr>
				<s:if test="gatewayWarnLt.size()>0">
					<s:iterator value ="gatewayWarnLt">
						<tr bgcolor="#FFFFFF">
							<td align="center" width="15%">
								<s:if test='"1"==type'>�豸�澯</s:if>
								<s:if test='"2"==type'>���������澯</s:if>
								<s:if test='"3"==type'>ͨ�Ÿ澯</s:if>
								<s:if test='"4"==type'>����ʧ�ܸ澯></s:if>
								<s:if test='"5"==type'>����ϵͳ�����ĸ澯</s:if>
							</td>
							<td align="center" width="15%">
								<s:if test='"1"==mlevel'>�����澯</s:if>
								<s:if test='"2"==mlevel'>��Ҫ�澯</s:if>
								<s:if test='"3"==mlevel'>��Ҫ�澯</s:if>
								<s:if test='"4"==mlevel'>����澯</s:if>
								<s:if test='"5"==mlevel'>��ȷ���澯</s:if>
								<s:if test='"6"==mlevel'>������澯</s:if>
							</td>
							<td align="center" width="15%">
								<s:if test='"1"==status'>δȷ��δ����澯</s:if>
								<s:if test='"2"==status'>��ȷ��δ����澯</s:if>
								<s:if test='"3"==status'>δȷ�ϵ�������澯</s:if>
								<s:if test='"4"==status'>��ȷ�ϲ�������澯</s:if>
							</td>
							<td align="center" width="15%">
								<s:property value="possiblereason"/>
							</td>
							<td align="center" width="15%">
								<s:property value="occurtime"/>
							</td>
							<td align="center" width="15%">
								<s:property value="cleartime"/>
							</td>
							<td align="center" width="15%">
								<s:property value="clearsuggestion"/>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
						<tr bgcolor="#FFFFFF">
							<td colspan="7" align="center">
								��ʱû������
							</td>
						</tr>
				</s:else>
			</table>
	</tr>
</TABLE>