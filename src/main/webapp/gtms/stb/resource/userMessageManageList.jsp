
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="flex.messaging.util.URLDecoder;"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务平台类型查询结果查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function update(servaccount) {
		var url = "<s:url value='/gtms/stb/resource/userMessage!queryservaccount.action'/>?servaccount=" + servaccount;
		window.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	function look(servaccount) {
		var url = "<s:url value='/gtms/stb/resource/userMessage!queryLook.action'/>?servaccount=" + servaccount ;
		window
				.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	function delete1(servaccount) {
		var sure = window.confirm("确定删除吗？");
		if (!sure) {
			return;
		}
		var url = "<s:url value='gtms/stb/resource/userMessage!deleteuserMessage.action'/>";
		$.post(url, {
			servaccount:servaccount
		}, function(ajax) {
			if (ajax == "成功") {
				alert("删除成功!");
				window.parent.query();
			} else {
				alert(ajax);
			}
		});
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>业务账号</th>
				<th>接入账号</th>
				<th>MAC</th>
				<th>手机号码</th>
				<th>平台类型</th>
				<th>用户分组</th>
				<th>属地</th>
				<th>开户时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
						<tr align="center">
							<td><s:property value="servaccount" /></td>
							<td><s:property value="stbuser" /></td>
							<td><s:property value="MAC" /></td>
							<td><s:property value="iptvBindPhone" /></td>
							<td><s:property value="platformType" /></td>
							<td><s:property value="userGroupID" /></td>
							<td><s:property value="city_name" /></td>
							<%-- <td><s:property value="stbuptyle" /></td>
							<td><s:property value="stbaccessStyle" /></td> --%>
							<td><s:property value="dealDate" /></td>
							<td><a
								href="javascript:look('<s:property value="servaccount" />')">详细信息</a>|<a
								href="javascript:update('<s:property value="servaccount" />')">编辑</a>|
								<a
								href="javascript:delete1('<s:property value="servaccount" />')">删除</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>系统没有该用户的业务信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>系统没有此用户!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="right"><lk:pages
						url="/gtms/stb/resource/userMessage!query.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>