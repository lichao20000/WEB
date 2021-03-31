<%--
Note		: 子表达式定义 添加、修改、删除
Date		: 2006-2-17
Author		: shenkejian
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.vipms.ConfigCPE"%>
<%
request.setCharacterEncoding("GBK");
//调用接口，返回Flag的值,1操作成功，-1操作失败;-98:代表通知后台失败;-99:表示端口已经用了，-100表达式已经存在
ConfigCPE cpe = new ConfigCPE(request);
int flag = cpe.OperPathID();
String isCPE=cpe.getIsCPE();


%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall=<%=flag%>;
	parent.isCPE="<%=isCPE%>";
//-->
</SCRIPT>
