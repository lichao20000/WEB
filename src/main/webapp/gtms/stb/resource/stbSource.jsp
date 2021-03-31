<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒溯源</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function queryDevice(){
	document.selectForm.action = "<s:url value='/gtms/stb/resource/stbSource!qryStbSource.action'/>";
	document.selectForm.submit();
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

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
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
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
$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
</head>
<body>
<form name="selectForm" action="<s:url value="/gtms/stb/resource/stbSource!qryStbSource.action"/>" target="dataForm">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	您当前的位置：机顶盒溯源
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">机顶盒溯源信息查询</th></tr>
	<TR id="tr21" STYLE="display:">
		<TD width="10%"  class="title_2">LOID</TD>
		<TD width="40%">
			<input type="text" name="loid" value=""   class="bk"/>
		</TD>
		<TD width="10%"  class="title_2">机顶盒账号</TD>
		<TD width="40%">
			<input type="text" name="servAccount" value=""   class="bk"/>
		</TD>
	</TR>
	
	<TR id="tr22" STYLE="display:">
		<TD width="10%"  class="title_2">设备序列号</TD>
		<TD width="40%">
			<input type="text" name="devSn" value=""   class="bk"/>
		</TD>
		<TD width="10%" class="title_2" >机顶盒序列号</TD>
		<TD width="40%">
			<input type="text" name="stbdevSn" value=""   class="bk"/>
		</TD>
	</TR>
	
	<TR id="tr23" STYLE="display:">
		<TD width="10%" class="title_2">MAC</TD>
		<TD width="40%"  >
		<input type="text" name="mac" value=""   class="bk"/>
		</TD> 
		  <TD width="10%" class="title_2" >机顶盒MAC</TD>
		<TD width="40%">
			<input type="text" name="stbMac" value=""   class="bk"/>
		</TD>  
	</TR>
	<TR id="tr23" STYLE="display:">
	    <TD width="10%"  class="title_2">宽带账号</TD>
		<TD width="40%" colspan="3">
			<input type="text" name="netUsername" value=""  class="bk"/>
		</TD>
		</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
			<div class="right">
				<button align="right" onclick="javascript:queryDevice()"> 查 询 </button>
			</div>
		</td>
	</tr>
</TABLE>
<br>
<div align="center"><iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="98%" src=""></iframe></div>
</form>
</body>
<br>
<br>
