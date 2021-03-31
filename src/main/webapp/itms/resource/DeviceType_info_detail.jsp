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
											<TH>设备版本基本信息</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<s:iterator value="deviceList">
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">设备厂商：</TD>
									<TD width="30%"><s:property value="vendor_add" /></TD>
									<TD class=column align="right" width="20%">设备型号 ：</TD>
									<TD width="30%"><s:property value="device_model" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%">特定版本：</TD>
									<TD width="30%"><s:property value="specversion" /></TD>
									<TD class=column align="right" width="20%">硬件版本：</TD>
									<TD width="30%"><s:property value="hardwareversion" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">软件版本：</TD>
									<TD><s:property value="softwareversion" /></TD>
									<TD class=column align="right">是否审核：</TD>
									<TD><s:if test="is_check==1">
										经过审核
									</s:if> <s:if test="is_check==-1">
											<font color='red'>未审核</font>
										</s:if></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">设备类型：</TD>
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
									<TD class=column align="right">上行方式：</TD>
									<TD><s:property value="type_name" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">语音协议：</TD>
									<TD><s:property value="server_type" /></TD>
									<TD class=column align="right">终端规格：</TD>
									<TD><s:property value="specName" /></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">设备IP支持方式：</TD>
									<TD><s:if test='0==ip_model_type'>
									IPv4
									</s:if> 
									<s:elseif test="1==ip_model_type">
									IPv4和IPv6
									</s:elseif>
									<s:elseif test="2==ip_model_type">
									DS-Lite
									</s:elseif>
									<s:elseif test="3==ip_model_type">
									LAFT6
									</s:elseif>
									<s:elseif test="4==ip_model_type">
									纯IPV6
									</s:elseif>
									</TD>
									<TD class=column align="right">是否为规范版本:</TD>
									<TD><s:if test='1==is_normal'>
									是
								</s:if> <s:else>
									否
								</s:else></TD>
								</TR>
								<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
								<TR bgcolor="#FFFFFF">
								    <%if("sd_dx".equals(InstArea)){%>
									<TD class=column align="right">是否支持千兆宽带：</TD>
									<%}else{ %>
									<TD class=column align="right">是否支持百兆宽带：</TD>
									<%} %>
									<TD><s:if test='1==mbbroadband'>
									是
								</s:if> <s:elseif test='2==mbbroadband'>
									否
								</s:elseif></TD>
									<TD class=column align="right">是否支持IPV6：</TD>
									<TD><s:if test='0==ip_type'>
									否
								</s:if> <s:else>
									是
								</s:else></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">是否支持机顶盒零配置：</TD>
									<TD><s:if test='1==zeroconf'>
									是
								</s:if> <s:elseif test='2==zeroconf'>
									否
								</s:elseif></TD>
									<TD class=column align="right">版本定版时间：</TD>
									<TD><s:property value="versionttime" /></TD>
								</TR>
								 <%if("sd_dx".equals(InstArea)){%>
								 <TR bgcolor="#FFFFFF">
									<TD class=column align="right">设备版本类型</TD>
									<TD><s:property value="device_version_type" /></TD>
									<TD class=column align="right">是否支持测速</TD>
									<TD>
									    <s:if test='1==is_speedtest'>
                                            是
                                        </s:if> <s:elseif test='0==is_speedtest'>
                                            否
                                        </s:elseif>
                                        <s:else>未维护</s:else>
                                    </TD>
								</TR>
								 <%} %>
								<%if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "js_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))) { %>
										<TR>
											<TD class=column align="right">是否支持wifi开通：</TD>
											<TD colspan=3 bgcolor="#FFFFFF">
												<s:if test='1==is_awifi'>
													是
												</s:if>
												<s:elseif test='2==is_awifi'>
													否
												</s:elseif>
										</TD>
									</TR>
								<%} %>
								<%if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "nmg_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))) { %>
									<TR>
										<TD class=column align="right">是否支持wifi：</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test='1==wifi'>
												是
											</s:if>
											<s:elseif test='0==wifi'>
												否
											</s:elseif>
											<s:else>无</s:else>
										</TD>
										<TD class=column align="right">wifi能力：</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==wifi_ability">802.11b</s:if>
											<s:elseif test="2==wifi_ability">802.11b/g</s:elseif> 
											<s:elseif test="3==wifi_ability">802.11b/g/n</s:elseif> 
											<s:elseif test="4==wifi_ability">802.11b/g/n/ac</s:elseif>
											<s:else>无</s:else>
										</TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">千兆口数量：</TD>
										<TD width="30%"><s:property value="gigabitnum" /></TD>
										<TD class=column align="right" width="20%">百兆口数量：</TD>
										<TD width="30%"><s:property value="mbitnum" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">语音口数量：</TD>
										<TD width="30%"><s:property value="voipnum" /></TD>
										<TD class=column align="right" width="20%">WIFI是否双频：</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==is_wifi_double">是</s:if>
											<s:elseif test="0==is_wifi_double">否</s:elseif>
											<s:else>无</s:else>
										</TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">融合功能：</TD>
										<TD width="30%"><s:property value="fusion_ability" /></TD>
										<TD class=column align="right" width="20%">融合终端接入方式：</TD>
										<TD width="30%"><s:property value="terminal_access_method" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="right" width="20%">光猫支持最大速率（MB/S）：</TD>
										<TD width="30%"><s:property value="devmaxspeed" /></TD>
										<TD class=column align="right" width="20%">设备版本类型:</TD>
										<TD  bgcolor="#FFFFFF">
											<s:if test="1==device_version_type">E8-C</s:if>
											<s:elseif test="2==device_version_type">PON融合</s:elseif> 
											<s:elseif test="3==device_version_type">10GPON</s:elseif> 
											<s:elseif test="4==device_version_type">政企网关</s:elseif>
											<s:elseif test="5==device_version_type">天翼网关1.0</s:elseif>
											<s:elseif test="6==device_version_type">天翼网关2.0</s:elseif>
											<s:elseif test="7==device_version_type">天翼网关3.0</s:elseif>
											<s:else>无</s:else>
										</TD>
									</TR>
								<%} %>
							</s:iterator>
							<TR>
								<TD colspan="4" align="right" class=foot><INPUT
									TYPE="button" value=" 关 闭 " class=btn
									onclick="javascript:window.close()"> &nbsp;&nbsp;</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
