<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkForm(){
	//$("form[@name='fileUpload']").attr("action","/itms/resource/deviceTestAccountACT!fileupAccount.action");
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("��ѡ���ļ�!");
		return false;
	}
	/* if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3) && "xls"!= myFile.substr(myFile.length-3,3)&&"csv"!= myFile.substr(myFile.length-3,3))){
		alert("�밴��ע�������ϴ��ļ���");
		return false;
	} */
	return true;
}


//����ҳ�����
function enableTemplete(enable){
	if(enable){
		$("#excelTemplateA").css("display","");
	}else{
		$("#excelTemplateA").css("display","none");
	}
}

</script>
<head>
<title>Struts 2 File Upload</title>
</head>
<body>
	<s:form name='fileUpload' action="/itms/resource/deviceTestAccountACT!fileupAccount.action" method="POST" enctype="multipart/form-data"
		onsubmit="return checkForm();">
		<s:file name="myFile" size="40" label="Image File" cssClass="bk" />
		<s:submit value=" �� �� " cssClass="jianbian" />
	</s:form>
	<!-- <TR>
		<TD HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
				STYLE="display: none"></IFRAME>
		</TD>
	</TR> -->
	</TABLE>
</body>
