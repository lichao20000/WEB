<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ include file="../../timelater.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO绑定日志</title>
<style>
.#msg{background:#68af02;color:#fff;left:50%;top:0px;position:absolute;margin-left:-25%;margin-left:expression(-this.offsetWidth/2);font-size:12px;text-align:center;padding:0 28px;height:20px;line-height:20px;white-space:nowrap;}
.errmsg {background:#ef8f00;}
</style>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script language="JavaScript">
<!--
function query(){
	$("iframe[@id='dataForm']").css("display","");
	dyniframesize();
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

$(window).resize(function(){
	dyniframesize();
}); 

//-->
</script>
</head>
<body>
<form name="selectForm" action="<s:url value="/bbms/service/EVDOBindLog!queryData.action"/>" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" align=center>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						EVDO绑定日志
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
				<tr bgcolor=#ffffff>
					<td class=column align=center>开始时间</td>
					<td>
						<input type="text" size="14" name="startDate" value='<s:property value="startDate" />' readonly class=bk>
						<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.startDate,'%Y-%M-%D',true,'whyGreen')"
							src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
					</td>
					<td class=column align=center>结束时间</td>
					<td>
						<input type="text" size="14" name="endDate" value='<s:property value="endDate" />' readonly class=bk>
						<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.endDate,'%Y-%M-%D',true,'whyGreen')"
							src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center>属    地</td>
					<td>
						<s:select label="选择属地" list="cityList" name="cityId" listKey="city_id" listValue="city_name"
            				emptyOption="true"  value="cityId"/>
					</td>
					<td class=column align=center>设备序列号</td>
					<td><input type="text" size="20" class=bk name="deviceNo" /></td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column colspan=4 align=right>
						<input type="button" value=" 查 询 " class=jianbian onclick="query()">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="58%" frameborder="0" scrolling="no" width="100%" src="" STYLE="display:none">正在统计...</iframe>
		</td>
	</tr>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>