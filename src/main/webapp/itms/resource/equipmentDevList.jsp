<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
	function ListToExcel(cityId, startOpenDate, endOpenDate, deviceType,
			isActive, specName, vendor, devicemodel, hardVersion, softVersion,
			temval) {
		var page = "<s:url value='/itms/resource/EquipmentQuery!queryEquipmentDevExcel.action'/>?"
				+ "city_id="
				+ cityId
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&deviceType="
				+ deviceType
				+ "&isActive="
				+ isActive
				+ "&specName="
				+ specName
				+ "&vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel
				+ "&hardVersion="
				+ hardVersion
				+ "&softVersion="
				+ softVersion
				+ "&temval=" + temval;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">
	<caption>设备统计详细信息</caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>设备序列号</th>
			<th>厂商</th>
			<th>型号</th>
			<th>硬件版本</th>
			<th>软件版本</th>
			<th>注册时间</th>
			<th>终端规格</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevList!=null">
			<s:if test="deployDevList.size()>0">
				<s:iterator value="deployDevList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="vendor_id" /></td>
						<td><s:property value="device_model_id" /></td>
						<td><s:property value="hardwareversion" /></td>
						<td><s:property value="softwareversion" /></td>
						<td><s:property value="complete_time" /></td>
						<td><s:property value="spec_id" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>暂无新增功能部署报表信息</td>
				</tr>
			</s:else>

		</s:if>
		<s:else>
			<tr>
				<td colspan=8>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=8><span style="float: right;"> <lk:pages
						url="/itms/resource/EquipmentQuery!queryEquipmentDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>',
				'<s:property value="startOpenDate"/>',
				'<s:property value="endOpenDate"/>',
				'<s:property value="deviceType"/>',
				'<s:property value="isActive"/>',
				'<s:property value="specName"/>',
				'<s:property value="vendor"/>',
				'<s:property value="devicemodel"/>',
				'<s:property value="hardVersion"/>',
				'<s:property value="softVersion"/>',
				'<s:property value="temval"/>')">
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=8>
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
