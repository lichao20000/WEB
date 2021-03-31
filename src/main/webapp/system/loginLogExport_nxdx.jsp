<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition","attachment; filename=loginLog.xls" );

	ArrayList logList = LogItem.getInstance().getLoginLogExport(request);
	Cursor cursor = (Cursor) logList.get(0);
	Map fields = cursor.getNext();
	String strData = "";
	if (fields == null) {
    	strData += "<TR align='center'><TD class=column colspan=4 height='20'>没有数据</TD></TR>";
	} 
	else {
		 while(null!=fields)
		 {
			 strData += "<TR align='center'>";
			 strData += "<TD class=column height='20'>" + fields.get("acc_loginname") + "</TD>";
			 strData += "<TD class=column height='20'>" + fields.get("acc_login_ip") + "</TD>";
			 strData += "<TD class=column height='20'>" + new DateTimeUtil(Long.parseLong((String) fields.get("time")) * 1000).getLongDate() + "</TD>";					 
			 strData += "<TD class=column height='20'>" + fields.get("errorlogin_desc") + "</TD>";
			 fields = cursor.getNext();
		 }
		 //clear
		 cursor = null;
		 fields = null;
	}
%>
<HTML>


<HEAD>
<TITLE>登录日志数据导出</TITLE>


<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">

<style>
TD{

  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;

}
</style>
</HEAD>
<BODY>
<TABLE boder=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td align="center"><b>登录失败日志统计</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>操作人</TH>
							<TH>客户端IP</TH>
							<TH>操作时间</TH>
							<TH>失败原因</TH>
						</TR> 
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>	
</TD></TR>
</TABLE>
</BODY>
</HTML>