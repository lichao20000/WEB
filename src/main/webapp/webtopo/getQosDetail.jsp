<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import = "com.linkage.litms.webtopo.QOSConfigInfoAct"%>
<%
request.setCharacterEncoding("GBK");
String str_device_id = request.getParameter("device_id");
String str_qosindex = request.getParameter("qosindex");

String strCmd = "";
QOSConfigInfoAct qosConfig = new QOSConfigInfoAct(request);
// �����״�ṹ�����ַ���
strCmd = qosConfig.getQOSDetail();


if(strCmd.equals("<br>"))
	strCmd = "��ȡ�豸QOS����ָ�����������������Ա��ϵ��";
%>
<script language="JavaScript">
<!--
	alert("<%=strCmd%>");
//-->
</script>