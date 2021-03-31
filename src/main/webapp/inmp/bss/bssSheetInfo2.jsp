<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="querytable">

	<TR>
		<th colspan="4">
			BSS工单详细信息
		</th>
	</tr>
	<s:if test="bssParaList!=null">
		<s:if test="bssParaList.size()>0">
			<s:iterator value="bssParaList">
				<TR>
					<TD class=column width="15%" align='right'>
						原始工单信息
					</TD>
					<TD width="85%" colspan="3">
						<s:property value="sheet_para_desc" />
					</TD>
				</TR>
			</s:iterator>
		</s:if>
		<s:else>
			<TR>
				<TD class=column width="15%" align='right'>
					原始工单信息
				</TD>
				<TD width="85%" colspan="3">
				</TD>
			</TR>
		</s:else>
	</s:if>
	<s:else>
		<TR>
			<TD class=column width="15%" align='right'>
				原始工单信息
			</TD>
			<TD width="85%" colspan="3">
			</TD>
		</TR>
	</s:else>
	<tr>
		<TD class=column width="15%" align='right'>
			回单信息
		</TD>
		<TD width="85%" colspan="3">
			0|||00|||成功
		</TD>
	</tr>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">关闭</a>
		</td>
	</TR>
</table>
