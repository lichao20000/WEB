<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
var currPage = "";

function checkForm(){
	$("form[@name='fileUpload']").attr("action","<s:url value='/itms/resource/memfileUpload.action'/>");
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("请选择文件!");
		return false;
	}
	if(currPage == "addorupdate"){
		if((myFile.indexOf(".xls") == -1) && (myFile.indexOf(".doc") == -1) && (myFile.indexOf(".docx") == -1)
				&& (myFile.indexOf(".png") == -1) && (myFile.indexOf(".pdf") == -1) && (myFile.indexOf(".jpg") == -1)){
			alert("附件格式请按照注意事项上传文件！");
			return false;
		}
	}else
		if(currPage == "import"){
			if(myFile.indexOf(".csv") == -1){
				alert("文件格式请按照注意事项上传文件！");
				return false;
			}
		}
	return true;
}

function reloadUrl(){
	if(parent.document.forms[0].gwShare_fileName == undefined || parent.document.forms[0].gwShare_fileName == null)
	{
		parent.document.getElementsByName("gwShare_fileName")[0].value = "";
	}else
	{
		parent.document.forms[0].gwShare_fileName.value = "";
	}
	window.location="FileUpload.jsp";
}

function initPage(page){
	currPage = page;
	$("input[name='myFile']").val("");
	function resetForm(id,basideId){
		  $(':input','#fileUpload').not(':button,:submit,:reset,:hidden').val('').removeAttr('checked').removeAttr('checked');
	}
}
</script>
<head>
<title>Struts 2 File Upload</title>
</head>
<body>
	<s:form name="fileUpload" id="fileUpload" action="" method="POST" enctype="multipart/form-data" onsubmit="return checkForm();">
		<s:file name="myFile" size="40" label="Image File" cssClass="bk" />
		<s:submit value=" 上 传 " cssClass="jianbian" />
	</s:form>
	</TABLE>
	<TR>
		<TD HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
				STYLE="display: none"></IFRAME>
		</TD>
	</TR>
	</TABLE>
</body>
