<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="com.linkage.litms.tree.Item"%>
<%
Item Item = new Item();

String tree_id = request.getParameter("tree_id");
//delete item_id from tree_id
Item.delItems(tree_id);

String[] item_id = request.getParameterValues("item_id");
//join item_id from tree_id
out.println("<script language=javascript>parent.batchItems("+ Item.saveItems(tree_id,item_id) +");</script>");

%>


