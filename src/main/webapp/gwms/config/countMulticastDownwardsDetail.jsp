<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.system.*"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>

<table width="100%" class="listtable" id=userTable>
    <caption> �鲥���Ʋ������� </caption>
	<thead>
       <tr>
          <th width="10%"> �豸����</th>
          <th width="10%"> �ͺ�</th>
          <th width="20%"> ����汾</th>
          <th width="10%"> ����</th>
          <th width="20%"> �豸���к�</th>
          <th width="20%"> �ɼ�ʱ��</th>
          <th width="10%"> �ɼ����</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="multicastDownwardsDetail!=null ">
			<s:if test="multicastDownwardsDetail.size()>0">
				<s:iterator value="multicastDownwardsDetail">
					<tr>
					    <td align="center">
							<s:property value="vendor_name" />
						</td>
						<td align="center">
							 <s:property value="device_model" />
						</td>
						<td align="center">
						  <s:property value="software" />
						</td>
						<td align="center">
						     <s:property value="city_name" />
						</td>
						<td align="center">
						    <s:property value="device_serialnumber" />
						</td>
						 <td align="center">
							<s:property value="gathertime" />
						</td>
						 <td align="center">
							<s:property value="gatherRes" />
						</td>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
			<tr>
				<td colspan="7" align="right">&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
						url="/gwms/config/countMulticastBatch!mcDownwardsDetail.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
					<td colspan="7" align="right"><IMG
						SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
						style='cursor: hand' onclick="ToExcel('<s:property value="cityId" />','<s:property value="type" />',
						'<s:property value="vendorId" />','<s:property value="deviceModelId" />')">
					</td>
			</tr>
			<tr STYLE="display: none">
			 <td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		  </tr>
	</tfoot>
</table>
		
<script LANGUAGE="JavaScript">
$(function() {
	parent.dyniframesize();
});

function ToExcel(cityId,type,vendorId,deviceModelId) {
	var page="<s:url value='/gwms/config/countMulticastBatch!mcDownwardsDetailExcel.action'/>?"
		+"cityId=" + cityId +"&type="+type+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId;
	document.all("childFrm").src=page;
}

</script>