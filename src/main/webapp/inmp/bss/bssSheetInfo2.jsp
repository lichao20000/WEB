<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="querytable">

	<TR>
		<th colspan="4">
			BSS������ϸ��Ϣ
		</th>
	</tr>
	<s:if test="bssParaList!=null">
		<s:if test="bssParaList.size()>0">
			<s:iterator value="bssParaList">
				<TR>
					<TD class=column width="15%" align='right'>
						ԭʼ������Ϣ
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
					ԭʼ������Ϣ
				</TD>
				<TD width="85%" colspan="3">
				</TD>
			</TR>
		</s:else>
	</s:if>
	<s:else>
		<TR>
			<TD class=column width="15%" align='right'>
				ԭʼ������Ϣ
			</TD>
			<TD width="85%" colspan="3">
			</TD>
		</TR>
	</s:else>
	<tr>
		<TD class=column width="15%" align='right'>
			�ص���Ϣ
		</TD>
		<TD width="85%" colspan="3">
			0|||00|||�ɹ�
		</TD>
	</tr>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">�ر�</a>
		</td>
	</TR>
</table>
