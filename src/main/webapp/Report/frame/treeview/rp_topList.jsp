<%@ page import="com.linkage.litms.common.database.SyLimitRowQuery" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>

<%
request.setCharacterEncoding("gb2312");
String strSQL = "select * from tab_report_manager where ishas=0 and report_stat=1 and public_stat=1 order by reg_time desc";
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select id, report_name, user_id, reg_time, report_path " +
            "from tab_report_manager where ishas=0 and report_stat=1 and public_stat=1 order by reg_time desc";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = SyLimitRowQuery.getCursor(strSQL,10);
Map fields = cursor.getNext();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
<title>最新发布的报表（TOP10）</title>
</head>

<body>
<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="13%"><img src="images/shenhe_tu1.gif" width="85" height="78"></td>
        <td width="2%"><img src="images/shenhe_tu2.gif" width="3" height="69"></td>
        <td width="85%" valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td class="html_title"><strong>说明</strong></td>
            </tr>
            <tr>
              <td height="55" bgcolor="#EFEBFF" class="text">您当前选择的目录&gt;&gt;<font color=blue>
                          <script>document.write(top.leftPage.treeView.getCurrentPath());</script>
                          </font></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%"  border="1" cellspacing="0" cellpadding="0" bordercolor="#999999" bordercolordark="#ffffff" bordercolorlight="#999999">
	  <tr><td colspan=4>最新发布的报表（TOP10）</td></Tr>
      <tr>
        <TH>报表名称</TH>
        <TH>申请人</TH>
        <TH>申请时间</TH>
        <TH>发布路径</TH>
      </tr>
	  <%
	  if(fields != null){
	  	String id;
	  	while(fields != null){
			id = (String)fields.get("id");
			
			out.println("<tr height=21>");
			out.println("<td class=column>"+ fields.get("report_name") +"</td>");
			out.println("<td class=column>"+ fields.get("user_id") +"</td>");
			out.println("<td class=column>"+ fields.get("reg_time") +"</td>");
			out.println("<td class=column>"+ fields.get("report_path") +"</td>");
			out.println("</tr>");
			fields = cursor.getNext();
		}
	  }else{
	  	out.println("<tr height=25><td colspan=4>最近没有发布新报表</td></Tr>");
	  }
	  %>
    </table></td>
  </tr>
</table>
</body>
</html>
