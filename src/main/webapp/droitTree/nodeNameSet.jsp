<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%request.setCharacterEncoding("gb2312");%>
<%
boolean flag = false;
List lItem = null;
Map mItem = null;

//检索到此界面
if(request.getParameter("item_query")!= null){
	String item_name = request.getParameter("item_name");
	Item Item = new Item();
	lItem = Item.getItemListByItemName(item_name);
	
	//判断是否有值
	if(lItem!=null && lItem.size()>0){
		flag = true;
	}
	
	//clear
	Item = null;
}
%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.tree.Item"%>
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
        <p>欢迎来到Liposs系统功能点编辑界面，在此界面可以检索、调整软件功能点名称功能.</p>
        <table cellpadding="3" cellspacing="2" border="0" width="100%">
          <tr> 
            <td class="jive-setup-category-header"  width="10%" nowrap>
              功能名称
            </td>
            <td  class="jive-setup-category-header" width="22%" nowrap> 
              <input type=text name=item_name class="jive-label">
              &nbsp;
              <input type=submit class="btn" value=检索 name="item_query">
               </td>
            <td  class="jive-setup-category-header" width="68%" nowrap>&nbsp;</td>
          </tr>
          <tr> 
            <td  class="jive-setup-category-header" width="10%">&nbsp;</td>
            <td  class="jive-setup-category-header" width="22%">&nbsp;</td>
            <td  class="jive-setup-category-header" width="68%">&nbsp;</td>
          </tr>
          <tr> 
            <td width="10%" > </td>
            <td width="22%" ></td>
            <td width="68%" ></td>
          </tr>
          <tr> 
            <td  class="jive-setup-category-header" width="10%">功能名称</td>
            <td  class="jive-setup-category-header" width="22%">功能地址</td>
            <td  class="jive-setup-category-header" width="68%">操作</td>
          </tr>
          <%
          if(flag){
	          int len = lItem.size();
	          
	          for(int k=0;k<len;k++){
				mItem = (Map)lItem.get(k);
          %>
          <tr> 
            <td width="10%" nowrap ><%=mItem.get("item_name") %>&nbsp; </td>
            <td width="22%" nowrap ><%=mItem.get("item_url") %>&nbsp;</td>
            <td width="68%" nowrap ><a href=javascript:void(0) onclick=changeParameterOfItem('<%=mItem.get("item_id") %>')>编辑</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href=javascript:void(0) onclick=delItem('<%=mItem.get("item_id") %>')>删除</a></td>
          </tr>
          <%
          	}
	          //clear
	          mItem = null;
	          lItem.clear();
	          lItem = null;
          }
          %>
          <tr> 
            <td class="jive-setup-category-header" width="10%">&nbsp;</td>
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
function changeParameterOfItem(item_id){
	var returnObj = window.showModalDialog("changeItemInfo.jsp?item_id="+ item_id +"&t="+new Date().getTime(),window,"status:yes;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:300px;dialogWidth:400px");		
}

function delItem(item_id){
	document.all("childFrm").src = "item_save.jsp?action=delete&item_id="+ item_id ;
}

function itemDelete(flag){
	if(flag){
		alert("功能点删除成功");
	}else{
		alert("功能点删除失败");
	}
}
//-->
</script>
