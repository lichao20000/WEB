<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.tree.Item"%>
<%@ page import ="com.linkage.litms.tree.Tree"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page import ="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import ="com.linkage.litms.common.database.Cursor" %>
<%@ page import ="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="qryp" scope="page" class="com.linkage.litms.common.database.QueryPage" /> 
<%
request.setCharacterEncoding("GBK");
//String username = (String)session.getAttribute("username");
String username = user.getAccount();

if(username==null) username="";
String strSQL = "select * from tab_report_manager where ishas=0 and user_id='"+username+"' and public_stat=-1";
// teledb
if (DBUtil.GetDB() == 3) {
	strSQL = "select id, report_name, reg_time, report_path" +
			" from tab_report_manager where ishas=0 and user_id='"+username+"' and public_stat=-1";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();

%>
<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/Calendar.js"></SCRIPT>
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
var userPremession = '<%=session.getValue("ldims")%>';
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/toolbars.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doDelete(id){
	if(confirm("���Ҫɾ���ñ�����\n��������ɾ���Ĳ��ָܻ�������")){
		page = "rp_nodesave.jsp?id="+id+"&report_stat=-3";
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
</SCRIPT>
</head>

<body>
<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					˽�б���
				</td>
			</tr>
 </table></td>
  </tr> 
  <tr>
    <td><table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
	  <tr bgcolor='#ffffff'><td colspan=4>�㷢����˽�б����б�</td></Tr>
      <tr>
        <TH>��������</TH>
        <TH>����ʱ��</TH>
        <TH>����·��</TH>
		<TH>����</TH>
      </tr>
	  <%
	  if(fields != null){
	  	String id,report_stat,tmp,tree_id;
	  	while(fields != null){
			id = (String)fields.get("id");
			//tree_id=(String)fields.get("tree_id");
			out.println("<tr height=21 bgcolor='#ffffff'>");
			out.println("<td class=column>"+ fields.get("report_name") +"</td>");
			out.println("<td class=column>"+ fields.get("reg_time") +"</td>");
			out.println("<td class=column>"+ fields.get("report_path") +"&nbsp;</td>");
			out.println("<td class=column><a href=\"javascript:doDelete('"+ id +"')\">ɾ��</a></td>");
			out.println("</tr>");
			fields = cursor.getNext();
		}
		
	  }else{
	  	out.println("<tr height=25 bgcolor='#ffffff'><td colspan=4 align=center>���û�з����±���</td></Tr>");
	  }
	  %>
    </table></td>
  </tr>
</table>
<iframe id="childFrm" name="childFrm" src="about:blank" style="display:none"></iframe>
</body>
</html>
