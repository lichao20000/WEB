<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>

<SCRIPT LANGUAGE="JavaScript" >
function hide()
{
	if(isIE)
	{
		var fm = window.parent.document.getElementById('frame');
		fm.cols = "1,*";
		//window.parent.frames[2].document.getElementById('showImg').style.display='inline';	
		//alert(window.parent.frames[2]);
		parent.rightPage.document.all('showImg').style.display='inline';
	}	
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

function resize()
{
	var obj = document.getElementById("treeView");
	if(isNC6) obj.height=window.innerHeight-20;
}
</SCRIPT>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="./../../css/css_blue.css" type="text/css">

<style>
a.title:visited {
	font-size: 10pt;
	font-weight: bold;
	color: white;
	text-decoration: none;
}
a.title:hover {
	font-size: 10pt;
	font-weight: bold;
	color: #FF9900;
	text-decoration: underline;
}
a.title:link {
	font-size: 10pt;
	font-weight: bold;
	color:white;
	text-decoration: none;
}
</style>
</head>
<body onload="status='报表系统';resize();" onresize="resize()">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height=100%>
	<tr>
		<td style="background-color:#2951AD;FONT:bold 13px Verdana ;color=white">&nbsp;<!-- <a href="javascript:parent.navigate('../../../Report/');" class=title>报表主页</a> --></td>
		<td height=20 align=right style="background-color:#2951AD">
		<SCRIPT LANGUAGE="JavaScript">
		if(isIE)
		{
			document.write("<IMG id='img1' SRC='../img/hidetoc1.gif' title='隐藏菜单' BORDER=0 style='cursor: pointer; cursor: hand;'");
			document.write(" onmouseover=\"img1.style.display='none';img2.style.display='inline';status='隐藏菜单';\"");
			document.write(" onmouseout=\"img1.style.display='inline';img2.style.display='none';status='报表系统';\"");
			document.write(" onclick=\"javascript:hide()\"></IMG>");
			document.write(" <IMG id='img2' SRC='../img/hidetoc2.gif' title='隐藏菜单' BORDER=0 style='cursor: pointer; cursor: hand;display:none'");
			document.write(" onmouseover=\"img2.style.display='inline';img1.style.display='none';status='隐藏菜单';\"");
			document.write(" onmouseout=\"img2.style.display='none';img1.style.display='inline';status='报表系统';\"");
			document.write(" onclick='javascript:hide()'></IMG>");
		}
		if(isNC6)
		{
			document.write("<div style='color:#639ace'>s</div>");
		}
		</SCRIPT>
		</td>
	</tr>
	<tr>
		<td valign=top colspan=2>
		<iframe id='treeView' src="../treeview/rp_resstat_tree.jsp?rootid=4" frameborder=0 height=100% width=100% border="1">
		</td>
	</tr>
</table>
</body>
</html>
