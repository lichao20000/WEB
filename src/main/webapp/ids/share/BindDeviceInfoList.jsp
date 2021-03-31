<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>每月新装设备报表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		var a = $("#bind_total").val();
		if(a > 100000){
			alert("数据超过10w条，不允许导出，请重新选择更小的时间段！");
			return;
		}else{
			parent.ToExcel();
		}
	}
</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<%if(!"1".equals(gw_type)){ %>
	<caption>更换光猫记录 </caption>
	<%}else{ %>	
	<caption>更换光猫记录 </caption>
	<%}%>	
	<thead>
		<tr>
			<%if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
			<th align="center">属地</th>
			<%}%>
			<th align="center">loid</th>
			<th align="center">宽带账号</th>
			<th align="center">旧厂家</th>
			<th align="center">旧SN</th>
			<!-- <th align="center">旧软件版本</th>
			<th align="center">旧硬件版本</th> -->
			<th align="center">旧型号</th>
			<th align="center">新厂家</th>
			<th align="center">新SN</th>
			
			<!-- <th align="center">新软件版本</th>
			<th align="center">新硬件版本</th> -->
			<th align="center">新型号</th>
			<s:if test="isNewNeed != 'yes'">
			  <th align="center">解绑时间</th>
			</s:if>
			<th align="center">绑定时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null">
			<s:if test="deviceList.size()>0">
				<s:iterator value="deviceList">
						<tr>
						<%if("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
							<td class=column1><s:property value="cityName"/></td>
						<%}%>						
							<td class=column1><s:property value="loid"/></td>
							<td class=column1><s:property value="username"/></td>
							<td class=column1><s:property value="old_vendor_name"/></td>
							<td class=column1><s:property value="old_devsn"/></td>
							<%-- <td class=column1><s:property value="old_hardwareversion"/></td>
							<td class=column1><s:property value="old_softwareversion"/></td> --%>
							<td class=column1><s:property value="old_device_model"/></td>
							
							<td class=column1><s:property value="vendor_name"/></td>
							<td class=column1><s:property value="new_devsn"/></td>
							<%-- <td class=column1><s:property value="softwareversion"/></td>
							<td class=column1><s:property value="hardwareversion"/></td> --%>
							<td class=column1><s:property value="device_model"/></td>
							<s:if test="isNewNeed != 'yes'">
							   <td class=column1><s:property value="unbinddate"/></td>
							</s:if>
							<td class=column1><s:property value="binddate"/></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=14>没有更换的设备信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=14>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="14" align="right"><lk:pages
						url="/gwms/service/gwBindDevQuery!queryTabBindList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">(尽量在夜间22:00之后操作!)</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>