<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>关闭宽带DHCP节点的策略查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<table class="listtable">
			<thead>
			<tr>
				<th>
					业务名称
				</th>
				<th>
					定制时间
				</th>
				<th>
					执行时间
				</th>
				<th>
					策略状态
				</th>
				<th>
					策略结果
				</th>
				<th>
					结果描述
				</th>
			</tr>
		</thead>
		<tbody style="background-color:#FFFFFF;height: 25 ">
			<s:if test="list.size()>0">
				<s:iterator value="list">
					<tr style="vertical-align:middle; text-align:center;">
						<td align="center">
							<s:property value="service_id"  />
						</td>
						<td align="center">
							<s:property value="time" />
						</td>
						<td align="center">
							<s:property value="start_time" /> 
						</td>
						<td align="center">
							<s:property value="status" /> 
						</td>
						<td align="center">
							<s:property value="result_id" /> 
						</td>
						<td align="center">
							<s:property value="result_desc" /> 
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6 >
						<font color="red">没有满足条件的数据！</font>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tr>
			<td colspan="6" align="right">
				<lk:pages url="/gtms/config/netByDHCPStop!queryNetByDHCPList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>