<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.module.gwms.dao.gw.EventLevelLefDAO" %>
<%
request.setCharacterEncoding("GBK");
String  str_rule_id = request.getParameter("rule_id");

String  str_DEvice = request.getParameter("DEvice");
if (str_rule_id == null)
	response.sendRedirect("../error.jsp?errid=0");
String sql = "select * from fault_filter_multi where rule_id="+str_rule_id+" ";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select is_active, event_level_id, creatorname, rule_name, elapse_time, fault_desc, rule_user, " +
			"def_time, repeat_count, device_ip " +
			" from fault_filter_multi where rule_id="+str_rule_id+" ";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();
String content = (String) fields.get("is_active");
if (content.equals("1")) {
	content = content.replaceAll("1", "����");
}
if (content.equals("0")) {
	content = content.replaceAll("0", "��ͣ");
}
String content11 = (String) fields.get("event_level_id");
EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
Map warnMap = eventLevelLefDAO.getWarnLevel();
String content1 = (String)warnMap.get(content11);
	//if (content1.equals("1")) {
	//	content1 = content1.replaceAll("1", "������־");
	//}
	//if (content1.equals("2")) {
	//	content1 = content1.replaceAll("2", "��ʾ�澯");
	//}
	//if (content1.equals("3")) {
	//	content1 = content1.replaceAll("3", "һ��澯");
	//}
	//if (content1.equals("4")) {
	//	content1 = content1.replaceAll("4", "���ظ澯");
	//}
	//if (content1.equals("5")) {
	//	content1 = content1.replaceAll("5", "�����澯");
	//}		

String content2 = (String) fields.get("creatorname");
if (content2.equals("-1")) {
	content2 = content2.replaceAll("-1", "��ѡ��");
}
if (content2.equals("hostman")) {
	content2 = content2.replaceAll("hostman", "������Ӧ�ø澯");
}
if (content2.equals("pmee")) {
	content2 = content2.replaceAll("pmee", "�������ܸ澯");
}
if (content2.equals("Trap Probe 1")) {
	content2 = content2.replaceAll("Trap Probe 1", "SnmpTrap�澯");
}
if (content2.equals("ServiceAgent")) {
	content2 = content2.replaceAll("ServiceAgent", "ҵ��ģ�����");
}
if (content2.equals("ipcheck")) {
	content2 = content2.replaceAll("ipcheck", "Ping����豸ͨ��ϵͳ");
}
if (content2.equals("visualman")) {
	content2 = content2.replaceAll("visualman", "visualman���������澯");
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="99%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="4" align="center">��ʾ�澯������Ϣ</TH>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">��������</TD>
					<TD><%=(String)fields.get("rule_name")%></TD>
					<TD class=column align="center">�澯������</TD>
					<TD><%=content2%></TD>
				</TR>				
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">�¼�����</TD>
					<TD><%=str_DEvice%></TD>
					<TD class=column align="center">���س���ʱ��</TD>
					<TD><%=(String)fields.get("elapse_time")%></TD>
				</TR>							
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">�澯��������</TD>
					<TD><%=content1%></TD>
					<TD class=column align="center">�澯��ϸ����</TD>
					<TD><%=(String)fields.get("fault_desc")%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">�Ƿ�����</TD>
					<TD><%=content%></TD>
					<TD class=column align="center">��������</TD>
					<TD><%=(String)fields.get("rule_user")%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">������ʱ��</TD>
					<TD><%=(String)fields.get("def_time")%></TD>
					<TD class=column align="center">�澯����</TD>
						<TD><%=(String)fields.get("repeat_count")%></TD>					
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">�澯ԴIP</TD>
					<TD colspan="3"><%=(String)fields.get("device_ip")%></TD>					
				</TR>
				<TR>
					<TD colspan="4" align="center" class=foot>
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../foot.jsp"%>