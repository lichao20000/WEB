<%--
Author: ��ɭ��
Version: 1.0.0
Date: 2009-11-12
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor=#999999>
	<s:if test="userList==null">
		<TR bgcolor="#FFFFFF">
			<TD>
				<table width="100%" border=0 cellspacing=1 cellpadding=1
					align="center" bgcolor="#999999">
					<tr>
						<th>
							�û������Ϣ

						</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD align="center">
							<table border=0 cellspacing=1 cellpadding=1 width="100%"
								align="center" bgcolor=#999999>
								<tr>
									<td class="green_title" width="25%">
										�û��˺�
									</td>
									<td class="green_title" width="25%">
										����
									</td>
									<td class="green_title" width="25%">
										���豸oui
									</td>
									<td class="green_title" width="25%">
										���豸���к�
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td align="center">
										<s:property value="username" />
									</td>
									<td align="center">
										<s:property value="cityName" />
									</td>
									<td align="center">

									</td>
									<td align="center">

									</td>
								</tr>
							</table>
						</TD>
					</TR>
				</table>
			</TD>
		</TR>
		<tr bgcolor="#FFFFFF">
			<td>
				<font color="red">δ�鵽�����������û������ʵ������Ƿ���ȷ���������ϣ�����
					<button onclick="openinst()">
						��װ
					</button>��</font>
			</td>
		</tr>


	</s:if>
	<s:else>
		<s:if test="userList.size()==1">
			<TR bgcolor="#FFFFFF">
				<TD>
					<table width="100%" border=0 cellspacing=1 cellpadding=1
						align="center" bgcolor="#999999">
						<tr>
							<th>
								�û������Ϣ

							</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<table border=0 cellspacing=1 cellpadding=1 width="100%"
									align="center" bgcolor=#999999>
									<tr>
										<td class="green_title" width="20%">
											�û��˺�
										</td>
										<td class="green_title" width="20%">
											����
										</td>
										<td class="green_title" width="20%">
											���豸oui
										</td>
										<td class="green_title" width="20%">
											���豸���к�
										</td>
										<td class="green_title" width="20%">
											����֤
										</td>
									</tr>
									<s:iterator value="userList">
										<input type="hidden" name="userId"
											value="<s:property value="user_id" />">
										<tr bgcolor="#FFFFFF">
											<td align="center">
												<s:property value="username" />
											</td>
											<td align="center">
												<s:property value="city_name" />
											</td>
											<td align="center">
												<s:property value="oui" />
											</td>
											<td align="center">
												<s:property value="device_serialnumber" />
											</td>
											<td align="center">
												<s:property value="is_chk_bind" />
											</td>
										</tr>
									</s:iterator>
								</table>
							</TD>
						</TR>
					</table>
				</TD>
			</TR>

			<s:if test="deviceList!=null">
				<s:if test="deviceList.size()==1">
					<tr bgcolor="#FFFFFF">
						<td height="20">
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD>
							<table width="100%" border=0 cellspacing=1 cellpadding=1
								align="center" bgcolor="#999999">
								<tr>
									<th>
										�û��Ѱ��豸�������Ϣ
									</th>
								</tr>
								<tr bgcolor="#FFFFFF">
									<TD align="center">
										<table border=0 cellspacing=1 cellpadding=1 width="100%"
											align="center" bgcolor="#999999">
											<tr>
												<td class="green_title" width="20%">
													�豸����
												</td>
												<td class="green_title" width="20%">
													�豸oui
												</td>
												<td class="green_title" width="20%">
													�ͺ�
												</td>
												<td class="green_title" width="20%">
													����汾
												</td>
												<td class="green_title" width="20%">
													�豸���к�
												</td>

											</tr>
											<s:iterator value="deviceList" var="deviceList">
												<input type="hidden" name="oldDeviceId"
													value="<s:property value="device_id" />">
												<tr bgcolor="#FFFFFF">
													<td align="center">
														<s:property value="vendor_add" />
													</td>
													<td align="center">
														<s:property value="oui" />
													</td>
													<td align="center">
														<s:property value="device_model" />
													</td>
													<td align="center">
														<s:property value="softwareversion" />
													</td>
													<td align="center">
														<s:property value="device_serialnumber" />
													</td>

												</tr>
											</s:iterator>
										</table>
									</TD>
								</TR>
							</table>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td height="20">
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD>
							<TABLE border=0 cellspacing=1 cellpadding=1 width="100%"
								bgcolor="#999999">
								<TR bgcolor="#FFFFFF">
									<TH>
										��ѡ���豸
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">

									<TD align="left">
										�豸���к�:
										<input type="text" size="25" class="bk"
											name=device_serialnumber maxlength="30" value="">
										<input type="button" class=jianbian name="sort" value=" ��  ѯ "
											onclick="checkSerialno()" />
										<font color="red">�������������6λ�豸���кŽ��в�ѯ��</font>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR style="display: none" id="tr_deviceinfo">
						<TD>
							<div id="div_device" style="width: 100%; z-index: 1; top: 100px">
							</div>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td height="20">
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD>
							<TABLE border=0 cellspacing=1 cellpadding=1 width="100%"
								bgcolor="#999999">
								<TR bgcolor="#FFFFFF">
									<TH colspan="2">
										��ѡ������ԭ��
									</TH>
								</TR>
								<tr id="fault" bgcolor="#FFFFFF">
									<td align="left">
										����&nbsp;&nbsp;ԭ��:

										<select name="faultId" class="bk">
											<option value="-1">
												--��ѡ��--
											</option>
											<option value="1">
												�û���������
											</option>
											<option value="2">
												�豸���ֶ˿���
											</option>
											<option value="3">
												�豸�����ȱ��
											</option>
											<option value="4">
												����ͨ����ͨ���û���������
											</option>
											<option value="5">
												����ԭ��
											</option>
										</select>
									</TD>
								</tr>
							</TABLE>
						</TD>
					</TR>

				</s:if>
				<s:else>

					<tr bgcolor="#FFFFFF">
						<td>
							<font color="red">����ѯ���û��豸����Ϣ�쳣�����ʵ������Ƿ���ȷ�����������Ա��ϵ��</font>
						</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>

				<tr bgcolor="#FFFFFF">
					<td>
						<font color="red">����ѯ���û�δ���豸�����ʵ������Ƿ���ȷ���������ϣ�����
							<button onclick="openinst()">
								��װ
							</button>��</font>
					</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>

			<tr bgcolor="#FFFFFF">
				<td>
					<font color="red">����ѯ���û���Ϣ�쳣�����ʵ������Ƿ���ȷ�����������Ա��ϵ��</font>
				</td>
			</tr>
		</s:else>
	</s:else>
</table>
