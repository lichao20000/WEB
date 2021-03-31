<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>企业网关维挽预警</title>
<%
	/**
	 * 企业网关维挽预警查询结果页面
	 * 
	 * @author chenjie
	 * @since 2012-12-06
	 */
%>
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


	function ToExcel(){
		parent.ToExcel();
	}

	function GoContent(user_id,gw_type){
	 	 //gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		 if(parseInt(gw_type)==2){
		 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
		 window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>企业网关维挽预警查询</caption>
	<thead>
		<tr>
			<th>用户名</th>
			<th>服务区</th>
			<th>客户联系电话</th>
			<th>装机地址</th>
			<th>订购产品类型</th>
			<th>装机日期</th>
			<th>设备厂商</th>
			<th>设备类型</th>
			<th>接入类型</th>
			<th>行业类型</th>
			<th>预警原因</th>
			<th>预警时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="warnReportList!=null">
			<s:if test="warnReportList.size()>0">
				<s:iterator value="warnReportList">
					<tr>
						<td><a
							href="javascript:GoContent('<s:property value="user_id" />',2);">
						<s:property value="username" /> </a></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="linkphone" /></td>
						<td><s:property value="customer_address" /></td>
						<td><s:property value="product_type" /></td>
						<td><s:property value="opendate" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_type" /></td>
						<td><s:property value="adsl_hl_str" /></td>
						<td><s:property value="industry" /></td>
						<td><s:property value="warning_reason" /></td>
						<td><s:property value="warning_date" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12>系统没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>系统没有查询到相关信息！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

			<td colspan="12" align="right">
				&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
				url="/bbms/report/warnReportACT!queryWarnReport.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<!-- 
		<tr>
			<td colspan="8" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
		 -->
	</tfoot>
</table>
</body>
</html>