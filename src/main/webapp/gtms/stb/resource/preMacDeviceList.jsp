<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<script language="JavaScript">
	
$(function(){
	parent.dyniframesize();
	
});	

// ɾ��
function delete1(id){
	if(confirm("�Ƿ�ȷ��ɾ��������")){
		var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!delete.action'/>";
		
		$.post(url, {
			preMacId : id
			}, function(ajax) {
				if("1" == ajax){
					alert("MACǰ׺�����ڣ�");
				}else{
					alert("ɾ���ɹ���");
				}
				parent.query();
		});
	}
}

// �༭
function edit(id,pre_mac,vendor_name,device_model,softwareversion,hardwareversion,
		vendor_id,device_model_id)
{
	var page = "<s:url value='/gtms/stb/resource/preMacDeviceUpdate.jsp?preMacId='/>" + id 
				+ "&pre_mac=" + pre_mac 
				+ "&vendor_name=" + vendor_name 
				+ "&device_model=" + device_model 
				+ "&softwareversion=" + softwareversion 
				+ "&hardwareversion=" + hardwareversion 
				+ "&vendor_id="+vendor_id
				+ "&device_model_id="+device_model_id;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

</script>
</head>

<body>
	<table class="listtable" width="100%" align="center">
		<thead>
			<tr>
				<th align="center" width="15%">MACǰ׺</th>
				<!-- <th align="center" width="15%">ƽ̨����</th> -->
				<th align="center" width="15%">����</th>
				<th align="center" width="15%">�ͺ�</th>
				<th align="center" width="15%">����汾</th>
				<th align="center" width="15%">Ӳ���汾</th>
				<th align="center" width="10%">����</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="macDeviceList">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="pre_mac" /></td>
					<!-- <td><s:property value="platform_name" /></td> -->
					<td><s:property value="vendor_add" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="hardwareversion" /></td>
					<td align="center">
						<label onclick="javascript:edit('<s:property value='id'/>',
														'<s:property value='pre_mac'/>',
														'<s:property value='vendor_add'/>',
														'<s:property value='device_model'/>',
														'<s:property value='softwareversion'/>',
														'<s:property value='hardwareversion'/>',
														'<s:property value='vendor_id'/>',
														'<s:property value='device_model_id'/>');">
							<img SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭' style='cursor: hand'>
						</label>
						<label onclick="javascript:delete1('<s:property value='id'/>');">
							<img src="<s:url value="/images/del.gif"/>" border='0' alt='ɾ��' style='cursor: hand'>
						</label>
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="8" align="right">
					<lk:pages url="/gtms/stb/resource/PreMacDeviceACT!queryMacDeviceTypeList.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</table>
</body>
