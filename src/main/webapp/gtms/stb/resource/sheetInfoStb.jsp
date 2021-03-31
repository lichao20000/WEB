<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
						业务账号
					</TD>
					<TD width="35%">
						<s:property value="serv_account" />
					</TD>
					<TD class=column width="15%" align='right'>
						上网方式
					</TD>
					<TD width="35%">
						<s:property value="net_type" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
					接入帐号
					</TD>
					<TD width="35%">
						<s:property value="pppoe_user" />
					</TD>
					<TD class=column width="15%" align='right'>
						接入密码
					</TD>
					<TD width="35%">
						<s:property value="pppoe_pwd" />
					</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						机顶盒MAC
					</TD>
					<TD width="35%">
						<s:property value="cpe_mac" />
					</TD>
					<TD class=column width="15%" align='right'>
						终端类型
					</TD>
					<TD width="35%">
						<s:property value="device_type" />
					</TD>
				</TR>
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
