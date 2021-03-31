<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>光功率采集结果查询</title>
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
	
	function queryResult(task_id){
    	page = "<s:url value='/stb/resource/picStrategyQueryResult!result.action'/>?task_id=" + task_id;
	    window.open(page,"","left=200,top=100,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function cusomerDetail(serv_account, deviceId) {
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!querySTBCustomerDetail.action'/>?serv_account="
				+ serv_account + "&deviceId=" + deviceId + "&status=" + "";
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>属地</th>
				<th>账号</th>
				<th>MAC地址</th>
				<th>设备序列号</th>
				<th>操作类型</th>
				<th>接收时间</th>
				<th>绑定时间</th>
				<th>接口返回结果</th>
				<th>绑定结果</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr bgcolor="#FFFFFF" align="center">
							<td class="column1"><s:property value="city_name"/></td>
							<td class="column1">
								
									<a href="#" onclick="cusomerDetail('<s:property value="serv_account" />','<s:property value="device_id"/>');" >
										<s:property value="user_info" />
									</a>
								
								
							</td>
							<td class="column1"><s:property value="device_sn"/></td>
							<td class="column1">
								
									<a href="#" onClick="window.open('/itms/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action?deviceId='+'<s:property value="device_id" />'+'&gw_type=4','','resizable=yes,menubar=no,status=no,scrollbars=yes,location=no,top=60,left=650,toolbar=no,width=1200,height=580');" >
										<s:property value="device_serialnumber" />
									</a>
								
							</td>
							<td class="column1"><s:property value="operate_type"/></td>
							<td class="column1"><s:property value="update_time"/></td>
							<td class="column1"><s:property value="bind_time"/></td>
							<td class="column1"><s:property value="inft_result" /></td>
							<td class="column1"><s:property value="bind_result" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=10>请零配绑定设备!</td>
					</tr>
				</s:else>
			</s:if>
			<s:if test='tipMsg=="1"'>
				<tr>
					<td colspan=10>该账号不存在!</td>
				</tr>
			</s:if>
			<s:elseif test='tipMsg=="2"'>
				<tr>
					<td colspan=10>请零配绑定设备!</td>
				</tr>
			</s:elseif>
			<s:elseif test='tipMsg=="3"'>
				<tr>
					<td colspan=10>设备没有上报!</td>
				</tr>
			</s:elseif>
			<%-- <s:else>
				<tr>
					<td colspan=10>没有数据!</td>
				</tr>
			</s:else> --%>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages url="itms/service/zeroconfiguration_XJ!query.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>