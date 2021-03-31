<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
long role_id = user.getRoleId();
%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@page import="com.linkage.litms.common.util.RandomGUID"%>
<script language=javascript>
<%
Module module = new Module(); 

String action = request.getParameter("action");
String module_name = request.getParameter("module_name");

if(action.equals("add")){
	module.setModule_id(new RandomGUID().toString());
	module.setModule_name(request.getParameter("module_name").trim());
	module.setModule_code(request.getParameter("module_code").trim());
	module.setModule_url(request.getParameter("module_url").trim());
	module.setSequence(request.getParameter("sequence").trim());
	module.setModule_desc(request.getParameter("module_desc").trim());
	module.setModule_pic(request.getParameter("module_pic").trim());
	
	boolean flag = module.saveModuleInfo(module,role_id);
	
	module = null;
	out.print("parent.document.all('Submit2').click();parent.document.all('module_name').focus();parent.saveModuleInfo('"+ flag +"','"+ module_name +"');");	
}else if(action.equals("delete")){
	boolean flag = module.deleteModuleInfo(request.getParameter("module_id"));
	out.print("parent.deleteModuleInfo('"+ flag +"','"+ module_name +"');parent.window.location.reload();");	
}else if(action.equals("update")){
	module.setModule_id(request.getParameter("module_id"));
	module.setModule_name(request.getParameter("module_name").trim());
	module.setModule_code(request.getParameter("module_code").trim());
	module.setModule_url(request.getParameter("module_url").trim());
	module.setSequence(request.getParameter("sequence").trim());
	module.setModule_desc(request.getParameter("module_desc").trim());
	module.setModule_pic(request.getParameter("module_pic").trim());
	
	boolean flag = module.updateModuleInfo(module);
	out.print("parent.updateModuleInfo('"+ flag +"','"+ module_name +"');");	
}
%>
</script>

