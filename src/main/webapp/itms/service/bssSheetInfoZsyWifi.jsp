<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="querytable">

	<TR>
		<th colspan="4">
			BSS������ϸ��Ϣ
		</th>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>
						ҵ������
					</TD>
					<TD width="35%">
						<s:property value="serv_type" />
					</TD>
					<TD class=column width="15%" align='right'>
						��������
					</TD>
					<TD width="35%">
						�����޸�
					</TD>
				</TR>
				<TR>
                    <TD class=column width="15%" align='right'>
                        WLAN
                    </TD>
                    <TD width="35%">
                        SSID1
                    </TD>
                    <TD class=column width="15%" align='right'>
                        ����ͨ��֤
                    </TD>
                    <TD width="35%">
                       <s:property value="passwd" />
                    </TD>
                </TR>
				<tr>
					<TD class=column width="15%" align='right'>
						�ص���Ϣ
					</TD>
					<TD width="85%" colspan="3">
						0|||00|||�ɹ�
					</TD>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>
					������Ϣ�����ڻ����!
				</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>
				�û���Ϣ����!
			</td>
		</tr>
	</s:else>

	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">�ر�</a>
		</td>
	</TR>
</table>
