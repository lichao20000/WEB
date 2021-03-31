<%//@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String ip = request.getParameter("ip"); 
String disp_type = request.getParameter("disp_type");
if(disp_type == null) disp_type="1";
int iType = Integer.parseInt(disp_type);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>[<%=ip%>] 设备详细信息</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="webtopo.css" TYPE="text/css">
</HEAD>

<BODY>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<!-- 主程序 start-->
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR><TD>
		<TABLE width="500" border=0 cellspacing=0 cellpadding=0>
		  <TR height=30>	
			<TD class="<%=((iType==2)?"curendtab":"endtab")%>"><a href="?disp_type=2" class="tab_A">日报表</a></TD>
			<TD class="<%=((iType==3)?"curendtab":"endtab")%>"><a href="?disp_type=3" class="tab_A">周报表</a></TD>
			<TD class="<%=((iType==4)?"curendtab":"endtab")%>"><a href="?disp_type=4" class="tab_A">月报表</a></TD>
			<TD class="<%=((iType==5)?"curendtab":"endtab")%>"><a href="?disp_type=5" class="tab_A">阶段报表</a></TD>
			<TD class="<%=((iType==6)?"curendtab":"endtab")%>"><a href="?disp_type=6" class="tab_A">年报表</a></TD>
		  </TR>
		</TABLE>
	  </TD></TR>
	  <TR><TD bgcolor=#999999 id="idData">
	    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
		  <TR bgcolor="#FFFFFF" height=22><TD>正在获取数据......</TD></TR>
		</TABLE>
	  </TD></TR>
	</TABLE>
	<!-- 主程序 end-->
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</BODY>
</HTML>
