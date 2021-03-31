<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.io.*,java.util.*"%>
<HTML>
<HEAD>
<TITLE>系统架构创建向导 </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<BODY>
<span class="jive-setup-header">
<table cellpadding="8" cellspacing="0" border="0" width="100%">
<tr>
    <td width="99%"> <B>联创科技Liposs系统架构创建</B></td>
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
        <p>欢迎来到Liposs系统管理界面，在左侧选择编辑功能.</p>
      <table cellpadding="3" cellspacing="2" border="0" width="100%">
        <tr> 
            <td class="jive-setup-category-header">&nbsp;</td>
        </tr>
        <tr>
            <td  class="jive-setup-category-header">&nbsp;</td>
        </tr>
        <tr> 
          <td >  
          </td>
        </tr>
        <tr> 
            <td class="jive-setup-category-header">&nbsp;</td>
        </tr>
        <tr> 
            <td >&nbsp; </td>
        </tr>
        <tr> 
          <td class="jive-setup-category-header">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%@ page import="java.util.Calendar" %>

<p><br>
<center>
&copy; <a href="http://www.lianchuang.com" target="_blank">Linkage</a>,
2000-<%= (Calendar.getInstance()).get(Calendar.YEAR) %>
</center>
</body>
</html>
