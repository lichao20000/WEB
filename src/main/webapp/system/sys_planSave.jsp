<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>
<%
request.setCharacterEncoding("GBK");

String strAction = request.getParameter("action");


int strMsg = systemMaintenance.insertSysPlan(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	strMsg = <%=strMsg%>;
	var action = '<%=strAction%>';
	if (strMsg > 0) {
		if(action=="add") {
			alert("���������ɹ�");
			parent.reload();
		}
		if(action=="update") {
			alert("���¼�¼�ɹ�!");
		}
		if(action=="delete") {
			alert("ɾ�������ɹ�");
			parent.reload();
		}
	} else {
		if(action=="add") {
			alert("������¼ʧ��,��ȷ�����²�����");
		}
		if(action=="update") {
			alert("���¼�¼ʧ��,��ȷ�����²�����");
		}
		if(action=="delete") {
			alert("ɾ������ʧ��,��ȷ�����²�����");
		}
	}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<%@ include file="../foot.jsp"%>