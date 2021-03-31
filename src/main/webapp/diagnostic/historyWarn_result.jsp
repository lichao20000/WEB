<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.linkage.litms.common.util.CommonMap" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil" %>
<%@ page import="com.linkage.litms.common.database.QueryPage" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
String gather_id = request.getParameter("gather_id");
String start_time = request.getParameter("hidstart");
String end_time = request.getParameter("hidend");

Map eventMap = new  HashMap();
eventMap.put("0 BOOTSTRAP","首次连接");
eventMap.put("1 BOOT","设备启动");
eventMap.put("2 PERIODIC","周期通知");
eventMap.put("3 SCHEDULED","定时通知");
eventMap.put("4 VALUE CHANGE","参数变化");
eventMap.put("6 CONNECTION REQUEST","连接请求");
eventMap.put("7 TRANSFER COMPLETE","传输完成");
eventMap.put("8 DIAGNOSTICS COMPLETE","诊断完成");
eventMap.put("X CT-COM ACCOUNTCHANGE","配置维护帐号");
eventMap.put("M Reboot","设备重启");
eventMap.put("M Upload","上传文件");
eventMap.put("M Download","下载配置");
eventMap.put("M DeleteObject","删除实例");
eventMap.put("M SetParameterValues","参数配置");

DateTimeUtil dateTime = new DateTimeUtil(start_time);
long s_time = dateTime.getLongTime();
dateTime = new DateTimeUtil(end_time);
long e_time = dateTime.getLongTime();
        
String vendor_id = request.getParameter("vendor_id");
String device_serialnumber = request.getParameter("device_serialnumber");

String search = "&hidstart=" + start_time + "&hidend=" + end_time;

String sql = "select a.*, b.gather_id from tab_event a, tab_gw_device b where a.time > " 
			+ s_time + " and a.time <" + e_time
			+ " and a.device_id = b.device_id";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select a.oui, a.device_serialnumber, a.time, a.eventcode, a.commandkey, a.descr, b.gather_id" +
			" from tab_event a, tab_gw_device b where a.time > "
			+ s_time + " and a.time <" + e_time
			+ " and a.device_id = b.device_id";
}

if ((gather_id != null) &&(!"-1".equals(gather_id))){
	sql += " and b.gather_id = '" + gather_id + "'";
	search += "&gather_id=" + gather_id;
}

if ((vendor_id != null) &&(!"-1".equals(vendor_id))){
	sql += " and a.oui = '" + vendor_id + "'";
	search += "&vendor_id=" + vendor_id;
}

if ((device_serialnumber != null) &&(!"".equals(device_serialnumber))){
	if(device_serialnumber.length()>5){
		sql += " and b.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
	}
	sql += " and a.device_serialnumber like '%" + device_serialnumber + "'";
	search += "&device_serialnumber=" + device_serialnumber;
	
}

//Cursor cursor1 = DataSetBean.getCursor(sql);
//int total = cursor1.getRecordSize();


				
String stroffset = request.getParameter("offset");
int offset=1;
int pagelen = 50;
if (stroffset != null){
	offset = Integer.parseInt(stroffset);
}

if (stroffset == null)
	offset = 1;
else
	offset = Integer.parseInt(stroffset);

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
QueryPage qryp = new QueryPage();
qryp.initPage(sql, offset, pagelen);

String strBar = qryp.getPageBar(search);

Cursor cursor = DataSetBean.getCursor(sql,offset,pagelen);
Map fields = cursor.getNext();


Map vendorMap = CommonMap.getVendorMap();

%>

<%@ include file="../head.jsp"%>
<form name="frm" action="./historyWarn_result.jsp" method="POST">
	<input type="hidden" name="gather_id" value="<%=gather_id%>">
	<input type="hidden" name="hidstart" value="<%=start_time%>">
	<input type="hidden" name="hidend" value="<%=end_time%>">
	<input type="hidden" name="vendor_id" value="<%=vendor_id%>">
	<input type="hidden" name="device_serialnumber" value="<%=device_serialnumber%>">
	<input type="hidden" name="offset" value="1">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							事件管理
						</TD>
						<td><img src="../images/attention_2.gif" width="15" height="12">事件历史告警列表</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<tr>
						<TH colspan="6">历史查询</TH>
					</tr>

					<tr>
						<TD class="green_title2">厂商OUI</TD>
						<TD class="green_title2">设备序列号</TD>
						<TD class="green_title2">告警时间</TD>
						<TD class="green_title2">告警代码</TD>
						<TD class="green_title2">关键字</TD>
						<TD class="green_title2">描述</TD>
					</tr>
					<%
					if (fields != null){
					while (fields != null){ %>
					<tr bgcolor=#ffffff>
						<td width="15%" class=column align=left nowrap><%=vendorMap.get(fields.get("oui"))+"("+fields.get("oui")+")"%></td>
						<td width="15%" class=column align=left nowrap><%=fields.get("device_serialnumber")%></td>
						<td width="15%" class=column align=left nowrap><%=(new DateTimeUtil(Long.parseLong((String)fields.get("time"))*1000)).getLongDate()%></td>
						<td width="15%" class=column align=left nowrap><%=eventMap.get(fields.get("eventcode"))%></td>
						<td width="15%" class=column align=left nowrap><%=fields.get("commandkey")%></td>
						<td width="15%" class=column align=left nowrap><textarea rows="2" cols="30" class="input-textarea"><%=fields.get("descr")%></textarea></td>
					</tr>
					<%
					fields = cursor.getNext();
					}
					%>
					<tr>
						<TD class="green_title2">厂商OUI</TD>
						<TD class="green_title2">设备序列号</TD>
						<TD class="green_title2">告警时间</TD>
						<TD class="green_title2">告警代码</TD>
						<TD class="green_title2">关键字</TD>
						<TD class="green_title2">描述</TD>
					</tr>
					<%
					}else{%>
					<tr><td colspan="6" class=column>没有查询到数据</td></tr>
					<%} %>
					<tr>
						<td colspan="6" align=right class=green_foot><%=strBar %></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
<BR><BR>
<%@ include file="../foot.jsp"%>

