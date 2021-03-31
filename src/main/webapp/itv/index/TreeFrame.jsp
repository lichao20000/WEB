<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
/**
 * 菜单树显示界面
 * @author czm(5243) tel：1234567890123
 * @version 1.0
 * @since 2009-12-23 上午10:50:06
 * 
 * <br>版权：南京联创科技 网管科技部
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>result</title>
<lk:res addRes="/mztreeview2/scripts/jsframework.js"/>
<style type="text/css">

</style>
<script type="text/javascript">
<!--
var path = "<s:url value="/"/>";
Using("System.Web.UI.WebControls.MzTreeView");
var a = new MzTreeView();
var data;

$(function(){
	data={};
	data["-1_<s:property value="systemTree.moduleId"/>"] = "text:<s:property value="systemTree.moduleName"/>";
	
	$("node").each(function(){
		if (this.isLeaf == "true"){
			data[this.treeId] = "text:" + this.nodeName + ";url:" + path + this.urlParam + ";target:indexContent";
		}
		else{
			data[this.treeId] = "text:"+ this.nodeName;
		}
	});
	
	a.dataSource = data
	a.loadXmlDataString("", 1);
	a.setJsDataPath("<s:url value="/mztreeview2/scripts/csdn/community/treedata/"/>");
	a.setXmlDataPath("<s:url value="/mztreeview2/scripts/csdn/community/treedata/"/>");
	a.autoSort=false;
	a.useCheckbox=false;
	a.canOperate=true;
	$("#tree").html(a.render());
	a.expandLevel(1);
	
});

//判断节点是否展开
MzTreeView.prototype.isExpand = function(id)
{
	var source = this.getNodeById(id);
	
	if (source){
		if (source.expanded){
			return true;
		}
		else{
			return false;
		}
	}
	else{
		return false;
	}
}
//-->
</script>
</head>
<body>
<tree name="treeData">
	<s:iterator value="systemTree.nodelList">
	<node nodeId="<s:property value="nodeId"/>" nodeName="<s:property value="nodeName"/>" urlParam="<s:property value="urlParam"/>" isLeaf="<s:property value="isLeaf"/>" treeId="<s:property value="treeId"/>"></node>
	</s:iterator>
</tree>
<div id="tree" style="width: 100%;height: 100%"></div>
</body>
</html>