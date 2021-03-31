<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>Insert title here</title>
	<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<style type="text/css">
	/* ѡ������ʽ */
	.select{
		background-color: #E8E8FF;
	}
	</style>
<script type="text/javascript"	src="<s:url value="/lims/itv/zeroconf/js/rightMenu.js"/>"></script>
	<script type="text/javascript">
$(function(){
		showResultData();
		parent.dyniframesize();

	});


//���over�¼�
function m_over(obj){

	$(obj).addClass("select");
}
// ���out�¼�
function m_out(obj){

	$(obj).removeClass("select");
}

function editMac(orderId,packageNo,vendorName,supplyMode,deviceModel,mac,deviceSn,deviceId,type){
	parent.addeditMac(orderId,packageNo,vendorName,supplyMode,deviceModel,mac,deviceSn,deviceId,type);
}
//չʾ��ѯ���
	function showResultData()
	{
		$(window.parent.document).find("#tip_loading").hide();
		$(window.parent.document).find("#first").show();
	}
</script>
</head>
<body>
	<table class="listtable" align="center" width="100%">
		<thead>
			<tr align="center">
				<th width="">����</th>
				<th width="">��Ʒ�ͺ�</th>
				<th width="">MAC��ַ</th>
				<th width="">������ʽ</th>
				<th width="">��װ���</th>
				<th width="">����ID</th>
				<th width="">¼��ʱ��</th>
				<th width="">�������ʱ��</th>
				<th width="">������</th>
				<s:if test='%{isFlag=="1"}'>
					<th width="">����</th>
				</s:if>

			</tr>
		</thead>
		<tbody>
		<s:if test="macInfoList.size>0">
			<s:iterator var="topBox" value="macInfoList">
				<tr onmouseover="m_over(this);" onmouseout="m_out(this);">
					<td><s:property value="#topBox.vendor_name"/></td>
					<td><s:property value="#topBox.device_model"/></td>
					<td><s:property value="#topBox.cpe_mac"/></td>
					<td><s:property value="#topBox.supply_mode"/></td>
					<td><s:property value="#topBox.package_no"/></td>
					<td><s:property value="#topBox.order_id"/></td>
					<td><s:property value="#topBox.buy_time"/></td>
					<td><s:property value="#topBox.cpe_currentupdatetime"/></td>
					<td><s:property value="#topBox.staff_id"/></td>
					<s:if test='%{isFlag=="1"}'>
							<td><label
						onclick="javascript:editMac('<s:property value='#topBox.order_id'/>','<s:property value='#topBox.package_no'/>','<s:property value='#topBox.vendor_name'/>','<s:property value='#topBox.supply_mode'/>','<s:property value='#topBox.device_model'/>','<s:property value='#topBox.cpe_mac'/>','<s:property value='#topBox.device_sn'/>','<s:property value='#topBox.device_id'/>','2');">
					<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
						style='cursor: hand'> </label></td>
					</s:if>

				</tr>
			</s:iterator>
		</s:if>
		</tbody>
	</table>
	<table class="listtable" width=100%>
		<tfoot>
		<tr>
			<td align="right"><lk:pages url="/gtms/stb/resource/macQueryInfo!query.action" styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
	</table>
</body>
</html>
