<%--
Author		: lizhaojun
Date		: 2007-4-20
Note		:
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>

<%
request.setCharacterEncoding("GBK");

String tmpSql = request.getParameter("tmpSql");

Map fields = null;
Cursor cursor = null ;
//查询数据库
if(tmpSql != null && !tmpSql.equals("")){
	cursor = DataSetBean.getCursor(tmpSql);
	fields = cursor.getNext();
}


%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<HTML>
<BODY>
<meta http-equiv="refresh" content="10">;
<%@ include file="../head.jsp"%>

<div id="idList">
<TABLE width="100%" height="30" border="0" cellpadding="0"
	cellspacing="0" class="green_gargtd">
	<TR>
		<TD width="162" align="center" class="title_bigwhite">正在执行操作...</TD>
	</TR>
</TABLE>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable"
			oncontextmenu="return false;">
			<tr>
				<th width="" nowrap>表名</th>
				<th width="" nowrap>文件名</th>
				<th width="" nowrap>执行状态</th>				
				<th width="" nowrap>执行时间</th>
				<th width="" nowrap>结束时间</th>
			</tr>
			<%

            String[] arrStyle = new String[11];
            arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
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
                int iStatus = 2;
                while (fields != null) {
					tmp = (String)fields.get("exec_status");
					if(tmp.equals("0") || tmp.equals("1")){
						iStatus = 0;
					}
                    out.println("<tr " + arrStyle[iStatus]+ " >");
                    out.println("<td><nobr>" + fields.get("tab_name")
                            + "</nobr></td>");
                    out.println("<td><nobr>" + fields.get("tab_name_bk")
                            + "</nobr></td>");
                            
                    tmp = (String)fields.get("exec_status"); 
                    if(tmp.equals("0")){
                    	tmp = "准备执行...";
                    } else if(tmp.equals("1")){
                    	tmp = "正在执行...";
                    } else if(tmp.equals("2")){
                    	tmp = "执行成功";
                    } else if(tmp.equals("3")){
                    	tmp = "执行失败";
                    } 
                    out.println("<td><nobr>" + tmp
                            + "</nobr></td>");  
                                                                                                 
					tmp =  (String)fields.get("execute_time");
					if(tmp != null && !tmp.equals("")){
						tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong(tmp));
					}
                    out.println("<td><nobr>" + tmp
                            + "</nobr></td>");
                    tmp =  (String)fields.get("complet_time");        
					if(tmp != null && !tmp.equals("")){
						tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong(tmp));
					}
                    out.println("<td><nobr>" + tmp
                            + "</nobr></td>");
                    out.println("</tr>");

                    fields = cursor.getNext();
                }
            } else {
                out.println("<tr bgcolor='#ffffff' ><td align=left colspan=5>系统没有记录！</td></tr>");
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
//parent.wsState();

//-->
</SCRIPT>
</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
</HTML>
