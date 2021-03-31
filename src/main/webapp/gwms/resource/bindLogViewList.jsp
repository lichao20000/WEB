<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<%
	/**
		 *  
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2010-5-21
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
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			统计结果
		</caption>
		<thead>
			<tr>
				<th>安装地点</th>
				<th>操作人</th>
				<th>设备序列号</th>
				<th>用户账号</th>
				<th>安装时间</th>
				<th>操作方式</th>
				<th>操作类型</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rsList">
				<tr bgcolor="#FFFFFF">
					<td class=column1><s:property value="cityName"/></td>
					<td class=column1><s:property value="dealstaff"/></td>
					<td class=column1><s:property value="deviceSn"/></td>
					<td class=column1><s:property value="username"/></td>
					<td class=column1><s:property value="binddate"/></td>
					<td class=column1><s:property value="userline"/></td>
					<td class=column1><s:property value="operType"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tr>
			<td colspan="7" align="right">
				<lk:pages url="/gwms/resource/bindLogView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>