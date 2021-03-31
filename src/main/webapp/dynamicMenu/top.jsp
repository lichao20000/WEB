<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>LITMS---e家终端综合管理系统</title>
<%
		String telecom = LipossGlobals.getLipossProperty("telecom");
		String strArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
		String title = "";
		String help = "";
		if(LipossGlobals.IsETMS()){
			title = "title_gd.jpg";
			help = "help_bbms.jsp";
		} else {
			if(telecom.equalsIgnoreCase("CUC")){
				if(strArea.equalsIgnoreCase("sd_lt")){
					title = "title_lt_sd.jpg";
				}else{
					title = "title_lt.jpg";
				}
			}else if(telecom.equalsIgnoreCase("CTC")){
				title = "title.jpg";
			}else{
				title = "title_yd.jpg";
			}
			help = "help.jsp";
		}

 %>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url(../images/title_back.jpg);
}
-->
</style>
<link href="../liulu.css" rel="stylesheet" type="text/css">
<link href="../css_ico.css" rel="stylesheet" type="text/css">
<script language="javascript">
function GoLeft(v) {
	var page = v;
	parent.leftFrame.location.href=page;
}

function forwardPage(page){
	top.window.location.href = page;
}

function showURL(){
		window.top.frames[2].document.all('viewPage').src="../webtopo/main.jsp?type=1"		
}

function hide()
{
	if(isIE)
	{
		var fm = window.top.document.getElementById('frame');
		fm.cols = "1,*";
		window.top.frames[2].document.getElementById('showImg').style.display='inline';
	}	
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

function resize()
{
	var obj = document.getElementById("treeView");
	if(isNC6) obj.height=window.innerHeight-20;
}


//added by yanhj 2007-5-15:退出
function closeWin() {
  if(isIE){
    window.top.close();
  }else {
    window.opener= null;
    window.open(" ","_self");
    window.top.close();
    if(window.top){
      window.top.location.href="about:blank";
    }
  }
}
//end 2007-5-15

</script>

</head>

<body >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="54"><img src="../images/<%=title%>" width="1002" height="54" border="0" usemap="#Map">
      <map name="Map">
        <area shape="rect" coords="692,17,751,35" href="javascript:void(0);forwardPage('../index.jsp');">
        <area shape="rect" coords="760,17,837,34" href="javascript:void(0);forwardPage('../login.jsp');">
		<!-- modified by yanhj 2007-5-15 -->
        <area shape="rect" coords="846,17,905,34" href="javascript:closeWin();">
		<!-- end 2007-5-15 -->
        <area shape="rect" coords="913,16,972,33" href="javascript:void(0);window.showModalDialog('../<%=help%>',window,'dialogHeight:400px;dialogWidth:500px');">
    </map></td>
  </tr>
 </table>
</body>
</html>