<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.webtopo.IpBrowserAct"%>
<SCRIPT LANGUAGE="JavaScript">
<%
	IpBrowserAct act = new IpBrowserAct();
	int result =act.addDevices(request);
	if(result==0)
	{
		out.println("alert(\"�������˳ɹ���\");");
	}
	else if(result==-1)
	{
		out.println("alert(\"���ݿ����ʧ��!\");");
	}
	else if(result==-2)
	{
		out.println("alert(\"��������!\");");
	}
	else if(result==-3)
	{
		out.println("alert(\"֪ͨ��̨ʧ��\");");
	}
	else if(result==-5)
	{
		out.println("alert(\"�豸�Ѵ���ϵͳ�л�ϵͳ��֧�ִ��豸\");");
	}
	else
	{
		out.println("alert(\"��̨����ʧ��\");");
	}
%>
</script>