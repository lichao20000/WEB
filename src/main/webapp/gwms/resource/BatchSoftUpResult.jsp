<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
var excuteResult = "<s:property value='excuteResult' />";

$(function() {
	if("success" == excuteResult){
		alert("���Ƴɹ�");
		parent.document.getElementById("civiButton").style.display="";
	}else if("fail" == excuteResult){
		alert("����ʧ��");
		parent.document.getElementById("civiButton").style.display="";
	}else if("repeat" == excuteResult){
		alert("�������ظ�,����ʧ��!");
		parent.document.getElementById("civiButton").style.display="";
	}else if("updateSuccess" == excuteResult){
		alert("�޸Ĳ����ɹ�");
	}else if("updateFail" == excuteResult){
		alert("�޸Ĳ���ʧ��");
	}
	parent.dyniframesize();
});

</script>
</head>

</html>