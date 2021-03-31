<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音注册错误原因统计</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

</head>
<body>
<table class="listtable" id="listTable">
	<caption>语音注册错误原因统计 </caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>IAD模块错误</th>
			<th>访问路由不通</th>
			<th>访问服务器无响应</th>
			<th>帐号、密码错误</th>
			<th>未知错误</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="voicefailMap!=null">
			<s:if test="voicefailMap.size()>0">
				<s:iterator value="voicefailMap">
						<tr>
							<td>
							
								<s:property value="city_name" />
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','1')">
								<s:property value="oneNum" />
								</a>
							</td>
							<td>
							
								<a href="javascript:openCity('<s:property value="city_id"/>','2')">
								<s:property value="twoNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','3')">
								<s:property value="threeNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','4')">
								<s:property value="fourNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','5')">
								<s:property value="fiveNum" />
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
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>

		<tr>
			<td colspan="6" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>