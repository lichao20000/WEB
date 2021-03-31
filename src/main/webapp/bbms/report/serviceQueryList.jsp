<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		用户业务使用
	</caption>
	<s:if test="data.size()>0">
		<s:iterator value="data" var="usermap">
			<thead>
				<tr>
					<th colspan="4">
						用户设备信息
					</th>
				</tr>
			</thead>
			<tbody>

				<tr>
					<td width="25%" class="green_k">
						用户账号
					</td>
					<td width="25%" bgcolor="#FFFFFF">
						<s:property value="username" />
					</td>
					<td width="25%" class="green_k">
						属地
					</td>
					<td width="25%" bgcolor="#FFFFFF">
						<s:property value="city_name" />
					</td>
				</tr>
				<tr>
					<td class="green_k">
						设备序列号
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="device" />
					</td>
					<td class="green_k">
						厂商
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="vendor_add" />
					</td>
				</tr>
				<tr>
					<td class="green_k">
						型号
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="device_model" />
					</td>
					<td class="green_k">
						软件版本
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="softwareversion" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center" bgcolor="#D0D0FF">
						<strong>业务信息</strong>
					</td>
				</tr>
				<tr>
					<td bgcolor="#E1EEEE" align="center">
						<strong>业务ID</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>业务种类</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>是否开启</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>采集时间</strong>
					</td>
				</tr>
				<s:iterator value="#usermap.servicelist" var="servicemap">
					<tr>
						<td width="25%" align="center">
							<s:property value="service_id" />
						</td>
						<td width="25%" align="center">
							<s:property value="service_name" />
						</td>
						<td width="25%" align="center">
							<s:property value="service_result" />
						</td>
						<td width="25%" align="center">
							<s:property value="gather_time" />
						</td>
					</tr>
				</s:iterator>
			</tbody>
			<tr>
				<td height="20" colspan="4">
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
	<tbody>
	<tr>
		<td colspan="4">
				用户或设备没有相关业务信息
			</td>
		</tr>
	</tbody>
	</s:else>
	<tfoot>
		<tr>
			<td colspan="4">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="serviceId"/>','<s:property value="device_serialnumber"/>','<s:property value="userName"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


