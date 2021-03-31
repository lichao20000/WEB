<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager"%>

<%
	request.setCharacterEncoding("GBK");
	String action=request.getParameter("action");
	String objs=request.getParameter("objs");
	String parentid=request.getParameter("parent_id");	
	String refresh_id=request.getParameter("refresh_id");
	MCControlManager mc=new MCControlManager(user.getAccount(),user.getPasswd());
	int flag=-1;
	if(action.equals("paste"))
	{
		flag=mc.PasteObjs(parentid,objs);
	}
	else if(action.equals("delete"))
	{
		flag=mc.DelObjs(objs);
		String content="删除设备";		
		String result="删除成功";
		if(0!=flag)
		{
			result="删除失败";
		}
		//转换下字符集，记日志
		try
		{
			content = Encoder.toISO(content);
			result=Encoder.toISO(result);
		}
		catch(Exception e)
		{
			content="delete device";
			result ="success";
		}
		
		if(null!=objs&&!"".equals(objs))
		LogItem.getInstance().writeItemLog(request,1,objs.substring(objs.lastIndexOf("/")+1,objs.length()),content,result);
	}

%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.curPasteObj=null;
	var refresh_id="<%=refresh_id%>";
	parent.document.all("childFrm").src="getChildTopo.jsp?pid="+refresh_id;

//-->
</SCRIPT>
</head>
<body>

</body>
</html>