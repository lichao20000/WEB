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
	<caption>�豸ͳ����ϸ��Ϣ</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�豸���к�</th>
			<th>����</th>
			<th>�ͺ�</th>
			<th>Ӳ���汾</th>
			<th>����汾</th>
			<th>ע��ʱ��</th>
			<th>�ն˹��</th>
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
					<td colspan=8>�����������ܲ��𱨱���Ϣ</td>
				</tr>
			</s:else>

		</s:if>
		<s:else>
			<tr>
				<td colspan=8>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=8><span style="float: right;"> <lk:pages
						url="/itms/resource/EquipmentQuery!queryEquipmentDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
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
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
