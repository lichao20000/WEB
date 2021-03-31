<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ѯ���</title>

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
});

function toExcel(){
	var gwShare_fileName = $('#gwShare_fileName').val();
	// alert(gwShare_fileName);
	var url = "<s:url value='/itms/resource/QueryDeviceByVoipIpACT!ipCount.action'/>";
	$.post(url, {
		gwShare_fileName: gwShare_fileName
		}, function(ajax) {
			if(ajax>1000){
   				alert("�������ֻ�ܵ���1000�У���ִε��룡");
   				return;
			}
			if(ajax < 0){
				alert('�ļ������������ϴ����ļ���ʽ����ȷ������');
				return;
			}
			else{
				var mainForm = document.getElementById("excelForm");
				mainForm.action="<s:url value='/itms/resource/QueryDeviceByVoipIpACT!getExcel.action'/>";
				mainForm.submit();
			}
	});
}
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>��ͥ����������Ϣ��ѯ</caption>
	<thead>
		<tr>
			<th>����IP</th>
			<th>LOID</th>
			<th>����</th>
			<th>�ն����к�</th>
			<th>�ն˳���</th>
			<th>�ն��ͺ�</th>
			<th>�ն�����汾</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null">
			<s:if test="data.size()>0">
				<s:iterator value="data">
					<tr>
						<td><s:property value="ipaddress" /></td>
						<td><s:property value="username" /></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="softwareversion" /></td>
						
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7><s:property value="msg" /></td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7" align="right"><lk:pages
				url="/itms/resource/QueryDeviceByVoipIpACT.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		<tr>
			<td colspan="7">
			<form id="excelForm" action="">
				<input id="gwShare_fileName" name="gwShare_fileName" type="hidden" value='<s:property value="gwShare_fileName"/>'/>
			</form>
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="toExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>

</html>