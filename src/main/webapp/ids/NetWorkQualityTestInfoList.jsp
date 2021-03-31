<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>网络质量检测详细页面</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	
<script type="text/javascript">

		function ListToExcel(device_sn,startOpenDate,endOpenDate) {
			
			var page="<s:url value='/ids/NetWorkQualityTest!voiceOrderQueryExcel.action' />?"+"device_sn="+device_sn
					+ "&startOpenDate=" + startOpenDate 
					+ "&endOpenDate=" +endOpenDate;
			document.all("childFrm").src=page;
		}
	
</script>	
	
</head>
<body>
<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>LOID</th>
			<th>装机地址</th>
			<th>时延数</th>
			<th>丢包率</th>
			<th>检测时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="netWorkQualityList!=null">
			<s:if test="netWorkQualityList.size()>0">
				<s:iterator value="netWorkQualityList">
						<tr>
							<td>
								<s:property value="device_serialnumber" />
							</td>
							<td>
								<s:property value="loid" />
							</td>
							<td>
								<s:property value="address" />
							</td>
							<td>
								<s:property value="avg_delay" />
							</td>
							<td>
								<s:property value="loss_pp" />
							</td>
							<td>
								<s:property value="update_time"/>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>没有质量检测分析信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan=6>
				<span style="float: right;"> <lk:pages
						url="/ids/NetWorkQualityTest!netWorkQualityTestInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'    
						onclick="javaScript:ListToExcel('<s:property value="device_sn"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>');" > 
			</td>
		</tr>
	</tfoot>
	
	<tr STYLE="display: none">
		<td colspan="6">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
</body>
</html>