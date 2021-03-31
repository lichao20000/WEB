<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<head>
<link rel="stylesheet" href="<s:url value="/css2/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css2/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function addFilePath(){
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeFilePathAdd_hnlt.jsp'/>";
		window.open(strpage,"","left=200,top=60,width=500,height=450,resizable=yes,scrollbars=yes");
	}
	
	function showFilePath(path_id,device_model_id,goal_devicetype_id,vendorName,vendor_id,deviceModel,
			goalDeviceTypeName,hard_version,version_path,special_path,dcn_path)
	{
		var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeFilePathUpdate_hnlt.jsp'/>?"
							+ "path_id=" + path_id
							+ "&vendorName=" + vendorName 
							+ "&vendor_id=" + vendor_id 
							+ "&deviceModel=" +deviceModel
							+ "&device_model_id=" +device_model_id
							+ "&goalDeviceTypeName=" +goalDeviceTypeName
							+ "&goal_devicetype_id=" +goal_devicetype_id
							+ "&hard_version=" +hard_version
							+ "&version_path=" +version_path
							+ "&special_path=" +special_path
							+ "&dcn_path=" +dcn_path;
		
		window.open(strpage,"","left=200,top=60,width=500,height=450,resizable=yes,scrollbars=yes");
	}
	function deleteFilePath(pathId,modelId,typeId,vendor_id){
		if (!confirm('请确认是否删除当前版本路径！')) {
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!deleteUpgradeFilePath.action'/>"; 
		$.post(url,{
			pathId : pathId,
			deviceModelId : modelId,
			vendorId:vendor_id,
			goal_devicetypeId : typeId
		},function(ajax){
			if("1"== ajax){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
			window.location.reload();
		});
	}

</script>
</head>

<body>
<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
			<th>厂商</th>
			<th>型号</th>
			<th>硬件版本</th>
			<th>目标版本</th>
			<th>公网路径</th>
			<th>专网路径</th>
			<th>DCN路径</th>
			<th>操&nbsp;&nbsp;作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="filePathList != null ">
			<s:if test="filePathList.size() > 0">
				<s:iterator value="filePathList">
					<tr align="center">
						<td><s:property value="vendorName" /></td>
						<td><s:property value="deviceModel" /></td>
						<td><s:property value="hard_version" /></td>
						<td><s:property value="goalDeviceTypeName" /></td>
						<td><s:property value="version_path" /></td>
						<td><s:property value="special_path" /></td>
						<td><s:property value="dcn_path" /></td>
						<td>
							<input type="button" value="修改" 
								onclick="showFilePath('<s:property value="path_id"/>',
											'<s:property value="device_model_id"/>',
											'<s:property value="goal_devicetype_id"/>',
											'<s:property value="vendorName"/>',
											'<s:property value="vendor_id"/>',
											'<s:property value="deviceModel"/>',
											'<s:property value="goalDeviceTypeName"/>',
											'<s:property value="hard_version"/>',
											'<s:property value="version_path"/>',
											'<s:property value="special_path"/>',
											'<s:property value="dcn_path"/>')"/>
							&nbsp;&nbsp;&nbsp;
							<input type="button" value="删除" 
								onclick="deleteFilePath('<s:property value="path_id"/>',
											'<s:property value="device_model_id"/>',
											'<s:property value="goal_devicetype_id"/>',
											'<s:property value="vendor_id"/>')" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8 align=left>没有查询到相关数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8" align="right" height="15" nowrap="nowrap">
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeFilePathList.action" styleClass="" 
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
</body>