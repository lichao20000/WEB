<SCRIPT LANGUAGE="JavaScript" >
function hide()
{
	if(isIE)
	{
		var fm = window.parent.document.getElementById('frame');
		fm.cols = "1,*";
		//window.parent.frames[2].document.getElementById('showImg').style.display='inline';	
		window.parent.frames[2].document.getElementById('tdmenu').width="209";
	}	
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

function resize()
{
	var obj = document.getElementById("treeView");
	if(isNC6) obj.height=window.innerHeight-20;
}
function ShowCut(){
	var obj = document.getElementById("treeView");
	obj.src = "about:blank";
	obj.src = "shortCut.jsp";
}
</SCRIPT>
<%@ page contentType="text/html;charset=GBK"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
<body onload="status='';resize();" onresize="resize()">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height=100%>
	<tr  height=30>
		<td width="20%" nowrap background="../../images/button_line.jpg" align=right>
			<a href="javascript://" onclick="ShowCut()"><img src="../../images/button_fast.gif" border=0></a>
		</td>
		<td width="80%" background="../../images/button_line.jpg" align=right>
		<SCRIPT LANGUAGE="JavaScript">
		if(isIE)
		{
			document.write("<IMG id='img1' SRC='../img/hidetoc1.gif' title='隐藏菜单' BORDER=0 style='cursor: pointer; cursor: hand;'");
			document.write(" onmouseover=\"img1.style.display='none';img2.style.display='inline';status='隐藏菜单';\"");
			document.write(" onmouseout=\"img1.style.display='inline';img2.style.display='none';status=' ';\"");
			document.write(" onclick=\"javascript:hide()\"></IMG>");
			document.write(" <IMG id='img2' SRC='../img/hidetoc2.gif' title='隐藏菜单' BORDER=0 style='cursor: pointer; cursor: hand;display:none'");
			document.write(" onmouseover=\"img2.style.display='inline';img1.style.display='none';status='隐藏菜单';\"");
			document.write(" onmouseout=\"img2.style.display='none';img1.style.display='inline';status=' ';\"");
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
		<td background="../../images/left_back.jpg" colspan=2>
		<iframe id='treeView' src="shortCut.jsp" frameborder=0 height=100% width=100% border="1">
		</td>
	</tr>
</table>
</body>
</html>

<script type="text/javascript">

 //** iframe自动适应页面 **//

 //输入你希望根据页面高度自动调整高度的iframe的名称的列表
 //用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。

 //定义iframe的ID
 var iframeids=["treeView"]

 //如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
 var iframehide="yes"

 function dyniframesize() 
 {
  var dyniframe=new Array()
  for (i=0; i<iframeids.length; i++)
  {
   if (document.getElementById)
   {
    //自动调整iframe高度
    dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
    if (dyniframe[i] && !window.opera)
    {
     dyniframe[i].style.display="block"
     if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight) //如果用户的浏览器是NetScape
      dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
     else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) //如果用户的浏览器是IE
      dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
    }
   }
   //根据设定的参数来处理不支持iframe的浏览器的显示问题
   if ((document.all || document.getElementById) && iframehide=="no")
   {
    var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    tempobj.style.display="block"
   }
  }
 }

 if (window.addEventListener)
 window.addEventListener("load", dyniframesize, false)
 else if (window.attachEvent)
 window.attachEvent("onload", dyniframesize)
 else
 window.onload=dyniframesize
</script>