<%--
FileName	: download_webtopo_img.jsp
Author		: shenkj
Date		: 2007-2-7
Note		: 导出拓扑图为图片
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.webtopo.TopoGraphics"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("application/x-msdownload");
//String pid = request.getParameter("pid");
String str_filename=null,str_Msg=null,str_gather_id=null;
//调用接口，实现拓扑图片保存为jpg格式
TopoGraphics topographics = new TopoGraphics();
ArrayList list  = new ArrayList();
list.clear();
list=topographics.createImage();
str_Msg=String.valueOf(list.get(0)); 
str_gather_id=String.valueOf(list.get(1));
//str_path=String.valueOf(list.get(2));
str_filename=String.valueOf(list.get(3));
%>
<%@page import="java.util.ArrayList"%>
<SCRIPT Language="JavaScript">
<!--//
function down(url){
  	window.location.href="download_img.jsp?filenme="+url;
	return false;
}
//->
</SCRIPT>
<link rel="stylesheet" href="../css/css_blue.css" type="text/css">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR>
	<TD>
	  <TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	   <TR>
		 <TD  bgcolor=#000000>
		    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
		    <TR>
		    	<TH align="center" colspan="2">导出拓扑图为图片</TH>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF width="10%">系统提示：</TD>
		    	<TD bgcolor=#FFFFFF><%=str_Msg%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF width="10%">采集区域：</TD>
		    	<TD bgcolor=#FFFFFF><%=str_gather_id%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF>图片名称：</TD>
		    	<TD bgcolor=#FFFFFF><%=str_filename%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF>操&nbsp;&nbsp;&nbsp;&nbsp;作：</TD>
		    	<TD bgcolor=#FFFFFF><a href="javascript:down('<%=str_filename%>');">保&nbsp;存</a></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF colspan="2" align="right"><INPUT class="jianbian" onclick="javascript:window.close();" value="关闭该页面" type="button"></TD>
		    </TR>
		    </TABLE>
		 </TD>
	   </TR>
	</TABLE>
	</TD>
</TR>
</TABLE>