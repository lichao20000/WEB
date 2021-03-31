<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%
request.setCharacterEncoding("gb2312");
%>
<%@ page import ="com.linkage.litms.tree.Item"%>
<script language=javascript>
<%
Item node = new Item();

String action = request.getParameter("action");
String item_name = request.getParameter("item_name");

if(action.equals("add")){
	boolean flag = node.itemAdd(request); 
	out.print("parent.document.all('Submit2').click();parent.document.all('item_name').focus();parent.itemAdd('"+ flag +"','"+ item_name +"');");	
}else if(action.equals("delete")){
	boolean flag = node.itemDelete(request);
	out.print("parent.itemDelete('"+ flag +"');");
}else if(action.equals("update")){
	boolean flag = node.itemUpdate(request);
	out.print("parent.itemUpdate('"+ flag +"','"+ item_name +"');");	
}
%>
</script>

