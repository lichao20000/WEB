<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查询结果</title>

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
   				alert("单次最多只能导入1000行，请分次导入！");
   				return;
			}
			if(ajax < 0){
				alert('文件解析出错，或上传的文件格式不正确，请检查');
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
	<caption>家庭网关语音信息查询</caption>
	<thead>
		<tr>
			<th>语音IP</th>
			<th>LOID</th>
			<th>局向</th>
			<th>终端序列号</th>
			<th>终端厂家</th>
			<th>终端型号</th>
			<th>终端软件版本</th>
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
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="toExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>

</html>