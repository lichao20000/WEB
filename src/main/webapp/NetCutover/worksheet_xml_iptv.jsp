<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct"%>
<%@ page import="com.linkage.module.gwms.util.StringUtil"%>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManageIPTV"/>
<jsp:useBean id="faultCode" scope="request" class="com.linkage.module.gwms.Global"/>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%

request.setCharacterEncoding("GBK");

String str_polltime = request.getParameter("polltime");

String serviceType = request.getParameter("serviceType");
Logger logger = LoggerFactory.getLogger(this.getClass());
// 业务类型，名称Map
Map serv_typeMap = ServiceAct.getGwServTypeMap();
// 操作类型，名称Map
Map oper_typeMap = ServiceAct.getGwOperTypeMap();
//查询数据库
Cursor cursor = sheetManage.getSheetList(request);
Map fields = cursor.getNext();

Map cityMap = com.linkage.module.gwms.dao.tabquery.CityDAO.getCityIdCityNameMap();

%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>

<BODY>
<%
	if (str_polltime != null) {
                int polltime = StringUtil.getIntegerValue(str_polltime) * 60;
                out.println("<meta http-equiv=\"refresh\" content=\""
                        + polltime + "\">");
    }
%>
<%@ include file="../head.jsp"%>
<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">工单列表</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="10" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">
						<IMG SRC="../images/excel.gif" WIDTH="16" HEIGHT="16" BORDER="0" onclick="exportExcel()" ALT="导出到EXCEL" style="cursor:hand">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>策略编号</th>
				<!-- <th width="" nowrap>业务账号</th> -->
				<th width="" nowrap>设备序列号</th>
				<th width="" nowrap>业务类型</th>				
				<th width="" nowrap>操作类型</th>
				<th width="" nowrap>策略执行结果</th>
				<th width="" nowrap>策略开始时间</th>
				<th width="" nowrap>策略结束时间</th>
				<th nowrap>失败原因</th>
			</tr>
			<%

            String[] arrStyle = new String[11];
            arrStyle[0] = "class=trOutgreen onmouseover='this.className=\"trOutgreen\"' onmouseout='this.className=\"trOutgreen\"'";
            arrStyle[1] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
            arrStyle[2] = "class=trOut  onmouseover='this.className=\"trOver\"' onmouseout='this.className=\"trOut\"'";
            arrStyle[3] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[4] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[5] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
            arrStyle[6] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
            arrStyle[7] = "class=trOutchense onmouseover='this.className=\"trOverchense\"' onmouseout='this.className=\"trOutchense\"'";
            arrStyle[8] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
            arrStyle[9] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
            arrStyle[10] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
			
            if (fields != null) {
                String tmp;
                String tmpValue;
                int iStatus = 0;
                while (fields != null) {
                	tmp = StringUtil.getStringValue(fields.get("status"));
					if ("-1".equals(tmp)) {
						iStatus = 8;
					} else if("100".equals(tmp)){
                		iStatus = 0;
                	}else{
                		tmp = StringUtil.getStringValue(fields.get("result_id"));
                		if("1".equals(tmp)){
                			iStatus = 2;
                		}else if("-1".equals(tmp) || "-2".equals(tmp) || "-3".equals(tmp) || "-4".equals(tmp) || "-5".equals(tmp)){
                			iStatus = 3;
                		}else{
                			iStatus = 9;
                		}
                	}
                    tmp = fields.get("id") + "," + fields.get("receive_time") + "," + fields.get("gather_id");
					tmpValue = StringUtil.getStringValue(fields.get("result_id"));
					
					if ("-1".equals(tmpValue)){
						tmpValue = "2";
					}
					else if("1".equals(tmpValue)){
						tmpValue = "1";
					}
                    out.println("<tr style='background-color:white;'  onclick=doClick(this) value='" + tmpValue + "'>");
                    out.println("<td><nobr>" + fields.get("id") + "</nobr></td>");
                    //out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("username")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("device_serialnumber")) + "</nobr></td>");
                    
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("serv_type")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("oper_type")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("status_name")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("start_time_str")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("end_time_str")) + "</nobr></td>");
                    out.println("<td><nobr>" + StringUtil.getStringValue(fields.get("fault_desc")) + "</nobr></td>");
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
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
