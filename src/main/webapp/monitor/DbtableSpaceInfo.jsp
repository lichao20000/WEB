<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>监控平台</title>
<link href="<s:url value='/css/iconfont/iconfont.css'/>" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="<s:url value='/css/liulu.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/listview.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_ico.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/user-defined.css'/>" type="text/css">
<Script>
</Script>
</head>
<body>
<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr><td height=20>&nbsp;&nbsp;</td></tr>
	<tr>
  		<td>
			<table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						数据库表空间监控平台
					</td>
					<td>
						<img src="<s:url value='/images/attention_2.gif'/>" width="15" height="12">以下显示的是数据库表空间信息
					</td>
				</tr>
			</table>
		</td>
	<tr>
	<td>
		<table border=0 cellspacing=1 cellpadding=2 bgcolor="#999999" width="100%" align="center">
			<thead>
				<tr>
					<th>表空间名称</th>
					<th>表空间总量</th>
					<th>已使用空间</th>
					<th>未使用空间</th>
					<th>使用率(%)</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="tableSpaceInfoList!=null && tableSpaceInfoList.size()>0">
					<s:iterator value="tableSpaceInfoList" var="monitorMap">
						<tr>
							<td class="column" align="center">
								<s:property value="tablespace_name" />
							</td>
							<td class="column" align="center">
								<s:property value="summb" />
							</td>
							<td class="column" align="center">
								<s:property value="usedmb" />
							</td>
							<td class="column" align="center">
								<s:property value="freemb" />
							</td>
							<td class="column" align="center">
								<s:property value="usedpase" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
						<tr>
							<td colspan="6">无数据</td>
						</tr>
				</s:else>
			</tbody>
		</table>
	</td>
	</tr>
</table>
</body>
<%@ include file="../foot.jsp"%>
</html>