<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������뿪������</title>
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
	alert("���ϴ��ļ���");
	return false;
}
/* var groupidS ="";
$("input[@name='groupIDS'][@checked]").each(function(){ 
	groupidS += $(this).val()+",";
});
if(groupidS==""){
	alert("��ѡ�����!");
	return false;
} */
/* if(groupidS.length>0){
	$("input[@name='groupids']").attr("value",groupidS.substring(0,groupidS.length-1));
} */
var Invalidtime= $("input[@name='Invalid_time']").val();
if(Invalidtime=="")
	{
	alert("��ѡ��ʧЧʱ��!");
	return false;
	}
var startPic = $("input[@id='startFile']").val();
var bootPic = $("input[@id='bootFile']").val();
var authPic = $("input[@id='authFile']").val();
if(checkChinese(startPic)){
	alert("����ͼƬ���Ʋ��ɺ�������");
	return false;
}

if(checkChinese(bootPic)){
	alert("����ͼƬ���Ʋ��ɺ�������");
	return false;
}

if(checkChinese(authPic)){
	alert("��֤ͼƬ���Ʋ��ɺ�������");
	return false;
}
var taskName =  $("#taskName").val();
if(""==taskName){
	alert("����������Ϊ�գ�");
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
    		alert("����ͼƬ�����ϴ�!");
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
    		alert("����ͼƬ�����ϴ�!");
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
    		alert("��֤ͼƬ�����ϴ�!");
    		return false;
    	}
   		var authfile = authPic.split("\\");
   		var authfileName = authfile[authfile.length-1];
   		$("input[@name='authFileName']").attr("value",authfileName);
	}
	
	//�µ��޸ģ����߲���ͬʱΪ��
	if(""==bootPic&&""==startPic&&""==authPic){
		alert("��������������֤����ͼƬ����ͬʱΪ�գ�");
		return false;
	}
	return true;
}
//����У��
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
					$("div[@id='groupid']").append("û�з��飡");
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
			<td colspan="4" class="title_1" id="gwShare_thTitle">�� �� �� ��</td>
		</tr>
		<tr id="gwShare_tr31" bgcolor="#FFFFFF">
			<td align="right" width="15%">�ύ�ļ�</td>
			<td colspan="2" width="65%">
				<div id="importUsername">
					<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
						src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20"
						width="100%"> </iframe>
					<input type="hidden" name=gwShare_fileName value="" />
				</div>
			</td>
			<td width="20%">
<a href="javascript:void(0);" onclick="toExportCust();"><font color="red">����ģ��</font></a>		</td>	
		</tr>
		<tr id="gwShare_tr32">
			<td CLASS="green_foot" align="right">ע������</td>
			<td colspan="3" CLASS="green_foot">
				1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ �� <br> 2���ļ��ĵ�һ��Ϊ�����У�����ҵ���˺š���
				<br> 3���ļ�ֻ��һ�С� <br> 4���ļ�������Ҫ����5000�У�����Ӱ�����ܡ�
			</td>
		</tr>
		<TR>
			<TD class="title_2" align="center" width="15%">��������</TD>
			<TD colspan="3"><input type="text" id="taskName" name="taskName"
				width="500"></TD>
		</TR>
			 <%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
				<TR id="startPic">
					<TD width="15%"  class="title_2">����ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<TR id="bootPic">
					<TD width="15%"  class="title_2">��������</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(zip)</font>
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">��֤ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp;<font color="red">��ʽΪ(jpg/jpeg/png)</font>
					</TD>
				</TR>
				<%}else{ %>
				<TR id="startPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%} %>
					
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50"  id="startFile" name="startFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="bootPic">
					<%if("jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD width="15%"  class="title_2">����ͼƬ</TD>
					<%}else{ %>
						<TD width="15%"  class="title_2">��������</TD>
					<%} %>
					
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="bootFile" name="bootFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<TR id="aurthPic">
					<TD width="15%"  class="title_2">��֤ͼƬ</TD>
					<TD width="35%" colspan="3">
						<s:file label="�ϴ�" theme="simple" size="50" id="authFile" name="authFile" ></s:file>&nbsp;&nbsp;&nbsp; 
					</TD>
				</TR>
				<%} %>
		<ms:inArea areaCode="jl_dx" notInMode="true">
		<TR>
			<TD class=column align="right">���ȼ�</TD>
			<TD colspan=3><select name="priority" class="bk">
					<option value="1">���ȼ�1</option>
					<option value="2">���ȼ�2</option>
					<option value="3">���ȼ�3</option>
			</select></TD>
		</TR>
		</ms:inArea>
		<!-- <TR>
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
					<TD width="85%" colspan="3">
						<div id="groupid">
							 
						</div>
					</TD>
				</TR> -->
		<TR id="aurthPic">
			<TD width="15%"  class="title_2">����ʧЧʱ��</TD>
			<TD width="35%" colspan="3"><input type="text" 
				name="Invalid_time" id="Invalidtime" readonly value="<s:property value='endTime'/>"> 
				<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.Invalidtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��" />
			</TD>
		</TR>
		<tr bgcolor="#FFFFFF">
			<td colspan="4" align="right" class="foot" width="100%">
				<div align="right">
					<button onclick="javascript:ExecMod();" name="gwShare_queryButton" id="doButton"
						style="CURSOR: hand" style="display:">�� ��</button>
				</div>
			</td>
		</tr>
	</TABLE>
</s:form>