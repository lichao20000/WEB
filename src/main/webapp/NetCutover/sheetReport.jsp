<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<HTML>
	<BODY>
		<meta http-equiv="refresh" content="10">   
		<%@ include file="../head.jsp"%>
		<TR>
			<TD height="40">
				&nbsp;
			</TD>
		</TR>
		<TABLE width="100%" height="30" border="0" cellpadding="0"
			cellspacing="0" class="green_gargtd">
			<TR>
				<TD width="162" align="center" class="title_bigwhite">				
					正在操作...
				</TD>
			</TR>
		</TABLE>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
						id="idTable" oncontextmenu="return false;">
						<tr>
							<th width="" nowrap>
								工单编号
							</th>
							<th width="" nowrap>
								采集点
							</th>
							<th width="" nowrap>
								业务名称
							</th>
							<th width="" nowrap>
								用户帐户
							</th>
							<th width="" nowrap>
								执行状态
							</th>
							<th width="" nowrap>
								执行结果
							</th>
							<!-- <th width="" nowrap>错误代码</th> -->
							<th width="" nowrap>
								开始时间
							</th>
							<th width="" nowrap>
								结束时间
							</th>
							<th nowrap>
								失败原因描述
							</th>
						</tr>
						<s:iterator value="list">
						<TR 
							
							<s:if test="exec_status == 0">
								class=trOutyellow onmouseover='this.className="trOutyellow"'
								onmouseout='this.className="trOutyellow"'
							</s:if>
							<s:if test="exec_status == -1">
								class=trOutyellow onmouseover='this.className="trOutyellow"'
								onmouseout='this.className="trOutyellow"'
							</s:if>
							<s:if test="fault_code < 1">
								class=trOutred onmouseover='this.className="trOutred"' onmouseout='this.className="trOutred"'
							</s:if>
							<s:else>
								class=trOut  onmouseover='this.className="trOver"' onmouseout='this.className="trOut"'
							</s:else>
															
							parames="<s:property value="sheet_id"/>,<s:property value="receive_time"/>,<s:property value="gather_id"/>"
							value="">
							
							
								<TD>
									<nobr>
										<s:property value="sheet_id" />
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="gather_id_desc" />
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="service_id_desc" />
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="username" />
									</nobr>
								</TD>
								<TD>
									<nobr>
									<s:if test="exec_status == 0">
										正在执行.....
									</s:if>
									<s:elseif test="exec_status == 1">
										执行完成
									</s:elseif>
									<s:elseif test="exec_status == -1">
										等待执行
									</s:elseif>
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:if test="fault_code == 1">
											执行成功
										</s:if>
										<s:elseif test="fault_code == 0">
											未知错误
										</s:elseif>
										<s:elseif test="fault_code == -1">
											连接不上
										</s:elseif>										
										<s:elseif test="fault_code == -2">
											连接超时
										</s:elseif>
										<s:elseif test="fault_code == -3">
											没有工单
										</s:elseif>		
										<s:elseif test="fault_code == -4">
											没有设备
										</s:elseif>		
										<s:elseif test="fault_code == -5">
											没有模板
										</s:elseif>		
										<s:elseif test="fault_code == -6">
											设备正忙
										</s:elseif>		
										<s:elseif test="fault_code == -7">
											参数错误
										</s:elseif>
										<s:else>
											<s:property value="fault_code"/>
										</s:else>																																																		
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="start_time_desc" />
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="end_time_desc" />
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:property value="fault_desc" />
									</nobr>
								</TD>
							</TR>
						</s:iterator>

					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</BODY>
</HTML>
