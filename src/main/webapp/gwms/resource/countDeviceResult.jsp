<%@  page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备按版本统计</title>
<%
	/**
	 *
	 *
	 * @author
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	$("#trData", parent.document).css("display","none");
	$('#cha',window.parent.document).attr("disabled", false);
});
function  queryDataForExcel(vendor,devicemodel,devicetype,rela_dev_type,is_check,startTime,endTime,protocol2,protocol1,protocol0){
	parent.queryDataForExcel(vendor,devicemodel,devicetype,rela_dev_type,is_check,startTime,endTime,protocol2,protocol1,protocol0);
}

function detail(devicetype_id,cityID){

	parent.detail(devicetype_id,cityID);
}

function detailAll(cityId,filterDevicetype){

	parent.detailAll(cityId,filterDevicetype);
}



</script>


</head>

<body>
<table class="listtable" width="100%">

	<thead>
		<tr>
			<s:iterator value="titleList" var="titleList" status="servSt">
				<th nowrap><s:property /></th>
			</s:iterator>
		</tr>
	</thead>
	<tbody>

		<s:if test="dataList.size()>0">
			<s:iterator value="dataList" var="dataList">
				<tr>
					<td nowrap rowspan="<s:property value="childInt"/>" align='right'>
					<s:property value="vendor_name" /></td>
					<s:iterator value="childList">
						<td nowrap rowspan="<s:property value="childInt"/>" align='right'>
						<s:property value="device_model" /></td>
						<s:iterator value="num" var="num">
							<s:iterator status="typeList">
								<s:if test="#typeList.getIndex()==0">
									<td nowrap align='right'><s:property escapeHtml="false" /></td>
								</s:if>
								<s:else>
									<td nowrap align='right'><s:property escapeHtml="false" /></td>
								</s:else>
							</s:iterator>
				</tr>
			</s:iterator>
			</s:iterator>
			</s:iterator>
<tr>
				<td colspan="6" align='right'>小计</td>
				<s:iterator value="totalNumList" var="totalNumList">
					<td nowrap align='right'><s:property escapeHtml="false" /></td>
				</s:iterator>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td colspan=21>系统没有该条件的设备！</td>
			</tr>
		</s:else>
		<tr>
			<td colspan=21><a
				href="javascript:queryDataForExcel('<s:property value="vendor"/>','<s:property value="devicemodel"/>','<s:property value="devicetype"/>','<s:property value="rela_dev_type"/>','<s:property value="is_check"/>','<s:property value="startTime"/>','<s:property value="endTime"/>','<s:property value="protocol2"/>','<s:property value="protocol1"/>','<s:property value="protocol0"/>');">
			<img src="../../images/excel.gif" border="0" width="16" height="16"></img>
			</a></td>
		</tr>
	</tbody>
</table>
</body>
<script>
</script>
</html>
