<%@ include file="../../timelater.jsp"%>
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
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">

$(function(){
	setValue();
});
	
	
function query(){
	var LOID = $.trim($("input[@name='username']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var bindStartTime = $.trim($("input[@name='bindStartTime']").val());
	var bindEndTime = $.trim($("input[@name='bindEndTime']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	if(""!=deviceSn && deviceSn.length<6){
		alert("请至少输入设备序列号后六位！");
		return false;
	}
	if(""==deviceSn && ""==LOID){
		alert("用户帐号或设备序列号至少输入一项!");
		return false;
	}
	isShow();
	$("button[@name='button']").attr("disabled", true);
	$("div[@id='QueryData']").html("正在统计，请稍等....");
	 var url = '<s:url value='/gwms/resource/bindLogView!startQuery.action'/>';
	 document.selectForm.submit();
	 $("div[@id='QueryData']").html("");
		$("button[@name='button']").attr("disabled", false);
	
}
function isShow() {
	$("tr[@id='trData']").show();
}
function setValue(){
	theday=new Date();
	day=theday.getDate()-1;
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	startDay=day;
	startMonth=theday.getMonth()+1;
	if(startDay==1){
		startMonth=theday.getMonth();
		if(startMonth==1||startMonth==3||startMonth==5||startMonth==7||startMonth==8||startMonth==10||startMonth==12){
			startDay=31;
		}else if(startMonth==2){
			if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
				startDay=28;
			}else{
				startDay=29;
			}
		}else{
			startDay=30;
		}
	}else{
		startDay=startDay-1;
	}
	
	document.selectForm.bindStartTime.value = year+"-"+startMonth+"-"+startDay+" 00:00:00";
	document.selectForm.bindEndTime.value = year+"-"+month+"-"+day+" 23:59:59";
}
function res(){
	$("input[@name='deviceSn']").val('');
	$("input[@name='username']").val('');
	var obj = document.getElementById("cityId");
	obj.options[0].selected=true;
	setValue();
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
		action="<s:url value="/gwms/resource/bindLogView!startQuery.action"/>"
		target="dataForm">
		<table >
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
								现场安装信息
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
								现场安装信息
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column name = "LOID" width="15%" align='right'>
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
								<select name="cityId" id="cityId" class="bk">
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
								<button onclick="query()" name="button">&nbsp;查  询&nbsp;</button>
								&nbsp;&nbsp;
								<button type="button" onclick="res()" >&nbsp;重 置&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在统计，请稍等....
					</div>
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