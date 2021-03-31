<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>客户资料查询结果界面</title>
<%
	/**
		 * 客户资料查询结果界面
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-04
		 * @category
		 */
%>
<link href="<s:url value='/css/css_green.css'/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>
<script type="text/javascript">
$(function(){
	
	var curPage = "<s:property value='curPage_splitPage'/>";
	var num = "<s:property value='num_splitPage'/>";
	var maxPage = "<s:property value='maxPage_splitPage'/>";
	var paramList = "<s:property value='paramList_splitPage'/>";
	
	//初始化翻页按钮
	$.initPage(
		"<s:url value='/bbms/CustomerInfo!goPage.action'/>", 
		"#toolbar", 
		curPage, 
		num, 
		maxPage, 
		paramList
	);
	
	var msg = '<s:property value="msg"/>';
	if (msg != ''){
		alert(msg);
	}
})

//编辑
function editCustomer(customer_id){
	parent.location.href = "<s:url value='/bbms/CustomerInfo!editFrom.action'/>?customer_id="+customer_id;
}

//删除
function delCustomer(customer_id){
	if (confirm("是否要删除该客户资料？")){
		window.location.href = "<s:url value='/bbms/CustomerInfo!delCustomer.action'/>?customer_id="+customer_id;
	}
}

//详细信息
function showDetail(customer_id){
	window.open("<s:url value='/bbms/CustomerInfo!detailInfo.action'/>?customer_id="+customer_id,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}

//导出文件
function exportExcel(){
	document.frm.action = "<s:url value='/bbms/CustomerInfo!exportExcel.action'/>";
	document.frm.submit();
}
</script>
</head>

<body>
<form name="frm" action="" method="post">
<input type="hidden" name="customer_name" value="<s:property value="customer_name"/>">
<input type="hidden" name="customer_id" value="<s:property value="customer_id"/>">
<input type="hidden" name="customer_account" value="<s:property value="customer_account"/>">
</form>
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td>
			<table width="100%" height="10" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">
						<IMG SRC="<s:url value='/images/excel.gif'/>" WIDTH="16" HEIGHT="16" BORDER="0" onclick="exportExcel()" ALT="导出到EXCEL" style="cursor:hand">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#999999">
				<tr>
					<th>客户ID</th>
					<!-- <th>客户账户</th>-->
					<th>客户名称</th>
					<th>联系人</th>
					<th>联系电话</th>
					<th>属地</th>
					<th>操作</th>
				</tr>
				<s:iterator value="customerList">
					<tr bgcolor="#FFFFFF">
						<td class=column1><s:property value="customer_id"/></td>
						<!--  <td class=column1><s:property value="customer_account"/></td>-->
						<td class=column1><s:property value="customer_name"/></td>
						<td class=column1><s:property value="linkman"/></td>
						<td class=column1><s:property value="linkphone"/></td>
						<td class=column1><s:property value="city_id"/></td>
						<td align="center" class=column1>
							<IMG SRC="<s:url value='/images/edit.gif'/>" BORDER="0" onclick="editCustomer('<s:property value='customer_id'/>')" ALT="编辑" style="cursor:hand">
							<IMG SRC="<s:url value='/images/del.gif'/>" BORDER="0" onclick="delCustomer('<s:property value='customer_id'/>')" ALT="删除" style="cursor:hand">
							<IMG SRC="<s:url value='/images/view.gif'/>" BORDER="0" onclick="showDetail('<s:property value='customer_id'/>')" ALT="查看" style="cursor:hand">
						</td>
					</tr>
				</s:iterator>
				
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#999999">
				<tr><td class="green_foot" align="right"><div id="toolbar"></div></td></tr>
			</table>
		</td>
	</tr>
</table>
<BR>
<%@ include file="../foot.jsp"%>
</body>
</html>
