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
	content = content.replaceAll("1", "启用");
}
if (content.equals("0")) {
	content = content.replaceAll("0", "暂停");
}
String content11 = (String) fields.get("event_level_id");
EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
Map warnMap = eventLevelLefDAO.getWarnLevel();
String content1 = (String)warnMap.get(content11);
	//if (content1.equals("1")) {
	//	content1 = content1.replaceAll("1", "正常日志");
	//}
	//if (content1.equals("2")) {
	//	content1 = content1.replaceAll("2", "提示告警");
	//}
	//if (content1.equals("3")) {
	//	content1 = content1.replaceAll("3", "一般告警");
	//}
	//if (content1.equals("4")) {
	//	content1 = content1.replaceAll("4", "严重告警");
	//}
	//if (content1.equals("5")) {
	//	content1 = content1.replaceAll("5", "紧急告警");
	//}		

String content2 = (String) fields.get("creatorname");
if (content2.equals("-1")) {
	content2 = content2.replaceAll("-1", "请选择");
}
if (content2.equals("hostman")) {
	content2 = content2.replaceAll("hostman", "主机及应用告警");
}
if (content2.equals("pmee")) {
	content2 = content2.replaceAll("pmee", "网络性能告警");
}
if (content2.equals("Trap Probe 1")) {
	content2 = content2.replaceAll("Trap Probe 1", "SnmpTrap告警");
}
if (content2.equals("ServiceAgent")) {
	content2 = content2.replaceAll("ServiceAgent", "业务模拟测试");
}
if (content2.equals("ipcheck")) {
	content2 = content2.replaceAll("ipcheck", "Ping检测设备通断系统");
}
if (content2.equals("visualman")) {
	content2 = content2.replaceAll("visualman", "visualman网络流量告警");
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
					<TH colspan="4" align="center">显示告警排重信息</TH>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">规则名称</TD>
					<TD><%=(String)fields.get("rule_name")%></TD>
					<TD class=column align="center">告警创建者</TD>
					<TD><%=content2%></TD>
				</TR>				
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">事件类型</TD>
					<TD><%=str_DEvice%></TD>
					<TD class=column align="center">排重持续时间</TD>
					<TD><%=(String)fields.get("elapse_time")%></TD>
				</TR>							
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">告警提升级别</TD>
					<TD><%=content1%></TD>
					<TD class=column align="center">告警详细描述</TD>
					<TD><%=(String)fields.get("fault_desc")%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">是否启用</TD>
					<TD><%=content%></TD>
					<TD class=column align="center">规则定义者</TD>
					<TD><%=(String)fields.get("rule_user")%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">规则定义时间</TD>
					<TD><%=(String)fields.get("def_time")%></TD>
					<TD class=column align="center">告警次数</TD>
						<TD><%=(String)fields.get("repeat_count")%></TD>					
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="center">告警源IP</TD>
					<TD colspan="3"><%=(String)fields.get("device_ip")%></TD>					
				</TR>
				<TR>
					<TD colspan="4" align="center" class=foot>
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
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