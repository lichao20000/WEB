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
					���ڲ���...
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
								�������
							</th>
							<th width="" nowrap>
								�ɼ���
							</th>
							<th width="" nowrap>
								ҵ������
							</th>
							<th width="" nowrap>
								�û��ʻ�
							</th>
							<th width="" nowrap>
								ִ��״̬
							</th>
							<th width="" nowrap>
								ִ�н��
							</th>
							<!-- <th width="" nowrap>�������</th> -->
							<th width="" nowrap>
								��ʼʱ��
							</th>
							<th width="" nowrap>
								����ʱ��
							</th>
							<th nowrap>
								ʧ��ԭ������
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
										����ִ��.....
									</s:if>
									<s:elseif test="exec_status == 1">
										ִ�����
									</s:elseif>
									<s:elseif test="exec_status == -1">
										�ȴ�ִ��
									</s:elseif>
									</nobr>
								</TD>
								<TD>
									<nobr>
										<s:if test="fault_code == 1">
											ִ�гɹ�
										</s:if>
										<s:elseif test="fault_code == 0">
											δ֪����
										</s:elseif>
										<s:elseif test="fault_code == -1">
											���Ӳ���
										</s:elseif>										
										<s:elseif test="fault_code == -2">
											���ӳ�ʱ
										</s:elseif>
										<s:elseif test="fault_code == -3">
											û�й���
										</s:elseif>		
										<s:elseif test="fault_code == -4">
											û���豸
										</s:elseif>		
										<s:elseif test="fault_code == -5">
											û��ģ��
										</s:elseif>		
										<s:elseif test="fault_code == -6">
											�豸��æ
										</s:elseif>		
										<s:elseif test="fault_code == -7">
											��������
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
