
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<input type="hidden" name="deviceSN" value="<s:property value="deviceSN" />" />
<table class="listtable">
	<caption>�豸 <s:property value="deviceSN" /> ������Ϣ</caption>
	<thead>
		<tr>
			<th>ִ��ʱ��</th>
			<th>�·�ʱ��</th>
			<th>����ִ��״̬</th>
			<th>�������</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="configList!=null">
			<s:if test="configList.size()>0">
				<s:iterator value="configList">
					<tr>
						<td><s:property value="start_time" /></td>
						<td><s:property value="end_time"/></td>
						<td><s:property value="status" /></td>
						<td><s:property value="result_desc" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>
						<s:if test='openstatus=="1"'>
							�豸��������Ҫ�󣬲����·�����!
						</s:if>
						<s:else>
							ϵͳû��������Ϣ!
						</s:else>
					</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д�ҵ����Ϣ!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6" align="right"><a
				href="javascript:configInfoClose()">�ر�</a></td>
		</tr>
	</tfoot>
</table>
