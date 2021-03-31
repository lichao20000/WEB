<%@ page import="com.linkage.module.gwms.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%
String status = StringUtil.getStringValue(request.getParameter("status"));
String cityId = StringUtil.getStringValue(request.getParameter("cityId"));
%>

<title>分组管理基本信息报表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});


	function ToExcel(){
		var cityId = '<%=cityId%>';
		var status = '<%=status%>';
		var page="<s:url value='/gwms/report/groupManage!getDetailExcel.action'/>";
		page+="?cityId="+cityId+"&status="+status;
		document.all("childFrm").src=page;
	}
</script>

<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th>OUI</th>
			<th>终端序列号</th>
			<th>终端唯一标识</th>
			<th>宽带账号</th>
			<th>终端厂商</th>
			<th>型号名称</th>
			<th>用户姓名</th>
			<th>用户住址</th>
			<th>联系电话</th>
			<th>用户备注</th>
			<th>终端硬件版本</th>
			<th>软件版本</th>
			<th>IP地址</th>
			<th>终端归属域路径</th>
			<th>备注</th>
			<th>终端注册状态</th>
			<th>逻辑ID</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="data">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="oui"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<td class=column1><s:property value="osn"/></td>
				<td class=column1><s:property value="username"/></td>
				<td class=column1><s:property value="vendor_name"/></td>
				<td class=column1><s:property value="device_model"/></td>
				<td class=column1><s:property value="linkman"/></td>
				<td class=column1><s:property value="linkaddress"/></td>
				<td class=column1><s:property value="mobile"/></td>
				<td class=column1><s:property value="userremark"/></td>
				<td class=column1><s:property value="hardwareversion"/></td>
				<td class=column1><s:property value="softwareversion"/></td>
				<td class=column1><s:property value="loopback_ip"/></td>
				<td class=column1><s:property value="city_name"/></td>
				<td class=column1><s:property value="remark"/></td>
				<td class=column1><s:property value="openstatus"/></td>
				<td class=column1><s:property value="loid"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="17" align="right">
				<lk:pages url="/gwms/report/groupManage!getGroupManageList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tr>
			<td colspan="17" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					 style='cursor: hand' onclick="ToExcel()"></td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="17">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
