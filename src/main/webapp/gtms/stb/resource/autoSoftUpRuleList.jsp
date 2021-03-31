<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<script language="JavaScript">
$(function(){
	parent.dyniframesize();
});	

// 删除
function deleteSoftUpRule(rule_id)
{
	if(confirm("是否确认删除此规则数据"))
	{
		var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!delete.action'/>";
		$.post(url, {
			rule_id : rule_id
			}, function(ajax) {
				alert(ajax);
				if("规则删除成功！"==ajax){
					parent.query();
				}
		});
	}
}

// 编辑
function edit(rule_id)
{
	var page = "<s:url value='/gtms/stb/resource/autoSoftUpRule!addjsp.action?rule_id='/>" + rule_id;
	window.open(page,"","left=20,top=20,width=1000,height=400,resizable=yes,scrollbars=yes");
}
</script>
</head>

<table class="listtable" width="100%" align="center">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="15%">厂商</th>
			<th width="15%">型号</th>
			<th width="15%">软件版本</th>
			<th width="15%">硬件版本</th>
			<th width="15%">用户实际网络类型</th>
			<th width="15%">目标版本</th>
			<s:if test="showType!=''">
				<th width="10%">操作</th>
			</s:if>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="ruleList">
			<tr align="center">
				<td><s:property value="vendor_add" /></td>
				<td><s:property value="device_model" /></td>
				<td><s:property value="softwareversion" /></td>
				<td><s:property value="hardwareversion" /></td>
				<td><s:property value="user_net_type" /></td>
				<td><s:property value="targetVersion" /></td>
				<s:if test="showType!=''">
					<td align="center">
						<label onclick="javascript:edit('<s:property value='rule_id'/>');">
							<img SRC="<s:url value="/images/edit.gif"/>" 
									border='0' ALT='编辑' style='cursor: hand'>
						</label>
						<s:if test="showType==1">
							<label onclick="javascript:deleteSoftUpRule('<s:property value='rule_id'/>');">
								<img src="<s:url value="/images/del.gif"/>" 
										border='0' alt='删除' style='cursor: hand'>
							</label>
						</s:if>
					</td>
				</s:if>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7" align="right">
				<lk:pages url="/gtms/stb/resource/autoSoftUpRule!queryRuleList.action" 
								styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
