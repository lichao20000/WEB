<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备批量重启统计</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<% 
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
	
	int cols=9;
	if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		cols=8;
	}
%>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	parent.isButn(true);
});

function ToExcel() {
	parent.ToExcel();
}
</script>
</head>

<body>
<table class="listtable" id="listTable">
	<%if(!"1".equals(gw_type)){ %>
		<ms:inArea areaCode="nx_dx" notInMode="true">
			<caption>机顶盒批量重启统计 </caption>
		</ms:inArea>
		<ms:inArea areaCode="nx_dx" notInMode="false">
			<caption>机顶盒批量重启查询</caption>
		</ms:inArea>
	<%}else{ %>
		<ms:inArea areaCode="nx_dx" notInMode="true">
			<caption>光猫批量重启统计 </caption>
		</ms:inArea>
		<ms:inArea areaCode="nx_dx" notInMode="false">
			<caption>光猫批量重启查询</caption>
		</ms:inArea>
	<%}%>	
	<thead>
		<tr>
			<ms:inArea areaCode="nx_dx" notInMode="true">
				<th>任务id</th>
			</ms:inArea>
			<ms:inArea areaCode="nx_dx" notInMode="false">
				<%if(!"1".equals(gw_type)){ %>
					<th>业务账号</th>
				<%}else{ %>
					<th>LOID</th>
				<%}%>	
			</ms:inArea>
			<th>厂家</th>
			<th>型号</th>
			<th>版本</th>
			<th>属地</th>
			<th>设备序列号</th>
			<ms:inArea areaCode="nx_dx" notInMode="true">
				<th>任务定制时间</th>
			</ms:inArea>
			<th>重启结果</th>
			<th>重启时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="restartDevMap!=null && restartDevMap.size()>0">
			<s:iterator value="restartDevMap">
					<tr>
						<ms:inArea areaCode="nx_dx" notInMode="true">
							<td><s:property value="task_id" /></td>
						</ms:inArea>
						<ms:inArea areaCode="nx_dx" notInMode="false">
							<%if(!"1".equals(gw_type)){ %>
								<td><s:property value="serv_account" /></td>
							<%}else{ %>
								<td><s:property value="loid" /></td>
							<%}%>	
						</ms:inArea>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="softwareversion" /></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<ms:inArea areaCode="nx_dx" notInMode="true">
							<td><s:property value="add_time" /></td>
						</ms:inArea>
						<td><s:property value="restart_status" /></td>
						<td><s:property value="restart_time" /></td>
					</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="<%=cols%>">没有重启的设备信息</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="<%=cols%>" align="right">
				<lk:pages url="/itms/resource/DevBatchRestartQuery!devBatchRestartQueryInfo.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<tr>
			<td colspan="<%=cols%>" align="right">
				<img SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()" >
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>