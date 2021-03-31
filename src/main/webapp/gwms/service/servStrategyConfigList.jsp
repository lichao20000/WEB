<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
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
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

	function resetData(deviceId,taskId,tempId){
		if(confirm('确实要重置吗?')){
			var url = "<s:url value="/gwms/service/servStrategyView!resetData.action"/>";
			$.post(url,{
				deviceId:deviceId,
				taskId:taskId,
				id:tempId
		    },function(mesg){
		    	alert(mesg);
		    	document.location.reload();
		    });
		}else{
			//return false;
		}
	}

	function cancelData(deviceId,taskId,tempId){
		if(confirm('确实要取消吗?')){
			var url = "<s:url value="/gwms/service/servStrategyView!cancelData.action"/>";
			$.post(url,{
				deviceId:deviceId,
				taskId:taskId,
				id:tempId
		    },function(mesg){
		    	alert(mesg);
		    	document.location.reload();
		    });
		}else{
			//return false;
		}
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			统计结果
		</caption>
		<thead>
			<tr>
				<th rowspan="2" width="5%">操作</th>
				<th colspan="2">设备信息</th>
				<th colspan="5">配置信息</th>
			</tr>
			<tr>
				<th >设备序列号</th>
				<th >用户账号</th>
				<th>业务名称</th>
				<th>策略状态</th>
				<th>定制时间</th>
				<th>执行时间</th>
				<th>策略结果</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="servStrategyList" var="servStrategyInfo" status="status">
				<tr>
					<td align="center" rowspan="<s:property value="listSize"/>">
					<a href="javascript:resetData('<s:property value="deviceId"/>',
												  '<s:property value="taskId"/>',
												  '<s:property value="id"/>' )">重置</a>
					<br>
					<a href="javascript:cancelData('<s:property value="deviceId"/>',
												  '<s:property value="taskId"/>',
												  '<s:property value="id"/>')">取消</a></td>
					<td rowspan="<s:property value="listSize"/>"><s:property value="deviceSerialnumber"/></td>
					<td rowspan="<s:property value="listSize"/>"><s:property value="username"/></td>
					<s:iterator value="list" var="infoList">
							<td ><s:property value="service_name"/></td>
							<td ><s:if test="status!=null">
							<s:property value="status"/>
							</s:if></td>
							<td ><s:property value="time"/></td>
							<td ><s:property value="start_time"/></td>
							<td ><s:property value="result_id"/></td>
						</tr>
					</s:iterator>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8" align="right">
					<lk:pages url="/gwms/service/servStrategyView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>
