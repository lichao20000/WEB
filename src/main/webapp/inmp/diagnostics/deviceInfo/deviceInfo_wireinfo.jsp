<%--
FileName	: deviceInfo.jsp
Date		: 2009年6月25日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column5 align="center">线路状态</TD>
		<TD class=column5 align="center">线路协议</TD>
		<TD class=column5 align="center">上行线路衰减</TD>
		<TD class=column5 align="center">下行线路衰减</TD>
		<TD class=column5 align="center">上行速率</TD>
		<TD class=column5 align="center">下行速率</TD>
		<TD class=column5 align="center">上行噪声裕量</TD>
		<TD class=column5 align="center">下行噪声裕量</TD>
		<TD class=column5 align="center">交织深度</TD>
		<TD class=column5 align="center">fast/Interleave</TD>
	</tr>
	<s:iterator value="wireInfoObjArr" var="wideNetInfo">
		<tr align="center" bgcolor="#FFFFFF">
			<s:if test="#wideNetInfo.wireStatus=='N/A' || #wideNetInfo.wireStatus=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="wireStatus"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.modulationType=='N/A' || #wideNetInfo.modulationType=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="modulationType"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.upstreamAttenuation=='N/A' || #wideNetInfo.upstreamAttenuation=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="upstreamAttenuation"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.downstreamAttenuation=='N/A' || #wideNetInfo.downstreamAttenuation=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="downstreamAttenuation"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.upstreamMaxRate=='N/A' || #wideNetInfo.upstreamMaxRate=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="upstreamMaxRate"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.downstreamMaxRate=='N/A' || #wideNetInfo.downstreamMaxRate=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="downstreamMaxRate"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.upNoise=='N/A' || #wideNetInfo.upNoise=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="upNoise"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.downNoise=='N/A' || #wideNetInfo.downNoise=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="downNoise"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.interleaveDepth=='N/A' || #wideNetInfo.interleaveDepth=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="interleaveDepth"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.dataPath=='N/A' || #wideNetInfo.dataPath=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="dataPath"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="10"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>
