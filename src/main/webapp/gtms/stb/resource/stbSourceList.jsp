<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒溯源列表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function stbDeviceDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="
				+ deviceId + "&gw_type=4";
		window.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	
	function deviceDetail(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<caption>机顶盒溯源记录</caption>
	<thead>
		<tr>
			<th align="center">LOID</th>
			<th align="center">设备序列号</th>
			<th align="center">MAC</th>
			
			<th align="center">机顶盒业务账号</th>
			<th align="center">机顶盒序列号</th>
			<th align="center">机顶盒MAC</th>
			
			<th align="center">上报时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devSourceList!=null">
			<s:if test="devSourceList.size()>0">
				<s:iterator value="devSourceList">
						<tr>
							<td class=column1 align="center"><s:property value="loid"/></td>
							<td class=column1 align="center">
							  <a href="javascript:deviceDetail('<s:property value="devId" />')"><s:property value="devSn"/></a>
							</td>
							<td class=column1 align="center"><s:property value="mac"/></td>
							
							<td class=column1 align="center"><s:property value="servAccount"/></td>
							<td class=column1 align="center">
							<a href="javascript:stbDeviceDetail('<s:property value="stbDevId" />')">
							<s:property value="stbDevSn"/>
							</a>
							</td>
							<td class=column1 align="center"><s:property value="stbmac"/></td>
							
							<td class=column1 align="center"><s:property value="updatetime"/></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>没有相关查询信息信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>没有相关查询信息</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="7" align="right"><lk:pages
						url="/gtms/stb/resource/stbSource!qryStbSource.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		<%-- 	<tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">(尽量在夜间22:00之后操作!)</td>
			</tr> --%>
		</tfoot>
	</table>
</body>
</html>