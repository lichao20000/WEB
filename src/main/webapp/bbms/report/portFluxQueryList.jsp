<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		端口利用率
	</caption>
	<thead>
		<tr>
			<th>
				属地
			</th>
			<th>
				设备序列号
			</th>
			<th>
				设备IP
			</th>
			<th>
				端口信息
			</th>
			<th>
				平均利用率
			</th>
			<th>
				最大利用率
			</th>
			<th>
				客户名称
			</th>
			<th>
				用户账号
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="loopback_ip" />
					</td>
					<td>
						<s:property value="port_info" />
					</td>
					<td>
						<s:property value="avg_count" />

					</td>
					<td>
						<s:property value="max_count" />

					</td>
					<td>
						<s:property value="customer_name" />
					</td>
					<td>
						<s:property value="username" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">
					<s:property value="message" />
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="queryType" />','<s:property value="deviceSN" />','<s:property value="loopbackIp" />','<s:property value="userName" />','<s:property value="customerName" />','<s:property value="linkphone" />','<s:property value="reportType" />','<s:property value="stat_time" />')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


