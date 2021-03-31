<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务配置策略视图</title>
<%
	/**
		 * 业务配置策略视图
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2008-12-18
		 * @category
		 */
%>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="../../Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="../../Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
	
	//导出excel查询
	function queryDataForExcel(username,city_id,productSpecId,startTime,
								endTime,oper_type_id,bind_state){
								
		var url = '<s:url value="/gwms/service/sheetStateView!getExcelReport.action"/>';
		url = url + "?username="+username+"&city_id="+city_id
				  +"&productSpecId="+productSpecId
				  +"&startTime="+startTime
				  +"&endTime="+endTime
				  +"&oper_type_id="+oper_type_id
				  +"&bind_state="+bind_state;
		alert(url);
		document.frm2.action = url;
		document.frm2.method = "post";
		document.frm2.submit();
	}
</script>

</head>

<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#999999">
					
					<tr bgcolor="#FFFFFF">
						<td colspan="9" align="left">
							<a href="javascript:queryDataForExcel('<s:property value="username"/>',
																  '<s:property value="city_id"/>',
																  '<s:property value="productSpecId"/>',
																  '<s:property value="startTime"/>',
																  '<s:property value="endTime"/>',
																  '<s:property value="oper_type_id"/>',
																  '<s:property value="bind_state"/>');">
								<img src="../../images/excel.gif"  border="0" width="16" height="16"></img>
							</a>
						</td>
					</tr>
					<tr>
						<th>工单ID</th>
						<th>来单时间</th>
						<th>属地</th>
						<th>客户名称</th>
						<th>用户账号</th>
						<th>操作类型</th>
						<th>受理状态</th>
						<th>绑定状态</th>
						<!-- <th>绑定时间</th> -->
						<th>配置执行状态</th>
					</tr>
					<s:iterator value="dataSheet">
						<tr bgcolor="#FFFFFF">
							<td class=column1><s:property value="bss_sheet_id"/></td>
							<td class=column1><s:property value="receive_date"/></td>
							<td class=column1><s:property value="city_name"/></td>
							<td class=column1><s:property value="customer_name"/></td>
							<td class=column1><s:property value="username"/></td>
							<td class=column1><s:property value="oper_type_name"/></td>
							<td class=column1><s:property value="result"/></td>
							<td class=column1><s:property value="bind_state"/></td>
							<!--<td class=column1><s:property value="bind_time"/></td> -->
							<td class=column1><s:property value="result_spec_state"/></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="8" align="right">
				<lk:pages url="/gwms/service/sheetStateView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
	
<form name="frm2"></form>
</body>
</html>