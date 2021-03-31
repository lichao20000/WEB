</HEAD>
<BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.tree.Tree"%>
<%@page import="com.linkage.litms.tree.Item"%>
<%
	//判断若是不满足要求则不进行以下步骤的执行
	if (temp_TreeItemId != null && temp_TreeItemId.indexOf("|") > 0) {
		String tmp_tree_id = temp_TreeItemId.substring(0,temp_TreeItemId.indexOf("|"));
		String tmp_item_id = temp_TreeItemId.substring(temp_TreeItemId.indexOf("|") + 1);

		Tree Tree = new Tree();
		Item Item = new Item();
		String system_path = Tree.getFullParentPathByTreeId(tmp_tree_id);
		system_path += "->" + (Item.getItemInfoByItemId(tmp_item_id)) .get("item_name");
		if(system_path.startsWith("->")){
		    system_path = system_path.substring(2);	    
		}
		out.println("<br>&nbsp;&nbsp;&nbsp;&nbsp;<b color=blue>"+system_path+"</b>");

		//clear
		system_path = null;
		Tree = null;
		Item = null;
		tmp_tree_id = null;
		tmp_item_id = null;
	}
%>

