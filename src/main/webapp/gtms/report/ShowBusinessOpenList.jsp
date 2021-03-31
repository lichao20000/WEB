<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>用户业务列表</title>
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
		<script language="JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/Js/edittable.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
		<script type="text/javascript">
			function ListToExcel(openStatus,cityId,start,end,parentCityId,selectTypeId) {
				var page="<s:url value='/gtms/report/businessOpen!getUserExcel.action'/>?"
					+ "openStatus=" + openStatus
					+ "&cityId=" + cityId 
					+ "&start=" +start
					+ "&end=" +end
					+ "&parentCityId=" +parentCityId
					+ "&selectTypeId=" +selectTypeId;
				document.all("childFrm").src=page;
			}
		</script>
	</head>
	
	<body>
		<table class="listtable">
			<caption>用户业务列表</caption>
			<thead>
				<tr>
					<th>逻辑SN</th>
					<th>属地</th>
					<th>业务</th>
					<th>业务开通状态</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="businessOpenUserList.size()>0">
					<s:iterator value="businessOpenUserList">
						<tr>
							<td align="center"><s:property value="username" /></td>
							<td align="center"><s:property value="cityName" /></td>
							<td align="center"><s:property value="servTypeName" /></td>
							<td align="center">
								<s:if test='openStatus=="1"'>已开通</s:if>
								<s:elseif test='openStatus=="0"'>未开通</s:elseif>
								<s:elseif test='openStatus=="-1"'>开通失败</s:elseif>
								<s:else>未知</s:else>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>系统没有相关的用户业务信息!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<span style="float: right;"> 
							<lk:pages url="/gtms/report/businessOpen!getUserList.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
							style='cursor: hand'
							onclick="ListToExcel('<s:property value="openStatus"/>','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>')">
						
					</td>
				</tr>
		
				<TR>
					<TD align="center" colspan="9">
						<button onclick="javascript:window.close();">&nbsp;关 闭&nbsp;</button>
					</TD>
				</TR>
			</tfoot>
		
			<tr STYLE="display: none">
				<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</body>
	<%@ include file="/foot.jsp"%>
</html>