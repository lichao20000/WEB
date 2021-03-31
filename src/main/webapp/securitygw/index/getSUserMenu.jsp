<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%--
/**
 * 该jsp是为了安全网关使用的
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2008-03-26
 * @category security
 * 
 * 
 */
 --%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/model_vip/js/jquery.treeview.pack.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript">
		$(document).ready(function(){
		$("body > ul:first").Treeview();
			});
			//定位安全设备
	function goSGW(id,cusName)
	{
	var  url= "<s:url value="/model_vip/Goto!goSubMenu.action"/>?device_id="+id+"&custom_name="+cusName;
	parent.bottom.all.rightbottom.src=url;
	}
	function goArea(area_id)
	{
		var url = "<s:url value="/model_vip/Goto.action"/>?area_id="+area_id;
		parent.bottom.all.rightbottom.src=url;
	}
	</script>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
@IMPORT url("<s:url value="/model_vip/css/css_ico.css"/>");
		.treeview, .treeview ul { 
			padding: 0;
			margin: 0;
			list-style: none;
		}	

		.treeview li { 
			margin: 0;
			padding: 3px 0pt 3px 16px;
		}
		
		ul.dir li { padding: 2px 0 0 16px; }
		
	  	.treeview li { background: url(<s:url value="/model_vip/images/tv-item.gif"/>) 0 0 no-repeat; }
	  	.treeview .collapsable { background-image: url(<s:url value="/model_vip/images/tv-collapsable.gif"/>); }
	  	.treeview .expandable { background-image: url(<s:url value="/model_vip/images/tv-expandable.gif"/>); }
	  	.treeview .last { background-image: url(<s:url value="/model_vip/images/tv-item-last.gif"/>); }
	  	.treeview .lastCollapsable { background-image: url(<s:url value="/model_vip/images/tv-collapsable-last.gif"/>); }
	  	.treeview .lastExpandable { background-image: url(<s:url value="/model_vip/images/tv-expandable-last.gif"/>); }
	body
{
	background-image: url("<s:url value="/model_vip/images/left_back.jpg"/>");
	margin-top:5px;
	padding-top:5px;
	font-size: 9pt;
	font-weight: bold;
}
#root
{
font-size: 9pt;
margin-left:5px;
font-weight: bold;
}
	</style>

</head>
<body>
<span id="root">安全网关树型视图</span>
<ul>
	<li><img src="<s:url value="/model_vip/images/tree_ico1.gif"/>" /><a href="javascript:goArea('<s:property value="sgwItem.area_id"/>');"/><s:property
		value="sgwItem.area_name" /></a>
	<ul>
		<s:iterator value="sgwItem.SGWList">
			<li><img src="<s:url value="/model_vip/images/tree_ico2.gif"/>" /><a
				href="javascript:goSGW('<s:property value="device_id"/>','<s:property value="customname"/>')"><s:property
				value="customname" /></a></li>
		</s:iterator>
		<s:iterator value="sgwItem.ChildArea">
			<li class="closed"><img
				src="<s:url value="/model_vip/images/tree_ico1.gif"/>" /><a href="javascript:goArea('<s:property value="area_id"/>');"><s:property
				value="area_name" /></a>
			<ul>
				<s:iterator value="SGWList">
					<li><img
						src="<s:url value="/model_vip/images/tree_ico2.gif"/>" /><a
						href="javascript:goSGW('<s:property value="device_id"/>','<s:property value="customname"/>')"><s:property
						value="customname" /></a></li>
				</s:iterator>
				<s:iterator value="ChildArea">
					<li><img
						src="<s:url value="/model_vip/images/tree_ico1.gif"/>" /><a href="javascript:goArea('<s:property value="area_id"/>');"><s:property
						value="area_name" /></a>
					<ul>
						<s:iterator value="SGWList">
							<li><img
								src="<s:url value="/model_vip/images/tree_ico2.gif"/>" /><a
								href="javascript:goSGW('<s:property value="device_id"/>','<s:property value="customname"/>')"><s:property
								value="customname" /></a></li>
						</s:iterator>
					</ul>
					</li>
				</s:iterator>
			</ul>
			</li>
		</s:iterator>
	</ul>
	</li>
</ul>
</body>
</html>
