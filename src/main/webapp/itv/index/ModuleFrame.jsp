<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
/**
 * 模块列表显示
 * @author czm(5243) tel：1234567890123
 * @version 1.0
 * @since 2009-12-23 上午10:49:57
 * 
 * <br>版权：南京联创科技 网管科技部
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模块列表显示</title>
<lk:res addRes="/Js/toolbars.js"/>
<link rel="stylesheet" href="<s:url value="/itv/css/layout.css"/>" type="text/css">
<script type="text/javascript">
<!--

//进入模块页面
function showSystemTree(moduleId, moduleName, moduleUrl){
	//alert(moduleId + "  " + moduleName);
	
	//调整页面大小
	parent.indexBottom.cols="185px,*";
	//显示菜单
	parent.indexBottom.all.indexTree.src = "<s:url value='/gtms/itv/MenuManager!createTree.action'/>?moduleId="+moduleId+"&moduleName="+moduleName;
	//显示模块首页
	parent.indexBottom.all.indexContent.src = "<s:url value="/"/>" + moduleUrl;
}

//返回首页
function firstPage(){
	//调整页面大小
	parent.indexBottom.cols="0px,*";
	//显示菜单
	parent.indexBottom.all.indexContent.src = "<s:url value='/itv/index/index_welcome.jsp'/>";
}

//重新登陆
function relogin()
{
	top.location.href="<s:url value='/itv/login.jsp'/>";
}
//退出系统
function loginout()
{
	top.location.href="<s:url value='/itv/login.jsp'/>";
}

//初始化页面
$(function(){
	//调整页面大小
	parent.indexBottom.cols="0px,*";
});

//点击链接
function switchTab(id)
{
	$("a[name='menuTag']").each(function(){
		this.className = "";
	});
	
	$("#" + id).attr("class","hot");
}

//-->
</script>
<link href="<s:url value="/itv/css/dialog.css"/>" rel="stylesheet" type="text/css">
<script src="<s:url value="/itv/js/dialog.js"/>" type="text/javascript" ></script>
</head>

<body class="top-bg">
<div>
<img src="<s:url value="/itv/images/logo.jpg"/>" width="402" height="54"/>
<div  class="quick-link">
<img src="<s:url value="/itv/images/top-ico.jpg"/>" border="0" usemap="#Map" / >
<map name="Map" id="Map">
  <area shape="rect" coords="21,19,70,41" href="javascript:firstPage();" />
  <area shape="rect" coords="80,19,160,41" href="javascript:relogin();" />
  <area shape="rect" coords="167,19,228,40" href="javascript:loginout();" />
</map>
</div>
<div class="navlist">
	<a href="javascript:switchTab('homePage');firstPage();" id="homePage" name="menuTag" hidefocus="true" class="hot"><span class="n-btn">首页</span></a>
	<s:iterator value="moduleList" status="list">
		<a href="javascript:switchTab('<s:property value="moduleId"/>');showSystemTree('<s:property value="moduleId"/>','<s:property value="moduleName"/>','<s:property value="moduleUrl"/>');" id="<s:property value="moduleId"/>" name="menuTag" hidefocus="true" class=""><span class="n-btn"><s:property value="moduleName"/></span></a>
	</s:iterator>
</div>
</div>
</body>
</html>