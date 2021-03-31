<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>部门查询列表</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

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
				<th width="20%">名称</th>
				<th width="25%">描述</th>
				<th width="15%">操作时间</th>
				<th width="20%">操作人</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="departList!=null">
				<s:if test="departList.size()>0">
					<s:iterator value="departList">
						<tr>
							<td><s:property value="depart_name" /></td>
							<td><s:property value="depart_desc" /></td>
							<td style="text-align:center;"><s:property value="depart_time" /></td>
							<td style="text-align:center;"><s:property value="acc_loginname" /></td>
							<td style="text-align:center;">
								<a href="javascript:parent.delDeaprt('<s:property value="depart_id" />')">删除</a>|
								<a href="javascript:parent.editDeaprt('<s:property value="depart_id" />',
																	'<s:property value="depart_name" />',
																	'<s:property value="depart_desc" />')">编辑</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>没有查询到相关设备！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>没有查询到相关设备！</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/departManage!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
</html>