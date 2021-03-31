<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<link rel="stylesheet" href="<s:url value='../../css/inmp/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
	</SCRIPT>



<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="2" class="green_title_left">
						宽带上网
					</td>
				</tr>
				<tr align="left" id="trnet" STYLE="display: ">
					<td colspan="1" width="15" class=column>

					</td>
					<td bgcolor=#999999>
						<div id="divnet">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">
										上行方式
									</TD>
									<TD class=column5 align="center">
										PVC/VLAN配置
									</TD>
								</tr>
								<s:if test="internetlist!=null">
									<s:iterator value="internetlist">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center">
												<s:property value="accessType" />
											</TD>
											<s:if test='accessType=="DSL"'>
												<TD align="center">
													<s:property value="vpi_id" />
													/
													<s:property value="vci_id" />
												</TD>
											</s:if>
											<s:else>
												<TD align="center">
													<s:property value="vlan_id" />
												</TD>
											</s:else>
										</tr>
									</s:iterator>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="2">
											<div id="div_wideNetconfig"
												style="width: 100%; z-index: 1; top: 100px">

												<font color="red"> <s:if test='wideNetMsg=="0"'>
														检查结果：配置不正确
													<button
															onclick="wideNetconfig('<s:property value="accessType" />');" style="display:none">
															重新下发
														</button>
													</s:if> <s:else>
													检查结果：正常
												</s:else> </font>
											</div>
										</TD>
									</tr>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<td colspan="2">
											<s:property value="wideNetMsg" />
										</td>
									</tr>
								</s:else>

							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>

	<tr height="20">
		<td colspan="1" width="15" class=column>
		</td>
	</tr>

	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="2" class="green_title_left">
						&nbsp;&nbsp;IPTV&nbsp;
					</td>
				</tr>
				<tr align="left" id="triptv" STYLE="display: ">
					<td colspan="1" width="15" class=column>

					</td>
					<td bgcolor=#999999>
						<div id="diviptv">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">
										上行方式
									</TD>
									<TD class=column5 align="center">
										PVC/VLAN
									</TD>
									<TD class=column5 align="center">
										绑定端口
									</TD>
									<TD class=column5 align="center">
										IGMP
									</TD>
									<TD class=column5 align="center">
										Proxy
									</TD>
									<TD class=column5 align="center">
										Snooping
									</TD>
								</tr>
								<s:if test="iptvlist!=null">
									<s:iterator value="iptvlist">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center">
												<s:property value="accessType" />
											</TD>
											<TD align="center">
												<s:if test='accessType=="DSL"'>
													<s:property value="vpi_id" />
													/
													<s:property value="vci_id" />
												</s:if>
												<s:else>
													<s:property value="vlan_id" />
												</s:else>
											</TD>
											<TD align="center">
												<s:property value="bind_port" />
											</TD>
											<TD align="center">
												<s:if test='igmp=="1"'>启用</s:if>
												<s:else>没启用</s:else>
											</TD>
											<TD align="center">
												<s:if test='proxy=="1"'>启用</s:if>
												<s:else>没启用</s:else>
											</TD>
											<TD align="center">
												<s:if test='snooping=="1"'>启用</s:if>
												<s:else>没启用</s:else>
											</TD>
										</tr>
									</s:iterator>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="6">
											<div id="div_iptvconfig"
												style="width: 100%; z-index: 1; top: 100px">
												<font color="red"> <s:if test='iptvMsg=="0"'>
														检查结果：配置不正确
													<button
															onclick="iptvconfig('<s:property value="accessType" />');" style="display:none">
															重新下发
														</button>
													</s:if> <s:else>
													检查结果：正常
												</s:else> </font>
											</div>
										</TD>
									</tr>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<td colspan="6">
											<s:property value="iptvMsg" />
										</td>
									</tr>
								</s:else>
							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column>
		</td>
	</tr>
	<tr>
		<td>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="2" class="green_title_left">
						&nbsp;&nbsp;VOIP&nbsp;
					</td>
				</tr>
				<tr align="left" id="trvoip" STYLE="display: ">
					<td colspan="1" width="15" class=column>

					</td>
					<td bgcolor=#999999>
						<div id="divvoip">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<TD class=column5 align="center" colspan="2">
										上行方式
									</TD>
									<TD class=column5 align="center" colspan="2">
										协议类型
									</TD>
									<TD class=column5 align="center" colspan="2">
										PVC/VLAN
									</TD>
								</tr>
								<s:if test="voipPVClist!=null">
									<s:iterator value="voipPVClist">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center" colspan="2">
												<s:property value="accessType" />
											</td>
											<TD align="center" colspan="2">
												<s:property value="voipProtocalTypeStr" />
											</td>
											<s:if test='accessType=="DSL"'>
												<TD align="center" colspan="2">
													<s:property value="vpi_id" />
													/
													<s:property value="vci_id" />
												</TD>
											</s:if>
											<s:else>
												<TD align="center" colspan="2">
													<s:property value="vlan_id" />
												</TD>
											</s:else>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="2">
											<s:property value="accessType" />
										</td>
										<TD align="center" colspan="2">
											-
										</td>
										<TD align="center" colspan="2">
											-
										</TD>
									</tr>
								</s:else>
								<tr align="center" bgcolor="#FFFFFF">
									<!-- <TD class=column5 align="center">
										上行方式
									</TD> -->
									<TD class=column5 align="center">
										语音端口
									</TD>
									<TD class=column5 align="center">
										主服务器地址
									</TD>
									<TD class=column5 align="center">
										主服务器端口
									</TD>
									<TD class=column5 align="center">
										备服务器地址
									</TD>
									<TD class=column5 align="center">
										备服务器端口
									</TD>
									<TD class=column5 align="center">
										注册帐号
									</TD>
								</tr>
								<s:if test="voipdevicelist!=null">
									<s:iterator value="voipdevicelist">
										<tr align="center" bgcolor="#FFFFFF">
											<!-- <TD align="center">
												<s:property value="accessType" />
											</TD> -->
											<TD align="center">
												<s:property value="line_id" />
											</TD>

											<TD align="center">
												<s:property value="prox_serv" />
											</TD>
											<TD align="center">
												<s:property value="prox_port" />
											</TD>
											<TD align="center">
												<s:property value="prox_serv_2" />
											</TD>
											<TD align="center">
												<s:property value="prox_port_2" />
											</TD>
											<TD align="center">
												<s:property value="username" />
											</TD>
										</tr>
									</s:iterator>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="6">
											<div id="div_voipconfig"
												style="width: 100%; z-index: 1; top: 100px">
												<font color="red"> <s:if test='voipMsg=="0"'>
														检查结果：配置不正确
													<button
															onclick="voipconfig('<s:property value="accessType" />');" style="display:none">
															重新下发
														</button>
													</s:if> <s:else>
													检查结果：正常
												</s:else> </font>
											</div>
										</TD>
									</tr>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<td colspan="6">
											<s:property value="voipMsg" />
										</td>
									</tr>
								</s:else>
							</TABLE>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</tr>
	<tr height="20">
		<td colspan="1" width="15" class=column>
		</td>
	</tr>
	<tr>
		<td>
			<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr align="left" height="25">
					<td colspan="2" class="green_title_left">
						QOS配置
					</td>
				</tr>
				<tr align="left" id="trwireinfo" STYLE="display: ">
					<td colspan="1" width="15" class=column>

					</td>
					<td bgcolor=#999999>
						<div id="divwireinfo">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 width="60%" align="center">
										Mode
									</TD>
									<TD class=column5 width="40%" align="center">
										Enable
									</TD>
								</tr>
								<s:if test="qosmap!=null">
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center">
											<s:property value="qosmap.qos_mode" />
										</TD>
										<TD align="center">
											<s:if test='qosmap.enable=="1"'>启用</s:if>
											<s:else>没启用</s:else>
										</TD>
									</tr>
									<tr align="center" bgcolor="#FFFFFF">
										<TD align="center" colspan="2">
											<div id="div_qosconfig"
												style="width: 100%; z-index: 1; top: 100px">
												<font color="red"> <s:if test='qosMsg=="0"'>
														检查结果：配置不正确
													<button onclick="qosconfig();" style="display:none">
															重新下发
														</button>
													</s:if> <s:else>
													检查结果：正常
												</s:else> </font>
											</div>
										</TD>
									</tr>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<td colspan="2">
											<s:property value="qosMsg" />
										</td>
									</tr>
								</s:else>
							</TABLE>
						</div>
					</td>
				</tr>
			</TABLE>
		</td>
	</tr>
</table>