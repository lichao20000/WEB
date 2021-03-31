<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<script type="text/javascript">
<!--
<%
request.setCharacterEncoding("GBK");

Module module = new Module();

if(request.getParameter("submit") != null){
	//更新节点信息
	module.setModule_id(request.getParameter("module_id"));
	module.setModule_name(request.getParameter("module_name").trim());
	module.setModule_code(request.getParameter("module_code").trim());
	module.setModule_url(request.getParameter("module_url").trim());
	module.setSequence(request.getParameter("sequence").trim());
	module.setModule_desc(request.getParameter("module_desc").trim());
	module.setModule_pic(request.getParameter("module_pic").trim());
	
	boolean flag = module.updateModuleInfo(module);
	
	if(flag){
		out.println("alert('更新系统模块信息成功');parent.window.location.reload();");
	}else{
		out.println("alert('更新系统模块信息失败');");
	}
	
	//关闭窗口
	out.println("window.close();");
}

String module_id = request.getParameter("module_id");

Module mModule = module.getModuleInfo(module_id);

boolean flag = false;

if(mModule != null){
	flag = true;
}
%>
//-->
</script>
<%@page import="com.linkage.litms.tree.Module"%>
<html>
<head>
<title>编辑系统模块信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="style.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action=""  target="childFrm">
  <table cellpadding="3" cellspacing="2" border="0" width="100%">
  <%if(flag){ %>
    <tr> 
      <td  class="jive-setup-header" colspan="2" nowrap> 
        <div align="left">&nbsp;</div>
      </td>
    </tr>
    <tr> 
      <td class="jive-setup-category-header"  width="25%" nowrap> 
        <div align="right">模块名称</div>
      </td>
      <td  class="jive-setup-category-header" width="75%" > 
        <input type=text name=module_name value=<%=mModule.getModule_name() %> class="jive-label" size="35">
        &nbsp;</td>
    </tr>
    <tr> 
      <td  class="jive-setup-category-header" width="10%" nowrap> 
        <div align="right">模块编码</div>
      </td>
      <td  class="jive-setup-category-header" > 
        <input type=text name=module_code value=<%=mModule.getModule_code() %> class="jive-label" size="35">
      </td>
    </tr>    
    <tr> 
      <td  class="jive-setup-category-header" width="10%" nowrap> 
        <div align="right">主界面链接</div>
      </td>
      <td  class="jive-setup-category-header" > 
        <input type=text name=module_url value=<%=mModule.getModule_url() %> class="jive-label" size="35">
      </td>
    </tr>
    <tr> 
      <td  class="jive-setup-category-header" width="10%" nowrap> 
        <div align="right">树型菜单图标</div>
      </td>
      <td  class="jive-setup-category-header" > 
        <input type=text name=module_pic value=<%=mModule.getModule_pic() %> class="jive-label" size="35">
      </td>
    </tr> 
    <tr> 
      <td  class="jive-setup-category-header" width="10%" nowrap> 
        <div align="right">显示顺序</div>
      </td>
      <td  class="jive-setup-category-header" > 
          <select name=sequence>
          <%for(int k=0;k<20;k++){%>
          <option value=<%=k %>><%=k %></option>
          <%} %>
          </select>
          &nbsp;&nbsp;（注：按照序号由小到大排列）    
          <script>
			document.all("sequence").value = "<%=mModule.getSequence()%>";
		</script>    
      </td>
    </tr>           
    <tr>
      <td  class="jive-setup-category-header" width="10%" nowrap valign="top"> 
        <div align="right">功能描述</div>
        &nbsp;</td>
      <td  class="jive-setup-category-header" >
        <textarea name="module_desc" class="jive-description" rows="6" cols="40"><%=mModule.getModule_desc() %></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2"> 
        <div align="center"> 
          <input type=hidden name=module_id value=<%=mModule.getModule_id() %>>
          <input type=submit name=submit value=更新 class="btn">
          &nbsp;</div>
      </td>
    </tr>
    <%mModule=null;}else{ %>
    <tr> 
      <td  class="jive-setup-header" nowrap> 
        <div align="center">选择编辑的系统模块已经不存在&nbsp;</div>
      </td>
    </tr>    
    <%} %>
  </table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
</body>
</html>
<%
module = null;
%>