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
//String username = (String)session.getAttribute("username");
String username = user.getAccount();
if(username==null) username="";
String strSQL = "select * from tab_report_manager where ishas=0 and user_id='"+username+"' and public_stat>=0 order by reg_time desc";
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select id, report_stat, report_name, reg_time, report_path " +
			"from tab_report_manager where ishas=0 and user_id='"+username+"' and public_stat>=0 order by reg_time desc";
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
<title>�㷢���ı����б�</title>
</head>
<script>
<!--
function doDelete(id){
	if(confirm("���Ҫɾ���ñ�����\n��������ɾ���Ĳ��ָܻ�������")){
		page = "rp_nodesave.jsp?id="+id+"&report_stat=-2";
		document.all("childFrm").src = page;
	}
}
function doString(flag){
	if(flag == 1){
		alert("ɾ��˽�б���ɹ�!");
		//top.leftPage.location.reload();
		this.location.reload();	
	}
	else if(flag==-1)
		alert("ɾ��˽�б���ʧ��!");
}
//-->
</script>
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
					���б���
				</td>
			</tr>
 </table>
	</td>
  </tr>  
  <tr>
    <td><table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
	  <tr bgcolor='#ffffff'><td colspan=5>�㷢���Ĺ��������б�</td></Tr>
      <tr>
        <TH>��������</TH>
        <TH>����״̬</TH>
        <TH>����ʱ��</TH>
        <TH>����·��</TH>
        <TH>����</TH>
      </tr>
	  <%
	  if(fields != null){
	  	String id,report_stat,tmp,tree_id;
	  	while(fields != null){
			id = (String)fields.get("id");
			//tree_id = (String)fields.get("tree_id");
			report_stat = (String)fields.get("report_stat");
			if(report_stat.equals("1"))
				tmp="<font color=green>���ͨ��</font>";
			else if(report_stat.equals("-1"))
				tmp="<font color=red>���δͨ��</font>";
			else
				tmp="�ύ����";
			
			out.println("<tr height=21 bgcolor='#ffffff'>");
			out.println("<td class=column>"+ fields.get("report_name") +"</td>");
			out.println("<td class=column>"+ tmp +"</td>");
			out.println("<td class=column>"+ fields.get("reg_time") +"</td>");
			out.println("<td class=column>"+ fields.get("report_path") +"&nbsp;</td>");
			if(username.equals("admin"))
			{
			   out.println("<td class=column><a href=\"javascript:doDelete('"+ id +"')\">ɾ��</a></td>");
			}
	  	else
	  	{
	  		 out.println("<td class=column></td>");
	  	}
			out.println("</tr>");
			fields = cursor.getNext();
		}
		out.println("<tr bgcolor='#ffffff'><td  class=column COLSPAN=5 align=right>"+strBar+"</td></Tr>");
	  }else{
	  	out.println("<tr height=25 bgcolor='#ffffff'><td colspan=5 align=center>���û�з����±���</td></Tr>");
	  }
	  %>
    </table></td>
  </tr>
</table>
<iframe id="childFrm" name="childFrm" src="about:blank" style="display:none"></iframe>
</body>
</html>
