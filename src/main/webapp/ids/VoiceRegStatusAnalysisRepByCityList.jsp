<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音端口使用情况</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>语音端口使用情况 </caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>语音端口1总数</th>
			<th>语音端口1未启用总数</th>
			<th>语音端口2总数</th>
			<th>语音端口2未启用总数</th>
			<th>语音端口1和语音端口2同时未启用总数</th>
		</tr>
	</thead>
	<tbody>
			<s:if test="voicePortCityList.size()>0">
				<s:iterator value="voicePortCityList">
						<tr>
							<td>
								<s:property value="cityName" />
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','1');">
									<s:property value="line1_count" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','2');">
								<s:property value="line1_disabled" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','3');">
								<s:property value="line2_count" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','4');">
								<s:property value="line2_disabled" />
								</a>
							</td>
							<td>
								<a href="javascript:downFile('<s:property value="city_id"/>','5');">
								<s:property value="line1_2_disabled" />
								</a>
							</td>
						</tr>
				</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan=6>系统中没有查询出需要的信息!</td>
				</tr>
		</s:else>
	</tbody>
	
</table>
</body>
</html>