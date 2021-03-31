<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Map"%>
<%request.setCharacterEncoding("GBK");%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../../../Js/CheckForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function doUpload(){
	var url = $("#versionPath",parent.document).val();
	var url = $("select[@name='versionPath']",parent.document).val();
	if(url=="-1"){
		alert("请选择版本路径");
		$("select[@name='versionPath']",parent.document).focus();
		return false;
	}
	/**
	var serverPort = url.indexOf("/",15);
    var name = url.indexOf("/",serverPort+1);
    var server = url.substring(0,name);
    var lastLen = url.lastIndexOf("/");
    var path = url.substring(lastLen+1,url.length);
	*/
	var index = url.indexOf("FileServer");
    var server = url.substring(0,index+10);
    var path = url.substring(index+11,url.length);
	var filePath = $("input[@type='file']").val();
	var fp = document.frm.file1.value;
	if(fp.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		$("input[@name='file1']").focus();
		return false;
	}
	var filet = "";
    var fileP = "";
    filet = filePath.split(".");
    if(filet.length < 2){
            $("input[@name='file1']").focus();
            return false;
    }
    $("input[@name='upload']").val("正在上传...");
    fileP = filePath.split("\\");
    $("input[@name='fileName']").val(fileP[fileP.length-1]);
    var fileRename = $("input[@name='fileName']").val();
    //文件名与 上传文件名相同
    //var newfileName = $("#newfileName",parent.document).val();
    //var arr = fileRename.split(".");
    //var fileType = arr[1];
    //if(null == newfileName || ""==$.trim(newfileName)){
    //	 fileRename = $("input[@name='fileName']").val();
    //}else{
    //	 fileRename = newfileName+"."+fileType;
    //}
    // $("input[@name='newfileName']",parent.document).val(fileRename);
    
	var urlParameter = server+"/Upload4S.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + fileRename
	var uploadType = $("input[@name='uploadType']").val();
	if(uploadType == '1')
	{
		//上传文件通过WEB中转
		$("input[@name='urlParameter']").val("");
		document.getElementById("urlParameter").value = urlParameter;
		document.frm.action = "<s:url value='/gtms/stb/resource/deviceVersion!uploadFile.action'/>";
	}else
	{
		//地址直达
		document.frm.action = urlParameter;
		
	}
	
	document.frm.submit();
	document.frm.upload.disabled = true;
}
</SCRIPT>
<style>
body {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>

<BODY>
<FORM NAME="frm" METHOD=POST action="" ENCTYPE="multipart/form-data" target="dataForm" >
	<table width="100%">
	<tr>
		<td width="50%">
			<input type="hidden" id="uploadType" name="uploadType" value="1" />
			<input type="hidden" id="fileName" name="fileName" value="" />
			<input type="hidden" id="urlParameter" name="urlParameter" value=""/>
			<INPUT TYPE="file" NAME="file1"  class="bk">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<INPUT TYPE="button" name="upload" id="upload" value=" 上 传 " onclick="doUpload()" class=jianbian>&nbsp;&nbsp;
		</td>
		<td>
			<iframe id="dataForm" name="dataForm" height="25"
						frameborder="0" scrolling="no" width="100%" src="" ></iframe>
		</td>
	</tr>
</table>
</FORM>
</BODY>
</HTML>
