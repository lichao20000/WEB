<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import = "com.linkage.litms.webtopo.QOSConfigInfoAct"%>
<%
request.setCharacterEncoding("GBK");
//String str_device_id = request.getParameter("device_id");
//String title = request.getParameter("ifindex");


QOSConfigInfoAct qosConfig = new QOSConfigInfoAct(request);
//֪ͨ��̨���²ɼ��豸
qosConfig.I_InformGather();
%>
<script language="JavaScript">
<!--
	alert("��֪ͨ��̨�Դ��豸��QOS������Ϣ�������²ɼ������Ժ���в鿴");
	parent.close();
//-->
</script>
