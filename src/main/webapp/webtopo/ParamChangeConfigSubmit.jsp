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
	var msg = "�ϱ�" + (flag == true ? "�ɹ���" : "ʧ�ܣ�������!");
	alert(msg);
	parent.ClearProcess();
}
doAct(<%=flag%>);
//-->
</SCRIPT>
<%
ParameterAct = null;
%>