<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.*"%>

<%

int flag = SheetManage.changeDevice(request);

%>
<SCRIPT LANGUAGE="JavaScript">
	var _flag = "<%=flag%>";
	if(_flag == 1){
		alert("�豸�������ݿ�����ɹ���");
		parent.goList('jt_Work_handForm.jsp');
	} else {
		alert("�豸�������ݿ����ʧ�ܣ������²�����");
	}
</SCRIPT>

