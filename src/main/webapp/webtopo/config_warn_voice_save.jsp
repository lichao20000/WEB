<%--
Note		: �ӱ��ʽ���� ��ӡ��޸ġ�ɾ��
Date		: 2006-2-17
Author		: shenkejian
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.webtopo.warn.WarnVoiceManager"%>

<%
request.setCharacterEncoding("GBK");
//���ýӿڣ�����Flag��ֵ,1�����ɹ���-1����ʧ��;-100:���ʽ�Ѿ�����

WarnVoiceManager manager = new WarnVoiceManager(request);
int flag = manager.WarnVoiceSave();
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall=<%=flag%>;
//-->
</SCRIPT>
