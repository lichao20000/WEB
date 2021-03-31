<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="com.linkage.litms.tree.Droit"%>
<%
String role_id = request.getParameter("role_id");

//delete tree_id from role_id
Droit Droit = new Droit();
Droit.delRoleDroitByRoleId(role_id); 
Droit.delItemDroitByRoleId(role_id);
Droit.delTreeDroitByRoleId(role_id);

out.print("<script language=javascript>parent.deleteRole(true);</script>");	

%>