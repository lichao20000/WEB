<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
String info = request.getParameter("info");
if (info.equals("1")) {
%>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<TR><TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id='myTable'>
				<TR>
					<TD bgcolor=#000000>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
							<TH colspan="4" align="center">������û���Ϣ:<FONT COLOR="red">�˽�����û�Ϊ�����û�!</FONT></TH>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
	</TD></TR>
	</TABLE>
	<script language="JavaScript">
	<!--
	parent.document.all("userinfo").style.display="";
	parent.document.all("userinfo").innerHTML=document.all.myTable.outerHTML;
	//-->
	</script>
<%
} else if (info.equals("2")){
%>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<TR><TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id='myTable'>
				<TR>
					<TD bgcolor=#000000>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
							<TH colspan="4" align="center">�ֹ���·��Ϣ:<FONT COLOR="red">�ڹ�����·ǰ,���ȹ����û���Ϣ!</FONT></TH>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
	</TD></TR>
	</TABLE>
	<script language="JavaScript">
	<!--
	parent.document.all("linkInfo").style.display="";
	parent.document.all("linkInfo").innerHTML=document.all.myTable.outerHTML;
//-->
</script>
<%
}
%>

