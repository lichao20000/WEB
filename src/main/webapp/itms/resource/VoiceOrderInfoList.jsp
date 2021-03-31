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
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
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
		<s:if test="voiceMap!=null">
			<s:if test="voiceMap.size()>0">
				<s:iterator value="voiceMap">
						<tr>
							<td>
							
								<s:property value="city_name" />
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','1');">
									<s:property value="lineOneNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','2');">
								<s:property value="lineOneNoNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','3');">
								<s:property value="lineTwoNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','4');">
								<s:property value="lineTwoNoNum" />
								</a>
							</td>
							<td>
								<a href="javascript:openCity('<s:property value="city_id"/>','5');">
								<s:property value="lineOneTwoNoNum" />
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
				BORDER=0  ALT="导出列表" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>