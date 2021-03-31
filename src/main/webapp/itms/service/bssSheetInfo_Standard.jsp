<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<table class="querytable">
	<TR>
		<th colspan="4">
			BSS工单详细信息
		</th>
	</tr>
	<tr style="cursor: hand; background-color: #F0FFFF">
		<td colspan="4" align='left' class="green_title2">
			基本信息
		</td>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()>0">
			<TR>
				<TD class=column width="15%" align='right'>
					业务类型
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).serv_type" />
				</TD>
				<TD class=column width="15%" align='right'>
					操作类型
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).serv_status" />
				</TD>
			</TR>
			<TR>
				<%if (LipossGlobals.inArea("sd_lt")) { %>
				<TD class=column width="15%" align='right'>
					RMS接收时间
				</TD>
				<%}else{ %>
				<TD class=column width="15%" align='right'>
					ITMS接收时间
				</TD>
				<%} %>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).opendate" />
				</TD>
				<TD class=column width="15%" align='right'>
					设备类型
				</TD>
				<TD width="35%">
					<s:property value="type_name" />
				</TD>
			</TR>
			<TR>
				<TD class=column width="15%" align='right'>
					LOID
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).username" />
				</TD>
				<TD class=column width="15%" align='right'>
					属地
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).city_name" />
				</TD>
			</TR>
			<TR>
				<TD class=column width="15%" align='right'>
					协议类型
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).protocol" />
				</TD>
				<TD class=column width="15%" align='right'>
					IP获取方式
				</TD>
				<TD width="35%">
					<s:property value="bssSheetList.get(0).wan_type" />
				</TD>
			</TR>
			<tr>
				<TD class=column width="15%" align='right'>
					VLANID
				</TD>
				<TD width="85%" colspan="3">
					<s:property value="bssSheetList.get(0).vlanid" />
				</TD>
			</tr>
			<tr>
				<TD class=column width="15%" align='right'>
					回单信息
				</TD>
				<TD width="85%" colspan="3">
					0|||00|||成功
				</TD>
			</tr>
			<s:iterator value="bssSheetList">
				<TR style="cursor: hand; background-color: #F0FFFF">
					<TD width="15%" align='left'>
						语音端口
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="line_id" />
					</TD>
				</TR>
				<s:if test="protocol!='H248' && protocol != 'IMS H248'">
					<%-- <s:if test='%{error_code=="1"}'> --%>
					<TR>
						<TD class=column width="15%" align='right'>
							VoIP认证账号
						</TD>
						<TD width="35%">
							<s:property value="voip_username" />
						</TD>
						<TD class=column width="15%" align='right'>
							VoIP认证密码
						</TD>
						<%if (LipossGlobals.inArea("cq_dx")) { %>
						<TD width="35%">
							******
						</TD>
						<%}else{ %>
						<TD width="35%">
							<s:property value="voip_passwd" />
						</TD>
						<%} %>
					</tr>
					<tr>
						<TD class=column width="15%" align='right'>
							业务电话号码
						</TD>
						<TD width="35%">
							<s:property value="voip_phone" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							主用代理服务器地址
						</TD>
						<TD width="35%">
							<s:property value="prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							主用代理服务器端口
						</TD>
						<TD width="35%">
							<s:property value="prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							备用代理服务器地址
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							备用代理服务器端口
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							主用注册服务器地址
						</TD>
						<TD width="35%">
							<s:property value="regi_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							主用注册服务器端口
						</TD>
						<TD width="35%">
							<s:property value="regi_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							备用注册服务器地址
						</TD>
						<TD width="35%">
							<s:property value="stand_regi_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							备用注册服务器端口
						</TD>
						<TD width="35%">
							<s:property value="stand_regi_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							主用绑定服务器地址
						</TD>
						<TD width="35%">
							<s:property value="out_bound_proxy" />
						</TD>
						<TD class=column width="15%" align='right'>
							主用绑定服务器端口
						</TD>
						<TD width="35%">
							<s:property value="out_bound_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							备用绑定服务器地址
						</TD>
						<TD width="35%">
							<s:property value="stand_out_bound_proxy" />
						</TD>
						<TD class=column width="15%" align='right'>
							备用绑定服务器端口
						</TD>
						<TD width="35%">
							<s:property value="stand_out_bound_port" />
						</TD>
					</TR>
					<!-- added by zhouli5 -->
					<TR>
						<TD class=column width="15%" align='right'>
							IP地址
						</TD>
						<TD width="35%">
							<s:property value="ipaddress" />
						</TD>
						<TD class=column width="15%" align='right'>
							掩码
						</TD>
						<TD width="35%">
							<s:property value="ipmask" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							网关
						</TD>
						<TD width="35%">
							<s:property value="gateway" />
						</TD>
						<TD class=column width="15%" align='right'>
							终端标识类型
						</TD>
						<TD width="35%">
							<s:property value="reg_id_type" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							终端标识
						</TD>
						<TD width="35%">
							<s:property value="reg_id" />
						</TD>
						<ms:inArea areaCode="hb_dx" notInMode="false">
						<TD class=column width="15%" align='right'>
							终端物理标识
						</TD>
						<TD width="35%">
							<s:property value="voip_port" />
						</TD>
						</ms:inArea>
						<ms:inArea areaCode="cq_dx" notInMode="false">
						<TD class=column width="15%" align='right'>
							DNS服务器地址
						</TD>
						<TD width="35%">
							<s:property value="adsl_ser" />
						</TD>
						</ms:inArea>
					</TR>
					
					<%-- </s:if>
					<s:elseif test='%{error_code=="0"}'>
						<TD colspan="4"  align="center"><s:property value='error_desc'/> </TD>
					</s:elseif> --%>
				</s:if>
				<s:else>
					<s:if test="wan_type != 'DHCP'">
						<TR>
							<TD class=column width="15%" align='right'>
								IP地址
							</TD>
							<TD width="35%">
								<s:property value="ipaddress" />
							</TD>
							<TD class=column width="15%" align='right'>
								掩码
							</TD>
							<TD width="35%">
								<s:property value="ipmask" />
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>
								网关
							</TD>
							<TD width="35%">
								<s:property value="gateway" />
							</TD>
							<TD class=column width="15%" align='right'>
								DNS值
							</TD>
							<TD width="35%">
								<s:property value="adsl_ser" />
							</TD>
						</TR>
					</s:if>
					<TR>
						<TD class=column width="15%" align='right'>
							主MGC服务器地址
						</TD>
						<TD width="35%">
							<s:property value="prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							主MGC服务器端口
						</TD>
						<TD width="35%">
							<s:property value="prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							备MGC服务器地址
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_serv" />
						</TD>
						<TD class=column width="15%" align='right'>
							备MGC服务器端口
						</TD>
						<TD width="35%">
							<s:property value="stand_prox_port" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							终端标识类型
						</TD>
						<TD width="35%">
							<s:property value="reg_id_type" />
						</TD>
						<TD class=column width="15%" align='right'>
							终端标识
						</TD>
						<TD width="35%">
							<s:property value="reg_id" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>
							终端物理标识
						</TD>
						<TD width="35%">
							<s:property value="voip_port" />
						</TD>
						<TD class=column width="15%" align='right'>
							业务电话号码
						</TD>
						<TD width="35%">
							<s:property value="voip_phone" />
						</TD>
					</TR>
				</s:else>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					工单信息不存在或错误!
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>
				用户信息错误!
			</td>
		</tr>
	</s:else>

	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">关闭</a>
		</td>
	</TR>
</table>
