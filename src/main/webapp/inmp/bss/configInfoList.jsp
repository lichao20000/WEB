
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<input type="hidden" name="deviceSN" value="<s:property value="deviceSN" />" />
<table class="listtable">
	<caption>设备 <s:property value="deviceSN" /> 配置信息</caption>
	<thead>
		<tr>
			<th>业务名称</th>
			<th>执行时间</th>
			<th>下发时间</th>
			<th>工单执行状态</th>
			<th>结果描述</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="configInfoList!=null">
			<s:if test="configInfoList.size()>0">
				<s:iterator value="configInfoList">
					<tr>
						<td><s:property value="serviceName" /></td>
						<td><s:property value="start_time" /></td>
						<td><s:property value="end_time"/></td>
						<td><s:property value="status" /></td>
						<td><s:property value="fault_reason" /></td>
						<td><a
							href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />')">详细信息</a>|
						<a
							href="javascript:configLog('<s:property value="deviceSN" />','<s:property value="deviceId" />','<s:property value="servTypeId" />','<s:property value="servstauts" />','<s:property value="wanType" />')">历史配置</a>|
							<a href="javascript:solutions('<s:property value="result_id" />','<s:property value="deviceSN" />')">处理意见</a>
						<s:if test='service_id=="1401"'>
							<s:if test='result_id=="-8"'>
								|<a
									href="javascript:resetData('<s:property value="userId" />','<s:property value="deviceId" />','<s:property value="oui" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />','<s:property value="servstauts" />')">重新激活</a>
							</s:if>
						</s:if>
						
						</td>
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
		 <%  if(LipossGlobals.isXJDX()) {%>
		<s:if test="softUpList!=null">
			<s:if test="softUpList.size()>0">
				<s:iterator value="softUpList">
					<tr>
                                <td><s:property value="serviceName" /></td>
                                <td><s:property value="start_time" /></td>
                                <td><s:property value="end_time"/></td>
                                <td><s:property value="status" /></td>
                                <td><s:property value="fault_reason" /></td>
                                <td><a
                                     href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />')">详细信息</a>|
                                     <a href="javascript:solutions('<s:property value="result_id" />','<s:property value="deviceSN" />')">处理意见</a>
                                     <s:if test='result_id=="-8"'>|<a href="javascript:resetData('<s:property value="userId" />','<s:property value="deviceId" />','<s:property value="oui" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />','<s:property value="servstauts" />')">重新激活</a>
                                      </s:if>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
		  <%} %>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6" align="right"><a
				href="javascript:configInfoClose()">关闭</a></td>
		</tr>
	</tfoot>
</table>
