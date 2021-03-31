<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>资源绑定模块内存管理</title>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<link rel="stylesheet" href="../../css/tab.css" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"> 

$(function(){
	winNavigate(1);
});

//var iframeSrcUser="<s:url value='/gwms/diagnostics/deviceInfo.action?deviceId='/>";
var iframeSrcUser="resourceBindMemConfigUser.jsp";
var iframeSrcDevice="resourceBindMemConfigDevice.jsp";
var iframeBindTest="<s:url value='/gwms/sysConfig/itmsInstTest.action'/>";

function winNavigate(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("myiframe1").src = iframeSrcUser;
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("myiframe1").src = iframeSrcDevice;
			break;
		}
		case 3:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="curendtab_bbms";
			document.all("myiframe1").src = iframeBindTest;
			break;
		}
	}
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe1"]

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


$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>

</head>
<body>
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" nowrap>
						资源绑定模块内存管理
					</td>
					<td nowrap>
						<img src="../../images/attention_2.gif" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR  id="tr2"  STYLE="display" >
  		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD>
						<TABLE width="40%" border=0 cellspacing=0 cellpadding=0>
							<TR height=30>
								<a class="tab_A" href="javascript:winNavigate(1);"><TH class="curendtab_bbms" style="cursor:hand" id="td1" width="35%">用户信息</TH></a>
								<a class="tab_A" href="javascript:winNavigate(2);"><TH class="endtab_bbms" style="cursor:hand" id="td2" width="35%">设备信息</TH></a>
								<a class="tab_A" href="javascript:winNavigate(3);"><TH class="endtab_bbms" style="cursor:hand" id="td3" width="30%">绑定测试</TH></a>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR>
					<td>
						<DIV id=qh_con0 style="DISPLAY: block">
							<iframe id=myiframe1 src="" frameborder="0" scrolling="no" height="100%" width="100%" onload=this.height=myiframe1.document.body.scrollHeight;></iframe>
						</DIV>
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>
