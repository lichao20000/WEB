<%--
Author		: Yan.HaiJian Card NO:5126
Date		: 2006-9-30
Desc		: config flux.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%@ page import ="com.linkage.litms.vipms.flux.FluxPortInfo"%>
<%@ page import ="com.linkage.litms.vipms.flux.PortJudgeAttr"%>
<SCRIPT LANGUAGE="JavaScript">
<%
try{
request.setCharacterEncoding("GBK");
ManagerFluxConfig mfc=new ManagerFluxConfig(request);
String action = request.getParameter("action");
String type= request.getParameter("type");
String device_id = request.getParameter("device_id");
if(null!=action&&"delete".equals(action))
{
	int resultCode = mfc.deleteDeviceFluxConfig();	
	if(0==resultCode)
	{
		out.println("alert(\"ȡ���������óɹ���\");");
	}
	else if(-1==resultCode)
	{
		out.println("alert(\"��������ɾ��ʧ�ܣ�\");");
	}
	else if(-2==resultCode)
	{
		out.println("alert(\"���ݿ�ɾ��ʧ�ܣ�\");");
	}
	else
	{
		out.println("alert(\"֪ͨ��̨ʧ�ܣ�\");");
	}
	out.println("this.location.href=\"webtop_ConfigFlux.jsp?device_id="+device_id+"&type="+type+"\";");
}
else
{
	//mfc.setSerial();
	int resultCode=mfc.saveDeviceOperator();
	if(0==resultCode)
	{
		out.println("window.alert(\"�豸�����������!\");");
		out.println("parent.window.close();");
	}
	else
	{
		out.println("alert(\"�豸û��������֤���\");");
	}	
}
}
catch(Exception e)
{
	e.printStackTrace();
}
%>
</SCRIPT>	




