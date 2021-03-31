<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPOE失败原因统计</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>PPPOE失败原因统计 </caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>ERROR_ISP_TIME_OUT</th>
			<th>ERROR_AUTHENTICATION_FAILURE</th>
			<th>ERROR_ISP_DISCONNECT</th>
		</tr>
	</thead>
	<tbody>
			<s:if test="ppoeFailList.size()>0">
				<s:iterator value="ppoeFailList">
						<tr>
							<td>
								<s:property value="cityName" />
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','1');">
									<s:property value="time_out" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','2');">
								<s:property value="auth_failure" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','3');">
								<s:property value="disconnect" />
								</a>
							</td>
						</tr>
				</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan=4>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
	</tbody>
	
</table>
</body>
</html>