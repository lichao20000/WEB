<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="ParameterAct" scope="request" class="com.linkage.litms.webtopo.ParameterAct"/>
<%
boolean flag = ParameterAct.setParamChangeAct(request);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doAct(flag){
	var msg = "上报" + (flag == true ? "成功！" : "失败，请重试!");
	alert(msg);
	parent.ClearProcess();
}
doAct(<%=flag%>);
//-->
</SCRIPT>
<%
ParameterAct = null;
%>