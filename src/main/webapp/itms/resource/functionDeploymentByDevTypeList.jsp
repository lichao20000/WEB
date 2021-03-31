<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>新增功能部署报表设备型号情况</title>

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
	<caption>新增功能部署报表设备型号情况 </caption>
	<thead>
		<tr>
			<th width="50%">设备型号</th>
			<th width="50%">已开通终端数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployList!=null">
			<s:if test="deployList.size()>0">
				<s:iterator value="deployList">
						<tr>
							<td width="50%" >
							
								<s:property value="modelType" />
							</td>
							<td width="50%">
								<a href="javascript:openModel('<s:property value="model_id"/>');">
									<s:property value="deploy_total" />
								</a>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=2>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=2>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="2" align="right"><IMG SRC="/itms/images/excel.gif" 
				BORDER=0  ALT="导出列表" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>