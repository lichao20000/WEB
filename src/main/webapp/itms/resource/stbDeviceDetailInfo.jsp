<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>山东联通RMS平台机顶盒设备统计详细列表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
			<th>设备厂商</th>
			<th>型号</th>
			<th>软件版本</th>
			<th>属地</th>
			<th>设备序列号</th>
		</tr>
		</thead>
		<tbody>
			<s:if test="stbDevList!=null">
				<s:if test="stbDevList.size()>0">
					<s:iterator value="stbDevList">
						<tr>
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="cityName" /></td>
							<td><s:property value="deviceSn" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="5">系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan="5">系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10" align="right"><lk:pages
						url="/itms/resource/stbDeviceCount!getDetailInfo.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>