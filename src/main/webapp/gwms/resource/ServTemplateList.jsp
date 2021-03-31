<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模板查询</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	//var instArea = window.parent.document.getElementById("instArea").value;
});

$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
	<%-- <input type='hidden' name="editDeviceType" value="<s:property value="editDeviceType" />" /> --%>
	<table class="listtable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>模板名称</th>
				<th>业务类型</th>
				<!-- <th>VLANID</th> -->
				<th>修改时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="deviceList!=null">
				<s:if test="deviceList.size()>0">
					<s:iterator value="deviceList">
						<tr>
							<td align="center"><s:property value="name" /></td>
							<td align="center">机顶盒</td>
							<%-- <td align="center"><s:property value="vlanid" /></td> --%>
							<td align="center"><s:property value="update_time" /></td>
							<td align="center">
								<a href="javascript:detailDevice('<s:property value="id" />')">详细信息</a>|
								<a href="javascript:editDevice('<s:property value="id" />')">编辑</a>|
								<a href="javascript:delDevice('<s:property value="id" />')">删除</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>没有查询到相关信息！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/servTemplate!init.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
<script>
//编辑设备类型
function detailDevice(id)
{     
	window.open("<s:url value='/gwms/resource/servTemplate!queryDetail.action' />?id="+id);
}


function editDevice(id)
{     
	window.open("<s:url value='/gwms/resource/servTemplate!queryDetail4Edit.action' />?id="+id);
	//var returnVal = window.showModalDialog("<s:url value='/gwms/resource/servTemplate!queryDetail4Edit.action' />?id="+id,"","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
}


//删除
function delDevice(id)
{
	if(confirm("真的要删除该模板吗？\n本操作所删除的不能恢复！！！")){
		$.ajax({
    	    type:"POST",
    	    async:false,
    	    url:"<s:url value='/gwms/resource/servTemplate!deleteDevice.action' />?id="+id,
    	    dataType:"text",
    	    data: {},
    	    success:function (data) {
    	    	if(data==1){
    	    		alert("模板删除成功");
    	    	}
    	    	else
    	 		{
    	 			alert("删除模板时异常,请与管理员联系");
    	 		}
    	 		window.location.reload();
    	 		//window.opener.Init();
    	    }
    	});
		return true;
	}
	else{
		return false;
	}
}


function toExcel(taskId,upResult){
	var url =  "<s:url value='/itms/resource/deviceTypeInfo!exportList.action'/>?deviceTypeId="+devicetype_id;
	document.all("childFrm").src=url;
}


</script>
</html>