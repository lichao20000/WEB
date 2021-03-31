<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>业务配置历史信息</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript">
function configDetailInfo(strategyId,deviceSN,servTypeId){
	var gw_type = document.getElementsByName("gw_type")[0].value;
	var page = "<s:url value='/itms/service/bssSheetServ!getConfigLogDetail.action'/>?gw_type=" + gw_type+ "&strategyId="+strategyId+"&deviceSN="+deviceSN+"&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>
	</head>
	<body>
		<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
		<table class="listtable">
			<caption>
				设备
				<s:property value="deviceSN" />
				配置信息
			</caption>
			<thead>
				<tr>
					<th>
						业务名称
					</th>
					<th>
						执行时间
					</th>
					<th>
						下发完成时间
					</th>
					<th>
						工单执行状态
					</th>
					<th>
						结果描述及建议
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="configLogInfoList!=null">
					<s:if test="configLogInfoList.size()>0">
						<s:iterator value="configLogInfoList">
							<tr>
								<td>
									<s:property value="serviceName" />
								</td>
								<td>
									<s:property value="start_time" />
								</td>
								<td>
									<s:property value="end_time" />
								</td>
								<td>
									<s:property value="status" />
								</td>
								<td>
									<s:property value="result_id" />
								</td>
								<%  if(LipossGlobals.isGSDX() || LipossGlobals.isSXLT()) {%>
								<td>
									<a href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />','<s:property value="servTypeId" />')">详细信息</a>
								</td>
								<%} else {%>
								<td>
									<a href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="deviceSN" />')">详细信息</a>
								</td>
								<%}%>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=6>
								系统没有配置信息!
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>
							系统没有此业务信息!
						</td>
					</tr>
				</s:else>
			</tbody>
		</table>
	</body>