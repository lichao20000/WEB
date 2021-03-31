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
		alert("定制成功");
		parent.document.getElementById("civiButton").style.display="";
	}else if("fail" == excuteResult){
		alert("定制失败");
		parent.document.getElementById("civiButton").style.display="";
	}else if("repeat" == excuteResult){
		alert("任务名重复,定制失败!");
		parent.document.getElementById("civiButton").style.display="";
	}else if("updateSuccess" == excuteResult){
		alert("修改操作成功");
	}else if("updateFail" == excuteResult){
		alert("修改操作失败");
	}
	parent.dyniframesize();
});

</script>
</head>

</html>