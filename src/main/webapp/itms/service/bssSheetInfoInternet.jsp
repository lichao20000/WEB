<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<table class="querytable">
	<TR>
		<th colspan="4">BSS工单详细信息</th>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>业务类型</TD>
					<TD width="35%"><s:property value="serv_type" /></TD>
					<TD class=column width="15%" align='right'>操作类型</TD>
					<TD width="35%"><s:property value="serv_status" /></TD>
				</TR>
				<TR>
					<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt")) { %>
						<TD class=column width="15%" align='right'>RMS接收时间</TD>
					<%}else{ %>
						<TD class=column width="15%" align='right'>ITMS接收时间</TD>
					<%} %>
					<TD width="35%"><s:property value="opendate" /></TD>
					<TD class=column width="15%" align='right'>设备类型</TD>   
					<TD width="35%"><s:property value="type_name" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						<%if(LipossGlobals.inArea("sx_lt")) { %>
							唯一标识
						<%}else{ %>
							LOID
						<%} %>
					</TD>
					<TD width="35%"><s:property value="username" /></TD>
					<TD class=column width="15%" align='right'>宽带帐号或专线接入号</TD>
					<TD width="35%"><s:property value="account" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>属地</TD>
					<TD width="35%"><s:property value="city_name" /></TD>
					<TD class=column width="15%" align='right'>上网方式</TD>
					<TD width="85%" colspan="3"><s:property value="wan_type" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>VLANID</TD>
					<TD width="35%"><s:property value="vlanid" /></TD>
					<%if (LipossGlobals.inArea("xj_dx")) {%>
						<TD class=column width="15%" align='right'>带宽</TD>
						<TD width="85%" colspan="3"><s:property value="downBandwidth" /></TD>
					<%}else if(LipossGlobals.inArea("jl_lt")) {%>
						<TD class=column width="15%" align='right'>签约宽带</TD>
						<TD width="85%" colspan="3"><s:property value="rate" /></TD>
					<%}else{%>
						<TD class=column width="15%" align='right'></TD>
						<TD width="85%" colspan="3"></TD>
					<%} %>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>用户IP类型</TD>
					<TD width="35%">
						<s:if test='%{dslite_enable=="是"}'>Ds-Lite</s:if>
						<s:else><s:property value="ip_type" /></s:else>
					</TD>
					<TD class=column width="15%" align='right'></TD>
					<TD width="85%" colspan="3"></TD>
				</TR>
				<tr>
					<TD class=column width="15%" align='right'>回单信息</TD>
					<TD width="85%" colspan="3">0|||00|||成功</TD>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>工单信息不存在或错误!</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>用户信息错误!</td>
		</tr>
	</s:else>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">关闭</a>
		</td>
	</TR>
</table>
