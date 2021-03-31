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
//所有节点列表
List itemList = Item.getItemList();
//未被选中的所有节点
List itemOutOfSelectedList = Item.getOutOfItemSelected();
//目录下面被选中的所有节点
List itemSelectedList = Item.getItemListByTreeId(tree_id);
//合并以上两者
itemOutOfSelectedList.addAll(itemSelectedList);
//剔除重复元素
itemOutOfSelectedList = new ArrayList(new HashSet(itemOutOfSelectedList));
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashSet"%>
<html>
<head>
<title>功能点选择&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</head>

<script type="text/javascript">
<!--
function batchItems(flag){
	if(flag){
		window.alert("保存功能点列表成功");
	}else{
		window.alert("保存功能点列表失败,重联系管理员或者是重新试一试");
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
    <td width="49%" class="jive-setup-category-header">功能点名称</td>
  </tr>
<%
int h = itemList.size();

Map item = null;

for(int k=0;k<h;k++){
	item = (Map)itemList.get(k);
	
	//判断是否包含在其中
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

//给目录关联菜单时，需要清除菜单用到的静态变量，菜单才能所见所得 MODIFY BY HEMC
Module.clearModuleRoleMap();
%>
  <tr> 
    <td class="jive-setup-category-header"> 
      <div align="center"> 
        <input type=hidden name=tree_id value=<%=tree_id%>>
        <input type="submit" name="Submit" value="保存">
      </div>
    </td>
  </tr>
</table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</div>
</body>
</html>
