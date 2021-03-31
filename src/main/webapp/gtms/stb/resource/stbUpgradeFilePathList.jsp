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
		parent.dyniframesize();
	});
	
	function addFilePath(){
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeFilePathAdd.jsp'/>?operateType=add";
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	function showFilePath(path_id,device_model_id,goal_devicetype_id,vendorName,deviceModel,goalDeviceTypeName){
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeFilePathUpdate.jsp'/>?path_id=" + path_id + "&vendorName=" + vendorName + "&deviceModel=" +deviceModel
		+ "&device_model_id=" +device_model_id+ "&goalDeviceTypeName=" +goalDeviceTypeName+ "&goal_devicetype_id=" +goal_devicetype_id;
		window.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function deleteFilePath(pathId,modelId,typeId ){
		if (!confirm('请确认是否删除当前版本路径！')) {
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!deleteUpgradeFilePath.action'/>"; 
		$.post(url,{
			pathId : pathId,
			deviceModelId : modelId,
			goal_devicetypeId : typeId
		},function(ajax){
			if("1"== ajax){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
			//window.location.reload();//此方法会造成ie再次发送
			document.URL=location.href;
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
				<th width="10%">厂商</th>
				<th width="12%">型号</th>
				<th width="20%">目标版本</th>
				<th>升级路径</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="filePathList != null ">
				<s:if test="filePathList.size() > 0">
					<s:iterator value="filePathList">
						<tr align="center">
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="goalDeviceTypeName" /></td>
							<td><s:property value="version_path" /></td>
							<td>
								<input type="button" value="修改" onclick="showFilePath('<s:property value="path_id"/>','<s:property value="device_model_id"/>'
								,'<s:property value="goal_devicetype_id"/>','<s:property value="vendorName"/>','<s:property value="deviceModel"/>'
								,'<s:property value="goalDeviceTypeName"/>')"/>
								<input type="button" value="删除" onclick="deleteFilePath('<s:property value="path_id"/>','<s:property value="device_model_id"/>','<s:property value="goal_devicetype_id"/>')" />
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
				<% if ( !LipossGlobals.inArea("jx_dx") ) { %>
				<td align="left" height="15">
					<button onclick="javascript:addFilePath();" 
				name="addFilePath" style="CURSOR:hand" style="display:" > 新 增 </button>
				</td>
				<%} %>
				<td colspan="5" align="right" height="15">[ 统计总数 : <s:property
						value='queryCount' /> ]&nbsp;<lk:pages
						url="/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeFilePathList.action" showType=""
						isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>

</div>
