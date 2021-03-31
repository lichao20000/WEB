<%--
Note		: 子表达式定义 添加、修改、删除
Date		: 2006-2-17
Author		: shenkejian
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.webtopo.warn.WarnVoiceManager"%>

<%
request.setCharacterEncoding("GBK");
//调用接口，返回Flag的值,1操作成功，-1操作失败;-100:表达式已经存在

WarnVoiceManager manager = new WarnVoiceManager(request);
int flag = manager.WarnVoiceSave();
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall=<%=flag%>;
//-->
</SCRIPT>
