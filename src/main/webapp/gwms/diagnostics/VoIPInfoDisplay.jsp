<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>VoIP实时采集信息展示</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	</head>

	<body>
		<FORM NAME="frm" METHOD="post" action="">
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
				<tr>
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									VoIP连接信息
								</td>
							</tr>

							<tr align="left" id="trnet">
								<td colspan="3" bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<tr align="center" bgcolor="#FFFFFF">
											<TD class=column5 align="center">
												连接类型
											</TD>
											<TD class=column5 align="center">
												上连方式
											</TD>
											<TD class=column5 align="center">
												PVC/VLANID
											</TD>
											<TD class=column5 align="center">
												IP地址/PPPoE账号
											</TD>
											<TD class=column5 align="center">
												网关
											</TD>
											<TD class=column5 align="center">
												子网掩码
											</TD>
											<TD class=column5 align="center">
												连接状态
											</TD>
											<TD class=column5 align="center">
												服务类型
											</TD>
										</tr>
										<s:if test="wanConfigList.size()>0">
										<s:iterator var="wanConfigList" value="wanConfigList">
											<tr align="center" bgcolor="#FFFFFF">

												<s:if test='"wanConfigList.connType=="N/A" || #wanConfigList.connType=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"><s:property value="connType" /></TD>
												</s:else>

												<s:if test='"wanConfigList.ipType=="N/A" || #wanConfigList.ipType=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"><s:property value="ipType" /></TD>
												</s:else>

												<s:if test='"wanConfigList.vlanId=="N/A" || #wanConfigList.vlanId=="null" '>
													<TD align="center" id="pvc">
														PVC:
														<s:property value="vpi" />
														/
														<s:property value="vci" />/<s:property value="vlanId" />
													</TD>

													<s:if test='"wanConfigList.username=="N/A" || #wanConfigList.username=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="username" /> </TD>
													</s:else>

												</s:if>
												<s:else>
													<TD align="center">
														VLANID: <s:property value="vlanId" />
													</TD>

													<s:if test='"wanConfigList.ip=="N/A" || #wanConfigList.ip=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="ip" /> </TD>
													</s:else>
												</s:else>

												<s:if test='"wanConfigList.geteway=="N/A" || #wanConfigList.geteway=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"> <s:property value="geteway" /> </TD>
												</s:else>

												<s:if test='"wanConfigList.mask=="N/A" || #wanConfigList.mask=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"> <s:property value="mask" /> </TD>
												</s:else>

												<s:if test='"wanConfigList.connStatus=="N/A" || #wanConfigList.connStatus=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"> <s:property value="connStatus" /> </TD>
												</s:else>

												<s:if test='"wanConfigList.servList=="N/A" || #wanConfigList.servList=="null" '>
													<TD align="center"> - </TD>
												</s:if>
												<s:else>
													<TD align="center"> <s:property value="servList" /> </TD>
												</s:else>
											</tr>
										</s:iterator>
										</s:if>
										<s:else>
											<tr align="center" bgcolor="#FFFFFF">
												<TD colspan="11" align="center">
													<s:property value="corbaMsg" />
												</TD>
											</tr>
										</s:else>
									</TABLE>
								</td>
							</tr>

						</table>
					</TD>
				</tr>

				<tr height="20">
					<td colspan="1" width="15" class=column></td>
				</tr>
				<!-- 展示VoIP相关信息 -->
				<tr id="trvoip" STYLE="display: ">
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									VoIP详细信息
								</td>
							</tr>

							<tr align="left">
								<td colspan="3" bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<s:if test='voipProtocalTypeStr == "H.248"'>
											<tr align="center" bgcolor="#FFFFFF">
												<TD class=column5 align="center">
													语音端口
												</TD>
												<TD class=column5 align="center">
													协议类型
												</TD>
												<TD class=column5 align="center">
													服务器地址
												</TD>
												<TD class=column5 align="center">
													服务器端口
												</TD>
												<TD class=column5 align="center">
													备用地址
												</TD>
												<TD class=column5 align="center">
													备用端口
												</TD>
												<TD class=column5 align="center">
													开关
												</TD>
												<TD class=column5 align="center">
													UDP端口
												</TD>
												<TD class=column5 align="center">
													标识类型
												</TD>
												<TD class=column5 align="center">
													全局唯一标识
												</TD>
												<TD class=column5 align="center">
													线路状态
												</TD>
											</tr>
										</s:if>
										<s:else>
											<tr align="center" bgcolor="#FFFFFF">
												<TD class=column5 align="center">
													协议类型
												</TD>
												<TD class=column5 align="center">
													服务器地址
												</TD>
												<TD class=column5 align="center">
													服务器端口
												</TD>
												<TD class=column5 align="center">
													备用地址
												</TD>
												<TD class=column5 align="center">
													备用端口
												</TD>
											</tr>
										</s:else>
										<s:if test="voipInfoList.size()>0">
										<s:iterator value="voipInfoList" var="voipInfo">
											<s:if test='voipProtocalTypeStr == "H.248"'>
												<tr align="center" bgcolor="#FFFFFF">
													<s:if test='#voipInfo.line=="N/A" || #voipInfo.line=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="line" /> </TD>
													</s:else>

													<TD align="center"> <s:property value="voipProtocalTypeStr" /> </TD>

													<s:if test='#voipInfo.media_gateway_controler=="N/A" || #voipInfo.media_gateway_controler=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="media_gateway_controler" /> </TD>
													</s:else>

													<s:if test='"voipInfo.media_gateway_controler_port=="N/A" || #voipInfo.media_gateway_controler_port=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="media_gateway_controler_port" /> </TD>
													</s:else>

													<s:if test='"voipInfo.media_gateway_controler_2=="N/A" || #voipInfo.media_gateway_controler_2=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="media_gateway_controler_2" /> </TD>
													</s:else>

													<s:if test='"voipInfo.media_gateway_controler_port_2=="N/A" || #voipInfo.media_gateway_controler_port_2=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="media_gateway_controler_port_2" /> </TD>
													</s:else>

													<s:if test='"voipInfo.enable=="N/A" || #voipInfo.enable=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="enable" /> </TD>
													</s:else>

													<s:if test='"voipInfo.media_gateway_port=="N/A" || #voipInfo.media_gateway_port=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="media_gateway_port" /> </TD>
													</s:else>

													<s:if test='"voipInfo.h248_device_id_type=="N/A" || #voipInfo.h248_device_id_type=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="h248_device_id_type" /> </TD>
													</s:else>

													<s:if test='"voipInfo.h248_device_id=="N/A" || #voipInfo.h248_device_id=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="h248_device_id" /> </TD>
													</s:else>

													<s:if test='"voipInfo.status=="N/A" || #voipInfo.status=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="status" /> </TD>
													</s:else>
												</tr>
											</s:if>
											<s:else>
												<tr align="center" bgcolor="#FFFFFF">
													<TD align="center"> <s:property value="voipProtocalTypeStr" /> </TD>

													<s:if test='"voipInfo.prox_serv=="N/A" || #voipInfo.prox_serv=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="prox_serv" /> </TD>
													</s:else>

													<s:if test='"voipInfo.prox_port=="N/A" || #voipInfo.prox_port=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="prox_port" /> </TD>
													</s:else>

													<s:if test='"voipInfo.prox_serv_2=="N/A" || #voipInfo.prox_serv_2=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="prox_serv_2" /> </TD>
													</s:else>

													<s:if test='"voipInfo.prox_port_2=="N/A" || #voipInfo.prox_port_2=="null" '>
														<TD align="center"> - </TD>
													</s:if>
													<s:else>
														<TD align="center"> <s:property value="prox_port_2" /> </TD>
													</s:else>
												</tr>
											</s:else>
										</s:iterator>
										</s:if>
										<s:else>
											<tr align="center" bgcolor="#FFFFFF">
												<TD colspan="11" align="center">
													<s:property value="voipInfoMsg" />
												</TD>
											</tr>
										</s:else>
									</TABLE>
								</td>
							</tr>

						</table>
					</TD>
				</tr>

				<tr height="20">
					<td colspan="1" width="15" class=column></td>
				</tr>

			</TABLE>
		</FORM>
	</body>
</html>
