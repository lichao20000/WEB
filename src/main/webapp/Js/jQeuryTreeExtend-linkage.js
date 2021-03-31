//���ߣ� ������5243  jQuery���Ϳؼ�����չ����
jQuery.extend({
/**
*���ߣ���־��
*���Ľ�������
*@param targetid����������div��id
*@param json,����Ϊ����json���飬���Ǳ���Ϊ[{'name':'XXXX','id':'XXX','leaf':true or false,'attr':{json����},'child':[{�������ͬ�ĸ�ʽ}]}]��
return ���ͨ����Ϊtrue������false
*/
linageAnalyTree : function(targetid,json)
{
	var tar = $("#"+targetid.replace(/\./g,"\\\."));
	if(typeof json !='object')
	{
		alert("�����json������ȷ�Ķ��󣬸�ʽ����");
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
	//��һ�����ӵ��ӽڵ㣬������ֻ�û������<ul></ul>�����һ��
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