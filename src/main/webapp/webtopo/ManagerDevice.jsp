<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager"%>

<%
	request.setCharacterEncoding("GBK");
	String type=request.getParameter("type");
	String objs=request.getParameter("objs");
	MCControlManager mc=new MCControlManager(user.getAccount(),user.getPasswd());
	String flag="0";
	if(mc.ManageObjects(objs,type))
	{
		flag="1";
	}

%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<%
	out.println("<script language='javascript'>");
	if(flag.compareTo("0")==0)
	{
		String msg="�豸����";
		if(type.compareTo("0")==0)
		{
			msg="�豸ȡ������";
		}
		out.println("alert('"+msg+"ʧ��');");
	}
	else
	{
		out.println("parent.ManagerDevice('"+objs+"','"+type+"');");
	
	}

	out.println("</script>");
%>
</head>
<body>

</body>
</html>