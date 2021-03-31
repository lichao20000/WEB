<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
String hostIP = request.getParameter("hostip");
String gather_id = request.getParameter("gather_id");

Cursor cursor = null;
Map fields = null;
String maxTime = "";
if (!"".equals(hostIP) && !"-1".equals(hostIP) && !"undefined".equals(hostIP))
{
	String sql="select max(gettime) as maxtime from tab_performance where hostip='"
			+ hostIP + "'";
	System.out.println(sql);
	cursor = DataSetBean.getCursor(sql);
	fields = cursor.getNext();
	if (fields != null){
		maxTime = (String)fields.get("maxtime");
	}

	sql = "select a.*,b.warmvalue from tab_performance a left join tab_attrwarconf b "
			+ "on a.gather_id=b.gather_id and a.hostip=b.hostip and a.attr_id=b.attr_id "
			+"where a.hostip='"+ hostIP + "' and a.gettime=" + maxTime 
			+ " and a.gather_id='" + gather_id + "' order by a.attr_id ";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select a.attr_id, a.value1, a.value2, a.instance, b.warmvalue " +
				"from tab_performance a left join tab_attrwarconf b "
				+ "on a.gather_id=b.gather_id and a.hostip=b.hostip and a.attr_id=b.attr_id "
				+"where a.hostip='"+ hostIP + "' and a.gettime=" + maxTime
				+ " and a.gather_id='" + gather_id + "' order by a.attr_id ";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	System.out.println(sql);
	cursor = DataSetBean.getCursor(sql);
	fields = cursor.getNext();
}

//增加自动刷新功能
String str_polltime = request.getParameter("polltime");
if(str_polltime==null)str_polltime="5";
int polltime = Integer.parseInt(str_polltime) * 60;
out.println("<meta http-equiv=\"refresh\" content=\""+ polltime + "\">");
%>


<HTML>
<HEAD>
<TITLE>Alarm XML</TITLE>
</HEAD>

<BODY>
<%@ include file="../head.jsp"%>
	<table border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#000000>
		<% if (fields == null){%>
		<tr bgcolor="#FFFFFF"><td class=column>当前没有性能监控数据！</td></tr>
		<%} %>
		<%while (fields != null){ 
			String attr_id = (String)fields.get("attr_id");
			String warmValue = (String)fields.get("warmvalue");
			float warn = 100;
			float cur = 0;
			if (warmValue != null && !"".equals(warmValue)){
				warn = Float.parseFloat(warmValue);
			}
			if ("1".equals(attr_id)){ 
				cur = Float.parseFloat((String)fields.get("value1"));
			%>
				<tr bgcolor="#FFFFFF">
				<td class=column colspan="2">CPU</td>
				<td class=column>使用率</td>
				<%if (cur > warn) {%>
				<td colspan="2"><font color=red><%=(String)fields.get("value1")%>%</font></td>
				<%}
				else{ %>
				<td colspan="2"><%=(String)fields.get("value1")%>%</td>
				<%} %>
				<td class=column>空闲率</td>
				<td colspan="2"><%=(String)fields.get("value2")%>%</td>
			<%}
			else if ("2".equals(attr_id)){ 
				String total = (String)fields.get("value1");
				String free = (String)fields.get("value2");
				String warmFlag = "0";
				
				cur = 100 - (Float.parseFloat(free)*100)/Float.parseFloat(total);				
				String curStr = "";//StringUtils.formatNumber(String.valueOf(cur),2);
				if(!Float.isNaN(cur)){
					curStr = StringUtils.formatNumber(String.valueOf(cur),2);
				}
				if (!Float.isNaN(cur) && cur > warn){					
					warmFlag = "1";
				}%>
				<tr bgcolor="#FFFFFF">
				<td class=column colspan="2">内存</td>
				<td class=column>总大小（M）</td>
				<td><%=(int)Float.parseFloat(total)/1000%></td>
				<td class=column>空闲大小（M）</td>
				<td><%=(int)Float.parseFloat(free)/1000%></td>
				<td class=column>使用率</td>
				<%if ("1".equals(warmFlag)){ %>
				<td><font color=red><%=Float.isNaN(cur)?"0.00":curStr%>%</font></td>
				<%}
				else{ %>
				<td><%=Float.isNaN(cur)?"0.00":curStr%>%</td>
				<%} %>
			<%}
			else if ("3".equals(attr_id)){ 
				String total = (String)fields.get("value1");
				String free = (String)fields.get("value2");
				String warmFlag = "0";
				
				cur = (Float.parseFloat(free)*100)/Float.parseFloat(total);	
				String curStr = "";
				if(!Float.isNaN(cur)){
					curStr = StringUtils.formatNumber(String.valueOf(cur),2);
				}
				if (!Float.isNaN(cur) && cur > warn){					
					warmFlag = "1";
				}%>
				<tr bgcolor="#FFFFFF">
				<td class=column width="12%">文件系统</td>
				<td class=column width="12%"><%=fields.get("instance")%></td>
				<td class=column width="12%">总大小（K）</td>
				<td width="12%"><%=total%></td>
				<td class=column width="12%">使用大小（K）</td>
				<td width="12%"><%=free%></td>
				<td class=column width="12%">使用率</td>
				<%if ("1".equals(warmFlag)){ %>
				<td width="12%"><font color=red><%=Float.isNaN(cur)?"0.00":curStr%>%</font></td>
				<%}
				else{ %>
				<td width="12%"><%=Float.isNaN(cur)?"0.00":curStr%>%</td>
				<%} %>
			<%}
			else if ("4".equals(attr_id)){ 
				String status = "down";
				if ("1.00".equals((String)fields.get("value1"))||"1".equals((String)fields.get("value1"))||"1.0".equals((String)fields.get("value1"))){
					status = "<font color=green>up</font>";
				}
				else{
					status = "<font color=red>down</font>";
				}
				%>
				<tr bgcolor="#FFFFFF">
				<td class=column>进程</td>
				<td class=column colspan="2"><%=fields.get("instance")%>状态</td>
				<td colspan="5"><%=status%></td>
			<%} else if ("5".equals(attr_id)){ 
				String status = "down";
				if ("1.00".equals((String)fields.get("value1"))){
					status = "<font color=green>up</font>";
				}
				else{
					status = "<font color=red>down</font>";
				}
				%>
				<tr bgcolor="#FFFFFF">
				<td class=column>进程并发数</td>
				<td class=column colspan="2"><%=fields.get("instance")%></td>
				<td colspan="5"><%=fields.get("value1")%></td>
			<%} %>
				<%
			fields = cursor.getNext();
		} %>
			</tr>
	</table>
</BODY>
</HTML>
<script type="text/javascript">
<!--
parent.closeMsgDlg();
//-->
</script>