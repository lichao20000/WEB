<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%
request.setCharacterEncoding("GBK");
String str_serialno = request.getParameter("serialno");
String str_subserialno = request.getParameter("subserialno");
String tablename = request.getParameter("tablename");
String start=request.getParameter("start");
String end=request.getParameter("end");
String ips=request.getParameter("ip");
String strSQL = "select * FROM "+tablename+" where serialno='"+ str_serialno +"' and subserialno="+str_subserialno+" and createtime >=" + start + " and createtime<="+ end +" and sourceip like '"+ips+"'";

Map fields = (Map)DataSetBean.getRecord(strSQL);

String ip = (fields != null)?((String)fields.get("SOURCEIP".toLowerCase())):"";
Map f = DataSetBean.getRecord("select device_model from tab_deviceresource where loopback_ip='"+ip+"'");
String device_model = "";
if(f!=null){
	device_model = (String)f.get("DEVICE_MODEL".toLowerCase());
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> 〖<%=str_serialno%>〗 报警信息</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
</HEAD>

<BODY scrolling="no">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan=4>〖<%=str_serialno%>〗报警信息</TH>
					</TR>
					<%
					DateTimeUtil timeUtil = null;
					String str_creatortype="";
					String _creatortype = "";
					String[] arr_warnlevel = new String[6];
					arr_warnlevel[0] = "清除告警";
					arr_warnlevel[1] = "事件告警";
					arr_warnlevel[2] = "警告告警";
					arr_warnlevel[3] = "次要告警";
					arr_warnlevel[4] = "主要告警";
					arr_warnlevel[5] = "严重告警";
					
					String[] arr_createtime;
					
					
					switch(Integer.parseInt((String)fields.get("CREATORTYPE".toLowerCase()))){
						case 1:
							str_creatortype = "主机告警";
							break;
						case 2:
							str_creatortype = "PMEE告警";
							break;
						case 3:
							str_creatortype = "Syslog告警";
							break;
						case 4:
							str_creatortype = "Trap告警";
							break;
						case 6:
							str_creatortype = "Topo告警";
							break;	
						case 5:
							str_creatortype = "规则引擎";
							break;
						case 7:
							str_creatortype = "业务告警";
							break;
						case 8:
							str_creatortype = "Ping检测设备通断";
							break;
						case 9:
							str_creatortype = "华为设备端口检查";
							break;
						case 10:
							str_creatortype = "Visualman";
							break;
						case 20:
							str_creatortype = "亚信告警";
							break;
						case 21:
							str_creatortype = "短信告警";
							break;
						default:
							str_creatortype = "未知告警";
					}
					
				//	arr_createtime = StringUtils.secondToDateStr(Integer.parseInt((String)fields.get("CREATETIME".toLowerCase())));
					timeUtil = new DateTimeUtil(Long.parseLong((String)fields.get("CREATETIME".toLowerCase()))*1000);
					%>
					<TR>
					  <TD class=column1 width=120>事件编号</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SERIALNO".toLowerCase())%></TD>
					  <TD class=column1 width=120>创建者类型</TD>
					  <TD bgcolor=#ffffff><%=str_creatortype%></TD>
					</TR>
					<TR>
					  <TD class=column1>创建者名称</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("CREATORNAME".toLowerCase())%></TD>
					  <TD class=column1>创建时间</TD>
					  <TD bgcolor=#ffffff><%= timeUtil.getLongDate() %></TD>
					</TR>
					<TR>
					  <TD class=column1>报警标题</TD>
					  <TD bgcolor=#ffffff colspan=3><%=(String)fields.get("DISPLAYTITLE".toLowerCase())%></TD>
					</TR>
					<TR>
					  <TD class=column1>报警内容</TD>
					  <TD bgcolor=#ffffff colspan=3><%=(String)fields.get("DISPLAYSTRING".toLowerCase())%></TD>
					</TR>
<!-- 					<TR>
					  <TD class=column1>设备类型</TD>
					  <TD bgcolor=#ffffff><%//=(String)fields.get("DEVICETYPE")%></TD>
					  <TD class=column1>设备厂商</TD>
					  <TD bgcolor=#ffffff><%//=(String)fields.get("DEVICECOMPANY")%></TD>
					</TR> -->
					<TR>
					  <TD class=column1>设备型号</TD>
					  <TD bgcolor=#ffffff><%=device_model%></TD>
					  <TD class=column1>设备名称</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SOURCENAME".toLowerCase())%></TD>
					</TR>
					<TR>
					  <TD class=column1>设备Ip</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SOURCEIP".toLowerCase())%></TD>
					  <TD class=column1>告警等级</TD>
					  <TD bgcolor=#ffffff><%=arr_warnlevel[Integer.parseInt((String)fields.get("SEVERITY".toLowerCase()))]%></TD>
					</TR>
					<TR><TD class=foot colspan=4 align=right>
						<INPUT TYPE="button" value="关 闭" onclick="javascript:window.close();" class=btn>
					</TD></TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</BODY>
</HTML>

