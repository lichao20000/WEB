<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%
int resultCode =-2;
try
{
	ManagerFluxConfig mfc = new ManagerFluxConfig(request);
	resultCode =mfc.updateJSDevicePortInfo();
}catch(Exception e)
{ 	
	e.printStackTrace();
}
%>
<SCRIPT LANGUAGE="JavaScript">
<%if(0==resultCode){%>
alert("�޸ĳɹ���");
<%}else if(-1==resultCode){%>
alert("���������޸�ʧ�ܣ�");
<%}else if(-2==resultCode){%>
alert("���ݿ�ɾ��ʧ�ܣ�");
<%}else{%>
alert("֪ͨ��̨ʧ�ܣ�");
<%}%>
</SCRIPT>