<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<TABLE class=listtable width='100%' bgcolor='#999999'>
	<thead>
		<TH>
			������
		</TH>
		<TH>����ֵ</TH>
		<TH>����</TH>
	</thead>
	<tbody>
		<s:if test="null==valueList">
			<TR bgcolor=#FFFFFF>
				<td colspan="3">
					��ȡʧ�ܣ�
				</td>
			</TR>
		</s:if>
		<s:else>
			<s:iterator value="valueList">
			<TR bgcolor=#FFFFFF>
				<td>
					<s:property value="name"/>
				</td>
				<td>
					<s:property value="value"/>
				</td>
				<td>
					<s:property value="type"/>
				</td>
			</TR>
			</s:iterator>
		</s:else>
	</tbody>
</TABLE>
