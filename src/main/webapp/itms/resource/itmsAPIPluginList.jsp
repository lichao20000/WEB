<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		//parent.document.getElementById("queryButton").disabled = false;
		parent.dyniframesize();
	});

	function showUpdate(classifyId,classifyName,status,classifyDesc){
		var strpage = "<s:url value='/itms/resource/apiPlugin!update.action'/>?classifyId=" + classifyId + "&classifyName=" + classifyName + "&status=" +status
		+ "&classifyDesc=" +classifyDesc;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function deleteApiPlugin(classifyId,classifyName){
		if (!confirm('请确认是否删除'+classifyName)) {
			return false;
		}
		//var page="<s:url value='/itms/resource/apiPlugin!deleteApiPlugin.action'/>?"
		//	+ "classifyId=" + classifyId;
		//window.open(page,"","left=50,top=50,height=250,width=400,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		var url = "<s:url value='/itms/resource/apiPlugin!deleteApiPlugin.action'/>"; 
		$.post(url,{
			classifyId : classifyId
		},function(ajax){
			if("1"== ajax){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
		});
	}
 
</script>
<div>
	<table id="updatetable" style="display: none">
		<tr>
			<td>blue</td>
		</tr>
	</table>
	<table class="listtable">
		<caption>统计结果</caption>
		<thead>
			<tr>
				<th>API权限分类名称</th>
				<th>分类详细描述</th>
				<th>创建时间</th>
				<th>创建者</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="pluginList != null ">
				<s:if test="pluginList.size() > 0">
					<s:iterator value="pluginList">
						<tr align="center">
							<td><s:property value="classify_name" /></td>
							<td><s:property value="classify_desc" /></td>
							<td><s:property value="create_time" /></td>
							<td><s:property value="creator" /></td>
							<td><s:property value="status" /></td>
							<td><input type="button" value="修改"
								onclick="showUpdate('<s:property value="classify_id"/>','<s:property value="classify_name"/>','<s:property value="status"/>','<s:property value="classify_desc"/>')" />/
								<input type="button" value="删除" onclick="deleteApiPlugin('<s:property value="classify_id"/>','<s:property value="classify_name"/>')" />
								</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6 align=left>没有查询到相关数据！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" align="right" height="15">[ 统计总数 : <s:property
						value='queryCount' /> ]&nbsp;<lk:pages
						url="/itms/resource/apiPlugin!getApiPluginList.action" showType=""
						isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>

</div>
