<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>
<%
request.setCharacterEncoding("GBK");

String strAction = request.getParameter("action");


int strMsg = systemMaintenance.tabItemAct(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var _msg = '<%=strMsg%>';
	var action = '<%=strAction%>';
	if (_msg > 0) {
		if(action=="add") {
			alert("��Ӳ����ɹ�");
			parent.reload();
		}
/**		if(action=="update") {
			alert("���¼�¼�ɹ�!");
		}
		if(action=="delete") {
			alert("ɾ�������ɹ�");
			parent.reload();
		}
*/
	} else if(_msg < 0){
		if(action=="add") {
			alert("������¼ʧ��,��ȷ�����²�����");
		}
/**		if(action=="update") {
			alert("���¼�¼ʧ��,��ȷ�����²�����");
		}
		if(action=="delete") {
			alert("ɾ������ʧ��,��ȷ�����²�����");
		}
*/
	} else {
		if(action=="add") {
			alert("���Ѿ�����,��ȷ���������룡");
		}
	}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<%@ include file="../foot.jsp"%>