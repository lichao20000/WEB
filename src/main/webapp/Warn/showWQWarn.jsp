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
<TITLE> ��<%=str_serialno%>�� ������Ϣ</TITLE>
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
						<TH colspan=4>��<%=str_serialno%>��������Ϣ</TH>
					</TR>
					<%
					DateTimeUtil timeUtil = null;
					String str_creatortype="";
					String _creatortype = "";
					String[] arr_warnlevel = new String[6];
					arr_warnlevel[0] = "����澯";
					arr_warnlevel[1] = "�¼��澯";
					arr_warnlevel[2] = "����澯";
					arr_warnlevel[3] = "��Ҫ�澯";
					arr_warnlevel[4] = "��Ҫ�澯";
					arr_warnlevel[5] = "���ظ澯";
					
					String[] arr_createtime;
					
					
					switch(Integer.parseInt((String)fields.get("CREATORTYPE".toLowerCase()))){
						case 1:
							str_creatortype = "�����澯";
							break;
						case 2:
							str_creatortype = "PMEE�澯";
							break;
						case 3:
							str_creatortype = "Syslog�澯";
							break;
						case 4:
							str_creatortype = "Trap�澯";
							break;
						case 6:
							str_creatortype = "Topo�澯";
							break;	
						case 5:
							str_creatortype = "��������";
							break;
						case 7:
							str_creatortype = "ҵ��澯";
							break;
						case 8:
							str_creatortype = "Ping����豸ͨ��";
							break;
						case 9:
							str_creatortype = "��Ϊ�豸�˿ڼ��";
							break;
						case 10:
							str_creatortype = "Visualman";
							break;
						case 20:
							str_creatortype = "���Ÿ澯";
							break;
						case 21:
							str_creatortype = "���Ÿ澯";
							break;
						default:
							str_creatortype = "δ֪�澯";
					}
					
				//	arr_createtime = StringUtils.secondToDateStr(Integer.parseInt((String)fields.get("CREATETIME".toLowerCase())));
					timeUtil = new DateTimeUtil(Long.parseLong((String)fields.get("CREATETIME".toLowerCase()))*1000);
					%>
					<TR>
					  <TD class=column1 width=120>�¼����</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SERIALNO".toLowerCase())%></TD>
					  <TD class=column1 width=120>����������</TD>
					  <TD bgcolor=#ffffff><%=str_creatortype%></TD>
					</TR>
					<TR>
					  <TD class=column1>����������</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("CREATORNAME".toLowerCase())%></TD>
					  <TD class=column1>����ʱ��</TD>
					  <TD bgcolor=#ffffff><%= timeUtil.getLongDate() %></TD>
					</TR>
					<TR>
					  <TD class=column1>��������</TD>
					  <TD bgcolor=#ffffff colspan=3><%=(String)fields.get("DISPLAYTITLE".toLowerCase())%></TD>
					</TR>
					<TR>
					  <TD class=column1>��������</TD>
					  <TD bgcolor=#ffffff colspan=3><%=(String)fields.get("DISPLAYSTRING".toLowerCase())%></TD>
					</TR>
<!-- 					<TR>
					  <TD class=column1>�豸����</TD>
					  <TD bgcolor=#ffffff><%//=(String)fields.get("DEVICETYPE")%></TD>
					  <TD class=column1>�豸����</TD>
					  <TD bgcolor=#ffffff><%//=(String)fields.get("DEVICECOMPANY")%></TD>
					</TR> -->
					<TR>
					  <TD class=column1>�豸�ͺ�</TD>
					  <TD bgcolor=#ffffff><%=device_model%></TD>
					  <TD class=column1>�豸����</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SOURCENAME".toLowerCase())%></TD>
					</TR>
					<TR>
					  <TD class=column1>�豸Ip</TD>
					  <TD bgcolor=#ffffff><%=(String)fields.get("SOURCEIP".toLowerCase())%></TD>
					  <TD class=column1>�澯�ȼ�</TD>
					  <TD bgcolor=#ffffff><%=arr_warnlevel[Integer.parseInt((String)fields.get("SEVERITY".toLowerCase()))]%></TD>
					</TR>
					<TR><TD class=foot colspan=4 align=right>
						<INPUT TYPE="button" value="�� ��" onclick="javascript:window.close();" class=btn>
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

