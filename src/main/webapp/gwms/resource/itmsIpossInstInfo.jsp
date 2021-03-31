<%--
Author: 王森博
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
								用户相关信息

							</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<table border=0 cellspacing=1 cellpadding=1 width="100%"
									align="center" bgcolor=#999999>
									<tr>
										<td class="green_title" width="25%">
											用户账号
										</td>
										<td class="green_title" width="25%">
											属地
										</td>
										<td class="green_title" width="25%">
											绑定设备oui
										</td>
										<td class="green_title" width="25%">
											绑定设备序列号
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
				<font color="red">未查到符合条件的用户！请核实输入的是否正确！如果继续新装，用户将会被新建！</font>
			</td>
		</tr>
		<TR bgcolor="#FFFFFF">
			<TD HEIGHT=30>
			</TD>
		</TR>
		<TR bgcolor="#FFFFFF">
			<TD>
				<TABLE border=0 cellspacing=1 cellpadding=1 width="100%"
					bgcolor="#999999">
					<TR bgcolor="#FFFFFF">
						<TH>
							请选择设备
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">

						<TD align="left">
							设备序列号:
							<input type="text" size="25" class="bk" name=device_serialnumber
								maxlength="30" value="">
							<input type="button" class=jianbian name="sort" value=" 查  询 "
								onclick="checkSerialno()" />
							<font color="red">请至少输入最后6位设备序列号进行查询！</font>
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

	</s:if>
	<s:else>
		<s:if test="userList.size()==1">
			<TR bgcolor="#FFFFFF">
				<TD>
					<table width="100%" border=0 cellspacing=1 cellpadding=1
						align="center" bgcolor="#999999">
						<tr>
							<th>
								用户相关信息

							</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<table border=0 cellspacing=1 cellpadding=1 width="100%"
									align="center" bgcolor=#999999>
									<tr>
										<td class="green_title" width="25%">
											用户账号
										</td>
										<td class="green_title" width="25%">
											属地
										</td>
										<td class="green_title" width="25%">
											绑定设备oui
										</td>
										<td class="green_title" width="25%">
											绑定设备序列号
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
			<s:if test="deviceList!=null">
				<TR bgcolor="#FFFFFF">
					<TD>
						<table width="100%" border=0 cellspacing=1 cellpadding=1
							align="center" bgcolor="#999999">
							<tr>
								<th>
									用户已绑定设备的相关信息
								</th>
							</tr>
							<tr bgcolor="#FFFFFF">
								<TD align="center">
									<table border=0 cellspacing=1 cellpadding=1 width="100%"
										align="center" bgcolor="#999999">
										<tr>
											<td class="green_title" width="20%">
												设备厂商
											</td>
											<td class="green_title" width="20%">
												设备oui
											</td>
											<td class="green_title" width="20%">
												型号
											</td>
											<td class="green_title" width="20%">
												软件版本
											</td>
											<td class="green_title" width="20%">
												设备序列号
											</td>

										</tr>


										<s:iterator value="deviceList" var="deviceList">
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
					<td>
						<font color="red">您查询的用户已经绑定设备！请核实输入的是否正确！如需修障请进入修障页面！</font>
					</td>
				</tr>
			</s:if>
			<s:else>
				<TR>
					<TD>
						<TABLE border=0 cellspacing=1 cellpadding=1 width="100%"
							bgcolor=#999999>
							<TR bgcolor="#FFFFFF">
								<TH>
									可以选择新设备的相关信息
								</TH>
							</TR>
							<TR bgcolor="#FFFFFF">

								<TD align="left">
									设备序列号:
									<input type="text" size="25" class="bk"
										name=device_serialnumber maxlength="30" value="">
									<input type="button" class=jianbian name="sort" value=" 查  询 "
										onclick="checkSerialno()" />
									<font color="red">请至少输入最后6位设备序列号进行查询！</font>
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
			</s:else>

		</s:if>
		<s:else>
			<tr bgcolor="#FFFFFF">
				<td height="15">
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td>
					<font color="red">您查询的用户信息异常！请核实输入的是否正确！或请与管理员联系！</font>
				</td>
			</tr>

		</s:else>
	</s:else>
</table>
