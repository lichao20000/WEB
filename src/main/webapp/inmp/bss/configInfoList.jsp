
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<input type="hidden" name="deviceSN" value="<s:property value="deviceSN" />" />
<table class="listtable">
	<caption>�豸 <s:property value="deviceSN" /> ������Ϣ</caption>
	<thead>
		<tr>
			<th>ҵ������</th>
			<th>ִ��ʱ��</th>
			<th>�·�ʱ��</th>
			<th>����ִ��״̬</th>
			<th>�������</th>
			<th>����</th>
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
							href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />')">��ϸ��Ϣ</a>|
						<a
							href="javascript:configLog('<s:property value="deviceSN" />','<s:property value="deviceId" />','<s:property value="servTypeId" />','<s:property value="servstauts" />','<s:property value="wanType" />')">��ʷ����</a>|
							<a href="javascript:solutions('<s:property value="result_id" />','<s:property value="deviceSN" />')">�������</a>
						<s:if test='service_id=="1401"'>
							<s:if test='result_id=="-8"'>
								|<a
									href="javascript:resetData('<s:property value="userId" />','<s:property value="deviceId" />','<s:property value="oui" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />','<s:property value="servstauts" />')">���¼���</a>
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
                                     href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />')">��ϸ��Ϣ</a>|
                                     <a href="javascript:solutions('<s:property value="result_id" />','<s:property value="deviceSN" />')">�������</a>
                                     <s:if test='result_id=="-8"'>|<a href="javascript:resetData('<s:property value="userId" />','<s:property value="deviceId" />','<s:property value="oui" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />','<s:property value="servstauts" />')">���¼���</a>
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
				href="javascript:configInfoClose()">�ر�</a></td>
		</tr>
	</tfoot>
</table>
