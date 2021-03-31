<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>绑定日志统计</title>
<%
	 /**
	 * 绑定日志统计
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-05-21
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">

$(function(){
	setValue();
});
	
	
function query(){
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
		action="<s:url value="/gwms/resource/bindLogCount!startQuery.action"/>"
		target="dataForm">
		<table border=0 cellspacing=0 cellpadding=0 width="98%"
			align="center">
			<tr>
				<td HEIGHT=20>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table  class="green_gargtd">
						<tr>
							<th>
								绑定日志统计
							</th>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
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
								绑定日志统计
							</th>
						</tr>
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
						<TR>
							<td colspan="4" class=foot>
								<button onclick="query()">&nbsp;查  询&nbsp;</button>
								&nbsp;&nbsp;
								<button type="reset" >&nbsp;重 置&nbsp;</button>
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