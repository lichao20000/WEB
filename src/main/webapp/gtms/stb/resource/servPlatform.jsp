<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务平台类型管理</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function Query()
{
	 var platformname = $("input[@name='platformname']").val();
	/*  if(platformname==""||platformname==null)
		 {
		 alert("请填写业务平台名称");
		 return false;
		 } */
    document.frm.submit();
}
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block";
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
  		tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

	function ADD()
	{
		var strpage = "<s:url value='/gtms/stb/resource/servPlatformAdd.jsp'/>";
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
</head>
<body>
	<form NAME="frm" method="post"
		action="<s:url value="/gtms/stb/resource/stbservplatform!query.action"/>"
		target="dataForm">
		<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<th>
									业务平台类型管理
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">业务平台类型管理！
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<tr>
			<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									业务平台类型管理
								</th>
							</tr>
							<TR>
								<TD class=column width="30%" align='right'>
									业务平台类型名称
								</TD>
								<TD colspan="3">
								<input type="text" name="platformname" id="platformname" class="bk" value="">
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="Query()">
										&nbsp;查 询&nbsp;
									</button>
									<button onclick="ADD()">
										&nbsp;添加&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
					<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
		</table>
	</form>
</body>
</html>