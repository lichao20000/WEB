<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>


<table class="listtable">
	<caption>
		用户绑定终端情况列表
	</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>宽带账号</th>
			<th>属地</th>
			<th>厂商</th>
			<th>型号</th>
			<th>业务开通时间</th>
			<th>绑定时间</th>
			<th>最近上线时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><s:property value="loid" /></td>
					<td><s:property value="broadband_account" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="vendor_name" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="opendate" /></td>
					<td><s:property value="binddate" /></td>
					<td><s:property value="last_time" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>
					系统没有相关的用户信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<span style="float: right;"> 
					<lk:pages url="/itms/report/userOpenBindACT!userOpenBindListForPage.action" 
						styleClass="" showType="" isGoTo="true" changeNum="true" /> 
				</span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表' style='cursor: hand'
					onclick="toExcel('<s:property value="cityId"/>', '<s:property value="starttime"/>',
					'<s:property value="endtime"/>')">
			</td>
		</tr>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
