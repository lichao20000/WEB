<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>绑定日志视图</title>
<%
	 /**
	 * 绑定日志视图
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-05-21
	 * @category
	 */
%>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">
<script type="text/javascript" src="../../Js/inmp/jquery.js"/></script>
<lk:res />
<script type="text/javascript">

$(function(){
	setValue();
});
	
	
function query(){
var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var username = $.trim($("input[@name='username']").val());
	if(""!=deviceSn && deviceSn.length<6){
		alert("请至少输入设备序列号后六位！");
		return false;
	}
	if(""==deviceSn && ""==username){
		alert("用户帐号或设备序列号至少输入一项!");
		return false;
	}
	document.selectForm.submit();
}

function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	document.selectForm.bindStartTime.value = year+"-"+month+"-"+day+" 00:00:00";
	document.selectForm.bindEndTime.value = year+"-"+month+"-"+day+" 23:59:59";
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

</script>
</head>
<body>
	<form name="selectForm"
		action="<s:url value="/inmp/resource/bindLogView!startQuery.action"/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
						<tr>
							<th width="162" align="center" class="title_bigwhite" nowrap>
								现场安装信息
							</th>
							<td nowrap>
								<img src="<s:url value="../../images/inmp/attention_2.gif"/>" width="15" height="12">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<tr leaf="simple">
							<th colspan="4">
								现场安装信息
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								LOID
							</TD>
							<TD width="35%">
								<input type="input" name="username" size="25" class=bk />
							</TD>
							<TD class=column width="15%" align='right'>
								设备序列号
							</TD>
							<TD width="35%">
								<input type="input" name="deviceSn" size="25" class=bk />
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								开始时间
							</TD>
							<TD width="35%">
								<lk:date id="bindStartTime" name="bindStartTime" type="all" />
							</TD>
							<TD class=column width="15%" align='right'>
								结束时间
							</TD>
							<TD width="35%">
								<lk:date id="bindEndTime" name="bindEndTime" type="all" />
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								属地
							</TD>
							<TD width="35%">
								<select name="cityId" class="bk">
									<option value="-1">==请选择==</option>
									<s:iterator value="cityIdList">
										<option value="<s:property value="city_id" />">
											==<s:property value="city_name" />==
										</option>
									</s:iterator>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>
								
							</TD>
							<TD width="35%">
								
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<td align="right" colspan="4"  class="green_foot" width="100%">
								<input type="button" onclick="query()" class=jianbian value=" 查 询 " style="margin-left: 1063" />
								<input type="reset" class=jianbian value=" 重 置 " style="margin-left: 6"/>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25">
				</td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>