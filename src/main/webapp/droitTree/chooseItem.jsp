<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.tree.Item"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%
String tree_id = request.getParameter("tree_id");

Item Item = new Item();
//���нڵ��б�
List itemList = Item.getItemList();
//δ��ѡ�е����нڵ�
List itemOutOfSelectedList = Item.getOutOfItemSelected();
//Ŀ¼���汻ѡ�е����нڵ�
List itemSelectedList = Item.getItemListByTreeId(tree_id);
//�ϲ���������
itemOutOfSelectedList.addAll(itemSelectedList);
//�޳��ظ�Ԫ��
itemOutOfSelectedList = new ArrayList(new HashSet(itemOutOfSelectedList));
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashSet"%>
<html>
<head>
<title>���ܵ�ѡ��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</head>

<script type="text/javascript">
<!--
function batchItems(flag){
	if(flag){
		window.alert("���湦�ܵ��б�ɹ�");
	}else{
		window.alert("���湦�ܵ��б�ʧ��,����ϵ����Ա������������һ��");
	}
	
	window.close();
}

//-->
</script>

<body bgcolor="#FFFFFF" text="#000000">
<div align=center>
<form name="form1" method="post" action="itemSelectedSave.jsp"  target="childFrm">
  <table width="85%" cellpadding="3" cellspacing="2" border="0">
    <tr> 
    <td width="49%" class="jive-setup-category-header">���ܵ�����</td>
  </tr>
<%
int h = itemList.size();

Map item = null;

for(int k=0;k<h;k++){
	item = (Map)itemList.get(k);
	
	//�ж��Ƿ����������
	if(!itemOutOfSelectedList.contains(item.get("item_id"))){
		continue;
	}
%>  
  <tr> 
    <td width="49%" class="jive-setup-category-header"> 
      <input type="checkbox" name="item_id" value="<%=item.get("item_id") %>" <%out.print(itemSelectedList.contains(item.get("item_id"))?"checked":""); %>><%=item.get("item_name") %>
    </td>
  </tr>
<%
}

//clear
itemOutOfSelectedList.clear();
itemOutOfSelectedList = null;

itemSelectedList.clear();
itemSelectedList = null;

itemList.clear();
itemList = null;

//��Ŀ¼�����˵�ʱ����Ҫ����˵��õ��ľ�̬�������˵������������� MODIFY BY HEMC
Module.clearModuleRoleMap();
%>
  <tr> 
    <td class="jive-setup-category-header"> 
      <div align="center"> 
        <input type=hidden name=tree_id value=<%=tree_id%>>
        <input type="submit" name="Submit" value="����">
      </div>
    </td>
  </tr>
</table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</div>
</body>
</html>
