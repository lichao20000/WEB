<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>光猫序列号匹配状态查询</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	var uname = $.trim($("input[@name='loid']").val());
	if(uname == null || uname.length == 0){
		$("input[@name='loid']").focus();
		alert("请输入LOID进行查询");
		return false;
	}
	else
	{
		document.selectForm.submit();
	}
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

</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/service/QueryDevsnCompareStatus!getStatusInfo.action'/>"
			target="dataForm">
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
								<td width="162" align="center" class="title_bigwhite">
									光猫序列号匹配状态查询
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									查询光猫序列号匹配状态情况
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<TR>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">
								光猫序列号匹配状态查询
							</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>
								LOID
							</TD>
							<TD width="35%">
								<input type="text" name="loid" size="20" maxlength="30"
									class=bk />
									&nbsp;<font color="red"> *</font>
							</TD>
						<TR>
					</table>
				</td>
			</TR>
			<TR>
				<td colspan="4" align="right" class=foot>
					<input type="button" onclick='query()' value="查 询"/>
				</td> 
			</TR>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
						scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>