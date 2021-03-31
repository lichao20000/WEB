<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function downloadcase(fileName) {
		var iffile = "<s:url value='/itms/resource/deviceTestAccountACT!isfileexit.action'/>";
		$.post(iffile,{
			fileName : fileName
		},function(ajax){
			if("1"== ajax){
				alert("文件不存在！");
				return;
			}else{
				$("input[@name='fileName']").val(fileName);
				var url = "<s:url value='/itms/resource/deviceTestAccountACT!download.action'/>";
				document.getElementById("mainForm").action = url;
				document.getElementById("mainForm").submit();
				document.getElementById("mainForm").reset();
			}
		});
		
	}
</script>
<div>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
		<input type="hidden" name="fileName" value="">
		</FORM>
	<table id="updatetable" style="display: none">
		<tr>
			<td>blue</td>
		</tr>
	</table>
	<table class="listtable">
		<caption>统计结果</caption>
		<thead>
			<tr>
				<th>设备序列号</th>
				<th>厂商</th>
				<th>型号</th>
				<th>硬件版本</th>
				<th>软件版本</th>
				<th>测试时间</th>
				<th>操作人</th>
				<th width="10%">下载</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="testAccountList != null ">
				<s:if test="testAccountList.size() > 0">
					<s:iterator value="testAccountList">
						<tr align="center">
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="complete_time" /></td>
							<td><s:property value="accname" /></td>
							<td>
							<a href="javascript:void(0);" onClick="downloadcase('<s:property value="file_path" />');">
							<font color='red'><s:property value="file_path" /></font></a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=8 align=left>没有查询到相关数据！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8" align="right" height="15">
				<lk:pages
						url="/itms/resource/deviceTestAccountACT!getList.action" showType=""
						isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>

</div>
