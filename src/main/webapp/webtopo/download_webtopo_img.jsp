<%--
FileName	: download_webtopo_img.jsp
Author		: shenkj
Date		: 2007-2-7
Note		: ��������ͼΪͼƬ
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.webtopo.TopoGraphics"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("application/x-msdownload");
//String pid = request.getParameter("pid");
String str_filename=null,str_Msg=null,str_gather_id=null;
//���ýӿڣ�ʵ������ͼƬ����Ϊjpg��ʽ
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
		    	<TH align="center" colspan="2">��������ͼΪͼƬ</TH>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF width="10%">ϵͳ��ʾ��</TD>
		    	<TD bgcolor=#FFFFFF><%=str_Msg%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF width="10%">�ɼ�����</TD>
		    	<TD bgcolor=#FFFFFF><%=str_gather_id%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF>ͼƬ���ƣ�</TD>
		    	<TD bgcolor=#FFFFFF><%=str_filename%></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF>��&nbsp;&nbsp;&nbsp;&nbsp;����</TD>
		    	<TD bgcolor=#FFFFFF><a href="javascript:down('<%=str_filename%>');">��&nbsp;��</a></TD>
		    </TR>
		    <TR>
		    	<TD bgcolor=#FFFFFF colspan="2" align="right"><INPUT class="jianbian" onclick="javascript:window.close();" value="�رո�ҳ��" type="button"></TD>
		    </TR>
		    </TABLE>
		 </TD>
	   </TR>
	</TABLE>
	</TD>
</TR>
</TABLE>