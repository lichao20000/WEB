<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%request.setCharacterEncoding("gb2312");%>
<HTML>
<HEAD>
<TITLE> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<style>
	#details span{
		font-size:10px;
		font-family:arial;
		display:block;
	}
	#details input,#details textarea{
		border: 1px solid gray;
	}
	#actions a{
		font-size:12px;
		font-family:arial;
		margin:5px;
		text-decoration:none;
	}
	#actions {
		border-bottom:1px solid blue;
		padding:2px;
		margin-bottom:10px;
		margin-top:10px;
	}
	#showproc{
		border:1px solid;
		border-color: lightblue blue blue lightblue;
		background-color: #87ceeb;
		font-family:arial;
		font-size: 14px;
		padding: 5px;
		position: absolute;
		top:100px;
		left:600px;
	}
</style>
<script type="text/javascript">
<!--
function itemAdd(flag,name){
	if(flag){
		window.alert("功能："+ name +" 添加成功");
	}else{
		window.alert("功能："+ name +" 添加失败");
	}
}

//-->
</script>
<BODY style="height:100%"  bgcolor="#FFFFFF" text="#000000">
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
      <p class="jive-setup-page-header">报表创建</p>
		
        <p>欢迎来到Liposs系统目录编辑界面. 这个工具是用来编辑系统目录结构为后期权限控制作准备而设计. 在继续之前,请注意系统有可能是用户在网运行系统，任何编辑变更目录结构的行为都会导致其它用户使用系统功能的变化.</p>
      <table cellpadding="3" cellspacing="2" border="0" width="100%">
        <tr> 
          <td colspan="3">
          	
<form name="form1" method="post" action="item_save.jsp" target="childFrm">
              <table width="100%" cellpadding="3" cellspacing="2" border="0">
                <tr > 
                  <td colspan="2" class="jive-setup-category-header"> 
                    <div align="center">功能点定义</div>
                  </td>
                </tr>
                <tr  > 
                  <td width="30%" class="jive-setup-category-header"> 
                    <div align="right">功能点名称</div>
                  </td>
                  <td width="70%" class="jive-setup-category-header"> 
                    <input type="text" name="item_name" class="jive-description" size="35">
                  </td>
                </tr>
                <tr> 
                  <td class="jive-setup-category-header" > 
                    <div align="right">功能点链接</div>
                  </td>
                  <td class="jive-setup-category-header" > 
                    <input type="text" name="item_url" class="jive-description" size="35">
                  </td>
                </tr>
                <tr> 
                  <td valign="top" class="jive-setup-category-header"> 
                    <div align="right">功能说明</div>
                  </td>
                  <td class="jive-setup-category-header"> 
                    <textarea name="item_desc" rows="6" class="jive-description" cols="35"></textarea>
                  </td>
                </tr>
                <tr> 
                  <td class="jive-setup-category-header" > 
                    <div align="right">是否可见</div>
                  </td>
                  <td class="jive-setup-category-header" > 
                    <input type="radio" name="item_visual" value="1" checked>
                    可见 
                    <input type="radio" name="item_visual" value="0">
                    不可见</td>
                </tr>
                <tr> 
                  <td colspan="2" class="jive-setup-category-header"> 
                    <div align="center"> 
                      <input type="submit" name="Submit" value="增 加">
                      <input type="reset" name="Submit2" value="取 消">
                      <input type="hidden" name="action" value="add">
                    </div>
                  </td>
                </tr>
              </table>
</form>
<iframe id=childFrm name=childFrm src="" style="display:none"></iframe>
          	
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr><td align=right colspan=3>&nbsp;</td></TR>
</table>
<%@ page import="java.util.Calendar" %>

<p><br>
<center>
&copy; <a href="http://www.lianchuang.com" target="_blank">Linkage</a>,
2000-<%= (Calendar.getInstance()).get(Calendar.YEAR) %>
</center>
</body>
</html>
