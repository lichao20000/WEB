<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量导入开机画面</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script> <lk:res />
<SCRIPT LANGUAGE="JavaScript">
function ExecMod() {
var filename = $("input[@name='gwShare_fileName']").val();
if ("" == filename || null == filename) {
	alert("请上传文件！");
	return false;
}
/* var groupidS ="";
$("input[@name='groupIDS'][@checked]").each(function(){ 
	groupidS += $(this).val()+",";
});
if(groupidS==""){
	alert("请选择分组!");
	return false;
} */
/* if(groupidS.length>0){
	$("input[@name='groupids']").attr("value",groupidS.substring(0,groupidS.length-1));
} */
var Invalidtime= $("input[@name='Invalid_time']").val();
if(Invalidtime=="")
	{
	alert("请选择失效时间!");
	return false;
	}
var startPic = $("input[@id='startFile']").val();
var bootPic = $("input[@id='bootFile']").val();
var authPic = $("input[@id='authFile']").val();
if(checkChinese(startPic)){
	alert("启动图片名称不可含有中文");
	return false;
}

if(checkChinese(bootPic)){
	alert("开机图片名称不可含有中文");
	return false;
}

if(checkChinese(authPic)){
	alert("认证图片名称不可含有中文");
	return false;
}
var taskName =  $("#taskName").val();
if(""==taskName){
	alert("任务名不能为空！");
	return false; 
} 
var isPicPass  =  checkPic();
if(isPicPass){
	$("#doButton").attr("disabled",true);
	 var form = document.getElementById("form"); 
	$("form[@name='batchexform']").attr("action","btachImportAdvertisement!batchImport.action");
	$("form[@name='batchexform']").submit();
}else{
	return false;
}
}
function toExportCust()
{
	$("form[@name='batchexform']").attr("action","openDeviceShowPic!downloadTemplateCust.action");
	$("form[@name='batchexform']").submit();
}
function checkPic(){
	var bootPic = $("input[@name='bootFile']").val();
	if(""==bootPic){
		
	}else{
		var bootfilet = bootPic.split(".");
    	if(bootfilet.length<2){
    		alert("开机图片必须上传!");
    		return false;
    	}
        
   		var bootfile = bootPic.split("\\");
   		var bootfileName = bootfile[bootfile.length-1];
   		$("input[@name='bootFileName']").attr("value",bootfileName);
	}
	
		var startPic = $("input[@name='startFile']").val();
	if(""==startPic){
		
	}else{
		var startfilet = startPic.split(".");
    	if(startfilet.length<2){
    		alert("启动图片必须上传!");
    		return false;
    	}
        
    	var startfile = startPic.split("\\");
   		var startfileName = startfile[startfile.length-1];
   		$("input[@name='startFileName']").attr("value",startfileName);
	}
	
		var authPic = $("input[@name='authFile']").val();
	if(""==authPic){
		
	}else{
		var authfilet = authPic.split(".");
    	if(authfilet.length<2){
    		alert("认证图片必须上传!");
    		return false;
    	}
   		var authfile = authPic.split("\\");
   		var authfileName = authfile[authfile.length-1];
   		$("input[@name='authFileName']").attr("value",authfileName);
	}
	
	//新的修改，三者不能同时为空
	if(""==bootPic&&""==startPic&&""==authPic){
		alert("启动，开机，认证三张图片不能同时为空！");
		return false;
	}
	return true;
}
//中文校验
function checkChinese(str){
	var regTest = /^[\u4e00-\u9fa5]+$/;
	var flag = false;
	if(str != null && $.trim(str) != ""){
		if(str.indexOf("\\") != -1){
			var strArr = str.split("\\");
			str = strArr[strArr.length-1];
		}
		//alert(str);
    	for(var i=0 ; i<str.length ; i++){
			var word = str.substring(i,i+1);
			if(regTest.test(word)){
				flag = true ; 
				break;
			}
		}
    }
	return flag;
}

$(function() {
	var url = "<s:url value='/gtms/stb/resource/stbBootAdvertisement!getGroupId.action'/>";
		$("div[@id='groupid']").html("");
		$.post(url,{
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='groupIDS' value='"+xValue+"'>"+xText+"  ";
						$("div[@id='groupid']").append(checkboxtxt);
						}
					}
				}else{
					$("div[@id='groupid']").append("没有分组！");
				}
		});
});
</SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
</head>
<s:form action="openDeviceShowPic!importConfig.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="">
	<TABLE width="100%" class="querytable" align="center">
		<input type="hidden" name="bootFileName" value="">
		<input type="hidden" name="startFileName" value="">
		<input type="hidden" name="authFileName" value="">
		 <input type="hidden" name="groupids"   value="">
		<tr>
			<td colspan="4" class="title_1" id="gwShare_thTitle">批 量 导 入</td>
		</tr>
		<tr id="gwShare_tr31" bgcolor="#FFFFFF">
			<td align="right" width="15%">提交文件</td>
			<td colspan="2" width="65%">
				<div id="importUsername">
					<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
						src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20"
						width="100%"> </iframe>
					<input type="hidden" name=gwShare_fileName value="" />
				</div>
			</td>
			<td width="20%">
<a href="javascript:void(0);" onclick="toExportCust();"><font color="red">下载模板</font></a>		</td>	
		</tr>
		<tr id="gwShare_tr32">
			<td CLASS="green_foot" align="right">注意事项</td>
			<td colspan="3" CLASS="green_foot">
				1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。 <br> 2、文件的第一行为标题行，即【业务账号】。
				<br> 3、文件只有一列。 <br> 4、文件行数不要超过5000行，以免影响性能。
			</td>
		</tr>
		<TR>
			<TD class="title_2" align="center" width="15%">任务名称</TD>
			<TD colspan="3"><input type="text" id="taskName" name="taskName"
				width="500"></TD>
		</TR>
			 <%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
				<TR id="startPic">
					<TD width="15%"  class="title_2">开机图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">格式为(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<TR id="bootPic">
					<TD width="15%"  class="title_2">开机动画</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">格式为(zip)</font>
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">认证图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">格式为(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<%}else{ %>
				<TR id="startPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">启动图片</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">开机图片</TD>
					<%} %>
					
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="bootPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">开机图片</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">开机动画</TD>
					<%} %>
					
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">认证图片</TD>
					<TD width="35%" colspan="3">
						<s:file label="上传" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<%} %>
		<ms:inArea areaCode="jl_dx" notInMode="true">
		<TR>
			<TD class=column align="right">优先级</TD>
			<TD colspan=3><select name="priority" class="bk">
					<option value="1">优先级1</option>
					<option value="2">优先级2</option>
					<option value="3">优先级3</option>
			</select></TD>
		</TR>
		</ms:inArea>
		<!-- <TR>
					<TD class="title_2" align="center" width="15%">
						分组
					</TD>
					<TD width="85%" colspan="3">
						<div id="groupid">
							 
						</div>
					</TD>
				</TR> -->
		<TR id="aurthPic">
			<TD width="15%"  class="title_2">任务失效时间</TD>
			<TD width="35%" colspan="3"><input type="text" 
				name="Invalid_time" id="Invalidtime" readonly value="<s:property value='endTime'/>"> 
				<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.Invalidtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
			</TD>
		</TR>
		<tr bgcolor="#FFFFFF">
			<td colspan="4" align="right" class="foot" width="100%">
				<div align="right">
					<button onclick="javascript:ExecMod();" name="gwShare_queryButton" id="doButton"
						style="CURSOR: hand" style="display:">定 制</button>
				</div>
			</td>
		</tr>
	</TABLE>
</s:form>