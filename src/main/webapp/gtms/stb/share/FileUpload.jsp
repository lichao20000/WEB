<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkForm(){
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("��ѡ���ļ�!");
		return false;
	}
	if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3) && "xls"!= myFile.substr(myFile.length-3,3))){
		alert("�밴��ע�������ϴ��ļ���");
		return false;
	}
	
	return true;
}

</script>

<body>
<s:form action ="fileUpload" method ="POST" enctype ="multipart/form-data" onsubmit="return checkForm();">
	<s:file name ="myFile" size="40" label ="Image File"/> 
 	<s:submit name="uploadbtn" value=" �� �� " cssClass="jianbian"/> 
</s:form >
</body>
