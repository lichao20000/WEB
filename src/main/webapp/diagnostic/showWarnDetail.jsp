<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.paramConfig.warnAction" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
String id = request.getParameter("id");
String device_id = request.getParameter("device_id");
String time = request.getParameter("time");
String sql = "";


// teledb
if (DBUtil.GetDB() == 3) {
	sql += "select occurtime, cleartime, possiblereason, location, clearsuggestion, description, id, type, mlevel, " +
			"device_id, status from tab_alarm where id = " + id + " and device_id = '" + device_id + "' and occurtime = " + time;
}
else {
	sql += "select * from tab_alarm where id = " + id + " and device_id = '" + device_id + "' and occurtime = " + time;
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);
Map fields = cursor.getNext();

warnAction warnAct = new warnAction();

%>

<%@ include file="../head.jsp"%>
<form name="frm" method="post">
	<table width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							告警详细信息
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<%if (fields != null){ 
					
					String occTime = (new DateTimeUtil(Long.parseLong((String)fields.get("occurtime"))*1000)).getLongDate();
					
					//告警清除时间
					String cleanTime = (String)fields.get("cleartime");
					long cleanTimeLong = 0;
					if (cleanTime != null && !"".equals(cleanTime)){
						cleanTimeLong = Long.parseLong(cleanTime);
					}
					
					if (cleanTimeLong != 0){
						cleanTime = (new DateTimeUtil(cleanTimeLong*1000)).getLongDate();
					}
					else{
						cleanTime = "";
					}
					
					//告警最可能的起因
					String possibleReason = (String)fields.get("possiblereason");
					if (possibleReason==null || "null".equals(possibleReason)){
						possibleReason = "";
					}
					
					//告警定位信息
					String location = (String)fields.get("location");
					if (location==null || "null".equals(location)){
						location = "";
					}
					
					//修复建议
					String clearSuggestion = (String)fields.get("clearsuggestion");
					if (clearSuggestion==null || "null".equals(clearSuggestion)){
						clearSuggestion = "";
					}
					
					//附加说明
					String description = (String)fields.get("description");
					if (description==null || "null".equals(description)){
						description = "";
					}
					
					%>
					<tr bgcolor=#ffffff>
						<td class=column width="20%" nowrap>告警名称</td>
						<td width="30%"><%=warnAct.getWarnName(fields.get("id")) %></td>
						<td class=column width="20%" nowrap>告警类型</td>
						<td width="30%"><%=warnAct.getWarnType(fields.get("type")) %></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column nowrap>告警级别</td>
						<td><%=warnAct.getWarnLevel(fields.get("mlevel")) %></td>
						<td class=column nowrap>告警设备编号</td>
						<td><%=fields.get("device_id") %></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column nowrap>告警状态</td>
						<td><%=warnAct.getWarnStatus(fields.get("status")) %></td>
						<td class=column nowrap>告警最可能的起因</td>
						<td><%=possibleReason %></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column nowrap>告警定位信息</td>
						<td><%=location %></td>
						<td class=column nowrap>告警发生时间</td>
						<td><%=occTime %></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column nowrap>告警清除时间</td>
						<td><%=cleanTime %></td>
						<td class=column nowrap>修复建议</td>
						<td><%=clearSuggestion %></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column nowrap>附加说明</td>
						<td colspan="3"><%=description %></td>
					</tr>
					<%}
					else{ %>
					<tr bgcolor=#ffffff>
						<td colspan="4" width="100%" nowrap>没有对应的告警信息</td>
					</tr>
					<%} %>
					<tr bgcolor=#ffffff>
						<td class=column colspan="4" align="center"><input type="button" value="关闭" onclick="window.close()"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
<%@ include file="../foot.jsp"%>