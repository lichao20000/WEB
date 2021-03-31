//作者： 陈仲民5243  jQuery树型控件的扩展方法
jQuery.extend({
/**
*作者：王志猛
*树的解析函数
*@param targetid，插入树的div的id
*@param json,解析为树的json数组，个是必须为[{'name':'XXXX','id':'XXX','leaf':true or false,'attr':{json数组},'child':[{和外层相同的格式}]}]，
return 检查通过则为true，否则false
*/
linageAnalyTree : function(targetid,json)
{
	var tar = $("#"+targetid.replace(/\./g,"\\\."));
	if(typeof json !='object')
	{
		alert("传入的json不是正确的对象，格式不对");
		return ;
	}
	$.linkageTreeRoot(tar,json);
},
linkageTreeRoot : function(tgtDom,json)
{
	var ar = json[0];
	var name=ar['name'];
	var id=ar['id'];
	var leaf=ar['leaf'];
	var dom = $("<ul><li id='"+id+"' attr='"+ar['attr']+"'><img src=''/><a href='javascript://'>"+name+"</a></li></ul>");
	tgtDom.append(dom);
	if((!leaf)&&(ar['child']!=undefined))
	{
		$.linkageTreeChild($("#"+id.replace(/\./g,"\\\.")),ar['child']);		
	}
},
linkageTreeChild : function(tgtDom,json)
{
	//第一个增加的子节点，如果发现还没有增加<ul></ul>则添加一个
	if($("ul:first",tgtDom).length==0)
	{
	tgtDom.append("<ul></ul>");
	}
	var childRoot = $("ul:first",tgtDom);
	var len=json.length;
	for(var i=0;i<len;i++)
	{
		var js = json[i];
		var idc= js['id'];
		childRoot.append("<li id='"+idc+"' attr='"+js['attr']+"'><img src=''/><a href='javascript://'>"+js['name']+"</a></li>");
		if((!js['leaf'])&&(js['child']!=undefined))
		{
			$.linkageTreeChild($("#"+idc.replace(/\./g,"\\\.")),js['child']);
		}
	}
}
});