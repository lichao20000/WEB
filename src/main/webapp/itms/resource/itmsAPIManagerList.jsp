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

//	function showUpdate(managerId,servicenameZh,servicenameEn,functionDesc,apiListName){
//		var strpage = "itmsAPIManagerUpdate.jsp?managerId=" + managerId + "&servicenameZh=" + servicenameZh + "&servicenameEn=" +servicenameEn
//		+ "&functionDesc=" +functionDesc+ "&apiListName=" +apiListName;
//		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
//	}
	function showUpdate(managerId,servicenameZh,servicenameEn,functionDesc,apiListName){
		var strpage = "<s:url value='/itms/resource/apiManager!update.action'/>?managerId=" + managerId + "&servicenameZh=" + servicenameZh + "&servicenameEn=" +servicenameEn
		+ "&functionDesc=" +functionDesc+ "&apiListName=" +apiListName;
		window.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function deleteApiManager(managerId,servicenameZh){
		if (!confirm('请确认是否删除'+servicenameZh)) {
			return false;
		}
		//var page="<s:url value='/itms/resource/apiManager!deleteApiManager.action'/>?"
		//	+ "managerId=" + managerId;
		//window.open(page,"","left=50,top=50,height=250,width=400,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		var url = "<s:url value='/itms/resource/apiManager!deleteApiManager.action'/>"; 
		$.post(url,{
			managerId : managerId
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
				<th width="10%">服务类名(中文)</th>
				<th width="12%">服务类名(英文)</th>
				<th width="8%">权限分类</th>
				<th width="20%">功能描述</th>
				<th>API接口名</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="managerList != null ">
				<s:if test="managerList.size() > 0">
					<s:iterator value="managerList">
						<tr align="center">
							<td><s:property value="servicename_zh" /></td>
							<td><s:property value="servicename_en" /></td>
							<td><s:property value="classify_name" /></td>
							<td><s:property value="function_desc" /></td>
							<td><s:property value="api_list_name" /></td>
							<td><input type="button" value="修改"
								onclick="showUpdate('<s:property value="manager_id"/>','<s:property value="servicename_zh"/>','<s:property value="servicename_en"/>','<s:property value="function_desc"/>','<s:property value="api_list_name"/>')"/>
								<input type="button" value="删除" onclick="deleteApiManager('<s:property value="manager_id"/>','<s:property value="servicename_zh"/>')" />
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
						url="/itms/resource/apiManager!getApiManagerList.action" showType=""
						isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>

</div>
