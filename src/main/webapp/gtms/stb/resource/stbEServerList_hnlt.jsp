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
$(function() {
	parent.dyniframesize();
});

function detailEserver(sheetId)
{
	var strpage = "<s:url value='/gtms/stb/resource/stbEServerQuery!queryEServerInfo.action'/>?"
					+"sheetId=" + sheetId;
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function toExcel()
{
	parent.toExcel();
}
</script>

<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="10%">工单时间</th>
			<th width="10%">工单来源</th>
			<th width="8%">业务账号</th>
			<th width="6%">机顶盒MAC</th>
			<th width="8%">工单类型</th>
			<th width="9%">网格</th>
			<th width="9%">操作员</th>
			<th width="8%">返回码</th>
			<th width="8%">返回信息</th>
			<th width="9%">操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="receive_date" /></td>
					<td><s:property value="from_id" /></td>
					<td><s:property value="username" /></td>
					<td><s:property value="mac" /></td>
					<td><s:property value="server_type" /></td>
					<td><s:property value="grid" /></td>
					<td><s:property value="opertor" /></td>
					<td><s:property value="result" /></td>
					<td><s:property value="returnt_context" /></td>
					<td>
						<a href="javascript:detailEserver('<s:property value="bss_sheet_id"/>')">详细信息</a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10 align=left>没有查询到相关数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10" align="right" height="15">
				<img src="<s:url value="/images/excel.gif"/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()" align=left />
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbEServerQuery!queryEServerList.action" 
					showType=""	isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
