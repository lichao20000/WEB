<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备版本查询</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>
<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
       <FORM NAME="picForm" id="picForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
		    <input type="hidden" name="fileName" value="">
		</FORM>
	<input type='hidden' name="editDeviceType" value="<s:property value="editDeviceType" />" />
	<table class="listtable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th align="center">业务编号</th>
				<th align="center">终端厂家</th>
				<th align="center">终端类型</th>
				<th align="center">终端型号</th>
				<th align="center">硬件版本</th>
				<th align="center">软件版本</th>
				<th align="center">工作事项</th>
				<th style="width:13%" align="center">工作内容</th>
				<th align="center">厂家联系人</th>
				<th align="center">厂家联系方式</th>
				<th align="center">接待人员</th>
				<th style="width:10%" align="center">开始时间</th>
				<th style="width:10%" align="center">结束时间</th>
				<th align="center">完成状态</th>
				<th style="width:13%" align="center">备注</th>
				<th align="center">资料文件</th>
				<th style="width:6%" align="center">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="workMemorialList!=null">
				<s:if test="workMemorialList.size()>0">
					<s:iterator value="workMemorialList">
						<tr>
							<td><s:property value="busNo" /></td>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="spec_type" /></td>
							<td><s:property value="spec_model" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="workOpts" /></td>
							<td>
							 <div style="overflow-y:auto;height:30px;">
							   <s:property value="workContent" />
							</div>
							</td>
							<td><s:property value="connPerson" /></td>
							<td><s:property value="phone" /></td>
							<td><s:property value="reseption" /></td>
							<td><s:property value="startTime" /></td>
							<td><s:property value="endTime" /></td>
							<td><s:property value="status" /></td>
							<td>
							 <div style="overflow-y:auto;height:30px;">
							   <s:property value="commit" />
							</div>
							</td>
							<td><a href="javascript:downPic('<s:property value="fileNmae" />')"><s:property value="fileNmae" /></a></td>
							<td>
								<a href="javascript:delDevice('<s:property value="busNo" />')">删除</a>|
								<a href="javascript:edit('<s:property value="busNo" />',
																	'<s:property value="vendor_name" />',
																	'<s:property value="spec_type" />',
																	'<s:property value="spec_model" />',
																	'<s:property value="hardwareversion" />',
																	'<s:property value="softwareversion" />',
																	'<s:property value="workOpts" />',
																	'<s:property value="workContent" />',
																	'<s:property value="connPerson" />',
																	'<s:property value="phone"/>',
																	'<s:property value="reseption"/>',
																	'<s:property value="startTime"/>',
																	'<s:property value="endTime" />',
																	'<s:property value="commit" />',
																	'<s:property value="fileNmae" />',
																	'<s:property value="status" />'
																)">修改</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=17>没有查询到相关备忘录信息！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=17>没有查询到相关备忘录信息！</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="17" align="right" id="toolbar">
					<lk:pages url="/itms/resource/memBook!queryNextList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
			<tr>
				 <td colspan="17" align="right">
		              <a href="javascript:toExport();">
			             <IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出' style='cursor: hand'>
		             </a>
		         </td>
			</tr>
		</tfoot>

	</table>
</body>
<script>

//编辑 
function edit(busNo,vendor_name,spec_type,spec_model,hardwareversion,softwareversion,workOpts,
		workContent,connPerson,phone,reseption,startTime,endTime,commit,fileNmae,status)
{  
	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_name;
	parent.change_select_add('specType',spec_type);
	//window.parent.document.getElementsByName("spec_type_add")[0].value=spec_type;
	window.parent.document.getElementsByName("spec_model_add")[0].value=spec_model;	
	window.parent.document.getElementsByName("hardware_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("software_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("workOpts_add")[0].value=workOpts;	
    window.parent.document.getElementsByName("work_status")[0].value=status;
    window.parent.document.getElementsByName("work_status")[0].disabled = false;
    window.parent.document.getElementsByName("reception_add")[0].value=reseption;	
    window.parent.document.getElementsByName("connPerson_add")[0].value=connPerson;	
    window.parent.document.getElementsByName("phone_add")[0].value=phone;	
    window.parent.document.getElementsByName("startOpenDate")[0].value=startTime;	
    
    if(fileNmae == "" || fileNmae == null || fileNmae == undefined){
    	window.parent.document.getElementById("importPNG_msgs").style.display="none";
    	window.parent.document.getElementById("importPNG").style.display="";
    }else{
    	window.parent.document.getElementById("importPNG").style.display="none";
    	window.parent.document.getElementById("importPNG_msgs").style.display="";
    	window.parent.document.getElementById("importPNG_msgs_th").innerHTML= fileNmae+"&nbsp;&nbsp;&nbsp;<a href='javascript:reUpload()'>重新上传</a>";
    }
    
    if(endTime == 0 ){
    	endTime = "";
    }
    window.parent.document.getElementsByName("endOpenDate")[0].value=endTime;	
    window.parent.document.getElementsByName("endOpenDate")[0].disabled=false;	
    window.parent.document.getElementsByName("content_add")[0].value=workContent;	
    window.parent.document.getElementsByName("commit_add")[0].value=commit;
    window.parent.document.getElementById("actLabel").innerHTML = "编辑";
    window.parent.document.getElementsByName("operType")[0].value= 2;
    window.parent.document.getElementsByName("busno_edit")[0].value= busNo;
    window.parent.showAddPart("addTable",true);
    if(fileNmae == "" || fileNmae == null || fileNmae == undefined){
    	window.parent.reLoadFile()
    }
}
 

function downPic(fileName){
	var url =  "<s:url value='/itms/resource/memBook!downLoad.action'/>";
	$("input[@name='fileName']").val(fileName);
	document.getElementById("picForm").action = url;
	document.getElementById("picForm").submit();
	document.getElementById("picForm").reset();
}
function toExport(){
	window.parent.toExport();
}

function delDevice(busNo){
	var url =  "<s:url value='/itms/resource/memBook!operWorkMem.action'/>";
	$.post(url,{
		operType : 3,
		busNo : busNo
	},function(ajax){
		alert(ajax);
		window.parent.query();
	})
}

 
</script>
</html>