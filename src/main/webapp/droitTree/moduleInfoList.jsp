<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%request.setCharacterEncoding("gb2312");%>
<%
boolean flag = false;
Module module = new Module();
List lModule = module.getModuleInfoList();
Map mModule = null;
%>

<%@page import="com.linkage.litms.tree.Module"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<BODY>
<span class="jive-setup-header">
<table cellpadding="8" cellspacing="0" border="0" width="100%">
<tr>
    <td width="99%"> <B>联创科技Liposs系统目录分布</B></td>
    <td width="1%" nowrap>&nbsp;</td>
</tr>
</table>
</span>
<table bgcolor="#bbbbbb" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<table bgcolor="#dddddd" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<table bgcolor="#eeeeee" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr><td><img src="images/blank.gif" width="1" height="1" border="0"></td></tr>
</table>
<br>
<form name="form1" action="" method="post">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr valign="top">
	<td width="1%" nowrap>
		<table bgcolor="#cccccc" cellpadding="0" cellspacing="0" border="0" width="200">
		<tr><td>
<%@ include file="./left_bar.jsp"%>

		</td></tr>
		</table>
	</td>
    <td width="1%" nowrap><img src="./images/blank.gif" width="15" height="1" border="0"></td>
    <td width="98%">
		
        <p class="jive-setup-page-header">&nbsp;</p>
        <p>欢迎来到Liposs系统模块编辑界面.</p>
        <table cellpadding="3" cellspacing="2" border="0" width="100%">
          <tr> 
            <td  class="jive-setup-category-header" width="10%">模块名称</td>
            <td  class="jive-setup-category-header" width="22%">模块地址</td>
            <td  class="jive-setup-category-header" width="22%">显示位置</td>
            <td  class="jive-setup-category-header" width="68%">操作</td>
          </tr>
          <%
	          int len = lModule.size();
	          
	          for(int k=0;k<len;k++){
	        	  mModule = (Map)lModule.get(k);
          %>
          <tr> 
            <td width="10%" nowrap ><%=mModule.get("module_name") %>&nbsp; </td>
            <td width="22%" nowrap ><%=mModule.get("module_url") %>&nbsp;</td>
            <td width="22%" nowrap ><%=mModule.get("sequence") %>&nbsp;</td>
            <td width="68%" nowrap >
            <a href=javascript:void(0) onclick=changeParameterOfModule('<%=mModule.get("module_id") %>')>编辑</a>
            &nbsp;&nbsp;|&nbsp;&nbsp;
            <a href=javascript:void(0) onclick=delModuleInfo('<%=mModule.get("module_id") %>','<%=mModule.get("module_name") %>')>删除</a>
            </td>
          </tr>
          <%
          	}
	          //clear
	          mModule = null;
	          lModule.clear();
	          lModule = null;
          %>
          <tr> 
            <td class="jive-setup-category-header" width="10%">&nbsp;</td>
            <td class="jive-setup-category-header" width="22%">&nbsp;</td>
            <td class="jive-setup-category-header" width="22%">&nbsp;</td>
            <td class="jive-setup-category-header" width="68%">&nbsp;</td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
<%@ page import="java.util.Calendar" %>
<p>
<center>
&copy; <a href="http://www.lianchuang.com" target="_blank">Linkage</a>,
2000-<%= (Calendar.getInstance()).get(Calendar.YEAR) %>
</center>
</body>
</html>

<script type="text/javascript">
<!--
function changeParameterOfModule(module_id){
	var returnObj = window.showModalDialog("changeModuleInfo.jsp?module_id="+ module_id +"&t="+new Date().getTime(),window,"status:yes;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:400px;dialogWidth:400px");		
}

function delModuleInfo(module_id,module_name){
	document.all("childFrm").src = "module_save.jsp?action=delete&module_id="+ module_id +"&module_name="+ module_name;
}

function deleteModuleInfo(flag,module_name){
	if(flag){
		alert("系统模块："+ module_name +"删除成功");	
	}else{
		alert("系统模块："+ module_name +"删除失败");	
	}
}
//-->
</script>
