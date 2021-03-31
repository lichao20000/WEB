<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO绑定日志查询结果</title>
<%
	/**
		 * EVDO绑定日志查询结果
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2009-10-29
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>

</head>

<body>
	<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
		<tr>
			<th>数据卡</th>
			<th>设备序列号</th>
			<th>操作类型</th>
			<th>绑定类型</th>
			<th>绑定时间</th>
		</tr>
		<s:iterator value="dataList" var="dataList">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="esn"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<td class=column1><s:property value="oper_type"/></td>
				<td class=column1><s:property value="bind_type"/></td>
				<td class=column1><s:property value="binddate"/></td>							
			</tr>
		</s:iterator>
		<tr bgcolor="#FFFFFF">
			<td colspan="8" align="right">
				<lk:pages url="/bbms/service/EVDOBindLog!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>