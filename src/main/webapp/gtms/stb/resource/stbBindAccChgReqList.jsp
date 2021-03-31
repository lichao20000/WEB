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
	var strpage = "<s:url value='/gtms/stb/resource/stbEServerQuery!queryDetail.action'/>?"
					+"sheetId=" + sheetId;
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}


function passEserver(mac,serv_account,sheetId)
{
    var url = "<s:url value='/gtms/stb/resource/stbEServerQuery!passEserver.action'/>";
    $.post(url, {
        mac : mac,
        servAccount:serv_account,
        sheetId : sheetId
    }, function(mesg) {
        alert(mesg);
        window.parent.query();
    });
}

function refuseEserver(sheetId)
{
	var url = "<s:url value='/gtms/stb/resource/stbEServerQuery!refuseEserver.action'/>";
        $.post(url, {
            sheetId : sheetId
        }, function(mesg) {
            alert(mesg)
            window.parent.query();
        });
}


</script>

<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="12%">工单时间</th>
			<th width="12%">工单来源</th>
			<th width="12%">业务账号</th>
			<th width="12%">机顶盒MAC</th>
			<th width="12%">网格</th>
			<th width="12%">操作员</th>
			<th width="12%">状态</th>
			<th width="16%">操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="add_time" /></td>
					<td><s:property value="from_id" /></td>
					<td><s:property value="serv_account" /></td>
					<td><s:property value="mac" /></td>
					<td><s:property value="grid" /></td>
					<td><s:property value="opertor" /></td>
					<td><s:property value="status_desc" /></td>
					<td>
						<a href="javascript:detailEserver('<s:property value="cmd_id"/>')">详细信息</a>
						<s:if test='groupOid=="2"'>
						    <s:if test='status=="0"'>
                                <a href="javascript:passEserver('<s:property value="mac"/>','<s:property value="serv_account"/>','<s:property value="cmd_id"/>')">通过</a>
                                <a href="javascript:refuseEserver('<s:property value="cmd_id"/>')">拒绝</a>
						    </s:if>
						</s:if>
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
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbEServerQuery!query.action"
					showType=""	isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
