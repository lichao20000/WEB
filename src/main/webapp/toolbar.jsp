</HEAD>
<BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.tree.Tree"%>
<%@page import="com.linkage.litms.tree.Item"%>
<%
	//判断若是不满足要求则不进行以下步骤的执行
	if (temp_TreeItemId != null && temp_TreeItemId.indexOf("|") > 0) {
		String tmp_tree_id = temp_TreeItemId.substring(0,temp_TreeItemId.indexOf("|"));
		String tmp_item_id = temp_TreeItemId.substring(temp_TreeItemId.indexOf("|") + 1);

		Tree Tree = new Tree();
		Item Item = new Item();
		String system_path = Tree.getFullParentPathByTreeId(tmp_tree_id);
		system_path += "->" + (Item.getItemInfoByItemId(tmp_item_id)) .get("item_name");
		if(system_path.startsWith("->")){
		    system_path = system_path.substring(2);	    
		}
		out.println("<br>&nbsp;&nbsp;&nbsp;&nbsp;<b color=blue>"+system_path+"</b>");

		//clear
		system_path = null;
		Tree = null;
		Item = null;
		tmp_tree_id = null;
		tmp_item_id = null;
	}
%>
<script LANGUAGE="JavaScript">

var has_showModalDialog = !!window.showModalDialog;//定义一个全局变量判定是否有原生showModalDialog方法  
if(!has_showModalDialog &&!!(window.opener)){         
    window.onbeforeunload=function(){  
        window.opener.hasOpenWindow = false;        //弹窗关闭时告诉opener：它子窗口已经关闭  
    }  
}  
//定义window.showModalDialog如果它不存在  
if(window.showModalDialog == undefined){  
    window.showModalDialog = function(url,mixedVar,features){  
        if(window.hasOpenWindow){  
            window.myNewWindow.focus();  
        }  
        window.hasOpenWindow = true;  
        if(mixedVar) var mixedVar = mixedVar;  
        //因window.showmodaldialog 与 window.open 参数不一样，所以封装的时候用正则去格式化一下参数  
        if(features) var features = features.replace(/(dialog)|(px)/ig,"").replace(/;/g,',').replace(/\:/g,"=");  
        var left = (window.screen.width - parseInt(features.match(/width[\s]*=[\s]*([\d]+)/i)[1]))/2;  
        var top = (window.screen.height - parseInt(features.match(/height[\s]*=[\s]*([\d]+)/i)[1]))/2;  
        window.myNewWindow = window.open(url,"_blank",features);  
    }  
} 


</script>
