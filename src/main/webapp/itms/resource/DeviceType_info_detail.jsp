<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/Calendar.js"></SCRIPT>
<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../css/css_ico.css" type="text/css">
<link rel="stylesheet" href="../../css/user-defined.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/toolbars.js"></SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#000000>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<TH>�豸�汾������Ϣ</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<s:iterator value="deviceList">
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">�豸���̣�</TD>
									<TD width="30%"><s:property value="vendor_add" /></TD>
									<TD class=column align="right" width="20%">�豸�ͺ� ��</TD>
									<TD width="30%"><s:property value="device_model" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">�ض��汾��</TD>
									<TD width="30%"><s:property value="specversion" /></TD>
									<TD class=column align="right" width="20%">Ӳ���汾��</TD>
									<TD width="30%"><s:property value="hardwareversion" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">����汾��</TD>
									<TD><s:property value="softwareversion" /></TD>
									<TD class=column align="right">�Ƿ���ˣ�</TD>
									<TD><s:if test="is_check==1">
										�������
									</s:if> <s:if test="is_check==-1">
											<font color='red'>δ���</font>
										</s:if></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">�豸���ͣ�</TD>
									<TD>
										<!--< s:if test="rela_dev_type_id==1">
										e8-b
									</s:if> <s:if test="rela_dev_type_id==2">
										e8-c
									</s:if> 
									 <s:if test="rela_dev_type_id==3">
										A8-B
									</s:if> 
									 <s:if test="rela_dev_type_id==4">
										A8-C
									</s:if>--> <s:property value="rela_dev_type_name" />
									<TD class=column align="right">���з�ʽ��</TD>
									<TD><s:property value="type_name" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">����Э�飺</TD>
									<TD><s:property value="server_type" /></TD>
									<TD class=column align="right">�ն˹��</TD>
									<TD><s:property value="specName" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">�豸IP֧�ַ�ʽ��</TD>
									<TD><s:if test='0==ip_model_type'>
									IPv4
									</s:if> 
									<s:elseif test="1==ip_model_type">
									IPv4��IPv6
									</s:elseif>
									<s:elseif test="2==ip_model_type">
									DS-Lite
									</s:elseif>
									<s:elseif test="3==ip_model_type">
									LAFT6
									</s:elseif>
									<s:elseif test="4==ip_model_type">
									��IPV6
									</s:elseif>
									</TD>
									<TD class=column align="right">�Ƿ�Ϊ�淶�汾:</TD>
									<TD><s:if test='1==is_normal'>
									��
								</s:if> <s:else>
									��
								</s:else></TD>
								</TR>
								<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
								<TR bgcolor="#FFFFFF">
								    <%if("sd_dx".equals(InstArea)){%>
									<TD class=column align="right">�Ƿ�֧��ǧ�׿����</TD>
									<%}else{ %>
									<TD class=column align="right">�Ƿ�֧�ְ��׿����</TD>
									<%} %>
									<TD><s:if test='1==mbbroadband'>
									��
								</s:if> <s:elseif test='2==mbbroadband'>
									��
								</s:elseif></TD>
									<TD class=column align="right">�Ƿ�֧��IPV6��</TD>
									<TD><s:if test='0==ip_type'>
									��
								</s:if> <s:else>
									��
								</s:else></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">�Ƿ�֧�ֻ����������ã�</TD>
									<TD><s:if test='1==zeroconf'>
									��
								</s:if> <s:elseif test='2==zeroconf'>
									��
								</s:elseif></TD>
									<TD class=column align="right">�汾����ʱ�䣺</TD>
									<TD><s:property value="versionttime" /></TD>
								</TR>
								 <%if("sd_dx".equals(InstArea)){%>
								 <TR bgcolor="#FFFFFF">
									<TD class=column align="right">�豸�汾����</TD>
									<TD><s:property value="device_version_type" /></TD>
									<TD class=column align="right">�Ƿ�֧�ֲ���</TD>
									<TD>
									    <s:if test='1==is_speedtest'>
                                            ��
                                        </s:if> <s:elseif test='0==is_speedtest'>
                                            ��
                                        </s:elseif>
                                        <s:else>δά��</s:else>
                                    </TD>
								</TR>
								 <%} %>
								<%if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "js_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))) { %>
										<TR>
											<TD class=column align="right">�Ƿ�֧��wifi��ͨ��</TD>
											<TD colspan=3 bgcolor="#FFFFFF">
												<s:if test='1==is_awifi'>
													��
												</s:if>
												<s:elseif test='2==is_awifi'>
													��
												</s:elseif>
										</TD>
									</TR>
								<%} %>
								<%if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "nmg_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))) { %>
									<TR>
										<TD class=column align="right">�Ƿ�֧��wifi��</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test='1==wifi'>
												��
											</s:if>
											<s:elseif test='0==wifi'>
												��
											</s:elseif>
											<s:else>��</s:else>
										</TD>
										<TD class=column align="right">wifi������</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==wifi_ability">802.11b</s:if>
											<s:elseif test="2==wifi_ability">802.11b/g</s:elseif> 
											<s:elseif test="3==wifi_ability">802.11b/g/n</s:elseif> 
											<s:elseif test="4==wifi_ability">802.11b/g/n/ac</s:elseif>
											<s:else>��</s:else>
										</TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">ǧ�׿�������</TD>
										<TD width="30%"><s:property value="gigabitnum" /></TD>
										<TD class=column align="right" width="20%">���׿�������</TD>
										<TD width="30%"><s:property value="mbitnum" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">������������</TD>
										<TD width="30%"><s:property value="voipnum" /></TD>
										<TD class=column align="right" width="20%">WIFI�Ƿ�˫Ƶ��</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==is_wifi_double">��</s:if>
											<s:elseif test="0==is_wifi_double">��</s:elseif>
											<s:else>��</s:else>
										</TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">�ںϹ��ܣ�</TD>
										<TD width="30%"><s:property value="fusion_ability" /></TD>
										<TD class=column align="right" width="20%">�ں��ն˽��뷽ʽ��</TD>
										<TD width="30%"><s:property value="terminal_access_method" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">��è֧��������ʣ�MB/S����</TD>
										<TD width="30%"><s:property value="devmaxspeed" /></TD>
										<TD class=column align="right" width="20%">�豸�汾����:</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==device_version_type">E8-C</s:if>
											<s:elseif test="2==device_version_type">PON�ں�</s:elseif> 
											<s:elseif test="3==device_version_type">10GPON</s:elseif> 
											<s:elseif test="4==device_version_type">��������</s:elseif>
											<s:elseif test="5==device_version_type">��������1.0</s:elseif>
											<s:elseif test="6==device_version_type">��������2.0</s:elseif>
											<s:elseif test="7==device_version_type">��������3.0</s:elseif>
											<s:else>��</s:else>
										</TD>
									</TR>
								<%} %>
							</s:iterator>
							<TR>
								<TD colspan="4" align="right" class=foot><INPUT
									TYPE="button" value=" �� �� " class=btn
									onclick="javascript:window.close()"> &nbsp;&nbsp;</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
