<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String gw_type = request.getParameter("gw_type");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>登录失败日志</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var gw_type = "<%= gw_type%>"
$(function(){
	document.selectForm.submit();
});
	
function query(){
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

</script>
		<style type="text/css">
<!--
select {
	position: relative;
	font-size: 12px;
	width: 160px;
	line-height: 18px;
	border: 0px;
	color: #909993;
}
-->
</style>
	</head>

	<body>
		<form name="selectForm" action="<s:url value="loginLog_cqdx.jsp"/>" target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<table>
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
									登录失败日志
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">登录失败日志查询页面
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="6">登录失败日志查询</th>
							</tr>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									用户：
								</td>
								<td width="24%">
									<input type="text" name="username" class=bk>
								</td>
								<td class=column width="10%" align='right'>
									开始时间：
								</td>
								<td width="23%">
									<input type="text" name="time_start" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({maxDate:'#F{$dp.$D(\'time_end\',{d:-0});}',el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
								<td class=column width="10%" align='right'>
									结束时间:
								</td>
								<td width="23%">
									<input type="text" name="time_end" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({minDate:'#F{$dp.$D(\'time_start\',{d:0});}',el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
							</tr>
							<TR>
								<td colspan="6" align="right" class=foot>
									<button onclick="query()">&nbsp;查  询&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" frameborder="0" style="width: 100%" scrolling="no" src=""></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../foot.jsp"%>