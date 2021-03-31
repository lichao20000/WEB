<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>版本库查询</title>
<%
	/**
	 * @author chenjie
	 * @version 1.0
	 * @since 2012-12-14
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

</script>

</head>

<body>
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>设备厂商</th>
			<th>设备型号</th>
			<th>特定版本</th>

			<th>硬件版本</th>
			<th>软件版本</th>
			<th>是否规范</th>
			<th>是否审核</th>
			<th>设备类型</th>
			<th>上行方式</th>
			<th>终端规格</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceVersionList!=null">
			<s:if test="deviceVersionList.size()>0">
				<s:iterator value="deviceVersionList">
					<tr>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model_name" /></td>
						<td><s:property value="specversion" /></td>
						<td><s:property value="hardversion" /></td>
						<td><s:property value="softversion" /></td>
						<td>
							<s:if test="1==is_normal">
								是
							</s:if>
							<s:else>
								否
							</s:else>
						</td>
						<td><s:if test="is_check==1">
										经过审核
									</s:if> <s:else>
							<font color='red'>未测试</font>
						</s:else></td>
						<td><s:if test="rela_dev_type_id==1">
										e8-b
									</s:if> <s:if test="rela_dev_type_id==2">
										e8-c
									</s:if> 
									<s:if test="rela_dev_type_id==3">A8-B</s:if>
									<s:if test="rela_dev_type_id==4">A8-C</s:if>
									</td>
						<td><s:property value="type_name" /></td>
						<td><s:property value="specName" /></td>
						<td>
						<a
						    href="javascript:delDevice('<s:property value="id" />','<s:property value="file_path" />')">删除</a>|
						<a 
							href="javascript:editDevice('<s:property value="id" />')">编辑</a>
						</td>

					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>没有查询到相关设备版本库！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>没有查询到相关设备版本库！</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="11" align="right"><lk:pages
				url="/itms/resource/deviceVersionManageACT!queryDeviceVersion.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
<FORM method="post" action="" id="mainForm" name="mainForm" >
	<input type="hidden" name="filepath" id="filepath" />
</FORM>
</body>
<script>

// 编辑
function editDevice(id)
{     
	var url = "<s:url value='/itms/resource/deviceVersionManageACT!queryForModify.action' />" + "?id=" + id;
	var ans = window.showModalDialog(url,"",'dialogHeight:500px;dialogWidth:800px');

	if(ans == "1")
	{
		window.parent.queryDevice();
	}
}

// 删除
function delDevice(id,path)
{
	if(!delWarn())
		return;
	var url = "<s:url value="/itms/resource/deviceVersionManageACT!deleteDeviceVersion.action"/>";

	$.post(url,{
		id:id,
		filepath:path},
	function(msg){
		var result = parseInt(msg);
		if(result == 0)
		{
			alert("删除失败！");
			return;
		}
		else
		{
			alert("删除成功！");
			// 重新查询
			window.parent.queryDevice();
		}
	});
}

function delWarn(){
	if(confirm("确认要删除吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

// 下载
function download(path)
{
	if(confirm("确认要下载文件:[" + path + "]?"))
	{
		var url = "<s:url value="/itms/resource/deviceVersionManageACT!download.action"/>";
		$("input[@id='filepath']").val(path);
		
		document.mainForm.action = url;
		document.mainForm.submit();
		return;
	}
	
}

// 展示文件路径
function show(path)
{	
	alert("文件：" + path);
}
</script>
</html>