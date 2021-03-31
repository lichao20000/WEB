<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
/**
 * 菜单树显示界面
 * @author czm(5243) tel：1234567890123
 * @version 1.0
 * @since 2010-1-28 上午11:00:42
 * 
 * <br>版权：南京联创科技 网管科技部
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>菜单树</title>
<lk:res/>
<link href="<s:url value="/itv/css/css_ico.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/itv/css/layout.css"/>" rel="stylesheet" type="text/css">
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
	font-size: 8pt;
}
</style>
<script type="text/javascript">
<!--
var baseDir="<s:url value="/"/>";
var clickId="-1";
function open(){
	$("img",this).attr("src",$(this).attr("layer")=="0"?"<s:url value='/itv/images/button_open.gif'/>":"<s:url value='/itv/images/button_open2.gif'/>");

	var condition ="li[@pid='"+$(this).attr("id")+"']";
	$(condition).each(function(){$(this)[0].style.display="";});
}

function close(){
	if($(this).attr("layer")=="0")
	{
		$("img",this).attr("src","<s:url value='/itv/images/button_close.gif'/>");
	}
	else if($(this).attr("layer")=="1")
	{
		$("img",this).attr("src","<s:url value='/itv/images/button_close2.gif'/>");
	}
	else if($(this).attr("layer")=="2")
	{
		$("img",this).attr("src","<s:url value='/itv/images/button_close2.gif'/>");
	}
	var condition ="li[@pid='"+$(this).attr("id")+"']";
	$(condition).each(function(){
		$(this)[0].style.display="none";
		if($(this).attr("isleaf")=='false')
		{
			if($(this).attr("layer")=="1")
			{
				$("img",this).attr("src","<s:url value='/itv/images/button_close2.gif'/>");
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

function goLocation(id)
{
	clickId=id;
	$("li").removeClass("left_title2on").removeClass("left_title3on");
	$("#"+id).addClass("left_title2on");
	parent.indexBottom.all.indexContent.src = baseDir + $("#"+id).attr("url");
	<%--
	$.ajax({
		type: "POST",
		url: '<s:url value="/gtms/itv/GetMenu!initBar.action"/>',
		data: {"menuId":id},
		success: function(data){
			alert(data);
			parent.parent.frames.indexTop.frm.all("bar").innerHTML = data;
		},
		error: function(xmlR,msg,other){}
	});--%>

}
//-->
</script>
</head>
<body>
<div class="left-box" >
<table width="100%"  border="0">
  <tr>
    <td><img src="<s:url value="/itv/images/bite.gif"/>" width="24" height="24"></td>
  </tr>
</table>
<ul>
	<s:iterator value="systemTree.nodelList">
		<li id="<s:property value="nodeId"/>" url="<s:property value="urlParam"/>" 
		    class="dian_line"
		    pid="<s:property value="nodeParentId"/>"
			layer="<s:property value="layer"/>"
			isleaf="<s:property value="isLeaf"/>"
			style="display:<s:property value="layer==0?'':'none'"/>">
			<img src="<s:url value="%{isLeaf=='true'?'/itv/images/button_dian.gif':(layer==0?'/itv/images/button_close.gif':'/images/button_close2.gif')}"/>" />
			<s:if test="isLeaf">
				<a href="javascript:goLocation('<s:property value="nodeId"/>')">
					<s:property value="nodeName" />
				</a>
			</s:if>
			<s:else>
				<s:property value="nodeName" />
			</s:else>
		</li>
	</s:iterator>
</ul>
<div>
</body>
</html>