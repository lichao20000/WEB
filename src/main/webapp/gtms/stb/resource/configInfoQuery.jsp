<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒历史配置信息查询</title>
<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function chooseQuery() {
       
		var servAccount = $("input[@name='servAccount']").val();
		var serialnumber = $("input[@name='serialnumber']").val();
		var startTime = $("input[@name='startTime']").val();
		var endTime = $("input[@name='endTime']").val();

		startTime1 = startTime.replace(/-/gi, '/');
		endTime1 = endTime.replace(/-/gi, '/');

		//var miStart = Long.parseLong(Date.parse(startTime1));
		//alert(miStart);
		//	  var maStart = Integer.parseInt();
		var maStart = Date.parse(startTime1);
		var miStart = Date.parse(endTime1);
     if (servAccount == '' && serialnumber == '') {
                alert("业务账号和设备序列号必须输入一个");
                return;
           //     if (((miStart - maStart) / 86400000) > 7) {
                  //   alert("业务账号与序列号为空时时间跨度不得超过7天");
    			//	return;
    		//	}

		} else {

			if (((miStart - maStart) / 86400000) > 31) {

				alert("时间跨度不能超过31天");
				return;
			}

		}

		document.selectForm.action = "<s:url value='/gtms/stb/resource/configInfo!query.action'/>";
		document.selectForm.submit();
	}
</script>
</head>
<body>
<form id="selectForm" name="selectForm" action="" target="first"
	method="post"><input type="hidden" name="status" value="6">
<table class="querytable" align="center"  width="98%"
	 id="tabs">
	<tr>
		<td class="title_1" colspan="4">机顶盒历史配置信息查询</td>
	</tr>
	<tr align="center">
		<td class="title_2" width="15%">业务帐号：</td>
		<td><input type="text" maxlength="50" name="servAccount"
			id="servAccount" /></td>
		<td class="title_2" width="15%">序列号：</td>
		<td><input type="text" maxlength="50" name="serialnumber"
			id="serialnumber" /></td>
	</tr>
	<tr align="center">
		<td class="title_2" width="15%">开始时间：</td>
		<td><lk:date id="startTime" name="startTime" dateOffset="-1"
			maxDateOffset="0" type="all" defaultDate="%{queryTime}"></lk:date></td>
		
		<td class="title_2" width="15%">结束时间</td>
		<td><lk:date id="endTime" name="endTime" dateOffset="0"
			maxDateOffset="0" type="all"></lk:date></td>
	</tr>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<button type="button" onclick=
	chooseQuery();
>查询</button>
		&nbsp;&nbsp;</div>
		</td>
	</tr>
	
</table>
</form>

	
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999 id="td"><iframe id="first"
			name="first" height="0" frameborder="0" scrolling="no"
			width="100%" src=""></iframe></TD>
	</TR>
</TABLE>	
	
	
	
</body>


<SCRIPT LANGUAGE="JavaScript">


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["first"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block";
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
</html>