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

//����tree_view����
tree.showLine=true;//��ʾ�������
tree.target="_blank";//Ŀ����
tree.folderClass1="FOLDER_1";//�ļ�����ʽ(����״̬)
tree.folderClass2="FOLDER_2";//�ļ�����ʽ(���λ���ļ�����ʱ)
tree.folderClass3="FOLDER_3";//�ļ�����ʽ(ѡ��״̬)
tree.fileClass1="FILE_1";//�ļ���ʽ(����״̬)
tree.fileClass2="FILE_2";//�ļ���ʽ(���λ���ļ���ʱ)
tree.fileClass3="FILE_3";//�ļ���ʽ(ѡ��״̬)

//����CSS��ʽ��ע�⣬��ʽ��TD.XXX{...},(XXX����ʽ��,��folderClass1,selectClass)
var css="<style>"+
	"TD.FOLDER_1{padding:1pt 5pt  }"+
	"TD.FOLDER_2{color:red;padding:1pt 5pt}"+
	"TD.FOLDER_3{text-decoration:underline;color:brown;padding:1pt 5pt}"+
	"TD.FILE_1{padding:1pt 5pt}"+
	"TD.FILE_2{color:blue;padding:1pt 5pt}"+
	"TD.FILE_3{text-decoration:underline;color:green;padding:1pt 5pt}"+
	"</style>";
document.write(css);//tree.refresh();

//��Ӹ����treeNode
var rootNode=tree.add(0,Tree_ROOT,0,"������ͼ",'1/0');


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
	//��ȡTopo XML����
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

<input type="button" name="show" value="��ʾtopoͼ" onclick="showTopo()">
<input type="hidden" name="showID" value="1/0">
</body>
</html>
