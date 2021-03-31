<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkForm(){
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("请选择文件!");
		return false;
	}
	if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3) && "xls"!= myFile.substr(myFile.length-3,3))){
		alert("请按照注意事项上传文件！");
		return false;
	}
	
	return true;
}

</script>

<body>
<s:form action ="fileUpload" method ="POST" enctype ="multipart/form-data" onsubmit="return checkForm();">
	<s:file name ="myFile" size="40" label ="Image File"/> 
 	<s:submit name="uploadbtn" value=" 上 传 " cssClass="jianbian"/> 
</s:form >
</body>
