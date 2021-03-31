<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		带宽占用率TopN
		<s:if test='reportType=="day"'>日报表</s:if>
		<s:if test='reportType=="week"'>周报表</s:if>
		<s:if test='reportType=="month"'>月报表</s:if>
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
			<td colspan="7">
				<s:property value="message" />
			</td>
		</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId" />','<s:property value="reportType" />','<s:property value="stat_time" />','<s:property value="countDesc" />')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


