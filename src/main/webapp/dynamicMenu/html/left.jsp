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
			document.write("<IMG id='img1' SRC='../img/hidetoc1.gif' title='���ز˵�' BORDER=0 style='cursor: pointer; cursor: hand;'");
			document.write(" onmouseover=\"img1.style.display='none';img2.style.display='inline';status='���ز˵�';\"");
			document.write(" onmouseout=\"img1.style.display='inline';img2.style.display='none';status=' ';\"");
			document.write(" onclick=\"javascript:hide()\"></IMG>");
			document.write(" <IMG id='img2' SRC='../img/hidetoc2.gif' title='���ز˵�' BORDER=0 style='cursor: pointer; cursor: hand;display:none'");
			document.write(" onmouseover=\"img2.style.display='inline';img1.style.display='none';status='���ز˵�';\"");
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

 //** iframe�Զ���Ӧҳ�� **//

 //������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
 //�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�

 //����iframe��ID
 var iframeids=["treeView"]

 //����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
 var iframehide="yes"

 function dyniframesize() 
 {
  var dyniframe=new Array()
  for (i=0; i<iframeids.length; i++)
  {
   if (document.getElementById)
   {
    //�Զ�����iframe�߶�
    dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
    if (dyniframe[i] && !window.opera)
    {
     dyniframe[i].style.display="block"
     if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight) //����û����������NetScape
      dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
     else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) //����û����������IE
      dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
    }
   }
   //�����趨�Ĳ���������֧��iframe�����������ʾ����
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