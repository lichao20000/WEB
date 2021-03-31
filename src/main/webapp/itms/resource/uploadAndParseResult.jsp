<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
	var excuteResult = "<s:property value='excuteResult' />";

	$(function() {
		var lineData = excuteResult.split("#");
		var type = lineData[0];
		var result = lineData[1];
		if(type == 1){
			parent.document.getElementById("it_ok").innerHTML = result;
			parent.document.getElementById("it_ok").style.display="";
			parent.document.getElementById("it_stips").style.display="none";
		} else {
			parent.document.getElementById("it_stips").innerHTML = result;
			parent.document.getElementById("it_stips").style.display="";
			parent.document.getElementById("it_ok").style.display="none";
		}
		parent.document.getElementById("uploadButton").disabled = true;
		parent.dyniframesize();
	});

</SCRIPT>
</html>