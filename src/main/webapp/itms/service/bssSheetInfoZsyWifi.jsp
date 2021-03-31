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
						业务类型
					</TD>
					<TD width="35%">
						<s:property value="serv_type" />
					</TD>
					<TD class=column width="15%" align='right'>
						操作类型
					</TD>
					<TD width="35%">
						密码修改
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
                        天翼通行证
                    </TD>
                    <TD width="35%">
                       <s:property value="passwd" />
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
