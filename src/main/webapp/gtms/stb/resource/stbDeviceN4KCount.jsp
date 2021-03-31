<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<title>IPTV非4K机顶盒设备统计</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	dyniframesize();
});

function query(data_type,queryTime)
{
	if("exp"!=data_type){
		isShowButton(false);
		isCountDesc("正在统计，请稍等....");
	}
	
	$("input[@name='data_type']").val(data_type);
	$("input[@name='queryTime']").val(queryTime);
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gtms/stb/resource/stbDeviceCount!stbN4KCount.action'/>";
	form.submit();
}

function isShowButton(tag){
	if(tag){
		$("button[@name='queryButton']").attr("disabled", false);
	}else{
		$("button[@name='queryButton']").attr("disabled", true);
	}
}

function isCountDesc(str){
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html(str);
}

</SCRIPT>
</head>

<body>
<form name="mainForm" action="" target="dataForm">
	<input type='hidden' name="data_type" value="" />
	<input type='hidden' name="queryTime" value="" />
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	您当前的位置：IPTV非4K机顶盒设备统计
			</TD>
		</TR>
	</TABLE>
	<TABLE width="98%" class="querytable" align="center">
		<tr>
			<th id="thTitle" class="title_1">全省非4K机顶盒分布报表</th>
		</tr>
		<tr align="right">
			<td class="foot" align="right">
				<div class="right">
					<button name="queryButton" align="right" onclick="javascript:query('query','')"> 统 计 </button>
				</div>
			</td>
		</tr>
	</TABLE>
	<br>
	<div id="resultData">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
	</div>
</form>
<table>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
</table>

</body>
<SCRIPT LANGUAGE="JavaScript">
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

</SCRIPT>
</html>