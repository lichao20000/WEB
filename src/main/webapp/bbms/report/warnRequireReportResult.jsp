<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>企业网关需求挖掘</title>
<%
	/**
	 * 企业网关需求挖掘查询结果页面
	 * 
	 * @author chenjie
	 * @since 2012-12-05
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
	<caption>企业网关需求挖掘查询</caption>
	<thead>
		<tr>
			<th>用户名</th>
			<th>服务区</th>
			<th>行业类型</th>
			<th>业务类型</th>
			<th>设备类型</th>
			<th>接入类型</th>
			<th>流量大小</th>
			<th>流量生成时间</th>
			<th>在线时长</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="warnRequireReportList!=null">
			<s:if test="warnRequireReportList.size()>0">
				<s:iterator value="warnRequireReportList">
					<tr>
						<td><a
							href="javascript:GoContent('<s:property value="user_id" />',2);">
						<s:property value="username" /> </a></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="industry" /></td>
						<td><s:property value="serv_type" /></td>
						<td><s:property value="device_type" /></td>
						<td><s:property value="adsl_hl_str" /></td>
						<td><s:property value="flow" /></td>
						<td><s:property value="flow_date" /></td>
						<td><s:property value="onlinedate" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>系统没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>系统没有查询到相关信息！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

			<td colspan="9" align="right">
				&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
				url="/bbms/report/warnReportACT!queryWarnRequireReport.action"
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