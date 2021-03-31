<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>规范版本查询</title>

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
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>设备厂商</th>
			<th>设备型号</th>
			<th>软件版本</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceVersionList!=null">
			<s:if test="deviceVersionList.size()>0">
				<s:iterator value="deviceVersionList">
					<tr>
						<td><s:property value="vendor_add" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="softwareversion" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>没有查询到相关版本！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>没有查询到相关版本！</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="4" align="right"><lk:pages
				url="/itms/resource/deviceVersionInfo!queryList.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
</body>
</html>