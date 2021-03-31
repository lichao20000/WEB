<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
	function ListToExcel(cityId, endOpenDate,gn) {
		var page = "<s:url value='/itms/resource/FunctionDeploymentByArea!quertFunctionDeployByAreaDevExcel.action'/>?"
				+ "city_id="+ cityId
				+ "&gn"+ gn
				+ "&endOpenDate="+ endOpenDate;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">
	<caption>�������ܲ��𱨱��������</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>LOID</th>
			<th>�ն����к�</th>
			<th>����ʱ��</th>
			<th>��ͨ״̬</th>
			<th>��������</th>
			<th>�豸�ͺ�</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevList.size()>0">
			<s:iterator value="deployDevList">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="loid" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="start_time" /></td>
					<td><s:property value="status" /></td>
					<td><s:property value="timelist" /></td>
					<td><s:property value="device_model" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>�����������ܲ��𱨱���Ϣ</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=7><span style="float: right;"> <lk:pages
						url="/itms/resource/FunctionDeploymentByArea!quertFunctionDeployByAreaDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="endOpenDate"/>','<s:property value="gn"/>')">
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=7>
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
