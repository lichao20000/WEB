<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<TABLE class=listtable width='100%' bgcolor='#999999'>
	<thead>
		<TH>
			参数名
		</TH>
		<TH>参数值</TH>
		<TH>类型</TH>
	</thead>
	<tbody>
		<s:if test="null==valueList">
			<TR bgcolor=#FFFFFF>
				<td colspan="3">
					获取失败！
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
