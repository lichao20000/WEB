<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<%
	/**
		 *  
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2011-7-24
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
	
	function openCust(param){
		window.open("<s:url value='/bbms/CustomerInfo!detailInfo.action'/>?customer_id="+param,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}
	
	function openDevice(deviceId){
		if(deviceId==null){
			alert("设备不存在!");
			return false;
		}
		var strpage="<s:url value='/gwms/blocTest/DeviceShow.jsp'/>?gw_type=2&device_id=" + deviceId;
		window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
	
	function openUser(userId){
		if(userId==null){
			alert("设备没有绑定用户!");
			return false;
		}
		var strpage="<s:url value='/gwms/blocTest/EGWUserRelatedInfo.jsp'/>?user_id=" + userId;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	
	function openBssSheet(param){
		var strpage="EGWUserRelatedInfo.jsp?user_id=" + param;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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
			    <th>工单编号</th>
				<th>客户ID</th>
				<th>客户名称</th>
				<th>联系电话</th>
				<th>区局</th>
				<th>设备序列号</th>
				<th>宽带帐号</th>
				<th>IP地址</th>
				<th>工单状态</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rsList">
				<tr bgcolor="#FFFFFF">
					<td class=column1><s:property value="bssSheetId"/></td>
					<td class=column1><s:property value="customerId"/></td>
					<td class=column1>
						<a href="javascript:openCust('<s:property value="customerId"/>')">
							<s:property value="customerName"/>
						</a>
					</td>
					<td class=column1><s:property value="linkphone"/></td>
					<td class=column1><s:property value="cityName"/></td>
					<td class=column1>
						<a href="javascript:openDevice('<s:property value="deviceId"/>')">
							<s:property value="deviceSn"/>
						</a>
					</td>
					<td class=column1>
						<a href="javascript:openUser('<s:property value="userId"/>')">
							<s:property value="username"/>
						</a>
					
					</td>
					<td class=column1><s:property value="loopbackIp"/></td>
					<td class=column1><s:property value="result"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tr>
			<td colspan="8" align="right">
				<lk:pages url="/gwms/blocTest/baseInfoView!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>