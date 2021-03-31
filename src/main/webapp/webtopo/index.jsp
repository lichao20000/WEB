<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.*"%>
<%
request.setCharacterEncoding("GBK");
String strAreaName2 = LipossGlobals.getLipossProperty("InstArea.Name");
if(strAreaName2 == null){
	strAreaName2 = "联创科技";
}
%> 
<html>
<head>
<title>Web Topo-主界面</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="../style.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	background-color: #c0c0c0;
}

.title {
	font-family: "宋体", Verdana, Arial, helvetica;
	font-size: 14px;
	color: #515151;
	font-weight: bold;
	line-height: 22px;
}
-->
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
function showWebTopo(type){
	var page = "main.jsp?type="+type;
	var height=window.screen.availHeight;
	var width=window.screen.availWidth;
	var option ="height="+height+",width="+width+",toolbars=no,scrollbars=no,menubar=no,directories=no,status=yes,resizable =yes";	
	wt = window.open(page,null,option);
}
//-->
</SCRIPT>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- ImageReady Slices (webtopshow-主界面.psd) -->
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="middle"><table id="__01" width="797" height="547" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td> <img src="images/webtop_01.png" width="797" height="55" alt=""></td>
      </tr>
      <tr>
        <td height="25" valign="middle" background="images/webtop_02.jpg"> <div align="center">
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="58%">&nbsp;</td>
					<td width="12%"><div id="login" onclick=""><img src="images/login.gif" width="81" height="22"></div></td>
					<td width="16%"><div id="home" onclick="javascript:window.navigate('../index.jsp');"><img src="images/return-home.gif" width="110" height="22"></div></td>
                    <td width="14%"><div id="logout" onclick=""><img src="images/logout.gif" width="81" height="22"></div></td>
            </tr>
          </table>
        </div></td>
      </tr>
      <tr>
        <td> <img src="images/webtop_03.jpg" width="797" height="25" alt=""></td>
      </tr>
      <tr>
        <td height="315" valign="top" background="images/webtop_04.jpg"> <div align="center">
          <table width="90%"  border="0" cellspacing="2" cellpadding="0">
            <tr>
              <td height="35" valign="top" class="title">&nbsp;&nbsp;&nbsp;&nbsp;点击进入以下视图:</td>
              <td height="35" valign="top" class="title">&nbsp;&nbsp;&nbsp;&nbsp;</td>
            </tr>
            <tr valign="top">
              <td width="53%" class="input"><br>
			    <div id="idNetTopo" onclick="showWebTopo('1')">
                <table width="228" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="74" valign="middle" align="center"><img src="images/button_kuandai.jpg" width="35" height="34"></td>
                    <td width="146" height="34" valign="middle">网络视图</td>
                  </tr>
                </table>
				</div> 
                  <br>
				  <br>
				  <br>
				  <br>
                  <div id="idNetTopo" onclick="showWebTopo('2')">
					<table width="228" border="0" align="center" cellpadding="0" cellspacing="0">
					  <tr>
                        <td width="74" valign="middle" align="center"><img src="images/button_zhuji.jpg" width="35" height="34"></td>
                        <td width="146" height="34" valign="middle">主机视图</td>
					  </tr>
					</table>
				  </div> 
                  <br>
				  <!--
                  <table width="228" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="74" valign="middle"><div align="center">&nbsp;<img src="images/button_yewu.jpg" width="35" height="34"> </div></td>
                      <td width="146" valign="middle"><a href="#" class="blue">业务视图</a></td>
                    </tr>
                  </table>
                  <br>
                  <table width="228" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="74" valign="middle"><div align="center">&nbsp;<img src="images/button_yonghu.jpg" width="35" height="34"> </div></td>
                      <td width="146" valign="middle"><a href="#" class="blue">用户视图</a></td>
                    </tr>
                </table>
				-->
				</td>
				<td width="47%" class="input">
						&nbsp;
                 </td>
            </tr>
          </table>
        </div></td>
      </tr>
      <tr>
        <td height="127" valign="top" background="images/webtop_05.jpg" class="input"><br>          <table width="80%"  border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="51%" class="text">
			<TABLE border=0 cellspacing=0 cellpadding=0 width='100%'>
				<TR><TD ><SPAN class=90v>请以 <FONT face=Arial>IE5.5 </FONT>以上版本 <FONT face=Arial>1024 * 768</FONT> 浏览<BR>&copy; 2005 <%=strAreaName2%>.版权所有;Powered By：<a href='http://www.lianchuang.com/'>联创科技</a>
				</TD></TR>
			 </TABLE>
			</td>
            <td width="49%">&nbsp;</td>
          </tr>
        </table> </td>
      </tr>
    </table></td>
  </tr>
</table>
<!-- End ImageReady Slices -->
</body>
</html>