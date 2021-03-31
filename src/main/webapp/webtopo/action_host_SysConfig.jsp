<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.host.*" %>

<%
	SysConfAct sca = new SysConfAct();
	sca.setRequest(request);
	int retflag = sca.UpdateSysConf();	
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>