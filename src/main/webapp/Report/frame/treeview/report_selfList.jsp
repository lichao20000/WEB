<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.tree.Item"%>
<%@ page import ="com.linkage.litms.tree.Tree"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page import ="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import ="com.linkage.litms.common.database.Cursor" %>
<%@ page import ="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="qryp" scope="page" class="com.linkage.litms.common.database.QueryPage" /> 
<%
request.setCharacterEncoding("gb2312");
String stroffset = request.getParameter("offset");

String reportname = request.getParameter("reportname");

if(reportname == null){
	reportname = "";
}

//String username = (String)session.getAttribute("username");
String username = user.getAccount();
if(username==null) username="";
String cond = "";
if (!"admin".equals(username)) {
	cond = " and user_id='" + username + "'";
}
String strSQL = "select * from tab_report_manager where ishas=0 "+cond+" and public_stat>=0 and report_name like '%" + reportname + "%' order by reg_time desc";
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select id, report_name, user_id, reg_time, report_path " +
			"from tab_report_manager where ishas=0 "+cond+" and public_stat>=0 and report_name like '%" + reportname + "%' order by reg_time desc";
}

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();

int pagelen = 20;
int offset;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

qryp.initPage(strSQL,offset,pagelen);
String strBar = qryp.getPageBar();
String strClr = "";
Cursor cursor = DataSetBean.getCursor(strSQL,offset,pagelen);
Map fields = cursor.getNext();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<title>你发布的报表列表</title>
</head>
<script>
<!--
function doDelete(id){
	if(confirm("真的要删除该报表吗？\n本操作所删除的不能恢复！！！")){
		page = "rp_nodesave.jsp?id="+id+"&report_stat=-2";
		document.all("childFrm").src = page;
	}
}

function doString(flag){
	if(flag == 1){
		alert("删除报表成功!");
		//top.leftPage.location.reload();
		document.getElementById("rn").value = "";
		//window.location.submit();
		document.all.frm.submit();
	} else if(flag==-1) {
		alert("删除报表失败!");
	}
}

//-->
</script>
<body>
<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">

<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
	<table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">订制报表</td>
					<td>
						<img src="../../../images/attention_2.gif" width="15" height="12">
						以下是所有手工定制的报表
					</td>
				</tr>
			</table>
			</td>
		</tr>
			
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					
				</td>
			</tr>
 </table>
	</td>
  </tr>  
  <tr>
    <td><table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
	  <tr bgcolor='#ffffff'>
	  	<td colspan="5" align="right">
			报表名称：
			<input type="text" id="rn" name="reportname" class=bk value="">
			<input type="submit" name="query" value=" 查 询 ">
		</td>
	  </Tr>
      <tr>
        <TH>报表名称</TH>
        <TH>定制人员</TH>
        <TH>定制时间</TH>
        <TH>发布路径</TH>
        <TH>操作</TH>
      </tr>
	  <%
	  if(fields != null){
	  	String id,report_stat,tmp,tree_id;
	  	while(fields != null){
			id = (String)fields.get("id");
			
			out.println("<tr height=21 bgcolor='#ffffff'>");
			out.println("<td class=column>"+ fields.get("report_name") +"</td>");
			out.println("<td class=column>"+ fields.get("user_id") +"</td>");
			out.println("<td class=column>"+ fields.get("reg_time") +"</td>");
			out.println("<td class=column>"+ fields.get("report_path") +"&nbsp;</td>");
			out.println("<td class=column><a href=\"javascript:doDelete('"+ id +"')\">删除</a></td>");
			out.println("</tr>");
			fields = cursor.getNext();
		}
		out.println("<tr bgcolor='#ffffff'><td  class=column COLSPAN=5 align=right>"+strBar+"</td></Tr>");
	  }else{
	  	out.println("<tr height=25 bgcolor='#ffffff'><td colspan=5 align=center>最近没有发布新报表</td></Tr>");
	  }
	  %>
    </table></td>
  </tr>
</table>
</FORM>
<iframe id="childFrm" name="childFrm" src="about:blank" style="display:none"></iframe>
</body>
</html>
