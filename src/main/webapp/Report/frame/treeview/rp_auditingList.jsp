<%@ page contentType="text/html;charset=gb2312"%>
<%@ include file="../../../timelater.jsp"%>
<%@ page import ="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("gb2312");
String strSQL = "select * from tab_report_manager where ishas=0 and report_stat=0 and public_stat<>-1";
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select id, report_name, user_id, reg_time " +
			"from tab_report_manager where ishas=0 and report_stat=0 and public_stat<>-1";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<title>需审核的报表列表</title>
<script language="javascript">
function doAuditing(id,name){
	page = "rp_auditingForm.jsp?id="+id+"&report_name="+name;
	window.open(page,"","left=20,top=20,width=600,height=400,resizable=yes,scrollbars=yes");
}

function doView(id){
	
}

function doDelete(id){
	if(confirm("真的要删除该报表吗？\n本操作所删除的不能恢复！！！")){
		page = "rp_nodesave.jsp?id="+id+"&report_stat=-3";
		document.all("childFrm").src = page;
	}
}

function doString(flag){
	if(flag == 1)
		alert("删除未审核报表成功!");		
	else if(flag==-1)
		alert("删除未审核报表失败!");

	this.location.reload();	
}

</script>
</head>

<body>
<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
  <td>
  <table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					报表审核
				</td>
			</tr>
 </table>
  </td>
  </tr>  
  <tr>
    <td><table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
	  <tr bgcolor='#ffffff'><td colspan=4>未被审核的报表列表</td></Tr>
      <tr>
        <TH>报表名称</TH>
        <TH>申请人</TH>
        <TH>申请时间</TH>
        <TH>操作</TH>
      </tr>
	  <%
	  if(fields != null){
	  	String id;
	  	while(fields != null){
			id = (String)fields.get("id");
			
			out.println("<tr bgcolor='#ffffff'>");
			out.println("<td class=column>"+ fields.get("report_name") +"</td>");
			out.println("<td class=column>"+ fields.get("user_id") +"</td>");
			out.println("<td class=column>"+ fields.get("reg_time") +"</td>");
			out.println("<td class=column><a href=\"javascript:doAuditing('"+ id +"','"+ fields.get("report_name")+"')\">审核</a>&nbsp;|&nbsp;<a href=\"javascript:doDelete('"+ id +"')\">删除</a></td>");
			out.println("</tr>");
			fields = cursor.getNext();
		}
	  }else{
	  	out.println("<tr height=25 bgcolor='#ffffff'><td colspan=4 align=center>没有要被审核的报表</td></Tr>");
	  }
	  %>
    </table></td>
  </tr>
</table>
<iframe id="childFrm" name="childFrm" src="about:blank" style="display:none;width:500;height:500"></iframe>
</body>
</html>
