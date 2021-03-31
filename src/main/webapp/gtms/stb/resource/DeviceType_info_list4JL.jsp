<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备版本查询</title>
<%
	/**
	 * 
	 * 
	 * @author 
	 * @version 1.0
	 * @since 2010-09-08
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
			<!-- 
			<th>是否规范</th>
			<th>是否审核</th>
			<th>设备类型</th>
			<th>上行方式</th>
			<th>终端规格</th>
			<th>操作</th>
			 -->
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null">
			<s:if test="deviceList.size()>0">
				<s:iterator value="deviceList">
					<tr>
						<td><s:property value="vendor_add" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="specversion" /></td>
						<td><s:property value="hardwareversion" /></td>
						<td><s:property value="softwareversion" /></td>
						<!-- 
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
						<td><a  
							href="javascript:delDevice('<s:property value="devicetype_id" />','<s:property value="gw_type" />')">删除</a>|
						<a href="javascript:editDevice('<s:property value="devicetype_id" />','<s:property value="vendor_id" />',
																	'<s:property value="device_model_id" />',
																	'<s:property value="specversion" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="is_check" />',
																	'<s:property value="rela_dev_type_id" />',
																	'<s:property value="type_id" />',
																	'<s:property value="ip_type"/>',
																	'<s:property value="is_normal"/>',
																	'<s:property value="spec_id"/>'
																	)">编辑</a>|
						<a
							href="javascript:checkDevice('<s:property value="devicetype_id" />')">审核</a>|
						<a
							href="javascript:detailDevice('<s:property value="devicetype_id" />','<s:property value="spec_id" />')">详细信息</a>
						</td>
 						-->
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>没有查询到相关设备！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>没有查询到相关设备！</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="11" align="right"><lk:pages
				url="/gtms/stb/resource/stbDeviceTypeInfo!queryList.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
</body>
<script>


  function detailDevice(devicetype_id,specId){
   window.open("<s:url value='/itms/resource/deviceTypeInfo!queryDetail.action'/>?deviceTypeId="+devicetype_id+"&detailSpecId="+specId,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
    }


function editDevice(device_type_id,vendor_id,device_model_id,
				   specversion,hardwareversion,softwareversion,
				   is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id)
{     
  if(is_check=="" ){
	  is_check=-2;
	 
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;	
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;	
    window.parent.document.getElementsByName("specId")[0].value=spec_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	if(ip_type==0){
		 window.parent.document.getElementsByName("ipType")[0].checked="checked";
	}else{
		window.parent.document.getElementsByName("ipType")[1].checked="checked";
	}
	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";
	window.parent.document.getElementById("actLabel").innerHTML="编辑";
	parent.queryTypeName(type_id);
	parent.disableLabel(true);
	parent.showAddPart(true);
		
}




function delDevice(id,gw_type)
{
	if(!delWarn())
		return;
	var url = "<s:url value="/itms/resource/deviceTypeInfo!deleteDevice.action"/>";
	
	$.post(url,{
		deleteID:id,
		gw_type:gw_type
	},function(ajax){
		var result = parseInt(ajax);
		if(result == 0)
		{
			alert("该设备版本下有相关设备，不允许删除！");
			return;
		}
		if(result > 0)
		{
			alert("设备型号删除成功");
			
			// 普通方式提交
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			parent.reset();
            //form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("设备型号删除失败！");
		}
	});
}

function delWarn(){
	if(confirm("真的要删除该设备版本吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function checkDevice(id)
{
	var url = "<s:url value="/itms/resource/deviceTypeInfo!updateIsCheck.action"/>";
	$.post(url,{
		deviceTypeId:id
	},function(ajax){
		var result = parseInt(ajax);
		if(result > 0)
		{
			alert("审核设备成功");
			
			// 普通方式提交
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			//form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("审核设备失败");
		}
	});
}
</script>
</html>