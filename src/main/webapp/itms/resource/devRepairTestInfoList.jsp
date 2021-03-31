<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.document.getElementById("queryButton").disabled = false;
		parent.dyniframesize();
	});

	function getRepairDevDetail(device_serialnumber,attribution_city) {
		// ����������
		attribution_city = encodeURI(attribution_city);  
		attribution_city = encodeURI(attribution_city);  
		var page = "<s:url value='/itms/resource/devRepairTestInfo!queryRepairDevDetail.action'/>?device_serialnumber="+device_serialnumber+"&attribution_city="+attribution_city;
		window.open(page,"","left=20,top=20,width=1200,height=600,resizable=no,scrollbars=yes");
	};
</script>
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="it_table">
		<tbody>
			<tr>
				<th>ά�޳���</th>
				<th>���ڱ���</th>
				<th>�豸���к�</th>
				<th>�ն˳���</th>
				<th>�豸�ͺ�</th>
				<th>�����</th>
				<th>��������</th>
				<th>��������</th>
				<th>���޳�������</th>
				<th>����ʹ�úϸ����</th>
				<th>����</th>
			</tr>
			<s:if test="DevRepairTestInfoList != null ">
				<s:if test="DevRepairTestInfoList.size() > 0">
					<s:iterator value="DevRepairTestInfoList">
						<tr>
							<td><s:property value="repair_vendor" /></td>
							<td><s:property value="insurance_status" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="device_vendor" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="check_result" /></td>
							<td><s:property value="attribution_city" /></td>
							<td><s:property value="send_city" /></td>
							<td><s:property value="manufacture_date" /></td>
							<td><s:property value="qualified_status" /></td>
							<td><a class="itta_more" href="javascript:void(0);"
								onclick="getRepairDevDetail('<s:property value="device_serialnumber" />','<s:property value="attribution_city" />')">��ϸ��Ϣ</a></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11 align=left>û�в�ѯ��������ݣ�</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11 align=left>û�в�ѯ��������ݣ�</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11" align="right">[ ͳ������ : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/resource/devRepairTestInfo!queryRepairDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</div>
