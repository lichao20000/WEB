<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%request.setCharacterEncoding("GBK");%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../../Js/CheckForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doUpload(){
	var vendorName = parent.document.all("vendor_name");
	var softwareversion = parent.document.all("softwareversion");
	//var deviceModel = parent.document.getElementsByName("deviceModel");
	//var deviceModel = parent.document.all("device_model");
	var deviceModel = '';
	//var hardwareversion = parent.document.getElementsByName("hardwareversion");
	//var hardwareversion = parent.document.getElementById("hardwareversion").value;   hardwareversion
	var hardwareversion =parent.document.all("hardwareversion").value;
	var hardwareversionArr = hardwareversion.split(";");
	
	var strDeviceModel = '';
	var strHardwareversion = '';
	if(!IsNull(parent.document.frm.filename.value,"文件名")){
		parent.document.frm.filename.focus();
		parent.document.frm.filename.select();
		return false;
	}
	if(parent.document.frm.fileserver.value == -1){
		alert("请选择存储路径！");
		parent.document.frm.serUser.focus();
		parent.document.frm.serUser.select();
		return false;
	}
	if(vendorName.value == -1){
		alert("请选择厂商！");
		vendorName.select();
		return false;
	}
	//if(deviceModel.value == -1){
	//alert("请选择型号！");
		//vendorName.select();
		//return false;
	//}
	//if(!deviceModel || (deviceModel.length <=0)){
	//	alert("请选择型号！");
	//	deviceModel.select();
	//	return false;
	//}
	if(!softwareversion || softwareversion.value == -1 || softwareversion.value == ''){
		alert("请选择软件版本！");
		softwareversion.select();
		return false;
	}
	
	if(!hardwareversionArr || (hardwareversionArr.length <=0)){
		alert("没有符合条件的硬件版本！");
		return false;
	}
	
	var respqce = new RegExp(" ","g");
	var replus = /\+/g;
	//for (var i=0;i<deviceModel.length;i++){
	//	if(deviceModel[i].checked){
	//		//空格
	//		strDeviceModel += deviceModel[i].value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	//	}
	//}
	//strDeviceModel = deviceModel.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	strDeviceModel = deviceModel.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	//if(strDeviceModel==''){
	//	alert("请选择型号！");
	//	return false;
	//}
	strDeviceModel += "LINKAGE";
	//for (var i=0;i<hardwareversion.length;i++){
	//	if(hardwareversion[i].checked){
	//		//空格
	//		strHardwareversion += hardwareversion[i].value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	//	}
	//}
	for (var i=0;i<hardwareversionArr.length;i++){
		// 空格 做特殊处理
		strHardwareversion += hardwareversionArr[i].replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	}
	if(strHardwareversion==''){
		alert("没有符合条件的硬件版本！");
		return false;
	}
	strHardwareversion += "LINKAGE";
	//alert(strDeviceModel);
	//alert(strHardwareversion);
	/**
	modified by zhaixf
	if(parent.document.frm.device_model_id.value == -1){
		alert("请选择设备型号！");
		parent.document.frm.device_model_id.focus();
		parent.document.frm.device_model_id.select();
		return false;
	}
	if(parent.document.frm.devicetype_id.value == -1){
		alert("请选择软件版本！");
		parent.document.frm.devicetype_id.focus();
		parent.document.frm.devicetype_id.select();
		return false;
	}
	**/

//modified by yanhj.2007-7-25
//	if(!IsNull(parent.document.frm.serUser.value,"用户名")){
//		parent.document.frm.serUser.focus();
//		parent.document.frm.serUser.select();
//		return false;
//	}
//	if(!IsNull(parent.document.frm.serPass.value,"密码")){
//		parent.document.frm.serPass.focus();
//		parent.document.frm.serPass.select();
//		return false;
//	}
//modified end.2007-7-25
	if(!IsNull(document.frm.file1.value,"上传文件")){
		document.frm.file1.focus();
		document.frm.file1.select();
		return false;
	}
	for(var i=0;i< parent.document.frm.inORout.length ;i++){
		if(parent.document.frm.inORout[i].checked){
			inORout = parent.document.frm.inORout[i].value;
			break;
		}
	}
	
	var server = parent.document.frm.fileserver.value;
	var serList = new Array();  
	serList = server.split("|");
	if (inORout == 0) {
		//内网URL
		var url = serList[0];
	} else {
		//外网URL
		var url = serList[5];
	}
	var path = serList[1];
	var dir_id = serList[2];
	//var remark =  parent.document.frm.remark.value;
	var remark='';
	var filetype = 1;
	//for(var i=0;i< parent.document.frm.filestatus.length;i++)
	//{
	//	if(parent.document.frm.filestatus[i].checked)
	//	{
		//	filetype=parent.document.frm.filestatus[i].value;
		//	break;
		//}
	//}
	
	var citys="";
	var services="";
	for (var i=0;i<parent.document.frm.elements.length;i++)
   	{
		var e = parent.document.frm.elements[i];
		if (e.name == 'city' && e.checked==true)
		{
			citys=citys+","+parent.document.frm.elements[i].value;
		}
		if (e.name == 'service' && e.checked==true)
		{
			services=services+","+parent.document.frm.elements[i].value;
		}
   	}
    citys = citys + ",";
    services = services + ",";
	
	idWait.innerHTML = "正在上传......";
	
	/**
	document.frm.action = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + encodeURIComponent(parent.document.frm.filename.value)
							+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value
						    +"&remark=" +remark + "&file_status=" + filetype + "&devicetype_id=" + parent.document.frm.devicetype_id.value 
						    + "&device_model_id=" + parent.document.frm.device_model_id.value 
						    + "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;
	**/
	
	//add by zhangcong 2011-11-25
	var uploadType = <%= LipossGlobals.getLipossProperty("uploadFileTransitByWEB")%>
	//alert(uploadType);
	
	var fileRename = encodeURIComponent(parent.document.frm.filename.value);
	
	//var urlParameter = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + fileRename
	//	+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK')
	//	+"&remark=" +remark + "&file_status=" + filetype + "&softwareversion=" + softwareversion.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') 
	//	+ "&device_model=" + strDeviceModel + "&vendor_name=" + vendorName.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + "&hardwareversion=" + strHardwareversion
	//	+ "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;



	var urlParameter = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + fileRename
	+"&dir_id="+dir_id+"&seruser="+"&serpass="
	+"&remark=" +remark + "&file_status=" + filetype + "&softwareversion=" + softwareversion.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') 
	+ "&device_model="+"ZXV10H618B" + strDeviceModel + "&vendor_name=" + vendorName.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + "&hardwareversion="+ strHardwareversion
	+ "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;

	
	if(uploadType == '1')
	{
		
		//alert("111");
		//上传文件通过WEB中转
		$("input[@name='urlParameter']").val("");
		document.getElementById("urlParameter").value = urlParameter;
		//alert(document.getElementById("urlParameter").value);
		//$("input[@name='urlParameter']").append(urlParameter);
		//alert(urlParameter);
	
		document.frm.action = "<s:url value='/gtms/blocTest/SoftwareAction!uploadLocalFile.action'/>";
		//document.frm.action = "<s:url value='/gwms/resource/software!uploadLocalFile.action'/>";
		
	}else
	{
		//alert("222");
		//地址直达
		document.frm.action = urlParameter;
		
	}
	//提交表单
	//alert(document.frm.action);
	document.frm.submit();
	document.frm.upload.disabled = true;
	return;
	//add by zhangcong 2011-11-25
	
	//////////================无用代码，暂存 2011-11-25 zhangcong@
	var requestURL = "<s:url value='/gwms/resource/software!checkUploadLocalFile.action'/>";
	//AJAX判断上传文件是否合法
	$.post(requestURL,{
		strRename:fileRename
	},function(ajax){
		//alert(ajax);
		
		//如果文件已经存在，则中止
		if(ajax.indexOf("ERROR:") > -1)
		{
			alert(ajax.substring(ajax.indexOf(":") + 1));
			idWait.innerHTML = ajax.substring(ajax.indexOf(":") + 1);
			return;
		}
		
		document.frm.action = "<s:url value='/gwms/resource/software!uploadLocalFile.action'/>";
		
		//alert(document.frm.action);
		document.frm.submit();
		document.frm.upload.disabled = true;
	});
	//alert("action end!");
	return;
}
//-->
</SCRIPT>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>

<BODY>
<FORM NAME="frm" METHOD=POST action="" ENCTYPE="multipart/form-data">
<input type="hidden" id="urlParameter" name="urlParameter" value=""/>
<INPUT TYPE="file" NAME="file1" class="bk">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="button" name="upload" value=" 上 传 " onclick="doUpload()" class=jianbian>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
</FORM>
</BODY>
</HTML>
