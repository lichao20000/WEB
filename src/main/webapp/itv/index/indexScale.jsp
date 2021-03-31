<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 伸缩页面
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-8-14 下午04:15:36
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>左侧伸缩页面</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<style type="text/css">
body
{
margin: 0px;
padding: 0px;
background-image: url("<s:url value="/images/left_repeat_bg.gif"/>");
}
div
{
cursor:hand;
height: 100%;
width: 100%;
background-repeat: no-repeat;
background-position: center center;
}
div.open
{
background-image: url("<s:url value="/FBA/images/left_jtout.jpg"/>");
}
div.openLight
{
background-image: url("<s:url value="/FBA/images/left_jton.jpg"/>");
}
div.close
{
background-image: url("<s:url value="/FBA/images/right_jtout.jpg"/>");
}
div.closeLight
{
background-image: url("<s:url value="/FBA/images/right_jton.jpg"/>");
}
</style>
<script type="text/javascript">
//给左侧TAB页面indexTab调用的方法
function indexMenuOpen()
{
	if(parent.indexBottom.all.indexTree.width<100)
		{
			parent.indexBottom.cols="215px,15px,*";
			$("div").removeClass("closeLight").removeClass("openLight").removeClass("close").addClass("open");
		}
}
var isclick=false;
$(function(){
$("div").hover(function(){
	if(isclick)
	{
		isclick=false;
		return;
	}
	if($(this).attr("class")=="open")
	{
		$(this).removeClass("open");
		$(this).addClass("openLight");
	}
	else
	{
		$(this).removeClass("close");
		$(this).addClass("closeLight");
	}
},function(){
	if(isclick)
	{
		isclick=false;
		return;
	}
	if($(this).attr("class")=="openLight")
	{
		$(this).removeClass("openLight");
		$(this).addClass("open");
	}
	else
	{
		$(this).removeClass("closeLight");
		$(this).addClass("close");
	}
}).click(function(){
	isclick=true;
	if(parent.indexBottom.all.indexTree.width>100)
	{
		$(this).removeClass("openLight");
		$(this).removeClass("open");
		$(this).removeClass("closeLight");
		$(this).addClass("close");
		parent.indexBottom.cols="0px,15px,*";
	}
	else
	{
		$(this).removeClass("closeLight");
		$(this).removeClass("openLight");
		$(this).removeClass("close");
		$(this).addClass("open");
		parent.indexBottom.cols="215px,15px,*";
	}
});
});
</script>
</head>
<body>
<div class="open"></div>
</body>
</html>