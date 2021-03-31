<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function detailDevice(mac,login_time)
{
	var table=$("input[name=table]").val();
	var url = "<s:url value='/gtms/stb/resource/stbCallHistoryACT!detailDevice.action'/>"
					+ "?mac=" + mac
					+ "&login_time="+login_time
					+ "&table="+table;
	window.open(url, "","left=20,top=20,width=700,height=650,resizable=yes,scrollbars=yes");
}
</script>

<div>
<input type="hidden" name="table" readonly value="<s:property value='table'/>" />
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="10%">请求时间</th>
			<th width="8%">请求IP</th>
			<th width="8%">请求类型</th>
			<th width="9%">机顶盒MAC</th>
			<th width="8%">认证APK版本</th>
			<th width="8%">服务端主机</th>
			<th width="6%">服务端端口</th>
			<th width="6%">服务端版本</th>
			<th width="11%">请求业务账号</th>
			<th width="11%">返回业务账号</th>
			<th width="6%">返回错误码</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
		<s:iterator value="data">
			<tr align="center">
				<td><s:property value="request_time" /></td>
				<td><s:property value="login_ip" /></td>
				<td><s:property value="event_id" /></td>
				<td><s:property value="mac" /></td>
				<td><s:property value="apk_version_name" /></td>
				<td><s:property value="server_host" /></td>
				<td><s:property value="server_port" /></td>
				<td><s:property value="server_version" /></td>
				<td><s:property value="request_username" /></td>
				<td><s:property value="result_username" /></td>
				<td><s:property value="result_code" /></td>
				<td>
					<a href="javascript:detailDevice('<s:property value="mac"/>',
														'<s:property value="login_time"/>')">
						详细信息
					</a>
				</td>
			</tr>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12 align=left>没有查询到上报数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="12" align="right" height="15">
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbCallHistoryACT!query.action" showType=""
					isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</div>
