<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="../../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>报表下载任务统计视图</title>
<%
	/**
		 * 报表下载任务统计视图
		 * 
		 * @author guankai
		 * @version 1.0
		 * @since 2020-04-20
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
	
	
	function downloadFile(status,fileName) {
	
			if("未完成"== status){
				alert("文件未生成！");
				return;
			}else{
				$("input[@name='filename']").val(fileName);
				var url = "<s:url value='/itms/report/fileExportReport!download.action'/>";
				document.getElementById("mainForm").action = url;
				document.getElementById("mainForm").submit();
				document.getElementById("mainForm").reset();
			}
	
	}
</script>

</head>

<body>
<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
		<input type="hidden" name="filename" value="">
</FORM>
<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th>导出操作人</th>
			<th>导出时间</th>
			<th>文件生成时间</th>
			<th>说明</th>			
			<th>文件导出状态</th>
			<th>导出文件名</th>
			<th>地市</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="data">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="fileexportuser"/></td>
				<td class=column1><s:property value="fileexporttime"/></td>
				<td class=column1><s:property value="filefinishtime"/></td>
				<td class=column1><s:property value="fileexportdesc"/></td>
				<td class=column1><s:property value="status"/></td>
				<td class=column1><a href="javascript:;" onclick="downloadFile('<s:property value="status"/>','<s:property value="filename"/>')"><s:property value="filename"/></a></td>
				<td class=column1><s:property value="cityname"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7" align="right">
				<lk:pages url="/itms/report/fileExportReport!getFileExportInfo.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				<%--<ms:simplePages url="/itms/report/fileExportReport!getFileExportInfo.action" />--%>
			</td>
		</tr>
	</tfoot>
</body>
</html>