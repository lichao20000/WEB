<%//@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.Scheduler,
					java.util.ArrayList,
					java.io.File,
					com.linkage.litms.LipossGlobals,
					javax.swing.ImageIcon"%>

<%
request.setCharacterEncoding("GBK");
int warn_num=20;
if(LipossGlobals.getLipossProperty("webtopo.WARN_VIEW_NUM")!=null)
{
  warn_num=Integer.parseInt(LipossGlobals.getLipossProperty("webtopo.WARN_VIEW_NUM"));
}

ArrayList imageList=Scheduler.getImageList();

%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<%
out.println("<script language='javascript'>");
out.println("var tmp_arrImgSize = new Object();");
out.println("var arr = new Array(2);");
String iconname="";
String tempicon="";
ImageIcon image=null;
int index_size=0;	
for(int i=0;i<imageList.size();i++)
{
	image=(ImageIcon) imageList.get(i);
	iconname=image.toString();
	
	iconname=iconname.substring(iconname.lastIndexOf(File.separator)+1);
	index_size=iconname.indexOf(".");
	tempicon=iconname.substring(0,index_size)+"_glory."+iconname.substring(index_size+1);
	out.println("arr=new Array(2);");
	out.println("arr[0]="+image.getIconWidth()+";");
	out.println("arr[1]="+image.getIconHeight()+";");
	out.println("tmp_arrImgSize[\""+iconname+"\"]=arr;");
	out.println("tmp_arrImgSize[\""+tempicon+"\"]=arr;");
}

out.println("parent.arrImgSize=tmp_arrImgSize;");//arrImgSize存放ICON大小 定义在topo.js中
//out.println("alert(tmp_arrImgSize);");
//out.println("alert(parent.arrImgSize);");
out.println("parent.userPremession=\""+session.getAttribute("ldims")+"\";");//userPremession 定义在main_control.js中
out.println("parent.initialize("+warn_num+");");//初始化函数 定义在main_control.js中
out.println("</script>");
%>
</head>
<body>

</body>
</html>