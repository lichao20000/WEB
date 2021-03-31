<%@ page import="com.linkage.module.gtms.itv.system.DbAuthorization"%>
<%@ page import="com.linkage.module.gtms.itv.system.OnlineUser"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>

<%
//String username = request.getParameter("acc_loginname");
//System.out.println("IsLogin = "+ session.getAttribute("IsLogin"));
if(session.getAttribute("IsLogin") == null){
  response.sendRedirect("./login.jsp");
  return;
}

if(session.getAttribute("curUser") == null){
	//初始化用户关键信息
	//new DbAuthorization(request, response).initUserRes();
	response.sendRedirect("./login.jsp");
	return;
}

//初始化session的监听器
OnlineUser listener = (OnlineUser )session.getAttribute("HttpSessionBindingListener");
if (listener == null) {
	listener = new OnlineUser();
	session.setAttribute("HttpSessionBindingListener", listener );
}

//request.getRequestDispatcher("./FrameIndex.jsp").forward(request, response);
response.sendRedirect("./FrameIndex.jsp");
%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

//
//UserRes curUser = (UserRes)session.getAttribute("curUser");
//User user = curUser.getUser();
  
//System.out.println("当前用户的采集机数:" + curUser.getUserProcesses().size());
//System.out.println("当前用户的属地：" + curUser.getCityId());
//long ACC_OID = user.getId();
//user = null;
//curUser = null;

String strArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
String strAreaName = LipossGlobals.getLipossProperty("InstArea.Name");
String banner_img = "images/ldims_banner_";
if(strArea!=null){
	banner_img += strArea + ".jpg";
} 
else{
	banner_img = "images/ldims_banner.jpg";
}
if(strAreaName == null){
	strAreaName = "联创科技";
}

//modify 2005-9-19 by yuht 区别故障拦截名称
String strFlautName = LipossGlobals.getLipossProperty("FlautName");
if(strFlautName==null)
	strFlautName = "10000号故障拦截";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> <%=LipossGlobals.getLipossName()%> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=GBK">
<link rel="stylesheet" href="./css/css_blue.css" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
<!--
function newWin(){
    window.open("SupporterInfo.jsp",null,"height=200,width=300");
}
var banner_img = "<%=banner_img%>";
var sys_title = "<%=LipossGlobals.getLipossName()%>";
var strFlautName = "<%=strFlautName%>";
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="Js/coolbar.js"></SCRIPT>
</HEAD>

<BODY onload="newWin()">
<!-- 
<SCRIPT LANGUAGE="JavaScript" SRC="Js/res_comm_index.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="Js/res.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="Js/global.js"></SCRIPT>
 -->
<TABLE border=0 cellspacing=0 cellpadding=0 width="1002" align="center">
<TR><TD> 
<table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="183" valign="top" background="images/left_back.jpg"><img src="images/left_top.jpg" width="183" height="50"><br>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
		</td>
        <td valign="top" background="images/back.jpg"><table width="819" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="27" background="images/button_line2.jpg" id="idShowSubMenu">&nbsp;</td>
          </tr>
          <tr>
            <td height="420">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
    </table>
</TD></TR>
<TR><TD>
  <table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
	  <td height="5" background="images/buttom_line.gif"></td>
	</tr>
  </table>
  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<tr>
	  <td height="81" valign="top" background="images/bottom_line.jpg"><div align="center">
		<table width="493" border="0" cellpadding="0" cellspacing="0" background="images/bottom_middle.jpg">
		  <tr>
			<td width="53" rowspan="3" class="text">&nbsp;</td>
			<td width="381" height="17" class="text"><div align="center">请以 IE5.0 以上版本 1024 * 768 浏览 </div></td>
			<td width="59" rowspan="3" class="text">&nbsp;</td>
		  </tr>
		  <tr>
			<td height="23" class="text"><div align="center">&copy; 2005 <%=strAreaName%>.版权所有;Powered By：<a href='http://www.lianchuang.com/'>联创科技</a> </div></td>
		  </tr>
		</table>
	  </div></td>
	</tr>
  </table>
</TD></TR>
</TABLE>
<map name="Map">
  <area shape="rect" coords="921,16,980,34" href="login.jsp">
  <area shape="rect" coords="856,17,887,33" href="javascript:this.location.reload();">
</map>
</BODY>
</HTML>