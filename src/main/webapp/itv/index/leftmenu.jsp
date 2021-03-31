<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>left menu for index</title>
<lk:res/>
<link href="<s:url value="/FBA/css/css_ico.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
body,ul,li {
	margin: 0xp;
	padding: 0px;
}

li {
	padding-top: 4px;
	list-style: none;
}

a {
	color: black;
	text-decoration: none;
}

body {
	font-size: 9pt;
}

body,ul {
	background-image: url("../../images/left_back.jpg");
}
</style>
<script type="text/javascript">
<!--
var baseDir="<s:url value="/"/>";
var clickId="-1";
function open(){
	$("img",this).attr("src",$(this).attr("layer")=="0"?"<s:url value="/images/button_open.gif"/>":"<s:url value="/images/button_open2.gif"/>");

	var condition ="li[@pid='"+$(this).attr("id")+"']";
	$(condition).each(function(){$(this)[0].style.display="";});
}

function close(){
	if($(this).attr("layer")=="0")
	{
		$("img",this).attr("src","<s:url value="/images/button_close.gif"/>");
	}
	else if($(this).attr("layer")=="1")
	{
		$("img",this).attr("src","<s:url value="/images/button_close2.gif"/>");
	}
	else if($(this).attr("layer")=="2")
	{
		$("img",this).attr("src","<s:url value="/images/button_close2.gif"/>");
	}
	var condition ="li[@pid='"+$(this).attr("id")+"']";
	$(condition).each(function(){
		$(this)[0].style.display="none";
		if($(this).attr("isleaf")=='false')
		{
			if($(this).attr("layer")=="1")
			{
				$("img",this).attr("src","<s:url value="/images/button_close2.gif"/>");
			}
			var condition ="li[@pid='"+$(this).attr("id")+"']";
			$(condition).each(function(){
				$(this)[0].style.display="none";
			});
		}
	});
}
$(document).ready(function(){
	$("li").each(function(){
		if($(this).attr("layer")=="0")
		{
			$(this).addClass("left_title");
		}
		else if($(this).attr("layer")=="1")
		{
			$(this).addClass("left_title2");
		}
		else if($(this).attr("layer")=="2")
		{
			$(this).addClass("left_title3");
		}
		else if($(this).attr("layer")=="3")
		{
			$(this).addClass("left_title4");
		}
		
		$(this).hover(function(){
			if($(this).attr("layer")=="0")
			{
				$(this).addClass("left_titleon");
			}
			else if($(this).attr("layer")=="1")
			{
				$(this).addClass("left_title2on");
			}
			else if($(this).attr("layer")=="2")
			{
				$(this).addClass("left_title3on");
			}
			else if($(this).attr("layer")=="3")
			{
				$(this).addClass("left_title4on");
			}
		},function(){
			if($(this).attr("id")==clickId)
			{
				return;
			}
			if($(this).attr("layer")=="0")
			{
				$(this).removeClass("left_titleon");
			}
			else if($(this).attr("layer")=="1")
			{
				$(this).removeClass("left_title2on");
			}
			else if($(this).attr("layer")=="2")
			{
				$(this).removeClass("left_title3on");
			}
			else if($(this).attr("layer")=="3")
			{
				$(this).removeClass("left_title4on");
			}
		});
		
		if($(this).attr("isleaf")!="true")
		{
			$(this).toggle(open,close);
		}
	});
});

//页面跳转
function goLocation(id)
{
	clickId=id;
	$("li").removeClass("left_title2on").removeClass("left_title3on");
	$("#"+id).addClass("left_title2on");
	parent.indexBottom.all.indexContent.src = baseDir + $("#"+id).attr("url");
	//查询导航信息
	$.ajax({
		type: "POST",
		url: '<s:url value="/LIMS/menu/GetMenu!initBar.action"/>',
		data: {"menuId":id},
		success: function(data){
			parent.parent.frames.indexTop.frm.all("bar").innerHTML = data;
		},
		error: function(xmlR,msg,other){}
	});

}
//-->
</script>
</head>
<body>
<div>
<ul>
	<s:iterator value="menu">
		<li id="<s:property value="id"/>" url="<s:property value="url"/>" pid="<s:property value="pid"/>"
		    class="dian_line"
			layer="<s:property value="layer"/>"
			isleaf="<s:property value="isleaf"/>"
			style="display:<s:property value="layer==0?'':'none'"/>">
			<img src="<s:url value="%{isleaf?'/images/button_dian.gif':(layer==0?'/images/button_close.gif':'/images/button_close2.gif')}"/>" />
			<s:if test="isleaf">
				<a href="javascript:goLocation('<s:property value="id"/>')">
					<s:property value="name" />
				</a>
			</s:if>
			<s:else>
				<s:property value="name" />
			</s:else>
		</li>
	</s:iterator>
</ul>
<div>
</body>
</html>