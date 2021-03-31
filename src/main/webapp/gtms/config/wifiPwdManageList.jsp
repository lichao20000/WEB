<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>wifi密码查询修改管理</title>
<%
	/**
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
	
	
	function getwifiInfoUpdate(wifiname,deviceid,wifipath) {
		if(!confirm("确认进行更新操作?"))
			return;
		var url = "<s:url value='/gtms/config/wifiPwdManageAction!getwifiInfoUpdate.action'/>";
		$.post(url, {
			deviceid : deviceid,
			wifiname : wifiname,
			wifipath : wifipath
		}, function(mesg) {
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(mesg);
		});
	}
</script>

</head>

<body>

<table border=0 cellspacing=0 cellpadding=0 width="98%" align=center class="listtable">
	<caption>
		采集结果:<s:property value="resmessage"/>
	</caption>
	<thead>
		<tr>
			<th>WIFI帐号</th>
			<th>WIFI密码</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="servList">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="ssidname"/></td>
				<td class=column1><s:property value="PreSharedKeyvalue"/></td>
				<td class=column1>
				<a href="javascript:getwifiInfoUpdate('<s:property value="ssidname"/>','<s:property value="deviceId"/>','<s:property value="PreSharedKeyPath"/>')">更新密码</a>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="3">
			</td>
		</tr>
	</tfoot>
</body>
</html>