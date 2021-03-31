
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<input type="hidden" name="deviceSN" value="<s:property value="deviceSN" />" />
<table class="listtable">
	<caption>设备 <s:property value="deviceSN" /> 配置信息</caption>
	<thead>
		<tr>
			<th>执行时间</th>
			<th>下发时间</th>
			<th>工单执行状态</th>
			<th>结果描述</th>
			<th>操作</th>
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
							设备符合配置要求，不用下发配置!
						</s:if>
						<s:else>
							系统没有配置信息!
						</s:else>
					</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>系统没有此业务信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6" align="right"><a
				href="javascript:configInfoClose()">关闭</a></td>
		</tr>
	</tfoot>
</table>
