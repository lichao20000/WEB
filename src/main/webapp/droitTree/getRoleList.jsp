<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%request.setCharacterEncoding("gb2312");%>
<%
Droit Droit = new Droit();
//List list = Droit.getAllRoles();
//���г���ǰ�õĽ�ɫ
List list = Droit.getAllRolesExpSelf(user.getRoleId());
Map object = null;
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.tree.Droit"%>

<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<style>
	#details span{
		font-size:10px;
		font-family:arial;
		display:block;
	}
	#details input,#details textarea{
		border: 1px solid gray;
	}
	#actions a{
		font-size:12px;
		font-family:arial;
		margin:5px;
		text-decoration:none;
	}
	#actions {
		border-bottom:1px solid blue;
		padding:2px;
		margin-bottom:10px;
		margin-top:10px;
	}
	#showproc{
		border:1px solid;
		border-color: lightblue blue blue lightblue;
		background-color: #87ceeb;
		font-family:arial;
		font-size: 14px;
		padding: 5px;
		position: absolute;
		top:100px;
		left:600px;
	}
</style>

<script type="text/javascript">
function deleteRole(flag){
	if(flag){
		alert("ɾ����ɫ�ɹ�");
	}else{
		alert("ɾ����ɫʧ��");
	}
	
	document.location.reload();
}

function edit(url){
	var f = document.forms["detailsForm"];
	//f.target = "detailsForm";
	f.action = url;
	f.submit();	
}

function del(url){
	//var f = document.forms["actionframe"];
	//f.target = "detailsForm";
	//f.src = url;
	//f.submit();
	
	document.all("actionframe").src = url;
}
</script>

<BODY style="height:100%" >
<span class="jive-setup-header">
<table cellpadding="8" cellspacing="0" border="0" width="100%">
<tr>
    <td width="99%"> <B>�����Ƽ�LipossϵͳĿ¼�ֲ�</B></td>
    <td width="1%" nowrap>&nbsp;</td>
</tr>
</table>
</span>
<table bgcolor="#bbbbbb" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<table bgcolor="#dddddd" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<table bgcolor="#eeeeee" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<br>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr valign="top">
	<td width="1%" nowrap>
		<table bgcolor="#cccccc" cellpadding="0" cellspacing="0" border="0" width="200">
		<tr><td>
<%@ include file="./left_bar.jsp"%>
		</td></tr>
		</table>
	</td>
    <td width="1%" nowrap><img src="./images/blank.gif" width="15" height="1" border="0"></td>
    <td width="98%">
      <p class="jive-setup-page-header">�ѷ����ɫȨ���б�</p>
		
        <p>��ӭ����LipossϵͳĿ¼�༭����. ��������������༭ϵͳĿ¼�ṹΪ����Ȩ�޿�����׼�������. �ڼ���֮ǰ,��ע��ϵͳ�п������û���������ϵͳ���κα༭���Ŀ¼�ṹ����Ϊ���ᵼ�������û�ʹ��ϵͳ���ܵı仯.</p>
      <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr> 
          <td colspan="3">
          	

<form name="detailsForm" action="" target=actionframe method="post">
              <table width="100%" cellpadding="3" cellspacing="2" border="0">
                <tr > 
                  <td colspan="2" class="jive-setup-category-header">&nbsp;</td>
                </tr>
                <tr nowrap> 
                  <td class="jive-setup-category-header">��ɫ����</td>
                  <td class="jive-setup-category-header">����</td>
                </tr>
                <%for(int k=0;k<list.size();k++){
					object = (Map)list.get(k);
  				%>
                <tr nowrap> 
                  <td class="jive-setup-category-header"><%=object.get("role_name") %>&nbsp;</td>
                  <td class="jive-setup-category-header"><a href="droitEdit.jsp?role_id=<%=object.get("role_id") %>">�༭</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:void(0);" onclick="del('roleDelete.jsp?role_id=<%=object.get("role_id") %>');">ɾ��</a>&nbsp;</td>
                </tr>
                <%
	} 
	
  //clear
  object = null;
  list.clear();
  list = null;
  Droit = null;
%>
              </table>
</form>
<!--- iframe for submiting details form to --->
<iframe name="actionframe" id="actionframe" frameborder="0" width="100%" height="0"></iframe>

         	
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr><td align=right colspan=3>&nbsp;</td></TR>
</table>
<%@ page import="java.util.Calendar" %>

<p><br>
<center>
&copy; <a href="http://www.lianchuang.com" target="_blank">Linkage</a>,
2000-<%= (Calendar.getInstance()).get(Calendar.YEAR) %>
</center>
</body>
</html>
