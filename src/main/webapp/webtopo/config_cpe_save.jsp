<%--
Note		: �ӱ��ʽ���� ��ӡ��޸ġ�ɾ��
Date		: 2006-2-17
Author		: shenkejian
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.vipms.ConfigCPE"%>
<%
request.setCharacterEncoding("GBK");
//���ýӿڣ�����Flag��ֵ,1�����ɹ���-1����ʧ��;-98:����֪ͨ��̨ʧ��;-99:��ʾ�˿��Ѿ����ˣ�-100���ʽ�Ѿ�����
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
