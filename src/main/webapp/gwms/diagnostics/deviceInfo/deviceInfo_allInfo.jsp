<%--
FileName	: deviceInfo.jsp
Date		: 2009��6��25��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:wideBandNet();" stytle="CURSOR:hand;"> ������� <IMG
							name="imgnet" SRC="../../images/up_enabled.gif" WIDTH="7"
							HEIGHT="9" BORDER="0" ALT="">
					</a></td>
				</tr>
				<tr align="left" id="trnet" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="divnet">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">PVC/VLAN����</TD>
									<%if (LipossGlobals.inArea("js_dx")) { %>
									<TD class=column5 align="center">���DHCP����</TD>
									<%}%>
									<TD class=column5 align="center">��������</TD>
									<TD class=column5 align="center">�󶨶˿�</TD>
									<TD class=column5 align="center">����״̬</TD>
									<TD class=column5 align="center">DNS(IPV4)</TD>
									<TD class=column5 align="center">IP��ַ(IPV4)</TD>
									<TD class=column5 align="center">DNS(IPV6)</TD>
									<TD class=column5 align="center">IP��ַ(IPV6)</TD>
									<TD class=column5 align="center">PPPoE�˺�</TD>
									<TD class=column5 align="center">����ʧ�ܴ�����</TD>
								</tr>
								<s:iterator value="wideNetInfoList" var="wideNetInfo">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if test="accessType==1">
											<TD align="center"><s:property value="pvc" /></TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="vlanid" /></TD>
										</s:else>

										<%if (LipossGlobals.inArea("js_dx")) { %>
										<TD align="center"><s:property value="dhcp_status" /></TD>
										<%} %>


										<s:if
											test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="connType" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.bindPort=='N/A' || #wideNetInfo.bindPort=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="bindPort" /></TD>
										</s:else>
										<!--
										<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="status"/></TD>
										</s:else>
										 -->
										<TD align="center"><s:property value="status" /></TD>
										<s:if
											test="#wideNetInfo.dns=='N/A' || #wideNetInfo.dns=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="dns" /></TD>
										</s:else>

										<!-- <s:if
											test="#wideNetInfo.password=='N/A' || #wideNetInfo.password=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="password" />
											</TD>
										</s:else> -->
										<s:if
											test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ip" /></TD>
										</s:else>
										<!-- ipv6 dns -->
										<s:if
											test="#wideNetInfo.dns_ipv6=='N/A' || #wideNetInfo.dns_ipv6=='null' || #wideNetInfo.dns_ipv6=='NULL'">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="dns_ipv6" /></TD>
										</s:else>
										<!-- ipv6 dns -->
										<!-- ipv6 ip -->
										<s:if
											test="#wideNetInfo.ip_ipv6=='N/A' || #wideNetInfo.ip_ipv6=='null' || #wideNetInfo.ip_ipv6=='NULL'">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ip_ipv6" /></TD>
										</s:else>
										<!-- ipv6 ip -->
										<s:if
											test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="username" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.connError=='N/A' || #wideNetInfo.connError=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="connError" /></TD>
										</s:else>
									</tr>
								</s:iterator>
								<tr align="center" bgcolor="#FFFFFF">
									<%if (LipossGlobals.inArea("js_dx")) { %>
									<TD align="center" colspan="11">
										<%} else{%>

									<TD align="center" colspan="10">
										<%} %> <s:property value="wideNetMsg" />
									</TD>
								</tr>
							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>

    <%if (LipossGlobals.inArea("gs_dx")) { %>
    <tr>
    	<td>
    		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
    			<tr align="left" height="25">
    				<td colspan="5" class="green_title_left"><a
    					href="javascript:cloudBandNet();" stytle="CURSOR:hand;"> ������������� <IMG
    						name="imgcloudnet" SRC="../../images/up_enabled.gif" WIDTH="7"
    						HEIGHT="9" BORDER="0" ALT="">
    				</a></td>
    			</tr>
    			<tr align="left" id="trcloudnet" STYLE="display:">
    				<td colspan="1" width="15" class=column></td>
    				<td colspan="4" bgcolor=#999999>
    					<div id="divnet">
    						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
    							<tr align="center" bgcolor="#FFFFFF">
    								<TD class=column5 align="center">PVC/VLAN����</TD>
    								<TD class=column5 align="center">��������</TD>
    								<TD class=column5 align="center">�󶨶˿�</TD>
    								<TD class=column5 align="center">����״̬</TD>
    								<TD class=column5 align="center">DNS(IPV4)</TD>
    								<TD class=column5 align="center">IP��ַ(IPV4)</TD>
    								<TD class=column5 align="center">DNS(IPV6)</TD>
    								<TD class=column5 align="center">IP��ַ(IPV6)</TD>
    								<TD class=column5 align="center">PPPoE�˺�</TD>
    								<TD class=column5 align="center">����ʧ�ܴ�����</TD>
    							</tr>
    							<s:iterator value="cloudNetInfoList" var="cloudNetInfo">
    								<tr align="center" bgcolor="#FFFFFF">
    									<s:if test="accessType==1">
    										<TD align="center"><s:property value="pvc" /></TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="vlanid" /></TD>
    									</s:else>

    									<s:if
    										test="#cloudNetInfo.connType=='N/A' || #cloudNetInfo.connType=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="connType" /></TD>
    									</s:else>
    									<s:if
    										test="#cloudNetInfo.bindPort=='N/A' || #cloudNetInfo.bindPort=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="bindPort" /></TD>
    									</s:else>
    									<!--
    									<s:if test="#cloudNetInfo.status=='N/A' || #cloudNetInfo.status=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="status"/></TD>
    									</s:else>
    									 -->
    									<TD align="center"><s:property value="status" /></TD>
    									<s:if
    										test="#cloudNetInfo.dns=='N/A' || #cloudNetInfo.dns=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="dns" /></TD>
    									</s:else>

    									<s:if
    										test="#cloudNetInfo.ip=='N/A' || #cloudNetInfo.ip=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="ip" /></TD>
    									</s:else>
    									<!-- ipv6 dns -->
    									<s:if
    										test="#cloudNetInfo.dns_ipv6=='N/A' || #cloudNetInfo.dns_ipv6=='null' || #cloudNetInfo.dns_ipv6=='NULL'">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="dns_ipv6" /></TD>
    									</s:else>
    									<!-- ipv6 dns -->
    									<!-- ipv6 ip -->
    									<s:if
    										test="#cloudNetInfo.ip_ipv6=='N/A' || #cloudNetInfo.ip_ipv6=='null' || #cloudNetInfo.ip_ipv6=='NULL'">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="ip_ipv6" /></TD>
    									</s:else>
    									<!-- ipv6 ip -->
    									<s:if
    										test="#cloudNetInfo.username=='N/A' || #cloudNetInfo.username=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="username" /></TD>
    									</s:else>
    									<s:if
    										test="#cloudNetInfo.connError=='N/A' || #cloudNetInfo.connError=='null' ">
    										<TD align="center">-</TD>
    									</s:if>
    									<s:else>
    										<TD align="center"><s:property value="connError" /></TD>
    									</s:else>
    								</tr>
    							</s:iterator>
    						</TABLE>
    					</div>
    				</td>
    			</tr>
    		</table>
    	</TD>
    </tr>
    <tr height="20">
    	<td colspan="1" width="15" class=column></td>
    </tr>

    <%} %>
	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:iptvInfo();" stytle="CURSOR:hand;">
							&nbsp;&nbsp;IPTV&nbsp; <IMG name="imgiptv"
							SRC="../../images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0"
							ALT="">
					</a></td>
				</tr>
				<tr align="left" id="triptv" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="diviptv">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">PVC/VLAN</TD>
									<%if (LipossGlobals.inArea("js_dx")) { %>
									<TD class=column5 align="center">�鲥VLAN</TD>
									<%} %>
									<%if (LipossGlobals.inArea("cq_dx")) { %>
									<TD class=column5 align="center">�鲥VLAN</TD>
									<TD class=column5 align="center">����snoopingʹ��</TD>
									<%} %>
									<TD class=column5 align="center">����״̬</TD>
									<TD class=column5 align="center">��������</TD>
									<TD class=column5 align="center">�󶨶˿�</TD>
									<%if (LipossGlobals.inArea("hb_lt")) { %>
									<!-- 											<TD class=column5 align="center"> -->
									<!-- 												DNS(IPV4) -->
									<!-- 											</TD> -->
									<TD class=column5 align="center">IP��ַ(IPV4)</TD>
									<!-- 											<TD class=column5 align="center"> -->
									<!-- 												DNS(IPV6) -->
									<!-- 											</TD> -->
									<TD class=column5 align="center">IP��ַ(IPV6)</TD>
									<TD class=column5 align="center">IPOE�˺�</TD>
									<TD class=column5 align="center">����ʧ�ܴ�����</TD>
									<TD class=column5 align="center">����</TD>
									<TD class=column5 align="center">����</TD>
									<%} %>
								</tr>
								<s:iterator value="iptvInfoList" var="wideNetInfo">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if test="accessType==1">
											<TD align="center"><s:property value="pvc" /></TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="vlanid" /></TD>
										</s:else>
										<%if (LipossGlobals.inArea("js_dx")) { %>
										<s:if
											test="#wideNetInfo.multicast_vlan=='N/A' || #wideNetInfo.multicast_vlan=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="multicast_vlan" />
											</TD>
										</s:else>
										<%} %>
										<%if (LipossGlobals.inArea("cq_dx")) { %>
										<s:if
											test="#wideNetInfo.multicast_vlan=='N/A' || #wideNetInfo.multicast_vlan=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="multicast_vlan" />
											</TD>
										</s:else>
										<s:if
											test="#wideNetInfo.snooping_enable=='N/A' || #wideNetInfo.snooping_enable=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="snooping_enable" />
											</TD>
										</s:else>
										<%} %>
										<s:if
											test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="status" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="connType" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.bindPort=='N/A' || #wideNetInfo.bindPort=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="bindPort" /></TD>
										</s:else>


										<%if (LipossGlobals.inArea("hb_lt")) { %>

										<%-- 												<s:if test="#wideNetInfo.dns=='N/A' || #wideNetInfo.dns=='null' "> --%>
										<!-- 													<TD align="center"> -->
										<!-- 														- -->
										<!-- 													</TD> -->
										<%-- 												</s:if> --%>
										<%-- 												<s:else> --%>
										<!-- 													<TD align="center"> -->
										<%-- 														<s:property value="dns" /> --%>
										<!-- 													</TD> -->
										<%-- 												</s:else> --%>

										<s:if
											test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ip" /></TD>
										</s:else>
										<!-- ipv6 dns -->
										<%-- 												<s:if test="#wideNetInfo.dns_ipv6=='N/A' || #wideNetInfo.dns_ipv6=='null' || #wideNetInfo.dns_ipv6=='NULL'"> --%>
										<!-- 													<TD align="center"> -->
										<!-- 														- -->
										<!-- 													</TD> -->
										<%-- 												</s:if> --%>
										<%-- 												<s:else> --%>
										<!-- 													<TD align="center"> -->
										<%-- 														<s:property value="dns_ipv6" /> --%>
										<!-- 													</TD> -->
										<%-- 												</s:else> --%>
										<!-- ipv6 dns -->
										<!-- ipv6 ip -->
										<s:if
											test="#wideNetInfo.ip_ipv6=='N/A' || #wideNetInfo.ip_ipv6=='null' || #wideNetInfo.ip_ipv6=='NULL'">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ip_ipv6" /></TD>
										</s:else>
										<!-- ipv6 ip -->
										<s:if
											test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="username" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.connError=='N/A' || #wideNetInfo.connError=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="connError" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.mask=='N/A' || #wideNetInfo.mask=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="mask" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.gateway=='N/A' || #wideNetInfo.gateway=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="gateway" /></TD>
										</s:else>
										<%} %>
									</tr>
								</s:iterator>
								<tr align="center" bgcolor="#FFFFFF">
									<%if (LipossGlobals.inArea("js_dx")) { %>
									<TD align="center" colspan="5">
										<%} else if (LipossGlobals.inArea("hb_lt")){%>

									<TD align="center" colspan="10">
										<%} else {%>

									<TD align="center" colspan="4">
										<%}%> <s:property value="iptvMsg" />
									</TD>
								</tr>
							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>
	<tr>
		<td>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:voipInfo();" stytle="CURSOR:hand;">
							&nbsp;&nbsp;VOIP&nbsp; <IMG name="imgvoip"
							SRC="../../images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0"
							ALT="">
					</a></td>
				</tr>
				<tr align="left" id="trvoip" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="divvoip">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">�����˿�</TD>
									<TD class=column5 align="center">Э������</TD>
									<TD class=column5 align="center">PVC/VLAN</TD>
									<TD class=column5 align="center">DHCP��ַ</TD>
									<TD class=column5 align="center">��������</TD>
									<TD class=column5 align="center">����״̬</TD>
									<s:if test="voipProtocalTypeStr=='H.248'">
										<%if (LipossGlobals.inArea("hb_lt")) { %>
										<TD class=column5 align="center">H248�˿�״̬</TD>
										<%}%>
										<TD class=column5 align="center">��������ַ</TD>
										<TD class=column5 align="center">���÷�����</TD>
										<TD class=column5 align="center">�ն˱�ʶ����</TD>
										<TD class=column5 align="center">�����ʶ</TD>
									</s:if>
									<s:else>
										<TD class=column5 align="center">��������ַ</TD>
										<TD class=column5 align="center">���÷�����</TD>
										<TD class=column5 align="center">ע��״̬</TD>
										<TD class=column5 align="center">ע���ʺ�</TD>
										<!-- ��������
													<TD class=column5 align="center">
														����
													</TD>
												-->
									</s:else>
								</tr>
								<s:iterator value="voipInfoList" var="wideNetInfo">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if
											test="#wideNetInfo.line=='N/A' || #wideNetInfo.line=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="line" /></TD>
										</s:else>
										<TD align="center"><s:property
												value="voipProtocalTypeStr" /></TD>
										<s:if test="accessType==1">
											<TD align="center"><s:property value="pvc" /></TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="vlanid" /></TD>
										</s:else>


										<s:if
											test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ip" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="connType" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="status" /></TD>
										</s:else>
										<s:if test="voipProtocalTypeStr=='H.248'">
											<%if (LipossGlobals.inArea("hb_lt")) { %>
											<s:if
												test="#wideNetInfo.interfaceState=='N/A' ||  #wideNetInfo.interfaceState=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="interfaceState" />
												</TD>
											</s:else>
											<%}%>
											<s:if
												test="#wideNetInfo.media_gateway_controler=='N/A' ||  #wideNetInfo.media_gateway_controler=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property
														value="media_gateway_controler" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.media_gateway_controler_2=='N/A' ||  #wideNetInfo.media_gateway_controler_2=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property
														value="media_gateway_controler_2" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.h248_device_id=='N/A' ||  #wideNetInfo.h248_device_id=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="h248_device_id" />
												</TD>
											</s:else>
											<s:if
												test="#wideNetInfo.physical_term_id=='N/A' ||  #wideNetInfo.physical_term_id=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="physical_term_id" />
												</TD>
											</s:else>
										</s:if>
										<s:else>
											<s:if
												test="#wideNetInfo.prox_serv=='N/A' || #wideNetInfo.prox_serv=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="prox_serv" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.prox_serv_2=='N/A' || #wideNetInfo.prox_serv_2=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="prox_serv_2" />
												</TD>
											</s:else>
											<s:if test="#wideNetInfo.regist_status=='Up'">
												<TD align="center">��ע��</TD>
											</s:if>
											<s:else>
												<TD align="center">δע��</TD>
											</s:else>
											<s:if
												test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="username" /></TD>
											</s:else>

											<!-- �������룬����й¶
														<s:if
															test="#wideNetInfo.password=='N/A' || #wideNetInfo.password=='null' ">
															<TD align="center">
																-
															</TD>
														</s:if>
														<s:else>
															<TD align="center">
																<s:property value="password" />
															</TD>
														</s:else>
													-->
										</s:else>
									</tr>
								</s:iterator>
								<tr align="center" bgcolor="#FFFFFF">
									<TD align="center" colspan="11"><s:property
											value="voipMsg" /></TD>
								</tr>
							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>
	<s:if test='accessType=="3" || accessType=="4" '>
		<tr>
			<td>
				<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr align="left" height="25">
						<td colspan="5" class="green_title_left"><a
							href="javascript:ponInfo();" style="CURSOR: hand;"> <s:if
									test="pon_type=='EPON'">
								EPON��Ϣ
							</s:if> <s:elseif test="pon_type=='GPON'">
								GPON��Ϣ
							</s:elseif> <s:else>
								PON��Ϣ
							</s:else> <IMG name="imgponInfo" SRC="../../images/up_enabled.gif"
								WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a></td>
					</tr>
					<tr align="left" id="trponInfo" STYLE="display:">
						<td colspan="1" width="15" class=column></td>
						<td colspan="4" bgcolor=#999999>
							<div id="divponInfo">
								<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
									<s:if test="ponInfoOBJArr!=null">
										<s:iterator value="ponInfoOBJArr" var="ponInfoOBJ">
											<tr>
												<td bgcolor=#999999>
													<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<tr align="center" bgcolor="#FFFFFF">
															<TD class=column5 align="center">��·״̬</TD>
															<TD class=column5 align="center">����⹦��<%if(LipossGlobals.inArea("sd_lt")){ %>
																(0.1��W) <%} else{%> (dBm) <%} %>
															</TD>
															<TD class=column5 align="center">���չ⹦��<%if(LipossGlobals.inArea("sd_lt")){ %>
																(0.1��W) <%} else{%> (dBm) <%} %>
															</TD>
															<%if(LipossGlobals.inArea("jl_dx")){ %>
															<TD class=column5 align="center">��˥(dB)</TD>
															<%}%>
															<%if(LipossGlobals.inArea("jx_dx")){ %>
															<TD class=column5 align="center">�����¶�(��)</TD>
															<TD class=column5 align="center">�����ѹ(V)</TD>
															<TD class=column5 align="center">ƫ�õ�·(mA)</TD>
															<%} else{%>
															<TD class=column5 align="center">�����¶�(1/256��)</TD>
															<TD class=column5 align="center">�����ѹ(100΢��)</TD>
															<TD class=column5 align="center">ƫ�õ���(2΢��)</TD>
															<%} %>
														</tr>
														<tr align="center" bgcolor="#FFFFFF">
															<s:if
																test="#ponInfoOBJ.status=='N/A' || #ponInfoOBJ.status=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="status" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.txpower=='N/A' || #ponInfoOBJ.txpower=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="txpower" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rxpower=='N/A' || #ponInfoOBJ.rxpower=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="rxpower" />
																</TD>
															</s:else>
															<%if(LipossGlobals.inArea("jl_dx")){ %>
															<s:if
																test="#ponInfoOBJ.rxpower=='N/A' || #ponInfoOBJ.rxpower=='null' || #ponInfoOBJ.txpower=='N/A' || #ponInfoOBJ.txpower=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="subpower" />
																</TD>
															</s:else>
															<%}%>
															<s:if
																test="#ponInfoOBJ.transceiverTemperature=='N/A' || #ponInfoOBJ.transceiverTemperature=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="transceiverTemperature" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.supplyVottage=='N/A' || #ponInfoOBJ.supplyVottage=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="supplyVottage" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.biasCurrent=='N/A' || #ponInfoOBJ.biasCurrent=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="biasCurrent" />
																</TD>
															</s:else>
														</tr>
													</TABLE>
												</td>
											</tr>
											<tr>
												<td bgcolor=#999999>
													<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<tr align="center" bgcolor="#FFFFFF">
															<TD class=column5 align="center">�����ֽ���</TD>
															<TD class=column5 align="center">�����ֽ���</TD>
															<TD class=column5 align="center">����֡����</TD>
															<TD class=column5 align="center">����֡����</TD>
															<TD class=column5 align="center">���͵���֡��</TD>
															<TD class=column5 align="center">���յ���֡��</TD>
															<TD class=column5 align="center">�����鲨֡��</TD>
															<TD class=column5 align="center">�����鲨֡��</TD>
															<TD class=column5 align="center">���͹㲥��֡��</TD>
															<TD class=column5 align="center">���չ㲥��֡��</TD>
															<TD class=column5 align="center">����FEC����֡��</TD>
															<TD class=column5 align="center">����HEC����֡��</TD>
															<TD class=column5 align="center">���ͷ���֡��</TD>
															<TD class=column5 align="center">����PAUSE������֡��</TD>
															<TD class=column5 align="center">����PAUSE������֡��</TD>
														</tr>
														<tr align="center" bgcolor="#FFFFFF">
															<s:if
																test="#ponInfoOBJ.bytesSent=='N/A' || #ponInfoOBJ.bytesSent=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="bytesSent" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.bytesReceived=='N/A' || #ponInfoOBJ.bytesReceived=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="bytesReceived" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.packetsSent=='N/A' || #ponInfoOBJ.packetsSent=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="packetsSent" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.packetsReceived=='N/A' || #ponInfoOBJ.packetsReceived=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="packetsReceived" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.sunicastPackets=='N/A' || #ponInfoOBJ.sunicastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="sunicastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.runicastPackets=='N/A' || #ponInfoOBJ.runicastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="runicastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.smulticastPackets=='N/A' || #ponInfoOBJ.smulticastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="smulticastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rmulticastPackets=='N/A' || #ponInfoOBJ.rmulticastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="rmulticastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.sbroadcastPackets=='N/A' || #ponInfoOBJ.sbroadcastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="sbroadcastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rbroadcastPackets=='N/A' || #ponInfoOBJ.rbroadcastPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="rbroadcastPackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.fecError=='N/A' || #ponInfoOBJ.fecError=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="fecError" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.hecError=='N/A' || #ponInfoOBJ.hecError=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="hecError" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.dropPackets=='N/A' || #ponInfoOBJ.dropPackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property value="dropPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.spausePackets=='N/A' || #ponInfoOBJ.spausePackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="spausePackets" /></TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rpausePackets=='N/A' || #ponInfoOBJ.rpausePackets=='null' ">
																<TD align="center">-</TD>
															</s:if>
															<s:else>
																<TD align="center"><s:property
																		value="rpausePackets" /></TD>
															</s:else>
														</tr>
													</TABLE>
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<s:else>
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center" colspan="10"><s:property
													value="wireMsg" /></TD>
										</tr>
									</s:else>
								</TABLE>
							</div>
						</td>
					</tr>
				</TABLE>
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<td>
				<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr align="left" height="25">
						<td colspan="5" class="green_title_left"><a
							href="javascript:wireinfoInfo();" stytle="CURSOR:hand;"> ��·��Ϣ
								<IMG name="imgwireinfo" SRC="../../images/up_enabled.gif"
								WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a></td>
					</tr>
					<tr align="left" id="trwireinfo" STYLE="display:">
						<td colspan="1" width="15" class=column></td>
						<td colspan="4" bgcolor=#999999>
							<div id="divwireinfo">
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<tr align="center" bgcolor="#FFFFFF">
										<TD class=column5 align="center">��·״̬</TD>
										<TD class=column5 align="center">��·Э��</TD>
										<TD class=column5 align="center">������·˥��(dB)</TD>
										<TD class=column5 align="center">������·˥��(dB)</TD>
										<TD class=column5 align="center">��������(Kbps)</TD>
										<TD class=column5 align="center">��������(Kbps)</TD>
										<TD class=column5 align="center">��������ԣ��(dB)</TD>
										<TD class=column5 align="center">��������ԣ��(dB)</TD>
										<TD class=column5 align="center">��֯���</TD>
										<TD class=column5 align="center">����·��</TD>
									</tr>
									<s:iterator value="wireInfoObjArr" var="wideNetInfo">
										<tr align="center" bgcolor="#FFFFFF">
											<s:if
												test="#wideNetInfo.wireStatus=='N/A' || #wideNetInfo.wireStatus=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="wireStatus" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.modulationType=='N/A' || #wideNetInfo.modulationType=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="modulationType" />
												</TD>
											</s:else>
											<s:if
												test="#wideNetInfo.upstreamAttenuation=='N/A' || #wideNetInfo.upstreamAttenuation=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property
														value="upstreamAttenuation" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.downstreamAttenuation=='N/A' || #wideNetInfo.downstreamAttenuation=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property
														value="downstreamAttenuation" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.upstreamMaxRate=='N/A' || #wideNetInfo.upstreamMaxRate=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="upstreamMaxRate" />
												</TD>
											</s:else>
											<s:if
												test="#wideNetInfo.downstreamMaxRate=='N/A' || #wideNetInfo.downstreamMaxRate=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property
														value="downstreamMaxRate" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.upNoise=='N/A' || #wideNetInfo.upNoise=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="upNoise" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.downNoise=='N/A' || #wideNetInfo.downNoise=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="downNoise" /></TD>
											</s:else>
											<s:if
												test="#wideNetInfo.interleaveDepth=='N/A' || #wideNetInfo.interleaveDepth=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="interleaveDepth" />
												</TD>
											</s:else>
											<s:if
												test="#wideNetInfo.dataPath=='N/A' || #wideNetInfo.dataPath=='null' ">
												<TD align="center">-</TD>
											</s:if>
											<s:else>
												<TD align="center"><s:property value="dataPath" /></TD>
											</s:else>
										</tr>
									</s:iterator>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="10"><s:property
												value="wireMsg" /></TD>
									</tr>
								</TABLE>
							</div>
						</td>
					</tr>
				</TABLE>
			</td>
		</tr>
	</s:else>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>
	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:tr069Info();" stytle="CURSOR:hand;"> ����ͨ����Ϣ <IMG
							name="imgtr069" SRC="../../images/up_enabled.gif" WIDTH="7"
							HEIGHT="9" BORDER="0" ALT="">
					</a></td>
				</tr>
				<tr align="left" id="trtr069" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="divtr069">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">PVC/VLAN</TD>
									<TD class=column5 align="center">URL</TD>
									<TD class=column5 align="center">��¼�û���</TD>
									<TD class=column5 align="center">��¼����</TD>
									<TD class=column5 align="center">DHCP��ַ</TD>
									<!--  <TD class=column5 align="center">�����ն���֤�û���</TD>
									<TD class=column5 align="center">�����ն���֤����</TD>-->
								</tr>
								<tr align="center" bgcolor="#FFFFFF">
									<s:if test="#gwTr069OBJ.pvc=='N/A' || #gwTr069OBJ.pvc=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property value="gwTr069OBJ.pvc" />
										</TD>
									</s:else>
									<s:if test="#gwTr069OBJ.url=='N/A' || #gwTr069OBJ.url=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property value="gwTr069OBJ.url" />
										</TD>
									</s:else>
									<s:if
										test="#gwTr069OBJ.cpeUsername=='N/A' || #gwTr069OBJ.cpeUsername=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property
												value="gwTr069OBJ.cpeUsername" /></TD>
									</s:else>
									<s:if
										test="#gwTr069OBJ.cpePasswd=='N/A' || #gwTr069OBJ.cpePasswd=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property
												value="gwTr069OBJ.cpePasswd" /></TD>
									</s:else>
									<s:if
										test="#gwTr069OBJ.loopbackIp=='N/A' || #gwTr069OBJ.loopbackIp=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property
												value="gwTr069OBJ.loopbackIp" /></TD>
									</s:else>
								</tr>
								<tr align="center" bgcolor="#FFFFFF">
									<TD align="center" colspan="5"><s:property
											value="tr069Msg" /></TD>
								</tr>
							</TABLE>
						</div>
					</td>
				</tr>
			</TABLE>
		</td>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>
	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:lanInfo();" stytle="CURSOR:hand;"> LAN����Ϣ <IMG
							name="imglan" SRC="../../images/up_enabled.gif" WIDTH="7"
							HEIGHT="9" BORDER="0" ALT="">
					</a></td>
				</tr>
				<tr align="left" id="trlan" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="divlan">
							<table border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column align="right" nowrap width="12%">DHCP��ַ��
									</TD>
									<TD class=column align="right" nowrap width="12%">��ʼ��ַ:</TD>
									<TD width="32%"><s:if
											test="#gwLanHostconfMap.min_addr=='N/A' || #gwLanHostconfMap.min_addr=='null' ">
											-
										</s:if> <s:else>
											<s:property value="gwLanHostconfMap.min_addr" />
										</s:else></TD>
									<TD class=column align="right" width="12%">������ַ:</TD>
									<TD width="32%"><s:if
											test="#gwLanHostconfMap.max_addr=='N/A' || #gwLanHostconfMap.max_addr=='null' ">
											-
										</s:if> <s:else>
											<s:property value="gwLanHostconfMap.max_addr" />
										</s:else></TD>
								</tr>
							</table>
							<table border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">����</TD>
									<!-- <TD class=column5 align="center">MAC��ַ</TD> -->
									<TD class=column5 align="center">��������</TD>
									<TD class=column5 align="center">����״̬</TD>
									<TD class=column5 align="center">�����ֽ���</TD>
									<TD class=column5 align="center">�����ֽ���</TD>
									<TD class=column5 align="center">�հ���</TD>
									<TD class=column5 align="center">������</TD>
								</tr>
								<s:iterator value="lanEthList" var="lanEthList">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if
											test="#lanEthList.lan_eth_id=='N/A' || #lanEthList.lan_eth_id=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center">LAN <s:property value="lan_eth_id" />
											</TD>
										</s:else>
										<!-- <s:if test="#lanEthList.mac_address=='N/A' || #lanEthList.mac_address=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="mac_address"/></TD>
										</s:else>
										 -->
										<s:if
											test="#lanEthList.max_bit_rate=='N/A' || #lanEthList.max_bit_rate=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="max_bit_rate" />
											</TD>
										</s:else>
										<s:if
											test="#lanEthList.status=='N/A' || #lanEthList.status=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="status" /></TD>
										</s:else>
										<s:if
											test="#lanEthList.byte_rece=='N/A' || #lanEthList.byte_rece=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="byte_rece" /></TD>
										</s:else>
										<s:if
											test="#lanEthList.byte_sent=='N/A' || #lanEthList.byte_sent=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="byte_sent" /></TD>
										</s:else>
										<s:if
											test="#lanEthList.pack_rece=='N/A' || #lanEthList.pack_rece=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="pack_rece" /></TD>
										</s:else>
										<s:if
											test="#lanEthList.pack_sent=='N/A' || #lanEthList.pack_sent=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="pack_sent" /></TD>
										</s:else>
									</tr>
								</s:iterator>
								<tr align="center" bgcolor="#FFFFFF">
									<TD align="center" colspan="8"><s:property value="lanMsg" />
									</TD>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</TABLE>
		</td>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column></td>
	</tr>
	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="5" class="green_title_left"><a
						href="javascript:wlanInfo();" stytle="CURSOR:hand;"> WLAN����Ϣ <IMG
							name="imgwlan" SRC="../../images/up_enabled.gif" WIDTH="7"
							HEIGHT="9" BORDER="0" ALT="">
					</a></td>
				</tr>
				<tr align="left" id="trwlan" STYLE="display:">
					<td colspan="1" width="15" class=column></td>
					<td colspan="4" bgcolor=#999999>
						<div id="divwlan">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">SSID����</TD>
									<TD class=column5 align="center">��ǰ����</TD>
									<TD class=column5 align="center">�ŵ�</TD>
									<TD class=column5 align="center">����״̬</TD>
									<TD class=column5 align="center">�����豸��</TD>
									<TD class=column5 align="center">�Ƿ�����</TD>
									<TD class=column5 align="center">����</TD>
									<TD class=column5 align="center">�㲥</TD>
									<TD class=column5 align="center">���ܷ�ʽ</TD>
									<TD class=column5 align="center">802.11����ģʽ</TD>
								</tr>
								<s:iterator value="wlanList" var="wideNetInfo">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if
											test="#wideNetInfo.ssid=='N/A' || #wideNetInfo.ssid=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="ssid" /></TD>
										</s:else>
										<!--
										<s:if test="#wideNetInfo.powervalue=='N/A' || #wideNetInfo.powervalue=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="powervalue"/></TD>
										</s:else>
										 -->
										<TD align="center"><s:property value="powervalue" /></TD>
										<s:if
											test="#wideNetInfo.channel_in_use=='N/A' || #wideNetInfo.channel_in_use=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="channel_in_use" />
											</TD>
										</s:else>
										<s:if
											test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="status" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.associatedNum=='N/A' || #wideNetInfo.associatedNum=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><a
												href="javascript:detailDevice('<s:property value="device_id"/>','<s:property value="lan_id"/>','<s:property value="lan_wlan_id"/>');">
													<s:property value="associated_num" />
											</a></TD>
										</s:else>
										<!--
										<s:if test="#wideNetInfo.enable=='N/A' || #wideNetInfo.enable=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.enable==1">
											<TD align="center">��</TD>
										</s:if>
										<s:else>
											<TD align="center">��</TD>
										</s:else>
										<!--
										</s:else>
										<s:if test="#wideNetInfo.hide=='N/A' || #wideNetInfo.hide=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.hide==1">
											<TD align="center">��</TD>
										</s:if>
										<s:else>
											<TD align="center">��</TD>
										</s:else>
										<!--
										</s:else>
										<s:if test="#wideNetInfo.radio_enable=='N/A' || #wideNetInfo.radio_enable=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.radio_enable==1">
											<TD align="center">��</TD>
										</s:if>
										<s:else>
											<TD align="center">��</TD>
										</s:else>
										<!-- </s:else>-->
										<s:if
											test="#wideNetInfo.beacontype=='N/A' || #wideNetInfo.beacontype=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="beacontype" /></TD>
										</s:else>
										<s:if
											test="#wideNetInfo.standard=='N/A' || #wideNetInfo.standard=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="standard" /></TD>
										</s:else>
									</tr>
								</s:iterator>
								<tr align="center" bgcolor="#FFFFFF">
									<TD align="center" colspan="10"><s:property
											value="wlanMsg" /></TD>
								</tr>
							</TABLE>
						</div>
					</td>
				</tr>
			</TABLE>
		</td>
	</tr>
</table>
