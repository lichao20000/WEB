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
		alert("文件上传失败");
	}else if("4" == excuteResult){
		alert("库中存在同名文件");
		}else if("3" == excuteResult){
		alert("信息入库失败");
		}else if("5" == excuteResult){
		alert("上传文件应小于200M!");
		}else if("2" == excuteResult){
		alert("文件维护成功");
		parent.document.getElementById("addTable").style.display="none";
	  }else if("1" == excuteResult){
		alert("更新成功");
		}else{
			alert("操作失败");
		}
	parent.dyniframesize();
});

</script>
</head>

</html>