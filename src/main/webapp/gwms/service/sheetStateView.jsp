<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>工单状态查询</title>
<%
	/**
		* 工单状态查询
		* 
		* @author qixueqi(4174)
		* @version 1.0
		* @since 2009-06-16
		* @category
		*/
%>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="../../Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">

function query(){
	document.selectForm.submit();
	dyniframesize();
	
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


$(window).resize(function(){
	dyniframesize();
}); 

</script>

</head>

<body>
<form name="selectForm" action="<s:url value="/gwms/service/sheetStateView!startQuery.action"/>"  target="dataForm">
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							工单状态查询
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">		
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					
					<tr leaf="simple">
						<th colspan="4">工单状态查询</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">业务类型：</TD>
						<TD class=column width="35%">
							<select name="productSpecId" class="bk">
								<option value="60">==上网业务==</option>
							</select>
						</TD>
						<TD class=column width="15%">请选择属地：</TD>
						<TD class=column width="35%">
							<select name="city_id" class="bk">
								<s:iterator value="cityList">
									<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
								</s:iterator>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">操作类型：</TD>
						<TD class=column width="35%">
							<select name="oper_type_id" class="bk">
								<option value="-1" selected="selected"></option>
								<s:iterator value="gwOperTypeList">
									<option value="<s:property value="oper_type_id" />">==<s:property value="oper_type_name" />==</option>
								</s:iterator>
							</select>
						</TD>
						<TD class=column width="15%">绑定状态：</TD>
						<TD class=column width="35%">
							<select name="bind_state" class="bk">
								<option value="-1" selected="selected"></option>
								<option value="0">==未绑定==</option>
								<option value="1">==已绑定==</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="15%">定制起始时间：</td>
						<td width="35%">
							<input type="text" name="startTime" value='<s:property value="startTime" />' readonly class=bk>
							<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.startTime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="../../images/search.gif" width="15" height="12" border="0" alt="选择">
						</td>
						<td class=column width="15%">定制截止时间：</td>
						<td width="35%">
							<input type="text" name="endTime" value='<s:property value="endTime" />' readonly class=bk>
							<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.endTime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="../../images/search.gif" width="15" height="12" border="0" alt="选择">
						</td>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">宽带账号：</TD>
						<TD class=column width="35%"><input type="input" name="username" size="30"/></TD>
						<TD class=column align="center" width="50%" colspan="4">
							<input type="button" value="查  询" class="jianbian" onclick="query()"/>
						</TD>
					</TR>
					
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<br>
				<iframe id="dataForm" name="dataForm" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>