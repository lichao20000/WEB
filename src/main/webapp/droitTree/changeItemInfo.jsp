<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.tree.Item"%>
<%@ page contentType="text/html;charset=GBK"%>
<<script type="text/javascript">
<!--
<%
request.setCharacterEncoding("GBK");

Item Item = new Item();

if(request.getParameter("submit") != null){
	//���½ڵ���Ϣ
	boolean flag = Item.itemUpdate(request);
	
	if(flag){
		out.println("alert('���¹��ܽڵ���Ϣ�ɹ�');");
	}else{
		out.println("alert('���¹��ܽڵ���Ϣʧ��');");
	}
	
	//�رմ���
	out.println("window.close();");
}


String item_id = request.getParameter("item_id");


Map mItem = Item.getItemInfoByItemId(item_id);
Item = null;

boolean flag = false;

if(mItem != null){
	flag = true;
}
%>
//-->
</script>
<%@page import="java.util.Map"%>
<html>
<head>
<title>�༭���ܵ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="style.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action=""  target="childFrm">
  <table cellpadding="3" cellspacing="2" border="0" width="100%">
  <%if(flag){ %>
    <tr> 
      <td  class="jive-setup-header" colspan="2" nowrap> 
        <div align="left">�༭ϵͳ������Ϣ&nbsp;</div>
      </td>
    </tr>
    <tr> 
      <td class="jive-setup-category-header"  width="25%" nowrap> 
        <div align="right">��������</div>
      </td>
      <td  class="jive-setup-category-header" width="75%" > 
        <input type=text name=item_name value=<%=mItem.get("item_name") %> class="jive-label" size="35">
        &nbsp;</td>
    </tr>
    <tr> 
      <td  class="jive-setup-category-header" width="10%" nowrap> 
        <div align="right">���ܵ�ַ</div>
      </td>
      <td  class="jive-setup-category-header" > 
        <input type=text name=item_url value=<%=mItem.get("item_url") %> class="jive-label" size="35">
      </td>
    </tr>
    <tr>
      <td  class="jive-setup-category-header" width="10%" nowrap valign="top"> 
        <div align="right">��������</div>
        &nbsp;</td>
      <td  class="jive-setup-category-header" >
        <textarea name="item_desc" class="jive-description" rows="6" cols="40"> <%=mItem.get("item_name") %></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2"> 
        <div align="center"> 
	      <input type=hidden name=item_visual value=1>
          <input type=hidden name=item_id value=<%=mItem.get("item_id") %>>
          <input type=submit name=submit value=���� class="btn">
          &nbsp;</div>
      </td>
    </tr>
    <%mItem=null;}else{ %>
    <tr> 
      <td  class="jive-setup-header" nowrap> 
        <div align="center">ѡ��༭��ϵͳ���ܽڵ��Ѿ�������&nbsp;</div>
      </td>
    </tr>    
    <%} %>
  </table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</body>
</html>
