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
	
	if("0" == excuteResult){
		alert("�ļ��ϴ�ʧ��");
	}else if("4" == excuteResult){
		alert("���д���ͬ���ļ�");
		}else if("3" == excuteResult){
		alert("��Ϣ���ʧ��");
		}else if("5" == excuteResult){
		alert("�ϴ��ļ�ӦС��200M!");
		}else if("2" == excuteResult){
		alert("�ļ�ά���ɹ�");
		parent.document.getElementById("addTable").style.display="none";
	  }else if("1" == excuteResult){
		alert("���³ɹ�");
		}else{
			alert("����ʧ��");
		}
	parent.dyniframesize();
});

</script>
</head>

</html>