<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
/**
 * ģ���б���ʾ
 * @author czm(5243) tel��1234567890123
 * @version 1.0
 * @since 2009-12-23 ����10:49:57
 * 
 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ģ���б���ʾ</title>
<lk:res addRes="/Js/toolbars.js"/>
<link rel="stylesheet" href="<s:url value="/itv/css/layout.css"/>" type="text/css">
<script type="text/javascript">
<!--

//����ģ��ҳ��
function showSystemTree(moduleId, moduleName, moduleUrl){
	//alert(moduleId + "  " + moduleName);
	
	//����ҳ���С
	parent.indexBottom.cols="185px,*";
	//��ʾ�˵�
	parent.indexBottom.all.indexTree.src = "<s:url value='/gtms/itv/MenuManager!createTree.action'/>?moduleId="+moduleId+"&moduleName="+moduleName;
	//��ʾģ����ҳ
	parent.indexBottom.all.indexContent.src = "<s:url value="/"/>" + moduleUrl;
}

//������ҳ
function firstPage(){
	//����ҳ���С
	parent.indexBottom.cols="0px,*";
	//��ʾ�˵�
	parent.indexBottom.all.indexContent.src = "<s:url value='/itv/index/index_welcome.jsp'/>";
}

//���µ�½
function relogin()
{
	top.location.href="<s:url value='/itv/login.jsp'/>";
}
//�˳�ϵͳ
function loginout()
{
	top.location.href="<s:url value='/itv/login.jsp'/>";
}

//��ʼ��ҳ��
$(function(){
	//����ҳ���С
	parent.indexBottom.cols="0px,*";
});

//�������
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
	<a href="javascript:switchTab('homePage');firstPage();" id="homePage" name="menuTag" hidefocus="true" class="hot"><span class="n-btn">��ҳ</span></a>
	<s:iterator value="moduleList" status="list">
		<a href="javascript:switchTab('<s:property value="moduleId"/>');showSystemTree('<s:property value="moduleId"/>','<s:property value="moduleName"/>','<s:property value="moduleUrl"/>');" id="<s:property value="moduleId"/>" name="menuTag" hidefocus="true" class=""><span class="n-btn"><s:property value="moduleName"/></span></a>
	</s:iterator>
</div>
</div>
</body>
</html>