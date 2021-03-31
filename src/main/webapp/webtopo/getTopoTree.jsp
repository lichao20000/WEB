<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*,com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%
request.setCharacterEncoding("GBK");

String sql = "select * from tp_gw_segment where managed = 1";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select labelname, id, parentid from tp_gw_segment where managed = 1";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(sql);

String topoXml = "";
Map fields = cursor.getNext();

topoXml += "<Nodes>";

while(fields != null){
	topoXml += "<Device ";
	topoXml += "title='" + fields.get("labelname") + "' id='" + fields.get("id") + "' pid='" + fields.get("parentid") + "' ";
	topoXml += "/>";
	fields = cursor.getNext();
}

topoXml += "</Nodes>";

fields = null;
cursor = null;
%>

<html>
<head></head>

<body>
<div id="divTree1"></div>


<script language="JavaScript" src="../Js/tree_maker.js"></script>
<script language="JavaScript">

var tree=new Tree_treeView();

//设置tree_view属性
tree.showLine=true;//显示结点连线
tree.target="_blank";//目标框架
tree.folderClass1="FOLDER_1";//文件夹样式(正常状态)
tree.folderClass2="FOLDER_2";//文件夹样式(鼠标位于文件夹上时)
tree.folderClass3="FOLDER_3";//文件夹样式(选择状态)
tree.fileClass1="FILE_1";//文件样式(正常状态)
tree.fileClass2="FILE_2";//文件样式(鼠标位于文件上时)
tree.fileClass3="FILE_3";//文件样式(选择状态)

//生成CSS样式，注意，格式是TD.XXX{...},(XXX是样式名,如folderClass1,selectClass)
var css="<style>"+
	"TD.FOLDER_1{padding:1pt 5pt  }"+
	"TD.FOLDER_2{color:red;padding:1pt 5pt}"+
	"TD.FOLDER_3{text-decoration:underline;color:brown;padding:1pt 5pt}"+
	"TD.FILE_1{padding:1pt 5pt}"+
	"TD.FILE_2{color:blue;padding:1pt 5pt}"+
	"TD.FILE_3{text-decoration:underline;color:green;padding:1pt 5pt}"+
	"</style>";
document.write(css);//tree.refresh();

//添加根结点treeNode
var rootNode=tree.add(0,Tree_ROOT,0,"网络视图",'1/0');


tree.callback_click=function my_click(nodeID){

		var node_click=tree.getNode(nodeID);
		
		//NetViewDbClick(node_click.hint);
		//parent.getTopoByid(node_click.hint);
		
		document.all("showID").value = node_click.hint;
		
		if (node_click.childCount == 0){
			getTopoInfo(node_click,node_click.hint);
		}
}


function getTopoInfo(treeNode,treeID){
	//读取Topo XML数据
	var XMLDoc;
	XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	XMLDoc.async = false;
	XMLDoc.loadXML("<%=topoXml%>");
	
	var nodes = XMLDoc.selectNodes("//Nodes/Device");
	var ln = nodes.length;

	var node = null;
	var type = null;
	var child = null;
	for(var i=0;i<ln;i++){
		node = nodes.item(i);
		
		var parentid = node.getAttribute("pid");
		
		if (treeID == parentid){
			//alert(node.getAttribute("title"));
			child=treeNode.addChild(Tree_LAST,node.getAttribute("title"),node.getAttribute("id"),'');
		}
	}
	
	if (child != null){
		treeNode.expand(true);
	}
}

function showTopo(){
	parent.getTopoByid(document.all("showID").value);
}

</script>

<input type="button" name="show" value="显示topo图" onclick="showTopo()">
<input type="hidden" name="showID" value="1/0">
</body>
</html>
