<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String[] pNames = new String[2];
pNames[0] = "SnmpConf";
pNames[1] = "adsl";
String[] pDescs = new String[2];
pDescs[0] = "Snmp 配置后台";
pDescs[1] = "Adsl 工单后台";

lookProcess lkProcess = new lookProcess();
Vector list = lkProcess.getProcessStatus(pNames);

String[] tmp = new String[2];
String strClr;
String strData = "";
for(int i=0;i<list.size();i++){
	if((i%2)==0) strClr="#e7e7e7";
	else strClr = "#FFFFFF";

	tmp = (String[])list.get(i);

	strData += "<TR bgcolor='"+strClr+"'>";
	strData += "<TD>"+ tmp[0] + "</TD>";
	if(tmp[1].equals("-1")){
		strData += "<TD>&nbsp;</TD>";
		strData += "<TD><font color=red><b>down</b></font></TD>";	
	}
	else{
		strData += "<TD>"+ tmp[1] + "</TD>";
		strData += "<TD><font color=green><b>up</b></font></TD>";
	}
	strData += "<TD>"+ pDescs[i] + "</TD>";
	strData += "</TR>";
}

%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.lookProcess"%>
<%@page import="java.util.Vector"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Refresh(){
	n = Math.round(Math.random()*1000);
	this.location = "?number="+ n;
}
//-->
</SCRIPT>
 
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="4" >后台进程运行状态&nbsp;&nbsp;<a href="javascript:Refresh();">刷新</a></TD>
					</TR>
					<TR>
						<TH>进程名称</TH>
						<TH>PID</TH>
						<TH>运行状态</TH>
						<TH>进程描述</TH>
					</TR>
					<%out.println(strData);%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
