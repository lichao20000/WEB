<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>��·�����</title>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" id="myTable" width="100%" bgcolor=#999999>
	<s:if test='accessType=="3" || accessType=="4" '>
		<tr>
			<td>
				<tABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<tr align="left" height="25">
						<td colspan="5" class="green_title_left">
							<s:if test="pon_type=='EPON'">
								EPON��Ϣ
							</s:if>
							<s:elseif test="pon_type=='GPON'">
								GPON��Ϣ
							</s:elseif>
							<s:else>
								PON��Ϣ
							</s:else>
						</td>
					</tr>
					<tr align="left" id="trponInfo" STYLE="display: ">
						<td>
							<div id="divponInfo">
								<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
									<s:if test="ponInfoOBJArr!=null">
										<s:iterator value="ponInfoOBJArr" var="ponInfoOBJ">
											<tr>
												<td>
													<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
														<tr>
															<th>��·״̬</th>
															<th>����⹦��(dBm)</th>
															<th>���չ⹦��(dBm)</th>
															<th>�����¶�(1/256��)</th>
															<th>�����ѹ(100΢��)</th>
															<th>ƫ�õ���(2΢��)</th>
														</tr>
														<tr>
															<s:if
																test="#ponInfoOBJ.status=='N/A' || #ponInfoOBJ.status=='null' || #ponInfoOBJ.status==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="status" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.txpower=='N/A' || #ponInfoOBJ.txpower=='null' || #ponInfoOBJ.txpower==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="txpower" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rxpower=='N/A' || #ponInfoOBJ.rxpower=='null' || #ponInfoOBJ.rxpower==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="rxpower" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.transceiverTemperature=='N/A' || #ponInfoOBJ.transceiverTemperature=='null' || #ponInfoOBJ.transceiverTemperature==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="transceiverTemperature" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.supplyVottage=='N/A' || #ponInfoOBJ.supplyVottage=='null' || #ponInfoOBJ.supplyVottage==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="supplyVottage" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.biasCurrent=='N/A' || #ponInfoOBJ.biasCurrent=='null' || #ponInfoOBJ.biasCurrent==''">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="biasCurrent" />
																</TD>
															</s:else>
														</tr>
													</TABLE>
												</td>
											</tr>
											<tr height="3">
												<td colspan="1" width="15" class=column>
												</td>
											</tr>
											<tr>
												<td>
													<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
														<tr>
															<th>�����ֽ���</th>
															<th>�����ֽ���</th>
															<th>����֡����</th>
															<th>����֡����</th>
															<th>���͵���֡��</th>
															<th>���յ���֡��</th>
															<th>�����鲨֡��</th>
															<th>�����鲨֡��</th>
															<th>���͹㲥��֡��</th>
															<th>���չ㲥��֡��</th>
															<th>����FEC����֡��</th>
															<th>����HEC����֡��</th>
															<th>���ͷ���֡��</th>
															<th>����PAUSE������֡��</th>
															<th>����PAUSE������֡��</th>
														</tr>
														<tr>
															<s:if
																test="#ponInfoOBJ.bytesSent=='N/A' || #ponInfoOBJ.bytesSent=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="bytesSent" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.bytesReceived=='N/A' || #ponInfoOBJ.bytesReceived=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="bytesReceived" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.packetsSent=='N/A' || #ponInfoOBJ.packetsSent=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="packetsSent" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.packetsReceived=='N/A' || #ponInfoOBJ.packetsReceived=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="packetsReceived" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.sunicastPackets=='N/A' || #ponInfoOBJ.sunicastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="sunicastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.runicastPackets=='N/A' || #ponInfoOBJ.runicastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="runicastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.smulticastPackets=='N/A' || #ponInfoOBJ.smulticastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="smulticastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rmulticastPackets=='N/A' || #ponInfoOBJ.rmulticastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="rmulticastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.sbroadcastPackets=='N/A' || #ponInfoOBJ.sbroadcastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="sbroadcastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rbroadcastPackets=='N/A' || #ponInfoOBJ.rbroadcastPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="rbroadcastPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.fecError=='N/A' || #ponInfoOBJ.fecError=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="fecError" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.hecError=='N/A' || #ponInfoOBJ.hecError=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="hecError" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.dropPackets=='N/A' || #ponInfoOBJ.dropPackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="dropPackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.spausePackets=='N/A' || #ponInfoOBJ.spausePackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="spausePackets" />
																</TD>
															</s:else>
															<s:if
																test="#ponInfoOBJ.rpausePackets=='N/A' || #ponInfoOBJ.rpausePackets=='null' ">
																<TD align="center" class="column">
																	-
																</TD>
															</s:if>
															<s:else>
																<TD align="center" class="column">
																	<s:property value="rpausePackets" />
																</TD>
															</s:else>
														</tr>
													</TABLE>
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<s:else>
										<tr>
											<TD align="center" colspan="10"  class="column">
												<s:property value="wireMsg" />
											</TD>
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
						<td colspan="5" class="green_title_left">
							��·��Ϣ
						</td>
					</tr>
					<tr align="left" id="trwireinfo" STYLE="display: ">
						<td>
							<div id="divwireinfo">
								<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
									<tr>
										<th width="10%">��·״̬</th>
										<th width="10%">��·Э��</th>
										<th width="10%">��������(Kbps)</th>
										<th width="10%">��������(Kbps)</th>
										<th width="15%">������·˥��(dB)</th>
										<th width="15%">������·˥��(dB)</th>
										<th width="15%">��������</th>
										<th width="15%">������</th>
									</tr>

									<s:if test="diagResult.pass == '-2'">
										<tr>
											<td colspan="8" class="column" >
												<s:property value="diagResult.failture" escapeHtml="false" default=""/>
											</td>
										</tr>
									</s:if>
									<s:else>
										<s:if test="diagResult.pass == '-1'">
											<tr>
												<td colspan="8" class="column" >
													<FONT color="red"><s:property value="diagResult.faultDesc" escapeHtml="false" default=""/></FONT>
												</td>
											</tr>
											<tr>
												<td colspan="8" class="column" >
													<FONT color="green"><s:property value="diagResult.suggest" escapeHtml="false" default=""/></FONT>
												</td>
											</tr>
										</s:if>
										<s:else>

											<s:iterator value="diagResult.rList">
												<tr>
													<s:if test="'N/A' == wireStatus || 'null' == wireStatus">
														<td class="column" align="center">-</td>
													</s:if>
													<s:else>
														<td class="column" align="center">
															<s:property value="wireStatus" escapeHtml="false"/>
														</td>
													</s:else>

													<s:if test="'N/A' == modulationType || 'null' == modulationType">
														<td class="column" align="center">-</td>
													</s:if>
													<s:else>
														<td class="column" align="center">
															<s:property value="modulationType" escapeHtml="false"/>
														</td>
													</s:else>

													<td class="column" align="center"><s:property value="upRate" escapeHtml="false"/></td>
													<td class="column" align="center"><s:property value="downRate" escapeHtml="false"/></td>
													<td class="column" align="center"><s:property value="upAttenuation" escapeHtml="false"/></td>
													<td class="column" align="center"><s:property value="downAttenuation" escapeHtml="false"/></td>

													<s:if test="diagResult.pass == '-3'">
														<td class="column" align="center">
															<FONT color="red"><s:property value="passMessage" escapeHtml="false" default=""/></FONT>
														</td>
														<td class="column" align="center">
															<FONT color="green"><s:property value="passSuggest" escapeHtml="false" default="��"/></FONT>
														</td>
													</s:if>
													<s:else>
														<td class="column" align="center">
															<FONT color="green">����</FONT>
														</td>
														<td class="column" align="center">
															<FONT color="green">��</FONT>
														</td>
													</s:else>
												</tr>
											</s:iterator>
										</s:else>
									</s:else>
								</TABLE>
							</div>
						</td>
					</tr>
				</TABLE>
			</td>
		</tr>
	</s:else>
</table>
</body>
</html>
