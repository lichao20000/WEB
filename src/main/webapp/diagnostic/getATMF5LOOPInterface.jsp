<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.*,java.util.*"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
//�����豸 �����ݿ��в�ѯ��òɼ���
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
FileSevice fileService = new FileSevice();
String strList = fileService.getATMF5LOOPInterfaceListBox(device_id);
fileService = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setPingInterface){
	var _strList = "<%=strList%>";
	if(_strList == ""){
		alert("��ȡʧ��,�豸��æ���߷���δ֪���Ӵ���!");
	} else {
		parent.document.all("divATMF5Interface").innerHTML = _strList;
	}
	
}
//-->
</SCRIPT>