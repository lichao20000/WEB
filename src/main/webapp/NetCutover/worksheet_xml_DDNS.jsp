<%--
Author		: zhaixf
Date		: 2008-03-17
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import ="com.linkage.litms.common.database.*,com.linkage.litms.netcutover.*,java.util.*" %>
<%
request.setCharacterEncoding("GBK");
String str_lms = request.getParameter("start");
String str_polltime = request.getParameter("polltime");
String str_filter = request.getParameter("filter");

//----------------------属地过滤 add by YYS 2006-9-24 ----------------
String str_ifcontainChild = request.getParameter("ifcontainChild");
String str_city_id = request.getParameter("city_id");
if (str_city_id == null) {
	str_city_id = user.getCityId();
	str_ifcontainChild = "1";
}
//--------------------------------------------------------------------

long lms = Long.parseLong(str_lms);

ObjectWorkSheet objSheet = new ObjectWorkSheet();
objSheet.setWorksheet_status(str_filter);
objSheet.setGather_id(curUser.getUserProcesses());

//查询数据库
long m_AreaId = user.getAreaId();

SheetList sheet = new SheetList(lms, 1, -1, objSheet, m_AreaId);
Cursor cursor = sheet.getDdnsSheetList(str_city_id, str_ifcontainChild);
Map fields = cursor.getNext();
Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();

//判别是否苏州
boolean isSZ = SelectCityFilter.isSZ(curUser.getCityId());
String local_ip = request.getServerName();
%>
<%@page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<HTML>

<BODY>
<%if (str_polltime != null) {
                int polltime = Integer.parseInt(str_polltime) * 60;
                out.println("<meta http-equiv=\"refresh\" content=\""
                        + polltime + "\">");
            }%>
<%@ include file="../head.jsp"%>

<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">工单列表</TD>
	</TR>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>工单编号</th>
				<th width="" nowrap>属地</th>
				<th width="" nowrap>客户名称</th>
				<th width="" nowrap>操作类型</th>
				<th width="" nowrap>域名</th>
				<th width="" nowrap>域名账号</th>
				<th width="" nowrap>域名密码</th>
				<th width="" nowrap>来单时间</th>
				<th nowrap>工单执行结果描述</th>
			</tr>
			<%
            if (fields != null) {
                String tmp;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                while (fields != null) {
					out.println("<tr value = " + fields.get("result_spec") + " bgcolor='#ffffff'>");
                    out.println("<td><nobr>" + fields.get("id")
                            + "</nobr></td>");
                    tmp = (String) fields.get("city_id");
                    out.println("<td><nobr>" + cityMap.get(tmp)
                            + "</nobr></td>");

                    out.println("<td><nobr>" + fields.get("customer_name")
                            + "</nobr></td>");
                    out.println("<td>" + fields.get("type")
                            + "</td>");
					out.println("<td><nobr>"
                            + fields.get("1")
                            + "</nobr></td>");
					out.println("<td><nobr>"
                            + fields.get("1").toString().substring(0, fields.get("1").toString().indexOf("."))
                            + "</nobr></td>");
					out.println("<td><nobr>"
                            + fields.get("2")
                            + "</nobr></td>");
					out.println("<td>"
                            + sdf.format(new Date(new Long((String)fields.get("receive_date"))))
                            + "</td>");
                    out.println("<td><nobr>" 
                    		+ fields.get("result_tail") 
                    		+ "</nobr></td>");
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=9>没有工单记录</td></tr>");
            }
        %>
		</table>
		</TD>
	</TR>
</TABLE>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.idList.innerHTML = idList.innerHTML;
parent.closeMsgDlg();
parent.wsState();

//-->
</SCRIPT>
</BODY>
</HTML>
