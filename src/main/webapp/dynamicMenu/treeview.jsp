<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="java.util.List"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%
//主菜单目录节点编码
String tree_id = request.getParameter("tree_id");
//权限角色编码
String role_id = String.valueOf(user.getRoleId());

//目录树
NodeData nodeData = new NodeData();
List nodeList = nodeData.getNodeList(tree_id,role_id,LipossGlobals.getLipossProperty("ClusterMode.mode"));

//模块
Module module = new Module();
Module mModule = module.getModuleInfo(tree_id);
%>
<%@page import="com.linkage.litms.tree.NodeData"%>
<%@page import="com.linkage.litms.tree.Node"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="./css/css_blue.css" TYPE="text/css">
</HEAD>

<style>

    A:LINK, A:VISITED, A:ACTIVE, A:HOVER
    {
      color: #000000;
      font-size: 12px;
      padding-left: 5px;
      TEXT-DECORATION: NONE;
      font-family: Tahoma,Verdana,宋体,Fixedsys;
    }
    SPAN
    {
      font-family: Tahoma,Verdana,宋体,Fixedsys;
    }
A.MzTreeview
{
  font-size: 9pt;
  padding-left: 3px;
}
</style>

<BODY style="height:100%"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0"  background="../../images/left_back.jpg">
	<tr>
		<td  width="220" height="19" align=left background="../images/left_top.jpg"></td>
	</tr>
</table>

<script language="JavaScript" src="./MzTreeView10/MzTreeView10.js"></script>
<script language="JavaScript">
  var tree = new MzTreeView("tree");

  tree.icons["property"] = "property.gif";
  tree.icons["css"] = "collection.gif";
  tree.icons["book"]  = "book.gif";
  tree.iconsExpand["book"] = "bookopen.gif"; //展开时对应的图片

  //tree.setIconPath("http://www.meizz.com/Icons/TreeView/"); //可用相对路径
  tree.setIconPath("./MzTreeView10/"); //可用相对路径
  

  tree.nodes["0_<%=tree_id%>"] = "text:<%=mModule.getModule_name()%>";
<%
String server_path = request.getContextPath();

Node node = null;
String prefix = null;
String next_data = null;

for(int k=0;k<nodeList.size();k++){
	node = (Node)nodeList.get(k);
	prefix = node.getNode_parent_id()+"_"+node.getNode_self_id();
	
	next_data = "text:"+node.getNode_text();
	next_data += (node.getNode_icon() != null && !node.getNode_icon().equals("null"))?("; icon:"+ node.getNode_icon()):"";
	next_data += (node.getNode_url() != null && !node.getNode_url().equals("null"))?("; url:"+ server_path +node.getNode_url()):"";
	next_data += (node.getNode_data() != null && !node.getNode_data().equals("null"))?("; data:"+ node.getNode_data()):"";
	
%>
	tree.nodes["<%=prefix%>"] = "<%=next_data%>";
<%}%>

  //tree.setURL("/Catalog.asp");
  tree.setTarget("MzMain");
  //tree.setTarget(parent.parent.window.frames[2]);
  document.write(tree.toString());    //亦可用 obj.innerHTML = tree.toString();
</script>
</body>
</html>
<%
mModule = null;
module = null;
%>

