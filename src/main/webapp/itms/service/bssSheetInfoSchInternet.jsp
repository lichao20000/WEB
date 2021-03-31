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
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						业务类型
					</TD>
					<TD width="35%">
						<s:property value="serv_type" />
					</TD>
					<TD class=column width="15%" align='right'>
						操作类型
					</TD>
					<TD width="35%">
						<s:property value="serv_status" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					ITMS接收时间
					</TD>
					<TD width="35%">
						<s:property value="opendate" />
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
						<ms:inArea areaCode="sx_lt" notInMode="false">
							唯一标识
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID
						</ms:inArea>
					</TD>
					<TD width="35%">
						<s:property value="username" />
					</TD>
					<TD class=column width="15%" align='right'>
						宽带帐号或专线接入号
					</TD>
					<TD width="35%">
						<s:property value="account" />
					</TD>
				</TR>
				<TR>
					
					<TD class=column width="15%" align='right'>
						属地
					</TD>
					<TD width="35%">
						<s:property value="city_name" />
					</TD>
					<TD class=column width="15%" align='right'>
						上网方式
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="wan_type" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						VLANID
					</TD>
					<TD width="35%">
						<s:property value="vlanid" />
					</TD>
					<TD class=column width="15%" align='right'>
						用户IP类型
					</TD>
					<TD width="35%">
						<s:if test='%{dslite_enable=="是"}'>
							Ds-Lite
						</s:if>
						<s:else>
							<s:property value="ip_type" />
						</s:else>
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						设备是否具备SDN的能力
					</TD>
					<TD width="35%">
						<s:property value="capacity" />
					</TD>
					<TD class=column width="15%" align='right'>
						是否启用SDN功能
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="enable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						SDN控制器的主用地址
					</TD>
					<TD width="35%">
						<s:property value="controllerAddress" />
					</TD>
					<TD class=column width="15%" align='right'>
						控制器主备功能启用开关
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="backupEnable" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						SDN控制器的备用地址
					</TD>
					<TD width="35%">
						<s:property value="backupControllerAddress" />
					</TD>
					<TD class=column width="15%" align='right'>
					</TD>
					<TD width="85%" colspan="3">
					</TD>
				</TR>
				<tr>
					<TD class=column width="15%" align='right'>
						回单信息
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||成功
					</TD>
				</tr>
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
