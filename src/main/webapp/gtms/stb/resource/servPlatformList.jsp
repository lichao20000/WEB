
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%String absPath=request.getContextPath(); %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import=" flex.messaging.util.URLDecoder; "%>
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
	function update(platform_id,platform_name,remark) {
		var url = "<s:url value='/gtms/stb/resource/stbservplatform!queryplatformname.action'/>?platformid="+platform_id;
		window.open(url, "","left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	function delete1(platform_id,platform_name)
	{
		var sure = window.confirm("确定删除吗？");
		if(!sure)
		{
			return;
		}
		 var url="<s:url value='/gtms/stb/resource/stbservplatform!deleteservPlatform.action'/>";
		 $.post(url,{
			 platformid:platform_id
			},function(ajax){
				if(ajax==1)
				{
					alert("删除成功!");
					window.parent.Query();
				}else
				{
					alert("删除失败!");
				}
			});
	}
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>IPSEC业务信息</caption>
		<thead>
			<tr>
				<th>业务平台类型ID</th>
				<th>业务平台类型名称</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
							<tr align="center">
								<td><s:property value="platform_id" /></td>
								<td><s:property value="platform_name" /></td>
								<td><s:property value="remark" /></td>
								<td><a href="javascript:update('<s:property value="platform_id" />','<s:property value="platform_name" />','<s:property value="remark" />')">编辑</a>|
								<a href="javascript:delete1('<s:property value="platform_id" />','<s:property value="platform_name" />')">删除</a>
								</td>
							</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>系统没有该用户的业务信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统没有此用户!</td>
				</tr>
			</s:else>
		</tbody>
	</table>
</body>
</html>