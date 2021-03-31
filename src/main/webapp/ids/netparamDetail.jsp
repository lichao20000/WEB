<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
</script>


<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>LOID</th>
			<th>�豸���к�</th>
			<th>����״̬</th>
			<th>�ϱ�ʱ��</th>
			<th>���ʱ��</th>
			<th colspan="3">���һ�γ��Բ���ʧ��ԭ��</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="netparamList!=null">
			<s:if test="netparamList.size()>0">
				<s:iterator value="netparamList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
						<td colspan="3"><s:property value="reason" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="10">���ҵ���������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">���ҵ���������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="10" align="right">
				<lk:pages url="/ids/DeviceMonitoringQuery!queryNetparamByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>