<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>



<%request.setCharacterEncoding("GBK");%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doUpload(){
	if(isEmpty(parent.document.frm.filename.value)){
		alert("名称不能为空");
		parent.document.frm.filename.focus();
		parent.document.frm.filename.select();
		return false;
	}
	if(parent.document.frm.devicetype_id.value == -1){
		alert("请选择设备型号！");
		parent.document.frm.devicetype_id.focus();
		parent.document.frm.devicetype_id.select();
		return false;
	}
	if(parent.document.frm.fileserver.value == -1){
		alert("请选择存储路径！");
		parent.document.frm.serUser.focus();
		parent.document.frm.serUser.select();
		return false;
	}
	//if(!IsNull(parent.document.frm.serUser.value,"用户名")){
		//parent.document.frm.serUser.focus();
		//parent.document.frm.serUser.select();
		//return false;
	//}
	//if(!IsNull(parent.document.frm.serPass.value,"密码")){
		//parent.document.frm.serPass.focus();
		//parent.document.frm.serPass.select();
		//return false;
	//}
	if(isEmpty(parent.document.frm.area_name.value)){
		alert("管理域不能为空");
		parent.document.frm.area_name.focus();
		parent.document.frm.area_name.select();
		return false;
	}
	/* if(parent.document.frm.gw_type.value == -1){
		alert("请选择家庭网关或者企业网关");
		document.frm.gw_type.focus();
		document.frm.gw_type.select();
		return false;
	} */
	if(isEmpty(document.frm.file1.value)){
		alert("上传文件不能为空");
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
	var remark =  parent.document.frm.remark.value;
	
	var filetype = 0;
	for(var i=0;i< parent.document.frm.filestatus.length;i++)
	{
		if(parent.document.frm.filestatus[i].checked)
		{
			filetype=parent.document.frm.filestatus[i].value;
			break;
		}
	}
	
	var inst_area = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
	idWait.innerHTML = "正在上传......";
	if("nx_dx" ==  inst_area)
	{
		var gw_type = '<%= request.getParameter("gw_type")%>';
		var urlParameter = url + "/doUploadNX.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + parent.document.frm.filename.value
		+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value
		+"&remark=" +remark + "&file_status=" + filetype + "&devicetype_id=" + parent.document.frm.devicetype_id.value 
		+ "&area_id=" + parent.document.frm.area_id.value + "&tablename=tab_vercon_file&gw_type="+gw_type;
		
		$("input[@name='urlParameter']").val("");
		document.getElementById("urlParameter").value = urlParameter;
		//alert(document.getElementById("urlParameter").value);
		//$("input[@name='urlParameter']").append(urlParameter);
		//alert(urlParameter);
	
		document.frm.action = "<s:url value='/bbms/resource/uploadByNX!uploadLocalFile.action'/>";
	}
	else
	{
		document.frm.action = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + parent.document.frm.filename.value
		+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value
		+"&remark=" +remark + "&file_status=" + filetype + "&devicetype_id=" + parent.document.frm.devicetype_id.value 
		+ "&area_id=" + parent.document.frm.area_id.value + "&tablename=tab_vercon_file";
	}
	document.frm.submit();
}

function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
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
<INPUT TYPE="file" NAME="file1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="button" value=" 上 传 " onclick="doUpload()" class=jianbian>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
</FORM>
</BODY>
</HTML>
