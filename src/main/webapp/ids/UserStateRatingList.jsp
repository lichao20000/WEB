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
			<th >LOID</th>
			<th>�豸���к�</th>
			<th>��˥(dBm)</th>
			<th>�¶�(��)</th>
			<th>����(2΢��)</th>
			<th>��ѹ(100΢��)</th>
			<th>�˿�״̬</th>
			<th>�ϱ�ʱ��</th>
			<th>���ʱ��</th>
			<th>��������</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="userStatusList!=null">
			<s:if test="userStatusList.size()>0">
				<s:iterator value="userStatusList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="droop" /></td>
						<td><s:property value="temperature" /></td>
						<td><s:property value="bais_current" /></td>
						<td><s:property value="vottage" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
						<td><s:property value="userLevel" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="10">����״̬��������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">����״̬��������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10" align="right">
				<lk:pages url="/ids/UserStateRating!queryPonStatusByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>