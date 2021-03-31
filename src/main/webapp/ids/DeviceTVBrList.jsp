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
	// String tvbTotal = request.getParameter("tvbTotal");
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

function ListToExcel(starttime,endtime,cityId,temperature,bais_current,vottage){
	var page = "<s:url value='/ids/deviceTVB!queryByTVBListExcel.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&temperature=" + temperature
		+ "&bais_current=" + bais_current
		+ "&vottage=" + vottage;
	document.all("childFrm").src=page;		
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th >逻辑SN</th>
				<th >设备序列号</th>
				<th >时间</th>
				<th >温度</th>
				<th >电流</th>
				<th >电压</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="rlist!=null">
				<s:if test="rlist.size()>0">
					<s:iterator value="rlist">
						<tr>
							<td ><s:property value="loid" /></td>
							<td ><s:property value="device_serialnumber" /></td>
							<td ><s:property value="upload_time" /></td>
							<td ><s:property value="temperature" /></td>
							<td ><s:property value="bais_current" /></td>
							<td ><s:property value="vottage" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="6"><span style="float:right"><lk:pages
						url="/ids/deviceTVB!queryByTVBList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="cityId"/>','<s:property value="temperature"/>','<s:property value="bais_current"/>','<s:property value="vottage"/>')">	
				</td>
			</tr>
	<tr STYLE="display: none">
			<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>