<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for ip info menu</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.treeview.pack.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryTreeExtend-linkage.js"/>"></script>

<script type="text/javascript">

//鼠标点击跳转窗口的方法
function goLocation(url)
{
//parent.bottom.all.right.src=url;
parent.document.getElementById("right").src=url;
}
//装载树数据的方法
function loadTree()
{
$.ajax({ type: "POST",
			url: "<s:url value='/hgwipMgSys/getMenu!getTree.action'/>",
			data: {"subnet_len":"<s:property value='subnet_len'/>"},
			success:
				function(data)
				{
            	var a=eval('('+data+')');
            	$.linageAnalyTree("menu",a);
				$("body> div > ul:first").Treeview();
				anly();
				},
				erro:function(xmlR,msg,other){alert(msg);}});
}
/**
*解析函数，负责加载完树以后，修改图片和href的值
*/
var u= "<s:url value='/hgwipMgSys/pageDispatcher.action'/>?attr=";
function anly()
{	
	//迭代所有的li，改变他的img图片和href的源
	//attr 的格式为 userstat/subnet/subnetgrp/inetmask/assign/leaf 
	$("li").each(function(){
		var attr=$(this).attr('attr');
		$("a:first",this).bind("click",function(){goLocation(u+attr);});
		var attrs = attr.split("/");
		if(attrs[5]=='true')
		{
			if(attrs[4]=='0')
			{
					//可以分配
					$("img:first",$(this)).attr("src","<s:url value='/hgwipMgSys/images/ip_ico3.gif'/>");
			}
			else
			{
				if(attrs[0]=='0')
				{
					//省局
					$("img:first",$(this)).attr("src","<s:url value='/hgwipMgSys/images/ip_ico4.gif'/>");
				}
				else
				{
					if(attrs[4]!='5')
					{
						//地市
						$("img:first",$(this)).attr("src","<s:url value='/hgwipMgSys/images/IPUSER.gif'/>");
					}
					else
					{
						$("img:first",$(this)).attr("src","<s:url value='/hgwipMgSys/images/IPWAIT.gif'/>");
					}
				}
			}
		}
		else
		{
			$("img:first",$(this)).attr("src","<s:url value='/hgwipMgSys/images/ip_ico2.gif'/>");
		}
	});
}
//装载树
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
	loadTree();
});
//提供给其他页面的用来重新装载菜单的方法
function reload(attr)
{
	$("#menu").empty();
	var attrs = attr.split("/");
	$.ajax({ type: "POST",
			url: "<s:url value='/hgwipMgSys/getMenu!getTree.action'/>",
			data: {"subnet_len":"<s:property value='subnet_len'/>"},
			success:
				function(data)
				{
            	var a=eval('('+data+')');
            	$.linageAnalyTree("menu",a);
				$("body> div > ul:first").Treeview();
				anly();
				var id =("#"+attrs[1]+"_"+attrs[2]+"_"+attrs[3]).replace(/\./g,"\\\.");
					goLocation(u+$(id).attr("attr"));
				},
				erro:function(xmlR,msg,other){alert(msg);}});
}
</script>
<style type="text/css">
.treeview,.treeview ul {
	padding: 0;
	margin: 0;
	list-style: none;
}

.treeview li {
	margin: 0;
	padding: 3px 0pt 3px 16px;
}

ul.dir li {
	padding: 2px 0 0 16px;
}

.treeview li {
	background: url(./images/tv-item.gif) 0 0 no-repeat;
}

.treeview .collapsable {
	background-image: url(./images/tv-collapsable.gif);
}

.treeview .expandable {
	background-image: url(./images/tv-expandable.gif);
}

.treeview .last {
	background-image: url(./images/tv-item-last.gif);
}

.treeview .lastCollapsable {
	background-image: url(./images/tv-collapsable-last.gif);
}

.treeview .lastExpandable {
	background-image: url(./images/tv-expandable-last.gif);
}

li img {
	height: 12px;
	width: 12px;
}

A:visited {
	COLOR: #006790;
	TEXT-DECORATION: none;
	line-height: 19px;
}

A:link {
	COLOR: #006790;
	TEXT-DECORATION: none;
	line-height: 19px;
}

A:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline;
	line-height: 19px;
}

A.amap:link {
	COLOR: #ffffff;
	TEXT-DECORATION: none
}

A.amap:visited {
	COLOR: #ffffff;
	TEXT-DECORATION: none
}

A.amap:active {
	COLOR: #ffffff;
	TEXT-DECORATION: none
}

A.amap:hover {
	COLOR: #ff6633;
	TEXT-DECORATION: underline
}

A.bbs:link {
	COLOR: #000066;
	TEXT-DECORATION: none
}

A.bbs:visited {
	COLOR: #000066;
	TEXT-DECORATION: none
}

A.bbs:active {
	COLOR: #000066;
	TEXT-DECORATION: none
}

A.bbs:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline
}

a.title:visited {
	font-size: 10pt;
	font-weight: bold;
	color: black;
	text-decoration: none;
}

a.title:hover {
	font-size: 10pt;
	font-weight: bold;
	color: #FF8D00;
	text-decoration: none;
}

a.title:link {
	font-size: 10pt;
	font-weight: bold;
	color: black;
	text-decoration: none;
}

body {
	background-color: #E1EEEE;
}
</style>
</head>
<body>
<div id="menu"></div>
</body>
</html>