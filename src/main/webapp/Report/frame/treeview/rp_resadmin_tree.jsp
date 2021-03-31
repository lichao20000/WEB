<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%
request.setCharacterEncoding("gb2312");
String rootid = request.getParameter("rootid");
if(rootid==null) rootid="1";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>TreeView</title>
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
<script language="JavaScript" src="js/tree_maker.js"></script>
<script language="JavaScript" src="js/tree_res.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function delMenuText(){
	var node=tree.getSelect();

	if(node==null){
		alert("没有选择的菜单");return;
	}
	
	var node_id = node.id;
	tree.del(node_id);

}

function addSub(text,link,target){
	if(tree.selectID==null){
		alert("没有选择的菜单");return;
	}
	
	hint = text;
	status = text;
	
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,text,hint,status,"","");
	node.hint=hint;
	node.status=status;
	
	if(link != null){
		node.setLink(link,target);
	}
}

function setMenuText(text)
{
	var node=tree.getSelect();
	if(node==null){
		alert("没有选择的菜单");return;
	}

	node.setText(text);
	node.hint=text;
	node.status=text;
}
//-->
</SCRIPT>
</head>

<body>
<TABLE  border=0 cellpadding=0 cellspacing=0 width="100%">
  <tr>
  	<TD width="240">
		<div id="idTreeView" XMLSrc="treeview_admin_xml.jsp?type=admin" onselectstart='return false' style="overflow:auto;width:220;height:400;padding:2pt 2pt 2pt 2pt" > 
		<script language="JavaScript">
			init_tree();
		</script>
		</div>
	</TD>
  </tr>
</TABLE>
</body>
</html>

