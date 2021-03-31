<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  预检预修告警信息
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
	 String serialnumber = request.getParameter("serialnumber");
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
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});

function ListToExcel(taskname,serialnumber,starttime2,endtime2){
	var page = "<s:url value='/ids/httpUpload!toExcel.action'/>?"
			+"taskname="+taskname
			+"&serialnumber="+serialnumber
			+ "&starttime2=" + starttime2
			+ "&endtime2=" + endtime2;
	document.all("childFrm").src=page;		
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th >任务名称</th>
				<th >属地</th>
				<th >设备厂商</th>
				<th >型号</th>
				<th >设备序列号</th>
				<th >上网方式</th>
				<th >采集开始时间</th>
				<th >采集结束时间</th>
				<th >上传字节数</th>
				<th >测速上传速率</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
					<td ><s:hidden value="task_id" /></td>
						<tr>
							<td ><s:property value="task_name" /></td>
							<td ><s:property value="city_name" /></td>
							<td ><s:property value="vendor_name" /></td>
							<td ><s:property value="device_model" /></td>
							<td ><s:property value="device_serialnumber" /></td>
							<td ><s:property value="wan_type" /></td>
							<td ><s:property value="bomtime" /></td>
							<td ><s:property value="eomtime" /></td>
							<td ><s:property value="totalbytessent" /></td>
							<td ><s:property value="upload_pert" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11"><span style="float:right"><lk:pages
						url="/ids/httpUpload!queryDevBySerialnumber.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
						
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="taskname" />','<s:property value="serialnumber"/>','<s:property value="starttime2"/>','<s:property value="endtime2"/>')">		
				</td>
			</tr>
	<tr STYLE="display: none">
			<td colspan="11"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>