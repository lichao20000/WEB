<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPoE拨号错误详细列表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
 <script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});
</script> 
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
			<th>LOID</th>
			<th>设备序列号</th>
			<th>连接状态</th>
			<th>上报时间</th>
			<th>入库时间</th>
			<th colspan="3">最后一次尝试拨号失败原因</th>
		</tr>
		</thead>
		<tbody>
			<s:if test="rlist!=null">
				<s:if test="rlist.size()>0">
					<s:iterator value="rlist">
						<tr>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="status" /></td>
							<td><s:property value="upload_time" /></td>
							<td><s:property value="add_time" /></td>
							<td colspan="3"><s:property value="reason" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="8">系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan="8">系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8"><span style="float:right"><lk:pages
						url="/ids/pppoeCount!queryByPPPoEList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
				</td>
			</tr>
			<tr>
			<td align="center" colspan="8">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</td>
		</tr>
		<tr STYLE="display: none">
				<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
		</tr>
			</tfoot>
	</table>
</body>
</html>